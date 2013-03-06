/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
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
import org.eclipse.jface.viewers.IElementComparer;
import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridCellRenderer;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.nebula.widgets.grid.internal.DefaultCellRenderer;
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
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.menus.IMenuService;

import com.mmxlabs.common.Equality;
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
import com.mmxlabs.models.lng.cargo.ui.util.SpotSlotHelper;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.input.ElementAssignment;
import com.mmxlabs.models.lng.input.InputModel;
import com.mmxlabs.models.lng.input.editor.utils.AssignmentEditorHelper;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.spotmarkets.SpotType;
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
import com.mmxlabs.models.ui.tabular.EObjectTableViewer;
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

/**
 * Tabular editor displaying cargoes and slots with a custom wiring editor. This implementation is "stupid" in that any changes to the data cause a full update. This has the disadvantage of loosing
 * the current ordering of items. Each row is a cargo. Changing the wiring will re-order slots. The {@link CargoWiringComposite} based view only re-orders slots when requested permitting the original
 * (or at least the wiring at time of opening the editor) wiring to be seem via rows and the current wiring via the wire lines.
 * 
 * 
 * @author Simon Goodall
 * 
 */
public class TradesWiringViewer extends ScenarioTableViewerPane {

	private final List<Integer> wiring = new ArrayList<Integer>();
	private TradesWiringDiagram wiringDiagram;

	private final ArrayList<Boolean> leftTerminalsExist = new ArrayList<Boolean>();
	private final ArrayList<Boolean> rightTerminalsExist = new ArrayList<Boolean>();
	protected List<RowData> rowData;

	private final ArrayList<Cargo> cargoes = new ArrayList<Cargo>();
	private final ArrayList<LoadSlot> loadSlots = new ArrayList<LoadSlot>();
	private final ArrayList<DischargeSlot> dischargeSlots = new ArrayList<DischargeSlot>();

	private final Set<GridColumn> loadColumns = new HashSet<GridColumn>();
	private final Set<GridColumn> dischargeColumns = new HashSet<GridColumn>();

	private Object[] sortedChildren;
	private int[] sortedIndices;

	protected int[] reverseSortedIndices;

	private boolean locked;

