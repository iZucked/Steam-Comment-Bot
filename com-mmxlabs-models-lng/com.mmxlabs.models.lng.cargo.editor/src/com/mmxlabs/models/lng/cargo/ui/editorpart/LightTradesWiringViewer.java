/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.time.LocalDate;
import java.time.YearMonth;
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
import java.util.Objects;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.IElementComparer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.DefaultCellRenderer;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridCellRenderer;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.menus.IMenuService;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.service.event.EventHandler;

import com.mmxlabs.common.Equality;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.editor.PreferenceConstants;
import com.mmxlabs.models.lng.cargo.presentation.CargoEditorPlugin;
import com.mmxlabs.models.lng.cargo.ui.editorpart.CargoModelRowTransformer.GroupData;
import com.mmxlabs.models.lng.cargo.ui.editorpart.CargoModelRowTransformer.RootData;
import com.mmxlabs.models.lng.cargo.ui.editorpart.CargoModelRowTransformer.RowData;
import com.mmxlabs.models.lng.cargo.ui.editorpart.CargoModelRowTransformer.RowDataEMFPath;
import com.mmxlabs.models.lng.cargo.ui.editorpart.CargoModelRowTransformer.Type;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.CargoEditingCommands;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.CargoEditorMenuHelper;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.DefaultMenuCreatorAction;
import com.mmxlabs.models.lng.cargo.ui.editorpart.trades.ITradesTableContextMenuExtension;
import com.mmxlabs.models.lng.cargo.ui.editorpart.trades.TradesTableContextMenuExtensionUtil;
import com.mmxlabs.models.lng.cargo.ui.util.TimeWindowHelper;
import com.mmxlabs.models.lng.cargo.util.CargoEditorFilterUtils;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.SlotContractParams;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.MarketAllocation;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewer;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.ICommandHandler;
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
import com.mmxlabs.rcp.common.CommonImages;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.rcp.common.CommonImages.IconMode;
import com.mmxlabs.rcp.common.CommonImages.IconPaths;
import com.mmxlabs.rcp.common.actions.PackGridTreeColumnsAction;
import com.mmxlabs.rcp.common.dnd.BasicDragSource;
import com.mmxlabs.rcp.common.handlers.TodayHandler;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
import com.mmxlabs.scenario.service.model.manager.ModelReference;

/**
 * Tabular editor displaying cargoes and slots with a custom wiring editor. This implementation is "stupid" in that any changes to the data cause a full update. This has the disadvantage of loosing
 * the current ordering of items. Each row is a cargo. Changing the wiring will re-order slots. The {@link CargoWiringComposite} based view only re-orders slots when requested permitting the original
 * (or at least the wiring at time of opening the editor) wiring to be seem via rows and the current wiring via the wire lines.
 * 
 * 
 * @author Simon Goodall
 * 
 */
public class LightTradesWiringViewer extends ScenarioTableViewerPane {

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

	private GridViewerColumn assignmentColumn;

	private IPropertyChangeListener propertyChangeListener;
	private final Set<String> filtersOpenContracts = new HashSet<>();
	private IExtraFiltersProvider extraFiltersProvider;

	public LightTradesWiringViewer(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation scenarioEditingLocation, final IActionBars actionBars) {
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
				
				setTitle("Cargoes", PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_DEF_VIEW));

