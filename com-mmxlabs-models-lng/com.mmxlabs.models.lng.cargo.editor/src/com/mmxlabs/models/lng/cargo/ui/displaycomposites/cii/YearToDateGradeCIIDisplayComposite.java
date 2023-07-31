package com.mmxlabs.models.lng.cargo.ui.displaycomposites.cii;

import java.time.Year;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

import com.mmxlabs.models.lng.cargo.CIIStartOptions;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.schedule.cii.UtilsCII;

public class YearToDateGradeCIIDisplayComposite extends Composite {
	
	private static final String UNDEFINED_CII_GRADE_LABEL_TEXT = "-";
	private Label ciiGradeLabel = null;
	private final VesselCharter eVesselCharter;

	public YearToDateGradeCIIDisplayComposite(final Composite parent, final EObject eVesselCharterEObject) {
		super(parent, SWT.NONE);
		if (eVesselCharterEObject instanceof final VesselCharter vesselCharterEObjectIndeed) {
			this.eVesselCharter = vesselCharterEObjectIndeed;
		} else {
			this.eVesselCharter = null;
		}
		init();
	}
	
	

	private void init() {
		setLayout(GridLayoutFactory.swtDefaults().numColumns(2).create());
		setLayoutData(GridDataFactory.fillDefaults().create());
		setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		
		final Label yearToDateCIIGradeLabel = new Label(this, SWT.NONE);
		yearToDateCIIGradeLabel.setText("YTD Rating:");
		yearToDateCIIGradeLabel.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		
		ciiGradeLabel = new Label(this, SWT.NONE);
		ciiGradeLabel.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		setGrade();
	}

	private void setGrade() {
		String resultingCIIGradeLabelTest = UNDEFINED_CII_GRADE_LABEL_TEXT;
		
		final Vessel vessel = eVesselCharter.getVessel();
		final CIIStartOptions ciiStartOptions = eVesselCharter.getCiiStartOptions();
		if (vessel != null && ciiStartOptions != null) {
			final double ciiValue = UtilsCII.findCII(vessel, ciiStartOptions.getYearToDateEmissions(), ciiStartOptions.getYearToDateDistance());
			resultingCIIGradeLabelTest = UtilsCII.getLetterGrade(vessel, ciiValue, Year.from(eVesselCharter.getStartBy()));
		}
		
		ciiGradeLabel.setText(resultingCIIGradeLabelTest);
	}
}
