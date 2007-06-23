package org.opencad.modelling.walls;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.opencad.modelling.corners.PickupCornerState;
import org.opencad.ui.editor.GLEditor;

public class AddWallActionDelegate implements IEditorActionDelegate {

	GLEditor editor;

	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		if (targetEditor instanceof GLEditor) {
			editor = (GLEditor) targetEditor;
			action.setEnabled(true);
		} else {
			action.setEnabled(false);
		}
	}

	public void run(IAction action) {
		Wall wall = new Wall(null, null);
		new PickupCornerState(editor, wall, action,
				PickupCornerState.CornerType.ENDING).freshen();
		new PickupCornerState(editor, wall, action,
				PickupCornerState.CornerType.STARTING).freshen();
		action.setEnabled(false);
	}

	public void selectionChanged(IAction action, ISelection selection) {
	}
}
