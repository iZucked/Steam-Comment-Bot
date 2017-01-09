/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common.csv;

import java.util.function.Consumer;

import org.eclipse.jdt.annotation.NonNull;

/**
 * Implementation of {@link IDeferment} to allow Java 8 lambdas
 */
public final class FunctionalDeferment implements IDeferment {
	private final int stage;
	private final Consumer<@NonNull IImportContext> consumer;

	public FunctionalDeferment(int stage, Consumer<@NonNull IImportContext> consumer) {
		this.stage = stage;
		this.consumer = consumer;
	}

	@Override
	public void run(@NonNull final IImportContext context) {
		consumer.accept(context);
	}

	@Override
	public int getStage() {
		return stage;
	}
}