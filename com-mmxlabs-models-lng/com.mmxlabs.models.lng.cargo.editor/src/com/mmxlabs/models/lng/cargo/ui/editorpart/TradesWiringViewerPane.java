/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
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
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.window.Window;
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
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.menus.IMenuService;
import org.osgi.service.event.EventHandler;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.editor.CargoEditorPlugin;
import com.mmxlabs.models.lng.cargo.editor.PreferenceConstants;
import com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow;
import com.mmxlabs.models.lng.cargo.ui.editorpart.CargoModelRowTransformer.RootData;
import com.mmxlabs.models.lng.cargo.ui.editorpart.CreateStripDialog.StripType;
import com.mmxlabs.models.lng.cargo.ui.editorpart.TradesRowEMFPath.Type;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.CargoEditingCommands;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.CargoEditorMenuHelper;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.DefaultMenuCreatorAction;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.DischargePortFilterAction;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.EMFPathFilterAction;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.EntityFilterAction;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.LoadPortFilterAction;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.OpenCargoFilterAction;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.PeriodFilterAction;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.ShippedCargoFilterAction;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.TrimmedVesselFilterAction;
import com.mmxlabs.models.lng.cargo.ui.editorpart.trades.ITradesTableContextMenuExtension;
import com.mmxlabs.models.lng.cargo.ui.editorpart.trades.TradesTableContextMenuExtensionUtil;
import com.mmxlabs.models.lng.cargo.ui.inlineeditors.GroupedSlotsDialog;
import com.mmxlabs.models.lng.cargo.ui.util.CargoTransferUtil;
import com.mmxlabs.models.lng.cargo.ui.util.TimeWindowHelper;
import com.mmxlabs.models.lng.commercial.BaseEntityBook;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.SlotContractParams;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.EntityProfitAndLoss;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.GroupProfitAndLoss;
import com.mmxlabs.models.lng.schedule.MarketAllocation;
import com.mmxlabs.models.lng.schedule.NonShippedSlotVisit;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.types.DESPurchaseDealType;
import com.mmxlabs.models.lng.types.FOBSaleDealType;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.lng.ui.actions.DuplicateAction;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewer;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.Activator;
import com.mmxlabs.models.ui.date.DateTimeFormatsProvider;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.ICommandHandler;
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
import com.mmxlabs.models.util.emfpath.EMFPath;
import com.mmxlabs.rcp.common.RunnerHelper;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.rcp.common.actions.CopyToClipboardActionFactory;
import com.mmxlabs.rcp.common.actions.LockableAction;
import com.mmxlabs.rcp.common.actions.PackGridTreeColumnsAction;
import com.mmxlabs.rcp.common.dnd.BasicDragSource;
import com.mmxlabs.rcp.common.handlers.TodayHandler;
import com.mmxlabs.rcp.common.menus.LocalMenuHelper;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.ScenarioLock;

/**
 * Tabular editor displaying cargoes and slots with a custom wiring editor. This
 * implementation is "stupid" in that any changes to the data cause a full
 * update. This has the disadvantage of loosing the current ordering of items.
 * Each row is a cargo. Changing the wiring will re-order slots. The
 * {@link CargoWiringComposite} based view only re-orders slots when requested
 * permitting the original (or at least the wiring at time of opening the
 * editor) wiring to be seem via rows and the current wiring via the wire lines.
 * 
 * 
 * @author Simon Goodall
 * 
 */
public class TradesWiringViewerPane extends ScenarioTableViewerPane {

	private static final int LINE_WRAP = 40;
	private static String nl = System.getProperty("line.separator");
	
	private LocalResourceManager localResourceManager;
	private TradesWiringColourScheme colourScheme;

	private Iterable<ITradesTableContextMenuExtension> contextMenuExtensions;

	private TradesWiringDiagram wiringDiagram;

