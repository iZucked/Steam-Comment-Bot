/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.common.delayedactions.impl;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.common.ITransformer;

@RunWith(JMock.class)
public class ReflectiveFieldSetterTest {

	Mockery context = new JUnit4Mockery();

	@Test
	public void testReflectiveMethodSetter() throws SecurityException, NoSuchFieldException {

		final MockClass mock = new MockClass();
		@SuppressWarnings("unchecked")
		final ITransformer<String, String> transformer = (ITransformer<String, String>) context.mock(ITransformer.class);

		final String source = "String";
		final ReflectiveFieldSetter<String> setter = new ReflectiveFieldSetter<String>(mock, MockClass.class.getDeclaredField("field1"), transformer, source);
		context.checking(new Expectations() {
			{
				one(transformer).transform(source);
				will(returnValue(source));
			}
		});

		Assert.assertNull(mock.field1);
		setter.run();

		Assert.assertSame(source, mock.field1);

		context.assertIsSatisfied();
	}

	@Test(expected = RuntimeException.class)
	public void testReflectiveMethodSetter2() throws SecurityException, NoSuchFieldException {

		final MockClass mock = new MockClass();
		@SuppressWarnings("unchecked")
		final ITransformer<String, String> transformer = (ITransformer<String, String>) context.mock(ITransformer.class);

		final String source = "String";
		final ReflectiveFieldSetter<String> setter = new ReflectiveFieldSetter<String>(mock, MockClass.class.getDeclaredField("field2"), transformer, source);
		context.checking(new Expectations() {
			{
				one(transformer).transform(source);
				will(returnValue(source));
			}
		});

		setter.run();

		context.assertIsSatisfied();
	}

	class MockClass {

		public String field1;
		@SuppressWarnings("unused")
		private String field2;
	}
}
