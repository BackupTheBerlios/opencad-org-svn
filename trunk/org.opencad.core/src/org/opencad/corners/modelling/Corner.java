package org.opencad.corners.modelling;

import org.opencad.corners.rendering.CornerEditorRenderer;
import org.opencad.corners.ui.SelectCornerState;
import org.opencad.model.modelling.Primitive;
import org.opencad.model.modelling.PrimitiveTypeRegister;
import org.opencad.ui.behaviour.Hoverable;
import org.opencad.ui.behaviour.Selectable;
import org.opencad.ui.editors.GLEditor;
import org.opencad.ui.editors.state.GLEditorState;

public class Corner extends Primitive implements Hoverable, Selectable {

	private static final long serialVersionUID = -1332083715502519329L;
	
	static {
		PrimitiveTypeRegister.registerPrimitiveType(Corner.class);
	}
	
	private Double x, y;

	private boolean hover;

	private boolean selected;
	
	private double hoverSlack = 0.06d;


	public final Double getX() {
		return x;
	}

	public final void setX(Double x) {
		this.x = x;
	}

	public final Double getY() {
		return y;
	}

	public final void setY(Double y) {
		this.y = y;
	}

	public Corner(Double x, Double y) {
		this.x = x;
		this.y = y;
		addRenderer(new CornerEditorRenderer(this));
	}

	public String toString() {
		return String.format("%.2f:%.2f", x, y);
	}

	public boolean isHoverCoordinates(double x, double y) {
		return Math.abs(this.x - x) < hoverSlack
				&& Math.abs(this.y - y) < hoverSlack;
	}

	public boolean setHover(boolean hover) {
		boolean changed = this.hover != hover;
		this.hover = hover;
		return changed;
	}

	public boolean isHover() {
		return hover;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public GLEditorState getSelectionState(GLEditor editor) {
		return new SelectCornerState(editor, this);
	}
}