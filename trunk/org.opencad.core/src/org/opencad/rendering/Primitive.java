package org.opencad.rendering;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Primitive implements Serializable {

	private ArrayList<Renderable> renderers;

	public boolean addRenderer(Renderable o) {
		return renderers.add(o);
	}

	public Primitive() {
		renderers = new ArrayList<Renderable>();
	}

	public void render(Class<? extends Renderable> type) {
		for (Renderable renderer : renderers) {
			if (type.isAssignableFrom(renderer.getClass())) {
				renderer.glRender();
			}
		}
	}

}
