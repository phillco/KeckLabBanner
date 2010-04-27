import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

public class Client extends NetworkDongle
{
	private DataInputStream inputStream;

	private DataOutputStream outputStream;

	private Socket localSocket;

	private String serverAddress;

	private int serverPort;

	private ReceiveThread receiveThread = new ReceiveThread();

	/**
	 * Connects the client to given server address.
	 */
	public Client( BannerController localController, String address, int port )
	{
		super( localController );
		this.serverAddress = address;
		this.serverPort = port;

		// Connect to the server!
		try
		{
			// Hook up the socket.
			localSocket = new Socket( serverAddress, serverPort );
			inputStream = new DataInputStream( localSocket.getInputStream() );
			outputStream = new DataOutputStream( localSocket.getOutputStream() );

			// Initial handshaking.
			outputStream.writeInt( MainForm.screenWidth );
			outputStream.flush();
		}
		catch ( UnknownHostException e )
		{
			JOptionPane.showMessageDialog( null, "Couldn't look up " + address + ".", "Connection error", JOptionPane.ERROR_MESSAGE );
			return;
		}
		catch ( IOException e )
		{
			JOptionPane.showMessageDialog( null, "Couldn't connect to " + address + ".", "Connection error", JOptionPane.ERROR_MESSAGE );
			return;
		}

		// We're connected!
		connected = true;
		System.out.println( "Client connected to " + serverAddress + ":" + serverPort + "!" );

		// Start auxiliary threads.
		localController.start();
		receiveThread.start();
	}

	/**
	 * Shuts down the client.
	 */
	@SuppressWarnings( "deprecation" )
	@Override
	public void disconnect()
	{

		try
		{
			// Close the local socket.
			if ( localSocket != null )
				localSocket.close();
		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}

		// Stop the receiving thread.
		if ( receiveThread != null )
			receiveThread.stop();
	}

	@Override
	public String toString()
	{
		if ( connected )
			return "Client connected to " + serverAddress + ":" + serverPort + ".";
		else
			return "Disconnected client";
	}

	/**
	 * Constantly listens for messages from the server.
	 */
	protected class ReceiveThread extends Thread
	{
		@Override
		public void run()
		{
			while ( connected )
			{
				try
				{
					int i = inputStream.readByte();
					if ( i == 32 )
					{
						int currentX = inputStream.readInt();
						int globalWidth = inputStream.readInt();
						int localOffset = inputStream.readInt();
						localController.updateOffsetData( currentX, globalWidth, localOffset );
						System.out.println( "Received reflow: x = " + currentX + ", width = " + globalWidth + ", offset = " + localOffset );
					}
					else
						System.out.println( "RECEIVED: " + i );
				}
				catch ( IOException e )
				{
					// Receive error - disconnect.
					connected = false;
					System.out.println( "DISCONNECTED :(" );
				}
			}
		}
	}
}
