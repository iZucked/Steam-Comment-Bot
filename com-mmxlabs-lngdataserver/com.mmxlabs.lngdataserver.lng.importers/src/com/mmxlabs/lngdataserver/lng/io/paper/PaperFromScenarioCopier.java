/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.io.paper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lngdataserver.integration.paper.model.DatahubPaperDeal;
import com.mmxlabs.lngdataserver.integration.paper.model.DatahubPaperDeal.Kind;
import com.mmxlabs.lngdataserver.integration.paper.model.DatahubPaperDeal.PricingType;
import com.mmxlabs.lngdataserver.integration.paper.model.PaperVersion;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.PaperDeal;
import com.mmxlabs.models.lng.cargo.PaperPricingType;
import com.mmxlabs.models.lng.cargo.SellPaperDeal;
import com.mmxlabs.models.mmxcore.VersionRecord;
import com.mmxlabs.rcp.common.versions.VersionsUtil;

public class PaperFromScenarioCopier {

	private static final Logger LOGGER = LoggerFactory.getLogger(PaperFromScenarioCopier.class);

	public static PaperVersion generateVersion(CargoModel cargoModel) {
		List<PaperDeal> paperDeals = cargoModel.getPaperDeals();
		PaperVersion paperVersion = new PaperVersion();
		
		for (PaperDeal paper : paperDeals) {
			paperVersion.getPaperDeals().add(transformPaperDeal(paper));
		}
		
		VersionRecord record = cargoModel.getPaperDealsVersionRecord();
		if (record == null || record.getVersion() == null) {
			record = VersionsUtil.createNewRecord();
			cargoModel.setPaperDealsVersionRecord(record);
		}
		
		return paperVersion;
	}
	
	private static Map<PaperPricingType, PricingType> pricingTypeMap = new HashMap<>();
	static {
		pricingTypeMap.put(PaperPricingType.CALENDAR, PricingType.CALENDAR);
		pricingTypeMap.put(PaperPricingType.PERIOD_AVG, PricingType.PERIOD_AVG);
		pricingTypeMap.put(PaperPricingType.INSTRUMENT, PricingType.INSTRUMENT);
	}

	private static DatahubPaperDeal transformPaperDeal(PaperDeal paperDeal) {
		Kind dealType = Kind.BUY_PAPER_DEAL;
		if (paperDeal instanceof SellPaperDeal) {
			dealType = Kind.SELL_PAPER_DEAL;
		}
		
		return new DatahubPaperDeal(
			paperDeal.getPrice(),
			paperDeal.getQuantity(),
			paperDeal.getYear(),
			paperDeal.getPricingMonth(),
			paperDeal.getPricingPeriodStart(),
			paperDeal.getPricingPeriodEnd(),
			paperDeal.getHedgingPeriodStart(),
			paperDeal.getHedgingPeriodEnd(),
			paperDeal.getName(),
			paperDeal.getIndex(),
			paperDeal.getComment(),
			pricingTypeMap.get(paperDeal.getPricingType()),
			dealType
		);
	}
}
