package org.opencad.ui.editor;



public interface Selectable {

	public void setSelected(boolean selected);
	
	public GLEditorState getSelectionState(GLEditor editor);

	public boolean isSelected();
}
