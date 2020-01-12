/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.guice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mmxlabs.common.parser.series.CalendarMonthMapper;
import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProviderEditor;
import com.mmxlabs.optimiser.common.dcproviders.impl.HashMapElementDurationEditor;
import com.mmxlabs.optimiser.common.dcproviders.impl.indexed.IndexedElementDurationEditor;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProviderEditor;
import com.mmxlabs.scheduler.optimiser.shared.SharedDataModule;

public class DataComponentProviderModuleTest {

	@Test
	public void testDataComponentProviderModule() {

		final Injector injector = Guice.createInjector(new DataComponentProviderModule(), new SharedDataModule(), new AbstractModule() {

			@Override
			protected void configure() {
				bind(CalendarMonthMapper.class).toInstance(Mockito.mock(CalendarMonthMapper.class));
			}
		});

		final IVesselProvider provider = injector.getInstance(IVesselProvider.class);
		final IVesselProviderEditor editor = injector.getInstance(IVesselProviderEditor.class);

		Assertions.assertNotNull(provider);
		Assertions.assertNotNull(editor);

		Assertions.assertSame(provider, editor);
	}

	@Test
	public void testDataComponentProviderModuleBoolean_True() {
		final Injector injector = Guice.createInjector(new DataComponentProviderModule(true), new SharedDataModule(), new AbstractModule() {

			@Override
			protected void configure() {
				bind(CalendarMonthMapper.class).toInstance(Mockito.mock(CalendarMonthMapper.class));
			}
		});

		final IElementDurationProviderEditor editor = injector.getInstance(IElementDurationProviderEditor.class);
		Assertions.assertNotNull(editor);

		Assertions.assertTrue(editor instanceof IndexedElementDurationEditor);
	}

	@Test
	public void testDataComponentProviderModuleBoolean_False() {
		final Injector injector = Guice.createInjector(new DataComponentProviderModule(false), new SharedDataModule(), new AbstractModule() {

			@Override
			protected void configure() {
				bind(CalendarMonthMapper.class).toInstance(Mockito.mock(CalendarMonthMapper.class));
			}
		});

		final IElementDurationProviderEditor editor = injector.getInstance(IElementDurationProviderEditor.class);
		Assertions.assertNotNull(editor);

		Assertions.assertTrue(editor instanceof HashMapElementDurationEditor);
	}

}
