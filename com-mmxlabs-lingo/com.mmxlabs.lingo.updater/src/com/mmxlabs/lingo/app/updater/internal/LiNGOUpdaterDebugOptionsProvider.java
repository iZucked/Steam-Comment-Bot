/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.updater.internal;

import java.util.EnumSet;

import org.eclipse.osgi.service.debug.DebugOptions;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.rcp.common.debug.DebugOptionsLevel;
import com.mmxlabs.rcp.common.debug.IDebugOptionsProvider;

public class LiNGOUpdaterDebugOptionsProvider implements IDebugOptionsProvider {

	@Override
	public String getName() {
		return "LiNGO Update";
	}

	@Override
	public String getDescription() {
		return "Enable debug logging for beta LiNGO updater";
	}

	@Override
	public boolean isAvailable() {
		return LicenseFeatures.isPermitted("features:lingo-updater");
	}

	@Override
	public EnumSet<DebugOptionsLevel> getSupportedLevels() {
		return EnumSet.of(DebugOptionsLevel.OFF, DebugOptionsLevel.ON);
	}

	@Override
	public void apply(final DebugOptions debugOptions, final DebugOptionsLevel lvl) {
		if (lvl == DebugOptionsLevel.PARTIAL) {
			// Probably means configured via .options file - ignore
			return;
		}

		// Enable logging if needed
		if (lvl != DebugOptionsLevel.OFF && !debugOptions.isDebugEnabled()) {
			debugOptions.setDebugEnabled(true);
		}
		final String state = Boolean.toString(lvl != DebugOptionsLevel.OFF);
		debugOptions.setOption(LiNGOUpdaterDebugContants.DEBUG_DOWNLOAD, state);

	}

	@Override
	public DebugOptionsLevel getCurrentLevel(final DebugOptions debugOptions) {

		if (debugOptions.isDebugEnabled()) {
			DebugOptionsLevel lvl = DebugOptionsLevel.OFF;
			int count = 0;
			if (debugOptions.getBooleanOption(LiNGOUpdaterDebugContants.DEBUG_DOWNLOAD, false)) {
				++count;
			}

			if (count > 0) {
				lvl = DebugOptionsLevel.PARTIAL;
			}
			if (count == 1) {
				lvl = DebugOptionsLevel.ON;
			}
			return lvl;
		}

		return DebugOptionsLevel.OFF;
	}

}
