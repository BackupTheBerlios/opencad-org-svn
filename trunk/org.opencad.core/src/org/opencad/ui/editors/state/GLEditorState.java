package org.opencad.ui.editors.state;

import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.widgets.Listener;
import org.opencad.ui.editors.GLEditor;

public abstract class GLEditorState {

	public enum Status {
		FRESH, RUNNING, SLEEPING, TERMINATED
	}

	private Status status;

	protected GLEditor glEditor;

	public final GLEditor getGlEditor() {
		return glEditor;
	}

	public final void setGlEditor(GLEditor glEditor) {
		this.glEditor = glEditor;
	}

	public void notifyEditor() {
		glEditor.stateChanged(this);
	}

	public Status getStatus() {
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
		this.status = Status.FRESH;
	}

	public void run() {
		this.status = Status.RUNNING;
	}

	public void sleep() {
		this.status = Status.SLEEPING;
	}

	public void terminate() {
		this.status = Status.TERMINATED;
	}

}
