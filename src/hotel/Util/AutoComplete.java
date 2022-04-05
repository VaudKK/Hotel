/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotel.Util;

import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

/**
 *
 * @author Vaud Keith
 */
public class AutoComplete implements DocumentListener{
    
    private static enum Mode{
        INSERT,COMPLETE;
    }
    
    private JTextField textField;
    private final List<String> source;
    private Mode mode = Mode.INSERT;
    
    public AutoComplete(JTextField textField,List<String> source){
        this.textField = textField;
        this.source = source;
        Collections.sort(source);
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
       
        if(e.getLength() != 1)
            return;

        int position = e.getOffset();
        String content = null;
        
        try {    
            content = textField.getText(0, position + 1);
            System.out.println(content);
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
          
        //Find Where the word starts
         
         int w;
         for(w = position; w >= 0; w--){
             if(!Character.isLetter(content.charAt(w))){
                 break;
             }
         }
         
         //Too few characters
         
         if(position - w < 2){
             return;}
         
         String prefix = content.substring(w+1).toLowerCase();
         
         int n = Collections.binarySearch(source, prefix);
         
         
         if(n < 0 && -n <= source.size()){
             String match = source.get(-n - 1);
             
             if(match.startsWith(prefix)){
                 //A completion is found
                 String completion = match.substring(position - w);
                 //We cannot modify document from within notification
                 //So we submit a task that does the change later
                
                 SwingUtilities.invokeLater(new CompletionTask(completion, position + 1));
                
             }
         }else{
             //NOTHING FOUND
             mode = Mode.INSERT;
         }
         
         
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
    }
    
    public class CommitAction extends AbstractAction{
        
        //private static final long serialVersionUID = 5794543109646743416L;

        @Override
        public void actionPerformed(ActionEvent e) {
            if(mode == Mode.COMPLETE){
                int pos = textField.getSelectionEnd();
                StringBuffer sb = new StringBuffer(textField.getText());
                sb.insert(pos, " ");
                textField.setText(sb.toString());
                textField.setCaretPosition(pos +  1);
                mode = Mode.INSERT;
            }else{
                textField.replaceSelection("\t");
            }
        }
        
    }
    
    private class CompletionTask implements Runnable{
        
        private String completion;
        private int position;
        
        CompletionTask(String completion,int position){
            this.completion = completion;
            this.position = position;
        }

        @Override
        public void run() {
            StringBuffer sb = new StringBuffer(textField.getText());
            sb.insert(position, completion);
            textField.setText(sb.toString());
            textField.setCaretPosition(position  + completion.length());
            textField.moveCaretPosition(position);
            mode = Mode.COMPLETE;
        }
        
    }
    
}
