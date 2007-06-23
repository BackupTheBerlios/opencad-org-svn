package org.opencad.modelling.walls.features;

import org.eclipse.opengl.GL;
import org.opencad.modelling.corners.Corner;
import org.opencad.modelling.walls.Wall;
import org.opencad.ui.editor.RenderStage;

public class Door extends WallFeature {
	private static final long serialVersionUID = 1L;

	private static final double thickness = Corner.thickness / 4;

	public Door() {
		setGroundOffset(1d);
		setHeight(1.5d);
		setWidth(1d + 4 * thickness);
		setStartOffset(0d);
	}

	public void editorRender() {
		GL.glTranslated(0d, -Corner.thickness, 0d);
		if (isSelected()) {
			GL.glColor3d(0d, 0.5d, 1d);
		} else if (isHover()) {
			GL.glColor3d(1d, 0d, 0d);
		} else {
			GL.glColor3d(0d, 0d, 0d);
		}
		GL.glBegin(GL.GL_LINES);
		{
			GL.glVertex2d(0, 0);
			GL.glVertex2d(0, Corner.thickness * 2);
			GL.glVertex2d(getWidth(), 0);
			GL.glVertex2d(getWidth(), Corner.thickness * 2);
			GL.glVertex2d(0, Corner.thickness);
			GL.glVertex2d(getWidth() / 6, Corner.thickness);
			GL.glVertex2d(getWidth() * 5 / 6, Corner.thickness);
			GL.glVertex2d(getWidth(), Corner.thickness);
			GL.glVertex2d(getWidth() / 6, Corner.thickness);
			GL.glVertex2d(getWidth() * 4.5 / 6, Corner.thickness * 3);
		}
		GL.glEnd();
	}

	public static void getColor(RenderStage stage) {
		switch (stage) {
		case FILL:
			GL.glColor3d(0.95d, 0.93d, 0.92d);
			break;
		case WIRE:
			GL.glColor3d(0.4d, 0.37d, 0.35d);
			break;
		}
	}

