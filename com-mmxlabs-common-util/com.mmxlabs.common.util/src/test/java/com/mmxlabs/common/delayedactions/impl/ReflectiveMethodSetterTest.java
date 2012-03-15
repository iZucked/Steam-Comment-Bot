/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.common.delayedactions.impl;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.common.ITransformer;

@RunWith(JMock.class)
public class ReflectiveMethodSetterTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testReflectiveMethodSetter() throws SecurityException, NoSuchMethodException {

		final MockClass mock = context.mock(MockClass.class);
		@SuppressWarnings("unchecked")
		final ITransformer<String, String> transformer = (ITransformer<String, String>) context.mock(ITransformer.class);

		final String source = "String";
		final ReflectiveMethodSetter<String> setter = new ReflectiveMethodSetter<String>(mock, MockClass.class.getDeclaredMethod("setField", String.class), transformer, source);
		context.checking(new Expectations() {
			{
				one(transformer).transform(source);
				will(returnValue(source));

				one(mock).setField(source);
			}
		});

		setter.run();

		context.assertIsSatisfied();
	}

	interface MockClass {

		public void setField(String field);
	}
}
