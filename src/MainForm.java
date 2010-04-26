import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The startup form with buttons to host/connect.
 * @author Phillip Cohen
 */
public class MainForm extends JDialog
{
	private static final long serialVersionUID = 2062760208030859011L;

	public MainForm()
	{		
		setLayout( new BoxLayout( getContentPane(), BoxLayout.Y_AXIS ) );

		// Add the title JPanel.
		JPanel titlePanel = new JPanel();
		{
			JLabel titleLabel = new JLabel( "Keck Lab Banner!" );
			titleLabel.setForeground(  Color.LIGHT_GRAY );
			titleLabel.setFont( new Font( "Sans serif", Font.BOLD, 28 ) );
			titlePanel.add( Box.createRigidArea( new Dimension( 1, 30 ) ) );
			titlePanel.add( titleLabel );
			titlePanel.setBackground(  Color.gray );
		}
		add( titlePanel );
		
		// Add the host/connect buttons.
		add( Box.createRigidArea( new Dimension( 1, 25 ) ) );
		add( new JButton( "Start a new session" ));
		add( Box.createRigidArea( new Dimension( 1, 5 ) ) );
		add( new JButton( "Join existing session" ));
		add( Box.createRigidArea( new Dimension( 1, 35 ) ) );
		
		// Add the footer.
		JPanel footerPanel = new JPanel();
		{
			JLabel footerLabel = new JLabel("Version 1.0 / created by Phillip Cohen");
			footerLabel.setForeground( Color.gray );
			footerPanel.add( footerLabel );
		}
		add( footerPanel );
		
		// Other attributes...
		setTitle( "Keck Lab Banner!" );
		setSize( 400, 230 );
		setVisible( true );
	}
}
