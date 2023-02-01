/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.schedule;

import java.util.UUID;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.actions.ActionFactory;

import com.google.inject.Inject;
import com.mmxlabs.lingo.reports.customizable.CustomReportDefinition;
import com.mmxlabs.lingo.reports.utils.ColumnConfigurationDialog.OptionInfo;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.models.ui.tabular.columngeneration.ColumnBlock;
import com.mmxlabs.models.ui.tabular.columngeneration.ColumnBlockManager;
import com.mmxlabs.rcp.common.actions.CopyGridToHtmlClipboardAction;
import com.mmxlabs.rcp.common.actions.IAdditionalAttributeProvider;
import com.mmxlabs.rcp.common.actions.PackActionFactory;

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
	
	@Override
	protected void makeActions() {
		super.makeActions();
		copyTableAction = new CopyGridToHtmlClipboardAction(viewer.getGrid(), true, () -> setCopyPasteMode(true), () -> setCopyPasteMode(false));
		copyTableAction.setAdditionalAttributeProvider(new IAdditionalAttributeProvider() {
			
			@Override
			public @NonNull String getTopLeftCellUpperText() {
				return "";
			}
			
			@Override
			public @NonNull String getTopLeftCellText() {
				return "Pinned";
			}
			
			@Override
			public @NonNull String getTopLeftCellLowerText() {
				return "";
			}
			
			@Override
			public @NonNull String @Nullable [] getAdditionalRowHeaderAttributes(@NonNull GridItem item) {
				return null;
			}
			
			@Override
			public String getAdditionalRowHeaderText(@NonNull GridItem item){
				if (item.getData() instanceof Row row && row.isReference()) {
					return "Y";
				}
				return "";
			};
			
			@Override
			public @NonNull String @Nullable [] getAdditionalPreRows() {
				return null;
			}
			
			@Override
			public @NonNull String @Nullable [] getAdditionalHeaderAttributes(GridColumn column) {
				return null;
			}
			
			@Override
			public @NonNull String @Nullable [] getAdditionalAttributes(@NonNull GridItem item, int columnIdx) {
				return null;
			}
		});
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
