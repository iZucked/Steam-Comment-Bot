/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.presentation.composites;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.ui.inlineeditors.NominatedVesselEditorWrapper;
import com.mmxlabs.models.lng.cargo.ui.inlineeditors.RestrictionsOverrideMultiReferenceInlineEditor;
import com.mmxlabs.models.lng.cargo.ui.inlineeditors.RestrictionsOverridePortMultiReferenceInlineEditor;
import com.mmxlabs.models.lng.cargo.ui.inlineeditors.VolumeInlineEditor;
import com.mmxlabs.models.lng.fleet.ui.inlineeditors.TextualVesselReferenceInlineEditor;
import com.mmxlabs.models.lng.port.ui.editorpart.TextualPortReferenceInlineEditor;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.BaseComponentHelper;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.IComponentHelper;
import com.mmxlabs.models.ui.IInlineEditorContainer;
import com.mmxlabs.models.ui.date.LocalDateTextFormatter;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.impl.LocalDateInlineEditor;
import com.mmxlabs.models.ui.editors.impl.MultiTextInlineEditor;
import com.mmxlabs.models.ui.editors.impl.PermissiveRestrictiveInlineEditor;
import com.mmxlabs.models.ui.editors.impl.TextInlineEditor;
import com.mmxlabs.models.ui.registries.IComponentHelperRegistry;

/**
 * A component helper for Slot instances
 * 
 * @generated
 */
public class SlotComponentHelper extends BaseComponentHelper {
	protected List<IComponentHelper> superClassesHelpers = new ArrayList<IComponentHelper>();

	/**
	 * Construct a new instance, using the platform adapter manager
	 * 
	 * @generated
	 */
	public SlotComponentHelper() {
		this(Platform.getAdapterManager());
	}

	/**
	 * Construct a new instance of this helper
	 * 
	 * @generated NO there are no superclass fields we want here.
	 * 
	 *            TODO this could be an editor factory override instead.
	 */
	public SlotComponentHelper(final IAdapterManager adapterManager) {
		final IComponentHelperRegistry registry = com.mmxlabs.models.ui.Activator.getDefault().getComponentHelperRegistry();
		{
			superClassesHelpers.addAll(registry.getComponentHelpers(MMXCorePackage.Literals.UUID_OBJECT));
			superClassesHelpers.addAll(registry.getComponentHelpers(MMXCorePackage.Literals.NAMED_OBJECT));
		}
		// {
		// final IComponentHelper helper = registry.getComponentHelper(TypesPackage.Literals.ITIMEZONE_PROVIDER);
		// if (helper != null) superClassesHelpers.add(helper);
		// }
	}

	/**
	 * add editors to a composite, using Slot as the supertype
	 * 
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite) {
		addEditorsToComposite(detailComposite, CargoPackage.Literals.SLOT);	
	}

	/**
	 * Create the editors for features on this class directly, and superclass' features.
	 * 
	 * @generated
	 */
	@Override
	public void addEditorsToComposite(final IInlineEditorContainer detailComposite, final EClass topClass) {
		for (final IComponentHelper helper : superClassesHelpers) helper.addEditorsToComposite(detailComposite, topClass);
		add_contractEditor(detailComposite, topClass);
		add_counterpartyEditor(detailComposite, topClass);
		add_cnEditor(detailComposite, topClass);
		add_portEditor(detailComposite, topClass);
		add_windowStartEditor(detailComposite, topClass);
		add_windowStartTimeEditor(detailComposite, topClass);
		add_windowSizeEditor(detailComposite, topClass);
		add_windowSizeUnitsEditor(detailComposite, topClass);
		add_windowFlexEditor(detailComposite, topClass);
		add_windowFlexUnitsEditor(detailComposite, topClass);
		add_durationEditor(detailComposite, topClass);
		add_volumeLimitsUnitEditor(detailComposite, topClass);
		add_minQuantityEditor(detailComposite, topClass);
		add_maxQuantityEditor(detailComposite, topClass);
		add_operationalToleranceEditor(detailComposite, topClass);
		add_fullCargoLotEditor(detailComposite, topClass);
		add_optionalEditor(detailComposite, topClass);
		add_priceExpressionEditor(detailComposite, topClass);
		add_cargoEditor(detailComposite, topClass);
		add_pricingEventEditor(detailComposite, topClass);
		add_pricingDateEditor(detailComposite, topClass);
		add_notesEditor(detailComposite, topClass);
		add_shippingDaysRestrictionEditor(detailComposite, topClass);
		add_entityEditor(detailComposite, topClass);
		add_restrictedContractsEditor(detailComposite, topClass);
		add_restrictedContractsArePermissiveEditor(detailComposite, topClass);
		add_restrictedContractsOverrideEditor(detailComposite, topClass);
		add_restrictedPortsEditor(detailComposite, topClass);
		add_restrictedPortsArePermissiveEditor(detailComposite, topClass);
		add_restrictedPortsOverrideEditor(detailComposite, topClass);
		add_restrictedVesselsEditor(detailComposite, topClass);
		add_restrictedVesselsArePermissiveEditor(detailComposite, topClass);
		add_restrictedVesselsOverrideEditor(detailComposite, topClass);
		add_restrictedSlotsEditor(detailComposite, topClass);
		add_restrictedSlotsArePermissiveEditor(detailComposite, topClass);
		add_hedgesEditor(detailComposite, topClass);
		add_miscCostsEditor(detailComposite, topClass);
		add_cancellationExpressionEditor(detailComposite, topClass);
		add_nominatedVesselEditor(detailComposite, topClass);
		add_lockedEditor(detailComposite, topClass);
		add_cancelledEditor(detailComposite, topClass);
		add_windowCounterPartyEditor(detailComposite, topClass);
	}

