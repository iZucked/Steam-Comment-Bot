/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.common.detailtree;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DetailTreeTest {

	@Test
	public void testConstructor() {

		final DetailTree tree = new DetailTree();
		Assertions.assertEquals("", tree.getKey());
		Assertions.assertNull(tree.getValue());
		Assertions.assertNotNull(tree.getChildren());
		Assertions.assertEquals(0, tree.getChildren().size());
	}

	@Test
	public void testConstructor2() {
		final String key = "key";
		final Object value = new Object();

		final DetailTree tree = new DetailTree(key, value);
		Assertions.assertSame(key, tree.getKey());
		Assertions.assertSame(value, tree.getValue());
		Assertions.assertNotNull(tree.getChildren());
		Assertions.assertEquals(0, tree.getChildren().size());
	}

	@Test
	public void testAddChild1() {
		final String key1 = "key";
		final Object value1 = new Object();

		final DetailTree tree = new DetailTree(key1, value1);

		final String key2 = "key2";
		final Object value2 = new Object();

		final DetailTree child = new DetailTree(key2, value2);

		tree.addChild(child);

		Assertions.assertSame(key1, tree.getKey());
		Assertions.assertSame(value1, tree.getValue());
		Assertions.assertNotNull(tree.getChildren());
		Assertions.assertEquals(1, tree.getChildren().size());

		Assertions.assertSame(child, tree.getChildren().get(0));

		Assertions.assertSame(key2, child.getKey());
		Assertions.assertSame(value2, child.getValue());

		Assertions.assertNotNull(child.getChildren());
		Assertions.assertEquals(0, child.getChildren().size());
	}

	@Test
	public void testAddChild2() {
		final String key1 = "key";
		final Object value1 = new Object();

		final DetailTree tree = new DetailTree(key1, value1);

		final String key2 = "key2";
		final Object value2 = new Object();

		tree.addChild(key2, value2);

		Assertions.assertSame(key1, tree.getKey());
		Assertions.assertSame(value1, tree.getValue());
		Assertions.assertNotNull(tree.getChildren());
		Assertions.assertEquals(1, tree.getChildren().size());

		final IDetailTree child = tree.getChildren().get(0);

		Assertions.assertSame(key2, child.getKey());
		Assertions.assertSame(value2, child.getValue());

		Assertions.assertNotNull(child.getChildren());
		Assertions.assertEquals(0, child.getChildren().size());
	}

	@Test
	public void testToString() {
		final String key1 = "key";
		final Object value1 = new Object();

		final DetailTree tree = new DetailTree(key1, value1);

		final String toString = tree.toString();

		Assertions.assertNotNull(toString);
	}
}