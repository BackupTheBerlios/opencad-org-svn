package org.opencad.ui.view;

import org.eclipse.core.internal.resources.WorkspaceRoot;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.opengl.GL;
import org.eclipse.opengl.GLU;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;
import org.opencad.ui.editor.GLEditor;

import sun.security.acl.WorldGroupImpl;

public class GLView extends ViewPart implements MouseMoveListener {
	GLCanvas glCanvas;

	double fov, eyex, eyey, eyez, centerx, centery, centerz;

	double zoomSpeed;

	private double min_eyez;
	
	void modifyDistance(double fraction) {
		fraction = 1 + fraction;
		eyex = centerx + (eyex - centerx) * fraction;
		eyey = centerx + (eyey - centerx) * fraction;
		eyez = centerx + (eyez - centerx) * fraction;
	}

	void doZoom(int count, boolean fov) {
		if (fov) {
			this.fov += count;
		} else {
			modifyDistance(count * zoomSpeed);
		}
		doResize();
		doDraw();
	}

	@Override
	public void createPartControl(Composite parent) {
		GLData data = new GLData();
		data.doubleBuffer = true;
		glCanvas = new GLCanvas(parent, SWT.NO_BACKGROUND, data);
		glCanvas.addPaintListener(new PaintListener() {

			public void paintControl(PaintEvent e) {
				doDraw();
			}
		});
		glCanvas.addControlListener(new ControlAdapter() {

			public void controlResized(ControlEvent e) {
				doResize();
			}
		});
		glCanvas.addListener(SWT.MouseWheel, new Listener() {
			public void handleEvent(Event event) {
				doZoom(event.count, (event.stateMask & SWT.SHIFT) > 0);
			}
		});
		glCanvas.addListener(SWT.MouseDown, new Listener() {
			public void handleEvent(Event event) {
				registerMoveListener(event, true);
			}
		});
		glCanvas.addListener(SWT.MouseUp, new Listener() {
			public void handleEvent(Event event) {
				registerMoveListener(event, false);
			}
		});
		eyex = 50;
		eyey = 50;
		eyez = 50;
		min_eyez = 1;
		centerx = 0;
		centery = 0;
		centerz = 0;
		fov = 90;
		zoomSpeed = 0.1;
		doInit();
	}

	int dragx;

	int dragy;

	private boolean disabled;

	void registerMoveListener(Event event, boolean add) {
		if (add) {
			dragx = event.x;
			dragy = event.y;
			glCanvas.addMouseMoveListener(this);
		} else {
			glCanvas.removeMouseMoveListener(this);
		}
	}

	void doInit() {
		synchronized (GL.class) {
			glCanvas.setCurrent();
			glInit();
		}
	}

	void glInit() {
		GL.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		GL.glColor3f(0.0f, 0.0f, 0.0f);
		GL.glClearDepth(1.0f);
		GL.glEnable(GL.GL_DEPTH_TEST);
		GL.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
	}

	void doDraw() {
		synchronized (GL.class) {
			glCanvas.setCurrent();
			glDraw();
			glCanvas.swapBuffers();
		}
	}

	void doResize() {
		synchronized (GL.class) {
			glCanvas.setCurrent();
			handleResize();
		}
	}

	void handleResize() {
		Rectangle size = glCanvas.getClientArea();
		GL.glMatrixMode(GL.GL_PROJECTION);
		GL.glLoadIdentity();
		GL.glViewport(0, 0, size.width, size.height);
		setupProjection(size);
		GL.glMatrixMode(GL.GL_MODELVIEW);
		GL.glLoadIdentity();
	}

	void setupProjection(Rectangle size) {
		if (size.width > 0 && size.height > 0) {
			double aspect = size.width / size.height;
			GLU.gluPerspective(fov, aspect, 0.1, 100);

			checkBounds();

			GLU.gluLookAt(eyex, eyey, eyez, centerx, centery, centerz, 0, 0, 1);
			disabled = false;
		} else {
			disabled = true;
		}
	}

	void glDraw() {
		if (!disabled) {
			GL.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
			GL.glLoadIdentity();
			GL.glPushMatrix();
			{
				GL.glTranslated(0d, 0d, -0.1d);
				GLEditor.drawGrid();
				GL.glTranslated(0d, 0d, -0.1d);
				GLEditor.drawAnchor();
			}
			GL.glPopMatrix();
			//outline.getEditor().getModel().editorRender();
		}
	}

	@Override
	public void setFocus() {
		glCanvas.setFocus();
	}

	public void checkBounds() {
		eyez = eyez < min_eyez ? min_eyez : eyez;
	}

	public void mouseMove(MouseEvent e) {
		int dx = e.x - dragx;
		int dy = e.y - dragy;

		double angle = Math.atan2(eyey - centery, eyex - centerx);
		eyey += dy * zoomSpeed * Math.sin(angle) + dx * Math.cos(angle)
				* zoomSpeed;
		eyex += -dx * zoomSpeed * Math.sin(angle) + dy * Math.cos(angle)
				* zoomSpeed;

		dragx = e.x;
		dragy = e.y;
		doResize();
		doDraw();
	}
	
	@Override
	public void init(IViewSite site) throws PartInitException {
		super.init(site);
		System.out.println(site.getPage());
	}
}