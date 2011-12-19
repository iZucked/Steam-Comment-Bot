/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.model_editors;

import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.action.ControlContribution;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPage;

import scenario.cargo.CargoPackage;
import scenario.presentation.ScenarioEditor;
import scenario.presentation.cargoeditor.handlers.ReplicateCargoAction;
import scenario.presentation.cargoeditor.handlers.SwapDischargeSlotsAction;
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
	public CargoEVP(IWorkbenchPage page, ScenarioEditor part) {
		super(page, part);
	}

	@Override
	public void init(List<EReference> path, AdapterFactory adapterFactory) {
		super.init(path, adapterFactory);
		final CargoPackage cargoPackage = CargoPackage.eINSTANCE;

		{
			final BasicAttributeManipulator id = new BasicAttributeManipulator(
					cargoPackage.getCargo_Id(), part.getEditingDomain());
			addColumn("ID", id, id);
		}

		{
			final EnumAttributeManipulator type = new EnumAttributeManipulator(
					cargoPackage.getCargo_CargoType(), part.getEditingDomain());
			addColumn("Type", type, type);
		}

		{
			final SingleReferenceManipulator port = new SingleReferenceManipulator(
					cargoPackage.getSlot_Port(), part.getPortProvider(),
					part.getEditingDomain());
			addColumn("Load Port", port, port,
					cargoPackage.getCargo_LoadSlot());
		}

		{
			final DateManipulator date = new DateManipulator(
					cargoPackage.getSlot_WindowStart(), part.getEditingDomain());
			addColumn("Load Date", date, date,
					cargoPackage.getCargo_LoadSlot());
		}

		{
			final SingleReferenceManipulator port = new SingleReferenceManipulator(
					cargoPackage.getSlot_Contract(), part.getContractProvider(),
					part.getEditingDomain());

			addColumn("Load Contract", port, port,
					cargoPackage.getCargo_LoadSlot());
		}

		{
			final SingleReferenceManipulator port = new SingleReferenceManipulator(
					cargoPackage.getSlot_Port(), part.getPortProvider(),
					part.getEditingDomain());
			addColumn("Discharge Port", port, port,
					cargoPackage.getCargo_DischargeSlot());
		}
		{
			final DateManipulator date = new DateManipulator(
					cargoPackage.getSlot_WindowStart(), part.getEditingDomain());
			addColumn("Discharge Date", date, date,
					cargoPackage.getCargo_DischargeSlot());
		}

		{
			final SingleReferenceManipulator port = new SingleReferenceManipulator(
					cargoPackage.getSlot_Contract(), part.getContractProvider(),
					part.getEditingDomain());
			addColumn("Discharge Contract", port, port,
					cargoPackage.getCargo_DischargeSlot());
		}

		{
			final MultipleReferenceManipulator av = new MultipleReferenceManipulator(
					cargoPackage.getCargo_AllowedVessels(),
					part.getEditingDomain(), part.getVesselProvider(), namedObjectName);
			addTypicalColumn("Restrict To", av);
		}

		{
			final CargoInitialVesselManipulator civm = new CargoInitialVesselManipulator(
					part.getEditingDomain());
			addTypicalColumn("Initial Vessel", civm);
		}
	}
	
	final SwapDischargeSlotsAction swapAction = new SwapDischargeSlotsAction();
	final ReplicateCargoAction replicateAction = new ReplicateCargoAction();

	@Override
	public EObjectTableViewer createViewer(final Composite parent) {
		final EObjectTableViewer v = super.createViewer(parent);

//		getToolBarManager().appendToGroup("edit", new ControlContribution("com.mmxlabs.cargoviewer.filter") {
//			@Override
//			protected Control createControl(final Composite parent) {
//				final Text t = new Text(parent, SWT.SEARCH);
//				t.addModifyListener(new ModifyListener() {
//					@Override
//					public void modifyText(ModifyEvent e) {
//						v.refresh(false);
//					}
//				});
//				v.addFilter(new ViewerFilter() {
//					@Override
//					public boolean select(Viewer viewer, Object parentElement, Object element) {
//						final String filterText = t.getText();
//						if (filterText.isEmpty()) return true;
//						if (element instanceof EObject) {
//							if (filter((EObject) element, filterText)) return true;
//							final TreeIterator<EObject> iterator = ((EObject)element).eAllContents();
//							while (iterator.hasNext()) {
//								if (filter(iterator.next(), filterText)) return true;
//							}
//							return false;
//						}
//						
//						return true;
//					}
//
//					private boolean filter(EObject element, String text) {
//						for (final EAttribute attribute : element.eClass().getEAllAttributes()) {
//							final String s = "" + element.eGet(attribute);
//							if (s.toLowerCase().contains(text.toLowerCase())) return true;
//						}
//
//						return false;
//					}});
//				
//				return t;
//			}
//		});
		
		getToolBarManager().appendToGroup("edit", swapAction);
		getToolBarManager().appendToGroup("edit", replicateAction);
		getToolBarManager().update(true);

		v.addSelectionChangedListener(swapAction);

		v.getControl().addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(final KeyEvent e) {
			}

			@Override
			public void keyReleased(final KeyEvent e) {

				if (e.keyCode == ' ') {
					final ISelection selection = v.getSelection();
					if (selection instanceof IStructuredSelection) {
						final IStructuredSelection ssel = (IStructuredSelection) selection;
						final List l = Arrays.asList(ssel.toArray());

						final WiringDialog wiringDialog = new WiringDialog(
								v.getControl().getShell());

						wiringDialog.open(l, part.getEditingDomain(),
								part.getPortProvider());
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
