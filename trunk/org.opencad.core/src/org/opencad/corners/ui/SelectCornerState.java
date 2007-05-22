package org.opencad.corners.ui;

import org.eclipse.swt.events.MouseEvent;
import org.opencad.corners.modelling.Corner;
import org.opencad.ui.editors.GLEditor;
import org.opencad.ui.editors.state.GLEditorState;

public class SelectCornerState extends GLEditorState {

	boolean started = false;

	Corner corner;

	public SelectCornerState(GLEditor glEditor, Corner corner) {
		super(glEditor);
		this.corner = corner;
	}

	public void mouseDoubleClick(MouseEvent e) {
	}
	
	@Override
	public void run() {
		if (started || !corner.isHover()) {
			terminate();
		} else {
			super.run();
			new DragCornerState(glEditor, corner).freshen();
			started = true;
		}
	}
}
