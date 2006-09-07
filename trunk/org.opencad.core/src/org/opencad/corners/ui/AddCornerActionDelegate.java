package org.opencad.corners.ui;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.opencad.corners.modelling.Corner;
import org.opencad.ui.editors.GLEditor;

public class AddCornerActionDelegate implements IEditorActionDelegate {

  GLEditor editor;

  AddCornerState state = new AddCornerState();

  public void setActiveEditor(IAction action, IEditorPart targetEditor) {
    if (targetEditor instanceof GLEditor) {
      editor = (GLEditor) targetEditor;
      state.setGlEditor(editor);
      state.setAction(action);
      action.setEnabled(true);
    } else {
      action.setEnabled(false);
    }
  }

  public void run(IAction action) {
    state.freshen();
    Corner corner = new Corner(0d, 0d);
    editor.getModel().addPrimitive(corner);
    editor.setDirty(true);
    state.setCorner(corner);
    action.setEnabled(false);
    editor.stateChanged(state);
  }

  public void selectionChanged(IAction action, ISelection selection) {
  }

}
