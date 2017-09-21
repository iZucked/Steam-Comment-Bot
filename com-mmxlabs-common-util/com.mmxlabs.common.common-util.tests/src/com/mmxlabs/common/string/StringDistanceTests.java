package com.mmxlabs.common.string;

import static org.junit.Assert.*;

import org.junit.Test;

import com.mmxlabs.common.strings.StringDistance;

public class StringDistanceTests {
	
	@Test
	public void testStringDistance() {
		assertEquals(3, StringDistance.defaultInstance().distance("relevant", "elephant"));
		assertEquals(2, StringDistance.defaultInstance().distance("a cat", "a abct"));
	}

}
