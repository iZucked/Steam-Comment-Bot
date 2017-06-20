/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.ext;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.commercial.CommercialModel;

public interface IADPProfileProvider {

	void createProfiles(@NonNull CommercialModel commercialModelModel, @NonNull ADPModel existingModel);

}
