/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.transformer.ui.views;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.DestinationType;
import com.mmxlabs.models.lng.analytics.ShippingCostRow;
import com.mmxlabs.models.lng.analytics.manipulators.DestinationTypeAttributeManipulator;
import com.mmxlabs.models.lng.ui.actions.ScenarioModifyingAction;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.ui.dates.DateAttributeManipulator;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.NumericAttributeManipulator;
import com.mmxlabs.models.ui.tabular.SingleReferenceManipulator;
import com.mmxlabs.scenario.service.model.ScenarioLock;

/**
 * A viewer pane for editing shipping cost rows.
 * 
 * @author Simon Goodall
 * 
 */
public class ShippingCostRowViewerPane extends ScenarioTableViewerPane {

	public ShippingCostRowViewerPane(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);
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
		addTypicalColumn("Port", new SingleReferenceManipulator(AnalyticsPackage.eINSTANCE.getShippingCostRow_Port(), getReferenceValueProviderCache(), getEditingDomain()));
		addTypicalColumn("Date", new DateAttributeManipulator(AnalyticsPackage.eINSTANCE.getShippingCostRow_Date(), getEditingDomain()));
		addTypicalColumn("Gas Price", new NumericAttributeManipulator(AnalyticsPackage.eINSTANCE.getShippingCostRow_CargoPrice(), getEditingDomain()) {
			@Override
			public boolean canEdit(final Object object) {

				if (object instanceof ShippingCostRow) {
					if (((ShippingCostRow) object).getDestinationType() == DestinationType.END) {
						return false;
					}
				}

				return super.canEdit(object);
			}
		});
		addTypicalColumn("Gas CV", new NumericAttributeManipulator(AnalyticsPackage.eINSTANCE.getShippingCostRow_CvValue(), getEditingDomain()) {
			@Override
			public boolean canEdit(final Object object) {

				if (object instanceof ShippingCostRow) {
					if (((ShippingCostRow) object).getDestinationType() == DestinationType.END) {
						return false;
					}
				}

				return super.canEdit(object);
			}
		});
		addTypicalColumn("Gas Volume", new NumericAttributeManipulator(AnalyticsPackage.eINSTANCE.getShippingCostRow_HeelVolume(), getEditingDomain()) {
			@Override
			public boolean canEdit(final Object object) {

				if (object instanceof ShippingCostRow) {
					if (((ShippingCostRow) object).getDestinationType() == DestinationType.END) {
						return false;
					}
				}

				return super.canEdit(object);
			}
		});
		addTypicalColumn("Type", new DestinationTypeAttributeManipulator(AnalyticsPackage.eINSTANCE.getShippingCostRow_DestinationType(), getEditingDomain()));
		// Disable sorting
		getScenarioViewer().setComparator(null);
	}

	@Override
	protected Action createDeleteAction() {
		return new ScenarioModifyingAction("Delete") {
			{
				setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
				setDisabledImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE_DISABLED));
				viewer.addSelectionChangedListener(this);
			}

			@Override
			public void run() {

				// Delete commands can be slow, so show the busy indicator while deleting.
				final Runnable runnable = new Runnable() {

					@Override
					public void run() {

						final ScenarioLock editorLock = jointModelEditorPart.getEditorLock();
						editorLock.awaitClaim();
						getJointModelEditorPart().setDisableUpdates(true);
						try {
							final ISelection sel = getLastSelection();
							if (sel instanceof IStructuredSelection) {
								final EditingDomain ed = jointModelEditorPart.getEditingDomain();
								// Copy selection
								@SuppressWarnings("unchecked")
								final List<?> objects = new ArrayList<Object>(((IStructuredSelection) sel).toList());

								// Clear current selection
								selectionChanged(new SelectionChangedEvent(viewer, StructuredSelection.EMPTY));

								// Execute command
								final Command deleteCommand = DeleteCommand.create(ed, objects);
								ed.getCommandStack().execute(deleteCommand);
							}
						} finally {
							editorLock.release();
							getJointModelEditorPart().setDisableUpdates(false);
						}
					}
				};
				BusyIndicator.showWhile(null, runnable);
			}

			@Override
			protected boolean isApplicableToSelection(final ISelection selection) {
				if (selection.isEmpty() == false && selection instanceof IStructuredSelection) {
					final IStructuredSelection iStructuredSelection = (IStructuredSelection) selection;
					final Iterator<?> itr = iStructuredSelection.iterator();

					while (itr.hasNext()) {
						final Object obj = itr.next();
						if (obj instanceof ShippingCostRow) {
							final ShippingCostRow shippingCostRow = (ShippingCostRow) obj;
							if (shippingCostRow.getDestinationType() == DestinationType.START) {
								return false;
							}
							if (shippingCostRow.getDestinationType() == DestinationType.END) {
								return false;
							}
						} else {
							return false;
						}
					}
					return true;
				}
				return false;
			}
		};
	}
}