	public void realRender(RenderStage stage) {
		if (stage == RenderStage.ALPHA)
			return;
		GL.glTranslated(0d, -Corner.thickness, 0d);
		double ctmt = Corner.thickness - thickness;
		double ctpt = Corner.thickness + thickness;
		double ct = 2 * Corner.thickness;
		double tt = 2 * thickness;
		double wmtt = getWidth() - tt;
		double m = getMaxGroundOffset();
		double mmtt = m - tt;
		double h = Wall.height;
		double w = getWidth();
		switch (stage) {
		case FILL: {
			getColor(stage);
			GL.glBegin(GL.GL_QUADS);
			{
				GL.glVertex3d(tt, ctmt, 0);
				GL.glVertex3d(tt, ctmt, mmtt);
				GL.glVertex3d(tt, ctpt, mmtt);
				GL.glVertex3d(tt, ctpt, 0);

				GL.glVertex3d(wmtt, ctmt, 0);
				GL.glVertex3d(wmtt, ctmt, mmtt);
				GL.glVertex3d(wmtt, ctpt, mmtt);
				GL.glVertex3d(wmtt, ctpt, 0);

				GL.glVertex3d(tt, ctmt, mmtt);
				GL.glVertex3d(wmtt, ctmt, mmtt);
				GL.glVertex3d(wmtt, ctpt, mmtt);
				GL.glVertex3d(tt, ctpt, mmtt);

				GL.glVertex3d(0, ctmt, 0);
				GL.glVertex3d(tt, ctmt, 0);
				GL.glVertex3d(tt, ctmt, mmtt);
				GL.glVertex3d(0, ctmt, mmtt);

				GL.glVertex3d(0, ctpt, 0);
				GL.glVertex3d(tt, ctpt, 0);
				GL.glVertex3d(tt, ctpt, mmtt);
				GL.glVertex3d(0, ctpt, mmtt);

				GL.glVertex3d(w, ctmt, 0);
				GL.glVertex3d(wmtt, ctmt, 0);
				GL.glVertex3d(wmtt, ctmt, mmtt);
				GL.glVertex3d(w, ctmt, mmtt);

				GL.glVertex3d(w, ctpt, 0);
				GL.glVertex3d(wmtt, ctpt, 0);
				GL.glVertex3d(wmtt, ctpt, mmtt);
				GL.glVertex3d(w, ctpt, mmtt);

				GL.glVertex3d(0, ctmt, mmtt);
				GL.glVertex3d(w, ctmt, mmtt);
				GL.glVertex3d(w, ctmt, m);
				GL.glVertex3d(0, ctmt, m);

				GL.glVertex3d(0, ctpt, mmtt);
				GL.glVertex3d(w, ctpt, mmtt);
				GL.glVertex3d(w, ctpt, m);
				GL.glVertex3d(0, ctpt, m);
			}
			GL.glEnd();
			Wall.getColor(stage);
			GL.glBegin(GL.GL_QUADS);
			{

				GL.glVertex3d(0, 0, m);
				GL.glVertex3d(w, 0, m);
				GL.glVertex3d(w, 0, h);
				GL.glVertex3d(0, 0, h);

				GL.glVertex3d(w, 0, 0);
				GL.glVertex3d(w, 0, m);
				GL.glVertex3d(w, ctmt, m);
				GL.glVertex3d(w, ctmt, 0);

				GL.glVertex3d(w, ct, 0);
				GL.glVertex3d(w, ct, m);
				GL.glVertex3d(w, ctpt, m);
				GL.glVertex3d(w, ctpt, 0);

				GL.glVertex3d(0, 0, 0);
				GL.glVertex3d(0, 0, m);
				GL.glVertex3d(0, ctmt, m);
				GL.glVertex3d(0, ctmt, 0);

				GL.glVertex3d(w, 0, m);
				GL.glVertex3d(0, 0, m);
				GL.glVertex3d(0, ctmt, m);
				GL.glVertex3d(w, ctmt, m);

				GL.glVertex3d(0, ct, 0);
				GL.glVertex3d(0, ct, m);
				GL.glVertex3d(0, ctpt, m);
				GL.glVertex3d(0, ctpt, 0);

				GL.glVertex3d(w, ctpt, m);
				GL.glVertex3d(0, ctpt, m);
				GL.glVertex3d(0, ct, m);
				GL.glVertex3d(w, ct, m);

				GL.glVertex3d(w, 0, h);
				GL.glVertex3d(0, 0, h);
				GL.glVertex3d(0, ct, h);
				GL.glVertex3d(w, ct, h);

				GL.glVertex3d(0, ct, m);
				GL.glVertex3d(w, ct, m);
				GL.glVertex3d(w, ct, h);
				GL.glVertex3d(0, ct, h);
			}
			GL.glEnd();
		}
			break;
		case WIRE: {
			getColor(stage);
			GL.glBegin(GL.GL_LINES);
			{
				GL.glVertex3d(tt, ctmt, 0);
				GL.glVertex3d(tt, ctpt, 0);
				GL.glVertex3d(tt, ctmt, mmtt);
				GL.glVertex3d(tt, ctpt, mmtt);

				GL.glVertex3d(wmtt, ctmt, 0);
				GL.glVertex3d(wmtt, ctpt, 0);
				GL.glVertex3d(wmtt, ctmt, mmtt);
				GL.glVertex3d(wmtt, ctpt, mmtt);

				GL.glVertex3d(0, ctmt, 0);
				GL.glVertex3d(tt, ctmt, 0);
				GL.glVertex3d(0, ctpt, 0);
				GL.glVertex3d(tt, ctpt, 0);
				GL.glVertex3d(0, ctmt, 0);
				GL.glVertex3d(tt, ctmt, 0);
				GL.glVertex3d(0, ctpt, 0);
				GL.glVertex3d(tt, ctpt, 0);

				GL.glVertex3d(w, ctmt, 0);
				GL.glVertex3d(wmtt, ctmt, 0);
				GL.glVertex3d(w, ctpt, 0);
				GL.glVertex3d(wmtt, ctpt, 0);
				GL.glVertex3d(w, ctmt, 0);
				GL.glVertex3d(wmtt, ctmt, 0);
				GL.glVertex3d(w, ctpt, 0);
				GL.glVertex3d(wmtt, ctpt, 0);

				GL.glVertex3d(tt, ctmt, 0);
				GL.glVertex3d(tt, ctmt, mmtt);
				GL.glVertex3d(tt, ctpt, 0);
				GL.glVertex3d(tt, ctpt, mmtt);
				GL.glVertex3d(wmtt, ctmt, 0);
				GL.glVertex3d(wmtt, ctmt, mmtt);
				GL.glVertex3d(wmtt, ctpt, 0);
				GL.glVertex3d(wmtt, ctpt, mmtt);

				GL.glVertex3d(tt, ctmt, mmtt);
				GL.glVertex3d(wmtt, ctmt, mmtt);
				GL.glVertex3d(tt, ctpt, mmtt);
				GL.glVertex3d(wmtt, ctpt, mmtt);
			}
			GL.glEnd();
			Wall.getColor(stage);
			GL.glBegin(GL.GL_LINES);
			{

				GL.glVertex3d(0, 0, 0);
				GL.glVertex3d(0, ctmt, 0);
				GL.glVertex3d(0, ctpt, 0);
				GL.glVertex3d(0, ct, 0);

				GL.glVertex3d(w, 0, 0);
				GL.glVertex3d(w, ctmt, 0);
				GL.glVertex3d(w, ctpt, 0);
				GL.glVertex3d(w, ct, 0);

				GL.glVertex3d(0, 0, h);
				GL.glVertex3d(w, 0, h);

				GL.glVertex3d(0, ctmt, m);
				GL.glVertex3d(w, ctmt, m);

				GL.glVertex3d(0, ctpt, m);
				GL.glVertex3d(w, ctpt, m);

				GL.glVertex3d(0, ct, h);
				GL.glVertex3d(w, ct, h);

				GL.glVertex3d(0, 0, m);
				GL.glVertex3d(w, 0, m);
				GL.glVertex3d(0, ct, m);
				GL.glVertex3d(w, ct, m);

				GL.glVertex3d(0, 0, 0);
				GL.glVertex3d(0, 0, m);

				GL.glVertex3d(0, ctmt, 0);
				GL.glVertex3d(0, ctmt, m);

				GL.glVertex3d(0, ctpt, 0);
				GL.glVertex3d(0, ctpt, m);

				GL.glVertex3d(w, ctmt, 0);
				GL.glVertex3d(w, ctmt, m);

				GL.glVertex3d(w, ctpt, 0);
				GL.glVertex3d(w, ctpt, m);

				GL.glVertex3d(0, ct, m);
				GL.glVertex3d(0, ct, 0);

				GL.glVertex3d(w, 0, 0);
				GL.glVertex3d(w, 0, m);
				GL.glVertex3d(w, ct, m);
				GL.glVertex3d(w, ct, 0);

				GL.glVertex3d(0, 0, m);
				GL.glVertex3d(0, ctmt, m);
				GL.glVertex3d(0, ctpt, m);
				GL.glVertex3d(0, ct, m);

				GL.glVertex3d(w, 0, m);
				GL.glVertex3d(w, ctmt, m);
				GL.glVertex3d(w, ctpt, m);
				GL.glVertex3d(w, ct, m);
			}
			GL.glEnd();
		}
		}
		GL.glTranslated(2 * thickness, Corner.thickness + thickness, 0);
		GL.glRotated(60d, 0d, 0d, 1d);
		GL.glTranslated(0, -2 * thickness, 0);
		double wx = getWidth() - 4 * thickness;
		double wz = getMaxGroundOffset() - 2 * thickness;
		double wy = 2 * thickness;
		getColor(stage);
		GL.glBegin(GL.GL_QUADS);
		{
			GL.glVertex3d(0, 0, 0);
			GL.glVertex3d(wx, 0, 0);
			GL.glVertex3d(wx, 0, wz);
			GL.glVertex3d(0, 0, wz);

			GL.glVertex3d(0, wy, 0);
			GL.glVertex3d(wx, wy, 0);
			GL.glVertex3d(wx, wy, wz);
			GL.glVertex3d(0, wy, wz);

			GL.glVertex3d(wx, 0, wz);
			GL.glVertex3d(0, 0, wz);
			GL.glVertex3d(0, wy, wz);
			GL.glVertex3d(wx, wy, wz);

			GL.glVertex3d(wx, 0, 0);
			GL.glVertex3d(0, 0, 0);
			GL.glVertex3d(0, wy, 0);
			GL.glVertex3d(wx, wy, 0);

			GL.glVertex3d(wx, 0, 0);
			GL.glVertex3d(wx, wy, 0);
			GL.glVertex3d(wx, wy, wz);
			GL.glVertex3d(wx, 0, wz);

			GL.glVertex3d(0, 0, 0);
			GL.glVertex3d(0, wy, 0);
			GL.glVertex3d(0, wy, wz);
			GL.glVertex3d(0, 0, wz);
		}
		GL.glEnd();
		GL.glTranslated(getWidth() - 6 * thickness, 2 * thickness,
				getMaxGroundOffset() / 2 - thickness);
		double t = thickness;
		switch (stage) {
		case FILL: {
			GL.glBegin(GL.GL_QUADS);
			{
				GL.glVertex3d(0, 0, 0);
				GL.glVertex3d(0, t, 0);
				GL.glVertex3d(0, t, t);
				GL.glVertex3d(0, 0, t);

				GL.glVertex3d(0, 0, t);
				GL.glVertex3d(0, t, t);
				GL.glVertex3d(t, t, t);
				GL.glVertex3d(t, 0, t);

				GL.glVertex3d(0, 0, 0);
				GL.glVertex3d(0, t, 0);
				GL.glVertex3d(t, t, 0);
				GL.glVertex3d(t, 0, 0);

				GL.glVertex3d(t, 0, 0);
				GL.glVertex3d(t, t, 0);
				GL.glVertex3d(t, t, t);
				GL.glVertex3d(t, 0, t);

				GL.glVertex3d(0, t, 0);
				GL.glVertex3d(0, t, t);
				GL.glVertex3d(t, t, t);
				GL.glVertex3d(t, t, 0);
			}
			GL.glEnd();
		}
			break;
		case WIRE: {
			GL.glBegin(GL.GL_LINES);
			{
				GL.glVertex3d(0, 0, 0);
				GL.glVertex3d(0, t, 0);
				GL.glVertex3d(t, 0, 0);
				GL.glVertex3d(t, t, 0);
				GL.glVertex3d(0, 0, t);
				GL.glVertex3d(0, t, t);
				GL.glVertex3d(t, 0, t);
				GL.glVertex3d(t, t, t);

				GL.glVertex3d(0, t, 0);
				GL.glVertex3d(0, t, t);
				GL.glVertex3d(t, t, 0);
				GL.glVertex3d(t, t, t);

				GL.glVertex3d(0, t, 0);
				GL.glVertex3d(t, t, 0);
				GL.glVertex3d(0, t, t);
				GL.glVertex3d(t, t, t);
			}
			GL.glEnd();
		}
		}
	}

	public String getImage() {
		return "icons/door.gif";
	}
}
