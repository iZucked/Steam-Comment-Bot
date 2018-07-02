/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.common.delayedactions.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.mmxlabs.common.ITransformer;

public class ReflectiveFieldSetterTest {

	@Test
	public void testReflectiveMethodSetter() throws SecurityException, NoSuchFieldException {

		final MockClass mock = new MockClass();
		@SuppressWarnings("unchecked")
		final ITransformer<String, String> transformer = (ITransformer<String, String>) Mockito.mock(ITransformer.class);

		final String source = "String";
		final ReflectiveFieldSetter<String> setter = new ReflectiveFieldSetter<>(mock, MockClass.class.getDeclaredField("field1"), transformer, source);

		Mockito.when(transformer.transform(source)).thenReturn(source);

		Assertions.assertNull(mock.field1);
		setter.run();

		Assertions.assertSame(source, mock.field1);
	}

	@Test
	public void testReflectiveMethodSetter2() throws SecurityException, NoSuchFieldException {

		final MockClass mock = new MockClass();
		@SuppressWarnings("unchecked")
		final ITransformer<String, String> transformer = (ITransformer<String, String>) Mockito.mock(ITransformer.class);

		final String source = "String";
		final ReflectiveFieldSetter<String> setter = new ReflectiveFieldSetter<>(mock, MockClass.class.getDeclaredField("field2"), transformer, source);

		Mockito.when(transformer.transform(source)).thenReturn(source);
		Assertions.assertThrows(RuntimeException.class, () -> setter.run());

	}

	class MockClass {

		public String field1;
		@SuppressWarnings("unused")
		private String field2;
	}
}
