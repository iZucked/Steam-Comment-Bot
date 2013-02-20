/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.migration.extensions;

import org.eclipse.jdt.annotation.Nullable;
import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

/**
 * @since 2.0
 */
@ExtensionBean("com.mmxlabs.models.migration.MigrationContext")
public interface MigrationContextExtensionPoint {

	@MapName("name")
	@Nullable String getContextName();

	@MapName("latestVersion")
	@Nullable String getLatestVersion();
}
