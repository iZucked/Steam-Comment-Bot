/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
* Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.emissions;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IMemento;

import com.mmxlabs.lingo.reports.modelbased.AbstractSimpleModelBasedReportView;
import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.DefaultMenuCreatorAction;
import com.mmxlabs.models.lng.fleet.CIIReferenceData;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;

public class CargoEmissionAccountingReportView extends AbstractSimpleModelBasedReportView<CargoEmissionAccountingReportModelV1> {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.lingo.reports.emissions.CargoEmissionAccountingReportView";
	private DeltaHelper deltaHelper;

	@SuppressWarnings("null")
	public CargoEmissionAccountingReportView() {
		super(CargoEmissionAccountingReportModelV1.class);
	}

	@Override
	protected synchronized List<CargoEmissionAccountingReportModelV1> transform(final ISelectedDataProvider selectedDataProvider) {

		final List<CargoEmissionAccountingReportModelV1> rows = new LinkedList<>();
		for (final var o : selectedDataProvider.getAllScenarioResults()) {
			final ScheduleModel typedResult = o.getTypedResult(ScheduleModel.class);
			final FleetModel fleetModel = ScenarioModelUtil.getFleetModel(o.getScenarioDataProvider());
			final CIIReferenceData ciiReferenceData = fleetModel.getCiiReferences();
			final String scenarioName = o.getScenarioInstance().getName();
			if (typedResult != null) {
				final List<CargoEmissionAccountingReportModelV1> data = //
						CargoEmissionAccountingReportJSONGenerator.createReportData(typedResult, //
						ciiReferenceData,
						o.equals(selectedDataProvider.getPinnedScenarioResult()), scenarioName, //
						aggregationMode.getValue(), groupingMode == GroupingMode.INCLUDE);
				rows.addAll(data);
			}
		}

		final boolean processDeltas = selectedDataProvider.inPinDiffMode();
		final boolean showScenarioColumn = processDeltas || selectedDataProvider.getAllScenarioResults().size() >= 2;
		EmissionReportUtils.changeScenarioNameColumnVisibility(columnInfo, showScenarioColumn);
		deltaHelper.setProcessDeltas(processDeltas);
		if (processDeltas) {
			return processDeltas(rows);
		}
		return rows;
	}

	private List<CargoEmissionAccountingReportModelV1> processDeltas(final List<CargoEmissionAccountingReportModelV1> rows) {
		final List<CargoEmissionAccountingReportModelV1> result = new ArrayList<>();
		
		final CargoEmissionAccountingReportModelV1 totalModel = new CargoEmissionAccountingReportModelV1();
		totalModel.initZeroes();

		int group = 0;

		for (final CargoEmissionAccountingReportModelV1 r1 : rows) {
			if (r1.isPinned()) {
				for (final CargoEmissionAccountingReportModelV1 r2 : rows) {
					if (!r2.isPinned() && EmissionReportUtils.checkIVesselEmissionsAreSimilar(r1, r2)) {
						
						final CargoEmissionAccountingReportModelV1 model = new CargoEmissionAccountingReportModelV1();
						model.setDelta(r2, r1);

						if (!r1.equals(r2) && !(result.contains(r1) && result.contains(r2))) {
							totalModel.addToTotal(model);
							r1.setGroup(group);
							r2.setGroup(group);
							model.setGroup(group);
							result.add(r1);
							result.add(r2);
							result.add(model);
							++group;
						}
						break;
					}
				}
			}
		}

		if (totalModel.isNonZero()) {
			totalModel.setGroup(Integer.MIN_VALUE);
			totalModel.scenarioName = "Total Δ";
			result.add(0, totalModel);
		}
		
		return result;
	}

	@Override
	protected BiConsumer<ViewerCell, Field> createStyler() {
		return EmissionReportUtils::styleTheCell;
	}

