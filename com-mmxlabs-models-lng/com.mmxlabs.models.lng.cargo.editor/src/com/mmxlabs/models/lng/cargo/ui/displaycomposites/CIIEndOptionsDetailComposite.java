package com.mmxlabs.models.lng.cargo.ui.displaycomposites;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.ui.editors.IDisplayCompositeLayoutProvider;
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
				.withFeature(CargoPackage.Literals.CII_END_OPTIONS__DESIRED_CII_GRADE, "Desired Grade", 75) //
				.makeRow() //
				.make();
	}
}
