package org.opencad.ui.editor;

public interface Outlineable {
	boolean hasChildren();

	Object[] getChildren();

	String getText();
}
