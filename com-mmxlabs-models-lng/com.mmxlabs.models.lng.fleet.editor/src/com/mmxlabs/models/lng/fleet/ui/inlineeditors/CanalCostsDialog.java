/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.ui.inlineeditors;

import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselClassRouteParameters;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.tabular.EObjectTableViewer;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;

/**
 * A dialog for editing the canal costs associated with a vessel class
 * 
 * Displays a table in which there is a row for each canal, and a column for each attribute.
 * 
 * @author Tom Hinton
 * 
 */
public class CanalCostsDialog extends Dialog {
	private EObjectTableViewer tableViewer;

	private EObject container;
	private EReference containment;

	private AdapterFactory adapterFactory;
	private EditingDomain editingDomain;

	public CanalCostsDialog(final Shell parentShell) {
		super(parentShell);
	}

	public CanalCostsDialog(final IShellProvider parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createDialogArea(final Composite parent) {
		final Composite content = (Composite) super.createDialogArea(parent);
		tableViewer = new EObjectTableViewer(content, SWT.FULL_SELECTION | SWT.BORDER);

		tableViewer.init(adapterFactory, editingDomain.getCommandStack(), containment);

		final Grid table = tableViewer.getGrid();

		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setRowHeaderVisible(true);
		tableViewer.setRowHeaderLabelProvider(new CellLabelProvider() {
			@Override
			public void update(final ViewerCell cell) {
				cell.setText(((VesselClassRouteParameters) cell.getElement()).getRoute().getName());
			}
		});

		tableViewer.addTypicalColumn("Transit Time", new NumericAttributeManipulator(FleetPackage.eINSTANCE.getVesselClassRouteParameters_ExtraTransitTime(), editingDomain));
		tableViewer.addTypicalColumn("Laden Consumption Rate", new NumericAttributeManipulator(FleetPackage.eINSTANCE.getVesselClassRouteParameters_LadenConsumptionRate(), editingDomain));
		tableViewer.addTypicalColumn("Ballast Consumption Rate", new NumericAttributeManipulator(FleetPackage.eINSTANCE.getVesselClassRouteParameters_BallastConsumptionRate(), editingDomain));
		tableViewer.addTypicalColumn("Laden NBO Rate", new NumericAttributeManipulator(FleetPackage.eINSTANCE.getVesselClassRouteParameters_LadenNBORate(), editingDomain));
		tableViewer.addTypicalColumn("Ballast NBO Rate", new NumericAttributeManipulator(FleetPackage.eINSTANCE.getVesselClassRouteParameters_BallastNBORate(), editingDomain));

		tableViewer.setInput(container);

		tableViewer.refresh();

		table.setLayoutData(new GridData(GridData.FILL_BOTH));

		return content;
	}

	public int open(final AdapterFactory adapterFactory, final EditingDomain editingDomain, final MMXRootObject rootObject, final EObject container, final EReference containment) {
		this.container = EcoreUtil.copy(container);
		this.containment = containment;

		final List<VesselClassRouteParameters> l = (List<VesselClassRouteParameters>) this.container.eGet(containment);
		if (rootObject instanceof LNGScenarioModel) {
			final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
			final PortModel portModel = ScenarioModelUtil.getPortModel(lngScenarioModel);
			route_loop: for (final Route r : portModel.getRoutes()) {
				if (r.getRouteOption() == RouteOption.DIRECT) {
					continue route_loop;
				}
				for (final VesselClassRouteParameters vcrp : l) {
					if (vcrp.getRoute() == r) {
						continue route_loop;
					}
				}

				final VesselClassRouteParameters vcrp = FleetFactory.eINSTANCE.createVesselClassRouteParameters();
				vcrp.setRoute(r);
				l.add(vcrp);
			}
		}

		this.editingDomain = editingDomain;
		this.adapterFactory = adapterFactory;

		return super.open();
	}

	public EList<VesselClassRouteParameters> getResult() {
		return (EList<VesselClassRouteParameters>) this.container.eGet(containment);
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	@Override
	public void create() {
		super.create();
		getShell().setText("Canal Parameters for " + ((VesselClass) container).getName());
	}
}
