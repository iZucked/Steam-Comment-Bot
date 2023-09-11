package com.mmxlabs.models.lng.scenario.importWizards.paperdeals.fromexcel.util;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.YearMonthPoint;

public class DefaultCommodityCurveImporter implements ICommodityCurveImporter {

	@Override
	public List<CommodityCurve> getCommodityCurves(ExcelReader reader) {
		List<CommodityCurve> curves = new ArrayList<>();
		List<CommodityCurveData> sheetData = getSheetData(reader);
		
		for(CommodityCurveData curveData : sheetData) {
			CommodityCurve curve = PricingFactory.eINSTANCE.createCommodityCurve();
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
		}
		
		return curves;
	}

	protected List<CommodityCurveData> getSheetData(ExcelReader reader){
		return null;
	}
}
