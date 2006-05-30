/**
 * 
 */
package org.opencad.scene;

import java.util.Iterator;
import org.eclipse.opengl.GL;
import org.eclipse.swt.widgets.Composite;
import org.opencad.model.Constants;
import org.opencad.model.Model;

/**
 * @author George
 */
public class PreviewScene extends GLScene {
	/**
	 * @param parent
	 * @param scene
	 * @param projectionStyle
	 */
	ModellingScene mscene;

	public PreviewScene(Composite parent, ModellingScene scene) {
		super(parent, GLScene.PERSPECTIVE);
		this.mscene = scene;
	}
	protected void drawGrid() {
		final int lines = 10;
		final double courtSize = Constants.SCENE_SIZE;
		GL.glColor3d(0.2d, 0.2d, 0.2d);
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
		GL.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_LINE);
		Iterator i = mscene.iterator();
		while (i.hasNext()) {
			((Model) i.next()).drawModel();
		}
	}
}