	/**
	 * Create the editor for the windowStart feature on Slot
	 * 
	 * @generated NOT
	 */
	protected void add_windowStartEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {

		final IInlineEditor editor;
		if (topClass.getEAllSuperTypes().contains(CargoPackage.eINSTANCE.getSpotSlot())) {
			// For spot slots, only allow month portion to be edited
			editor = new LocalDateInlineEditor(CargoPackage.eINSTANCE.getSlot_WindowStart(), new LocalDateTextFormatter("MM/yyyy"));
		} else {
			editor = ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.SLOT__WINDOW_START);
		}
		detailComposite.addInlineEditor(editor);
	}

	/**
	 * Create the editor for the pricingDate feature on Slot
	 * 
	 * @generated
	 */
	protected void add_pricingDateEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.SLOT__PRICING_DATE));
	}

	/**
	 * Create the editor for the notes feature on Slot
	 * 
	 * @generated NO
	 */
	protected void add_notesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(new MultiTextInlineEditor(CargoPackage.Literals.SLOT__NOTES));
	}

	/**
	 * Create the editor for the entity feature on Slot
	 * 
	 * @generated
	 */
	protected void add_entityEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.SLOT__ENTITY));
	}

	/**
	 * Create the editor for the restrictedContracts feature on Slot
	 * 
	 * @generated NOT
	 */
	protected void add_restrictedContractsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(new RestrictionsOverrideMultiReferenceInlineEditor(CargoPackage.Literals.SLOT__RESTRICTED_CONTRACTS, CargoPackage.Literals.SLOT__RESTRICTED_CONTRACTS_OVERRIDE));

	}

	/**
	 * Create the editor for the restrictedContractsArePermissive feature on Slot
	 *
	 * @generated NOT
	 */
	protected void add_restrictedContractsArePermissiveEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(new PermissiveRestrictiveInlineEditor(CargoPackage.Literals.SLOT__RESTRICTED_CONTRACTS_ARE_PERMISSIVE));
	}

	/**
	 * Create the editor for the restrictedContractsOverride feature on Slot
	 *
	 * @generated NOT
	 */
	protected void add_restrictedContractsOverrideEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
	}

	/**
	 * Create the editor for the restrictedPorts feature on Slot
	 * 
	 * @generated NOT
	 */
	protected void add_restrictedPortsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(new RestrictionsOverridePortMultiReferenceInlineEditor(CargoPackage.Literals.SLOT__RESTRICTED_PORTS, CargoPackage.Literals.SLOT__RESTRICTED_PORTS_OVERRIDE));
	}

	/**
	 * Create the editor for the restrictedPortsArePermissive feature on Slot
	 *
	 * @generated NOT
	 */
	protected void add_restrictedPortsArePermissiveEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(new PermissiveRestrictiveInlineEditor(CargoPackage.Literals.SLOT__RESTRICTED_PORTS_ARE_PERMISSIVE));
	}

	/**
	 * Create the editor for the restrictedPortsOverride feature on Slot
	 *
	 * @generated NOT
	 */
	protected void add_restrictedPortsOverrideEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
	}

	/**
	 * Create the editor for the restrictedSlots feature on Slot
	 *
	 * @generated
	 */
	protected void add_restrictedSlotsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.SLOT__RESTRICTED_SLOTS));
	}

	/**
	 * Create the editor for the restrictedSlotsArePermissive feature on Slot
	 *
	 * @generated NOT
	 */
	protected void add_restrictedSlotsArePermissiveEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(new PermissiveRestrictiveInlineEditor(CargoPackage.Literals.SLOT__RESTRICTED_SLOTS_ARE_PERMISSIVE));
	}

	/**
	 * Create the editor for the restrictedVessels feature on Slot
	 *
	 * @generated NOT
	 */
	protected void add_restrictedVesselsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(new RestrictionsOverrideMultiReferenceInlineEditor(CargoPackage.Literals.SLOT__RESTRICTED_VESSELS, CargoPackage.Literals.SLOT__RESTRICTED_VESSELS_OVERRIDE));
	}

	/**
	 * Create the editor for the restrictedVesselsArePermissive feature on Slot
	 *
	 * @generated NOT
	 */
	protected void add_restrictedVesselsArePermissiveEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(new PermissiveRestrictiveInlineEditor(CargoPackage.Literals.SLOT__RESTRICTED_VESSELS_ARE_PERMISSIVE));
	}

	/**
	 * Create the editor for the restrictedVesselsOverride feature on Slot
	 *
	 * @generated NOT
	 */
	protected void add_restrictedVesselsOverrideEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
	}

	/**
	 * Create the editor for the hedges feature on Slot
	 * 
	 * @generated
	 */
	protected void add_hedgesEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.SLOT__HEDGES));
	}

	/**
	 * Create the editor for the miscCosts feature on Slot
	 *
	 * @generated
	 */
	protected void add_miscCostsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.SLOT__MISC_COSTS));
	}

	/**
	 * Create the editor for the cancellationFee feature on Slot
	 * 
	 * @generated NOT
	 */
	protected void add_cancellationFeeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
	}

	/**
	 * Create the editor for the cancellationExpression feature on Slot
	 *
	 * @generated
	 */
	protected void add_cancellationExpressionEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.SLOT__CANCELLATION_EXPRESSION));
	}

	/**
	 * Create the editor for the nominatedVessel feature on Slot
	 * 
	 * @generated NOT
	 */
	protected void add_nominatedVesselEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(new NominatedVesselEditorWrapper(new TextualVesselReferenceInlineEditor(CargoPackage.Literals.SLOT__NOMINATED_VESSEL)));
	}

	/**
	 * Create the editor for the locked feature on Slot
	 * 
	 * @generated NOT
	 */
	protected void add_lockedEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		 detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.SLOT__LOCKED));
	}

	/**
	 * Create the editor for the cancelled feature on Slot
	 *
	 * @generated
	 */
	protected void add_cancelledEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.SLOT__CANCELLED));
	}

	/**
	 * Create the editor for the windowCounterParty feature on Slot
	 *
	 * @generated
	 */
	protected void add_windowCounterPartyEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.SLOT__WINDOW_COUNTER_PARTY));
	}

	/**
	 * Create the editor for the shippingDaysRestriction feature on Slot
	 * 
	 * @generated NOT
	 */
	protected void add_shippingDaysRestrictionEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {

		final IInlineEditor editor = ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.SLOT__SHIPPING_DAYS_RESTRICTION);
		editor.addNotificationChangedListener(new ShippingDaysRestrictionInlineEditorChangedListener());
		detailComposite.addInlineEditor(editor);
	}

	/**
	 * Create the editor for the windowStartTime feature on Slot
	 * 
	 * @generated
	 */
	protected void add_windowStartTimeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.SLOT__WINDOW_START_TIME));
	}

	/**
	 * Create the editor for the windowSize feature on Slot
	 * 
	 * @generated NOT
	 */
	protected void add_windowSizeEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.SLOT__WINDOW_SIZE));
