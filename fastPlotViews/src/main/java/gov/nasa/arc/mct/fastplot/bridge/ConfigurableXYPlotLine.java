package gov.nasa.arc.mct.fastplot.bridge;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

import plotter.xy.LinearXYPlotLine;
import plotter.xy.XYAxis;
import plotter.xy.XYDimension;

public class ConfigurableXYPlotLine extends LinearXYPlotLine {
	private static final long serialVersionUID = -656948949864598815L;

	private Graphics2DWrapper graphicsWrapper = new Graphics2DWrapper();
	private int     weight = 1;
	private boolean draw   = true;
	private Icon    icon   = null;
//		new Icon() {
//
//		@Override
//		public int getIconHeight() {
//			return 8;
//		}
//
//		@Override
//		public int getIconWidth() {
//			return 8;
//		}
//
//		@Override
//		public void paintIcon(Component c, Graphics g, int x, int y) {
//			g.setColor(c.getForeground());
//			g.fillRect(x, y, getIconWidth(), getIconHeight());
//		}
//		
//	};
	
	public static void main(String[] args) {
		final JFrame frame = new JFrame("menu");
		frame.setBackground(Color.PINK);
		
		final JLabel label = new JLabel(" ");
		label.setFont(label.getFont().deriveFont(144.0f));
		label.setForeground(Color.CYAN.darker().darker());
		
		JPanel panel = new JPanel();
		panel.add(label);
		final JTextField field = new JTextField();
		field.setColumns(1);

		JMenuItem item = new JMenuItem();
		item.add(field);
		
		final JPopupMenu menu = new JPopupMenu("menu");
		JMenu submenu = new JMenu("Enter symbol");
		submenu.add(field);
		
		menu.add(new JMenuItem("test"));
		menu.add(submenu);
		field.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				menu.setVisible(false);
				label.setText(field.getText());
				frame.pack();
				frame.repaint();
			}
			
		});
		
		panel.addMouseListener( new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				menu.show(e.getComponent(), e.getX(), e.getY());
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				menu.show(e.getComponent(), e.getX(), e.getY());
			}
			
		});
		
		frame.getContentPane().add(panel);
		
		
		//frame.setSize(300, 300);
		frame.setVisible(true);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
	}
	
	public ConfigurableXYPlotLine(XYAxis xAxis, XYAxis yAxis,
			XYDimension independentDimension) {
		super(xAxis, yAxis, independentDimension);
//		setStroke(new BasicStroke(5));
//		setLineMode(LineMode.STRAIGHT);
//		
	}
	
	protected void paintComponent(Graphics g) {
		if (g instanceof Graphics2D) {
			graphicsWrapper.setParent((Graphics2D) g);
			super.paintComponent(graphicsWrapper);
		}
	}

	@Override
	public void setStroke(Stroke stroke) {
		super.setStroke(stroke);
		if (stroke instanceof BasicStroke) {
			weight = (int) ((BasicStroke) stroke).getLineWidth();
		} else {
			weight = 1;
		}
	}


	@Override
	public void repaint(long tm, int x, int y, int width, int height) {
		super.repaint(tm, x - weight/2, y - weight/2, width + weight, height + weight);
	}

	@Override
	public void repaint(int x, int y, int width, int height) {
		// TODO Auto-generated method stub
		super.repaint(x - weight/2, y - weight/2, width + weight, height + weight);
	}


	
	
	
	private class Graphics2DWrapper extends Graphics2D {
		Graphics2D parent;
		
		public void setParent(Graphics2D parent) {
			this.parent = parent;
		}
		
		public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {
			parent.drawPolyline(xPoints, yPoints, nPoints);
			if (icon != null) {
				int x = icon.getIconWidth()  / 2;
				int y = icon.getIconHeight() / 2;
				for (int i = 0; i < nPoints; i++) {
					icon.paintIcon(ConfigurableXYPlotLine.this, parent, xPoints[i] - x, yPoints[i] - y);
				}
			}
		}
		
		// Everything else is delegated....

		public void addRenderingHints(Map<?, ?> hints) {
			parent.addRenderingHints(hints);
		}

		public void clearRect(int x, int y, int width, int height) {
			parent.clearRect(x, y, width, height);
		}

		public void clip(Shape s) {
			parent.clip(s);
		}

		public void clipRect(int x, int y, int width, int height) {
			parent.clipRect(x, y, width, height);
		}

		public void copyArea(int x, int y, int width, int height, int dx, int dy) {
			parent.copyArea(x, y, width, height, dx, dy);
		}

		public Graphics create() {
			return parent.create();
		}

		public Graphics create(int x, int y, int width, int height) {
			return parent.create(x, y, width, height);
		}

		public void dispose() {
			parent.dispose();
		}

		public void draw(Shape s) {
			parent.draw(s);
		}

		public void draw3DRect(int x, int y, int width, int height,
				boolean raised) {
			parent.draw3DRect(x, y, width, height, raised);
		}

		public void drawArc(int x, int y, int width, int height,
				int startAngle, int arcAngle) {
			parent.drawArc(x, y, width, height, startAngle, arcAngle);
		}

		public void drawBytes(byte[] data, int offset, int length, int x, int y) {
			parent.drawBytes(data, offset, length, x, y);
		}

		public void drawChars(char[] data, int offset, int length, int x, int y) {
			parent.drawChars(data, offset, length, x, y);
		}

		public void drawGlyphVector(GlyphVector g, float x, float y) {
			parent.drawGlyphVector(g, x, y);
		}

		public void drawImage(BufferedImage img, BufferedImageOp op, int x,
				int y) {
			parent.drawImage(img, op, x, y);
		}

		public boolean drawImage(Image img, AffineTransform xform,
				ImageObserver obs) {
			return parent.drawImage(img, xform, obs);
		}

		public boolean drawImage(Image img, int x, int y, Color bgcolor,
				ImageObserver observer) {
			return parent.drawImage(img, x, y, bgcolor, observer);
		}

		public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
			return parent.drawImage(img, x, y, observer);
		}

		public boolean drawImage(Image img, int x, int y, int width,
				int height, Color bgcolor, ImageObserver observer) {
			return parent
					.drawImage(img, x, y, width, height, bgcolor, observer);
		}

		public boolean drawImage(Image img, int x, int y, int width,
				int height, ImageObserver observer) {
			return parent.drawImage(img, x, y, width, height, observer);
		}

		public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2,
				int sx1, int sy1, int sx2, int sy2, Color bgcolor,
				ImageObserver observer) {
			return parent.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2,
					sy2, bgcolor, observer);
		}

		public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2,
				int sx1, int sy1, int sx2, int sy2, ImageObserver observer) {
			return parent.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2,
					sy2, observer);
		}

		public void drawLine(int x1, int y1, int x2, int y2) {
			parent.drawLine(x1, y1, x2, y2);
		}

		public void drawOval(int x, int y, int width, int height) {
			parent.drawOval(x, y, width, height);
		}

		public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
			parent.drawPolygon(xPoints, yPoints, nPoints);
		}

		public void drawPolygon(Polygon p) {
			parent.drawPolygon(p);
		}

		public void drawRect(int x, int y, int width, int height) {
			parent.drawRect(x, y, width, height);
		}

		public void drawRenderableImage(RenderableImage img,
				AffineTransform xform) {
			parent.drawRenderableImage(img, xform);
		}

		public void drawRenderedImage(RenderedImage img, AffineTransform xform) {
			parent.drawRenderedImage(img, xform);
		}

		public void drawRoundRect(int x, int y, int width, int height,
				int arcWidth, int arcHeight) {
			parent.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
		}

		public void drawString(AttributedCharacterIterator iterator, float x,
				float y) {
			parent.drawString(iterator, x, y);
		}

		public void drawString(AttributedCharacterIterator iterator, int x,
				int y) {
			parent.drawString(iterator, x, y);
		}

		public void drawString(String str, float x, float y) {
			parent.drawString(str, x, y);
		}

		public void drawString(String str, int x, int y) {
			parent.drawString(str, x, y);
		}

		public boolean equals(Object obj) {
			return parent.equals(obj);
		}

		public void fill(Shape s) {
			parent.fill(s);
		}

		public void fill3DRect(int x, int y, int width, int height,
				boolean raised) {
			parent.fill3DRect(x, y, width, height, raised);
		}

		public void fillArc(int x, int y, int width, int height,
				int startAngle, int arcAngle) {
			parent.fillArc(x, y, width, height, startAngle, arcAngle);
		}

		public void fillOval(int x, int y, int width, int height) {
			parent.fillOval(x, y, width, height);
		}

		public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {
			parent.fillPolygon(xPoints, yPoints, nPoints);
		}

		public void fillPolygon(Polygon p) {
			parent.fillPolygon(p);
		}

		public void fillRect(int x, int y, int width, int height) {
			parent.fillRect(x, y, width, height);
		}

		public void fillRoundRect(int x, int y, int width, int height,
				int arcWidth, int arcHeight) {
			parent.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
		}

		public void finalize() {
			parent.finalize();
		}

		public Color getBackground() {
			return parent.getBackground();
		}

		public Shape getClip() {
			return parent.getClip();
		}

		public Rectangle getClipBounds() {
			return parent.getClipBounds();
		}

		public Rectangle getClipBounds(Rectangle r) {
			return parent.getClipBounds(r);
		}

		public Rectangle getClipRect() {
			return parent.getClipRect();
		}

		public Color getColor() {
			return parent.getColor();
		}

		public Composite getComposite() {
			return parent.getComposite();
		}

		public GraphicsConfiguration getDeviceConfiguration() {
			return parent.getDeviceConfiguration();
		}

		public Font getFont() {
			return parent.getFont();
		}

		public FontMetrics getFontMetrics() {
			return parent.getFontMetrics();
		}

		public FontMetrics getFontMetrics(Font f) {
			return parent.getFontMetrics(f);
		}

		public FontRenderContext getFontRenderContext() {
			return parent.getFontRenderContext();
		}

		public Paint getPaint() {
			return parent.getPaint();
		}

		public Object getRenderingHint(Key hintKey) {
			return parent.getRenderingHint(hintKey);
		}

		public RenderingHints getRenderingHints() {
			return parent.getRenderingHints();
		}

		public Stroke getStroke() {
			return parent.getStroke();
		}

		public AffineTransform getTransform() {
			return parent.getTransform();
		}

		public int hashCode() {
			return parent.hashCode();
		}

		public boolean hit(Rectangle rect, Shape s, boolean onStroke) {
			return parent.hit(rect, s, onStroke);
		}

		public boolean hitClip(int x, int y, int width, int height) {
			return parent.hitClip(x, y, width, height);
		}

		public void rotate(double theta, double x, double y) {
			parent.rotate(theta, x, y);
		}

		public void rotate(double theta) {
			parent.rotate(theta);
		}

		public void scale(double sx, double sy) {
			parent.scale(sx, sy);
		}

		public void setBackground(Color color) {
			parent.setBackground(color);
		}

		public void setClip(int x, int y, int width, int height) {
			parent.setClip(x, y, width, height);
		}

		public void setClip(Shape clip) {
			parent.setClip(clip);
		}

		public void setColor(Color c) {
			parent.setColor(c);
		}

		public void setComposite(Composite comp) {
			parent.setComposite(comp);
		}

		public void setFont(Font font) {
			parent.setFont(font);
		}

		public void setPaint(Paint paint) {
			parent.setPaint(paint);
		}

		public void setPaintMode() {
			parent.setPaintMode();
		}

		public void setRenderingHint(Key hintKey, Object hintValue) {
			parent.setRenderingHint(hintKey, hintValue);
		}

		public void setRenderingHints(Map<?, ?> hints) {
			parent.setRenderingHints(hints);
		}

		public void setStroke(Stroke s) {
			parent.setStroke(s);
		}

		public void setTransform(AffineTransform Tx) {
			parent.setTransform(Tx);
		}

		public void setXORMode(Color c1) {
			parent.setXORMode(c1);
		}

		public void shear(double shx, double shy) {
			parent.shear(shx, shy);
		}

		public String toString() {
			return parent.toString();
		}

		public void transform(AffineTransform Tx) {
			parent.transform(Tx);
		}

		public void translate(double tx, double ty) {
			parent.translate(tx, ty);
		}

		public void translate(int x, int y) {
			parent.translate(x, y);
		}
		
		
		
	}

}
