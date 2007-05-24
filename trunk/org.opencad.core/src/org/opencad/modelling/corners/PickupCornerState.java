package org.opencad.modelling.corners;

import org.eclipse.jface.action.IAction;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Rectangle;
import org.opencad.modelling.walls.Wall;
import org.opencad.ui.editor.GLEditor;
import org.opencad.ui.editor.GLEditorState;
import org.opencad.ui.editor.Hoverable;

public class PickupCornerState extends GLEditorState implements MouseListener,
		MouseMoveListener {

	public enum CornerType implements CornerSetter {
		STARTING {
			public void changeCornerOf(Wall wall, Corner corner) {
				wall.setStartingCorner(corner);
			}
		},
		ENDING {
			public void changeCornerOf(Wall wall, Corner corner) {
				wall.setEndingCorner(corner);
			}
		}
	}

	Wall wall;

	CornerType cornerType;

	private IAction action;

	Corner babyCorner = null;

	Corner getBabyCorner(double x, double y) {
		if (babyCorner == null) {
			babyCorner = new Corner(x, y);
			glEditor.getModel().addPrimitive(babyCorner);
		} else {
			babyCorner.setX(x);
			babyCorner.setY(y);
		}
		return babyCorner;
	}

	Corner giveBirth() {
		Corner corner = babyCorner;
		babyCorner = null;
		return corner;
	}

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
			Hoverable hoverable = glEditor.getModel().trapHoverable(glx, gly,
					babyCorner);
			if (hoverable == null) {
				hoverable = babyCorner;
			}
			if (hoverable != null && hoverable instanceof Corner) {
				if (hoverable != babyCorner) {
					glEditor.getModel().removePrimitive(babyCorner);
				}
				giveBirth();
				switch (cornerType) {
				case STARTING:
					wall.setStartingCorner((Corner) hoverable);
					glEditor.getModel().addPrimitive(wall);
					terminate();
					break;
				case ENDING:
					if (hoverable != wall.getStartingCorner()) {
						wall.setEndingCorner((Corner) hoverable);
						glEditor.setDirty(true);
						this.action.setEnabled(true);
						terminate();
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
		cornerType.changeCornerOf(wall, getBabyCorner(glx, gly));
		glEditor.getModel().informHoverables(glx, gly);
		glEditor.doRefresh();
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
