/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

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

@RunWith(JMock.class)
public class ContractCvConstraintCheckerTest {

	Mockery context = new JUnit4Mockery();

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
		final ISequenceElement o1 = context.mock(ISequenceElement.class, "1");
		final ISequenceElement o2 = context.mock(ISequenceElement.class, "2");
		final ISequenceElement o3 = context.mock(ISequenceElement.class, "3");
		final ISequenceElement o4 = context.mock(ISequenceElement.class, "4");

		final ILoadSlot s1 = context.mock(ILoadSlot.class, "s1");
		// discharge slot s2 should be OK
		final IDischargeSlot s2 = context.mock(IDischargeSlot.class, "s2");
		// discharge slot s3 should be out of bounds (load CV too low)
		final IDischargeSlot s3 = context.mock(IDischargeSlot.class, "s3");
		// discharge slot s3 should be out of bounds (load CV too high)
		final IDischargeSlot s4 = context.mock(IDischargeSlot.class, "s4");

		portSlotProvider.setPortSlot(o1, s1);
		portSlotProvider.setPortSlot(o2, s2);
		portSlotProvider.setPortSlot(o3, s3);
		portSlotProvider.setPortSlot(o4, s4);
		
		portTypeProvider.setPortType(o1, PortType.Load);
		portTypeProvider.setPortType(o2, PortType.Discharge);
		portTypeProvider.setPortType(o3, PortType.Discharge);
		portTypeProvider.setPortType(o4, PortType.Discharge);

		final IResource resource = context.mock(IResource.class);

		context.checking(new Expectations() {
			{
				atLeast(1).of(s1).getCargoCVValue();
				will(returnValue(100));
				atLeast(1).of(s2).getMinCvValue();
				will(returnValue(0l));
				atLeast(1).of(s2).getMaxCvValue();
				will(returnValue(200l));
				atLeast(1).of(s3).getMinCvValue();
				will(returnValue(200l));
				atLeast(0).of(s3).getMaxCvValue();
				will(returnValue(300l));
				atLeast(1).of(s4).getMinCvValue();
				will(returnValue(0l));
				atLeast(1).of(s4).getMaxCvValue();
				will(returnValue(50l));
			}
		});

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
		
		context.assertIsSatisfied();
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
