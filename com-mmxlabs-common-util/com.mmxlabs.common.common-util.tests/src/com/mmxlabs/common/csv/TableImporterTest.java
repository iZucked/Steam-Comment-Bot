/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.common.csv;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;

import com.mmxlabs.common.CollectionsUtil;

public class TableImporterTest {

	@Test
	public void testSimpleCase() throws IOException {
		final StringBuilder sb = new StringBuilder();

		sb.append("corner,col1,col2,col3\n");
		sb.append("row1, item11,   item12   ,item13\n");
		sb.append("row2, item21,item22,      item23  \n");

		final InputStream is = createInputStream(sb.toString());
		final TableImporter ti = new TableImporter(is);
		is.close();

		// Check input data
		final Iterable<String> columnKeys = ti.getColumnKeys();
		final Set<String> cKeys = CollectionsUtil.makeHashSet("col1", "col2", "col3");

		for (final String s : columnKeys) {
			Assert.assertTrue(cKeys.remove(s));
		}
		Assert.assertTrue(cKeys.isEmpty());

		final Iterable<String> rowKeys = ti.getRowKeys();
		final Set<String> rKeys = CollectionsUtil.makeHashSet("row1", "row2");

		for (final String s : rowKeys) {
			Assert.assertTrue(rKeys.remove(s));
		}
		Assert.assertTrue(rKeys.isEmpty());

		Assert.assertTrue(ti.contains("row1", "col1"));
		Assert.assertTrue(ti.contains("row1", "col2"));
		Assert.assertTrue(ti.contains("row1", "col3"));
		Assert.assertTrue(ti.contains("row2", "col1"));
		Assert.assertTrue(ti.contains("row2", "col2"));
		Assert.assertTrue(ti.contains("row2", "col3"));

		Assert.assertEquals("item11", ti.getCell("row1", "col1"));
		Assert.assertEquals("item12", ti.getCell("row1", "col2"));
		Assert.assertEquals("item13", ti.getCell("row1", "col3"));
		Assert.assertEquals("item21", ti.getCell("row2", "col1"));
		Assert.assertEquals("item22", ti.getCell("row2", "col2"));
		Assert.assertEquals("item23", ti.getCell("row2", "col3"));
	}

	@Test(expected = NullPointerException.class)
	public void testUnknownRowKey() throws IOException {
		final StringBuilder sb = new StringBuilder();

		sb.append("corner,col1,col2,col3\n");
		sb.append("row1, item11,item12,item13\n");
		sb.append("row2, item21,item22,item23\n");

		final InputStream is = createInputStream(sb.toString());
		final TableImporter ti = new TableImporter(is);
		is.close();

		Assert.assertFalse(ti.contains("unknown", "col1"));

		// FIXME: An unknown column returns something, but unknown row is a NPE
		ti.getCell("unknown", "col1");
	}

	@Test
	// (expected = NullPointerException.class)
	public void testUnknownColumnKey() throws IOException {
		final StringBuilder sb = new StringBuilder();

		sb.append("corner,col1,col2,col3\n");
		sb.append("row1, item11,item12,item13\n");
		sb.append("row2, item21,item22,item23\n");

		final InputStream is = createInputStream(sb.toString());
		final TableImporter ti = new TableImporter(is);
		is.close();

		Assert.assertFalse(ti.contains("row1", "unknown"));

		// FIXME: An unknown column returns something, but unknown row is a NPE
		Assert.assertNull(ti.getCell("row1", "unknown"));
	}

	@Test
	public void testDataHoles1() throws IOException {
		final StringBuilder sb = new StringBuilder();

		sb.append("corner,col1,col2,col3\n");
		sb.append("row1, item11,item12\n");
		sb.append("row2, item21,item22,item23\n");

		final InputStream is = createInputStream(sb.toString());
		final TableImporter ti = new TableImporter(is);
		is.close();

		// Check input data
		final Iterable<String> columnKeys = ti.getColumnKeys();
		final Set<String> cKeys = CollectionsUtil.makeHashSet("col1", "col2", "col3");

		for (final String s : columnKeys) {
			Assert.assertTrue(cKeys.remove(s));
		}
		Assert.assertTrue(cKeys.isEmpty());

		final Iterable<String> rowKeys = ti.getRowKeys();
		final Set<String> rKeys = CollectionsUtil.makeHashSet("row1", "row2");

		for (final String s : rowKeys) {
			Assert.assertTrue(rKeys.remove(s));
		}
		Assert.assertTrue(rKeys.isEmpty());

		Assert.assertTrue(ti.contains("row1", "col1"));
		Assert.assertTrue(ti.contains("row1", "col2"));
		Assert.assertFalse(ti.contains("row1", "col3"));
		Assert.assertTrue(ti.contains("row2", "col1"));
		Assert.assertTrue(ti.contains("row2", "col2"));
		Assert.assertTrue(ti.contains("row2", "col3"));

		Assert.assertEquals("item11", ti.getCell("row1", "col1"));
		Assert.assertEquals("item12", ti.getCell("row1", "col2"));
		Assert.assertEquals(null, ti.getCell("row1", "col3"));
		Assert.assertEquals("item21", ti.getCell("row2", "col1"));
		Assert.assertEquals("item22", ti.getCell("row2", "col2"));
		Assert.assertEquals("item23", ti.getCell("row2", "col3"));
	}

