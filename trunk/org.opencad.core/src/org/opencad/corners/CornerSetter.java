package org.opencad.corners;

import org.opencad.walls.Wall;

public interface CornerSetter {
	void changeCornerOf(Wall wall, Corner corner);
}
