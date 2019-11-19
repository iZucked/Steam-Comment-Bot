package com.mmxlabs.scheduler.optimiser.contracts.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.mmxlabs.common.curves.ConstantValueLongCurve;
import com.mmxlabs.common.curves.StepwiseLongCurve;

public class WeightedAverageCharterCostCalculatorTest {

	@Test
	public void testConstantValueCharterRate() {
		ConstantValueLongCurve charterRate = new ConstantValueLongCurve(9);
		WeightAverageCharterCostCalculator waccc = new WeightAverageCharterCostCalculator(charterRate);
		
		assertEquals(9*21, waccc.getCharterCost(0, 0, 0, 21*24));
	}
	
	@Test
	public void testMonthlyChangeCharterRate() {
		StepwiseLongCurve monthlyCharterRate = new StepwiseLongCurve();
		monthlyCharterRate.setValueAfter(0, 10000);
		monthlyCharterRate.setValueAfter(31*24, 20000);
		monthlyCharterRate.setValueAfter(31*2*24, 30000);
		WeightAverageCharterCostCalculator waccc = new WeightAverageCharterCostCalculator(monthlyCharterRate);			
		assertEquals(15000 * 31 * 2, waccc.getCharterCost(0, 0, 0, (31*2*24)-1));
	}
	
	@Test
	public void testMonthlyChangeCharterRateBelowLowerRate() {
		StepwiseLongCurve monthlyCharterRate = new StepwiseLongCurve();
		monthlyCharterRate.setDefaultValue(10000);
		monthlyCharterRate.setValueAfter(31*24, 20000);
		monthlyCharterRate.setValueAfter(31*2*24, 30000);
		WeightAverageCharterCostCalculator waccc = new WeightAverageCharterCostCalculator(monthlyCharterRate);
		assertEquals(15000 * 31 * 2, waccc.getCharterCost(0, 0, 0, (31*2*24)-1));
	}
/*
 * NB: StepwiseLongCurve does not recognise having a max bound, only a min bound time value...
	@Test
	public void testMonthlyChangeCharterRateAboveUpperRate() {
		StepwiseLongCurve monthlyCharterRate = new StepwiseLongCurve();
		monthlyCharterRate.setDefaultValue(20000);
		monthlyCharterRate.setValueAfter(0, 10000);
		monthlyCharterRate.setValueAfter(31*24, 20000);

		WeightAverageCharterCostCalculator waccc = new WeightAverageCharterCostCalculator(monthlyCharterRate);
		assertEquals(15000 * 31 * 2, waccc.getCharterCost(0, 0, 0, (31*2*24)-1));
	}
	*/	
}
