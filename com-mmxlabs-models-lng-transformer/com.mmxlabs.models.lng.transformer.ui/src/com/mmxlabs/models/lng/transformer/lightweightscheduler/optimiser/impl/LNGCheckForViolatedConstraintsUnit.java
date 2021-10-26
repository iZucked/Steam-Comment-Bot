/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.lightweightscheduler.optimiser.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Injector;
import com.google.inject.Module;
import com.mmxlabs.common.util.exceptions.UserFeedbackException;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.ProfileConstraint;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.chain.ChainBuilder;
import com.mmxlabs.models.lng.transformer.chain.IChainLink;
import com.mmxlabs.models.lng.transformer.chain.ILNGStateTransformerUnit;
import com.mmxlabs.models.lng.transformer.chain.SequencesContainer;
import com.mmxlabs.models.lng.transformer.chain.impl.InitialSequencesModule;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.inject.LNGTransformerHelper;
import com.mmxlabs.models.lng.transformer.inject.modules.InitialPhaseOptimisationDataModule;
import com.mmxlabs.models.lng.transformer.inject.modules.InputSequencesModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGEvaluationModule;
import com.mmxlabs.models.lng.transformer.inject.modules.LNGParameters_EvaluationSettingsModule;
import com.mmxlabs.optimiser.core.IMultiStateResult;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.MultiStateResult;
import com.mmxlabs.optimiser.core.inject.scopes.ThreadLocalScopeImpl;
import com.mmxlabs.scheduler.optimiser.explainer.CheckForViolatedConstraints;
import com.mmxlabs.scheduler.optimiser.peaberry.IOptimiserInjectorService;
import com.mmxlabs.scheduler.optimiser.providers.ConstraintInfo;

public class LNGCheckForViolatedConstraintsUnit implements ILNGStateTransformerUnit {

	private static final Logger LOG = LoggerFactory.getLogger(LNGCheckForViolatedConstraintsUnit.class);

	@NonNull
	public static IChainLink chain(@NonNull final ChainBuilder chainBuilder, @NonNull final UserSettings userSettings, final int progressTicks) {
		final IChainLink link = new IChainLink() {

			@Override
			public IMultiStateResult run(final SequencesContainer initialSequences, final IMultiStateResult inputState, final IProgressMonitor monitor) {
				final LNGDataTransformer dt = chainBuilder.getDataTransformer();
				LNGCheckForViolatedConstraintsUnit t = new LNGCheckForViolatedConstraintsUnit(dt, userSettings, initialSequences.getSequences(), inputState.getBestSolution().getFirst(),
						dt.getHints());
				return t.run(monitor);
			}

			@Override
			public int getProgressTicks() {
				return progressTicks;
			}
		};
		chainBuilder.addLink(link);
		return link;
	}

	@NonNull
	private final LNGDataTransformer dataTransformer;

	@NonNull
	private final Injector injector;

	@NonNull
	private final CheckForViolatedConstraints violatedConstraintChecker;

	@NonNull
	private final IMultiStateResult inputState;

	public LNGCheckForViolatedConstraintsUnit(@NonNull final LNGDataTransformer dataTransformer, @NonNull final UserSettings userSettings, @NonNull ISequences initialSequences,
			@NonNull final ISequences inputSequences, @NonNull final Collection<@NonNull String> hints) {
		this.dataTransformer = dataTransformer;

		final Collection<@NonNull IOptimiserInjectorService> services = dataTransformer.getModuleServices();
		
		final List<Module> modules = new LinkedList<>();

		modules.add(new InitialSequencesModule(initialSequences));
		modules.add(new InputSequencesModule(inputSequences));
		modules.add(new InitialPhaseOptimisationDataModule());
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(
				new LNGParameters_EvaluationSettingsModule(dataTransformer.getUserSettings(), dataTransformer.getSolutionBuilderSettings().getConstraintAndFitnessSettings()), services,
				IOptimiserInjectorService.ModuleType.Module_EvaluationParametersModule, hints));
		modules.addAll(LNGTransformerHelper.getModulesWithOverrides(new LNGEvaluationModule(hints), services, IOptimiserInjectorService.ModuleType.Module_Evaluation, hints));
		injector = dataTransformer.getInjector().createChildInjector(modules);
		violatedConstraintChecker = injector.getInstance(CheckForViolatedConstraints.class);
		inputState = new MultiStateResult(inputSequences, new HashMap<>());
	}

	@NonNull
	public LNGDataTransformer getDataTransformer() {
		return dataTransformer;
	}

	@NonNull
	public Injector getInjector() {
		return injector;
	}

	@Override
	public IMultiStateResult run(@NonNull final IProgressMonitor monitor) {
		try (ThreadLocalScopeImpl scope = injector.getInstance(ThreadLocalScopeImpl.class)) {
			scope.enter();
			monitor.beginTask("", 1);
			try {
				ISequences newResult = violatedConstraintChecker.run(inputState.getBestSolution().getFirst());
				return new MultiStateResult(newResult, new HashMap<>());
			} 
			catch (UserFeedbackException ufe) {
				if (ufe.getAdditionalInfo() != null) {
					List<Object> cis = ufe.getAdditionalInfo();
					if (cis != null) {
						String errorDetails = getErrorMessageDetails(cis);
						String errorMsg = ufe.getMessage()+errorDetails;
						throw new UserFeedbackException(errorMsg);
					}
				}
				throw ufe;
			}
			finally {
				monitor.done();
			}
		}
	}
	
	//Has to be here to avoid circular dependency since optimiser code cannot know about EMF.
	private String getErrorMessageDetails(List<Object> cis) {
		StringBuilder errorMessage = new StringBuilder();
		
		List<ConstraintInfo<ContractProfile, ProfileConstraint,?>> failedConstraintInfos = new ArrayList<>();
		for (var ci : cis) {
			if (ci instanceof ConstraintInfo<?,?,?> 
			&& ((ConstraintInfo) ci).getContractProfile() instanceof ContractProfile 
			&& ((ConstraintInfo) ci).getProfileConstraint() instanceof ProfileConstraint) {
				failedConstraintInfos.add((ConstraintInfo<ContractProfile, ProfileConstraint,?>)ci);
			}
		}
		
		ModelEntityMap mem = this.injector.getInstance(ModelEntityMap.class);
		LightWeightOptimisationDataFactory.addViolatedConstraintDetails(mem, failedConstraintInfos, errorMessage);
		
		return errorMessage.toString();
	}
}
