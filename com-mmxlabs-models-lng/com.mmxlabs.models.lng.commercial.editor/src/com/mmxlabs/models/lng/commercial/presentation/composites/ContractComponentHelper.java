/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.presentation.composites;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.ui.displaycomposites.DivertibleContractInlineEditorChangedListener;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.dates.MonthInlineEditor;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.impl.MultiTextInlineEditor;
import com.mmxlabs.models.ui.editors.impl.PermissiveRestrictiveInlineEditor;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

/**
 * A component helper for Contract instances
 *
 * @generated
 */
public class ContractComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 *
	 * @generated
	 */
	public ContractComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 *
	 * @generated
	 */
	public ContractComponentHelper(IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		superClassesHelpers.addAll(registry.getComponentHelpers(MMXCorePackage.Literals.UUID_OBJECT));
		superClassesHelpers.addAll(registry.getComponentHelpers(MMXCorePackage.Literals.NAMED_OBJECT));
	}
	
	/**
	 * add editors to a composite, using Contract as the supertype
	 *
	 * @generated
	 */
	 @Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, CommercialPackage.Literals.CONTRACT);	
	}
	
	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 *
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_codeEditor(detailComposite, topClass);
		add_counterpartyEditor(detailComposite, topClass);
		add_cnEditor(detailComposite, topClass);
		add_entityEditor(detailComposite, topClass);
		add_startDateEditor(detailComposite, topClass);
		add_endDateEditor(detailComposite, topClass);
		add_contractYearStartEditor(detailComposite, topClass);
		add_allowedPortsEditor(detailComposite, topClass);
		add_preferredPortEditor(detailComposite, topClass);
		add_minQuantityEditor(detailComposite, topClass);
		add_maxQuantityEditor(detailComposite, topClass);
		add_volumeLimitsUnitEditor(detailComposite, topClass);
		add_operationalToleranceEditor(detailComposite, topClass);
		add_fullCargoLotEditor(detailComposite, topClass);
		add_restrictedContractsEditor(detailComposite, topClass);
		add_restrictedContractsArePermissiveEditor(detailComposite, topClass);
		add_restrictedPortsEditor(detailComposite, topClass);
		add_restrictedPortsArePermissiveEditor(detailComposite, topClass);
		add_restrictedVesselsEditor(detailComposite, topClass);
		add_restrictedVesselsArePermissiveEditor(detailComposite, topClass);
		add_priceInfoEditor(detailComposite, topClass);
		add_notesEditor(detailComposite, topClass);
		add_contractTypeEditor(detailComposite, topClass);
		add_pricingEventEditor(detailComposite, topClass);
		add_cancellationExpressionEditor(detailComposite, topClass);
		add_shippingDaysRestrictionEditor(detailComposite, topClass);
	}
	
	/**
	 * Create the editor for the notes feature on Contract
	 * 
	 * @generated NO
	 */
	private void add_notesEditor(IInlineEditorContainer detailComposite,
			EClass topClass) {
		detailComposite.addInlineEditor(new MultiTextInlineEditor(CommercialPackage.Literals.CONTRACT__NOTES));
	}

	/**
	 * Create the editor for the contractType feature on Contract
	 *
	 * @generated
	 */
	protected void add_contractTypeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.CONTRACT__CONTRACT_TYPE));
	}

	/**
	 * Create the editor for the pricingEvent feature on Contract
	 *
	 * @generated
	 */
	protected void add_pricingEventEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.CONTRACT__PRICING_EVENT));
	}

	/**
	 * Create the editor for the cancellationFee feature on Contract
	 *
	 * @generated NOT
	 */
	protected void add_cancellationFeeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		// DEPRECATED
//		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.CONTRACT__CANCELLATION_FEE));
	}

	/**
	 * Create the editor for the cancellationExpression feature on Contract
	 *
	 * @generated
	 */
	protected void add_cancellationExpressionEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.CONTRACT__CANCELLATION_EXPRESSION));
	}
 
	/**
	 * Create the editor for the shippingDaysRestriction feature on Contract
	 *
	 * @generated NOT
	 */
	protected void add_shippingDaysRestrictionEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		final IInlineEditor editor = ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.CONTRACT__SHIPPING_DAYS_RESTRICTION);
		editor.addNotificationChangedListener(new DivertibleContractInlineEditorChangedListener());
		detailComposite.addInlineEditor(editor);
	}

	/**
	 * Create the editor for the contractYearStart feature on Contract
	 *
	 * @generated NOT
	 */
	protected void add_contractYearStartEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(new MonthInlineEditor(CommercialPackage.Literals.CONTRACT__CONTRACT_YEAR_START));
	}

	/**
	 * Create the editor for the code feature on Contract
	 *
	 * @generated NOT
	 */
	protected void add_codeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		if (LicenseFeatures.isPermitted("features:adp")) {
			detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.CONTRACT__CODE));
		}

	}

	/**
	 * Create the editor for the counterparty feature on Contract
	 *
	 * @generated
	 */
	protected void add_counterpartyEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.CONTRACT__COUNTERPARTY));
	}

	/**
	 * Create the editor for the cn feature on Contract
	 *
	 * @generated
	 */
	protected void add_cnEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.CONTRACT__CN));
	}

	/**
	 * Create the editor for the startDate feature on Contract
	 *
	 * @generated
	 */
	protected void add_startDateEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.CONTRACT__START_DATE));
	}

	/**
	 * Create the editor for the endDate feature on Contract
	 *
	 * @generated
	 */
	protected void add_endDateEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.CONTRACT__END_DATE));
	}

	/**
	 * Create the editor for the volumeParams feature on Contract
	 *
	 * @generated NOT
	 */
	protected void add_volumeParamsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
