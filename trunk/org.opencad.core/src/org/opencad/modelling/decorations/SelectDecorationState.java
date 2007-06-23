package org.opencad.modelling.decorations;

import org.opencad.ui.editor.GLEditor;
import org.opencad.ui.editor.GLEditorState;

public class SelectDecorationState extends GLEditorState {

	boolean started = false;

	Decoration decoration;

	public SelectDecorationState(GLEditor glEditor,
			Decoration decoration) {
		super(glEditor);
		this.decoration = decoration;
	}

	@Override
	public void run() {
		if (started || !decoration.isHover()) {
			terminate();
		} else {
			super.run();
			new PlaceDecorationState(glEditor, decoration, false)
					.freshen();
			started = true;
		}
	}
}
