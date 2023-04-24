/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.schedule;

import java.util.UUID;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.swt.widgets.Composite;

import com.google.inject.Inject;
import com.mmxlabs.lingo.reports.customizable.CustomReportDefinition;
import com.mmxlabs.lingo.reports.utils.ColumnConfigurationDialog.OptionInfo;
import com.mmxlabs.models.ui.tabular.columngeneration.ColumnBlock;
import com.mmxlabs.models.ui.tabular.columngeneration.ColumnBlockManager;

public class ScheduleSummaryReport extends AbstractConfigurableScheduleReportView {
	
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.shiplingo.platform.reports.views.SchedulePnLReport";
	
	public static final String UUID_PREFIX = "CustomReport_";
	
	private String uuid = null;
	
	@Inject
	public ScheduleSummaryReport(final ScheduleBasedReportBuilder builder) {
		super("com.mmxlabs.lingo.doc.Reports_ScheduleSummary", builder);
	}
	
	@Override
	public void initPartControl(final Composite parent) {
		super.initPartControl(parent);
		
		//Normally have ID at the top of this class.
		String id = this.getViewSite().getId();
		
		//Normally null, unless dynamically created.
		String id2 = this.getViewSite().getSecondaryId();
				
		IConfigurationElement config = this.getConfigurationElement();
		if (config != null) {
			String configId = config.getAttribute("id");
			if (configId != null && !configId.equals(ID)) {
				uuid = id;
			}
		}
		/*
		final IActionBars bars = getViewSite().getActionBars();
		final IToolBarManager toolbar = bars.getToolBarManager();
	   
		CommonImages.setImageDescriptors(refreshButton, IconPaths.ReEvaluate16);

		toolbar.appendToGroup("edit", saveToJSONButton);
		toolbar.update(true);	
		*/	
	}

	public CustomReportDefinition getReportDefinition() {
		CustomReportDefinition jsonObject = new CustomReportDefinition();
		if (this.uuid == null) {
			uuid = UUID_PREFIX+UUID.randomUUID().toString();
		}
		System.out.println("UUID = "+uuid);
		jsonObject.setUuid(this.uuid);
				
		String name = this.getTitle();
		System.out.println("Name = "+name);
		jsonObject.setName(name);
		
		String type = this.getClass().getSimpleName();
		System.out.println("Type = "+type);
		jsonObject.setType(type);
		
		System.out.println("Filters = ");
		for (final String option : builder.getRowFilterInfo()) {
			OptionInfo oi = builder.getRowFilterOptionInfo(option);
			if (oi != null) {
				String rowType = builder.getRowFilterOptionInfo(option).type;
				System.out.print(rowType + " ");
				jsonObject.getFilters().add(rowType);
			}
			else {
				System.err.println("Could not find row filter option: "+option);
			}
		}
	
		ColumnBlockManager bm = this.getBlockManager();
		for (final String blockID : bm.getBlockIDOrder()) {
			final ColumnBlock block = bm.getBlockByID(blockID);
			boolean isVisible = bm.getBlockVisible(block);
			if (isVisible) { //We are only interested in visible columns.
				System.out.println("ColumnBlock = "+blockID);
				jsonObject.getColumns().add(blockID);
			}
		}
		
		for (final String option : builder.getDiffFilterInfo()) {
			OptionInfo oi = builder.getDiffFilterOptionInfo(option);
			if (oi != null) {
				String diffFilter = builder.getDiffFilterOptionInfo(option).type;
				System.out.println("DiffFilter "+diffFilter);
				jsonObject.getDiffOptions().add(diffFilter);
			}
			else {
				System.err.println("Could not find diff filter option: "+option);
			}	
		}	
		
		return jsonObject;
	}
	
}
