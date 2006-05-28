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
import org.eclipse.swt.examples.openglview.SceneGrip;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.opengl.GL;

/**
 * Draws a picture I needed for the article.
 * 
 * @author Bo Majewski
 */
public class Rotation extends GLScene {
    public static final float AXIS_SIZE = 5.0f;
    private SceneGrip grip;
    
    public Rotation(Composite parent) {
        super(parent);
        this.grip = new SceneGrip();
        this.grip.setOffsets(-3.25f, 3.25f, -30.5f);
        this.grip.setRotation(45.0f, -30.0f);
        
        this.getCanvas().addMouseListener(this.grip);
        this.getCanvas().addMouseMoveListener(this.grip);
        this.getCanvas().addListener(SWT.MouseWheel, this.grip);
        this.getCanvas().addKeyListener(this.grip);
    }
    
    protected void initGL() {
        super.initGL();
        
        GL.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        GL.glEnable(GL.GL_BLEND);
        GL.glEnable(GL.GL_LINE_SMOOTH);
    }
    
    protected void drawScene() {
        double angle, slice;
        float x = 0.0f, y = 0.0f, z = 0.0f;
        
        super.drawScene();
        this.grip.adjust();       
        
        GL.glColor3f(1.0f, 1.0f, 1.0f);
        GL.glLineWidth(2.0f);
        GL.glBegin(GL.GL_LINE_STRIP);
            GL.glVertex3f(0.0f, 0.0f, AXIS_SIZE);
            GL.glVertex3f(0.0f, 0.0f, 0.0f);
            GL.glVertex3f(AXIS_SIZE, 0.0f, 0.0f);
        GL.glEnd();
        GL.glBegin(GL.GL_LINES);
            GL.glVertex3f(0.0f, 0.0f, 0.0f);
            GL.glVertex3f(0.0f, AXIS_SIZE, 0.0f);
        GL.glEnd();
        GL.glBegin(GL.GL_LINE_STRIP);
            GL.glVertex3f(-0.1f, AXIS_SIZE-0.2f, 0.0f);
            GL.glVertex3f(0.0f, AXIS_SIZE, 0.0f);
            GL.glVertex3f(0.1f, AXIS_SIZE-0.2f, 0.0f);
        GL.glEnd();
        GL.glBegin(GL.GL_LINE_STRIP);
            GL.glVertex3f(AXIS_SIZE-0.2f, 0.0f, -0.1f);
            GL.glVertex3f(AXIS_SIZE, 0.0f, 0.0f);
            GL.glVertex3f(AXIS_SIZE-0.2f, 0.0f, 0.1f);
        GL.glEnd();
        GL.glBegin(GL.GL_LINE_STRIP);
            GL.glVertex3f(0.0f, -0.1f, AXIS_SIZE-0.2f);
            GL.glVertex3f(0.0f, 0.0f, AXIS_SIZE);
            GL.glVertex3f(0.0f, 0.1f, AXIS_SIZE-0.2f);
        GL.glEnd();
        
        angle = 0.0;
        slice = (2*Math.PI/100.0);
        
        GL.glTranslatef(AXIS_SIZE-1.0f, 0.0f, 0.0f);
        GL.glBegin(GL.GL_LINE_STRIP);
            GL.glVertex3f(-0.1f, 0.2f, 1.0f);
            GL.glVertex3f(0.0f, 0.0f, 1.0f);
            GL.glVertex3f(0.1f, 0.2f, 1.0f);
        GL.glEnd();
        GL.glBegin(GL.GL_LINE_STRIP);
        GL.glVertex3f(0.0f, 0.0f, 1.0f);
        
        for (int i = 0; i < 90; ++ i) {
            y = (float) Math.sin(angle);
            z = (float) Math.cos(angle);
            GL.glVertex3f(0.0f, y, z);
            angle += slice;
        }
        
        GL.glEnd();
        GL.glTranslatef(-AXIS_SIZE+1.0f, AXIS_SIZE-1.0f, 0.0f);
        angle = 0.0;
        GL.glBegin(GL.GL_LINE_STRIP);
            GL.glVertex3f(0.2f, 0.1f, 1.0f);
            GL.glVertex3f(0.0f, 0.0f, 1.0f);
            GL.glVertex3f(0.2f, -0.1f, 1.0f);
        GL.glEnd();
        GL.glBegin(GL.GL_LINE_STRIP);
        GL.glVertex3f(0.0f, 0.0f, 1.0f);
        
        for (int i = 0; i < 90; ++ i) {
            x = (float) Math.sin(angle);
            z = (float) Math.cos(angle);
            GL.glVertex3f(x, 0, z);
            angle += slice;
        }
        
        GL.glEnd();
        GL.glTranslatef(0.0f, -AXIS_SIZE+1.0f, AXIS_SIZE-1.0f);
        GL.glBegin(GL.GL_LINE_STRIP);
            GL.glVertex3f(0.2f, 1.0f, -0.1f);
            GL.glVertex3f(0.0f, 1.0f, 0.0f);
            GL.glVertex3f(0.2f, 1.0f, 0.1f);
        GL.glEnd();
        angle = 0.0;
        GL.glBegin(GL.GL_LINE_STRIP);
        GL.glVertex3f(0.0f, 1.0f, 0.0f);
        
        for (int i = 0; i < 90; ++ i) {
            x = (float) Math.sin(angle);
            y = (float) Math.cos(angle);
            GL.glVertex3f(x, y, 0.0f);
            angle += slice;
        }
        
        GL.glEnd();
    }
}
