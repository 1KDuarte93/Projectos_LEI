package rmiserver;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
	static Socket mySocket;
	static int conectado = 0;
	static Scanner myScanner;

	public static void main(String args[]) throws UnknownHostException, IOException, InterruptedException{
		
		if (args.length != 2) {
			System.out.println("Utilização:\njava Special <endereço> <porto>");
			return;
		}

		try {
			String address = args[0];
			int port = Integer.parseInt(args[1]);	
			mySocket = new Socket(address, port);
			DataInputStream is = new DataInputStream(mySocket.getInputStream());
			DataOutputStream os = new DataOutputStream(mySocket.getOutputStream());
			String bufferOut;
			myScanner = new Scanner(System.in);

			ReceberThread myreceberthread = new ReceberThread(is);
			myreceberthread.start();
			
			System.out.println("Bem-vindo ao Meeto!");

			while (true) {
				bufferOut = myScanner.nextLine();
				os.writeUTF(bufferOut);
			}

		} catch (UnknownHostException e) {
			System.out.println("Host inexistente!");
			Client.main(null);

		} catch (IOException e) {
			System.out.println("Servidor não encontrado. A reconectar...");

			try {
				Thread.sleep(2000);
			} catch (InterruptedException ie) {
				// Do nothing!
			}

			Client.main(null);
		}
	}

	public static int getConectado() {
		return conectado;
	}

	public static void setConectado(int conectado) {
		Client.conectado = conectado;
	}
}

class ReceberThread extends Thread {
	DataInputStream dis;
	String bufferIn;
	
	public ReceberThread(DataInputStream dis) {
		this.dis = dis;
	}
	
	public void run() {
		while (true) {
			try {
				bufferIn = dis.readUTF();
				System.out.println(bufferIn);
			} catch (IOException e) {
				// Do nothing!
			}
		}
	}
}
