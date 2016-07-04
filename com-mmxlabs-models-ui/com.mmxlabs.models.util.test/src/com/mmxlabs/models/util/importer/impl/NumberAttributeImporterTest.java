/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.util.importer.impl;

import java.text.ParseException;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EcoreFactory;
import org.junit.Assert;
import org.junit.Test;

public class NumberAttributeImporterTest {

	private static final char[] decimalSeparators = new char[] { ',', '.', ';', 'A' };

	@Test
	public void testReadWriteInteger() throws ParseException {

		final int[] testNumbers = new int[] { 1, 1000, 2000, -3000 };
		final String[] expectedPatterns = new String[] { "1", "1000", "2000", "-3000" };

		final EAttribute attribute = EcoreFactory.eINSTANCE.createEAttribute();

		for (final char decimalSeparator : decimalSeparators) {
			final NumberAttributeImporter importer = new NumberAttributeImporter(decimalSeparator);

			for (int i = 0; i < testNumbers.length; ++i) {
				final String str = importer.intToString(testNumbers[i], attribute);
				Assert.assertEquals(String.format(expectedPatterns[i], decimalSeparator), str);
				final int result = importer.stringToInt(str, attribute);
				Assert.assertEquals(testNumbers[i], result);
			}
		}
	}

	@Test
	public void testReadWriteFloat() throws ParseException {

		final float[] testNumbers = new float[] { 1.0f, 1000f, 1234.56789f, -3000.5f };
		final String[] expectedPatterns = new String[] { "1%s000", "1000%s000", "1234%s568", "-3000%s500" };
		final float[] expectedNumbers = new float[] { 1.0f, 1000f, 1234.568f, -3000.5f };

		final EAttribute attribute = EcoreFactory.eINSTANCE.createEAttribute();
		for (final char decimalSeparator : decimalSeparators) {
			final NumberAttributeImporter importer = new NumberAttributeImporter(decimalSeparator);

			for (int i = 0; i < testNumbers.length; ++i) {
				final String str = importer.floatToString(testNumbers[i], attribute);
				Assert.assertEquals(String.format(expectedPatterns[i], decimalSeparator), str);
				final float result = importer.stringToFloat(str, attribute);
				Assert.assertEquals(expectedNumbers[i], result, 0.0f);
			}
		}
	}

	@Test
	public void testReadWriteDouble() throws ParseException {

		final double[] testNumbers = new double[] { 1.0, 1000, 1234.56789, -3000.5 };
		final String[] expectedPatterns = new String[] { "1%s000", "1000%s000", "1234%s568", "-3000%s500" };
		final double[] expectedNumbers = new double[] { 1.0, 1000, 1234.568, -3000.5 };

		final EAttribute attribute = EcoreFactory.eINSTANCE.createEAttribute();
		for (final char decimalSeparator : decimalSeparators) {
			final NumberAttributeImporter importer = new NumberAttributeImporter(decimalSeparator);
			for (int i = 0; i < testNumbers.length; ++i) {
				final String str = importer.doubleToString(testNumbers[i], attribute);
				Assert.assertEquals(String.format(expectedPatterns[i], decimalSeparator), str);
				final double result = importer.stringToDouble(str, attribute);
				Assert.assertEquals(expectedNumbers[i], result, 0.0f);
			}
		}
	}
	@Test
	public void testReadWriteDoubleWithExportFormat() throws ParseException {

		final double[] testNumbers = new double[] { 1.0, 1000, 1234.56789, -3000.5 };
		final String[] expectedPatterns = new String[] { "1%s0", "1000%s0", "1234%s6", "-3000%s5" };
		final double[] expectedNumbers = new double[] { 1.0, 1000, 1234.6, -3000.5 };

		final EAttribute attribute = EcoreFactory.eINSTANCE.createEAttribute();
		{
			final EAnnotation annotation = EcoreFactory.eINSTANCE.createEAnnotation();
			annotation.setSource("http://www.mmxlabs.com/models/ui/numberFormat");
			annotation.getDetails().put("exportFormatString", "#.0");
			attribute.getEAnnotations().add(annotation);
		}

		for (final char decimalSeparator : decimalSeparators) {
			final NumberAttributeImporter importer = new NumberAttributeImporter(decimalSeparator);
			for (int i = 0; i < testNumbers.length; ++i) {
				final String str = importer.doubleToString(testNumbers[i], attribute);
				Assert.assertEquals(String.format(expectedPatterns[i], decimalSeparator), str);
				final double result = importer.stringToDouble(str, attribute);
				Assert.assertEquals(expectedNumbers[i], result, 0.0f);
			}
		}
	}

	@Test
	public void testReadWriteDoubleWithFormat() throws ParseException {

		final double[] testNumbers = new double[] { 1.0, 1000, 1234.56789, -3000.5 };
		final String[] expectedPatterns = new String[] { "1%s00000", "1000%s00000", "1234%s56789", "-3000%s50000" };
		final double[] expectedNumbers = new double[] { 1.0, 1000, 1234.56789, -3000.5 };

		final EAttribute attribute = EcoreFactory.eINSTANCE.createEAttribute();
		{
			final EAnnotation annotation = EcoreFactory.eINSTANCE.createEAnnotation();
			annotation.setSource("http://www.mmxlabs.com/models/ui/numberFormat");
			annotation.getDetails().put("formatString", "#.00000");
			attribute.getEAnnotations().add(annotation);
		}

		for (final char decimalSeparator : decimalSeparators) {
			final NumberAttributeImporter importer = new NumberAttributeImporter(decimalSeparator);
			for (int i = 0; i < testNumbers.length; ++i) {
				final String str = importer.doubleToString(testNumbers[i], attribute);
				Assert.assertEquals(String.format(expectedPatterns[i], decimalSeparator), str);
				final double result = importer.stringToDouble(str, attribute);
				Assert.assertEquals(expectedNumbers[i], result, 0.0f);
			}
		}
	}
}
