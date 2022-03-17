/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard.pnlcalcs;

import java.util.function.Supplier;

import org.eclipse.jface.viewers.IColorProvider;

import com.mmxlabs.models.ui.tabular.ICellRenderer;

public class PNLCalcsReportRow {
	public int order;
	public String name;
	public boolean includeUnits;
	public String prefixUnit;
	public String suffixUnit;
	public ICellRenderer formatter;
	public IColorProvider colourProvider;
	public boolean isCost;
	
	public Supplier<String> tooltip;
}