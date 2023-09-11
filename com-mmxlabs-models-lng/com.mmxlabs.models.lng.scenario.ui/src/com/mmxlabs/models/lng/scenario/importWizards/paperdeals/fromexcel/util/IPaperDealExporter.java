package com.mmxlabs.models.lng.scenario.importWizards.paperdeals.fromexcel.util;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.BuyPaperDeal;
import com.mmxlabs.models.lng.cargo.SellPaperDeal;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;

public interface IPaperDealExporter {
	Pair<List<BuyPaperDeal>, List<SellPaperDeal>> getPaperDeals(ExcelReader reader, LNGScenarioModel lngScenarioModel, List<PaperDealExcelImportResultDescriptor> messages, IProgressMonitor monitor);
}
