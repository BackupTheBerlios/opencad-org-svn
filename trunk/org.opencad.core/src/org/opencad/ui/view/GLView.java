package org.opencad.ui.view;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.opengl.GL;
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
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;
import org.opencad.ui.editor.GLEditor;

public class GLView extends ViewPart implements MouseMoveListener,
		ISelectionListener, ISelectionChangedListener, IPropertyListener {
	GLCanvas glCanvas;

	double xrot, zrot, dist;

	double zoomSpeed;

	GLEditor glEditor;

	private int dist_max;

	private int xrot_min;

	private int xrot_max;

	private double spinSpeed;

	void modifyDistance(double fraction) {
		dist *= 1 + fraction;
	}

	void doZoom(int count) {
		modifyDistance(count * zoomSpeed);
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
				doZoom(event.count);
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
		zoomSpeed = 0.1;
		spinSpeed = 0.5d;
		dist_max = 100;
		dist = 20;
		zrot = 0;
		xrot = 0;
		xrot_min = 5;
		xrot_max = 85;
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
		selectionChanged(getSite().getPage().getActivePart(), getSite()
				.getPage().getSelection());
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
			double min = Math.min(size.height, size.width);
			double scale = 0.1d;
			double range = 200;
			double px2gl = scale / min;
			double glX = size.width * px2gl / 2;
			double glY = size.height * px2gl / 2;
			GL.glFrustum(-glX, +glX, -glY, glY, scale, range);
			checkBounds();
			GL.glTranslated(0, 0, - dist);
			GL.glRotated(-90d + xrot, 1, 0, 0);
			GL.glRotated(zrot, 0, 0, 1);
			disabled = false;
		} else {
			disabled = true;
		}
	}

	public static void drawGrid() {
		final int gridCount = 20;
		final int gridSkip = 5;
		final double gridSize = 5d;
		GL.glPushMatrix();
		{
			GL.glTranslated(0d, -gridSize * gridCount, 0d);
			for (int i = 0; i <= gridCount * 2; i++) {
				if (i % gridSkip == 0) {
					GL.glColor3d(0.7d, 0.7d, 0.7d);
				} else {
					GL.glColor3d(0.9d, 0.9d, 0.9d);
				}
				GL.glBegin(GL.GL_LINES);
				{
					GL.glVertex2d(-gridSize * gridCount, 0);
					GL.glVertex2d(+gridSize * gridCount, 0);
				}
				GL.glEnd();
				GL.glTranslated(0d, (double) gridSize, 0d);
			}
		}
		GL.glPopMatrix();
		GL.glPushMatrix();
		{
			GL.glTranslated(-gridSize * gridCount, 0d, 0d);
			for (int i = 0; i <= 2 * gridCount; i++) {
				if (i % gridSkip == 0) {
					GL.glColor3d(0.7d, 0.7d, 0.7d);
				} else {
					GL.glColor3d(0.9d, 0.9d, 0.9d);
				}
				GL.glBegin(GL.GL_LINES);
				{
					GL.glVertex2d(0, -gridSize * gridCount);
					GL.glVertex2d(0, +gridSize * gridCount);
				}
				GL.glEnd();
				GL.glTranslated((double) gridSize, 0d, 0d);
			}
		}
		GL.glPopMatrix();
	}

	public static void drawAnchor() {
		double arrowWidth = 0.01d;
		double arrowHeight = 0.07d;
		double segmentHeight = 1d - arrowHeight;
		GL.glColor3d(0d, 0.1d, 0.5d);
		GL.glBegin(GL.GL_LINES);
		{
			GL.glVertex2d(-arrowHeight, 0d);
			GL.glVertex2d(segmentHeight, 0d);
			GL.glVertex2d(0d, -arrowHeight);
			GL.glVertex2d(0d, segmentHeight);
			GL.glVertex3d(0d, 0d, -arrowHeight);
			GL.glVertex3d(0d, 0d, segmentHeight);
		}
		GL.glEnd();
		GL.glBegin(GL.GL_POLYGON);
		{
			GL.glVertex3d(0d, 0d, 1d);
			GL.glVertex3d(-arrowWidth, 0d, segmentHeight);
			GL.glVertex3d(arrowWidth, 0d, segmentHeight);
		}
		GL.glEnd();
		GL.glBegin(GL.GL_POLYGON);
		{
			GL.glVertex2d(1d, 0d);
			GL.glVertex2d(segmentHeight, -arrowWidth);
			GL.glVertex2d(segmentHeight, arrowWidth);
		}
		GL.glEnd();
		GL.glBegin(GL.GL_POLYGON);
		{
			GL.glVertex2d(0d, 1d);
			GL.glVertex2d(-arrowWidth, segmentHeight);
			GL.glVertex2d(arrowWidth, segmentHeight);
		}
		GL.glEnd();
	}

	void glDraw() {
		if (!disabled) {
			GL.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
			GL.glLoadIdentity();
			drawAnchor();
			GL.glTranslated(0d, 0d, -0.01d);
			drawGrid();
			GL.glLoadIdentity();
			glEditor.getModel().realRender();
		}
	}

	@Override
	public void setFocus() {
		glCanvas.setFocus();
	}

	public void checkBounds() {
		dist = dist > dist_max ? dist_max : dist;
		xrot = xrot > xrot_max ? xrot_max : xrot;
		xrot = xrot < xrot_min ? xrot_min : xrot;
	}

	public void mouseMove(MouseEvent e) {
		int dx = e.x - dragx;
		int dy = e.y - dragy;
		xrot += dy * spinSpeed;
		zrot += dx * spinSpeed;
		zrot = zrot % 360;
		xrot = xrot % 360;
		dragx = e.x;
		dragy = e.y;
		doResize();
		doDraw();
	}

	@Override
	public void init(IViewSite site) throws PartInitException {
		super.init(site);
		IWorkbenchPage page = site.getPage();
		page.addSelectionListener(this);
	}

	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		IEditorPart editor = getSite().getPage().getActiveEditor();
		if (editor instanceof GLEditor) {
			disabled = false;
			glEditor = (GLEditor) editor;
			glEditor.getSite().getSelectionProvider()
					.addSelectionChangedListener(this);
			glEditor.addPropertyListener(this);
			glCanvas.setEnabled(true);
			doDraw();
		} else {
			disabled = true;
			if (glEditor != null) {
				glEditor.getSite().getSelectionProvider()
						.removeSelectionChangedListener(this);
				glEditor.removePropertyListener(this);
			}
			glEditor = null;
			glCanvas.setEnabled(false);
		}
	}

	public void selectionChanged(SelectionChangedEvent event) {
		doDraw();
	}

	public void propertyChanged(Object source, int propId) {
		if (propId == GLEditor.PROP_DIRTY) {
			doDraw();
		}
	}
}