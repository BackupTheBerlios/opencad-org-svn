package org.opencad.ui.editors;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

public class GLEditorOutlinePage extends ContentOutlinePage {
	final GLEditor editor;

	GLEditorTreeContentProvider contentProvider;

	public GLEditorOutlinePage(final GLEditor editor) {
		super();
		this.editor = editor;
		editor.addPropertyListener(new IPropertyListener() {
			public void propertyChanged(Object source, int propId) {
				if (propId == GLEditor.PROP_DIRTY) {
					getTreeViewer().setInput(editor.getModel());
				}
			}
		});
	}

	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		contentProvider = new GLEditorTreeContentProvider();
		getTreeViewer().setContentProvider(contentProvider);
		getTreeViewer().setLabelProvider(contentProvider);
		getTreeViewer().addSelectionChangedListener(this);
		getTreeViewer().setInput(editor.getModel());
	}
}