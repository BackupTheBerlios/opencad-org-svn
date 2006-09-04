package org.opencad.modelling;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

public class Model implements Serializable {

	private static final long serialVersionUID = -4303353063372058728L;

	private Hashtable<Integer, Wall> walls;

	public final Hashtable<Integer, Wall> getWalls() {
		return walls;
	}

	private boolean dirty;

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	public boolean isDirty() {
		return dirty;
	}

	public Model() {
		walls = new Hashtable<Integer, Wall>();
		dirty = false;
	}

	public String toString() {
		Enumeration<Integer> wallKeys = walls.keys();
		String ret = "Model {\n";
		while (wallKeys.hasMoreElements()) {
			ret += "  " + walls.get(wallKeys.nextElement()) + "\n";
		}
		return ret + "}";
	}
}
