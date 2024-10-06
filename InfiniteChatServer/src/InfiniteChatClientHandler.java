/**
 * @(#)InfiniteClientHandler.java
 *
 * InfiniteChatServer application
 *
 * @author 
 * @version 1.00 2008/10/4
 */
 
import java.net.Socket;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.BufferedInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class InfiniteChatClientHandler implements Runnable{
	
	InfiniteChatServer myServer;
	Socket newClientHandle;
	BufferedReader iReader;
	Thread iThread;
	String iClntMsg;
	
	public InfiniteChatClientHandler(InfiniteChatServer server,Socket client){
		myServer=server;
		newClientHandle=client;
		try {	
			iReader=new BufferedReader(new InputStreamReader(new DataInputStream(new BufferedInputStream(newClientHandle.getInputStream()))));		
		}catch(IOException e){}
		iThread=new Thread(this);
		iThread.start();
	}
	
	public void run(){
		while(iThread!=null){
			try{
				iClntMsg=iReader.readLine();
				
				if(iClntMsg.startsWith("NEWCLIENT")){
					myServer.addClient(newClientHandle,iClntMsg.substring(10));
				}
				else if(iClntMsg.startsWith("LOGGINGOUT")){
					myServer.removeClient(iClntMsg.substring(11));
				}
				else if(iClntMsg.startsWith("MESSAGETO")){
					String recepient=iClntMsg.substring(10,iClntMsg.indexOf(" ",10));
					String message=iClntMsg.substring(iClntMsg.indexOf(" ",10)+1);
					myServer.unicast(myServer.getClientSocket(recepient),"MESSAGEFROM "+message);
				}
			}catch(IOException e){}
		}
	}
}