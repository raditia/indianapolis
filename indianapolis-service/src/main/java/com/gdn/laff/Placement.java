package com.gdn.laff;

/**
 * Placement as in box in a space.
 *
 * The box does not necessarily fill the whole space.
 */

public class Placement {

	private Space space;
	private Box box;

	public Placement(Space space, Box box) {
		this.space = space;
		this.box = box;
	}

	public Placement(Space space) {
		this.space = space;
	}

	public Space getSpace() {
		return space;
	}

	public Box getBox() {
		return box;
	}

	public void setBox(Box box) {
		this.box = box;
	}

	@Override
	public String toString() {
		return "Placement [" +
				space.getX() + "x" + space.getY() + "x" + space.getZ() + ", " +
				"name=" + box.getName() + ", " +
				"width=" + box.getWidth() + ", " +
				"depth=" + box.getDepth() + ", " +
				"height=" + box.getHeight() + "]";
	}

	double getCenterX() {
		return space.getX() + (box.getWidth() / 2);
	}

	double getCenterY() {
		return space.getY() + (box.getDepth() / 2);
	}

	boolean intersects(Placement placement) {
		return intersectsX(placement) && intersectsY(placement);
	}

	private boolean intersectsY(Placement placement) {

		double startY = space.getY();
		double endY = startY + box.getDepth() - 1;

		if (startY <= placement.getSpace().getY() && placement.getSpace().getY() <= endY) {
			return true;
		}

		return startY <= placement.getSpace().getY() + placement.getBox().getDepth() - 1 &&
				placement.getSpace().getY() + placement.getBox().getDepth() - 1 <= endY;

	}

	private boolean intersectsX(Placement placement) {

		double startX = space.getX();
		double endX = startX + box.getWidth() - 1;

		if (startX <= placement.getSpace().getX() && placement.getSpace().getX() <= endX) {
			return true;
		}

		return startX <= placement.getSpace().getX() + placement.getBox().getWidth() - 1 &&
				placement.getSpace().getX() + placement.getBox().getWidth() - 1 <= endX;

	}
}
