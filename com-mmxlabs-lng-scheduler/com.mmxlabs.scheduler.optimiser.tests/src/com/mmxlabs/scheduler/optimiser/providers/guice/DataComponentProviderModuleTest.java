/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.guice;

import org.junit.Assert;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mmxlabs.optimiser.common.dcproviders.ITimeWindowDataComponentProviderEditor;
import com.mmxlabs.optimiser.common.dcproviders.impl.TimeWindowDataComponentProvider;
import com.mmxlabs.optimiser.common.dcproviders.impl.indexed.IndexedTimeWindowEditor;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProviderEditor;

public class DataComponentProviderModuleTest {

	@Test
	public void testDataComponentProviderModule() {

		final Injector injector = Guice.createInjector(new DataComponentProviderModule());

		final IVesselProvider provider = injector.getInstance(IVesselProvider.class);
		final IVesselProviderEditor editor = injector.getInstance(IVesselProviderEditor.class);

		Assert.assertNotNull(provider);
		Assert.assertNotNull(editor);

		Assert.assertSame(provider, editor);
	}

	@Test
	public void testDataComponentProviderModuleBoolean_True() {
		final Injector injector = Guice.createInjector(new DataComponentProviderModule(true));

		final ITimeWindowDataComponentProviderEditor editor = injector.getInstance(ITimeWindowDataComponentProviderEditor.class);
		Assert.assertNotNull(editor);

		Assert.assertTrue(editor instanceof IndexedTimeWindowEditor);
	}

	@Test
	public void testDataComponentProviderModuleBoolean_False() {
		final Injector injector = Guice.createInjector(new DataComponentProviderModule(false));

		final ITimeWindowDataComponentProviderEditor editor = injector.getInstance(ITimeWindowDataComponentProviderEditor.class);
		Assert.assertNotNull(editor);

		Assert.assertTrue(editor instanceof TimeWindowDataComponentProvider);
	}

}
