/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.migration.extensions;

import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

import com.mmxlabs.models.migration.IMigrationUnit;

@ExtensionBean("com.mmxlabs.models.migration.MigrationUnit")
public interface MigrationUnitExtensionPoint {

	@MapName("context")
	String getContext();

	@MapName("from")
	String getFrom();

	@MapName("to")
	String getTo();

	@MapName("class")
	IMigrationUnit createMigrationUnit();
}
