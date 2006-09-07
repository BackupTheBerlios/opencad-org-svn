package org.opencad.walls.rendering;

import org.eclipse.opengl.GL;
import org.opencad.rendering.EditorRenderable;
import org.opencad.walls.modelling.Wall;

public class WallEditorRenderer implements EditorRenderable {

	private static final long serialVersionUID = 9135395816936424298L;

	Wall wall;

	public WallEditorRenderer(Wall wall) {
		this.wall = wall;
	}

	public void glRender() {
		GL.glColor3d(0d, 0d, 0d);
		GL.glBegin(GL.GL_LINES);
		{
			GL.glVertex2d(wall.getStartingCorner().getX(), wall
					.getStartingCorner().getY());
			GL.glVertex2d(wall.getEndingCorner().getX(), wall.getEndingCorner()
					.getY());
		}
		GL.glEnd();
	}

}
