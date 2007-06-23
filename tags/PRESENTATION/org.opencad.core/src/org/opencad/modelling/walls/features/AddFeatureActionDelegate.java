package org.opencad.modelling.walls.features;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.opencad.ui.editor.GLEditor;

public class AddFeatureActionDelegate implements IEditorActionDelegate {

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
		new PlaceFeatureState(editor, action2feature(action)).freshen();
	}

	private WallFeature action2feature(IAction action) {
		if (action.getId().equals("org.opencad.core.addDoorAction")) {
			return new Door();
		} else if (action.getId().equals(
				"org.opencad.core.addWindowAction")) {
			return new Window();
		}
		return null;
	}

	public void selectionChanged(IAction action, ISelection selection) {
	}
}