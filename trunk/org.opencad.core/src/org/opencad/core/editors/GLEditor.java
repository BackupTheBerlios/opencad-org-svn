package org.opencad.core.editors;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

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
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
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
	
	float x = 0;
	
	void renderGLCanvas() {
	    GL.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
	    GL.glLoadIdentity();
	    GL.glTranslatef(0.0f, 0.0f, -6.0f);
	    GL.glRotatef(x++, 1, 0, 0);
	    GL.glBegin(GL.GL_QUADS);
	    GL.glVertex3f(-1.0f, 1.0f, 0.0f);
	    GL.glVertex3f(1.0f, 1.0f, 0.0f);
	    GL.glVertex3f(1.0f, -1.0f, 0.0f);
	    GL.glVertex3f(-1.0f, -1.0f, 0.0f);
	    GL.glEnd();
	}

	void initGLCanvas() {
		GL.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		GL.glColor3f(0.0f, 0.0f, 0.0f);
		GL.glClearDepth(1.0f);
		GL.glEnable(GL.GL_DEPTH_TEST);
		GL.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
	}

	void resizeGLCanvas() {
		Rectangle rect = glCanvas.getClientArea();
		int width = rect.width;
		int height = Math.max(rect.height, 1);
		GL.glViewport(0, 0, width, height);
		GL.glMatrixMode(GL.GL_PROJECTION);
		GL.glLoadIdentity();
		float aspect = (float) width / (float) height;
		GLU.gluPerspective(45.0f, aspect, 0.5f, 400.0f);
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
			//
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
		parseFile(input);
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

	@Override
	public void createPartControl(Composite parent) {
		GLData data = new GLData();
		data.doubleBuffer = true;
		glCanvas = new GLCanvas(parent, SWT.NO_BACKGROUND, data);
		glCanvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				glCanvas.setCurrent();
				renderGLCanvas();
				glCanvas.swapBuffers();
			}
		});
		glCanvas.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				glCanvas.setCurrent();
				resizeGLCanvas();
			}
		});
		glCanvas.setCurrent();
		initGLCanvas();
	}

	@Override
	public void setFocus() {
	}

}