	@Test
	public void testDataHoles2() throws IOException {
		final StringBuilder sb = new StringBuilder();

		sb.append("corner,col1,col2,col3\n");
		sb.append("row1, item11,item12\n");
		sb.append("row2, ,item22,item23\n");

		final InputStream is = createInputStream(sb.toString());
		final TableImporter ti = new TableImporter(is);
		is.close();

		// Check input data
		final Iterable<String> columnKeys = ti.getColumnKeys();
		final Set<String> cKeys = CollectionsUtil.makeHashSet("col1", "col2", "col3");

		for (final String s : columnKeys) {
			Assert.assertTrue(cKeys.remove(s));
		}
		Assert.assertTrue(cKeys.isEmpty());

		final Iterable<String> rowKeys = ti.getRowKeys();
		final Set<String> rKeys = CollectionsUtil.makeHashSet("row1", "row2");

		for (final String s : rowKeys) {
			Assert.assertTrue(rKeys.remove(s));
		}
		Assert.assertTrue(rKeys.isEmpty());

		Assert.assertTrue(ti.contains("row1", "col1"));
		Assert.assertTrue(ti.contains("row1", "col2"));
		Assert.assertFalse(ti.contains("row1", "col3"));
		Assert.assertFalse(ti.contains("row2", "col1"));
		Assert.assertTrue(ti.contains("row2", "col2"));
		Assert.assertTrue(ti.contains("row2", "col3"));

		Assert.assertEquals("item11", ti.getCell("row1", "col1"));
		Assert.assertEquals("item12", ti.getCell("row1", "col2"));
		Assert.assertEquals(null, ti.getCell("row1", "col3"));
		Assert.assertEquals(null, ti.getCell("row2", "col1"));
		Assert.assertEquals("item22", ti.getCell("row2", "col2"));
		Assert.assertEquals("item23", ti.getCell("row2", "col3"));
	}

	@Test
	public void testDataHoles3() throws IOException {
		final StringBuilder sb = new StringBuilder();

		sb.append("corner,col1,col2,col3\n");
		sb.append("row1,,item12\n");
		sb.append("row2, ,item22,item23\n");

		final InputStream is = createInputStream(sb.toString());
		final TableImporter ti = new TableImporter(is);
		is.close();

		// Check input data
		final Iterable<String> columnKeys = ti.getColumnKeys();
		final Set<String> cKeys = CollectionsUtil.makeHashSet("col1", "col2", "col3");

		for (final String s : columnKeys) {
			Assert.assertTrue(cKeys.remove(s));
		}
		Assert.assertTrue(cKeys.isEmpty());

		final Iterable<String> rowKeys = ti.getRowKeys();
		final Set<String> rKeys = CollectionsUtil.makeHashSet("row1", "row2");

		for (final String s : rowKeys) {
			Assert.assertTrue(rKeys.remove(s));
		}
		Assert.assertTrue(rKeys.isEmpty());

		Assert.assertFalse(ti.contains("row1", "col1"));
		Assert.assertTrue(ti.contains("row1", "col2"));
		Assert.assertFalse(ti.contains("row1", "col3"));
		Assert.assertFalse(ti.contains("row2", "col1"));
		Assert.assertTrue(ti.contains("row2", "col2"));
		Assert.assertTrue(ti.contains("row2", "col3"));

		// FIXME: Inconsistency: Empty cell vs missing cell is empty string vs null
		Assert.assertEquals(null, ti.getCell("row1", "col1"));
		Assert.assertEquals("item12", ti.getCell("row1", "col2"));
		Assert.assertEquals(null, ti.getCell("row2", "col1"));
		Assert.assertEquals("item22", ti.getCell("row2", "col2"));
		Assert.assertEquals("item23", ti.getCell("row2", "col3"));
	}

	private InputStream createInputStream(final String data) {
		return new ByteArrayInputStream(data.getBytes());
	}
}
