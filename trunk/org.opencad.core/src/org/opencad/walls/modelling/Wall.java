package org.opencad.walls.modelling;

import org.opencad.corners.modelling.Corner;
import org.opencad.model.modelling.Primitive;
import org.opencad.model.modelling.PrimitiveTypeRegister;
import org.opencad.walls.rendering.WallEditorRenderer;

public class Wall extends Primitive {

	private static final long serialVersionUID = -8662848078904155699L;
	
	static {
		PrimitiveTypeRegister.registerPrimitiveType(Wall.class);
	}

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
		this.addRenderer(new WallEditorRenderer(this));
	}

	public String toString() {
		return String.format("%s=%s",startingCorner, endingCorner);
	}
}
