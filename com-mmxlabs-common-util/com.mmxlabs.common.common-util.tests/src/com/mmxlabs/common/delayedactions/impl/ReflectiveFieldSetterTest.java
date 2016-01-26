/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.delayedactions.impl;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.common.ITransformer;

public class ReflectiveFieldSetterTest {

	@Test
	public void testReflectiveMethodSetter() throws SecurityException, NoSuchFieldException {

		final MockClass mock = new MockClass();
		@SuppressWarnings("unchecked")
		final ITransformer<String, String> transformer = (ITransformer<String, String>) Mockito.mock(ITransformer.class);

		final String source = "String";
		final ReflectiveFieldSetter<String> setter = new ReflectiveFieldSetter<String>(mock, MockClass.class.getDeclaredField("field1"), transformer, source);

		Mockito.when(transformer.transform(source)).thenReturn(source);

		Assert.assertNull(mock.field1);
		setter.run();

		Assert.assertSame(source, mock.field1);
	}

	@Test(expected = RuntimeException.class)
	public void testReflectiveMethodSetter2() throws SecurityException, NoSuchFieldException {

		final MockClass mock = new MockClass();
		@SuppressWarnings("unchecked")
		final ITransformer<String, String> transformer = (ITransformer<String, String>) Mockito.mock(ITransformer.class);

		final String source = "String";
		final ReflectiveFieldSetter<String> setter = new ReflectiveFieldSetter<String>(mock, MockClass.class.getDeclaredField("field2"), transformer, source);
		
		Mockito.when(transformer.transform(source)).thenReturn(source);
		setter.run();

	}

	class MockClass {

		public String field1;
		@SuppressWarnings("unused")
		private String field2;
	}
}
