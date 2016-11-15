/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.common.dcproviders.IOrderedSequenceElementsDataComponentProvider;
import com.mmxlabs.optimiser.common.dcproviders.impl.OrderedSequenceElementsDataComponentProvider;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.impl.ListSequence;
import com.mmxlabs.optimiser.core.impl.Sequences;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapPortTypeEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapVesselEditor;

@SuppressWarnings("null")
public class PortTypeConstraintCheckerTest {

	@Test
	public void testPortTypeConstraintChecker() {

		final String name = "checker";
		final PortTypeConstraintChecker checker = new PortTypeConstraintChecker(name);

		Assert.assertSame(name, checker.getName());
	}

	@Test
	public void testCheckConstraintsISequencesOfT1() {

		final IPortTypeProviderEditor portTypeProvider = new HashMapPortTypeEditor();
		final IVesselProviderEditor vesselProvider = new HashMapVesselEditor();
		final IOrderedSequenceElementsDataComponentProvider orderedSequenceElementsProvider = new OrderedSequenceElementsDataComponentProvider();
		final PortTypeConstraintChecker checker = createChecker("checker", vesselProvider, portTypeProvider, orderedSequenceElementsProvider);

		final ISequenceElement o1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement o2 = Mockito.mock(ISequenceElement.class, "2");
		final ISequenceElement o3 = Mockito.mock(ISequenceElement.class, "3");
		final ISequenceElement o4 = Mockito.mock(ISequenceElement.class, "4");
		final ISequenceElement o5 = Mockito.mock(ISequenceElement.class, "5");
		final ISequenceElement o6 = Mockito.mock(ISequenceElement.class, "6");

		portTypeProvider.setPortType(o1, PortType.Start);
		portTypeProvider.setPortType(o2, PortType.Load);
		portTypeProvider.setPortType(o3, PortType.Discharge);
		portTypeProvider.setPortType(o4, PortType.Load);
		portTypeProvider.setPortType(o5, PortType.Discharge);
		portTypeProvider.setPortType(o6, PortType.End);

		final ISequence sequence = new ListSequence(CollectionsUtil.makeArrayList(o1, o2, o3, o4, o5, o6));

		final IResource r = Mockito.mock(IResource.class);
		final IVesselAvailability va = Mockito.mock(IVesselAvailability.class);

		vesselProvider.setVesselAvailabilityResource(r, va);

		Mockito.when(va.getVesselInstanceType()).thenReturn(VesselInstanceType.FLEET);

		final Map<IResource, ISequence> m = CollectionsUtil.makeHashMap(r, sequence);
		final Sequences sequences = new Sequences(CollectionsUtil.makeArrayList(r), m);

		Assert.assertTrue(checker.checkConstraints(sequences, null));
	}

	@Test
	public void testCheckConstraintsISequencesOfT2() {

		final IPortTypeProviderEditor portTypeProvider = new HashMapPortTypeEditor();
		final IVesselProviderEditor vesselProvider = new HashMapVesselEditor();
		final IOrderedSequenceElementsDataComponentProvider orderedSequenceElementsProvider = new OrderedSequenceElementsDataComponentProvider();
		final PortTypeConstraintChecker checker = createChecker("checker", vesselProvider, portTypeProvider, orderedSequenceElementsProvider);

		final ISequenceElement o1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement o2 = Mockito.mock(ISequenceElement.class, "2");
		final ISequenceElement o3 = Mockito.mock(ISequenceElement.class, "3");
		final ISequenceElement o4 = Mockito.mock(ISequenceElement.class, "4");
		final ISequenceElement o5 = Mockito.mock(ISequenceElement.class, "5");
		final ISequenceElement o6 = Mockito.mock(ISequenceElement.class, "6");

		portTypeProvider.setPortType(o1, PortType.Start);
		portTypeProvider.setPortType(o2, PortType.Discharge);
		portTypeProvider.setPortType(o3, PortType.Load);
		portTypeProvider.setPortType(o4, PortType.Discharge);
		portTypeProvider.setPortType(o5, PortType.Load);
		portTypeProvider.setPortType(o6, PortType.End);

		final ISequence sequence = new ListSequence(CollectionsUtil.makeArrayList(o1, o2, o3, o4, o5, o6));

		final IResource r = Mockito.mock(IResource.class);
		final IVesselAvailability va = Mockito.mock(IVesselAvailability.class);

		vesselProvider.setVesselAvailabilityResource(r, va);

		Mockito.when(va.getVesselInstanceType()).thenReturn(VesselInstanceType.FLEET);

		final Map<IResource, ISequence> m = CollectionsUtil.makeHashMap(r, sequence);
		final Sequences sequences = new Sequences(CollectionsUtil.makeArrayList(r), m);

		Assert.assertFalse(checker.checkConstraints(sequences, null));
	}

