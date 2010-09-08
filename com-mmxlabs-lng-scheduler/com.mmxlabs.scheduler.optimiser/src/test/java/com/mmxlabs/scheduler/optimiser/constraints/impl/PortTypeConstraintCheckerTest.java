package com.mmxlabs.scheduler.optimiser.constraints.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.impl.ListSequence;
import com.mmxlabs.optimiser.core.impl.Sequences;
import com.mmxlabs.optimiser.core.scenario.impl.OptimisationData;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapPortTypeEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapVesselEditor;

@RunWith(JMock.class)
public class PortTypeConstraintCheckerTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testPortTypeConstraintChecker() {

		final String name = "checker";
		final String key = "key";
		final String vkey = "vkey";
		final PortTypeConstraintChecker<Object> checker = new PortTypeConstraintChecker<Object>(
				name, key, vkey);

		Assert.assertSame(name, checker.getName());
	}

	@Test
	public void testCheckConstraintsISequencesOfT1() {

		final PortTypeConstraintChecker<Object> checker = new PortTypeConstraintChecker<Object>(
				"checker", "key", "vkey");

		final IPortTypeProviderEditor<Object> portTypeProvider = new HashMapPortTypeEditor<Object>(
				"key");
		checker.setPortTypeProvider(portTypeProvider);

		final IVesselProviderEditor vesselProvider = new HashMapVesselEditor("vkey"); 

		checker.setVesselProvider(vesselProvider);
		
		final Object o1 = new Object();
		final Object o2 = new Object();
		final Object o3 = new Object();
		final Object o4 = new Object();
		final Object o5 = new Object();
		final Object o6 = new Object();
		portTypeProvider.setPortType(o1, PortType.Start);
		portTypeProvider.setPortType(o2, PortType.Load);
		portTypeProvider.setPortType(o3, PortType.Discharge);
		portTypeProvider.setPortType(o4, PortType.Load);
		portTypeProvider.setPortType(o5, PortType.Discharge);
		portTypeProvider.setPortType(o6, PortType.End);

		final ISequence<Object> sequence = new ListSequence<Object>(
				CollectionsUtil.makeArrayList(o1, o2, o3, o4, o5, o6));

		final IResource r = context.mock(IResource.class);

		final IVessel v = context.mock(IVessel.class);
		
		vesselProvider.setVesselResource(r, v);
		
		context.checking(new Expectations() {{
			atLeast(1).of(v).getVesselInstanceType();
			will(returnValue(VesselInstanceType.FLEET));
		}});
		
		final Map<IResource, ISequence<Object>> m = CollectionsUtil.makeHashMap(r,
				sequence);
		final Sequences<Object> sequences = new Sequences<Object>(
				CollectionsUtil.makeArrayList(r), m);

		Assert.assertTrue(checker.checkConstraints(sequences));
	}
	
	@Test
	public void testCheckConstraintsISequencesOfT2() {

		final PortTypeConstraintChecker<Object> checker = new PortTypeConstraintChecker<Object>(
				"checker", "key", "vkey");

		final IPortTypeProviderEditor<Object> portTypeProvider = new HashMapPortTypeEditor<Object>(
				"key");
		checker.setPortTypeProvider(portTypeProvider);

		final IVesselProviderEditor vesselProvider = new HashMapVesselEditor("vkey"); 

		checker.setVesselProvider(vesselProvider);
		
		final Object o1 = new Object();
		final Object o2 = new Object();
		final Object o3 = new Object();
		final Object o4 = new Object();
		final Object o5 = new Object();
		final Object o6 = new Object();
		portTypeProvider.setPortType(o1, PortType.Start);
		portTypeProvider.setPortType(o2, PortType.Discharge);
		portTypeProvider.setPortType(o3, PortType.Load);
		portTypeProvider.setPortType(o4, PortType.Discharge);
		portTypeProvider.setPortType(o5, PortType.Load);
		portTypeProvider.setPortType(o6, PortType.End);

		final ISequence<Object> sequence = new ListSequence<Object>(
				CollectionsUtil.makeArrayList(o1, o2, o3, o4, o5, o6));

		final IResource r = context.mock(IResource.class);
		
		final IVessel v = context.mock(IVessel.class);
		
		vesselProvider.setVesselResource(r, v);
		
		context.checking(new Expectations() {{
			atLeast(1).of(v).getVesselInstanceType();
			will(returnValue(VesselInstanceType.FLEET));
		}});
		
		final Map<IResource, ISequence<Object>> m = CollectionsUtil.makeHashMap(r,
				sequence);
		final Sequences<Object> sequences = new Sequences<Object>(
				CollectionsUtil.makeArrayList(r), m);

		Assert.assertFalse(checker.checkConstraints(sequences));
	}

	@Test
	public void testCheckConstraintsISequencesOfTListOfString1() {

		final PortTypeConstraintChecker<Object> checker = new PortTypeConstraintChecker<Object>(
				"checker", "key", "vkey");

		final IPortTypeProviderEditor<Object> portTypeProvider = new HashMapPortTypeEditor<Object>(
				"key");
		checker.setPortTypeProvider(portTypeProvider);

		final IVesselProviderEditor vesselProvider = new HashMapVesselEditor("vkey"); 

		checker.setVesselProvider(vesselProvider);
		
		
		final Object o1 = new Object();
		final Object o2 = new Object();
		final Object o3 = new Object();
		final Object o4 = new Object();
		final Object o5 = new Object();
		final Object o6 = new Object();
		portTypeProvider.setPortType(o1, PortType.Start);
		portTypeProvider.setPortType(o2, PortType.Load);
		portTypeProvider.setPortType(o3, PortType.Discharge);
		portTypeProvider.setPortType(o4, PortType.Load);
		portTypeProvider.setPortType(o5, PortType.Discharge);
		portTypeProvider.setPortType(o6, PortType.End);

		final ISequence<Object> sequence = new ListSequence<Object>(
				CollectionsUtil.makeArrayList(o1, o2, o3, o4, o5, o6));

		final IResource r = context.mock(IResource.class);
		final IVessel v = context.mock(IVessel.class);
		
		vesselProvider.setVesselResource(r, v);
		
		context.checking(new Expectations() {{
			atLeast(1).of(v).getVesselInstanceType();
			will(returnValue(VesselInstanceType.FLEET));
		}});
		final Map<IResource, ISequence<Object>> m = CollectionsUtil.makeHashMap(r,
				sequence);
		final Sequences<Object> sequences = new Sequences<Object>(
				CollectionsUtil.makeArrayList(r), m);

		final List<String> messages = new ArrayList<String>(1);
		Assert.assertTrue(checker.checkConstraints(sequences, messages));
		Assert.assertEquals(0, messages.size());
	}
	
	@Test
	public void testCheckConstraintsISequencesOfTListOfString2() {

		final PortTypeConstraintChecker<Object> checker = new PortTypeConstraintChecker<Object>(
				"checker", "key", "vkey");

		final IPortTypeProviderEditor<Object> portTypeProvider = new HashMapPortTypeEditor<Object>(
				"key");
		checker.setPortTypeProvider(portTypeProvider);
		
		final IVesselProviderEditor vesselProvider = new HashMapVesselEditor("vkey"); 

		checker.setVesselProvider(vesselProvider);
		
		final Object o1 = new Object();
		final Object o2 = new Object();
		final Object o3 = new Object();
		final Object o4 = new Object();
		final Object o5 = new Object();
		final Object o6 = new Object();
		portTypeProvider.setPortType(o1, PortType.Start);
		portTypeProvider.setPortType(o2, PortType.Discharge);
		portTypeProvider.setPortType(o3, PortType.Load);
		portTypeProvider.setPortType(o4, PortType.Discharge);
		portTypeProvider.setPortType(o5, PortType.Load);
		portTypeProvider.setPortType(o6, PortType.End);

		final ISequence<Object> sequence = new ListSequence<Object>(
				CollectionsUtil.makeArrayList(o1, o2, o3, o4, o5, o6));

		final IResource r = context.mock(IResource.class);
		final IVessel v = context.mock(IVessel.class);
		
		vesselProvider.setVesselResource(r, v);
		
		context.checking(new Expectations() {{
			atLeast(1).of(v).getVesselInstanceType();
			will(returnValue(VesselInstanceType.FLEET));
		}});
		final Map<IResource, ISequence<Object>> m = CollectionsUtil.makeHashMap(r,
				sequence);
		final Sequences<Object> sequences = new Sequences<Object>(
				CollectionsUtil.makeArrayList(r), m);
		
		final List<String> messages = new ArrayList<String>(1);
		Assert.assertFalse(checker.checkConstraints(sequences, messages));
		Assert.assertEquals(1, messages.size());
	}

	@Test
	public void testSetOptimisationData() {

		final String name = "checker";
		final String key = "key";
		final String vkey = "vkey";
		
		@SuppressWarnings("unchecked")
		final IPortTypeProvider<Object> portTypeProvider = context
				.mock(IPortTypeProvider.class);
		final OptimisationData<Object> data = new OptimisationData<Object>();
		data.addDataComponentProvider(key, portTypeProvider);
		
		final IVesselProvider vesselProvider = context.mock(IVesselProvider.class);
		data.addDataComponentProvider(vkey, vesselProvider);
		final PortTypeConstraintChecker<Object> checker = new PortTypeConstraintChecker<Object>(
				name, key, vkey);
		checker.setOptimisationData(data);

		Assert.assertSame(portTypeProvider, checker.getPortTypeProvider());
		Assert.assertSame(vesselProvider, checker.getVesselProvider());
	}

	/**
	 * Test simple sequence
	 */
	@Test
	public void testCheckSequence1() {
		final PortTypeConstraintChecker<Object> checker = new PortTypeConstraintChecker<Object>(
				"checker", "key", "vkey");

		final IPortTypeProviderEditor<Object> portTypeProvider = new HashMapPortTypeEditor<Object>(
				"key");
		checker.setPortTypeProvider(portTypeProvider);
		final IVesselProviderEditor vesselProvider = new HashMapVesselEditor("vkey"); 

		checker.setVesselProvider(vesselProvider);
		
		final Object o1 = new Object();
		final Object o2 = new Object();
		final Object o3 = new Object();
		final Object o4 = new Object();
		final Object o5 = new Object();
		final Object o6 = new Object();
		portTypeProvider.setPortType(o1, PortType.Start);
		portTypeProvider.setPortType(o2, PortType.Load);
		portTypeProvider.setPortType(o3, PortType.Discharge);
		portTypeProvider.setPortType(o4, PortType.Load);
		portTypeProvider.setPortType(o5, PortType.Discharge);
		portTypeProvider.setPortType(o6, PortType.End);

		final ISequence<Object> sequence = new ListSequence<Object>(
				CollectionsUtil.makeArrayList(o1, o2, o3, o4, o5, o6));

		
		final List<String> messages = new ArrayList<String>(1);
		Assert.assertTrue(checker.checkSequence(sequence, messages, VesselInstanceType.FLEET));

		Assert.assertEquals(0, messages.size());
	}

	/**
	 * Test only start fails
	 */
	@Test
	public void testCheckSequence2() {
		final PortTypeConstraintChecker<Object> checker = new PortTypeConstraintChecker<Object>(
				"checker", "key", "vkey");

		final IPortTypeProviderEditor<Object> portTypeProvider = new HashMapPortTypeEditor<Object>(
				"key");
		checker.setPortTypeProvider(portTypeProvider);
		final IVesselProviderEditor vesselProvider = new HashMapVesselEditor("vkey"); 

		checker.setVesselProvider(vesselProvider);
		
		final Object o1 = new Object();
		portTypeProvider.setPortType(o1, PortType.Start);

		final ISequence<Object> sequence = new ListSequence<Object>(
				CollectionsUtil.makeArrayList(o1));

		final List<String> messages = new ArrayList<String>(1);
		Assert.assertFalse(checker.checkSequence(sequence, messages, VesselInstanceType.FLEET));

		Assert.assertEquals(1, messages.size());
	}

	/**
	 * Test only end fails
	 */

	@Test
	public void testCheckSequence3() {
		final PortTypeConstraintChecker<Object> checker = new PortTypeConstraintChecker<Object>(
				"checker", "key", "vkey");

		final IPortTypeProviderEditor<Object> portTypeProvider = new HashMapPortTypeEditor<Object>(
				"key");
		checker.setPortTypeProvider(portTypeProvider);
		final IVesselProviderEditor vesselProvider = new HashMapVesselEditor("vkey"); 

		checker.setVesselProvider(vesselProvider);
		
		final Object o1 = new Object();
		portTypeProvider.setPortType(o1, PortType.End);

		final ISequence<Object> sequence = new ListSequence<Object>(
				CollectionsUtil.makeArrayList(o1));

		final List<String> messages = new ArrayList<String>(1);
		Assert.assertFalse(checker.checkSequence(sequence, messages, VesselInstanceType.FLEET));

		Assert.assertEquals(1, messages.size());
	}

	/**
	 * Test discharge before load fails
	 */

	@Test
	public void testCheckSequence4() {
		final PortTypeConstraintChecker<Object> checker = new PortTypeConstraintChecker<Object>(
				"checker", "key", "vkey");

		final IPortTypeProviderEditor<Object> portTypeProvider = new HashMapPortTypeEditor<Object>(
				"key");
		checker.setPortTypeProvider(portTypeProvider);
		final IVesselProviderEditor vesselProvider = new HashMapVesselEditor("vkey"); 

		checker.setVesselProvider(vesselProvider);
		
		final Object o1 = new Object();
		final Object o2 = new Object();
		final Object o3 = new Object();
		final Object o4 = new Object();
		portTypeProvider.setPortType(o1, PortType.Start);
		portTypeProvider.setPortType(o2, PortType.Discharge);
		portTypeProvider.setPortType(o3, PortType.Load);
		portTypeProvider.setPortType(o4, PortType.End);

		final ISequence<Object> sequence = new ListSequence<Object>(
				CollectionsUtil.makeArrayList(o1, o2, o3, o4));

		final List<String> messages = new ArrayList<String>(1);
		Assert.assertFalse(checker.checkSequence(sequence, messages, VesselInstanceType.FLEET));

		Assert.assertEquals(1, messages.size());
	}

	/**
	 * Test {@link PortType#Waypoint} insertion is ok
	 */
	@Test
	public void testCheckSequence5() {

		final PortTypeConstraintChecker<Object> checker = new PortTypeConstraintChecker<Object>(
				"checker", "key", "vkey");

		final IPortTypeProviderEditor<Object> portTypeProvider = new HashMapPortTypeEditor<Object>(
				"key");
		checker.setPortTypeProvider(portTypeProvider);
		final IVesselProviderEditor vesselProvider = new HashMapVesselEditor("vkey"); 

		checker.setVesselProvider(vesselProvider);
		
		final Object o1 = new Object();
		final Object o2 = new Object();
		final Object o3 = new Object();
		final Object o4 = new Object();
		final Object o5 = new Object();
		final Object o6 = new Object();
		final Object o7 = new Object();

		portTypeProvider.setPortType(o1, PortType.Start);
		portTypeProvider.setPortType(o2, PortType.Waypoint);
		portTypeProvider.setPortType(o3, PortType.Load);
		portTypeProvider.setPortType(o4, PortType.Waypoint);
		portTypeProvider.setPortType(o5, PortType.Discharge);
		portTypeProvider.setPortType(o6, PortType.Waypoint);
		portTypeProvider.setPortType(o7, PortType.End);

		final ISequence<Object> sequence = new ListSequence<Object>(
				CollectionsUtil.makeArrayList(o1, o2, o3, o4, o5, o6, o7));

		final List<String> messages = new ArrayList<String>(1);
		Assert.assertTrue(checker.checkSequence(sequence, messages, VesselInstanceType.FLEET));

		Assert.assertEquals(0, messages.size());
	}

	/**
	 * Test two loads fails
	 */
	@Test
	public void testCheckSequence6() {

		final PortTypeConstraintChecker<Object> checker = new PortTypeConstraintChecker<Object>(
				"checker", "key", "vkey");

		final IPortTypeProviderEditor<Object> portTypeProvider = new HashMapPortTypeEditor<Object>(
				"key");
		checker.setPortTypeProvider(portTypeProvider);
		final IVesselProviderEditor vesselProvider = new HashMapVesselEditor("vkey"); 

		checker.setVesselProvider(vesselProvider);
		
		final Object o1 = new Object();
		final Object o2 = new Object();
		final Object o3 = new Object();
		final Object o4 = new Object();
		final Object o5 = new Object();
		final Object o6 = new Object();

		portTypeProvider.setPortType(o1, PortType.Start);
		portTypeProvider.setPortType(o2, PortType.Waypoint);
		portTypeProvider.setPortType(o3, PortType.Load);
		portTypeProvider.setPortType(o4, PortType.Load);
		portTypeProvider.setPortType(o5, PortType.Discharge);
		portTypeProvider.setPortType(o6, PortType.End);

		final ISequence<Object> sequence = new ListSequence<Object>(
				CollectionsUtil.makeArrayList(o1, o2, o3, o4, o5, o6));

		final List<String> messages = new ArrayList<String>(1);
		Assert.assertFalse(checker.checkSequence(sequence, messages, VesselInstanceType.FLEET));

		Assert.assertEquals(1, messages.size());
	}

	/**
	 * Test two discharges fails
	 */
	@Test
	public void testCheckSequence7() {

		final PortTypeConstraintChecker<Object> checker = new PortTypeConstraintChecker<Object>(
				"checker", "key", "vkey");

		final IPortTypeProviderEditor<Object> portTypeProvider = new HashMapPortTypeEditor<Object>(
				"key");
		checker.setPortTypeProvider(portTypeProvider);
		final IVesselProviderEditor vesselProvider = new HashMapVesselEditor("vkey"); 

		checker.setVesselProvider(vesselProvider);
		
		final Object o1 = new Object();
		final Object o2 = new Object();
		final Object o3 = new Object();
		final Object o4 = new Object();
		final Object o5 = new Object();
		final Object o6 = new Object();

		portTypeProvider.setPortType(o1, PortType.Start);
		portTypeProvider.setPortType(o2, PortType.Waypoint);
		portTypeProvider.setPortType(o3, PortType.Load);
		portTypeProvider.setPortType(o4, PortType.Discharge);
		portTypeProvider.setPortType(o5, PortType.Discharge);
		portTypeProvider.setPortType(o6, PortType.End);

		final ISequence<Object> sequence = new ListSequence<Object>(
				CollectionsUtil.makeArrayList(o1, o2, o3, o4, o5, o6));

		final List<String> messages = new ArrayList<String>(1);
		Assert.assertFalse(checker.checkSequence(sequence, messages, VesselInstanceType.FLEET));

		Assert.assertEquals(1, messages.size());
	}

	/**
	 * Test other between load and discharge fails
	 */
	@Test
	public void testCheckSequence8() {

		final PortTypeConstraintChecker<Object> checker = new PortTypeConstraintChecker<Object>(
				"checker", "key", "vkey");

		final IPortTypeProviderEditor<Object> portTypeProvider = new HashMapPortTypeEditor<Object>(
				"key");
		checker.setPortTypeProvider(portTypeProvider);
		final IVesselProviderEditor vesselProvider = new HashMapVesselEditor("vkey"); 

		checker.setVesselProvider(vesselProvider);
		
		final Object o1 = new Object();
		final Object o2 = new Object();
		final Object o3 = new Object();
		final Object o4 = new Object();
		final Object o5 = new Object();

		portTypeProvider.setPortType(o1, PortType.Start);
		portTypeProvider.setPortType(o2, PortType.Load);
		portTypeProvider.setPortType(o3, PortType.Other);
		portTypeProvider.setPortType(o4, PortType.Discharge);
		portTypeProvider.setPortType(o5, PortType.End);

		final ISequence<Object> sequence = new ListSequence<Object>(
				CollectionsUtil.makeArrayList(o1, o2, o3, o4, o5));

		final List<String> messages = new ArrayList<String>(1);
		Assert.assertFalse(checker.checkSequence(sequence, messages, VesselInstanceType.FLEET));

		Assert.assertEquals(1, messages.size());
	}

	/**
	 * Test drydock between load and discharge fails
	 */
	@Test
	public void testCheckSequence9() {

		final PortTypeConstraintChecker<Object> checker = new PortTypeConstraintChecker<Object>(
				"checker", "key", "vkey");

		final IPortTypeProviderEditor<Object> portTypeProvider = new HashMapPortTypeEditor<Object>(
				"key");
		checker.setPortTypeProvider(portTypeProvider);
		final IVesselProviderEditor vesselProvider = new HashMapVesselEditor("vkey"); 

		checker.setVesselProvider(vesselProvider);
		
		final Object o1 = new Object();
		final Object o2 = new Object();
		final Object o3 = new Object();
		final Object o4 = new Object();
		final Object o5 = new Object();

		portTypeProvider.setPortType(o1, PortType.Start);
		portTypeProvider.setPortType(o2, PortType.Load);
		portTypeProvider.setPortType(o3, PortType.DryDock);
		portTypeProvider.setPortType(o4, PortType.Discharge);
		portTypeProvider.setPortType(o5, PortType.End);

		final ISequence<Object> sequence = new ListSequence<Object>(
				CollectionsUtil.makeArrayList(o1, o2, o3, o4, o5));

		final List<String> messages = new ArrayList<String>(1);
		Assert.assertFalse(checker.checkSequence(sequence, messages, VesselInstanceType.FLEET));

		Assert.assertEquals(1, messages.size());
	}

	/**
	 * Test drydock outside load and discharge is ok
	 */
	@Test
	public void testCheckSequence10() {

		final PortTypeConstraintChecker<Object> checker = new PortTypeConstraintChecker<Object>(
				"checker", "key", "vkey");

		final IPortTypeProviderEditor<Object> portTypeProvider = new HashMapPortTypeEditor<Object>(
				"key");
		checker.setPortTypeProvider(portTypeProvider);
		final IVesselProviderEditor vesselProvider = new HashMapVesselEditor("vkey"); 

		checker.setVesselProvider(vesselProvider);
		
		final Object o1 = new Object();
		final Object o2 = new Object();
		final Object o3 = new Object();
		final Object o4 = new Object();
		final Object o5 = new Object();
		final Object o6 = new Object();

		portTypeProvider.setPortType(o1, PortType.Start);
		portTypeProvider.setPortType(o2, PortType.DryDock);
		portTypeProvider.setPortType(o3, PortType.Load);
		portTypeProvider.setPortType(o4, PortType.Discharge);
		portTypeProvider.setPortType(o5, PortType.DryDock);
		portTypeProvider.setPortType(o6, PortType.End);

		final ISequence<Object> sequence = new ListSequence<Object>(
				CollectionsUtil.makeArrayList(o1, o2, o3, o4, o5, o6));

		final List<String> messages = new ArrayList<String>(1);
		Assert.assertTrue(checker.checkSequence(sequence, messages, VesselInstanceType.FLEET));

		Assert.assertEquals(0, messages.size());
	}

	/**
	 * Test other outside load and discharge is ok
	 */
	@Test
	public void testCheckSequence11() {

		final PortTypeConstraintChecker<Object> checker = new PortTypeConstraintChecker<Object>(
				"checker", "key", "vkey");

		final IPortTypeProviderEditor<Object> portTypeProvider = new HashMapPortTypeEditor<Object>(
				"key");
		checker.setPortTypeProvider(portTypeProvider);
		final IVesselProviderEditor vesselProvider = new HashMapVesselEditor("vkey"); 

		checker.setVesselProvider(vesselProvider);
		
		final Object o1 = new Object();
		final Object o2 = new Object();
		final Object o3 = new Object();
		final Object o4 = new Object();
		final Object o5 = new Object();
		final Object o6 = new Object();

		portTypeProvider.setPortType(o1, PortType.Start);
		portTypeProvider.setPortType(o2, PortType.Other);
		portTypeProvider.setPortType(o3, PortType.Load);
		portTypeProvider.setPortType(o4, PortType.Discharge);
		portTypeProvider.setPortType(o5, PortType.Other);
		portTypeProvider.setPortType(o6, PortType.End);

		final ISequence<Object> sequence = new ListSequence<Object>(
				CollectionsUtil.makeArrayList(o1, o2, o3, o4, o5, o6));

		final List<String> messages = new ArrayList<String>(1);
		Assert.assertTrue(checker.checkSequence(sequence, messages, VesselInstanceType.FLEET));

		Assert.assertEquals(0, messages.size());
	}

	/**
	 * Test multiple start fails
	 */
	@Test
	public void testCheckSequence12() {

		final PortTypeConstraintChecker<Object> checker = new PortTypeConstraintChecker<Object>(
				"checker", "key", "vkey");

		final IPortTypeProviderEditor<Object> portTypeProvider = new HashMapPortTypeEditor<Object>(
				"key");
		checker.setPortTypeProvider(portTypeProvider);
		final IVesselProviderEditor vesselProvider = new HashMapVesselEditor("vkey"); 

		checker.setVesselProvider(vesselProvider);
		
		final Object o1 = new Object();
		final Object o2 = new Object();
		final Object o3 = new Object();
		final Object o4 = new Object();
		final Object o5 = new Object();
		final Object o6 = new Object();

		portTypeProvider.setPortType(o1, PortType.Start);
		portTypeProvider.setPortType(o2, PortType.Start);
		portTypeProvider.setPortType(o3, PortType.Load);
		portTypeProvider.setPortType(o4, PortType.Discharge);
		portTypeProvider.setPortType(o5, PortType.DryDock);
		portTypeProvider.setPortType(o6, PortType.End);

		final ISequence<Object> sequence = new ListSequence<Object>(
				CollectionsUtil.makeArrayList(o1, o2, o3, o4, o5, o6));

		final List<String> messages = new ArrayList<String>(1);
		Assert.assertFalse(checker.checkSequence(sequence, messages, VesselInstanceType.FLEET));

		Assert.assertEquals(1, messages.size());
	}

	/**
	 * Test multiple end fails
	 */
	@Test
	public void testCheckSequence13() {

		final PortTypeConstraintChecker<Object> checker = new PortTypeConstraintChecker<Object>(
				"checker", "key", "vkey");

		final IPortTypeProviderEditor<Object> portTypeProvider = new HashMapPortTypeEditor<Object>(
				"key");
		checker.setPortTypeProvider(portTypeProvider);
		final IVesselProviderEditor vesselProvider = new HashMapVesselEditor("vkey"); 

		checker.setVesselProvider(vesselProvider);
		
		final Object o1 = new Object();
		final Object o2 = new Object();
		final Object o3 = new Object();
		final Object o4 = new Object();
		final Object o5 = new Object();
		final Object o6 = new Object();

		portTypeProvider.setPortType(o1, PortType.Start);
		portTypeProvider.setPortType(o2, PortType.DryDock);
		portTypeProvider.setPortType(o3, PortType.Load);
		portTypeProvider.setPortType(o4, PortType.Discharge);
		portTypeProvider.setPortType(o5, PortType.End);
		portTypeProvider.setPortType(o6, PortType.End);

		final ISequence<Object> sequence = new ListSequence<Object>(
				CollectionsUtil.makeArrayList(o1, o2, o3, o4, o5, o6));

		final List<String> messages = new ArrayList<String>(1);
		Assert.assertFalse(checker.checkSequence(sequence, messages, VesselInstanceType.FLEET));

		Assert.assertEquals(1, messages.size());
	}
}
