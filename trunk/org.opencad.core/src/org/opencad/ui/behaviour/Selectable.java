package org.opencad.ui.behaviour;

import org.opencad.ui.editors.GLEditor;

public interface Selectable {

	public void setSelected(boolean selected, GLEditor editor);

	public boolean isSelected();
}
