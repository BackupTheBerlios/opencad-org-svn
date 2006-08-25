package org.opencad.core.editors;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.opengl.GL;
import org.eclipse.opengl.GLU;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class GLEditor extends EditorPart {

	GLCanvas glCanvas;

	/**
	 * IN GL Units, the top coordinate that is to be displayed at the top of the
	 * viewport
	 */
	double topAnchor;

	/**
	 * IN GL Units, the left coordinate thate is to be displayed at the left of
	 * the viewport
	 */
	double leftAnchor;

	/**
	 * In GL Units, max(width, height) of the drawing shown on screen.
	 */
	double scale;

	private int zoomSpeed;

	private int panSpeed;

	protected double minScale;

	protected double maxScale;

	public GLEditor() {
		super();
	}

	public void dispose() {
		glCanvas.dispose();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub

	}

	void glDraw() {
		GL.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		GL.glLoadIdentity();

		GL.glColor3d(1., 0., 0.);
		GL.glBegin(GL.GL_QUADS);
		{
			GL.glVertex3f(-1.0f, 1.0f, 0.0f);
			GL.glVertex3f(1.0f, 1.0f, 0.0f);
			GL.glVertex3f(1.0f, -1.0f, 0.0f);
			GL.glVertex3f(-1.0f, -1.0f, 0.0f);
		}
		GL.glEnd();
		GL.glTranslatef(1.5f, 0f, 0.4f);
		GL.glColor3d(1., 1., 0.);
		GL.glBegin(GL.GL_QUADS);
		{
			GL.glVertex3f(-1.0f, 1.0f, 0.0f);
			GL.glVertex3f(1.0f, 1.0f, 0.0f);
			GL.glVertex3f(1.0f, -1.0f, 0.0f);
			GL.glVertex3f(-1.0f, -1.0f, 0.0f);
		}
		GL.glEnd();
		GL.glTranslatef(1.5f, 0f, -0.2f);
		GL.glColor3d(0., 1., 0.);
		GL.glBegin(GL.GL_QUADS);
		{
			GL.glVertex3f(-1.0f, 1.0f, 0.0f);
			GL.glVertex3f(1.0f, 1.0f, 0.0f);
			GL.glVertex3f(1.0f, -1.0f, 0.0f);
			GL.glVertex3f(-1.0f, -1.0f, 0.0f);
		}
		GL.glEnd();
		GL.glTranslatef(0f, 1.5f, 0.1f);
		GL.glColor3d(0., 0., 1.);
		GL.glBegin(GL.GL_QUADS);
		{
			GL.glVertex3f(-1.0f, 1.0f, 0.0f);
			GL.glVertex3f(1.0f, 1.0f, 0.0f);
			GL.glVertex3f(1.0f, -1.0f, 0.0f);
			GL.glVertex3f(-1.0f, -1.0f, 0.0f);
		}
		GL.glEnd();
	}

	void glInit() {
		GL.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		GL.glColor3f(0.0f, 0.0f, 0.0f);
		GL.glClearDepth(1.0f);
		GL.glEnable(GL.GL_DEPTH_TEST);
		GL.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
	}

	void setupProjection(Rectangle size) {
		double px2gl = px2gl(size);
		double glX = size.width * px2gl;
		double glY = size.height * px2gl;
		double left = leftAnchor - glX / 2;
		double right = left + glX;
		double bottom = topAnchor - glY / 2;
		double top = bottom + glY;
		GLU.gluOrtho2D(left, right, bottom, top);
	}
	
	double px2gl(Rectangle size) {
		return scale / Math.min(size.width, size.height);
	}

	void handleResize() {
		Rectangle size = glCanvas.getClientArea();
		GL.glMatrixMode(GL.GL_PROJECTION);
		GL.glLoadIdentity();
		{
			GL.glViewport(0, 0, size.width, size.height);
			setupProjection(size);
		}
		GL.glMatrixMode(GL.GL_MODELVIEW);
		GL.glLoadIdentity();
	}

	void parseFile(IEditorInput input) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			InputStream inputStream = ((FileEditorInput) input).getFile()
					.getContents();
			DocumentBuilder parser = factory.newDocumentBuilder();
			Document document = parser.parse(inputStream);
			Element model = document.getDocumentElement();
			NodeList anchors = model.getElementsByTagName("anchor");
			for (int i = 0; i < anchors.getLength(); i++) {
				Node anchor = anchors.item(i);
				System.out.println(anchor.getAttributes().getNamedItem("id")
						.getTextContent());
			}
		} catch (CoreException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setSite(site);
		setInput(input);
		// parseFile(input);
	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
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

	void doInit() {
		synchronized (GL.class) {
			glCanvas.setCurrent();
			glInit();
		}
	}
	
	void doRefresh() {
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
		glCanvas.addListener(SWT.MouseWheel, new Listener() {
			public void handleEvent(Event event) {
				double factor = (double) event.count / zoomSpeed;
				double newScale = scale * (1 + factor);
				if (newScale > maxScale || newScale < minScale) return;
				Rectangle size = glCanvas.getClientArea();
				double oldpx2gl = px2gl(size);
				scale = newScale;
				double dpx2gl = px2gl(size) - oldpx2gl;
				leftAnchor -= (event.x - (double) size.width / 2) * dpx2gl;
				topAnchor += (event.y - (double) size.height / 2) * dpx2gl;
				doRefresh();
			}
		});
		glCanvas.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				double nudge = panSpeed * px2gl(glCanvas.getClientArea());
				switch (e.keyCode) {
				case SWT.ARROW_LEFT:
					leftAnchor -= nudge;
					break;
				case SWT.ARROW_RIGHT:
					leftAnchor += nudge;
					break;
				case SWT.ARROW_UP:
					topAnchor += nudge;
					break;
				case SWT.ARROW_DOWN:
					topAnchor -= nudge;
					break;
				}
				doRefresh();
			}

			public void keyReleased(KeyEvent e) {
			}
		});
		glCanvas.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				doResize();
			}
		});
		topAnchor = 0;
		leftAnchor = 0;
		scale = 4;
		zoomSpeed = 10;
		panSpeed = 10;
		minScale = 0.5;
		maxScale = 100;
		doInit();
	}

	@Override
	public void setFocus() {
		glCanvas.setFocus();
	}

}
