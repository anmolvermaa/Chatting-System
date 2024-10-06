

import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.io.IOException;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Image;
import java.awt.event.*;
 
public class InfiniteChatServer implements Runnable,ActionListener,KeyListener{
	
	JFrame iFrame;
	Image iChat_icon;
	ImageIcon infinite_loop_logo;
	JPanel iPaneLbl;
	JTextField iAcquirePortNo;
	JButton srtServer,stpServer,extServer;
	JLabel iName,iEntrPortNo;
	
	boolean isServerRunning=false;
	ServerSocket iChatServer;
	int iPortNo;
	
	Thread iMainThread;
	ArrayList iClientList;
	
	public InfiniteChatServer(){
		iFrame=new JFrame("iChat Server v1.0");
		iFrame.setLayout(null);
		iFrame.setBounds(40,40,500,270);
		iFrame.setResizable(false);
		iFrame.addKeyListener(this);
		
		iChat_icon=Toolkit.getDefaultToolkit().getImage("../images/iChat_icon_small.gif");
		iFrame.setIconImage(iChat_icon);
		
		
		iPaneLbl=new JPanel(null);
		iPaneLbl.setBounds(0,0,500,60);
		iPaneLbl.setBackground(Color.black);
		iName=new JLabel("iChat Server v1.0");
		iName.setBounds(175,5,200,50);
		iName.setForeground(Color.white);
		iName.setFont(new Font("Helvitica",Font.BOLD,20));
		iPaneLbl.add(iName);
		iFrame.add(iPaneLbl);
		
		iEntrPortNo=new JLabel("Enter Port No. :");
		iEntrPortNo.setFont(new Font("Helvitica",Font.BOLD,15));
		iEntrPortNo.setBounds(50,90,300,30);
		iFrame.add(iEntrPortNo);
		
		iAcquirePortNo=new JTextField(5);
		iAcquirePortNo.setText("1664");
		iAcquirePortNo.setBounds(170,90,35,25);
		iAcquirePortNo.addKeyListener(this);
		iFrame.add(iAcquirePortNo);
		
		srtServer=new JButton("Start iChat");
		srtServer.setBounds(50,140,100,50);
		srtServer.addActionListener(this);
		srtServer.addKeyListener(this);
		iFrame.add(srtServer);
		
		stpServer=new JButton("Stop iChat");
		stpServer.setBounds(200,140,100,50);
		stpServer.setEnabled(false);
		stpServer.addActionListener(this);
		stpServer.addKeyListener(this);
		iFrame.add(stpServer);
		
		extServer=new JButton("Exit iChat");
		extServer.setBounds(350,140,100,50);
		extServer.addActionListener(this);
		iFrame.add(extServer);
		
		iFrame.addWindowListener(
			new WindowAdapter(){
				public void windowClosing(WindowEvent e){
					exit_iChatServer();
				}
			}
		);
		iFrame.setVisible(true);
	}
	
	//Listening to ActionEvents...
	public void actionPerformed(ActionEvent iEvent){
		if(iEvent.getSource()==srtServer){
			start_iChatServer();
		}
		if(iEvent.getSource()==stpServer){
			stop_iChatServer();
		}	
		if(iEvent.getSource()==extServer){
			exit_iChatServer();
		}
	}
	
	//Listeneing to KeyEvents...
	public void keyPressed(KeyEvent e){
		if(e.getKeyChar()=='\n' && !isServerRunning){
			start_iChatServer();
		}
		else if(e.getKeyChar()=='\n' && isServerRunning){
			stop_iChatServer();
		}
	}
	public void keyTyped(KeyEvent e){}
	public void keyReleased(KeyEvent e){}
	
	
	//Setting up the server...
	protected void start_iChatServer(){
		try{
			iPortNo=Integer.parseInt(iAcquirePortNo.getText());
		}catch(NumberFormatException e){
			System.out.println("\n"+e);
			System.out.println("Starting at default port : 1664...");
			iPortNo=1664;
		}
		
		try{
			iChatServer=new ServerSocket(iPortNo);
			isServerRunning=true;
			System.out.println("\nServer set up and running at port "+iPortNo+"...\nWelcome to iChat Server v1.0...");
			iAcquirePortNo.setEnabled(false);
			srtServer.setEnabled(false);
			stpServer.setEnabled(true);
			iClientList=new ArrayList();
			iMainThread=new Thread(this,"iChat Server v1.0");
			iMainThread.start();
		}catch(IOException e){
			System.out.println("\n"+e);
			System.out.println("Error occured while setting up the server...\nTry another port...");
		}
	}
	
