package com.mmxlabs.models.lng.cargo.presentation.composites;

import java.time.Year;

import com.mmxlabs.models.lng.cargo.CIIStartOptions;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.schedule.cii.UtilsCII;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.impl.SimpleOperationInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultComponentHelper;

public class CIIStartOptionsComponentHelper extends DefaultComponentHelper {
	
	private final static CargoPackage cp = CargoPackage.eINSTANCE;

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
			final IInlineEditor editor = new SimpleOperationInlineEditor("YTD Rating", cp.getCIIStartOptions__YearToDatePartialCII()) {
				
				@Override
				protected void updateDisplay(Object value) {
					if (text == null || text.isDisposed()) {
						return;
					}
					//					if (value instanceof final VesselCharter vesselCharter) {
					//						final Vessel vessel = vesselCharter.getVessel();
					//						final CIIStartOptions ciiStartOptions = vesselCharter.getCiiStartOptions();
					//						if (vessel != null && ciiStartOptions != null) {
					//							final double ciiValue = UtilsCII.findCII(vessel, ciiStartOptions.getYearToDateEmissions(), ciiStartOptions.getYearToDateDistance());
					//							final String grade = UtilsCII.getLetterGrade(vessel, ciiValue, Year.from(vesselCharter.getStartBy()));
					//							text.setText(grade);
					//						}
					//					}
					text.setText(String.valueOf(value));
				}
				
				@Override
				protected boolean updateOnChangeToFeature(final Object changedFeature) {
					return cp.getCIIStartOptions_YearToDateEmissions().equals(changedFeature)
							|| cp.getCIIStartOptions_YearToDateDistance().equals(changedFeature);
				}
			};
			return new CIIYearToDateGradeInlineEditorWrapper(editor);
		});
	}

}
