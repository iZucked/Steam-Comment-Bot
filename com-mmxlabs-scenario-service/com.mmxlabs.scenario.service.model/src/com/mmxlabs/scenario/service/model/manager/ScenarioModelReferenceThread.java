/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.manager;

import java.util.function.Consumer;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

/**
 * A thread subclass to keep a {@link ModelReference} open during the lifetime of the {@link Thread}. Once {@link #run()} completes, the reference is released.
 * 
 * @author Simon Goodall
 *
 */
@NonNullByDefault
public class ScenarioModelReferenceThread extends Thread {
	private @Nullable Consumer<IScenarioDataProvider> action;
	private final IScenarioDataProvider scenarioDataProvider;

	public ScenarioModelReferenceThread(final String name, final ScenarioModelRecord modelRecord, final Consumer<IScenarioDataProvider> action) {
		super(createThreadGroup(name), name);
		this.scenarioDataProvider = modelRecord.aquireScenarioDataProvider("ModelReferenceThread:1");
		this.action = action;
	}

	@Override
	public void run() {

		try {
			if (action != null) {
				action.accept(scenarioDataProvider);
			}
		} finally {
			// Null action to release memory
			action = null;
			scenarioDataProvider.close();
		}
	}

	private static ThreadGroup createThreadGroup(final String name) {
		final ThreadGroup threadGroup = new ThreadGroup(name);
		// Set daemon flag to auto-clean up when all child threads exit
		threadGroup.setDaemon(true);
		return threadGroup;
	}
}
