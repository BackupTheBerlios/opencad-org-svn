package org.opencad.ui.view;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
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

  double fov, eyex, eyey, eyez, centerx, centery, centerz;

  double zoomSpeed;

  private double min_eyez, max_eyez;

  GLEditor glEditor;

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
    max_eyez = 50;
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
    selectionChanged(getSite().getPage().getActivePart(), getSite().getPage()
        .getSelection());
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
      double min = Math.min(size.height , size.width);
      double scale = 0.1d;
      double range = 200;
      double px2gl = scale / min;
      double glX = size.width * px2gl / 2;
      double glY = size.height * px2gl / 2;
      GL.glFrustum(-glX, +glX, -glY, glY, scale, range);
      checkBounds();
      GLU.gluLookAt(eyex, eyey, eyez, centerx, centery, centerz, 0, 0, 1);
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
    eyez = eyez < min_eyez ? min_eyez : eyez;
    eyez = eyez > max_eyez ? max_eyez : eyez;
    eyex = eyex > max_eyez ? max_eyez : eyex;
    eyey = eyey > max_eyez ? max_eyez : eyey;
    eyex = eyex < -max_eyez ? -max_eyez : eyex;
    eyey = eyey < -max_eyez ? -max_eyez : eyey;
  }

  public void mouseMove(MouseEvent e) {
    int dx = e.x - dragx;
    int dy = e.y - dragy;
    double angle = Math.atan2(eyey - centery, eyex - centerx);
    eyey += dy * zoomSpeed * Math.sin(angle) + dx * Math.cos(angle) * zoomSpeed;
    eyex += dx * zoomSpeed * Math.sin(angle) - dy * Math.cos(angle)
        * zoomSpeed;
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
      glEditor.getSite().getSelectionProvider().addSelectionChangedListener(
          this);
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