/*******************************************************************************
 * Mission Control Technologies, Copyright (c) 2009-2012, United States Government
 * as represented by the Administrator of the National Aeronautics and Space 
 * Administration. All rights reserved.
 *
 * The MCT platform is licensed under the Apache License, Version 2.0 (the 
 * "License"); you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT 
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the 
 * License for the specific language governing permissions and limitations under 
 * the License.
 *
 * MCT includes source code licensed under additional open source licenses. See 
 * the MCT Open Source Licenses file included with this distribution or the About 
 * MCT Licenses dialog available at runtime from the MCT Help menu for additional 
 * information. 
 *******************************************************************************/
package gov.nasa.arc.mct.fastplot.bridge;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Defines set of shapes to be used as markers when plotting lines.
 *
 */
public class PlotLineShapePalette {
	private static final ResourceBundle BUNDLE = 
        ResourceBundle.getBundle(PlotLineShapePalette.class.getName().substring(0, 
        		PlotLineShapePalette.class.getName().lastIndexOf("."))+".Bundle");

	private static List<Shape> shapeSet = new ArrayList<Shape>();
	
	private static final double SIZE = 10.0;
	private static final double POLYGON_RESOLUTION = 1000;

	private static void loadShapes() {
		addShape(makeRegularPoly(4), 2, 0.1250);
		addShape(makeRegularPoly(3), 4);
		addShape(makeHourglass(),    4, 0.1250);
		addShape(makeLBlock(),       8);
		addShape(makeCross(),        2, 0.1250);
		addShape(makeStovepipe(),    8);
		addShape(makeRegularPoly(8), 2, 0.0625);
		addShape(makeRegularPoly(5), 4);
	}
	
	private static Shape makeStovepipe() {
		Polygon p = new Polygon();
		
		p.addPoint(-1,  0);
		p.addPoint(-1, -3);
		p.addPoint( 1, -3);
		p.addPoint( 1,  0);
		p.addPoint( 3,  3);
		p.addPoint(-3,  3);
		
		return AffineTransform
			.getScaleInstance(SIZE/6, SIZE/6)
			.createTransformedShape(p);		
	}

	private static Shape makeCross() {
		Polygon p = new Polygon();
		
		p.addPoint(-1, -1);
		p.addPoint(-1, -3);
		p.addPoint( 1, -3);
		p.addPoint( 1, -1);
		p.addPoint( 3, -1);
		p.addPoint( 3,  1);
		p.addPoint( 1,  1);
		p.addPoint( 1,  3);
		p.addPoint(-1,  3);
		p.addPoint(-1,  1);
		p.addPoint(-3,  1);
		p.addPoint(-3, -1);		
		
		return AffineTransform
			.getScaleInstance(SIZE/6, SIZE/6)
			.createTransformedShape(p);	
	}
	
	private static Shape makeHourglass() {
		Polygon p = new Polygon();
		
		p.addPoint( 0,  0);
		p.addPoint( 1,  1);
		p.addPoint(-1,  1);
		p.addPoint( 0,  0);
		p.addPoint(-1, -1);
		p.addPoint( 1, -1);
		
		return AffineTransform
			.getScaleInstance(SIZE/2, SIZE/2)
			.createTransformedShape(p);	
	}
	
	private static Shape makeLBlock() {
		Polygon p = new Polygon();
		
		p.addPoint( 0,  0);
		p.addPoint( 0,  1);
		p.addPoint( 1,  1);
		p.addPoint( 1, -1);
		p.addPoint(-1, -1);
		p.addPoint(-1,  0);
		
		return AffineTransform
			.getScaleInstance(SIZE/2, SIZE/2)
			.createTransformedShape(p);
	}
	
	private static Shape makeRegularPoly (int sides) {
		Polygon p = new Polygon();
		
		double r = 1.41 * SIZE/2;
		double step = Math.PI * 2.0 / (double) sides;
		for (int i = 0; i < sides; i++) {
			double angle = step * i;
			int x = (int) (Math.cos(angle) * r * POLYGON_RESOLUTION);
			int y = (int) (Math.sin(angle) * r * POLYGON_RESOLUTION);
			p.addPoint(x, y);
		}
		
		return AffineTransform
			.getScaleInstance(1/POLYGON_RESOLUTION, 1/POLYGON_RESOLUTION)
			.createTransformedShape(p);
	}
	
	private static void addShape(Shape shape, int count) {
		addShape(shape, count, 1.0f / (float) count);
	}
	
	private static void addShape(Shape shape, int count, double rotationStep) {
		double step = Math.PI * 2.0 * rotationStep;
		double angle = 0.0;
		shapeSet.add(shape);
		for (int i = 1; i < count; i++) {
			angle += step;
			AffineTransform transform = AffineTransform.getRotateInstance(angle);
			shapeSet.add(transform.createTransformedShape(shape));			
		}
	}
	
	/**
	 * Return the ith color in the palette. If color is specified in the 
	 * UIManager (by way of viewColor.properties), use that; otherwise, fall 
	 * back to defaults.
	 * 
	 * @param i the index of the color to use
	 * @return the ith color
	 */
	public static Shape getShape(int i) {
		if (shapeSet.size() == 0) {
			loadShapes();
		}
		
		if (i >= shapeSet.size()){
			throw new IllegalArgumentException("Requested shape " + i + " out of range [ 0 .. "+ shapeSet.size() + "]");
		}
		
		return shapeSet.get(i);
	}

	/**
	 * Return the number of shapes in the pallete
	 * @return the number of shapes
	 */
	public static int getShapeCount() {
		if (shapeSet.size() == 0) {
			loadShapes();
		}

		return shapeSet.size();
	}
}
