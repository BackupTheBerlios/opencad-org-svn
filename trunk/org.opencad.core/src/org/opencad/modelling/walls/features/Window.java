package org.opencad.modelling.walls.features;

import org.eclipse.opengl.GL;
import org.opencad.modelling.corners.Corner;
import org.opencad.modelling.walls.Wall;
import org.opencad.ui.editor.GLEditor;
import org.opencad.ui.editor.GLEditorState;
import org.opencad.ui.editor.RenderStage;

public class Window extends WallFeature {
	private static final long serialVersionUID = 1L;

	private static final double thickness = Corner.thickness / 4;

	public Window() {
		setGroundOffset(1d);
		setHeight(1.5d);
		setWidth(2.5d);
		setStartOffset(0d);
	}

	public void editorRender() {
		GL.glTranslated(0d, -Corner.thickness, 0d);
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
			GL.glVertex2d(getWidth() * 2.5 / 6, Corner.thickness * 3);
			GL.glVertex2d(getWidth() * 5 / 6, Corner.thickness);
			GL.glVertex2d(getWidth() * 3.5 / 6, Corner.thickness * 3);
		}
		GL.glEnd();
	}

	public static void getColor(RenderStage stage) {
		switch (stage) {
		case FILL:
			GL.glColor3d(1d, 1d, 1d);
			break;
		case WIRE:
			GL.glColor3d(0.5d, 0.5d, 0.5d);
			break;
		}
	}

	public void realRender(RenderStage stage) {
		double w = getWidth();
		double g = getGroundOffset();
		double m = getMaxGroundOffset();
		double h = Wall.height;
		double t = 2 * Corner.thickness;
		double cy = Corner.thickness;
		double sy = Corner.thickness - thickness;
		double ly = Corner.thickness + thickness;
		double tt = 2 * thickness;
		double wmtt = w - tt;
		double gptt = g + tt;
		double mmtt = m - tt;

		GL.glTranslated(0d, -Corner.thickness, 0d);
		switch (stage) {
		case FILL: {
			getColor(stage);
			GL.glBegin(GL.GL_QUADS);
			{
				GL.glVertex3d(w, sy, g);
				GL.glVertex3d(wmtt, sy, g);
				GL.glVertex3d(wmtt, sy, m);
				GL.glVertex3d(w, sy, m);

				GL.glVertex3d(w, ly, g);
				GL.glVertex3d(wmtt, ly, g);
				GL.glVertex3d(wmtt, ly, m);
				GL.glVertex3d(w, ly, m);

				GL.glVertex3d(tt, sy, g);
				GL.glVertex3d(tt, sy, gptt);
				GL.glVertex3d(wmtt, sy, gptt);
				GL.glVertex3d(wmtt, sy, g);

				GL.glVertex3d(tt, ly, m);
				GL.glVertex3d(tt, ly, mmtt);
				GL.glVertex3d(wmtt, ly, mmtt);
				GL.glVertex3d(wmtt, ly, m);

				GL.glVertex3d(tt, sy, m);
				GL.glVertex3d(tt, sy, mmtt);
				GL.glVertex3d(wmtt, sy, mmtt);
				GL.glVertex3d(wmtt, sy, m);

				GL.glVertex3d(tt, ly, g);
				GL.glVertex3d(tt, ly, gptt);
				GL.glVertex3d(wmtt, ly, gptt);
				GL.glVertex3d(wmtt, ly, g);

				GL.glVertex3d(wmtt, ly, mmtt);
				GL.glVertex3d(tt, ly, mmtt);
				GL.glVertex3d(tt, sy, mmtt);
				GL.glVertex3d(wmtt, sy, mmtt);

				GL.glVertex3d(tt, ly, gptt);
				GL.glVertex3d(tt, ly, mmtt);
				GL.glVertex3d(tt, sy, mmtt);
				GL.glVertex3d(tt, sy, gptt);

				GL.glVertex3d(wmtt, ly, gptt);
				GL.glVertex3d(wmtt, ly, mmtt);
				GL.glVertex3d(wmtt, sy, mmtt);
				GL.glVertex3d(wmtt, sy, gptt);

				GL.glVertex3d(wmtt, ly, gptt);
				GL.glVertex3d(tt, ly, gptt);
				GL.glVertex3d(tt, sy, gptt);
				GL.glVertex3d(wmtt, sy, gptt);

				GL.glVertex3d(0, sy, g);
				GL.glVertex3d(tt, sy, g);
				GL.glVertex3d(tt, sy, m);
				GL.glVertex3d(0, sy, m);

				GL.glVertex3d(0, ly, g);
				GL.glVertex3d(tt, ly, g);
				GL.glVertex3d(tt, ly, m);
				GL.glVertex3d(0, ly, m);
			}
			GL.glEnd();
			Wall.getColor(stage);
			GL.glBegin(GL.GL_QUADS);
			{
				GL.glVertex3d(0, 0, 0);
				GL.glVertex3d(w, 0, 0);
				GL.glVertex3d(w, 0, g);
				GL.glVertex3d(0, 0, g);

				GL.glVertex3d(0, 0, m);
				GL.glVertex3d(w, 0, m);
				GL.glVertex3d(w, 0, h);
				GL.glVertex3d(0, 0, h);

				GL.glVertex3d(w, 0, g);
				GL.glVertex3d(w, 0, m);
				GL.glVertex3d(w, sy, m);
				GL.glVertex3d(w, sy, g);

				GL.glVertex3d(w, ly, g);
				GL.glVertex3d(w, ly, m);
				GL.glVertex3d(w, t, m);
				GL.glVertex3d(w, t, g);

				GL.glVertex3d(0, 0, g);
				GL.glVertex3d(0, 0, m);
				GL.glVertex3d(0, sy, m);
				GL.glVertex3d(0, sy, g);

				GL.glVertex3d(0, ly, g);
				GL.glVertex3d(0, ly, m);
				GL.glVertex3d(0, t, m);
				GL.glVertex3d(0, t, g);

				GL.glVertex3d(w, 0, g);
				GL.glVertex3d(0, 0, g);
				GL.glVertex3d(0, sy, g);
				GL.glVertex3d(w, sy, g);

				GL.glVertex3d(w, ly, g);
				GL.glVertex3d(0, ly, g);
				GL.glVertex3d(0, t, g);
				GL.glVertex3d(w, t, g);

				GL.glVertex3d(w, 0, m);
				GL.glVertex3d(0, 0, m);
				GL.glVertex3d(0, sy, m);
				GL.glVertex3d(w, sy, m);

				GL.glVertex3d(w, ly, m);
				GL.glVertex3d(0, ly, m);
				GL.glVertex3d(0, t, m);
				GL.glVertex3d(w, t, m);

				GL.glVertex3d(w, 0, h);
				GL.glVertex3d(0, 0, h);
				GL.glVertex3d(0, t, h);
				GL.glVertex3d(w, t, h);

				GL.glVertex3d(0, t, 0);
				GL.glVertex3d(w, t, 0);
				GL.glVertex3d(w, t, g);
				GL.glVertex3d(0, t, g);

				GL.glVertex3d(0, t, m);
				GL.glVertex3d(w, t, m);
				GL.glVertex3d(w, t, h);
				GL.glVertex3d(0, t, h);
			}
			GL.glEnd();
		}
			break;
		case WIRE: {
			Wall.getColor(stage);
			GL.glBegin(GL.GL_LINES);
			{
				GL.glVertex3d(0, 0, 0);
				GL.glVertex3d(w, 0, 0);
				GL.glVertex3d(0, t, 0);
				GL.glVertex3d(w, t, 0);

				GL.glVertex3d(0, sy, g);
				GL.glVertex3d(w, sy, g);
				GL.glVertex3d(0, ly, g);
				GL.glVertex3d(w, ly, g);

				GL.glVertex3d(0, sy, m);
				GL.glVertex3d(w, sy, m);
				GL.glVertex3d(0, ly, m);
				GL.glVertex3d(w, ly, m);

				GL.glVertex3d(0, 0, h);
				GL.glVertex3d(w, 0, h);
				GL.glVertex3d(0, t, h);
				GL.glVertex3d(w, t, h);

				GL.glVertex3d(0, 0, g);
				GL.glVertex3d(w, 0, g);
				GL.glVertex3d(0, t, g);
				GL.glVertex3d(w, t, g);

				GL.glVertex3d(0, 0, m);
				GL.glVertex3d(w, 0, m);
				GL.glVertex3d(0, t, m);
				GL.glVertex3d(w, t, m);

				GL.glVertex3d(0, 0, g);
				GL.glVertex3d(0, 0, m);
				GL.glVertex3d(0, t, m);
				GL.glVertex3d(0, t, g);

				GL.glVertex3d(w, 0, g);
				GL.glVertex3d(w, 0, m);
				GL.glVertex3d(w, t, m);
				GL.glVertex3d(w, t, g);

				GL.glVertex3d(w, sy, g);
				GL.glVertex3d(w, sy, m);
				GL.glVertex3d(w, ly, m);
				GL.glVertex3d(w, ly, g);

				GL.glVertex3d(0, sy, g);
				GL.glVertex3d(0, sy, m);
				GL.glVertex3d(0, ly, m);
				GL.glVertex3d(0, ly, g);

				GL.glVertex3d(0, 0, g);
				GL.glVertex3d(0, sy, g);
				GL.glVertex3d(0, ly, g);
				GL.glVertex3d(0, t, g);

				GL.glVertex3d(w, 0, g);
				GL.glVertex3d(w, sy, g);
				GL.glVertex3d(w, ly, g);
				GL.glVertex3d(w, t, g);

				GL.glVertex3d(0, 0, m);
				GL.glVertex3d(0, sy, m);
				GL.glVertex3d(0, ly, m);
				GL.glVertex3d(0, t, m);

				GL.glVertex3d(w, 0, m);
				GL.glVertex3d(w, sy, m);
				GL.glVertex3d(w, ly, m);
				GL.glVertex3d(w, t, m);
			}
			GL.glEnd();
			getColor(stage);
			GL.glBegin(GL.GL_LINES);
			{

				GL.glVertex3d(tt, sy, gptt);
				GL.glVertex3d(wmtt, sy, gptt);
				GL.glVertex3d(tt, ly, gptt);
				GL.glVertex3d(wmtt, ly, gptt);

				GL.glVertex3d(tt, sy, mmtt);
				GL.glVertex3d(wmtt, sy, mmtt);
				GL.glVertex3d(tt, ly, mmtt);
				GL.glVertex3d(wmtt, ly, mmtt);

				GL.glVertex3d(tt, sy, gptt);
				GL.glVertex3d(tt, ly, gptt);
				GL.glVertex3d(wmtt, sy, gptt);
				GL.glVertex3d(wmtt, ly, gptt);

				GL.glVertex3d(tt, sy, mmtt);
				GL.glVertex3d(tt, ly, mmtt);
				GL.glVertex3d(wmtt, sy, mmtt);
				GL.glVertex3d(wmtt, ly, mmtt);

				GL.glVertex3d(tt, sy, gptt);
				GL.glVertex3d(tt, sy, mmtt);
				GL.glVertex3d(wmtt, sy, gptt);
				GL.glVertex3d(wmtt, sy, mmtt);

				GL.glVertex3d(tt, ly, gptt);
				GL.glVertex3d(tt, ly, mmtt);
				GL.glVertex3d(wmtt, ly, gptt);
				GL.glVertex3d(wmtt, ly, mmtt);
			}
			GL.glEnd();
		}
			break;
		case ALPHA: {
			GL.glColor4d(0.9d, 1d, 1d, 0.5d);
			GL.glBegin(GL.GL_QUADS);
			{
				GL.glVertex3d(tt, cy, gptt);
				GL.glVertex3d(wmtt, cy, gptt);
				GL.glVertex3d(wmtt, cy, mmtt);
				GL.glVertex3d(tt, cy, mmtt);
			}
			GL.glEnd();
		}
			break;
		}
	}

	public GLEditorState getSelectionState(GLEditor editor) {
		return new SelectFeatureState(editor, this);
	}

	public String getImage() {
		return "icons/window.gif";
	}
}