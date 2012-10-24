/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.TreeSet;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.menus.IMenuService;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.SpotLoadSlot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.presentation.CargoEditorPlugin;
import com.mmxlabs.models.lng.cargo.ui.dialogs.WiringDiagram;
import com.mmxlabs.models.lng.cargo.ui.util.SpotSlotHelper;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.input.ElementAssignment;
import com.mmxlabs.models.lng.input.InputModel;
import com.mmxlabs.models.lng.input.editor.utils.AssignmentEditorHelper;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.pricing.DESPurchaseMarket;
import com.mmxlabs.models.lng.pricing.DESSalesMarket;
import com.mmxlabs.models.lng.pricing.FOBSalesMarket;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.SpotMarket;
import com.mmxlabs.models.lng.pricing.SpotMarketGroup;
import com.mmxlabs.models.lng.pricing.SpotType;
import com.mmxlabs.models.lng.types.APort;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewer;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.impl.MMXAdapterImpl;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.dates.DateAttributeManipulator;
import com.mmxlabs.models.ui.dates.LocalDateUtil;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialog;
import com.mmxlabs.models.ui.modelfactories.IModelFactory;
import com.mmxlabs.models.ui.modelfactories.IModelFactory.ISetting;
import com.mmxlabs.models.ui.tabular.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.EObjectTableViewerColumnProvider;
import com.mmxlabs.models.ui.tabular.ICellManipulator;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.ui.tabular.SingleReferenceManipulator;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;
import com.mmxlabs.models.util.emfpath.EMFPath;
import com.mmxlabs.rcp.common.actions.CopyGridToClipboardAction;
import com.mmxlabs.rcp.common.actions.CopyTableToClipboardAction;
import com.mmxlabs.rcp.common.actions.CopyTreeToClipboardAction;
import com.mmxlabs.rcp.common.actions.PackGridTableColumnsAction;

public class TradesWiringViewer extends ScenarioTableViewerPane {

	private CompoundCommand currentWiringCommand = null;

	// protected static final String VIEW_GROUP = "view";
	// protected static final String ADD_REMOVE_GROUP = "addremove";
	// protected static final String EDIT_GROUP = "edit";
	//
	// protected IScenarioEditingLocation location;

	// protected ToolBarManager toolBarManager;
	//
	// private EObjectTableViewer scenarioViewer;
	// // private final CargoWiringComposite wiringComposite;
	// // private final ToolBar actionBar;
	//
	// private FilterField filterField;
	// private ToolBarManager externalToolbarManager;
	//
	// private final IActionBars actionBars;
	// private Action deleteAction;

	private final Image wiredImage;
	private final Image lockedImage;

	private final Object updateLock = new Object();

	private final MMXAdapterImpl cargoChangeAdapter = new MMXAdapterImpl() {

		protected void missedNotifications(final List<Notification> missed) {
			Display.getDefault().asyncExec(new Runnable() {

				@Override
				public void run() {
					synchronized (updateLock) {
						refreshContent();
					}
				}
			});
		}

		@Override
		public synchronized void reallyNotifyChanged(final Notification notification) {
			Display.getDefault().asyncExec(new Runnable() {

				@Override
				public void run() {
					synchronized (updateLock) {
						localNotifyChanged(notification);
						// performControlUpdate(false);

					}
				}
			});
		}

		public synchronized void localNotifyChanged(final Notification notification) {

			if (notification.getEventType() == Notification.REMOVING_ADAPTER) {
				return;
			}
			//
			// boolean rowAdded = false;
			// boolean performUpdate = false;
			//
			// if (notification.getNotifier() instanceof Cargo) {
			// final Cargo cargo = (Cargo) notification.getNotifier();
			// // Check cargo wiring
			// if (notification.getFeature() == CargoPackage.eINSTANCE.getCargo_LoadSlot()) {
			// final Cargo c = (Cargo) notification.getNotifier();
			// final LoadSlot loadSlot = (LoadSlot) notification.getNewValue();
			//
			// final DischargeSlot dischargeSlot = c.getDischargeSlot();
			//
			// final int ret = performCargoUpdate(cargo, loadSlot, dischargeSlot);
			// if (ret == 2) {
			// rowAdded = true;
			// } else if (ret == 1) {
			// performUpdate = true;
			// }
			//
			// if (loadSlot == null) {
			// final LoadSlot oldLoadSlot = (LoadSlot) notification.getOldValue();
			// if (loadSlots.contains(oldLoadSlot)) {
			// final int loadIdx = loadSlots.indexOf(oldLoadSlot);
			// wiring.set(loadIdx, -1);
			// cargoes.set(loadIdx, null);
			// performUpdate = true;
			// }
			// }
			//
			// } else if (notification.getFeature() == CargoPackage.eINSTANCE.getCargo_DischargeSlot()) {
			// final Cargo c = (Cargo) notification.getNotifier();
			// final LoadSlot loadSlot = c.getLoadSlot();
			// final DischargeSlot dischargeSlot = (DischargeSlot) notification.getNewValue();
			//
			// final int ret = performCargoUpdate(cargo, loadSlot, dischargeSlot);
			// if (ret == 2) {
			// rowAdded = true;
			// } else if (ret == 1) {
			// performUpdate = true;
			// }
			// }
			// } else if (notification.getNotifier() instanceof CargoModel) {
			// if (notification.getFeature() == CargoPackage.eINSTANCE.getCargoModel_Cargoes()) {
			// if (notification.getEventType() == Notification.ADD) {
			// final Cargo cargo = (Cargo) notification.getNewValue();
			// cargo.eAdapters().add(cargoChangeAdapter);
			// final LoadSlot loadSlot = cargo.getLoadSlot();
			// final DischargeSlot dischargeSlot = cargo.getDischargeSlot();
			//
			// final int ret = performCargoUpdate(cargo, loadSlot, dischargeSlot);
			// if (ret == 2) {
			// rowAdded = true;
			// } else if (ret == 1) {
			// performUpdate = true;
			// }
			//
			// } else if (notification.getEventType() == Notification.REMOVE) {
			// final Cargo cargo = (Cargo) notification.getOldValue();
			// final LoadSlot loadSlot = cargo.getLoadSlot();
			// final DischargeSlot dischargeSlot = cargo.getDischargeSlot();
			//
			// final int ret = performCargoUpdate(null, loadSlot, dischargeSlot);
			// if (ret == 2) {
			// rowAdded = true;
			// } else if (ret == 1) {
			// performUpdate = true;
			// }
			// }
			// } else if (notification.getFeature() == CargoPackage.eINSTANCE.getCargoModel_LoadSlots()) {
			// if (notification.getEventType() == Notification.ADD) {
			// final LoadSlot loadSlot = (LoadSlot) notification.getNewValue();
			// final Cargo cargo = loadSlot.getCargo();
			// final DischargeSlot dischargeSlot = cargo != null ? cargo.getDischargeSlot() : null;
			//
			// final int ret = performCargoUpdate(cargo, loadSlot, dischargeSlot);
			// if (ret == 2) {
			// rowAdded = true;
			// } else if (ret == 1) {
			// performUpdate = true;
			// }
			// } else if (notification.getEventType() == Notification.REMOVE) {
			// final LoadSlot slot = (LoadSlot) notification.getOldValue();
			// if (loadSlots.contains(slot)) {
			// // Remove slot
			// final int index = loadSlots.indexOf(slot);
			// loadSlots.set(index, null);
			// // Remove the wiring
			// wiring.set(index, -1);
			// // Probably be handled later - but to be safe...
			// cargoes.set(index, null);
			// performUpdate = true;
			// }
			// }
			// } else if (notification.getFeature() == CargoPackage.eINSTANCE.getCargoModel_DischargeSlots()) {
			// if (notification.getEventType() == Notification.ADD) {
			// final DischargeSlot dischargeSlot = (DischargeSlot) notification.getNewValue();
			// final Cargo cargo = dischargeSlot.getCargo();
			// final LoadSlot loadSlot = cargo != null ? cargo.getLoadSlot() : null;
			//
			// final int ret = performCargoUpdate(cargo, loadSlot, dischargeSlot);
			// if (ret == 2) {
			// rowAdded = true;
			// } else if (ret == 1) {
			// performUpdate = true;
			// }
			// } else if (notification.getEventType() == Notification.REMOVE) {
			// final DischargeSlot slot = (DischargeSlot) notification.getOldValue();
			// if (dischargeSlots.contains(slot)) {
			// // Remove slot
			// final int index = dischargeSlots.indexOf(slot);
			// dischargeSlots.set(index, null);
			// // Remove the wiring
			// final int wiringIndex = wiring.indexOf(index);
			// if (wiringIndex != -1) {
			// wiring.set(wiringIndex, -1);
			// // Probably be handled later - but to be safe...
			// cargoes.set(wiringIndex, null);
			// }
			// performUpdate = true;
			// }
			// }
			// }
			// }
			//
			// // Perform a prune of empty rows
			// boolean rowRemoved = false;
			// for (int i = numberOfRows - 1; i >= 0; --i) {
			// if (loadSlots.get(i) == null && dischargeSlots.get(i) == null) {
			// cargoes.remove(i);
			// loadSlots.remove(i);
			// dischargeSlots.remove(i);
			//
			// // Re-index wiring
			// for (int j = 0; j < numberOfRows; ++j) {
			// final int idx = wiring.get(j);
			// if (idx > i) {
			// wiring.set(j, idx - 1);
			// }
			// }
			//
			// wiring.remove(i);
			// numberOfRows--;
			// performUpdate = true;
			// rowRemoved = true;
			// }
			// }
			//
			// if (rowAdded) {
			//
			// if (wiring.get(numberOfRows) == null) {
			// wiring.set(numberOfRows, -1);
			// }
			// numberOfRows++;
			//
			// // performControlUpdate(true);
			// } else if (rowRemoved) {
			//
			// // performControlUpdate(true);
			// } else if (performUpdate) {
			// wiringDiagram.setWiring(wiring);
			//
			// // performControlUpdate(false);
			// }

			performControlUpdate(false);
		}
	};

