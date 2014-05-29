/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.models.migration.extensions;

import org.eclipse.jdt.annotation.Nullable;
import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

import com.mmxlabs.models.migration.IClientMigrationUnit;

/**
 */
@ExtensionBean("com.mmxlabs.models.migration.ClientMigrationUnit")
public interface ClientMigrationUnitExtensionPoint {

	/**
	 */
	@MapName("id")
	@Nullable
	String getID();

	@MapName("scenarioContext")
	@Nullable
	String getScenarioContext();
	
	@MapName("clientContext")
	@Nullable
	String getClientContext();

	@MapName("version")
	@Nullable
	String getScenarioVersion();
	
	@MapName("from")
	@Nullable
	String getClientFrom();

	@MapName("to")
	@Nullable
	String getClientTo();

	@MapName("class")
	@Nullable
	IClientMigrationUnit createClientMigrationUnit();
}
