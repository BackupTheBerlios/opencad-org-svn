package org.opencad;

import org.eclipse.jface.action.Action;
import org.opencad.model.Corner;
import org.opencad.scene.ModellingScene;

public class AddWallAction extends Action {
	public AddWallAction() {
		setId(ICommandIds.CMD_OPEN);
		setText("Add Wall");
		setActionDefinitionId(ICommandIds.CMD_OPEN);
	}
	public void run() {
		ModellingScene.getInstance(null).state = ModellingScene.STATE_PICK_FIRST_CORNER;
	}
}
