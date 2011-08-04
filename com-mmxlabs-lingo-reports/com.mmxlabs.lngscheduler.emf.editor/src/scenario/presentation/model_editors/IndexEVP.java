/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.model_editors;

import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.ui.IWorkbenchPage;

import scenario.market.MarketPackage;
import scenario.presentation.ScenarioEditor;

import com.mmxlabs.shiplingo.ui.tableview.NumericAttributeManipulator;

/**
 * EVP for indices.
 * 
 * @author Tom Hinton
 *
 */
public class IndexEVP extends NamedObjectEVP {
	public IndexEVP(IWorkbenchPage page, ScenarioEditor part) {
		super(page, part);
	}

	@Override
	public void init(List<EReference> path, AdapterFactory adapterFactory) {
		super.init(path, adapterFactory);

		final NumericAttributeManipulator defaultValue = new NumericAttributeManipulator(
				MarketPackage.eINSTANCE.getStepwisePriceCurve_DefaultValue(),
				part.getEditingDomain());

		addColumn("Default Value", defaultValue, defaultValue,
				MarketPackage.eINSTANCE.getIndex_PriceCurve());
	}
}
