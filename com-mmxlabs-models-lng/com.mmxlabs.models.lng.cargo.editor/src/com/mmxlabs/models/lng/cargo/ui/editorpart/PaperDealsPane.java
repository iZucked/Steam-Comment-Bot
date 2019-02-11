/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.BuyPaperDeal;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.PaperDeal;
import com.mmxlabs.models.lng.cargo.SellPaperDeal;
import com.mmxlabs.models.lng.cargo.ui.editorpart.CreatePaperStripDialog.StripType;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.DefaultMenuCreatorAction;
import com.mmxlabs.models.lng.cargo.ui.editorpart.events.IVesselEventsTableContextMenuExtension;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.ui.ImageConstants;
import com.mmxlabs.models.lng.ui.LngUIActivator;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.ui.tabular.manipulators.LocalDateAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.StringAttributeManipulator;
import com.mmxlabs.rcp.common.actions.LockableAction;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.ScenarioLock;

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
		
		
		final CreatePaperStripMenuAction cpsma = new CreatePaperStripMenuAction("Bulk add");
		
		getToolBarManager().appendToGroup(VIEW_GROUP, cpsma);
		getToolBarManager().update(true);
		
	}
	
	private class CreatePaperStripMenuAction extends DefaultMenuCreatorAction{

		public CreatePaperStripMenuAction(String label) {
			super(label);
			setImageDescriptor(LngUIActivator.getDefault().getImageRegistry().getDescriptor(ImageConstants.IMAGE_DUPLICATE));
		}

		@Override
		protected void populate(Menu menu) {
			final CreateStripAction csabp = new CreateStripAction("Buy Paper", StripType.TYPE_PAPER_BUY);
			final CreateStripAction csasp = new CreateStripAction("Sell Paper", StripType.TYPE_PAPER_SELL);
			addActionToMenu(csabp, menu);
			addActionToMenu(csasp, menu);
		}
	}
	
	private class CreateStripAction extends LockableAction {

		private final StripType stripType;

		public CreateStripAction(final String text, final StripType stripType) {
			super(text);
			this.stripType = stripType;
		}

		@Override
		public void run() {

			final ScenarioLock editorLock = scenarioEditingLocation.getEditorLock();
			try {
				editorLock.lock();
				try {
					scenarioEditingLocation.setDisableUpdates(true);

					final MMXRootObject rootObject = scenarioEditingLocation.getRootObject();
					if (rootObject instanceof LNGScenarioModel) {

						PaperDeal selectedDeal = null;
						final ISelection selection = PaperDealsPane.this.getScenarioViewer().getSelection();
						if (selection instanceof IStructuredSelection) {
							final IStructuredSelection ss = (IStructuredSelection) selection;

							final Iterator<?> itr = ss.iterator();
							while (itr.hasNext()) {
								Object o = itr.next();

								if (o instanceof PaperDeal) {
									selectedDeal = (PaperDeal) o;
								}
							}
						}

						final LNGScenarioModel scenarioModel = (LNGScenarioModel) rootObject;
						final CreatePaperStripDialog d = new CreatePaperStripDialog(scenarioEditingLocation.getShell(), scenarioEditingLocation, stripType, selectedDeal) {
							@Override
							protected void configureShell(final Shell newShell) {
								newShell.setMinimumSize(SWT.DEFAULT, 630);
								super.configureShell(newShell);
							}
						};
						if (Window.OK == d.open()) {
							final Command cmd = d.createStrip(scenarioModel.getCargoModel(), getEditingDomain());
							if (cmd.canExecute()) {
								getEditingDomain().getCommandStack().execute(cmd);
							}
						}

					} else {
						setEnabled(false);
					}

				} finally {
					scenarioEditingLocation.setDisableUpdates(false);
				}
			} finally {
				editorLock.unlock();
			}
		}
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
