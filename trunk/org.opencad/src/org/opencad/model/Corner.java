/**
 * 
 */
package org.opencad.model;

import org.eclipse.opengl.GL;

/**
 * @author George
 */
public class Corner extends Model {
	double x, y;

	public double getX() {
		return this.x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return this.y;
	}
	public void setY(double y) {
		this.y = y;
	}
	/**
	 * @param x
	 * @param y
	 */
	public Corner(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opencad.model.Model#drawModel()
	 */
	public void drawModel() {
		// TODO Auto-generated method stub
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opencad.model.Model#drawSchematic()
	 */
	public void drawSchematic() {
		GL.glColor3d(0d, 1d, 0d);
		GL.glPushMatrix();
		GL.glTranslated(x, y, 0);
		{
			GL.glBegin(GL.GL_QUADS);
			{
				GL.glVertex2d(-0.1d, +0.1d);
				GL.glVertex2d(+0.1d, +0.1d);
				GL.glVertex2d(+0.1d, -0.1d);
				GL.glVertex2d(-0.1d, -0.1d);
			}
			GL.glEnd();
		}
		GL.glPopMatrix();
	}
}
