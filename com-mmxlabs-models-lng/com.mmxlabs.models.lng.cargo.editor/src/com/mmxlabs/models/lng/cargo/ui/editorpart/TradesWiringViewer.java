/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.IElementComparer;
import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.Window;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridCellRenderer;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.nebula.widgets.grid.internal.DefaultCellRenderer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.Transfer;
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
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.menus.IMenuService;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.service.event.EventHandler;

import com.mmxlabs.common.Equality;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.editor.PreferenceConstants;
import com.mmxlabs.models.lng.cargo.presentation.CargoEditorPlugin;
import com.mmxlabs.models.lng.cargo.ui.editorpart.CargoModelRowTransformer.GroupData;
import com.mmxlabs.models.lng.cargo.ui.editorpart.CargoModelRowTransformer.RootData;
import com.mmxlabs.models.lng.cargo.ui.editorpart.CargoModelRowTransformer.RowData;
import com.mmxlabs.models.lng.cargo.ui.editorpart.CargoModelRowTransformer.RowDataEMFPath;
import com.mmxlabs.models.lng.cargo.ui.editorpart.CargoModelRowTransformer.Type;
import com.mmxlabs.models.lng.cargo.ui.editorpart.CreateStripDialog.StripType;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.CargoEditingCommands;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.CargoEditorMenuHelper;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.ComplexCargoAction;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.DefaultMenuCreatorAction;
import com.mmxlabs.models.lng.cargo.ui.editorpart.trades.ITradesTableContextMenuExtension;
import com.mmxlabs.models.lng.cargo.ui.editorpart.trades.TradesTableContextMenuExtensionUtil;
import com.mmxlabs.models.lng.cargo.ui.util.TimeWindowHelper;
import com.mmxlabs.models.lng.commercial.BaseEntityBook;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.SlotContractParams;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.EntityProfitAndLoss;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.GroupProfitAndLoss;
import com.mmxlabs.models.lng.schedule.MarketAllocation;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.ui.ImageConstants;
import com.mmxlabs.models.lng.ui.LngUIActivator;
import com.mmxlabs.models.lng.ui.actions.DuplicateAction;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewer;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.date.DateTimeFormatsProvider;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialog;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialogUtil;
import com.mmxlabs.models.ui.editors.dialogs.MultiDetailDialog;
import com.mmxlabs.models.ui.tabular.DefaultToolTipProvider;
import com.mmxlabs.models.ui.tabular.EObjectTableViewer;
import com.mmxlabs.models.ui.tabular.EObjectTableViewerColumnProvider;
import com.mmxlabs.models.ui.tabular.EObjectTableViewerValidationSupport;
import com.mmxlabs.models.ui.tabular.ICellManipulator;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.LocalDateAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.ReadOnlyManipulatorWrapper;
import com.mmxlabs.models.ui.tabular.manipulators.SingleReferenceManipulator;
import com.mmxlabs.models.ui.validation.IStatusProvider;
import com.mmxlabs.models.ui.validation.IStatusProvider.IStatusChangedListener;
import com.mmxlabs.models.ui.valueproviders.IReferenceValueProviderProvider;
import com.mmxlabs.models.util.emfpath.EMFMultiPath;
import com.mmxlabs.models.util.emfpath.EMFPath;
import com.mmxlabs.models.util.emfpath.IEMFPath;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.rcp.common.actions.CopyGridToClipboardAction;
import com.mmxlabs.rcp.common.actions.CopyTableToClipboardAction;
import com.mmxlabs.rcp.common.actions.CopyTreeToClipboardAction;
import com.mmxlabs.rcp.common.actions.LockableAction;
import com.mmxlabs.rcp.common.actions.PackGridTreeColumnsAction;
import com.mmxlabs.rcp.common.dnd.BasicDragSource;
import com.mmxlabs.rcp.common.handlers.TodayHandler;
import com.mmxlabs.rcp.common.menus.LocalMenuHelper;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.ScenarioLock;

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

	private static int LineWrap = 40;
	private static String nl = System.getProperty("line.separator");

	private Iterable<ITradesTableContextMenuExtension> contextMenuExtensions;

	private TradesWiringDiagram wiringDiagram;

	protected RootData rootData;
	/**
	 * A reference {@link RootData} object. This is used by a {@link CargoModelRowTransformer} to retain load/discharge row pairings but allow wires to cross rows. Initially null until the first
	 * rootData object is created. May be "nulled" again to reset state by an action.
	 * 
	 */
	protected RootData referenceRootData;

	private final Set<GridColumn> loadColumns = new HashSet<>();
	private final Set<GridColumn> dischargeColumns = new HashSet<>();

	private Object[] sortedChildren;
	private int[] sortedIndices;

	// todayHandler
	private EventHandler todayHandler;

	protected int[] reverseSortedIndices;

	private boolean locked;

	CargoEditingCommands cec;
	private CargoEditorMenuHelper menuHelper;

	private final Image notesImage;

	private IStatusChangedListener statusChangedListener;

	private final TradesFilter tradesFilter = new TradesFilter();

	private final TradesCargoFilter tradesCargoFilter = new TradesCargoFilter();

	private final ShippedCargoFilter shippedCargoFilter = new ShippedCargoFilter();

	private final TimePeriodFilter monthFilter = new TimePeriodFilter();
	private LocalDate earliest;
	private LocalDate latest;

	private Action resetSortOrder;

	private PromptToolbarEditor promptToolbarEditor;

	private GridViewerColumn assignmentColumn;

	private IPropertyChangeListener propertyChangeListener;
	private final Set<String> filtersOpenContracts = new HashSet<>();
	private IExtraFiltersProvider extraFiltersProvider;

	public TradesWiringViewer(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation scenarioEditingLocation, final IActionBars actionBars) {
		super(page, part, scenarioEditingLocation, actionBars);

		final LNGScenarioModel scenarioModel = (LNGScenarioModel) scenarioEditingLocation.getRootObject();
		this.cec = new CargoEditingCommands(scenarioEditingLocation.getEditingDomain(), scenarioModel, ScenarioModelUtil.getCargoModel(scenarioModel),
				ScenarioModelUtil.getCommercialModel(scenarioModel), Activator.getDefault().getModelFactoryRegistry());
		this.menuHelper = new CargoEditorMenuHelper(part.getSite().getShell(), scenarioEditingLocation, scenarioModel);
		notesImage = CargoEditorPlugin.getPlugin().getImage(CargoEditorPlugin.IMAGE_CARGO_NOTES);

		ServiceHelper.withOptionalServiceConsumer(IExtraFiltersProvider.class, p -> {
			if (p == null) {
				setExtraFiltersProvider(new DefaultExtraFiltersProvider());
			} else {
				setExtraFiltersProvider(p);
			}
		});

	}

	private void setExtraFiltersProvider(final IExtraFiltersProvider provider) {
		this.extraFiltersProvider = provider;
	}

	@Override
	public void dispose() {

		final IStatusProvider statusProvider = scenarioEditingLocation.getStatusProvider();
		if (statusProvider != null) {
			statusProvider.removeStatusChangedListener(statusChangedListener);
		}
		statusChangedListener = null;

		final IPreferenceStore preferenceStore = CargoEditorPlugin.getPlugin().getPreferenceStore();
		if (preferenceStore != null) {
			preferenceStore.removePropertyChangeListener(propertyChangeListener);
		}

		this.rootData = null;
		this.referenceRootData = null;
		this.sortedChildren = null;

		if (promptToolbarEditor != null) {
			// promptToolbarEditor.dispose();
			promptToolbarEditor = null;
		}
		this.cec = null;
		this.menuHelper = null;

		if (wiringDiagram != null) {
			wiringDiagram.dispose();
			wiringDiagram = null;
		}

		if (this.todayHandler != null) {
			final IEventBroker eventBroker = PlatformUI.getWorkbench().getService(IEventBroker.class);
			eventBroker.unsubscribe(this.todayHandler);
		}

		super.dispose();
	}

	@Override
	protected ScenarioTableViewer constructViewer(final Composite parent) {

		final ScenarioTableViewer scenarioViewer = new ScenarioTableViewer(parent, SWT.FULL_SELECTION | SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL, scenarioEditingLocation) {

			@Override
			protected void cellEditorActivated(final Widget widget, final CellEditor cellEditor) {
				if (deleteAction != null) {
					deleteAction.setEnabled(false);
				}
			}

			@Override
			protected void cellEditorDeactivated(final Widget widget, final CellEditor cellEditor) {
				if (deleteAction != null) {
					deleteAction.setEnabled(true);
				}
			}

			@Override
			public EReference getCurrentContainment() {
				return CargoPackage.eINSTANCE.getCargoModel_Cargoes();
			}

			@Override
			public EObject getCurrentContainer() {
				return getScenarioModel().getCargoModel();
			}

			/**
			 * Overridden method to convert internal RowData objects into a collection of EMF Objects
			 */
			@Override
			protected void updateSelection(final ISelection selection) {

				if (selection instanceof IStructuredSelection) {
					final IStructuredSelection originalSelection = (IStructuredSelection) selection;

					final List<Object> selectedObjects = new LinkedList<>();

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
			public void init(final AdapterFactory adapterFactory, final ModelReference modelReference, final EReference... path) {
				super.init(adapterFactory, modelReference, path);

				init(new ITreeContentProvider() {

					@Override
					public void dispose() {

					}

					@Override
					public Object[] getElements(final Object inputElement) {

						final CargoModel cargoModel = getScenarioModel().getCargoModel();
						final ScheduleModel scheduleModel = getScenarioModel().getScheduleModel();

						final RootData root = setCargoes(cargoModel, scheduleModel, referenceRootData);

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

					@Override
					public Object[] getChildren(final Object parentElement) {
						return null;
					}

					@Override
					public Object getParent(final Object element) {
						return null;
					}

					@Override
					public boolean hasChildren(final Object element) {
						return false;
					}

				}, modelReference);
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

				addFilters();
			}

			private void addFilters() {
				addFilter(tradesFilter);
				addFilter(tradesCargoFilter);
				addFilter(shippedCargoFilter);
				addFilter(monthFilter);

				if (extraFiltersProvider != null) {
					for (final ViewerFilter vf : extraFiltersProvider.getExtraFilters()) {
						addFilter(vf);
					}
				}
			}

			@Override
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

					@Override
					protected void updateObject(final EObject object, final IStatus status, final boolean update) {
						if (rootData == null) {
							return;
						}
						if (object == null) {
							return;
						}
						final RootData pRootData = rootData;
						if (pRootData == null) {
							return;
						}
						int idx = -1;
						if (pRootData.getLoadSlots().contains(object)) {
							idx = pRootData.getLoadSlots().indexOf(object);
						} else if (pRootData.getDischargeSlots().contains(object)) {
							idx = pRootData.getDischargeSlots().indexOf(object);
						} else if (pRootData.getCargoes().contains(object)) {
							idx = pRootData.getCargoes().indexOf(object);
						}

						RowData rd = null;
						if (idx >= 0) {
							rd = pRootData.getRows().get(idx);
						}

						if (rd != null) {
							getValidationSupport().setStatus(rd, status);
						}
					}
				};
			}

			@Override
			public EObject getElementForNotificationTarget(final EObject source) {

				if (source instanceof SlotContractParams) {
					return source.eContainer();
				} else if (source instanceof LoadSlot) {
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

			@Override
			protected void doCommandStackChanged() {
				ViewerHelper.refresh(this, true);
			}
		};

		scenarioViewer.setToolTipProvider(new DefaultToolTipProvider() {

			@Override
			public String getToolTipText(final Object element) {
				final RowData rd = (RowData) element;
				final LoadSlot ls = rd.getLoadSlot();
				final DischargeSlot ds = rd.getDischargeSlot();

				String lString = ls != null ? (ls.getNotes() != null ? ls.getNotes() : "") : "";
				String dString = ds != null ? (ds.getNotes() != null ? ds.getNotes() : "") : "";
				if (lString.length() + dString.length() == 0) {
					return null;
				} else {
					if (lString.length() > LineWrap) {
						lString = wrapString(lString, LineWrap);
					}
					if (dString.length() > LineWrap) {
						dString = wrapString(dString, LineWrap);
					}
					return (lString.length() > 0 ? lString : "") + (dString.length() > 0 ? nl + " --- " + nl + dString : "");
				}
			}

			@Override
			public Point getToolTipShift(final Object object) {
				return new Point(10, 10);
			}
		});

		final MenuManager mgr = new MenuManager();

		contextMenuExtensions = TradesTableContextMenuExtensionUtil.getContextMenuExtensions();

		scenarioViewer.getGrid().addMenuDetectListener(new MenuDetectListener() {

			private Menu menu;

			private void populateSingleSelectionMenu(final GridItem item, final GridColumn column) {
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
					{
						final IContributionItem[] items = mgr.getItems();
						mgr.removeAll();
						for (final IContributionItem mItem : items) {
							mItem.dispose();
						}
					}

					if (loadColumns.contains(column)) {
						if (rowDataItem.loadSlot != null) {
							final IMenuListener listener = menuHelper.createLoadSlotMenuListener(rootData.getLoadSlots(), idx);
							listener.menuAboutToShow(mgr);
							if (contextMenuExtensions != null) {
								final Slot<?> slot = rootData.getLoadSlots().get(idx);
								for (final ITradesTableContextMenuExtension ext : contextMenuExtensions) {
									ext.contributeToMenu(scenarioEditingLocation, slot, mgr);
								}
							}
						} else {
							final IMenuListener listener = menuHelper.createDischargeSlotMenuListener(rootData.getDischargeSlots(), idx);
							listener.menuAboutToShow(mgr);
							if (contextMenuExtensions != null) {
								final Slot<?> slot = rootData.getDischargeSlots().get(idx);
								for (final ITradesTableContextMenuExtension ext : contextMenuExtensions) {
									ext.contributeToMenu(scenarioEditingLocation, slot, mgr);
								}
							}
						}
					}
					if (dischargeColumns.contains(column)) {
						if (rowDataItem.dischargeSlot != null) {
							final IMenuListener listener = menuHelper.createDischargeSlotMenuListener(rootData.getDischargeSlots(), idx);
							listener.menuAboutToShow(mgr);
							if (contextMenuExtensions != null) {
								final Slot<?> slot = rootData.getDischargeSlots().get(idx);
								for (final ITradesTableContextMenuExtension ext : contextMenuExtensions) {
									ext.contributeToMenu(scenarioEditingLocation, slot, mgr);
								}
							}
						} else if (rowDataItem.loadSlot != null) {
							final IMenuListener listener = menuHelper.createLoadSlotMenuListener(rootData.getLoadSlots(), idx);
							listener.menuAboutToShow(mgr);
							if (contextMenuExtensions != null) {
								final Slot<?> slot = rootData.getLoadSlots().get(idx);
								for (final ITradesTableContextMenuExtension ext : contextMenuExtensions) {
									ext.contributeToMenu(scenarioEditingLocation, slot, mgr);
								}
							}
						}
					}

					menu.setVisible(true);
				}
			}

			@Override
			public void menuDetected(final MenuDetectEvent e) {

				if (locked) {
					return;
				}

				final Grid grid = getScenarioViewer().getGrid();

				final Point mousePoint = grid.toControl(new Point(e.x, e.y));
				final GridColumn column = grid.getColumn(mousePoint);

				final IStructuredSelection selection = (IStructuredSelection) getScenarioViewer().getSelection();

				if (selection.size() <= 1) {
					populateSingleSelectionMenu(grid.getItem(mousePoint), column);
				} else {
					final Set<Cargo> cargoes = new HashSet<>();
					final Set<LoadSlot> loads = new HashSet<>();
					final Set<DischargeSlot> discharges = new HashSet<>();
					boolean loadSide = loadColumns.contains(column);
					boolean dischargeSide = dischargeColumns.contains(column);
					for (final Object item : selection.toList()) {
						RowData row = (RowData) item;
						final Cargo cargo = row.cargo;
						if (cargo != null) {
							cargoes.add(cargo);
						}
						if (loadSide && row.getLoadSlot() != null) {
							loads.add(row.getLoadSlot());
						}
						if (dischargeSide && row.getDischargeSlot() != null) {
							discharges.add(row.getDischargeSlot());
						}
					}
					populateMultipleSelectionMenu(cargoes, loads, discharges, selection, column);
				}
			}

			private void populateMultipleSelectionMenu(final Set<Cargo> cargoes, Set<LoadSlot> loads, Set<DischargeSlot> discharges, final IStructuredSelection selection, GridColumn column) {
				if (menu == null) {
					menu = mgr.createContextMenu(scenarioViewer.getGrid());
				}
				mgr.removeAll();
				{
					final IContributionItem[] items = mgr.getItems();
					mgr.removeAll();
					for (final IContributionItem item : items) {
						item.dispose();
					}
				}

				final IMenuListener listener = menuHelper.createMultipleSelectionMenuListener(cargoes, loads, discharges, loadColumns.contains(column), dischargeColumns.contains(column),
						assignmentColumn.getColumn() == column);
				listener.menuAboutToShow(mgr);

				if (contextMenuExtensions != null) {
					for (final ITradesTableContextMenuExtension ext : contextMenuExtensions) {
						ext.contributeToMenu(scenarioEditingLocation, selection, mgr);
					}
				}

				menu.setVisible(true);
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
				final Set<Object> aSet = new HashSet<>();
				if (a instanceof RowData) {
					final RowData rd = (RowData) a;
					aSet.add(rd);
					aSet.add(rd.cargo);
					aSet.add(rd.loadSlot);
					aSet.add(rd.dischargeSlot);
					aSet.remove(null);
				} else if (a instanceof CargoAllocation) {
					final CargoAllocation cargoAllocation = (CargoAllocation) a;
					// aSet.add(cargoAllocation.getInputCargo());
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

				} else if (a instanceof OpenSlotAllocation) {
					final OpenSlotAllocation osa = (OpenSlotAllocation) a;
					final Slot s = osa.getSlot();
					if (s != null) {
						aSet.add(s);
					}
				} else {
					aSet.add(a);
				}
				return aSet;
			}
		});

		final IPreferenceStore preferenceStore = CargoEditorPlugin.getPlugin().getPreferenceStore();
		propertyChangeListener = event -> {
			final String property = event.getProperty();
			if (PreferenceConstants.P_CONTRACTS_TO_CONSIDER_OPEN.equals(property)) {
				final String value = preferenceStore.getString(property);
				filtersOpenContracts.clear();
				if (value != null) {
					if (value.contains(",")) {
						final String[] split = value.split(",");
						for (final String str : split) {
							filtersOpenContracts.add(str.trim().toLowerCase());
						}
					} else {
						filtersOpenContracts.add(value.trim().toLowerCase());
					}
					filtersOpenContracts.remove("");
				}
				viewer.refresh();
			}
		};
		preferenceStore.addPropertyChangeListener(propertyChangeListener);
		final String value = preferenceStore.getString(PreferenceConstants.P_CONTRACTS_TO_CONSIDER_OPEN);
		if (value != null) {
			if (value.contains(",")) {
				final String[] split = value.split(",");
				for (final String str : split) {
					filtersOpenContracts.add(str.trim().toLowerCase());
				}
			} else {
				filtersOpenContracts.add(value.trim().toLowerCase());
			}
			filtersOpenContracts.remove("");
		}

		return scenarioViewer;
	}

	/**
	 * Break up inputString into lines, then break up those input lines into wrapped lines lineLength characters long.
	 * 
	 * @param inputString
	 * @param lineLength
	 * @return the wrapped lines
	 */
	protected static String wrapString(String inputString, int lineLength) {
		// System.out.println("It's a wrap yo: \n" + inputString);
		String newString = "";
		// Split input string into inputLineStrings, each of which is a list of words
		// which will result in one or more lines of formatted text
		String[] inputLineStrings = inputString.split(nl);
		for (int i = 0; i < inputLineStrings.length; i++) {
			String inputLine = inputLineStrings[i];
			// Wrap inputLine
			if (inputLine.length() > lineLength) {
				// Split on spaces and decompose into wrapped lines, adding each word to newLine
				// up to line wrap limit, then starting a newLine.
				String[] words = inputLine.split(" ");
				String newLine = words[0] + " ";
				for (int j = 1; j < words.length; j++) {
					String word = words[j];
					// Add word if within limit
					if (newLine.length() + word.length() <= lineLength) {
						newLine += word + " ";
					} else {
						// else add line and start a new line with this word
						newString += newLine + nl;
						newLine = word + " ";
					}
				}
				// Any last words...
				newString += newLine + nl;
			} else {
				// inputLine needs no wrapping.
				newString += inputLine + nl;
			}
		}
		return newString;
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final ModelReference modelReference) {
		getScenarioViewer().init(adapterFactory, modelReference);

		final IStatusProvider statusProvider = scenarioEditingLocation.getStatusProvider();
		getScenarioViewer().setStatusProvider(statusProvider);

		this.earliest = getEarliestScenarioDate();
		this.latest = getLatestScenarioDate();

		if (statusProvider != null) {
			statusChangedListener = (provider, status) -> {
				RunnerHelper.asyncExec(() -> {
					final CargoModelRowTransformer transformer = new CargoModelRowTransformer();
					final ScenarioTableViewer scenarioViewer2 = getScenarioViewer();
					if (scenarioViewer2 != null) {
						transformer.updateWiringValidity(rootData, scenarioViewer2.getValidationSupport().getValidationErrors());
						wiringDiagram.redraw();
					}
				});
			};
			statusProvider.addStatusChangedListener(statusChangedListener);
		}

		final Grid table = getScenarioViewer().getGrid();

		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		// set up snap-to-today
		final IEventBroker eventBroker = PlatformUI.getWorkbench().getService(IEventBroker.class);
		this.todayHandler = event -> snapTo((LocalDate) event.getProperty(IEventBroker.DATA));
		eventBroker.subscribe(TodayHandler.EVENT_SNAP_TO_DATE, this.todayHandler);

		// set up toolbars
		final ToolBarManager toolbar = getToolBarManager();

		setMinToolbarHeight(30);
		promptToolbarEditor = new PromptToolbarEditor("prompt", scenarioEditingLocation.getEditingDomain(), (LNGScenarioModel) scenarioEditingLocation.getRootObject());
		toolbar.add(promptToolbarEditor);

		toolbar.add(new GroupMarker(EDIT_GROUP));
		toolbar.add(new GroupMarker(ADD_REMOVE_GROUP));
		toolbar.add(new GroupMarker(VIEW_GROUP));
		toolbar.appendToGroup(VIEW_GROUP, new PackGridTreeColumnsAction(scenarioViewer));

		/*
		 * final ActionContributionItem filter = filterField.getContribution();
		 * 
		 * toolbar.appendToGroup(VIEW_GROUP, filter);
		 */

		final Action addAction = new AddAction("Add");
		addAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ADD));
		toolbar.appendToGroup(ADD_REMOVE_GROUP, addAction);

		final Action filterAction = new FilterMenuAction("Filter");
		filterAction.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.models.ui.tabular", "/icons/filter.gif"));
		toolbar.appendToGroup(VIEW_GROUP, filterAction);

		// add extension points to toolbar
		{
			final String toolbarID = getToolbarID();
			final IMenuService menuService = PlatformUI.getWorkbench().getService(IMenuService.class);
			if (menuService != null) {
				menuService.populateContributionManager(toolbar, toolbarID);
				toolbar.getControl().addDisposeListener(e -> menuService.releaseContributions(toolbar));
			}
		}

		if (addAction != null) {
			// if we can't add one, we can't duplicate one either.
			final Action dupAction = createDuplicateAction();

			if (dupAction != null) {
				toolbar.appendToGroup(ADD_REMOVE_GROUP, dupAction);
			}
		}
		deleteAction = createDeleteAction(objectsToDelete -> {

			final List<Object> extraObjects = new LinkedList<>();
			for (final Object o : objectsToDelete) {
				Cargo c = null;
				if (o instanceof Slot<?>) {
					c = ((Slot<?>) o).getCargo();
				}
				if (c != null) {
					for (final Slot<?> s : c.getSlots()) {
						if (s instanceof SpotSlot) {
							extraObjects.add(s);
						}
					}
				}
			}
			return extraObjects;
		});
		if (deleteAction != null) {
			toolbar.appendToGroup(ADD_REMOVE_GROUP, deleteAction);
		}
		if (actionBars != null) {
			actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(), deleteAction);
		}

		// TODO: uncomment lines below, to add relevant actions to trades wiring tool
		// bar

		/*
		 * copyAction = createCopyAction(); if (copyAction != null) { toolbar.appendToGroup(ADD_REMOVE_GROUP, copyAction); } if (actionBars != null) {
		 * actionBars.setGlobalActionHandler(ActionFactory.COPY.getId(), copyAction); }
		 * 
		 * pasteAction = createPasteAction(); if (pasteAction != null) { toolbar.appendToGroup(ADD_REMOVE_GROUP, pasteAction); } if (actionBars != null) {
		 * actionBars.setGlobalActionHandler(ActionFactory.COPY.getId(), pasteAction); }
		 */

		Action copyToClipboardAction = null;
		if (viewer instanceof TableViewer) {
			copyToClipboardAction = new CopyTableToClipboardAction(((TableViewer) viewer).getTable());
		} else if (viewer instanceof TreeViewer) {
			copyToClipboardAction = new CopyTreeToClipboardAction(((TreeViewer) viewer).getTree());
		} else if (viewer instanceof GridTableViewer) {
			copyToClipboardAction = new CopyGridToClipboardAction(((GridTableViewer) viewer).getGrid());
		} else if (viewer instanceof GridTreeViewer) {
			copyToClipboardAction = new CopyGridToClipboardAction(((GridTreeViewer) viewer).getGrid());
		}

		if (copyToClipboardAction != null) {
			toolbar.add(copyToClipboardAction);
		}

		// Reset sort order
		{
			resetSortOrder = new Action() {
				@Override
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
		{
			final AssignmentManipulator assignmentManipulator = new AssignmentManipulator(scenarioEditingLocation);
			final RowDataEMFPath assignmentPath = new RowDataEMFPath(true, Type.SLOT_OR_CARGO);
			assignmentColumn = addTradesColumn(null, "Vessel", new ReadOnlyManipulatorWrapper<>(assignmentManipulator), assignmentPath);
		}
		{
			final BasicAttributeManipulator manipulator = new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getNamedObject_Name(), editingDomain);
			final RowDataEMFPath assignmentPath = new RowDataEMFPath(false, Type.LOAD);
			final GridViewerColumn idColumn = addTradesColumn(loadColumns, "L-ID", manipulator, assignmentPath);
			idColumn.setLabelProvider(new EObjectTableViewerColumnProvider(getScenarioViewer(), manipulator, assignmentPath) {
				@Override
				public Image getImage(final Object element) {

					if (element instanceof RowData) {
						final RowData rowDataItem = (RowData) element;
						final Object object = assignmentPath.get(rowDataItem);
						if (object instanceof LoadSlot) {
							final LoadSlot ds = (LoadSlot) object;

							if (ds.getNotes() != null && !ds.getNotes().isEmpty()) {
								return notesImage;
							}
						}
					}

					return super.getImage(element);
				}
			});
		}

		addTradesColumn(loadColumns, "Port", new SingleReferenceManipulator(pkg.getSlot_Port(), provider, editingDomain), new RowDataEMFPath(false, Type.LOAD));
		addTradesColumn(loadColumns, "Buy At", new ContractManipulator(provider, editingDomain), new RowDataEMFPath(false, Type.LOAD));
		addTradesColumn(loadColumns, "Price",
				new ReadOnlyManipulatorWrapper<BasicAttributeManipulator>(new BasicAttributeManipulator(SchedulePackage.eINSTANCE.getSlotAllocation_Price(), editingDomain) {

					@Override
					protected String renderSetValue(final Object container, final Object setValue) {
						if (setValue instanceof Number) {
							return String.format("$%.2f", ((Number) setValue).doubleValue());
						}
						return super.renderSetValue(container, setValue);
					}
				}), new RowDataEMFPath(false, Type.LOAD_ALLOCATION));
		final GridViewerColumn loadVol = addTradesColumn(loadColumns, "Vol", new VolumeAttributeManipulator(pkg.getSlot_MaxQuantity(), editingDomain), new RowDataEMFPath(false, Type.LOAD));
		loadVol.getColumn().setHeaderTooltip("in m³ or mmBtu");

		final GridViewerColumn loadDateColumn = addTradesColumn(loadColumns, "Window", new LocalDateAttributeManipulator(pkg.getSlot_WindowStart(), editingDomain) {
			@Override
			public String renderSetValue(final Object owner, final Object object) {
				String v = super.renderSetValue(owner, object);
				if (!v.isEmpty()) {
					final String suffix = TimeWindowHelper.getTimeWindowSuffix(owner);
					v = v + suffix;
				}
				v = v + (v.isEmpty() ? "" : " ");
				v = v + TimeWindowHelper.getCPWindowSuffix(owner);
				return v;
			}

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

		final SchedulePackage sp = SchedulePackage.eINSTANCE;
		if (LicenseFeatures.isPermitted("features:report-arrivaltimes")) {

			final GridViewerColumn loadScheduledDate = addTradesColumn(loadColumns, "Arriving",
					new ReadOnlyManipulatorWrapper<BasicAttributeManipulator>(new BasicAttributeManipulator(sp.getEvent_Start(), editingDomain) {

						/**
						 * FM had to copy the entire code from com.mmxlabs.lingo.reports.views.formatters AsLocalDateFormatter from getLocalDate() to not create cross dependency
						 */
						@Override
						public String renderSetValue(final Object owner, final Object object) {
							if (object instanceof ZonedDateTime) {
								final DateTimeFormatter sdf = DateTimeFormatsProvider.INSTANCE.createDateStringDisplayFormatter();
								return ((ZonedDateTime) object).toLocalDateTime().format(sdf);
							}
							return "";
						}
					}), new RowDataEMFPath(false, Type.LOAD_ALLOCATION, sp.getSlotAllocation_SlotVisit()));
			loadScheduledDate.getColumn().setData(EObjectTableViewer.COLUMN_SORT_PATH, new RowDataEMFPath(false, Type.LOAD_ALLOCATION));
		}

		final GridViewerColumn wiringColumn = addWiringColumn();

		final GridViewerColumn dischargeDateColumn = addTradesColumn(dischargeColumns, "Window", new LocalDateAttributeManipulator(pkg.getSlot_WindowStart(), editingDomain) {

			@Override
			public String renderSetValue(final Object owner, final Object object) {
				String v = super.renderSetValue(owner, object);
				if (!v.isEmpty()) {
					final String suffix = TimeWindowHelper.getTimeWindowSuffix(owner);
					v = v + suffix;
				}
				v = v + (v.isEmpty() ? "" : " ");
				v = v + TimeWindowHelper.getCPWindowSuffix(owner);
				return v;
			}

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

		if (LicenseFeatures.isPermitted("features:report-arrivaltimes")) {
			final GridViewerColumn dischargeScheduledDate = addTradesColumn(loadColumns, "Arriving",
					new ReadOnlyManipulatorWrapper<BasicAttributeManipulator>(new BasicAttributeManipulator(sp.getEvent_Start(), editingDomain) {

						/**
						 * FM had to copy the entire code from com.mmxlabs.lingo.reports.views.formatters AsLocalDateFormatter from getLocalDate() to not create cross dependency
						 */
						@Override
						public String renderSetValue(final Object owner, final Object object) {
							if (object instanceof ZonedDateTime) {
								final DateTimeFormatter sdf = DateTimeFormatsProvider.INSTANCE.createDateStringDisplayFormatter();
								return ((ZonedDateTime) object).toLocalDateTime().format(sdf);
							}
							return "";
						}
					}), new RowDataEMFPath(false, Type.DISCHARGE_ALLOCATION, sp.getSlotAllocation_SlotVisit()));
			dischargeScheduledDate.getColumn().setData(EObjectTableViewer.COLUMN_SORT_PATH, new RowDataEMFPath(false, Type.DISCHARGE_ALLOCATION));
		}

		addTradesColumn(dischargeColumns, "Sell At", new ContractManipulator(provider, editingDomain), new RowDataEMFPath(false, Type.DISCHARGE));
		if (LicenseFeatures.isPermitted("features:report-counterparty")) {
			addTradesColumn(dischargeColumns, "Counterparty", new ReadOnlyManipulatorWrapper<BasicAttributeManipulator>(new BasicAttributeManipulator(pkg.getSlot_Counterparty(), editingDomain) {

				@Override
				protected String renderSetValue(final Object container, final Object setValue) {
					return setValue == null ? "" : setValue.toString();
				}

				@Override
				public boolean isValueUnset(final Object object) {
					return false;
				}

			}), new RowDataEMFPath(false, Type.DISCHARGE));
		}
		addTradesColumn(dischargeColumns, "Price",
				new ReadOnlyManipulatorWrapper<BasicAttributeManipulator>(new BasicAttributeManipulator(SchedulePackage.eINSTANCE.getSlotAllocation_Price(), editingDomain) {

					@Override
					protected String renderSetValue(final Object container, final Object setValue) {
						if (setValue instanceof Number) {
							return String.format("$%.2f", ((Number) setValue).doubleValue());
						}
						return super.renderSetValue(container, setValue);
					}
				}), new RowDataEMFPath(false, Type.DISCHARGE_ALLOCATION));

		addTradesColumn(dischargeColumns, "Vol", new VolumeAttributeManipulator(pkg.getSlot_MaxQuantity(), editingDomain), new RowDataEMFPath(false, Type.DISCHARGE)).getColumn()
				.setHeaderTooltip("in m³ or mmBtu");
		addTradesColumn(dischargeColumns, "Port", new SingleReferenceManipulator(pkg.getSlot_Port(), provider, editingDomain), new RowDataEMFPath(false, Type.DISCHARGE));
		// addTradesColumn(dischargeColumns, "D-ID", new
		// BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getNamedObject_Name(),
		// editingDomain), new RowDataEMFPath(false, Type.DISCHARGE));
		{
			final BasicAttributeManipulator manipulator = new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getNamedObject_Name(), editingDomain);
			final RowDataEMFPath assignmentPath = new RowDataEMFPath(false, Type.DISCHARGE);
			final GridViewerColumn idColumn = addTradesColumn(dischargeColumns, "D-ID", manipulator, assignmentPath);
			idColumn.setLabelProvider(new EObjectTableViewerColumnProvider(getScenarioViewer(), manipulator, assignmentPath) {
				@Override
				public Image getImage(final Object element) {

					if (element instanceof RowData) {
						final RowData rowDataItem = (RowData) element;
						final Object object = assignmentPath.get(rowDataItem);
						if (object instanceof DischargeSlot) {
							final DischargeSlot ds = (DischargeSlot) object;

							if (ds.getNotes() != null && !ds.getNotes().isEmpty()) {
								return notesImage;
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

		// addPNLColumn("P&L (Trade)",
		// CommercialPackage.Literals.BASE_LEGAL_ENTITY__TRADING_BOOK,
		// new
		// BasicAttributeManipulator(SchedulePackage.eINSTANCE.getProfitAndLossContainer_GroupProfitAndLoss(),
		// editingDomain),
		// new RowDataEMFPath(true, Type.CARGO_OR_MARKET_OR_OPEN_ALLOCATION));
		// addPNLColumn("P&L (Shipping)",
		// CommercialPackage.Literals.BASE_LEGAL_ENTITY__SHIPPING_BOOK,
		// new
		// BasicAttributeManipulator(SchedulePackage.eINSTANCE.getProfitAndLossContainer_GroupProfitAndLoss(),
		// editingDomain),
		// new RowDataEMFPath(true, Type.CARGO_OR_MARKET_OR_OPEN_ALLOCATION));
		if (LicenseFeatures.isPermitted("features:report-equity-book")) {
			addPNLColumn("P&L (Equity)", CommercialPackage.Literals.BASE_LEGAL_ENTITY__UPSTREAM_BOOK,
					new BasicAttributeManipulator(SchedulePackage.eINSTANCE.getProfitAndLossContainer_GroupProfitAndLoss(), editingDomain),
					new RowDataEMFPath(true, Type.CARGO_OR_MARKET_OR_OPEN_ALLOCATION));
		}
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
				final int[] heights = new int[rootData.getRows().size() + 1];
				heights[0] = getScenarioViewer().getGrid().getHeaderHeight();

				// Pass one, get heights
				final GridItem[] items = getScenarioViewer().getGrid().getItems();
				for (int idx = 1; idx < Math.min(heights.length, 1 + items.length); ++idx) {
					final GridItem item = items[idx - 1];
					heights[idx] = 1 + heights[idx - 1] + item.getHeight();
				}
				// Find the row at the top of the table and get it's "height" so we can adjust
				// it later
				final int vPod = getScenarioViewer().getGrid().getVerticalBar().getSelection();
				final int hOffset = (heights[vPod]) - getScenarioViewer().getGrid().getHeaderHeight();
				// Pass 2 get mid-points
				for (int idx = 0; idx < Math.min(heights.length, items.length); ++idx) {
					final GridItem item = items[idx];
					heights[idx] += item.getHeight() / 2;
				}

				// Pass 3, offset to so top visible row in table is at height "0"
				for (int idx = 0; idx < Math.min(heights.length, items.length); ++idx) {
					heights[idx] -= hOffset;
				}

				// Convert to
				final List<Float> data = new ArrayList<>(heights.length);
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
				for (int ii = 0; ii < columnOrder.length; ++ii) {
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
				// This used to be based in column index, now for some reason it is px based. If
				// the old behaviour re-appears, then this value is the second for loop
				// initialiser value.
				offset -= getScenarioViewer().getGrid().getHorizontalBar().getSelection();
				// TODO: Take into account h scroll final int colWidth =
				// getScenarioViewer().getGrid().getColumn(wiringColumnIndex).getWidth();
				return new Rectangle(area.x + offset, area.y + getScenarioViewer().getGrid().getHeaderHeight(), wiringColumn.getColumn().getWidth(), area.height);
			}
		};
		wiringDiagram.setSortOrder(rootData, sortedIndices, reverseSortedIndices);

		// Hook in a listener to notify mouse events
		final WiringDiagramMouseListener listener = new WiringDiagramMouseListener();
		getScenarioViewer().getGrid().addMouseMoveListener(listener);
		getScenarioViewer().getGrid().addMouseListener(listener);

		final DragSource source = new DragSource(getScenarioViewer().getControl(), DND.DROP_MOVE | DND.DROP_LINK);
		final Transfer[] types = new Transfer[] { LocalSelectionTransfer.getTransfer() };
		source.setTransfer(types);

		source.addDragListener(new BasicDragSource(viewer) {
			@Override
			public void dragStart(final DragSourceEvent event) {
				if (getScenarioViewer().getGrid().getColumn(new Point(event.x, event.y)) == wiringColumn.getColumn()) {
					event.doit = false;
					return;
				}

				super.dragStart(event);
			}
		});
	}

	private void snapTo(final LocalDate property) {
		if (scenarioViewer == null) {
			return;
		}
		final Grid grid = scenarioViewer.getGrid();
		if (grid == null) {
			return;
		}
		final int count = grid.getItemCount();
		if (count <= 0) {
			return;
		}

		final GridItem[] items = grid.getItems();
		int pos = -1;
		for (final GridItem item : items) {
			final Object oData = item.getData();
			if (oData instanceof RowData) {
				final RowData rd = (RowData) oData;
				final LoadSlot ls = rd.getLoadSlot();
				if (ls != null) {
					if (ls.getWindowStart().isAfter(property)) {
						break;
					}
					pos++;
					continue;
				}
				final DischargeSlot ds = rd.getDischargeSlot();
				if (ds != null) {
					if (ds.getWindowStart().isAfter(property)) {
						break;
					}
					pos++;
					continue;
				}
			}
		}
		if (pos != -1) {
			grid.deselectAll();
			grid.select(pos);
			grid.showSelection();
		}
	}

	private LocalDate getEarliestScenarioDate() {
		LocalDate result = LocalDate.now();

		final IScenarioDataProvider sdp = scenarioEditingLocation.getScenarioDataProvider();
		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(sdp);

		LocalDate erl = result;

		for (final LoadSlot ls : cargoModel.getLoadSlots()) {
			if (ls.getWindowStart() != null && erl.isAfter(ls.getWindowStart())) {
				erl = ls.getWindowStart();
			}
		}
		for (final DischargeSlot ds : cargoModel.getDischargeSlots()) {
			if (ds.getWindowStart() != null && erl.isAfter(ds.getWindowStart())) {
				erl = ds.getWindowStart();
			}
		}
		if (erl.isBefore(result)) {
			result = erl;
		}

		return result;
	}

	private LocalDate getLatestScenarioDate() {
		LocalDate result = LocalDate.now();

		final IScenarioDataProvider sdp = scenarioEditingLocation.getScenarioDataProvider();
		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(sdp);

		LocalDate erl = result;

		for (final LoadSlot ls : cargoModel.getLoadSlots()) {
			if (ls.getSchedulingTimeWindow().getEnd() != null && erl.isBefore(ls.getSchedulingTimeWindow().getEnd().toLocalDate())) {
				erl = ls.getSchedulingTimeWindow().getEnd().toLocalDate();
			}
		}
		for (final DischargeSlot ds : cargoModel.getDischargeSlots()) {
			if (ds.getSchedulingTimeWindow().getEnd() != null && erl.isBefore(ds.getSchedulingTimeWindow().getEnd().toLocalDate())) {
				erl = ds.getSchedulingTimeWindow().getEnd().toLocalDate();
			}
		}
		if (erl.isAfter(result)) {
			result = erl;
		}

		return result;
	}

	private <T extends ICellManipulator & ICellRenderer> GridViewerColumn addPNLColumn(final String columnName, final EStructuralFeature bookContainmentFeature, final T manipulator,
			final EMFPath path) {

		final ReadOnlyManipulatorWrapper<T> wrapper = new ReadOnlyManipulatorWrapper<T>(manipulator) {
			@Override
			public Comparable<?> getComparable(final Object element) {
				final Object object = path.get((EObject) element);
				if (object instanceof ProfitAndLossContainer) {
					final ProfitAndLossContainer container = (ProfitAndLossContainer) object;
					final GroupProfitAndLoss groupProfitAndLoss = container.getGroupProfitAndLoss();
					if (groupProfitAndLoss != null) {

						long totalPNL = 0;
						for (final EntityProfitAndLoss entityPNL : groupProfitAndLoss.getEntityProfitAndLosses()) {
							final BaseEntityBook entityBook = entityPNL.getEntityBook();
							if (entityBook == null) {
								// Fall back code path for old models.
								if (bookContainmentFeature == CommercialPackage.Literals.BASE_LEGAL_ENTITY__TRADING_BOOK) {
									return groupProfitAndLoss.getProfitAndLoss();
								} else {
									return 0;
								}
							} else {
								if (entityBook.eContainmentFeature() == bookContainmentFeature) {
									totalPNL += entityPNL.getProfitAndLoss();
								}
							}
						}
						return totalPNL;
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
					final GroupProfitAndLoss groupProfitAndLoss = container.getGroupProfitAndLoss();
					if (groupProfitAndLoss != null) {

						long totalPNL = 0;
						for (final EntityProfitAndLoss entityPNL : groupProfitAndLoss.getEntityProfitAndLosses()) {
							final BaseEntityBook entityBook = entityPNL.getEntityBook();
							if (entityBook == null) {
								// Fall back code path for old models.
								if (bookContainmentFeature == CommercialPackage.Literals.BASE_LEGAL_ENTITY__TRADING_BOOK) {
									totalPNL = groupProfitAndLoss.getProfitAndLoss();
								}
								break;
							} else {
								if (entityBook.eContainmentFeature() == bookContainmentFeature) {
									totalPNL += entityPNL.getProfitAndLoss();
								}
							}
						}
						return String.format("%.2fm", ((float) totalPNL) / 1000000);
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
	 */
	public RootData setCargoes(final CargoModel cargoModel, final ScheduleModel scheduleModel, final RootData existingData) {
		final CargoModelRowTransformer transformer = new CargoModelRowTransformer();
		return transformer.transform(cargoModel, scheduleModel, getScenarioViewer().getValidationSupport().getValidationErrors(), existingData);
	}

	public void init(final AdapterFactory adapterFactory, final ModelReference modelReference) {
		getScenarioViewer().init(adapterFactory, modelReference);

	}

	@Override
	protected void enableOpenListener() {
		final LocalMenuHelper helper = new LocalMenuHelper(scenarioViewer.getGrid());
		scenarioViewer.getControl().addDisposeListener(e -> helper.dispose());
		scenarioViewer.addOpenListener(new IOpenListener() {

			private final AssignToMenuHelper assignToHelper = new AssignToMenuHelper(scenarioEditingLocation.getShell(), scenarioEditingLocation, getScenarioModel());

			@Override
			public void open(final OpenEvent event) {

				if (scenarioViewer.getSelection() instanceof IStructuredSelection) {
					final IStructuredSelection structuredSelection = (IStructuredSelection) scenarioViewer.getSelection();
					if (!structuredSelection.isEmpty()) {

						// Attempt to detect the column we clicked on.
						final GridColumn column = null;
						final IWorkbench workbench = PlatformUI.getWorkbench();
						if (workbench != null) {
							final Display display = workbench.getDisplay();
							if (display != null) {
								final Point cursorLocation = display.getCursorLocation();
								if (cursorLocation != null) {

									final Grid grid = getScenarioViewer().getGrid();

									final Point mousePoint = grid.toControl(cursorLocation);
									if (assignmentColumn.getColumn() == grid.getColumn(mousePoint)) {
										if (!scenarioViewer.isLocked()) {
											final Iterator<?> itr = structuredSelection.iterator();
											final Object obj = itr.next();
											if (obj instanceof RowData) {
												final RowData rd = (RowData) obj;
												if (rd.cargo != null && rd.cargo.getCargoType() == CargoType.FLEET) {
													helper.clearActions();
													assignToHelper.createAssignmentMenus(helper, rd.cargo);
													helper.open();
												} else if (rd.loadSlot != null && rd.loadSlot.isDESPurchase()) {
													helper.clearActions();
													assignToHelper.createAssignmentMenus(helper, rd.loadSlot);
													helper.open();
												} else if (rd.dischargeSlot != null && rd.dischargeSlot.isFOBSale()) {
													helper.clearActions();
													assignToHelper.createAssignmentMenus(helper, rd.dischargeSlot);
													helper.open();
												}
											}
										}
										return;
									}
								}
							}
						}

						final List<EObject> editorTargets = new ArrayList<>();
						final Iterator<?> itr = structuredSelection.iterator();
						while (itr.hasNext()) {
							final Object obj = itr.next();
							EObject target = null;
							if (obj instanceof RowData) {
								final RowData rd = (RowData) obj;
								if (rd.cargo != null) {
									target = rd.cargo;
								} else if ((column == null || loadColumns.contains(column)) && rd.loadSlot != null) {
									target = rd.loadSlot;
								} else if ((column == null || dischargeColumns.contains(column) || !loadColumns.contains(column)) && rd.dischargeSlot != null) {
									target = rd.dischargeSlot;
								}
							}
							if (target != null) {
								editorTargets.add(target);
							}
						}
						if (!editorTargets.isEmpty() && !scenarioViewer.isLocked()) {
							final ScenarioLock editorLock = scenarioEditingLocation.getEditorLock();
							if (editorLock.tryLock(500)) {
								try {
									scenarioEditingLocation.setDisableUpdates(true);
									if (editorTargets.size() > 1) {
										final MultiDetailDialog mdd = new MultiDetailDialog(event.getViewer().getControl().getShell(), scenarioEditingLocation.getRootObject(),
												scenarioEditingLocation.getDefaultCommandHandler());
										mdd.open(scenarioEditingLocation, editorTargets);
									} else {
										final DetailCompositeDialog dcd = new DetailCompositeDialog(event.getViewer().getControl().getShell(), scenarioEditingLocation.getDefaultCommandHandler(),
												~SWT.MAX) {
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
									editorLock.unlock();
								}
							}
						}
					}
				}
			}

		});
	}

	/**
	 */
	protected void doWiringChanged(final Map<RowData, RowData> newWiring, final boolean ctrlPressed) {

		final boolean swapDischarges = ctrlPressed;
		// Complex cargoes no longer supported in wiring DND
		final boolean createComplexCargo = false; // ctrlPressed && LicenseFeatures.isPermitted("features:complex-cargo")

		final List<Command> setCommands = new LinkedList<>();

		final CargoModel cargoModel = getScenarioModel().getCargoModel();

		final Set<Slot<?>> slotsToRemove = new HashSet<>();
		final Set<Slot<?>> slotsToKeep = new HashSet<>();
		final Set<Cargo> cargoesToRemove = new HashSet<>();
		final Set<Cargo> cargoesToKeep = new HashSet<>();
		for (final Map.Entry<RowData, RowData> e : newWiring.entrySet()) {
			final RowData loadSide = e.getKey();
			final RowData dischargeSide = e.getValue();

			{
				// Address new A -> B wiring
				if (dischargeSide != null && dischargeSide.dischargeSlot != null) {
					if (loadSide.loadSlot.isLocked() || dischargeSide.dischargeSlot.isLocked() || loadSide.loadSlot.isCancelled() || dischargeSide.dischargeSlot.isCancelled()) {
						// Slots are not permitted to be wired together
						return;
					}
					final Cargo c;
					if (loadSide.cargo == null) {
						// New Cargo
						c = cec.createNewCargo(setCommands, cargoModel);
						setCommands.add(SetCommand.create(scenarioEditingLocation.getEditingDomain(), loadSide.loadSlot, CargoPackage.eINSTANCE.getSlot_Cargo(), c));
					} else {
						c = loadSide.cargo;

						// Swap discharges will take care of this later if needed
						if (!createComplexCargo && !swapDischarges) {
							// Break the existing wiring
							for (final Slot<?> s : c.getSlots()) {
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

					// Kept cargoes should not force fixed wiring after a change
					setCommands.add(SetCommand.create(scenarioEditingLocation.getEditingDomain(), c, CargoPackage.Literals.CARGO__ALLOW_REWIRING, Boolean.TRUE));

					setCommands.add(SetCommand.create(scenarioEditingLocation.getEditingDomain(), dischargeSide.dischargeSlot, CargoPackage.eINSTANCE.getSlot_Cargo(), c));
					if (c != null && dischargeSide.dischargeSlot.isFOBSale()) {
						// Cargo assignments should be removed.
						setCommands.add(SetCommand.create(scenarioEditingLocation.getEditingDomain(), c, CargoPackage.eINSTANCE.getAssignableElement_VesselAssignmentType(), SetCommand.UNSET_VALUE));
						setCommands.add(SetCommand.create(scenarioEditingLocation.getEditingDomain(), c, CargoPackage.eINSTANCE.getAssignableElement_Locked(), Boolean.FALSE));
						setCommands.add(SetCommand.create(scenarioEditingLocation.getEditingDomain(), c, CargoPackage.eINSTANCE.getAssignableElement_SequenceHint(), SetCommand.UNSET_VALUE));
						setCommands.add(SetCommand.create(scenarioEditingLocation.getEditingDomain(), c, CargoPackage.eINSTANCE.getAssignableElement_SpotIndex(), SetCommand.UNSET_VALUE));
					}

					{
						Cargo dischargeCargo = null;
						if (dischargeSide != null && dischargeSide.dischargeSlot != null) {
							dischargeCargo = dischargeSide.dischargeSlot.getCargo();
						}
						if (dischargeCargo != null) {
							// Make sure the discharge from the original load is now passed into the new
							// cargo
							if (swapDischarges && loadSide.dischargeSlot != null) {
								// Break the existing wiring
								for (final Slot<?> s : c.getSlots()) {
									if (s != loadSide.loadSlot) {
										setCommands.add(SetCommand.create(scenarioEditingLocation.getEditingDomain(), s, CargoPackage.eINSTANCE.getSlot_Cargo(), dischargeCargo));
									}
								}
							} else {
								// We want to break up the discharge cargo
								if (dischargeCargo.getSlots().size() <= 2) {
									for (final Slot<?> s : dischargeCargo.getSlots()) {
										if (s != dischargeSide.dischargeSlot) {
											setCommands.add(SetCommand.create(scenarioEditingLocation.getEditingDomain(), s, CargoPackage.eINSTANCE.getSlot_Cargo(), null));

											// Optional market slots can be removed.
											if (s instanceof SpotSlot && s.isOptional()) {
												slotsToRemove.add(s);
											}

										}
									}
									cargoesToRemove.add(dischargeCargo);
								} else {
									// Kept cargoes should not force fixed wiring after a change
									setCommands.add(SetCommand.create(scenarioEditingLocation.getEditingDomain(), dischargeCargo, CargoPackage.Literals.CARGO__ALLOW_REWIRING, Boolean.TRUE));
								}
							}
						} else if (swapDischarges) {
							// Swapping discharges, but we don't have a pair of cargoes, so fall back to non-swap behaviour.

							// Break the existing wiring
							for (final Slot<?> s : c.getSlots()) {
								if (s != loadSide.loadSlot) {
									setCommands.add(SetCommand.create(scenarioEditingLocation.getEditingDomain(), s, CargoPackage.eINSTANCE.getSlot_Cargo(), dischargeCargo));

									// Optional market slots can be removed.
									if (s instanceof SpotSlot && s.isOptional()) {
										slotsToRemove.add(s);
									}
								}
							}
						}
					}
				} else {
					// Broken the wiring
					if (loadSide.cargo != null) {
						cargoesToRemove.add(loadSide.cargo);
						for (final Slot<?> s : loadSide.cargo.getSlots()) {
							// Optional market slots can be removed.
							if (s instanceof SpotSlot && s.isOptional()) {
								slotsToRemove.add(s);
							}
						}
					}
				}
			}
		}
		cargoesToRemove.removeAll(cargoesToKeep);

		final Set<Object> allObjectsToDelete = new HashSet<>();
		allObjectsToDelete.addAll(cargoesToRemove);
		slotsToRemove.removeAll(slotsToKeep);
		allObjectsToDelete.addAll(slotsToRemove);

		final CompoundCommand currentWiringCommand = new CompoundCommand("Rewire Cargoes");
		// Process set before delete
		for (final Command c : setCommands) {
			currentWiringCommand.append(c);
		}
		// Append delete command
		if (!allObjectsToDelete.isEmpty()) {
			currentWiringCommand.append(DeleteCommand.create(scenarioEditingLocation.getEditingDomain(), allObjectsToDelete));
		}

		executeCurrentWiringCommand(currentWiringCommand);

		// SANITTY CHECKING!
		{
			cec.verifyCargoModel(cargoModel);
		}
	}

	/**
	 */
	protected LNGScenarioModel getScenarioModel() {
		return ((LNGScenarioModel) scenarioEditingLocation.getRootObject());
	}

	private void executeCurrentWiringCommand(final CompoundCommand currentWiringCommand) {
		// Delete commands can be slow, so show the busy indicator while deleting.
		if (currentWiringCommand.isEmpty()) {
			return;
		}
		// TODO: Race conditions in the app can cause this command to fail. If two
		// editing command happen too quickly, the first command could have executed but
		// the second command is created
		// before the UI state has refreshed properly (due to various asyncExec calls).
		BusyIndicator.showWhile(null, () -> scenarioEditingLocation.getEditingDomain().getCommandStack().execute(currentWiringCommand));
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

		col.setLabelProvider(new EObjectTableViewerColumnProvider(getScenarioViewer(), manipulator, path) {
			@Override
			public Color getForeground(final Object element) {
				if (element instanceof EObject) {
					final EObject eObject = (EObject) element;
					final Object o = path.get(eObject);
					if (manipulator.isValueUnset(o)) {
						return Display.getDefault().getSystemColor(SWT.COLOR_DARK_GRAY);
					}
				}
				return null;
			}
		});

		return col;
	}

	@Override
	public void setLocked(final boolean locked) {
		if (this.locked == locked) {
			return;
		}
		if (promptToolbarEditor != null) {
			promptToolbarEditor.setLocked(locked);
		}

		this.locked = locked;
		super.setLocked(locked);
		wiringDiagram.setLocked(locked);
	}

	private class TradesFilter extends ViewerFilter {
		private final Map<IEMFPath, EObject> filterValues = new HashMap<>();

		public void setFilterValue(final IEMFPath path, final EObject value) {
			// adding a particular filter value resets all others
			filterValues.clear();
			if (value != null) {
				filterValues.put(path, value);
			} else {
				filterValues.remove(path);
			}
		}

		public EObject getFilterValue(final IEMFPath path) {
			return filterValues.get(path);
		}

		@Override
		public boolean select(final Viewer viewer, final Object parentElement, final Object element) {
			// TODO Auto-generated method stub
			if (element instanceof EObject) {
				final EObject object = (EObject) element;

				for (final Entry<IEMFPath, EObject> entry : filterValues.entrySet()) {
					final Object value = entry.getKey().get(object);
					if (value != entry.getValue()) {
						return false;
					}
				}
			}

			return true;
		}

		public void clear() {
			filterValues.clear();
		}
	}

	private enum CargoFilterOption {
		NONE, CARGO, LONG, SHORT, OPEN
	}

	private class TradesCargoFilter extends ViewerFilter {
		private CargoFilterOption option = CargoFilterOption.NONE;

		@Override
		public boolean select(final Viewer viewer, final Object parentElement, final Object element) {
			RowData row = null;
			Cargo cargo = null;
			if (element instanceof RowData) {
				row = (RowData) element;
				cargo = row.getCargo();
			}
			switch (option) {
			case NONE:
				return true;
			case CARGO:
				return cargo != null;
			case LONG:
				return isLong(row, cargo);
			case SHORT:
				return isShort(row, cargo);
			case OPEN:
				return isLong(row, cargo) || isShort(row, cargo);
			}
			return false;
		}

		private boolean isShort(final RowData row, final Cargo cargo) {
			if (row != null && cargo == null && row.getDischargeSlot() != null) {
				return true;
			} else if (row != null && cargo != null && row.getDischargeSlot() != null && row.getLoadSlot() instanceof SpotSlot) {
				return true;
			} else if (row != null && cargo != null && row.getDischargeSlot() != null && row.getLoadSlot() != null) {
				final LoadSlot load = row.getLoadSlot();
				final Contract contract = load.getContract();
				if (contract != null && contract.getName() != null) {
					return filtersOpenContracts.contains(contract.getName().toLowerCase());
				}
			}
			return false;
		}

		private boolean isLong(final RowData row, final Cargo cargo) {
			if (row != null && cargo == null && row.getLoadSlot() != null) {
				return true;
			} else if (row != null && cargo != null && row.getLoadSlot() != null && row.getDischargeSlot() instanceof SpotSlot) {
				return true;
			} else if (row != null && cargo != null && row.getLoadSlot() != null && row.getDischargeSlot() != null) {
				final DischargeSlot discharge = row.getDischargeSlot();
				final Contract contract = discharge.getContract();
				if (contract != null && contract.getName() != null) {
					return filtersOpenContracts.contains(contract.getName().toLowerCase());
				}
			}
			return false;
		}

	}

	private enum ShippedCargoFilterOption {
		NONE, SHIPPED, NON_SHIPPED, DES, FOB
	}

	private class ShippedCargoFilter extends ViewerFilter {
		private ShippedCargoFilterOption option = ShippedCargoFilterOption.NONE;

		@Override
		public boolean select(final Viewer viewer, final Object parentElement, final Object element) {
			RowData row = null;
			Cargo cargo = null;
			CargoType cargoType = null;
			if (element instanceof RowData) {
				row = (RowData) element;
				cargo = row.getCargo();
				if (cargo != null) {
					cargoType = cargo.getCargoType();
				}
			}
			switch (option) {
			case NONE:
				return true;
			case SHIPPED:
				return cargo != null && cargoType == CargoType.FLEET;
			case NON_SHIPPED:
				return cargo != null && cargoType != CargoType.FLEET;
			case DES:
				return cargo != null && cargoType == CargoType.DES;
			case FOB:
				return cargo != null && cargoType == CargoType.FOB;
			}
			return false;
		}
	}

	private enum TimeFilterType {
		NONE, YEARMONTH, PROMPT, CURRENT
	}

	private class TimePeriodFilter extends ViewerFilter {
		private TimeFilterType type = TimeFilterType.NONE;
		private YearMonth choice = null;
		private int promptMonth = 3;

		@Override
		public boolean select(final Viewer viewer, final Object parentElement, final Object element) {
			if (type == TimeFilterType.NONE)
				return true;
			if (element instanceof RowData) {
				final RowData row = (RowData) element;
				switch (type) {
				case YEARMONTH:
					return checkYearMonth(row, choice);
				case PROMPT:
					return checkPrompt(row, promptMonth);
				case CURRENT:
					return checkCurrent(row);
				}
			}
			return false;
		}

		private boolean checkYearMonth(final RowData row, final YearMonth choice) {
			LocalDate start = null;
			LocalDate end = null;
			final LoadSlot ls = row.getLoadSlot();
			if (ls != null) {
				start = ls.getSchedulingTimeWindow().getStart().toLocalDate();
				end = ls.getSchedulingTimeWindow().getEnd().toLocalDate();
			}
			if ((start != null) && (end != null)) {
				final YearMonth yms = YearMonth.from(start);
				final YearMonth yme = YearMonth.from(end);
				if ((yms.equals(choice)) || (yme.equals(choice))) {
					return true;
				}
			}
			final DischargeSlot ds = row.getDischargeSlot();
			if (ds != null) {
				start = ds.getSchedulingTimeWindow().getStart().toLocalDate();
				end = ds.getSchedulingTimeWindow().getEnd().toLocalDate();
			}
			if ((start != null) && (end != null)) {
				final YearMonth yms = YearMonth.from(start);
				final YearMonth yme = YearMonth.from(end);
				if ((yms.equals(choice)) || (yme.equals(choice))) {
					return true;
				}
			}
			return false;
		}

		private boolean checkPrompt(final RowData row, final int month) {
			final boolean result = false;
			LocalDate start = null;
			LocalDate end = null;
			final LocalDate today = LocalDate.now();
			final LocalDate prompt = today.plusMonths(month);
			final LoadSlot ls = row.getLoadSlot();
			if (ls != null) {
				start = ls.getSchedulingTimeWindow().getStart().toLocalDate();
				end = ls.getSchedulingTimeWindow().getEnd().toLocalDate();
			}
			if (start != null && end != null) {
				if (start.isAfter(today) && start.isBefore(prompt)) {
					return true;
				}
				if (end.isAfter(today) && end.isBefore(prompt)) {
					return true;
				}
			}
			final DischargeSlot ds = row.getDischargeSlot();
			if (ds != null) {
				start = ds.getSchedulingTimeWindow().getStart().toLocalDate();
				end = ds.getSchedulingTimeWindow().getEnd().toLocalDate();
			}
			if (start != null && end != null) {
				if (start.isAfter(today) && start.isBefore(prompt)) {
					return true;
				}
				if (end.isAfter(today) && end.isBefore(prompt)) {
					return true;
				}
			}

			return result;
		}
		
		private boolean checkCurrent(final RowData row) {
			final boolean result = false;
			LocalDate start = null;
			LocalDate end = null;
			final LocalDate today = LocalDate.now();
			final LoadSlot ls = row.getLoadSlot();
			if (ls != null) {
				start = ls.getSchedulingTimeWindow().getStart().toLocalDate();
				end = ls.getSchedulingTimeWindow().getEnd().toLocalDate();
			}
			if (start != null && end != null) {
				if (start.isAfter(today)) {
					return true;
				}
				if (end.isAfter(today)) {
					return true;
				}
			}
			final DischargeSlot ds = row.getDischargeSlot();
			if (ds != null) {
				start = ds.getSchedulingTimeWindow().getStart().toLocalDate();
				end = ds.getSchedulingTimeWindow().getEnd().toLocalDate();
			}
			if (start != null && end != null) {
				if (start.isAfter(today)) {
					return true;
				}
				if (end.isAfter(today)) {
					return true;
				}
			}

			return result;
		}

	}

	private class FilterMenuAction extends DefaultMenuCreatorAction {
		/**
		 * A holder for a menu list of filter actions on different fields for the trades wiring table.
		 * 
		 * @param label
		 *            The label to show in the UI for this menu.
		 */
		public FilterMenuAction(final String label) {
			super(label);
		}

		/**
		 * Add the filterable fields to the menu for this item.
		 */
		@Override
		protected void populate(final Menu menu) {
			final LNGScenarioModel scenario = (LNGScenarioModel) scenarioEditingLocation.getRootObject();
			final CommercialModel commercialModel = scenario.getReferenceModel().getCommercialModel();
			final FleetModel fleetModel = scenario.getReferenceModel().getFleetModel();

			final EMFPath purchaseContractPath = new RowDataEMFPath(false, CargoModelRowTransformer.Type.LOAD, CargoPackage.Literals.SLOT__CONTRACT);
			final EMFPath salesContractPath = new RowDataEMFPath(false, CargoModelRowTransformer.Type.DISCHARGE, CargoPackage.Literals.SLOT__CONTRACT);
			final EMFPath vesselPath1 = new RowDataEMFPath(false, false, CargoModelRowTransformer.Type.SLOT_OR_CARGO, CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE,
					CargoPackage.Literals.VESSEL_AVAILABILITY__VESSEL);
			final EMFPath vesselPath2 = new RowDataEMFPath(false, false, CargoModelRowTransformer.Type.SLOT_OR_CARGO, CargoPackage.Literals.SLOT__NOMINATED_VESSEL);

			final Action clearAction = new Action("Clear Filter") {
				@Override
				public void run() {
					tradesFilter.clear();
					tradesCargoFilter.option = CargoFilterOption.NONE;
					shippedCargoFilter.option = ShippedCargoFilterOption.NONE;
					monthFilter.type = TimeFilterType.NONE;
					extraFiltersProvider.clear();
					scenarioViewer.refresh(false);
				}
			};

			addActionToMenu(clearAction, menu);
			addActionToMenu(new FilterAction("Purchase Contracts", commercialModel, CommercialPackage.Literals.COMMERCIAL_MODEL__PURCHASE_CONTRACTS, purchaseContractPath), menu);
			addActionToMenu(new FilterAction("Sale Contracts", commercialModel, CommercialPackage.Literals.COMMERCIAL_MODEL__SALES_CONTRACTS, salesContractPath), menu);
			addActionToMenu(new FilterAction("Vessels", fleetModel, FleetPackage.Literals.FLEET_MODEL__VESSELS, new EMFMultiPath(true, vesselPath1, vesselPath2)), menu);

			final DefaultMenuCreatorAction dmca = new DefaultMenuCreatorAction("Open/Cargo") {

				@Override
				protected void populate(final Menu menu) {
					final Action cargoesAction = new Action("Cargoes only") {
						@Override
						public void run() {
							tradesCargoFilter.option = CargoFilterOption.CARGO;
							scenarioViewer.refresh(false);
						}
					};
					final Action openAction = new Action("Open only") {
						@Override
						public void run() {
							tradesCargoFilter.option = CargoFilterOption.OPEN;
							scenarioViewer.refresh(false);
						}
					};
					final Action longsAction = new Action("Longs") {
						@Override
						public void run() {
							tradesCargoFilter.option = CargoFilterOption.LONG;
							scenarioViewer.refresh(false);
						}
					};
					final Action shortsAction = new Action("Shorts") {
						@Override
						public void run() {
							tradesCargoFilter.option = CargoFilterOption.SHORT;
							scenarioViewer.refresh(false);
						}
					};
					addActionToMenu(cargoesAction, menu);
					addActionToMenu(openAction, menu);
					addActionToMenu(longsAction, menu);
					addActionToMenu(shortsAction, menu);
				}
			};
			addActionToMenu(dmca, menu);

			final DefaultMenuCreatorAction dmcaShippedCargos = new DefaultMenuCreatorAction("Shipped") {

				@Override
				protected void populate(final Menu menu) {
					final Action cargoesAction = new Action("Shipped") {
						@Override
						public void run() {
							shippedCargoFilter.option = ShippedCargoFilterOption.SHIPPED;
							scenarioViewer.refresh(false);
						}
					};
					final Action openAction = new Action("Non Shipped") {
						@Override
						public void run() {
							shippedCargoFilter.option = ShippedCargoFilterOption.NON_SHIPPED;
							scenarioViewer.refresh(false);
						}
					};
					final Action longsAction = new Action("FOB") {
						@Override
						public void run() {
							shippedCargoFilter.option = ShippedCargoFilterOption.FOB;
							scenarioViewer.refresh(false);
						}
					};
					final Action shortsAction = new Action("DES") {
						@Override
						public void run() {
							shippedCargoFilter.option = ShippedCargoFilterOption.DES;
							scenarioViewer.refresh(false);
						}
					};
					addActionToMenu(cargoesAction, menu);
					addActionToMenu(openAction, menu);
					addActionToMenu(longsAction, menu);
					addActionToMenu(shortsAction, menu);
				}
			};
			addActionToMenu(dmcaShippedCargos, menu);

			final DefaultMenuCreatorAction dmcaTimePeriod = new DefaultMenuCreatorAction("Period") {

				@Override
				protected void populate(final Menu menu) {
					
					final Action currentAction = new Action("Today onwards") {
						@Override
						public void run() {
							monthFilter.type = TimeFilterType.CURRENT;
							scenarioViewer.refresh(false);
						}
					};
					addActionToMenu(currentAction, menu);

					final Action promptAction = new Action("Today +3m") {
						@Override
						public void run() {
							monthFilter.type = TimeFilterType.PROMPT;
							monthFilter.promptMonth = 3;
							scenarioViewer.refresh(false);
						}
					};
					addActionToMenu(promptAction, menu);
					
					new MenuItem(menu, SWT.SEPARATOR);

					buildMonth(earliest, latest, menu);
				}

				private void buildMonth(final LocalDate start, final LocalDate end, final Menu menu) {
					final YearMonth yms = YearMonth.from(start);
					final int firstYear = yms.getYear();
					final YearMonth yme = YearMonth.from(end);
					int i = 0;

					while (!yme.isBefore(yms.plusMonths(i))) {
						final YearMonth ymc = yms.plusMonths(i);
						if (i != 0 && ymc.getMonthValue() == 1) {
							new MenuItem(menu, SWT.SEPARATOR);
						}
						final Action fooAction = new Action(formatMe(ymc, firstYear != ymc.getYear())) {
							@Override
							public void run() {
								monthFilter.choice = ymc;
								monthFilter.type = TimeFilterType.YEARMONTH;
								scenarioViewer.refresh(false);
							}
						};

						addActionToMenu(fooAction, menu);
						i++;
					}
				}

				private String formatMe(final YearMonth val, final boolean showYear) {
					String result = String.format("%s", val.getMonth().getDisplayName(TextStyle.SHORT, Locale.getDefault()));
					if (showYear) {
						result += String.format(" '%d", (val.getYear() % 100));
					}
					return result;
				}

			};

			addActionToMenu(dmcaTimePeriod, menu);

			if (extraFiltersProvider != null) {
				for (final DefaultMenuCreatorAction edmca : extraFiltersProvider.getExtraMenuActions(scenarioViewer)) {
					addActionToMenu(edmca, menu);
				}
			}

			// TODO : Consider using TradesBasedFilterHandler!
		}
	}

	private class FilterAction extends DefaultMenuCreatorAction {
		private final EObject sourceObject;
		private final EStructuralFeature sourceFeature;
		private final IEMFPath filterPath;

		/**
		 * An action which updates the filter on the trades wiring table and refreshes the table.
		 * 
		 * @param label
		 *            The label to associate with this action (the feature from the cargo row it represents).
		 * @param sourceObject
		 *            The source object in the EMF model which holds the list of possible values for the filter.
		 * @param sourceFeature
		 *            The EMF feature of the source object where the list of possible values resides.
		 * @param filterPath
		 *            The path within a cargo row object of the field which the table is being filtered on.
		 */
		public FilterAction(final String label, final EObject sourceObject, final EStructuralFeature sourceFeature, final IEMFPath filterPath) {
			super(label);
			this.sourceObject = sourceObject;
			this.sourceFeature = sourceFeature;
			this.filterPath = filterPath;
		}

		/**
		 * Add actions to the submenu associated with this action.
		 */
		@Override
		protected void populate(final Menu menu) {
			// Get the labels to populate the menu from the source object in the EMF model
			final EList<NamedObject> values = (EList<NamedObject>) sourceObject.eGet(sourceFeature);
			final List<NamedObject> copiedValues = new LinkedList(values);
			Collections.sort(copiedValues, (a, b) -> {
				if (a != null && a.getName() != null && b != null) {
					return a.getName().compareTo(b.getName());
				}
				return 0;
			});

			if (copiedValues.size() > 15) {
				int counter = 0;
				String firstEntry = "";
				String lastEntry = "";
				final List<Action> collection = new LinkedList<Action>();
				for (final NamedObject value : copiedValues) {
					if (counter == 0) {
						firstEntry = value.getName();
					} else {
						lastEntry = value.getName();
					}
					final Action action = createAction(value);
					collection.add(action);
					counter++;

					if (counter == 15) {
						addListOfActionsToMenu(menu, firstEntry, lastEntry, collection);
						counter = 0;
					}
				}
				if (counter > 0) {
					addListOfActionsToMenu(menu, firstEntry, lastEntry, collection);
				}
			} else {
				// Show the list of labels (one for each item in the source object feature)
				for (final NamedObject value : copiedValues) {

					final Action action = createAction(value);
					addActionToMenu(action, menu);
				}
			}

		}

		private Action createAction(final NamedObject value) {
			// WEIRD: action returns wrong value from isChecked() so set the checked value
			// here
			// A label is checked if it is the value already selected in the trades filter
			final boolean checked = tradesFilter.getFilterValue(filterPath) == value;
			final Action action = new Action(value.getName(), IAction.AS_RADIO_BUTTON) {
				@Override
				public void run() {
					// When we select a checked option, we want to turn the filter off
					final EObject newValue = (checked) ? null : value;
					tradesFilter.setFilterValue(filterPath, newValue);
					scenarioViewer.refresh(false);
				}
			};
			action.setChecked(checked);
			return action;
		}

		private void addListOfActionsToMenu(final Menu menu, String firstEntry, String lastEntry, final List<Action> collection) {
			final String title = String.format("%s ... %s", firstEntry, lastEntry);

			final DefaultMenuCreatorAction dmca = new DefaultMenuCreatorAction(title) {
				final List<Action> local = new LinkedList<>(collection);

				@Override
				protected void populate(Menu menu) {
					for (final Action a : this.local) {
						addActionToMenu(a, menu);
					}
				}
			};
			addActionToMenu(dmca, menu);
			collection.clear();
		}

	}

	private class AddAction extends DefaultMenuCreatorAction {

		public AddAction(final String label) {
			super(label);
		}

		/**
		 * Subclasses should fill their menu with actions here.
		 * 
		 * @param menu
		 *            the menu which is about to be displayed
		 */
		protected void populate(final Menu menu) {
			{
				final DuplicateAction result = new DuplicateAction(getJointModelEditorPart());
				// Translate into real objects, not just row object!
				final List<Object> selectedObjects = new LinkedList<>();
				if (scenarioViewer.getSelection() instanceof IStructuredSelection) {
					final IStructuredSelection structuredSelection = (IStructuredSelection) scenarioViewer.getSelection();

					final Iterator<?> itr = structuredSelection.iterator();
					while (itr.hasNext()) {
						final Object o = itr.next();
						if (o instanceof RowData) {
							final RowData rowData = (RowData) o;
							// TODO: Check logic, a row may contain two distinct items
							if (rowData.cargo != null) {
								selectedObjects.add(rowData.cargo);
								continue;
							}
							if (rowData.loadSlot != null) {
								selectedObjects.add(rowData.loadSlot);
							}
							if (rowData.dischargeSlot != null) {
								selectedObjects.add(rowData.dischargeSlot);
							}
						}
					}
				}

				result.selectionChanged(new SelectionChangedEvent(scenarioViewer, new StructuredSelection(selectedObjects)));
				addActionToMenu(result, menu);
			}

			final CargoModel cargoModel = getScenarioModel().getCargoModel();
			final CommandStack commandStack = scenarioEditingLocation.getEditingDomain().getCommandStack();

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
					@Override
					public void run() {

						final List<Command> setCommands = new LinkedList<>();

						final Cargo newCargo = cec.createNewCargo(setCommands, cargoModel);

						final LoadSlot newLoad = cec.createNewLoad(setCommands, cargoModel, false);
						initialiseSlot(newLoad, true, referenceRowData);

						final DischargeSlot newDischarge = cec.createNewDischarge(setCommands, cargoModel, false);
						initialiseSlot(newDischarge, false, referenceRowData);

						newLoad.setCargo(newCargo);
						newDischarge.setCargo(newCargo);

						newCargo.setAllowRewiring(true);
						final CompoundCommand cmd = new CompoundCommand("Cargo");
						setCommands.forEach(cmd::append);

						scenarioEditingLocation.getEditingDomain().getCommandStack().execute(cmd);
					}
				};
				addActionToMenu(newLoad, menu);
			}
			{
				final Action newLoad = new Action("FOB Purchase") {
					@Override
					public void run() {

						final List<Command> setCommands = new LinkedList<>();

						final LoadSlot newLoad = cec.createNewLoad(setCommands, cargoModel, false);
						initialiseSlot(newLoad, true, referenceRowData);

						final CompoundCommand cmd = new CompoundCommand("FOB Purchase");
						setCommands.forEach(cmd::append);

						commandStack.execute(cmd);
						DetailCompositeDialogUtil.editSingleObjectWithUndoOnCancel(getJointModelEditorPart(), newLoad, commandStack.getMostRecentCommand());
					}
				};
				addActionToMenu(newLoad, menu);
			}
			{
				final Action newDESPurchase = new Action("DES Purchase") {
					@Override
					public void run() {

						final List<Command> setCommands = new LinkedList<>();

						final LoadSlot newLoad = cec.createNewLoad(setCommands, cargoModel, true);
						initialiseSlot(newLoad, true, referenceRowData);

						final CompoundCommand cmd = new CompoundCommand("DES Purchase");
						setCommands.forEach(cmd::append);
						commandStack.execute(cmd);
						DetailCompositeDialogUtil.editSingleObjectWithUndoOnCancel(getJointModelEditorPart(), newLoad, commandStack.getMostRecentCommand());
					}
				};
				addActionToMenu(newDESPurchase, menu);
			}
			{
				final Action newDischarge = new Action("DES Sale") {
					@Override
					public void run() {

						final List<Command> setCommands = new LinkedList<>();

						final DischargeSlot newDischarge = cec.createNewDischarge(setCommands, cargoModel, false);
						initialiseSlot(newDischarge, false, referenceRowData);

						final CompoundCommand cmd = new CompoundCommand("DES Sale");
						setCommands.forEach(cmd::append);

						commandStack.execute(cmd);
						DetailCompositeDialogUtil.editSingleObjectWithUndoOnCancel(getJointModelEditorPart(), newDischarge, commandStack.getMostRecentCommand());
					}
				};

				addActionToMenu(newDischarge, menu);
			}
			{
				final Action newFOBSale = new Action("FOB Sale") {
					@Override
					public void run() {

						final List<Command> setCommands = new LinkedList<>();

						final DischargeSlot newDischarge = cec.createNewDischarge(setCommands, cargoModel, true);
						initialiseSlot(newDischarge, false, referenceRowData);

						final CompoundCommand cmd = new CompoundCommand("FOB Sale");
						setCommands.forEach(cmd::append);

						commandStack.execute(cmd);
						DetailCompositeDialogUtil.editSingleObjectWithUndoOnCancel(getJointModelEditorPart(), newDischarge, commandStack.getMostRecentCommand());
					}
				};
				addActionToMenu(newFOBSale, menu);
			}

			if (LicenseFeatures.isPermitted("features:complex-cargo")) {
				final ComplexCargoAction newComplexCargo = new ComplexCargoAction("Complex Cargo", scenarioEditingLocation, viewer.getControl().getShell());
				addActionToMenu(newComplexCargo, menu);
			}
		}

		private final void initialiseSlot(final Slot<?> newSlot, final boolean isLoad, final RowData referenceRowData) {
			newSlot.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), EcoreUtil.generateUUID());
			newSlot.setOptional(false);
			newSlot.setName("");
			// Set window so that via default sorting inserts new slot at current table
			// position
			if (referenceRowData != null) {
				final Slot<?> primarySortSlot = isLoad ? referenceRowData.loadSlot : referenceRowData.dischargeSlot;
				final Slot<?> secondarySortSlot = isLoad ? referenceRowData.dischargeSlot : referenceRowData.loadSlot;
				if (primarySortSlot != null) {
					newSlot.setWindowStart(primarySortSlot.getWindowStart());
					newSlot.setPort(primarySortSlot.getPort());
				} else if (secondarySortSlot != null) {
					newSlot.setWindowStart(secondarySortSlot.getWindowStart());
				}
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
						if (item2 != null && vScroll != 0) {
							// Almost! it will show part of the item, but it may be obscured by the h.scroll
							// bar
							getScenarioViewer().getGrid().showItem(item2);
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

	private class CreateStripMenuAction extends LockableAction implements IMenuCreator {

		private Menu lastMenu;

		public CreateStripMenuAction(final String label) {
			super(label, IAction.AS_DROP_DOWN_MENU);
			setImageDescriptor(LngUIActivator.getDefault().getImageRegistry().getDescriptor(ImageConstants.IMAGE_DUPLICATE));
			setDisabledImageDescriptor(LngUIActivator.getDefault().getImageRegistry().getDescriptor(ImageConstants.IMAGE_DUPLICATE_DISABLED));
		}

		@Override
		public void dispose() {
			if ((lastMenu != null) && (!lastMenu.isDisposed())) {
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

	private class CreateStripAction extends LockableAction {

		private final CreateStripDialog.StripType stripType;

		protected CreateStripAction(final String text, final StripType stripType) {
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

						Slot<?> selectedObject = null;
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

								if (o instanceof Slot<?>) {
									selectedObject = (Slot<?>) o;
									break;
								}

							}
						}

						final LNGScenarioModel scenarioModel = (LNGScenarioModel) rootObject;
						final CreateStripDialog d = new CreateStripDialog(scenarioEditingLocation.getShell(), scenarioEditingLocation, stripType, selectedObject) {
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

	/**
	 * Return an action which duplicates the selection
	 * 
	 * @return
	 */
	@Override
	protected Action createDuplicateAction() {
		return new CreateStripMenuAction("Create Strip");
	}
}