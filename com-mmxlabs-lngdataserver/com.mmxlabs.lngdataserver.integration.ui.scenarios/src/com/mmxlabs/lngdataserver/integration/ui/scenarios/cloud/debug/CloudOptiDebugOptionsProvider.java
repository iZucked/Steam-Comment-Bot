package com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud.debug;

import java.util.EnumSet;

import org.eclipse.osgi.service.debug.DebugOptions;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.rcp.common.debug.DebugOptionsLevel;
import com.mmxlabs.rcp.common.debug.IDebugOptionsProvider;

public class CloudOptiDebugOptionsProvider implements IDebugOptionsProvider {

	@Override
	public String getName() {
		return "Cloud Optimisation";
	}

	@Override
	public String getDescription() {
		return "Enable debug logging for sending and receiving cloud optimisations";
	}

	@Override
	public boolean isAvailable() {
		return LicenseFeatures.isPermitted(KnownFeatures.FEATURE_CLOUD_OPTIMISATION);
	}

	@Override
	public EnumSet<DebugOptionsLevel> getSupportedLevels() {
		return EnumSet.of(DebugOptionsLevel.NONE, DebugOptionsLevel.FULL);
	}

	@Override
	public void apply(final DebugOptions debugOptions, final DebugOptionsLevel lvl) {
		if (lvl == DebugOptionsLevel.PARTIAL) {
			// Probably means configured via .options file - ignore 
			return;
		}
		
		// Enable logging if needed
		if (lvl != DebugOptionsLevel.NONE && !debugOptions.isDebugEnabled()) {
			debugOptions.setDebugEnabled(true);
		}
		final String state = Boolean.toString(lvl != DebugOptionsLevel.NONE);
		debugOptions.setOption(CloudOptiDebugContants.DEBUG_DOWNLOAD, state);
		debugOptions.setOption(CloudOptiDebugContants.DEBUG_IMPORT, state);
		debugOptions.setOption(CloudOptiDebugContants.DEBUG_POLL, state);

	}

	@Override
	public DebugOptionsLevel getCurrentLevel(final DebugOptions debugOptions) {

		if (debugOptions.isDebugEnabled()) {
			DebugOptionsLevel lvl = DebugOptionsLevel.NONE;
			int count = 0;
			if (debugOptions.getBooleanOption(CloudOptiDebugContants.DEBUG_DOWNLOAD, false)) {
				++count;
			}
			if (debugOptions.getBooleanOption(CloudOptiDebugContants.DEBUG_IMPORT, false)) {
				++count;
			}
			if (debugOptions.getBooleanOption(CloudOptiDebugContants.DEBUG_POLL, false)) {
				++count;
			}
			if (count > 0) {
				lvl = DebugOptionsLevel.PARTIAL;
			}
			if (count == 3) {
				lvl = DebugOptionsLevel.FULL;
			}
			return lvl;
		}

		return DebugOptionsLevel.NONE;
	}

}