//		detailComposite.addInlineEditor(new WindowSizeInlineEditor(CargoPackage.Literals.SLOT__WINDOW_SIZE  , CargoPackage.Literals.SLOT__WINDOW_SIZE_UNITS));

	}

	/**
	 * Create the editor for the windowSizeUnits feature on Slot
	 *
	 * @generated
	 */
	protected void add_windowSizeUnitsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.SLOT__WINDOW_SIZE_UNITS));
	}

	/**
	 * Create the editor for the windowFlex feature on Slot
	 *
	 * @generated
	 */
	protected void add_windowFlexEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.SLOT__WINDOW_FLEX));
	}

	/**
	 * Create the editor for the windowFlexUnits feature on Slot
	 *
	 * @generated
	 */
	protected void add_windowFlexUnitsEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.SLOT__WINDOW_FLEX_UNITS));
	}

	/**
	 * Create the editor for the port feature on Slot
	 * 
	 * @generated NOT
	 */
	protected void add_portEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(new TextualPortReferenceInlineEditor(CargoPackage.Literals.SLOT__PORT));
	}

	/**
	 * Create the editor for the contract feature on Slot
	 * 
	 * @generated NOT
	 */
	protected void add_contractEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		// Do not show contract editor for market slots.
		if (!CargoPackage.Literals.SPOT_SLOT.isSuperTypeOf(topClass)) {
			detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.SLOT__CONTRACT));
		}
	}

	/**
	 * Create the editor for the counterparty feature on Slot
	 *
	 * @generated
	 */
	protected void add_counterpartyEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.SLOT__COUNTERPARTY));
	}

	/**
	 * Create the editor for the cn feature on Slot
	 *
	 * @generated NOT
	 */
	protected void add_cnEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(new TextInlineEditor(CargoPackage.Literals.SLOT__CN));
	}

	/**
	 * Create the editor for the duration feature on Slot
	 * 
	 * @generated
	 */
	protected void add_durationEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.SLOT__DURATION));
	}

	/**
	 * Create the editor for the volumeLimitsUnit feature on Slot
	 *
	 * @generated
	 */
	protected void add_volumeLimitsUnitEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.SLOT__VOLUME_LIMITS_UNIT));
	}

	/**
	 * Create the editor for the minQuantity feature on Slot
	 * 
	 * @generated
	 */
	protected void add_minQuantityEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.SLOT__MIN_QUANTITY));
	}

	/**
	 * Create the editor for the maxQuantity feature on Slot
	 * 
	 * @generated NOT
	 */
	protected void add_maxQuantityEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(new VolumeInlineEditor(CargoPackage.Literals.SLOT__MAX_QUANTITY));
	}

	/**
	 * Create the editor for the operationalTolerance feature on Slot
	 *
	 * @generated
	 */
	protected void add_operationalToleranceEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.SLOT__OPERATIONAL_TOLERANCE));
	}

	/**
	 * Create the editor for the fullCargoLot feature on Slot
	 *
	 * @generated
	 */
	protected void add_fullCargoLotEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.SLOT__FULL_CARGO_LOT));
	}

	/**
	 * Create the editor for the optional feature on Slot
	 * 
	 * @generated
	 */
	protected void add_optionalEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.SLOT__OPTIONAL));
	}

	/**
	 * Create the editor for the priceExpression feature on Slot
	 * 
	 * @generated
	 */
	protected void add_priceExpressionEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.SLOT__PRICE_EXPRESSION));
	}

	/**
	 * Create the editor for the cargo feature on Slot
	 * 
	 * @generated NOT
	 */
	protected void add_cargoEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		// detailComposite.addInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.SLOT__CARGO));
	}

	/**
	 * Create the editor for the pricingEvent feature on Slot
	 * 
	 * @generated NOT
	 */
	protected void add_pricingEventEditor(final IInlineEditorContainer detailComposite, final EClass topClass) {
		final IInlineEditor editor = ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.SLOT__PRICING_EVENT);
		editor.addNotificationChangedListener(new PricingEventInlineEditorChangedListener());
		detailComposite.addInlineEditor(editor);
	}
}