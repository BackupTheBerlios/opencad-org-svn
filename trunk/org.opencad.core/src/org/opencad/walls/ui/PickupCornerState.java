package org.opencad.walls.ui;

import org.eclipse.jface.action.IAction;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Rectangle;
import org.opencad.corners.modelling.Corner;
import org.opencad.ui.behaviour.Hoverable;
import org.opencad.ui.editors.GLEditor;
import org.opencad.ui.editors.state.GLEditorState;
import org.opencad.walls.modelling.Wall;

public class PickupCornerState extends GLEditorState implements MouseListener,
		MouseMoveListener {

	public enum CornerType {
		STARTING, ENDING
	}

	Wall wall;

	CornerType cornerType;

	private IAction action;

	public PickupCornerState(GLEditor glEditor, Wall wall, CornerType cornerType) {
		super(glEditor);
		this.wall = wall;
		this.cornerType = cornerType;
	}

	public void mouseDoubleClick(MouseEvent e) {
	}

	public void mouseDown(MouseEvent e) {
		Rectangle size = glEditor.getCanvasClientArea();
		double px2gl = glEditor.px2gl(size);
		double glx = (e.x - (double) size.width / 2) * px2gl
				+ glEditor.getLeftAnchor();
		double gly = ((double) size.height / 2 - e.y) * px2gl
				+ glEditor.getTopAnchor();
		glEditor.getModel().informHoverables(glx, gly);
		if (e.button == 1) {
			Hoverable hoverable = glEditor.getModel().trapHoverable(glx, gly);
			if (hoverable != null && hoverable instanceof Corner) {
				switch (cornerType) {
				case STARTING:
					wall.setStartingCorner((Corner) hoverable);
					terminate();
					notifyEditor();
					break;
				case ENDING:
					if (hoverable != wall.getStartingCorner()) {
						wall.setEndingCorner((Corner) hoverable);
						glEditor.getModel().addPrimitive(wall);
						glEditor.setDirty(true);
						this.action.setEnabled(true);
						terminate();
						notifyEditor();
						glEditor.doRefresh();
					}
					break;
				}
			}
		}
	}

	public void mouseUp(MouseEvent e) {
	}

	public void mouseMove(MouseEvent e) {
		Rectangle size = glEditor.getCanvasClientArea();
		double px2gl = glEditor.px2gl(size);
		double glx = (e.x - (double) size.width / 2) * px2gl
				+ glEditor.getLeftAnchor();
		double gly = ((double) size.height / 2 - e.y) * px2gl
				+ glEditor.getTopAnchor();
		if (glEditor.getModel().informHoverables(glx, gly)) {
			glEditor.doRefresh();
		}
	}

	public final Wall getWall() {
		return wall;
	}

	public final void setWall(Wall wall) {
		this.wall = wall;
	}

	public void setAction(IAction action) {
		this.action = action;
	}

	public MouseListener getMouseListener() {
		return this;
	}

	public MouseMoveListener getMouseMoveListener() {
		return this;
	}
}
