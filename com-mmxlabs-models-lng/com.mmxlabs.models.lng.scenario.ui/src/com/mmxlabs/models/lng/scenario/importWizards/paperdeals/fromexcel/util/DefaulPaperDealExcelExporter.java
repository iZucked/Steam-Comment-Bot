package com.mmxlabs.models.lng.scenario.importWizards.paperdeals.fromexcel.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

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

public class DefaulPaperDealExcelExporter implements IPaperDealExporter{

	private static final int REFERENCE_COL_NUM = 27;
	private static final int BUY_OR_SELL_COL_NUM = 12;
	private static final int INDEX_COL_NUM = 16;
	private static final int QUANTITY_COL_NUM = 20;
	private static final int PRICE_COL_NUM = 17;
	private static final int CONTRACT_MONTH_COL_NUM = 15;
	private static final int HEADGING_PERIOD_START_COL_NUM = 13;
	private static final int HEADGING_PERIOD_END_COL_NUM = 14;
	private static final String ENTITY_NAME = "cusa";
	
	private Map<String, String> instrumentMap = Map.of(
				"DATED_BRENT_Futures", "DATED_BRENT_SWAP",
				"ICE_BRENT_FUTURES", "BRENT_FUTURES",
				"JKM_LNG_Future", "JKM_SWAP",
				"TTF_ICE_FUTURES", "TFU_SWAP",
				"TTF_USDMM", "TTF_SWAP"
			);
	
	@Override
	public Pair<List<BuyPaperDeal>, List<SellPaperDeal>> getPaperDeals(final ExcelReader reader, final LNGScenarioModel lngScenarioModel, final List<PaperDealExcelImportResultDescriptor> messages) {
		Pair<List<BuyPaperDeal>, List<SellPaperDeal>> deals = new Pair<>();
		List<BuyPaperDeal> buyPaperDeals = new ArrayList<>();
		List<SellPaperDeal> sellPaperDeals = new ArrayList<>();
		final PricingModel pricingModel = ScenarioModelUtil.getPricingModel(lngScenarioModel);
		final List<SettleStrategy> sStrategies = pricingModel.getSettleStrategies();
		
		BaseLegalEntity entity = getEntity(lngScenarioModel);
		
		int sc = 0;
		int fc = 0;
		for(int i = 1; i < reader.getNumRows(); i++) {
			PaperDeal paperDeal = null;
			
			try {
				paperDeal = getPaperDeal(reader, i, sStrategies, entity, messages);
			}
			catch(Exception e) {
				e.printStackTrace();
				continue;
			}
			
			
			if(paperDeal != null) {
				sc++;
				if(paperDeal instanceof BuyPaperDeal buyPaperDeal)
					buyPaperDeals.add(buyPaperDeal);
				else if(paperDeal instanceof SellPaperDeal sellPaperDeal)
					sellPaperDeals.add(sellPaperDeal);
			} else {
				fc++;
			}
		}
		messages.add(new PaperDealExcelImportResultDescriptor("END", -1, -1,String.format("Finished importing. Successfull: %d; Failed: %d",sc,fc)));
		
		deals.setBoth(buyPaperDeals, sellPaperDeals);
		return deals;
	}
	
	/**
	 * Finds an entity to assign paper deals to
	 * By default returns first entity from the commercial model
	 * @param lngScenarioModel
	 * @return
	 */
	protected @NonNull BaseLegalEntity getEntity(final LNGScenarioModel lngScenarioModel) {
		CommercialModel commercialModel = ScenarioModelUtil.getCommercialModel(lngScenarioModel);
		for (final BaseLegalEntity e : commercialModel.getEntities()) {
			return e;
		}
		throw new IllegalStateException("No entity was found.");
	}
	
