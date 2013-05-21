/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.migration.extensions;

import org.eclipse.jdt.annotation.Nullable;
import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

import com.mmxlabs.models.migration.IMigrationUnit;

/**
 * @since 2.0
 */
@ExtensionBean("com.mmxlabs.models.migration.MigrationUnit")
public interface MigrationUnitExtensionPoint {

	/**
	 * @since 3.0
	 */
	@MapName("id")
	@Nullable
	String getID();

	@MapName("context")
	@Nullable
	String getContext();

	@MapName("from")
	@Nullable
	String getFrom();

	@MapName("to")
	@Nullable
	String getTo();

	@MapName("class")
	@Nullable
	IMigrationUnit createMigrationUnit();
}
