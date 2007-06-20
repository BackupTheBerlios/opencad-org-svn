package org.opencad.modelling.walls.features;

import org.opencad.ui.editor.GLEditor;
import org.opencad.ui.editor.GLEditorState;

public class SelectFeatureState extends GLEditorState {

	boolean started = false;

	WallFeature feature;

	public SelectFeatureState(GLEditor glEditor, WallFeature feature) {
		super(glEditor);
		this.feature = feature;
	}

	@Override
	public void run() {
		if (started || !feature.isHover()) {
			terminate();
		} else {
			super.run();
			new PlaceFeatureState(glEditor, feature).freshen();
			started = true;
		}
	}
}
