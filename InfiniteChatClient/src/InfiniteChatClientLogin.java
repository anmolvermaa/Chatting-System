
import java.net.Socket;
import java.net.URL;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

public class InfiniteChatClientLogin implements ActionListener,KeyListener{
	Socket iClient;
	JFrame iFrame;
	Image iChat_icon;
	JPanel iPaneLbl;
	JLabel iName,iUserName,iServer,iPort;
	JTextField iGetUserName,iGetServer,iGetPortNo;
	JButton srtClient,extClient;
	
	String userName,serverName,server_msg;
	int portNo=1664;
	BufferedReader msgFromServer;
	DataOutputStream msgToServer;
	
	InfiniteChatClientHandle iHandle;
	/**
	 * Method InfiniteChatClientLogin
	 *
	 *
	 */
	public InfiniteChatClientLogin() {
		iFrame=new JFrame("iChat Client v1.0");
		iFrame.setLayout(null);
		iFrame.setBounds(800,80,220,600);
		iFrame.setResizable(false);
		
		iChat_icon=Toolkit.getDefaultToolkit().getImage("../images/iChat_icon_small.gif");
		iFrame.setIconImage(iChat_icon);
		
		iPaneLbl=new JPanel(null);
		iPaneLbl.setBounds(0,0,220,60);
		iPaneLbl.setBackground(Color.black);
		iName=new JLabel("iChat Client v1.0");
		iName.setBounds(30,5,200,50);
		iName.setForeground(Color.white);
		iName.setFont(new Font("Helvitica",Font.BOLD,20));
		iPaneLbl.add(iName);
		iFrame.add(iPaneLbl);
		
		iUserName=new JLabel("Username :");
		iUserName.setBounds(30,100,100,25);
		iFrame.add(iUserName);
		
		iServer=new JLabel("Server :");
		iServer.setBounds(30,150,100,25);
		iFrame.add(iServer);
		
		iPort=new JLabel("Port No. :");
		iPort.setBounds(30,200,100,25);
		iFrame.add(iPort);
		
		iGetUserName=new JTextField(15);
		iGetUserName.setBounds(100,100,90,25);
		iGetUserName.addKeyListener(this);
		iFrame.add(iGetUserName);
		
		iGetServer=new JTextField();
		iGetServer.setBounds(100,150,90,25);
		iGetServer.addKeyListener(this);
		iFrame.add(iGetServer);
		
		iGetPortNo=new JTextField(5);
		iGetPortNo.setBounds(100,200,50,25);
		iGetPortNo.setText("1664");
		iGetPortNo.addKeyListener(this);
		iFrame.add(iGetPortNo);
		
		srtClient=new JButton("Log In");
		srtClient.setBounds(60,450,100,30);
		srtClient.addActionListener(this);
		srtClient.addKeyListener(this);
		iFrame.add(srtClient);
		
		extClient=new JButton("Exit iChat");
		extClient.setBounds(60,490,100,30);
		extClient.addActionListener(this);
		iFrame.add(extClient);
		
		iFrame.addWindowListener(
			new WindowAdapter(){
				public void windowClosing(WindowEvent e){
					exit_iChatClient();
				}
			}
		);
		iFrame.setVisible(true);
	}
	
	//Listenting Action Events...
	public void actionPerformed(ActionEvent e){
    	if(e.getSource()==srtClient){
    		log_in();
    	}
    	if(e.getSource()==extClient){
    		exit_iChatClient();
    	}
    }
    
    //Listening to KeyEvents...
    public void keyPressed(KeyEvent e){
    	if(e.getKeyChar()=='\n'){
    		log_in();
    	}
    }
    public void keyReleased(KeyEvent e){}
    public void keyTyped(KeyEvent e){}
	
	//Log in to iChat Client v1.0...
	private void log_in(){
    	userName=iGetUserName.getText();
    	userName.trim();
    	if(userName.isEmpty()){
    		System.out.println("\nUsername field is not optional...\nPlease give a username...");
    		new DialogBox(iFrame,"Username Please...","Username is not optional...");
    		return;
    	}
    	else if(userName.contains(" ")){
    		System.out.println("\nUsername shouldn't have spaces...\nTry something else...");
    		new DialogBox(iFrame,"Spaces not allowed !!!","Username can't contain spaces...");
    		return;
    	}
    	serverName=iGetServer.getText();
    	if(serverName.equals("")){
    		System.out.println("\nServer address field is not optional...\nPlease give a server address...");
    		new DialogBox(iFrame,"Server Please...","Server field is not optional...");
    		return;
    	}
    	try{
    		portNo=Integer.parseInt(iGetPortNo.getText());
    	}catch(NumberFormatException e){
    		System.out.println(e);
    	}
    	try{
    		System.out.println("\nEstablishing connection...");
    		iClient=new Socket(serverName,portNo);
    		msgFromServer=new BufferedReader(new InputStreamReader(new DataInputStream(iClient.getInputStream())));
    		msgToServer=new DataOutputStream(iClient.getOutputStream());	
    	}catch(IOException e){
    		new DialogBox(iFrame,"Can not find server...","Please correct server/port no.");
    		System.out.println("\nCouldn't connect to server...\nCheck if you've given correct server name & port no.");
    	}
    	send_Message("NEWCLIENT "+userName);
		try{
			server_msg=msgFromServer.readLine();
		}catch(IOException e){
			System.out.println(e);
		}
		if(server_msg.equals("USEREXISTS")){
			System.out.println("\nThe username is not available...\nTry some other name...");
			new DialogBox(iFrame,"Username already in use...","User with this name exists...");
			return;
		}
		if(server_msg.equals("WELCOME")){
			iHandle=new InfiniteChatClientHandle();
			iHandle.setClientSocket(iClient);
			iHandle.setInputStream(msgFromServer);
			iHandle.setOutputStream(msgToServer);
			iHandle.setPortNo(portNo);
			iHandle.setServerName(serverName);
			iHandle.setUserName(userName);
			System.out.println("\nLogged in successfully...\nChat infinite with iChat...");
			InfiniteChatClient iChat=new InfiniteChatClient(iHandle);
			iFrame.dispose();
		}
    }
    
    //Exit from iChat Client v1.0...
    public void exit_iChatClient(){
    	iFrame.dispose();
    	System.out.println("\nThank you for using iChat Client v1.0...\nHave a nice day...");
    	System.exit(0);
    }
    
    //Send message to server...
    private void send_Message(String msg){
    	try{
    		msgToServer.writeBytes(msg+"\r\n");
    	}catch(IOException e){
    		System.out.println(e);   	
    	}
    }
    
	/**
	 * Method main
	 *
	 *
	 * @param args
	 *
	 */	
	public static void main(String[] args) {
		InfiniteChatClientLogin iChat=new InfiniteChatClientLogin();
	}
}
