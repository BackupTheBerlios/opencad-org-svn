package org.opencad.sandbox;

import java.util.Random;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * JFace example
 */
public class MyApplicationWindow extends ApplicationWindow {
	private Composite _composite;
	private ExitAction _exitAction = new ExitAction(this);

	private class ExitAction extends Action {
		ApplicationWindow _window;

		public ExitAction(ApplicationWindow window) {
			_window = window;
			setText("E&xit@Ctrl+X");
			setToolTipText("Exit Application");
		}
		public void run() {
			_window.close();
		}
	}

	private ChangeColorAction _changeColorAction = new ChangeColorAction();

	private class ChangeColorAction extends Action {
		private Color[] colors;;

		public ChangeColorAction() {
			Display d = Display.getDefault();
			setText("Change C&olor@Alt+C");
			setToolTipText("Change Color");
			colors = new Color[] { d.getSystemColor(SWT.COLOR_BLACK), d.getSystemColor(SWT.COLOR_BLUE),
			    d.getSystemColor(SWT.COLOR_RED), d.getSystemColor(SWT.COLOR_YELLOW),
			    d.getSystemColor(SWT.COLOR_GREEN) };
		}
		public void run() {
			Random generator = new Random();
			int index = generator.nextInt(4);
			Color color = colors[index];
			_composite.setBackground(color);
			setStatus(color.toString());
		}
	}

	protected MenuManager createMenuManager() {
		MenuManager menuManager = new MenuManager();
		MenuManager fileMenu = new MenuManager("&File");
		fileMenu.add(_changeColorAction);
		fileMenu.add(_exitAction);
		menuManager.add(fileMenu);
		return menuManager;
	}
	protected ToolBarManager createToolBarManager(int style) {
		ToolBarManager toolBarManager = new ToolBarManager(style);
		toolBarManager.add(_changeColorAction);
		toolBarManager.add(_exitAction);
		return toolBarManager;
	}
	public MyApplicationWindow(Shell shell) {
		super(shell);
		addMenuBar();
		addToolBar(SWT.FLAT | SWT.WRAP);
		addStatusLine();
	}
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("JFace Example");
	}
	protected void initializeBounds() {
		getShell().setSize(640, 480);
		getShell().setLocation(0, 0);
	}
	public MyApplicationWindow() {
		this(null);
	}
	protected Control createContents(Composite parent) {
		_composite = new Composite(parent, SWT.NONE);
		return _composite;
	}
	public static void main(String[] args) {
		MyApplicationWindow window = new MyApplicationWindow();
		window.setBlockOnOpen(true);
		window.open();
		Display.getCurrent().dispose();
	}
}