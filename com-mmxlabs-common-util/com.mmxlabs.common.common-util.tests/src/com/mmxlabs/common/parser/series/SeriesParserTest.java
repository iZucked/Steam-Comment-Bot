package com.mmxlabs.common.parser.series;

import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.common.parser.IExpression;

public class SeriesParserTest {

	@Test
	public void run() {
		Assert.assertEquals(4.5, parse("36.5*1.304/10-0.2"), 0.1);
	}

	double parse(String expression) {
		SeriesParser parser = new SeriesParser();

		IExpression<ISeries> parsed = parser.parse(expression);

		return parsed.evaluate().evaluate(0).doubleValue();
	}
}
