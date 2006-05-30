/**
 * 
 */
package org.opencad.scene;

import java.util.Iterator;
import java.util.LinkedList;
import org.eclipse.opengl.GL;
import org.eclipse.swt.widgets.Composite;
import org.opencad.model.Constants;
import org.opencad.model.Corner;
import org.opencad.model.Model;
import org.opencad.model.Wall;

/**
 * @author George
 */
public class ModellingScene extends GLScene {
	private static final int STATE_DEFAULT = 0;
	private static final int STATE_DRAG_CORNER = 1;
	public static final int STATE_PICK_FIRST_CORNER = 2;
	private static final int STATE_PICK_SECOND_CORNER = 3;
	LinkedList models;
	public int state = STATE_DEFAULT;
	private Corner draggedCorner;
	private Corner firstCorner;
	static ModellingScene instance;

	protected void drawCursor() {
		GL.glColor3d(0.8d, 0.8d, 0.8d);
		GL.glBegin(GL.GL_LINES);
		{
			GL.glVertex2d(x - 0.1, y);
			GL.glVertex2d(x + 0.1, y);
			GL.glVertex2d(x, y - 0.1);
			GL.glVertex2d(x, y + 0.1);
		}
		GL.glEnd();
	}
	protected void drawGrid() {
		final int lines = 10;
		final double courtSize = Constants.SCENE_SIZE;
		GL.glColor3d(0.1d, 0.2d, 0.4d);
		GL.glBegin(GL.GL_LINES);
		{
			for (int i = 0; i <= lines; i++) {
				GL.glVertex2d((double) i / (double) lines * courtSize, 0);
				GL.glVertex2d((double) i / (double) lines * courtSize, courtSize);
				GL.glVertex2d(0, (double) i / (double) lines * courtSize);
				GL.glVertex2d(courtSize, (double) i / (double) lines * courtSize);
			}
		}
		GL.glEnd();
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opencad.scene.GLScene#drawScene()
	 */
	protected void drawScene() {
		super.drawScene();
		drawGrid();
		drawCursor();
		Iterator i = iterator();
		while (i.hasNext()) {
			((Model) i.next()).drawSchematic();
		}
	}
	public static ModellingScene getInstance(Composite parent) {
		if (instance == null) {
			instance = new ModellingScene(parent);
		}
		return instance;
	}
	/**
	 * @param parent
	 * @param projectionStyle
	 */
	protected ModellingScene(Composite parent) {
		super(parent, GLScene.ORTHO3D);
		models = new LinkedList();
	}
	/**
	 * @param arg0
	 * @return
	 * @see java.util.LinkedList#add(java.lang.Object)
	 */
	public boolean add(Model model) {
		return this.models.add(model);
	}
	/**
	 * @return
	 */
	public Iterator iterator() {
		return this.models.iterator();
	}
	/**
	 * @param arg0
	 * @return
	 * @see java.util.LinkedList#remove(java.lang.Object)
	 */
	public boolean remove(Model model) {
		return this.models.remove(model);
	}
	public void drag() {
		switch (state) {
			case STATE_DRAG_CORNER:
				draggedCorner.setX(x);
				draggedCorner.setY(y);
			break;
		}
	}
	public void push() {
		switch (state) {
			case STATE_DEFAULT: {
				Iterator i = iterator();
				while (i.hasNext()) {
					Model model = (Model) i.next();
					if (model instanceof Corner) {
						Corner corner = (Corner) model;
						double c_x = corner.getX();
						double c_y = corner.getY();
						if (c_x > x - Constants.DRAG_TOLERANCE && c_x < x + Constants.DRAG_TOLERANCE
						    && c_y > y - Constants.DRAG_TOLERANCE && c_y < y + Constants.DRAG_TOLERANCE) {
							draggedCorner = corner;
							state = STATE_DRAG_CORNER;
						}
					}
				}
			}
			break;
			case STATE_PICK_FIRST_CORNER: {
				Iterator i = iterator();
				while (i.hasNext()) {
					Model model = (Model) i.next();
					if (model instanceof Corner) {
						Corner corner = (Corner) model;
						double c_x = corner.getX();
						double c_y = corner.getY();
						if (c_x > x - Constants.DRAG_TOLERANCE && c_x < x + Constants.DRAG_TOLERANCE
						    && c_y > y - Constants.DRAG_TOLERANCE && c_y < y + Constants.DRAG_TOLERANCE) {
							firstCorner = corner;
							state = STATE_PICK_SECOND_CORNER;
						}
					}
				}
			}
			break;
			case STATE_PICK_SECOND_CORNER: {
				Iterator i = iterator();
				while (i.hasNext()) {
					Model model = (Model) i.next();
					if (model instanceof Corner) {
						Corner corner = (Corner) model;
						double c_x = corner.getX();
						double c_y = corner.getY();
						if (c_x > x - Constants.DRAG_TOLERANCE && c_x < x + Constants.DRAG_TOLERANCE
						    && c_y > y - Constants.DRAG_TOLERANCE && c_y < y + Constants.DRAG_TOLERANCE) {
							add(new Wall(firstCorner, corner));
							state = STATE_DEFAULT;
						}
					}
				}
			}
			break;
		}
	}
	public void pop() {
		switch (state) {
			case STATE_DRAG_CORNER:
				state = STATE_DEFAULT;
			break;
		}
	}
}
