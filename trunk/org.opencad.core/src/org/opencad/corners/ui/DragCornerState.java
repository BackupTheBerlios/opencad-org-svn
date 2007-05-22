package org.opencad.corners.ui;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Rectangle;
import org.opencad.corners.modelling.Corner;
import org.opencad.ui.editors.GLEditor;
import org.opencad.ui.editors.state.GLEditorState;

public class DragCornerState extends GLEditorState implements
		MouseMoveListener, MouseListener {

	Corner corner;

	private int startX;

	private int startY;

	private boolean started = false;

	public final Corner getCorner() {
		return corner;
	}

	public final void setCorner(Corner corner) {
		this.corner = corner;
	}

	public DragCornerState(GLEditor glEditor, Corner corner) {
		super(glEditor);
		this.corner = corner;
	}

	public MouseMoveListener getMouseMoveListener() {
		return this;
	}

	public MouseListener getMouseListener() {
		return this;
	}

	public void mouseMove(MouseEvent e) {
		glEditor.setDirty(true);
		if (started) {
			Rectangle size = glEditor.getCanvasClientArea();
			double px2gl = glEditor.px2gl(size);

			corner.setX(corner.getX() + (double) (e.x - startX) * px2gl);
			corner.setY(corner.getY() - (double) (e.y - startY) * px2gl);
			glEditor.doRefresh();
		}
		startX = e.x;
		startY = e.y;
		started = true;
	}

	public void mouseDoubleClick(MouseEvent e) {
	}

	public void mouseDown(MouseEvent e) {
	}

	public void mouseUp(MouseEvent e) {
		started = false;
		terminate();
	}

}
