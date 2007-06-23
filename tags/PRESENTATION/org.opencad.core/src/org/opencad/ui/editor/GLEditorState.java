package org.opencad.ui.editor;

import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.widgets.Listener;

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
		this.status = Status.FRESH;
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
		notifyEditor();
	}

	public void run() {
		this.status = Status.RUNNING;
		notifyEditor();
	}

	public void sleep() {
		this.status = Status.SLEEPING;
		notifyEditor();
	}

	public void terminate() {
		this.status = Status.TERMINATED;
		notifyEditor();
	}

}
