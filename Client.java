import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client 
{
	public static void main(String[] args) {
		System.out.println("Signin <message>\nMessage <message>\nSignout");


		if (args.length != 1) {
			System.out.println("Error: you forgot <server IP>");
			return;
		}
		InetAddress serverAddress = null;
		int serverPort = 4242;
		DatagramSocket socket = null;

		//Metatroph IP paralipth apo String se InetAddress
		String serverIP = args[0];
		try {
			serverAddress = InetAddress.getByName(serverIP);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	
		//Dhmiourgeia socket gia send kai receive.
		try {
                	socket = new DatagramSocket();
					receiveThread temp = new receiveThread(socket);
					temp.start();
            	}catch (SocketException e) {
                	e.printStackTrace();
            	}	

                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while (true) 
                {
                    try 
                    {
                        String data = br.readLine();
                        NetworkTools.sendData(data, serverAddress, serverPort, socket);
                    }
                    catch (IOException e) 
                    {
                        e.printStackTrace();
                    }
                }	
	}
}

class receiveThread extends Thread
{
    DatagramSocket socket;
    public receiveThread(DatagramSocket socket)
    {
        this.socket = socket;
    }
    
    public void run()
    {
        while (true) {
			try {
				String s = NetworkTools.receiveData(this.socket);
				System.out.println(s);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    }
}