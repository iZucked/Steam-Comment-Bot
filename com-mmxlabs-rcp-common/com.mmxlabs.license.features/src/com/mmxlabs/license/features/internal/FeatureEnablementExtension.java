/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.license.features.internal;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.ops4j.peaberry.eclipse.ExtensionBean;
import org.ops4j.peaberry.eclipse.MapName;

@ExtensionBean("com.mmxlabs.license.features.FeatureEnablement")
public interface FeatureEnablementExtension {

	@MapName("feature")
	@NonNull
	String getFeature();

	@MapName("expiry-date")
	@Nullable
	String getExpiryDate();
}
