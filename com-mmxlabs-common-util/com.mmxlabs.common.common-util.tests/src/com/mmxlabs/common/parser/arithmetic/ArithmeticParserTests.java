/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.parser.arithmetic;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.mmxlabs.common.parser.IExpression;

class ArithmeticParserTests {

	@Test
	void testParser() {

		final String[] args = new String[] { "a+b", "a", "1.0", "b", "2.0" };
		final double expected = 3.0;

		final ArithmeticParser parser = new ArithmeticParser();
		for (int i = 1; i < args.length; i += 2) {
			final String name = args[i];
			assert name != null;
			parser.addVariable(name, Double.parseDouble(args[i + 1]));
		}
		final IExpression<Double> expression = parser.parse(args[0]);
		Assertions.assertEquals(expected, expression.evaluate().doubleValue(), 0.0);
	}

}
