package org.opencad.modelling.walls;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Rectangle;
import org.opencad.ui.editor.GLEditor;
import org.opencad.ui.editor.GLEditorState;
import org.opencad.ui.editor.Hoverable;

public class PlaceFeatureState extends GLEditorState implements MouseListener,
		MouseMoveListener {

	WallFeature feature;

	public PlaceFeatureState(GLEditor glEditor, WallFeature feature) {
		super(glEditor);
		this.feature = feature;
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
		if (wall != null) {
			glEditor.setDirty(true);
		}
		terminate();
	}

	public void mouseUp(MouseEvent e) {
	}

	Wall wall = null;

	public void mouseMove(MouseEvent e) {
		Rectangle size = glEditor.getCanvasClientArea();
		double px2gl = glEditor.px2gl(size);
		double glx = (e.x - (double) size.width / 2) * px2gl
				+ glEditor.getLeftAnchor();
		double gly = ((double) size.height / 2 - e.y) * px2gl
				+ glEditor.getTopAnchor();
		glEditor.getModel().informHoverables(glx, gly);
		Hoverable selection = getGlEditor().getModel().trapHoverable(glx, gly);
		if (selection == null) {
			selection = wall;
		}
		if (selection instanceof Wall) {
			if (wall != null) {
				wall.removeFeature(feature);
			}
			wall = (Wall) selection;
			if (wall != null) {
				double[] prj = wall.getProjectionOf(glx, gly);
				feature.setStartOffset(prj[2] * wall.getLength());
				wall.addFeature(feature);
			}
		}
		glEditor.doRefresh();
	}
}
