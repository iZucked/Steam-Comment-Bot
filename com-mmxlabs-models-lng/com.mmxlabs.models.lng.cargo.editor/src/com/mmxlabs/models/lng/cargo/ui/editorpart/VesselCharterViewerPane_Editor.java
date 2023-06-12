/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.port.ui.editorpart.MultiplePortReferenceManipulator;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.date.DateTimeFormatsProvider;
import com.mmxlabs.models.ui.date.LocalDateTimeTextFormatter;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.BooleanAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.LocalDateTimeAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.ReadOnlyManipulatorWrapper;
import com.mmxlabs.models.ui.tabular.manipulators.SingleReferenceManipulator;
import com.mmxlabs.scenario.service.model.manager.ModelReference;

public class VesselCharterViewerPane_Editor extends ScenarioTableViewerPane {

	public VesselCharterViewerPane_Editor(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final ModelReference modelReference) {
		super.init(path, adapterFactory, modelReference);
		final ICommandHandler commandHandler = scenarioEditingLocation.getDefaultCommandHandler();
		final String dateTimeFormat = DateTimeFormatsProvider.INSTANCE.getDateTimeStringEdit();
		final String dateFormat = DateTimeFormatsProvider.INSTANCE.getDateStringEdit();
		ReadOnlyManipulatorWrapper<BasicAttributeManipulator> nameManipulator = new ReadOnlyManipulatorWrapper<>(
				new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getNamedObject_Name(), commandHandler));

		addTypicalColumn("Name", nameManipulator, CargoPackage.eINSTANCE.getVesselCharter_Vessel());

		addTypicalColumn("Optional", new BooleanAttributeManipulator(CargoPackage.eINSTANCE.getVesselCharter_Optional(), commandHandler));

		addTypicalColumn("Charter", new BasicAttributeManipulator(CargoPackage.eINSTANCE.getVesselCharter_TimeCharterRate(), commandHandler));

		addTypicalColumn("Start Port", new SingleReferenceManipulator(CargoPackage.eINSTANCE.getVesselCharter_StartAt(), scenarioEditingLocation.getReferenceValueProviderCache(),
				commandHandler));

		addTypicalColumn("From", new LocalDateTimeAttributeManipulator(CargoPackage.eINSTANCE.getVesselCharter_StartAfter(), commandHandler, new LocalDateTimeTextFormatter(dateTimeFormat,dateFormat)));

		addTypicalColumn("To", new LocalDateTimeAttributeManipulator(CargoPackage.eINSTANCE.getVesselCharter_StartBy(), commandHandler, new LocalDateTimeTextFormatter(dateTimeFormat,dateFormat)));

		addTypicalColumn("End Port", new MultiplePortReferenceManipulator(CargoPackage.eINSTANCE.getVesselCharter_EndAt(), scenarioEditingLocation.getReferenceValueProviderCache(),
				commandHandler, MMXCorePackage.eINSTANCE.getNamedObject_Name()));

		addTypicalColumn("From", new LocalDateTimeAttributeManipulator(CargoPackage.eINSTANCE.getVesselCharter_EndAfter(), commandHandler, new LocalDateTimeTextFormatter(dateTimeFormat,dateFormat)));

		addTypicalColumn("To", new LocalDateTimeAttributeManipulator(CargoPackage.eINSTANCE.getVesselCharter_EndBy(), commandHandler, new LocalDateTimeTextFormatter(dateTimeFormat,dateFormat)));

		setTitle("Vessel Charters", PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_DEF_VIEW));
	}
}
