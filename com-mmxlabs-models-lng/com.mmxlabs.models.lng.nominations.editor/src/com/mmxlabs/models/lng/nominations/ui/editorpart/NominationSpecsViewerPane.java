/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.nominations.ui.editorpart;

import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.models.lng.nominations.DatePeriodPrior;
import com.mmxlabs.models.lng.nominations.NominationsPackage;
import com.mmxlabs.models.lng.nominations.Side;
import com.mmxlabs.models.lng.nominations.utils.NominationsModelUtils;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.BooleanFlagAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.TextualEnumAttributeManipulator;
import com.mmxlabs.scenario.service.model.manager.ModelReference;

public class NominationSpecsViewerPane extends AbstractNominationsViewerPane {

	public NominationSpecsViewerPane(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final ModelReference modelReference) {
		super.init(path, adapterFactory, modelReference);

		setMinToolbarHeight(30);
				
		addTypicalColumn("Type", new BasicAttributeManipulator(NominationsPackage.eINSTANCE.getAbstractNominationSpec_Type(), getCommandHandler()));
		addTypicalColumn("Side", new TextualEnumAttributeManipulator(NominationsPackage.eINSTANCE.getAbstractNominationSpec_Side(), getCommandHandler(), e -> NominationsModelUtils.mapName((Side) e)));
		addTypicalColumn("Contract", new BasicAttributeManipulator(NominationsPackage.eINSTANCE.getAbstractNominationSpec_RefererId(), getCommandHandler()));
		addTypicalColumn("Day of month", new NumericAttributeManipulator(NominationsPackage.eINSTANCE.getAbstractNominationSpec_DayOfMonth(), getCommandHandler()));
		addTypicalColumn("C/P", new BooleanFlagAttributeManipulator(NominationsPackage.eINSTANCE.getAbstractNominationSpec_Counterparty(), getCommandHandler()));
		addTypicalColumn("Period", new NumericAttributeManipulator(NominationsPackage.eINSTANCE.getAbstractNominationSpec_Size(), getCommandHandler()));
		addTypicalColumn("Units", new TextualEnumAttributeManipulator(NominationsPackage.eINSTANCE.getAbstractNominationSpec_SizeUnits(), getCommandHandler(), e -> NominationsModelUtils.mapName((DatePeriodPrior) e)));
		addTypicalColumn("Alert", new NumericAttributeManipulator(NominationsPackage.eINSTANCE.getAbstractNominationSpec_AlertSize(), getCommandHandler()));
		addTypicalColumn("Units", new TextualEnumAttributeManipulator(NominationsPackage.eINSTANCE.getAbstractNominationSpec_AlertSizeUnits(), getCommandHandler(), e -> NominationsModelUtils.mapName((DatePeriodPrior) e)));
		addTypicalColumn("Remark", new BasicAttributeManipulator(NominationsPackage.eINSTANCE.getAbstractNominationSpec_Remark(), getCommandHandler()));

		setTitle("Nomination Specifications", PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_DEF_VIEW));
	}
}
