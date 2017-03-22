/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.headless;

import java.util.Map;

import javax.inject.Singleton;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mmxlabs.optimiser.lso.IMoveGenerator;
import com.mmxlabs.scheduler.optimiser.lso.RouletteWheelMoveGenerator;
import com.mmxlabs.scheduler.optimiser.lso.moves.MoveMapper;
import com.mmxlabs.scheduler.optimiser.moves.util.MoveHelper;

public class LNGOptimsationOverrideModule extends AbstractModule {

	private final SettingsOverride settings;

	public LNGOptimsationOverrideModule(final SettingsOverride settings) {
		this.settings = settings;
	}

	@Override
	protected void configure() {
		if (settings.isUseRouletteWheel()) {
			bind(RouletteWheelMoveGenerator.class).in(Singleton.class);
			bind(IMoveGenerator.class).to(RouletteWheelMoveGenerator.class);
		}
	}

	@Provides
	@Named(RouletteWheelMoveGenerator.EQUAL_DISTRIBUTION)
	private boolean isEqualDistributions() {
		return settings.isEqualMoveDistributions();
	}

	@Provides
	@Named(RouletteWheelMoveGenerator.MOVE_DISTRIBUTION)
	private Map<String, Double> getMoveDistributions() {
		return settings.getMoveMap();
	}

	@Provides
	@Named(MoveHelper.LEGACY_CHECK_RESOURCE)
	private boolean useLegacyCheck() {
		return settings.useLegacyCheck();
	}

	@Provides
	@Named(MoveMapper.USE_GUIDED_MOVES)
	private boolean useguidedMoves() {
		return settings.useGuidedMoves();
	}

}
