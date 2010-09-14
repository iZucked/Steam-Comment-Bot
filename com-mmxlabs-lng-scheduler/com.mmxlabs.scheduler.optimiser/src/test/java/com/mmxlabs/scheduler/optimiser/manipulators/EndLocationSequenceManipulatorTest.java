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
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.manipulators.EndLocationSequenceManipulator.EndLocationRule;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapPortEditor;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapPortTypeEditor;

@RunWith(JMock.class)
public class EndLocationSequenceManipulatorTest {

	Mockery context = new JUnit4Mockery();

	

	@Test
	public void testGetSetPortTypeProvider() {
		final EndLocationSequenceManipulator<Object> manipulator =
			new EndLocationSequenceManipulator<Object>();

		Assert.assertNull(manipulator.getPortTypeProvider());

		@SuppressWarnings("unchecked")
		final IPortTypeProvider<Object> portTypeProvider = context
				.mock(IPortTypeProvider.class);

		manipulator.setPortTypeProvider(portTypeProvider);

		Assert.assertSame(portTypeProvider, manipulator.getPortTypeProvider());
	}
	

	@Test
	public void testGetSetEndLocationRule() {

		final EndLocationSequenceManipulator<Object> manipulator = 
			new EndLocationSequenceManipulator<Object>();

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
