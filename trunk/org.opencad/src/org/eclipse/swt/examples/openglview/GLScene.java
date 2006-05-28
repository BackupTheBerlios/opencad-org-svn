/*******************************************************************************
 * Copyright (c) 2005 Bo Majewski 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/org/documents/epl-v10.html
 * 
 * Contributors:
 *     Bo Majewski - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.openglview;

import java.io.InputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import org.eclipse.opengl.GL;
import org.eclipse.opengl.GLU;

/**
 * Creates an OpenGL scene. In order to draw on it, overrider the 
 * <code>drawScene</code> method.
 *  
 * @author Bo Majewski
 */
public class GLScene {
    private GLCanvas canvas;
    
    /**
     * Creates a new scene owned by the specified parent component.
     * 
     * @param parent The parent of this scene.
     */
    public GLScene(Composite parent) {
    	GLData data = new GLData();
    	data.depthSize = 1;
    	data.doubleBuffer = true;
        this.canvas = new GLCanvas(parent, SWT.NO_BACKGROUND, data);
        
        this.canvas.addControlListener(new ControlAdapter() {
            public void controlResized(ControlEvent e) {
                resizeScene();
            }
        });  
        this.canvas.addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
                dispose();
            }
        });
        this.init();
        Rectangle clientArea = parent.getClientArea();
        this.canvas.setSize(clientArea.width, clientArea.height);
    }
    
    /**
     * Initializes this scene, by calling the <code>initGLContext</code> and
     * <code>initGL</code> method.
     */
    protected void init() {
        this.initGLContext();
        this.initGL();
    }
    
    /**
     * Disposes of the GL context. This method is called when the canvas is
     * disposed.
     */
    protected void dispose() {}
    
    /**
     * Returns whether or not this scene is disposed.
     * 
     * @return Whether or not the scene is disposed.
     */
    public boolean isDisposed() {
        return this.canvas.isDisposed();
    }
    
    /**
     * Causes the receiver to have the <em>keyboard focus</em>, 
     *
     * @return Whether or not the control got focus.
     */
    public boolean setFocus() {
        return this.canvas.setFocus();
    }

    /**
     * Provides direct access to this GL Canvas.
     * 
     * @return The GL canvas of this scene.
     */
    protected GLCanvas getGLContext() {
        return this.canvas;
    }

    /**
     * Returns the drawable used by the GLContext to render GL scenes.
     * 
     * @return The canvas used by the GL context.
     */
    protected Canvas getCanvas() {
        return this.canvas;
    }
    
    public Display getDisplay() {
        return this.canvas.getDisplay();
    }
    
    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------
    
    /**
     * Renders the next scene.
     */
    public void render() {
        if (!this.canvas.isCurrent()) {
            this.canvas.setCurrent();
        }
        
        this.drawScene();
        this.canvas.swapBuffers();
    }
    
    // -------------------------------------------------------------------------
    // Utilities
    // -------------------------------------------------------------------------
    
    /**
     * Loads and returns ImageData that may be used as texture with the 
     * <code>GL.glTexImage2D</code> method. 
     */
    protected ImageData getTextureImageData(String resource) {
        return this.getTextureImageData(this.getClass().getResourceAsStream(resource));
    }
    
    /**
     * Loads and returns ImageData that may be used as texture with the 
     * <code>GL.glTexImage2D</code> method. 
     */
    protected ImageData getTextureImageData(InputStream is) {
        ImageData source = new ImageData(is);
        Image resized = null, original = null;
        
        if (!((source.width == 256 && source.height == 256) ||
            (source.height == 128 && source.width == 128) ||
            (source.height == 64 && source.width == 64))) {
            original = new Image(this.canvas.getDisplay(), source);
            resized = new Image(this.canvas.getDisplay(), 256, 256);
            GC gc = new GC(resized);
            gc.drawImage(original, 0, 0, source.width, source.height, 0, 0, 256, 256);
            source = resized.getImageData();
            gc.dispose();
        }
        
        source = ImageDataUtil.convertImageData(source);
        
        if (original != null) {
            resized.dispose();
            original.dispose();
        }
        
        return source;
    }

    // -------------------------------------------------------------------------
    // OpenGL
    // -------------------------------------------------------------------------
    
    protected void initGLContext() {
        this.canvas.setCurrent(); // Activate the rendering context
    }
    
    /**
     * Initializes OpenGL by creating a context, setting background, shading
     * model, and such.
     */
    protected void initGL() {
        GL.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GL.glClearDepth(1.0f);
        GL.glDepthFunc(GL.GL_LESS);
        GL.glEnable(GL.GL_DEPTH_TEST);
        GL.glShadeModel(GL.GL_SMOOTH);
        GL.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
    }

    /**
     * Corrects the size of the GL scene.
     */
    protected void resizeScene() {
        Rectangle rect = this.canvas.getClientArea();
        GL.glViewport(0, 0, rect.width, rect.height);
        GL.glMatrixMode(GL.GL_PROJECTION);
        GL.glLoadIdentity();
        GLU.gluPerspective(45.0f, (float) rect.width / (float) rect.height, 0.1f, 100.0f);
        GL.glMatrixMode(GL.GL_MODELVIEW);
        GL.glLoadIdentity();
    }
    
    /**
     * Draws the GL scene. The default implementation clears the scene and
     * resets the view.
     */
    protected void drawScene() {
        GL.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        GL.glLoadIdentity(); 
    }
}
