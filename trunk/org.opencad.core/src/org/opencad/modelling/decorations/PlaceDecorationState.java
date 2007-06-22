package org.opencad.modelling.decorations;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Rectangle;
import org.opencad.ui.editor.GLEditor;
import org.opencad.ui.editor.GLEditorState;
import org.opencad.ui.editor.Hoverable;

public class PlaceDecorationState extends GLEditorState implements MouseListener, MouseMoveListener {

	Decoration decoration;

	public PlaceDecorationState(GLEditor glEditor, Decoration decoration) {
		super(glEditor);
		glEditor.getModel().addPrimitive(decoration);
		this.decoration = decoration;
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
		glEditor.setDirty(true);
		terminate();
	}

	public void mouseUp(MouseEvent e) {
	}

	public void mouseMove(MouseEvent e) {
		Rectangle size = glEditor.getCanvasClientArea();
		double px2gl = glEditor.px2gl(size);
		double glx = (e.x - (double) size.width / 2) * px2gl + glEditor.getLeftAnchor();
		double gly = ((double) size.height / 2 - e.y) * px2gl + glEditor.getTopAnchor();
		glEditor.getModel().informHoverables(glx, gly);
		Hoverable selection = getGlEditor().getModel().trapHoverable(glx, gly, decoration);
//		if (selection == null) {
//		}
		decoration.setX(glx);
		decoration.setY(gly);
		glEditor.doRefresh();
	}
}