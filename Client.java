import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Client extends JFrame {
  
	private static final String String = null;
	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String message ="";
	private String serverIP;
	private Socket connection;
	
	//constructor
	public Client(String host){
		super("Rafi Messanger - Client");
		serverIP = host;
		userText = new JTextField();
		userText.setEditable(false);
		userText.addActionListener(
		
		   new ActionListener(){
			   public void actionPerformed(ActionEvent event){
				   sendMessage(event.getActionCommand());
				   userText.setText("");
			   }

		   }
		);
		
		add(userText, BorderLayout.NORTH);
		chatWindow = new JTextArea();
		add(new JScrollPane(chatWindow), BorderLayout.CENTER);
		setSize(800,650);
		setVisible(true);
	}
	//connect to server 
	public void startRunning(){
		try{
			connectToServer();
			setupStreams();
			whileChatting();
		}catch(EOFException eofException){
			showMessage("\n Client Terminated connection");
		}catch(IOException ioException){
			ioException.printStackTrace();	
	   }finally{
		   closeCrap();
	   }
	}
	//connect to server
	private void connectToServer() throws IOException{
		showMessage("Attempting conection... \n");
		connection = new Socket(InetAddress.getByName(serverIP), 6789);
		showMessage("Connected to" + connection.getInetAddress().getHostName());
	}
	

    //set up streams to send and receive messages
	private void setupStreams() throws IOException{
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		showMessage("\n Streams are now good to go! \n");
	
			
	}
	    //While chatting
		private void whileChatting() throws IOException{
		ableToType(true);
		do{
			try{
				message = (String) input.readObject();
				showMessage("\n" + message);
			}catch(ClassNotFoundException classNotfoundException){
				showMessage("\n I dont know that Object type");
			}
		}while(!message.equals("SERVER - END"));
			
		}
	//close the streams and sockets
	private void closeCrap(){
		showMessage("\n Closing Down.....");
		ableToType(false);
		try{
			output.close();
			input.close();
			connection.close();
		}catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	//send messages to the server
	private void sendMessage(String message){
		try{
			output.writeObject("CLIENT - " + message);
			output.flush();
			showMessage("\nClient - " + message);
		}catch(IOException ioException){
			chatWindow.append("\n something messed up!");
		}
	}
	//change or update chat window
	private void showMessage(final String m){
		SwingUtilities.invokeLater(
		   new Runnable(){
			   public void run(){
				   chatWindow.append(m);
			   }
		   }
		);
	}
	//gives user permission to type
	private void ableToType(final Boolean tof){
		SwingUtilities.invokeLater(
				   new Runnable(){
					   public void run(){
						   userText.setEditable(tof);
		    }
	      }
		);
	}
}
    

