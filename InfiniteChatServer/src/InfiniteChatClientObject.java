
import java.net.Socket;

public class InfiniteChatClientObject {
	
	Socket iClientSocket;
	String iClientName;
	
	public InfiniteChatClientObject(){
	}
	
	public InfiniteChatClientObject(Socket clientSocket,String clientName){
		iClientSocket=clientSocket;
		iClientName=clientName;
	}
	
	public void setClientSocket(Socket clientSocket){
		iClientSocket=clientSocket;
	}
	
	public void setClientName(String clientName){
		iClientName=clientName;
	}
	
	public Socket getClientSocket(){
		return(iClientSocket);
	}
	
	public String getClientName(){
		return(iClientName);
	}	
	
	public static void main(String args[]){
		InfiniteChatClientObject ico=new InfiniteChatClientObject();
	}
}
