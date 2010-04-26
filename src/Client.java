import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
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

	private ListenThread listenThread = new ListenThread();

	private BannerController localController;

	public Client( BannerController localController, String address, int port )
	{
		localSocket = null;
		this.serverAddress = address;
		this.serverPort = port;

		try
		{
			localSocket = new Socket( serverAddress, serverPort );
			inputStream = new DataInputStream( localSocket.getInputStream() );
			outputStream = new DataOutputStream( localSocket.getOutputStream() );
		}
		catch ( UnknownHostException e )
		{
			JOptionPane.showMessageDialog( null, "Don't know about host: " + address );
			return;
		}
		catch ( IOException e )
		{
			JOptionPane.showMessageDialog( null, "Couldn't get I/O for the connection to: " + address );
			return;
		}

		listenThread.start();
		connected = true;
		
		try
		{
			outputStream.writeInt( MainForm.screenWidth );
		}
		catch ( IOException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println( "Client connected to " + serverAddress + ":" + serverPort + "!" );
		this.localController = localController;
		this.localController.start();
	}

	public void listenLoop()
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
				// TODO Auto-generated catch block
				connected = false;
				System.out.println( "DISCONNECTED :(" );
			}
		}
	}

	@Override
	public void disconnect()
	{
		if ( localSocket != null )
		{
			try
			{
				localSocket.close();
			}
			catch ( IOException e )
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			localSocket = null;
			listenThread.stop();
		}
		if ( localController != null )
			localController.stop();
	}

	@Override
	public String getStatusString()
	{
		if ( connected )
			return "Client connected to " + serverAddress + ":" + serverPort + ".";
		else
			return "Disconnected client";
	}

	/**
	 * The thread that listens for clients.
	 */
	protected class ListenThread extends Thread
	{
		@Override
		public void run()
		{
			listenLoop();
		}
	}
}
