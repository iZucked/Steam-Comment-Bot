package com.mmxlabs.models.lng.cargo.util;

import java.io.FileInputStream;
import java.util.List;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.BuyPaperDeal;
import com.mmxlabs.models.lng.cargo.SellPaperDeal;

public class DefaulPaperDealExcelExporter implements IPaperDealExporter{

	@Override
	public Pair<List<BuyPaperDeal>, List<SellPaperDeal>> getPaperDeals(FileInputStream fis) {
		return null;
	}

}
