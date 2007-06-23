package org.opencad.ui;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class PerspectiveFactory implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();

		IFolderLayout left = layout.createFolder("left",
				IPageLayout.LEFT, 0.25f, editorArea);

		left.addView(IPageLayout.ID_RES_NAV);
		left.addView(IPageLayout.ID_OUTLINE);

		IFolderLayout bottomLeft = layout.createFolder("bottomLeft",
				IPageLayout.BOTTOM, 0.50f, "left");
		bottomLeft.addView("org.opencad.core.glView");
	}
}