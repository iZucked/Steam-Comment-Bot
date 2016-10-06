package com.mmxlabs.models.lng.analytics.ui.views.evaluators;

import com.mmxlabs.models.lng.analytics.AnalysisResultRow;
import com.mmxlabs.models.lng.analytics.BaseCase;
import com.mmxlabs.models.lng.analytics.BaseCaseRow;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.ResultSet;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.BaseCaseEvaluator.IMapperClass;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;

public class ResultsSetEvaluator extends BaseCaseEvaluator {

	protected static void buildScenario(final LNGScenarioModel clone, final OptionAnalysisModel clonedModel, final BaseCase clonedBaseCase, final IMapperClass mapper) {

		createShipping(clone, clonedBaseCase);
		ResultSet rs = new ResultSet();
		rs.get
		for (final AnalysisResultRow row : rs.getRows()) {
			final BuyOption buy = row.getBuyOption();
			final LoadSlot loadSlot = AnalyticsBuilder.makeLoadSlot(buy, clone);

			mapper.addMapping(buy, loadSlot);

			final SellOption sell = row.getSellOption();
			final DischargeSlot dischargeSlot = AnalyticsBuilder.makeDischargeSlot(sell, clone);
			mapper.addMapping(sell, dischargeSlot);

			Cargo cargo = null;
			if (loadSlot != null && dischargeSlot != null) {
				cargo = CargoFactory.eINSTANCE.createCargo();
				cargo.getSlots().add(loadSlot);
				cargo.getSlots().add(dischargeSlot);
			}
			setShipping(loadSlot, dischargeSlot, cargo, row.getShipping(), clone);

			if (loadSlot != null && !clone.getCargoModel().getLoadSlots().contains(loadSlot)) {
				clone.getCargoModel().getLoadSlots().add(loadSlot);
			}
			if (dischargeSlot != null && !clone.getCargoModel().getDischargeSlots().contains(dischargeSlot)) {
				clone.getCargoModel().getDischargeSlots().add(dischargeSlot);
			}
			if (cargo != null && !clone.getCargoModel().getCargoes().contains(cargo)) {
				clone.getCargoModel().getCargoes().add(cargo);
			}
		}

	}

}
