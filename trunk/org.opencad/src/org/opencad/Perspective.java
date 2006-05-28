package org.opencad;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		
		layout.addStandaloneView(NavigationView.ID,  false, IPageLayout.LEFT, 0.25f, editorArea);
		layout.addView("org.eclipse.swt.examples.openglview.view", IPageLayout.LEFT, 0.5f, editorArea);
		layout.getViewLayout(NavigationView.ID).setCloseable(false);
	}
}
