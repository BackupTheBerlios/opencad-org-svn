package org.opencad;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		layout.addView("org.opencad.views.modelling", IPageLayout.TOP, 0.5f, editorArea);
		layout.addView("org.opencad.views.preview", IPageLayout.TOP, 0.5f, editorArea);
	}
}
