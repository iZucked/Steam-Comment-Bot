/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CollectionsUtilTest {

	@Test
	public void makeArrayListTest() {

		// TODO: We should test the actual object instances rather than rely
		// upon equals as different instances could be equal.

		final List<Integer> list = CollectionsUtil.makeArrayList(5, 4, 3, 2, 1);

		Assertions.assertEquals(5, list.size());

		Assertions.assertEquals(Integer.valueOf(5), list.get(0));
		Assertions.assertEquals(Integer.valueOf(4), list.get(1));
		Assertions.assertEquals(Integer.valueOf(3), list.get(2));
		Assertions.assertEquals(Integer.valueOf(2), list.get(3));
		Assertions.assertEquals(Integer.valueOf(1), list.get(4));
	}

	@Test
	public void makeHashMapTest() {

		// TODO: We should test the actual object instances rather than rely
		// upon equals as different instances could be equal.

		final Map<String, String> map = CollectionsUtil.makeHashMap("key1", "value1", "key2", "value2");

		Assertions.assertEquals(2, map.size());

		Assertions.assertTrue(map.containsKey("key1"));
		Assertions.assertTrue(map.containsKey("key2"));

		Assertions.assertTrue(map.containsValue("value1"));
		Assertions.assertTrue(map.containsValue("value2"));

		Assertions.assertEquals("value1", map.get("key1"));
		Assertions.assertEquals("value2", map.get("key2"));
	}

	@Test
	public void makeHashSetTest() {

		final Set<String> set = CollectionsUtil.makeHashSet("value1", "value2", "value2");

		Assertions.assertEquals(2, set.size());

		Assertions.assertTrue(set.contains("value1"));
		Assertions.assertTrue(set.contains("value2"));

		Assertions.assertFalse(set.contains("value3"));
	}

	@Test
	public void longsToLongArrayTest() {

		final List<@NonNull Long> l = new ArrayList<>(4);
		l.add(1l);
		l.add(2l);
		l.add(3l);
		l.add(4l);

		final long[] longs = CollectionsUtil.longsToLongArray(l);

		Assertions.assertEquals(4, longs.length);

		Assertions.assertEquals(1, longs[0]);
		Assertions.assertEquals(2, longs[1]);
		Assertions.assertEquals(3, longs[2]);
		Assertions.assertEquals(4, longs[3]);
	}

	@Test
	public void integersToIntArrayTest() {

		final List<@NonNull Integer> l = new ArrayList<>(4);
		l.add(1);
		l.add(2);
		l.add(3);
		l.add(4);

		final int[] ints = CollectionsUtil.integersToIntArray(l);

		Assertions.assertEquals(4, ints.length);

		Assertions.assertEquals(1, ints[0]);
		Assertions.assertEquals(2, ints[1]);
		Assertions.assertEquals(3, ints[2]);
		Assertions.assertEquals(4, ints[3]);
	}

	@Test
	public void getValueTest() {

		final Map<String, Object> map = new HashMap<>();

		final String key1 = "key1";
		final String key2 = "key2";

		final Object object1 = new Object();
		final Object object2 = new Object();

		Assertions.assertSame(object2, CollectionsUtil.getValue(map, key1, object2));
		Assertions.assertSame(object2, CollectionsUtil.getValue(map, key2, object2));

		map.put(key1, object1);
		Assertions.assertTrue(map.containsValue(object1));
		Assertions.assertFalse(map.containsValue(object2));

		Assertions.assertSame(object1, CollectionsUtil.getValue(map, key1, object2));
		Assertions.assertSame(object2, CollectionsUtil.getValue(map, key2, object2));

		map.put(key2, object2);
		Assertions.assertTrue(map.containsValue(object1));
		Assertions.assertTrue(map.containsValue(object2));

		Assertions.assertSame(object1, CollectionsUtil.getValue(map, key1, object2));
		Assertions.assertSame(object2, CollectionsUtil.getValue(map, key2, object2));

	}

	@Test
	public void toArrayListShortTest() {

		final short[] arr = new short[] { 1, 2, 3 };
		final ArrayList<Short> arrayList = CollectionsUtil.toArrayList(arr);

		Assertions.assertEquals(arr.length, arrayList.size());

		for (int i = 0; i < arr.length; ++i) {
			Assertions.assertEquals(Short.valueOf(arr[i]), arrayList.get(i));
		}
	}

	@Test
	public void toArrayListIntegerTest() {

		final int[] arr = new int[] { 1, 2, 3 };
		final ArrayList<Integer> arrayList = CollectionsUtil.toArrayList(arr);

		Assertions.assertEquals(arr.length, arrayList.size());

		for (int i = 0; i < arr.length; ++i) {
			Assertions.assertEquals(Integer.valueOf(arr[i]), arrayList.get(i));
		}
	}

	@Test
	public void toArrayListLongTest() {

		final long[] arr = new long[] { 1, 2, 3 };
		final ArrayList<Long> arrayList = CollectionsUtil.toArrayList(arr);

		Assertions.assertEquals(arr.length, arrayList.size());

		for (int i = 0; i < arr.length; ++i) {
			Assertions.assertEquals(Long.valueOf(arr[i]), arrayList.get(i));
		}
	}
}
