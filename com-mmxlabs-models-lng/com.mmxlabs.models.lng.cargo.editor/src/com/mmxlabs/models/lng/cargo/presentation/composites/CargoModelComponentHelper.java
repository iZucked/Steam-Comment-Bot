/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.presentation.composites;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

/**
 * A component helper for CargoModel instances
 *
 * @generated
 */
public class CargoModelComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public CargoModelComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public CargoModelComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(MMXCorePackage.Literals.UUID_OBJECT));
	}
	
	/**
	 * add editors to a composite, using CargoModel as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, CargoPackage.Literals.CARGO_MODEL);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_loadSlotsEditor(detailComposite, topClass);
		add_dischargeSlotsEditor(detailComposite, topClass);
		add_cargoesEditor(detailComposite, topClass);
		add_cargoGroupsEditor(detailComposite, topClass);
		add_vesselAvailabilitiesEditor(detailComposite, topClass);
		add_vesselEventsEditor(detailComposite, topClass);
		add_vesselTypeGroupsEditor(detailComposite, topClass);
		add_inventoryModelsEditor(detailComposite, topClass);
		add_canalBookingsEditor(detailComposite, topClass);
		add_charterInMarketOverridesEditor(detailComposite, topClass);
		add_paperDealsEditor(detailComposite, topClass);
		add_dealSetsEditor(detailComposite, topClass);
		add_cargoesForExposuresEditor(detailComposite, topClass);
		add_cargoesForHedgingEditor(detailComposite, topClass);
		add_groupedDischargeSlotsEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the loadSlots feature on CargoModel
	 *
	 * @generated
	 */
	protected void add_loadSlotsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CARGO_MODEL__LOAD_SLOTS));
	}
	/**
	 * Create the editor for the dischargeSlots feature on CargoModel
	 *
	 * @generated
	 */
	protected void add_dischargeSlotsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CARGO_MODEL__DISCHARGE_SLOTS));
	}
	/**
	 * Create the editor for the cargoes feature on CargoModel
	 *
	 * @generated
	 */
	protected void add_cargoesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CARGO_MODEL__CARGOES));
	}

	/**
	 * Create the editor for the cargoGroups feature on CargoModel
	 *
	 * @generated
	 */
	protected void add_cargoGroupsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CARGO_MODEL__CARGO_GROUPS));
	}

	/**
	 * Create the editor for the vesselAvailabilities feature on CargoModel
	 *
	 * @generated
	 */
	protected void add_vesselAvailabilitiesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CARGO_MODEL__VESSEL_AVAILABILITIES));
	}

	/**
	 * Create the editor for the vesselEvents feature on CargoModel
	 *
	 * @generated
	 */
	protected void add_vesselEventsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CARGO_MODEL__VESSEL_EVENTS));
	}

	/**
	 * Create the editor for the vesselTypeGroups feature on CargoModel
	 *
	 * @generated
	 */
	protected void add_vesselTypeGroupsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CARGO_MODEL__VESSEL_TYPE_GROUPS));
	}

	/**
	 * Create the editor for the inventoryModels feature on CargoModel
	 *
	 * @generated
	 */
	protected void add_inventoryModelsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CARGO_MODEL__INVENTORY_MODELS));
	}

	/**
	 * Create the editor for the canalBookings feature on CargoModel
	 *
	 * @generated
	 */
	protected void add_canalBookingsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CARGO_MODEL__CANAL_BOOKINGS));
	}

	/**
	 * Create the editor for the charterInMarketOverrides feature on CargoModel
	 *
	 * @generated
	 */
	protected void add_charterInMarketOverridesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CARGO_MODEL__CHARTER_IN_MARKET_OVERRIDES));
	}

	/**
	 * Create the editor for the paperDeals feature on CargoModel
	 *
	 * @generated
	 */
	protected void add_paperDealsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CARGO_MODEL__PAPER_DEALS));
	}

	/**
	 * Create the editor for the dealSets feature on CargoModel
	 *
	 * @generated
	 */
	protected void add_dealSetsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CARGO_MODEL__DEAL_SETS));
	}

	/**
	 * Create the editor for the cargoesForExposures feature on CargoModel
	 *
	 * @generated
	 */
	protected void add_cargoesForExposuresEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CARGO_MODEL__CARGOES_FOR_EXPOSURES));
	}

	/**
	 * Create the editor for the cargoesForHedging feature on CargoModel
	 *
	 * @generated
	 */
	protected void add_cargoesForHedgingEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CARGO_MODEL__CARGOES_FOR_HEDGING));
	}

	/**
	 * Create the editor for the groupedDischargeSlots feature on CargoModel
	 *
	 * @generated
	 */
	protected void add_groupedDischargeSlotsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.CARGO_MODEL__GROUPED_DISCHARGE_SLOTS));
	}
}