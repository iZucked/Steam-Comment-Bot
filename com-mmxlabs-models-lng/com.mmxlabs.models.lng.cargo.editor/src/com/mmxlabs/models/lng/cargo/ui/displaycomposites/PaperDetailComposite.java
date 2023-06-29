/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.displaycomposites;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.google.common.collect.Lists;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.editors.IDisplayCompositeLayoutProvider;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultDetailComposite;
import com.mmxlabs.models.ui.impl.RowGroupDisplayCompositeLayoutProviderBuilder;

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
//		if (editor.getFeature() == CargoPackage.eINSTANCE.getPaperDeal_StartDate()
//				|| editor.getFeature() == CargoPackage.eINSTANCE.getPaperDeal_EndDate()) {
//			cdg = PaperDetailGroup.CALENDAR;
//		}

		return super.addInlineEditor(editor);
	}
	
	@Override
	protected void sortEditors(List<IInlineEditor> editors) {
		// Sub classes can sort the editor list prior to rendering
		List<ETypedElement> orderedFeatures = Lists.newArrayList( //
				MMXCorePackage.Literals.NAMED_OBJECT__NAME, //
				
				cp.getPaperDeal_Quantity(), //
				cp.getPaperDeal_Entity(), //
				
				cp.getPaperDeal_Price(), //
				cp.getPaperDeal_Index(), //				
				
				cp.getPaperDeal_PricingType(), //
				cp.getPaperDeal_Instrument(), //
				
				cp.getPaperDeal_PricingMonth(), //
				cp.getPaperDeal_Year(), //
				
				cp.getPaperDeal_PricingPeriodStart(), //
				cp.getPaperDeal_PricingPeriodEnd(), //
				
				cp.getPaperDeal_HedgingPeriodStart(), //
				cp.getPaperDeal_HedgingPeriodEnd(), //

				
				
				cp.getPaperDeal_Comment()
		);

		// Reverse the list so that we can move the editors to the head of the list
		Collections.reverse(orderedFeatures);
		for (var feature : orderedFeatures) {
			for (var editor : editors) {
				if (editor.getFeature() == feature) {
					editors.remove(editor);
					editors.add(0, editor);
					break;
				}
			}
		}

	}
	
	private static final CargoPackage cp = CargoPackage.eINSTANCE;
	
	@Override
	protected IDisplayCompositeLayoutProvider createLayoutProvider(final EClass eClass) {

		return new RowGroupDisplayCompositeLayoutProviderBuilder() //
//				.withRow() //
//				.withFeature(MMXCorePackage.Literals.NAMED_OBJECT__NAME) //
//				.makeRow() //
				//
				.withRow() //
				.withFeature(cp.getPaperDeal_Quantity())
				.withFeature(cp.getPaperDeal_Entity())
				.makeRow() //
				//
				.withRow() //
				.withFeature(cp.getPaperDeal_Price(), "Price")
				.withFeature(cp.getPaperDeal_Index(), "MTM Curve")
				.makeRow() //
				//
				.withRow() //
				.withFeature(cp.getPaperDeal_PricingType())
				.withFeature(cp.getPaperDeal_Instrument())
				.makeRow() //
				//
				.withRow() //
				.withFeature(cp.getPaperDeal_PricingMonth())
				.withFeature(cp.getPaperDeal_Year())
				.makeRow() //
				//
				.withRow() //
				.withFeature(cp.getPaperDeal_PricingPeriodStart(), "Pricing from")
				.withFeature(cp.getPaperDeal_PricingPeriodEnd(), "to")
				.makeRow() //
				//
				.withRow() //
				.withFeature(cp.getPaperDeal_HedgingPeriodStart(), "Hedging from")
				.withFeature(cp.getPaperDeal_HedgingPeriodEnd(), "to")
				.makeRow() //
				//
				.make() //
		;
	}
}
