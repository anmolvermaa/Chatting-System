
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.*;
import java.awt.Image;
import java.awt.Toolkit;

public class DialogBox implements ActionListener,KeyListener{
	JFrame parentFrame,iFrame;
	JLabel msg;
	JButton ok;
	Image iChat_icon;
	/**
	 * Method DialogBox
	 *
	 *
	 */
	public DialogBox(JFrame parent,String title,String message){
		parentFrame=parent;
		parentFrame.setEnabled(false);
		iFrame=new JFrame(title);
		iFrame.setLayout(null);
		iFrame.setAlwaysOnTop(true);
		iFrame.setBounds(800,185,220,150);
		iFrame.setResizable(false);
		
		iChat_icon=Toolkit.getDefaultToolkit().getImage("../images/iChat_icon_small.gif");
		iFrame.setIconImage(iChat_icon);
		
		msg=new JLabel(message);
		msg.setBounds(20,20,200,25);
		iFrame.add(msg);
		
		ok=new JButton("OK");
		ok.setBounds(80,60,60,40);
		ok.addActionListener(this);
		ok.addKeyListener(this);
		iFrame.add(ok);
		
		iFrame.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==ok){
			parentFrame.setEnabled(true);
			iFrame.dispose();
		}
	}
	
	public void keyPressed(KeyEvent e){
		if(e.getKeyChar()=='\n'){
			parentFrame.setEnabled(true);
			iFrame.dispose();
		}
	}
	public void keyReleased(KeyEvent e){}
    public void keyTyped(KeyEvent e){}
}
