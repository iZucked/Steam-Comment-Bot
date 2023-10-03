package com.mmxlabs.models.lng.cargo.presentation.composites;

import java.time.Year;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.swt.widgets.Label;

import com.mmxlabs.models.lng.cargo.CIIStartOptions;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.schedule.cii.UtilsCII;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.editors.impl.ReadOnlyInlineEditorWrapper;

public class CIIYearToDateGradeInlineEditorWrapper extends ReadOnlyInlineEditorWrapper {
	
	private static final String UNDEFINED_CII_GRADE_LABEL_TEXT = "-";
	
	private boolean enabled = false;
	private IDialogEditingContext dialogContext = null;
	private MMXRootObject scenario;
	private Collection<EObject> range = null;

	protected CIIYearToDateGradeInlineEditorWrapper(final @NonNull IInlineEditor wrapped) {
		super(wrapped);
	}
	
	@Override
	protected boolean isEnabled() {
		return enabled;
	}
	
	@Override
	public void display(IDialogEditingContext dialogContext, MMXRootObject scenario, EObject object, Collection<EObject> range) {
		
		this.dialogContext = dialogContext;
		this.scenario = scenario;
		this.range = range;

		enabled = true;
		if (object instanceof final CIIStartOptions options && options.eContainer() instanceof final VesselCharter vesselCharter) {
			
			if (wrapped instanceof CIIYearToDateGradeInlineEditor editor) {
				updateCIIGrade(vesselCharter, editor);
			}
		}
		super.display(dialogContext, scenario, object, range);
	}
	
	private void updateCIIGrade(final VesselCharter vesselCharter, final CIIYearToDateGradeInlineEditor editor) {
		String resultingCIIGradeLabelTest = UNDEFINED_CII_GRADE_LABEL_TEXT;
		
		final Vessel vessel = vesselCharter.getVessel();
		final CIIStartOptions ciiStartOptions = vesselCharter.getCiiStartOptions();
		if (vessel != null && ciiStartOptions != null && vesselCharter.getStartBy() != null) {
			final double ciiValue = UtilsCII.findCII(vessel, ciiStartOptions.getYearToDateEmissions(), ciiStartOptions.getYearToDateDistance());
			resultingCIIGradeLabelTest = UtilsCII.getLetterGrade(vessel, ciiValue, Year.from(vesselCharter.getStartBy()));
		}
		editor.setGrade(resultingCIIGradeLabelTest);
	}

	@Override
	protected boolean respondToNotification(final Notification notification) {
		final EObject object = (EObject) notification.getNotifier();
		if (notification.getFeature() == CargoPackage.eINSTANCE.getCIIStartOptions_YearToDateDistance() ||
				notification.getFeature() == CargoPackage.eINSTANCE.getCIIStartOptions_YearToDateEmissions() ||
				notification.getFeature() == CargoPackage.eINSTANCE.getVesselCharter_Vessel()) {
			if (object instanceof final CIIStartOptions options && options.eContainer() instanceof final VesselCharter vesselCharter) {
				if (wrapped instanceof CIIYearToDateGradeInlineEditor editor) {

					updateCIIGrade(vesselCharter, editor);
					
					dialogContext.getDialogController().setEditorVisibility(object, getFeature(), true);
					dialogContext.getDialogController().updateEditorVisibility();
					super.display(dialogContext, scenario, input, range);
					Label label = getLabel();
					if (label != null) {
						label.pack();
					}
					dialogContext.getDialogController().relayout();
					return true;
				}
			}
		}
				
		return false;
	}
}
