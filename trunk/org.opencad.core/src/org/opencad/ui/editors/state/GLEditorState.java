package org.opencad.ui.editors.state;

import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.widgets.Listener;
import org.opencad.ui.editors.GLEditor;

public abstract class GLEditorState {

	public static final int FRESH = 0;

	public static final int RUNNING = 1;

	public static final int SLEEPING = 2;

	public static final int TERMINATED = 4;

	private int status;

	private GLEditor glEditor;

	protected void notifyEditor() {
		glEditor.stateChanged(this);
	}

	public int getStatus() {
		return this.status;
	}

	public GLEditorState(GLEditor glEditor) {
		this.glEditor = glEditor;
		freshen();
	}

	public KeyListener getKeyListener() {
		return null;
	}

	public MouseListener getMouseListener() {
		return null;
	}

	public MouseMoveListener getMouseMoveListener() {
		return null;
	}

	public MouseTrackListener getMouseTrackListener() {
		return null;
	}

	public Listener getMouseWheelListener() {
		return null;
	}

	public void freshen() {
		this.status = FRESH;
	}

	public void run() {
		this.status = RUNNING;
	}

	public void sleep() {
		this.status = SLEEPING;
	}

	public void terminate() {
		this.status = TERMINATED;
	}

}
