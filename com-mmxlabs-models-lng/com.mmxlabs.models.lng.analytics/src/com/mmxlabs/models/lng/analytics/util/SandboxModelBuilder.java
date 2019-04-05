package com.mmxlabs.models.lng.analytics.util;

import java.time.LocalDate;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.BaseCaseRow;
import com.mmxlabs.models.lng.analytics.BuyMarket;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.analytics.ExistingVesselCharterOption;
import com.mmxlabs.models.lng.analytics.OpenBuy;
import com.mmxlabs.models.lng.analytics.OpenSell;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.analytics.SellMarket;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.SellReference;
import com.mmxlabs.models.lng.analytics.ShippingOption;
import com.mmxlabs.models.lng.analytics.VesselEventOption;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;

public class SandboxModelBuilder {
	private final @NonNull AnalyticsModel analyticsModel;

	private final @NonNull OptionAnalysisModel optionAnalysisModel;

	public static SandboxModelBuilder createSandbox(final @NonNull AnalyticsModel analyticsModel) {
		final OptionAnalysisModel optionAnalysisModel = AnalyticsFactory.eINSTANCE.createOptionAnalysisModel();
		optionAnalysisModel.setName("Sandbox");
		optionAnalysisModel.setBaseCase(AnalyticsFactory.eINSTANCE.createBaseCase());
		optionAnalysisModel.setPartialCase(AnalyticsFactory.eINSTANCE.createPartialCase());

		analyticsModel.getOptionModels().add(optionAnalysisModel);
		return new SandboxModelBuilder(analyticsModel, optionAnalysisModel);
	}

	private SandboxModelBuilder(final @NonNull AnalyticsModel analyticsModel, final @NonNull OptionAnalysisModel optionAnalysisModel) {
		this.analyticsModel = analyticsModel;
		this.optionAnalysisModel = optionAnalysisModel;
	}

	@NonNull
	public AnalyticsModel getAnalyticsModel() {
		return analyticsModel;
	}

	@NonNull
	public OptionAnalysisModel getOptionAnalysisModel() {
		return optionAnalysisModel;
	}

	public void setPortfolioMode(final boolean b) {
		optionAnalysisModel.getBaseCase().setKeepExistingScenario(b);
		optionAnalysisModel.getPartialCase().setKeepExistingScenario(b);

	}

	public void setPortfolioBreakevenMode(final boolean b) {
		optionAnalysisModel.setUseTargetPNL(b);
	}

	public void setManualSandboxMode() {
		optionAnalysisModel.setMode(0);
	}

	public void setOptimiseSandboxMode() {
		optionAnalysisModel.setMode(1);
	}

	public void setOptioniseSandboxMode() {
		optionAnalysisModel.setMode(2);
	}

	public BuyReference createBuyReference(final LoadSlot slot) {
		final BuyReference buy = AnalyticsFactory.eINSTANCE.createBuyReference();
		buy.setSlot(slot);
		optionAnalysisModel.getBuys().add(buy);
		return buy;
	}

	public SellReference createSellReference(final DischargeSlot slot) {
		final SellReference sell = AnalyticsFactory.eINSTANCE.createSellReference();
		sell.setSlot(slot);
		optionAnalysisModel.getSells().add(sell);
		return sell;
	}

	public BuyMarket createBuyMarketOption(final SpotMarket market) {
		final BuyMarket buy = AnalyticsFactory.eINSTANCE.createBuyMarket();
		buy.setMarket(market);
		optionAnalysisModel.getBuys().add(buy);
		return buy;
	}

	public SellMarket createSellMarketOption(final SpotMarket market) {
		final SellMarket sell = AnalyticsFactory.eINSTANCE.createSellMarket();
		sell.setMarket(market);
		optionAnalysisModel.getSells().add(sell);
		return sell;
	}

	public OpenBuy createOpenBuy() {
		final OpenBuy buy = AnalyticsFactory.eINSTANCE.createOpenBuy();
		optionAnalysisModel.getBuys().add(buy);
		return buy;
	}

