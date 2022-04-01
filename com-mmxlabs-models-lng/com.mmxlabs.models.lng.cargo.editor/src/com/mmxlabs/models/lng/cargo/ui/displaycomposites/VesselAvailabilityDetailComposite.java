/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.displaycomposites;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.ui.editors.IDisplayCompositeLayoutProvider;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.editors.dialogs.IDialogEditingContext;
import com.mmxlabs.models.ui.impl.DefaultDetailComposite;
import com.mmxlabs.models.ui.impl.RowGroupDisplayCompositeLayoutProviderBuilder;

/**
 * Detail composite for vessel state attributes; adds an additional bit to the
 * bottom of the composite which contains a fuel curve table.
 * 
 * @author alex, FM
 * 
 */
public class VesselAvailabilityDetailComposite extends DefaultDetailComposite {

	private final VesselAvailabilityDetailGroup detailGroup;
	private final IDialogEditingContext dialogContext;

	public enum VesselAvailabilityDetailGroup {
		TOP_LEFT, TOP_LEFT_EXTRA, TOP_RIGHT, TOP_RIGHT_EXTRA, GENERAL, START, END;
	}

	public VesselAvailabilityDetailComposite(Composite parent, int style, FormToolkit toolkit, VesselAvailabilityDetailGroup detailGroup, IDialogEditingContext dialogContext) {
		super(parent, style, toolkit);
		this.detailGroup = detailGroup;
		this.dialogContext = dialogContext;
		this.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
	}

	@Override
	public IInlineEditor addInlineEditor(final IInlineEditor editor) {

		if (editor == null) {
			return null;
		}

		// By default all elements are in the main tab
		VesselAvailabilityDetailGroup cdg = VesselAvailabilityDetailGroup.GENERAL;

		if (editor.getFeature() == CargoPackage.eINSTANCE.getVesselAvailability_Vessel() || editor.getFeature() == CargoPackage.eINSTANCE.getVesselAvailability_CharterNumber()
				|| editor.getFeature() == CargoPackage.eINSTANCE.getVesselAvailability_TimeCharterRate() || editor.getFeature() == CargoPackage.eINSTANCE.getVesselAvailability_MinDuration()
				|| editor.getFeature() == CargoPackage.eINSTANCE.getVesselAvailability_MaxDuration()) {
			cdg = VesselAvailabilityDetailGroup.TOP_LEFT;
		}

		if (editor.getFeature() == CargoPackage.eINSTANCE.getVesselAvailability_Optional() || editor.getFeature() == CargoPackage.eINSTANCE.getVesselAvailability_Entity()
				|| editor.getFeature() == CargoPackage.eINSTANCE.getVesselAvailability_TimeCharterRate()) {
			cdg = VesselAvailabilityDetailGroup.TOP_RIGHT;
		}

		if (dialogContext != null && !dialogContext.isMultiEdit() && editor.getFeature() == CargoPackage.eINSTANCE.getVesselAvailability_GenericCharterContract()) {
			cdg = VesselAvailabilityDetailGroup.TOP_LEFT;
		}

		// Here the exceptions are listed for the elements which should go into the
		// middle composite
		if (editor.getFeature() == CargoPackage.eINSTANCE.getVesselAvailability_StartAt() || editor.getFeature() == CargoPackage.eINSTANCE.getVesselAvailability_StartAfter()
				|| editor.getFeature() == CargoPackage.eINSTANCE.getVesselAvailability_StartBy()) {
			cdg = VesselAvailabilityDetailGroup.START;
		}

		// Here the exceptions are listed for the elements which should go into the
		// bottom
		if (editor.getFeature() == CargoPackage.eINSTANCE.getVesselAvailability_EndAt() || editor.getFeature() == CargoPackage.eINSTANCE.getVesselAvailability_EndAfter()
				|| editor.getFeature() == CargoPackage.eINSTANCE.getVesselAvailability_EndBy()) {
			cdg = VesselAvailabilityDetailGroup.END;
		}

		// Do not add elements if they are for the wrong section.
		if (detailGroup != cdg) {
			// Rejected...
			return null;
		}

		return super.addInlineEditor(editor);
	}

	protected IDisplayCompositeLayoutProvider createLayoutProvider() {

		return new RowGroupDisplayCompositeLayoutProviderBuilder() //
				.withRow() //
				.withLabel("Vessel") //
				.withFeature(CargoPackage.Literals.VESSEL_AVAILABILITY__VESSEL) //
				.withFeature(CargoPackage.Literals.VESSEL_AVAILABILITY__CHARTER_NUMBER) //
				.makeRow() //
				//
				.withRow() //
				.withFeature(CargoPackage.Literals.VESSEL_AVAILABILITY__START_AFTER, "After (UTC)") //
				.withFeature(CargoPackage.Literals.VESSEL_AVAILABILITY__START_BY, "By") //
				.makeRow() //
				//
				.withRow() //
				.withFeature(CargoPackage.Literals.VESSEL_AVAILABILITY__END_AFTER, "After (UTC)") //
				.withFeature(CargoPackage.Literals.VESSEL_AVAILABILITY__END_BY, "By") //
				.makeRow() //
				//
				.withRow() //
				.withFeature(CargoPackage.Literals.VESSEL_AVAILABILITY__MIN_DURATION, "Duration") //
				.withFeature(CargoPackage.Literals.VESSEL_AVAILABILITY__MAX_DURATION, " up to ") //
				.makeRow() //
				//
				.withRow() //
				.withLabel("Charter terms") //
				.withFeature(CargoPackage.Literals.VESSEL_AVAILABILITY__GENERIC_CHARTER_CONTRACT, "Duration") //
				.makeRow() //
				//
				.make() //
		;
	}

}