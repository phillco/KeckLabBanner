
/**
 * Controls where the banner will be displayed, and where that data comes from (locally, or from the server).
 * @author Phillip Cohen
 */
public class BannerController
{
	private static int x = 0;
	
	public static int getBannerX()
	{
		return x;
	}
	
	public static void tick()
	{
		x += 5;
	}

}
