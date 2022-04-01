/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.util;

import java.time.LocalDate;
import java.util.function.Consumer;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.SellOpportunity;
import com.mmxlabs.models.lng.analytics.VolumeMode;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.types.VolumeUnits;

public class SellOpportunityMaker {
	@NonNull
	protected final SandboxModelBuilder builder;

	protected SellOpportunity opportunity = null;

	public SellOpportunityMaker(@NonNull final SandboxModelBuilder builder) {
		this.builder = builder;
		this.opportunity = AnalyticsFactory.eINSTANCE.createSellOpportunity();
		this.opportunity.setVolumeMode(VolumeMode.NOT_SPECIFIED);
	}

	@NonNull
	public SellOpportunityMaker create(boolean isFOBSale, @NonNull final Port port, String priceExpression) {
		this.opportunity.setFobSale(isFOBSale);
		this.opportunity.setPort(port);
		this.opportunity.setPriceExpression(priceExpression);
		return this;
	}

	public @NonNull SellOpportunity build() {
		this.builder.getOptionAnalysisModel().getSells().add(opportunity);
		return opportunity;
	}

	/**
	 * Generic modifier call
	 * 
	 * @param action
	 * @return
	 */
	@NonNull
	public SellOpportunityMaker with(@NonNull Consumer<SellOpportunity> action) {
		action.accept(opportunity);
		return this;
	}

	@NonNull
	public SellOpportunityMaker withVolumeRange(final int minVolume, final int maxVolume, @NonNull final VolumeUnits volumeUnit) {
		opportunity.setVolumeMode(VolumeMode.RANGE);
		opportunity.setVolumeUnits(volumeUnit);
		opportunity.setMinVolume(minVolume);
		opportunity.setMaxVolume(maxVolume);
		return this;
	}

	@NonNull
	public SellOpportunityMaker withVolumeFixed(final int volume, @NonNull final VolumeUnits volumeUnit) {
		opportunity.setVolumeMode(VolumeMode.FIXED);
		opportunity.setVolumeUnits(volumeUnit);
		opportunity.setMinVolume(volume);
		opportunity.setMaxVolume(volume);
		return this;
	}

	@NonNull
	public SellOpportunityMaker withEntity(final BaseLegalEntity entity) {
		opportunity.setEntity(entity);
		return this;
	}

	@NonNull
	public SellOpportunityMaker withContract(final SalesContract contract) {
		opportunity.setContract(contract);
		return this;
	}

	@NonNull
	public SellOpportunityMaker withMiscCosts(final int costs) {
		opportunity.setMiscCosts(costs);
		return this;
	}

	public @NonNull SellOpportunityMaker withCancellationFee(final @NonNull String expression) {
		opportunity.setCancellationExpression(expression);
		return this;
	}

	public SellOpportunityMaker withDate(@NonNull LocalDate date) {
		opportunity.setDate(date);
		return this;
	}
}
