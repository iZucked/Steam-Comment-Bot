/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.migration.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.models.migration.IClientMigrationUnit;
import com.mmxlabs.models.migration.IMigrationUnit;
import com.mmxlabs.models.migration.IMigrationUnitExtension;
import com.mmxlabs.models.migration.PackageData;

public class MigrationRegistryTests {

	@Test(expected = IllegalArgumentException.class)
	public void testMissingContext1() {
		final MigrationRegistry registry = new MigrationRegistry();

		final String context = "context";
		Assert.assertFalse(registry.isContextRegistered(context));
		registry.getLatestContextVersion(context);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMissingContext2() {
		final MigrationRegistry registry = new MigrationRegistry();

		final String context = "context";

		Assert.assertFalse(registry.isContextRegistered(context));
		registry.getMigrationChain(context, -1, -1, null, 0, 0);
	}

	public void testMissingClientContext1() {
		final MigrationRegistry registry = new MigrationRegistry();

		final String context = "context";
		Assert.assertFalse(registry.isClientContextRegistered(context));
		Assert.assertEquals(0, registry.getLatestClientContextVersion(context));
	}

	@Test
	public void testRegisterContext() {
		final MigrationRegistry registry = new MigrationRegistry();

		final String context = "context";
		final int latestVersion = 10;

		Assert.assertFalse(registry.isContextRegistered(context));
		registry.registerContext(context, latestVersion);

		Assert.assertTrue(registry.isContextRegistered(context));

		Assert.assertEquals(latestVersion, registry.getLatestContextVersion(context));
		Assert.assertNotNull(registry.getMigrationChain(context, -1, -1, null, 0, 0));
	}

	@Test
	public void testGetMigrationChain1() {

		final MigrationRegistry registry = new MigrationRegistry();

		final String context = "context";
		final int latestVersion = 10;

		registry.registerContext(context, latestVersion);

		final IMigrationUnit unit1 = mock(IMigrationUnit.class);
		when(unit1.getScenarioContext()).thenReturn(context);
		when(unit1.getScenarioSourceVersion()).thenReturn(1);
		when(unit1.getScenarioDestinationVersion()).thenReturn(2);

		final IMigrationUnit unit2 = mock(IMigrationUnit.class);
		when(unit2.getScenarioContext()).thenReturn(context);
		when(unit2.getScenarioSourceVersion()).thenReturn(2);
		when(unit2.getScenarioDestinationVersion()).thenReturn(3);

		final IMigrationUnit unit3 = mock(IMigrationUnit.class);
		when(unit3.getScenarioContext()).thenReturn(context);
		when(unit3.getScenarioSourceVersion()).thenReturn(3);
		when(unit3.getScenarioDestinationVersion()).thenReturn(10);

		registry.registerMigrationUnit(unit3);
		registry.registerMigrationUnit(unit2);
		registry.registerMigrationUnit(unit1);

		final List<IMigrationUnit> chain0 = registry.getMigrationChain(context, 1, 1, null, 0, 0);
		Assert.assertTrue(chain0.isEmpty());

		final List<IMigrationUnit> chain1 = registry.getMigrationChain(context, 1, 2, null, 0, 0);
		Assert.assertEquals(1, chain1.size());
		Assert.assertSame(unit1, chain1.get(0));

		final List<IMigrationUnit> chain2 = registry.getMigrationChain(context, 1, 10, null, 0, 0);
		Assert.assertEquals(3, chain2.size());
		Assert.assertSame(unit1, chain2.get(0));
		Assert.assertSame(unit2, chain2.get(1));
		Assert.assertSame(unit3, chain2.get(2));
	}

	@Test
	public void testGetMigrationChain2() {

		final MigrationRegistry registry = new MigrationRegistry();

		final String context = "context";
		final int latestVersion = 10;

		final String unit1ID = "unit1";
		final String unit2ID = "unit2";
		final String unit3ID = "unit3";

		registry.registerContext(context, latestVersion);

		final IMigrationUnit unit1 = mock(IMigrationUnit.class);
		when(unit1.getScenarioContext()).thenReturn(context);
		when(unit1.getScenarioSourceVersion()).thenReturn(1);
		when(unit1.getScenarioDestinationVersion()).thenReturn(2);

		final IMigrationUnit unit2 = mock(IMigrationUnit.class);
		when(unit2.getScenarioContext()).thenReturn(context);
		when(unit2.getScenarioSourceVersion()).thenReturn(2);
		when(unit2.getScenarioDestinationVersion()).thenReturn(3);

		final IMigrationUnit unit3 = mock(IMigrationUnit.class);
		when(unit3.getScenarioContext()).thenReturn(context);
		when(unit3.getScenarioSourceVersion()).thenReturn(3);
		when(unit3.getScenarioDestinationVersion()).thenReturn(10);

		registry.registerMigrationUnit(unit3ID, unit3);
		registry.registerMigrationUnit(unit2ID, unit2);
		registry.registerMigrationUnit(unit1ID, unit1);

		final IMigrationUnitExtension ext1 = new AbstractMigrationUnitExtension() {
			@Override
			public void migrate(@NonNull final URI uri, @Nullable final Map<URI, PackageData> extraPackages) throws Exception {
				// TODO Auto-generated method stub

			}
		};
		registry.registerMigrationUnitExtension(unit1ID, ext1);

		final List<IMigrationUnit> chain0 = registry.getMigrationChain(context, 1, 1, null, 0, 0);
		Assert.assertTrue(chain0.isEmpty());

		final List<IMigrationUnit> chain1 = registry.getMigrationChain(context, 1, 2, null, 0, 0);
		Assert.assertEquals(1, chain1.size());
		Assert.assertSame(ext1, chain1.get(0));

		final List<IMigrationUnit> chain2 = registry.getMigrationChain(context, 1, 10, null, 0, 0);
		Assert.assertEquals(3, chain2.size());
		Assert.assertSame(ext1, chain2.get(0));
		Assert.assertSame(unit2, chain2.get(1));
		Assert.assertSame(unit3, chain2.get(2));
	}

	/**
	 * Combined test of scenario, client and extension wrappers.
	 * 
	 */
	@Test
	public void testGetMigrationChain3() {

		final MigrationRegistry registry = new MigrationRegistry();

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

		final IMigrationUnitExtension ext1 = new AbstractMigrationUnitExtension() {
			@Override
			public void migrate(@NonNull final URI uri, @Nullable final Map<URI, PackageData> extraPackages) throws Exception {
				// TODO Auto-generated method stub

			}
		};

		registry.registerContext(scenarioContext, latestScenarioVersion);
		registry.registerClientContext(clientContext, latestClientVersion);

		registry.registerMigrationUnit("unit1", unit1);
		registry.registerMigrationUnit("unit2", unit2);
		registry.registerMigrationUnitExtension("unit1", ext1);

		registry.registerClientMigrationUnit("client1", clientUnit1);
		registry.registerClientMigrationUnit("client2", clientUnit2);
		registry.registerClientMigrationUnit("client3", clientUnit3);

		final List<IMigrationUnit> chain = registry.getMigrationChain(scenarioContext, scenarioVersion, latestScenarioVersion, clientContext, clientVersion, latestClientVersion);
		Assert.assertEquals(5, chain.size());
		Assert.assertSame(clientUnit1, chain.get(0));
		Assert.assertSame(ext1, chain.get(1));
		Assert.assertSame(clientUnit2, chain.get(2));
		Assert.assertSame(clientUnit3, chain.get(3));
		Assert.assertSame(unit2, chain.get(4));

	}

	/**
	 * Client migration only
	 * 
	 */

	@Test
	public void testGetMigrationChain4() {

		final MigrationRegistry registry = new MigrationRegistry();

		final String scenarioContext = "ScenarioContext";
		final int scenarioVersion = 1;
		final int latestScenarioVersion = 1;

		final String clientContext = "ClientContext";
		final int clientVersion = 1;
		final int latestClientVersion = 3;

		// Expect this to kick in on current version
		final IClientMigrationUnit clientUnit1 = Mockito.mock(IClientMigrationUnit.class);
		when(clientUnit1.getScenarioContext()).thenReturn(scenarioContext);
		when(clientUnit1.getScenarioSourceVersion()).thenReturn(1);
		when(clientUnit1.getScenarioDestinationVersion()).thenReturn(1);
		when(clientUnit1.getClientContext()).thenReturn(clientContext);
		when(clientUnit1.getClientSourceVersion()).thenReturn(1);
		when(clientUnit1.getClientDestinationVersion()).thenReturn(2);

		// Expect this to kick in on second version
		final IClientMigrationUnit clientUnit2 = Mockito.mock(IClientMigrationUnit.class);
		when(clientUnit2.getScenarioContext()).thenReturn(scenarioContext);
		when(clientUnit2.getScenarioSourceVersion()).thenReturn(1);
		when(clientUnit2.getScenarioDestinationVersion()).thenReturn(1);
		when(clientUnit2.getClientContext()).thenReturn(clientContext);
		when(clientUnit2.getClientSourceVersion()).thenReturn(2);
		when(clientUnit2.getClientDestinationVersion()).thenReturn(3);

		registry.registerContext(scenarioContext, latestScenarioVersion);
		registry.registerClientContext(clientContext, latestClientVersion);

		registry.registerClientMigrationUnit("client1", clientUnit1);
		registry.registerClientMigrationUnit("client2", clientUnit2);

		final List<IMigrationUnit> chain = registry.getMigrationChain(scenarioContext, scenarioVersion, latestScenarioVersion, clientContext, clientVersion, latestClientVersion);
		Assert.assertEquals(2, chain.size());
		Assert.assertSame(clientUnit1, chain.get(0));
		Assert.assertSame(clientUnit2, chain.get(1));

	}

	/**
	 * Client migration only. Same as {@link #testGetMigrationChain4()} but this client unit 2 requires a different scenario version (not registered) and thus cannot be used to migrate up to target
	 * version.
	 * 
	 */

	@Test
	public void testGetMigrationChain5() {

		final MigrationRegistry registry = new MigrationRegistry();

		final String scenarioContext = "ScenarioContext";
		final int scenarioVersion = 1;
		final int latestScenarioVersion = 1;

		final String clientContext = "ClientContext";
		final int clientVersion = 1;
		final int latestClientVersion = 3;

		// Expect this to kick in on current version
		final IClientMigrationUnit clientUnit1 = Mockito.mock(IClientMigrationUnit.class);
		when(clientUnit1.getScenarioContext()).thenReturn(scenarioContext);
		when(clientUnit1.getScenarioSourceVersion()).thenReturn(1);
		when(clientUnit1.getScenarioDestinationVersion()).thenReturn(1);
		when(clientUnit1.getClientContext()).thenReturn(clientContext);
		when(clientUnit1.getClientSourceVersion()).thenReturn(1);
		when(clientUnit1.getClientDestinationVersion()).thenReturn(2);

		// Expect this to kick in on second version
		final IClientMigrationUnit clientUnit2 = Mockito.mock(IClientMigrationUnit.class);
		when(clientUnit2.getScenarioContext()).thenReturn(scenarioContext);
		when(clientUnit2.getScenarioSourceVersion()).thenReturn(2);
		when(clientUnit2.getScenarioDestinationVersion()).thenReturn(2);
		when(clientUnit2.getClientContext()).thenReturn(clientContext);
		when(clientUnit2.getClientSourceVersion()).thenReturn(2);
		when(clientUnit2.getClientDestinationVersion()).thenReturn(3);

		registry.registerContext(scenarioContext, latestScenarioVersion);
		registry.registerClientContext(clientContext, latestClientVersion);

		registry.registerClientMigrationUnit("client1", clientUnit1);
		registry.registerClientMigrationUnit("client2", clientUnit2);

		final List<IMigrationUnit> chain = registry.getMigrationChain(scenarioContext, scenarioVersion, latestScenarioVersion, clientContext, clientVersion, latestClientVersion);
		Assert.assertEquals(1, chain.size());
		Assert.assertSame(clientUnit1, chain.get(0));
	}

	/**
	 * Test initially added due to recursion error. Timeout to stop eventual out of memory error.s
	 */
	@Test(timeout = 5000)
	public void testComplexChain1() {

		final String scenarioContext = "ScenarioContext";
		final int latestScenarioVersion = 25;

		final String clientContext = "ClientContext";
		final int latestClientVersion = 7;

		final MigrationRegistry registry = new MigrationRegistry();
		registry.registerContext(scenarioContext, latestScenarioVersion);
		registry.registerClientContext(clientContext, latestClientVersion);

		registry.registerMigrationUnit(createMigrationUnit(scenarioContext, 17, 18));
		registry.registerMigrationUnit(createMigrationUnit(scenarioContext, 18, 19));
		registry.registerMigrationUnit(createMigrationUnit(scenarioContext, 19, 20));
		registry.registerMigrationUnit(createMigrationUnit(scenarioContext, 20, 21));
		registry.registerMigrationUnit(createMigrationUnit(scenarioContext, 21, 22));
		registry.registerMigrationUnit(createMigrationUnit(scenarioContext, 22, 23));
		registry.registerMigrationUnit(createMigrationUnit(scenarioContext, 23, 24));
		registry.registerMigrationUnit(createMigrationUnit(scenarioContext, 24, 25));

		registry.registerClientMigrationUnit(createClientUnit(scenarioContext, 17, clientContext, 0, 1));
		registry.registerClientMigrationUnit(createClientUnit(scenarioContext, 17, clientContext, 1, 2));
		registry.registerClientMigrationUnit(createClientUnit(scenarioContext, 17, clientContext, 2, 3));
		registry.registerClientMigrationUnit(createClientUnit(scenarioContext, 18, clientContext, 3, 4));
		registry.registerClientMigrationUnit(createClientUnit(scenarioContext, 19, clientContext, 4, 5));
		// /
		registry.registerClientMigrationUnit(createClientUnit(scenarioContext, 22, clientContext, 5, 6));
		registry.registerClientMigrationUnit(createClientUnit(scenarioContext, 22, clientContext, 6, 7));
		registry.registerClientMigrationUnit(createClientUnit(scenarioContext, 22, clientContext, 0, 5));
		registry.registerClientMigrationUnit(createClientUnit(scenarioContext, 22, clientContext, 3, 5));
		registry.registerClientMigrationUnit(createClientUnit(scenarioContext, 22, clientContext, 0, 6));
		registry.registerClientMigrationUnit(createClientUnit(scenarioContext, 24, clientContext, 7, 8));
		{
			final List<IMigrationUnit> chain = registry.getMigrationChain(scenarioContext, 18, latestScenarioVersion, clientContext, 3, latestClientVersion);
			Assert.assertFalse(chain.isEmpty());
		}
		{
			final List<IMigrationUnit> chain = registry.getMigrationChain(scenarioContext, 22, latestScenarioVersion, clientContext, 3, latestClientVersion);
			Assert.assertFalse(chain.isEmpty());
		}
		{
			final List<IMigrationUnit> chain = registry.getMigrationChain(scenarioContext, 19, latestScenarioVersion, clientContext, 3, latestClientVersion);
			Assert.assertFalse(chain.isEmpty());
		}
		{
			final List<IMigrationUnit> chain = registry.getMigrationChain(scenarioContext, 19, latestScenarioVersion, clientContext, 0, latestClientVersion);
			Assert.assertFalse(chain.isEmpty());
		}
		{
			final List<IMigrationUnit> chain = registry.getMigrationChain(scenarioContext, 17, latestScenarioVersion, clientContext, 0, latestClientVersion);
			Assert.assertFalse(chain.isEmpty());
		}
		{
			final List<IMigrationUnit> chain = registry.getMigrationChain(scenarioContext, 17, latestScenarioVersion, clientContext, 0, latestClientVersion);
			Assert.assertFalse(chain.isEmpty());
		}
	}

	@NonNull
	private IMigrationUnit createMigrationUnit(String scenarioContext, int scenarioFrom, int scenarioTo) {
		final IMigrationUnit unit1 = Mockito.mock(IMigrationUnit.class);
		when(unit1.getScenarioContext()).thenReturn(scenarioContext);
		when(unit1.getScenarioSourceVersion()).thenReturn(scenarioFrom);
		when(unit1.getScenarioDestinationVersion()).thenReturn(scenarioTo);
		return unit1;
	}

	@NonNull
	private IClientMigrationUnit createClientUnit(String scenarioContext, int scenarioVersion, String clientContext, int clientFrom, int clientTo) {
		final IClientMigrationUnit clientUnit1 = Mockito.mock(IClientMigrationUnit.class);
		when(clientUnit1.getScenarioContext()).thenReturn(scenarioContext);
		when(clientUnit1.getScenarioSourceVersion()).thenReturn(scenarioVersion);
		when(clientUnit1.getScenarioDestinationVersion()).thenReturn(scenarioVersion);
		when(clientUnit1.getClientContext()).thenReturn(clientContext);
		when(clientUnit1.getClientSourceVersion()).thenReturn(clientFrom);
		when(clientUnit1.getClientDestinationVersion()).thenReturn(clientTo);
		return clientUnit1;
	}
}