	protected RootData rootData;
	/**
	 * A reference {@link RootData} object. This is used by a
	 * {@link CargoModelRowTransformer} to retain load/discharge row pairings but
	 * allow wires to cross rows. Initially null until the first rootData object is
	 * created. May be "nulled" again to reset state by an action.
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
	private final Image transferImage;
	private final Image notesAndTransferImage;
	private final Image heelCarryImage;

	private IStatusChangedListener statusChangedListener;
	final Set<String> filtersOpenContracts = new HashSet<>();

	private LocalDate earliest = LocalDate.now();
	private LocalDate latest = LocalDate.now();

	private Action resetSortOrder;

	private PromptToolbarEditor promptToolbarEditor;

	private GridViewerColumn assignmentColumn;

	StructuredViewerFilterManager filterManager;

	private IPropertyChangeListener propertyChangeListener;
	IExtraFiltersProvider extraFiltersProvider;

	private final boolean allowLDD = LicenseFeatures.isPermitted(KnownFeatures.FEATURE_COMPLEX_CARGO);

	public TradesWiringViewerPane(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation scenarioEditingLocation, final IActionBars actionBars) {
		super(page, part, scenarioEditingLocation, actionBars);

		final LNGScenarioModel scenarioModel = (LNGScenarioModel) scenarioEditingLocation.getRootObject();
		this.cec = new CargoEditingCommands(scenarioEditingLocation.getEditingDomain(), scenarioModel, ScenarioModelUtil.getCargoModel(scenarioModel),
				ScenarioModelUtil.getCommercialModel(scenarioModel), Activator.getDefault().getModelFactoryRegistry());
		this.menuHelper = new CargoEditorMenuHelper(part.getSite().getShell(), scenarioEditingLocation, scenarioModel);
		notesImage = CargoEditorPlugin.getPlugin().getImage(CargoEditorPlugin.IMAGE_CARGO_NOTES);
		transferImage = CargoEditorPlugin.getPlugin().getImage(CargoEditorPlugin.IMAGE_CARGO_TRANSFER);
		notesAndTransferImage = CargoEditorPlugin.getPlugin().getImage(CargoEditorPlugin.IMAGE_CARGO_NOTES_AND_TRANSFER);
		heelCarryImage = CargoEditorPlugin.getPlugin().getImage(CargoEditorPlugin.IMAGE_CARGO_HEEL_CARRY);

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
		
		this.localResourceManager = null;
		this.colourScheme = null;

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
		localResourceManager = new LocalResourceManager(JFaceResources.getResources(), parent);
		colourScheme = new TradesWiringColourScheme(localResourceManager);
		

		final ScenarioTableViewer scenarioViewer = new TradesWiringViewer(parent, SWT.FULL_SELECTION | SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL, scenarioEditingLocation);

		scenarioViewer.setToolTipProvider(new DefaultToolTipProvider() {

			@Override
			public String getToolTipText(final Object element) {
				final TradesRow tradesRow = (TradesRow) element;
				final LoadSlot ls = tradesRow.getLoadSlot();
				final DischargeSlot ds = tradesRow.getDischargeSlot();

				String lString = getNotes(ls);
				String dString = getNotes(ds);
				if (lString.length() + dString.length() == 0) {
					return null;
				} else {
					return lString + (dString.length() > 0 ? nl + " --- " + nl + dString : "");
				}
			}
			
			private String getNotes(Slot<?> slot) {
				if (slot != null && slot.getNotes() != null) {
					if (slot.getNotes().length() > LINE_WRAP) {
						return wrapString(slot.getNotes(), LINE_WRAP);
					} else {
						return slot.getNotes();
					}
				}
				return "";
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
				if (data instanceof final TradesRow tradesRow) {
					final int idx = rootData.getRows().indexOf(tradesRow);

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
						if (tradesRow.getLoadSlot() != null) {
							createLoadSlotMenuListener(mgr, idx);
						} else {
							createDischargeSlotMenuListener(mgr, idx);
						}
					}
					if (dischargeColumns.contains(column)) {
						if (tradesRow.getDischargeSlot() != null) {
							createDischargeSlotMenuListener(mgr, idx);
						} else if (tradesRow.getLoadSlot() != null) {
							createLoadSlotMenuListener(mgr, idx);
						}
					}

					menu.setVisible(true);
				}
			}

			private void createDischargeSlotMenuListener(final MenuManager mgr, final int idx) {
				final IMenuListener listener = menuHelper.createDischargeSlotMenuListener(rootData.getDischargeSlots(), idx);
				listener.menuAboutToShow(mgr);
				if (contextMenuExtensions != null) {
					final Slot<?> slot = rootData.getDischargeSlots().get(idx);
					for (final ITradesTableContextMenuExtension ext : contextMenuExtensions) {
						ext.contributeToMenu(scenarioEditingLocation, slot, mgr);
					}
				}
			}

			private void createLoadSlotMenuListener(final MenuManager mgr, final int idx) {
				final IMenuListener listener = menuHelper.createLoadSlotMenuListener(rootData.getLoadSlots(), idx);
				listener.menuAboutToShow(mgr);
				if (contextMenuExtensions != null) {
					final Slot<?> slot = rootData.getLoadSlots().get(idx);
					for (final ITradesTableContextMenuExtension ext : contextMenuExtensions) {
						ext.contributeToMenu(scenarioEditingLocation, slot, mgr);
					}
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
				final IStructuredSelection selection = getScenarioViewer().getStructuredSelection();

				if (selection.size() <= 1) {
					populateSingleSelectionMenu(grid.getItem(mousePoint), column);
				} else {
					final Set<Cargo> cargoes = new HashSet<>();
					final Set<LoadSlot> loads = new HashSet<>();
					final Set<DischargeSlot> discharges = new HashSet<>();
					final boolean loadSide = loadColumns.contains(column);
					final boolean dischargeSide = dischargeColumns.contains(column);
					for (final Object item : selection.toList()) {
						final TradesRow tradesRow = (TradesRow) item;
						final Cargo cargo = tradesRow.getCargo();
						if (cargo != null) {
							cargoes.add(cargo);
						}
						if (loadSide && tradesRow.getLoadSlot() != null) {
							loads.add(tradesRow.getLoadSlot());
						}
						if (dischargeSide && tradesRow.getDischargeSlot() != null) {
							discharges.add(tradesRow.getDischargeSlot());
						}
					}
					populateMultipleSelectionMenu(cargoes, loads, discharges, selection, column);
				}
			}

			private void populateMultipleSelectionMenu(final Set<Cargo> cargoes, final Set<LoadSlot> loads, final Set<DischargeSlot> discharges, final IStructuredSelection selection,
					final GridColumn column) {
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

				return Objects.equals(a, b);
			}

			private @Nullable Set<Object> getObjectSet(final Object a) {
				final Set<Object> aSet = new HashSet<>();
				if (a instanceof final TradesRow tradesRow) {
					aSet.add(tradesRow);
					aSet.add(tradesRow.getCargo());
					aSet.add(tradesRow.getLoadSlot());
					aSet.add(tradesRow.getDischargeSlot());
					aSet.remove(null);
				} else if (a instanceof final CargoAllocation cargoAllocation) {
					// aSet.add(cargoAllocation.getInputCargo());
					for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
						aSet.add(slotAllocation.getSlot());
					}
				} else if (a instanceof final MarketAllocation marketAllocation) {
					aSet.add(marketAllocation.getSlot());
				} else if (a instanceof final SlotAllocation slotAllocation) {
					aSet.add(slotAllocation.getSlot());
				} else if (a instanceof final SlotVisit slotVisit) {
					aSet.add(slotVisit.getSlotAllocation().getSlot());
				} else if (a instanceof final NonShippedSlotVisit slotVisit) {
					aSet.add(slotVisit.getSlot());
				} else if (a instanceof Event evt) {
					while (evt != null) {
						if (evt instanceof VesselEventVisit) {
							return null;
						} else if (evt instanceof GeneratedCharterOut) {
							return null;
						} else if (evt instanceof SlotVisit) {
							return getObjectSet(evt);
						} else if (evt instanceof NonShippedSlotVisit) {
							return getObjectSet(evt);
						}
						evt = evt.getPreviousEvent();
					}

				} else if (a instanceof final OpenSlotAllocation osa) {
					final Slot<?> s = osa.getSlot();
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
	 * Break up inputString into lines, then break up those input lines into wrapped
	 * lines lineLength characters long.
	 * 
	 * @param inputString
	 * @param lineLength
	 * @return the wrapped lines
	 */
	protected static String wrapString(final String inputString, final int lineLength) {
		// System.out.println("It's a wrap yo: \n" + inputString);
		String newString = "";
		// Split input string into inputLineStrings, each of which is a list of words
		// which will result in one or more lines of formatted text
		final String[] inputLineStrings = inputString.split(nl);
		for (int i = 0; i < inputLineStrings.length; i++) {
			final String inputLine = inputLineStrings[i];
			// Wrap inputLine
			if (inputLine.length() > lineLength) {
				// Split on spaces and decompose into wrapped lines, adding each word to newLine
				// up to line wrap limit, then starting a newLine.
				final String[] words = inputLine.split(" ");
				String newLine = words[0] + " ";
				for (int j = 1; j < words.length; j++) {
					final String word = words[j];
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

		this.earliest = ScenarioModelUtil.getEarliestScenarioDate(scenarioEditingLocation.getScenarioDataProvider());
		this.latest = ScenarioModelUtil.getLatestScenarioDate(scenarioEditingLocation.getScenarioDataProvider());

		if (statusProvider != null) {
			statusChangedListener = (provider, status) -> RunnerHelper.asyncExec(() -> {
				final CargoModelRowTransformer transformer = new CargoModelRowTransformer(colourScheme);
				final ScenarioTableViewer scenarioViewer2 = getScenarioViewer();
				if (scenarioViewer2 != null) {
					transformer.updateWiringValidity(rootData, scenarioViewer2.getValidationSupport().getValidationErrors());
					wiringDiagram.redraw();
				}
			});

			statusProvider.addStatusChangedListener(statusChangedListener);
		}

		final Grid table = getScenarioViewer().getGrid();

		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		// set up filter manager
		filterManager = new StructuredViewerFilterManager(getScenarioViewer());

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
		addAction.setImageDescriptor(CommonImages.getImageDescriptor(IconPaths.Plus, IconMode.Enabled));
		addAction.setDisabledImageDescriptor(CommonImages.getImageDescriptor(IconPaths.Plus, IconMode.Disabled));
		toolbar.appendToGroup(ADD_REMOVE_GROUP, addAction);

		final Action filterAction = new TradesWiringViewerPaneFilterMenuAction("Filter");
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

		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_GROUPED_OPTIONAL_SLOTS_CONSTRAINTS)) {

			// TODO: Add action directly once there is an icon rather than use the
			// ActionToolbarEditor created just for this class.
			final Action editConstraintsAction = new EditConstraintsAction("Constraints");
			final ActionToolbarEditor ci = new ActionToolbarEditor("grouped-constraints-editor", editConstraintsAction);
			toolbar.appendToGroup(ADD_REMOVE_GROUP, ci);
		}

		if (actionBars != null) {
			actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(), deleteAction);
		}

		// TODO: uncomment lines below, to add relevant actions to trades wiring tool
		// bar

		/*
		 * copyAction = createCopyAction(); if (copyAction != null) {
		 * toolbar.appendToGroup(ADD_REMOVE_GROUP, copyAction); } if (actionBars !=
		 * null) { actionBars.setGlobalActionHandler(ActionFactory.COPY.getId(),
		 * copyAction); }
		 * 
		 * pasteAction = createPasteAction(); if (pasteAction != null) {
		 * toolbar.appendToGroup(ADD_REMOVE_GROUP, pasteAction); } if (actionBars !=
		 * null) { actionBars.setGlobalActionHandler(ActionFactory.COPY.getId(),
		 * pasteAction); }
		 */

		final Action copyToClipboardAction = CopyToClipboardActionFactory.createCopyToClipboardAction(getScenarioViewer());

		if (copyToClipboardAction != null) {
			toolbar.add(copyToClipboardAction);
		}

		// Reset sort order
		{
			resetSortOrder = new Action() {
				@Override
				public void run() {
					TradesWiringViewerPane.this.referenceRootData = null;
					TradesWiringViewerPane.this.viewer.refresh();
					this.setEnabled(false);
				}
			};
			resetSortOrder.setText("Reset Wiring");
			resetSortOrder.setImageDescriptor(CommonImages.getImageDescriptor(IconPaths.ResetWiring, IconMode.Enabled));
			resetSortOrder.setDisabledImageDescriptor(CommonImages.getImageDescriptor(IconPaths.ResetWiring, IconMode.Disabled));
			toolbar.add(resetSortOrder);

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
			final TradesRowEMFPath assignmentPath = new TradesRowEMFPath(true, Type.SLOT_OR_CARGO);
			assignmentColumn = addTradesColumn(null, "Vessel", new ReadOnlyManipulatorWrapper<>(assignmentManipulator), assignmentPath);
		}
		{
			final BasicAttributeManipulator manipulator = new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getNamedObject_Name(), commandHandler);
			final TradesRowEMFPath assignmentPath = new TradesRowEMFPath(false, Type.LOAD);
			final GridViewerColumn idColumn = addTradesColumn(loadColumns, "ID", manipulator, assignmentPath);
			idColumn.setLabelProvider(new EObjectTableViewerColumnProvider(getScenarioViewer(), manipulator, assignmentPath) {

				@Override
				public Image getImage(final Object element) {
					if (element instanceof final TradesRow tradesRow) {
						final Object object = assignmentPath.get(tradesRow);
						if (object instanceof final LoadSlot ls) {
							final boolean slotTransferred = LicenseFeatures.isPermitted(KnownFeatures.FEATURE_TRANSFER_MODEL) //
									&& CargoTransferUtil.isSlotReferencedByTransferRecord(ls, getScenarioModel());
							if (ls.getNotes() != null && !ls.getNotes().isEmpty()) {
								if (slotTransferred) {
									return notesAndTransferImage;
								}
								return notesImage;
							}
							if (slotTransferred) {
								return transferImage;
							}
						}
					}

					return super.getImage(element);
				}
			});
		}

