package org.opencad.ui.editor;

public interface Outlineable extends ImageProvider {
	boolean hasChildren();

	Object[] getChildren();

	String getText();
}
