package com.mmxlabs.models.lng.cargo.presentation.composites;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.impl.DefaultComponentHelper;

public class CIIStartOptionsComponentHelper extends DefaultComponentHelper {
	
	private static final CargoPackage cp = CargoPackage.eINSTANCE;

	public CIIStartOptionsComponentHelper() {
		super(cp.getCIIStartOptions());
		includeEOperations = true;
		
		addEditor(cp.getCIIStartOptions_YearToDateEmissions(), topClass -> {
			return ComponentHelperUtils.createDefaultEditor(topClass, cp.getCIIStartOptions_YearToDateEmissions());
		});
		
		addEditor(cp.getCIIStartOptions_YearToDateDistance(), topClass -> {
			return ComponentHelperUtils.createDefaultEditor(topClass, cp.getCIIStartOptions_YearToDateDistance());
		});
		
		addEditor(cp.getCIIStartOptions__YearToDatePartialCII(), topClass -> {
			return new CIIYearToDateGradeInlineEditorWrapper(new CIIYearToDateGradeInlineEditor("YTD Rating", cp.getCIIStartOptions__YearToDatePartialCII())); 
		});
	}

}