	@Test
	public void testCheckConstraintsISequencesOfTListOfString1() {

		final IPortTypeProviderEditor portTypeProvider = new HashMapPortTypeEditor();
		final IVesselProviderEditor vesselProvider = new HashMapVesselEditor();
		final IOrderedSequenceElementsDataComponentProvider orderedSequenceElementsProvider = new OrderedSequenceElementsDataComponentProvider();
		final PortTypeConstraintChecker checker = createChecker("checker", vesselProvider, portTypeProvider, orderedSequenceElementsProvider);

		final ISequenceElement o1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement o2 = Mockito.mock(ISequenceElement.class, "2");
		final ISequenceElement o3 = Mockito.mock(ISequenceElement.class, "3");
		final ISequenceElement o4 = Mockito.mock(ISequenceElement.class, "4");
		final ISequenceElement o5 = Mockito.mock(ISequenceElement.class, "5");
		final ISequenceElement o6 = Mockito.mock(ISequenceElement.class, "6");

		portTypeProvider.setPortType(o1, PortType.Start);
		portTypeProvider.setPortType(o2, PortType.Load);
		portTypeProvider.setPortType(o3, PortType.Discharge);
		portTypeProvider.setPortType(o4, PortType.Load);
		portTypeProvider.setPortType(o5, PortType.Discharge);
		portTypeProvider.setPortType(o6, PortType.End);

		final ISequence sequence = new ListSequence(CollectionsUtil.makeArrayList(o1, o2, o3, o4, o5, o6));

		final IResource r = Mockito.mock(IResource.class);
		final IVesselAvailability va = Mockito.mock(IVesselAvailability.class);

		vesselProvider.setVesselAvailabilityResource(r, va);

		Mockito.when(va.getVesselInstanceType()).thenReturn(VesselInstanceType.FLEET);

		final Map<IResource, ISequence> m = CollectionsUtil.makeHashMap(r, sequence);
		final Sequences sequences = new Sequences(CollectionsUtil.makeArrayList(r), m);

		final List<String> messages = new ArrayList<String>(1);
		Assert.assertTrue(checker.checkConstraints(sequences, null, messages));
		Assert.assertEquals(0, messages.size());
	}

	@Test
	public void testCheckConstraintsISequencesOfTListOfString2() {
		final IPortTypeProviderEditor portTypeProvider = new HashMapPortTypeEditor();
		final IVesselProviderEditor vesselProvider = new HashMapVesselEditor();
		final IOrderedSequenceElementsDataComponentProvider orderedSequenceElementsProvider = new OrderedSequenceElementsDataComponentProvider();
		final PortTypeConstraintChecker checker = createChecker("checker", vesselProvider, portTypeProvider, orderedSequenceElementsProvider);

		final ISequenceElement o1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement o2 = Mockito.mock(ISequenceElement.class, "2");
		final ISequenceElement o3 = Mockito.mock(ISequenceElement.class, "3");
		final ISequenceElement o4 = Mockito.mock(ISequenceElement.class, "4");
		final ISequenceElement o5 = Mockito.mock(ISequenceElement.class, "5");
		final ISequenceElement o6 = Mockito.mock(ISequenceElement.class, "6");

		portTypeProvider.setPortType(o1, PortType.Start);
		portTypeProvider.setPortType(o2, PortType.Discharge);
		portTypeProvider.setPortType(o3, PortType.Load);
		portTypeProvider.setPortType(o4, PortType.Discharge);
		portTypeProvider.setPortType(o5, PortType.Load);
		portTypeProvider.setPortType(o6, PortType.End);

		final ISequence sequence = new ListSequence(CollectionsUtil.makeArrayList(o1, o2, o3, o4, o5, o6));

		final IResource r = Mockito.mock(IResource.class);
		final IVesselAvailability va = Mockito.mock(IVesselAvailability.class);

		vesselProvider.setVesselAvailabilityResource(r, va);

		Mockito.when(va.getVesselInstanceType()).thenReturn(VesselInstanceType.FLEET);

		final Map<IResource, ISequence> m = CollectionsUtil.makeHashMap(r, sequence);
		final Sequences sequences = new Sequences(CollectionsUtil.makeArrayList(r), m);

		final List<String> messages = new ArrayList<String>(1);
		Assert.assertFalse(checker.checkConstraints(sequences, null, messages));
		Assert.assertEquals(1, messages.size());
	}

