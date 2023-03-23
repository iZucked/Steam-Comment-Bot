/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.presentation.composites;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.editor.SlotExpressionWrapper;
import com.mmxlabs.models.lng.cargo.ui.inlineeditors.NominatedVesselEditorWrapper;
import com.mmxlabs.models.lng.cargo.ui.inlineeditors.RestrictionsOverrideMultiReferenceInlineEditor;
import com.mmxlabs.models.lng.cargo.ui.inlineeditors.VolumeInlineEditor;
import com.mmxlabs.models.lng.fleet.ui.inlineeditors.TextualVesselReferenceInlineEditor;
import com.mmxlabs.models.lng.port.ui.editorpart.TextualPortReferenceInlineEditor;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.date.LocalDateTextFormatter;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.impl.LocalDateInlineEditor;
import com.mmxlabs.models.ui.editors.impl.MultiTextInlineEditor;
import com.mmxlabs.models.ui.editors.impl.PermissiveRestrictiveInlineEditor;
import com.mmxlabs.models.ui.editors.impl.TextInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultComponentHelper;

/**
 * A component helper for Slot instances
 * 
 * @generated NOT
 */
public class SlotComponentHelper extends DefaultComponentHelper {

	public SlotComponentHelper() {
		super(CargoPackage.Literals.SLOT);

		ignoreFeatures.add(CargoPackage.Literals.SLOT__CARGO);

		// These are hidden as they are linked to another control
		ignoreFeatures.add(CargoPackage.Literals.SLOT__RESTRICTED_CONTRACTS_OVERRIDE);
		ignoreFeatures.add(CargoPackage.Literals.SLOT__RESTRICTED_PORTS_OVERRIDE);
		ignoreFeatures.add(CargoPackage.Literals.SLOT__RESTRICTED_VESSELS_OVERRIDE);
		ignoreFeatures.add(CargoPackage.Literals.SLOT__ALLOWED_PORTS_OVERRIDE);
		ignoreFeatures.add(CargoPackage.Literals.SLOT__COMPUTE_EXPOSURE);
		ignoreFeatures.add(CargoPackage.Literals.SLOT__COMPUTE_HEDGE);

		addEditor(CargoPackage.Literals.SLOT__WINDOW_START, topClass -> {
			if (topClass.getEAllSuperTypes().contains(CargoPackage.eINSTANCE.getSpotSlot())) {
				// For spot slots, only allow month portion to be edited
				return new LocalDateInlineEditor(CargoPackage.eINSTANCE.getSlot_WindowStart(), new LocalDateTextFormatter("MM/yyyy"));
			} else {
				return ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.SLOT__WINDOW_START);
			}
		});

		addDefaultEditorWithWrapper(CargoPackage.Literals.SLOT__PRICE_EXPRESSION, SlotExpressionWrapper::new);

		addEditor(CargoPackage.Literals.SLOT__NOTES, topClass -> new MultiTextInlineEditor(CargoPackage.Literals.SLOT__NOTES));

		addEditor(CargoPackage.Literals.SLOT__RESTRICTED_CONTRACTS,
				topClass -> new RestrictionsOverrideMultiReferenceInlineEditor(CargoPackage.Literals.SLOT__RESTRICTED_CONTRACTS, CargoPackage.Literals.SLOT__RESTRICTED_CONTRACTS_OVERRIDE));
		addEditor(CargoPackage.Literals.SLOT__RESTRICTED_VESSELS,
				topClass -> new RestrictionsOverrideMultiReferenceInlineEditor(CargoPackage.Literals.SLOT__RESTRICTED_VESSELS, CargoPackage.Literals.SLOT__RESTRICTED_VESSELS_OVERRIDE));
		addEditor(CargoPackage.Literals.SLOT__RESTRICTED_PORTS,
				topClass -> new RestrictionsOverrideMultiReferenceInlineEditor(CargoPackage.Literals.SLOT__RESTRICTED_PORTS, CargoPackage.Literals.SLOT__RESTRICTED_PORTS_OVERRIDE));
		addEditor(CargoPackage.Literals.SLOT__RESTRICTED_PORTS,
				topClass -> new RestrictionsOverrideMultiReferenceInlineEditor(CargoPackage.Literals.SLOT__RESTRICTED_PORTS, CargoPackage.Literals.SLOT__RESTRICTED_PORTS_OVERRIDE));

		addEditor(CargoPackage.Literals.SLOT__RESTRICTED_CONTRACTS_ARE_PERMISSIVE, topClass -> new PermissiveRestrictiveInlineEditor(CargoPackage.Literals.SLOT__RESTRICTED_CONTRACTS_ARE_PERMISSIVE));
		addEditor(CargoPackage.Literals.SLOT__RESTRICTED_PORTS_ARE_PERMISSIVE, topClass -> new PermissiveRestrictiveInlineEditor(CargoPackage.Literals.SLOT__RESTRICTED_PORTS_ARE_PERMISSIVE));
		addEditor(CargoPackage.Literals.SLOT__RESTRICTED_VESSELS_ARE_PERMISSIVE, topClass -> new PermissiveRestrictiveInlineEditor(CargoPackage.Literals.SLOT__RESTRICTED_VESSELS_ARE_PERMISSIVE));

		addEditor(CargoPackage.Literals.SLOT__RESTRICTED_SLOTS_ARE_PERMISSIVE, topClass -> new PermissiveRestrictiveInlineEditor(CargoPackage.Literals.SLOT__RESTRICTED_SLOTS_ARE_PERMISSIVE));

		addEditor(CargoPackage.Literals.SLOT__NOMINATED_VESSEL, topClass -> new NominatedVesselEditorWrapper(new TextualVesselReferenceInlineEditor(CargoPackage.Literals.SLOT__NOMINATED_VESSEL)));

		addEditor(CargoPackage.Literals.SLOT__CONTRACT, topClass -> {
			if (!CargoPackage.Literals.SPOT_SLOT.isSuperTypeOf(topClass)) {
				return ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.SLOT__CONTRACT);
			}
			return null;

		});
		addEditor(CargoPackage.Literals.SLOT__PORT, topClass -> new TextualPortReferenceInlineEditor(CargoPackage.Literals.SLOT__PORT));
		addEditor(CargoPackage.Literals.SLOT__CN, topClass -> new TextInlineEditor(CargoPackage.Literals.SLOT__CN));
		addEditor(CargoPackage.Literals.SLOT__MAX_QUANTITY, topClass -> new VolumeInlineEditor(CargoPackage.Literals.SLOT__MAX_QUANTITY));

		addEditor(CargoPackage.Literals.SLOT__SHIPPING_DAYS_RESTRICTION, topClass -> {
			final IInlineEditor editor = ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.SLOT__SHIPPING_DAYS_RESTRICTION);
			editor.addNotificationChangedListener(new ShippingDaysRestrictionInlineEditorChangedListener());
			return editor;
		});

		addEditor(CargoPackage.Literals.SLOT__PRICING_EVENT, topClass -> {
			final IInlineEditor editor = ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.SLOT__PRICING_EVENT);
			editor.addNotificationChangedListener(new PricingEventInlineEditorChangedListener());
			return editor;
		});
	}
}