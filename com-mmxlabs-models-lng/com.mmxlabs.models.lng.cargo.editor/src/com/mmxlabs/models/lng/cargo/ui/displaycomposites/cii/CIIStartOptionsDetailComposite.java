/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.displaycomposites.cii;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.ui.editors.IDisplayCompositeLayoutProvider;
import com.mmxlabs.models.ui.impl.DefaultDetailComposite;
import com.mmxlabs.models.ui.impl.RowGroupDisplayCompositeLayoutProviderBuilder;

public class CIIStartOptionsDetailComposite extends DefaultDetailComposite {

	public CIIStartOptionsDetailComposite(Composite parent, int style, FormToolkit toolkit) {
		super(parent, style, toolkit);
	}

	@Override
	protected IDisplayCompositeLayoutProvider createLayoutProvider(final EClass eClass) {
		return new RowGroupDisplayCompositeLayoutProviderBuilder() //
				.withRow() //
				.withFeature(CargoPackage.Literals.CII_START_OPTIONS__YEAR_TO_DATE_DISTANCE, "YTD Distance", 75) //
				.withFeature(CargoPackage.Literals.CII_START_OPTIONS__YEAR_TO_DATE_EMISSIONS, "YTD Emissions", 130) //
				.withFeature(CargoPackage.eINSTANCE.getCIIStartOptions__YearToDatePartialCII())
				.makeRow() //
				.make() //
		;
	}

//	@Override
//	public @Nullable IInlineEditor addInlineEditor(IInlineEditor editor) {
//		if (editor.getFeature().equals(CargoPackage.Literals.CII_START_OPTIONS__YEAR_TO_DATE_DISTANCE) 
//			|| editor.getFeature().equals(CargoPackage.Literals.CII_START_OPTIONS__YEAR_TO_DATE_EMISSIONS)
//		) {
//			editor.addNotificationChangedListener(new IInlineEditorExternalNotificationListener() {
//				@Override
//				public void notifyChanged(Notification notification) {
//					System.err.println("AAAAAAAAAAAAAAAAA");
//				}
//				@Override
//				public void postDisplay(IInlineEditor editor, IDialogEditingContext context, MMXRootObject scenario, EObject object, Collection<EObject> range) {
//					System.err.println("BBBBBBBBBBBBBBBBB");
//				}
//			});;
//		}
//		return super.addInlineEditor(editor);
//	}
}