	/**
	 * Test simple sequence
	 */
	@Test
	public void testCheckSequence1() {
		final IPortTypeProviderEditor portTypeProvider = new HashMapPortTypeEditor();
		final IVesselProviderEditor vesselProvider = new HashMapVesselEditor();
		final IOrderedSequenceElementsDataComponentProvider orderedSequenceElementsProvider = new OrderedSequenceElementsDataComponentProvider();
		final PortTypeConstraintChecker checker = createChecker("checker", vesselProvider, portTypeProvider, orderedSequenceElementsProvider);

		final ISequenceElement o1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement o2 = Mockito.mock(ISequenceElement.class, "2");
		final ISequenceElement o3 = Mockito.mock(ISequenceElement.class, "3");
		final ISequenceElement o4 = Mockito.mock(ISequenceElement.class, "4");
		final ISequenceElement o5 = Mockito.mock(ISequenceElement.class, "5");
		final ISequenceElement o6 = Mockito.mock(ISequenceElement.class, "6");

		portTypeProvider.setPortType(o1, PortType.Start);
		portTypeProvider.setPortType(o2, PortType.Load);
		portTypeProvider.setPortType(o3, PortType.Discharge);
		portTypeProvider.setPortType(o4, PortType.Load);
		portTypeProvider.setPortType(o5, PortType.Discharge);
		portTypeProvider.setPortType(o6, PortType.End);

		final ISequence sequence = new ListSequence(CollectionsUtil.makeArrayList(o1, o2, o3, o4, o5, o6));

		final List<String> messages = new ArrayList<String>(1);
		Assert.assertTrue(checker.checkSequence(sequence, messages, VesselInstanceType.FLEET, false));

		Assert.assertEquals(0, messages.size());
	}

	/**
	 * Test only start fails
	 */
	@Test
	public void testCheckSequence2() {
		final IPortTypeProviderEditor portTypeProvider = new HashMapPortTypeEditor();
		final IVesselProviderEditor vesselProvider = new HashMapVesselEditor();
		final IOrderedSequenceElementsDataComponentProvider orderedSequenceElementsProvider = new OrderedSequenceElementsDataComponentProvider();
		final PortTypeConstraintChecker checker = createChecker("checker", vesselProvider, portTypeProvider, orderedSequenceElementsProvider);

		final ISequenceElement o1 = Mockito.mock(ISequenceElement.class, "1");

		portTypeProvider.setPortType(o1, PortType.Start);

		final ISequence sequence = new ListSequence(CollectionsUtil.makeArrayList(o1));

		final List<String> messages = new ArrayList<String>(1);
		Assert.assertFalse(checker.checkSequence(sequence, messages, VesselInstanceType.FLEET, false));

		Assert.assertEquals(1, messages.size());
	}

	/**
	 * Test only end fails
	 */

	@Test
	public void testCheckSequence3() {
		final IPortTypeProviderEditor portTypeProvider = new HashMapPortTypeEditor();
		final IVesselProviderEditor vesselProvider = new HashMapVesselEditor();
		final IOrderedSequenceElementsDataComponentProvider orderedSequenceElementsProvider = new OrderedSequenceElementsDataComponentProvider();
		final PortTypeConstraintChecker checker = createChecker("checker", vesselProvider, portTypeProvider, orderedSequenceElementsProvider);

		final ISequenceElement o1 = Mockito.mock(ISequenceElement.class, "1");

		portTypeProvider.setPortType(o1, PortType.End);

		final ISequence sequence = new ListSequence(CollectionsUtil.makeArrayList(o1));

		final List<String> messages = new ArrayList<String>(1);
		Assert.assertFalse(checker.checkSequence(sequence, messages, VesselInstanceType.FLEET, false));

		Assert.assertEquals(1, messages.size());
	}

