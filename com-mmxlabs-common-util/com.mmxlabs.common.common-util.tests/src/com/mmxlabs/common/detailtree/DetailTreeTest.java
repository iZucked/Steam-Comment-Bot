/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.detailtree;

import org.junit.Assert;
import org.junit.Test;

public class DetailTreeTest {

	@Test
	public void testConstructor() {

		final DetailTree tree = new DetailTree();
		Assert.assertEquals("", tree.getKey());
		Assert.assertNull(tree.getValue());
		Assert.assertNotNull(tree.getChildren());
		Assert.assertEquals(0, tree.getChildren().size());
	}

	@Test
	public void testConstructor2() {
		final String key = "key";
		final Object value = new Object();

		final DetailTree tree = new DetailTree(key, value);
		Assert.assertSame(key, tree.getKey());
		Assert.assertSame(value, tree.getValue());
		Assert.assertNotNull(tree.getChildren());
		Assert.assertEquals(0, tree.getChildren().size());
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

		Assert.assertSame(key1, tree.getKey());
		Assert.assertSame(value1, tree.getValue());
		Assert.assertNotNull(tree.getChildren());
		Assert.assertEquals(1, tree.getChildren().size());

		Assert.assertSame(child, tree.getChildren().get(0));

		Assert.assertSame(key2, child.getKey());
		Assert.assertSame(value2, child.getValue());

		Assert.assertNotNull(child.getChildren());
		Assert.assertEquals(0, child.getChildren().size());
	}

	@Test
	public void testAddChild2() {
		final String key1 = "key";
		final Object value1 = new Object();

		final DetailTree tree = new DetailTree(key1, value1);

		final String key2 = "key2";
		final Object value2 = new Object();

		tree.addChild(key2, value2);

		Assert.assertSame(key1, tree.getKey());
		Assert.assertSame(value1, tree.getValue());
		Assert.assertNotNull(tree.getChildren());
		Assert.assertEquals(1, tree.getChildren().size());

		final IDetailTree child = tree.getChildren().get(0);

		Assert.assertSame(key2, child.getKey());
		Assert.assertSame(value2, child.getValue());

		Assert.assertNotNull(child.getChildren());
		Assert.assertEquals(0, child.getChildren().size());
	}

	@Test
	public void testToString() {
		final String key1 = "key";
		final Object value1 = new Object();

		final DetailTree tree = new DetailTree(key1, value1);

		final String toString = tree.toString();

		Assert.assertNotNull(toString);
	}
}