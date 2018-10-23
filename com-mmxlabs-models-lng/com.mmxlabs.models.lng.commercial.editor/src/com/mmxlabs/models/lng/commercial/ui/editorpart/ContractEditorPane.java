/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.ui.editorpart;

import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.ui.editorpart.VolumeAttributeManipulator;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.ExpressionPriceParameters;
import com.mmxlabs.models.lng.commercial.ui.manipulators.ContractTypeEnumAttributeManipulator;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.BooleanAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.DateTimeAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.SingleReferenceManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.TextualEnumAttributeManipulator;
import com.mmxlabs.scenario.service.model.manager.ModelReference;

public class ContractEditorPane extends ScenarioTableViewerPane {
	public ContractEditorPane(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final ModelReference modelReference) {
		super.init(path, adapterFactory, modelReference);
		addNameManipulator("Name");

		addTypicalColumn("Entity", new SingleReferenceManipulator(CommercialPackage.eINSTANCE.getContract_Entity(), getReferenceValueProviderCache(), getEditingDomain()));
		addTypicalColumn("Type", new ContractTypeEnumAttributeManipulator(CommercialPackage.eINSTANCE.getContract_ContractType(), getEditingDomain()));
		addTypicalColumn("Volume", new VolumeAttributeManipulator(CommercialPackage.eINSTANCE.getContract_MaxQuantity(), getEditingDomain()));
		addTypicalColumn("Price", new BasicAttributeManipulator(CommercialPackage.eINSTANCE.getExpressionPriceParameters_PriceExpression(), getEditingDomain()) {
			@Override
			public @Nullable String render(Object object) {
				if (!(object instanceof ExpressionPriceParameters)) {
					return "n/a";
				}
				return super.render(object);
			}
		}, CommercialPackage.eINSTANCE.getContract_PriceInfo());
		
		if(LicenseFeatures.isPermitted("features:nominations")) {
			addTypicalColumn("Date Nom", new NumericAttributeManipulator(CommercialPackage.eINSTANCE.getContract_WindowNominationSize(), getEditingDomain()));
			addTypicalColumn("Date Nom Units", new TextualEnumAttributeManipulator(CommercialPackage.eINSTANCE.getContract_WindowNominationSizeUnits(), getEditingDomain(),
				(e) -> mapName((TimePeriod) e)));
		}

		defaultSetTitle("Contracts");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.models.lng.types.ui.tabular.ScenarioTableViewerPane#defaultSetTitle(java.lang.String)
	 */
	@Override
	public void defaultSetTitle(final String string) {
		super.defaultSetTitle(string);
	}
	
	private static String mapName(final TimePeriod units) {

		switch (units) {
		case DAYS:
			return "Days";
		case HOURS:
			return "Hours";
		case MONTHS:
			return "Months";
		default:
			break;
		}
		return units.getName();
	}

}
