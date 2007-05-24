package org.opencad.walls;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.opencad.corners.PickupCornerState;
import org.opencad.ui.editors.GLEditor;

public class AddWallActionDelegate implements IEditorActionDelegate {

	GLEditor editor;

	PickupCornerState pickupStartingCornerState = new PickupCornerState(null,
			null, PickupCornerState.CornerType.STARTING);

	PickupCornerState pickupEndingCornerState = new PickupCornerState(null,
			null, PickupCornerState.CornerType.ENDING);

	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		if (targetEditor instanceof GLEditor) {
			editor = (GLEditor) targetEditor;
			pickupStartingCornerState.setGlEditor(editor);
			pickupStartingCornerState.setAction(action);
			pickupEndingCornerState.setAction(action);
			pickupEndingCornerState.setGlEditor(editor);
			action.setEnabled(true);
		} else {
			action.setEnabled(false);
		}
	}

	public void run(IAction action) {
		Wall wall = new Wall(null, null);
		pickupStartingCornerState.setWall(wall);
		pickupEndingCornerState.setWall(wall);
		pickupEndingCornerState.freshen();
		pickupStartingCornerState.freshen();
		action.setEnabled(false);
	}

	public void selectionChanged(IAction action, ISelection selection) {
	}
}