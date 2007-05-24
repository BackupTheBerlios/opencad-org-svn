package org.opencad.ui.editors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class NavigationState extends GLEditorState implements
		MouseMoveListener, Listener, KeyListener, MouseListener {

	@Override
	public KeyListener getKeyListener() {
		return this;
	}

	@Override
	public Listener getMouseWheelListener() {
		return this;
	}

	@Override
	public MouseListener getMouseListener() {
		return this;
	}

	@Override
	public MouseMoveListener getMouseMoveListener() {
		return this;
	}

	public NavigationState(GLEditor glEditor) {
		super(glEditor);
	}

	int startX, startY;

	boolean drag = false;

	public void mouseMove(MouseEvent e) {
		Rectangle size = glEditor.getCanvasClientArea();
		double px2gl = glEditor.px2gl(size);
		boolean refresh = false;
		double glx = (e.x - (double) size.width / 2) * px2gl
				+ glEditor.getLeftAnchor();
		double gly = ((double) size.height / 2 - e.y) * px2gl
				+ glEditor.getTopAnchor();

		refresh = refresh || glEditor.getModel().informHoverables(glx, gly);

		if (drag) {
			glEditor.setLeftAnchor(glEditor.getLeftAnchor()
					- (double) (e.x - startX) * px2gl);
			glEditor.setTopAnchor(glEditor.getTopAnchor()
					+ (double) (e.y - startY) * px2gl);
			startX = e.x;
			startY = e.y;
			refresh = true;
		}
		if (refresh) {
			glEditor.doRefresh();
		}
	}

	public void handleEvent(Event event) {
		double factor = (double) event.count / glEditor.getZoomSpeed();
		double newScale = glEditor.getScale() * (1 + factor);
		if (newScale > glEditor.getMaxScale()
				|| newScale < glEditor.getMinScale())
			return;
		Rectangle size = glEditor.getCanvasClientArea();
		double oldpx2gl = glEditor.px2gl(size);
		glEditor.setScale(newScale);
		double dpx2gl = glEditor.px2gl(size) - oldpx2gl;
		glEditor.setLeftAnchor(glEditor.getLeftAnchor()
				- (event.x - (double) size.width / 2) * dpx2gl);
		glEditor.setTopAnchor(glEditor.getTopAnchor()
				+ (event.y - (double) size.height / 2) * dpx2gl);
		glEditor.doRefresh();
	}

	public void keyPressed(KeyEvent e) {
		double nudge = glEditor.getPanSpeed()
				* glEditor.px2gl(glEditor.getCanvasClientArea());
		switch (e.keyCode) {
		case SWT.ARROW_LEFT:
			glEditor.setLeftAnchor(glEditor.getLeftAnchor() - nudge);
			break;
		case SWT.ARROW_RIGHT:
			glEditor.setLeftAnchor(glEditor.getLeftAnchor() + nudge);
			break;
		case SWT.ARROW_UP:
			glEditor.setTopAnchor(glEditor.getTopAnchor() + nudge);
			break;
		case SWT.ARROW_DOWN:
			glEditor.setTopAnchor(glEditor.getTopAnchor() - nudge);
			break;
		}
		glEditor.doRefresh();
	}

	public void keyReleased(KeyEvent e) {
	}

	public void mouseDoubleClick(MouseEvent e) {
	}

	public void mouseDown(MouseEvent e) {
		if (e.button == 1) {
			Rectangle size = glEditor.getCanvasClientArea();
			double px2gl = glEditor.px2gl(size);
			double glx = (e.x - (double) size.width / 2) * px2gl
					+ glEditor.getLeftAnchor();
			double gly = ((double) size.height / 2 - e.y) * px2gl
					+ glEditor.getTopAnchor();
			Hoverable hoverable = glEditor.getModel().trapHoverable(glx, gly);
			if (hoverable != null && hoverable instanceof Selectable) {
				glEditor.setSelection((Selectable) hoverable);
			} else {
				drag = true;
				startX = e.x;
				startY = e.y;
			}
		}
	}

	public void mouseUp(MouseEvent e) {
		drag = false;
	}
}
