/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.lso;

import java.util.Random;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Injector;
import com.google.inject.Provider;
import com.mmxlabs.optimiser.common.components.ILookupManager;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.moves.IMove;
import com.mmxlabs.optimiser.core.scenario.IPhaseOptimisationData;
import com.mmxlabs.optimiser.lso.IMoveGenerator;
import com.mmxlabs.scheduler.optimiser.lso.guided.GuidedMoveGenerator;
import com.mmxlabs.scheduler.optimiser.lso.guided.GuidedMoveHandlerWrapper;
import com.mmxlabs.scheduler.optimiser.lso.guided.GuidedMoveTypes;
import com.mmxlabs.scheduler.optimiser.lso.guided.MoveTypesAnnotation;
import com.mmxlabs.scheduler.optimiser.moves.handlers.ShuffleElementsMoveHandler;
import com.mmxlabs.scheduler.optimiser.providers.IPromptPeriodProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

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
 * @author hinton
 * 
 * @param
 */
public class ConstrainedMoveGenerator implements IMoveGenerator {
	/**
	 * The proportion of moves which are related to optional elements
	 * 
	 * TODO make this a parameter.
	 */
	private static final double elementSwapMoveFrequency = 0.10;
	private static final double optionalMoveFrequency = 0.10;
	private static final double shuffleMoveFrequency = 0.20;
	private static final double swapMoveFrequency = 0.01;

	private SequencesConstrainedMoveGeneratorUnit sequencesMoveGenerator;
	private OptionalConstrainedMoveGeneratorUnit optionalMoveGenerator;
	private ShuffleElementsMoveHandler shuffleMoveGenerator;
	private SwapElementsInSequenceMoveGeneratorUnit swapElementsMoveGenerator;
	private ElementSwapMoveGenerator elementSwapMoveGenerator;

	@Inject
	private Provider<GuidedMoveGenerator> guidedMoveGenerator;

	// TODO: Inject
	private final boolean enableSwapElementsMoveGenerator = false;

	@com.google.inject.Inject(optional = true)
	private IPhaseOptimisationData phaseOptimisationData;

	@Inject
	private Injector injector;

	public static final String LSO_MOVES_SCMG = "CMG_LSOMovesSCGM";

	public static final String LSO_MOVES_UnusedAndNominalMove = "CMG_Enable_UnusedAndNominalMove";
	
	public static final String LSO_MOVES_GUIDED = "CMG_EnabledGuided";


	@com.google.inject.Inject(optional = true)
	@Named(LSO_MOVES_SCMG)
	private boolean isLoopingSCMG;

	@Inject
	@MoveTypesAnnotation(GuidedMoveTypes.Swap_Cargo_Vessel)
	private GuidedMoveHandlerWrapper moveCargoOrVesselEventHandler;

	@Inject
	private TargetedMoveGenerator targetedNominalAndUnusedMoveHandler;

	private boolean enableSwapCargoMove = true;

	@com.google.inject.Inject(optional = true)
	@Named(LSO_MOVES_UnusedAndNominalMove)
	private boolean enableNominalAndUnusedSlotMove = false;
	
	@com.google.inject.Inject(optional = true)
	@Named(LSO_MOVES_GUIDED)
	private boolean enableGuidedMoveGenerator = false;

 
	@Inject
	public void init(@Named(OptimiserConstants.SEQUENCE_TYPE_INPUT) final ISequences inputRawSequences) {

		// Enable new move if this is a full optimisation or a period greater than 6 months AND there is at least one nominal cargo.
		// if (!promptPeriodProvider.isPeriodOptimisation() //
		// || (promptPeriodProvider.getEndOfOptimisationPeriod() - promptPeriodProvider.getStartOfOptimisationPeriod() > 30 * 6 * 24)) {
		// boolean hasNominalCargoes = false;
		// for (final IResource r : inputRawSequences.getResources()) {
		// @NonNull
		// final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(r);
		// if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.ROUND_TRIP) {
		// final ISequence s = inputRawSequences.getSequence(r);
		// if (s.size() > 2) {
		// hasNominalCargoes = true;
		// break;
		// }
		// }
		// }
		// enableSwapCargoMove = hasNominalCargoes;
		// }

		if (isLoopingSCMG) {
			this.sequencesMoveGenerator = new SequencesConstrainedLoopingMoveGeneratorUnit();
		} else {
			this.sequencesMoveGenerator = new SequencesConstrainedMoveGeneratorUnit();
		}

		injector.injectMembers(sequencesMoveGenerator);

		this.shuffleMoveGenerator = new ShuffleElementsMoveHandler();
		injector.injectMembers(shuffleMoveGenerator);

		this.elementSwapMoveGenerator = new ElementSwapMoveGenerator();
		injector.injectMembers(elementSwapMoveGenerator);

		if (phaseOptimisationData != null) {
			if (!phaseOptimisationData.getOptionalElements().isEmpty()) {
				this.optionalMoveGenerator = new OptionalConstrainedMoveGeneratorUnit();
				injector.injectMembers(optionalMoveGenerator);
			} else {
				this.optionalMoveGenerator = null;
			}
		} else {
			this.optionalMoveGenerator = null;
		}
	}

	@Override
	public @Nullable IMove generateMove(@NonNull final ISequences rawSequences, @NonNull final ILookupManager stateManager, @NonNull final Random random) {
		if (enableGuidedMoveGenerator && random.nextDouble() < 0.01) {
			return guidedMoveGenerator.get().generateMove(rawSequences, stateManager, random);
		}
		if (enableNominalAndUnusedSlotMove && (random.nextDouble() < 0.05)) {
			return targetedNominalAndUnusedMoveHandler.generateMove(rawSequences, stateManager, random);
		}
		if (enableSwapCargoMove) {
			if (random.nextDouble() < 0.05) {
				return moveCargoOrVesselEventHandler.generateMove(rawSequences, stateManager, random);
			}
		}

		if (enableSwapElementsMoveGenerator && ((elementSwapMoveGenerator != null) && (random.nextDouble() < elementSwapMoveFrequency))) {
			return elementSwapMoveGenerator.generateMove(rawSequences, stateManager, random);
		} else if ((optionalMoveGenerator != null) && (random.nextDouble() < optionalMoveFrequency)) {
			return optionalMoveGenerator.generateMove(rawSequences, stateManager, random);
		} else if ((shuffleMoveGenerator != null) && (random.nextDouble() < shuffleMoveFrequency)) {
			return shuffleMoveGenerator.generateMove(rawSequences, stateManager, random);
		} else if ((swapElementsMoveGenerator != null) && (random.nextDouble() < swapMoveFrequency)) {
			return swapElementsMoveGenerator.generateMove(rawSequences, stateManager, random);
		} else {
			return sequencesMoveGenerator.generateMove(rawSequences, stateManager, random);
		}
	}
}