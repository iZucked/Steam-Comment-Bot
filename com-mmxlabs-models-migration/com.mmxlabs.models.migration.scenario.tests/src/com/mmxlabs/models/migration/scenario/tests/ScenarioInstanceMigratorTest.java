/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.migration.scenario.tests;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jdt.annotation.NonNull;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import com.google.common.collect.Lists;
import com.mmxlabs.models.migration.IClientMigrationUnit;
import com.mmxlabs.models.migration.IMigrationRegistry;
import com.mmxlabs.models.migration.IMigrationUnit;
import com.mmxlabs.models.migration.PackageData;
import com.mmxlabs.models.migration.impl.MigrationRegistry;
import com.mmxlabs.models.migration.scenario.ScenarioInstanceMigrator;
import com.mmxlabs.scenario.service.util.encryption.IScenarioCipherProvider;

@SuppressWarnings("null")
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
		final int clientVersion = 1;
		final int latestClientVersion = 4;

		// Expect this to kick in on current version
		final IClientMigrationUnit clientUnit1 = Mockito.mock(IClientMigrationUnit.class);
		when(clientUnit1.getScenarioContext()).thenReturn(scenarioContext);
		when(clientUnit1.getScenarioSourceVersion()).thenReturn(1);
		when(clientUnit1.getScenarioDestinationVersion()).thenReturn(1);
		when(clientUnit1.getClientContext()).thenReturn(clientContext);
		when(clientUnit1.getClientSourceVersion()).thenReturn(1);
		when(clientUnit1.getClientDestinationVersion()).thenReturn(2);

		final IMigrationUnit unit1 = Mockito.mock(IMigrationUnit.class);
		when(unit1.getScenarioContext()).thenReturn(scenarioContext);
		when(unit1.getScenarioSourceVersion()).thenReturn(1);
		when(unit1.getScenarioDestinationVersion()).thenReturn(2);

		// Expect this to kick in on second version
		final IClientMigrationUnit clientUnit2 = Mockito.mock(IClientMigrationUnit.class);
		when(clientUnit2.getScenarioContext()).thenReturn(scenarioContext);
		when(clientUnit2.getScenarioSourceVersion()).thenReturn(2);
		when(clientUnit2.getScenarioDestinationVersion()).thenReturn(2);
		when(clientUnit2.getClientContext()).thenReturn(clientContext);
		when(clientUnit2.getClientSourceVersion()).thenReturn(2);
		when(clientUnit2.getClientDestinationVersion()).thenReturn(3);

		// Expect this to kick in on second version
		final IClientMigrationUnit clientUnit3 = Mockito.mock(IClientMigrationUnit.class);
		when(clientUnit3.getScenarioContext()).thenReturn(scenarioContext);
		when(clientUnit3.getScenarioSourceVersion()).thenReturn(2);
		when(clientUnit3.getScenarioDestinationVersion()).thenReturn(2);
		when(clientUnit3.getClientContext()).thenReturn(clientContext);
		when(clientUnit3.getClientSourceVersion()).thenReturn(3);
		when(clientUnit3.getClientDestinationVersion()).thenReturn(4);

		final IMigrationUnit unit2 = Mockito.mock(IMigrationUnit.class);
		when(unit2.getScenarioContext()).thenReturn(scenarioContext);
		when(unit2.getScenarioSourceVersion()).thenReturn(2);
		when(unit2.getScenarioDestinationVersion()).thenReturn(3);

		final List<IMigrationUnit> units = Lists.newArrayList(clientUnit1, unit1, clientUnit2, clientUnit3, unit2);

		when(migrationRegistry.getMigrationChain(scenarioContext, scenarioVersion, latestScenarioVersion, clientContext, clientVersion, latestClientVersion)).thenReturn(units);

		final ScenarioInstanceMigrator migrator = new ScenarioInstanceMigrator(migrationRegistry, scenarioCipherProvider);
		migrator.applyMigrationChain(scenarioContext, scenarioVersion, latestScenarioVersion, clientContext, clientVersion, latestClientVersion, tmpURI, new NullProgressMonitor());

		final InOrder order = inOrder(clientUnit1, unit1, clientUnit2, clientUnit3, unit2);
		order.verify(clientUnit1).migrate(tmpURI, Collections.<URI, PackageData> emptyMap());
		order.verify(unit1).migrate(tmpURI, Collections.<URI, PackageData> emptyMap());
		order.verify(clientUnit2).migrate(tmpURI, Collections.<URI, PackageData> emptyMap());
		order.verify(clientUnit3).migrate(tmpURI, Collections.<URI, PackageData> emptyMap());
		order.verify(unit2).migrate(tmpURI, Collections.<URI, PackageData> emptyMap());

	}

	/**
	 * Add test case combining real {@link MigrationRegistry} instance to test multiple migration code paths.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testComplexChain1() throws Exception {

		final IScenarioCipherProvider scenarioCipherProvider = Mockito.mock(IScenarioCipherProvider.class);

		final String scenarioContext = "ScenarioContext";
		final int latestScenarioVersion = 25;

		final String clientContext = "ClientContext";
		final int latestClientVersion = 8;

		final MigrationRegistry registry = new MigrationRegistry();
		registry.registerContext(scenarioContext, latestScenarioVersion);
		registry.registerClientContext(clientContext, latestClientVersion);

		final IMigrationUnit unit17to18 = createMigrationUnit(scenarioContext, 17, 18);
		final IMigrationUnit unit18to19 = createMigrationUnit(scenarioContext, 18, 19);
		final IMigrationUnit unit19to20 = createMigrationUnit(scenarioContext, 19, 20);
		final IMigrationUnit unit20to21 = createMigrationUnit(scenarioContext, 20, 21);
		final IMigrationUnit unit21to22 = createMigrationUnit(scenarioContext, 21, 22);
		final IMigrationUnit unit22to23 = createMigrationUnit(scenarioContext, 22, 23);
		final IMigrationUnit unit23to24 = createMigrationUnit(scenarioContext, 23, 24);
		final IMigrationUnit unit24to25 = createMigrationUnit(scenarioContext, 24, 25);

		registry.registerMigrationUnit(unit17to18);
		registry.registerMigrationUnit(unit18to19);
		registry.registerMigrationUnit(unit19to20);
		registry.registerMigrationUnit(unit20to21);
		registry.registerMigrationUnit(unit21to22);
		registry.registerMigrationUnit(unit22to23);
		registry.registerMigrationUnit(unit23to24);
		registry.registerMigrationUnit(unit24to25);

		final IClientMigrationUnit client0to1 = createClientUnit(scenarioContext, 17, clientContext, 0, 1);
		final IClientMigrationUnit client1to2 = createClientUnit(scenarioContext, 17, clientContext, 1, 2);
		final IClientMigrationUnit client2to3 = createClientUnit(scenarioContext, 17, clientContext, 2, 3);
		final IClientMigrationUnit client3to4 = createClientUnit(scenarioContext, 18, clientContext, 3, 4);
		final IClientMigrationUnit client4to5 = createClientUnit(scenarioContext, 19, clientContext, 4, 5);
		final IClientMigrationUnit client5to6 = createClientUnit(scenarioContext, 22, clientContext, 5, 6);
		final IClientMigrationUnit client6to7 = createClientUnit(scenarioContext, 22, clientContext, 6, 7);
		final IClientMigrationUnit client7to8 = createClientUnit(scenarioContext, 24, clientContext, 7, 8);

		final IClientMigrationUnit client0to5 = createClientUnit(scenarioContext, 22, clientContext, 0, 5);
		final IClientMigrationUnit client3to5 = createClientUnit(scenarioContext, 22, clientContext, 3, 5);
		final IClientMigrationUnit client0to6 = createClientUnit(scenarioContext, 22, clientContext, 0, 6);

		registry.registerClientMigrationUnit(client0to1);
		registry.registerClientMigrationUnit(client1to2);
		registry.registerClientMigrationUnit(client2to3);
		registry.registerClientMigrationUnit(client3to4);
		registry.registerClientMigrationUnit(client4to5);
		registry.registerClientMigrationUnit(client5to6);
		registry.registerClientMigrationUnit(client6to7);
		registry.registerClientMigrationUnit(client7to8);
		registry.registerClientMigrationUnit(client0to5);
		registry.registerClientMigrationUnit(client3to5);
		registry.registerClientMigrationUnit(client0to6);

		final URI tmpURI = URI.createURI("migration");
		assert tmpURI != null;

		final ScenarioInstanceMigrator migrator = new ScenarioInstanceMigrator(registry, scenarioCipherProvider);

		// TODO: Separate test cases
		{
			// Example case 1

			// Apply Migration Chain
			final int[] migratedVersion = migrator.applyMigrationChain(scenarioContext, 19, latestScenarioVersion, clientContext, 3, latestClientVersion, tmpURI, new NullProgressMonitor());

			Assert.assertEquals(latestScenarioVersion, migratedVersion[0]);
			Assert.assertEquals(latestClientVersion, migratedVersion[1]);

			final InOrder order = inOrder(unit19to20, unit20to21, unit21to22, client3to5, client5to6, client6to7, unit23to24, client7to8, unit24to25);
			order.verify(unit19to20).migrate(tmpURI, Collections.<URI, PackageData> emptyMap());
			order.verify(unit20to21).migrate(tmpURI, Collections.<URI, PackageData> emptyMap());
			order.verify(unit21to22).migrate(tmpURI, Collections.<URI, PackageData> emptyMap());
			order.verify(client3to5).migrate(tmpURI, Collections.<URI, PackageData> emptyMap());
			order.verify(client5to6).migrate(tmpURI, Collections.<URI, PackageData> emptyMap());
			order.verify(client6to7).migrate(tmpURI, Collections.<URI, PackageData> emptyMap());
			order.verify(unit23to24).migrate(tmpURI, Collections.<URI, PackageData> emptyMap());
			order.verify(client7to8).migrate(tmpURI, Collections.<URI, PackageData> emptyMap());
			order.verify(unit24to25).migrate(tmpURI, Collections.<URI, PackageData> emptyMap());
		}
		{
			final List<IMigrationUnit> chain = registry.getMigrationChain(scenarioContext, 22, latestScenarioVersion, clientContext, 3, latestClientVersion);
			Assert.assertFalse(chain.isEmpty());

			// Example case 2

			// Apply Migration Chain
			final int[] migratedVersion = migrator.applyMigrationChain(scenarioContext, 22, latestScenarioVersion, clientContext, 3, latestClientVersion, tmpURI, new NullProgressMonitor());

			Assert.assertEquals(latestScenarioVersion, migratedVersion[0]);
			Assert.assertEquals(latestClientVersion, migratedVersion[1]);

			final InOrder order = inOrder(client3to5, client5to6, client6to7, unit23to24, client7to8, unit24to25);
			order.verify(client3to5).migrate(tmpURI, Collections.<URI, PackageData> emptyMap());
			order.verify(client5to6).migrate(tmpURI, Collections.<URI, PackageData> emptyMap());
			order.verify(client6to7).migrate(tmpURI, Collections.<URI, PackageData> emptyMap());
			order.verify(unit23to24).migrate(tmpURI, Collections.<URI, PackageData> emptyMap());
			order.verify(client7to8).migrate(tmpURI, Collections.<URI, PackageData> emptyMap());
			order.verify(unit24to25).migrate(tmpURI, Collections.<URI, PackageData> emptyMap());
		}
		{

			// Apply Migration Chain
			final int[] migratedVersion = migrator.applyMigrationChain(scenarioContext, 18, latestScenarioVersion, clientContext, 3, latestClientVersion, tmpURI, new NullProgressMonitor());
			Assert.assertEquals(latestScenarioVersion, migratedVersion[0]);
			Assert.assertEquals(latestClientVersion, migratedVersion[1]);

			// Example case 1

			final InOrder order = inOrder(client3to4, unit18to19, client4to5, unit19to20, unit20to21, unit21to22, client5to6, client6to7, unit22to23, unit23to24, client7to8, unit24to25);
			order.verify(client3to4).migrate(tmpURI, Collections.<URI, PackageData> emptyMap());
			order.verify(unit18to19).migrate(tmpURI, Collections.<URI, PackageData> emptyMap());
			order.verify(client4to5).migrate(tmpURI, Collections.<URI, PackageData> emptyMap());
			order.verify(unit19to20).migrate(tmpURI, Collections.<URI, PackageData> emptyMap());
			order.verify(unit20to21).migrate(tmpURI, Collections.<URI, PackageData> emptyMap());
			order.verify(unit21to22).migrate(tmpURI, Collections.<URI, PackageData> emptyMap());
			order.verify(client5to6).migrate(tmpURI, Collections.<URI, PackageData> emptyMap());
			order.verify(client6to7).migrate(tmpURI, Collections.<URI, PackageData> emptyMap());
			order.verify(unit22to23).migrate(tmpURI, Collections.<URI, PackageData> emptyMap());
			order.verify(unit23to24).migrate(tmpURI, Collections.<URI, PackageData> emptyMap());
			order.verify(client7to8).migrate(tmpURI, Collections.<URI, PackageData> emptyMap());
			order.verify(unit24to25).migrate(tmpURI, Collections.<URI, PackageData> emptyMap());
		}
	}

	@Test
	public void testComplexChain2() throws Exception {

		final IScenarioCipherProvider scenarioCipherProvider = Mockito.mock(IScenarioCipherProvider.class);

		final String scenarioContext = "ScenarioContext";
		final int latestScenarioVersion = 25;

		final String clientContext = "ClientContext";
		final int latestClientVersion = 8;

		final MigrationRegistry registry = new MigrationRegistry();
		registry.registerContext(scenarioContext, latestScenarioVersion);
		registry.registerClientContext(clientContext, latestClientVersion);

		final IMigrationUnit unit17to18 = createMigrationUnit(scenarioContext, 17, 18);
		final IMigrationUnit unit18to19 = createMigrationUnit(scenarioContext, 18, 19);
		final IMigrationUnit unit19to20 = createMigrationUnit(scenarioContext, 19, 20);
		final IMigrationUnit unit20to21 = createMigrationUnit(scenarioContext, 20, 21);
		final IMigrationUnit unit21to22 = createMigrationUnit(scenarioContext, 21, 22);
		final IMigrationUnit unit22to23 = createMigrationUnit(scenarioContext, 22, 23);
		final IMigrationUnit unit23to24 = createMigrationUnit(scenarioContext, 23, 24);
		final IMigrationUnit unit24to25 = createMigrationUnit(scenarioContext, 24, 25);

		registry.registerMigrationUnit(unit17to18);
		registry.registerMigrationUnit(unit18to19);
		registry.registerMigrationUnit(unit19to20);
		registry.registerMigrationUnit(unit20to21);
		registry.registerMigrationUnit(unit21to22);
		registry.registerMigrationUnit(unit22to23);
		registry.registerMigrationUnit(unit23to24);
		registry.registerMigrationUnit(unit24to25);

		final IClientMigrationUnit client0to1 = createClientUnit(scenarioContext, 17, clientContext, 0, 1);
		final IClientMigrationUnit client1to2 = createClientUnit(scenarioContext, 17, clientContext, 1, 2);
		final IClientMigrationUnit client2to3 = createClientUnit(scenarioContext, 17, clientContext, 2, 3);
		final IClientMigrationUnit client3to4 = createClientUnit(scenarioContext, 18, clientContext, 3, 4);
		final IClientMigrationUnit client4to5 = createClientUnit(scenarioContext, 19, clientContext, 4, 5);
		final IClientMigrationUnit client5to6 = createClientUnit(scenarioContext, 22, clientContext, 5, 6);
		final IClientMigrationUnit client6to7 = createClientUnit(scenarioContext, 22, clientContext, 6, 7);
		final IClientMigrationUnit client7to8 = createClientUnit(scenarioContext, 24, clientContext, 7, 8);

		// final IClientMigrationUnit client0to5 = createClientUnit(scenarioContext, 22, clientContext, 0, 5);
		// final IClientMigrationUnit client3to5 = createClientUnit(scenarioContext, 22, clientContext, 3, 5);
		// final IClientMigrationUnit client0to6 = createClientUnit(scenarioContext, 22, clientContext, 0, 6);

		registry.registerClientMigrationUnit(client0to1);
		registry.registerClientMigrationUnit(client1to2);
		registry.registerClientMigrationUnit(client2to3);
		registry.registerClientMigrationUnit(client3to4);
		registry.registerClientMigrationUnit(client4to5);
		registry.registerClientMigrationUnit(client5to6);
		registry.registerClientMigrationUnit(client6to7);
		registry.registerClientMigrationUnit(client7to8);
		// registry.registerClientMigrationUnit(client0to5);
		// registry.registerClientMigrationUnit(client3to5);
		// registry.registerClientMigrationUnit(client0to6);

		final URI tmpURI = URI.createURI("migration");
		assert tmpURI != null;

		final ScenarioInstanceMigrator migrator = new ScenarioInstanceMigrator(registry, scenarioCipherProvider);

		{
			final List<IMigrationUnit> chain = registry.getMigrationChain(scenarioContext, 22, latestScenarioVersion, clientContext, 3, latestClientVersion);
			Assert.assertFalse(chain.isEmpty());

			// Apply Migration Chain
			final int[] migratedVersion = migrator.applyMigrationChain(scenarioContext, 22, latestScenarioVersion, clientContext, 3, latestClientVersion, tmpURI, new NullProgressMonitor());

			Assert.assertEquals(latestScenarioVersion, migratedVersion[0]);
			// Assert.assertEquals(latestClientVersion, migratedVersion[1]);
			Assert.assertEquals(3, migratedVersion[1]);
		}
	}

	@NonNull
	private IMigrationUnit createMigrationUnit(final String scenarioContext, final int scenarioFrom, final int scenarioTo) {
		final IMigrationUnit unit1 = Mockito.mock(IMigrationUnit.class, String.format("Scenario %d -> %d", scenarioFrom, scenarioTo));
		when(unit1.getScenarioContext()).thenReturn(scenarioContext);
		when(unit1.getScenarioSourceVersion()).thenReturn(scenarioFrom);
		when(unit1.getScenarioDestinationVersion()).thenReturn(scenarioTo);
		return unit1;
	}

	@NonNull
	private IClientMigrationUnit createClientUnit(final String scenarioContext, final int scenarioVersion, final String clientContext, final int clientFrom, final int clientTo) {
		final IClientMigrationUnit clientUnit1 = Mockito.mock(IClientMigrationUnit.class, String.format("Client %d -> %d", clientFrom, clientTo));
		when(clientUnit1.getScenarioContext()).thenReturn(scenarioContext);
		when(clientUnit1.getScenarioSourceVersion()).thenReturn(scenarioVersion);
		when(clientUnit1.getScenarioDestinationVersion()).thenReturn(scenarioVersion);
		when(clientUnit1.getClientContext()).thenReturn(clientContext);
		when(clientUnit1.getClientSourceVersion()).thenReturn(clientFrom);
		when(clientUnit1.getClientDestinationVersion()).thenReturn(clientTo);
		return clientUnit1;
	}
}
