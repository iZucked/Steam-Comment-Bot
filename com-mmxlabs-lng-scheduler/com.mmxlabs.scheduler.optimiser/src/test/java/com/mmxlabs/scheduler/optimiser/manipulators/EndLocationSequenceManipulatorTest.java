package com.mmxlabs.scheduler.optimiser.manipulators;

import java.util.Map;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.impl.ListModifiableSequence;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.impl.Resource;
import com.mmxlabs.scheduler.optimiser.manipulators.EndLocationSequenceManipulator.EndLocationRule;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapPortTypeEditor;

@RunWith(JMock.class)
public class EndLocationSequenceManipulatorTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testEndLocationSequenceManipulator() {

		final Object virtualLocation = new Object();
		final EndLocationSequenceManipulator<Object> manipulator = new EndLocationSequenceManipulator<Object>(
				virtualLocation);
		Assert.assertSame(virtualLocation, manipulator.getVirtualLocation());
	}

	@Test
	public void testManipulateIModifiableSequencesOfT() {
		final Object virtualLocation = new Object();
		final EndLocationSequenceManipulator<Object> manipulator = new EndLocationSequenceManipulator<Object>(
				virtualLocation);

		final Object e1 = new Object();
		final Object e2 = new Object();
		final Object e3 = new Object();
		final Object e4 = new Object();
		final Object newEndLocation1 = new Object();
		final Object newEndLocation2 = new Object();

		manipulator.setElementMapping(e1, newEndLocation1);
		manipulator.setElementMapping(e3, newEndLocation2);

		final HashMapPortTypeEditor<Object> portTypeProvider = new HashMapPortTypeEditor<Object>(
				"name");
		portTypeProvider.setPortType(e1, PortType.Load);
		portTypeProvider.setPortType(e2, PortType.Discharge);
		portTypeProvider.setPortType(e3, PortType.Load);
		portTypeProvider.setPortType(e4, PortType.Discharge);

		portTypeProvider.setPortType(virtualLocation, PortType.End);

		manipulator.setPortTypeProvider(portTypeProvider);

		final IResource resource1 = new Resource();
		final IResource resource2 = new Resource();
		final IResource resource3 = new Resource();

		final IModifiableSequence<Object> seq1 = new ListModifiableSequence<Object>(
				CollectionsUtil.makeArrayList(e1, e2, e3, e4, virtualLocation));
		final IModifiableSequence<Object> seq2 = new ListModifiableSequence<Object>(
				CollectionsUtil.makeArrayList(e1, e2, e3, e4, virtualLocation));
		final IModifiableSequence<Object> seq3 = new ListModifiableSequence<Object>(
				CollectionsUtil.makeArrayList(e1, e2, e3, e4, virtualLocation));

		final Map<IResource, IModifiableSequence<Object>> sequenceMap = CollectionsUtil
				.makeHashMap(resource1, seq1, resource2, seq2, resource3, seq3);
		final IModifiableSequences<Object> sequences = new ModifiableSequences<Object>(
				CollectionsUtil.makeArrayList(resource1, resource2, resource3),
				sequenceMap);

		manipulator.setEndLocationRule(resource1, EndLocationRule.NONE);
		manipulator.setEndLocationRule(resource2,
				EndLocationRule.RETURN_TO_LAST_LOAD);
		manipulator.setEndLocationRule(resource3,
				EndLocationRule.RETURN_TO_FIRST_LOAD);

		manipulator.manipulate(sequences);

		Assert.assertEquals(5, seq1.size());
		Assert.assertSame(e1, seq1.get(0));
		Assert.assertSame(e2, seq1.get(1));
		Assert.assertSame(e3, seq1.get(2));
		Assert.assertSame(e4, seq1.get(3));
		Assert.assertSame(virtualLocation, seq1.get(4));

		Assert.assertEquals(5, seq2.size());
		Assert.assertSame(e1, seq2.get(0));
		Assert.assertSame(e2, seq2.get(1));
		Assert.assertSame(e3, seq2.get(2));
		Assert.assertSame(e4, seq2.get(3));
		Assert.assertSame(newEndLocation2, seq2.get(4));

		Assert.assertEquals(5, seq3.size());
		Assert.assertSame(e1, seq3.get(0));
		Assert.assertSame(e2, seq3.get(1));
		Assert.assertSame(e3, seq3.get(2));
		Assert.assertSame(e4, seq3.get(3));
		Assert.assertSame(newEndLocation1, seq3.get(4));
	}

	@Test
	public void testReturnToLastLoadPort() {

		final Object virtualLocation = new Object();
		final EndLocationSequenceManipulator<Object> manipulator = new EndLocationSequenceManipulator<Object>(
				virtualLocation);

		final Object e1 = new Object();
		final Object e2 = new Object();
		final Object e3 = new Object();
		final Object e4 = new Object();

		final Object newEndLocation1 = new Object();
		final Object newEndLocation2 = new Object();

		Assert.assertNull(manipulator.getElementMapping(e1));
		Assert.assertNull(manipulator.getElementMapping(e2));
		Assert.assertNull(manipulator.getElementMapping(e3));
		Assert.assertNull(manipulator.getElementMapping(e4));

		manipulator.setElementMapping(e1, newEndLocation1);
		manipulator.setElementMapping(e3, newEndLocation2);

		final HashMapPortTypeEditor<Object> portTypeProvider = new HashMapPortTypeEditor<Object>(
				"name");
		portTypeProvider.setPortType(e1, PortType.Load);
		portTypeProvider.setPortType(e2, PortType.Discharge);
		portTypeProvider.setPortType(e3, PortType.Load);
		portTypeProvider.setPortType(e4, PortType.Discharge);
		portTypeProvider.setPortType(virtualLocation, PortType.End);

		manipulator.setPortTypeProvider(portTypeProvider);

		final IResource resource = new Resource();

		final IModifiableSequence<Object> seq = new ListModifiableSequence<Object>(
				CollectionsUtil.makeArrayList(e1, e2, e3, e4, virtualLocation));

		manipulator.returnToLastLoadPort(resource, seq);

		Assert.assertEquals(5, seq.size());

		Assert.assertSame(e1, seq.get(0));
		Assert.assertSame(e2, seq.get(1));
		Assert.assertSame(e3, seq.get(2));
		Assert.assertSame(e4, seq.get(3));
		Assert.assertSame(newEndLocation2, seq.get(4));
	}

	@Test
	public void testReturnToFirstLoadPort() {

		final Object virtualLocation = new Object();
		final EndLocationSequenceManipulator<Object> manipulator = new EndLocationSequenceManipulator<Object>(
				virtualLocation);

		final Object e1 = new Object();
		final Object e2 = new Object();
		final Object e3 = new Object();
		final Object e4 = new Object();

		final Object newEndLocation1 = new Object();
		final Object newEndLocation2 = new Object();

		Assert.assertNull(manipulator.getElementMapping(e1));
		Assert.assertNull(manipulator.getElementMapping(e2));
		Assert.assertNull(manipulator.getElementMapping(e3));
		Assert.assertNull(manipulator.getElementMapping(e4));

		manipulator.setElementMapping(e1, newEndLocation1);
		manipulator.setElementMapping(e3, newEndLocation2);

		final HashMapPortTypeEditor<Object> portTypeProvider = new HashMapPortTypeEditor<Object>(
				"name");
		portTypeProvider.setPortType(e1, PortType.Load);
		portTypeProvider.setPortType(e2, PortType.Discharge);
		portTypeProvider.setPortType(e3, PortType.Load);
		portTypeProvider.setPortType(e4, PortType.Discharge);
		portTypeProvider.setPortType(virtualLocation, PortType.End);

		manipulator.setPortTypeProvider(portTypeProvider);

		final IResource resource = new Resource();

		final IModifiableSequence<Object> seq = new ListModifiableSequence<Object>(
				CollectionsUtil.makeArrayList(e1, e2, e3, e4, virtualLocation));

		manipulator.returnToFirstLoadPort(resource, seq);

		Assert.assertEquals(5, seq.size());

		Assert.assertSame(e1, seq.get(0));
		Assert.assertSame(e2, seq.get(1));
		Assert.assertSame(e3, seq.get(2));
		Assert.assertSame(e4, seq.get(3));
		Assert.assertSame(newEndLocation1, seq.get(4));
	}

	@Test
	public void testGetSetPortTypeProvider() {

		final Object virtualLocation = new Object();
		final EndLocationSequenceManipulator<Object> manipulator = new EndLocationSequenceManipulator<Object>(
				virtualLocation);

		Assert.assertNull(manipulator.getPortTypeProvider());

		@SuppressWarnings("unchecked")
		final IPortTypeProvider<Object> portTypeProvider = context
				.mock(IPortTypeProvider.class);

		manipulator.setPortTypeProvider(portTypeProvider);

		Assert.assertSame(portTypeProvider, manipulator.getPortTypeProvider());
	}

	@Test
	public void testGetSetElementMapping() {

		final Object virtualLocation = new Object();
		final EndLocationSequenceManipulator<Object> manipulator = new EndLocationSequenceManipulator<Object>(
				virtualLocation);

		final Object e1 = new Object();
		final Object e2 = new Object();
		final Object e3 = new Object();

		Assert.assertNull(manipulator.getElementMapping(e1));
		Assert.assertNull(manipulator.getElementMapping(e2));
		Assert.assertNull(manipulator.getElementMapping(e3));

		manipulator.setElementMapping(e1, e2);

		Assert.assertSame(e2, manipulator.getElementMapping(e1));
		Assert.assertNull(manipulator.getElementMapping(e2));
		Assert.assertNull(manipulator.getElementMapping(e3));

		manipulator.dispose();

		Assert.assertNull(manipulator.getElementMapping(e1));
		Assert.assertNull(manipulator.getElementMapping(e2));
		Assert.assertNull(manipulator.getElementMapping(e3));

	}

	@Test
	public void testGetSetEndLocationRule() {

		final Object virtualLocation = new Object();
		final EndLocationSequenceManipulator<Object> manipulator = new EndLocationSequenceManipulator<Object>(
				virtualLocation);

		final IResource resource1 = new Resource();
		final IResource resource2 = new Resource();
		final IResource resource3 = new Resource();

		Assert.assertSame(EndLocationRule.NONE,
				manipulator.getEndLocationRule(resource1));
		Assert.assertSame(EndLocationRule.NONE,
				manipulator.getEndLocationRule(resource2));
		Assert.assertSame(EndLocationRule.NONE,
				manipulator.getEndLocationRule(resource3));

		manipulator.setEndLocationRule(resource1, EndLocationRule.NONE);
		manipulator.setEndLocationRule(resource2,
				EndLocationRule.RETURN_TO_FIRST_LOAD);
		manipulator.setEndLocationRule(resource3,
				EndLocationRule.RETURN_TO_LAST_LOAD);

		Assert.assertSame(EndLocationRule.NONE,
				manipulator.getEndLocationRule(resource1));
		Assert.assertSame(EndLocationRule.RETURN_TO_FIRST_LOAD,
				manipulator.getEndLocationRule(resource2));
		Assert.assertSame(EndLocationRule.RETURN_TO_LAST_LOAD,
				manipulator.getEndLocationRule(resource3));

		manipulator.dispose();

		Assert.assertSame(EndLocationRule.NONE,
				manipulator.getEndLocationRule(resource1));
		Assert.assertSame(EndLocationRule.NONE,
				manipulator.getEndLocationRule(resource2));
		Assert.assertSame(EndLocationRule.NONE,
				manipulator.getEndLocationRule(resource3));
	}
}
