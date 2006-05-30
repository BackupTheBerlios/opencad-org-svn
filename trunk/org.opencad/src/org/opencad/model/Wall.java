/**
 * 
 */
package org.opencad.model;

import org.eclipse.opengl.GL;

/**
 * @author George
 */
public class Wall extends Model {
	Corner alpha, omega;

	public Corner getAlpha() {
		return this.alpha;
	}
	public void setAlpha(Corner alpha) {
		this.alpha = alpha;
	}
	public Corner getOmega() {
		return this.omega;
	}
	public void setOmega(Corner omega) {
		this.omega = omega;
	}
	/**
	 * @param alpha
	 * @param omega
	 */
	public Wall(Corner alpha, Corner omega) {
		super();
		this.alpha = alpha;
		this.omega = omega;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opencad.model.Model#drawModel()
	 */
	@Override
	void drawModel() {
		GL.glPushMatrix();
		GL.glTranslated(alpha.getX(), alpha.getY(), 0);
		GL.glRotated(getAngle(), 0d, 0d, 1d);
		{
			GL.glBegin(GL.GL_QUADS);
			{
				double length = getLength();
				GL.glVertex3d(0d, 0d, 0d);
				GL.glVertex3d(0d, 0d, Constants.FLOOR_HEIGHT);
				GL.glVertex3d(length, 0d, Constants.FLOOR_HEIGHT);
				GL.glVertex3d(length, 0d, 0d);
			}
			GL.glEnd();
		}
		GL.glPopMatrix();
	}
	public double getHeight() {
		return omega.getY() - alpha.getY();
	}
	public double getWidth() {
		return omega.getX() - alpha.getX();
	}
	public double getAngle() {
		return Math.atan2(getHeight(), getWidth()) * 180 / Math.PI;
	}
	public double getLength() {
		return Math.hypot(getWidth(), getHeight());
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opencad.model.Model#drawSchematic()
	 */
	@Override
	void drawSchematic() {
		GL.glPushMatrix();
		GL.glTranslated(alpha.getX(), alpha.getY(), 0);
		GL.glRotated(getAngle(), 0d, 0d, 1d);
		{
			GL.glBegin(GL.GL_QUADS);
			{
				double length = getLength();
				GL.glVertex2d(-1.0d, +1.0d);
				GL.glVertex2d(length - 1.0d, +1.0d);
				GL.glVertex2d(+1.0d, -1.0d);
				GL.glVertex2d(length - 1.0d, -1.0d);
			}
			GL.glEnd();
		}
		GL.glPopMatrix();
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opencad.model.Model#initGL()
	 */
	@Override
	void initGL() {
		// TODO Auto-generated method stub
	}
}
