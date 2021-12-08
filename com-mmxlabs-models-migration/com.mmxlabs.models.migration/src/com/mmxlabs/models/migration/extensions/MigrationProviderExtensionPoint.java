/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.migration.extensions;

import org.eclipse.jdt.annotation.NonNull;
import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

import com.mmxlabs.models.migration.IMigrationProvider;

/**
 */
@ExtensionBean("com.mmxlabs.models.migration.MigrationProvider")
public interface MigrationProviderExtensionPoint {

	@MapName("class")
	@NonNull
	IMigrationProvider createMigrationProvider();
}
