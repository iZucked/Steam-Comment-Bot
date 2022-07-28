/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
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
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselRouteParameters;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.port.util.PortModelLabeller;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editors.DefaultCommandHandler;
import com.mmxlabs.models.ui.editors.ICommandHandler;
import com.mmxlabs.models.ui.tabular.EObjectTableViewer;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;
import com.mmxlabs.scenario.service.model.manager.ModelReference;

/**
 * A dialog for editing the canal costs associated with a vessel
 * 
 * Displays a table in which there is a row for each canal, and a column for each attribute.
 * 
 * @author Tom Hinton
 * 
 */
public class CanalParametersDialog extends Dialog {
	private EObjectTableViewer tableViewer;

	private EObject container;
	private EReference containment;

	private AdapterFactory adapterFactory;
	private ICommandHandler commandHandler;
	private ModelReference modelReference;

	public CanalParametersDialog(final Shell parentShell) {
		super(parentShell);
	}

	public CanalParametersDialog(final IShellProvider parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createDialogArea(final Composite parent) {
		final Composite content = (Composite) super.createDialogArea(parent);
		tableViewer = new EObjectTableViewer(content, SWT.FULL_SELECTION | SWT.BORDER);

		tableViewer.init(adapterFactory, modelReference, containment);

		final Grid table = tableViewer.getGrid();

		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setRowHeaderVisible(true);
		tableViewer.setRowHeaderLabelProvider(new CellLabelProvider() {
			@Override
			public void update(final ViewerCell cell) {
				VesselRouteParameters params = (VesselRouteParameters) cell.getElement();
				cell.setText(PortModelLabeller.getName(params.getRouteOption()));
			}
		});

		tableViewer.addTypicalColumn("Transit Time", new NumericAttributeManipulator(FleetPackage.eINSTANCE.getVesselRouteParameters_ExtraTransitTime(), commandHandler));
		tableViewer.addTypicalColumn("Laden Consumption Rate", new NumericAttributeManipulator(FleetPackage.eINSTANCE.getVesselRouteParameters_LadenConsumptionRate(), commandHandler));
		tableViewer.addTypicalColumn("Ballast Consumption Rate", new NumericAttributeManipulator(FleetPackage.eINSTANCE.getVesselRouteParameters_BallastConsumptionRate(), commandHandler));
		tableViewer.addTypicalColumn("Laden NBO Rate", new NumericAttributeManipulator(FleetPackage.eINSTANCE.getVesselRouteParameters_LadenNBORate(), commandHandler));
		tableViewer.addTypicalColumn("Ballast NBO Rate", new NumericAttributeManipulator(FleetPackage.eINSTANCE.getVesselRouteParameters_BallastNBORate(), commandHandler));

		tableViewer.setInput(container);

		tableViewer.refresh();

		table.setLayoutData(new GridData(GridData.FILL_BOTH));

		return content;
	}

	public int open(final AdapterFactory adapterFactory, final ModelReference modelReference, final MMXRootObject rootObject, final EObject container, final EReference containment) {
		this.container = EcoreUtil.copy(container);
		this.containment = containment;

		final List<VesselRouteParameters> l = (List<VesselRouteParameters>) this.container.eGet(containment);
		if (rootObject instanceof LNGScenarioModel) {
			final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
			final PortModel portModel = ScenarioModelUtil.getPortModel(lngScenarioModel);
			route_loop: for (final Route r : portModel.getRoutes()) {
				if (r.getRouteOption() == RouteOption.DIRECT) {
					continue route_loop;
				}
				for (final VesselRouteParameters vcrp : l) {
					if (vcrp.getRouteOption() == r.getRouteOption()) {
						continue route_loop;
					}
				}

				final VesselRouteParameters vcrp = FleetFactory.eINSTANCE.createVesselRouteParameters();
				vcrp.setRouteOption(r.getRouteOption());
				l.add(vcrp);
			}
		}

		this.modelReference = modelReference;
		this.commandHandler = new DefaultCommandHandler(modelReference, null);
		this.adapterFactory = adapterFactory;

		return super.open();
	}

	public EList<VesselRouteParameters> getResult() {
		return (EList<VesselRouteParameters>) this.container.eGet(containment);
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	@Override
	public void create() {
		super.create();
		getShell().setText("Canal Parameters for " + ((Vessel) container).getName());
	}
}
