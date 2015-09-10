import javax.swing.JFrame;

public class ServerTest {

	public static void main(String[] args){
		Server nhr = new Server();
		nhr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		nhr.startRunning();
	}
}
