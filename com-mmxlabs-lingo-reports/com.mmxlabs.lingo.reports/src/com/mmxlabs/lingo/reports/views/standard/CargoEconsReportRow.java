/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard;

import java.util.function.Supplier;

import org.eclipse.jface.viewers.IColorProvider;

import com.mmxlabs.models.ui.tabular.ICellRenderer;

public class CargoEconsReportRow {
	public int order;
	public String name;
	public String unit;
	public ICellRenderer formatter;
	public IColorProvider colourProvider;

	public Supplier<String> tooltip;
}