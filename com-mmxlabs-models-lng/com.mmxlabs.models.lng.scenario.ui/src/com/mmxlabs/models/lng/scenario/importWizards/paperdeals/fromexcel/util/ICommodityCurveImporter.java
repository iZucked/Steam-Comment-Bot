package com.mmxlabs.models.lng.scenario.importWizards.paperdeals.fromexcel.util;

import java.util.List;

import com.mmxlabs.models.lng.pricing.CommodityCurve;

public interface ICommodityCurveImporter {
	List<CommodityCurve> getCommodityCurves(ExcelReader reader);
}
