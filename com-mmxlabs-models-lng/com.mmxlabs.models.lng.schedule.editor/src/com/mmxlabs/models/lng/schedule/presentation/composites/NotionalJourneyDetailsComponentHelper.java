/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.schedule.presentation.composites;

import com.mmxlabs.models.lng.schedule.SchedulePackage;

import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;

import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;

import org.eclipse.emf.ecore.EClass;

/**
 * A component helper for NotionalJourneyDetails instances
 *
 * @generated
 */
public class NotionalJourneyDetailsComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public NotionalJourneyDetailsComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public NotionalJourneyDetailsComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(SchedulePackage.Literals.MATCHING_CONTRACT_DETAILS));
	}
	
	/**
	 * add editors to a composite, using NotionalJourneyDetails as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, SchedulePackage.Literals.NOTIONAL_JOURNEY_DETAILS);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_distanceEditor(detailComposite, topClass);
		add_totalTimeInDaysEditor(detailComposite, topClass);
		add_totalFuelUsedEditor(detailComposite, topClass);
		add_fuelPriceEditor(detailComposite, topClass);
		add_totalFuelCostEditor(detailComposite, topClass);
		add_hireRateEditor(detailComposite, topClass);
		add_hireCostEditor(detailComposite, topClass);
		add_routeTakenEditor(detailComposite, topClass);
		add_canalCostEditor(detailComposite, topClass);
		add_lumpSumEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the distance feature on NotionalJourneyDetails
	 *
	 * @generated
	 */
	protected void add_distanceEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.NOTIONAL_JOURNEY_DETAILS__DISTANCE));
	}
	/**
	 * Create the editor for the totalTimeInDays feature on NotionalJourneyDetails
	 *
	 * @generated
	 */
	protected void add_totalTimeInDaysEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.NOTIONAL_JOURNEY_DETAILS__TOTAL_TIME_IN_DAYS));
	}
	/**
	 * Create the editor for the totalFuelUsed feature on NotionalJourneyDetails
	 *
	 * @generated
	 */
	protected void add_totalFuelUsedEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.NOTIONAL_JOURNEY_DETAILS__TOTAL_FUEL_USED));
	}
	/**
	 * Create the editor for the fuelPrice feature on NotionalJourneyDetails
	 *
	 * @generated
	 */
	protected void add_fuelPriceEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.NOTIONAL_JOURNEY_DETAILS__FUEL_PRICE));
	}
	/**
	 * Create the editor for the totalFuelCost feature on NotionalJourneyDetails
	 *
	 * @generated
	 */
	protected void add_totalFuelCostEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.NOTIONAL_JOURNEY_DETAILS__TOTAL_FUEL_COST));
	}
	/**
	 * Create the editor for the hireRate feature on NotionalJourneyDetails
	 *
	 * @generated
	 */
	protected void add_hireRateEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.NOTIONAL_JOURNEY_DETAILS__HIRE_RATE));
	}
	/**
	 * Create the editor for the hireCost feature on NotionalJourneyDetails
	 *
	 * @generated
	 */
	protected void add_hireCostEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.NOTIONAL_JOURNEY_DETAILS__HIRE_COST));
	}
	/**
	 * Create the editor for the routeTaken feature on NotionalJourneyDetails
	 *
	 * @generated
	 */
	protected void add_routeTakenEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.NOTIONAL_JOURNEY_DETAILS__ROUTE_TAKEN));
	}
	/**
	 * Create the editor for the canalCost feature on NotionalJourneyDetails
	 *
	 * @generated
	 */
	protected void add_canalCostEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.NOTIONAL_JOURNEY_DETAILS__CANAL_COST));
	}
	/**
	 * Create the editor for the lumpSum feature on NotionalJourneyDetails
	 *
	 * @generated
	 */
	protected void add_lumpSumEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, SchedulePackage.Literals.NOTIONAL_JOURNEY_DETAILS__LUMP_SUM));
	}
}