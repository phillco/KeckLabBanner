import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

/**
 * Contains useful functions.
 */
public class Util
{
	/**
	 * Same as Thread.sleep, but doesn't throw anything if interrupted.
	 */
	public static void safeSleep( int milliSeconds )
	{
		try
		{
			Thread.sleep( milliSeconds );
		}
		catch ( final InterruptedException e )
		{
		}
	}

	/**
	 * Returns the width of the current screen.
	 */
	public static int getScreenWidth()
	{
		final GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		return graphicsDevice.getDisplayMode().getWidth();
	}
}
