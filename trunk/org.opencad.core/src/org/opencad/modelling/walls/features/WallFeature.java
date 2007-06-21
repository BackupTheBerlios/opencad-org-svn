package org.opencad.modelling.walls.features;

import org.opencad.modelling.Primitive;
import org.opencad.modelling.corners.Corner;
import org.opencad.modelling.walls.Wall;
import org.opencad.ui.editor.Outlineable;

public abstract class WallFeature extends Primitive implements
		Comparable<WallFeature>, Outlineable {
	private Double startOffset;

	private Double width;

	private Double height;

	private Double groundOffset;

	private Wall wall;

	@Override
	public boolean isRenderable() {
		return false;
	}

	public Wall getWall() {
		return wall;
	}

	public void setWall(Wall wall) {
		this.wall = wall;
	}

	public Double getMaxStartOffset() {
		return startOffset + width;
	}

	public double getMaxGroundOffset() {
		return groundOffset + height;
	}

	public Double getGroundOffset() {
		return groundOffset;
	}

	public void setGroundOffset(Double bottomOffset) {
		this.groundOffset = bottomOffset;
	}

	public Double getWidth() {
		return width;
	}

	public void setWidth(Double endOffset) {
		this.width = endOffset;
	}

	public Double getHeight() {
		return height;
	}

	public void setHeight(Double topOffset) {
		this.height = topOffset;
	}

	public Double getStartOffset() {
		return startOffset;
	}

	public void setStartOffset(Double startOffset) {
		this.startOffset = startOffset;
	}

	public int compareTo(WallFeature o) {
		return startOffset.compareTo(o.startOffset);
	}
	
	final public int getZIndex() {
		return -50;
	}

	final public boolean isHoverCoordinates(double x, double y) {
		if (wall != null) {
			double[] prj = wall.getProjectionOf(x, y);
			double len = wall.getLength();
			double sp = startOffset / len;
			double ep = getMaxStartOffset() / len;
			if (prj != null) {
				return Wall.sqr(x - prj[0]) + Wall.sqr(y - prj[1]) < Wall
						.sqr(Corner.thickness)
						&& prj[2] > sp && prj[2] < ep;
			}
		}
		return false;
	}

	final public String getText() {
		return String.format("%s @%.2f", getClass().getSimpleName(),
				getStartOffset());
	}
	
	final public boolean hasChildren() {
		return false;
	}
	
	final public Object[] getChildren() {
		return null;
	}
}