package org.opencad;

import org.eclipse.jface.action.Action;
import org.opencad.model.Corner;
import org.opencad.scene.ModellingScene;

public class AddCornerAction extends Action {
	public AddCornerAction() {
		setId(ICommandIds.CMD_OPEN);
		setText("Add Corner");
		setActionDefinitionId(ICommandIds.CMD_OPEN);
	}
	public void run() {
		ModellingScene.getInstance(null).add(new Corner(3, 5));
	}
}
