package org.opencad.modelling.decorations;

import org.eclipse.opengl.GL;
import org.opencad.modelling.walls.Wall;
import org.opencad.ui.editor.RenderStage;

public class Sofa extends Decoration {

	private static final long serialVersionUID = 1L;

	private static final double armHeight = 0.7d;

	private static final double armWidth = 0.2d;

	private static final double seatHeight = 0.3d;

	private static final double pillowHeight = 0.2d;

	private static final double pillowSide = 0.5d;

	private static final int lengthInPillows = 4;

	private static final double seatWidth = lengthInPillows * pillowSide;

	private static final double length = pillowSide + pillowHeight;

	public String getImage() {
		return "icons/startof.gif";
	}

	public void editorRender() {
		if (isSelected()) {
			GL.glColor3d(0d, 0.5d, 1d);
		} else if (isHover()) {
			GL.glColor3d(1d, 0d, 0d);
		} else {
			GL.glColor3d(0d, 0d, 0d);
		}
		GL.glTranslated(x, y, 0);
		GL.glRotated(getRotation(), 0, 0, 1d);
		GL.glBegin(GL.GL_QUADS);
		{
			GL.glVertex2d(0, 0);
			GL.glVertex2d(armWidth, 0);
			GL.glVertex2d(armWidth, length);
			GL.glVertex2d(0, length);
		}
		GL.glEnd();
		GL.glTranslated(armWidth, 0, 0);
		for (int i = 0; i < lengthInPillows; i++) {
			GL.glBegin(GL.GL_QUADS);
			{
				GL.glVertex2d(0, 0);
				GL.glVertex2d(pillowSide, 0);
				GL.glVertex2d(pillowSide, pillowSide);
				GL.glVertex2d(0, pillowSide);

				GL.glVertex2d(0, pillowSide);
				GL.glVertex2d(pillowSide, pillowSide);
				GL.glVertex2d(pillowSide, length);
				GL.glVertex2d(0, length);
			}
			GL.glEnd();
			GL.glTranslated(pillowSide, 0, 0);
		}
		GL.glBegin(GL.GL_QUADS);
		{
			GL.glVertex2d(0, 0);
			GL.glVertex2d(armWidth, 0);
			GL.glVertex2d(armWidth, length);
			GL.glVertex2d(0, length);
		}
		GL.glEnd();
	}

	private void drawLump(double l, double w, double lump) {
		double patchSize = 0.06;
		int ld = (int) (l / patchSize);
		int wd = (int) (w / patchSize);
		double height = 0.1 * lump * l;
		for (int i = 0; i < ld; i++) {
			double x = (double) i / ld;
			double xp = (double) (i + 1) / ld;
			double f = (1 - x) * x * 4;
			double fp = (1 - xp) * xp * 4;
			GL.glBegin(GL.GL_QUAD_STRIP);
			{
				for (int j = 0; j <= wd; j++) {
					double y = (double) j / wd;
					double g = (1 - y) * y * 4;
					GL.glVertex3d(l * x, w * y, height * f * g);
					GL.glVertex3d(l * xp, w * y, height * fp * g);
				}
			}
			GL.glEnd();
		}
	}

	private void drawArm() {
		GL.glPushMatrix();
		{
			GL.glBegin(GL.GL_QUADS);
			{
				GL.glVertex3d(0, 0, 0);
				GL.glVertex3d(0, 0, armHeight);
				GL.glVertex3d(0, length, armHeight);
				GL.glVertex3d(0, length, 0);

				GL.glVertex3d(armWidth, 0, 0);
				GL.glVertex3d(armWidth, 0, armHeight);
				GL.glVertex3d(armWidth, length, armHeight);
				GL.glVertex3d(armWidth, length, 0);

				GL.glVertex3d(0, 0, 0);
				GL.glVertex3d(0, 0, armHeight);
				GL.glVertex3d(armWidth, 0, armHeight);
				GL.glVertex3d(armWidth, 0, 0);
			}
			GL.glEnd();
			GL.glTranslated(0, 0, armHeight);
			drawLump(armWidth, length, 2);
			GL.glTranslated(0, length, 0);
			GL.glRotated(-90d, 1d, 0d, 0d);
			drawLump(armWidth, length, 2);
		}
		GL.glPopMatrix();
	}

	private void drawPillow() {
		GL.glPushMatrix();
		{
			drawLump(pillowSide, pillowSide, 1);
			GL.glTranslated(0, pillowSide, 0);
			GL.glRotated(-90d, 1d, 0d, 0d);
			drawLump(pillowSide, pillowHeight, 0.5);
		}
		GL.glPopMatrix();
	}

	public void realRender(RenderStage stage) {
		GL.glTranslated(x, y, 0);
		GL.glRotated(getRotation(), 0, 0, 1d);
		if (stage == RenderStage.WIRE) {
			GL.glColor3d(1d, 0.3d, 0d);
		} else {
			GL.glColor3d(1d, 0.9d, 0.8d);
		}
		drawArm();
		GL.glTranslated(armWidth, 0, 0);
		GL.glBegin(GL.GL_QUADS);
		{
			GL.glVertex3d(0, 0, 0);
			GL.glVertex3d(0, 0, seatHeight);
			GL.glVertex3d(seatWidth, 0, seatHeight);
			GL.glVertex3d(seatWidth, 0, 0);

			GL.glVertex3d(0, length, 0);
			GL.glVertex3d(0, length, seatHeight);
			GL.glVertex3d(seatWidth, length, seatHeight);
			GL.glVertex3d(seatWidth, length, 0);
		}
		GL.glEnd();
		GL.glPushMatrix();
		{
			GL.glTranslated(0, pillowHeight, seatHeight + pillowHeight);
			for (int i = 0; i < lengthInPillows; i++) {
				drawPillow();
				GL.glPushMatrix();
				{
					GL.glTranslated(pillowSide, 0, 0);
					GL.glRotated(180d, 0d, 0, 1d);
					GL.glRotated(70d, 1d, 0, 0d);
					drawPillow();
					GL.glTranslated(0, 0, -pillowHeight);
					GL.glBegin(GL.GL_QUADS);
					{
						GL.glVertex3d(0, 0, 0);
						GL.glVertex3d(0, pillowSide, 0);
						GL.glVertex3d(pillowSide, pillowSide, 0);
						GL.glVertex3d(pillowSide, 0, 0);

						if (i == lengthInPillows - 1) {
							GL.glVertex3d(0, 0, 0);
							GL.glVertex3d(0, 0, pillowHeight);
							GL.glVertex3d(0, pillowSide, pillowHeight);
							GL.glVertex3d(0, pillowSide, 0);
						}

						if (i == 0) {
							GL.glVertex3d(pillowSide, 0, 0);
							GL.glVertex3d(pillowSide, 0, pillowHeight);
							GL.glVertex3d(pillowSide, pillowSide, pillowHeight);
							GL.glVertex3d(pillowSide, pillowSide, 0);
						}
					}
					GL.glEnd();
				}
				GL.glPopMatrix();
				GL.glTranslated(pillowSide, 0, 0);
			}
		}
		GL.glPopMatrix();
		GL.glTranslated(seatWidth, 0, 0);
		drawArm();
	}

	public boolean isHoverCoordinates(double x, double y) {
		double[] rot = correctRotation(x, y);
		x = rot[0];
		y = rot[1];
		return x > this.x && this.x + seatWidth + 2 * armWidth > x && y > this.y && this.y + length > y;
	}
}