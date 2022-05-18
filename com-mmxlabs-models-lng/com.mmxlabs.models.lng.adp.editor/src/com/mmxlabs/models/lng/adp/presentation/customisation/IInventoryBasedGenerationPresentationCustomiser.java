package com.mmxlabs.models.lng.adp.presentation.customisation;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public interface IInventoryBasedGenerationPresentationCustomiser {
	String getDropDownMenuLabel();

	boolean showSummaryPane();
}
