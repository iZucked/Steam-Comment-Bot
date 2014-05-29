/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.transformer;

import com.mmxlabs.models.lng.analytics.ProvisionalCargo;
import com.mmxlabs.models.lng.analytics.UnitCostLine;
import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * 
 * 
 */
public interface ICargoSandboxTransformer {

	UnitCostLine createCostLine(MMXRootObject root, final ProvisionalCargo provisionalCargo);
}
