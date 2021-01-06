/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.util;

import java.time.LocalDate;
import java.util.function.Consumer;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.CharterOutOpportunity;
import com.mmxlabs.models.lng.port.Port;

public class CharterOutOpportunityMaker {
	@NonNull
	protected final SandboxModelBuilder builder;

	protected CharterOutOpportunity opportunity = null;

	public CharterOutOpportunityMaker(@NonNull final SandboxModelBuilder builder) {
		this.builder = builder;
		this.opportunity = AnalyticsFactory.eINSTANCE.createCharterOutOpportunity();
	}

	@NonNull
	public CharterOutOpportunityMaker create(@NonNull final Port port, LocalDate date, int duration) {
		this.opportunity.setPort(port);
		this.opportunity.setDate(date);
		this.opportunity.setDuration(duration);
		return this;
	}

	public @NonNull CharterOutOpportunity build() {
		this.builder.getOptionAnalysisModel().getVesselEvents().add(opportunity);
		return opportunity;
	}

	/**
	 * Generic modifier call
	 * 
	 * @param action
	 * @return
	 */
	@NonNull
	public CharterOutOpportunityMaker with(@NonNull Consumer<CharterOutOpportunity> action) {
		action.accept(opportunity);
		return this;
	}

	@NonNull
	public CharterOutOpportunityMaker withHireCost(int hireCost) {
		opportunity.setHireCost(hireCost);
		return this;
	}

	public CharterOutOpportunityMaker withDate(@NonNull LocalDate date) {
		opportunity.setDate(date);
		return this;
	}
}
