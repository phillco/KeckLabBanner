/**
 * Either a server or a client. This parent class is used to share code, and let BannerFrame control both seamlessly.
 */
public abstract class NetworkDongle
{
	/**
	 * Are we connected?
	 */
	protected boolean connected = false;

	/**
	 * The banner controller used on this computer.
	 */
	protected BannerController localController;

	/**
	 * Base constructor.
	 */
	public NetworkDongle( BannerController localController )
	{
		this.localController = localController;
	}

	/**
	 * Disconnects this server/client.
	 */
	public void disconnect()
	{
		connected = false;

		if ( localController != null )
			localController.stop();
	}
}
