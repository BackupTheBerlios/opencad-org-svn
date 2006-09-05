package org.opencad.corners.ui;

import org.eclipse.jface.action.IAction;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Rectangle;
import org.opencad.corners.modelling.Corner;
import org.opencad.ui.editors.GLEditor;
import org.opencad.ui.editors.state.GLEditorState;

public class AddCornerState extends GLEditorState implements MouseMoveListener,
    MouseListener {

  private IAction action;

  private Corner corner;

  public void setActionEnabled(boolean enabled) {
    action.setEnabled(enabled);
  }

  public AddCornerState(GLEditor glEditor) {
    super(glEditor);
  }

  public MouseListener getMouseListener() {
    return this;
  }

  public MouseMoveListener getMouseMoveListener() {
    return this;
  }

  public AddCornerState() {
    this(null);
  }

  public void setAction(IAction action) {
    this.action = action;
  }

  public void setCorner(Corner corner) {
    this.corner = corner;
  }

  public final Corner getCorner() {
    return corner;
  }

  public void mouseMove(MouseEvent e) {
    Rectangle size = glEditor.getCanvasClientArea();
    double px2gl = glEditor.px2gl(size);
    double x = (e.x - (double) size.width / 2) * px2gl
        + glEditor.getLeftAnchor();
    double y = -(e.y - (double) size.height / 2) * px2gl
        + glEditor.getTopAnchor();
    corner.setX(x);
    corner.setY(y);
    glEditor.doRefresh();
  }

  public void mouseDoubleClick(MouseEvent e) {
  }

  public void mouseDown(MouseEvent e) {
    terminate();
    notifyEditor();
    setActionEnabled(true);
  }

  public void mouseUp(MouseEvent e) {
  }

}
