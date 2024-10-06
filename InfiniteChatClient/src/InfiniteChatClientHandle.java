import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class InfiniteChatClientHandle {
	Socket clientSocket;
	String userName,serverName;
	int portNo=1664;
	BufferedReader msgFromServer;
	DataOutputStream msgToServer;
	
	/**
	 * Method InfiniteChatClientHandle
	 *
	 *
	 */
	public InfiniteChatClientHandle() {
		
	}
	
	public void setClientSocket(Socket client_socket){
		clientSocket=client_socket;
	}
	
	public void setPortNo(int port_no){
		portNo=port_no;
	}
	
	public void setUserName(String user_name){
		userName=user_name;
	}
	
	public void setServerName(String server_name){
		serverName=server_name;
	}
	
	public void setInputStream(BufferedReader inputStream){
		msgFromServer=inputStream;
	}
	
	public void setOutputStream(DataOutputStream outputStream){
		msgToServer=outputStream;
	}
	
	public Socket getClientSocket(){
		return(clientSocket);
	}
	
	public int getPortNo(){
		return(portNo);
	}
	
	public String getUserName(){
		return(userName);
	}
	
	public String getServerName(){
		return(serverName);
	}
	
	public BufferedReader getInputStream(){
		return(msgFromServer);
	}
	
	public DataOutputStream getOutputStream(){
		return(msgToServer);
	}
}