		addTradesColumn(loadColumns, "Port", new SingleReferenceManipulator(pkg.getSlot_Port(), provider, commandHandler), new TradesRowEMFPath(false, Type.LOAD));
		addTradesColumn(loadColumns, "Buy At", new ContractManipulator(provider, commandHandler), new TradesRowEMFPath(false, Type.LOAD));
		if (LicenseFeatures.isPermitted("features:report-load-counterparty")) {
			addTradesColumn(loadColumns, "C/P", new ReadOnlyManipulatorWrapper<BasicAttributeManipulator>(new BasicAttributeManipulator(pkg.getSlot_Counterparty(), commandHandler) {

				@Override
				protected String renderSetValue(final Object container, final Object setValue) {
					return setValue == null ? "" : setValue.toString();
				}

				@Override
				public boolean isValueUnset(final Object object) {
					return false;
				}

			}), new TradesRowEMFPath(false, Type.LOAD));
		}

		addTradesColumn(loadColumns, "Price",
				new ReadOnlyManipulatorWrapper<BasicAttributeManipulator>(new BasicAttributeManipulator(SchedulePackage.eINSTANCE.getSlotAllocation_Price(), commandHandler) {

					@Override
					protected String renderSetValue(final Object container, final Object setValue) {
						if (setValue instanceof final Number num) {
							return String.format("$%.2f", num.doubleValue());
						}
						return super.renderSetValue(container, setValue);
					}
				}), new TradesRowEMFPath(false, Type.LOAD_ALLOCATION));
		final GridViewerColumn loadVol = addTradesColumn(loadColumns, "Vol", new VolumeAttributeManipulator(pkg.getSlot_MaxQuantity(), commandHandler), new TradesRowEMFPath(false, Type.LOAD));
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

				if (object instanceof final TradesRow tradesRow) {
					if (tradesRow.getDischargeSlot() != null) {
						return tradesRow.getDischargeSlot().getWindowStart();
					}
				}

