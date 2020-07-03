/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.util;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.function.Consumer;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.BuyOpportunity;
import com.mmxlabs.models.lng.analytics.SellOpportunity;
import com.mmxlabs.models.lng.analytics.VolumeMode;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.PricingEvent;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.types.VolumeUnits;

public class BuyOpportunityMaker {
	@NonNull
	protected final SandboxModelBuilder builder;

	protected BuyOpportunity opportunity = null;

	public BuyOpportunityMaker(@NonNull final SandboxModelBuilder builder) {
		this.builder = builder;
		this.opportunity = AnalyticsFactory.eINSTANCE.createBuyOpportunity();
		this.opportunity.setVolumeMode(VolumeMode.NOT_SPECIFIED);
	}

	@NonNull
	public BuyOpportunityMaker create(boolean isDESPurchase, @NonNull final Port port, String priceExpression) {
		this.opportunity.setDesPurchase(isDESPurchase);
		this.opportunity.setPort(port);
		this.opportunity.setPriceExpression(priceExpression);
		return this;
	}

	public @NonNull BuyOpportunity build() {
		this.builder.getOptionAnalysisModel().getBuys().add(opportunity);
		return opportunity;
	}

	/**
	 * Generic modifier call
	 * 
	 * @param action
	 * @return
	 */
	@NonNull
	public BuyOpportunityMaker with(@NonNull Consumer<BuyOpportunity> action) {
		action.accept(opportunity);
		return this;
	}

	@NonNull
	public BuyOpportunityMaker withVolumeRange(final int minVolume, final int maxVolume, @NonNull final VolumeUnits volumeUnit) {
		opportunity.setVolumeMode(VolumeMode.RANGE);
		opportunity.setVolumeUnits(volumeUnit);
		opportunity.setMinVolume(minVolume);
		opportunity.setMaxVolume(maxVolume);
		return this;
	}

	@NonNull
	public BuyOpportunityMaker withVolumeFixed(final int volume, @NonNull final VolumeUnits volumeUnit) {
		opportunity.setVolumeMode(VolumeMode.FIXED);
		opportunity.setVolumeUnits(volumeUnit);
		opportunity.setMinVolume(volume);
		opportunity.setMaxVolume(volume);
		return this;
	}

	@NonNull
	public BuyOpportunityMaker withEntity(final BaseLegalEntity entity) {
		opportunity.setEntity(entity);
		return this;
	}

	@NonNull
	public BuyOpportunityMaker withContract(final PurchaseContract contract) {
		opportunity.setContract(contract);
		return this;
	}

	@NonNull
	public BuyOpportunityMaker withMiscCosts(final int costs) {
		opportunity.setMiscCosts(costs);
		return this;
	}

	@NonNull
	public BuyOpportunityMaker withCV(double cv) {
		opportunity.setCv(cv);
		return this;
	}

	public @NonNull BuyOpportunityMaker withCancellationFee(final @NonNull String expression) {
		opportunity.setCancellationExpression(expression);
		return this;
	}

	public BuyOpportunityMaker withDate(@NonNull LocalDate date) {
		opportunity.setDate(date);
		return this;
	}
}
