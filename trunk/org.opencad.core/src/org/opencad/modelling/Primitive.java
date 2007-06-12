	package org.opencad.modelling;

import java.io.Serializable;

import org.opencad.ui.editor.EditorRenderable;
import org.opencad.ui.editor.Hoverable;
import org.opencad.ui.editor.RealRenderable;
import org.opencad.ui.editor.Selectable;

public abstract class Primitive implements Serializable, EditorRenderable,
		RealRenderable, Hoverable, Selectable {
}