	/**
	 * Test discharge before load fails
	 */

	@Test
	public void testCheckSequence4() {
		final IPortTypeProviderEditor portTypeProvider = new HashMapPortTypeEditor();
		final IVesselProviderEditor vesselProvider = new HashMapVesselEditor();
		final IOrderedSequenceElementsDataComponentProvider orderedSequenceElementsProvider = new OrderedSequenceElementsDataComponentProvider();
		final PortTypeConstraintChecker checker = createChecker("checker", vesselProvider, portTypeProvider, orderedSequenceElementsProvider);

		final ISequenceElement o1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement o2 = Mockito.mock(ISequenceElement.class, "2");
		final ISequenceElement o3 = Mockito.mock(ISequenceElement.class, "3");
		final ISequenceElement o4 = Mockito.mock(ISequenceElement.class, "4");

		portTypeProvider.setPortType(o1, PortType.Start);
		portTypeProvider.setPortType(o2, PortType.Discharge);
		portTypeProvider.setPortType(o3, PortType.Load);
		portTypeProvider.setPortType(o4, PortType.End);

		final ISequence sequence = new ListSequence(CollectionsUtil.makeArrayList(o1, o2, o3, o4));

		final List<String> messages = new ArrayList<String>(1);
		Assert.assertFalse(checker.checkSequence(sequence, messages, VesselInstanceType.FLEET, false));

		Assert.assertEquals(1, messages.size());
	}

	/**
	 * Test {@link PortType#Waypoint} insertion is ok
	 */
	@Test
	public void testCheckSequence5() {

		final IPortTypeProviderEditor portTypeProvider = new HashMapPortTypeEditor();
		final IVesselProviderEditor vesselProvider = new HashMapVesselEditor();
		final IOrderedSequenceElementsDataComponentProvider orderedSequenceElementsProvider = new OrderedSequenceElementsDataComponentProvider();
		final PortTypeConstraintChecker checker = createChecker("checker", vesselProvider, portTypeProvider, orderedSequenceElementsProvider);

		final ISequenceElement o1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement o2 = Mockito.mock(ISequenceElement.class, "2");
		final ISequenceElement o3 = Mockito.mock(ISequenceElement.class, "3");
		final ISequenceElement o4 = Mockito.mock(ISequenceElement.class, "4");
		final ISequenceElement o5 = Mockito.mock(ISequenceElement.class, "5");
		final ISequenceElement o6 = Mockito.mock(ISequenceElement.class, "6");
		final ISequenceElement o7 = Mockito.mock(ISequenceElement.class, "7");

		portTypeProvider.setPortType(o1, PortType.Start);
		portTypeProvider.setPortType(o2, PortType.Waypoint);
		portTypeProvider.setPortType(o3, PortType.Load);
		portTypeProvider.setPortType(o4, PortType.Waypoint);
		portTypeProvider.setPortType(o5, PortType.Discharge);
		portTypeProvider.setPortType(o6, PortType.Waypoint);
		portTypeProvider.setPortType(o7, PortType.End);

		final ISequence sequence = new ListSequence(CollectionsUtil.makeArrayList(o1, o2, o3, o4, o5, o6, o7));

		final List<String> messages = new ArrayList<String>(1);
		Assert.assertTrue(checker.checkSequence(sequence, messages, VesselInstanceType.FLEET, false));

		Assert.assertEquals(0, messages.size());
	}

