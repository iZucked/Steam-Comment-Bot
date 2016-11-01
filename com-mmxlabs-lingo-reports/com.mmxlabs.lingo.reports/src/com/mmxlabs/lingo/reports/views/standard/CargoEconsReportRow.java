package com.mmxlabs.lingo.reports.views.standard;

import org.eclipse.jface.viewers.IColorProvider;

import com.mmxlabs.models.ui.tabular.ICellRenderer;

public class CargoEconsReportRow {
	public int order; 
	public String name;
	public String unit;
	public ICellRenderer formatter;
	public IColorProvider colourProvider;
}