/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.ui.editorpart;

import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.port.util.PortModelLabeller;
import com.mmxlabs.models.lng.pricing.CostModel;
import com.mmxlabs.models.lng.pricing.PanamaCanalTariff;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.RouteCost;
import com.mmxlabs.models.lng.pricing.SuezCanalTariff;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewer;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.EmbeddedDetailComposite;
import com.mmxlabs.models.ui.tabular.NonEditableColumn;
import com.mmxlabs.models.ui.tabular.manipulators.MultipleReferenceManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;
import com.mmxlabs.rcp.common.ecore.SafeEContentAdapter;
import com.mmxlabs.scenario.service.model.manager.ModelReference;

/**
 * Quick hack for vessel route cost editing
 * 
 * @author hinton
 * 
 */
public class CanalCostsPane extends ScenarioTableViewerPane {

	private EmbeddedDetailComposite suezDetailComposite;
	private EmbeddedDetailComposite panamaDetailComposite;

	private CostModel costModel;

	private CTabItem fixedCostsTabItem;

	public CanalCostsPane(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final ModelReference modelReference) {
		super.init(path, adapterFactory, modelReference);

		final NonEditableColumn routeManipulator = new NonEditableColumn() {
			@Override
			public String render(final Object object) {
				if (object instanceof RouteCost routeCost) {
					final RouteOption routeOption = routeCost.getRouteOption();
					if (routeOption != null) {
						return PortModelLabeller.getName(routeOption);
					}
				}
				return "Unknown";
			}

		};

		addTypicalColumn("Route", routeManipulator);
		addTypicalColumn("Vessels",
				new MultipleReferenceManipulator(PricingPackage.Literals.ROUTE_COST__VESSELS, getReferenceValueProviderCache(), getCommandHandler(), MMXCorePackage.eINSTANCE.getNamedObject_Name()));
		addTypicalColumn("Laden Toll", new NumericAttributeManipulator(PricingPackage.Literals.ROUTE_COST__LADEN_COST, getCommandHandler()));
		addTypicalColumn("Ballast Toll", new NumericAttributeManipulator(PricingPackage.Literals.ROUTE_COST__BALLAST_COST, getCommandHandler()));

		defaultSetTitle("Canal Costs");

		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "com.mmxlabs.lingo.doc.Editor_CanalCosts");
	}

	private final @NonNull AdapterImpl changeListener = new SafeEContentAdapter() {
		public void safeNotifyChanged(final org.eclipse.emf.common.notify.Notification msg) {

			if (msg.isTouch()) {
				return;
			}

			// This is here for e.g. Canal Cost DB updater to ensure we refresh properly
			if (msg.getFeature() == PricingPackage.Literals.COST_MODEL__SUEZ_CANAL_TARIFF || msg.getFeature() == PricingPackage.Literals.COST_MODEL__PANAMA_CANAL_TARIFF) {
				final CostModel cm = (CostModel) msg.getNotifier();
				setInput(cm, cm.getSuezCanalTariff(), cm.getPanamaCanalTariff());
			}
		}
	};

	@Override
	public ScenarioTableViewer createViewer(final Composite parent) {

		final CTabFolder folder = new CTabFolder(parent, SWT.BOTTOM);
		folder.setLayout(new GridLayout(1, true));
		folder.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());

		{
			final CTabItem tabItem = new CTabItem(folder, SWT.NONE);
			tabItem.setText("Suez");
			suezDetailComposite = new EmbeddedDetailComposite(folder, scenarioEditingLocation);
			tabItem.setControl(suezDetailComposite.getComposite());
		}

		// Display first tab
		folder.setSelection(0);

		{
			final CTabItem tabItem = new CTabItem(folder, SWT.NONE);
			tabItem.setText("Panama");
			panamaDetailComposite = new EmbeddedDetailComposite(folder, scenarioEditingLocation);
			tabItem.setControl(panamaDetailComposite.getComposite());
		}
		{
			CTabItem tabItem = new CTabItem(folder, SWT.NONE);
			tabItem.setText("Fixed costs");
			tabItem.setToolTipText("Legacy fixed canal costs");

			final ScenarioTableViewer v = super.createViewer(folder);
			tabItem.setControl(v.getControl());

			fixedCostsTabItem = tabItem;
			return v;
		}
	}

	public void setInput(final CostModel costModel, final SuezCanalTariff suezCanalTariff, final PanamaCanalTariff panamaCanalTariff) {

		if (this.costModel != null) {
			this.costModel.eAdapters().remove(changeListener);
		}

		this.costModel = costModel;
		if (!costModel.getRouteCosts().isEmpty()) {
			getViewer().setInput(costModel);
		} else {
			if (fixedCostsTabItem != null) {
				fixedCostsTabItem.dispose();
				fixedCostsTabItem = null;
			}

		}
		suezDetailComposite.setInput(suezCanalTariff);
		panamaDetailComposite.setInput(panamaCanalTariff);

		if (this.costModel != null) {
			this.costModel.eAdapters().add(changeListener);
		}
	}

	@Override
	public void dispose() {

		if (this.costModel != null) {
			this.costModel.eAdapters().remove(changeListener);
		}
		this.costModel = null;

		super.dispose();
	}

}