	public OpenSell createOpenSell() {
		final OpenSell sell = AnalyticsFactory.eINSTANCE.createOpenSell();
		optionAnalysisModel.getSells().add(sell);
		return sell;
	}

	public BuyOpportunityMaker makeBuyOpportunity(final boolean isDESPurchase, @NonNull final Port port, final BaseLegalEntity entity, final String priceExpression) {
		final BuyOpportunityMaker maker = new BuyOpportunityMaker(this);
		return maker.create(isDESPurchase, port, priceExpression).withEntity(entity);
	}

	public SellOpportunityMaker makeSellOpportunity(final boolean isFOBSale, @NonNull final Port port, final BaseLegalEntity entity, final String priceExpression) {
		final SellOpportunityMaker maker = new SellOpportunityMaker(this);
		return maker.create(isFOBSale, port, priceExpression).withEntity(entity);
	}

	public CharterOutOpportunityMaker makeCharterOutOpportunity(@NonNull final Port port, final LocalDate date, final int duration) {
		final CharterOutOpportunityMaker maker = new CharterOutOpportunityMaker(this);
		return maker.create(port, date, duration);
	}

	public SimpleCharterOptionMaker makeSimpleCharter(final Vessel vessel, final BaseLegalEntity entity) {
		final SimpleCharterOptionMaker maker = new SimpleCharterOptionMaker(this);
		return maker.create(vessel, entity);
	}

	public FullCharterOptionMaker makeFullCharter(final Vessel vessel, final BaseLegalEntity entity) {
		final FullCharterOptionMaker maker = new FullCharterOptionMaker(this);
		return maker.create(vessel, entity);
	}

	public ExistingVesselCharterOption createExistingCharter(final VesselAvailability vesselCharter) {
		final ExistingVesselCharterOption option = AnalyticsFactory.eINSTANCE.createExistingVesselCharterOption();

		option.setVesselCharter(vesselCharter);

		return option;
	}

	public BaseCaseRow createBaseCaseRow(final BuyOption buy, final SellOption sell, final ShippingOption shipping) {
		final BaseCaseRow row = AnalyticsFactory.eINSTANCE.createBaseCaseRow();
		row.setBuyOption(buy);
		row.setSellOption(sell);
		row.setShipping(shipping);

		this.optionAnalysisModel.getBaseCase().getBaseCase().add(row);
		return row;

	}

	public BaseCaseRow createBaseCaseRow(final VesselEventOption event, @Nullable final ShippingOption shipping) {
		final BaseCaseRow row = AnalyticsFactory.eINSTANCE.createBaseCaseRow();
		row.setVesselEventOption(event);

		this.optionAnalysisModel.getBaseCase().getBaseCase().add(row);
		return row;

	}

	public PartialCaseRow createPartialCaseRow(final BuyOption buy, final SellOption sell, final ShippingOption shipping) {
		final PartialCaseRow row = AnalyticsFactory.eINSTANCE.createPartialCaseRow();
		if (buy != null) {
			row.getBuyOptions().add(buy);
		}
		if (sell != null) {
			row.getSellOptions().add(sell);
		}
		if (shipping != null) {
			row.getShipping().add(shipping);
		}
		this.optionAnalysisModel.getPartialCase().getPartialCase().add(row);
		return row;

	}

	public PartialCaseRow createPartialCaseRow(final VesselEventOption event, final ShippingOption shipping) {
		final PartialCaseRow row = AnalyticsFactory.eINSTANCE.createPartialCaseRow();
		if (event != null) {
			row.getVesselEventOptions().add(event);
		}
		if (shipping != null) {
			row.getShipping().add(shipping);
		}

		this.optionAnalysisModel.getPartialCase().getPartialCase().add(row);
		return row;
	}

	public PartialCaseRowMaker makePartialCaseRow() {
		return new PartialCaseRowMaker(this);
	}

}
