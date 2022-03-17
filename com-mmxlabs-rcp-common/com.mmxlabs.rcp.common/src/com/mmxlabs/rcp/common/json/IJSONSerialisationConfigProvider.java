/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.json;

import org.eclipse.emf.ecore.EStructuralFeature;

public interface IJSONSerialisationConfigProvider {
	boolean isFeatureIgnored(EStructuralFeature feature);
}
