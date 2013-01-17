/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.migration.extensions;

import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

@ExtensionBean("com.mmxlabs.models.migration.MigrationContext")
public interface MigrationContextExtensionPoint {

	@MapName("name")
	String getContextName();

	@MapName("latestVersion")
	String getLatestVersion();
}
