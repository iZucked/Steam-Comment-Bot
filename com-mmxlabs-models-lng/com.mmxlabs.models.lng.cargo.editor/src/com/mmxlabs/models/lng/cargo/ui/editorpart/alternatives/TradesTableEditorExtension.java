/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart.alternatives;

import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

@ExtensionBean("com.mmxlabs.models.lng.cargo.editor.TradesTableEditor")
public interface TradesTableEditorExtension {

	@MapName("class")
	IAlternativeEditorProvider getInstance();
}
