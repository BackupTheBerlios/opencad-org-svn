package org.opencad.walls.ui;

import org.opencad.corners.modelling.Corner;
import org.opencad.walls.modelling.Wall;

public interface CornerSetter {
	void changeCornerOf(Wall wall, Corner corner);
}