	//
	// private int performCargoUpdate(final Cargo cargo, final LoadSlot loadSlot, final DischargeSlot dischargeSlot) {
	//
	// int loadIdx = -1;
	// int dischargeIdx = -1;
	// boolean performUpdate = false;
	// boolean rowAdded = false;
	// if (loadSlot != null) {
	// if (loadSlots.contains(loadSlot)) {
	// loadIdx = loadSlots.indexOf(loadSlot);
	// } else {
	//
	// // If the load slot next to the discharge is empty, insert the load here rather than append to the end.
	// if (dischargeSlot != null && dischargeSlots.indexOf(dischargeSlot) != -1 && loadSlots.get(dischargeSlots.indexOf(dischargeSlot)) == null) {
	// loadIdx = dischargeSlots.indexOf(dischargeSlot);
	// loadSlots.set(loadIdx, loadSlot);
	// performUpdate = true;
	// } else {
	// loadIdx = numberOfRows;
	// ensureCapacity(numberOfRows + 1, cargoes, loadSlots, dischargeSlots, wiring);
	// wiring.set(numberOfRows, -1);
	//
	// loadSlots.set(loadIdx, loadSlot);
	// rowAdded = true;
	// }
	// }
	// } else if (cargo != null) {
	// final int oldIndex = loadSlots.indexOf(cargo.getLoadSlot());
	// if (oldIndex != -1) {
	// wiring.set(oldIndex, -1);
	// cargoes.set(oldIndex, null);
	// performUpdate = true;
	// }
	// }
	//
	// if (dischargeSlot != null) {
	//
	// if (dischargeSlots.contains(dischargeSlot)) {
	// dischargeIdx = dischargeSlots.indexOf(dischargeSlot);
	// } else {
	//
	// // If the discharge slot for an existing load is empty, then attempt to re-use it
	// if (!rowAdded && loadSlot != null && loadSlots.indexOf(loadSlot) != -1 && dischargeSlots.get(loadSlots.indexOf(loadSlot)) == null) {
	// dischargeIdx = loadSlots.indexOf(loadSlot);
	// dischargeSlots.set(dischargeIdx, dischargeSlot);
	// performUpdate = true;
	// } else {
	//
	// dischargeIdx = numberOfRows;
	// ensureCapacity(numberOfRows + 1, cargoes, loadSlots, dischargeSlots, wiring);
	//
	// dischargeSlots.set(dischargeIdx, dischargeSlot);
	// rowAdded = true;
	// }
	// }
	// } else if (cargo != null) {
	// final int oldIndex = loadSlots.indexOf(cargo.getLoadSlot());
	// if (oldIndex != -1) {
	// wiring.set(oldIndex, -1);
	// cargoes.set(oldIndex, null);
	// performUpdate = true;
	// }
	// }
	//
	// if (loadIdx != -1 && dischargeIdx != -1 && cargo != null) {
	// cargoes.set(loadIdx, cargo);
	// wiring.set(loadIdx, dischargeIdx);
	// performUpdate = true;
	// }
	//
	// if (cargo == null) {
	// if (loadIdx != -1) {
	// cargoes.set(loadIdx, null);
	// wiring.set(loadIdx, -1);
	// performUpdate = true;
	// }
	// }
	//
	// if (rowAdded) {
	// return 2;
	// } else if (performUpdate) {
	// return 1;
	// } else {
	// return 0;
	// }
	// }

	public TradesWiringViewer(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);
		// this.location = location;
		// this.actionBars = actionBars;

