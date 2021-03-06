package de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes;

import java.awt.Color;

import de.invation.code.toval.validate.ParameterException;

/**
 * <p>
 * The line attribute class can define a color, a shape, a style, and a width. It is used by places, transitions, pages, annotations and arcs.
 * </p>
 * 
 * @author Adrian Lange
 */
public class Line extends AbstractAttribute {

	/** Default color as CSS2 color string */
	public static final String DEFAULT_COLOR = "black";
	/** Default line shape */
	public static final Shape DEFAULT_SHAPE = Shape.LINE;
	/** Default line style */
	public static final Style DEFAULT_STYLE = Style.SOLID;
	/** Default width of the line */
	public static final double DEFAULT_WIDTH = 1.0;
	/** Line color as CSS2 color string */
	private String color = DEFAULT_COLOR;
	/** Shape of the line */
	private Shape shape = DEFAULT_SHAPE;
	/** Style of the line */
	private Style style = DEFAULT_STYLE;
	/** Width of the line */
	private double width = DEFAULT_WIDTH;

	/**
	 * Creates new line attribute with default values.
	 */
	public Line() {}

	/**
	 * Creates new line attribute with the specified values.
	 * 
	 * @param color
	 *            Color as CSS2 color string
	 * @param shape
	 *            Shape of the line
	 * @param style
	 *            Style of the line
	 * @param width
	 *            Width of the line
	 */
	public Line(String color, Shape shape, Style style, double width) {
		setColor(color);
		setShape(shape);
		setStyle(style);
		setWidth(width);
	}

	/**
	 * Returns the line color as CSS2 color string.
	 * 
	 * @return the color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * Returns the shape of the line.
	 * 
	 * @return the shape
	 */
	public Shape getShape() {
		return shape;
	}

	/**
	 * Returns the line style.
	 * 
	 * @return the style
	 */
	public Style getStyle() {
		return style;
	}

	/**
	 * Returns the line width.
	 * 
	 * @return the width
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * Sets the color of the line as CSS2 color string. Gets ignored if it has the value <code>null</code>.
	 * 
	 * @param color
	 *            the color to set
	 */
	public void setColor(String color) {
		if(color == null)
			return;
		try {
			Color.decode("0xFF0096");
		} catch(Exception e){
			throw new ParameterException("Invalid CSS color code: " + color);
		}
		this.color = color;
	}

	/**
	 * Sets the shape of the line. Gets ignored if it has the value <code>null</code>.
	 * 
	 * @param shape
	 *            the shape to set
	 */
	public void setShape(Shape shape) {
		this.shape = shape;
	}

	/**
	 * Sets the style of the line. Gets ignored if it has the value <code>null</code>.
	 * 
	 * @param style
	 *            the style to set
	 */
	public void setStyle(Style style) {
		this.style = style;
	}

	/**
	 * Sets the width of the line.
	 * 
	 * @param width
	 *            the width to set
	 */
	public void setWidth(double width) {
		this.width = width;
	}

	/**
	 * Enumeration to set the shape of the line
	 * 
	 * @author Adrian Lange
	 */
	public static enum Shape {
		LINE, CURVE;

		public static Shape getShape(String shapeStr) {
			if (shapeStr.equals("line"))
				return Shape.LINE;
			else if (shapeStr.equals("curve"))
				return Shape.CURVE;
			else
				return null;
		}

		@Override
		public String toString() {
			return super.toString().toLowerCase();
		}
	}

	/**
	 * Enumeration to set the line style
	 * 
	 * @author Adrian Lange
	 */
	public static enum Style {
		SOLID, DASH, DOT;

		public static Style getStyle(String styleStr) {
			if (styleStr.equals("solid"))
				return Style.SOLID;
			else if (styleStr.equals("dash"))
				return Style.DASH;
			else if (styleStr.equals("dot"))
				return Style.DOT;
			else
				return null;
		}

		@Override
		public String toString() {
			return super.toString().toLowerCase();
		}
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();

		str.append("Fill( ");
		if (getColor() != null)
			str.append("color:" + getColor() + " ");
		if (getShape() != null)
			str.append("shape:" + getShape() + " ");
		if (getStyle() != null)
			str.append("style:" + getStyle() + " ");
		if (getWidth() != DEFAULT_WIDTH)
			str.append("width:" + getWidth() + " ");
		str.append(")");

		return str.toString();
	}

	@Override
	public boolean hasContent() {
		// Because of the default value of width, this attribute always has content
		return true;
	}
}