	@Override
	protected void makeActions() {
		getViewSite().getActionBars().getToolBarManager().add(new GroupMarker("filters"));
		final AggregationModeActionMenu aggregationModeAction = new AggregationModeActionMenu("Aggregation and grouping");
		aggregationModeAction.setImageDescriptor(CommonImages.getImageDescriptor(IconPaths.Group, IconMode.Enabled));
		getViewSite().getActionBars().getToolBarManager().prependToGroup("filters", aggregationModeAction);
		
		getViewSite().getActionBars().getToolBarManager().add(deltaHelper.createDeltaAction());

		super.makeActions();
	}
	
	private class AggregationModeActionMenu extends DefaultMenuCreatorAction{

		public AggregationModeActionMenu(String label) {
			super(label);
		}

		@Override
		protected void populate(Menu menu) {
			
			for(final AggregationMode mode : AggregationMode.values()) {
				final Action action = new Action(mode.getLabel()) {
					@Override
					public void run() {
						aggregationMode = mode;
						getViewSite().getActionBars().updateActionBars();
						saveState(memento);
						CargoEmissionAccountingReportView.this.refresh();
					}
				};
				action.setChecked(aggregationMode == mode);
				addActionToMenu(action, menu);
			}
			
			new MenuItem(menu, SWT.SEPARATOR);
			
			for(final GroupingMode mode : GroupingMode.values()) {
				final Action action = new Action(mode.getLabel()) {
					@Override
					public void run() {
						groupingMode = mode;
						getViewSite().getActionBars().updateActionBars();
						saveState(memento);
						CargoEmissionAccountingReportView.this.refresh();
					}
				};
				action.setChecked(groupingMode == mode);
				addActionToMenu(action, menu);
			}
		}
	}

	private static final String GROUPING_MODE = "CARGO_EMISSION_ACCOUNTING_GROUPING_MODE";
	private static final String AGGREGATION_MODE = "CARGO_EMISSION_ACCOUNTING_AGGREGATION_MODE";
	
	@Override
	protected void saveConfigState(final IMemento configMemento) {
		final IMemento am = configMemento.createChild(AGGREGATION_MODE);
		am.putBoolean(AGGREGATION_MODE, aggregationMode.getValue());
		final IMemento gm = configMemento.createChild(GROUPING_MODE);
		gm.putString(GROUPING_MODE, groupingMode.getLabel());
	}
	
	@Override
	protected void initConfigMemento(final IMemento configMemento) {
		final IMemento am = configMemento.getChild(AGGREGATION_MODE);
		if (am != null && am.getBoolean(AGGREGATION_MODE) != null) {
			aggregationMode = am.getBoolean(AGGREGATION_MODE) ? AggregationMode.TRUE : AggregationMode.FALSE;
		}
		final IMemento gm = configMemento.getChild(GROUPING_MODE);
		if (gm != null && gm.getString(GROUPING_MODE) != null){
			groupingMode = gm.getString(GROUPING_MODE).equalsIgnoreCase(GroupingMode.INCLUDE.getLabel()) ? GroupingMode.INCLUDE : GroupingMode.SEPARATE; 
		}
	}
	
	private AggregationMode aggregationMode = AggregationMode.FALSE;
	
	private enum AggregationMode{
		TRUE("Discharge to Discharge", true), FALSE("Load to Load", false);
		
		private final String label;
		private final boolean value;

		private AggregationMode(String label, boolean value) {
			this.label = label;
			this.value = value;
		}
		
		public String getLabel() {
			return this.label;
		}
		
		public boolean getValue() {
			return this.value;
		}
	}
	
	private GroupingMode groupingMode = GroupingMode.INCLUDE;
	
	public enum GroupingMode{
		INCLUDE("Include Events"), SEPARATE("Separate Events");
		
		private final String label;

		private GroupingMode(String label) {
			this.label = label;
		}
		
		public String getLabel() {
			return this.label;
		}
	}
	
	@Override
	protected synchronized void refresh() {
		if (viewer != null) {
			viewer.refresh();
		}
		super.refresh();
	}

	@Override
	public void createPartControl(final Composite parent) {
		deltaHelper = new DeltaHelper();
		super.createPartControl(parent);

		if (viewer != null) {
			deltaHelper.setViewer(viewer);
		}
	}
}
