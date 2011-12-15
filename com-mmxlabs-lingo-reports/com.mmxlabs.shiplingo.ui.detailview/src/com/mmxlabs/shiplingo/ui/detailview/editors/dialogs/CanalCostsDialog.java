/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.ui.detailview.editors.dialogs;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

import scenario.fleet.FleetPackage;
import scenario.fleet.VesselClass;
import scenario.fleet.VesselClassCost;
import scenario.port.Canal;
import scenario.port.CanalModel;

import com.mmxlabs.lngscheduler.emf.extras.EMFUtils;
import com.mmxlabs.shiplingo.ui.tableview.EObjectTableViewer;
import com.mmxlabs.shiplingo.ui.tableview.NonEditableColumn;
import com.mmxlabs.shiplingo.ui.tableview.NumericAttributeManipulator;

/**
 * A dialog for editing the canal costs associated with a vessel class
 * 
 * Displays a table in which there is a row for each canal, and a column for
 * each attribute.
 * 
 * @author Tom Hinton
 * 
 */
public class CanalCostsDialog extends Dialog {
	private EObjectTableViewer tableViewer;
	private CanalModel canalModel;
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

		tableViewer.init(adapterFactory, containment);

		final Grid table = tableViewer.getGrid();
		final TableLayout layout = new TableLayout();
		table.setLayout(layout);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		tableViewer.addTypicalColumn("Canal", new NonEditableColumn() {
			@Override
			public String render(Object object) {
				return ((VesselClassCost) object).getCanal().getName();
			}
		});

		tableViewer.addTypicalColumn(
				"Laden Cost",
				new NumericAttributeManipulator(FleetPackage.eINSTANCE
						.getVesselClassCost_LadenCost(), editingDomain));
		tableViewer.addTypicalColumn(
				"Unladen Cost",
				new NumericAttributeManipulator(FleetPackage.eINSTANCE
						.getVesselClassCost_UnladenCost(), editingDomain));
		tableViewer.addTypicalColumn(
				"Transit Time",
				new NumericAttributeManipulator(FleetPackage.eINSTANCE
						.getVesselClassCost_TransitTime(), editingDomain));
		tableViewer.addTypicalColumn(
				"Transit Base Fuel",
				new NumericAttributeManipulator(FleetPackage.eINSTANCE
						.getVesselClassCost_TransitFuel(), editingDomain));

		tableViewer.setInput(container);

		tableViewer.refresh();
		
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		return content;
	}

	public int open(final AdapterFactory adapterFactory, final EditingDomain editingDomain,
			final CanalModel canalModel, final EObject container, final EReference containment) {
		this.canalModel = canalModel;

		this.container = EcoreUtil.copy(container);
		this.containment = containment;
		
		final EList<VesselClassCost> costs = (EList<VesselClassCost>) this.container.eGet(containment);
		
		for (final Canal c : canalModel.getCanals()) {
			boolean occurs = false;
			for (final VesselClassCost cost : costs) {
				if (cost.getCanal() == c) {
					occurs = true;
					break;
				}
			}
			if (occurs == false) {
				// add a cost for this canal to the costs list.
				final VesselClassCost newCost = (VesselClassCost) EMFUtils
						.createEObject(FleetPackage.eINSTANCE
								.getVesselClassCost());
				
				newCost.setCanal(c);
				costs.add(newCost);
			}
		}
		
		this.editingDomain = editingDomain;
		this.adapterFactory = adapterFactory;

		return super.open();
	}
	
	public EList<VesselClassCost> getResult() {
		return (EList<VesselClassCost>) this.container.eGet(containment);
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	@Override
	public void create() {
		super.create();
		getShell().setText("Canal Costs for " + ((VesselClass) container).getName());
	}
}
