/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.presentation.model_editors;

import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPage;

import scenario.cargo.CargoPackage;
import scenario.presentation.LngEditorPlugin;
import scenario.presentation.ScenarioEditor;
import scenario.presentation.cargoeditor.handlers.ReplicateCargoAction;
import scenario.presentation.cargoeditor.handlers.SwapDischargeSlotsAction;
import scenario.presentation.cargoeditor.handlers.debug.FixNamesAction;
import scenario.presentation.cargoeditor.handlers.debug.PerturbCargoesAction;
import scenario.presentation.cargoeditor.wiringeditor.WiringDialog;

import com.mmxlabs.shiplingo.ui.tableview.BasicAttributeManipulator;
import com.mmxlabs.shiplingo.ui.tableview.CargoInitialVesselManipulator;
import com.mmxlabs.shiplingo.ui.tableview.DateManipulator;
import com.mmxlabs.shiplingo.ui.tableview.EObjectTableViewer;
import com.mmxlabs.shiplingo.ui.tableview.EnumAttributeManipulator;
import com.mmxlabs.shiplingo.ui.tableview.MultipleReferenceManipulator;
import com.mmxlabs.shiplingo.ui.tableview.SingleReferenceManipulator;

/**
 * EVP for cargoes
 * 
 * @author Tom Hinton
 * 
 */
public class CargoEVP extends ScenarioObjectEditorViewerPane {
	public CargoEVP(final IWorkbenchPage page, final ScenarioEditor part) {
		super(page, part);
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory) {
		super.init(path, adapterFactory);
		final CargoPackage cargoPackage = CargoPackage.eINSTANCE;

		{
			final BasicAttributeManipulator id = new BasicAttributeManipulator(cargoPackage.getCargo_Id(), part.getEditingDomain());
			addColumn("ID", id, id);
		}

		{
			final EnumAttributeManipulator type = new EnumAttributeManipulator(cargoPackage.getCargo_CargoType(), part.getEditingDomain());
			addColumn("Type", type, type);
		}

		{
			final SingleReferenceManipulator port = new SingleReferenceManipulator(cargoPackage.getSlot_Port(), part.getPortProvider(), part.getEditingDomain());
			addColumn("Load Port", port, port, cargoPackage.getCargo_LoadSlot());
		}

		{
			final DateManipulator date = new DateManipulator(cargoPackage.getSlot_WindowStart(), part.getEditingDomain());
			addColumn("Load Date", date, date, cargoPackage.getCargo_LoadSlot());
		}

		{
			final SingleReferenceManipulator port = new SingleReferenceManipulator(cargoPackage.getSlot_Contract(), part.getContractProvider(), part.getEditingDomain());

			addColumn("Load Contract", port, port, cargoPackage.getCargo_LoadSlot());
		}

		{
			final SingleReferenceManipulator port = new SingleReferenceManipulator(cargoPackage.getSlot_Port(), part.getPortProvider(), part.getEditingDomain());
			addColumn("Discharge Port", port, port, cargoPackage.getCargo_DischargeSlot());
		}
		{
			final DateManipulator date = new DateManipulator(cargoPackage.getSlot_WindowStart(), part.getEditingDomain());
			addColumn("Discharge Date", date, date, cargoPackage.getCargo_DischargeSlot());
		}

		{
			final SingleReferenceManipulator port = new SingleReferenceManipulator(cargoPackage.getSlot_Contract(), part.getContractProvider(), part.getEditingDomain());
			addColumn("Discharge Contract", port, port, cargoPackage.getCargo_DischargeSlot());
		}

		{
			final MultipleReferenceManipulator av = new MultipleReferenceManipulator(cargoPackage.getCargo_AllowedVessels(), part.getEditingDomain(), part.getVesselProvider(), namedObjectName);
			addTypicalColumn("Restrict To", av);
		}

		{
			final CargoInitialVesselManipulator civm = new CargoInitialVesselManipulator(part.getEditingDomain());
			addTypicalColumn("Initial Vessel", civm);
		}
	}

	final SwapDischargeSlotsAction swapAction = new SwapDischargeSlotsAction();
	final ReplicateCargoAction replicateAction = new ReplicateCargoAction();

	@Override
	public EObjectTableViewer createViewer(final Composite parent) {
		final EObjectTableViewer v = super.createViewer(parent);

		getToolBarManager().appendToGroup("edit", swapAction);
		getToolBarManager().appendToGroup("edit", replicateAction);

		if (LngEditorPlugin.DEBUG_UI_ENABLED) {
			final PerturbCargoesAction perturb = new PerturbCargoesAction();
			getToolBarManager().appendToGroup("edit", perturb);
			v.addSelectionChangedListener(perturb);

			final FixNamesAction fixNames = new FixNamesAction();
			getToolBarManager().appendToGroup("edit", fixNames);
			v.addSelectionChangedListener(fixNames);
		}

		getToolBarManager().update(true);

		v.addSelectionChangedListener(swapAction);

		v.getControl().addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(final KeyEvent e) {
			}

			@Override
			public void keyReleased(final KeyEvent e) {

				if (e.keyCode == ' ') {
					if (isLockedForEditing()) {
						return;
					}
					final ISelection selection = v.getSelection();
					if (selection instanceof IStructuredSelection) {
						final IStructuredSelection ssel = (IStructuredSelection) selection;
						final List l = Arrays.asList(ssel.toArray());

						final WiringDialog wiringDialog = new WiringDialog(v.getControl().getShell());

						wiringDialog.open(l, part.getEditingDomain(), part.getPortProvider());
					}
				}
			}
		});

		return v;
	}

	@Override
	public void dispose() {

		getViewer().removeSelectionChangedListener(swapAction);

		super.dispose();
	}
}
