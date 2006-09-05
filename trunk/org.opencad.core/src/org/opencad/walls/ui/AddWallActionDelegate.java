package org.opencad.walls.ui;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.opencad.corners.modelling.Corner;
import org.opencad.ui.editors.GLEditor;
import org.opencad.walls.modelling.Wall;

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
    Corner starting = new Corner(1d, 1d);
    Corner ending = new Corner(4d, 4d);
    Wall wall = new Wall(starting, ending);
    editor.getModel().getPrimitives().add(wall);
    editor.getModel().setDirty(true);
  }

  public void selectionChanged(IAction action, ISelection selection) {
  }
}
