package application;

import java.io.*;

public class TCP_SERVER {

	void test() throws IOException {
		int port = 11111;
		java.net.ServerSocket serverSocket = new java.net.ServerSocket(port);
		java.net.Socket client = warteAufAnmeldung(serverSocket);
		String nachricht = leseNachricht(client);
		System.out.println(nachricht);
		//schreibeNachricht(client, nachricht);
	}

	java.net.Socket warteAufAnmeldung(java.net.ServerSocket serverSocket)
			throws IOException {
		java.net.Socket socket = serverSocket.accept(); // blockiert, bis sich
		// ein Client angemeldet
		// hat
		return socket;
	}

	String leseNachricht(java.net.Socket socket) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(socket.getInputStream()));
		char[] buffer = new char[200];
		int anzahlZeichen = bufferedReader.read(buffer, 0, 200); // blockiert
		// bis
		// Nachricht
		// empfangen
		String nachricht = new String(buffer, 0, anzahlZeichen);
		return nachricht;
	}
}