	/**
	 * Test two loads fails
	 */
	@Test
	public void testCheckSequence6() {

		final IPortTypeProviderEditor portTypeProvider = new HashMapPortTypeEditor();
		final IVesselProviderEditor vesselProvider = new HashMapVesselEditor();
		final IOrderedSequenceElementsDataComponentProvider orderedSequenceElementsProvider = new OrderedSequenceElementsDataComponentProvider();
		final PortTypeConstraintChecker checker = createChecker("checker", vesselProvider, portTypeProvider, orderedSequenceElementsProvider);

		final ISequenceElement o1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement o2 = Mockito.mock(ISequenceElement.class, "2");
		final ISequenceElement o3 = Mockito.mock(ISequenceElement.class, "3");
		final ISequenceElement o4 = Mockito.mock(ISequenceElement.class, "4");
		final ISequenceElement o5 = Mockito.mock(ISequenceElement.class, "5");
		final ISequenceElement o6 = Mockito.mock(ISequenceElement.class, "6");

		portTypeProvider.setPortType(o1, PortType.Start);
		portTypeProvider.setPortType(o2, PortType.Waypoint);
		portTypeProvider.setPortType(o3, PortType.Load);
		portTypeProvider.setPortType(o4, PortType.Load);
		portTypeProvider.setPortType(o5, PortType.Discharge);
		portTypeProvider.setPortType(o6, PortType.End);

		final ISequence sequence = new ListSequence(CollectionsUtil.makeArrayList(o1, o2, o3, o4, o5, o6));

		final List<String> messages = new ArrayList<String>(1);
		Assert.assertFalse(checker.checkSequence(sequence, messages, VesselInstanceType.FLEET, false));

		Assert.assertEquals(1, messages.size());
	}

	/**
	 * Test two discharges fails
	 */
	@Test
	public void testCheckSequence7() {

		final IPortTypeProviderEditor portTypeProvider = new HashMapPortTypeEditor();
		final IVesselProviderEditor vesselProvider = new HashMapVesselEditor();
		final IOrderedSequenceElementsDataComponentProvider orderedSequenceElementsProvider = new OrderedSequenceElementsDataComponentProvider();
		final PortTypeConstraintChecker checker = createChecker("checker", vesselProvider, portTypeProvider, orderedSequenceElementsProvider);

		final ISequenceElement o1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement o2 = Mockito.mock(ISequenceElement.class, "2");
		final ISequenceElement o3 = Mockito.mock(ISequenceElement.class, "3");
		final ISequenceElement o4 = Mockito.mock(ISequenceElement.class, "4");
		final ISequenceElement o5 = Mockito.mock(ISequenceElement.class, "5");
		final ISequenceElement o6 = Mockito.mock(ISequenceElement.class, "6");

		portTypeProvider.setPortType(o1, PortType.Start);
		portTypeProvider.setPortType(o2, PortType.Waypoint);
		portTypeProvider.setPortType(o3, PortType.Load);
		portTypeProvider.setPortType(o4, PortType.Discharge);
		portTypeProvider.setPortType(o5, PortType.Discharge);
		portTypeProvider.setPortType(o6, PortType.End);

		final ISequence sequence = new ListSequence(CollectionsUtil.makeArrayList(o1, o2, o3, o4, o5, o6));

		final List<String> messages = new ArrayList<String>(1);
		Assert.assertFalse(checker.checkSequence(sequence, messages, VesselInstanceType.FLEET, false));

		Assert.assertEquals(1, messages.size());
	}

	/**
	 * Test other between load and discharge fails
	 */
	@Test
	public void testCheckSequence8() {

		final IPortTypeProviderEditor portTypeProvider = new HashMapPortTypeEditor();
		final IVesselProviderEditor vesselProvider = new HashMapVesselEditor();
		final IOrderedSequenceElementsDataComponentProvider orderedSequenceElementsProvider = new OrderedSequenceElementsDataComponentProvider();
		final PortTypeConstraintChecker checker = createChecker("checker", vesselProvider, portTypeProvider, orderedSequenceElementsProvider);

		final ISequenceElement o1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement o2 = Mockito.mock(ISequenceElement.class, "2");
		final ISequenceElement o3 = Mockito.mock(ISequenceElement.class, "3");
		final ISequenceElement o4 = Mockito.mock(ISequenceElement.class, "4");
		final ISequenceElement o5 = Mockito.mock(ISequenceElement.class, "5");

		portTypeProvider.setPortType(o1, PortType.Start);
		portTypeProvider.setPortType(o2, PortType.Load);
		portTypeProvider.setPortType(o3, PortType.Other);
		portTypeProvider.setPortType(o4, PortType.Discharge);
		portTypeProvider.setPortType(o5, PortType.End);

		final ISequence sequence = new ListSequence(CollectionsUtil.makeArrayList(o1, o2, o3, o4, o5));

		final List<String> messages = new ArrayList<String>(1);
		Assert.assertFalse(checker.checkSequence(sequence, messages, VesselInstanceType.FLEET, false));

		Assert.assertEquals(1, messages.size());
	}

