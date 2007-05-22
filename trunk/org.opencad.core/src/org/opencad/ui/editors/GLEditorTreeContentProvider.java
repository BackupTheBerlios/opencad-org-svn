package org.opencad.ui.editors;

import java.util.HashSet;
import java.util.logging.Logger;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.opencad.model.modelling.Model;
import org.opencad.model.modelling.Primitive;
import org.opencad.model.modelling.PrimitiveTypeRegister;

public class GLEditorTreeContentProvider implements ITreeContentProvider,
		ILabelProvider {

	Model model;

	public GLEditorTreeContentProvider() {
	}
	
	public Object[] getChildren(Object parentElement) {
		HashSet<Primitive> children = new HashSet<Primitive>();
		if (parentElement instanceof Class) {
			for (Primitive primitive : model.getPrimitives()) {
				if (((Class<? extends Primitive>) parentElement)
						.isAssignableFrom(primitive.getClass())) {
					children.add(primitive);
				}
			}
		}
		return children.toArray();
	}

	public Object getParent(Object element) {
		if (element instanceof Primitive) {
			return element.getClass();
		}
		return null;
	}

	public boolean hasChildren(Object element) {
		if (element instanceof Class) {
			for (Primitive primitive : model.getPrimitives()) {
				if (((Class<? extends Primitive>) element)
						.isAssignableFrom(primitive.getClass())) {
					return true;
				}
			}
		}
		return false;
	}

	public Object[] getElements(Object inputElement) {
		return PrimitiveTypeRegister.getPrimitiveTypes();
	}

	public void dispose() {
	}

	HashSet<ILabelProviderListener> listeners = new HashSet<ILabelProviderListener>(
			1);

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		model = (Model) newInput;
	}

	public Image getImage(Object element) {
		return null;
	}

	public String getText(Object element) {
		if (element instanceof Class) {
			return ((Class) element).getSimpleName();
		} else if (element instanceof Primitive) {
			return element.toString();
		}
		return "?";
	}

	public void addListener(ILabelProviderListener listener) {
		listeners.add(listener);
	}

	public boolean isLabelProperty(Object element, String property) {
		Logger.getAnonymousLogger().info("?");
		return false;
	}

	public void removeListener(ILabelProviderListener listener) {
		listeners.remove(listener);
	}
}