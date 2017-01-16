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
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

/**
 * A component helper for CargoActuals instances
 *
 * @generated
 */
public class CargoActualsComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public CargoActualsComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public CargoActualsComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
	}
	
	/**
	 * add editors to a composite, using CargoActuals as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, ActualsPackage.Literals.CARGO_ACTUALS);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_actualsEditor(detailComposite, topClass);
		add_returnActualsEditor(detailComposite, topClass);
		add_contractYearEditor(detailComposite, topClass);
		add_operationNumberEditor(detailComposite, topClass);
		add_subOperationNumberEditor(detailComposite, topClass);
		add_sellerIDEditor(detailComposite, topClass);
		add_cargoReferenceEditor(detailComposite, topClass);
		add_cargoReferenceSellerEditor(detailComposite, topClass);
		add_vesselEditor(detailComposite, topClass);
		add_charterRatePerDayEditor(detailComposite, topClass);
		add_cargoEditor(detailComposite, topClass);
		add_baseFuelPriceEditor(detailComposite, topClass);
		add_insurancePremiumEditor(detailComposite, topClass);
	}
	/**
	 * Create the editor for the baseFuelPrice feature on CargoActuals
	 *
	 * @generated
	 */
	protected void add_baseFuelPriceEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ActualsPackage.Literals.CARGO_ACTUALS__BASE_FUEL_PRICE));
	}
	/**
	 * Create the editor for the insurancePremium feature on CargoActuals
	 *
	 * @generated
	 */
	protected void add_insurancePremiumEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ActualsPackage.Literals.CARGO_ACTUALS__INSURANCE_PREMIUM));
	}
	/**
	 * Create the editor for the actuals feature on CargoActuals
	 *
	 * @generated
	 */
	protected void add_actualsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ActualsPackage.Literals.CARGO_ACTUALS__ACTUALS));
	}

	/**
	 * Create the editor for the returnActuals feature on CargoActuals
	 *
	 * @generated
	 */
	protected void add_returnActualsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ActualsPackage.Literals.CARGO_ACTUALS__RETURN_ACTUALS));
	}

	/**
	 * Create the editor for the contractYear feature on CargoActuals
	 *
	 * @generated
	 */
	protected void add_contractYearEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ActualsPackage.Literals.CARGO_ACTUALS__CONTRACT_YEAR));
	}

	/**
	 * Create the editor for the operationNumber feature on CargoActuals
	 *
	 * @generated
	 */
	protected void add_operationNumberEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ActualsPackage.Literals.CARGO_ACTUALS__OPERATION_NUMBER));
	}

	/**
	 * Create the editor for the subOperationNumber feature on CargoActuals
	 *
	 * @generated
	 */
	protected void add_subOperationNumberEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ActualsPackage.Literals.CARGO_ACTUALS__SUB_OPERATION_NUMBER));
	}

	/**
	 * Create the editor for the sellerID feature on CargoActuals
	 *
	 * @generated
	 */
	protected void add_sellerIDEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ActualsPackage.Literals.CARGO_ACTUALS__SELLER_ID));
	}

	/**
	 * Create the editor for the cargoReference feature on CargoActuals
	 *
	 * @generated
	 */
	protected void add_cargoReferenceEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ActualsPackage.Literals.CARGO_ACTUALS__CARGO_REFERENCE));
	}

	/**
	 * Create the editor for the cargoReferenceSeller feature on CargoActuals
	 *
	 * @generated
	 */
	protected void add_cargoReferenceSellerEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ActualsPackage.Literals.CARGO_ACTUALS__CARGO_REFERENCE_SELLER));
	}

	/**
	 * Create the editor for the vessel feature on CargoActuals
	 *
	 * @generated
	 */
	protected void add_vesselEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ActualsPackage.Literals.CARGO_ACTUALS__VESSEL));
	}

	/**
	 * Create the editor for the charterRatePerDay feature on CargoActuals
	 *
	 * @generated
	 */
	protected void add_charterRatePerDayEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ActualsPackage.Literals.CARGO_ACTUALS__CHARTER_RATE_PER_DAY));
	}

	/**
	 * Create the editor for the cargo feature on CargoActuals
	 *
	 * @generated
	 */
	protected void add_cargoEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, ActualsPackage.Literals.CARGO_ACTUALS__CARGO));
	}
}