/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.migration.extensions;

import org.eclipse.jdt.annotation.NonNull;
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
	@NonNull
	String getID();

	@MapName("scenarioContext")
	@NonNull
	String getScenarioContext();

	@MapName("clientContext")
	@NonNull
	String getClientContext();

	@MapName("version")
	@NonNull
	String getScenarioVersion();

	@MapName("from")
	@NonNull
	String getClientFrom();

	@MapName("to")
	@NonNull
	String getClientTo();

	@MapName("class")
	@NonNull
	IClientMigrationUnit createClientMigrationUnit();
}
