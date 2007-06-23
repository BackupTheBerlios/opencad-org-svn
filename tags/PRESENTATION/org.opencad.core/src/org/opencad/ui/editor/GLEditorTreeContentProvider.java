package org.opencad.ui.editor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Logger;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.opencad.modelling.Model;
import org.opencad.modelling.Primitive;
import org.opencad.modelling.PrimitiveTypeRegister;
import org.opencad.ui.Activator;

public class GLEditorTreeContentProvider implements
		ITreeContentProvider, ILabelProvider {

	Model model;

	public GLEditorTreeContentProvider() {
	}

	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof Class) {
			HashSet<Primitive> children = new HashSet<Primitive>();
			for (Primitive primitive : model.getPrimitives()) {
				if (((Class<? extends Primitive>) parentElement)
						.isAssignableFrom(primitive.getClass())) {
					children.add(primitive);
				}
			}
			return children.toArray();
		} else if (parentElement instanceof Outlineable) {
			return ((Outlineable) parentElement).getChildren();
		}
		return null;
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
		} else if (element instanceof Outlineable) {
			return ((Outlineable) element).hasChildren();
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

	public void inputChanged(Viewer viewer, Object oldInput,
			Object newInput) {
		model = (Model) newInput;
	}

	HashMap<String, Image> images = new HashMap<String, Image>();

	public Image getImage(Object element) {
		if (element instanceof ImageProvider) {
			ImageProvider ip = (ImageProvider) element;
			if (images.get(ip.getImage()) == null) {
				ImageDescriptor id = Activator.getImageDescriptor(ip
						.getImage());
				images.put(ip.getImage(), id.createImage());
			}
			return images.get(ip.getImage());
		}
		return null;
	}

	public String getText(Object element) {
		if (element instanceof String) {
			return (String) element;
		} else if (element instanceof Class) {
			return ((Class) element).getSimpleName();
		} else if (element instanceof Outlineable) {
			return ((Outlineable) element).getText();
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