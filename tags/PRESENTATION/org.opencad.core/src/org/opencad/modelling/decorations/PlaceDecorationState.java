package org.opencad.modelling.decorations;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Rectangle;
import org.opencad.ui.editor.GLEditor;
import org.opencad.ui.editor.GLEditorState;

public class PlaceDecorationState extends GLEditorState implements
		MouseListener, MouseMoveListener {

	Decoration decoration;

	public PlaceDecorationState(GLEditor glEditor,
			Decoration decoration, boolean delay) {
		super(glEditor);
		glEditor.getModel().addPrimitive(decoration);
		this.decoration = decoration;
		flag = !delay;
	}

	@Override
	public MouseListener getMouseListener() {
		return this;
	}

	@Override
	public MouseMoveListener getMouseMoveListener() {
		return this;
	}

	public void mouseDoubleClick(MouseEvent e) {

	}

	public void mouseDown(MouseEvent e) {
		flag = true;
	}

	public void mouseUp(MouseEvent e) {
		glEditor.setDirty(true);
		terminate();
	}

	double xoff, yoff;

	boolean started = false;

	boolean flag = false;

	public void mouseMove(MouseEvent e) {
		if (!flag)
			return;
		Rectangle size = glEditor.getCanvasClientArea();
		double px2gl = glEditor.px2gl(size);
		double glx = (e.x - (double) size.width / 2) * px2gl
				+ glEditor.getLeftAnchor();
		double gly = ((double) size.height / 2 - e.y) * px2gl
				+ glEditor.getTopAnchor();
		if (!started) {
			xoff = glx;
			yoff = gly;
			started = decoration.isHoverCoordinates(glx, gly);
			return;
		}
		if ((e.stateMask & SWT.SHIFT) > 0) {
			decoration.setRotation(Math.atan2(gly - decoration.getY(),
					glx - decoration.getX())
					* 180 / Math.PI);
		} else {
			decoration.setX(decoration.getX() + glx - xoff);
			decoration.setY(decoration.getY() + gly - yoff);
			xoff = glx;
			yoff = gly;
		}
		glEditor.doRefresh();
	}
}