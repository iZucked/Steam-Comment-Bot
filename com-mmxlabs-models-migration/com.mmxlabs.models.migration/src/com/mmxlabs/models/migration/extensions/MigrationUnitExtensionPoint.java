/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.migration.extensions;

import org.eclipse.jdt.annotation.NonNull;
import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

import com.mmxlabs.models.migration.IMigrationUnit;

/**
 */
@ExtensionBean("com.mmxlabs.models.migration.MigrationUnit")
public interface MigrationUnitExtensionPoint {

	/**
	 */
	@MapName("id")
	@NonNull
	String getID();

	@MapName("context")
	@NonNull
	String getContext();

	@MapName("from")
	@NonNull
	String getFrom();

	@MapName("to")
	@NonNull
	String getTo();

	@MapName("class")
	@NonNull
	IMigrationUnit createMigrationUnit();
}
