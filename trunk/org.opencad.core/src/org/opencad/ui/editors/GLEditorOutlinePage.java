package org.opencad.ui.editors;

import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;
import org.opencad.model.modelling.Primitive;
import org.opencad.ui.behaviour.Selectable;

public class GLEditorOutlinePage extends ContentOutlinePage {
	final GLEditor editor;

	GLEditorTreeContentProvider contentProvider;

	public GLEditorOutlinePage(GLEditor editor) {
		super();
		this.editor = editor;
	}

	@Override
	public void init(IPageSite pageSite) {
		super.init(pageSite);
		pageSite.getSelectionProvider();
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		Primitive selection = (Primitive) ((TreeSelection) event.getSelection())
				.getFirstElement();
		editor.setSelection((Selectable) selection);
		super.selectionChanged(event);
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
