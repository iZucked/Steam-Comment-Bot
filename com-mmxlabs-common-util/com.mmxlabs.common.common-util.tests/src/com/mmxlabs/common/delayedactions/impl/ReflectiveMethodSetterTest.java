/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.delayedactions.impl;

import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.common.ITransformer;

public class ReflectiveMethodSetterTest {

	@Test
	public void testReflectiveMethodSetter() throws SecurityException, NoSuchMethodException {

		final MockClass mock = Mockito.mock(MockClass.class);
		@SuppressWarnings("unchecked")
		final ITransformer<String, String> transformer = (ITransformer<String, String>) Mockito.mock(ITransformer.class);

		final String source = "String";
		final ReflectiveMethodSetter<String> setter = new ReflectiveMethodSetter<String>(mock, MockClass.class.getDeclaredMethod("setField", String.class), transformer, source);

		Mockito.when(transformer.transform(source)).thenReturn(source);
		setter.run();
		Mockito.verify(mock).setField(source);
	}

	public static interface MockClass {

		public void setField(String field);
	}
}
