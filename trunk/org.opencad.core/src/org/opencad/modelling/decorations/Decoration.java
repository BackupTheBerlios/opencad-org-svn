package org.opencad.modelling.decorations;

import org.opencad.modelling.Model;
import org.opencad.modelling.Primitive;
import org.opencad.modelling.PrimitiveTypeRegister;
import org.opencad.ui.editor.Deletable;
import org.opencad.ui.editor.GLEditor;
import org.opencad.ui.editor.GLEditorState;
import org.opencad.ui.editor.Outlineable;

public abstract class Decoration extends Primitive implements Outlineable, Deletable {

	private double rotation;

	protected double x, y;

	static {
		PrimitiveTypeRegister.registerPrimitiveType(Decoration.class);
	}

	public int getZIndex() {
		return 0;
	}

	public GLEditorState getSelectionState(GLEditor editor) {
		return new SelectDecorationState(editor, this);
	}

	public double getRotation() {
		return rotation;
	}

	public void setRotation(double rotation) {
		this.rotation = rotation;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public Object[] getChildren() {
		return null;
	}

	public boolean hasChildren() {
		return false;
	}

	public String getText() {
		return String.format("%s @%.2f:%.2f", getClass().getSimpleName(), x, y);
	}

	public void delete(Model model) {
		model.removePrimitive(this);
	}

	public double[] correctRotation(double x, double y) {
		double dx = x - this.x;
		double dy = y - this.y;
		double angle = Math.atan2(y - this.y, x - this.x) - this.getRotation() / 180 * Math.PI;
		double modulus = Math.sqrt(dx * dx + dy * dy);
		x = modulus * Math.cos(angle) + this.x;
		y = modulus * Math.sin(angle) + this.y;
		return new double[] { x, y };
	}
}