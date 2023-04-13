/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.displaycomposites;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultDetailComposite;

public class PaperDetailComposite extends DefaultDetailComposite {

	public PaperDetailComposite(Composite parent, int style, FormToolkit toolkit) {
		super(parent, style, toolkit);
	}

	private PaperDetailGroup paperDetailGroup;
	
	public enum PaperDetailGroup{
		GENERAL, CALENDAR
	}

	@Override
	protected void setDefaultHelpContext(EObject object) {
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this, "com.mmxlabs.lingo.doc.DataModel_lng_paper");
	}

	@Override
	public IInlineEditor addInlineEditor(final IInlineEditor editor) {

		// By default all elements are in the main tab
		PaperDetailGroup cdg = PaperDetailGroup.GENERAL;

		// Here the exceptions are listed for the elements which should go into the bottom
		if (editor.getFeature() == CargoPackage.eINSTANCE.getPaperDeal_StartDate()
				|| editor.getFeature() == CargoPackage.eINSTANCE.getPaperDeal_EndDate()) {
			cdg = PaperDetailGroup.CALENDAR;
		}

		return super.addInlineEditor(editor);
	}
	
//	@Override
//	protected IDisplayCompositeLayoutProvider createLayoutProvider(final EClass eClass) {
//
//		return new RowGroupDisplayCompositeLayoutProviderBuilder() //
//				.withRow() //
//				.withFeature(MMXCorePackage.Literals.NAMED_OBJECT__NAME) //
//				.makeRow() //
//				//
//				.withRow() //
//				.withFeature(CargoPackage.Literals.PAPER_DEAL__PRICE, "Price")
//				.makeRow() //
//				//
//				.make() //
//		;
//
//	}
}
