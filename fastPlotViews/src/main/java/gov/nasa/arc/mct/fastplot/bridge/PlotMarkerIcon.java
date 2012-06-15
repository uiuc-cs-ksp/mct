package gov.nasa.arc.mct.fastplot.bridge;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.Icon;

public class PlotMarkerIcon implements Icon {
	private static final Color TRANSPARENT = new Color(0,0,0,0);
	
	private BufferedImage image;
	private Shape         shape;
	private float         xOffset;
	private float         yOffset;
	private Color         drawnColor;
	private boolean       center;
	private boolean       deriveColor = false;

	public PlotMarkerIcon(Shape shape) {
		this(shape, true);
	}
	
	public PlotMarkerIcon(Shape shape, boolean center) {
		this(shape, center, shape.getBounds().width+1, shape.getBounds().height+1);
	}
	public PlotMarkerIcon(Shape shape, boolean center, int width, int height) {
		this(shape, Color.GRAY, center, width, height);
		deriveColor = true;
	}
	
	public PlotMarkerIcon(Shape shape, Color color, boolean center) {
		this(shape, color, center, shape.getBounds().width+1, shape.getBounds().height+1);
	}
	
	public PlotMarkerIcon(Shape shape, Color color, boolean center, int width, int height) {
		this.shape  = shape;
		this.center = center;
		
		Rectangle r = shape.getBounds();		
		if (r.width > width || r.height > height) {
			float scale = Math.min((float) width / (float) (r.width+1), (float) width / (float) (r.height+1));
			this.shape = AffineTransform.getScaleInstance(scale, scale).createTransformedShape(shape);
			r = shape.getBounds();
		}
		
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		xOffset = r.x;
		yOffset = r.y;
		render(color);
	}
	
	public void render(Color c) {
		drawnColor = c;
		Graphics2D g = image.createGraphics();
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				image.setRGB(x, y, TRANSPARENT.getRGB());
			}
		}
		// We render rarely (mostly we just reuse old images) so we can afford to antialias
		Object oldHint = g.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(c);
		g.translate(image.getWidth()/2, image.getHeight()/2);
		g.fill(shape);
		g.setColor(c.darker().darker());
		g.draw(shape);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldHint);
	}
	
	@Override
	public int getIconHeight() {
		return image.getHeight();
	}

	@Override
	public int getIconWidth() {
		return image.getWidth();
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		if (deriveColor) {
			Color fg = c.getForeground();
			if (fg.getRGB() != drawnColor.getRGB()) { // Presumably this doesn't happen often
				render(fg);
			}
		}
		if (center) {
			g.drawImage(image, x + (int) xOffset, y + (int) yOffset, c);
		} else {
			g.drawImage(image, x, y, c);
		}
	}
	

}
