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
 * A conveninece class for creating compiled lists of commands.
 * 
 * @author Bo Majewski
 */
public abstract class CompiledShape {
    private int listIndex;
    
    public CompiledShape() {
        this.listIndex = GL.glGenLists(1);
    }
    
    public int getListIndex() {
        return this.listIndex;
    }
    
    public void draw() {
        GL.glCallList(this.getListIndex());
    }
    
    public void dispose() {
        GL.glDeleteLists(this.getListIndex(), 1);
    }
}
