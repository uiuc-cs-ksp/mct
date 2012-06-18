package gov.nasa.arc.mct.fastplot.bridge;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.JLabel;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TestPlotMarkerIcon {
	private static final Color BACKGROUND = Color.ORANGE;
	
	private JLabel label = new JLabel();
	
	
	@Test
	public void testSimpleInstantiation() {
		for (int i = 5; i < 20; i+=2) { // Try circles of some different sizes
			Icon icon = new PlotMarkerIcon(makeCircle(i));
			
			// Give a couple of pixels of tolerance for size
			Assert.assertTrue(icon.getIconWidth()  >= 2*i  );
			Assert.assertTrue(icon.getIconHeight() <= 2*i+2);			
		}
	}
	
	@Test
	public void testCenteredCircle() {
		for (int i = 5; i < 20; i+=2) { // Try circles of some different sizes
			Icon icon = new PlotMarkerIcon(makeCircle(i), true);
			
			// Give a couple of pixels of tolerance
			Assert.assertTrue(icon.getIconWidth()  >= 2*i  );
			Assert.assertTrue(icon.getIconHeight() <= 2*i+2);
			
			assertIsFilledEllipse (drawCenteredIcon(icon), label.getForeground());
		}
	}

	private void assertIsFilledEllipse(BufferedImage image, Color color) {
		int w = image.getWidth();
		int h = image.getHeight();
		int x = w / 2; // Expected origin
		int y = h / 2;
		int r = (x+y) / 2; // Estimate expected radius
		
		for (int v = 0; v < h; v++) {
			for (int u = 0; u < w; u++) {
				double d = Math.sqrt( (u-x)*(u-x) + (v-y)*(v-y) );
				// Note: Permit a region of uncertainty +- 2 pixels for border/rounding
				if (d > r + 2) {
					Assert.assertEquals(image.getRGB(u, v), BACKGROUND.getRGB());
				} else if (d < r - 2) {
					Assert.assertEquals(image.getRGB(u, v), color.getRGB());
				}
			}
		}
		
	}
	
	private BufferedImage drawCenteredIcon(Icon i) {
		BufferedImage image = new BufferedImage(i.getIconWidth(), i.getIconHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics g = image.createGraphics();
		g.setColor(BACKGROUND);
		g.fillRect(0, 0, i.getIconWidth(), i.getIconHeight());
		i.paintIcon(label, image.createGraphics(), i.getIconWidth()/2, i.getIconHeight()/2);		
		return image;
	}
	
	private Shape makeCircle (double radius) {
		return new Ellipse2D.Double(-radius, -radius, radius*2, radius*2);
	}
	
}