	/**
	 * Test drydock between load and discharge fails
	 */
	@Test
	public void testCheckSequence9() {

		final IPortTypeProviderEditor portTypeProvider = new HashMapPortTypeEditor();
		final IVesselProviderEditor vesselProvider = new HashMapVesselEditor();
		final IOrderedSequenceElementsDataComponentProvider orderedSequenceElementsProvider = new OrderedSequenceElementsDataComponentProvider();
		final PortTypeConstraintChecker checker = createChecker("checker", vesselProvider, portTypeProvider, orderedSequenceElementsProvider);

		final ISequenceElement o1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement o2 = Mockito.mock(ISequenceElement.class, "2");
		final ISequenceElement o3 = Mockito.mock(ISequenceElement.class, "3");
		final ISequenceElement o4 = Mockito.mock(ISequenceElement.class, "4");
		final ISequenceElement o5 = Mockito.mock(ISequenceElement.class, "5");

		portTypeProvider.setPortType(o1, PortType.Start);
		portTypeProvider.setPortType(o2, PortType.Load);
		portTypeProvider.setPortType(o3, PortType.DryDock);
		portTypeProvider.setPortType(o4, PortType.Discharge);
		portTypeProvider.setPortType(o5, PortType.End);

		final ISequence sequence = new ListSequence(CollectionsUtil.makeArrayList(o1, o2, o3, o4, o5));

		final List<String> messages = new ArrayList<String>(1);
		Assert.assertFalse(checker.checkSequence(sequence, messages, VesselInstanceType.FLEET, false));

		Assert.assertEquals(1, messages.size());
	}

	/**
	 * Test drydock outside load and discharge is ok
	 */
	@Test
	public void testCheckSequence10() {

		final IPortTypeProviderEditor portTypeProvider = new HashMapPortTypeEditor();
		final IVesselProviderEditor vesselProvider = new HashMapVesselEditor();
		final IOrderedSequenceElementsDataComponentProvider orderedSequenceElementsProvider = new OrderedSequenceElementsDataComponentProvider();
		final PortTypeConstraintChecker checker = createChecker("checker", vesselProvider, portTypeProvider, orderedSequenceElementsProvider);

		final ISequenceElement o1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement o2 = Mockito.mock(ISequenceElement.class, "2");
		final ISequenceElement o3 = Mockito.mock(ISequenceElement.class, "3");
		final ISequenceElement o4 = Mockito.mock(ISequenceElement.class, "4");
		final ISequenceElement o5 = Mockito.mock(ISequenceElement.class, "5");
		final ISequenceElement o6 = Mockito.mock(ISequenceElement.class, "6");

		portTypeProvider.setPortType(o1, PortType.Start);
		portTypeProvider.setPortType(o2, PortType.DryDock);
		portTypeProvider.setPortType(o3, PortType.Load);
		portTypeProvider.setPortType(o4, PortType.Discharge);
		portTypeProvider.setPortType(o5, PortType.DryDock);
		portTypeProvider.setPortType(o6, PortType.End);

		final ISequence sequence = new ListSequence(CollectionsUtil.makeArrayList(o1, o2, o3, o4, o5, o6));

		final List<String> messages = new ArrayList<String>(1);
		Assert.assertTrue(checker.checkSequence(sequence, messages, VesselInstanceType.FLEET, false));

		Assert.assertEquals(0, messages.size());
	}

	/**
	 * Test other outside load and discharge is ok
	 */
	@Test
	public void testCheckSequence11() {

		final IPortTypeProviderEditor portTypeProvider = new HashMapPortTypeEditor();
		final IVesselProviderEditor vesselProvider = new HashMapVesselEditor();
		final IOrderedSequenceElementsDataComponentProvider orderedSequenceElementsProvider = new OrderedSequenceElementsDataComponentProvider();
		final PortTypeConstraintChecker checker = createChecker("checker", vesselProvider, portTypeProvider, orderedSequenceElementsProvider);

		final ISequenceElement o1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement o2 = Mockito.mock(ISequenceElement.class, "2");
		final ISequenceElement o3 = Mockito.mock(ISequenceElement.class, "3");
		final ISequenceElement o4 = Mockito.mock(ISequenceElement.class, "4");
		final ISequenceElement o5 = Mockito.mock(ISequenceElement.class, "5");
		final ISequenceElement o6 = Mockito.mock(ISequenceElement.class, "6");

		portTypeProvider.setPortType(o1, PortType.Start);
		portTypeProvider.setPortType(o2, PortType.Other);
		portTypeProvider.setPortType(o3, PortType.Load);
		portTypeProvider.setPortType(o4, PortType.Discharge);
		portTypeProvider.setPortType(o5, PortType.Other);
		portTypeProvider.setPortType(o6, PortType.End);

		final ISequence sequence = new ListSequence(CollectionsUtil.makeArrayList(o1, o2, o3, o4, o5, o6));

		final List<String> messages = new ArrayList<String>(1);
		Assert.assertTrue(checker.checkSequence(sequence, messages, VesselInstanceType.FLEET, false));

		Assert.assertEquals(0, messages.size());
	}

