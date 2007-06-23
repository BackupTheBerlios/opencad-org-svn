package org.opencad.ui.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Rectangle;

public class DeletePrimitiveState extends GLEditorState implements
		MouseListener {

	Cursor cursor;

	public DeletePrimitiveState(GLEditor glEditor) {
		super(glEditor);
		cursor = new Cursor(glEditor.glCanvas.getDisplay(),
				SWT.CURSOR_CROSS);
		glEditor.glCanvas.setCursor(cursor);
	}

	public void mouseDoubleClick(MouseEvent e) {
	}

	@Override
	public MouseListener getMouseListener() {
		return this;
	}

	public void mouseDown(MouseEvent e) {
		Rectangle size = glEditor.getCanvasClientArea();
		double px2gl = glEditor.px2gl(size);
		double glx = (e.x - (double) size.width / 2) * px2gl
				+ glEditor.getLeftAnchor();
		double gly = ((double) size.height / 2 - e.y) * px2gl
				+ glEditor.getTopAnchor();
		glEditor.getModel().informHoverables(glx, gly);
		Hoverable selection = getGlEditor().getModel().trapHoverable(
				glx, gly);
		if (selection != null) {
			if (selection instanceof Deletable) {
				Deletable deletable = (Deletable) selection;
				deletable.delete(glEditor.getModel());
			}
		}
		glEditor.doRefresh();
	}

	public void mouseUp(MouseEvent e) {
		glEditor.glCanvas.setCursor(null);
		cursor.dispose();
		terminate();
	}
}