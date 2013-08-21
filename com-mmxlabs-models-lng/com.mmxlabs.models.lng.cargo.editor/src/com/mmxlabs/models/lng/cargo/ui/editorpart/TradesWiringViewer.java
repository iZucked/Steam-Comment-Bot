/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
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
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.IElementComparer;
import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.window.Window;
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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.menus.IMenuService;

import com.google.common.collect.Lists;
import com.mmxlabs.common.Equality;
import com.mmxlabs.models.lng.assignment.AssignmentModel;
import com.mmxlabs.models.lng.assignment.ElementAssignment;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.editor.editors.ldd.ComplexCargoEditor;
import com.mmxlabs.models.lng.cargo.presentation.CargoEditorPlugin;
import com.mmxlabs.models.lng.cargo.ui.editorpart.CargoModelRowTransformer.GroupData;
import com.mmxlabs.models.lng.cargo.ui.editorpart.CargoModelRowTransformer.RootData;
import com.mmxlabs.models.lng.cargo.ui.editorpart.CargoModelRowTransformer.RowData;
import com.mmxlabs.models.lng.cargo.ui.editorpart.CargoModelRowTransformer.RowDataEMFPath;
import com.mmxlabs.models.lng.cargo.ui.editorpart.CargoModelRowTransformer.Type;
import com.mmxlabs.models.lng.cargo.ui.editorpart.CreateStripDialog.StripType;
import com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.GroupProfitAndLoss;
import com.mmxlabs.models.lng.schedule.MarketAllocation;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.ui.ImageConstants;
import com.mmxlabs.models.lng.ui.LngUIActivator;
import com.mmxlabs.models.lng.ui.actions.DuplicateAction;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewer;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.dates.DateAttributeManipulator;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialog;
import com.mmxlabs.models.ui.editors.dialogs.MultiDetailDialog;
import com.mmxlabs.models.ui.tabular.EObjectTableViewer;
import com.mmxlabs.models.ui.tabular.EObjectTableViewerColumnProvider;
import com.mmxlabs.models.ui.tabular.EObjectTableViewerValidationSupport;
import com.mmxlabs.models.ui.tabular.ICellManipulator;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.ReadOnlyManipulatorWrapper;
import com.mmxlabs.models.ui.tabular.manipulators.SingleReferenceManipulator;
import com.mmxlabs.models.ui.validation.IStatusProvider;
import com.mmxlabs.models.ui.validation.IStatusProvider.IStatusChangedListener;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;
import com.mmxlabs.models.util.emfpath.EMFPath;
import com.mmxlabs.rcp.common.actions.CopyGridToClipboardAction;
import com.mmxlabs.rcp.common.actions.CopyTableToClipboardAction;
import com.mmxlabs.rcp.common.actions.CopyTreeToClipboardAction;
import com.mmxlabs.rcp.common.actions.PackGridTableColumnsAction;
import com.mmxlabs.scenario.service.model.ScenarioLock;

/**
 * Tabular editor displaying cargoes and slots with a custom wiring editor. This implementation is "stupid" in that any changes to the data cause a full update. This has the disadvantage of loosing
 * the current ordering of items. Each row is a cargo. Changing the wiring will re-order slots. The {@link CargoWiringComposite} based view only re-orders slots when requested permitting the original
 * (or at least the wiring at time of opening the editor) wiring to be seem via rows and the current wiring via the wire lines.
 * 
 * 
 * @author Simon Goodall
 * @since 3.0
 * 
 */
public class TradesWiringViewer extends ScenarioTableViewerPane {

	private TradesWiringDiagram wiringDiagram;

	protected RootData rootData;
	/**
	 * A reference {@link RootData} object. This is used by a {@link CargoModelRowTransformer} to retain load/discharge row pairings but allow wires to cross rows. Initially null until the first
	 * rootData object is created. May be "nulled" again to reset state by an action.
	 * 
	 * @since 5.0
	 */
	protected RootData referenceRootData;

	private final Set<GridColumn> loadColumns = new HashSet<GridColumn>();
	private final Set<GridColumn> dischargeColumns = new HashSet<GridColumn>();

	private Object[] sortedChildren;
	private int[] sortedIndices;

	protected int[] reverseSortedIndices;

	private boolean locked;

	private final CargoEditingCommands cec;
	private final CargoEditorMenuHelper menuHelper;

	private final Image lockedImage;

	private IStatusChangedListener statusChangedListener;

	private Action resetSortOrder;

	public TradesWiringViewer(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation scenarioEditingLocation, final IActionBars actionBars) {
		super(page, part, scenarioEditingLocation, actionBars);

		final LNGScenarioModel scenarioModel = (LNGScenarioModel) scenarioEditingLocation.getRootObject();
		this.cec = new CargoEditingCommands(scenarioEditingLocation.getEditingDomain(), scenarioModel, scenarioModel.getPortfolioModel());
		this.menuHelper = new CargoEditorMenuHelper(part.getSite().getShell(), scenarioEditingLocation, scenarioModel, scenarioModel.getPortfolioModel());
		lockedImage = CargoEditorPlugin.getPlugin().getImage(CargoEditorPlugin.IMAGE_CARGO_LOCK);
	}

	@Override
	public void dispose() {

		this.rootData = null;
		this.referenceRootData = null;

		super.dispose();
	}

