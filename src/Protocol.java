/**
 * The protocol between the server and client.
 */
public abstract class Protocol
{
	/**
	 * The default port the server runs on.
	 */
	public final static int DEFAULT_PORT = 50900;

	/**
	 * Messages sent from the server--->client.
	 */
	public enum ServerMessages
	{
		REFLOW( 1 );

		public final int networkId;

		private ServerMessages( int networkId )
		{
			this.networkId = networkId;
		}
	}
}
