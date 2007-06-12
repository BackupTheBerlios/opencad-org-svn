package org.opencad.modelling.window;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.opencad.ui.editor.GLEditor;

public class AddWindowActionDelegate implements IEditorActionDelegate {

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
		new PlaceWindowState(editor).freshen();
	}

	public void selectionChanged(IAction action, ISelection selection) {
	}
}