	/**
	 * Test multiple start fails
	 */
	@Test
	public void testCheckSequence12() {

		final IPortTypeProviderEditor portTypeProvider = new HashMapPortTypeEditor();
		final IVesselProviderEditor vesselProvider = new HashMapVesselEditor();
		final IOrderedSequenceElementsDataComponentProvider orderedSequenceElementsProvider = new OrderedSequenceElementsDataComponentProvider();
		final PortTypeConstraintChecker checker = createChecker("checker", vesselProvider, portTypeProvider, orderedSequenceElementsProvider);

		final ISequenceElement o1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement o2 = Mockito.mock(ISequenceElement.class, "2");
		final ISequenceElement o3 = Mockito.mock(ISequenceElement.class, "3");
		final ISequenceElement o4 = Mockito.mock(ISequenceElement.class, "4");
		final ISequenceElement o5 = Mockito.mock(ISequenceElement.class, "5");
		final ISequenceElement o6 = Mockito.mock(ISequenceElement.class, "6");

		portTypeProvider.setPortType(o1, PortType.Start);
		portTypeProvider.setPortType(o2, PortType.Start);
		portTypeProvider.setPortType(o3, PortType.Load);
		portTypeProvider.setPortType(o4, PortType.Discharge);
		portTypeProvider.setPortType(o5, PortType.DryDock);
		portTypeProvider.setPortType(o6, PortType.End);

		final ISequence sequence = new ListSequence(CollectionsUtil.makeArrayList(o1, o2, o3, o4, o5, o6));

		final List<String> messages = new ArrayList<String>(1);
		Assert.assertFalse(checker.checkSequence(sequence, messages, VesselInstanceType.FLEET, false));

		Assert.assertEquals(1, messages.size());
	}

	/**
	 * Test multiple end fails
	 */
	@Test
	public void testCheckSequence13() {

		final IPortTypeProviderEditor portTypeProvider = new HashMapPortTypeEditor();
		final IVesselProviderEditor vesselProvider = new HashMapVesselEditor();
		final IOrderedSequenceElementsDataComponentProvider orderedSequenceElementsProvider = new OrderedSequenceElementsDataComponentProvider();
		final PortTypeConstraintChecker checker = createChecker("checker", vesselProvider, portTypeProvider, orderedSequenceElementsProvider);

		final ISequenceElement o1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement o2 = Mockito.mock(ISequenceElement.class, "2");
		final ISequenceElement o3 = Mockito.mock(ISequenceElement.class, "3");
		final ISequenceElement o4 = Mockito.mock(ISequenceElement.class, "4");
		final ISequenceElement o5 = Mockito.mock(ISequenceElement.class, "5");
		final ISequenceElement o6 = Mockito.mock(ISequenceElement.class, "6");

		portTypeProvider.setPortType(o1, PortType.Start);
		portTypeProvider.setPortType(o2, PortType.DryDock);
		portTypeProvider.setPortType(o3, PortType.Load);
		portTypeProvider.setPortType(o4, PortType.Discharge);
		portTypeProvider.setPortType(o5, PortType.End);
		portTypeProvider.setPortType(o6, PortType.End);

		final ISequence sequence = new ListSequence(CollectionsUtil.makeArrayList(o1, o2, o3, o4, o5, o6));

		final List<String> messages = new ArrayList<String>(1);
		Assert.assertFalse(checker.checkSequence(sequence, messages, VesselInstanceType.FLEET, false));

		Assert.assertEquals(1, messages.size());
	}

