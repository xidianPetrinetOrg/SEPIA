package de.uni.freiburg.iig.telematik.sepia.graphic.netgraphics.attributes;

import java.awt.Color;
import java.net.URI;

import de.invation.code.toval.validate.ParameterException;

/**
 * <p>
 * The fill attribute class can define a color, a gradient color, a gradient rotation, and a filling image. It is used by annotations, pages, transitions and
 * pages.
 * </p>
 * 
 * @author Adrian Lange
 */
public class Fill extends AbstractAttribute {

	/** Default color as CSS2 color string */
	public static final String DEFAULT_COLOR = "white";
	/** Default gradient color as CSS2 color string */
	public static final String DEFAULT_GRADIENT_COLOR = null;
	/** Default gradient rotation */
	public static final GradientRotation DEFAULT_GRADIENT_ROTATION = null;
	/** Default image */
	public static final URI DEFAULT_IMAGE = null;
	/** CSS2 color string */
	private String color = DEFAULT_COLOR;
	/** CSS2 color string */
	private String gradientColor = DEFAULT_GRADIENT_COLOR;
	/** Gradient rotation enumeration value */
	private GradientRotation gradientRotation = DEFAULT_GRADIENT_ROTATION;
	/** URI to an image */
	private URI image = DEFAULT_IMAGE;

	/**
	 * Creates new fill attribute with default values.
	 */
	public Fill() {}

	/**
	 * Creates a new fill attribute with the specified values.
	 * 
	 * @param color
	 *            CSS2 color string for the filling color or <code>null</code> if it should be ignored.
	 * @param gradientColor
	 *            CSS2 color string for the gradient color or <code>null</code> if it should be ignored.
	 * @param gradientRotation
	 *            Rotation of the gradient or <code>null</code> if it should be ignored.
	 * @param image
	 *            Filling image URI or <code>null</code> if it should be ignored.
	 */
	public Fill(String color, String gradientColor, GradientRotation gradientRotation, URI image) {
		setColor(color);
		setGradientColor(gradientColor);
		setGradientRotation(gradientRotation);
		setImage(image);
	}

	/**
	 * Returns the fill color as CSS2 color string or <code>null</code> if no fill color has been specified.
	 * 
	 * @return the color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * Returns the gradient color as CSS2 color string or <code>null</code> if no gradient color has been specified.
	 * 
	 * @return the gradientColor
	 */
	public String getGradientColor() {
		return gradientColor;
	}

	/**
	 * Returns the gradient rotation or <code>null</code> if no gradient rotation has been specified.
	 * 
	 * @return the gradientRotation
	 */
	public GradientRotation getGradientRotation() {
		return gradientRotation;
	}

	/**
	 * Returns the image as URI or <code>null</code> if no image has been specified.
	 * 
	 * @return the image
	 */
	public URI getImage() {
		return image;
	}

	/**
	 * Sets the color of the fill attribute as CSS2 color string. Gets ignored if it has the value <code>null</code>.
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
	 * Sets the gradient color for the fill attribute as CSS2 color string. Gets ignored if it has the value <code>null</code>.
	 * 
	 * @param gradientColor
	 *            the gradientColor to set
	 */
	public void setGradientColor(String gradientColor) {
		if(gradientColor == null)
			return;
		try {
			Color.decode("0xFF0096");
		} catch(Exception e){
			throw new ParameterException("Invalid CSS color code: " + color);
		}
		this.gradientColor = gradientColor;
	}

	/**
	 * Sets the gradient rotation for the fill attribute. Gets ignored if it has the value <code>null</code>.
	 * 
	 * @param gradientRotation
	 *            the gradientRotation to set
	 */
	public void setGradientRotation(GradientRotation gradientRotation) {
		this.gradientRotation = gradientRotation;
	}

	/**
	 * Sets the image for the fill attribute. Gets ignored if it has the value <code>null</code>.
	 * 
	 * @param image
	 *            the image to set
	 */
	public void setImage(URI image) {
		this.image = image;
	}

	/**
	 * Enumeration to set the rotation of the gradient
	 * 
	 * @author Adrian Lange
	 */
	public static enum GradientRotation {
		VERTICAL, HORIZONTAL, DIAGONAL;
		
		public static GradientRotation getGradientRotation(String gradientRotationStr) {
			if (gradientRotationStr.equals("vertical"))
				return GradientRotation.VERTICAL;
			else if (gradientRotationStr.equals("horizontal"))
				return GradientRotation.HORIZONTAL;
			else if (gradientRotationStr.equals("diagonal"))
				return GradientRotation.DIAGONAL;
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
		if (getGradientColor() != null)
			str.append("gradientColor:" + getGradientColor() + " ");
		if (getGradientRotation() != null)
			str.append("gradientRotation:" + getGradientRotation() + " ");
		if (getImage() != null)
			str.append("image:" + getImage() + " ");
		str.append(")");

		return str.toString();
	}

	@Override
	public boolean hasContent() {
		return color != null || gradientColor != null || gradientRotation != null || image != null;
	}
}
