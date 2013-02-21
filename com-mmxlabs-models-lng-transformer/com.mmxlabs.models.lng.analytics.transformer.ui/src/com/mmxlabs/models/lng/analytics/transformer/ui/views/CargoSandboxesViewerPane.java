/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.transformer.ui.views;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySourceProvider;

import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.CargoSandbox;
import com.mmxlabs.models.lng.analytics.ProvisionalCargo;
import com.mmxlabs.models.lng.analytics.UnitCostLine;
import com.mmxlabs.models.lng.analytics.transformer.ICargoSandboxTransformer;
import com.mmxlabs.models.lng.analytics.transformer.impl.CargoSandboxTransformer;
import com.mmxlabs.models.lng.analytics.ui.properties.UnitCostLinePropertySource;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewer;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.dates.DateAttributeManipulator;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.modelfactories.IModelFactory;
import com.mmxlabs.models.ui.modelfactories.IModelFactory.ISetting;
import com.mmxlabs.models.ui.tabular.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.NumericAttributeManipulator;
import com.mmxlabs.models.ui.tabular.SingleReferenceManipulator;
import com.mmxlabs.scenario.service.model.ScenarioLock;

/**
 * A viewer pane for editing shipping cost rows.
 * 
 * @author Simon Goodall
 * 
 */
public class CargoSandboxesViewerPane extends ScenarioTableViewerPane {

	private final ICargoSandboxTransformer transformer = new CargoSandboxTransformer();

	private final IScenarioEditingLocation location;

	private Action addAction;

	private Action deleteAction;

