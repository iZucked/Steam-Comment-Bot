package com.mmxlabs.scheduler.optimiser.fitness.impl;

import org.junit.Assert;
import org.junit.Test;

public class SequenceSchedulerAdditionalInfoTest {
	private class Class1 {

	};

	private class Class2 {

	};

	@Test
	public void testGet() {

		final SequenceSchedulerAdditionalInfo info = new SequenceSchedulerAdditionalInfo();

		final String key = "key";

		final Class1 object1 = new Class1();

		info.put(key, object1);

		Assert.assertSame(object1, info.get(key, Class1.class));

		Assert.assertNull(info.get("unknown", Class1.class));
	}

	@Test(expected = ClassCastException.class)
	public void test2() {

		final SequenceSchedulerAdditionalInfo info = new SequenceSchedulerAdditionalInfo();

		final String key = "key";
		final Class1 object1 = new Class1();
		info.put(key, object1);
		info.get(key, Class2.class);
	}
}
