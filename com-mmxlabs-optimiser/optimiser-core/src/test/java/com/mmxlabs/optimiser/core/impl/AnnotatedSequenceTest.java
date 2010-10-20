/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.optimiser.core.impl;

import org.junit.Assert;
import org.junit.Test;

public class AnnotatedSequenceTest {

	private static final class TestClass {

	}

	private static final class TestClass2 {

	}

	@Test
	public void testGetSetAnnotation() {

		final Object element = new Object();
		final String key = "key";
		final TestClass annotation = new TestClass();

		final AnnotatedSequence<Object> sequence = new AnnotatedSequence<Object>();
		TestClass obj = sequence.getAnnotation(element, key, TestClass.class);

		Assert.assertNull(obj);

		sequence.setAnnotation(element, key, annotation);

		obj = sequence.getAnnotation(element, key, TestClass.class);

		Assert.assertSame(annotation, obj);
	}

	@Test(expected = ClassCastException.class)
	public void testGetSetAnnotation2() {

		final Object element = new Object();
		final String key = "key";
		final TestClass annotation = new TestClass();

		final AnnotatedSequence<Object> sequence = new AnnotatedSequence<Object>();
		final TestClass obj = sequence.getAnnotation(element, key, TestClass.class);

		Assert.assertNull(obj);

		sequence.setAnnotation(element, key, annotation);

		sequence.getAnnotation(element, key, TestClass2.class);
	}

	@Test
	public void testDispose() {
		final Object element = new Object();
		final String key = "key";
		final TestClass annotation = new TestClass();

		final AnnotatedSequence<Object> sequence = new AnnotatedSequence<Object>();
		TestClass obj = sequence.getAnnotation(element, key, TestClass.class);

		Assert.assertNull(obj);

		sequence.setAnnotation(element, key, annotation);

		obj = sequence.getAnnotation(element, key, TestClass.class);

		Assert.assertSame(annotation, obj);

		sequence.dispose();

		obj = sequence.getAnnotation(element, key, TestClass.class);

		Assert.assertNull(obj);
	}

	@Test
	public void testHasAnnotation() {

		final Object element = new Object();
		final String key = "key";
		final TestClass annotation = new TestClass();

		final AnnotatedSequence<Object> sequence = new AnnotatedSequence<Object>();

		Assert.assertFalse(sequence.hasAnnotation(element, key));

		sequence.setAnnotation(element, key, annotation);

		Assert.assertTrue(sequence.hasAnnotation(element, key));
	}

}
