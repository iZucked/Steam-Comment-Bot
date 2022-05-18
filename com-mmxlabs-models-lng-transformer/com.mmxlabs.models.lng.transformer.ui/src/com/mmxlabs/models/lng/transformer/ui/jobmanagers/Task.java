/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.jobmanagers;

import java.util.function.BiConsumer;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.JobDataRecord;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.ui.IProgressProvider;

/**
 * Representation of a running optimisation
 * 
 *
 */
public class Task {

	public IProgressProvider.RunType runType;

	public JobDataRecord job;

	public ScenarioModelRecord modelRecord;
	public IScenarioDataProvider sdp;
	/**
	 * A {@link Supplier} to provider the serialised parameters to run the task. This may prompt the user for choices.
	 */
	public Supplier<String> parametersFactory;

	/**
	 * A {@link Supplier} to run the validation required for the task. This may prompt the user. Returns true if there were no errors or the user said to proceed with warnings.
	 */
	public BooleanSupplier validationFactory;
	/**
	 * Called on successful completion of an optimisation. Handler is responsible for attaching the result to the data model
	 */
	public BiConsumer<IScenarioDataProvider, AbstractSolutionSet> successHandler;

	/**
	 * Called on when there is an exception. Expected to update task state,
	 */
	public BiConsumer<String, Exception> errorHandler;

	public TaskStatus taskStatus = TaskStatus.submitted();

	/**
	 * Remote job ID - just for debugging cloud tasks with manager view
	 */
	public String remoteJobID;

}