/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.BuyPaperDeal;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.SellPaperDeal;
import com.mmxlabs.models.lng.cargo.ui.editorpart.events.IVesselEventsTableContextMenuExtension;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.ui.tabular.manipulators.LocalDateAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.StringAttributeManipulator;
import com.mmxlabs.scenario.service.model.manager.ModelReference;

public class PaperDealsPane extends ScenarioTableViewerPane {

	private final IScenarioEditingLocation jointModelEditor;
	private Iterable<IVesselEventsTableContextMenuExtension> contextMenuExtensions;

	public PaperDealsPane(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);
		this.jointModelEditor = location;
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final ModelReference modelReference) {
		super.init(path, adapterFactory, modelReference);

		addNameManipulator("Name");
		addColumn("Type", createPaperDealTypeFormatter(), null);
		addTypicalColumn("Start Date", new LocalDateAttributeManipulator(CargoPackage.eINSTANCE.getPaperDeal_StartDate(), jointModelEditor.getEditingDomain()));
		addTypicalColumn("End Date", new LocalDateAttributeManipulator(CargoPackage.eINSTANCE.getPaperDeal_EndDate(), jointModelEditor.getEditingDomain()));
		addTypicalColumn("Price", new NumericAttributeManipulator(CargoPackage.eINSTANCE.getPaperDeal_Price(), jointModelEditor.getEditingDomain()));
		addTypicalColumn("Index", new StringAttributeManipulator(CargoPackage.eINSTANCE.getPaperDeal_Index(), jointModelEditor.getEditingDomain()));
		addTypicalColumn("Quantity", new NumericAttributeManipulator(CargoPackage.eINSTANCE.getPaperDeal_Quantity(), jointModelEditor.getEditingDomain()));

		setTitle("Paper", PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_DEF_VIEW));
	}

	private ICellRenderer createPaperDealTypeFormatter() {
		return new ICellRenderer() {

			@Override
			public Comparable getComparable(Object object) {
				// TODO Auto-generated method stub
				return render(object);
			}

			@Override
			public @Nullable String render(Object object) {
				if (object instanceof BuyPaperDeal) {
					return "Buy";
				} else if (object instanceof SellPaperDeal) {
					return "Sell";
				}
				// TODO Auto-generated method stub
				return "";
			}

			@Override
			public boolean isValueUnset(Object object) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public @Nullable Object getFilterValue(Object object) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public @Nullable Iterable<Pair<Notifier, List<Object>>> getExternalNotifiers(Object object) {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}
}
