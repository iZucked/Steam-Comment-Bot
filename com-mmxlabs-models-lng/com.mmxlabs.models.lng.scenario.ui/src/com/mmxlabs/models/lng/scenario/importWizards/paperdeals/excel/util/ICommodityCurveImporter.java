package com.mmxlabs.models.lng.scenario.importWizards.paperdeals.excel.util;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import com.mmxlabs.models.lng.pricing.CommodityCurve;

public interface ICommodityCurveImporter {
	List<CommodityCurve> getCommodityCurves(ExcelReader reader, IProgressMonitor monitor, List<ExcelImportResultDescriptor> messages);
}
