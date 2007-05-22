package org.opencad.ui.behaviour;

import org.opencad.ui.editors.GLEditor;
import org.opencad.ui.editors.state.GLEditorState;


public interface Selectable {

	public void setSelected(boolean selected);
	
	public GLEditorState getSelectionState(GLEditor editor);

	public boolean isSelected();
}
