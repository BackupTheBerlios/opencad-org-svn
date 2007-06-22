package org.opencad.ui.editor;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Rectangle;
import org.opencad.modelling.Primitive;
import org.opencad.modelling.walls.features.WallFeature;

public class DeletePrimitiveState extends GLEditorState implements MouseListener {

	public DeletePrimitiveState(GLEditor glEditor) {
		super(glEditor);
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
		double glx = (e.x - (double) size.width / 2) * px2gl + glEditor.getLeftAnchor();
		double gly = ((double) size.height / 2 - e.y) * px2gl + glEditor.getTopAnchor();
		glEditor.getModel().informHoverables(glx, gly);
		Hoverable selection = getGlEditor().getModel().trapHoverable(glx, gly);
		if (selection != null) {
			if (selection instanceof Deletable) {
				Deletable deletable = (Deletable) selection;
				deletable.delete(glEditor.getModel());
			}
		}
		glEditor.doRefresh();
	}

	public void mouseUp(MouseEvent e) {
		terminate();
	}
}