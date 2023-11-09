/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.debug;

import java.util.EnumSet;

import org.eclipse.osgi.service.debug.DebugOptions;

import com.mmxlabs.rcp.common.debug.DebugOptionsLevel;
import com.mmxlabs.rcp.common.debug.IDebugOptionsProvider;

public class BaseCaseDebugOptionsProvider implements IDebugOptionsProvider {

	@Override
	public String getName() {
		return "Base Case synchronisation";
	}

	@Override
	public String getDescription() {
		return "Enable debug logging for synchronising the base case";
	}

	@Override
	public boolean isAvailable() {
		return true;
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
		debugOptions.setOption(BaseCaseDebugContants.DEBUG_DOWNLOAD, state);
		debugOptions.setOption(BaseCaseDebugContants.DEBUG_UPLOAD, state);
		debugOptions.setOption(BaseCaseDebugContants.DEBUG_POLL, state);

	}

	@Override
	public DebugOptionsLevel getCurrentLevel(final DebugOptions debugOptions) {

		if (debugOptions.isDebugEnabled()) {
			DebugOptionsLevel lvl = DebugOptionsLevel.OFF;
			int count = 0;
			if (debugOptions.getBooleanOption(BaseCaseDebugContants.DEBUG_DOWNLOAD, false)) {
				++count;
			}
			if (debugOptions.getBooleanOption(BaseCaseDebugContants.DEBUG_UPLOAD, false)) {
				++count;
			}
			if (debugOptions.getBooleanOption(BaseCaseDebugContants.DEBUG_POLL, false)) {
				++count;
			}
			if (count > 0) {
				lvl = DebugOptionsLevel.PARTIAL;
			}
			if (count == 3) {
				lvl = DebugOptionsLevel.ON;
			}
			return lvl;
		}

		return DebugOptionsLevel.OFF;
	}

}
