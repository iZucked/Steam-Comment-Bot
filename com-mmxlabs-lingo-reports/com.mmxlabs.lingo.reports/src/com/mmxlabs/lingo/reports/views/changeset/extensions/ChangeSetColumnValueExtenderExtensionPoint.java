/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset.extensions;

import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

@ExtensionBean("com.mmxlabs.lingo.reports.ChangeSetColumnValueExtender")
public interface ChangeSetColumnValueExtenderExtensionPoint {

	@MapName("class")
	IChangeSetColumnValueExtender getExtender();
}
