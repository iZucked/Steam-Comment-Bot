/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.ui.importer;

import java.util.function.BiConsumer;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.csv.IDeferment;
import com.mmxlabs.common.csv.IImportContext;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.util.importer.IMMXImportContext;

public class LNGFunctionalDeferment implements IDeferment {
	private final int stage;
	private final BiConsumer<@NonNull LNGScenarioModel, @NonNull IMMXImportContext> consumer;

	public LNGFunctionalDeferment(final int stage, final BiConsumer<@NonNull LNGScenarioModel, @NonNull IMMXImportContext> consumer) {
		this.stage = stage;
		this.consumer = consumer;
	}

	@Override
	public void run(@NonNull final IImportContext context) {
		if (context instanceof IMMXImportContext) {
			final IMMXImportContext mmxImportContext = (IMMXImportContext) context;

			final MMXRootObject rootObject = mmxImportContext.getRootObject();
			if (rootObject instanceof LNGScenarioModel) {
				final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
				consumer.accept(lngScenarioModel, mmxImportContext);
				return;
			}

		}
		throw new ClassCastException("Context or root model is not of expected type");

	}

	@Override
	public int getStage() {
		return stage;
	}
}
