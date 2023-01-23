/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.validation;

import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils;

public class ValidPriceExpressionTest {

	// .lingo files can only be used once otherwise there will be conflicting output.
	// TODO: Add qualifier to args?
	public static Iterable<Object[]> generateTests() {
		return Arrays.asList(new Object[][] { //
				{ "5", true }, //
				{ "5+1", true }, //
				{ "5+AA", true }, //
				{ "5%AA", true }, //
				{ "5*(AA+6)", true }, //

				// Test SHIFT FUNCTION
				{ "SHIFT(AA,1)", true }, //
				{ "SHIFT(AA_bb_56k,1)", true }, // Longer name with underscores etc
				{ "ShiFT(AA,2)", true }, // Case insensitive
				{ "shift(aa,30)", true }, // Case insensitive
				{ " SHIFT ( AA ,   1 )", true }, // White space
				{ "55SHIFT ( AA ,   1 )99", true /* valid based on pattern matcher, invalid syntax however */ }, //
				{ "SHIFT(AA,-1)", true }, // Allow negative
				{ "SHIFT(AA,1.5)", false }, // Disallow floating point
				{ "SHIFT(AA,0.5)", false }, // Disallow floating point
				{ "SHIFT(AA,.5)", false }, // Disallow floating point
				{ "SHIFT(AA,.)", false }, // Disallow floating point
				{ "SHIFT(1,1)", false }, // Disallow number in LHS
				{ "SHIFT(AA,AA)", false }, // Disallow expression in RHS

				{ "SHIFT(NG_NYMEX,1)", true }, // Typical client use
		});
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("generateTests")
	public void testExpression(final String priceExpression, boolean expectValid) {
		Assertions.assertEquals(expectValid, PriceExpressionUtils.validateBasicSyntax(priceExpression));
	}

}
