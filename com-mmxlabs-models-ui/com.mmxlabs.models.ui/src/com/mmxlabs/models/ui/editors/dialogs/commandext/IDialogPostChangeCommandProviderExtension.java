/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.dialogs.commandext;

import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

import com.mmxlabs.models.ui.editors.dialogs.IDialogPostChangeCommandProvider;

/**
 * Interface defining an Extension used in Peaberry
 * 
 * @author Simon Goodall
 * 
 */
@ExtensionBean("com.mmxlabs.models.ui.dialogpostchangecommandprovider")
public interface IDialogPostChangeCommandProviderExtension {

	@MapName("class")
	IDialogPostChangeCommandProvider getCommandProvider();
}
