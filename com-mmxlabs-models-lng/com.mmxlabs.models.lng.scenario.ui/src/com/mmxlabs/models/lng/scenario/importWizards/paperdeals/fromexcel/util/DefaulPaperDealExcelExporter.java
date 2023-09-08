package com.mmxlabs.models.lng.scenario.importWizards.paperdeals.fromexcel.util;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.BuyPaperDeal;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.PaperDeal;
import com.mmxlabs.models.lng.cargo.PaperPricingType;
import com.mmxlabs.models.lng.cargo.SellPaperDeal;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.SettleStrategy;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;

public class DefaulPaperDealExcelExporter implements IPaperDealExporter{
	
	private int REFERENCE_COL_NUM = 27;
	private int BUY_OR_SELL_COL_NUM = 12;
	private int INDEX_COL_NUM = 16;
	private int ENTITY_COL_NUM = 4;
	private int QUANTITY_COL_NUM = 20;
	private int PRICE_COL_NUM = 17;
	private int CONTRACT_MONTH_COL_NUM = 15;
	private int HEADGING_PERIOD_START_COL_NUM = 13;
	private int HEADGING_PERIOD_END_COL_NUM = 14;
	private int INSTRUMENT_COL_NUM = 11;
	
	private Map<String, String> instrumentMap = Map.of(
				"DATED_BRENT_Futures", "DATED_BRENT_SWAP",
				"ICE_BRENT_FUTURES", "BRENT_FUTURES",
				"JKM_LNG_Future", "JKM_SWAP",
				"TTF_ICE_FUTURES", "TFU_SWAP",
				"TTF_USDMM", "TTF_SWAP"
			);

	
	@Override
	public Pair<List<BuyPaperDeal>, List<SellPaperDeal>> getPaperDeals(ExcelReader reader, LNGScenarioModel lngScenarioModel) {
		Pair<List<BuyPaperDeal>, List<SellPaperDeal>> deals = new Pair<>();
		List<BuyPaperDeal> buyPaperDeals = new ArrayList<>();
		List<SellPaperDeal> sellPaperDeals = new ArrayList<>();
		
		for(int i = 1; i < reader.getNumRows(); i++) {
			System.out.println(i);
			PaperDeal paperDeal = null;
			
			try {
				paperDeal = getPaperDeal(reader, i, lngScenarioModel);
			}
			catch(Exception e) {
				e.printStackTrace();
				continue;
			}
			
			
			if(paperDeal != null) {
				if(paperDeal instanceof BuyPaperDeal buyPaperDeal)
					buyPaperDeals.add(buyPaperDeal);
				else if(paperDeal instanceof SellPaperDeal sellPaperDeal)
					sellPaperDeals.add(sellPaperDeal);
			}
			// Perhaps store failed paper deals?
		}
		
		deals.setBoth(buyPaperDeals, sellPaperDeals);
		return deals;
	}

	@Override
	public PaperDeal getPaperDeal(ExcelReader reader, int rowId, LNGScenarioModel lngScenarioModel) {
		// Get data
		List<BigDecimal> numericalValues = reader.readNumCells(rowId, 0);
		List<LocalDate> dateValues = reader.readRowDates(rowId, 0);
				
		String reference = reader.readName(rowId, REFERENCE_COL_NUM);
		String buyOrSell = reader.readName(rowId, BUY_OR_SELL_COL_NUM);
		String name = buyOrSell + "_" + reference + "_" + rowId;
		String index = reader.readName(rowId, INDEX_COL_NUM);
		String entityName = reader.readName(rowId, ENTITY_COL_NUM);

		BigDecimal quantity = numericalValues.get(QUANTITY_COL_NUM);
		BigDecimal price = numericalValues.get(PRICE_COL_NUM);
				
		String contractMonthStr = reader.readName(rowId, CONTRACT_MONTH_COL_NUM);
		LocalDate hedgingPeriodStart = dateValues.get(HEADGING_PERIOD_START_COL_NUM);
		LocalDate hedgingPeriodEnd = dateValues.get(HEADGING_PERIOD_END_COL_NUM);
				
		boolean usingInstrument = !index.equals("NYMEX_NG");
				
		// Validate data
		if(
				reference.equals("") || !(buyOrSell.contains("Buy") || buyOrSell.contains("Sell")) || index.equals("") 
				|| entityName.equals("") || quantity == null || price == null || contractMonthStr.equals("") 
				|| hedgingPeriodStart == null || hedgingPeriodEnd == null
		)
			return null;
		
		
		// Get Contract month as YearMonth type
		YearMonth contractMonth;
		
		try {
			DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMM-yy", Locale.ENGLISH);
			contractMonth = YearMonth.parse(contractMonthStr, monthFormatter);
		}
		catch(Exception e) {
			return null;
		}
		
		
		// Get Instrument SettleStratergy
		SettleStrategy settleStrategy = null;
				
		if(usingInstrument) {
			final PricingModel pricingModel = ScenarioModelUtil.getPricingModel(lngScenarioModel);
			String instrument = instrumentMap.get(index);
			
			if(instrument == null)
				throw new IllegalStateException(String.format("No pricing instrument mapped to %s found!", index));
			
			for (final SettleStrategy ss : pricingModel.getSettleStrategies()) {
				if (ss.getName().equalsIgnoreCase(instrument)) {
					settleStrategy = ss;
				}
			}
			if (settleStrategy == null) {
				throw new IllegalStateException(String.format("No pricing instrument %s found!", instrument));
			}
		}
		
				
				
		// Get Entity
		BaseLegalEntity entity = null;
		
		CommercialModel commercialModel = ScenarioModelUtil.getCommercialModel(lngScenarioModel);
		
		for (final BaseLegalEntity e : commercialModel.getEntities()) {
			if(entityName.contains(e.getName()))
				entity = e;
		}
		if (entity == null) {
			throw new IllegalStateException(String.format("No entity found with similar name %s!", entityName));
		}
				

		// Check if buy or sell paper deal
		final PaperDeal paperDeal;
		if(buyOrSell.equals("Buy")) {
			paperDeal = CargoFactory.eINSTANCE.createBuyPaperDeal();
		}
		else {
			paperDeal = CargoFactory.eINSTANCE.createSellPaperDeal();
		}
				
		// Place data in paper deal
		paperDeal.setName(name);
		paperDeal.setQuantity(-quantity.longValueExact());
		paperDeal.setPrice(price.doubleValue());
		paperDeal.setPricingMonth(contractMonth);
		paperDeal.setHedgingPeriodStart(hedgingPeriodStart);
		paperDeal.setHedgingPeriodEnd(hedgingPeriodEnd);
		
		
		if(usingInstrument) {
			paperDeal.setPricingType(PaperPricingType.INSTRUMENT);
			
			if(settleStrategy != null)
				paperDeal.setInstrument(settleStrategy);
		}
		else { // For the case of index "NYMEX_NG"
			paperDeal.setPricingType(PaperPricingType.PERIOD_AVG);
		}
			
			
		
		paperDeal.setIndex(index);
		paperDeal.setEntity(entity);
		paperDeal.setYear(hedgingPeriodStart.getYear());
		paperDeal.setComment(reference);
		
		paperDeal.setPricingPeriodStart(contractMonth.atDay(1));
		paperDeal.setPricingPeriodEnd(contractMonth.atEndOfMonth());
						
				
		return paperDeal;
	}

}
