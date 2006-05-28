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
package org.eclipse.swt.examples.openglview.examples;

import org.eclipse.swt.examples.openglview.CompiledShape;
import org.eclipse.swt.examples.openglview.GLScene;
import org.eclipse.swt.examples.openglview.SceneGrip;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.opengl.GL;
import org.eclipse.opengl.GLU;

/**
 * A 3D cylinder chart.
 * 
 * @author Bo Majewski
 */
public class ChartScene extends GLScene {
    public static final int ROW_LENGTH = 7;
    public static final int CHART_COUNT = 4;
    
    private static final float[][] COLOR = {
        {1.0f, 1.0f, 0.0f, 0.7f},
        {0.0f, 1.0f, 0.0f, 0.7f},
        {0.0f, 0.0f, 1.0f, 0.7f},
        {1.0f, 0.0f, 1.0f, 0.7f},
    };

    private BarValue[][] chart;
    private Axis axis;
    private SceneGrip grip;
    
    public ChartScene(Composite parent) {
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
        
        BarValue.QUADRIC = GLU.gluNewQuadric();
        GL.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        GL.glEnable(GL.GL_BLEND);
        GL.glEnable(GL.GL_LINE_SMOOTH);
        GLU.gluQuadricNormals(BarValue.QUADRIC, GLU.GLU_SMOOTH);

        GL.glLightfv(GL.GL_LIGHT1, 
                     GL.GL_DIFFUSE, 
                     new float[] {1.0f, 1.0f, 1.0f, 1.0f});
        GL.glLightfv(GL.GL_LIGHT1, 
                     GL.GL_AMBIENT, 
                     new float[] {0.5f, 0.5f, 0.5f, 1.0f});
        GL.glLightfv(GL.GL_LIGHT1, 
                     GL.GL_POSITION, 
                     new float[] {-50.f, 50.0f, 100.0f, 1.0f});
        GL.glEnable(GL.GL_LIGHT1);
        GL.glEnable(GL.GL_LIGHTING);
        GL.glEnable(GL.GL_COLOR_MATERIAL);
        GL.glColorMaterial(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE);

        this.axis = new Axis(15.0f, 9.0f, 11.0f);
        this.chart = new BarValue[CHART_COUNT][ROW_LENGTH];
        double slice = Math.PI/ROW_LENGTH;
        
        for (int i = 0; i < this.chart.length; ++ i) {
            BarValue[] value = this.chart[i];
            double shift = i*Math.PI/4.0;
            
            for (int j = 1; j <= value.length; ++ j) {
                value[j-1] = new BarValue((float) (8.0*Math.abs(Math.sin(slice*j - shift))));
            }
        }
    }
    
    protected void drawScene() {
        super.drawScene();
        this.grip.adjust();

        GL.glLineWidth(1.0f);
        this.axis.draw();
        GL.glTranslatef(BarValue.RADIUS, 0.0f, BarValue.RADIUS);
        
        for (int i = 0; i < this.chart.length; ++ i) {
            BarValue[] value = this.chart[i];
            GL.glColor4fv(COLOR[i % COLOR.length]);
            
            for (int j = 0; j < value.length; ++ j) {
                value[j].draw();
                GL.glTranslatef(2.0f*BarValue.RADIUS, 0.0f, 0.0f);
            }
            
            GL.glTranslatef(-2.0f*BarValue.RADIUS*value.length, 
                            0.0f, 
                            2.0f*BarValue.RADIUS + 0.5f);
        }
    }    

    public void dispose() {
        GLU.gluDeleteQuadric(BarValue.QUADRIC);
        
        for (int i = 0; i < this.chart.length; ++ i) {
            BarValue[] value = this.chart[i];
            
            for (int j = 0; j < value.length; ++ j) {
                value[j].dispose();
                value[j] = null;
            }
        }
        
        this.axis.dispose();
        super.dispose();
    }
    
    private static class Axis extends CompiledShape {
        private static float[] COLOR1 = new float[] {0.6f, 0.6f, 0.6f, 0.3f};
        private static float[] COLOR2 = new float[] {1.0f, 1.0f, 1.0f, 1.0f};
        private static float[] COLOR3 = new float[] {0.6f, 0.0f, 0.0f, 1.0f};
        
        public Axis(float x, float y, float z) {
            GL.glNewList(this.getListIndex(), GL.GL_COMPILE);
                GL.glBegin(GL.GL_QUADS);
                    GL.glColor4fv(COLOR1);
                    GL.glVertex3f(0.0f, y, z);
                    GL.glVertex3f(0.0f, -1.0f, z);
                    GL.glVertex3f(0.0f, -1.0f, -1.0f);
                    GL.glVertex3f(0.0f, y, -1.0f);
                    
                    GL.glVertex3f(-1.0f, y, 0.0f);
                    GL.glVertex3f(-1.0f, -1.0f, 0.0f);
                    GL.glVertex3f(x, -1.0f, 0.0f);
                    GL.glVertex3f(x, y, 0.0f);
                GL.glEnd();
                
                GL.glColor4fv(COLOR2);
                for (float a = 1.0f; a < y; a += 1.0f) {
                    GL.glBegin(GL.GL_LINE_STRIP);
                        GL.glVertex3f(0.1f, a, z);
                        GL.glVertex3f(0.1f, a, 0.1f);
                        GL.glVertex3f(x, a, 0.1f);
                    GL.glEnd();
                }
                
                GL.glColor4fv(COLOR3);
                GL.glBegin(GL.GL_LINE_STRIP);
                    GL.glVertex3f(0.1f, 0.0f, z);
                    GL.glVertex3f(0.1f, 0.0f, 0.1f);
                    GL.glVertex3f(x, 0.0f, 0.1f);
                GL.glEnd();
                GL.glBegin(GL.GL_LINES);
                    GL.glVertex3f(0.1f, -1.0f, 0.1f);
                    GL.glVertex3f(0.1f, y, 0.1f);
                GL.glEnd();
            GL.glEndList();
        }
    }
    
    private static class BarValue extends CompiledShape {
        public static final float RADIUS = 1.0f;
        public static int QUADRIC;
        
        public BarValue(float value) {
            GL.glNewList(this.getListIndex(), GL.GL_COMPILE);
            GL.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
            GLU.gluCylinder(BarValue.QUADRIC, RADIUS, RADIUS, value, 32, 1);
            GLU.gluDisk(BarValue.QUADRIC, 0.0, RADIUS, 32, 32);
            GL.glTranslatef(0.0f, 0.0f, value);
            GLU.gluDisk(BarValue.QUADRIC, 0.0, RADIUS, 32, 32);
            GL.glTranslatef(0.0f, 0.0f, -value);
            GL.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
            GL.glEndList();
        }
    }
}
