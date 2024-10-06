import java.awt.Panel;
import java.awt.Image;
import java.awt.Graphics;

public class ImagePanel extends Panel{
	Image displayImage;
	/**
	 * Method ImagePanel
	 *
	 *
	 */
	public ImagePanel(Image image) {
		setLayout(null);
		displayImage=image;
	}	
	public void paint(Graphics graphics)
	{
		graphics.drawImage(displayImage,0,0,this);	
	}

}
