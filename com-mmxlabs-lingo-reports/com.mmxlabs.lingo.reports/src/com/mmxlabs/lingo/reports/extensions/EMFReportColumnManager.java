/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.extensions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.ETypedElement;

import com.mmxlabs.lingo.reports.components.ColumnBlockManager;
import com.mmxlabs.lingo.reports.components.ColumnType;
import com.mmxlabs.lingo.reports.components.EmfBlockColumnFactory;
import com.mmxlabs.lingo.reports.components.SimpleEmfBlockColumnFactory;
import com.mmxlabs.models.ui.tabular.ICellRenderer;

/**
 * A class to manage columns which are shared by reports of the same report type.
 * 
 * Global initialisation code calls the #registerColumn method to associate columns 
 * with particular report types. 
 * 
 * Report code calls the #addColumns method specifying a report type, which adds the
 * relevant columns to the report. A convenience method allows the report to control 
 * which columns appear in that report by default.
 * 
 * 
 *   
 * @author Simon McGregor
 *
 */
public class EMFReportColumnManager {
	private Map<String, List<EmfBlockColumnFactory>> columnsByType = new HashMap<>();
	
	public void registerColumn(final String reportType, final EmfBlockColumnFactory factory) {
		if (columnsByType.containsKey(reportType) == false) {
			columnsByType.put(reportType, new ArrayList<EmfBlockColumnFactory>());
		}
		columnsByType.get(reportType).add(factory);		
	}
	
	/**
	 * Adds all registered columns of the given report type to the specified report.
	 * These columns will default to "not visible".
	 * 
	 * @param report
	 * @param reportType
	 */
	public void addColumns(final String reportType, final ColumnBlockManager blockManager) {
		// code here needs to add all the requisite columns to the specified report
		// each column will be added to the appropriate managed block of columns on that report
		if (columnsByType.containsKey(reportType)) {
			for (EmfBlockColumnFactory factory: columnsByType.get(reportType)) {
				factory.addColumn(blockManager);
			}
		}
	}

	public void registerColumn(final String reportType, final String columnID, final String title, String tooltip, final ColumnType columnType, final ICellRenderer formatter, final ETypedElement... path) {
		registerColumn(reportType, new SimpleEmfBlockColumnFactory(columnID, title, tooltip, columnType, formatter, path));
	}
	

}
