
import java.net.URL;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;

public class InfiniteChatClientUI implements ActionListener,MouseListener{
	InfiniteChatClient iClient;
	
	JFrame iFrame;
	Image iChat_icon;
	JPanel iPaneLbl;
	JLabel iName,iOnline;
	JList iOnlineUsers;
	JButton stpClient,extClient;
	
	String userList;
	String onlineUsers[];
	
	/**
	 * Method InfiniteChatOnlineUsers
	 *
	 *
	 */
	public InfiniteChatClientUI(InfiniteChatClient client,String user_list){
		iClient=client;
		userList=user_list;
		onlineUsers=user_list.split(" ");
		
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
		
		iOnline=new JLabel("Online :");
		iOnline.setBounds(60,100,100,25);
		iFrame.add(iOnline);
		
		iOnlineUsers=new JList(onlineUsers);
		iOnlineUsers.setCellRenderer(new InfiniteChatClientListRenderer());
		iOnlineUsers.setBounds(60,125,100,300);
		iOnlineUsers.addMouseListener(this);
		iFrame.add(iOnlineUsers);
		
		stpClient=new JButton("Log Out");
		stpClient.setBounds(60,450,100,30);
		stpClient.addActionListener(this);
		iFrame.add(stpClient);
		
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
		
	public void actionPerformed(ActionEvent e){
    	if(e.getSource()==stpClient){
    		iClient.log_out();
    		iFrame.dispose();
    		InfiniteChatClientLogin iChatLogin=new InfiniteChatClientLogin();
    	}
    	if(e.getSource()==extClient){
    		exit_iChatClient();
    	}
    }
    
    public void mouseClicked(MouseEvent e){
    	if(e.getClickCount()==2){
    		int index=iOnlineUsers.locationToIndex(e.getPoint());
    		iClient.room_message(onlineUsers[index],"");
    	}
    }
    public void mouseExited(MouseEvent e){}
    public void mouseEntered(MouseEvent e){}
    public void mouseReleased(MouseEvent e){}
    public void mousePressed(MouseEvent e){}
    
    public void exit_iChatClient(){
    	iClient.log_out();
    	iFrame.dispose();
    }
    
    public void addUser(InfiniteChatClient client,String new_user){
    	userList=userList+" "+new_user;
    	iFrame.dispose();
    	client.iClientUI=new InfiniteChatClientUI(iClient,userList);
    }
    
    public void removeUser(InfiniteChatClient client,String remove_user){
    	if(userList.equals(remove_user)){
    		userList="";
    	}
    	else if(userList.startsWith(remove_user)){
    		userList=userList.substring(remove_user.length());
    	}
    	else if(userList.endsWith(remove_user)){
    		userList=userList.substring(0,userList.indexOf(remove_user));
    	}
    	else{
    		userList.replace(remove_user,"");
    	}
    	iFrame.dispose();
    	client.iClientUI=new InfiniteChatClientUI(iClient,userList);
    }
}
