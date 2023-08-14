/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.displaycomposites.cii;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.ui.editors.IDisplayCompositeLayoutProvider;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultDetailComposite;
import com.mmxlabs.models.ui.impl.RowGroupDisplayCompositeLayoutProviderBuilder;

public class CIIEndOptionsDetailComposite extends DefaultDetailComposite {

	public CIIEndOptionsDetailComposite(Composite parent, int style, FormToolkit toolkit) {
		super(parent, style, toolkit);
	}

	@Override
	protected IDisplayCompositeLayoutProvider createLayoutProvider(final EClass eClass) {
		return new RowGroupDisplayCompositeLayoutProviderBuilder() //
				.withRow() //
				.withFeature(CargoPackage.Literals.CII_END_OPTIONS__DESIRED_CII_GRADE, "Rating", 150) //
				.makeRow() //
				.make();
	}
	
	@Override
	public @Nullable IInlineEditor addInlineEditor(IInlineEditor editor) {
		if (editor.getFeature().equals(CargoPackage.Literals.CII_END_OPTIONS__DESIRED_CII_GRADE)) {
			return super.addInlineEditor(new CIIEndOptionsInlineEditor());
		}
		return super.addInlineEditor(editor);
	}
}
