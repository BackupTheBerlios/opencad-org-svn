package org.opencad.modelling.decorations;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.opencad.ui.editor.GLEditor;

public class AddDecorationActionDelegate implements
		IEditorActionDelegate {

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
		new PlaceDecorationState(editor, action2feature(action), true)
				.freshen();
	}

	private Decoration action2feature(IAction action) {
		if (action.getId().equals("org.opencad.core.addSofaAction")) {
			return new Sofa();
		} else if (action.getId().equals(
				"org.opencad.core.addTableAction")) {
			return new Table();
		}
		return null;
	}

	public void selectionChanged(IAction action, ISelection selection) {
	}
}