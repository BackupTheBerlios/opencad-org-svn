package org.opencad.corners.ui;

import java.util.logging.Logger;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.opencad.corners.modelling.Corner;
import org.opencad.ui.editors.GLEditor;
import org.opencad.ui.editors.state.GLEditorState;

public class SelectCornerState extends GLEditorState implements MouseListener {

	@Override
	public MouseListener getMouseListener() {
		return this;
	}

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
		Logger.getAnonymousLogger().info("?");
		super.run();
	}

	@Override
	public void sleep() {
		Logger.getAnonymousLogger().info("?");
		super.sleep();
	}

	@Override
	public void terminate() {
		Logger.getAnonymousLogger().info("?");
		super.terminate();
	}

	public void mouseDown(MouseEvent e) {
		if (corner.isHover()) {
			if (!started) {
				new DragCornerState(glEditor, corner).notifyEditor();
				started = true;
			}
		} else {
			terminate();
			notifyEditor();
		}
	}

	public void mouseUp(MouseEvent e) {
		Logger.getAnonymousLogger().info(String.format("%s", started));
		if (started) {
			terminate();
			notifyEditor();
		}
	}
}
