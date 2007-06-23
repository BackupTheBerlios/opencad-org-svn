package org.opencad.modelling;

import java.io.Serializable;

import org.opencad.ui.editor.EditorRenderable;
import org.opencad.ui.editor.Hoverable;
import org.opencad.ui.editor.RealRenderable;
import org.opencad.ui.editor.Selectable;

public abstract class Primitive implements Serializable,
		EditorRenderable, RealRenderable, Hoverable, Selectable {
	private boolean hover, selected;

	private boolean renderable = true;

	public boolean isRenderable() {
		return renderable;
	}

	public void setRenderable(boolean renderable) {
		this.renderable = renderable;
	}

	final public boolean isHover() {
		return hover;
	}

	final public boolean setHover(boolean hover) {
		boolean changed = this.hover != hover;
		this.hover = hover;
		return changed;
	}

	final public boolean isSelected() {
		return selected;
	}

	final public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
