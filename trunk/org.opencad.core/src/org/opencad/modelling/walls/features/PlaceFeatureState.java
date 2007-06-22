package org.opencad.modelling.walls.features;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Rectangle;
import org.opencad.modelling.walls.Wall;
import org.opencad.ui.editor.GLEditor;
import org.opencad.ui.editor.GLEditorState;
import org.opencad.ui.editor.Hoverable;

public class PlaceFeatureState extends GLEditorState implements MouseListener, MouseMoveListener {

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
	}

	public void mouseUp(MouseEvent e) {
		if (wall != null) {
			glEditor.setDirty(true);
		}
		terminate();
	}

	Wall wall = null;

	boolean started = false;

	double doff;

	public void mouseMove(MouseEvent e) {
		Rectangle size = glEditor.getCanvasClientArea();
		double px2gl = glEditor.px2gl(size);
		double glx = (e.x - (double) size.width / 2) * px2gl + glEditor.getLeftAnchor();
		double gly = ((double) size.height / 2 - e.y) * px2gl + glEditor.getTopAnchor();
		boolean reset = false;
		glEditor.getModel().informHoverables(glx, gly);
		Hoverable selection = getGlEditor().getModel().trapHoverable(glx, gly, feature);
		if (selection == null) {
			selection = wall;
		}
		if (selection instanceof Wall) {
			if (wall != selection) {
				reset = true;
			}
			if (wall != null) {
				feature.delete(glEditor.getModel());
			}
			wall = (Wall) selection;
			if (wall != null) {
				double[] prj = wall.getProjectionOf(glx, gly);
				if (prj != null) {
					double off = prj[2] * wall.getLength();
					if (!started) {
						doff = off;
						started = true;
						return;
					}
					double noff = feature.getStartOffset() + off - doff;
					if (reset) {
						noff = off - feature.getWidth() / 2;
					}
					if (noff > 0) {
						feature.setStartOffset(noff);
					} else {
						off = doff;
					}
					doff = off;
					wall.addFeature(feature);
					glEditor.getModel().addPrimitive(feature);
				}
			}
		}
		glEditor.doRefresh();
	}
}
