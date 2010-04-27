/**
 * Controls where the banner will be displayed.
 * @author Phillip Cohen
 */
public class BannerController
{
	// The thread that moves the banner.
	protected UpdaterThread updater = new UpdaterThread();

	// Speed at which the banner moves.
	protected final int VELOCITY = 13;

	// The starting position at which the banner is invisible.
	private final int ORIGINAL_POSITION = -3000;

	// The current absolute position of the banner.
	private int x = ORIGINAL_POSITION;

	// Total width of all the computer screens combined.
	private int totalWidth;

	// Offset of the current computer's screen in totalWidth.
	private int myOffset;

	/**
	 * Starts the banner's movement. Can only be called once.
	 */
	public void start()
	{
		updater.start();
	}

	/**
	 * Stops the banner and ends the movement thread.
	 */
	public void stop()
	{
		updater.stopRunning();
	}

	/**
	 * Updates the size of the global area (call when a client is added or removed).
	 */
	public void updateOffsetData( int x, int totalWidth, int localOffset )
	{
		this.x = x;
		updateOffsetData( totalWidth, localOffset );
	}

	/**
	 * Updates the size of the global area (call when a client is added or removed).
	 */
	public void updateOffsetData( int totalWidth, int localOffset )
	{
		this.totalWidth = totalWidth;
		myOffset = localOffset;
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
			x = ORIGINAL_POSITION;
	}

	/**
	 * Returns the GLOBAL banner address for networking.
	 */
	public int getX()
	{
		return x;
	}

	/**
	 * Returns the LOCAL banner address for drawing.
	 */
	public int getLocalBannerX()
	{
		return x - myOffset;
	}

	/**
	 * Returns a debugging status string.
	 */
	public String getStatusString()
	{
		return "x: " + x + " [ " + myOffset + "/" + totalWidth + " ]";
	}

	/**
	 * The thread that constantly moves the banner.
	 */
	protected class UpdaterThread extends Thread
	{
		private boolean shouldRun = true;

		public void stopRunning()
		{
			shouldRun = false;
		}

		@Override
		public void run()
		{
			while ( shouldRun )
				try
				{
					tick();
					Thread.sleep( 10 );
				}
				catch ( final InterruptedException e )
				{
				}
		}
	}
}
