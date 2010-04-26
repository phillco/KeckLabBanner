public abstract class NetworkDongle
{
	protected boolean connected = false;
	
	public void disconnect()
	{
		connected = false;
	}

	public abstract String getStatusString();
}
