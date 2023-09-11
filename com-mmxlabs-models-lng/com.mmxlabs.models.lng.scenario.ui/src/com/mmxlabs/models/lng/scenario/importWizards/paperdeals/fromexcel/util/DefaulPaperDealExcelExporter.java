package com.mmxlabs.models.lng.scenario.importWizards.paperdeals.fromexcel.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
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
	public Pair<List<BuyPaperDeal>, List<SellPaperDeal>> getPaperDeals(final ExcelReader reader, final LNGScenarioModel lngScenarioModel, final List<PaperDealExcelImportResultDescriptor> messages, IProgressMonitor monitor) {
		monitor.beginTask("Importing paper deals", reader.getNumRows());
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
			monitor.worked(1);
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
		return null;
	}
}
