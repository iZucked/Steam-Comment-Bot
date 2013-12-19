package com.mmxlabs.models.util.importer.impl;

import java.text.ParseException;

import org.junit.Assert;
import org.junit.Test;

public class NumberAttributeImporterTest {

	private static final char[] decimalSeparators = new char[] { ',', '.', ';', 'A' };

	@Test
	public void testReadWriteInteger() throws ParseException {

		final int[] testNumbers = new int[] { 1, 1000, 2000, -3000 };
		final String[] expectedPatterns = new String[] { "1", "1000", "2000", "-3000" };

		for (final char decimalSeparator : decimalSeparators) {
			final NumberAttributeImporter importer = new NumberAttributeImporter(decimalSeparator);

			for (int i = 0; i < testNumbers.length; ++i) {
				final String str = importer.intToString(testNumbers[i]);
				Assert.assertEquals(String.format(expectedPatterns[i], decimalSeparator), str);
				final int result = importer.stringToInt(str);
				Assert.assertEquals(testNumbers[i], result);
			}
		}
	}

	@Test
	public void testReadWriteFloat() throws ParseException {

		final float[] testNumbers = new float[] { 1.0f, 1000f, 1234.56789f, -3000.5f };
		final String[] expectedPatterns = new String[] { "1%s000", "1000%s000", "1234%s568", "-3000%s500" };
		final float[] expectedNumbers = new float[] { 1.0f, 1000f, 1234.568f, -3000.5f };

		for (final char decimalSeparator : decimalSeparators) {
			final NumberAttributeImporter importer = new NumberAttributeImporter(decimalSeparator);

			for (int i = 0; i < testNumbers.length; ++i) {
				final String str = importer.floatToString(testNumbers[i]);
				Assert.assertEquals(String.format(expectedPatterns[i], decimalSeparator), str);
				final float result = importer.stringToFloat(str);
				Assert.assertEquals(expectedNumbers[i], result, 0.0f);
			}
		}
	}

	@Test
	public void testReadWriteDouble() throws ParseException {

		final double[] testNumbers = new double[] { 1.0, 1000, 1234.56789, -3000.5 };
		final String[] expectedPatterns = new String[] { "1%s000", "1000%s000", "1234%s568", "-3000%s500" };
		final double[] expectedNumbers = new double[] { 1.0, 1000, 1234.568, -3000.5 };

		for (final char decimalSeparator : decimalSeparators) {
			final NumberAttributeImporter importer = new NumberAttributeImporter(decimalSeparator);

			for (int i = 0; i < testNumbers.length; ++i) {
				final String str = importer.doubleToString(testNumbers[i]);
				Assert.assertEquals(String.format(expectedPatterns[i], decimalSeparator), str);
				final double result = importer.stringToDouble(str);
				Assert.assertEquals(expectedNumbers[i], result, 0.0f);
			}
		}
	}

}