				init(new ITreeContentProvider() {

					@Override
					public void dispose() {

					}

					@Override
					public Object[] getElements(final Object inputElement) {

						final CargoModel cargoModel = getScenarioModel().getCargoModel();
						final ScheduleModel scheduleModel = getScenarioModel().getScheduleModel();

						final RootData root = setCargoes(cargoModel, scheduleModel, referenceRootData);

						LightTradesWiringViewer.this.rootData = root;

						if (LightTradesWiringViewer.this.referenceRootData == null) {
							LightTradesWiringViewer.this.referenceRootData = root;
						}

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

			@Override
			public void menuDetected(final MenuDetectEvent e) {

				if (locked) {
					return;
				}

				final IStructuredSelection selection = (IStructuredSelection) getScenarioViewer().getSelection();

				if (!selection.isEmpty()) {
					final Set<Cargo> cargoes = new HashSet<>();
					for (final Object item : selection.toList()) {
						RowData row = (RowData) item;
						final Cargo cargo = row.cargo;
						if (cargo != null) {
							cargoes.add(cargo);
						}
					}
					populateSelectionMenu(cargoes);
				}
			}

			private void populateSelectionMenu(final Set<Cargo> cargoes) {
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
				
				menuHelper.createLightEditorMenuListener(mgr, cargoes);

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

				return Objects.equals(a, b);
			}

			private Set<Object> getObjectSet(final Object a) {
				final Set<Object> aSet = new HashSet<>();
				if (a instanceof RowData rd) {
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

		toolbar.add(new GroupMarker(VIEW_GROUP));
		toolbar.appendToGroup(VIEW_GROUP, new PackGridTreeColumnsAction(scenarioViewer));

		final Action filterAction = new FilterMenuAction("Filter");
		filterAction.setImageDescriptor(CommonImages.getImageDescriptor(IconPaths.Filter, IconMode.Enabled));
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

		if (actionBars != null) {
			actionBars.updateActionBars();
		}

		toolbar.update(true);

		final CargoPackage pkg = CargoPackage.eINSTANCE;
		final IReferenceValueProviderProvider provider = scenarioEditingLocation.getReferenceValueProviderCache();
		final ICommandHandler commandHandler = scenarioEditingLocation.getDefaultCommandHandler();
		{
			final AssignmentManipulator assignmentManipulator = new AssignmentManipulator(scenarioEditingLocation);
			final RowDataEMFPath assignmentPath = new RowDataEMFPath(true, Type.SLOT_OR_CARGO);
			assignmentColumn = addTradesColumn(null, "Vessel", new ReadOnlyManipulatorWrapper<>(assignmentManipulator), assignmentPath);
		}
		{
			final BasicAttributeManipulator manipulator = new BasicAttributeManipulator(CargoPackage.eINSTANCE.getCargoModel_Cargoes(), commandHandler) {
				@Override
				public String render(final Object object) {
					if (object instanceof Cargo) {
						if (scenarioEditingLocation != null && scenarioEditingLocation.getRootObject() instanceof LNGScenarioModel) {
							final var lngScenario = (LNGScenarioModel) scenarioEditingLocation.getRootObject();
							final var cargoModel = lngScenario.getCargoModel();
							if (cargoModel.getCargoesForExposures().contains(object)) {
								return "Y";
							}
						}
					}
					return "N";
				}
			};
			final RowDataEMFPath assignmentPath = new RowDataEMFPath(false, Type.CARGO);
			addTradesColumn(null, "Exposure", manipulator, assignmentPath);
		}
		{
			final BasicAttributeManipulator manipulator = new BasicAttributeManipulator(CargoPackage.eINSTANCE.getCargoModel_Cargoes(), commandHandler) {
				@Override
				public String render(final Object object) {
					if (object instanceof Cargo) {
						if (scenarioEditingLocation != null && scenarioEditingLocation.getRootObject() instanceof LNGScenarioModel) {
							final var lngScenario = (LNGScenarioModel) scenarioEditingLocation.getRootObject();
							final var cargoModel = lngScenario.getCargoModel();
							if (cargoModel.getCargoesForHedging().contains(object)) {
								return "Y";
							}
						}
					}
					return "N";
				}
			};
			final RowDataEMFPath assignmentPath = new RowDataEMFPath(false, Type.CARGO);
			addTradesColumn(null, "Hedging", manipulator, assignmentPath);
		}
		{
			final BasicAttributeManipulator manipulator = new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getNamedObject_Name(), commandHandler);
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

		addTradesColumn(loadColumns, "Port", new SingleReferenceManipulator(pkg.getSlot_Port(), provider, commandHandler), new RowDataEMFPath(false, Type.LOAD));
		addTradesColumn(loadColumns, "Buy At", new ContractManipulator(provider, commandHandler), new RowDataEMFPath(false, Type.LOAD));
		
		final GridViewerColumn loadVol = addTradesColumn(loadColumns, "Vol", new VolumeAttributeManipulator(pkg.getSlot_MaxQuantity(), commandHandler), new RowDataEMFPath(false, Type.LOAD));
		loadVol.getColumn().setHeaderTooltip("in m³ or mmBtu");

		final GridViewerColumn loadDateColumn = addTradesColumn(loadColumns, "Window", new LocalDateAttributeManipulator(pkg.getSlot_WindowStart(), commandHandler) {
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

		final GridViewerColumn wiringColumn = addWiringColumn();

		final GridViewerColumn dischargeDateColumn = addTradesColumn(dischargeColumns, "Window", new LocalDateAttributeManipulator(pkg.getSlot_WindowStart(), commandHandler) {

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


		addTradesColumn(dischargeColumns, "Sell At", new ContractManipulator(provider, commandHandler), new RowDataEMFPath(false, Type.DISCHARGE));

		addTradesColumn(dischargeColumns, "Vol", new VolumeAttributeManipulator(pkg.getSlot_MaxQuantity(), commandHandler), new RowDataEMFPath(false, Type.DISCHARGE)).getColumn()
				.setHeaderTooltip("in m³ or mmBtu");
		addTradesColumn(dischargeColumns, "Port", new SingleReferenceManipulator(pkg.getSlot_Port(), provider, commandHandler), new RowDataEMFPath(false, Type.DISCHARGE));
		{
			final BasicAttributeManipulator manipulator = new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getNamedObject_Name(), commandHandler);
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
		{
			final BasicAttributeManipulator manipulator = new BasicAttributeManipulator(CargoPackage.eINSTANCE.getCargoModel_Cargoes(), commandHandler) {
				@Override
				public String render(final Object object) {
					if (object instanceof Cargo) {
						if (scenarioEditingLocation != null && scenarioEditingLocation.getRootObject() instanceof LNGScenarioModel) {
							final var lngScenario = (LNGScenarioModel) scenarioEditingLocation.getRootObject();
							final var cargoModel = lngScenario.getCargoModel();
							if (cargoModel.getCargoesForHedging().contains(object)) {
								return "Y";
							}
						}
					}
					return "N";
				}
			};
			final RowDataEMFPath assignmentPath = new RowDataEMFPath(false, Type.CARGO);
			addTradesColumn(null, "Hedging", manipulator, assignmentPath);
		}
		{
			final BasicAttributeManipulator manipulator = new BasicAttributeManipulator(CargoPackage.eINSTANCE.getCargoModel_Cargoes(), commandHandler) {
				@Override
				public String render(final Object object) {
					if (object instanceof Cargo) {
						if (scenarioEditingLocation != null && scenarioEditingLocation.getRootObject() instanceof LNGScenarioModel) {
							final var lngScenario = (LNGScenarioModel) scenarioEditingLocation.getRootObject();
							final var cargoModel = lngScenario.getCargoModel();
							if (cargoModel.getCargoesForExposures().contains(object)) {
								return "Y";
							}
						}
					}
					return "N";
				}
			};
			final RowDataEMFPath assignmentPath = new RowDataEMFPath(false, Type.CARGO);
			addTradesColumn(null, "Exposure", manipulator, assignmentPath);
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

		wiringDiagram = new TradesWiringDiagram(getScenarioViewer().getGrid()) {

			@Override
			protected void wiringChanged(final Map<RowData, RowData> newWiring, final boolean ctrlPressed) {
				if (locked) {
					return;
				}
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
			
			@Override
			public void mouseDown(final MouseEvent e) {
			}
			
			@Override
			public void mouseUp(final MouseEvent e) {
			}
			
			@Override
			public void mouseMove(final MouseEvent e) {
			}
		};
		wiringDiagram.setSortOrder(rootData, sortedIndices, reverseSortedIndices);

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
		return this.addTradesColumn(null, columnName, new ReadOnlyManipulatorWrapper<>(manipulator), path);
	}

	private <T extends ICellManipulator & ICellRenderer> GridViewerColumn addTradesColumn(final Set<GridColumn> group, final String columnName, final T manipulator, final EMFPath path) {
		final GridViewerColumn col = getScenarioViewer().addColumn(columnName, manipulator, new ReadOnlyManipulatorWrapper<>(manipulator), path);
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

	private class TradesCargoFilter extends ViewerFilter {
		private CargoEditorFilterUtils.CargoFilterOption option = CargoEditorFilterUtils.CargoFilterOption.NONE;

		@Override
		public boolean select(final Viewer viewer, final Object parentElement, final Object element) {
			if (element instanceof RowData) {
				final RowData row = (RowData) element;
				return CargoEditorFilterUtils.cargoTradesFilter(this.option, row.getCargo(), row.getLoadSlot(), row.getDischargeSlot(), filtersOpenContracts);
			} else {
				return false;
			}
		}
	}

	private class ShippedCargoFilter extends ViewerFilter {
		private CargoEditorFilterUtils.ShippedCargoFilterOption option = CargoEditorFilterUtils.ShippedCargoFilterOption.NONE;

		@Override
		public boolean select(final Viewer viewer, final Object parentElement, final Object element) {
			if (element instanceof RowData) {
				return CargoEditorFilterUtils.shippedCargoFilter(this.option, ((RowData) element).getCargo());
			} else {
				return false;
			}
		}
	}

	private class TimePeriodFilter extends ViewerFilter {
		private CargoEditorFilterUtils.TimeFilterOption option = CargoEditorFilterUtils.TimeFilterOption.NONE;
		private YearMonth choice = null;
		private int promptMonth = 3;

		@Override
		public boolean select(final Viewer viewer, final Object parentElement, final Object element) {
			if (element instanceof RowData) {
				final RowData row = (RowData) element;
				return CargoEditorFilterUtils.timePeriodFilter(option, row.getLoadSlot(), row.getDischargeSlot(), choice, promptMonth);
			} else {
				return false;
			}
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
					tradesCargoFilter.option = CargoEditorFilterUtils.CargoFilterOption.NONE;
					shippedCargoFilter.option = CargoEditorFilterUtils.ShippedCargoFilterOption.NONE;
					monthFilter.option = CargoEditorFilterUtils.TimeFilterOption.NONE;
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
							tradesCargoFilter.option = CargoEditorFilterUtils.CargoFilterOption.CARGO;
							scenarioViewer.refresh(false);
						}
					};
					final Action openAction = new Action("Open only") {
						@Override
						public void run() {
							tradesCargoFilter.option = CargoEditorFilterUtils.CargoFilterOption.OPEN;
							scenarioViewer.refresh(false);
						}
					};
					final Action longsAction = new Action("Longs") {
						@Override
						public void run() {
							tradesCargoFilter.option = CargoEditorFilterUtils.CargoFilterOption.LONG;
							scenarioViewer.refresh(false);
						}
					};
					final Action shortsAction = new Action("Shorts") {
						@Override
						public void run() {
							tradesCargoFilter.option = CargoEditorFilterUtils.CargoFilterOption.SHORT;
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
							shippedCargoFilter.option = CargoEditorFilterUtils.ShippedCargoFilterOption.SHIPPED;
							scenarioViewer.refresh(false);
						}
					};
					final Action openAction = new Action("Non Shipped") {
						@Override
						public void run() {
							shippedCargoFilter.option = CargoEditorFilterUtils.ShippedCargoFilterOption.NON_SHIPPED;
							scenarioViewer.refresh(false);
						}
					};
					final Action longsAction = new Action("FOB") {
						@Override
						public void run() {
							shippedCargoFilter.option = CargoEditorFilterUtils.ShippedCargoFilterOption.FOB;
							scenarioViewer.refresh(false);
						}
					};
					final Action shortsAction = new Action("DES") {
						@Override
						public void run() {
							shippedCargoFilter.option = CargoEditorFilterUtils.ShippedCargoFilterOption.DES;
							scenarioViewer.refresh(false);
						}
					};
					final Action nominalsAction = new Action("Nominal") {
						@Override
						public void run() {
							shippedCargoFilter.option = CargoEditorFilterUtils.ShippedCargoFilterOption.NOMINAL;
							scenarioViewer.refresh(false);
						}
					};
					addActionToMenu(cargoesAction, menu);
					addActionToMenu(openAction, menu);
					addActionToMenu(longsAction, menu);
					addActionToMenu(shortsAction, menu);
					addActionToMenu(nominalsAction, menu);
				}
			};
			addActionToMenu(dmcaShippedCargos, menu);

			final DefaultMenuCreatorAction dmcaTimePeriod = new DefaultMenuCreatorAction("Period") {

				@Override
				protected void populate(final Menu menu) {
					
					final Action currentAction = new Action("Today onwards") {
						@Override
						public void run() {
							monthFilter.option = CargoEditorFilterUtils.TimeFilterOption.CURRENT;
							scenarioViewer.refresh(false);
						}
					};
					addActionToMenu(currentAction, menu);

					final Action promptAction = new Action("Today +3m") {
						@Override
						public void run() {
							monthFilter.option = CargoEditorFilterUtils.TimeFilterOption.PROMPT;
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
								monthFilter.option = CargoEditorFilterUtils.TimeFilterOption.YEARMONTH;
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

}