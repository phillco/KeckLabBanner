/**
 * Controls where the banner will be displayed, and where that data comes from (locally, or from the server).
 * @author Phillip Cohen
 */
public class BannerController
{
	private static int x = 50, dx = 5;
	
	private static UpdaterThread updater;

	public static void start()
	{
		if ( updater == null )
		{
			updater = new UpdaterThread();
			updater.start();
		}
	}

	public static void tick()
	{
		x += dx;
		if ( x > 1440 )
			x = -1000;
	}
	
	public static int getBannerX()
	{
		return x;
	}

	/**
	 * The thread that constantly moves the banner.
	 */
	private static class UpdaterThread extends Thread
	{
		@Override
		public void run()
		{
			while ( true )
			{
				BannerController.tick();
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
