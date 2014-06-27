/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.lingo.reports.IScenarioInstanceElementCollector;
import com.mmxlabs.lingo.reports.components.EMFReportView;
import com.mmxlabs.lingo.reports.components.ScheduleBasedReportBuilder;
import com.mmxlabs.lingo.reports.utils.ScheduleDiffUtils;
import com.mmxlabs.models.ui.tabular.generic.GenericEMFTableDataModel;

/**
 * Abstract base class for the various reports based off CargoAllocations. We use the {@link GenericEMFTableDataModel} to create a table model where each "Node" or row contains references to the
 * CargoAllocation, and SlotAllocations for the load and discharge. For cargoes with more that one load or discharge, there will be multiple Node elements created - with the same CargoAllocation and
 * "Group" reference
 * 
 */
public abstract class AbstractCargoReportView extends EMFReportView {
	protected EPackage tableDataModel;
	protected EStructuralFeature cargoAllocationRef;
	protected EStructuralFeature loadAllocationRef;
	protected EStructuralFeature dischargeAllocationRef;

	private final ScheduleBasedReportBuilder builder;

	public AbstractCargoReportView(String id) {
		super(id);

		builder = new ScheduleBasedReportBuilder(this, pinDiffModeHelper);

		tableDataModel = builder.getTableDataModel();
		cargoAllocationRef = builder.getCargoAllocationRef();
		loadAllocationRef = builder.getLoadAllocationRef();
		dischargeAllocationRef = builder.getDischargeAllocationRef();

		// Show just cargoes
		builder.setRowFilter(ScheduleBasedReportBuilder.ROW_FILTER_CARGO_ROW);

		createColumns();
	}

	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);

		viewer.setComparator(GenericEMFTableDataModel.createGroupComparator(viewer.getComparator(), tableDataModel));
	}

	protected void createColumns() {
		builder.createPinDiffColumns();
	}

	@Override
	protected boolean handleSelections() {
		return true;
	}

	@Override
	protected void processInputs(final Object[] result) {
		builder.processInputs(result);
	}

	@Override
	protected boolean isElementDifferent(final EObject pinnedObject, final EObject otherObject) {
		return ScheduleDiffUtils.isElementDifferent((EObject) pinnedObject.eGet(cargoAllocationRef), (EObject) otherObject.eGet(cargoAllocationRef));
	}

	@Override
	protected IScenarioInstanceElementCollector getElementCollector() {
		return builder.getElementCollector();
	}

	/**
	 * Returns a key of some kind for the element
	 * 
	 * @param element
	 * @return
	 */
	@Override
	public String getElementKey(EObject element) {
		return builder.getElementKey(element);
	}

	@Override
	protected List<?> adaptSelectionFromWidget(List<?> selection) {
		return builder.adaptSelectionFromWidget(selection);
	}
}
