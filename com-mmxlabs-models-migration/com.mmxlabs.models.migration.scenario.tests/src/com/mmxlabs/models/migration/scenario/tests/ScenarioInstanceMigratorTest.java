/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.migration.scenario.tests;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import com.google.common.collect.Lists;
import com.mmxlabs.models.migration.IMigrationRegistry;
import com.mmxlabs.models.migration.IMigrationUnit;
import com.mmxlabs.models.migration.scenario.ScenarioInstanceMigrator;

public class ScenarioInstanceMigratorTest {

	@Test
	public void migrationTest() throws Exception {

		final IMigrationRegistry migrationRegistry = Mockito.mock(IMigrationRegistry.class);

		final URI tmpURI = URI.createURI("migration");

		final String context = "Context";
		final int scenarioVersion = 1;
		final int latestVersion = 3;

		final IMigrationUnit unit1 = Mockito.mock(IMigrationUnit.class);
		when(unit1.getContext()).thenReturn(context);
		when(unit1.getSourceVersion()).thenReturn(1);
		when(unit1.getDestinationVersion()).thenReturn(2);

		final IMigrationUnit unit2 = Mockito.mock(IMigrationUnit.class);
		when(unit2.getContext()).thenReturn(context);
		when(unit2.getSourceVersion()).thenReturn(2);
		when(unit2.getDestinationVersion()).thenReturn(3);

		final List<IMigrationUnit> units = Lists.newArrayList(unit1, unit2);

		when(migrationRegistry.getMigrationChain(context, scenarioVersion, latestVersion)).thenReturn(units);

		final ScenarioInstanceMigrator migrator = new ScenarioInstanceMigrator(migrationRegistry);
		migrator.applyMigrationChain(context, scenarioVersion, latestVersion, tmpURI);

		final InOrder order = inOrder(unit1, unit2);
		order.verify(unit1).migrate(tmpURI, Collections.<String, URI> emptyMap());
		order.verify(unit2).migrate(tmpURI, Collections.<String, URI> emptyMap());

	}
}