	private CompoundCommand currentWiringCommand = null;

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
			performControlUpdate(false);
		}
	};

	public TradesWiringViewer(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation jointModelEditorPart, final IActionBars actionBars) {
		super(page, part, jointModelEditorPart, actionBars);

		wiredImage = CargoEditorPlugin.getPlugin().getImage(CargoEditorPlugin.IMAGE_CARGO_LINK);
		lockedImage = CargoEditorPlugin.getPlugin().getImage(CargoEditorPlugin.IMAGE_CARGO_LOCK);
	}

	@Override
	public void dispose() {

		// this.location.getStatusProvider().removeStatusChangedListener(statusChangedListener);

		if (this.jointModelEditorPart != null) {
			final MMXRootObject rootObject = jointModelEditorPart.getRootObject();
			if (rootObject != null) {
				final CargoModel cargoModel = rootObject.getSubModel(CargoModel.class);
				if (cargoModel != null) {
					cargoModel.eAdapters().remove(cargoChangeAdapter);
					for (final Cargo c : cargoModel.getCargoes()) {
						c.eAdapters().remove(cargoChangeAdapter);
					}
				}
			}

			for (final Cargo c : cargoes) {
				if (c != null) {
					c.eAdapters().remove(cargoChangeAdapter);
				}
			}

		}

		this.cargoes.clear();
		this.loadSlots.clear();
		this.dischargeSlots.clear();

		this.currentWiringCommand = null;
		this.rightTerminalsExist.clear();
		this.leftTerminalsExist.clear();
		this.wiring.clear();

		this.rowData.clear();

		super.dispose();
	}

	protected ScenarioTableViewer constructViewer(final Composite parent) {

		final ScenarioTableViewer scenarioViewer = new ScenarioTableViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL, jointModelEditorPart) {

			/**
			 * Overridden method to convert internal RowData objects into a collection of EMF Objects
			 */
			protected void updateSelection(final ISelection selection) {

				if (selection instanceof IStructuredSelection) {
					final IStructuredSelection originalSelection = (IStructuredSelection) selection;

					final List<Object> selectedObjects = new LinkedList<Object>();

					final Iterator<?> itr = originalSelection.iterator();
					while (itr.hasNext()) {
						final Object obj = itr.next();
						if (obj instanceof RowData) {
							final RowData rd = (RowData) obj;
							if (rd.cargo != null) {
								selectedObjects.add(rd.cargo);
							}
							if (rd.loadSlot != null) {
								selectedObjects.add(rd.loadSlot);
							}
							if (rd.dischargeSlot != null) {
								selectedObjects.add(rd.dischargeSlot);
							}
						}
					}

					super.updateSelection(new StructuredSelection(selectedObjects));
				} else {
					super.updateSelection(selection);
				}
			}

			@Override
			protected Object[] getSortedChildren(final Object parent) {
				// This is the filtered and sorted children.
				// This may be smaller than the original set.
				sortedChildren = super.getSortedChildren(parent);

				sortedIndices = new int[rowData == null ? 0 : rowData.size()];
				reverseSortedIndices = new int[rowData == null ? 0 : rowData.size()];

				Arrays.fill(sortedIndices, -1);
				Arrays.fill(reverseSortedIndices, -1);

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
			protected void processStatus(final IStatus status, final boolean update) {
				super.processStatus(status, update);
				if (!isBusy()) {
					refresh();
				}
			}

			protected void updateObject(final EObject object, final IStatus status, final boolean update) {
				if (object == null) {
					return;
				}

				int idx = -1;
				if (loadSlots.contains(object)) {
					idx = loadSlots.indexOf(object);
				} else if (dischargeSlots.contains(object)) {
					idx = dischargeSlots.indexOf(object);
				} else if (cargoes.contains(object)) {
					idx = cargoes.indexOf(object);
				}

				RowData rd = null;
				if (idx >= 0) {
					rd = rowData.get(idx);
				}

				if (rd != null) {
					setStatus(rd, status);
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

			@Override
			@SuppressWarnings("restriction")
			protected GridCellRenderer createCellRenderer() {
				return new DefaultCellRenderer();
			}

		};
		final MenuManager mgr = new MenuManager();
		scenarioViewer.getGrid().addMenuDetectListener(new MenuDetectListener() {

			private Menu menu;

			@Override
			public void menuDetected(final MenuDetectEvent e) {

				if (locked) {
					return;
				}
				final Point mousePoint = getScenarioViewer().getGrid().toControl(new Point(e.x, e.y));
				final GridColumn column = getScenarioViewer().getGrid().getColumn(mousePoint);

				final GridItem item = getScenarioViewer().getGrid().getItem(mousePoint);
				if (item == null) {
					return;
				}
				final Object data = item.getData();
				if (data instanceof RowData) {

					final RowData rowDataItem = (RowData) data;
					final int idx = rowData.indexOf(rowDataItem);

					if (menu == null) {
						menu = mgr.createContextMenu(scenarioViewer.getGrid());
					}
					mgr.removeAll();

					// TODO: Simple load/discharge filter. Really need to determine when we build the columns and save into a set somewhere
					if (loadColumns.contains(column) && rowDataItem.loadSlot != null) {

						final IMenuListener listener = createLoadSlotMenuListener(idx);
						listener.menuAboutToShow(mgr);
					}
					if (dischargeColumns.contains(column) && rowDataItem.dischargeSlot != null) {

						final IMenuListener listener = createDischargeSlotMenuListener(idx);
						listener.menuAboutToShow(mgr);
					}

					menu.setVisible(true);
				}
			}
		});

		scenarioViewer.setComparer(new IElementComparer() {
			@Override
			public int hashCode(final Object element) {
				return element.hashCode();
			}

			@Override
			public boolean equals(final Object a, final Object b) {

				final Set<Object> aSet = getObjectSet(a);
				final Set<Object> bSet = getObjectSet(b);

				aSet.retainAll(bSet);
				if (!aSet.isEmpty()) {
					return true;
				}

				return Equality.isEqual(a, b);
			}

			private Set<Object> getObjectSet(final Object a) {
				final Set<Object> aSet = new HashSet<Object>();
				if (a instanceof RowData) {
					final RowData rd = (RowData) a;
					aSet.add(rd);
					aSet.add(rd.cargo);
					aSet.add(rd.loadSlot);
					aSet.add(rd.dischargeSlot);
					aSet.add(rd.elementAssignment);
					aSet.remove(null);
				} else if (a instanceof CargoAllocation) {
					final CargoAllocation cargoAllocation = (CargoAllocation) a;
					aSet.add(cargoAllocation.getInputCargo());
					aSet.add(cargoAllocation.getLoadAllocation().getSlot());
					aSet.add(cargoAllocation.getDischargeAllocation().getSlot());
				} else if (a instanceof SlotAllocation) {
					final SlotAllocation slotAllocation = (SlotAllocation) a;
					aSet.add(slotAllocation.getSlot());
				} else if (a instanceof SlotVisit) {
					final SlotVisit slotVisit = (SlotVisit) a;
					aSet.add(slotVisit.getSlotAllocation().getSlot());
				} else {
					aSet.add(a);
				}
				return aSet;
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

		final Action addAction = new AddAction("Add");
		addAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ADD));
		toolbar.appendToGroup(ADD_REMOVE_GROUP, addAction);

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

		addTradesColumn(loadColumns, "L-ID", new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getNamedObject_Name(), editingDomain), new RowDataEMFPath(Type.LOAD, true));
		addTradesColumn(loadColumns, "Port", new SingleReferenceManipulator(pkg.getSlot_Port(), provider, editingDomain), new RowDataEMFPath(Type.LOAD, true));
		addTradesColumn(loadColumns, "Buy At", new ContractManipulator(provider, editingDomain), new RowDataEMFPath(Type.LOAD, true));
		addTradesColumn(loadColumns, "Date", new DateAttributeManipulator(pkg.getSlot_WindowStart(), editingDomain), new RowDataEMFPath(Type.LOAD, true));

		final GridViewerColumn wiringColumn = addWiringColumn();

		addTradesColumn(dischargeColumns, "Date", new DateAttributeManipulator(pkg.getSlot_WindowStart(), editingDomain), new RowDataEMFPath(Type.DISCHARGE, true));
		addTradesColumn(dischargeColumns, "Sell At", new ContractManipulator(provider, editingDomain), new RowDataEMFPath(Type.DISCHARGE, true));
		addTradesColumn(dischargeColumns, "Port", new SingleReferenceManipulator(pkg.getSlot_Port(), provider, editingDomain), new RowDataEMFPath(Type.DISCHARGE, true));
		addTradesColumn(dischargeColumns, "D-ID", new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getNamedObject_Name(), editingDomain), new RowDataEMFPath(Type.DISCHARGE, true));

		addTradesColumn("Assignment", new AssignmentManipulator(jointModelEditorPart), new RowDataEMFPath(Type.CARGO, true));

		wiringDiagram = new TradesWiringDiagram(getScenarioViewer().getGrid()) {

			@Override
			protected void wiringChanged(final List<Integer> newWiring) {
				if (locked) {
					return;
				}
				doWiringChanged(newWiring);
			}

			@Override
			public void onMouseDown() {
				// Initiated a drag, Disable selection in table
				getScenarioViewer().getGrid().setCellSelectionEnabled(false);
				super.onMouseDown();
			}

			@Override
			public void onMouseup() {
				// Enable selection in table
				getScenarioViewer().getGrid().setCellSelectionEnabled(true);
				super.onMouseup();
			}

			@Override
			protected List<Float> getTerminalPositions() {

				// Determine the mid-point in each row and generate an ordered list of heights.

				// +1 to to make loop simpler
				final int heights[] = new int[wiring.size() + 1];
				int idx = 0;
				heights[0] = getScenarioViewer().getGrid().getHeaderHeight();

				// Pass one, get heights
				for (final GridItem item : getScenarioViewer().getGrid().getItems()) {
					idx++;
					heights[idx] = 1 + heights[idx - 1] + item.getHeight();
				}
				// Find the row at the top of the table and get it's "height" so we can adjust it later
				final int vPod = getScenarioViewer().getGrid().getVerticalBar().getSelection();
				final int hOffset = (heights[vPod]) - getScenarioViewer().getGrid().getHeaderHeight();
				// Pass 2 get mid-points
				idx = -1;
				for (final GridItem item : getScenarioViewer().getGrid().getItems()) {
					heights[++idx] += item.getHeight() / 2;
				}

				// Pass 3, offset to so top visible row in table is at height "0"
				idx = -1;
				for (final GridItem item : getScenarioViewer().getGrid().getItems()) {
					heights[++idx] -= hOffset;
				}

				// Convert to
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

	private class RowData extends EObjectImpl {

		Cargo cargo;
		LoadSlot loadSlot;
		DischargeSlot dischargeSlot;
		ElementAssignment elementAssignment;

		/**
		 * This is used in the {@link EObjectTableViewer} implementation of {@link ViewerComparator} for the fixed sort order.
		 */
		@Override
		public boolean equals(final Object obj) {

			if (obj instanceof RowData) {
				final RowData other = (RowData) obj;
				return Equality.isEqual(cargo, other.cargo) && Equality.isEqual(loadSlot, other.loadSlot) && Equality.isEqual(dischargeSlot, other.dischargeSlot)
						&& Equality.isEqual(elementAssignment, other.elementAssignment);
			}

			return false;
		}

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

		for (int i = 0; i < newCargoes.size(); i++) {
			final Cargo cargo = newCargoes.get(i);

			final RowData rd = new RowData();
			rd.cargo = cargo;
			rd.loadSlot = cargo.getLoadSlot();
			rd.dischargeSlot = cargo.getDischargeSlot();
			rows.add(rd);

			this.cargoes.add(rd.cargo);
			this.loadSlots.add(rd.loadSlot);
			this.dischargeSlots.add(rd.dischargeSlot);
		}

		for (final LoadSlot slot : loadSlots) {
			if (slot.getCargo() == null) {

				final RowData rd = new RowData();
				rd.loadSlot = slot;
				rows.add(rd);

				this.cargoes.add(rd.cargo);
				this.loadSlots.add(rd.loadSlot);
				this.dischargeSlots.add(rd.dischargeSlot);
			}
		}
		for (final DischargeSlot slot : dischargeSlots) {
			if (slot.getCargo() == null) {
				final RowData rd = new RowData();
				rd.dischargeSlot = slot;
				rows.add(rd);

				this.cargoes.add(rd.cargo);
				this.loadSlots.add(rd.loadSlot);
				this.dischargeSlots.add(rd.dischargeSlot);

			}
		}

		// Sanity check!
		assert rows.size() == this.cargoes.size();
		assert rows.size() == this.loadSlots.size();
		assert rows.size() == this.dischargeSlots.size();

		return rows;
	}

	public void init(final AdapterFactory adapterFactory) {
		getScenarioViewer().init(adapterFactory);

	}

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
		scenarioViewer.addOpenListener(new IOpenListener() {

			@Override
			public void open(final OpenEvent event) {
				if (scenarioViewer.getSelection() instanceof IStructuredSelection) {
					final IStructuredSelection structuredSelection = (IStructuredSelection) scenarioViewer.getSelection();
					if (structuredSelection.isEmpty() == false) {
						if (structuredSelection.size() == 1) {

							EObject target = null;
							final Object obj = structuredSelection.getFirstElement();
							if (obj instanceof RowData) {
								final RowData rd = (RowData) obj;
								if (rd.cargo != null) {
									target = rd.cargo;
								} else if (rd.loadSlot != null) {
									target = rd.loadSlot;
								} else if (rd.dischargeSlot != null) {
									target = rd.dischargeSlot;
								}
							}
							if (target == null) {
								return;
							}
							final DetailCompositeDialog dcd = new DetailCompositeDialog(event.getViewer().getControl().getShell(), jointModelEditorPart.getDefaultCommandHandler());
							try {
								jointModelEditorPart.getEditorLock().claim();
								jointModelEditorPart.setDisableUpdates(true);

								dcd.open(jointModelEditorPart, jointModelEditorPart.getRootObject(), Collections.singletonList(target), scenarioViewer.isLocked());
							} finally {
								jointModelEditorPart.setDisableUpdates(false);
								jointModelEditorPart.getEditorLock().release();
							}
						} else {
							// No support yet.

							// try {
							// jointModelEditorPart.getEditorLock().claim();
							// if (scenarioViewer.isLocked() == false) {
							// final MultiDetailDialog mdd = new MultiDetailDialog(event.getViewer().getControl().getShell(), jointModelEditorPart.getRootObject(), jointModelEditorPart
							// .getDefaultCommandHandler());
							// mdd.open(jointModelEditorPart, structuredSelection.toList());
							// }
							// } finally {
							// jointModelEditorPart.getEditorLock().release();
							// }
						}
					}
				}
			}
		});
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

		// if (getScenarioViewer() == null || getScenarioViewer().getGrid().isDisposed()) {
		// return;
		// }
		synchronized (updateLock) {
			// Re-set the input to trigger full update as we push a load of stuff into the content provider which probably should not be there....

			final Object oldInput = getScenarioViewer().getInput();
			// Attempt to grab the current sort order and pass into the viewer so input changes do not alter the default ordering.
			final ViewerComparator comparator = getScenarioViewer().getComparator();
			Object[] sortedElements = null;
			if (comparator != null) {

				final Object[] elements = ((IStructuredContentProvider) getScenarioViewer().getContentProvider()).getElements(oldInput);
				comparator.sort(getScenarioViewer(), elements);
				sortedElements = elements;

				getScenarioViewer().setFixedSortOrder(Arrays.asList(sortedElements));
			}

			getScenarioViewer().setInput(null);
			getScenarioViewer().setInput(oldInput);
		}
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
			if (loadSlot != null) {
				if (newIndex >= 0 && newIndex < newWiring.size()) {
					final DischargeSlot otherDischarge = rowData.get(newIndex).dischargeSlot;
					if (otherDischarge != null) {
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
					}
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
//		newLoad.setContract((Contract) market.getContract());
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
//		newDischarge.setContract((Contract) market.getContract());
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
				createEditContractMenu(manager, loadSlot, loadSlot.getContract());
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
				createEditContractMenu(manager, dischargeSlot, dischargeSlot.getContract());
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

	private void createEditContractMenu(final IMenuManager newMenuManager, final Slot slot, final Contract contract) {
		newMenuManager.add(new Separator());
		newMenuManager.add(new EditAction("Edit Contract", contract));
	}

	void createSpotMarketMenu(final IMenuManager manager, final SpotType spotType, final Slot source, final boolean sourceIsLoad) {
		final SpotMarketsModel pricingModel = jointModelEditorPart.getRootObject().getSubModel(SpotMarketsModel.class);
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

		if (rowData == null) {
			return true;
		}

		final Map<Object, IStatus> validationMap = getScenarioViewer().getValidationErrors();
		if (cargo == null) {
			return false;
		} else {
			final int indexOf = cargoes.indexOf(cargo);
			if (indexOf >= 0 && indexOf < rowData.size()) {
				final RowData rd = rowData.get(indexOf);
				if (validationMap.containsKey(rd)) {
					return false;
				}
			}
		}
		if (loadSlot == null) {
			return false;
		} else {
			final int indexOf = loadSlots.indexOf(loadSlot);
			if (indexOf >= 0 && indexOf < rowData.size()) {
				final RowData rd = rowData.get(indexOf);
				if (validationMap.containsKey(rd)) {
					return false;
				}
			}
		}
		if (dischargeSlot == null) {
			return false;
		} else {
			final int indexOf = dischargeSlots.indexOf(dischargeSlot);
			if (indexOf >= 0 && indexOf < rowData.size()) {
				final RowData rd = rowData.get(indexOf);
				if (validationMap.containsKey(rd)) {
					return false;
				}
			}
		}

		return true;
	}

	private GridViewerColumn addWiringColumn() {
		final GridViewerColumn wiringColumn = getScenarioViewer().addSimpleColumn("", false);
		wiringColumn.getColumn().setMinimumWidth(100);
		wiringColumn.getColumn().setWidth(100);
		wiringColumn.getColumn().setResizeable(false);

		wiringColumn.setLabelProvider(new EObjectTableViewerColumnProvider(getScenarioViewer(), null, null) {
			@Override
			public String getText(final Object element) {
				return null;
			}
		});
		return wiringColumn;
	}

	private <T extends ICellManipulator & ICellRenderer> GridViewerColumn addTradesColumn(final String columnName, final T manipulator, final EMFPath path) {
		return this.addTradesColumn(null, columnName, manipulator, path);
	}

	private <T extends ICellManipulator & ICellRenderer> GridViewerColumn addTradesColumn(final Set<GridColumn> group, final String columnName, final T manipulator, final EMFPath path) {
		final GridViewerColumn col = getScenarioViewer().addColumn(columnName, manipulator, manipulator, path);
		if (group != null) {
			group.add(col.getColumn());
		}
		return col;
	}

	public void setLocked(final boolean locked) {
		if (this.locked == locked) {
			return;
		}
		this.locked = locked;
		super.setLocked(locked);
		wiringDiagram.setLocked(locked);
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
}
