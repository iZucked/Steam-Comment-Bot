/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo.presentation.composites;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.editor.PaperDealTypeExtensionWrapper;
import com.mmxlabs.models.ui.impl.DefaultComponentHelper;

/**
 * A component helper for PaperDeal instances
 *
 * @generated NOT
 */
public class PaperDealComponentHelper extends DefaultComponentHelper {

	public PaperDealComponentHelper() {
		super(CargoPackage.Literals.PAPER_DEAL);

		addDefaultEditorWithWrapper(CargoPackage.Literals.PAPER_DEAL__PRICING_MONTH, PaperDealTypeExtensionWrapper::new);
		addDefaultEditorWithWrapper(CargoPackage.Literals.PAPER_DEAL__START_DATE, PaperDealTypeExtensionWrapper::new);
		addDefaultEditorWithWrapper(CargoPackage.Literals.PAPER_DEAL__END_DATE, PaperDealTypeExtensionWrapper::new);

	}
}