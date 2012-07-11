/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.ui.extensions;

import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

import com.mmxlabs.models.ui.editorpart.IJointModelEditorContribution;

@ExtensionBean("com.mmxlabs.models.ui.jointmodeleditors")
public interface IJointModelEditorExtension {
	@MapName("id") String getID();
	@MapName("subModelClass") String getSubModelClassName();
	/**
	 * Create a new instance. Note method needs to start with "create" to avoid Peaberry caching the result
	 * @return
	 */
	@MapName("implementation") IJointModelEditorContribution createInstance();
	@MapName("priority") String getPriority();
}
