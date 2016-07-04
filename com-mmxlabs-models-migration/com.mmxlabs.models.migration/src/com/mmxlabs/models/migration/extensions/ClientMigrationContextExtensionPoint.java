/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.migration.extensions;

import org.eclipse.jdt.annotation.NonNull;
import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

/**
 */
@ExtensionBean("com.mmxlabs.models.migration.ClientMigrationContext")
public interface ClientMigrationContextExtensionPoint {

	@MapName("name")
	@NonNull
	String getClientContextName();

	@MapName("latestVersion")
	@NonNull
	String getLatestClientVersion();
}
