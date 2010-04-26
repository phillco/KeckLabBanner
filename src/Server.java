import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;

public class Server
{
	private int port = 50000;

	private LinkedList<ClientComputer> clients = new LinkedList<ClientComputer>();

	private ServerSocket serverSocket;

	private BannerController localController;

	public Server( BannerController localController )
	{
		this.localController = localController;
		reflowClients();
		localController.start();

		// Now actually start the server.
		try
		{
			serverSocket = new ServerSocket( port );
		}
		catch ( IOException e )
		{
			System.out.println( "Could not listen on port: " + port + ".\nAnother server may already be running." );
			return;
		}
		
		// Listen for clients.
		new ListenLoop().start();
	}

	public String getServerIP()
	{
		try
		{
			InetAddress localHost = InetAddress.getLocalHost();
			InetAddress[] all_IPs = InetAddress.getAllByName( localHost.getHostName() );
			return ( all_IPs[0].toString().split( "/" ) )[1] + ":" + port;
		}
		catch ( UnknownHostException e )
		{
			return "Could not detect IP.";
		}
	}

	public void acceptLoop()
	{
		while ( true )
		{
			try
			{
				ClientComputer client = new ClientComputer( serverSocket.accept() ); // Stop here until the client connects.
				clients.add( client );
				reflowClients();
			}
			catch ( IOException e )
			{
			}
		}
	}

	public void reflowClients()
	{
		int globalWidth = ( clients.size() + 1 ) * BannerController.SCREEN_WIDTH;
		int currentLocation = 0;

		// Our instance is always first.
		localController.updateOffsetData( globalWidth, 0 );
		currentLocation = BannerController.SCREEN_WIDTH;

		// Update all the clients of the location of the banner / size of global area.
		for ( ClientComputer c : clients )
		{
			try
			{
				c.outputStream.writeByte( 0 );
				c.outputStream.writeByte( localController.getLocalBannerX() );
				c.outputStream.writeByte( globalWidth );
				c.outputStream.writeByte( currentLocation );
				c.outputStream.flush();
				currentLocation += BannerController.SCREEN_WIDTH;
			}
			catch ( IOException e )
			{
			}
		}
	}

	private class ClientComputer
	{
		public Socket socket;

		public DataOutputStream outputStream;

		public ClientComputer( Socket socket ) throws IOException
		{
			this.socket = socket;
			outputStream = new DataOutputStream( socket.getOutputStream() );
		}
	}
	
	/**
	 * The thread that listens for clients.
	 */
	protected class ListenLoop extends Thread
	{
		@Override
		public void run()
		{
			acceptLoop();
		}
	}
}
