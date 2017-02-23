/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.lso;

import java.util.Collection;
import java.util.Map;
import java.util.Random;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.mmxlabs.common.util.RouletteWheel;
import com.mmxlabs.optimiser.common.components.ILookupManager;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.moves.IMove;
import com.mmxlabs.optimiser.lso.IMoveGenerator;
import com.mmxlabs.optimiser.lso.impl.NullMove;
import com.mmxlabs.scheduler.optimiser.lso.moves.MoveMapper;
import com.mmxlabs.scheduler.optimiser.lso.moves.MoveTypes;
import com.mmxlabs.scheduler.optimiser.moves.handlers.ShuffleElementsMoveHandler;

/**
 * <p>
 * A move generator which tries to create moves that respect timed constraints, to avoid wasting too many cycles testing clearly impossible moves. Suggested in ticket #9.
 * </p>
 * <ol>
 * The moves generated should try to respect the following aspects, where possible.
 * <li>Time Windows + Voyage Durations</li>
 * <li>Port visit durations</li>
 * <li>Port restrictions</li>
 * <li>Load / Discharge ordering</li>
 * <li>Fixed load/discharge pairs</li>
 * <li>Vessel events</li>
 * <li>Vessel or class assignments</li>
 * </ol>
 * 
 * @author NS
 * 
 * @param
 */
public class RouletteWheelMoveGenerator implements IMoveGenerator {

	public static final String MOVE_DISTRIBUTION = "moveDistributions";
	public static final String EQUAL_DISTRIBUTION = "equalDistributions";

	@Inject
	private MoveMapper moveMapper;

	private RouletteWheel<MoveTypes> rouletteWheel;

	@Inject
	@Named(MOVE_DISTRIBUTION)
	private Map<String, Double> distribution;

	@Inject
	@Named(EQUAL_DISTRIBUTION)
	private boolean equalDistributions;

	@Inject
	public void generateRouletteWheel() {
		@NonNull
		final Collection<@NonNull MoveTypes> validGenerators = moveMapper.getSupportedMoveTypes();
		rouletteWheel = new RouletteWheel(validGenerators, distribution, equalDistributions);
	}

	@Override
	public @Nullable IMove generateMove(@NonNull ISequences rawSequences, @NonNull ILookupManager stateManager, @NonNull Random random) {
		final IMoveGenerator currentGenerator = moveMapper.getMoveHandler(rouletteWheel.spin(random));
		if (!(currentGenerator instanceof ShuffleElementsMoveHandler)) {

			int count = 1;
			IMove move = currentGenerator.generateMove(rawSequences, stateManager, random);

			while ((move instanceof NullMove || move == null) && count > 0) {
				move = currentGenerator.generateMove(rawSequences, stateManager, random);
				count--;
			}
			return move;
		} else {
			return currentGenerator.generateMove(rawSequences, stateManager, random);
		}

	}

}