	protected ScenarioTableViewer constructViewer(final Composite parent) {

		final ScenarioTableViewer scenarioViewer = new ScenarioTableViewer(parent, SWT.FULL_SELECTION | SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL, scenarioEditingLocation) {

			@Override
			public EReference getCurrentContainment() {
				return CargoPackage.eINSTANCE.getCargoModel_Cargoes();
			}

			@Override
			public EObject getCurrentContainer() {
				return getPortfolioModel().getCargoModel();
			}

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

				sortedIndices = new int[rootData == null ? 0 : rootData.getRows().size()];
				reverseSortedIndices = new int[rootData == null ? 0 : rootData.getRows().size()];

				Arrays.fill(sortedIndices, -1);
				Arrays.fill(reverseSortedIndices, -1);

				for (int i = 0; i < sortedChildren.length; ++i) {
					final int rawIndex = rootData.getRows().indexOf(sortedChildren[i]);
					sortedIndices[rawIndex] = i;
					reverseSortedIndices[i] = rawIndex;
				}
				if (wiringDiagram != null) {
					wiringDiagram.setSortOrder(rootData, sortedIndices, reverseSortedIndices);
				}
				return sortedChildren;
			}

			@Override
			public void init(final AdapterFactory adapterFactory, final CommandStack commandStack, final EReference... path) {
				super.init(adapterFactory, commandStack, path);

				init(new IStructuredContentProvider() {

					@Override
					public void dispose() {

					}

					@Override
					public Object[] getElements(final Object inputElement) {

						final CargoModel cargoModel = getPortfolioModel().getCargoModel();
						final AssignmentModel assignmentModel = getPortfolioModel().getAssignmentModel();
						final ScheduleModel scheduleModel = getPortfolioModel().getScheduleModel();

						final RootData root = setCargoes(assignmentModel, cargoModel, scheduleModel, referenceRootData);

						TradesWiringViewer.this.rootData = root;

						if (TradesWiringViewer.this.referenceRootData == null) {
							TradesWiringViewer.this.referenceRootData = root;
						}
						resetSortOrder.setEnabled(TradesWiringViewer.this.referenceRootData != null);

						return rootData.getRows().toArray();
					}

					@Override
					public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {

					}

				}, commandStack);
				// Get default comparator
				final ViewerComparator vc = getComparator();
				// Wrap around with group sorter
				setComparator(new ViewerComparator() {
					@Override
					public int compare(final Viewer viewer, final Object e1, final Object e2) {
						GroupData g1 = null;
						GroupData g2 = null;
						if (e1 instanceof RowData) {
							g1 = ((RowData) e1).getGroup();
						}
						if (e2 instanceof RowData) {
							g2 = ((RowData) e2).getGroup();
						}
						if (g1 == g2) {
							return vc.compare(viewer, e1, e2);
						} else {
							final Object rd1 = (g1 == null || g1.getRows().isEmpty()) ? e1 : g1.getRows().get(0);
							final Object rd2 = (g2 == null || g2.getRows().isEmpty()) ? e2 : g2.getRows().get(0);
							return vc.compare(viewer, rd1, rd2);
						}
					}
				});
			}

			protected EObjectTableViewerValidationSupport createValidationSupport() {
				return new EObjectTableViewerValidationSupport(this) {

					@Override
					public EObject getElementForValidationTarget(final EObject source) {
						return getElementForNotificationTarget(source);
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
						if (rootData.getLoadSlots().contains(object)) {
							idx = rootData.getLoadSlots().indexOf(object);
						} else if (rootData.getDischargeSlots().contains(object)) {
							idx = rootData.getDischargeSlots().indexOf(object);
						} else if (rootData.getCargoes().contains(object)) {
							idx = rootData.getCargoes().indexOf(object);
						}

						RowData rd = null;
						if (idx >= 0) {
							rd = rootData.getRows().get(idx);
						}

						if (rd != null) {
							getValidationSupport().setStatus(rd, status);
						}
					}
				};
			}

			@Override
			public EObject getElementForNotificationTarget(final EObject source) {
				if (source instanceof LoadSlot) {
					return source;
				} else if (source instanceof DischargeSlot) {
					return source;
				} else if (source instanceof Cargo) {
					return source;
				} else if (source instanceof ElementAssignment) {
					return ((ElementAssignment) source).getAssignedObject();
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
					final int idx = rootData.getRows().indexOf(rowDataItem);

					if (menu == null) {
						menu = mgr.createContextMenu(scenarioViewer.getGrid());
					}
					mgr.removeAll();

					if (loadColumns.contains(column)) {
						if (rowDataItem.loadSlot != null) {
							final IMenuListener listener = menuHelper.createLoadSlotMenuListener(rootData.getLoadSlots(), idx);
							listener.menuAboutToShow(mgr);
						} else {
							final IMenuListener listener = menuHelper.createDischargeSlotMenuListener(rootData.getDischargeSlots(), idx);
							listener.menuAboutToShow(mgr);
						}
					}
					if (dischargeColumns.contains(column)) {
						if (rowDataItem.dischargeSlot != null) {
							final IMenuListener listener = menuHelper.createDischargeSlotMenuListener(rootData.getDischargeSlots(), idx);
							listener.menuAboutToShow(mgr);
						} else if (rowDataItem.loadSlot != null) {
							final IMenuListener listener = menuHelper.createLoadSlotMenuListener(rootData.getLoadSlots(), idx);
							listener.menuAboutToShow(mgr);
						}
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

				if (aSet == null || bSet == null) {
					return false;
				}

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
					for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
						aSet.add(slotAllocation.getSlot());
					}
				} else if (a instanceof MarketAllocation) {
					final MarketAllocation marketAllocation = (MarketAllocation) a;
					aSet.add(marketAllocation.getSlot());
				} else if (a instanceof SlotAllocation) {
					final SlotAllocation slotAllocation = (SlotAllocation) a;
					aSet.add(slotAllocation.getSlot());
				} else if (a instanceof SlotVisit) {
					final SlotVisit slotVisit = (SlotVisit) a;
					aSet.add(slotVisit.getSlotAllocation().getSlot());

				} else if (a instanceof Event) {
					Event evt = (Event) a;
					while (evt != null) {
						if (evt instanceof VesselEventVisit) {
							return null;
						} else if (evt instanceof GeneratedCharterOut) {
							return null;
						} else if (evt instanceof SlotVisit) {
							return getObjectSet(evt);
						}
						evt = evt.getPreviousEvent();
					}

				} else {
					aSet.add(a);
				}
				return aSet;
			}
		});

		return scenarioViewer;
	}

	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final CommandStack commandStack) {
		getScenarioViewer().init(adapterFactory, commandStack, new EReference[0]);

		final IStatusProvider statusProvider = scenarioEditingLocation.getStatusProvider();
		getScenarioViewer().setStatusProvider(statusProvider);

		if (statusProvider != null) {
			statusChangedListener = new IStatusChangedListener() {

				@Override
				public void onStatusChanged(final IStatusProvider provider, final IStatus status) {
					final CargoModelRowTransformer transformer = new CargoModelRowTransformer();
					transformer.updateWiringValidity(rootData, getScenarioViewer().getValidationSupport().getValidationErrors());
					wiringDiagram.redraw();
				}
			};
			statusProvider.addStatusChangedListener(statusChangedListener);
		}

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

		if (addAction != null) {
			// if we can't add one, we can't duplicate one either.
			final Action dupAction = createDuplicateAction();

			if (dupAction != null) {
				toolbar.appendToGroup(ADD_REMOVE_GROUP, dupAction);
			}
		}
		deleteAction = createDeleteAction();
		if (deleteAction != null) {
			toolbar.appendToGroup(ADD_REMOVE_GROUP, deleteAction);
		}
		if (actionBars != null) {
			actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(), deleteAction);
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

		// Reset sort order
		{
			resetSortOrder = new Action() {

				public void run() {
					TradesWiringViewer.this.referenceRootData = null;
					TradesWiringViewer.this.viewer.refresh();
					this.setEnabled(false);
				}
			};
			resetSortOrder.setText("Reset Wiring");
			resetSortOrder.setImageDescriptor(CargoEditorPlugin.getPlugin().getImageRegistry().getDescriptor(CargoEditorPlugin.IMAGE_CARGO_WIRING));
			resetSortOrder.setDisabledImageDescriptor(CargoEditorPlugin.getPlugin().getImageRegistry().getDescriptor(CargoEditorPlugin.IMAGE_CARGO_WIRING_DISABLED));
			toolbar.add(resetSortOrder);

		}

		if (actionBars != null) {
			actionBars.updateActionBars();
		}

		toolbar.update(true);

		final CargoPackage pkg = CargoPackage.eINSTANCE;
		final IReferenceValueProviderProvider provider = scenarioEditingLocation.getReferenceValueProviderCache();
		final EditingDomain editingDomain = scenarioEditingLocation.getEditingDomain();

		addTradesColumn(loadColumns, "L-ID", new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getNamedObject_Name(), editingDomain), new RowDataEMFPath(false, Type.LOAD));
		addTradesColumn(loadColumns, "Port", new SingleReferenceManipulator(pkg.getSlot_Port(), provider, editingDomain), new RowDataEMFPath(false, Type.LOAD));
		addTradesColumn(loadColumns, "Buy At", new ContractManipulator(provider, editingDomain), new RowDataEMFPath(false, Type.LOAD));
		addTradesColumn(loadColumns, "Price", new ReadOnlyManipulatorWrapper<BasicAttributeManipulator>(new BasicAttributeManipulator(SchedulePackage.eINSTANCE.getSlotAllocation_Price(),
				editingDomain) {

			@Override
			protected String renderSetValue(final Object container, final Object setValue) {
				if (setValue instanceof Number) {
					return String.format("$%.2f", ((Number) setValue).doubleValue());
				}
				return super.renderSetValue(container, setValue);
			}
		}), new RowDataEMFPath(false, Type.LOAD_ALLOCATION));
		final GridViewerColumn loadDateColumn = addTradesColumn(loadColumns, "Date", new DateAttributeManipulator(pkg.getSlot_WindowStart(), editingDomain) {
			@Override
			public Comparable<?> getComparable(final Object object) {

				if (object instanceof RowData) {
					final RowData rowData = (RowData) object;
					if (rowData.dischargeSlot != null) {
						return rowData.dischargeSlot.getWindowStart();
					}
				}

				return super.getComparable(object);
			}
		}, new RowDataEMFPath(false, Type.LOAD));
		loadDateColumn.getColumn().setData(EObjectTableViewer.COLUMN_SORT_PATH, new RowDataEMFPath(false, Type.LOAD_OR_DISCHARGE));

		final GridViewerColumn wiringColumn = addWiringColumn();

		final GridViewerColumn dischargeDateColumn = addTradesColumn(dischargeColumns, "Date", new DateAttributeManipulator(pkg.getSlot_WindowStart(), editingDomain) {
			@Override
			public Comparable<?> getComparable(final Object object) {
				if (object instanceof RowData) {
					final RowData rowData = (RowData) object;
					if (rowData.loadSlot != null) {
						return rowData.loadSlot.getWindowStart();
					}
				}
				return super.getComparable(object);
			}
		}, new RowDataEMFPath(false, Type.DISCHARGE));
		dischargeDateColumn.getColumn().setData(EObjectTableViewer.COLUMN_SORT_PATH, new RowDataEMFPath(false, Type.DISCHARGE_OR_LOAD));

		addTradesColumn(dischargeColumns, "Sell At", new ContractManipulator(provider, editingDomain), new RowDataEMFPath(false, Type.DISCHARGE));
		addTradesColumn(dischargeColumns, "Price", new ReadOnlyManipulatorWrapper<BasicAttributeManipulator>(new BasicAttributeManipulator(SchedulePackage.eINSTANCE.getSlotAllocation_Price(),
				editingDomain) {

			@Override
			protected String renderSetValue(final Object container, final Object setValue) {
				if (setValue instanceof Number) {
					return String.format("$%.2f", ((Number) setValue).doubleValue());
				}
				return super.renderSetValue(container, setValue);
			}
		}), new RowDataEMFPath(false, Type.DISCHARGE_ALLOCATION));

		addTradesColumn(dischargeColumns, "Port", new SingleReferenceManipulator(pkg.getSlot_Port(), provider, editingDomain), new RowDataEMFPath(false, Type.DISCHARGE));
		addTradesColumn(dischargeColumns, "D-ID", new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getNamedObject_Name(), editingDomain), new RowDataEMFPath(false, Type.DISCHARGE));
		{
			final AssignmentManipulator assignmentManipulator = new AssignmentManipulator(scenarioEditingLocation);
			final RowDataEMFPath assignmentPath = new RowDataEMFPath(true, Type.CARGO);
			final GridViewerColumn assignmentColumn = addTradesColumn("Assignment", assignmentManipulator, assignmentPath);
			assignmentColumn.setLabelProvider(new EObjectTableViewerColumnProvider(getScenarioViewer(), assignmentManipulator, assignmentPath) {
				@Override
				public Image getImage(final Object element) {

					if (element instanceof RowData) {
						final RowData rowDataItem = (RowData) element;
						if (rowDataItem.elementAssignment != null) {
							if (rowDataItem.elementAssignment != null && rowDataItem.elementAssignment.isLocked()) {
								return lockedImage;
							}
						}
					}

					return super.getImage(element);

				}
			});
		}

		// Trigger sorting on the load date column to make this the initial sort column.
		{
			final Listener[] listeners = loadDateColumn.getColumn().getListeners(SWT.Selection);
			for (final Listener l : listeners) {
				final org.eclipse.swt.widgets.Event e = new org.eclipse.swt.widgets.Event();
				e.type = SWT.Selection;
				e.widget = loadDateColumn.getColumn();
				l.handleEvent(e);
			}
		}

		addPNLColumn("P&L", new BasicAttributeManipulator(SchedulePackage.eINSTANCE.getProfitAndLossContainer_GroupProfitAndLoss(), editingDomain), new RowDataEMFPath(true,
				Type.CARGO_OR_MARKET_ALLOCATION));
		wiringDiagram = new TradesWiringDiagram(getScenarioViewer().getGrid()) {

			@Override
			protected void wiringChanged(final Map<RowData, RowData> newWiring, final boolean ctrlPressed) {
				if (locked) {
					return;
				}
				doWiringChanged(newWiring, ctrlPressed);
			}

			@Override
			protected List<Float> getTerminalPositions(final RootData rootData) {

				// Determine the mid-point in each row and generate an ordered list of heights.

				// +1 to to make loop simpler
				final int heights[] = new int[rootData.getRows().size() + 1];
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
				boolean foundColumn = false;
				for (final GridColumn gc : getScenarioViewer().getGrid().getColumns()) {
					++wiringColumnIndexTmp;
					if (gc == wiringColumn.getColumn()) {
						foundColumn = true;
						break;
					}
				}
				if (!foundColumn) {
					return null;
				}
				final int wiringColumnIndex = wiringColumnIndexTmp;

				int offset = 0;
				offset += getScenarioViewer().getGrid().getRowHeaderWidth();
				// TODO: Get col number
				foundColumn = false;
				final int[] columnOrder = getScenarioViewer().getGrid().getColumnOrder();
				for (int ii = getScenarioViewer().getGrid().getHorizontalBar().getSelection(); ii < columnOrder.length; ++ii) {
					final int idx = columnOrder[ii];
					if (idx == wiringColumnIndex) {
						foundColumn = true;
						break;
					}
					offset += getScenarioViewer().getGrid().getColumn(idx).getWidth();
				}
				if (!foundColumn) {
					return null;
				}

				// TODO: Take into account h scroll final int colWidth = getScenarioViewer().getGrid().getColumn(wiringColumnIndex).getWidth();
				final Rectangle r = new Rectangle(area.x + offset, area.y + getScenarioViewer().getGrid().getHeaderHeight(), wiringColumn.getColumn().getWidth(), area.height);

				return r;
			}
		};
		wiringDiagram.setSortOrder(rootData, sortedIndices, reverseSortedIndices);

		// Hook in a listener to notify mouse events
		final WiringDiagramMouseListener listener = new WiringDiagramMouseListener();
		getScenarioViewer().getGrid().addMouseMoveListener(listener);
		getScenarioViewer().getGrid().addMouseListener(listener);
	}

	private <T extends ICellManipulator & ICellRenderer> GridViewerColumn addPNLColumn(final String columnName, final T manipulator, final EMFPath path) {

		final ReadOnlyManipulatorWrapper<T> wrapper = new ReadOnlyManipulatorWrapper<T>(manipulator) {
			@Override
			public Comparable<?> getComparable(final Object element) {
				final Object object = path.get((EObject) element);
				if (object instanceof ProfitAndLossContainer) {
					final ProfitAndLossContainer container = (ProfitAndLossContainer) object;
					final GroupProfitAndLoss data = container.getGroupProfitAndLoss();
					if (data != null) {
						return data.getProfitAndLoss();
					}
				}
				return super.getComparable(element);
			}
		};

		final GridViewerColumn col = addTradesColumn(columnName, wrapper, path);
		col.setLabelProvider(new EObjectTableViewerColumnProvider(getScenarioViewer(), manipulator, path) {

			@Override
			public String getText(final Object element) {

				final Object object = path.get((EObject) element);
				if (object instanceof ProfitAndLossContainer) {
					final ProfitAndLossContainer container = (ProfitAndLossContainer) object;
					final GroupProfitAndLoss data = container.getGroupProfitAndLoss();
					if (data != null) {
						return String.format("%.2fm", ((float) data.getProfitAndLoss()) / 1000000);
					}
				}
				return super.getText(element);
			}

		});
		return col;
	}

	/**
	 * Set the cargoes, and reset the wiring to match these cargoes.
	 * 
	 * @param newCargoes
	 * @since 5.0
	 */
	public RootData setCargoes(final AssignmentModel assignmentModel, final CargoModel cargoModel, final ScheduleModel scheduleModel, final RootData existingData) {
		final CargoModelRowTransformer transformer = new CargoModelRowTransformer();
		return transformer.transform(assignmentModel, cargoModel, scheduleModel, getScenarioViewer().getValidationSupport().getValidationErrors(), existingData);
	}

	public void init(final AdapterFactory adapterFactory, final CommandStack commandStack) {
		getScenarioViewer().init(adapterFactory, commandStack);

	}

	@Override
	protected void enableOpenListener() {
		scenarioViewer.addOpenListener(new IOpenListener() {

			@Override
			public void open(final OpenEvent event) {
				if (scenarioViewer.getSelection() instanceof IStructuredSelection) {
					final IStructuredSelection structuredSelection = (IStructuredSelection) scenarioViewer.getSelection();
					if (structuredSelection.isEmpty() == false) {

						boolean mixedContent = false;
						final List<EObject> editorTargets = new ArrayList<EObject>();
						final Iterator<?> itr = structuredSelection.iterator();
						EClass firstType = null;
						while (itr.hasNext()) {
							final Object obj = itr.next();
							EObject target = null;
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
							if (target != null) {
								editorTargets.add(target);
							}
							if (editorTargets.size() == 1) {
								firstType = target.eClass();
							} else {
								if (firstType != target.eClass()) {
									mixedContent = true;
								}
							}
						}
						if (!editorTargets.isEmpty() && scenarioViewer.isLocked() == false) {
							final ScenarioLock editorLock = scenarioEditingLocation.getEditorLock();
							try {
								editorLock.claim();
								scenarioEditingLocation.setDisableUpdates(true);
								if (editorTargets.size() > 1) {
									if (!mixedContent) {
										final MultiDetailDialog mdd = new MultiDetailDialog(event.getViewer().getControl().getShell(), scenarioEditingLocation.getRootObject(), scenarioEditingLocation
												.getDefaultCommandHandler());
										mdd.open(scenarioEditingLocation, editorTargets);
									} else {
										// Currently unable to edit mixed content!
									}
								} else {
									final DetailCompositeDialog dcd = new DetailCompositeDialog(event.getViewer().getControl().getShell(), scenarioEditingLocation.getDefaultCommandHandler(), ~SWT.MAX) {
										@Override
										protected void configureShell(final Shell newShell) {
											newShell.setMinimumSize(SWT.DEFAULT, 630);
											super.configureShell(newShell);
										}
									};
									dcd.open(scenarioEditingLocation, scenarioEditingLocation.getRootObject(), editorTargets, scenarioViewer.isLocked());
								}
							} finally {
								scenarioEditingLocation.setDisableUpdates(false);
								editorLock.release();
							}
						}
					}
				}
			}

		});
	}

	/**
	 * @since 4.0
	 */
	protected void doWiringChanged(final Map<RowData, RowData> newWiring, final boolean ctrlPressed) {

		final List<Command> setCommands = new LinkedList<Command>();
		final List<Command> deleteCommands = new LinkedList<Command>();

		final CargoModel cargoModel = getPortfolioModel().getCargoModel();
		final AssignmentModel assignmentModel = getPortfolioModel().getAssignmentModel();

		final Set<Slot> slotsToRemove = new HashSet<Slot>();
		final Set<Slot> slotsToKeep = new HashSet<Slot>();
		final Set<Cargo> cargoesToRemove = new HashSet<Cargo>();
		final Set<Cargo> cargoesToKeep = new HashSet<Cargo>();
		for (final Map.Entry<RowData, RowData> e : newWiring.entrySet()) {
			final RowData loadSide = e.getKey();
			final RowData dischargeSide = e.getValue();

			{
				// Address new A -> B wiring
				if (dischargeSide != null && dischargeSide.dischargeSlot != null) {
					final Cargo c;
					if (loadSide.cargo == null) {
						// New Cargo
						c = cec.createNewCargo(setCommands, cargoModel);
						c.setName(loadSide.loadSlot.getName());
						setCommands.add(SetCommand.create(scenarioEditingLocation.getEditingDomain(), loadSide.loadSlot, CargoPackage.eINSTANCE.getSlot_Cargo(), c));
					} else {
						c = loadSide.cargo;

						if (!ctrlPressed) {
							// Break the existing wiring
							for (final Slot s : c.getSlots()) {
								if (s != loadSide.loadSlot) {
									setCommands.add(SetCommand.create(scenarioEditingLocation.getEditingDomain(), s, CargoPackage.eINSTANCE.getSlot_Cargo(), null));

									// Optional market slots can be removed.
									if (s instanceof SpotSlot && s.isOptional()) {
										slotsToRemove.add(s);
									}

								}
							}
						}
					}

					cargoesToKeep.add(c);
					slotsToKeep.add(loadSide.loadSlot);

					setCommands.add(SetCommand.create(scenarioEditingLocation.getEditingDomain(), dischargeSide.dischargeSlot, CargoPackage.eINSTANCE.getSlot_Cargo(), c));

					cec.appendFOBDESCommands(setCommands, deleteCommands, scenarioEditingLocation.getEditingDomain(), assignmentModel, c, loadSide.loadSlot, dischargeSide.getDischargeSlot());

					{
						Cargo dischargeCargo = null;
						if (dischargeSide != null && dischargeSide.dischargeSlot != null) {
							dischargeCargo = dischargeSide.dischargeSlot.getCargo();
						}
						if (dischargeCargo != null) {
							if (dischargeCargo.getSlots().size() <= 2) {
								for (final Slot s : dischargeCargo.getSlots()) {
									if (s != dischargeSide.dischargeSlot) {
										setCommands.add(SetCommand.create(scenarioEditingLocation.getEditingDomain(), s, CargoPackage.eINSTANCE.getSlot_Cargo(), null));

										// Optional market slots can be removed.
										if (s instanceof SpotSlot && s.isOptional()) {
											slotsToRemove.add(s);
										}

									}
								}
								cargoesToRemove.add(dischargeCargo);
							}
						}
					}
				} else {
					// Broken the wiring
					if (loadSide.cargo != null) {
						cargoesToRemove.add(loadSide.cargo);
					}
				}
			}
		}

		cargoesToRemove.removeAll(cargoesToKeep);
		for (final Cargo cargo : cargoesToRemove) {
			deleteCommands.add(DeleteCommand.create(scenarioEditingLocation.getEditingDomain(), cargo));
		}
		slotsToRemove.removeAll(slotsToKeep);
		for (final Slot slot : slotsToRemove) {
			deleteCommands.add(DeleteCommand.create(scenarioEditingLocation.getEditingDomain(), slot));
		}

		final CompoundCommand currentWiringCommand = new CompoundCommand("Rewire Cargoes");
		// Process set before delete
		for (final Command c : setCommands) {
			currentWiringCommand.append(c);
		}
		for (final Command c : deleteCommands) {
			currentWiringCommand.append(c);
		}

		executeCurrentWiringCommand(currentWiringCommand);
	}

	/**
	 * @since 4.0
	 */
	protected LNGPortfolioModel getPortfolioModel() {
		return ((LNGScenarioModel) scenarioEditingLocation.getRootObject()).getPortfolioModel();
	}

	private void executeCurrentWiringCommand(final CompoundCommand currentWiringCommand) {
		// Delete commands can be slow, so show the busy indicator while deleting.
		if (currentWiringCommand.isEmpty()) {
			return;
		}
		final Runnable runnable = new Runnable() {

			@Override
			public void run() {
				scenarioEditingLocation.getEditingDomain().getCommandStack().execute(currentWiringCommand);
			}
		};
		BusyIndicator.showWhile(null, runnable);
	}

	/**
	 * Sub-classes should override to handle mouse drag notifications
	 * 
	 * @param newXPos
	 * @param newYPos
	 */
	protected void requestScrollTo(final int newXPos, final int newYPos) {
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
			final CargoModel cargoModel = getPortfolioModel().getCargoModel();

			RowData discoveredRowData = null;
			final ISelection selection = getScenarioViewer().getSelection();
			if (selection instanceof IStructuredSelection) {
				final Object firstElement = ((IStructuredSelection) selection).getFirstElement();
				if (firstElement instanceof RowData) {
					discoveredRowData = (RowData) firstElement;
				}
			}
			final RowData referenceRowData = discoveredRowData;
			{
				final Action newLoad = new Action("Cargo") {
					public void run() {

						final CompoundCommand cmd = new CompoundCommand("Cargo");

						final Cargo newCargo = cec.createObject(CargoPackage.eINSTANCE.getCargo(), CargoPackage.eINSTANCE.getCargoModel_Cargoes(), cargoModel);
						for (final Slot slot : newCargo.getSlots()) {
							if (slot instanceof LoadSlot) {
								final LoadSlot newLoad = (LoadSlot) slot;
								initialiseSlot(newLoad, true, referenceRowData);
								newLoad.setDESPurchase(false);
								cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), cargoModel, CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), newLoad));
							} else if (slot instanceof DischargeSlot) {
								final DischargeSlot newDischarge = (DischargeSlot) slot;
								initialiseSlot(newDischarge, false, referenceRowData);
								newDischarge.setFOBSale(false);
								cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), cargoModel, CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(), newDischarge));
							} else {
								throw new IllegalStateException("Unexpected slot type");
							}
						}
						newCargo.setAllowRewiring(true);
						newCargo.setName("");

						cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), cargoModel, CargoPackage.eINSTANCE.getCargoModel_Cargoes(), newCargo));

						scenarioEditingLocation.getEditingDomain().getCommandStack().execute(cmd);
					}
				};
				addActionToMenu(newLoad, menu);
			}
			{
				final Action newLoad = new Action("FOB purchase") {
					public void run() {

						final CompoundCommand cmd = new CompoundCommand("FOB purchase");

						final LoadSlot newLoad = cec.createObject(CargoPackage.eINSTANCE.getLoadSlot(), CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), cargoModel);
						newLoad.setDESPurchase(false);
						initialiseSlot(newLoad, true, referenceRowData);
						cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), cargoModel, CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), newLoad));
						scenarioEditingLocation.getEditingDomain().getCommandStack().execute(cmd);
					}
				};
				addActionToMenu(newLoad, menu);
			}
			{
				final Action newDESPurchase = new Action("DES purchase") {
					public void run() {

						final CompoundCommand cmd = new CompoundCommand("DES purchase");

						final LoadSlot newLoad = cec.createObject(CargoPackage.eINSTANCE.getLoadSlot(), CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), cargoModel);
						newLoad.setDESPurchase(true);
						initialiseSlot(newLoad, true, referenceRowData);
						cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), cargoModel, CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), newLoad));
						scenarioEditingLocation.getEditingDomain().getCommandStack().execute(cmd);
					}
				};
				addActionToMenu(newDESPurchase, menu);
			}
			{
				final Action newDischarge = new Action("DES sale") {
					public void run() {

						final CompoundCommand cmd = new CompoundCommand("DES sale");

						final DischargeSlot newDischarge = cec.createObject(CargoPackage.eINSTANCE.getDischargeSlot(), CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(), cargoModel);
						newDischarge.setFOBSale(false);
						initialiseSlot(newDischarge, false, referenceRowData);
						cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), cargoModel, CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(), newDischarge));
						scenarioEditingLocation.getEditingDomain().getCommandStack().execute(cmd);
					}
				};

				addActionToMenu(newDischarge, menu);
			}
			{
				final Action newFOBSale = new Action("FOB Sale") {
					public void run() {

						final CompoundCommand cmd = new CompoundCommand("FOB Sale");

						final DischargeSlot newDischarge = cec.createObject(CargoPackage.eINSTANCE.getDischargeSlot(), CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(), cargoModel);
						newDischarge.setFOBSale(true);
						initialiseSlot(newDischarge, false, referenceRowData);
						cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), cargoModel, CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(), newDischarge));
						scenarioEditingLocation.getEditingDomain().getCommandStack().execute(cmd);
					}
				};
				addActionToMenu(newFOBSale, menu);
			}

			{
				final ComplexCargoAction newComplexCargo = new ComplexCargoAction("Complex Cargo");
				addActionToMenu(newComplexCargo, menu);
			}
		}

		private final void initialiseSlot(final Slot newSlot, final boolean isLoad, final RowData referenceRowData) {
			newSlot.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), EcoreUtil.generateUUID());
			newSlot.setOptional(false);
			newSlot.setName("");
			// Set window so that via default sorting inserts new slot at current table position
			if (referenceRowData != null) {
				final Slot primarySortSlot = isLoad ? referenceRowData.loadSlot : referenceRowData.dischargeSlot;
				final Slot secondarySortSlot = isLoad ? referenceRowData.dischargeSlot : referenceRowData.loadSlot;
				if (primarySortSlot != null) {
					newSlot.setWindowStart(primarySortSlot.getWindowStart());
					newSlot.setPort(primarySortSlot.getPort());
				} else if (secondarySortSlot != null) {
					newSlot.setWindowStart(secondarySortSlot.getWindowStart());
				}
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

	private final class ComplexCargoAction extends Action {

		private ComplexCargoAction(final String text) {
			super(text);
		}

		@Override
		public void run() {

			final ScenarioLock editorLock = scenarioEditingLocation.getEditorLock();
			try {
				editorLock.claim();
				scenarioEditingLocation.setDisableUpdates(true);

				final ComplexCargoEditor editor = new ComplexCargoEditor(getScenarioViewer().getGrid().getShell(), scenarioEditingLocation);
				// editor.setBlockOnOpen(true);

				final Cargo cargo = CargoFactory.eINSTANCE.createCargo();

				final LoadSlot load = CargoFactory.eINSTANCE.createLoadSlot();
				final DischargeSlot discharge1 = CargoFactory.eINSTANCE.createDischargeSlot();
				final DischargeSlot discharge2 = CargoFactory.eINSTANCE.createDischargeSlot();

				cargo.getSlots().addAll(Lists.newArrayList(load, discharge1, discharge2));
				final int ret = editor.open(cargo);
				final CommandStack commandStack = scenarioEditingLocation.getEditingDomain().getCommandStack();
				if (ret == Window.OK) {

					final CargoModel cargomodel = getPortfolioModel().getCargoModel();

					final CompoundCommand cmd = new CompoundCommand("New LDD Cargo");
					cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), cargomodel, CargoPackage.eINSTANCE.getCargoModel_Cargoes(), Collections.singleton(cargo)));
					for (final Slot s : cargo.getSlots()) {

						if (s.eContainer() == null) {

							if (s instanceof LoadSlot) {
								cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), cargomodel, CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), Collections.singleton(s)));
							} else {
								cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), cargomodel, CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(), Collections.singleton(s)));
							}
						}
					}

					commandStack.execute(cmd);
				} else {
					final Iterator<Command> itr = new LinkedList<Command>(editor.getExecutedCommands()).descendingIterator();
					while (itr.hasNext()) {
						final Command cmd = itr.next();
						if (commandStack.getUndoCommand() == cmd) {
							commandStack.undo();
						} else {
							throw new IllegalStateException("Unable to cancel edit - command stack history is corrupt");
						}
					}

				}
			} finally {
				scenarioEditingLocation.setDisableUpdates(false);
				editorLock.release();
			}
		}
	}

	/**
	 * A combined {@link MouseListener} and {@link MouseMoveListener} to scroll the table during wiring operations.
	 * 
	 */
	private class WiringDiagramMouseListener implements MouseListener, MouseMoveListener {

		private boolean dragging = false;

		@Override
		public void mouseMove(final MouseEvent e) {
			if (dragging) {
				final Grid grid = getScenarioViewer().getGrid();

				// Get table area
				Rectangle bounds = getScenarioViewer().getGrid().getClientArea();
				// Clip for column headers
				final int headerheight = grid.getHeaderVisible() ? grid.getHeaderHeight() : 0;
				bounds = new Rectangle(bounds.x, bounds.y + headerheight, bounds.width, bounds.height - headerheight);

				GridItem item = null;
				// X/Y pos to use to find current selection to scroll from
				int x = e.x;
				int y = e.y;

				// Is the mouse out-side of the table area?
				if (!bounds.contains(e.x, e.y)) {
					int vScroll = 0;
					int hScroll = 0;

					// Determine where the cursor is and move the x/y to inside the table
					if (e.x < bounds.x) {
						hScroll = -1;
						x = bounds.x + 1;
					} else if (e.x > bounds.x + bounds.width) {
						hScroll = 1;
						x = bounds.x + bounds.width - 1;
					}

					if (e.y < bounds.y) {
						vScroll = -1;
						y = bounds.y + 1;
					} else if (e.y > bounds.y + bounds.height) {
						vScroll = 1;
						y = bounds.y + bounds.height - 1;
					}

					// Get the current item!
					item = grid.getItem(new Point(x, y));

					// Check for h scroll
					if (hScroll != 0) {
						final ScrollBar horizontalBar = grid.getHorizontalBar();
						final int selection = horizontalBar.getSelection();
						horizontalBar.setSelection(selection + hScroll);
					}

					// V Scroll using the showItem API
					if (item != null) {
						GridItem item2 = null;
						if (vScroll > 0) {
							item2 = getScenarioViewer().getGrid().getNextVisibleItem(item);
						} else {
							item2 = getScenarioViewer().getGrid().getPreviousVisibleItem(item);

						}
						if (item2 != null) {
							if (vScroll != 0) {
								// Almost! it will show part of the item, but it may be obscured by the h.scroll bar
								getScenarioViewer().getGrid().showItem(item2);
							}
						}
						getScenarioViewer().refresh();
					}
				}
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

	private class CreateDuplicateMenuAction extends Action implements IMenuCreator {

		private Menu lastMenu;

		public CreateDuplicateMenuAction(final String label) {
			super(label, IAction.AS_DROP_DOWN_MENU);
			setImageDescriptor(LngUIActivator.getDefault().getImageRegistry().getDescriptor(ImageConstants.IMAGE_DUPLICATE));
			setDisabledImageDescriptor(LngUIActivator.getDefault().getImageRegistry().getDescriptor(ImageConstants.IMAGE_DUPLICATE_DISABLED));
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

			final DuplicateAction result = new DuplicateAction(getJointModelEditorPart());
			// TODO: Translate into real objects, not just row object!
			result.selectionChanged(new SelectionChangedEvent(scenarioViewer, scenarioViewer.getSelection()));
			addActionToMenu(result, menu);

			for (final CreateStripDialog.StripType stripType : CreateStripDialog.StripType.values()) {
				final Action stripAction = new CreateStripAction(stripType.toString(), stripType);
				addActionToMenu(stripAction, menu);
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

	private class CreateStripAction extends Action {

		private final CreateStripDialog.StripType stripType;

		protected CreateStripAction(final String text, final StripType stripType) {
			super(text);
			this.stripType = stripType;
		}

		@Override
		public void run() {

			final ScenarioLock editorLock = scenarioEditingLocation.getEditorLock();
			try {
				editorLock.claim();
				scenarioEditingLocation.setDisableUpdates(true);

				final MMXRootObject rootObject = scenarioEditingLocation.getRootObject();
				if (rootObject instanceof LNGScenarioModel) {

					Slot selectedObject = null;
					final ISelection selection = TradesWiringViewer.this.getScenarioViewer().getSelection();
					if (selection instanceof IStructuredSelection) {
						final IStructuredSelection ss = (IStructuredSelection) selection;

						final Iterator<?> itr = ss.iterator();
						while (itr.hasNext()) {
							Object o = itr.next();

							if (o instanceof RowData) {
								final RowData rowData = (RowData) o;
								if (stripType == StripType.TYPE_FOB_PURCHASE_SLOT || stripType == StripType.TYPE_DES_PURCHASE_SLOT) {
									o = rowData.loadSlot;
								} else if (stripType == StripType.TYPE_FOB_SALE_SLOT || stripType == StripType.TYPE_DES_SALE_SLOT) {
									o = rowData.dischargeSlot;
								}
							}

							if (o instanceof Slot) {
								selectedObject = (Slot) o;
								break;
							}

						}
					}

					final LNGScenarioModel scenarioModel = (LNGScenarioModel) rootObject;
					final CreateStripDialog d = new CreateStripDialog(scenarioEditingLocation.getShell(), scenarioEditingLocation, stripType, selectedObject);
					if (Window.OK == d.open()) {
						final Command cmd = d.createStrip(scenarioModel.getPortfolioModel().getCargoModel(), getEditingDomain());
						if (cmd.canExecute()) {
							getEditingDomain().getCommandStack().execute(cmd);
						}
					}

				} else {
					setEnabled(false);
				}

			} finally {
				scenarioEditingLocation.setDisableUpdates(false);
				editorLock.release();
			}
		}
	}

	/**
	 * Return an action which duplicates the selection
	 * 
	 * @return
	 */
	protected Action createDuplicateAction() {
		return new CreateDuplicateMenuAction("Duplicate");
	}
}
