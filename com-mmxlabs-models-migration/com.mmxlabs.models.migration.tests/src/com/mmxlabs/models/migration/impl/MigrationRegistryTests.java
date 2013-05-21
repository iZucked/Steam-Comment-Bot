/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.migration.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.models.migration.IMigrationUnit;
import com.mmxlabs.models.migration.IMigrationUnitExtension;

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
		registry.getMigrationChain(context, -1, -1);
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
		Assert.assertNotNull(registry.getMigrationChain(context, -1, -1));
	}

	@Test
	public void testGetMigrationChain1() {

		final MigrationRegistry registry = new MigrationRegistry();

		final String context = "context";
		final int latestVersion = 10;

		registry.registerContext(context, latestVersion);

		final IMigrationUnit unit1 = mock(IMigrationUnit.class);
		when(unit1.getContext()).thenReturn(context);
		when(unit1.getSourceVersion()).thenReturn(1);
		when(unit1.getDestinationVersion()).thenReturn(2);

		final IMigrationUnit unit2 = mock(IMigrationUnit.class);
		when(unit2.getContext()).thenReturn(context);
		when(unit2.getSourceVersion()).thenReturn(2);
		when(unit2.getDestinationVersion()).thenReturn(3);

		final IMigrationUnit unit3 = mock(IMigrationUnit.class);
		when(unit3.getContext()).thenReturn(context);
		when(unit3.getSourceVersion()).thenReturn(3);
		when(unit3.getDestinationVersion()).thenReturn(10);

		registry.registerMigrationUnit(unit3);
		registry.registerMigrationUnit(unit2);
		registry.registerMigrationUnit(unit1);

		final List<IMigrationUnit> chain0 = registry.getMigrationChain(context, 1, 1);
		Assert.assertTrue(chain0.isEmpty());

		final List<IMigrationUnit> chain1 = registry.getMigrationChain(context, 1, 2);
		Assert.assertEquals(1, chain1.size());
		Assert.assertSame(unit1, chain1.get(0));

		final List<IMigrationUnit> chain2 = registry.getMigrationChain(context, 1, 10);
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
		when(unit1.getContext()).thenReturn(context);
		when(unit1.getSourceVersion()).thenReturn(1);
		when(unit1.getDestinationVersion()).thenReturn(2);

		final IMigrationUnit unit2 = mock(IMigrationUnit.class);
		when(unit2.getContext()).thenReturn(context);
		when(unit2.getSourceVersion()).thenReturn(2);
		when(unit2.getDestinationVersion()).thenReturn(3);

		final IMigrationUnit unit3 = mock(IMigrationUnit.class);
		when(unit3.getContext()).thenReturn(context);
		when(unit3.getSourceVersion()).thenReturn(3);
		when(unit3.getDestinationVersion()).thenReturn(10);

		registry.registerMigrationUnit(unit3ID, unit3);
		registry.registerMigrationUnit(unit2ID, unit2);
		registry.registerMigrationUnit(unit1ID, unit1);

		final IMigrationUnitExtension ext1 = new AbstractMigrationUnitExtension() {
			@Override
			public void migrate(@NonNull final List<URI> uris, @NonNull final URIConverter uc, @Nullable final Map<String, URI> extraPackages) throws Exception {
				// TODO Auto-generated method stub

			}
		};
		registry.registerMigrationUnitExtension(unit1ID, ext1);

		final List<IMigrationUnit> chain0 = registry.getMigrationChain(context, 1, 1);
		Assert.assertTrue(chain0.isEmpty());

		final List<IMigrationUnit> chain1 = registry.getMigrationChain(context, 1, 2);
		Assert.assertEquals(1, chain1.size());
		Assert.assertSame(ext1, chain1.get(0));

		final List<IMigrationUnit> chain2 = registry.getMigrationChain(context, 1, 10);
		Assert.assertEquals(3, chain2.size());
		Assert.assertSame(ext1, chain2.get(0));
		Assert.assertSame(unit2, chain2.get(1));
		Assert.assertSame(unit3, chain2.get(2));
	}
}
