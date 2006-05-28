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

import org.eclipse.swt.examples.openglview.GLScene;
import org.eclipse.swt.examples.openglview.Refresher;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Launches the main app.
 * 
 * @author Bo Majewski
 */
public class Launcher {
    public static final int WIDTH = 500;
    public static final int HEIGHT = 400;
    public static final int DELAY = 40;
    
    private Display display;
    private Shell shell;
    private GLScene scene;
    
    public Launcher() {
        this.display = new Display();
        this.shell = new Shell(this.display);
        this.createUI();
    }
    
    protected void createUI() {
        Rectangle screen = this.display.getClientArea();
        this.shell.setBounds((screen.width - WIDTH)/2,
                             (screen.height - HEIGHT)/2,
                             WIDTH, HEIGHT);
        this.shell.setLayout(new FillLayout());
        this.scene = new ChartScene(this.shell);
        this.shell.setText("OpenGL Chart");
    }
    
    public void run() {
        new Refresher(this.scene).run();
        this.shell.open();
        
        while (!this.shell.isDisposed()) {
            if (this.display.readAndDispatch()) {
                this.display.sleep();
            }
        }
        
        this.display.dispose();
    }
    
    public static void main(String[] args) {
        new Launcher().run();
    }
}
