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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.menus.IMenuService;

import com.google.common.collect.Lists;
import com.mmxlabs.common.Equality;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.editor.editors.ldd.LDDEditor;
import com.mmxlabs.models.lng.cargo.presentation.CargoEditorPlugin;
import com.mmxlabs.models.lng.cargo.ui.editorpart.CargoModelRowTransformer.GroupData;
import com.mmxlabs.models.lng.cargo.ui.editorpart.CargoModelRowTransformer.RootData;
import com.mmxlabs.models.lng.cargo.ui.editorpart.CargoModelRowTransformer.RowData;
import com.mmxlabs.models.lng.cargo.ui.editorpart.CargoModelRowTransformer.RowDataEMFPath;
import com.mmxlabs.models.lng.cargo.ui.editorpart.CargoModelRowTransformer.Type;
import com.mmxlabs.models.lng.input.InputModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewer;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.dates.DateAttributeManipulator;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialog;
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

	private TradesWiringDiagram wiringDiagram;

	protected RootData rootData;

	private final Set<GridColumn> loadColumns = new HashSet<GridColumn>();
	private final Set<GridColumn> dischargeColumns = new HashSet<GridColumn>();

	private Object[] sortedChildren;
	private int[] sortedIndices;

	protected int[] reverseSortedIndices;

	private boolean locked;

	private final CargoEditingCommands cec;
	private final CargoEditorMenuHelper menuHelper;

	private final Image wiredImage;
	private final Image lockedImage;

	private final Object updateLock = new Object();

	private IStatusChangedListener statusChangedListener;

	public TradesWiringViewer(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation scenarioEditingLocation, final IActionBars actionBars) {
		super(page, part, scenarioEditingLocation, actionBars);
		this.cec = new CargoEditingCommands(scenarioEditingLocation);
		this.menuHelper = new CargoEditorMenuHelper(part.getSite().getShell(), scenarioEditingLocation);
		wiredImage = CargoEditorPlugin.getPlugin().getImage(CargoEditorPlugin.IMAGE_CARGO_LINK);
		lockedImage = CargoEditorPlugin.getPlugin().getImage(CargoEditorPlugin.IMAGE_CARGO_LOCK);
	}

	@Override
	public void dispose() {

		this.rootData = null;

		super.dispose();
	}

	protected ScenarioTableViewer constructViewer(final Composite parent) {

		final ScenarioTableViewer scenarioViewer = new ScenarioTableViewer(parent, SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL, scenarioEditingLocation) {

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

						final CargoModel cargoModel = scenarioEditingLocation.getRootObject().getSubModel(CargoModel.class);
						final InputModel inputModel = scenarioEditingLocation.getRootObject().getSubModel(InputModel.class);
						final ScheduleModel scheduleModel = scenarioEditingLocation.getRootObject().getSubModel(ScheduleModel.class);

						final RootData root = setCargoes(inputModel, cargoModel, scheduleModel);

						TradesWiringViewer.this.rootData = root;

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

						int comparison = 0;
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
							if (g1 == null || g1.getRows().isEmpty()) {
								comparison = -1;
							} else if (g2 == null || g2.getRows().isEmpty()) {
								comparison = 1;
							} else {
								return vc.compare(viewer, g1.getRows().get(0), g2.getRows().get(0));
							}
						}
						return getScenarioViewer().getSortingSupport().isSortDescending() ? -comparison : comparison;
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

					// TODO: Simple load/discharge filter. Really need to determine when we build the columns and save into a set somewhere
					if (loadColumns.contains(column) && rowDataItem.loadSlot != null) {

						final IMenuListener listener = menuHelper.createLoadSlotMenuListener(rootData.getLoadSlots(), idx);
						listener.menuAboutToShow(mgr);
					}
					if (dischargeColumns.contains(column) && rowDataItem.dischargeSlot != null) {

						final IMenuListener listener = menuHelper.createDischargeSlotMenuListener(rootData.getDischargeSlots(), idx);
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
					for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
						aSet.add(slotAllocation.getSlot());
					}
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

	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final CommandStack commandStack) {
		getScenarioViewer().init(adapterFactory, commandStack, new EReference[0]);

		final IStatusProvider statusProvider = scenarioEditingLocation.getStatusProvider();
		getScenarioViewer().setStatusProvider(statusProvider);

		if (statusProvider != null) {
			statusChangedListener = new IStatusChangedListener() {

				@Override
				public void onStatusChanged(final IStatusProvider provider, final IStatus status) {
					// TODO Auto-generated method stub
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

		final Action lddAction = new LDDAction("New LDD");
		lddAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ADD));
		toolbar.appendToGroup(ADD_REMOVE_GROUP, lddAction);

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
		final IReferenceValueProviderProvider provider = scenarioEditingLocation.getReferenceValueProviderCache();
		final EditingDomain editingDomain = scenarioEditingLocation.getEditingDomain();
		//
		// final GridViewerColumn state = getScenarioViewer().addSimpleColumn("", false);
		// state.setLabelProvider(new EObjectTableViewerColumnProvider(getScenarioViewer(), null, null) {
		//
		// @Override
		// public String getText(final Object element) {
		// return null;
		// }
		//
		// @Override
		// public Image getImage(final Object element) {
		//
		// if (element instanceof RowData) {
		// final RowData rowDataItem = (RowData) element;
		// if (rowDataItem.cargo != null) {
		// final Cargo cargo = rowDataItem.cargo;
		// final InputModel inputModel = scenarioEditingLocation.getRootObject().getSubModel(InputModel.class);
		// final ElementAssignment assignment = AssignmentEditorHelper.getElementAssignment(inputModel, cargo);
		// if (assignment != null && assignment.isLocked()) {
		// return lockedImage;
		// } else if (!cargo.isAllowRewiring()) {
		// return wiredImage;
		//
		// }
		// }
		// }
		//
		// return super.getImage(element);
		// }
		// });

		addTradesColumn(loadColumns, "L-ID", new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getNamedObject_Name(), editingDomain), new RowDataEMFPath(Type.LOAD, true));
		addTradesColumn(loadColumns, "Port", new SingleReferenceManipulator(pkg.getSlot_Port(), provider, editingDomain), new RowDataEMFPath(Type.LOAD, true));
		addTradesColumn(loadColumns, "Buy At", new ContractManipulator(provider, editingDomain), new RowDataEMFPath(Type.LOAD, true));
		addTradesColumn(loadColumns, "Buy Price",
				new ReadOnlyManipulatorWrapper<BasicAttributeManipulator>(new BasicAttributeManipulator(SchedulePackage.eINSTANCE.getSlotAllocation_Price(), editingDomain)), new RowDataEMFPath(
						Type.LOAD_ALLOCATION, true));
		addTradesColumn(loadColumns, "Date", new DateAttributeManipulator(pkg.getSlot_WindowStart(), editingDomain), new RowDataEMFPath(Type.LOAD, true));

		final GridViewerColumn wiringColumn = addWiringColumn();

		addTradesColumn(dischargeColumns, "Date", new DateAttributeManipulator(pkg.getSlot_WindowStart(), editingDomain), new RowDataEMFPath(Type.DISCHARGE, true));
		addTradesColumn(dischargeColumns, "Sell At", new ContractManipulator(provider, editingDomain), new RowDataEMFPath(Type.DISCHARGE, true));
		addTradesColumn(loadColumns, "Sell Price",
				new ReadOnlyManipulatorWrapper<BasicAttributeManipulator>(new BasicAttributeManipulator(SchedulePackage.eINSTANCE.getSlotAllocation_Price(), editingDomain)), new RowDataEMFPath(
						Type.DISCHARGE_ALLOCATION, true));

		addTradesColumn(dischargeColumns, "Port", new SingleReferenceManipulator(pkg.getSlot_Port(), provider, editingDomain), new RowDataEMFPath(Type.DISCHARGE, true));
		addTradesColumn(dischargeColumns, "D-ID", new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getNamedObject_Name(), editingDomain), new RowDataEMFPath(Type.DISCHARGE, true));
		{
			AssignmentManipulator assignmentManipulator = new AssignmentManipulator(scenarioEditingLocation);
			RowDataEMFPath assignmentPath = new RowDataEMFPath(Type.CARGO, true);
			GridViewerColumn assignmentColumn = addTradesColumn("Assignment", assignmentManipulator, assignmentPath);
			assignmentColumn.setLabelProvider(new EObjectTableViewerColumnProvider(getScenarioViewer(), assignmentManipulator, assignmentPath) {
				@Override
				public Image getImage(Object element) {

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
		wiringDiagram = new TradesWiringDiagram(getScenarioViewer().getGrid()) {

			@Override
			protected void wiringChanged(final Map<RowData, RowData> newWiring) {
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
		wiringDiagram.setSortOrder(rootData, sortedIndices, reverseSortedIndices);
	}

	/**
	 * Set the cargoes, and reset the wiring to match these cargoes.
	 * 
	 * @param newCargoes
	 */
	public RootData setCargoes(final InputModel inputModel, final CargoModel cargoModel, ScheduleModel scheduleModel) {
		final CargoModelRowTransformer transformer = new CargoModelRowTransformer();
		return transformer.transform(inputModel, cargoModel, scheduleModel, getScenarioViewer().getValidationSupport().getValidationErrors());
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
							final DetailCompositeDialog dcd = new DetailCompositeDialog(event.getViewer().getControl().getShell(), scenarioEditingLocation.getDefaultCommandHandler());
							try {
								scenarioEditingLocation.getEditorLock().claim();
								scenarioEditingLocation.setDisableUpdates(true);

								dcd.open(scenarioEditingLocation, scenarioEditingLocation.getRootObject(), Collections.singletonList(target), scenarioViewer.isLocked());
							} finally {
								scenarioEditingLocation.setDisableUpdates(false);
								scenarioEditingLocation.getEditorLock().release();
							}
						} else {
							// No support yet.

							// try {
							// scenarioEditingLocation.getEditorLock().claim();
							// if (scenarioViewer.isLocked() == false) {
							// final MultiDetailDialog mdd = new MultiDetailDialog(event.getViewer().getControl().getShell(), scenarioEditingLocation.getRootObject(), scenarioEditingLocation
							// .getDefaultCommandHandler());
							// mdd.open(scenarioEditingLocation, structuredSelection.toList());
							// }
							// } finally {
							// scenarioEditingLocation.getEditorLock().release();
							// }
						}
					}
				}
			}

		});
	}

	// private synchronized void updateWiringColours(final TradesWiringDiagram diagram, final List<List<Integer>> wiring, final RootData root) {
	// final Color red = Display.getDefault().getSystemColor(SWT.COLOR_RED);
	// final Color green = Display.getDefault().getSystemColor(SWT.COLOR_GREEN);
	// final Color black = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
	// final Color gray = Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
	//
	// for (int i = 0; i < root.getRows().size(); ++i) {
	// final RowData rd = root.getRows().get(i);
	// final LoadSlot loadSlot = rd.loadSlot;
	// final DischargeSlot dischargeSlot = rd.dischargeSlot;
	// // if (loadSlot != null && loadSlot.isOptional()) {
	// // diagram.setLeftTerminalColor(i, green);
	// // diagram.setLeftTerminalOptional(i, true);
	// // } else {
	// // diagram.setLeftTerminalColor(i, red);
	// // diagram.setLeftTerminalOptional(i, false);
	// // }
	// // if (dischargeSlot != null && dischargeSlot.isOptional()) {
	// // diagram.setRightTerminalColor(i, green);
	// // diagram.setRightTerminalOptional(i, true);
	// // } else {
	// // diagram.setRightTerminalColor(i, red);
	// // diagram.setRightTerminalOptional(i, false);
	// // }
	// final int j = wiring.get(i);
	// if (j == -1) {
	// continue;
	// }
	//
	// if (loadSlot == null) {
	// continue;
	// }
	// if (dischargeSlot == null) {
	// continue;
	// }
	//
	// diagram.setLeftTerminalColor(i, green);
	// diagram.setRightTerminalColor(j, green);
	//
	// if (isWiringValid(root.getCargoes().get(i), loadSlot, dischargeSlot)) {
	// if (root.getCargoes().get(i).isAllowRewiring()) {
	// diagram.setWireColor(i, black);
	// } else {
	// diagram.setWireColor(i, gray);
	// }
	// } else {
	// diagram.setWireColor(i, red);
	// }
	// }
	// diagram.redraw();
	// }

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

				// getScenarioViewer().getSortingSupport().setFixedSortOrder(Arrays.asList(sortedElements));
			}

			getScenarioViewer().setInput(null);
			getScenarioViewer().setInput(oldInput);
		}
	}

	protected void doWiringChanged(final Map<RowData, RowData> newWiring) {

		final List<Command> setCommands = new LinkedList<Command>();
		final List<Command> deleteCommands = new LinkedList<Command>();

		final CargoModel cargoModel = scenarioEditingLocation.getRootObject().getSubModel(CargoModel.class);
		final InputModel inputModel = scenarioEditingLocation.getRootObject().getSubModel(InputModel.class);

		final Set<Slot> slotsToRemove = new HashSet<Slot>();
		final Set<Slot> slotsToKeep = new HashSet<Slot>();
		final Set<Cargo> cargoesToRemove = new HashSet<Cargo>();
		final Set<Cargo> cargoesToKeep = new HashSet<Cargo>();
		for (final Map.Entry<RowData, RowData> e : newWiring.entrySet()) {
			final RowData loadSide = e.getKey();
			final RowData dischargeSide = e.getValue();

			if (loadSide == null || dischargeSide == null) {
				// Break the wiring
				Cargo c = null;
				if (dischargeSide != null) {
					c = dischargeSide.cargo;
					if (dischargeSide.dischargeSlot != null) {
						setCommands.add(SetCommand.create(scenarioEditingLocation.getEditingDomain(), dischargeSide.dischargeSlot, CargoPackage.eINSTANCE.getSlot_Cargo(), null));
					}
				} else if (loadSide != null) {
					c = loadSide.cargo;
					if (loadSide.loadSlot != null) {
						setCommands.add(SetCommand.create(scenarioEditingLocation.getEditingDomain(), loadSide.loadSlot, CargoPackage.eINSTANCE.getSlot_Cargo(), null));
					}
				}
				if (c != null) {

					for (final Slot s : c.getSlots()) {

						// Optional market slots can be removed.
						if (s instanceof DischargeSlot) {
							final DischargeSlot oldSlot = (DischargeSlot) s;
							if (oldSlot instanceof SpotSlot && oldSlot.isOptional()) {
								slotsToRemove.add(oldSlot);
							}
						}

						if (s instanceof LoadSlot) {
							final LoadSlot oldSlot = (LoadSlot) s;
							if (oldSlot instanceof SpotSlot && oldSlot.isOptional()) {
								slotsToRemove.add(oldSlot);
							}
						}
					}
					cargoesToRemove.add(c);
				}
			} else {
				// New wiring!
				Cargo c = loadSide.cargo;
				slotsToKeep.add(loadSide.loadSlot);
				slotsToKeep.add(loadSide.dischargeSlot);
				if (c == null) {
					// create a new cargo
					c = cec.createNewCargo(setCommands, cargoModel);
					c.setName(loadSide.loadSlot.getName());

					setCommands.add(SetCommand.create(scenarioEditingLocation.getEditingDomain(), loadSide.loadSlot, CargoPackage.eINSTANCE.getSlot_Cargo(), c));
					setCommands.add(SetCommand.create(scenarioEditingLocation.getEditingDomain(), dischargeSide.dischargeSlot, CargoPackage.eINSTANCE.getSlot_Cargo(), c));
				} else {
					if (!c.getSlots().contains(dischargeSide.getDischargeSlot())) {
						setCommands.add(SetCommand.create(scenarioEditingLocation.getEditingDomain(), dischargeSide.getDischargeSlot(), CargoPackage.eINSTANCE.getSlot_Cargo(), c));

						// Optional market slots can be removed.
						for (final Slot s : c.getSlots()) {
							if (s instanceof DischargeSlot) {
								final DischargeSlot oldSlot = (DischargeSlot) s;
								if (oldSlot instanceof SpotSlot && oldSlot.isOptional()) {
									slotsToRemove.add(oldSlot);
								}
							}
						}
					}

				}
				cargoesToKeep.add(c);
				cec.appendFOBDESCommands(setCommands, deleteCommands, scenarioEditingLocation.getEditingDomain(), inputModel, c, loadSide.loadSlot, dischargeSide.getDischargeSlot());
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
		//
		// for (int i = 0; i < rootData.getRows().size(); ++i) {
		// // if (wiring.get(i).equals(newWiring.get(i))) {
		// // // No change
		// // continue;
		// // }
		//
		// final RowData rowI = rootData.getRows().get(i);
		//
		// final Integer newIndex = newWiring.get(i);
		// final LoadSlot loadSlot = rowI.loadSlot;
		// if (loadSlot != null) {
		// if (newIndex >= 0 && newIndex < newWiring.size()) {
		// final DischargeSlot otherDischarge = rootData.getRows().get(newIndex).dischargeSlot;
		// if (otherDischarge != null) {
		// Cargo c = rowI.cargo;
		// if (c != null) {
		// if (!c.getSlots().contains(otherDischarge)) {
		// setCommands.add(SetCommand.create(scenarioEditingLocation.getEditingDomain(), otherDischarge, CargoPackage.eINSTANCE.getSlot_Cargo(), c));
		//
		// // Optional market slots can be removed.
		// for (final Slot s : c.getSlots()) {
		// if (s instanceof DischargeSlot) {
		// final DischargeSlot oldSlot = (DischargeSlot) s;
		// if (oldSlot instanceof SpotSlot && oldSlot.isOptional()) {
		// deleteCommands.add(DeleteCommand.create(scenarioEditingLocation.getEditingDomain(), oldSlot));
		// }
		// }
		// }
		// }
		// } else {
		// // create a new cargo
		// c = cec.createNewCargo(setCommands, cargoModel);
		// c.setName(loadSlot.getName());
		//
		// setCommands.add(SetCommand.create(scenarioEditingLocation.getEditingDomain(), loadSlot, CargoPackage.eINSTANCE.getSlot_Cargo(), c));
		// setCommands.add(SetCommand.create(scenarioEditingLocation.getEditingDomain(), otherDischarge, CargoPackage.eINSTANCE.getSlot_Cargo(), c));
		// }
		// cec.appendFOBDESCommands(setCommands, deleteCommands, scenarioEditingLocation.getEditingDomain(), inputModel, c, loadSlot, otherDischarge);
		// }
		// } else if (newIndex == -1) {
		// final Cargo c = rowI.cargo;
		// if (c != null) {
		//
		// for (final Slot s : c.getSlots()) {
		//
		// // currentWiringCommand.append(SetCommand.create(scenarioEditingLocation.getEditingDomain(), s, CargoPackage.eINSTANCE.getSlot_Cargo(), SetCommand.UNSET_VALUE));
		//
		// // Optional market slots can be removed.
		// if (s instanceof DischargeSlot) {
		// final DischargeSlot oldSlot = (DischargeSlot) s;
		// if (oldSlot instanceof SpotSlot && oldSlot.isOptional()) {
		// deleteCommands.add(DeleteCommand.create(scenarioEditingLocation.getEditingDomain(), oldSlot));
		// }
		// }
		//
		// if (s instanceof LoadSlot) {
		// final LoadSlot oldSlot = (LoadSlot) s;
		// if (oldSlot instanceof SpotSlot && oldSlot.isOptional()) {
		// deleteCommands.add(DeleteCommand.create(scenarioEditingLocation.getEditingDomain(), oldSlot));
		// }
		// }
		// }
		// deleteCommands.add(DeleteCommand.create(scenarioEditingLocation.getEditingDomain(), c));
		//
		// } else {
		// // Error?
		// }
		// } else {
		// final DischargeSlot dischargeSlot = cec.createNewDischarge(setCommands, cargoModel, false);
		// // ensureCapacity(newIndex + 1, cargoes, loadSlots, dischargeSlots);
		// // dischargeSlots.set(newIndex, dischargeSlot);
		// Cargo c = rowI.cargo;
		// if (c == null) {
		// // create a cargo
		// c = cec.createNewCargo(setCommands, cargoModel);
		// c.setName(loadSlot.getName());
		//
		// setCommands.add(SetCommand.create(scenarioEditingLocation.getEditingDomain(), loadSlot, CargoPackage.eINSTANCE.getSlot_Cargo(), c));
		// setCommands.add(SetCommand.create(scenarioEditingLocation.getEditingDomain(), dischargeSlot, CargoPackage.eINSTANCE.getSlot_Cargo(), c));
		// } else {
		// setCommands.add(SetCommand.create(scenarioEditingLocation.getEditingDomain(), dischargeSlot, CargoPackage.eINSTANCE.getSlot_Cargo(), c));
		// }
		// cec.appendFOBDESCommands(setCommands, deleteCommands, scenarioEditingLocation.getEditingDomain(), inputModel, c, loadSlot, dischargeSlot);
		//
		// }
		// }
		// }

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

	private void refreshContent() {

		performControlUpdate(true);
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
			final CargoModel cargoModel = scenarioEditingLocation.getRootObject().getSubModel(CargoModel.class);

			{
				final Action newDESPurchase = new Action("New DES Purchase") {
					public void run() {

						final CompoundCommand cmd = new CompoundCommand("New DES Purchase");

						final LoadSlot newLoad = cec.createObject(CargoPackage.eINSTANCE.getLoadSlot(), CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), cargoModel);
						newLoad.setDESPurchase(true);
						newLoad.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), EcoreUtil.generateUUID());
						newLoad.setOptional(true);
						newLoad.setName("");
						cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), cargoModel, CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), newLoad));

						scenarioEditingLocation.getEditingDomain().getCommandStack().execute(cmd);
					}
				};
				addActionToMenu(newDESPurchase, menu);
			}
			{
				final Action newLoad = new Action("New Load Slot") {
					public void run() {

						final CompoundCommand cmd = new CompoundCommand("New Load Slot");

						final LoadSlot newLoad = cec.createObject(CargoPackage.eINSTANCE.getLoadSlot(), CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), cargoModel);
						newLoad.setDESPurchase(false);
						newLoad.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), EcoreUtil.generateUUID());
						newLoad.setOptional(true);
						newLoad.setName("");
						cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), cargoModel, CargoPackage.eINSTANCE.getCargoModel_LoadSlots(), newLoad));

						scenarioEditingLocation.getEditingDomain().getCommandStack().execute(cmd);
					}
				};
				addActionToMenu(newLoad, menu);
			}
			{
				final Action newFOBSale = new Action("New FOB Sale") {
					public void run() {

						final CompoundCommand cmd = new CompoundCommand("New FOB Sale");

						final DischargeSlot newDischarge = cec.createObject(CargoPackage.eINSTANCE.getDischargeSlot(), CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(), cargoModel);
						newDischarge.setFOBSale(true);
						newDischarge.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), EcoreUtil.generateUUID());
						newDischarge.setOptional(true);
						newDischarge.setName("");
						cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), cargoModel, CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(), newDischarge));

						scenarioEditingLocation.getEditingDomain().getCommandStack().execute(cmd);
					}
				};
				addActionToMenu(newFOBSale, menu);

			}
			{
				final Action newDischarge = new Action("New Discharge") {
					public void run() {

						final CompoundCommand cmd = new CompoundCommand("New Discharge Slot");

						final DischargeSlot newDischarge = cec.createObject(CargoPackage.eINSTANCE.getDischargeSlot(), CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(), cargoModel);
						newDischarge.setFOBSale(false);
						newDischarge.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), EcoreUtil.generateUUID());
						newDischarge.setOptional(true);
						newDischarge.setName("");
						cmd.append(AddCommand.create(scenarioEditingLocation.getEditingDomain(), cargoModel, CargoPackage.eINSTANCE.getCargoModel_DischargeSlots(), newDischarge));

						scenarioEditingLocation.getEditingDomain().getCommandStack().execute(cmd);
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

	private final class LDDAction extends Action {

		private LDDAction(final String text) {
			super(text);
		}

		@Override
		public void run() {

			try {
				scenarioEditingLocation.getEditorLock().claim();
				scenarioEditingLocation.setDisableUpdates(true);

				final LDDEditor editor = new LDDEditor(getScenarioViewer().getGrid().getShell(), scenarioEditingLocation);
				// editor.setBlockOnOpen(true);

				final Cargo cargo = CargoFactory.eINSTANCE.createCargo();

				final LoadSlot load = CargoFactory.eINSTANCE.createLoadSlot();
				final DischargeSlot discharge1 = CargoFactory.eINSTANCE.createDischargeSlot();
				final DischargeSlot discharge2 = CargoFactory.eINSTANCE.createDischargeSlot();

				cargo.getSlots().addAll(Lists.newArrayList(load, discharge1, discharge2));
				editor.open(cargo);

				final CargoModel cargomodel = scenarioEditingLocation.getRootObject().getSubModel(CargoModel.class);

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

				scenarioEditingLocation.getEditingDomain().getCommandStack().execute(cmd);
			} finally {
				scenarioEditingLocation.setDisableUpdates(false);
				scenarioEditingLocation.getEditorLock().release();
			}
		}
	}
}
