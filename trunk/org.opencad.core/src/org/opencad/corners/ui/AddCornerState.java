package org.opencad.corners.ui;

import org.eclipse.jface.action.IAction;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Rectangle;
import org.opencad.corners.modelling.Corner;
import org.opencad.ui.editors.GLEditor;
import org.opencad.ui.editors.state.GLEditorState;

class AddCornerMouseAdapter extends MouseAdapter {

  AddCornerState state;

  @Override
  public void mouseDown(MouseEvent e) {
    state.terminate();
    state.notifyEditor();
    state.setActionEnabled(true);
  }

  public AddCornerMouseAdapter(AddCornerState state) {
    this.state = state;
  }

}

class AddCornerMouseMoveListener implements MouseMoveListener {

  AddCornerState state;

  public AddCornerMouseMoveListener(AddCornerState state) {
    this.state = state;
  }

  public void mouseMove(MouseEvent e) {
    Rectangle size = state.getGlEditor().getCanvasClientArea();
    double px2gl = state.getGlEditor().px2gl(size);
    double x = (e.x  - (double) size.width / 2) * px2gl + state.getGlEditor().getLeftAnchor();
    double y = - (e.y - (double) size.height / 2) * px2gl + state.getGlEditor().getTopAnchor();
    state.getCorner().setX(x);
    state.getCorner().setY(y);
    state.getGlEditor().doRefresh();
  }

}

public class AddCornerState extends GLEditorState {

  private MouseListener mouseListener;

  private IAction action;

  private Corner corner;

  private AddCornerMouseMoveListener mouseMoveListener;

  public void setActionEnabled(boolean enabled) {
    action.setEnabled(enabled);
  }

  public AddCornerState(GLEditor glEditor) {
    super(glEditor);
    mouseListener = new AddCornerMouseAdapter(this);
    mouseMoveListener = new AddCornerMouseMoveListener(this);
  }

  public MouseListener getMouseListener() {
    return mouseListener;
  }

  public MouseMoveListener getMouseMoveListener() {
    return mouseMoveListener;
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

}
