package com.mmxlabs.common.curves;

import junit.framework.Assert;

import org.junit.Test;

public class UnitCurveTest {


	@Test
	public void testGetValueAtPoint() {
		
		final UnitCurve c = UnitCurve.getInstance();
		
		Assert.assertNotNull(c);
		
		for (int i = 0; i < 100; ++i) {
			Assert.assertEquals(1.0, c.getValueAtPoint(i));
		}
	}
}