//		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.CONTRACT__VOLUME_PARAMS));
	}

	/**
	 * Create the editor for the entity feature on Contract
	 *
	 * @generated
	 */
	protected void add_entityEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.CONTRACT__ENTITY));
	}

	/**
	 * Create the editor for the allowedPorts feature on Contract
	 *
	 * @generated
	 */
	protected void add_allowedPortsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.CONTRACT__ALLOWED_PORTS));
	}

	/**
	 * Create the editor for the preferredPort feature on Contract
	 *
	 * @generated
	 */
	protected void add_preferredPortEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.CONTRACT__PREFERRED_PORT));
	}

	/**
	 * Create the editor for the minQuantity feature on Contract
	 *
	 * @generated
	 */
	protected void add_minQuantityEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.CONTRACT__MIN_QUANTITY));
	}

	/**
	 * Create the editor for the maxQuantity feature on Contract
	 *
	 * @generated
	 */
	protected void add_maxQuantityEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.CONTRACT__MAX_QUANTITY));
	}

	/**
	 * Create the editor for the operationalTolerance feature on Contract
	 *
	 * @generated
	 */
	protected void add_operationalToleranceEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.CONTRACT__OPERATIONAL_TOLERANCE));
	}

	/**
	 * Create the editor for the fullCargoLot feature on Contract
	 *
	 * @generated
	 */
	protected void add_fullCargoLotEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.CONTRACT__FULL_CARGO_LOT));
	}

	/**
	 * Create the editor for the volumeLimitsUnit feature on Contract
	 *
	 * @generated
	 */
	protected void add_volumeLimitsUnitEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.CONTRACT__VOLUME_LIMITS_UNIT));
	}

	/**
	 * Create the editor for the restrictedContracts feature on Contract
	 *
	 * @generated
	 */
	protected void add_restrictedContractsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.CONTRACT__RESTRICTED_CONTRACTS));
	}

	/**
	 * Create the editor for the restrictedContractsArePermissive feature on Contract
	 *
	 * @generated NOT
	 */
	protected void add_restrictedContractsArePermissiveEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(new PermissiveRestrictiveInlineEditor(CommercialPackage.Literals.CONTRACT__RESTRICTED_CONTRACTS_ARE_PERMISSIVE));
		//detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.CONTRACT__RESTRICTED_CONTRACTS_ARE_PERMISSIVE));
	}

	/**
	 * Create the editor for the restrictedPorts feature on Contract
	 *
	 * @generated
	 */
	protected void add_restrictedPortsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.CONTRACT__RESTRICTED_PORTS));
	}

	/**
	 * Create the editor for the restrictedPortsArePermissive feature on Contract
	 *
	 * @generated NOT
	 */
	protected void add_restrictedPortsArePermissiveEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(new PermissiveRestrictiveInlineEditor(CommercialPackage.Literals.CONTRACT__RESTRICTED_PORTS_ARE_PERMISSIVE));
		//detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.CONTRACT__RESTRICTED_PORTS_ARE_PERMISSIVE));
	}

	/**
	 * Create the editor for the restrictedVessels feature on Contract
	 *
	 * @generated
	 */
	protected void add_restrictedVesselsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.CONTRACT__RESTRICTED_VESSELS));
	}

	/**
	 * Create the editor for the restrictedVesselsArePermissive feature on Contract
	 *
	 * @generated NOT
	 */
	protected void add_restrictedVesselsArePermissiveEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(new PermissiveRestrictiveInlineEditor(CommercialPackage.Literals.CONTRACT__RESTRICTED_VESSELS_ARE_PERMISSIVE));
		//detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.CONTRACT__RESTRICTED_VESSELS_ARE_PERMISSIVE));
	}

	/**
	 * Create the editor for the priceInfo feature on Contract
	 *
	 * @generated NO
	 */
	protected void add_priceInfoEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		//Composite composite = new Composite(detailComposite);
		//detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.CONTRACT__PRICE_INFO));
	}
}