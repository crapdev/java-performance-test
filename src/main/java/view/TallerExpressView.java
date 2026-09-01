
package view;

import javax.swing.JOptionPane;


public class TallerExpressView {
    
    public String input(String message){return JOptionPane.showInputDialog(null,message,"TallerExpress",JOptionPane.QUESTION_MESSAGE);}
    
    public void message(String message){JOptionPane.showMessageDialog(null,message,"TallerExpress",JOptionPane.INFORMATION_MESSAGE);}
    
    public void error(String message){JOptionPane.showMessageDialog(null,message,"TallerExpress - Error",JOptionPane.ERROR_MESSAGE);}
    
    public boolean confirm(String message){return JOptionPane.showConfirmDialog(null,message,"TallerExpress",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION;}
    
    public int option(String title,String message,String[] options){return JOptionPane.showOptionDialog(null,message,title,JOptionPane.DEFAULT_OPTION,JOptionPane.PLAIN_MESSAGE,null,options,options[0]);}
}
