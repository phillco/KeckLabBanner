/**
 * Controls where the banner will be displayed, and where that data comes from (locally, or from the server).
 * @author Phillip Cohen
 */
public class BannerController
{
	// Movement.
	protected UpdaterThread updater = new UpdaterThread();
	protected final int VELOCITY = 8;
	private boolean shouldRun = true;

	// Global positioning data.
	public final static int SCREEN_WIDTH = 1440;
	private int x = -2000, totalWidth, myOffset;

	public void start()
	{
		updater.start();
	}
	
	public void stop()
	{
		shouldRun = false;
	}
	

	/**
	 * Updates the size of the global banner (call when a client is added or removed).
	 */
	public void updateOffsetData( int x, int totalWidth, int localOffset )
	{
		this.x = x;
		this.totalWidth = totalWidth;
		this.myOffset = localOffset;
	}

	/**
	 * Updates the size of the global banner (call when a client is added or removed).
	 */
	public void updateOffsetData( int totalWidth, int localOffset )
	{
		this.totalWidth = totalWidth;
		this.myOffset = localOffset;
	}

	/**
	 * Moves the banner one step.
	 */
	public void tick()
	{
		// Move the banner globally.
		x += VELOCITY;

		// If the banner is off the last computer's screen, wrap it...
		if ( x > totalWidth )
			x = -1000;
	}

	/**
	 * Returns the LOCAL banner address for drawing.
	 */
	public int getLocalBannerX()
	{
		return x - myOffset;
	}

	/**
	 * The thread that constantly moves the banner.
	 */
	protected class UpdaterThread extends Thread
	{
		@Override
		public void run()
		{
			while ( shouldRun )
			{
				try
				{
					tick();
					Thread.sleep( 15 );
				}
				catch ( InterruptedException e )
				{
				}
			}
		}
	}
}
