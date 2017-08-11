package com.mmxlabs.common.parser.series;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.mmxlabs.common.parser.IExpression;

@RunWith(value = Parameterized.class)
public class SeriesParserTest {
	@Parameters(name = "{0}")
	public static Iterable<Object[]> generateTests() {
		return Arrays.asList(new Object[][] { //
				{ "-1", -1.0 }, //
				{ "+1", 1.0 }, //
				{ "5-1", 4.0 }, //
				{ "(5)", 5.0 }, //
				{ "(5.0)", 5.0 }, //
				{ "(5.0)-(1.0)", 4.0 }, //
				{ "((5.0)-(1.0))", 4.0 }, //
				{ "(((5.0)-(1.0)))", 4.0 }, //
				{ "MAX(1,MAX(2,3))", 3.0 }, //
				{ "MIN(1,MAX(2,3))", 1.0 }, //
				{ "36.5*1.304/10-0.2", 4.5 }, // Operator precedence test
				{ "HH", 1.0 }, //
				{ "10%HH", 0.1 }, //
				{ "10.0%HH", 0.1 }, //
				{ "(HH)", 1.0 }, //
				{ "(10%HH)", 0.1 }, //
				// { "((10)%HH)", 0.1 }, // Illegal syntax, cannot have () before %
				{ "(10.0%HH)", 0.1 }, //
				{ "10%(HH)", 0.1 }, //
				{ "(((10.0)-(3.0))/(1.0%(HH)))", 700.0 }, //

		});
	}

	private String expression;
	private double expected;

	public SeriesParserTest(String expression, double expected) {
		this.expression = expression;
		this.expected = expected;

	}

	@Test
	public void run() {
		Assert.assertEquals(expected, parse(expression), 0.1);
	}

	double parse(String expression) {
		SeriesParser parser = new SeriesParser();
		parser.addSeriesExpression("HH", "1.0");

		IExpression<ISeries> parsed = parser.parse(expression);

		return parsed.evaluate().evaluate(0).doubleValue();
	}
}