	public void run(){
		while(iMainThread!=null){
			try{
				Socket iNewClient=iChatServer.accept();
				InfiniteChatClientHandler iNewClientHandle=new InfiniteChatClientHandler(this,iNewClient);
				Thread.sleep(1000);
			}catch(InterruptedException e){
			}catch(IOException e){
			}
		}
	}
	
	//Cleaning up and quiting server...
	protected void stop_iChatServer(){
		if(iChatServer!=null){
			try{	
				iChatServer.close();
				iChatServer=null;
				isServerRunning=false;
				iClientList=null;
				System.out.println("\nClosing server socket at port "+iPortNo+"...");
				iAcquirePortNo.setEnabled(true);
				srtServer.setEnabled(true);
				stpServer.setEnabled(false);
				iMainThread=null;
			}catch(IOException e){
				System.out.println("\n"+e);
			}
		}
	}
	
	//Exit from server...
	protected void exit_iChatServer(){
		System.out.println("\nCleaning up and quiting...");
		stop_iChatServer();
		iFrame.dispose();
		System.out.println("\nThank you for using iChat Server v1.0...\nHave a nice day...");
		System.exit(0);
	}
	
	//Returns reference of type <InfiniteChatClientObject> with specified clientname...
	protected InfiniteChatClientObject getClientObject(String clientName){
		InfiniteChatClientObject returnClientObject=null;
		InfiniteChatClientObject tempClientObject;
		int listSize=iClientList.size();
		for(int iCount=0;iCount<listSize;iCount++){
			tempClientObject=(InfiniteChatClientObject)iClientList.get(iCount);
			if(clientName.equalsIgnoreCase(tempClientObject.getClientName())){
				returnClientObject=tempClientObject;
				break;
			}
		}
		return(returnClientObject);
	}
	
	protected Socket getClientSocket(String clientName){
		InfiniteChatClientObject client=getClientObject(clientName);
		return(client.getClientSocket());
	}
	
	//Returns true if client with specified username exists, false otherwise...
	protected boolean clientExists(String clientName){
		if(getClientObject(clientName)!=null){
			return true;
		}else{
			return false;
		}
	}
	
	//To add a client...
	public void addClient(Socket newClient,String clientName){
		if(clientExists(clientName)){
			unicast(newClient,"USEREXISTS");
			return;
		}
		else unicast(newClient,"WELCOME");
		System.out.println("\n"+clientName+" logged in successfully...");
		InfiniteChatClientObject newClientObject=new InfiniteChatClientObject(newClient,clientName);
		InfiniteChatClientObject tempClientObject;
		iClientList.add(newClientObject);
		
		StringBuffer clientList=new StringBuffer("USERLIST");
		String newclient="NEWUSER "+clientName;
		
		int listSize=iClientList.size();
		for(int iCount=0;iCount<listSize;iCount++){
			tempClientObject=(InfiniteChatClientObject)iClientList.get(iCount);
			if(!clientName.equals(tempClientObject.getClientName())){
				unicast(tempClientObject.getClientSocket(),newclient);
				clientList.append(" "+tempClientObject.getClientName());
			}	
		}
		unicast(newClient,clientList.toString());
	}
	
	//To remove a client...
	public void removeClient(String clientName){
		InfiniteChatClientObject logged_out_Client=getClientObject(clientName);
		Socket rmvSocket=logged_out_Client.getClientSocket();
		iClientList.remove(logged_out_Client);
		iClientList.trimToSize();
		try{
			rmvSocket.close();
			System.out.println("\n"+clientName+" logged out successfully...");
		}catch(IOException e){
			System.out.println(e);
			System.out.println("\nError occured while closing connection...");
		}
		rmvSocket=null;
		broadcast("LOGGEDOUT "+clientName);
	}
	
	//Sends messagestring <message> to single client-socket <clientSocket>...
	protected void unicast(Socket clientSocket,String message){
		try {
				new DataOutputStream(clientSocket.getOutputStream()).write(new String(message+"\r\n").getBytes());			
		}catch(IOException e){}
	}
	
	//Broadcasts message-string <message>...
	private void broadcast(String message){
		InfiniteChatClientObject tempClientObject;
		int listSize=iClientList.size();
		for(int iCount=0;iCount<listSize;iCount++){
			tempClientObject=(InfiniteChatClientObject)iClientList.get(iCount);
			unicast(tempClientObject.getClientSocket(),message);
		}
	}	
    
    public static void main(String[] args) {
    	InfiniteChatServer iChatServer=new InfiniteChatServer();
    }
}