		wiredImage = CargoEditorPlugin.getPlugin().getImage(CargoEditorPlugin.IMAGE_CARGO_LINK);
		lockedImage = CargoEditorPlugin.getPlugin().getImage(CargoEditorPlugin.IMAGE_CARGO_LOCK);
	}

	@Override
	public void dispose() {

		super.dispose();
	}

	// public void setExternalToolBarManager(final ToolBarManager manager) {
	// this.externalToolbarManager = manager;
	// }
	//
	// @Override
	// public ToolBarManager getToolBarManager() {
	// if (externalToolbarManager != null) {
	// return externalToolbarManager;
	// } else {
	// return super.getToolBarManager();
	// }
	// }
	//
	// @Override
	// public EObjectTableViewer createViewer(final Composite parent) {
	// if (scenarioViewer == null) {
	// scenarioViewer = constructViewer(parent);
	//
	// scenarioViewer.addOpenListener(new IOpenListener() {
	//
	// @Override
	// public void open(final OpenEvent event) {
	// if (scenarioViewer.getSelection() instanceof IStructuredSelection) {
	// final IStructuredSelection structuredSelection = (IStructuredSelection) scenarioViewer.getSelection();
	// if (structuredSelection.isEmpty() == false) {
	// if (structuredSelection.size() == 1) {
	// final DetailCompositeDialog dcd = new DetailCompositeDialog(event.getViewer().getControl().getShell(), jointModelEditorPart.getDefaultCommandHandler());
	// try {
	// jointModelEditorPart.getEditorLock().claim();
	// jointModelEditorPart.setDisableUpdates(true);
	// dcd.open(location, jointModelEditorPart.getRootObject(), structuredSelection.toList(), scenarioViewer.isLocked());
	// } finally {
	// jointModelEditorPart.setDisableUpdates(false);
	// jointModelEditorPart.getEditorLock().release();
	// }
	// } else {
	// try {
	// jointModelEditorPart.getEditorLock().claim();
	// if (scenarioViewer.isLocked() == false) {
	// final MultiDetailDialog mdd = new MultiDetailDialog(event.getViewer().getControl().getShell(), jointModelEditorPart.getRootObject(), jointModelEditorPart.getDefaultCommandHandler());
	// mdd.open(location, structuredSelection.toList());
	// }
	// } finally {
	// jointModelEditorPart.getEditorLock().release();
	// }
	// }
	// }
	// }
	// }
	// });
	//
	// scenarioViewer.getGrid().setCellSelectionEnabled(true);
	// filterField.setViewer(scenarioViewer);
	//
	// final ColumnViewerEditorActivationStrategy actSupport = new ColumnViewerEditorActivationStrategy(scenarioViewer) {
	// protected boolean isEditorActivationEvent(final ColumnViewerEditorActivationEvent event) {
	// return event.eventType == ColumnViewerEditorActivationEvent.TRAVERSAL || event.eventType == ColumnViewerEditorActivationEvent.MOUSE_CLICK_SELECTION
	// || event.eventType == ColumnViewerEditorActivationEvent.PROGRAMMATIC;
	// }
	// };
	//
	// GridViewerEditor.create(scenarioViewer, actSupport, ColumnViewerEditor.KEYBOARD_ACTIVATION | GridViewerEditor.SELECTION_FOLLOWS_EDITOR |
	// // ColumnViewerEditor.KEEP_EDITOR_ON_DOUBLE_CLICK |
	// ColumnViewerEditor.TABBING_HORIZONTAL | ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR | ColumnViewerEditor.TABBING_VERTICAL | ColumnViewerEditor.KEYBOARD_ACTIVATION);
	//
	// return scenarioViewer;
	// } else {
	// throw new RuntimeException("Did not expect two calls to createViewer()");
	// }
	//
	// }

	// @Override
	// public void createControl(final Composite parent) {
	// // interpose and create filter field
	// if (getControl() == null) {
	// container = parent;
	//
	// // Create view form.
	// // control = new ViewForm(parent, getStyle());
	// control = new ViewForm(parent, SWT.NONE);
	// control.addDisposeListener(new DisposeListener() {
	// @Override
	// public void widgetDisposed(final DisposeEvent event) {
	// dispose();
	// }
	// });
	//
	// control.marginWidth = 0;
	// control.marginHeight = 0;
	//
	// // Create a title bar.
	// if (externalToolbarManager == null)
	// createTitleBar();
	//
	// final Composite inner = new Composite(control, SWT.NONE);
	// filterField = new FilterField(inner);
	//
	// final GridLayout layout = new GridLayout(1, false);
	// layout.marginHeight = 0;
	// layout.marginWidth = 0;
	// inner.setLayout(layout);
	//
	// viewer = createViewer(inner);
	//
	// viewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
	//
	// control.setContent(inner);
	//
	// control.setTabList(new Control[] { inner });
	//
	// // When the pane or any child gains focus, notify the workbench.
	// control.addListener(SWT.Activate, this);
	// hookFocus(control);
	// hookFocus(viewer.getControl());
	// }
	// }

	// public TradesWiringViewer(final Composite parent, final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
	// public TradesWiringViewer(final Composite parent, final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
	// super(parent, SWT.NONE);
	// this.location = location;
	// actionBar = new ToolBar(this, SWT.FLAT | SWT.WRAP);
	//
	// final GridLayout gl_shell = new GridLayout(1, false);
	// // gl_shell.verticalSpacing = 6;
	// this.setLayout(gl_shell);
	// }

	private final List<Integer> wiring = new ArrayList<Integer>();
	private TradesWiringDiagram wiringDiagram;

	private final ArrayList<Boolean> leftTerminalsExist = new ArrayList<Boolean>();
	private final ArrayList<Boolean> rightTerminalsExist = new ArrayList<Boolean>();
	protected List<RowData> rowData;

	private final ArrayList<Cargo> cargoes = new ArrayList<Cargo>();
	private final ArrayList<LoadSlot> loadSlots = new ArrayList<LoadSlot>();
	private final ArrayList<DischargeSlot> dischargeSlots = new ArrayList<DischargeSlot>();

	private Object[] sortedChildren;
	private int[] sortedIndices;

	protected int[] reverseSortedIndices;

	private boolean locked;

	protected ScenarioTableViewer constructViewer(final Composite parent) {

		final ScenarioTableViewer scenarioViewer = new ScenarioTableViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL, jointModelEditorPart) {

			@Override
			protected Object[] getSortedChildren(final Object parent) {
				sortedChildren = super.getSortedChildren(parent);

				sortedIndices = new int[sortedChildren.length];
				reverseSortedIndices = new int[sortedChildren.length];

				for (int i = 0; i < sortedChildren.length; ++i) {
					final int rawIndex = rowData.indexOf(sortedChildren[i]);
					sortedIndices[rawIndex] = i;
					reverseSortedIndices[i] = rawIndex;
				}
				if (wiringDiagram != null) {
					wiringDiagram.setSortOrder(sortedIndices, reverseSortedIndices);
				}
				return sortedChildren;
			}

			@Override
			public void init(final AdapterFactory adapterFactory, final EReference... path) {
				super.init(adapterFactory, path);

				init(new IStructuredContentProvider() {

					@Override
					public void dispose() {

					}

					@Override
					public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {

						if (oldInput instanceof EObject) {

							((EObject) oldInput).eAdapters().remove(cargoChangeAdapter);
						}

						if (newInput instanceof EObject) {
							((EObject) newInput).eAdapters().add(cargoChangeAdapter);
						}

						wiring.clear();
					}

					@Override
					public Object[] getElements(final Object inputElement) {

						for (final Cargo c : cargoes) {
							if (c != null) {
								c.eAdapters().remove(cargoChangeAdapter);
							}
						}

						final MMXRootObject rootObject = jointModelEditorPart.getRootObject();
						if (rootObject != null) {
							final CargoModel cargoModel = rootObject.getSubModel(CargoModel.class);
							if (cargoModel != null) {
								cargoModel.eAdapters().remove(cargoChangeAdapter);
								cargoModel.eAdapters().add(cargoChangeAdapter);
								for (final Cargo c : cargoModel.getCargoes()) {
									c.eAdapters().remove(cargoChangeAdapter);
									c.eAdapters().add(cargoChangeAdapter);
								}
							}
						}

						final CargoModel cargoModel = jointModelEditorPart.getRootObject().getSubModel(CargoModel.class);
						final ArrayList<Pair<Color, Color>> terminalColors = new ArrayList<Pair<Color, Color>>();
						final ArrayList<Pair<Boolean, Boolean>> terminalOptionals = new ArrayList<Pair<Boolean, Boolean>>();
						final ArrayList<Color> wireColors = new ArrayList<Color>();
						final Color green = Display.getCurrent().getSystemColor(SWT.COLOR_GREEN);
						final Color black = Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);

						wiring.clear();
						leftTerminalsExist.clear();
						rightTerminalsExist.clear();

						final List<RowData> rows = setCargoes(cargoModel.getCargoes(), cargoModel.getLoadSlots(), cargoModel.getDischargeSlots());
						for (int i = 0; i < rows.size(); ++i) {
							leftTerminalsExist.add(rows.get(i).loadSlot != null);
							rightTerminalsExist.add(rows.get(i).dischargeSlot != null);
							if (rows.get(i).cargo != null) {
								wiring.add(i);
							} else {
								wiring.add(-1);
							}
							terminalColors.add(new Pair<Color, Color>(green, green));
							terminalOptionals.add(new Pair<Boolean, Boolean>(false, false));
							wireColors.add(black);

						}
						wiringDiagram.setWiring(wiring);

						final Color addColor = Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW);

						terminalColors.add(new Pair<Color, Color>(addColor, addColor));
						terminalColors.add(new Pair<Color, Color>(addColor, addColor));

						wiringDiagram.setWireColors(wireColors);
						wiringDiagram.setTerminalColors(terminalColors);
						wiringDiagram.setTerminalOptionals(terminalOptionals);

						wiringDiagram.setTerminalsValid(leftTerminalsExist, rightTerminalsExist);
						updateWiringColours(wiringDiagram, wiring, rows);

						TradesWiringViewer.this.rowData = rows;

						return rows.toArray();
					}

				});

			}

			
			@Override
			protected void processStatus(IStatus status, boolean update) {
				// TODO Auto-generated method stub
				super.processStatus(status, update);
				refresh();
			}
			protected void updateObject(final EObject object, final IStatus status, final boolean update) {
				if (object == null) {
					return;
				}
				
				RowData rd = null;
				if (loadSlots.contains(object)) {
					rd = rowData.get(loadSlots.indexOf(object));
				} else if (dischargeSlots.contains(object)) {
					rd = rowData.get(dischargeSlots.indexOf(object));
				} else if (cargoes.contains(object)) {
					rd = rowData.get(cargoes.indexOf(object));
				}

				if (rd != null) {
					setStatus(rd, status);
					if (update) {
//						updatse(rd, null);
					}
				}
			}

			@Override
			public EObject getElementForNotificationTarget(final EObject source) {
				if (source instanceof LoadSlot) {
					return source;
				} else if (source instanceof DischargeSlot) {
					return source;
				} else if (source instanceof Cargo) {
					return source;
				}

				return super.getElementForNotificationTarget(source);
			}

		};
		final MenuManager mgr = new MenuManager();
		scenarioViewer.getGrid().addMenuDetectListener(new MenuDetectListener() {

			private Menu menu;

			@Override
			public void menuDetected(final MenuDetectEvent e) {
				final Point mousePoint = getScenarioViewer().getGrid().toControl(new Point(e.x, e.y));
				final GridColumn column = getScenarioViewer().getGrid().getColumn(mousePoint);

				final GridItem item = getScenarioViewer().getGrid().getItem(mousePoint);
				final Object data = item.getData();
				if (data instanceof RowData) {

					final RowData rowDataItem = (RowData) data;
					final int idx = rowData.indexOf(rowDataItem);

					System.out.println(idx);

					System.out.println(rowDataItem.loadSlot);
					System.out.println(loadSlots.get(idx));

					if (menu == null) {
						menu = mgr.createContextMenu(scenarioViewer.getGrid());
					}
					mgr.removeAll();
					// TODO: Simple load/discharge filter. Really need to determine when we build the columns and save into a set somewhere
					if (column.getText().contains("Load") && rowDataItem.loadSlot != null) {

						final IMenuListener listener = createLoadSlotMenuListener(idx);
						listener.menuAboutToShow(mgr);
					}
					if (column.getText().contains("Discharge") && rowDataItem.dischargeSlot != null) {

						final IMenuListener listener = createDischargeSlotMenuListener(idx);
						listener.menuAboutToShow(mgr);
					}

					menu.setVisible(true);
				}
			}
		});
		return scenarioViewer;
	}

	public void init(final List<EReference> path, final AdapterFactory adapterFactory) {
		getScenarioViewer().init(adapterFactory, new EReference[0]);

		getScenarioViewer().setStatusProvider(getJointModelEditorPart().getStatusProvider());

		final Grid table = getScenarioViewer().getGrid();

		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		// set up toolbars
		final ToolBarManager toolbar = getToolBarManager();
		toolbar.add(new GroupMarker(EDIT_GROUP));
		toolbar.add(new GroupMarker(ADD_REMOVE_GROUP));
		toolbar.add(new GroupMarker(VIEW_GROUP));
		toolbar.appendToGroup(VIEW_GROUP, new PackGridTableColumnsAction(scenarioViewer));

		final ActionContributionItem filter = filterField.getContribution();

		toolbar.appendToGroup(VIEW_GROUP, filter);

		// // final EReference containment = path.get(path.size() - 1);
		// //
		// // final Action addAction = AddModelAction.create(containment.getEReferenceType(), new IAddContext() {
		// // @Override
		// // public MMXRootObject getRootObject() {
		// // return jointModelEditorPart.getRootObject();
		// // }
		// //
		// // @Override
		// // public EReference getContainment() {
		// // return containment;
		// // }
		// //
		// // @Override
		// // public EObject getContainer() {
		// // return scenarioViewer.getCurrentContainer();
		// // }
		// //
		// // @Override
		// // public ICommandHandler getCommandHandler() {
		// // return jointModelEditorPart.getDefaultCommandHandler();
		// // }
		// //
		// // @Override
		// // public IScenarioEditingLocation getEditorPart() {
		// // return jointModelEditorPart;
		// // }
		// //
		// // @Override
		// // public ISelection getCurrentSelection() {
		// // return viewer.getSelection();
		// // }
		// // });
		//
		// if (addAction != null) {
		// // if we can't add one, we can't duplicate one either.
		// final Action dupAction = createDuplicateAction();
		//
		// if (dupAction != null) {
		// toolbar.appendToGroup(ADD_REMOVE_GROUP, dupAction);
		// }
		//
		// toolbar.appendToGroup(ADD_REMOVE_GROUP, addAction);
		// }
		deleteAction = createDeleteAction();
		if (deleteAction != null) {
			toolbar.appendToGroup(ADD_REMOVE_GROUP, deleteAction);
		}
		if (actionBars != null) {
			actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(), deleteAction);
		}

		final Action importAction = createImportAction();
		if (importAction != null) {
			toolbar.appendToGroup(ADD_REMOVE_GROUP, importAction);
		}

		// add extension points to toolbar
		{
			final String toolbarID = getToolbarID();
			final IMenuService menuService = (IMenuService) PlatformUI.getWorkbench().getService(IMenuService.class);
			if (menuService != null) {
				menuService.populateContributionManager(toolbar, toolbarID);

				viewer.getControl().addDisposeListener(new DisposeListener() {
					@Override
					public void widgetDisposed(final DisposeEvent e) {
						menuService.releaseContributions(toolbar);
					}
				});
			}
		}

		Action copyToClipboardAction = null;
		if (viewer instanceof TableViewer) {
			copyToClipboardAction = new CopyTableToClipboardAction(((TableViewer) viewer).getTable());
		} else if (viewer instanceof TreeViewer) {
			copyToClipboardAction = new CopyTreeToClipboardAction(((TreeViewer) viewer).getTree());
		} else if (viewer instanceof GridTableViewer) {
			copyToClipboardAction = new CopyGridToClipboardAction(((GridTableViewer) viewer).getGrid());
		}

		if (copyToClipboardAction != null) {
			toolbar.add(copyToClipboardAction);
		}

		if (actionBars != null) {
			actionBars.updateActionBars();
		}

		toolbar.update(true);

		final CargoPackage pkg = CargoPackage.eINSTANCE;
		final IReferenceValueProviderProvider provider = jointModelEditorPart.getReferenceValueProviderCache();
		final EditingDomain editingDomain = jointModelEditorPart.getEditingDomain();

		final GridViewerColumn state = getScenarioViewer().addSimpleColumn("", false);
		state.setLabelProvider(new EObjectTableViewerColumnProvider(getScenarioViewer(), null, null) {

			@Override
			public String getText(final Object element) {
				return null;
			}

			@Override
			public Image getImage(final Object element) {

				if (element instanceof RowData) {
					final RowData rowDataItem = (RowData) element;
					if (rowDataItem.cargo != null) {
						final Cargo cargo = rowDataItem.cargo;
						final InputModel inputModel = jointModelEditorPart.getRootObject().getSubModel(InputModel.class);
						final ElementAssignment assignment = AssignmentEditorHelper.getElementAssignment(inputModel, cargo);
						if (assignment != null && assignment.isLocked()) {
							return lockedImage;
						} else if (!cargo.isAllowRewiring()) {
							return wiredImage;

						}
					}
				}

				return super.getImage(element);
			}
		});

		addTradesColumn("Load ID", new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getNamedObject_Name(), editingDomain), new RowDataEMFPath(Type.LOAD, true));
		addTradesColumn("Load Port", new SingleReferenceManipulator(pkg.getSlot_Port(), provider, editingDomain), new RowDataEMFPath(Type.LOAD, true));
		addTradesColumn("Load Contract", new ContractManipulator(provider, editingDomain), new RowDataEMFPath(Type.LOAD, true));
		addTradesColumn("Load Date", new DateAttributeManipulator(pkg.getSlot_WindowStart(), editingDomain), new RowDataEMFPath(Type.LOAD, true));

		final GridViewerColumn wiringColumn = addWiringColumn();

		addTradesColumn("Discharge ID", new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getNamedObject_Name(), editingDomain), new RowDataEMFPath(Type.DISCHARGE, true));
		addTradesColumn("Discharge Port", new SingleReferenceManipulator(pkg.getSlot_Port(), provider, editingDomain), new RowDataEMFPath(Type.DISCHARGE, true));
		addTradesColumn("Discharge Contract", new ContractManipulator(provider, editingDomain), new RowDataEMFPath(Type.DISCHARGE, true));
		addTradesColumn("Discharge Date", new DateAttributeManipulator(pkg.getSlot_WindowStart(), editingDomain), new RowDataEMFPath(Type.DISCHARGE, true));

		addTradesColumn("Assignment", new AssignmentManipulator(jointModelEditorPart), new RowDataEMFPath(Type.CARGO, true));

		wiringDiagram = new TradesWiringDiagram(getScenarioViewer().getGrid()) {

			@Override
			protected void wiringChanged(final List<Integer> newWiring) {
				// TODO Auto-generated method stub
				doWiringChanged(newWiring);
			}

			@Override
			protected List<Float> getTerminalPositions() {

				final int index = 0;
				final int vPod = getScenarioViewer().getGrid().getVerticalBar().getSelection();
				// +1 to to make loop simpler -
				final int heights[] = new int[wiring.size() + 1];
				int idx = 0;
				heights[0] = getScenarioViewer().getGrid().getHeaderHeight();

				// Pass one, get heights
				for (final GridItem item : getScenarioViewer().getGrid().getItems()) {
					idx++;
					heights[idx] = 1 + heights[idx - 1] + item.getHeight();
				}
				final int hOffset = (heights[vPod]) - getScenarioViewer().getGrid().getHeaderHeight();
				// Pass 2 get mids
				idx = -1;
				for (final GridItem item : getScenarioViewer().getGrid().getItems()) {
					heights[++idx] += item.getHeight() / 2;
				}

				// Pass 3, offset to vPod is the zero height
				idx = -1;
				for (final GridItem item : getScenarioViewer().getGrid().getItems()) {
					heights[++idx] -= hOffset;
				}

				final List<Float> data = new ArrayList<Float>(heights.length);
				for (final int h : heights) {
					data.add((float) h);
				}
				// Take off extra row added earlier
				return data.subList(0, data.size() - 1);
			}

			@Override
			public Rectangle getCanvasClientArea() {
				// Client area is full table, reduce the size to just the wiring column.

				final Rectangle area = super.getCanvasClientArea();

				int wiringColumnIndexTmp = -1;
				for (final GridColumn gc : getScenarioViewer().getGrid().getColumns()) {
					++wiringColumnIndexTmp;
					if (gc == wiringColumn.getColumn()) {
						break;
					}
				}
				final int wiringColumnIndex = wiringColumnIndexTmp;

				int offset = 0;
				offset += getScenarioViewer().getGrid().getRowHeaderWidth();
				// TODO: Get col number
				final int[] columnOrder = getScenarioViewer().getGrid().getColumnOrder();
				for (int ii = getScenarioViewer().getGrid().getHorizontalBar().getSelection(); ii < columnOrder.length; ++ii) {
					final int idx = columnOrder[ii];
					if (idx == wiringColumnIndex) {
						break;
					}
					offset += getScenarioViewer().getGrid().getColumn(idx).getWidth();
				}
				// TODO: Take into account h scroll final int colWidth = getScenarioViewer().getGrid().getColumn(wiringColumnIndex).getWidth();

				final Rectangle r = new Rectangle(area.x + offset, area.y + getScenarioViewer().getGrid().getHeaderHeight(), wiringColumn.getColumn().getWidth(), area.height);

				return r;
			}
		};
		wiringDiagram.setSortOrder(sortedIndices, reverseSortedIndices);
	}

	private String getActionName(final Slot slot, final boolean includePort, final boolean includeContract) {
		final StringBuilder sb = new StringBuilder();

		if (includeContract && slot.getContract() != null) {
			sb.append(slot.getContract().getName());
			sb.append(" - ");
		}

		if (includePort && slot.getPort() != null) {
			sb.append(slot.getPort().getName());
			sb.append(" - ");
		}
		{
			final DateFormat df = DateFormat.getDateInstance();
			if (slot.getPort() != null) {
				final TimeZone zone = LocalDateUtil.getTimeZone(slot, CargoPackage.eINSTANCE.getSlot_WindowStart());
				df.setTimeZone(zone);
			}
			sb.append(df.format(slot.getWindowStart()));
			sb.append(" - ");
		}
		{
			sb.append(slot.getName());
			sb.append(" - ");

		}
		Cargo c = null;
		if (slot instanceof LoadSlot) {
			if (((LoadSlot) slot).isDESPurchase()) {
				sb.append("(DES Purchase) ");
			}
			c = ((LoadSlot) slot).getCargo();
		}
		if (slot instanceof DischargeSlot) {
			if (((DischargeSlot) slot).isFOBSale()) {
				sb.append("(FOB Sale) ");
			}
			c = ((DischargeSlot) slot).getCargo();
		}
		if (slot instanceof SpotSlot) {
			sb.append(((SpotSlot) slot).getMarket().getName());
			sb.append(" - ");

		}
		if (c != null) {
			sb.append(" (Cargo " + c.getName() + ")");
		} else {
			sb.append("(unused)");
		}
		return sb.toString();
	}

	private <T> T createObject(final EClass clz, final EReference reference, final EObject container) {
		final List<IModelFactory> factories = Activator.getDefault().getModelFactoryRegistry().getModelFactories(clz);

		// TODO: Pre-generate and link to UI
		// TODO: Add FOB/DES etc as explicit slot types.
		final IModelFactory factory = factories.get(0);

		final Collection<? extends ISetting> settings = factory.createInstance(jointModelEditorPart.getRootObject(), container, reference, StructuredSelection.EMPTY);
		if (settings.isEmpty() == false) {

			for (final ISetting setting : settings) {

				return (T) setting.getInstance();
			}
		}
		return null;
	}

	private class AddAction extends Action implements IMenuCreator {

		private Menu lastMenu;

		public AddAction(final String label) {
			super(label, IAction.AS_DROP_DOWN_MENU);
		}

		@Override
		public void dispose() {
			if ((lastMenu != null) && (lastMenu.isDisposed() == false)) {
				lastMenu.dispose();
			}
			lastMenu = null;
		}

		@Override
		public IMenuCreator getMenuCreator() {
			return this;
		}

		@Override
		public Menu getMenu(final Control parent) {
			if (lastMenu != null) {
				lastMenu.dispose();
			}
			lastMenu = new Menu(parent);

			populate(lastMenu);

			return lastMenu;
		}

		protected void addActionToMenu(final Action a, final Menu m) {
			final ActionContributionItem aci = new ActionContributionItem(a);
			aci.fill(m, -1);
		}

		/**
		 * Subclasses should fill their menu with actions here.
		 * 
		 * @param menu
		 *            the menu which is about to be displayed
		 */
		protected void populate(final Menu menu) {
			final CargoModel cargoModel = jointModelEditorPart.getRootObject().getSubModel(CargoModel.class);

			{
				final Action newDESPurchase = new Action("New DES Purchase") {
					public void run() {

						final CompoundCommand cmd = new CompoundCommand("New DES Purchase");

						final LoadSlot newLoad = createObject(CargoPackage.eINSTANCE.getLoadSlot(), CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), cargoModel);
						newLoad.setDESPurchase(true);
						newLoad.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), EcoreUtil.generateUUID());
						newLoad.setOptional(true);
						newLoad.setName("");
						cmd.append(AddCommand.create(jointModelEditorPart.getEditingDomain(), cargoModel, CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), newLoad));

						jointModelEditorPart.getEditingDomain().getCommandStack().execute(cmd);
					}
				};
				addActionToMenu(newDESPurchase, menu);
			}
			{
				final Action newLoad = new Action("New Load Slot") {
					public void run() {

						final CompoundCommand cmd = new CompoundCommand("New Load Slot");

						final LoadSlot newLoad = createObject(CargoPackage.eINSTANCE.getLoadSlot(), CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), cargoModel);
						newLoad.setDESPurchase(false);
						newLoad.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), EcoreUtil.generateUUID());
						newLoad.setOptional(true);
						newLoad.setName("");
						cmd.append(AddCommand.create(jointModelEditorPart.getEditingDomain(), cargoModel, CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), newLoad));

						jointModelEditorPart.getEditingDomain().getCommandStack().execute(cmd);
					}
				};
				addActionToMenu(newLoad, menu);
			}
			{
				final Action newFOBSale = new Action("New FOB Sale") {
					public void run() {

						final CompoundCommand cmd = new CompoundCommand("New FOB Sale");

						final DischargeSlot newDischarge = createObject(CargoPackage.eINSTANCE.getDischargeSlot(), CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(), cargoModel);
						newDischarge.setFOBSale(true);
						newDischarge.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), EcoreUtil.generateUUID());
						newDischarge.setOptional(true);
						newDischarge.setName("");
						cmd.append(AddCommand.create(jointModelEditorPart.getEditingDomain(), cargoModel, CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(), newDischarge));

						jointModelEditorPart.getEditingDomain().getCommandStack().execute(cmd);
					}
				};
				addActionToMenu(newFOBSale, menu);

			}
			{
				final Action newDischarge = new Action("New Discharge") {
					public void run() {

						final CompoundCommand cmd = new CompoundCommand("New Discharge Slot");

						final DischargeSlot newDischarge = createObject(CargoPackage.eINSTANCE.getDischargeSlot(), CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(), cargoModel);
						newDischarge.setFOBSale(false);
						newDischarge.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), EcoreUtil.generateUUID());
						newDischarge.setOptional(true);
						newDischarge.setName("");
						cmd.append(AddCommand.create(jointModelEditorPart.getEditingDomain(), cargoModel, CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(), newDischarge));

						jointModelEditorPart.getEditingDomain().getCommandStack().execute(cmd);
					}
				};

				addActionToMenu(newDischarge, menu);
			}

		}

		@Override
		public Menu getMenu(final Menu parent) {
			if (lastMenu != null) {
				lastMenu.dispose();
			}
			lastMenu = new Menu(parent);

			populate(lastMenu);

			return lastMenu;
		}

	}

	private class RowData extends EObjectImpl {

		Cargo cargo;
		LoadSlot loadSlot;
		DischargeSlot dischargeSlot;
		ElementAssignment elementAssignment;
	};

	/**
	 * Set the cargoes, and reset the wiring to match these cargoes.
	 * 
	 * @param newCargoes
	 */
	public List<RowData> setCargoes(final List<Cargo> newCargoes, final List<LoadSlot> loadSlots, final List<DischargeSlot> dischargeSlots) {

		final List<RowData> rows = new ArrayList<RowData>();

		this.cargoes.clear();
		this.loadSlots.clear();
		this.dischargeSlots.clear();

		// // delete existing children
		// for (final Control c : getChildren()) {
		// c.dispose();
		// }
		// lhsComposites.clear();
		// rhsComposites.clear();
		// wiringDiagram = null;
		// wiring.clear();
		// this.loadSlots.clear();
		// this.dischargeSlots.clear();
		// this.cargoes.clear();
		// this.leftTerminalsExist.clear();
		// this.rightTerminalsExist.clear();

		for (int i = 0; i < newCargoes.size(); i++) {
			final Cargo cargo = newCargoes.get(i);

			final RowData rd = new RowData();
			rd.cargo = cargo;
			rd.loadSlot = cargo.getLoadSlot();
			rd.dischargeSlot = cargo.getDischargeSlot();
			rows.add(rd);
			//
			// this.wiring.add(i); // set default wiring
			this.cargoes.add(rd.cargo);
			this.loadSlots.add(rd.loadSlot);
			this.dischargeSlots.add(rd.dischargeSlot);
			// this.leftTerminalsExist.add(true);
			// this.rightTerminalsExist.add(true);
		}

		for (final LoadSlot slot : loadSlots) {
			if (slot.getCargo() == null) {

				final RowData rd = new RowData();
				rd.loadSlot = slot;
				rows.add(rd);

				this.cargoes.add(rd.cargo);
				this.loadSlots.add(rd.loadSlot);
				this.dischargeSlots.add(rd.dischargeSlot);
				// this.cargoes.add(null);
				// this.loadSlots.add(slot);
				// this.dischargeSlots.add(null);
				// this.wiring.add(-1);
				// this.leftTerminalsExist.add(false);
				// this.rightTerminalsExist.add(false);
			}
		}
		for (final DischargeSlot slot : dischargeSlots) {
			if (slot.getCargo() == null) {
				// this.loadSlots.add(null);
				// this.cargoes.add(null);
				// this.dischargeSlots.add(slot);
				// this.wiring.add(-1);
				// this.leftTerminalsExist.add(false);
				// this.rightTerminalsExist.add(false);
				final RowData rd = new RowData();
				rd.dischargeSlot = slot;
				rows.add(rd);

				this.cargoes.add(rd.cargo);
				this.loadSlots.add(rd.loadSlot);
				this.dischargeSlots.add(rd.dischargeSlot);

			}
		}
		
		assert rows.size() == this.cargoes.size();
		assert rows.size() == this.loadSlots.size();
		assert rows.size() == this.dischargeSlots.size();

		// numberOfRows = wiring.size();
		//
		// performControlUpdate(true);
		//
		// if (this.location != null && jointModelEditorPart.getStatusProvider() != null) {
		// // Perform initial validation
		// final IStatusProvider statusProvider = jointModelEditorPart.getStatusProvider();
		// statusChangedListener.onStatusChanged(statusProvider, statusProvider.getStatus());
		// }

		return rows;
	}

	// public Control getControl() {
	// // TODO Auto-generated method stub
	// return scenarioViewer == null ? null : scenarioViewer.getControl();
	// }
	//
	public void init(final AdapterFactory adapterFactory) {
		getScenarioViewer().init(adapterFactory);

	}

	//
	// public void setInput(final Object input) {
	// this.scenarioViewer.setInput(input);
	// }

	private enum Type {
		CARGO, LOAD, DISCHARGE, ASSIGNMENT
	}

	private class RowDataEMFPath extends EMFPath {

		private final Type type;

		public RowDataEMFPath(final Type type, final boolean failSilently, final Iterable<?> path) {
			super(failSilently, path);
			this.type = type;
		}

		//
		public RowDataEMFPath(final Type type, final boolean failSilently, final Object... path) {
			super(failSilently, path);
			this.type = type;
		}

		@Override
		public Object get(final EObject root, final int depth) {

			if (root instanceof RowData) {
				switch (type) {
				case ASSIGNMENT:
					return super.get(((RowData) root).elementAssignment, depth);
				case CARGO:
					return super.get(((RowData) root).cargo, depth);
				case DISCHARGE:
					return super.get(((RowData) root).dischargeSlot, depth);
				case LOAD:
					return super.get(((RowData) root).loadSlot, depth);
				}
			}
			return super.get(root, depth);
		}
	}

	@Override
	protected void enableOpenListener() {
		// Disable open handler
	}

	private synchronized void updateWiringColours(final TradesWiringDiagram diagram, final List<Integer> wiring, final List<RowData> rows) {
		final Color red = Display.getDefault().getSystemColor(SWT.COLOR_RED);
		final Color green = Display.getDefault().getSystemColor(SWT.COLOR_GREEN);
		final Color black = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
		final Color gray = Display.getDefault().getSystemColor(SWT.COLOR_GRAY);

		for (int i = 0; i < rows.size(); ++i) {
			final RowData rd = rows.get(i);
			final LoadSlot loadSlot = rd.loadSlot;
			final DischargeSlot dischargeSlot = rd.dischargeSlot;
			if (loadSlot != null && loadSlot.isOptional()) {
				diagram.setLeftTerminalColor(i, green);
				diagram.setLeftTerminalOptional(i, true);
			} else {
				diagram.setLeftTerminalColor(i, red);
				diagram.setLeftTerminalOptional(i, false);
			}
			if (dischargeSlot != null && dischargeSlot.isOptional()) {
				diagram.setRightTerminalColor(i, green);
				diagram.setRightTerminalOptional(i, true);
			} else {
				diagram.setRightTerminalColor(i, red);
				diagram.setRightTerminalOptional(i, false);
			}
			final int j = wiring.get(i);
			if (j == -1) {
				continue;
			}

			if (loadSlot == null) {
				continue;
			}
			if (dischargeSlot == null) {
				continue;
			}

			diagram.setLeftTerminalColor(i, green);
			diagram.setRightTerminalColor(j, green);

			// // TODO this is not exactly proper validation.
			if (isWiringValid(cargoes.get(i), loadSlot, dischargeSlot)) {
				if (cargoes.get(i).isAllowRewiring()) {
					diagram.setWireColor(i, black);
				} else {
					diagram.setWireColor(i, gray);
				}
			} else {
				diagram.setWireColor(i, red);
			}
		}
		diagram.redraw();
	}

	private void performControlUpdate(final boolean rowAdded) {

		System.out.println("Update");
		final Object oldInput = getScenarioViewer().getInput();
		getScenarioViewer().setInput(null);
		getScenarioViewer().setInput(oldInput);
		// synchronized (updateLock) {
		// if (isDisposed()) {
		// return;
		// }
		// // if (rowAdded) {
		// createChildren();
		// layout();
		// CargoWiringComposite.this.notifyListeners(SWT.Modify, new Event());
		// // } else {
		// // updateWiringColours(wiringDiagram, wiring, lhsComposites, rhsComposites);
		// // CargoWiringComposite.this.notifyListeners(SWT.Modify, new Event());
		// // }
		// }
	}

	protected void doWiringChanged(final List<Integer> newWiring) {

		currentWiringCommand = new CompoundCommand("Rewire Cargoes");
		final CargoModel cargoModel = jointModelEditorPart.getRootObject().getSubModel(CargoModel.class);
		final InputModel inputModel = jointModelEditorPart.getRootObject().getSubModel(InputModel.class);

		for (int i = 0; i < rowData.size(); ++i) {
			if (wiring.get(i).equals(newWiring.get(i))) {
				// No change
				continue;
			}

			final RowData rowI = rowData.get(i);

			final Integer newIndex = newWiring.get(i);
			final LoadSlot loadSlot = rowI.loadSlot;
			if (newIndex >= 0 && newIndex < newWiring.size()) {
				final DischargeSlot otherDischarge = rowData.get(newIndex).dischargeSlot;
				Cargo c = rowI.cargo;
				if (c != null) {
					if (c.getDischargeSlot() != otherDischarge) {
						currentWiringCommand.append(SetCommand.create(jointModelEditorPart.getEditingDomain(), c, CargoPackage.eINSTANCE.getCargo_DischargeSlot(), otherDischarge));

						// Optional market slots can be removed.
						if (c.getDischargeSlot() != null) {
							final DischargeSlot oldSlot = c.getDischargeSlot();
							if (oldSlot instanceof SpotSlot && oldSlot.isOptional()) {
								currentWiringCommand.append(DeleteCommand.create(jointModelEditorPart.getEditingDomain(), oldSlot));
							}
						}
					}
				} else {
					// create a new cargo
					c = createNewCargo(cargoModel);
					c.setName(loadSlot.getName());
					currentWiringCommand.append(SetCommand.create(jointModelEditorPart.getEditingDomain(), c, CargoPackage.eINSTANCE.getCargo_LoadSlot(), loadSlot));
					currentWiringCommand.append(SetCommand.create(jointModelEditorPart.getEditingDomain(), c, CargoPackage.eINSTANCE.getCargo_DischargeSlot(), otherDischarge));
				}
				appendFOBDESCommands(currentWiringCommand, jointModelEditorPart.getEditingDomain(), inputModel, c, loadSlot, otherDischarge);
			} else if (newIndex == -1) {
				final Cargo c = rowI.cargo;
				if (c != null) {
					currentWiringCommand.append(SetCommand.create(jointModelEditorPart.getEditingDomain(), c, CargoPackage.eINSTANCE.getCargo_DischargeSlot(), null));
					currentWiringCommand.append(DeleteCommand.create(jointModelEditorPart.getEditingDomain(), c));
					// Optional market slots can be removed.
					if (c.getDischargeSlot() != null) {
						final DischargeSlot oldSlot = c.getDischargeSlot();
						if (oldSlot instanceof SpotSlot && oldSlot.isOptional()) {
							currentWiringCommand.append(DeleteCommand.create(jointModelEditorPart.getEditingDomain(), oldSlot));
						}
					}

					// Optional market slots can be removed.
					if (c.getLoadSlot() != null) {
						final LoadSlot oldSlot = c.getLoadSlot();
						if (oldSlot instanceof SpotSlot && oldSlot.isOptional()) {
							currentWiringCommand.append(DeleteCommand.create(jointModelEditorPart.getEditingDomain(), oldSlot));
						}
					}
				} else {
					// Error?
				}
			} else {
				final DischargeSlot dischargeSlot = createNewDischarge(cargoModel, false);
				// ensureCapacity(newIndex + 1, cargoes, loadSlots, dischargeSlots);
				// dischargeSlots.set(newIndex, dischargeSlot);
				Cargo c = rowI.cargo;
				if (c == null) {
					// create a cargo
					c = createNewCargo(cargoModel);
					c.setName(loadSlot.getName());
					currentWiringCommand.append(SetCommand.create(jointModelEditorPart.getEditingDomain(), c, CargoPackage.eINSTANCE.getCargo_LoadSlot(), loadSlot));
					currentWiringCommand.append(SetCommand.create(jointModelEditorPart.getEditingDomain(), c, CargoPackage.eINSTANCE.getCargo_DischargeSlot(), dischargeSlot));
				} else {
					currentWiringCommand.append(SetCommand.create(jointModelEditorPart.getEditingDomain(), c, CargoPackage.eINSTANCE.getCargo_DischargeSlot(), dischargeSlot));
				}
				appendFOBDESCommands(currentWiringCommand, jointModelEditorPart.getEditingDomain(), inputModel, c, loadSlot, dischargeSlot);

			}
		}

		executeCurrentWiringCommand();
	}

	private void executeCurrentWiringCommand() {
		// Delete commands can be slow, so show the busy indicator while deleting.
		if (currentWiringCommand.isEmpty()) {
			currentWiringCommand = null;
			return;
		}
		final Runnable runnable = new Runnable() {

			@Override
			public void run() {
				jointModelEditorPart.getEditingDomain().getCommandStack().execute(currentWiringCommand);
			}
		};
		BusyIndicator.showWhile(null, runnable);
		currentWiringCommand = null;
	}

	private void appendFOBDESCommands(final CompoundCommand cmd, final EditingDomain editingDomain, final InputModel inputModel, final Cargo cargo, final LoadSlot loadSlot,
			final DischargeSlot dischargeSlot) {

		if (loadSlot.isDESPurchase()) {
			cmd.append(AssignmentEditorHelper.unassignElement(editingDomain, inputModel, cargo));

			cmd.append(SetCommand.create(editingDomain, loadSlot, CargoPackage.eINSTANCE.getLoadSlot_ArriveCold(), false));
			cmd.append(SetCommand.create(editingDomain, loadSlot, CargoPackage.eINSTANCE.getSlot_Duration(), 0));
			cmd.append(SetCommand.create(editingDomain, loadSlot, CargoPackage.eINSTANCE.getSlot_Port(), dischargeSlot.getPort()));
			if (loadSlot instanceof SpotSlot) {
				SpotSlotHelper.setSpotSlotTimeWindow(editingDomain, loadSlot, dischargeSlot, cmd);
			} else {
				cmd.append(SetCommand.create(editingDomain, loadSlot, CargoPackage.eINSTANCE.getSlot_WindowStart(), dischargeSlot.getWindowStart()));
				cmd.append(SetCommand.create(editingDomain, loadSlot, CargoPackage.eINSTANCE.getSlot_WindowStartTime(), dischargeSlot.getWindowStartTime()));
			}
		} else if (dischargeSlot.isFOBSale()) {
			cmd.append(AssignmentEditorHelper.unassignElement(editingDomain, inputModel, cargo));
			cmd.append(SetCommand.create(editingDomain, dischargeSlot, CargoPackage.eINSTANCE.getSlot_Duration(), 0));
			cmd.append(SetCommand.create(editingDomain, dischargeSlot, CargoPackage.eINSTANCE.getSlot_Port(), loadSlot.getPort()));
			if (dischargeSlot instanceof SpotSlot) {
				SpotSlotHelper.setSpotSlotTimeWindow(editingDomain, dischargeSlot, loadSlot, cmd);
			} else {
				cmd.append(SetCommand.create(editingDomain, dischargeSlot, CargoPackage.eINSTANCE.getSlot_WindowStart(), loadSlot.getWindowStart()));
				cmd.append(SetCommand.create(editingDomain, dischargeSlot, CargoPackage.eINSTANCE.getSlot_WindowStartTime(), loadSlot.getWindowStartTime()));
			}
		}
	}

	private void refreshContent() {

		// final CargoModel cargoModel = jointModelEditorPart.getRootObject().getSubModel(CargoModel.class);
		//
		// // Get current data
		// final Set<Cargo> newCargoes = new HashSet<Cargo>(cargoModel.getCargoes());
		// final Set<LoadSlot> newLoadSlots = new HashSet<LoadSlot>(cargoModel.getLoadSlots());
		// final Set<DischargeSlot> newDischargeSlots = new HashSet<DischargeSlot>(cargoModel.getDischargeSlots());
		//
		// // Get set of new items now known about
		// newCargoes.removeAll(cargoes);
		// newLoadSlots.removeAll(loadSlots);
		// newDischargeSlots.removeAll(dischargeSlots);
		//
		// // New items need to be added to the list.. however we also need to remove other items. These should not have a eContainer at this point.
		//
		// for (int i = 0; i < cargoes.size(); ++i) {
		// final EObject obj = cargoes.get(i);
		// if (obj != null && obj.eContainer() == null) {
		// cargoes.set(i, null);
		// }
		// }
		// for (int i = 0; i < loadSlots.size(); ++i) {
		// final EObject obj = loadSlots.get(i);
		// if (obj != null && obj.eContainer() == null) {
		// loadSlots.set(i, null);
		// }
		// }
		// for (int i = 0; i < dischargeSlots.size(); ++i) {
		// final EObject obj = dischargeSlots.get(i);
		// if (obj != null && obj.eContainer() == null) {
		// dischargeSlots.set(i, null);
		// }
		// }
		//
		// // Internal list should be full of valid items, so now we can add in the new items. First, add by cargo, then add remaining slots.
		//
		// for (final Cargo c : newCargoes) {
		// // Hook in adapter to catch changes.
		// c.eAdapters().add(cargoChangeAdapter);
		//
		// ensureCapacity(numberOfRows + 1, cargoes, loadSlots, dischargeSlots, wiring);
		// boolean addedItem = false;
		// if (loadSlots.contains(c.getLoadSlot())) {
		// cargoes.set(loadSlots.indexOf(c.getLoadSlot()), c);
		// } else {
		// boolean reusedRow = false;
		//
		// if (dischargeSlots.contains(c.getDischargeSlot())) {
		// final int dischargeIdx = dischargeSlots.indexOf(c.getDischargeSlot());
		// if (loadSlots.get(dischargeIdx) == null) {
		// cargoes.set(dischargeIdx, c);
		// loadSlots.set(dischargeIdx, c.getLoadSlot());
		// newLoadSlots.remove(c.getLoadSlot());
		// reusedRow = true;
		// }
		// }
		//
		// if (!reusedRow) {
		//
		// cargoes.set(numberOfRows, c);
		// loadSlots.set(numberOfRows, c.getLoadSlot());
		// newLoadSlots.remove(c.getLoadSlot());
		// addedItem = true;
		// }
		// }
		// if (dischargeSlots.contains(c.getDischargeSlot())) {
		// dischargeSlots.set(numberOfRows, null);
		// } else {
		//
		// boolean reusedRow = false;
		//
		// if (loadSlots.contains(c.getLoadSlot())) {
		// final int loadIdx = loadSlots.indexOf(c.getLoadSlot());
		// if (dischargeSlots.get(loadIdx) == null) {
		// dischargeSlots.set(loadIdx, c.getDischargeSlot());
		// newDischargeSlots.remove(c.getDischargeSlot());
		// reusedRow = true;
		// }
		// }
		//
		// if (!reusedRow) {
		// dischargeSlots.set(numberOfRows, c.getDischargeSlot());
		// newDischargeSlots.remove(c.getDischargeSlot());
		// addedItem = true;
		// }
		// }
		// if (addedItem) {
		// ++numberOfRows;
		// }
		// }
		// for (final LoadSlot slot : newLoadSlots) {
		// ensureCapacity(numberOfRows + 1, cargoes, loadSlots, dischargeSlots, wiring);
		// cargoes.set(numberOfRows, null);
		// loadSlots.set(numberOfRows, slot);
		// dischargeSlots.set(numberOfRows, null);
		//
		// ++numberOfRows;
		// }
		// for (final DischargeSlot slot : newDischargeSlots) {
		// ensureCapacity(numberOfRows + 1, cargoes, loadSlots, dischargeSlots, wiring);
		// cargoes.set(numberOfRows, null);
		// loadSlots.set(numberOfRows, null);
		// dischargeSlots.set(numberOfRows, slot);
		//
		// ++numberOfRows;
		// }
		//
		// // Now all the right data should be present. Lets update the wiring
		// for (int i = 0; i < numberOfRows; ++i) {
		// if (cargoes.get(i) != null) {
		// final int dischargeIndex = dischargeSlots.indexOf(cargoes.get(i).getDischargeSlot());
		// wiring.set(i, dischargeIndex);
		// } else {
		// wiring.set(i, -1);
		// }
		// }
		//
		// // Perform a prune of empty rows
		// for (int i = numberOfRows - 1; i >= 0; --i) {
		// if (loadSlots.get(i) == null && dischargeSlots.get(i) == null) {
		// cargoes.remove(i);
		// loadSlots.remove(i);
		// dischargeSlots.remove(i);
		//
		// // Re-index wiring
		// for (int j = 0; j < numberOfRows; ++j) {
		// final int idx = wiring.get(j);
		// if (idx > i) {
		// wiring.set(j, idx - 1);
		// }
		// }
		//
		// wiring.remove(i);
		// numberOfRows--;
		// }
		// }

		performControlUpdate(true);
	}

	private void runWiringUpdate(final LoadSlot loadSlot, final DischargeSlot dischargeSlot) {
		final CargoModel cargoModel = jointModelEditorPart.getRootObject().getSubModel(CargoModel.class);
		final InputModel inputModel = jointModelEditorPart.getRootObject().getSubModel(InputModel.class);

		// Discharge has an existing slot, so remove the cargo & wiring
		if (dischargeSlot.getCargo() != null) {
			currentWiringCommand.append(DeleteCommand.create(jointModelEditorPart.getEditingDomain(), dischargeSlot.getCargo()));

			// Optional market slots can be removed.
			final LoadSlot oldSlot = dischargeSlot.getCargo().getLoadSlot();
			if (oldSlot instanceof SpotSlot && oldSlot.isOptional()) {
				currentWiringCommand.append(DeleteCommand.create(jointModelEditorPart.getEditingDomain(), oldSlot));
			}
		}

		// Do we need to create a new cargo or re-wire and existing one.
		Cargo cargo = loadSlot.getCargo();
		if (cargo != null) {
			currentWiringCommand.append(SetCommand.create(jointModelEditorPart.getEditingDomain(), cargo, CargoPackage.eINSTANCE.getCargo_DischargeSlot(), dischargeSlot));

			// Optional market slots can be removed.
			if (cargo.getDischargeSlot() != null) {
				final DischargeSlot oldSlot = cargo.getDischargeSlot();
				if (oldSlot instanceof SpotSlot && oldSlot.isOptional()) {
					currentWiringCommand.append(DeleteCommand.create(jointModelEditorPart.getEditingDomain(), oldSlot));
				}
			}
		} else {
			cargo = createNewCargo(cargoModel);
			cargo.setName(loadSlot.getName());
			currentWiringCommand.append(SetCommand.create(jointModelEditorPart.getEditingDomain(), cargo, CargoPackage.eINSTANCE.getCargo_LoadSlot(), loadSlot));
			currentWiringCommand.append(SetCommand.create(jointModelEditorPart.getEditingDomain(), cargo, CargoPackage.eINSTANCE.getCargo_DischargeSlot(), dischargeSlot));
		}

		appendFOBDESCommands(currentWiringCommand, jointModelEditorPart.getEditingDomain(), inputModel, cargo, loadSlot, dischargeSlot);

		executeCurrentWiringCommand();
	}

	private void createMenus(final IMenuManager manager, final Slot source, final List<? extends Slot> possibleTargets, final boolean sourceIsLoad) {

		final Map<String, Set<Slot>> slotsByDate = new TreeMap<String, Set<Slot>>();
		final Map<String, Set<Slot>> slotsByContract = new TreeMap<String, Set<Slot>>();
		final Map<String, Set<Slot>> slotsByPort = new TreeMap<String, Set<Slot>>();

		for (final Slot target : possibleTargets) {

			final int daysDifference;
			// Perform some filtering on the possible targets
			{
				final LoadSlot loadSlot;
				final DischargeSlot dischargeSlot;
				if (sourceIsLoad) {
					loadSlot = (LoadSlot) source;
					dischargeSlot = (DischargeSlot) target;
				} else {
					loadSlot = (LoadSlot) target;
					dischargeSlot = (DischargeSlot) source;
				}
				// Filter out current pairing
				if (loadSlot.getCargo() != null && loadSlot.getCargo() == dischargeSlot.getCargo()) {
					continue;
				}

				// Filter backwards pairings
				if (loadSlot.getWindowStart().after(dischargeSlot.getWindowStart())) {
					continue;
				}
				final long diff = dischargeSlot.getWindowStart().getTime() - loadSlot.getWindowStart().getTime();
				daysDifference = (int) (diff / 1000 / 60 / 60 / 24);
			}

			final Contract contract = target.getContract();
			if (contract != null) {
				addSlotToTargets(target, contract.getName(), slotsByContract);
			}
			final Port port = target.getPort();
			if (port != null) {
				addSlotToTargets(target, port.getName(), slotsByPort);
			}

			if (daysDifference < 5) {
				addSlotToTargets(target, "Less than 5 Days", slotsByDate);
			}
			if (daysDifference < 10) {
				addSlotToTargets(target, "Less than 10 Days", slotsByDate);
			}
			if (daysDifference < 20) {
				addSlotToTargets(target, "Less than 20 Days", slotsByDate);
			}
			if (daysDifference < 30) {
				addSlotToTargets(target, "Less than 30 Days", slotsByDate);
			}
			if (daysDifference < 60) {
				addSlotToTargets(target, "Less than 60 Days", slotsByDate);
			}
			addSlotToTargets(target, "Any", slotsByDate);

		}
		{
			buildSubMenu(manager, "Slots By Contract", source, sourceIsLoad, slotsByContract, false, true);
			buildSubMenu(manager, "Slots By Date", source, sourceIsLoad, slotsByDate, true, true);
			buildSubMenu(manager, "Slots By Port", source, sourceIsLoad, slotsByPort, true, false);
		}
	}

	private void addSlotToTargets(final Slot target, final String group, final Map<String, Set<Slot>> targets) {
		Set<Slot> targetGroupSlots;
		if (targets.containsKey(group)) {
			targetGroupSlots = targets.get(group);
		} else {
			targetGroupSlots = createSlotTreeSet();
			targets.put(group, targetGroupSlots);
		}
		targetGroupSlots.add(target);
	}

	private void buildSubMenu(final IMenuManager manager, final String name, final Slot source, final boolean sourceIsLoad, final Map<String, Set<Slot>> targets, final boolean includeContract,
			final boolean includePort) {
		final MenuManager subMenu = new MenuManager(name, null);

		// For single item sub menus, skip the sub menu and add item directly
		if (targets.size() == 1) {
			for (final Map.Entry<String, Set<Slot>> e : targets.entrySet()) {
				for (final Slot target : e.getValue()) {
					createWireAction(subMenu, source, target, sourceIsLoad, includeContract, includePort);
				}
			}

		} else {
			for (final Map.Entry<String, Set<Slot>> e : targets.entrySet()) {
				final MenuManager subSubMenu = new MenuManager(e.getKey(), null);
				for (final Slot target : e.getValue()) {
					createWireAction(subSubMenu, source, target, sourceIsLoad, includeContract, includePort);
				}
				subMenu.add(subSubMenu);
			}

		}

		manager.add(subMenu);
	}

	private TreeSet<Slot> createSlotTreeSet() {
		final TreeSet<Slot> slotsByDate = new TreeSet<Slot>(new Comparator<Slot>() {

			@Override
			public int compare(final Slot o1, final Slot o2) {
				return o1.getWindowStart().compareTo(o2.getWindowStart());
			}
		});
		return slotsByDate;
	}

	private Cargo createNewCargo(final CargoModel cargoModel) {
		// Create a cargo
		final Cargo newCargo = createObject(CargoPackage.eINSTANCE.getCargo(), CargoPackage.eINSTANCE.getCargoModel_Cargoes(), cargoModel);
		newCargo.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), EcoreUtil.generateUUID());

		// Allow re-wiring
		newCargo.setAllowRewiring(true);

		currentWiringCommand.append(AddCommand.create(jointModelEditorPart.getEditingDomain(), cargoModel, CargoPackage.eINSTANCE.getCargoModel_Cargoes(), newCargo));
		return newCargo;
	}

	private SpotLoadSlot createNewSpotLoad(final CargoModel cargoModel, final boolean isDESPurchase, final SpotMarket market) {

		final SpotLoadSlot newLoad = createObject(CargoPackage.eINSTANCE.getSpotLoadSlot(), CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), cargoModel);
		newLoad.setDESPurchase(isDESPurchase);
		newLoad.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), EcoreUtil.generateUUID());
		newLoad.setMarket(market);
		newLoad.setContract((Contract) market.getContract());
		newLoad.setOptional(true);
		newLoad.setName("");
		currentWiringCommand.append(AddCommand.create(jointModelEditorPart.getEditingDomain(), cargoModel, CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), newLoad));

		return newLoad;
	}

	private LoadSlot createNewLoad(final CargoModel cargoModel, final boolean isDESPurchase) {

		final LoadSlot newLoad = createObject(CargoPackage.eINSTANCE.getLoadSlot(), CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), cargoModel);
		newLoad.setDESPurchase(isDESPurchase);
		newLoad.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), EcoreUtil.generateUUID());
		newLoad.setName("");
		currentWiringCommand.append(AddCommand.create(jointModelEditorPart.getEditingDomain(), cargoModel, CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), newLoad));

		return newLoad;
	}

	private DischargeSlot createNewDischarge(final CargoModel cargoModel, final boolean isFOBSale) {

		final DischargeSlot newDischarge = createObject(CargoPackage.eINSTANCE.getDischargeSlot(), CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(), cargoModel);
		newDischarge.setFOBSale(isFOBSale);
		newDischarge.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), EcoreUtil.generateUUID());
		newDischarge.setName("");
		currentWiringCommand.append(AddCommand.create(jointModelEditorPart.getEditingDomain(), cargoModel, CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(), newDischarge));
		return newDischarge;
	}

	private SpotDischargeSlot createNewSpotDischarge(final CargoModel cargoModel, final boolean isFOBSale, final SpotMarket market) {

		final SpotDischargeSlot newDischarge = createObject(CargoPackage.eINSTANCE.getSpotDischargeSlot(), CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(), cargoModel);
		newDischarge.setFOBSale(isFOBSale);
		newDischarge.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), EcoreUtil.generateUUID());
		newDischarge.setMarket(market);
		newDischarge.setContract((Contract) market.getContract());
		newDischarge.setName("");
		if (market instanceof DESSalesMarket) {

			final DESSalesMarket desSalesMarket = (DESSalesMarket) market;
			newDischarge.setPort((Port) desSalesMarket.getNotionalPort());
		}
		newDischarge.setOptional(true);
		currentWiringCommand.append(AddCommand.create(jointModelEditorPart.getEditingDomain(), cargoModel, CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(), newDischarge));
		return newDischarge;
	}

	IMenuListener createLoadSlotMenuListener(final int index) {
		final CargoModel cargoModel = jointModelEditorPart.getRootObject().getSubModel(CargoModel.class);
		final IMenuListener l = new IMenuListener() {

			@Override
			public void menuAboutToShow(final IMenuManager manager) {
				final LoadSlot loadSlot = loadSlots.get(index);
				final MenuManager newMenuManager = new MenuManager("New...", null);
				manager.add(newMenuManager);
				if (loadSlot.isDESPurchase()) {
					createNewSlotMenu(newMenuManager, loadSlot, true);
					createSpotMarketMenu(newMenuManager, SpotType.DES_SALE, loadSlot, true);
				} else {
					createNewSlotMenu(newMenuManager, loadSlot, true);
					createMenus(manager, loadSlot, cargoModel.getDischargeSlots(), true);
					createSpotMarketMenu(newMenuManager, SpotType.DES_SALE, loadSlot, true);
					createSpotMarketMenu(newMenuManager, SpotType.FOB_SALE, loadSlot, true);
				}
				createEditMenu(manager, loadSlot, loadSlot.getCargo());
				createDeleteSlotMenu(manager, loadSlot);
			}
		};
		return l;

	}

	IMenuListener createDischargeSlotMenuListener(final int index) {
		final CargoModel cargoModel = jointModelEditorPart.getRootObject().getSubModel(CargoModel.class);
		final IMenuListener l = new IMenuListener() {

			@Override
			public void menuAboutToShow(final IMenuManager manager) {

				final DischargeSlot dischargeSlot = dischargeSlots.get(index);

				final MenuManager newMenuManager = new MenuManager("New...", null);
				manager.add(newMenuManager);
				if (dischargeSlot.isFOBSale()) {
					createNewSlotMenu(newMenuManager, dischargeSlot, false);
					createSpotMarketMenu(newMenuManager, SpotType.FOB_PURCHASE, dischargeSlot, false);
				} else {
					createNewSlotMenu(newMenuManager, dischargeSlot, false);
					createMenus(manager, dischargeSlot, cargoModel.getLoadSlots(), false);
					createSpotMarketMenu(newMenuManager, SpotType.DES_PURCHASE, dischargeSlot, false);
					createSpotMarketMenu(newMenuManager, SpotType.FOB_PURCHASE, dischargeSlot, false);
				}
				createEditMenu(manager, dischargeSlot, dischargeSlot.getCargo());
				createDeleteSlotMenu(manager, dischargeSlot);
			}

		};
		return l;

	}

	private void createDeleteSlotMenu(final IMenuManager newMenuManager, final Slot slot) {
		final Action deleteAction = new Action("Delete") {
			@Override
			public void run() {

				currentWiringCommand = new CompoundCommand("Delete slot");
				currentWiringCommand.append(DeleteCommand.create(jointModelEditorPart.getEditingDomain(), slot));
				Cargo cargo = null;
				if (slot instanceof LoadSlot) {
					final LoadSlot loadSlot = (LoadSlot) slot;
					cargo = loadSlot.getCargo();
				}
				if (slot instanceof DischargeSlot) {
					final DischargeSlot dischargeSlot = (DischargeSlot) slot;
					cargo = dischargeSlot.getCargo();
				}
				if (cargo != null) {
					currentWiringCommand.append(DeleteCommand.create(jointModelEditorPart.getEditingDomain(), cargo));
				}
				executeCurrentWiringCommand();
			}
		};
		newMenuManager.add(new Separator());
		newMenuManager.add(deleteAction);

	}

	private void createEditMenu(final IMenuManager newMenuManager, final Slot slot, final Cargo cargo) {
		newMenuManager.add(new Separator());
		newMenuManager.add(new EditAction("Edit Slot", slot));
		if (cargo != null) {
			newMenuManager.add(new EditAction("Edit Cargo", cargo));
		}
	}

	void createSpotMarketMenu(final IMenuManager manager, final SpotType spotType, final Slot source, final boolean sourceIsLoad) {
		final PricingModel pricingModel = jointModelEditorPart.getRootObject().getSubModel(PricingModel.class);
		final Collection<SpotMarket> validMarkets = new LinkedList<SpotMarket>();
		String menuName = "";
		boolean isSpecial = false;
		if (spotType == SpotType.DES_PURCHASE) {
			menuName = "DES Purchase";
			final SpotMarketGroup group = pricingModel.getDesPurchaseSpotMarket();
			for (final SpotMarket market : group.getMarkets()) {
				final Set<APort> ports = SetUtils.getPorts(((DESPurchaseMarket) market).getDestinationPorts());
				if (ports.contains(source.getPort())) {
					validMarkets.add(market);
				}
			}
			isSpecial = true;
		} else if (spotType == SpotType.DES_SALE) {
			menuName = "DES Sale";
			validMarkets.addAll(pricingModel.getDesSalesSpotMarket().getMarkets());
		} else if (spotType == SpotType.FOB_PURCHASE) {
			menuName = "FOB Purchase";
			validMarkets.addAll(pricingModel.getFobPurchasesSpotMarket().getMarkets());
		} else if (spotType == SpotType.FOB_SALE) {
			menuName = "FOB Sale";
			final SpotMarketGroup group = pricingModel.getFobSalesSpotMarket();
			for (final SpotMarket market : group.getMarkets()) {
				final APort loadPort = ((FOBSalesMarket) market).getLoadPort();
				if (loadPort == source.getPort()) {
					validMarkets.add(market);
				}
			}
			isSpecial = true;
		}
		final MenuManager subMenu = new MenuManager("New " + menuName + " Market Slot", null);

		for (final SpotMarket market : validMarkets) {
			subMenu.add(new CreateSlotAction("Create " + market.getName() + " slot", source, market, sourceIsLoad, isSpecial));
		}

		manager.add(subMenu);

	}

	void createNewSlotMenu(final IMenuManager menuManager, final Slot source, final boolean sourceIsLoad) {

		if (sourceIsLoad) {
			final LoadSlot loadSlot = (LoadSlot) source;
			if (loadSlot.isDESPurchase()) {
				// Only create new Discharge
				menuManager.add(new CreateSlotAction("Discharge", source, null, sourceIsLoad, false));
			} else {
				//
				menuManager.add(new CreateSlotAction("Discharge", source, null, sourceIsLoad, false));
				menuManager.add(new CreateSlotAction("FOB Sale", source, null, sourceIsLoad, true));
			}
		} else {
			final DischargeSlot dischargeSlot = (DischargeSlot) source;
			if (dischargeSlot.isFOBSale()) {
				// only load
				menuManager.add(new CreateSlotAction("Load", source, null, sourceIsLoad, false));
			} else {
				menuManager.add(new CreateSlotAction("Load", source, null, sourceIsLoad, false));
				menuManager.add(new CreateSlotAction("DES Purchase", source, null, sourceIsLoad, true));
			}
		}
	}

	private final class EditAction extends Action {
		private final EObject target;

		private EditAction(final String text, final EObject target) {
			super(text);
			this.target = target;
		}

		@Override
		public void run() {

			final DetailCompositeDialog dcd = new DetailCompositeDialog(getScenarioViewer().getGrid().getShell(), jointModelEditorPart.getDefaultCommandHandler());
			try {
				jointModelEditorPart.getEditorLock().claim();
				jointModelEditorPart.setDisableUpdates(true);
				dcd.open(jointModelEditorPart, jointModelEditorPart.getRootObject(), Collections.<EObject> singletonList(target), locked);
			} finally {
				jointModelEditorPart.setDisableUpdates(false);
				jointModelEditorPart.getEditorLock().release();
			}
		}
	}

	class CreateSlotAction extends Action {

		private final Slot source;
		private final SpotMarket market;
		private final boolean sourceIsLoad;
		private final boolean isSpecial;

		public CreateSlotAction(final String name, final Slot source, final SpotMarket market, final boolean sourceIsLoad, final boolean isSpecial) {
			super(name);
			this.source = source;
			this.market = market;
			this.sourceIsLoad = sourceIsLoad;
			this.isSpecial = isSpecial;
		}

		@Override
		public void run() {
			final CargoModel cargoModel = jointModelEditorPart.getRootObject().getSubModel(CargoModel.class);

			currentWiringCommand = new CompoundCommand("Rewire Cargoes");
			LoadSlot loadSlot;
			DischargeSlot dischargeSlot;
			if (sourceIsLoad) {
				loadSlot = (LoadSlot) source;
				if (market == null) {
					dischargeSlot = createNewDischarge(cargoModel, isSpecial);
				} else {
					dischargeSlot = createNewSpotDischarge(cargoModel, isSpecial, market);
				}
			} else {
				if (market == null) {
					loadSlot = createNewLoad(cargoModel, isSpecial);
				} else {
					loadSlot = createNewSpotLoad(cargoModel, isSpecial, market);
				}
				dischargeSlot = (DischargeSlot) source;
			}
			runWiringUpdate(loadSlot, dischargeSlot);

		}
	}

	class WireAction extends Action {

		final private LoadSlot loadSlot;
		final private DischargeSlot dischargeSlot;

		public WireAction(final String text, final LoadSlot loadSlot, final DischargeSlot dischargeSlot) {
			super(text);
			this.loadSlot = loadSlot;
			this.dischargeSlot = dischargeSlot;
		}

		@Override
		public void run() {

			currentWiringCommand = new CompoundCommand("Rewire Cargoes");
			runWiringUpdate(loadSlot, dischargeSlot);
		}

	}

	private void createWireAction(final MenuManager subMenu, final Slot source, final Slot target, final boolean sourceIsLoad, final boolean includeContract, final boolean includePort) {
		final String name = getActionName(target, includeContract, includePort);
		if (sourceIsLoad) {
			subMenu.add(new WireAction(name, (LoadSlot) source, (DischargeSlot) target));
		} else {
			subMenu.add(new WireAction(name, (LoadSlot) target, (DischargeSlot) source));
		}
	}

	/**
	 * Sub-classes should override to handle mouse drag notifications
	 * 
	 * @param newXPos
	 * @param newYPos
	 */
	protected void requestScrollTo(final int newXPos, final int newYPos) {

	}

	/**
	 * A combined {@link MouseListener} and {@link MouseMoveListener} to call {@link CargoWiringComposite#requestScrollTo(int, int)} during mouse drag
	 * 
	 */
	private class WiringDiagramMouseListener implements MouseListener, MouseMoveListener {

		private boolean dragging = false;

		@Override
		public void mouseMove(final MouseEvent e) {
			if (dragging) {

				final Point p = getScenarioViewer().getGrid().toDisplay(e.x, e.y);
				requestScrollTo(p.x, p.y);
			}
		}

		@Override
		public void mouseDoubleClick(final MouseEvent e) {

		}

		@Override
		public void mouseDown(final MouseEvent e) {
			dragging = true;

		}

		@Override
		public void mouseUp(final MouseEvent e) {
			dragging = false;
		}
	}

	boolean isWiringValid(final Cargo cargo, final LoadSlot loadSlot, final DischargeSlot dischargeSlot) {

		if (cargo == null) {
			return false;
		}

		if (loadSlot == null || dischargeSlot == null) {
			return false;
		}

		// if (validationMap.contains(loadSlot) || validationMap.contains(dischargeSlot) || validationMap.contains(cargo)) {
		// return false;
		// }
		return true;
	}

	private GridViewerColumn addWiringColumn() {
		// TODO Auto-generated method stub
		final GridViewerColumn wiringColumn = getScenarioViewer().addSimpleColumn("", false);
		wiringColumn.getColumn().setMinimumWidth(100);
		wiringColumn.getColumn().setWidth(100);
		wiringColumn.getColumn().setResizeable(false);

		wiringColumn.setLabelProvider(new EObjectTableViewerColumnProvider(getScenarioViewer(), null, null) {
			@Override
			public String getText(final Object element) {
				// TODO Auto-generated method stub
				return null;
			}
		});
		return wiringColumn;
	}

	private <T extends ICellManipulator & ICellRenderer> GridViewerColumn addTradesColumn(final String columnName, final T manipulator, final EMFPath path) {
		return getScenarioViewer().addColumn(columnName, manipulator, manipulator, path);
	}

	public void setLocked(final boolean locked) {
		if (this.locked == locked) {
			return;
		}
		this.locked = locked;
		super.setLocked(locked);
		wiringDiagram.setLocked(locked);
	}

}
