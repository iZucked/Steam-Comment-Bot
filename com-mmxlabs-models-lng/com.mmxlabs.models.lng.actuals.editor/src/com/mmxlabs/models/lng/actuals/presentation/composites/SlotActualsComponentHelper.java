/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.actuals.presentation.composites;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.lng.actuals.ActualsPackage;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

/**
 * A component helper for SlotActuals instances
 *
 * @generated
 */
public class SlotActualsComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public SlotActualsComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public SlotActualsComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(TypesPackage.Literals.ITIMEZONE_PROVIDER));
	}
	
	/**
	 * add editors to a composite, using SlotActuals as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, ActualsPackage.Literals.SLOT_ACTUALS);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_slotEditor(detailComposite, topClass);
		add_counterpartyEditor(detailComposite, topClass);
		add_operationsStartEditor(detailComposite, topClass);
		add_operationsEndEditor(detailComposite, topClass);
		add_titleTransferPointEditor(detailComposite, topClass);
		add_volumeInM3Editor(detailComposite, topClass);
		add_volumeInMMBtuEditor(detailComposite, topClass);
		add_priceDOLEditor(detailComposite, topClass);
		add_penaltyEditor(detailComposite, topClass);
		add_notesEditor(detailComposite, topClass);
		add_CVEditor(detailComposite, topClass);
		add_baseFuelConsumptionEditor(detailComposite, topClass);
		add_portBaseFuelConsumptionEditor(detailComposite, topClass);
		add_routeEditor(detailComposite, topClass);
		add_distanceEditor(detailComposite, topClass);
		add_routeCostsEditor(detailComposite, topClass);
		add_crewBonusEditor(detailComposite, topClass);
		add_portChargesEditor(detailComposite, topClass);
		add_capacityChargesEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the CV feature on SlotActuals
	 *
	 * @generated
	 */
	protected void add_CVEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ActualsPackage.Literals.SLOT_ACTUALS__CV));
	}
	/**
	 * Create the editor for the portCharges feature on SlotActuals
	 *
	 * @generated
	 */
	protected void add_portChargesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ActualsPackage.Literals.SLOT_ACTUALS__PORT_CHARGES));
	}
	/**
	 * Create the editor for the capacityCharges feature on SlotActuals
	 *
	 * @generated
	 */
	protected void add_capacityChargesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ActualsPackage.Literals.SLOT_ACTUALS__CAPACITY_CHARGES));
	}

	/**
	 * Create the editor for the baseFuelConsumption feature on SlotActuals
	 *
	 * @generated
	 */
	protected void add_baseFuelConsumptionEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ActualsPackage.Literals.SLOT_ACTUALS__BASE_FUEL_CONSUMPTION));
	}

	/**
	 * Create the editor for the portBaseFuelConsumption feature on SlotActuals
	 *
	 * @generated
	 */
	protected void add_portBaseFuelConsumptionEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ActualsPackage.Literals.SLOT_ACTUALS__PORT_BASE_FUEL_CONSUMPTION));
	}

	/**
	 * Create the editor for the route feature on SlotActuals
	 *
	 * @generated
	 */
	protected void add_routeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ActualsPackage.Literals.SLOT_ACTUALS__ROUTE));
	}

	/**
	 * Create the editor for the distance feature on SlotActuals
	 *
	 * @generated
	 */
	protected void add_distanceEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ActualsPackage.Literals.SLOT_ACTUALS__DISTANCE));
	}

	/**
	 * Create the editor for the routeCosts feature on SlotActuals
	 *
	 * @generated
	 */
	protected void add_routeCostsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ActualsPackage.Literals.SLOT_ACTUALS__ROUTE_COSTS));
	}

	/**
	 * Create the editor for the crewBonus feature on SlotActuals
	 *
	 * @generated
	 */
	protected void add_crewBonusEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ActualsPackage.Literals.SLOT_ACTUALS__CREW_BONUS));
	}

	/**
	 * Create the editor for the slot feature on SlotActuals
	 *
	 * @generated
	 */
	protected void add_slotEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ActualsPackage.Literals.SLOT_ACTUALS__SLOT));
	}

	/**
	 * Create the editor for the counterparty feature on SlotActuals
	 *
	 * @generated
	 */
	protected void add_counterpartyEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ActualsPackage.Literals.SLOT_ACTUALS__COUNTERPARTY));
	}

	/**
	 * Create the editor for the operationsStart feature on SlotActuals
	 *
	 * @generated
	 */
	protected void add_operationsStartEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ActualsPackage.Literals.SLOT_ACTUALS__OPERATIONS_START));
	}

	/**
	 * Create the editor for the operationsEnd feature on SlotActuals
	 *
	 * @generated
	 */
	protected void add_operationsEndEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ActualsPackage.Literals.SLOT_ACTUALS__OPERATIONS_END));
	}

	/**
	 * Create the editor for the titleTransferPoint feature on SlotActuals
	 *
	 * @generated
	 */
	protected void add_titleTransferPointEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ActualsPackage.Literals.SLOT_ACTUALS__TITLE_TRANSFER_POINT));
	}

	/**
	 * Create the editor for the volumeInM3 feature on SlotActuals
	 *
	 * @generated
	 */
	protected void add_volumeInM3Editor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ActualsPackage.Literals.SLOT_ACTUALS__VOLUME_IN_M3));
	}

	/**
	 * Create the editor for the volumeInMMBtu feature on SlotActuals
	 *
	 * @generated
	 */
	protected void add_volumeInMMBtuEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ActualsPackage.Literals.SLOT_ACTUALS__VOLUME_IN_MM_BTU));
	}

	/**
	 * Create the editor for the priceDOL feature on SlotActuals
	 *
	 * @generated
	 */
	protected void add_priceDOLEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ActualsPackage.Literals.SLOT_ACTUALS__PRICE_DOL));
	}

	/**
	 * Create the editor for the penalty feature on SlotActuals
	 *
	 * @generated
	 */
	protected void add_penaltyEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ActualsPackage.Literals.SLOT_ACTUALS__PENALTY));
	}

	/**
	 * Create the editor for the notes feature on SlotActuals
	 *
	 * @generated
	 */
	protected void add_notesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ActualsPackage.Literals.SLOT_ACTUALS__NOTES));
	}
}