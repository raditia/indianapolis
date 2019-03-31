package com.gdn.laff;

public class Dimension {

	public static final Dimension EMPTY = new Dimension(0, 0, 0);

	public static Dimension decode(String size) {
		String[] dimensions = size.split("x");

		return newInstance(Integer.parseInt(dimensions[0]), Integer.parseInt(dimensions[1]), Integer.parseInt(dimensions[2]));
	}

	public static String encode(Dimension dto) {
		return encode(dto.getWidth(), dto.getDepth(), dto.getHeight());
	}

	public static String encode(double width, double depth, double height) {
		return width + "x" + depth + "x" + height;
	}

	public static Dimension newInstance(double width, double depth, double height) {
		return new Dimension(width, depth, height);
	}

	protected double width; // x
	protected double depth; // y
	protected double height; // z
	double volume;

	protected final String name;

	public Dimension(String name) {
		this.name = name;
	}

	public Dimension() {
		this(null);
	}

	public Dimension(String name, double w, double d, double h) {
		this.name = name;

		this.depth = d;
		this.width = w;
		this.height = h;

		this.volume = (depth) * (width) * (height);
	}

	public Dimension(double w, double d, double h) {
		this(null, w, d, h);
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	public double getDepth() {
		return depth;
	}

	/**
	 *
	 * Check whether a dimension fits within the current dimensions, rotated in 3D.
	 *
	 * @param dimension the space to fit
	 * @return true if any rotation of the argument can be placed inside this
	 *
	 */
	public boolean canHold3D(Dimension dimension) {
		return canHold3D(dimension.getWidth(), dimension.getDepth(), dimension.getHeight());
	}

	public boolean canHold3D(double w, double d, double h) {
        return (w <= width && h <= height && d <= depth) ||
               (h <= width && d <= height && w <= depth) ||
               (d <= width && w <= height && h <= depth) ||
               (h <= width && w <= height && d <= depth) ||
               (d <= width && h <= height && w <= depth) ||
               (w <= width && d <= height && h <= depth);
	}


	/**
	 *
	 * Check whether a dimension fits within the current object, rotated in 2D.
	 *
	 * @param dimension the dimension to fit
	 * @return true if any rotation of the argument can be placed inside this
	 *
	 */
	public boolean canHold2D(Dimension dimension) {
		return canHold2D(dimension.getWidth(), dimension.getDepth(), dimension.getHeight());
	}

	public boolean canHold2D(double w, double d, double h) {
		if(h > height) {
			return false;
		}
		return (w <= width && d <= depth) || (d <= width && w <= depth);
	}

	public double getFootprint() {
		return width * depth;
	}

	public boolean isSquare2D() {
		return width == depth;
	}

	public boolean isSquare3D() {
		return width == depth && width == height;
	}

	/**
	 * Check whether this object fits within a dimension (without rotation).
	 *
	 * @param dimension the dimensions to fit within
	 * @return true if this can fit within the argument space
	 */

	public boolean fitsInside3D(Dimension dimension) {
		return fitsInside3D(dimension.getWidth(), dimension.getDepth(), dimension.getHeight());
	}

	public boolean fitsInside3D(double w, double d, double h) {
		return w >= width && h >= height && d >= depth;
	}



	/**
	 * Check whether this object can fit within a dimension, with 3D rotation.
	 *
	 * @param dimension the dimensions to fit within
	 * @return true if this can fit within the argument space in any rotation
	 *
	 */

	public boolean canFitInside3D(Dimension dimension) {
		return dimension.canHold3D(this);
	}

	/**
	 * Check whether this object can fit within a dimension, with 2D rotation.
	 *
	 * @param dimension the dimensions to fit within
	 * @return true if this can fit within the argument space in any 2D rotation
	 *
	 */

	public boolean canFitInside2D(Dimension dimension) {
		return dimension.canHold2D(this);
	}

	public double getVolume() {
		return volume;
	}

	public boolean nonEmpty() {
		return width > 0 && depth > 0 && height > 0;
	}

	@Override
	public String toString() {
		return "Dimension [width=" + width + ", depth=" + depth + ", height=" + height + ", volume=" + volume + "]";
	}

	public String encode() {
		return encode(width, depth, height);
	}

	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (int) (prime * result + depth);
		result = (int) (prime * result + height);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((int) volume ^ ((int) volume >>> 32));
		result = (int) (prime * result + width);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Dimension other = (Dimension) obj;
		if (depth != other.depth)
			return false;
		if (height != other.height)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (volume != other.volume)
			return false;
		if (width != other.width)
			return false;
		return true;
	}

}
