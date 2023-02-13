/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.migration.extensions;

import org.eclipse.jdt.annotation.NonNull;
import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

/**
 */
@ExtensionBean("com.mmxlabs.models.migration.DefaultMigrationContext")
public interface DefaultMigrationContextExtensionPoint {

	@MapName("context")
	@NonNull
	String getContext();
}
