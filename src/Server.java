import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;

public class Server extends NetworkDongle
{
	public final static int DEFAULT_PORT = 51505;

	private int port;

	private LinkedList<Computer> clients = new LinkedList<Computer>();

	private ServerSocket serverSocket;

	private BannerController localController;

	private AcceptThread acceptThread = new AcceptThread();
	private PeriodicReflowThread reflowThread = new PeriodicReflowThread();

	public Server( BannerController localController, int port )
	{
		this.localController = localController;
		this.port = port;
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

		System.out.println( "Server started at " + getServerIP() + "!" );
		connected = true;
		clients.add( new ServerComputer() );

		// Listen for clients.
		acceptThread.start();
		reflowThread.start();
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
		while ( connected )
		{
			try
			{
				ClientComputer client = new ClientComputer( serverSocket.accept() ); // Stop here until the client connects.
				System.out.println( "Server: client received!" );
				clients.addFirst( client );
				reflowClients();
			}
			catch ( IOException e )
			{
			}
		}
	}

	public void reflowClients()
	{
		int globalWidth = clients.size() * BannerController.SCREEN_WIDTH;
		int currentLocation = 0;

		// Update all the clients of the location of the banner / size of global area.
		for ( Computer cm : clients )
		{
			if ( cm instanceof ServerComputer )
				localController.updateOffsetData( globalWidth, currentLocation );
			else
			{
				try
				{
					ClientComputer client = (ClientComputer) cm;
					client.outputStream.writeByte( 32 );
					client.outputStream.writeInt( localController.getLocalBannerX() );
					client.outputStream.writeInt( globalWidth );
					client.outputStream.writeInt( currentLocation );
					client.outputStream.flush();
				}
				catch ( IOException e )
				{
				}
			}
			currentLocation += BannerController.SCREEN_WIDTH;
		}
		System.out.println( "Server: " + clients.size() + " clients reflowed" );
	}

	private abstract class Computer
	{
	}

	private class ClientComputer extends Computer
	{
		public Socket socket;

		public DataOutputStream outputStream;

		public ClientComputer( Socket socket ) throws IOException
		{
			this.socket = socket;
			outputStream = new DataOutputStream( socket.getOutputStream() );
		}
	}

	private class ServerComputer extends Computer
	{
	}

	/**
	 * The thread that listens for clients.
	 */
	protected class AcceptThread extends Thread
	{
		@Override
		public void run()
		{
			acceptLoop();
		}
	}

	/**
	 * The thread that updates clients periodically.
	 */
	protected class PeriodicReflowThread extends Thread
	{
		@Override
		public void run()
		{
			while ( connected )
			{
				reflowClients();
				try
				{
					Thread.sleep( 200 );
				}
				catch ( InterruptedException e )
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void disconnect()
	{
		try
		{
			for ( Computer c : clients )
				if ( c instanceof ClientComputer )
					( (ClientComputer) c ).socket.close();

			clients.clear();
			if ( serverSocket != null )
			{
				serverSocket.close();
				serverSocket = null;
			}
		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}
		serverSocket = null;
		if ( acceptThread != null )
		{
			acceptThread.stop();
			acceptThread = null;
		}
		if ( reflowThread != null )
		{
			reflowThread.stop();
			reflowThread = null;
		}
		localController.stop();
	}

	@Override
	public String getStatusString()
	{
		if ( connected )
			return "Server at " + getServerIP() + ". " + ( clients.size() > 0 ? clients.size() + " client(s)" : "" );
		else
			return "Disconnected server";
	}
}
