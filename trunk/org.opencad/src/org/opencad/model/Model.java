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
	abstract public void drawModel();
	abstract public void drawSchematic();
}
