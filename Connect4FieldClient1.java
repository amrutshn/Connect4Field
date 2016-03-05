import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Connect4FieldClient1.java
 * 
 * Version:
 *    	1.1 November,6 2015
 *
 * Revisions:
 *      Initial revision
 */

/**
 * This program is a client socket for 4-player game
 * 
 * @author		Pooja Ketan Shah
 * @author		Amrut Shenoy
 *
 */

public class Connect4FieldClient1 {
	public static void main(String args[]) throws UnknownHostException, IOException{
		int port = 23463;
		BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
		String userInput;
		try {
			byte[] sendData = new byte[1024];
			byte[] sendDataRows = new byte[256];
			byte[] receiveData = new byte[1024];

			DatagramSocket clientSocket = new DatagramSocket();
			boolean toRead = false;
			InetAddress IPAddress = InetAddress.getByName("localhost");
			System.out.println("Enter game piece (X or # or @ or *) : ");
			String input = inputReader.readLine();
			sendData = input.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,IPAddress,port);
			clientSocket.send(sendPacket);
			
			while(true){
				sendData = new byte[1024];
				sendDataRows = new byte[256];
				receiveData = new byte[1024];

				
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				clientSocket.receive(receivePacket);
				byte[] receiveBytes = receivePacket.getData();
				String receivedData = new String(receiveBytes,0,receiveBytes.length);
				System.out.println(receivedData);
				if(receivedData.contains("column")){
					toRead = true;
				}
				else
					toRead = false;
				if(receivedData.contains("Winner")){
					break;
				}
				if(toRead){
					if((userInput = inputReader.readLine()) != null){
						sendDataRows = userInput.getBytes();
						DatagramPacket sendPacketRows = new DatagramPacket(sendDataRows, sendDataRows.length,IPAddress,port);
						clientSocket.send(sendPacketRows);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}