	public CargoSandboxesViewerPane(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);
		this.location = location;
	}

	protected boolean showSelectionColumn() {
		return false;
	}

	protected boolean showEvaluateAction() {
		return true;
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory) {
		super.init(path, adapterFactory);
		addTypicalColumn("Buy Port", new SingleReferenceManipulator(AnalyticsPackage.eINSTANCE.getBuyOpportunity_Port(), getReferenceValueProviderCache(), getEditingDomain()),
				AnalyticsPackage.eINSTANCE.getProvisionalCargo_Buy());
		addTypicalColumn("Buy Expr", new BasicAttributeManipulator(AnalyticsPackage.eINSTANCE.getBuyOpportunity_PriceExpression(), getEditingDomain()),
				AnalyticsPackage.eINSTANCE.getProvisionalCargo_Buy());
		addTypicalColumn("Buy Date", new DateAttributeManipulator(AnalyticsPackage.eINSTANCE.getBuyOpportunity_Date(), getEditingDomain()), AnalyticsPackage.eINSTANCE.getProvisionalCargo_Buy());
		addTypicalColumn("Sell Port", new SingleReferenceManipulator(AnalyticsPackage.eINSTANCE.getSellOpportunity_Port(), getReferenceValueProviderCache(), getEditingDomain()),
				AnalyticsPackage.eINSTANCE.getProvisionalCargo_Sell());
		addTypicalColumn("Sell Expr", new BasicAttributeManipulator(AnalyticsPackage.eINSTANCE.getSellOpportunity_PriceExpression(), getEditingDomain()),
				AnalyticsPackage.eINSTANCE.getProvisionalCargo_Sell());
		addTypicalColumn("Sell Date", new DateAttributeManipulator(AnalyticsPackage.eINSTANCE.getSellOpportunity_Date(), getEditingDomain()), AnalyticsPackage.eINSTANCE.getProvisionalCargo_Sell());
		addTypicalColumn("Profit", new NumericAttributeManipulator(AnalyticsPackage.eINSTANCE.getUnitCostLine_Profit(), getEditingDomain()), AnalyticsPackage.eINSTANCE.getProvisionalCargo_CostLine());

		// addTypicalColumn("Date", new DateAttributeManipulator(AnalyticsPackage.eINSTANCE.getShippingCostRow_Date(), getEditingDomain()));
		// addTypicalColumn("Gas Price", new NumericAttributeManipulator(AnalyticsPackage.eINSTANCE.getShippingCostRow_CargoPrice(), getEditingDomain()) {
		// @Override
		// public boolean canEdit(final Object object) {
		//
		// if (object instanceof ShippingCostRow) {
		// if (((ShippingCostRow) object).getDestinationType() == DestinationType.END) {
		// return false;
		// }
		// }
		//
		// return super.canEdit(object);
		// }
		// });
		// addTypicalColumn("Gas CV", new NumericAttributeManipulator(AnalyticsPackage.eINSTANCE.getShippingCostRow_CvValue(), getEditingDomain()) {
		// @Override
		// public boolean canEdit(final Object object) {
		//
		// if (object instanceof ShippingCostRow) {
		// if (((ShippingCostRow) object).getDestinationType() == DestinationType.END) {
		// return false;
		// }
		// }
		//
		// return super.canEdit(object);
		// }
		// });
		// addTypicalColumn("Gas Volume", new NumericAttributeManipulator(AnalyticsPackage.eINSTANCE.getShippingCostRow_HeelVolume(), getEditingDomain()) {
		// @Override
		// public boolean canEdit(final Object object) {
		//
		// if (object instanceof ShippingCostRow) {
		// if (((ShippingCostRow) object).getDestinationType() == DestinationType.END) {
		// return false;
		// }
		// }
		//
		// return super.canEdit(object);
		// }
		// });
		// addTypicalColumn("Type", new DestinationTypeAttributeManipulator(AnalyticsPackage.eINSTANCE.getShippingCostRow_DestinationType(), getEditingDomain()));
		// Disable sorting
		// getScenarioViewer().setComparator(null);

		// Add action to create and edit cargo groups
		// getToolBarManager().appendToGroup(EDIT_GROUP, new Action() {
		// {
		// setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.models.lng.port.editor", "/icons/group.gif"));
		// }
		//
		// @Override
		// public void run() {
		// // final DetailCompositeDialog dcd = new DetailCompositeDialog(CargoSandboxesViewerPane.this.getJointModelEditorPart().getShell(), CargoSandboxesViewerPane.this.getJointModelEditorPart()
		// // .getDefaultCommandHandler());
		// // dcd.open(getJointModelEditorPart(), getJointModelEditorPart().getRootObject(), (EObject) viewer.getInput(), CargoPackage.eINSTANCE.getCargoModel_CargoGroups());
		//
		//
		//
		// }
		// });

		// create add actions
		final List<IModelFactory> factories = Activator.getDefault().getModelFactoryRegistry().getModelFactories(AnalyticsPackage.eINSTANCE.getCargoSandbox());
		if (factories.isEmpty() == false) {
			if (factories.size() == 1) {
				addAction = createAddAction(factories.get(0));
				// addAction.setEnabled(false);
				getToolBarManager().add(addAction);
			} else {
				// multi-adder //TODO
			}
		}

		deleteAction = new Action() {
			{
				setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
				viewer.addSelectionChangedListener(new ISelectionChangedListener() {
					@Override
					public void selectionChanged(final SelectionChangedEvent event) {
						setEnabled(event.getSelection().isEmpty() == false);
					}
				});
			}

			@Override
			public void run() {
				final ISelection selection = viewer.getSelection();
				if (selection instanceof IStructuredSelection) {
					final Object element = ((IStructuredSelection) selection).getFirstElement();
					getEditingDomain().getCommandStack().execute(DeleteCommand.create(getEditingDomain(), element));
					viewer.refresh();
				}
			}
		};
		deleteAction.setEnabled(false);
		getToolBarManager().add(deleteAction);

		getToolBarManager().update(true);

	}

	//
	// protected Action createDeleteAction() {
	// return new ScenarioModifyingAction("Delete") {
	// {
	// setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
	// setDisabledImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE_DISABLED));
	// viewer.addSelectionChangedListener(this);
	// }
	//
	// @Override
	// public void run() {
	//
	// // Delete commands can be slow, so show the busy indicator while deleting.
	// final Runnable runnable = new Runnable() {
	//
	// @Override
	// public void run() {
	//
	// final ScenarioLock editorLock = jointModelEditorPart.getEditorLock();
	// editorLock.awaitClaim();
	// getJointModelEditorPart().setDisableUpdates(true);
	// try {
	// final ISelection sel = getLastSelection();
	// if (sel instanceof IStructuredSelection) {
	// final EditingDomain ed = jointModelEditorPart.getEditingDomain();
	// // Copy selection
	// @SuppressWarnings("unchecked")
	// final List<?> objects = new ArrayList<Object>(((IStructuredSelection) sel).toList());
	//
	// // Clear current selection
	// selectionChanged(new SelectionChangedEvent(viewer, StructuredSelection.EMPTY));
	//
	// // Execute command
	// final Command deleteCommand = DeleteCommand.create(ed, objects);
	// ed.getCommandStack().execute(deleteCommand);
	// }
	// } finally {
	// editorLock.release();
	// getJointModelEditorPart().setDisableUpdates(false);
	// }
	// }
	// };
	// BusyIndicator.showWhile(null, runnable);
	// }
	//
	// @Override
	// protected boolean isApplicableToSelection(final ISelection selection) {
	// if (selection.isEmpty() == false && selection instanceof IStructuredSelection) {
	// final IStructuredSelection iStructuredSelection = (IStructuredSelection) selection;
	// final Iterator<?> itr = iStructuredSelection.iterator();
	//
	// while (itr.hasNext()) {
	// final Object obj = itr.next();
	// if (obj instanceof ShippingCostRow) {
	// final ShippingCostRow shippingCostRow = (ShippingCostRow) obj;
	// if (shippingCostRow.getDestinationType() == DestinationType.START) {
	// return false;
	// }
	// if (shippingCostRow.getDestinationType() == DestinationType.END) {
	// return false;
	// }
	// } else {
	// return false;
	// }
	// }
	// return true;
	// }
	// return false;
	// }
	// };
	// }

	private Action createAddAction(final IModelFactory factory) {
		return new Action("Create new " + factory.getLabel(), PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ADD)) {

			@Override
			public void run() {
				final MMXRootObject rootObject = location.getRootObject();
				final AnalyticsModel analyticsModel = rootObject.getSubModel(AnalyticsModel.class);

				final Collection<? extends ISetting> settings = factory.createInstance(rootObject, analyticsModel, AnalyticsPackage.eINSTANCE.getAnalyticsModel_CargoSandboxes(), null);
				if (settings.isEmpty()) {
					return;
				}

				final ScenarioLock editorLock = location.getEditorLock();
				try {
					editorLock.claim();
					location.setDisableUpdates(true);

					// now create an add command, which will include adding any
					// other relevant objects
					final CompoundCommand add = new CompoundCommand();
					for (final ISetting setting : settings) {
						// final DetailCompositeDialog dialog = new DetailCompositeDialog(location.getShell(), location.getDefaultCommandHandler());
						// if (dialog.open(CargoSandboxesViewerPane.this, rootObject, Collections.singletonList(instance)) == Window.OK) {
						add.append(AddCommand.create(getEditingDomain(), setting.getContainer(), setting.getContainment(), setting.getInstance()));
						// }
					}
					getEditingDomain().getCommandStack().execute(add);
					viewer.refresh();
				} finally {
					location.setDisableUpdates(false);
					editorLock.release();
				}
			}
		};
	}

	private <T> T createObject(final EClass clz, final EReference reference, final EObject container) {
		final List<IModelFactory> factories = Activator.getDefault().getModelFactoryRegistry().getModelFactories(clz);

		// TODO: Pre-generate and link to UI
		// TODO: Add FOB/DES etc as explicit slot types.
		final IModelFactory factory = factories.get(0);

		final Collection<? extends ISetting> settings = factory.createInstance(location.getRootObject(), container, reference, StructuredSelection.EMPTY);
		if (settings.isEmpty() == false) {

			for (final ISetting setting : settings) {

				return (T) setting.getInstance();
			}
		}
		return null;
	}

	public void setInput(final CargoSandbox plan) {
		// TODO Auto-generated method stub
		viewer.setInput(plan);
	}

	private class LocalScenarioTableViewer extends ScenarioTableViewer implements IPropertySourceProvider {

		public LocalScenarioTableViewer(final Composite parent, final int style, final IScenarioEditingLocation jointModelEditorPart) {
			super(parent, style, jointModelEditorPart);
		}

		@Override
		public IPropertySource getPropertySource(final Object object) {
			if (object instanceof ProvisionalCargo) {

				final ProvisionalCargo provisionalCargo = (ProvisionalCargo) object;
				UnitCostLine costLine = provisionalCargo.getCostLine();
				// if (costLine == null) {
				try {
					costLine = transformer.createCostLine(location.getRootObject(), (ProvisionalCargo) object);
					((ProvisionalCargo) object).setCostLine(costLine);
				} catch (final Exception e) {
					// Log it!
					e.printStackTrace();
				}
				// }
				if (costLine != null) {
					return new UnitCostLinePropertySource(costLine);
				}
			} else if (object instanceof IPropertySource) {
				return (IPropertySource) object;
			}
			return null;
		}

	};

	@Override
	protected ScenarioTableViewer constructViewer(final Composite parent) {
		return new LocalScenarioTableViewer(parent, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL, jointModelEditorPart);
	}
}
