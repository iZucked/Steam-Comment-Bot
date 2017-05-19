/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.guice;

import org.junit.Assert;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProviderEditor;
import com.mmxlabs.optimiser.common.dcproviders.impl.HashMapElementDurationEditor;
import com.mmxlabs.optimiser.common.dcproviders.impl.indexed.IndexedElementDurationEditor;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProviderEditor;
import com.mmxlabs.scheduler.optimiser.shared.SharedDataModule;

public class DataComponentProviderModuleTest {

	@Test
	public void testDataComponentProviderModule() {

		final Injector injector = Guice.createInjector(new DataComponentProviderModule(), new SharedDataModule());

		final IVesselProvider provider = injector.getInstance(IVesselProvider.class);
		final IVesselProviderEditor editor = injector.getInstance(IVesselProviderEditor.class);

		Assert.assertNotNull(provider);
		Assert.assertNotNull(editor);

		Assert.assertSame(provider, editor);
	}

	@Test
	public void testDataComponentProviderModuleBoolean_True() {
		final Injector injector = Guice.createInjector(new DataComponentProviderModule(true), new SharedDataModule());

		final IElementDurationProviderEditor editor = injector.getInstance(IElementDurationProviderEditor.class);
		Assert.assertNotNull(editor);

		Assert.assertTrue(editor instanceof IndexedElementDurationEditor);
	}

	@Test
	public void testDataComponentProviderModuleBoolean_False() {
		final Injector injector = Guice.createInjector(new DataComponentProviderModule(false), new SharedDataModule());

		final IElementDurationProviderEditor editor = injector.getInstance(IElementDurationProviderEditor.class);
		Assert.assertNotNull(editor);

		Assert.assertTrue(editor instanceof HashMapElementDurationEditor);
	}

}
