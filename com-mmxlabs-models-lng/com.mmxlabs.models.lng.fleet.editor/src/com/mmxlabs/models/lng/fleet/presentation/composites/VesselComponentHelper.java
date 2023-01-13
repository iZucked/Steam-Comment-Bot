/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.presentation.composites;

import java.util.LinkedList;
import java.util.List;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.ui.inlineeditors.ReferenceVesselInlineEditor;
import com.mmxlabs.models.lng.fleet.ui.inlineeditors.RouteExclusionMultiInlineEditor;
import com.mmxlabs.models.lng.fleet.ui.inlineeditors.VesselFillVolumeInlineEditor;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.impl.MultiTextInlineEditor;
import com.mmxlabs.models.ui.editors.impl.YesNoInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultComponentHelper;

/**
 * A component helper for Vessel instances
 *
 * @generated NOT
 */
public class VesselComponentHelper extends DefaultComponentHelper {

	public VesselComponentHelper() {
		super(FleetPackage.Literals.VESSEL);

		ignoreFeatures.add(FleetPackage.Literals.VESSEL__MMX_ID);
		ignoreFeatures.add(FleetPackage.Literals.VESSEL__MMX_REFERENCE);
		ignoreFeatures.add(FleetPackage.Literals.VESSEL__INACCESSIBLE_ROUTES_OVERRIDE);
		ignoreFeatures.add(FleetPackage.Literals.VESSEL__INACCESSIBLE_PORTS_OVERRIDE);
		ignoreFeatures.add(FleetPackage.Literals.VESSEL__ROUTE_PARAMETERS_OVERRIDE);
		ignoreFeatures.add(FleetPackage.Literals.VESSEL__HAS_RELIQ_CAPABILITY_OVERRIDE);

		ignoreFeatures.add(FleetPackage.Literals.VESSEL__FILL_CAPACITY);
		editorFactories.put(FleetPackage.Literals.VESSEL__CAPACITY, topClass -> {
			List<IInlineEditor> editors = new LinkedList<>();
			editors.add(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL__CAPACITY));
			editors.add(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL__FILL_CAPACITY));
			editors.add(new VesselFillVolumeInlineEditor(FleetPackage.Literals.VESSEL__CAPACITY));
			return editors;
		});

		addEditor(FleetPackage.Literals.VESSEL__REFERENCE, topClass -> new ReferenceVesselInlineEditor(ComponentHelperUtils.createDefaultEditor(topClass, FleetPackage.Literals.VESSEL__REFERENCE)));
		addEditor(FleetPackage.Literals.VESSEL__NOTES, topClass -> new MultiTextInlineEditor(FleetPackage.Literals.VESSEL__NOTES));
		addEditor(FleetPackage.Literals.VESSEL__INACCESSIBLE_ROUTES, topClass -> new RouteExclusionMultiInlineEditor(FleetPackage.Literals.VESSEL__INACCESSIBLE_ROUTES));

		// License based editors
		addDefaultEditorForLicenseFeature(KnownFeatures.FEATURE_PURGE, FleetPackage.Literals.VESSEL__PURGE_TIME);
		addDefaultEditorForLicenseFeature(KnownFeatures.FEATURE_PURGE, FleetPackage.Literals.VESSEL__COOLING_TIME);
		addDefaultEditorForLicenseFeature(KnownFeatures.FEATURE_PURGE, FleetPackage.Literals.VESSEL__PURGE_VOLUME);
		addDefaultEditorForLicenseFeature("features:min-base-fuel-support", FleetPackage.Literals.VESSEL__MIN_BASE_FUEL_CONSUMPTION);

		addEditorForLicenseFeature("features:reliq-support", FleetPackage.Literals.VESSEL__HAS_RELIQ_CAPABILITY, topClass -> new YesNoInlineEditor(FleetPackage.Literals.VESSEL__HAS_RELIQ_CAPABILITY));
	}

	@Override
	protected void sortEditors(List<IInlineEditor> editors) {
		// After the reference vessel field and before the capacity field
		sortEditorBeforeOtherEditor(editors, FleetPackage.Literals.VESSEL__REFERENCE_VESSEL, FleetPackage.Literals.VESSEL__CAPACITY);
		sortEditorBeforeOtherEditor(editors, FleetPackage.Literals.VESSEL__MARKER, FleetPackage.Literals.VESSEL__CAPACITY);

	}
}