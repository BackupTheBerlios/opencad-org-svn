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
package org.eclipse.swt.examples.openglview.view;

import org.eclipse.swt.examples.openglview.GLScene;
import org.eclipse.swt.examples.openglview.Refresher;

import org.eclipse.swt.examples.openglview.examples.ChartScene;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

/**
 * An example view that uses GLScene to display its content.
 * 
 * @author Bo Majewski
 */
public class GLView extends ViewPart {
    private GLScene scene;

    public void createPartControl(Composite parent) {
        this.scene = new ChartScene(parent);
        new Refresher(this.scene).run();
    }

    public void setFocus() {
        this.scene.setFocus();
    }
}
