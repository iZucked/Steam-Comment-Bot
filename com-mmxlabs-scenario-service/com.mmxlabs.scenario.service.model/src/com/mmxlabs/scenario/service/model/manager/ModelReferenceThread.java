/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
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
public class ModelReferenceThread extends Thread {
	private @Nullable Consumer<ModelReference> action;
	private final ModelReference modelReference;

	public ModelReferenceThread(final String name, final ModelRecord modelRecord, final Consumer<ModelReference> action) {
		super(name);
		this.modelReference = modelRecord.aquireReference("ModelReferenceThread:1");
		this.action = action;
	}

	@Override
	public void run() {

		try {
			if (action != null) {
				action.accept(modelReference);
			}
		} finally {
			// Null action to release memory
			action = null;
			modelReference.close();
		}
	}
}
