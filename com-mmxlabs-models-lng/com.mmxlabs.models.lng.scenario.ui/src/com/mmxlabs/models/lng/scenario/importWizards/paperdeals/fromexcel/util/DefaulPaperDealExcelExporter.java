package com.mmxlabs.models.lng.scenario.importWizards.paperdeals.fromexcel.util;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.BuyPaperDeal;
import com.mmxlabs.models.lng.cargo.PaperDeal;
import com.mmxlabs.models.lng.cargo.SellPaperDeal;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.SettleStrategy;
import com.mmxlabs.models.lng.scenario.importWizards.paperdeals.fromexcel.util.ExcelImportResultDescriptor.MessageType;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;

public class DefaulPaperDealExcelExporter implements IPaperDealExporter{
	
	@Override
	public Pair<List<BuyPaperDeal>, List<SellPaperDeal>> getPaperDeals(final ExcelReader reader, final LNGScenarioModel lngScenarioModel, final List<ExcelImportResultDescriptor> messages, IProgressMonitor monitor) {
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
		messages.add(new ExcelImportResultDescriptor(MessageType.INFO, "", -1, -1,String.format("Finished importing. Successfull: %d; Failed: %d",sc,fc)));
		
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
	protected PaperDeal getPaperDeal(final ExcelReader reader, int rowId, final List<SettleStrategy> sStrategies, final BaseLegalEntity entity, final List<ExcelImportResultDescriptor> fails) {
		return null;
	}
}
