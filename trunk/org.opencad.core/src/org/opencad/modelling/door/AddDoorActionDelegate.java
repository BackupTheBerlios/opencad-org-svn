package org.opencad.modelling.door;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.opencad.ui.editor.GLEditor;

public class AddDoorActionDelegate implements IEditorActionDelegate {

	private GLEditor editor;

	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		if (targetEditor instanceof GLEditor) {
			editor = (GLEditor) targetEditor;
			action.setEnabled(true);
		} else {
			action.setEnabled(false);
		}
	}

	public void run(IAction action) {
		new PlaceDoorState(editor).freshen();
	}

	public void selectionChanged(IAction action, ISelection selection) {
	}
}