	/**
	 * For each row creates a paper deal
	 * By default does nothing
	 * @param reader
	 * @param rowId
	 * @param sStrategies
	 * @param entity
	 * @param fails
	 * @return
	 */
	protected PaperDeal getPaperDeal(final ExcelReader reader, int rowId, final List<SettleStrategy> sStrategies, final BaseLegalEntity entity, final List<PaperDealExcelImportResultDescriptor> fails) {
		// Get data
		List<BigDecimal> numericalValues = reader.readNumCells(rowId, 0);
		List<LocalDate> dateValues = reader.readRowDates(rowId, 0);
						
		String reference = reader.readName(rowId, REFERENCE_COL_NUM);
		String buyOrSell = reader.readName(rowId, BUY_OR_SELL_COL_NUM);
		String name = buyOrSell + "_" + reference + "_" + rowId;
		String index = reader.readName(rowId, INDEX_COL_NUM);

		BigDecimal quantity = numericalValues.get(QUANTITY_COL_NUM);
		BigDecimal price = numericalValues.get(PRICE_COL_NUM);
						
		String contractMonthStr = reader.readName(rowId, CONTRACT_MONTH_COL_NUM);
		LocalDate hedgingPeriodStart = dateValues.get(HEADGING_PERIOD_START_COL_NUM);
		LocalDate hedgingPeriodEnd = dateValues.get(HEADGING_PERIOD_END_COL_NUM);
						
		boolean usingInstrument = !index.equalsIgnoreCase("NYMEX_NG");
						
		// Validate data
		if(
				reference.isBlank() || !(buyOrSell.equalsIgnoreCase("Buy") || buyOrSell.equalsIgnoreCase("Sell")) || index.isBlank() 
				|| quantity == null || price == null || contractMonthStr.isBlank() 
				|| hedgingPeriodStart == null || hedgingPeriodEnd == null
		) {
			fails.add(new PaperDealExcelImportResultDescriptor(name, rowId, 0, String.format("Please check row %d. Data is empty", rowId)));
			return null;
		}
			
		// Get Contract month as YearMonth type
		YearMonth contractMonth = null;
			
		try {
			DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMM-yy", Locale.ENGLISH);
			contractMonth = YearMonth.parse(contractMonthStr, monthFormatter);
		}
		catch(Exception e) {
			fails.add(new PaperDealExcelImportResultDescriptor(name, rowId, CONTRACT_MONTH_COL_NUM, //
					String.format("Could not parse %s, row %d, column %d.", contractMonthStr, rowId, CONTRACT_MONTH_COL_NUM)));
			return null;
		} finally {
			if (contractMonth == null) {
				fails.add(new PaperDealExcelImportResultDescriptor(name, rowId, CONTRACT_MONTH_COL_NUM, //
						String.format("Could not determinte the contract month %s.",contractMonthStr)));
				return null;
			}
		}
			
		// Get Instrument SettleStratergy
		SettleStrategy settleStrategy = null;
					
		if(usingInstrument) {
			String instrument = instrumentMap.get(index);
			if(instrument == null) {
				fails.add(new PaperDealExcelImportResultDescriptor(name, rowId, INDEX_COL_NUM, //
						String.format("On row %d for curve %s instrument is null. Enter the instrument.", rowId, index)));
			}
			for (final SettleStrategy ss : sStrategies) {
				if (ss.getName().equalsIgnoreCase(instrument)) {
					settleStrategy = ss;
				}
			}
			if (settleStrategy == null) {
				fails.add(new PaperDealExcelImportResultDescriptor(name, rowId, INDEX_COL_NUM, //
						String.format("Could not find instrument with name %s for curve %s on row %d.", instrument, index, rowId)));
			}
		}
		
		// Check if buy or sell paper deal
		final PaperDeal paperDeal;
		if(buyOrSell.equalsIgnoreCase("buy")) {
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
		paperDeal.setIndex(index);
		paperDeal.setEntity(entity);
		paperDeal.setYear(hedgingPeriodStart.getYear());
		paperDeal.setComment(reference);
		paperDeal.setPricingPeriodStart(contractMonth.atDay(1));
		paperDeal.setPricingPeriodEnd(contractMonth.atEndOfMonth());
			
			
		if(usingInstrument) {
			paperDeal.setPricingType(PaperPricingType.INSTRUMENT);
				
			if(settleStrategy != null)
				paperDeal.setInstrument(settleStrategy);
		}
		else { // For the case of index "NYMEX_NG"
			paperDeal.setPricingType(PaperPricingType.PERIOD_AVG);
		}	
				
		return paperDeal;
	}
}
