/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo.presentation.composites;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.ui.editors.impl.MultiTextInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultComponentHelper;

/**
 * A component helper for PaperDeal instances
 *
 * @generated NOT
 */
public class PaperDealComponentHelper extends DefaultComponentHelper {
	
	private static final CargoPackage cp = CargoPackage.eINSTANCE;

	public PaperDealComponentHelper() {
		super(cp.getPaperDeal());
		
		ignoreFeatures.add(cp.getPaperDeal_PricingCalendar());
		
		addEditor(cp.getPaperDeal_Comment(), topClass -> new MultiTextInlineEditor(cp.getPaperDeal_Comment()));
	}
}