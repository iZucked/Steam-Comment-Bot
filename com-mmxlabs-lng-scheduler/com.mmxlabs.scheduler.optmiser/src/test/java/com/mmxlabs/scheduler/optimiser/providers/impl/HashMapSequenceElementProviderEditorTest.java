package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.List;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.scheduler.optimiser.components.ICargo;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.ISequenceElement;

@RunWith(JMock.class)
public class HashMapSequenceElementProviderEditorTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testHashMapSequenceElementProviderEditor() {
		final String name = "name";
		final HashMapSequenceElementProviderEditor editor = new HashMapSequenceElementProviderEditor(
				name);

		Assert.assertSame(name, editor.getName());
	}

	@Test
	public void testGetSetSequenceElement() {
		final String name = "name";
		final HashMapSequenceElementProviderEditor editor = new HashMapSequenceElementProviderEditor(
				name);

		final ICargo cargo = context.mock(ICargo.class);
		final IPort port1 = context.mock(IPort.class, "port-1");
		final IPort port2 = context.mock(IPort.class, "port-2");

		final ISequenceElement element1 = context.mock(ISequenceElement.class,
				"element-1");
		final ISequenceElement element2 = context.mock(ISequenceElement.class,
				"element-2");

		Assert.assertNull(editor.getSequenceElement(cargo, port1));
		editor.setSequenceElement(cargo, port1, element1);
		Assert.assertSame(element1, editor.getSequenceElement(cargo, port1));

		Assert.assertNull(editor.getSequenceElement(cargo, port2));
		editor.setSequenceElement(cargo, port2, element2);
		Assert.assertSame(element2, editor.getSequenceElement(cargo, port2));
	}

	@Test(expected = RuntimeException.class)
	public void testGetSetSequenceElement2() {
		final String name = "name";
		final HashMapSequenceElementProviderEditor editor = new HashMapSequenceElementProviderEditor(
				name);

		final ICargo cargo = context.mock(ICargo.class);
		final IPort port = context.mock(IPort.class);

		final ISequenceElement element = context.mock(ISequenceElement.class);

		Assert.assertNull(editor.getSequenceElement(cargo, port));

		editor.setSequenceElement(cargo, port, element);

		Assert.assertSame(element, editor.getSequenceElement(cargo, port));

		// Already added
		editor.setSequenceElement(cargo, port, element);
	}

	@Test
	public void testGetSequenceElements() {
		final String name = "name";
		final HashMapSequenceElementProviderEditor editor = new HashMapSequenceElementProviderEditor(
				name);

		final ICargo cargo = context.mock(ICargo.class);
		final IPort port1 = context.mock(IPort.class, "port-1");
		final IPort port2 = context.mock(IPort.class, "port-2");

		final ISequenceElement element1 = context.mock(ISequenceElement.class,
				"element-1");
		final ISequenceElement element2 = context.mock(ISequenceElement.class,
				"element-2");

		editor.setSequenceElement(cargo, port1, element1);
		editor.setSequenceElement(cargo, port2, element2);

		final List<ISequenceElement> elements = editor.getSequenceElements();
		Assert.assertEquals(2, elements.size());
		Assert.assertSame(element1, elements.get(0));
		Assert.assertSame(element2, elements.get(1));
	}

	@Test
	public void testDispose() {
		final String name = "name";
		final HashMapSequenceElementProviderEditor editor = new HashMapSequenceElementProviderEditor(
				name);

		final ICargo cargo = context.mock(ICargo.class);
		final IPort port = context.mock(IPort.class);

		final ISequenceElement element = context.mock(ISequenceElement.class);

		Assert.assertNull(editor.getSequenceElement(cargo, port));

		editor.setSequenceElement(cargo, port, element);

		Assert.assertSame(element, editor.getSequenceElement(cargo, port));

		editor.dispose();

		Assert.assertNull(editor.getSequenceElement(cargo, port));

	}
}
