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
		catch ( InterruptedException e )
		{
		}
	}
}
