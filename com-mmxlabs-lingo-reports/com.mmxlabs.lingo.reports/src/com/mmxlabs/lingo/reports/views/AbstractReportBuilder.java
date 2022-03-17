/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.ui.IMemento;

import com.mmxlabs.lingo.reports.utils.ColumnConfigurationDialog.OptionInfo;
import com.mmxlabs.lingo.reports.views.schedule.model.CompositeRow;
import com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage;
import com.mmxlabs.models.ui.tabular.columngeneration.ColumnBlockManager;

/**
 * @author Simon Goodall
 * 
 */
public abstract class AbstractReportBuilder {

	protected static final String ROW_FILTER_MEMENTO = "ROW_FILTER";
	protected static final String DIFF_FILTER_MEMENTO = "DIFF_FILTER";

	protected ColumnBlockManager blockManager;

	protected final Set<String> rowFilterInfo = new HashSet<>();
	protected final Set<String> diffFilterInfo = new HashSet<>();

	public OptionInfo[] ROW_FILTER_ALL;
	public OptionInfo[] DIFF_FILTER_ALL;

	private Map<String, OptionInfo> rowFilterToOptionInfo;
	private Map<String, OptionInfo> diffFilterToOptionInfo;
	
	
	@NonNull
	public static final OptionInfo DIFF_FILTER_PINNDED_SCENARIO = new OptionInfo("DIFF_FILTER_PINNDED_SCENARIO", "Show Pinned Scenario", "scenario");

	/**
	 * Replace the existing row filters with the following set.
	 * 
	 * @param filters
	 */
	public void setRowFilter(final String... filters) {
		rowFilterInfo.clear();
		for (final String filter : filters) {
			rowFilterInfo.add(filter);
		}
	}

	public void addRowFilters(final String... filters) {
		for (final String filter : filters) {
			rowFilterInfo.add(filter);
		}
	}

	public void removeRowFilters(final String... filters) {
		for (final String filter : filters) {
			rowFilterInfo.remove(filter);
		}
	}

	/**
	 * Replace the existing row filters with the following set.
	 * 
	 * @param filters
	 */
	public void setDiffFilter(final String... filters) {
		diffFilterInfo.clear();
		for (final String filter : filters) {
			diffFilterInfo.add(filter);
		}
	}

	public void addDiffFilters(final String... filters) {
		for (final String filter : filters) {
			diffFilterInfo.add(filter);
		}
	}

	public void removeDiffFilters(final String... filters) {
		for (final String filter : filters) {
			diffFilterInfo.remove(filter);
		}
	}

	public List<?> adaptSelectionFromWidget(final List<?> selection) {
		final List<Object> adaptedSelection = new ArrayList<Object>(selection.size() * 2);
		for (final Object obj : selection) {
			if (obj instanceof CompositeRow) {
				CompositeRow compositeRow = (CompositeRow) obj;
				
				if (compositeRow.getPinnedRow() != null) {
					adaptedSelection.add(compositeRow.getPinnedRow());
				}
				
				if (compositeRow.getPreviousRow() != null) {
					adaptedSelection.add(compositeRow.getPreviousRow());
				}
				
			} else {
				if (obj instanceof EObject) {
					adaptedSelection.add(((EObject) obj).eGet(ScheduleReportPackage.Literals.ROW__TARGET));
				}
				adaptedSelection.add(obj);
			}
			
		}

		return adaptedSelection;
	}

	public Set<String> getRowFilterInfo() {
		return rowFilterInfo;
	}

	public Set<String> getDiffFilterInfo() {
		return diffFilterInfo;
	}

	public OptionInfo getRowFilterOptionInfo(String rowFilterId) {
		if (this.rowFilterToOptionInfo == null) {
			this.rowFilterToOptionInfo = new HashMap<>();
			for (OptionInfo oi : ROW_FILTER_ALL) {
				this.rowFilterToOptionInfo.put(oi.id, oi);
			}
		}
		return this.rowFilterToOptionInfo.get(rowFilterId);
	}

	public OptionInfo getDiffFilterOptionInfo(String diffFilterId) {
		if (this.diffFilterToOptionInfo == null) {
			this.diffFilterToOptionInfo = new HashMap<>();
			for (OptionInfo oi : DIFF_FILTER_ALL) {
				this.diffFilterToOptionInfo.put(oi.id, oi);
			}
		}
		return this.diffFilterToOptionInfo.get(diffFilterId);
	}
	
	public void saveToMemento(final String uniqueConfigKey, final IMemento memento) {
		final IMemento rowsInfo = memento.createChild(uniqueConfigKey);
		for (final String option : rowFilterInfo) {
			final IMemento optionInfo = rowsInfo.createChild(ROW_FILTER_MEMENTO);
			optionInfo.putTextData(option);
		}
		for (final String option : diffFilterInfo) {
			final IMemento optionInfo = rowsInfo.createChild(DIFF_FILTER_MEMENTO);
			optionInfo.putTextData(option);
		}
	}

	public void initFromMemento(final String uniqueConfigKey, final IMemento memento) {
		final IMemento rowsInfo = memento.getChild(uniqueConfigKey);
		if (rowsInfo != null) {
			rowFilterInfo.clear();
			for (final IMemento optionInfo : rowsInfo.getChildren(ROW_FILTER_MEMENTO)) {
				rowFilterInfo.add(optionInfo.getTextData());
			}
			diffFilterInfo.clear();
			for (final IMemento optionInfo : rowsInfo.getChildren(DIFF_FILTER_MEMENTO)) {
				diffFilterInfo.add(optionInfo.getTextData());
			}
		} else {
			rowFilterInfo.addAll(OptionInfo.getIds(ROW_FILTER_ALL));
			diffFilterInfo.addAll(OptionInfo.getIds(DIFF_FILTER_ALL));
		}
		refreshDiffOptions();
	}

	public void refreshDiffOptions() {

	}

	public ColumnBlockManager getBlockManager() {
		return blockManager;
	}

	public void setBlockManager(ColumnBlockManager blockManager) {
		this.blockManager = blockManager;
	}
}
