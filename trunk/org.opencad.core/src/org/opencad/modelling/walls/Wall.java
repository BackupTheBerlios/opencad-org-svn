package org.opencad.modelling.walls;

import org.eclipse.opengl.GL;
import org.opencad.modelling.Primitive;
import org.opencad.modelling.PrimitiveTypeRegister;
import org.opencad.modelling.corners.Corner;

public class Wall extends Primitive {

	private static final long serialVersionUID = -8662848078904155699L;

	static {
		PrimitiveTypeRegister.registerPrimitiveType(Wall.class);
	}

	Corner startingCorner, endingCorner;

	public final Corner getEndingCorner() {
		return endingCorner;
	}

	public final void setEndingCorner(Corner endingCorner) {
		this.endingCorner = endingCorner;
	}

	public final Corner getStartingCorner() {
		return startingCorner;
	}

	public final void setStartingCorner(Corner startingCorner) {
		this.startingCorner = startingCorner;
	}

	public Wall(Corner startingCorner, Corner endingCorner) {
		this.startingCorner = startingCorner;
		this.endingCorner = endingCorner;
	}

	public String toString() {
		return String.format("%s=%s", startingCorner, endingCorner);
	}

	public void editorRender() {
		if (getStartingCorner() != null && getEndingCorner() != null) {
			GL.glColor3d(0d, 0d, 0d);
			GL.glBegin(GL.GL_LINES);
			{
				GL.glVertex2d(getStartingCorner().getX(), getStartingCorner()
						.getY());
				GL.glVertex2d(getEndingCorner().getX(), getEndingCorner()
						.getY());
			}
			GL.glEnd();
		}
	}
	
	public static final double height = 3.5d;

	public void realRender() {
		if (getStartingCorner() != null && getEndingCorner() != null) {
			GL.glColor3d(0d, 0d, 0d);
			GL.glBegin(GL.GL_LINES);
			{
				GL.glVertex3d(getStartingCorner().getX(), getStartingCorner()
						.getY(), 0);
				GL.glVertex3d(getEndingCorner().getX(), getEndingCorner()
						.getY(), 0);
				GL.glVertex3d(getStartingCorner().getX(), getStartingCorner()
						.getY(), height);
				GL.glVertex3d(getEndingCorner().getX(), getEndingCorner()
						.getY(), height);
			}
			GL.glEnd();
		}
	}
}