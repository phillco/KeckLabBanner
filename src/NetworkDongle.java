public abstract class NetworkDongle
{
	protected boolean connected = false;

	protected BannerController localController;

	public NetworkDongle( BannerController localController )
	{
		this.localController = localController;
	}

	public void disconnect()
	{
		connected = false;

		if ( localController != null )
			localController.stop();
	}
}
