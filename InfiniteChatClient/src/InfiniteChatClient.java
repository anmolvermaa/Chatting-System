/**
 * @(#)InfiniteChatClient.java
 *
 * InfiniteChatClient application
 *
 * @author 
 * @version 1.00 2008/10/6
 */
import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class InfiniteChatClient implements Runnable{
	
	Socket iClient;
	
	String userName,serverName,server_msg;
	int portNo=1664;
	BufferedReader msgFromServer;
	DataOutputStream msgToServer;
	
	Thread iThread;
	
	InfiniteChatClientUI iClientUI;
	ArrayList iConversationRoomList,iConferenceRoomList;
	
	public InfiniteChatClient(InfiniteChatClientHandle iHandle){
		
		iClient=iHandle.getClientSocket();
		msgFromServer=iHandle.getInputStream();
		msgToServer=iHandle.getOutputStream();
		portNo=iHandle.getPortNo();
		serverName=iHandle.getServerName();
		userName=iHandle.getUserName();
		iThread=new Thread(this,"iChat Client v1.0");
		iConversationRoomList=new ArrayList();
		iThread.start();
	}
    
    public void run(){
    	while(iThread!=null){
    		try{
    			server_msg=msgFromServer.readLine();
    		}catch(IOException e){
 				System.out.println(e);
    		}

    		if(server_msg.startsWith("USERLIST")){
    			server_msg=server_msg.substring(8);
    			iClientUI=new InfiniteChatClientUI(this,server_msg);
    		}
    		if(server_msg.startsWith("NEWUSER")){
    			server_msg=server_msg.substring(8);
    			iClientUI.addUser(this,server_msg);
    		}
    		if(server_msg.startsWith("LOGGEDOUT")){
    			server_msg=server_msg.substring(9);
    			iClientUI.removeUser(this,server_msg);
    		}
    		if(server_msg.startsWith("MESSAGEFROM")){
    			String sender=server_msg.substring(12,server_msg.indexOf(":")-1);
    			room_message(sender,server_msg.substring(server_msg.indexOf(":")+2));
    		}
    	}
    }
    
    //Send message to server...
    protected void send_Message(String msg){
    	try{
    		msgToServer.writeBytes(msg+"\r\n");
    	}catch(IOException e){
    		System.out.println(e);
    		log_out();
    	}
    }
    
    //Log out from iChat v1.0...
    public  void log_out(){ 
    	if(iClient!=null){
    		send_Message("LOGGINGOUT "+userName);
    		try{
    			System.out.println("\nClosing connection...");
    			iThread=null;
    			iClient.close();
    		}catch(IOException e){
    			System.out.println(e);
    		}
    		iClient=null;
    		System.out.println("Logged out successfully...");
    	}
    }
    
    public InfiniteChatRoomUI getRoom(String otherClient){
    	int roomNo=iConversationRoomList.size();
    	InfiniteChatRoomUI returnRoom=null,tempRoom;
    	for(int iCount=0;iCount<roomNo;iCount++){
    		tempRoom=(InfiniteChatRoomUI)iConversationRoomList.get(iCount);
    		if(tempRoom.iReceiver.equals(otherClient)){
    			returnRoom=tempRoom;
    		}
    	}
    	return(returnRoom);
    }
    
    public void removeRoom(String otherClient){
    	int roomNo=iConversationRoomList.size();
    	int remove_room=-1;
    	InfiniteChatRoomUI tempRoom=null;
    	for(int iCount=0;iCount<roomNo;iCount++){
    		tempRoom=(InfiniteChatRoomUI)iConversationRoomList.get(iCount);
    		if(tempRoom.iReceiver.equals(otherClient)){
    			remove_room=iCount;
    		}
    	}
    	iConversationRoomList.remove(remove_room);
    	iConversationRoomList.trimToSize();
    }
    
    public boolean roomExists(String otherClient){
    	InfiniteChatRoomUI room=getRoom(otherClient);
    	if(room!=null){
    		return true;
    	}
    	else return false;
    }
    public void room_message(String otherClient,String message){
    	if(roomExists(otherClient)){
    		InfiniteChatRoomUI room=getRoom(otherClient);
    		room.convOutput.append("\n"+otherClient+" : "+message);
    		room.conversation.append("\n"+otherClient+" : "+message);
    	}
    	else{
    		InfiniteChatRoomUI room=new InfiniteChatRoomUI(this,otherClient);
    		iConversationRoomList.add(room);
    		if(!message.equals("")){
    			room.convOutput.append("\n"+otherClient+" : "+message);
    			room.conversation.append("\n"+otherClient+" : "+message);
    		}
    		
    	}
    }
    
    public static void main(String args[]){
    	InfiniteChatClientLogin iChatLogin=new InfiniteChatClientLogin();
    }
}
