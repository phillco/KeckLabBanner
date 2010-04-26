/**
 * Controls where the banner will be displayed, and where that data comes from (locally, or from the server).
 * @author Phillip Cohen
 */
public class BannerController
{
	private int x = 50, dx = 5;

	private UpdaterThread updater;

	public BannerController()
	{
		updater = new UpdaterThread();
		updater.start();
	}

	public void tick()
	{
		x += dx;
		if ( x > 1440 )
			x = -1000;
	}

	public int getBannerX()
	{
		return x;
	}

	/**
	 * The thread that constantly moves the banner.
	 */
	private class UpdaterThread extends Thread
	{
		@Override
		public void run()
		{
			while ( true )
			{
				tick();
				try
				{
					Thread.sleep( 5 );
				}
				catch ( InterruptedException e )
				{
				}
			}
		}
	}
}
