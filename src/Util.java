public class Util
{
	public static void safeSleep( int milliSeconds )
	{
		try
		{
			Thread.sleep( milliSeconds );
		}
		catch ( InterruptedException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
