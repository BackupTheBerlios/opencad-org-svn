package org.opencad.ui.editor;

public interface Hoverable {

	public boolean setHover(boolean hover);

	public boolean isHoverCoordinates(double x, double y);

	public boolean isHover();

	public int getZIndex();
}