				return super.getComparable(object);
			}
		}, new TradesRowEMFPath(false, Type.LOAD));
		loadDateColumn.getColumn().setData(EObjectTableViewer.COLUMN_SORT_PATH, new TradesRowEMFPath(false, Type.LOAD_OR_DISCHARGE));

		final SchedulePackage sp = SchedulePackage.eINSTANCE;
		if (LicenseFeatures.isPermitted("features:report-arrivaltimes")) {

			final GridViewerColumn loadScheduledDate = addTradesColumn(loadColumns, "Arriving",
					new ReadOnlyManipulatorWrapper<BasicAttributeManipulator>(new BasicAttributeManipulator(sp.getEvent_Start(), commandHandler) {

						/**
						 * FM had to copy the entire code from
						 * com.mmxlabs.lingo.reports.views.formatters AsLocalDateFormatter from
						 * getLocalDate() to not create cross dependency
						 */
						@Override
						public String renderSetValue(final Object owner, final Object object) {
							if (object instanceof final ZonedDateTime zdt) {
								final DateTimeFormatter sdf = DateTimeFormatsProvider.INSTANCE.createDateStringDisplayFormatter();
								return zdt.toLocalDateTime().format(sdf);
							}
							return "";
						}
					}), new TradesRowEMFPath(false, Type.LOAD_ALLOCATION, sp.getSlotAllocation_SlotVisit()));
			loadScheduledDate.getColumn().setData(EObjectTableViewer.COLUMN_SORT_PATH, new TradesRowEMFPath(false, Type.LOAD_ALLOCATION));
		}

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
				if (object instanceof final TradesRow tradesRow) {
					if (tradesRow.getLoadSlot() != null) {
						return tradesRow.getLoadSlot().getWindowStart();
					}
				}
				return super.getComparable(object);
			}
		}, new TradesRowEMFPath(false, Type.DISCHARGE));
		dischargeDateColumn.getColumn().setData(EObjectTableViewer.COLUMN_SORT_PATH, new TradesRowEMFPath(false, Type.DISCHARGE_OR_LOAD));

		if (LicenseFeatures.isPermitted("features:report-arrivaltimes")) {
			final GridViewerColumn dischargeScheduledDate = addTradesColumn(loadColumns, "Arriving",
					new ReadOnlyManipulatorWrapper<BasicAttributeManipulator>(new BasicAttributeManipulator(sp.getEvent_Start(), commandHandler) {

						/**
						 * FM had to copy the entire code from
						 * com.mmxlabs.lingo.reports.views.formatters AsLocalDateFormatter from
						 * getLocalDate() to not create cross dependency
						 */
						@Override
						public String renderSetValue(final Object owner, final Object object) {
							if (object instanceof final ZonedDateTime zdt) {
								final DateTimeFormatter sdf = DateTimeFormatsProvider.INSTANCE.createDateStringDisplayFormatter();
								return zdt.toLocalDateTime().format(sdf);
							}
							return "";
						}
					}), new TradesRowEMFPath(false, Type.DISCHARGE_ALLOCATION, sp.getSlotAllocation_SlotVisit()));
			dischargeScheduledDate.getColumn().setData(EObjectTableViewer.COLUMN_SORT_PATH, new TradesRowEMFPath(false, Type.DISCHARGE_ALLOCATION));
		}

		addTradesColumn(dischargeColumns, "Sell At", new ContractManipulator(provider, commandHandler), new TradesRowEMFPath(false, Type.DISCHARGE));
		if (LicenseFeatures.isPermitted("features:report-discharge-counterparty")) {
			addTradesColumn(dischargeColumns, "C/P", new ReadOnlyManipulatorWrapper<BasicAttributeManipulator>(new BasicAttributeManipulator(pkg.getSlot_Counterparty(), commandHandler) {

				@Override
				protected String renderSetValue(final Object container, final Object setValue) {
					return setValue == null ? "" : setValue.toString();
				}

				@Override
				public boolean isValueUnset(final Object object) {
					return false;
				}

			}), new TradesRowEMFPath(false, Type.DISCHARGE));
		}
		addTradesColumn(dischargeColumns, "Price",
				new ReadOnlyManipulatorWrapper<BasicAttributeManipulator>(new BasicAttributeManipulator(SchedulePackage.eINSTANCE.getSlotAllocation_Price(), commandHandler) {

					@Override
					protected String renderSetValue(final Object container, final Object setValue) {
						if (setValue instanceof final Number num) {
							return String.format("$%.2f", num.doubleValue());
						}
						return super.renderSetValue(container, setValue);
					}
				}), new TradesRowEMFPath(false, Type.DISCHARGE_ALLOCATION));

		{
			final BasicAttributeManipulator manipulator = new VolumeAttributeManipulator(pkg.getSlot_MaxQuantity(), commandHandler);
			final TradesRowEMFPath assignmentPath = new TradesRowEMFPath(false, Type.DISCHARGE);
			final GridViewerColumn dischargeVol = addTradesColumn(dischargeColumns, "Vol", manipulator, assignmentPath);
			dischargeVol.getColumn().setHeaderTooltip("in m³ or mmBtu");
			dischargeVol.setLabelProvider(new EObjectTableViewerColumnProvider(getScenarioViewer(), manipulator, assignmentPath) {
				@Override
				public Image getImage(final Object element) {

					if (element instanceof TradesRow tradesRow) {
						final Object object = assignmentPath.get(tradesRow);
						if (object instanceof DischargeSlot ds) {
							if (ds.isHeelCarry()) {
								return heelCarryImage;
							}
						}
					}

					return super.getImage(element);
				}
			});
		}

		addTradesColumn(dischargeColumns, "Port", new SingleReferenceManipulator(pkg.getSlot_Port(), provider, commandHandler), new TradesRowEMFPath(false, Type.DISCHARGE));
		{
			final BasicAttributeManipulator manipulator = new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getNamedObject_Name(), commandHandler);
			final TradesRowEMFPath assignmentPath = new TradesRowEMFPath(false, Type.DISCHARGE);
			final GridViewerColumn idColumn = addTradesColumn(dischargeColumns, "ID", manipulator, assignmentPath);
			idColumn.setLabelProvider(new EObjectTableViewerColumnProvider(getScenarioViewer(), manipulator, assignmentPath) {
				@Override
				public Image getImage(final Object element) {

					if (element instanceof final TradesRow tradesRow) {
						final Object object = assignmentPath.get(tradesRow);
						if (object instanceof final DischargeSlot ds) {
							final boolean slotTransferred = LicenseFeatures.isPermitted(KnownFeatures.FEATURE_TRANSFER_MODEL) //
									&& CargoTransferUtil.isSlotReferencedByTransferRecord(ds, getScenarioModel());
							if (ds.getNotes() != null && !ds.getNotes().isEmpty()) {
								if (slotTransferred) {
									return notesAndTransferImage;
								}
								return notesImage;
							}
							if (slotTransferred) {
								return transferImage;
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
					new BasicAttributeManipulator(SchedulePackage.eINSTANCE.getProfitAndLossContainer_GroupProfitAndLoss(), commandHandler),
					new TradesRowEMFPath(true, Type.CARGO_OR_MARKET_OR_OPEN_ALLOCATION));
		}
		wiringDiagram = new TradesWiringDiagram(getScenarioViewer().getGrid(), colourScheme) {
			@Override
			protected void wiringChanged(final Map<TradesRow, TradesRow> newWiring, final boolean ctrlPressed, final boolean shiftPressed, final boolean altPressed) {
				if (locked) {
					return;
				}
				doWiringChanged(newWiring, ctrlPressed, shiftPressed, altPressed);
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
		final WiringDiagramMouseListener listener = new WiringDiagramMouseListener(getScenarioViewer().getGrid());
		getScenarioViewer().getGrid().addMouseMoveListener(listener);
		getScenarioViewer().getGrid().addMouseListener(listener);
		getScenarioViewer().getGrid().addKeyListener(listener);

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
			if (oData instanceof final TradesRow tradesRow) {
				final LoadSlot ls = tradesRow.getLoadSlot();
				if (ls != null) {
					if (ls.getWindowStart().isAfter(property)) {
						break;
					}
					pos++;
					continue;
				}
				final DischargeSlot ds = tradesRow.getDischargeSlot();
				if (ds != null) {
					if (ds.getWindowStart().isAfter(property)) {
						break;
					}
					pos++;
				}
			}
		}
		if (pos != -1) {
			grid.deselectAll();
			grid.select(pos);
			grid.showSelection();
		}
	}


	

	private <T extends ICellManipulator & ICellRenderer> GridViewerColumn addPNLColumn(final String columnName, final EStructuralFeature bookContainmentFeature, final T manipulator,
			final EMFPath path) {

		final ReadOnlyManipulatorWrapper<T> wrapper = new ReadOnlyManipulatorWrapper<T>(manipulator) {
			@Override
			public Comparable<?> getComparable(final Object element) {
				final Object object = path.get((EObject) element);
				if (object instanceof final ProfitAndLossContainer container) {
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

				if (object instanceof final ProfitAndLossContainer container) {
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
		final CargoModelRowTransformer transformer = new CargoModelRowTransformer(colourScheme);
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

				if (scenarioViewer.getSelection() instanceof final IStructuredSelection structuredSelection) {
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
											if (obj instanceof final TradesRow tradesRow) {
												if (tradesRow.getCargo() != null && tradesRow.getCargo().getCargoType() == CargoType.FLEET) {
													helper.clearActions();
													assignToHelper.createAssignmentMenus(helper, tradesRow.getCargo());
													helper.open();
												} else if (tradesRow.getLoadSlot() != null && tradesRow.getLoadSlot().isDESPurchase()) {
													helper.clearActions();
													assignToHelper.createAssignmentMenus(helper, tradesRow.getLoadSlot());
													helper.open();
												} else if (tradesRow.getDischargeSlot() != null && tradesRow.getDischargeSlot().isFOBSale()) {
													helper.clearActions();
													assignToHelper.createAssignmentMenus(helper, tradesRow.getDischargeSlot());
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
							if (obj instanceof final TradesRow tradesRow) {
								if (tradesRow.getCargo() != null) {
									target = tradesRow.getCargo();
								} else if ((column == null || loadColumns.contains(column)) && tradesRow.getLoadSlot() != null) {
									target = tradesRow.getLoadSlot();
								} else if ((column == null || dischargeColumns.contains(column) || !loadColumns.contains(column)) && tradesRow.getDischargeSlot() != null) {
									target = tradesRow.getDischargeSlot();
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
	protected void doWiringChanged(final Map<TradesRow, TradesRow> newWiring, final boolean _ctrlPressed, final boolean _shiftPressed, final boolean altPressed) {

		final boolean swapDischarges = _ctrlPressed;
		// Complex cargoes no longer supported in wiring DND
		final boolean createComplexCargo = !swapDischarges && _shiftPressed && allowLDD;

		final List<Command> setCommands = new LinkedList<>();

		final CargoModel cargoModel = getScenarioModel().getCargoModel();

		final Set<Slot<?>> slotsToRemove = new HashSet<>();
		final Set<Slot<?>> slotsToKeep = new HashSet<>();
		final Set<Cargo> cargoesToRemove = new HashSet<>();
		final Set<Cargo> cargoesToKeep = new HashSet<>();
		for (final Map.Entry<TradesRow, TradesRow> e : newWiring.entrySet()) {
			final TradesRow loadSide = e.getKey();
			final TradesRow dischargeSide = e.getValue();

			{
				// Address new A -> B wiring
				if (dischargeSide != null && dischargeSide.getDischargeSlot() != null) {
					if (loadSide.getLoadSlot().isLocked() || dischargeSide.getDischargeSlot().isLocked() || loadSide.getLoadSlot().isCancelled() || dischargeSide.getDischargeSlot().isCancelled()) {
						// Slots are not permitted to be wired together
						return;
					}
					final Cargo c;
					if (loadSide.getCargo() == null) {
						// New Cargo
						c = cec.createNewCargo(setCommands, cargoModel);
						setCommands.add(SetCommand.create(scenarioEditingLocation.getEditingDomain(), loadSide.getLoadSlot(), CargoPackage.eINSTANCE.getSlot_Cargo(), c));
					} else {
						c = loadSide.getCargo();

						// Swap discharges will take care of this later if needed
						if (!createComplexCargo && !swapDischarges) {
							// Break the existing wiring
							for (final Slot<?> s : c.getSlots()) {
								if (s != loadSide.getLoadSlot()) {
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
					slotsToKeep.add(loadSide.getLoadSlot());

					// Kept cargoes should not force fixed wiring after a change
					setCommands.add(SetCommand.create(scenarioEditingLocation.getEditingDomain(), c, CargoPackage.Literals.CARGO__ALLOW_REWIRING, Boolean.TRUE));

					setCommands.add(SetCommand.create(scenarioEditingLocation.getEditingDomain(), dischargeSide.getDischargeSlot(), CargoPackage.eINSTANCE.getSlot_Cargo(), c));
					if (c != null && dischargeSide.getDischargeSlot().isFOBSale()) {
						// Cargo assignments should be removed.
						setCommands.add(SetCommand.create(scenarioEditingLocation.getEditingDomain(), c, CargoPackage.eINSTANCE.getAssignableElement_VesselAssignmentType(), SetCommand.UNSET_VALUE));
						setCommands.add(SetCommand.create(scenarioEditingLocation.getEditingDomain(), c, CargoPackage.eINSTANCE.getAssignableElement_Locked(), Boolean.FALSE));
						setCommands.add(SetCommand.create(scenarioEditingLocation.getEditingDomain(), c, CargoPackage.eINSTANCE.getAssignableElement_SequenceHint(), SetCommand.UNSET_VALUE));
						setCommands.add(SetCommand.create(scenarioEditingLocation.getEditingDomain(), c, CargoPackage.eINSTANCE.getAssignableElement_SpotIndex(), SetCommand.UNSET_VALUE));

						// A Source Only FOB sale needs matching ports on both sides. Market FOB Sales
						// may have a set of valid ports. Automatically update if possible.
						if (dischargeSide.getDischargeSlot().getSlotOrDelegateFOBSaleDealType() == FOBSaleDealType.SOURCE_ONLY //
								&& loadSide.getLoadSlot().getPort() != dischargeSide.getDischargeSlot().getPort()) {
							if (dischargeSide.getDischargeSlot() instanceof final SpotSlot spotSlot) {
								final SpotMarket market = spotSlot.getMarket();
								if (market instanceof final FOBSalesMarket fobSalesMarket) {
									if (SetUtils.getObjects(fobSalesMarket.getOriginPorts()).contains(loadSide.getLoadSlot().getPort())) {
										setCommands.add(SetCommand.create(scenarioEditingLocation.getEditingDomain(), dischargeSide.getDischargeSlot(), CargoPackage.eINSTANCE.getSlot_Port(),
												loadSide.getLoadSlot().getPort()));
									}
								}

							}
						}
					}
					if (c != null && loadSide.getLoadSlot().isDESPurchase()) {

						// A Dest Only DES purchase needs matching ports on both sides. Market DES
						// purchases may have a set of valid ports. Automatically update if possible.
						if (loadSide.getLoadSlot().getSlotOrDelegateDESPurchaseDealType() == DESPurchaseDealType.DEST_ONLY //
								&& loadSide.getLoadSlot().getPort() != dischargeSide.getDischargeSlot().getPort()) {
							if (loadSide.getLoadSlot() instanceof final SpotSlot spotSlot) {
								final SpotMarket market = spotSlot.getMarket();
								if (market instanceof final DESPurchaseMarket desPurchaseMarket) {
									if (SetUtils.getObjects(desPurchaseMarket.getDestinationPorts()).contains(dischargeSide.getDischargeSlot().getPort())) {
										setCommands.add(SetCommand.create(scenarioEditingLocation.getEditingDomain(), loadSide.getLoadSlot(), CargoPackage.eINSTANCE.getSlot_Port(),
												dischargeSide.getDischargeSlot().getPort()));
									}
								}

							}
						}
					}

					{
						Cargo dischargeCargo = null;
						if (dischargeSide != null && dischargeSide.getDischargeSlot() != null) {
							dischargeCargo = dischargeSide.getDischargeSlot().getCargo();
						}
						if (dischargeCargo != null) {
							// Make sure the discharge from the original load is now passed into the new
							// cargo
							if (swapDischarges && loadSide.getDischargeSlot() != null) {
								// Break the existing wiring
								for (final Slot<?> s : c.getSlots()) {
									if (s != loadSide.getLoadSlot()) {
										setCommands.add(SetCommand.create(scenarioEditingLocation.getEditingDomain(), s, CargoPackage.eINSTANCE.getSlot_Cargo(), dischargeCargo));
									}
								}
							} else {
								// We want to break up the discharge cargo
								if (dischargeCargo.getSlots().size() <= 2) {
									for (final Slot<?> s : dischargeCargo.getSlots()) {
										if (s != dischargeSide.getDischargeSlot()) {
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
							// Swapping discharges, but we don't have a pair of cargoes, so fall back to
							// non-swap behaviour.

							// Break the existing wiring
							for (final Slot<?> s : c.getSlots()) {
								if (s != loadSide.getLoadSlot()) {
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
					if (loadSide.getCargo() != null) {
						cargoesToRemove.add(loadSide.getCargo());
						for (final Slot<?> s : loadSide.getCargo().getSlots()) {
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
		// Clear selection in case we keep stale references to deleted or re-arranged objects
		scenarioViewer.setSelection(StructuredSelection.EMPTY);
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
		// No default behaviour
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
				if (element instanceof final EObject eObject) {
					final Object o = path.get(eObject);
					if (manipulator.isValueUnset(o)) {
						return colourScheme.getSystemDarkGrey();
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

	public class TradesWiringViewer extends ScenarioTableViewer implements IFilterable {
		private TradesWiringViewer(Composite parent, int style, IScenarioEditingLocation part) {
			super(parent, style, part);
		}
		
		public StructuredViewerFilterManager getFilterManager() {
			return filterManager;
		}

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
		 * Overridden method to convert internal TradesRow objects into a collection of
		 * EMF Objects
		 */
		@Override
		protected void updateSelection(final ISelection selection) {

			if (selection instanceof final IStructuredSelection originalSelection) {

				final List<Object> selectedObjects = new LinkedList<>();

				final Iterator<?> itr = originalSelection.iterator();
				while (itr.hasNext()) {
					final Object obj = itr.next();
					if (obj instanceof final TradesRow tradesRow) {
						if (tradesRow.getCargo() != null) {
							selectedObjects.add(tradesRow.getCargo());
						}
						if (tradesRow.getLoadSlot() != null) {
							selectedObjects.add(tradesRow.getLoadSlot());
						}
						if (tradesRow.getDischargeSlot() != null) {
							selectedObjects.add(tradesRow.getDischargeSlot());
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

			if (rootData != null) {
				for (int i = 0; i < sortedChildren.length; ++i) {
					final int rawIndex = rootData.getRows().indexOf(sortedChildren[i]);
					sortedIndices[rawIndex] = i;
					reverseSortedIndices[i] = rawIndex;
				}
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
				public Object[] getElements(final Object inputElement) {

					final CargoModel cargoModel = getScenarioModel().getCargoModel();
					final ScheduleModel scheduleModel = getScenarioModel().getScheduleModel();

					final RootData root = setCargoes(cargoModel, scheduleModel, referenceRootData);

					TradesWiringViewerPane.this.rootData = root;

					if (TradesWiringViewerPane.this.referenceRootData == null) {
						TradesWiringViewerPane.this.referenceRootData = root;
					}
					resetSortOrder.setEnabled(TradesWiringViewerPane.this.referenceRootData != null);

					return rootData.getRows().toArray();
				}

				@Override
				public Object @Nullable [] getChildren(final Object parentElement) {
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
			setComparator(new TradesRowGroupComparator(vc));
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
					final RootData pRootData = rootData;
					if (pRootData == null) {
						return;
					}
					if (object == null) {
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

					TradesRow tradesRow = null;
					if (idx >= 0) {
						tradesRow = pRootData.getRows().get(idx);
					}

					if (tradesRow != null) {
						getValidationSupport().setStatus(tradesRow, status);
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
	}

	private class EditConstraintsAction extends LockableAction {
		public EditConstraintsAction(final String label) {
			super(label);
		}

		@Override
		public void run() {
			if (!scenarioViewer.isLocked()) {
				final ScenarioLock editorLock = scenarioEditingLocation.getEditorLock();
				if (editorLock.tryLock(500)) {
					try {
						scenarioEditingLocation.setDisableUpdates(true);
						final MMXRootObject rootObject = scenarioEditingLocation.getRootObject();
						if (rootObject instanceof LNGScenarioModel) {
							final GroupedSlotsDialog d = new GroupedSlotsDialog(scenarioEditingLocation.getShell(), scenarioEditingLocation);
							if (d.open() == Window.OK) {
								// clicked ok
							}
						} else {
							setEnabled(false);
						}
					} finally {
						scenarioEditingLocation.setDisableUpdates(false);
						editorLock.unlock();
					}
				}
			}
		}
	}

	private class AddAction extends DefaultMenuCreatorAction {

		public AddAction(final String label) {
			super(label);
		}

		/**
		 * Subclasses should fill their menu with actions here.
		 * 
		 * @param menu the menu which is about to be displayed
		 */
		protected void populate(final Menu menu) {
			{
				final DuplicateAction result = new DuplicateAction(getScenarioEditingLocation());
				// Translate into real objects, not just row object!
				final List<Object> selectedObjects = new LinkedList<>();
				if (scenarioViewer.getSelection() instanceof final IStructuredSelection structuredSelection) {

					final Iterator<?> itr = structuredSelection.iterator();
					while (itr.hasNext()) {
						final Object o = itr.next();
						if (o instanceof final TradesRow tradesRow) {
							// TODO: Check logic, a row may contain two distinct items
							if (tradesRow.getCargo() != null) {
								selectedObjects.add(tradesRow.getCargo());
								continue;
							}
							if (tradesRow.getLoadSlot() != null) {
								selectedObjects.add(tradesRow.getLoadSlot());
							}
							if (tradesRow.getDischargeSlot() != null) {
								selectedObjects.add(tradesRow.getDischargeSlot());
							}
						}
					}
				}

				result.selectionChanged(new SelectionChangedEvent(scenarioViewer, new StructuredSelection(selectedObjects)));
				addActionToMenu(result, menu);
			}

			final CargoModel cargoModel = getScenarioModel().getCargoModel();
			final CommandStack commandStack = scenarioEditingLocation.getEditingDomain().getCommandStack();

			TradesRow discoveredTradesRow = null;
			final ISelection selection = getScenarioViewer().getSelection();
			if (selection instanceof final IStructuredSelection ss) {
				final Object firstElement = ss.getFirstElement();
				if (firstElement instanceof final TradesRow tradesRow) {
					discoveredTradesRow = tradesRow;
				}
			}
			final TradesRow referenceTradesRow = discoveredTradesRow;
			{
				final Action newLoad = new Action("Cargo") {
					@Override
					public void run() {

						final List<Command> setCommands = new LinkedList<>();

						final Cargo newCargo = cec.createNewCargo(setCommands, cargoModel);

						final LoadSlot newLoad = cec.createNewLoad(setCommands, cargoModel, false);
						initialiseSlot(newLoad, true, referenceTradesRow);

						final DischargeSlot newDischarge = cec.createNewDischarge(setCommands, cargoModel, false);
						initialiseSlot(newDischarge, false, referenceTradesRow);

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
			if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_COMPLEX_CARGO)) {

				final Action newLoad = new Action("LDD cargo") {
					@Override
					public void run() {

						final List<Command> setCommands = new LinkedList<>();

						final Cargo newCargo = cec.createNewCargo(setCommands, cargoModel);

						final LoadSlot newLoad = cec.createNewLoad(setCommands, cargoModel, false);
						initialiseSlot(newLoad, true, referenceTradesRow);
						newLoad.setDESPurchase(false);

						final DischargeSlot newDischarge = cec.createNewDischarge(setCommands, cargoModel, false);
						initialiseSlot(newDischarge, false, referenceTradesRow);
						newDischarge.setFOBSale(false);

						final DischargeSlot newDischarge2 = cec.createNewDischarge(setCommands, cargoModel, false);
						initialiseSlot(newDischarge2, false, referenceTradesRow);
						newDischarge2.setFOBSale(false);
						if (newDischarge2.getWindowStart() != null) {
							newDischarge2.setWindowStart(newDischarge2.getWindowStart().plusDays(7));
						}

						newLoad.setCargo(newCargo);
						newDischarge.setCargo(newCargo);
						newDischarge2.setCargo(newCargo);

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
						initialiseSlot(newLoad, true, referenceTradesRow);

						final CompoundCommand cmd = new CompoundCommand("FOB Purchase");
						setCommands.forEach(cmd::append);

						commandStack.execute(cmd);
						DetailCompositeDialogUtil.editSingleObjectWithUndoOnCancel(getScenarioEditingLocation(), newLoad, commandStack.getMostRecentCommand());
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
						initialiseSlot(newLoad, true, referenceTradesRow);

						final CompoundCommand cmd = new CompoundCommand("DES Purchase");
						setCommands.forEach(cmd::append);
						commandStack.execute(cmd);
						DetailCompositeDialogUtil.editSingleObjectWithUndoOnCancel(getScenarioEditingLocation(), newLoad, commandStack.getMostRecentCommand());
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
						initialiseSlot(newDischarge, false, referenceTradesRow);

						final CompoundCommand cmd = new CompoundCommand("DES Sale");
						setCommands.forEach(cmd::append);

						commandStack.execute(cmd);
						DetailCompositeDialogUtil.editSingleObjectWithUndoOnCancel(getScenarioEditingLocation(), newDischarge, commandStack.getMostRecentCommand());
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
						initialiseSlot(newDischarge, false, referenceTradesRow);

						final CompoundCommand cmd = new CompoundCommand("FOB Sale");
						setCommands.forEach(cmd::append);

						commandStack.execute(cmd);
						DetailCompositeDialogUtil.editSingleObjectWithUndoOnCancel(getScenarioEditingLocation(), newDischarge, commandStack.getMostRecentCommand());
					}
				};
				addActionToMenu(newFOBSale, menu);
			}
			addActionToMenu(new CreateStripMenuAction("Create Strip"), menu);
		}

		private final void initialiseSlot(final Slot<?> newSlot, final boolean isLoad, final TradesRow referenceRowData) {
			newSlot.eSet(MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), EcoreUtil.generateUUID());
			newSlot.setOptional(false);
			newSlot.setName("");
			// Set window so that via default sorting inserts new slot at current table
			// position
			if (referenceRowData != null) {
				final Slot<?> primarySortSlot = isLoad ? referenceRowData.getLoadSlot() : referenceRowData.getDischargeSlot();
				final Slot<?> secondarySortSlot = isLoad ? referenceRowData.getDischargeSlot() : referenceRowData.getLoadSlot();
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
	 * A combined {@link MouseListener} and {@link MouseMoveListener} to scroll the
	 * table during wiring operations.
	 * 
	 */
	private class WiringDiagramMouseListener implements MouseListener, MouseMoveListener, KeyListener {

		private boolean dragging = false;

		private int x = 0; // Last drag x pos
		private int y = 0; // Last drag y pos
		private final Label lbl; // Tooltip label while dragging

		protected WiringDiagramMouseListener(final Grid grid) {
			lbl = new Label(grid.getParent(), SWT.BORDER);
			lbl.setText("");
			// lbl.setBackground(lbl.getShell().getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
			lbl.setBackground(colourScheme.getSystemInfoBackgroundColour(lbl.getShell().getDisplay()));
			lbl.setLayoutData(GridDataFactory.swtDefaults().exclude(true).create());
			// Make sure we are on top of the table viewer
			lbl.moveAbove(grid);
			// Hide until we are ready to show
			lbl.setVisible(false);
		}

		protected final void setDragMessage(final String message) {
			if (message == null || message.isBlank()) {
				if (this.lbl.isVisible()) {
					this.lbl.setVisible(false);
				}
				return;
			}

			if (!Objects.equals(this.lbl.getText(), message)) {
				this.lbl.setText(message);
				this.lbl.pack();

			}
			if (!this.lbl.isVisible()) {
				this.lbl.setVisible(true);
			}
		}

		@Override
		public void mouseMove(final MouseEvent e) {

			// Update tool tip location
			if (e.x != x || e.y != y) {
				x = e.x;
				y = e.y;
				this.lbl.setLocation(e.x + 0, e.y + 50);
			}
			// If drag off the table and release the mouse, the mouse up/down event will not
			// trigger here.
			// Re-check the mouse state and see if we have let go of the mouse button.

			if (dragging && ((e.stateMask & SWT.BUTTON1) == 0)) {
				dragging = false;
				setDragMessage(null);
			}
			if (dragging) {
				final Grid grid = getScenarioViewer().getGrid();

				// Update tooltip message
				updateTooltip(e.stateMask);

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
			} else {
				setDragMessage(null);
			}
		}

		@Override
		public void mouseDoubleClick(final MouseEvent e) {
			// No double click action
		}

		@Override
		public void mouseDown(final MouseEvent e) {
			if (e.button == 1) {
				dragging = true;
			}

		}

		@Override
		public void mouseUp(final MouseEvent e) {
			dragging = false;
			// Hide the tooltip
			setDragMessage(null);
		}

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.keyCode == SWT.CTRL || e.keyCode == SWT.SHIFT) {
				updateTooltip(e.keyCode);
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			if (e.keyCode == SWT.CTRL || e.keyCode == SWT.SHIFT) {
				updateTooltip(e.stateMask & ~e.keyCode);
			}
		}

		private void updateTooltip(int stateMask) {
			if (dragging) {
				final boolean ctrlPressed = (stateMask & SWT.CTRL) != 0;
				final boolean shiftPressed = (stateMask & SWT.SHIFT) != 0;
				// final boolean altPressed = (stateMask & SWT.ALT) != 0;
				if (ctrlPressed) {
					setDragMessage("Drag over another terminal to swap sells");
				} else if (shiftPressed && allowLDD) {
					setDragMessage("Drag over another terminal to create a LDD cargo");
				} else {
					if (allowLDD) {
						setDragMessage(
								"Drag over another terminal to create a new cargo.\nHold CTRL to swap sells\nHold SHIFT to make a LDD cargo\nDrag over white space to unpair the existing cargo");
					} else {
						setDragMessage("Drag over another terminal to create a new cargo.\nHold CTRL to swap sells\nDrag over white space to unpair the existing cargo");
					}
				}
			}
		}

	}

	private class CreateStripMenuAction extends LockableAction implements IMenuCreator {

		private Menu lastMenu;

		public CreateStripMenuAction(final String label) {
			super(label, IAction.AS_DROP_DOWN_MENU);
			CommonImages.setImageDescriptors(this, IconPaths.Plusplus);
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
		 * @param menu the menu which is about to be displayed
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
					if (rootObject instanceof final LNGScenarioModel scenarioModel) {

						Slot<?> selectedObject = null;
						final ISelection selection = TradesWiringViewerPane.this.getScenarioViewer().getSelection();
						if (selection instanceof final IStructuredSelection ss) {
							final Iterator<?> itr = ss.iterator();
							while (itr.hasNext()) {
								Object o = itr.next();

								if (o instanceof final TradesRow tradesRow) {
									if (stripType == StripType.TYPE_FOB_PURCHASE_SLOT || stripType == StripType.TYPE_DES_PURCHASE_SLOT) {
										o = tradesRow.getLoadSlot();
									} else if (stripType == StripType.TYPE_FOB_SALE_SLOT || stripType == StripType.TYPE_DES_SALE_SLOT) {
										o = tradesRow.getDischargeSlot();
									}
								}

								if (o instanceof Slot<?>) {
									selectedObject = (Slot<?>) o;
									break;
								}

							}
						}

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
	
	class TradesWiringViewerPaneFilterMenuAction extends DefaultMenuCreatorAction {
		

		/**
		 * A holder for a menu list of filter actions on different fields for the trades
		 * wiring table.
		 * 
		 * @param label The label to show in the UI for this menu.
		 */
		public TradesWiringViewerPaneFilterMenuAction(final String label) {
			super(label);
		}

		/**
		 * Add the filterable fields to the menu for this item.
		 */
		@Override
		protected void populate(final Menu menu) {
			final CommercialModel commercialModel = getScenarioModel().getReferenceModel().getCommercialModel();
			final FleetModel fleetModel = getScenarioModel().getReferenceModel().getFleetModel();
			final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(scenarioEditingLocation.getScenarioDataProvider());

			final EMFPath purchaseContractPath = new TradesRowEMFPath(false, Type.LOAD, CargoPackage.Literals.SLOT__CONTRACT);
			final EMFPath salesContractPath = new TradesRowEMFPath(false, Type.DISCHARGE, CargoPackage.Literals.SLOT__CONTRACT);
			
			final Action clearAction = new Action("Clear All Filters") {
				@Override
				public void run() {
					filterManager.removeFilters();
				}
			};

			addActionToMenu(clearAction, menu);
			addActionToMenu(new EMFPathFilterAction(filterManager, "Purchase Contracts", commercialModel, CommercialPackage.Literals.COMMERCIAL_MODEL__PURCHASE_CONTRACTS, purchaseContractPath), menu);
			addActionToMenu(new EMFPathFilterAction(filterManager, "Sale Contracts", commercialModel, CommercialPackage.Literals.COMMERCIAL_MODEL__SALES_CONTRACTS, salesContractPath), menu);
			addActionToMenu(new TrimmedVesselFilterAction(filterManager, cargoModel), menu);
			addActionToMenu(new EntityFilterAction(filterManager, commercialModel), menu);

			final OpenCargoFilterAction ocfa = new OpenCargoFilterAction(filterManager, filtersOpenContracts);

			addActionToMenu(ocfa, menu);

			final DefaultMenuCreatorAction dmcaShippedCargos = new ShippedCargoFilterAction(filterManager);
			addActionToMenu(dmcaShippedCargos, menu);
			
			addActionToMenu(new LoadPortFilterAction(filterManager, cargoModel), menu);
			addActionToMenu(new DischargePortFilterAction(filterManager, cargoModel), menu);

			final DefaultMenuCreatorAction dmcaTimePeriod = new PeriodFilterAction(filterManager, earliest, latest);

			addActionToMenu(dmcaTimePeriod, menu);

			if (extraFiltersProvider != null) {
				for (final DefaultMenuCreatorAction edmca : extraFiltersProvider.getExtraMenuActions(filterManager.getViewer())) {
					addActionToMenu(edmca, menu);
				}
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
		return null;
	}

	@Override
	protected void lookAtMySelection(final ISelection selection) {
		// nothing
		if (selection != null && !selection.isEmpty()) {

		}
	}

}
