package org.opencad.modelling;

import java.io.Serializable;

public class Wall implements Serializable {

	private static final long serialVersionUID = -8662848078904155699L;

	Corner startingCorner, endingCorner;

	public final Corner getEndingCorner() {
		return endingCorner;
	}

	public final void setEndingCorner(Corner endingCorner) {
		this.endingCorner = endingCorner;
	}

	public final Corner getStartingCorner() {
		return startingCorner;
	}

	public final void setStartingCorner(Corner startingCorner) {
		this.startingCorner = startingCorner;
	}

	public Wall(Corner startingCorner, Corner endingCorner) {
		this.startingCorner = startingCorner;
		this.endingCorner = endingCorner;
	}

	public String toString() {
		return "Wall {" + startingCorner + " -> " + endingCorner + "}";
	}
}
