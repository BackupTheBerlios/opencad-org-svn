package org.opencad.ui.editor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.opengl.GL;
import org.eclipse.opengl.GLU;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.opencad.modelling.Model;
import org.opencad.ui.Activator;

public class GLEditor extends EditorPart implements ISelectionChangedListener {
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

  private boolean dirty;

  private GLEditorOutlinePage outlinePage;

  public Rectangle getCanvasClientArea() {
    return glCanvas.getClientArea();
  }

  public void setSelection(final Selectable selection) {
    getSite().getSelectionProvider().removeSelectionChangedListener(this);
    if (model.setSelection(selection)) {
      getSite().getSelectionProvider().setSelection(
          new StructuredSelection(selection));
    }
    getSite().getSelectionProvider().addSelectionChangedListener(this);
    selection.getSelectionState(this).freshen();
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
      setDirty(false);
      oos.writeObject(model);
      oos.close();
      ByteArrayInputStream is = new ByteArrayInputStream(baos.toByteArray());
      fileEditorInput.getFile().setContents(is, 0, monitor);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (CoreException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void doSaveAs() {
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

  public void drawGrid() {
    final int gridCount = 1000;
    final int gridSkip = 5;
    final double gridSize = scale * 0.05;
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

  void glDraw() {
    GL.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
    GL.glLoadIdentity();
    GL.glPushMatrix();
    {
      GL.glTranslated(0d, 0d, -0.1d);
      drawAnchor();
      GL.glTranslated(0d, 0d, -0.1d);
      drawGrid();
    }
    GL.glPopMatrix();
    model.editorRender();
  }

  void glInit() {
    GL.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
    GL.glColor3f(0.0f, 0.0f, 0.0f);
//    GL.glEnable(GL.GL_LINE_SMOOTH);
//    GL.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
//    GL.glEnable(GL.GL_BLEND);
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
      IFile file = fei.getFile();
      this.setPartName(file.getName());
      InputStream is = file.getContents();
      ObjectInputStream ois = new ObjectInputStream(is);
      model = (Model) ois.readObject();
    } catch (EOFException e) {
      Activator.error("Probably new file", e);
    } catch (CoreException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    } catch (IllegalArgumentException e) {
      throw new RuntimeException(e);
    }
    if (model == null) {
      model = new Model();
    }
  }

  public void selectionChanged(SelectionChangedEvent event) {
    Object selection = ((StructuredSelection) event.getSelection())
        .getFirstElement();
    if (selection instanceof Selectable) {
      setSelection((Selectable) selection);
    }
  }

  @Override
  public void init(IEditorSite site, IEditorInput input)
      throws PartInitException {
    setSite(site);
    setInput(input);
    stateStack = new LinkedList<GLEditorState>();
    outlinePage = new GLEditorOutlinePage(this);
    site.setSelectionProvider(outlinePage);
    site.getSelectionProvider().addSelectionChangedListener(this);
    parseFile(input);
  }

  @Override
  public boolean isDirty() {
    return dirty;
  }

  public void setDirty(boolean dirty) {
    this.dirty = dirty;
    firePropertyChange(PROP_DIRTY);
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
      case FRESH:
        if (!stateStack.isEmpty()) {
          GLEditorState first = stateStack.getFirst();
          first.sleep();
          removeListeners(first);
        }
        if (stateStack.isEmpty() || stateStack.getFirst() != state) {
          stateStack.addFirst(state);
          addListeners(state);
          state.run();
        }
      break;
      case TERMINATED:
        if (!stateStack.isEmpty() && stateStack.getFirst() == state) {
          stateStack.removeFirst();
          removeListeners(state);
        }
        if (!stateStack.isEmpty()) {
          addListeners(stateStack.getFirst());
          stateStack.getFirst().run();
        }
      break;
    }
    doRefresh();
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

  @Override
  public Object getAdapter(Class adapter) {
    if (IContentOutlinePage.class.equals(adapter)) {
      return outlinePage;
    }
    return super.getAdapter(adapter);
  }
}
