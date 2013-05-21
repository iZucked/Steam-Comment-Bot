/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.migration.extensions;

import org.eclipse.jdt.annotation.Nullable;
import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

import com.mmxlabs.models.migration.IMigrationUnitExtension;

/**
 * @since 2.0
 */
@ExtensionBean("com.mmxlabs.models.migration.MigrationUnitExtension")
public interface MigrationUnitExtensionExtensionPoint {

	@MapName("migrationUnit")
	@Nullable
	String getMigrationUnitID();

	@MapName("class")
	@Nullable
	IMigrationUnitExtension createMigrationUnitExtension();
}
