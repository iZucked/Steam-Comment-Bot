/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.license.features.internal;

import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

@ExtensionBean("com.mmxlabs.license.features.FeatureEnablement")
public interface FeatureEnablementExtension {

	@MapName("feature")
	String getFeature();
}
