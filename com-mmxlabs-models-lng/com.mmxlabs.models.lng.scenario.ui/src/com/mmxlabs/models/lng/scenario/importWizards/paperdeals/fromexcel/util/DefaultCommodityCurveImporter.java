package com.mmxlabs.models.lng.scenario.importWizards.paperdeals.fromexcel.util;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.runtime.IProgressMonitor;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.YearMonthPoint;

public class DefaultCommodityCurveImporter implements ICommodityCurveImporter {

	/**
	 * Returns the list of commodity curves 
	 */
	@Override
	public List<CommodityCurve> getCommodityCurves(ExcelReader reader, IProgressMonitor monitor, List<ExcelImportResultDescriptor> messages) {
		monitor.beginTask("Import Commodity Curves", reader.getNumRows());
		List<CommodityCurve> curves = new ArrayList<>();
		final List<CommodityCurveData> sheetData = getSheetData(reader, messages);
		
		for(CommodityCurveData curveData : sheetData) {
			final CommodityCurve curve = PricingFactory.eINSTANCE.createCommodityCurve();
			curve.setName(curveData.getName());
			curve.setCurrencyUnit(curveData.getCurrency());
			curve.setVolumeUnit(curveData.getUnit());
			
			for(Pair<LocalDate, Double> dataPoint : curveData.getDatesAndPrices()) {
				YearMonthPoint ymp = PricingFactory.eINSTANCE.createYearMonthPoint();
				ymp.setDate(YearMonth.from(dataPoint.getFirst()));
				ymp.setValue(dataPoint.getSecond());
				curve.getPoints().add(ymp);
			}
			
			curves.add(curve);
			monitor.worked(1);
		}
		
		return curves;
	}

	protected List<CommodityCurveData> getSheetData(ExcelReader reader, List<ExcelImportResultDescriptor> messages){
		return null;
	}
}
