package org.opencad.modelling;

import java.io.Serializable;
import java.util.LinkedList;

import org.eclipse.opengl.GL;
import org.opencad.ui.editor.EditorRenderable;
import org.opencad.ui.editor.Hoverable;
import org.opencad.ui.editor.RealRenderable;
import org.opencad.ui.editor.RenderStage;
import org.opencad.ui.editor.Selectable;

public class Model implements EditorRenderable, RealRenderable, Serializable {

	private static final long serialVersionUID = -4303353063372058728L;

	private LinkedList<Primitive> primitives;

	private LinkedList<Hoverable> hoverables;

	private transient Selectable selection = null;

	public Selectable getSelection() {
		return selection;
	}

	public boolean setSelection(Selectable selection) {
		boolean changed = this.selection != selection;
		if (changed) {
			if (this.selection != null) {
				this.selection.setSelected(false);
			}
		}
		if (selection != null) {
			selection.setSelected(true);
		}
		this.selection = selection;
		return changed;
	}

	public void addPrimitive(Primitive o) {
		if (o instanceof Hoverable) {
			hoverables.add((Hoverable) o);
		}
		primitives.add(o);
	}

	public final LinkedList<Hoverable> getHoverables() {
		return hoverables;
	}

	public Model() {
		primitives = new LinkedList<Primitive>();
		hoverables = new LinkedList<Hoverable>();
	}

	public LinkedList<Primitive> getPrimitives() {
		return primitives;
	}

	public String toString() {
		String ret = "Model {\n";
		for (Primitive primitive : primitives) {
			ret += "  " + primitive + "\n";
		}
		return ret + "}";
	}

	public boolean removePrimitive(Primitive o) {
		if (o instanceof Hoverable) {
			hoverables.remove(o);
		}
		return primitives.remove(o);
	}

	public Hoverable trapHoverable(double x, double y, Hoverable... exceptions) {
		Hoverable selection = null;
		for (Hoverable hoverable : hoverables) {
			if (hoverable.isHoverCoordinates(x, y)) {
				boolean excepted = false;
				for (Hoverable exception : exceptions) {
					if (exception == hoverable) {
						excepted = true;
						break;
					}
				}
				if (!excepted
						&& (selection == null || selection.getZIndex() < hoverable
								.getZIndex())) {
					selection = hoverable;
				}
			}
		}
		return selection;
	}

	public boolean informHoverables(double x, double y) {
		boolean changed = false;
		for (Hoverable hoverable : hoverables) {
			if (hoverable.setHover(hoverable.isHoverCoordinates(x, y))) {
				changed = true;
			}
		}
		return changed;
	}

	public void editorRender() {
		for (Primitive primitive : primitives) {
			if (primitive.isRenderable()) {
				GL.glPushMatrix();
				{
					primitive.editorRender();
				}
				GL.glPopMatrix();
			}
		}
	}

	public void realRender(RenderStage stage) {
		for (Primitive primitive : primitives) {
			if (primitive.isRenderable()) {
				GL.glPushMatrix();
				{
					primitive.realRender(stage);
				}
				GL.glPopMatrix();
			}
		}
	}
}