/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.scheduler.optimiser.constraints.impl;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.impl.ListSequence;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.providers.IPortExclusionProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapPortEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapPortExclusionProvider;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapVesselEditor;

public class PortExclusionConstraintCheckerTest {
	@Test
	public void testName() {
		final String name = "checker";
		final String empty = "empty";
		final PortExclusionConstraintChecker<Object> checker = 
			new PortExclusionConstraintChecker<Object>(name, empty, empty, empty);
		
		Assert.assertSame(name, checker.getName());
	}
	
	@Test
	public void testConstraint() {
		final PortExclusionConstraintChecker<Object> checker = 
			new PortExclusionConstraintChecker<Object>("name", "exclusions", "vessels", "ports");
		
		final IPortExclusionProviderEditor exclusionProvider = new
			HashMapPortExclusionProvider("exclusions");
		
		checker.setPortExclusionProvider(exclusionProvider);
		Assert.assertSame(exclusionProvider, checker.getPortExclusionProvider());
		
		final IPortProviderEditor portProvider = new HashMapPortEditor("ports");
		
		checker.setPortProvider(portProvider);
		Assert.assertSame(portProvider, checker.getPortProvider());
		
		final IVesselProviderEditor vesselProvider = new HashMapVesselEditor("vessels");
		checker.setVesselProvider(vesselProvider);
		Assert.assertSame(vesselProvider, checker.getVesselProvider());
		
		//check empty behaviour
		Assert.assertTrue(exclusionProvider.hasNoExclusions());
		Object o1 = new Object();
		Object o2 = new Object();
		Object o3 = new Object();
		Object o4 = new Object();
		
		Mockery context = new Mockery();
		
		IPort p1 = context.mock(IPort.class, "p1"); 
		IPort p2 = context.mock(IPort.class, "p2");
		IPort p3 = context.mock(IPort.class, "p3");
		
		portProvider.setPortForElement(p1, o1);
		portProvider.setPortForElement(p1, o4);
		portProvider.setPortForElement(p2, o2);
		portProvider.setPortForElement(p3, o3);
		
		final IVessel vessel =  context.mock(IVessel.class);
		final IVesselClass vesselClass = context.mock(IVesselClass.class);
		IResource resource = context.mock(IResource.class);
		
		context.checking(new Expectations() {
			{
				atLeast(1).of(vessel).getVesselClass();
				will(returnValue(vesselClass));
			}
		});
		
		vesselProvider.setVesselResource(resource, vessel);
		
		Assert.assertTrue(checker.checkPairwiseConstraint(o1, o2, resource));
		final ISequence<Object> sequence = new ListSequence<Object>(CollectionsUtil.makeArrayList(o1, o2, o4));
		
		Assert.assertTrue(checker.checkSequence(sequence, resource));
		
		exclusionProvider.setExcludedPorts(vessel.getVesselClass(), CollectionsUtil.makeHashSet(p3));
		Assert.assertFalse(exclusionProvider.hasNoExclusions());
		Assert.assertTrue(exclusionProvider.getExcludedPorts(vesselClass).contains(p3));
		
		Assert.assertTrue(checker.checkSequence(sequence, resource));
		final ISequence<Object> sequence2 = new ListSequence<Object>(CollectionsUtil.makeArrayList(o1, o3, o4));
		Assert.assertFalse(checker.checkSequence(sequence2, resource));
		
		Assert.assertTrue(checker.checkPairwiseConstraint(o1, o2, resource));
		Assert.assertFalse(checker.checkPairwiseConstraint(o1, o3, resource));
		
		context.assertIsSatisfied();
	}
}