	/**
	 * Test maintenance between load and discharge fails
	 */
	@Test
	public void testCheckSequence14() {

		final IPortTypeProviderEditor portTypeProvider = new HashMapPortTypeEditor();
		final IVesselProviderEditor vesselProvider = new HashMapVesselEditor();
		final IOrderedSequenceElementsDataComponentProvider orderedSequenceElementsProvider = new OrderedSequenceElementsDataComponentProvider();
		final PortTypeConstraintChecker checker = createChecker("checker", vesselProvider, portTypeProvider, orderedSequenceElementsProvider);

		final ISequenceElement o1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement o2 = Mockito.mock(ISequenceElement.class, "2");
		final ISequenceElement o3 = Mockito.mock(ISequenceElement.class, "3");
		final ISequenceElement o4 = Mockito.mock(ISequenceElement.class, "4");
		final ISequenceElement o5 = Mockito.mock(ISequenceElement.class, "5");

		portTypeProvider.setPortType(o1, PortType.Start);
		portTypeProvider.setPortType(o2, PortType.Load);
		portTypeProvider.setPortType(o3, PortType.Maintenance);
		portTypeProvider.setPortType(o4, PortType.Discharge);
		portTypeProvider.setPortType(o5, PortType.End);

		final ISequence sequence = new ListSequence(CollectionsUtil.makeArrayList(o1, o2, o3, o4, o5));

		final List<String> messages = new ArrayList<String>(1);
		Assert.assertFalse(checker.checkSequence(sequence, messages, VesselInstanceType.FLEET, false));

		Assert.assertEquals(1, messages.size());
	}

	/**
	 * Test maintenance outside load and discharge is ok
	 */
	@Test
	public void testCheckSequence15() {

		final IPortTypeProviderEditor portTypeProvider = new HashMapPortTypeEditor();
		final IVesselProviderEditor vesselProvider = new HashMapVesselEditor();
		final IOrderedSequenceElementsDataComponentProvider orderedSequenceElementsProvider = new OrderedSequenceElementsDataComponentProvider();
		final PortTypeConstraintChecker checker = createChecker("checker", vesselProvider, portTypeProvider, orderedSequenceElementsProvider);

		final ISequenceElement o1 = Mockito.mock(ISequenceElement.class, "1");
		final ISequenceElement o2 = Mockito.mock(ISequenceElement.class, "2");
		final ISequenceElement o3 = Mockito.mock(ISequenceElement.class, "3");
		final ISequenceElement o4 = Mockito.mock(ISequenceElement.class, "4");
		final ISequenceElement o5 = Mockito.mock(ISequenceElement.class, "5");
		final ISequenceElement o6 = Mockito.mock(ISequenceElement.class, "6");

		portTypeProvider.setPortType(o1, PortType.Start);
		portTypeProvider.setPortType(o2, PortType.DryDock);
		portTypeProvider.setPortType(o3, PortType.Load);
		portTypeProvider.setPortType(o4, PortType.Discharge);
		portTypeProvider.setPortType(o5, PortType.Maintenance);
		portTypeProvider.setPortType(o6, PortType.End);

		final ISequence sequence = new ListSequence(CollectionsUtil.makeArrayList(o1, o2, o3, o4, o5, o6));

		final List<String> messages = new ArrayList<String>(1);
		Assert.assertTrue(checker.checkSequence(sequence, messages, VesselInstanceType.FLEET, false));

		Assert.assertEquals(0, messages.size());
	}

	private PortTypeConstraintChecker createChecker(final String name, final IVesselProvider vesselProvider, final IPortTypeProvider portTypeProvider,
			final IOrderedSequenceElementsDataComponentProvider orderedSequenceProvider) {
		final Injector injector = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
				bind(IVesselProvider.class).toInstance(vesselProvider);
				bind(IPortTypeProvider.class).toInstance(portTypeProvider);
				bind(IOrderedSequenceElementsDataComponentProvider.class).toInstance(orderedSequenceProvider);
			}

			@Provides
			PortTypeConstraintChecker create(final Injector injector) {
				final PortTypeConstraintChecker checker = new PortTypeConstraintChecker(name);
				injector.injectMembers(checker);
				return checker;
			}

		});
		return injector.getInstance(PortTypeConstraintChecker.class);
	}
}
