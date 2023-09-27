package com.mmxlabs.models.lng.cargo.presentation.composites;

import org.eclipse.emf.ecore.ETypedElement;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.ui.editors.impl.SimpleOperationInlineEditor;

public class CIIYearToDateGradeInlineEditor extends SimpleOperationInlineEditor {

	private static final CargoPackage cp = CargoPackage.eINSTANCE;
	private String grade = "";
	
	public CIIYearToDateGradeInlineEditor(ETypedElement typedElement, int style) {
		super(typedElement, style);
	}
	
	public CIIYearToDateGradeInlineEditor(String name, ETypedElement typedElement) {
		super(name, typedElement);
	}

	@Override
	protected boolean updateOnChangeToFeature(final Object changedFeature) {
		return cp.getCIIStartOptions_YearToDateEmissions().equals(changedFeature)
				|| cp.getCIIStartOptions_YearToDateDistance().equals(changedFeature)
				|| cp.getVesselCharter_Vessel().equals(changedFeature);
	}
	
	public void setGrade(final String grade) {
		this.grade = grade;
	}
	
	@Override
	protected void updateDisplay(Object value) {
		if (text == null || text.isDisposed()) {
			return;
		}
		text.setText(grade);
	}

}
