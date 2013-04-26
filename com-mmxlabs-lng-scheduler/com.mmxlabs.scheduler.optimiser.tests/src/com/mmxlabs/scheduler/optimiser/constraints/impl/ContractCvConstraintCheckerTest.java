/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.impl.ListSequence;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapPortSlotEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapPortTypeEditor;

public class ContractCvConstraintCheckerTest {

	@Test
	public void testName() {
		final String name = "checker";
		final ContractCvConstraintChecker checker = new ContractCvConstraintChecker(name);

		Assert.assertSame(name, checker.getName());
	}

	@Test
	public void testConstraint() {
		//
		final IPortSlotProviderEditor portSlotProvider = new HashMapPortSlotEditor("slots");
		final IPortTypeProviderEditor portTypeProvider = new HashMapPortTypeEditor("types");

		final ContractCvConstraintChecker checker = createChecker("name", portTypeProvider, portSlotProvider);

		// check empty behaviour
		final ISequenceElement o1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement o2 = Mockito.mock(ISequenceElement.class, "2");
		final ISequenceElement o3 = Mockito.mock(ISequenceElement.class, "3");
		final ISequenceElement o4 = Mockito.mock(ISequenceElement.class, "4");

		final ILoadSlot s1 = Mockito.mock(ILoadSlot.class, "s1");
		// discharge slot s2 should be OK
		final IDischargeSlot s2 = Mockito.mock(IDischargeSlot.class, "s2");
		// discharge slot s3 should be out of bounds (load CV too low)
		final IDischargeSlot s3 = Mockito.mock(IDischargeSlot.class, "s3");
		// discharge slot s3 should be out of bounds (load CV too high)
		final IDischargeSlot s4 = Mockito.mock(IDischargeSlot.class, "s4");

		portSlotProvider.setPortSlot(o1, s1);
		portSlotProvider.setPortSlot(o2, s2);
		portSlotProvider.setPortSlot(o3, s3);
		portSlotProvider.setPortSlot(o4, s4);

		portTypeProvider.setPortType(o1, PortType.Load);
		portTypeProvider.setPortType(o2, PortType.Discharge);
		portTypeProvider.setPortType(o3, PortType.Discharge);
		portTypeProvider.setPortType(o4, PortType.Discharge);

		final IResource resource = Mockito.mock(IResource.class);

		Mockito.when(s1.getCargoCVValue()).thenReturn(100);
		Mockito.when(s2.getMinCvValue()).thenReturn(0l);
		Mockito.when(s2.getMaxCvValue()).thenReturn(200l);
		Mockito.when(s3.getMinCvValue()).thenReturn(200l);
		Mockito.when(s3.getMaxCvValue()).thenReturn(300l);
		Mockito.when(s4.getMinCvValue()).thenReturn(0l);
		Mockito.when(s4.getMaxCvValue()).thenReturn(50l);

		// OK pairing
		Assert.assertTrue(checker.checkPairwiseConstraint(o1, o2, resource));
		final ISequence okSequence = new ListSequence(CollectionsUtil.makeArrayList(o1, o2));
		Assert.assertTrue(checker.checkSequence(okSequence, resource));

		// CV too low
		Assert.assertFalse(checker.checkPairwiseConstraint(o1, o3, resource));
		final ISequence tooLowSequence = new ListSequence(CollectionsUtil.makeArrayList(o1, o3));
		Assert.assertFalse(checker.checkSequence(tooLowSequence, resource));

		// CV too high
		Assert.assertFalse(checker.checkPairwiseConstraint(o1, o4, resource));
		final ISequence tooHighSequence = new ListSequence(CollectionsUtil.makeArrayList(o1, o4));
		Assert.assertFalse(checker.checkSequence(tooHighSequence, resource));

		// combined sequence of all cases should fail
		final ISequence combinedSequence = new ListSequence(CollectionsUtil.makeArrayList(o1, o2, o1, o3, o1, o4));
		Assert.assertFalse(checker.checkSequence(combinedSequence, resource));
	}

	private ContractCvConstraintChecker createChecker(final String name, final IPortTypeProviderEditor portTypeProvider, final IPortSlotProviderEditor portSlotProvider) {
		final Injector injector = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
				bind(IPortTypeProvider.class).toInstance(portTypeProvider);
				bind(IPortSlotProvider.class).toInstance(portSlotProvider);
			}

			@Provides
			ContractCvConstraintChecker create(final Injector injector) {
				final ContractCvConstraintChecker checker = new ContractCvConstraintChecker(name);
				injector.injectMembers(checker);
				return checker;
			}

		});
		return injector.getInstance(ContractCvConstraintChecker.class);
	}
}
