/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.util;

import java.util.function.Consumer;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.SimpleVesselCharterOption;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.fleet.Vessel;

public class SimpleCharterOptionMaker {

	protected final @NonNull  SandboxModelBuilder builder;

	protected final @NonNull SimpleVesselCharterOption option;

	public SimpleCharterOptionMaker(@NonNull final SandboxModelBuilder builder) {
		this.builder = builder;
		this.option = AnalyticsFactory.eINSTANCE.createSimpleVesselCharterOption();
		this.option.setUseSafetyHeel(true);
	}

	@NonNull
	public SimpleCharterOptionMaker create(Vessel vessel, @NonNull BaseLegalEntity entity) {
		this.option.setVessel(vessel);
		this.option.setEntity(entity);
		return this;
	}

	public @NonNull SimpleVesselCharterOption build() {
		this.builder.getOptionAnalysisModel().getShippingTemplates().add(option);
		return option;
	}

	/**
	 * Generic modifier call
	 * 
	 * @param action
	 * @return
	 */
	@NonNull
	public SimpleCharterOptionMaker with(@NonNull Consumer<SimpleVesselCharterOption> action) {
		action.accept(option);
		return this;
	}

	@NonNull
	public SimpleCharterOptionMaker withEntity(final BaseLegalEntity entity) {
		option.setEntity(entity);
		return this;
	}

	@NonNull
	public SimpleCharterOptionMaker withHireCosts(final String expression) {
		option.setHireCost(expression);
		return this;
	}

	@NonNull
	public SimpleCharterOptionMaker withUseSafetyHeel(final boolean useSafetyHeel) {
		option.setUseSafetyHeel(useSafetyHeel);
		return this;
	}
}
