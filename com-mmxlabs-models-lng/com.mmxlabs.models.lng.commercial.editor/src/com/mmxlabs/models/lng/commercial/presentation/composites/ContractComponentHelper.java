/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.presentation.composites;

import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.ui.displaycomposites.DivertibleContractInlineEditorChangedListener;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.dates.MonthInlineEditor;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.impl.MultiTextInlineEditor;
import com.mmxlabs.models.ui.editors.impl.PermissiveRestrictiveInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultComponentHelper;

/**
 * A component helper for Contract instances
 *
 */
public class ContractComponentHelper extends DefaultComponentHelper {

	public ContractComponentHelper() {
		super(CommercialPackage.Literals.CONTRACT);

		addEditor(CommercialPackage.Literals.CONTRACT__NOTES, topClass -> new MultiTextInlineEditor(CommercialPackage.Literals.CONTRACT__NOTES));

		addEditor(CommercialPackage.Literals.CONTRACT__CONTRACT_YEAR_START, topClass -> new MonthInlineEditor(CommercialPackage.Literals.CONTRACT__CONTRACT_YEAR_START));

		addEditor(CommercialPackage.Literals.CONTRACT__RESTRICTED_PORTS_ARE_PERMISSIVE,
				topClass -> new PermissiveRestrictiveInlineEditor(CommercialPackage.Literals.CONTRACT__RESTRICTED_PORTS_ARE_PERMISSIVE));
		addEditor(CommercialPackage.Literals.CONTRACT__RESTRICTED_VESSELS_ARE_PERMISSIVE,
				topClass -> new PermissiveRestrictiveInlineEditor(CommercialPackage.Literals.CONTRACT__RESTRICTED_VESSELS_ARE_PERMISSIVE));
		addEditor(CommercialPackage.Literals.CONTRACT__RESTRICTED_CONTRACTS_ARE_PERMISSIVE,
				topClass -> new PermissiveRestrictiveInlineEditor(CommercialPackage.Literals.CONTRACT__RESTRICTED_CONTRACTS_ARE_PERMISSIVE));

		addDefaultEditorForLicenseFeature("features:adp", CommercialPackage.Literals.CONTRACT__CODE);

		addEditor(CommercialPackage.Literals.CONTRACT__SHIPPING_DAYS_RESTRICTION, topClass -> {
			final IInlineEditor editor = ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.CONTRACT__SHIPPING_DAYS_RESTRICTION);
			editor.addNotificationChangedListener(new DivertibleContractInlineEditorChangedListener());
			return editor;
		});

	}
}