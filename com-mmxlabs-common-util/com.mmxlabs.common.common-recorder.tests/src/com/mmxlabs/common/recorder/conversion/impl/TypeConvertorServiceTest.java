/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.recorder.conversion.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.common.recorder.conversion.ITypeConvertor;

public class TypeConvertorServiceTest {

	@Test
	public void testTypeConvertorService() {
		final TypeConvertorService service = new TypeConvertorService();

		final String input = "input";

		Assert.assertEquals(input, service.toObject(String.class.getCanonicalName(), input));
		Assert.assertEquals(input, service.toString(String.class.getCanonicalName(), input));

	}

	@Test
	public void testTypeConvertorServiceBoolean() {
		final TypeConvertorService service = new TypeConvertorService(true);

		final String input = "input";

		Assert.assertEquals(input, service.toObject(String.class.getCanonicalName(), input));
		Assert.assertEquals(input, service.toString(String.class.getCanonicalName(), input));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testTypeConvertorServiceBoolean2() {
		final TypeConvertorService service = new TypeConvertorService(false);

		final String input = "input";

		service.toObject(String.class.getCanonicalName(), input);
	}

	@Test
	public void testRegisterTypeConvertorITypeConvertorOfQ() {

		final TypeConvertorService service = new TypeConvertorService(false);

		final String className = Object.class.getCanonicalName();

		@SuppressWarnings("unchecked")
		final ITypeConvertor<Object> mockTypeConvertor = mock(ITypeConvertor.class);

		final String input = "input";
		final Object output = new Object();

		when(mockTypeConvertor.getDataType()).thenReturn(Object.class);
		when(mockTypeConvertor.toObject(input)).thenReturn(output);
		when(mockTypeConvertor.toString(output)).thenReturn(input);

		service.registerTypeConvertor(mockTypeConvertor);

		Assert.assertEquals(output, service.toObject(className, input));
		Assert.assertEquals(input, service.toString(className, output));
	}

	@Test
	public void testRegisterTypeConvertorStringITypeConvertorOfQ() {
		final TypeConvertorService service = new TypeConvertorService(false);

		final String className = Object.class.getCanonicalName();

		@SuppressWarnings("unchecked")
		final ITypeConvertor<Object> mockTypeConvertor = mock(ITypeConvertor.class);

		final String input = "input";
		final Object output = new Object();

		when(mockTypeConvertor.getDataType()).thenReturn(Object.class);
		when(mockTypeConvertor.toObject(input)).thenReturn(output);
		when(mockTypeConvertor.toString(output)).thenReturn(input);

		service.registerTypeConvertor(className, mockTypeConvertor);

		Assert.assertEquals(output, service.toObject(className, input));
		Assert.assertEquals(input, service.toString(className, output));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRegisterTypeConvertorStringITypeConvertorOfQ2() {
		final TypeConvertorService service = new TypeConvertorService(false);

		final String className = Object.class.getCanonicalName();
		final String className2 = "unknown";

		@SuppressWarnings("unchecked")
		final ITypeConvertor<Object> mockTypeConvertor = mock(ITypeConvertor.class);

		final String input = "input";
		final Object output = new Object();

		when(mockTypeConvertor.getDataType()).thenReturn(Object.class);
		when(mockTypeConvertor.toObject(input)).thenReturn(output);
		when(mockTypeConvertor.toString(output)).thenReturn(input);

		service.registerTypeConvertor(className2, mockTypeConvertor);

		service.toObject(className, input);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testUnregisterTypeConvertor() {
		final TypeConvertorService service = new TypeConvertorService(false);

		final String className = Object.class.getCanonicalName();

		@SuppressWarnings("unchecked")
		final ITypeConvertor<Object> mockTypeConvertor = mock(ITypeConvertor.class);

		final String input = "input";
		final Object output = new Object();

		when(mockTypeConvertor.getDataType()).thenReturn(Object.class);
		when(mockTypeConvertor.toObject(input)).thenReturn(output);
		when(mockTypeConvertor.toString(output)).thenReturn(input);

		service.registerTypeConvertor(className, mockTypeConvertor);

		Assert.assertEquals(output, service.toObject(className, input));
		Assert.assertEquals(input, service.toString(className, output));

		service.unregisterTypeConvertor(className);

		// This should trigger the error!
		service.toObject(className, input);
	}
}
