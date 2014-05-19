/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.migration.scenario.tests;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import com.google.common.collect.Lists;
import com.mmxlabs.models.migration.IMigrationRegistry;
import com.mmxlabs.models.migration.IMigrationUnit;
import com.mmxlabs.models.migration.PackageData;
import com.mmxlabs.models.migration.scenario.ScenarioInstanceMigrator;
import com.mmxlabs.scenario.service.util.encryption.IScenarioCipherProvider;

public class ScenarioInstanceMigratorTest {

	@Test
	public void migrationTest() throws Exception {

		final IMigrationRegistry migrationRegistry = Mockito.mock(IMigrationRegistry.class);
		final IScenarioCipherProvider scenarioCipherProvider = Mockito.mock(IScenarioCipherProvider.class);

		final URI tmpURI = URI.createURI("migration");

		final String scenarioContext = "ScenarioContext";
		final int scenarioVersion = 1;
		final int latestScenarioVersion = 3;
		
		final String clientContext = "ClientContext";
		final int clientVersion = 0;
		final int latestClientVersion = 0;

		final IMigrationUnit unit1 = Mockito.mock(IMigrationUnit.class);
		when(unit1.getScenarioContext()).thenReturn(scenarioContext);
		when(unit1.getScenarioSourceVersion()).thenReturn(1);
		when(unit1.getScenarioDestinationVersion()).thenReturn(2);

		final IMigrationUnit unit2 = Mockito.mock(IMigrationUnit.class);
		when(unit2.getScenarioContext()).thenReturn(scenarioContext);
		when(unit2.getScenarioSourceVersion()).thenReturn(2);
		when(unit2.getScenarioDestinationVersion()).thenReturn(3);

		final List<IMigrationUnit> units = Lists.newArrayList(unit1, unit2);

		when(migrationRegistry.getMigrationChain(scenarioContext, scenarioVersion, latestScenarioVersion, clientContext, clientVersion, latestClientVersion)).thenReturn(units);

		final ScenarioInstanceMigrator migrator = new ScenarioInstanceMigrator(migrationRegistry, scenarioCipherProvider);
		migrator.applyMigrationChain(scenarioContext, scenarioVersion, latestScenarioVersion, clientContext, clientVersion, latestClientVersion, tmpURI);

		final InOrder order = inOrder(unit1, unit2);
		order.verify(unit1).migrate(tmpURI, Collections.<URI, PackageData> emptyMap());
		order.verify(unit2).migrate(tmpURI, Collections.<URI, PackageData> emptyMap());

	}
}
