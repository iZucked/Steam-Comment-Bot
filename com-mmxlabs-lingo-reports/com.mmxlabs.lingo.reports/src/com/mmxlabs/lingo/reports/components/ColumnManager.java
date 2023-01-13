/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.components;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

import com.mmxlabs.lingo.reports.views.standard.SimpleTabularReportView;

/**
 * Class used to represent a data provider for a column in the
 * {@link SimpleTabularReportView}
 *
 * @param <T>
 */
public abstract class ColumnManager<T> {

	private final String name;

	protected ColumnManager(final String name) {
		this.name = name;
	}

	public String getColumnText(final T obj) {
		return "";
	}

	public String getName() {
		return name;
	}

	public @Nullable Font getFont(final T element) {
		return null;
	}

	public @Nullable Image getColumnImage(final T obj) {
		return null;
	}

	public @Nullable Color getBackground(final T element) {
		return null;
	}

	public @Nullable Color getForeground(final T element) {
		return null;
	}

	public int compare(final T obj1, final T obj2) {
		return 0;
	}

	public boolean isTree() {
		return false;
	}

	public @Nullable String getTooltip() {
		return null;
	}
}