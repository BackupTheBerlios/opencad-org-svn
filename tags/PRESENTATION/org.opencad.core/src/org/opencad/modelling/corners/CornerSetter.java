package org.opencad.modelling.corners;

import org.opencad.modelling.walls.Wall;

public interface CornerSetter {
	void changeCornerOf(Wall wall, Corner corner);
}
