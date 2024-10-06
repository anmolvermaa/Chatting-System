
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.awt.TextArea;
import java.awt.Font;
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.FileDialog;
import java.awt.event.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;

public class InfiniteChatRoomUI implements ActionListener,KeyListener{
	
	JFrame iFrame;
	JPanel iPaneLbl;
	ImagePanel iChatLogo;
	JLabel iLabel;
	Image iChat_icon,iChat_logo;
	JTextField userInput;
	TextArea convOutput;
	JButton send,save;
	FileDialog saveTo;
	
	String iName,iReceiver,currentMsg;
	StringBuffer conversation;
	
	InfiniteChatClient iClient;
	/**
	 * Method InfiniteChatConversationRoom
	 *
	 *
	 */
	public InfiniteChatRoomUI(InfiniteChatClient iClientOb,String forUser){
		iClient=iClientOb;
		iName=iClient.userName;
		iReceiver=forUser;
		
		conversation=new StringBuffer("");
		
		iFrame=new JFrame("Conversation Room");
		iFrame.setLayout(null);
		iFrame.setBounds(180,80,600,500);
		iFrame.setResizable(false);
		
		iChat_icon=Toolkit.getDefaultToolkit().getImage("../images/iChat_icon_small.gif");
		iChat_logo=Toolkit.getDefaultToolkit().getImage("../images/iChat_logo_small.gif");
		iFrame.setIconImage(iChat_icon);
		
		iPaneLbl=new JPanel(null);
		iPaneLbl.setBounds(0,0,600,70);
		iPaneLbl.setBackground(Color.black);
		
		iChatLogo=new ImagePanel(iChat_logo);
		iChatLogo.setBounds(0,0,150,70);
		
		iLabel=new JLabel("Conversation with "+iReceiver+" :");
		iLabel.setBounds(200,15,200,50);
		iLabel.setForeground(Color.white);
		iLabel.setFont(new Font("Helvitica",Font.BOLD,15));
		
		save=new JButton("Save");
		save.setBounds(500,15,80,40);
		save.addActionListener(this);
		
		iPaneLbl.add(iChatLogo);
		iPaneLbl.add(iLabel);
		iPaneLbl.add(save);
		iFrame.add(iPaneLbl);
		
		convOutput=new TextArea("",0,0,1);
		convOutput.setBackground(Color.WHITE);
		convOutput.setBounds(10,80,580,300);
		convOutput.setEditable(false);
		iFrame.add(convOutput);
		
		userInput=new JTextField("");
		userInput.setBounds(10,400,480,40);
		userInput.addKeyListener(this);
		iFrame.add(userInput);
		
		send=new JButton("Send");
		send.setBounds(500,400,80,40);
		send.addActionListener(this);
		send.addKeyListener(this);
		iFrame.add(send);
		
		iFrame.addWindowListener(
			new WindowAdapter(){
				public void windowClosing(WindowEvent e){
					iClient.removeRoom(iReceiver);
					iFrame.dispose();
				}
			}
		);
		
		iFrame.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==send){
			send_message();
		}
		if(e.getSource()==save){
			save_file();
		}
	}
	
	public void keyPressed(KeyEvent e){
		if(e.getKeyChar()=='\n'){
			send_message();
		}
	}	
	public void keyReleased(KeyEvent e){}
	public void keyTyped(KeyEvent e){}
	
	public void send_message(){
		currentMsg=userInput.getText();
		iClient.send_Message("MESSAGETO "+iReceiver+" "+iName+" : "+currentMsg);
		userInput.setText("");
		conversation.append("\n"+iName+" : "+currentMsg);
		currentMsg="\n"+iName+" : "+currentMsg;
		convOutput.append(currentMsg);
	}
	
	public void save_file(){
		saveTo=new FileDialog(iFrame,"Save as...",FileDialog.SAVE);
		saveTo.setVisible(true);
		String dirname=saveTo.getDirectory();
		String fname=saveTo.getFile();
		FileOutputStream fout=null;
		if(fname.equals(null)){
			return;
		}
		else{
			fname=dirname+fname;
			try{
				fout=new FileOutputStream(fname);	
			}catch(FileNotFoundException e){
				System.out.println(e);
				return;
			}
			try{
				fout.write(conversation.toString().getBytes());
			}catch(IOException e){
				System.out.println(e);
				return;
			}
		}
	}
}
