/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.displaycomposites;

import org.eclipse.emf.ecore.EClass;
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
public class VesselCharterDetailComposite extends DefaultDetailComposite {

	private final VesselCharterDetailGroup detailGroup;
	private final IDialogEditingContext dialogContext;

	public enum VesselCharterDetailGroup {
		TOP_LEFT, TOP_LEFT_EXTRA, TOP_RIGHT, TOP_RIGHT_EXTRA, GENERAL, START, END;
	}

	public VesselCharterDetailComposite(Composite parent, int style, FormToolkit toolkit, VesselCharterDetailGroup detailGroup, IDialogEditingContext dialogContext) {
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
		VesselCharterDetailGroup cdg = VesselCharterDetailGroup.GENERAL;

		if (editor.getFeature() == CargoPackage.eINSTANCE.getVesselCharter_Vessel() || editor.getFeature() == CargoPackage.eINSTANCE.getVesselCharter_CharterNumber()
				|| editor.getFeature() == CargoPackage.eINSTANCE.getVesselCharter_TimeCharterRate() || editor.getFeature() == CargoPackage.eINSTANCE.getVesselCharter_MinDuration()
				|| editor.getFeature() == CargoPackage.eINSTANCE.getVesselCharter_MaxDuration()) {
			cdg = VesselCharterDetailGroup.TOP_LEFT;
		}

		if (editor.getFeature() == CargoPackage.eINSTANCE.getVesselCharter_Optional() || editor.getFeature() == CargoPackage.eINSTANCE.getVesselCharter_Entity()
				|| editor.getFeature() == CargoPackage.eINSTANCE.getVesselCharter_TimeCharterRate()) {
			cdg = VesselCharterDetailGroup.TOP_RIGHT;
		}

		if (dialogContext != null && !dialogContext.isMultiEdit() && editor.getFeature() == CargoPackage.eINSTANCE.getVesselCharter_GenericCharterContract()) {
			cdg = VesselCharterDetailGroup.TOP_LEFT;
		}

		// Here the exceptions are listed for the elements which should go into the
		// middle composite
		if (editor.getFeature() == CargoPackage.eINSTANCE.getVesselCharter_StartAt() || editor.getFeature() == CargoPackage.eINSTANCE.getVesselCharter_StartAfter()
				|| editor.getFeature() == CargoPackage.eINSTANCE.getVesselCharter_StartBy()) {
			cdg = VesselCharterDetailGroup.START;
		}

		// Here the exceptions are listed for the elements which should go into the
		// bottom
		if (editor.getFeature() == CargoPackage.eINSTANCE.getVesselCharter_EndAt() || editor.getFeature() == CargoPackage.eINSTANCE.getVesselCharter_EndAfter()
				|| editor.getFeature() == CargoPackage.eINSTANCE.getVesselCharter_EndBy()) {
			cdg = VesselCharterDetailGroup.END;
		}

		// Do not add elements if they are for the wrong section.
		if (detailGroup != cdg) {
			// Rejected...
			return null;
		}

		return super.addInlineEditor(editor);
	}

	@Override
	protected IDisplayCompositeLayoutProvider createLayoutProvider(final EClass eClass) {

		return new RowGroupDisplayCompositeLayoutProviderBuilder() //
				.withRow() //
				.withLabel("Vessel") //
				.withFeature(CargoPackage.Literals.VESSEL_CHARTER__VESSEL) //
				.withFeature(CargoPackage.Literals.VESSEL_CHARTER__CHARTER_NUMBER) //
				.makeRow() //
				//
				.withRow() //
				.withFeature(CargoPackage.Literals.VESSEL_CHARTER__START_AFTER, "After (UTC)") //
				.withFeature(CargoPackage.Literals.VESSEL_CHARTER__START_BY, "By") //
				.makeRow() //
				//
				.withRow() //
				.withFeature(CargoPackage.Literals.VESSEL_CHARTER__END_AFTER, "After (UTC)") //
				.withFeature(CargoPackage.Literals.VESSEL_CHARTER__END_BY, "By") //
				.makeRow() //
				//
				.withRow() //
				.withFeature(CargoPackage.Literals.VESSEL_CHARTER__MIN_DURATION, "Duration") //
				.withFeature(CargoPackage.Literals.VESSEL_CHARTER__MAX_DURATION, " up to ") //
				.makeRow() //
				//
				.withRow() //
				.withLabel("Charter terms") //
				.withFeature(CargoPackage.Literals.VESSEL_CHARTER__GENERIC_CHARTER_CONTRACT, "Duration") //
				.makeRow() //
				//
				.make() //
		;
	}

}