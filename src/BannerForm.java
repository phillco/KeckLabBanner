import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

/**
 * Displays the actual banner using data from <code>BannerController</code>.
 * @author Phillip Cohen
 */
public class BannerForm extends JFrame implements KeyListener
{
	/**
	 * The <code>Image</code> used for double buffering.
	 */
	private Image virtualMem;

	public BannerForm()
	{
		addKeyListener( this );
		setSize( 600, 600 );
		setDefaultCloseOperation( DISPOSE_ON_CLOSE );
		updateFullscreen();
		setVisible( true );
	}

	/**
	 * Draws the banner onto the given Graphics context.
	 */
	private void draw( Graphics g )
	{
		// Use fancy, anti-aliased rendering.
		{
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint( RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY );
			g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
			g2d.setRenderingHint( RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY );
			g2d.setRenderingHint( RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE );
			g2d.setRenderingHint( RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON );
			g2d.setRenderingHint( RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC );
			g2d.setRenderingHint( RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY );
			g2d.setRenderingHint( RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE );
			g2d.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
		}

		// Draw the background and the text.
		g.setColor( Color.white );
		g.fillRect( 0, 0, getWidth(), getHeight() );
		g.setFont( new Font( "Sans serif", Font.BOLD, 256 ) );
		g.setColor( Color.red );
		g.drawString( "Testing", BannerController.getBannerX(), 600 );
		BannerController.tick();
	}

	@Override
	public void paint( Graphics g )
	{
		// Create the image if needed.
		if ( virtualMem == null )
			virtualMem = createImage( getWidth(), getHeight() );

		// Draw the game's graphics.
		draw( virtualMem.getGraphics() );

		// Flip the buffer to the screen.
		g.drawImage( virtualMem, 0, 0, this );

		repaint();
	}

	/**
	 * Included to prevent the clearing of the screen between repaints.
	 */
	@Override
	public void update( Graphics g )
	{
		paint( g );
	}

	/**
	 * Sets the window to be the fullscreen window.
	 */
	private void updateFullscreen()
	{
		GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

		// Set fullscreen mode if we're not already.
		if ( graphicsDevice.getFullScreenWindow() != this )
		{
			dispose();
			setUndecorated( true );
			// gameCanvas.setSize( graphicsDevice.getDisplayMode().getWidth(), graphicsDevice.getDisplayMode().getHeight() );
			setSize( graphicsDevice.getDisplayMode().getWidth(), graphicsDevice.getDisplayMode().getHeight() );
			pack();
			graphicsDevice.setFullScreenWindow( this );

			// Hide the cursor.
			Image cursorImage = Toolkit.getDefaultToolkit().getImage( "xparent.gif" );
			Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor( cursorImage, new Point( 0, 0 ), "" );
			setCursor( blankCursor );
		}

		setVisible( true );
	}

	@Override
	public void keyPressed( KeyEvent arg0 )
	{
		if ( arg0.getKeyCode() == KeyEvent.VK_ESCAPE )
			dispose();
	}

	@Override
	public void keyReleased( KeyEvent arg0 )
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void keyTyped( KeyEvent arg0 )
	{
		// TODO Auto-generated method stub
	}
}
