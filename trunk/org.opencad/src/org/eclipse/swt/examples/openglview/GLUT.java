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

import org.eclipse.opengl.GL;

/**
 * Simulates some of the GLUT functions.
 * 
 * @author Bo Majewski
 */
public class GLUT {
    public static final void wireCube(float size) {
        float neg = -0.5f*size;
        float pos = 0.5f*size;

        // front face
        GL.glBegin(GL.GL_LINE_LOOP);
        GL.glVertex3f(neg, neg, neg);
        GL.glVertex3f(pos, neg, neg);
        GL.glVertex3f(pos, pos, neg);
        GL.glVertex3f(neg, pos, neg);
        GL.glEnd();
        
        // back face
        GL.glBegin(GL.GL_LINE_LOOP);
        GL.glVertex3f(neg, neg, pos);
        GL.glVertex3f(pos, neg, pos);
        GL.glVertex3f(pos, pos, pos);
        GL.glVertex3f(neg, pos, pos);
        GL.glEnd();
        
        GL.glBegin(GL.GL_LINES);
        GL.glVertex3f(neg, neg, neg);
        GL.glVertex3f(neg, neg, pos);
        
        GL.glVertex3f(pos, neg, neg);
        GL.glVertex3f(pos, neg, pos);
        
        GL.glVertex3f(pos, pos, neg);
        GL.glVertex3f(pos, pos, pos);
        
        GL.glVertex3f(neg, pos, neg);
        GL.glVertex3f(neg, pos, pos);
        GL.glEnd();
    }
    
    public static final void solidCube(float size) {
        float neg = -0.5f*size;
        float pos = 0.5f*size;
        
        GL.glBegin(GL.GL_QUADS);
        // Front Face
        GL.glNormal3f(0.0f, 0.0f, 1.0f);
        GL.glVertex3f(neg, neg, pos);
        GL.glVertex3f(pos, neg, pos); 
        GL.glVertex3f(pos, pos, pos); 
        GL.glVertex3f(neg, pos, pos);
        
        // Back Face
        GL.glNormal3f( 0.0f, 0.0f, -1.0f);
        GL.glVertex3f(neg, neg, neg);
        GL.glVertex3f(neg, pos, neg); 
        GL.glVertex3f(pos, pos, neg);
        GL.glVertex3f(pos, neg, neg);
        
        // Top Face
        GL.glNormal3f( 0.0f, 1.0f, 0.0f);
        GL.glVertex3f(neg, pos, neg);
        GL.glVertex3f(neg, pos, pos);
        GL.glVertex3f(pos, pos, pos);
        GL.glVertex3f(pos, pos, neg);
        
        // Bottom Face
        GL.glNormal3f( 0.0f, -1.0f, 0.0f); 
        GL.glVertex3f(neg, neg, neg);
        GL.glVertex3f(pos, neg, neg);
        GL.glVertex3f(pos, neg, pos);
        GL.glVertex3f(neg, neg, pos); 
        
        // Right face
        GL.glNormal3f(1.0f, 0.0f, 0.0f);
        GL.glVertex3f(pos, neg, neg); 
        GL.glVertex3f(pos, pos, neg);
        GL.glVertex3f(pos, pos, pos);
        GL.glVertex3f(pos, neg, pos); 
        
        // Left Face
        GL.glNormal3f(-1.0f, 0.0f, 0.0f);
        GL.glVertex3f(neg, neg, neg); 
        GL.glVertex3f(neg, neg, pos); 
        GL.glVertex3f(neg, pos, pos);
        GL.glVertex3f(neg, pos, neg);
        GL.glEnd();
    }
    
    public static final void texturedCube(float size) {
        float neg = -0.5f*size;
        float pos = 0.5f*size;
        
        GL.glBegin(GL.GL_QUADS);
        // Front Face
        GL.glNormal3f(0.0f, 0.0f, 1.0f);
        GL.glTexCoord2f(0.0f, 0.0f);
        GL.glVertex3f(neg, neg, pos);
        GL.glTexCoord2f(1.0f, 0.0f);
        GL.glVertex3f(pos, neg, pos); 
        GL.glTexCoord2f(1.0f, 1.0f);
        GL.glVertex3f(pos, pos, pos); 
        GL.glTexCoord2f(0.0f, 1.0f);
        GL.glVertex3f(neg, pos, pos);
        
        // Back Face
        GL.glNormal3f( 0.0f, 0.0f, -1.0f);
        GL.glTexCoord2f(1.0f, 0.0f);
        GL.glVertex3f(neg, neg, neg);
        GL.glTexCoord2f(1.0f, 1.0f);
        GL.glVertex3f(neg, pos, neg); 
        GL.glTexCoord2f(0.0f, 1.0f);
        GL.glVertex3f(pos, pos, neg);
        GL.glTexCoord2f(0.0f, 0.0f);
        GL.glVertex3f(pos, neg, neg);
        
        // Top Face
        GL.glNormal3f( 0.0f, 1.0f, 0.0f);
        GL.glTexCoord2f(0.0f, 1.0f);
        GL.glVertex3f(neg, pos, neg);
        GL.glTexCoord2f(0.0f, 0.0f);
        GL.glVertex3f(neg, pos, pos);
        GL.glTexCoord2f(1.0f, 0.0f);
        GL.glVertex3f(pos, pos, pos);
        GL.glTexCoord2f(1.0f, 1.0f);
        GL.glVertex3f(pos, pos, neg);
        
        // Bottom Face
        GL.glNormal3f( 0.0f, -1.0f, 0.0f); 
        GL.glTexCoord2f(1.0f, 1.0f);
        GL.glVertex3f(neg, neg, neg);
        GL.glTexCoord2f(0.0f, 1.0f);
        GL.glVertex3f(pos, neg, neg);
        GL.glTexCoord2f(0.0f, 0.0f);
        GL.glVertex3f(pos, neg, pos);
        GL.glTexCoord2f(1.0f, 0.0f);
        GL.glVertex3f(neg, neg, pos); 
        
        // Right face
        GL.glNormal3f(1.0f, 0.0f, 0.0f);
        GL.glTexCoord2f(1.0f, 0.0f);
        GL.glVertex3f(pos, neg, neg); 
        GL.glTexCoord2f(1.0f, 1.0f);
        GL.glVertex3f(pos, pos, neg);
        GL.glTexCoord2f(0.0f, 1.0f);
        GL.glVertex3f(pos, pos, pos);
        GL.glTexCoord2f(0.0f, 0.0f);
        GL.glVertex3f(pos, neg, pos); 
        
        // Left Face
        GL.glNormal3f(-1.0f, 0.0f, 0.0f);
        GL.glTexCoord2f(0.0f, 0.0f);
        GL.glVertex3f(neg, neg, neg); 
        GL.glTexCoord2f(1.0f, 0.0f);
        GL.glVertex3f(neg, neg, pos); 
        GL.glTexCoord2f(1.0f, 1.0f);
        GL.glVertex3f(neg, pos, pos);
        GL.glTexCoord2f(0.0f, 1.0f);
        GL.glVertex3f(neg, pos, neg);
        GL.glEnd();
    }
}
