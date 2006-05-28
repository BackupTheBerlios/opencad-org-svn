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
package org.eclipse.swt.examples.openglview.extras;

import org.eclipse.swt.examples.openglview.GLScene;
import org.eclipse.swt.examples.openglview.GLUT;
import org.eclipse.swt.examples.openglview.SceneGrip;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.opengl.GL;

/**
 * Draws a few, colored, wire frame cubes.
 * 
 * @author Bo Majewski
 */
public class CubeCanvas extends GLScene {
    private SceneGrip grip;

    public CubeCanvas(Composite parent) {
        super(parent);
        
        this.grip = new SceneGrip();
        this.grip.setOffsets(0.0f, 0.0f, -15.0f);
        this.grip.setRotation(45.0f, -30.0f);
        
        this.getCanvas().addMouseListener(this.grip);
        this.getCanvas().addMouseMoveListener(this.grip);
        this.getCanvas().addListener(SWT.MouseWheel, this.grip);
        this.getCanvas().addKeyListener(this.grip);
    }
    
    protected void initGL() {
        super.initGL();
        
        GL.glEnable(GL.GL_LINE_SMOOTH);
        GL.glHint(GL.GL_LINE_SMOOTH_HINT, GL.GL_NICEST);
        GL.glEnable(GL.GL_BLEND);
        GL.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);        
    }
    
    protected void drawScene() {
        super.drawScene();
        this.grip.adjust();
        
        GL.glColor3f(1.0f, 1.0f, 1.0f);
        GL.glBegin(GL.GL_LINE_LOOP);
        GL.glVertex3f(-6.0f, -1.0f, -9.0f);
        GL.glVertex3f(6.0f, -1.0f, -9.0f);
        GL.glVertex3f(6.0f, -1.0f, 3.0f);
        GL.glVertex3f(-6.0f, -1.0f, 3.0f);
        GL.glEnd();

        GL.glTranslatef(-3.0f, 0.0f, -6.0f);
        GL.glColor3f(1.0f, 0.0f, 0.0f);
        GLUT.wireCube(2.0f);
        GL.glTranslatef(3.0f, 0.0f, 6.0f);
        GL.glColor3f(0.0f, 1.0f, 0.0f);
        GLUT.wireCube(2.0f);
        GL.glTranslatef(3.0f, 0.0f, -6.0f);
        GL.glColor3f(0.0f, 0.0f, 1.0f);
        GLUT.wireCube(2.0f);
    }
}
