package org.opencad.ui.editors;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.LinkedList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.opengl.GL;
import org.eclipse.opengl.GLU;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
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
import org.opencad.modelling.Model;
import org.opencad.ui.editors.state.GLEditorState;
import org.opencad.ui.editors.state.NavigationState;

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

	private Model model;

	public Rectangle getCanvasClientArea() {
		return glCanvas.getClientArea();
	}

	public final Model getModel() {
		return model;
	}

	public GLEditor() {
		super();
	}

	public void dispose() {
		glCanvas.dispose();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		FileEditorInput fileEditorInput = (FileEditorInput) getEditorInput();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			model.setDirty(false);
			oos.writeObject(model);
			ByteArrayInputStream is = new ByteArrayInputStream(baos.toByteArray());
			fileEditorInput.getFile().setContents(is, 0, monitor);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void doSaveAs() {
	}

	void glDraw() {
		GL.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		GL.glLoadIdentity();
		GL.glBegin(GL.GL_LINES);
		{
			GL.glVertex2d(0d, 0d);
			GL.glVertex2d(1d, 0d);
			GL.glVertex2d(0d, 0d);
			GL.glVertex2d(0d, 1d);
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

	public double px2gl(Rectangle size) {
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
		model = null;
		try {
			FileEditorInput fei = (FileEditorInput) input;
			InputStream is = fei.getFile().getContents();
			ObjectInputStream ois = new ObjectInputStream(is);
			model = (Model) ois.readObject();
		} catch (CoreException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (model == null) {
			model = new Model();
		}
		System.out.println(model.toString());
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setSite(site);
		setInput(input);
		stateStack = new LinkedList<GLEditorState>();
		parseFile(input);
	}

	@Override
	public boolean isDirty() {
		return model.isDirty();
	}

	@Override
	public boolean isSaveAsAllowed() {
		return true;
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

	public void doRefresh() {
		doResize();
		doDraw();
	}

	LinkedList<GLEditorState> stateStack;

	private void removeListeners(GLEditorState state) {
		if (state.getKeyListener() != null) {
			glCanvas.removeKeyListener(state.getKeyListener());
		}
		if (state.getMouseWheelListener() != null) {
			glCanvas.removeListener(SWT.MouseWheel, state.getMouseWheelListener());
		}
		if (state.getMouseListener() != null) {
			glCanvas.removeMouseListener(state.getMouseListener());
		}
		if (state.getMouseMoveListener() != null) {
			glCanvas.removeMouseMoveListener(state.getMouseMoveListener());
		}
		if (state.getMouseTrackListener() != null) {
			glCanvas.removeMouseTrackListener(state.getMouseTrackListener());
		}
	}

	private void addListeners(GLEditorState state) {
		if (state.getKeyListener() != null) {
			glCanvas.addKeyListener(state.getKeyListener());
		}
		if (state.getMouseWheelListener() != null) {
			glCanvas.addListener(SWT.MouseWheel, state.getMouseWheelListener());
		}
		if (state.getMouseListener() != null) {
			glCanvas.addMouseListener(state.getMouseListener());
		}
		if (state.getMouseMoveListener() != null) {
			glCanvas.addMouseMoveListener(state.getMouseMoveListener());
		}
		if (state.getMouseTrackListener() != null) {
			glCanvas.addMouseTrackListener(state.getMouseTrackListener());
		}
	}

	public void stateChanged(GLEditorState state) {
		switch (state.getStatus()) {
			case GLEditorState.FRESH:
				if (!stateStack.isEmpty()) {
					GLEditorState first = stateStack.getFirst();
					first.sleep();
					removeListeners(first);
				}
				if (stateStack.contains(state)) {
					stateStack.remove(state);
				}
				stateStack.addFirst(state);
				addListeners(state);
				state.run();
			break;
			case GLEditorState.TERMINATED:
				if (stateStack.contains(state)) {
					stateStack.remove(state);
					removeListeners(state);
				}
			break;
		}
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
		topAnchor = 0;
		leftAnchor = 0;
		scale = 4;
		zoomSpeed = 10;
		panSpeed = 10;
		minScale = 0.5;
		maxScale = 100;
		NavigationState navigationState = new NavigationState(this);
		stateChanged(navigationState);
		doInit();
	}

	@Override
	public void setFocus() {
		glCanvas.setFocus();
	}

	public final double getLeftAnchor() {
		return leftAnchor;
	}

	public final void setLeftAnchor(double leftAnchor) {
		this.leftAnchor = leftAnchor;
	}

	public final double getScale() {
		return scale;
	}

	public final void setScale(double scale) {
		this.scale = scale;
	}

	public final double getTopAnchor() {
		return topAnchor;
	}

	public final void setTopAnchor(double topAnchor) {
		this.topAnchor = topAnchor;
	}

	public final double getMaxScale() {
		return maxScale;
	}

	public final void setMaxScale(double maxScale) {
		this.maxScale = maxScale;
	}

	public final double getMinScale() {
		return minScale;
	}

	public final void setMinScale(double minScale) {
		this.minScale = minScale;
	}

	public final int getPanSpeed() {
		return panSpeed;
	}

	public final void setPanSpeed(int panSpeed) {
		this.panSpeed = panSpeed;
	}

	public final int getZoomSpeed() {
		return zoomSpeed;
	}

	public final void setZoomSpeed(int zoomSpeed) {
		this.zoomSpeed = zoomSpeed;
	}

}
