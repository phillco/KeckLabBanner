import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * The startup form with buttons to host/connect.
 * @author Phillip Cohen
 */
public class MainForm extends JFrame implements ActionListener
{
	public static int screenWidth;

	private JButton hostButton, joinButton;

	public MainForm()
	{
		setLayout( new BoxLayout( getContentPane(), BoxLayout.Y_AXIS ) );

		// Add the title JPanel.
		JPanel titlePanel = new JPanel();
		{
			JLabel titleLabel = new JLabel( "Keck Lab Banner!" );
			titleLabel.setForeground( Color.LIGHT_GRAY );
			titleLabel.setFont( new Font( "Sans serif", Font.BOLD, 28 ) );
			titlePanel.add( Box.createRigidArea( new Dimension( 1, 30 ) ) );
			titlePanel.add( titleLabel );
			titlePanel.setBackground( Color.gray );
		}
		add( titlePanel );

		// Add the host/connect buttons.
		hostButton = new JButton( "Start a new session" );
		joinButton = new JButton( "Join existing session" );

		hostButton.addActionListener( this );
		joinButton.addActionListener( this );
		add( Box.createRigidArea( new Dimension( 1, 25 ) ) );
		add( hostButton );
		add( Box.createRigidArea( new Dimension( 1, 5 ) ) );
		add( joinButton );
		add( Box.createRigidArea( new Dimension( 1, 35 ) ) );

		// Add the footer.
		JPanel footerPanel = new JPanel();
		{
			JLabel footerLabel = new JLabel( "Version 1.0 / created by Phillip Cohen" );
			footerLabel.setForeground( Color.gray );
			footerPanel.add( footerLabel );
		}
		add( footerPanel );

		// Other attributes...
		setTitle( "Keck Lab Banner!" );
		setDefaultCloseOperation( DISPOSE_ON_CLOSE );
		setSize( 400, 230 );
		setResizable( false );
		setVisible( true );
	}

	@Override
	public void actionPerformed( ActionEvent e )
	{
		if ( e.getSource() == hostButton )
		{
			setVisible( false );
			BannerController controller = new BannerController();
			int port = 50903;// Integer.parseInt( JOptionPane.showInputDialog( "Enter the port number" ) );
			new BannerForm( controller, new Server( controller, port ) );
			dispose();
		}
		else if ( e.getSource() == joinButton )
		{
			setVisible( false );
			BannerController controller = new BannerController();
			String input = "10.40.123.23:50903";//JOptionPane.showInputDialog( "Enter the server's address and port." );
			new BannerForm( controller, new Client( controller, input.split( ":" )[0], Integer.parseInt( input.split( ":" )[1] ) ) );
			dispose();
		}

	}

	/**
	 * Program entry point.
	 * @param args Command-line args.
	 */
	public static void main( String[] args )
	{
		GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		screenWidth = graphicsDevice.getDisplayMode().getWidth();
		new MainForm().setVisible( true );
	}
}
