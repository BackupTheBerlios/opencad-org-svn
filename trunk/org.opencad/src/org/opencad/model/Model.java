/**
 * 
 */
package org.opencad.model;

/**
 * @author George
 */
public abstract class Model {
	boolean selected;

	public boolean isSelected() {
		return this.selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	abstract void drawModel();
	abstract void drawSchematic();
	abstract void initGL();
}
