/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard.econs;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.nebula.widgets.grid.GridColumnGroup;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.ViewPart;

import com.google.common.base.Functions;
import com.mmxlabs.lingo.reports.IReportContents;
import com.mmxlabs.lingo.reports.IReportContentsGenerator;
import com.mmxlabs.lingo.reports.ReportContents;
import com.mmxlabs.lingo.reports.internal.Activator;
import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.lingo.reports.services.ISelectedScenariosServiceListener;
import com.mmxlabs.lingo.reports.services.ScenarioComparisonService;
import com.mmxlabs.lingo.reports.services.SelectedDataProviderImpl;
import com.mmxlabs.lingo.reports.views.standard.econs.EconsOptions.MarginBy;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.PaperDeal;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.CharterLengthEvent;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.GroupedCharterLengthEvent;
import com.mmxlabs.models.lng.schedule.GroupedCharterOutEvent;
import com.mmxlabs.models.lng.schedule.MarketAllocation;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.PaperDealAllocation;
import com.mmxlabs.models.lng.schedule.PaperDealAllocationEntry;
import com.mmxlabs.models.lng.schedule.Purge;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelUtils;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.models.ui.tabular.IImageProvider;
import com.mmxlabs.models.ui.tabular.renderers.CenteringColumnGroupHeaderRenderer;
import com.mmxlabs.models.ui.tabular.renderers.ColumnGroupHeaderRenderer;
import com.mmxlabs.models.ui.tabular.renderers.ColumnHeaderRenderer;
import com.mmxlabs.models.ui.tabular.renderers.ColumnImageCenterHeaderRenderer;
import com.mmxlabs.rcp.common.CommonImages;
import com.mmxlabs.rcp.common.ServiceHelper;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.rcp.common.CommonImages.IconMode;
import com.mmxlabs.rcp.common.CommonImages.IconPaths;
import com.mmxlabs.rcp.common.actions.CopyGridToExcelMSClipboardAction;
import com.mmxlabs.rcp.common.actions.CopyTransposedGridToJSONUtil;
import com.mmxlabs.rcp.common.actions.IAdditionalAttributeProvider;
import com.mmxlabs.rcp.common.actions.PackActionFactory;
import com.mmxlabs.rcp.common.actions.RunnableAction;
import com.mmxlabs.scenario.service.ScenarioResult;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ModelReference;
import com.mmxlabs.scenario.service.model.manager.SSDataManager;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.ui.editing.IScenarioServiceEditorInput;

/**
 * The {@link CargoEconsReport} is a vertical report similar in concept to the
 * Properties View. This table is the transpose of most other tables. Columns
 * represent the input data and rows are pre-defined.
 * 
 * @author Simon Goodall
 * 
 */
public class CargoEconsReport extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.shiplingo.platform.reports.views.CargoEconsReport";

	private GridTableViewer viewer;

	/**
	 * List of dynamically generated columns to be disposed on selection changes
	 */
	private final List<GridViewerColumn> dataColumns = new LinkedList<>();
	private final EconsOptions options = new EconsOptions();

	private Map<String, GridColumnGroup> gridColumnGroupsMap = new HashMap<>();

	private Image pinImage = null;

	private boolean onlyDiffMode = false;

	private GridViewerColumn dummyDataCol;

	private ISelectedDataProvider selectedDataProvider;

	private ScenarioComparisonService scenarioComparisonService;
	private final ISelectedScenariosServiceListener listener = new ISelectedScenariosServiceListener() {

		@Override
		public void selectedDataProviderChanged(@NonNull final ISelectedDataProvider selectedDataProvider, final boolean block) {
			rebuild(selectedDataProvider);
		}

		@Override
		public void selectedObjectChanged(@Nullable final MPart source, final ISelection selection) {
			rebuild();
		}
	};

	@Override
	public void createPartControl(final Composite parent) {
		scenarioComparisonService = getSite().getService(ScenarioComparisonService.class);

		pinImage = CommonImages.getImageDescriptor(IconPaths.Pin, IconMode.Enabled).createImage();
//		createImage("icons/Pinned.gif");

		viewer = new GridTableViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL);

		GridViewerHelper.configureLookAndFeel(viewer);
		ColumnViewerToolTipSupport.enableFor(viewer);

		viewer.getGrid().setRowHeaderVisible(true);
		viewer.setContentProvider(new ArrayContentProvider());

		// Set row header column
		viewer.setRowHeaderLabelProvider(createRowHeaderLabelProvider());
		viewer.refreshRowHeaders(null);

		dummyDataCol = new GridViewerColumn(viewer, SWT.NONE);
		{
			GridViewerHelper.configureLookAndFeel(dummyDataCol);
			dummyDataCol.getColumn().setText("Dummy");
			dummyDataCol.setLabelProvider(new FieldTypeNameLabelProvider());
			dummyDataCol.getColumn().setVisible(false);
		}
		// All other columns dynamically added.

		// Our input!
		// Array content provider as we pass in an array of enums
		final List<CargoEconsReportRow> rows = new LinkedList<>();
		ServiceHelper.withAllServices(IEconsRowFactory.class, null, factory -> {
			rows.addAll(factory.createRows(options, null));
			return true;
		});
		Collections.sort(rows, (a, b) -> a.order - b.order);

		viewer.setInput(rows);
		viewer.getGrid().setHeaderVisible(true);

		// If we have data, dispose of the dummy name column immediately
		if (rows.isEmpty()) {
			dummyDataCol.getColumn().dispose();
		}

		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "com.mmxlabs.lingo.doc.Reports_CargoEcons");

		{
			final Map<MarginBy, Action> actionMap = new EnumMap<>(MarginBy.class);
			final Consumer<MarginBy> changeSelectedAction = mode -> {
				for (final Map.Entry<MarginBy, Action> e : actionMap.entrySet()) {
					e.getValue().setChecked(e.getKey() == mode);
				}
			};
			final Action volumeByPurchase = new RunnableAction("Per mmBtu by purchase volume", SWT.TOGGLE, () -> {
				setMarginMode(MarginBy.PURCHASE_VOLUME);
			});
			actionMap.put(MarginBy.PURCHASE_VOLUME, volumeByPurchase);
			getViewSite().getActionBars().getMenuManager().add(volumeByPurchase);

			final Action volumeBySell = new RunnableAction("Per mmBtu by sales volume", SWT.TOGGLE, () -> {
				setMarginMode(MarginBy.SALE_VOLUME);
			});
			actionMap.put(MarginBy.SALE_VOLUME, volumeBySell);
			getViewSite().getActionBars().getMenuManager().add(volumeBySell);

			final Action showPnLCalcs = new RunnableAction("Show PnL calcs", SWT.TOGGLE, () -> {
				setShowPnLCalcs(!isShowPnLCalcs());
				rebuild();
			});
			setShowPnLCalcs(true);
			showPnLCalcs.setChecked(true);
			getViewSite().getActionBars().getMenuManager().add(showPnLCalcs);

			final Action showOnlyDiff = new Action("Δ", SWT.TOGGLE) {
				@Override
				public void run() {
					toggleShowDiffOnly();
					setChecked(onlyDiffMode);
					rebuild();
				}
			};
			getViewSite().getActionBars().getToolBarManager().add(showOnlyDiff);

			// Default to sales volume
			volumeBySell.run();
		}

		final Runnable preOperation = () -> {
			setCopyPasteMode(true);
			ViewerHelper.refresh(getViewer(), true);
		};

		final Runnable postOperation = () -> {
			setCopyPasteMode(false);
			ViewerHelper.refresh(getViewer(), true);
		};

		final Action packAction = PackActionFactory.createPackColumnsAction(viewer);
		final CopyGridToExcelMSClipboardAction copyAction = new CopyGridToExcelMSClipboardAction(viewer.getGrid(), true, preOperation, postOperation);
		copyAction.setAdditionalAttributeProvider(new IAdditionalAttributeProvider() {

			@Override
			public Object getTypedValue(final GridItem item, final int i) {
				final String text = item.getText(i);
				try {
					return NumberFormat.getInstance().parseObject(text);
				} catch (final Exception e) {

				}
				return String.class;
			}

			@Override
			public @NonNull String getTopLeftCellUpperText() {
				return "";
			}

			@Override
			public @NonNull String getTopLeftCellText() {
				return "";
			}

			@Override
			public @NonNull String getTopLeftCellLowerText() {
				return "";
			}

			@Override
			public @NonNull String @Nullable [] getAdditionalRowHeaderAttributes(@NonNull final GridItem item) {
				return null;
			}

			@Override
			public @NonNull String @Nullable [] getAdditionalPreRows() {
				return null;
			}

			@Override
			public @NonNull String @Nullable [] getAdditionalHeaderAttributes(final GridColumn column) {
				return null;
			}

			@Override
			public @NonNull String @Nullable [] getAdditionalAttributes(@NonNull final GridItem item, final int columnIdx) {

				return null;
			}

			@Override
			public int getBorders(final GridItem item, final int i) {
				return 0;
			}

		});
		getViewSite().getActionBars().getToolBarManager().add(packAction);
		getViewSite().getActionBars().getToolBarManager().add(copyAction);
		getViewSite().getActionBars().setGlobalActionHandler(ActionFactory.COPY.getId(), copyAction);

		scenarioComparisonService.addListener(listener);
		scenarioComparisonService.triggerListener(listener, false);

	}

	@Override
	public void dispose() {

		if (pinImage != null) {
			pinImage.dispose();
			pinImage = null;
		}

		if (scenarioComparisonService != null) {
			scenarioComparisonService.removeListener(listener);
		}
	}

	private Image createImage(final String path) {
		final ImageDescriptor imageDescriptor = Activator.Implementation.getImageDescriptor(path);
		return imageDescriptor.createImage();
	}

	private CellLabelProvider createRowHeaderLabelProvider() {
		return new CellLabelProvider() {

			@Override
			public void update(final ViewerCell cell) {
				final Object element = cell.getElement();

				if (element == null) {
					return;
				}

				if (!(element instanceof CargoEconsReportRow)) {
					return;
				}

				final CargoEconsReportRow row = (CargoEconsReportRow) cell.getElement();
				cell.setText(row.name);
			}
		};
	}

	public void setFocus() {
		ViewerHelper.setFocus(viewer);
	}

	/**
	 * The {@link FieldType} is the row data. Each enum is a different row.
	 * Currently there is no table sorter so enum order is display order
	 * 
	 */

	/**
	 * A label provider for the "Name" column
	 * 
	 */
	private static class FieldTypeNameLabelProvider extends ColumnLabelProvider {

		@Override
		public Image getImage(final Object element) {
			return null;
		}

		@Override
		public String getText(final Object element) {
			if (element instanceof CargoEconsReportRow) {
				final CargoEconsReportRow fieldType = (CargoEconsReportRow) element;
				return fieldType.name;
			}
			return null;
		}
	}

	/**
	 * Label Provider created for each column. Returns text for the given
	 * {@link CargoEconsReportRow} enum (as the input element) for the
	 * {@link IFieldTypeMapper} object passed in during creation.
	 * 
	 */
	private class FieldTypeMapperLabelProvider extends ColumnLabelProvider {

		private final Object columnElement;

		public FieldTypeMapperLabelProvider(final Object columnElement) {
			this.columnElement = columnElement;
		}

		@Override
		public Image getImage(final Object element) {
			if (element instanceof CargoEconsReportRow) {
				final CargoEconsReportRow row = (CargoEconsReportRow) element;
				if (row.formatter instanceof IImageProvider) {
					final IImageProvider ip = (IImageProvider) row.formatter;
					return ip.getImage(columnElement);
				}
			}
			return null;
		}

		@Override
		public String getText(final Object element) {
			if (element instanceof CargoEconsReportRow) {
				final CargoEconsReportRow row = (CargoEconsReportRow) element;
				if (row.includeUnits) {
					final String value = row.formatter.render(columnElement);
					if (value != null) {
						return row.prefixUnit + value + row.suffixUnit;
					}
				} else {
					return row.formatter.render(columnElement);
				}
			}
			return null;
		}

		@Override
		public String getToolTipText(final Object element) {
			if (element instanceof CargoEconsReportRow) {
				final CargoEconsReportRow row = (CargoEconsReportRow) element;
				final Supplier<String> tooltip = row.tooltip;
				if (tooltip != null) {
					return tooltip.get();
				}
			}
			return null;
		}

		@Override
		public Color getForeground(final Object element) {
			if (element instanceof CargoEconsReportRow) {
				final CargoEconsReportRow row = ((CargoEconsReportRow) element);
				if (row.colourProvider != null) {
					return row.colourProvider.getForeground(columnElement);
				}
			}
			return null;
		}

		@Override
		public void update(final ViewerCell cell) {
			cell.setText(getText(cell.getElement()));
			cell.setForeground(getForeground(cell.getElement()));
			cell.setBackground(getBackground(cell.getElement()));
			cell.setImage(getImage(cell.getElement()));
		}
	}

	public void setIncludedUnit(final boolean includeUnit) {
		for (final GridItem item : viewer.getGrid().getItems()) {
			final Object obj = item.getData();
			if (obj instanceof CargoEconsReportRow) {
				((CargoEconsReportRow) obj).includeUnits = includeUnit;
			}
		}
	}

	public void toggleShowDiffOnly() {
		onlyDiffMode = !onlyDiffMode;
	}

	/**
	 * This method processes an {@link ISelection} object and return a list of
	 * objects that this report knows how to process. Currently we try to find
	 * associated {@link CargoAllocation} objects.
	 * 
	 * @param selection
	 * @return
	 */
	private List<Object> processSelection(final Collection<?> selectedObjects) {

		final Collection<Object> validObjects = new LinkedHashSet<>();

		// Assume input is from a ScheduleModel element
		for (Object obj : selectedObjects) {
			if (obj instanceof Event) {
				obj = ScheduleModelUtils.getSegmentStart((Event) obj);
			}
			if (obj instanceof StartEvent) {
				validObjects.add(obj);
			} else if (obj instanceof EndEvent) {
				validObjects.add(obj);
			} else if (obj instanceof GeneratedCharterOut) {
				validObjects.add(obj);
			} else if (obj instanceof CharterLengthEvent) {
				validObjects.add(obj);
			} else if (obj instanceof VesselEventVisit) {
				validObjects.add(obj);
			} else if (obj instanceof CargoAllocation) {
				validObjects.add(obj);
			} else if (obj instanceof OpenSlotAllocation) {
				validObjects.add(obj);
			} else if (obj instanceof SlotAllocation) {
				final SlotAllocation slotAllocation = (SlotAllocation) obj;
				if (slotAllocation.getCargoAllocation() != null) {
					validObjects.add(slotAllocation.getCargoAllocation());
				}
				if (slotAllocation.getMarketAllocation() != null) {
					validObjects.add(slotAllocation.getMarketAllocation());
				}
			} else if (obj instanceof SlotVisit) {
				validObjects.add((((SlotVisit) obj).getSlotAllocation().getCargoAllocation()));
			} else if (obj instanceof Purge) {
				validObjects.add(obj);
			} else if (obj instanceof PaperDealAllocation) {
				validObjects.add(obj);
			} else if (obj instanceof PaperDealAllocationEntry) {
				final EObject objContainer = ((PaperDealAllocationEntry) obj).eContainer();
				if (objContainer instanceof PaperDealAllocation) {
					validObjects.add(objContainer);
				}
			}
		}
		// Remove invalid items
		validObjects.remove(null);

		if (validObjects.isEmpty()) {
			// No valid schedule model elements, maybe it is a selection from e.g. trades
			// table so look up matching data from default schedule model.

			// Lazily computed caches (if coming from editor selection)
			Map<PaperDeal, PaperDealAllocation> paperDealMap = null;
			Map<VesselEvent, VesselEventVisit> vesselEventMap = null;
			Map<Collection<Slot<?>>, CargoAllocation> cargoMap = null;

			for (final Object obj : selectedObjects) {
				if (obj != null && (obj instanceof Cargo || obj instanceof Slot || obj instanceof VesselEvent || obj instanceof PaperDeal)) {
					final LNGScenarioModel scenarioModel = ScenarioModelUtil.findScenarioModel((EObject) obj);
					if (scenarioModel != null && scenarioModel.getScheduleModel() != null && scenarioModel.getScheduleModel().getSchedule() != null) {
						final Schedule schedule = scenarioModel.getScheduleModel().getSchedule();
						Cargo cargo = null;
						Slot<?> slot = null;
						VesselEvent event = null;
						if (obj instanceof VesselEvent) {
							event = (VesselEvent) obj;
						} else if (obj instanceof Cargo) {
							cargo = (Cargo) obj;
						} else if (obj instanceof PaperDeal) {
							final PaperDeal paperDeal = (PaperDeal) obj;
							if (schedule != null) {
								if (paperDealMap == null) {
									paperDealMap = schedule.getPaperDealAllocations().stream() //
											.collect(Collectors.toMap(PaperDealAllocation::getPaperDeal, Functions.identity()));
								}
								final PaperDealAllocation pda = paperDealMap.get(paperDeal);
								if (pda != null) {
									validObjects.add(pda);
								}
							}
						} else {
							// Must be a slot
							assert obj instanceof Slot;
							if (obj instanceof LoadSlot) {
								cargo = ((LoadSlot) obj).getCargo();
								slot = (LoadSlot) obj;
							} else if (obj instanceof DischargeSlot) {
								cargo = ((DischargeSlot) obj).getCargo();
								slot = (DischargeSlot) obj;
							}
						}

						if (cargo != null && schedule != null) {
							if (cargoMap == null) {
								cargoMap = new HashMap<>();
								for (final CargoAllocation ca : schedule.getCargoAllocations()) {
									final Set<Slot<?>> key = new HashSet<>();
									for (final SlotAllocation sa : ca.getSlotAllocations()) {
										final Slot<?> s = sa.getSlot();
										if (s != null) {
											key.add(s);
										}
									}
									cargoMap.put(key, ca);
								}
							}
							final CargoAllocation cargoAllocation = cargoMap.get(new HashSet<>(cargo.getSlots()));
							if (cargoAllocation != null) {
								validObjects.add(cargoAllocation);
							}
						} else if (slot != null && schedule != null) {
							for (final MarketAllocation marketAllocation : schedule.getMarketAllocations()) {
								if (slot == marketAllocation.getSlot()) {
									validObjects.add(marketAllocation);
									break;
								}
							}
							for (final OpenSlotAllocation openSlotAllocation : schedule.getOpenSlotAllocations()) {
								if (slot == openSlotAllocation.getSlot()) {
									validObjects.add(openSlotAllocation);
									break;
								}
							}
						} else if (event != null && schedule != null) {
							if (vesselEventMap == null) {
								vesselEventMap = schedule.getSequences().stream().flatMap(s -> s.getEvents().stream()) //
										.filter(VesselEventVisit.class::isInstance) //
										.map(VesselEventVisit.class::cast) //
										.collect(Collectors.toMap(VesselEventVisit::getVesselEvent, Functions.identity()));
							}
							final VesselEventVisit vesselEventVisit = vesselEventMap.get(event);
							if (vesselEventVisit != null) {
								validObjects.add(vesselEventVisit);
							}
						}
					}
				}
			}
		}

		// Remove invalid items
		validObjects.remove(null);

		return new LinkedList<>(validObjects);
	}

	public GridTableViewer getViewer() {
		return viewer;
	}

	public <T> int comparator(final T a, final T b) {
		String aName = "";
		String bName = "";

		if (a != null && b != null) {

			if (a instanceof CargoAllocation) {
				aName = ((CargoAllocation) a).getName();
			} else if (a instanceof OpenSlotAllocation) {
				aName = ((OpenSlotAllocation) a).getSlot().getName();
			} else if (a instanceof Event) {
				aName = ((Event) a).name();
			} else if (a instanceof DeltaPair) {
				aName = ((DeltaPair) a).getName();
			} else if (a instanceof PaperDealAllocation) {
				aName = ((PaperDealAllocation) a).getPaperDeal().getName();
			}

			if (b instanceof CargoAllocation) {
				bName = ((CargoAllocation) b).getName();
			} else if (b instanceof OpenSlotAllocation) {
				bName = ((OpenSlotAllocation) b).getSlot().getName();
			} else if (b instanceof Event) {
				bName = ((Event) b).name();
			} else if (b instanceof DeltaPair) {
				bName = ((DeltaPair) b).getName();
			} else if (b instanceof PaperDealAllocation) {
				bName = ((PaperDealAllocation) b).getPaperDeal().getName();
			}

			if (aName != null && bName != null) {
				int res = aName.compareTo(bName);

				if (res == 0 && !(a instanceof DeltaPair || b instanceof DeltaPair)) {
					if (selectedDataProvider != null && selectedDataProvider.isPinnedObject((EObject) a)) {
						res++;
					}

					if (selectedDataProvider != null && selectedDataProvider.isPinnedObject((EObject) b)) {
						res--;
					}
				}
				return res;
			}
		}

		return Integer.MAX_VALUE;
	}

	/**
	 * Adds a selection listener for the given partID. Listens to everything if null
	 */
	public void setMarginMode(final MarginBy mode) {
		options.marginBy = mode;
		ViewerHelper.refresh(getViewer(), false);
	}

	private @Nullable ModelReference getPartInstanceReference(final IWorkbenchPart part) {
		if (part instanceof IEditorPart) {
			final IEditorPart editorPart = (IEditorPart) part;
			final IEditorInput editorInput = editorPart.getEditorInput();
			if (editorInput instanceof IScenarioServiceEditorInput) {
				final IScenarioServiceEditorInput scenarioServiceEditorInput = (IScenarioServiceEditorInput) editorInput;
				final ScenarioInstance scenarioInstance = scenarioServiceEditorInput.getScenarioInstance();
				if (scenarioInstance != null) {
					final ScenarioModelRecord modelRecord = SSDataManager.Instance.getModelRecord(scenarioInstance);
					if (modelRecord != null) {
						return modelRecord.aquireReference("CargoEconsReport");
					}
				}
			}
		}
		return null;
	}

	@Override
	public <T> T getAdapter(final Class<T> adapter) {

		if (GridTableViewer.class.isAssignableFrom(adapter)) {
			return adapter.cast(viewer);
		}
		if (Grid.class.isAssignableFrom(adapter)) {
			return adapter.cast(viewer.getGrid());
		}
		if (IReportContentsGenerator.class.isAssignableFrom(adapter)) {
			dummyDataCol.getColumn().dispose();

			return adapter.cast(new IReportContentsGenerator() {
				public IReportContents getReportContents(final ScenarioResult pin, final ScenarioResult other, final @Nullable List<Object> selectedObjects) {
					final SelectedDataProviderImpl provider = new SelectedDataProviderImpl();
					if (pin != null) {
						provider.addScenario(pin);
						provider.setPinnedScenarioInstance(pin);
					}
					if (other != null) {
						provider.addScenario(other);
					}

					if (selectedObjects != null) {
						provider.setSelectedObjects(null, new StructuredSelection(selectedObjects));
					}

					// Request a blocking update ...
					listener.selectedDataProviderChanged(provider, true);
					// ... so the data is ready to be read here.
					final CopyTransposedGridToJSONUtil jsonUtil = new CopyTransposedGridToJSONUtil(viewer.getGrid(), true);
					final String jsonContents = jsonUtil.convert();
					return ReportContents.makeJSON(jsonContents);
				}
			});
		}
		if (IReportContents.class.isAssignableFrom(adapter)) {
			dummyDataCol.getColumn().dispose();
			if (IReportContents.class.isAssignableFrom(adapter)) {
				final CopyTransposedGridToJSONUtil jsonUtil = new CopyTransposedGridToJSONUtil(viewer.getGrid(), true);
				final String jsonContents = jsonUtil.convert();
				return adapter.cast(ReportContents.makeJSON(jsonContents));
			}
		}
		return super.getAdapter(adapter);
	}

	public Map<String, GridColumnGroup> createColumnGroups(final Collection<Object> objects) {
		final Map<String, GridColumnGroup> columnGroups = new HashMap<>();

		for (final Object object : objects) {
			String name = "";

			if (object instanceof Event) {
				name = ((Event) object).name();
			}

			if (object instanceof VesselEventVisit) {
				name = ((VesselEventVisit) object).name();
			}

			if (object instanceof CargoAllocation) {
				name = ((CargoAllocation) object).getName();
			}
			if (object instanceof CharterLengthEvent) {
				name = ((CharterLengthEvent) object).name();
			}
			if (object instanceof GroupedCharterLengthEvent) {
				name = ((GroupedCharterLengthEvent) object).name();
			}
			if (object instanceof GroupedCharterOutEvent) {
				name = ((GroupedCharterOutEvent) object).name();
			}
			if (object instanceof DeltaPair) {
				name = ((DeltaPair) object).getName();
			}

			if (object instanceof MarketAllocation) {
				name = ((MarketAllocation) object).getSlot().getName();
			}
			if (object instanceof OpenSlotAllocation) {
				name = ((OpenSlotAllocation) object).getSlot().getName();
			}

			if (object instanceof PaperDealAllocation) {
				name = ((PaperDealAllocation) object).getPaperDeal().getName();
			}

			if (!columnGroups.containsKey(name)) {
				final GridColumnGroup gridColumnGroup = new GridColumnGroup(viewer.getGrid(), SWT.CENTER);
				gridColumnGroup.setHeaderRenderer(new ColumnGroupHeaderRenderer());
				createCenteringGroupRenderer(gridColumnGroup);
				gridColumnGroup.setText(name);

				columnGroups.put(name, gridColumnGroup);
			}
		}

		return columnGroups;
	}

	public void rebuild() {
		rebuild(this.selectedDataProvider);
	}

	public void rebuild(final ISelectedDataProvider selectedDataProvider) {
		this.selectedDataProvider = selectedDataProvider;

		final boolean compareMode = selectedDataProvider != null && selectedDataProvider.inPinDiffMode();

		// Extract relevant selected objects.
		final List<Object> validObjects = processSelection(selectedDataProvider == null ? Collections.emptyList() : selectedDataProvider.getSelectedObjects());

		// Dispose old data columns - clone list to try to avoid concurrent modification
		// exceptions
		final List<GridViewerColumn> oldColumns = new ArrayList<>(dataColumns);
		dataColumns.clear();

		oldColumns.forEach(gvc -> gvc.getColumn().dispose());
		gridColumnGroupsMap.values().forEach(GridColumnGroup::dispose);
		gridColumnGroupsMap.clear();

		final List<CargoAllocation> cargoAllocations = new ArrayList<>();
		for (final Object obj : validObjects) {
			if (obj instanceof CargoAllocation) {
				cargoAllocations.add((CargoAllocation) obj);
			}
		}

		final List<OpenSlotAllocation> openSlotAllocations = new ArrayList<>();
		for (final Object obj : validObjects) {
			if (obj instanceof OpenSlotAllocation) {
				openSlotAllocations.add((OpenSlotAllocation) obj);
			}
		}

		final List<VesselEventVisit> vesselEventVisits = new ArrayList<>();
		for (final Object obj : validObjects) {
			if (obj instanceof VesselEventVisit) {
				vesselEventVisits.add((VesselEventVisit) obj);
			}
		}
		final List<PaperDealAllocation> paperDealAllocations = new ArrayList<>();
		for (final Object obj : validObjects) {
			if (obj instanceof PaperDealAllocation) {
				paperDealAllocations.add((PaperDealAllocation) obj);
			}
		}

		final List<Object> otherObjects = new ArrayList<>(validObjects);
		otherObjects.removeAll(vesselEventVisits);
		otherObjects.removeAll(cargoAllocations);
		otherObjects.removeAll(openSlotAllocations);
		otherObjects.removeAll(paperDealAllocations);

		// Create the row object
		final List<CargoEconsReportRow> rows = new LinkedList<>();
		ServiceHelper.withAllServices(IEconsRowFactory.class, null, factory -> {
			rows.addAll(factory.createRows(options, validObjects));
			return true;
		});
		Collections.sort(rows, (a, b) -> a.order - b.order);

		if (compareMode) {
			final List<DeltaPair> aggregateList = new ArrayList<>();

			final List<CargoAllocationPair> cargoAllocationPairs = CargoAllocationPair.generateDeltaPairs(selectedDataProvider, cargoAllocations);

			final List<VesselEventVisitPair> vesselEventVisitsPairs = VesselEventVisitPair.generateDeltaPairs(selectedDataProvider, vesselEventVisits);

			final List<OtherPair> otherPairs1 = OtherPair.generateDeltaPairs(selectedDataProvider, validObjects.stream() //
					.filter(StartEvent.class::isInstance) //
					.map(Event.class::cast) //
					.collect(Collectors.toList()));

			final List<OtherPair> otherPairs2 = OtherPair.generateDeltaPairs(selectedDataProvider, validObjects.stream() //
					.filter(EndEvent.class::isInstance)//
					.map(Event.class::cast) //
					.collect(Collectors.toList()));

			final List<OtherPair> otherPairs3 = OtherPair.generateSingles(selectedDataProvider, validObjects.stream() //
					.filter(GeneratedCharterOut.class::isInstance) //
					.map(Event.class::cast) //
					.collect(Collectors.toList()));

			final List<OpenAllocationPair> openAllocationPairs = OpenAllocationPair.generateDeltaPairs(selectedDataProvider, openSlotAllocations);

			final List<OtherPair> otherPairs4 = OtherPair.generateSingles(selectedDataProvider, validObjects.stream() //
					.filter(CharterLengthEvent.class::isInstance) //
					.map(Event.class::cast) //
					.collect(Collectors.toList()));

			aggregateList.addAll(cargoAllocationPairs);
			aggregateList.addAll(vesselEventVisitsPairs);
			aggregateList.addAll(otherPairs1);
			aggregateList.addAll(otherPairs2);
			aggregateList.addAll(otherPairs3);
			aggregateList.addAll(otherPairs4);
			aggregateList.addAll(openAllocationPairs);

			if (onlyDiffMode) {
				validObjects.clear();
			}
			validObjects.addAll(aggregateList);
			final List<Object> sortedObjects = new ArrayList<>(validObjects.size());

			// Create a new list to sort the elements and replace the content of the
			// LinkedHashMap with it (insert-order)
			sortedObjects.addAll(validObjects);
			Collections.sort(sortedObjects, this::comparator);

			validObjects.clear();
			validObjects.addAll(sortedObjects);

			final long numberOfdiffColumn = aggregateList.size();

			// Only create aggregate if more than two element
			// The cargo/vesselEvent and its partial pair
			if (numberOfdiffColumn > 0) {
				if (onlyDiffMode) {
					// Add to start
					validObjects.add(0, aggregateList);
				} else {
					// Add to end
					validObjects.add(aggregateList);
				}
			}
		}

		final ColumnHeaderRenderer columnHeaderCenteredRenderer = new ColumnHeaderRenderer();
		final ColumnImageCenterHeaderRenderer columnImageHeaderCenteredRenderer = new ColumnImageCenterHeaderRenderer();

		columnHeaderCenteredRenderer.setCenter(true);

		// Feed the element to be displayed

		gridColumnGroupsMap = createColumnGroups(validObjects);
		for (final Object selectedObject : validObjects) {
			// Currently only CargoAllocations
			if (selectedObject instanceof OpenSlotAllocation) {
				final OpenSlotAllocation openSlotAllocation = (OpenSlotAllocation) selectedObject;
				final GridColumnGroup gridColumnGroup = gridColumnGroupsMap.get(openSlotAllocation.getSlot().getName());
				final GridColumn gc = new GridColumn(gridColumnGroup, SWT.NONE);
				final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);

				GridViewerHelper.configureLookAndFeel(gvc);
				// Mark column for disposal on selection change
				dataColumns.add(gvc);

				gvc.setLabelProvider(new FieldTypeMapperLabelProvider(selectedObject));
				gvc.getColumn().setWidth(100);
				if (selectedDataProvider != null && selectedDataProvider.isPinnedObject(openSlotAllocation)) {
					gvc.getColumn().setHeaderRenderer(columnImageHeaderCenteredRenderer);
					gvc.getColumn().setImage(pinImage);
					gvc.getColumn().setText(""); // † <- Use this in copy/paste?

				}
			} else if (selectedObject instanceof CargoAllocation) {
				final CargoAllocation cargoAllocation = (CargoAllocation) selectedObject;
				final GridColumnGroup gridColumnGroup = gridColumnGroupsMap.get(cargoAllocation.getName());
				final GridColumn gc = new GridColumn(gridColumnGroup, SWT.NONE);
				final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);

				GridViewerHelper.configureLookAndFeel(gvc);
				// Mark column for disposal on selection change
				dataColumns.add(gvc);

				gvc.setLabelProvider(new FieldTypeMapperLabelProvider(selectedObject));
				gvc.getColumn().setWidth(100);

				if (selectedDataProvider != null && selectedDataProvider.isPinnedObject(cargoAllocation)) {
					gvc.getColumn().setHeaderRenderer(columnImageHeaderCenteredRenderer);
					gvc.getColumn().setImage(pinImage);
					gvc.getColumn().setText(""); // † <- Use this in copy/paste?

				}
				// Diff of cargo
			} else if (selectedObject instanceof DeltaPair) {
				final DeltaPair pair = (DeltaPair) selectedObject;
				if ((pair.first() != null && pair.second() != null) || onlyDiffMode) {

					final GridColumnGroup gridColumnGroup = gridColumnGroupsMap.get(pair.getName());
					final GridColumn gc = new GridColumn(gridColumnGroup, SWT.NONE);
					final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
					GridViewerHelper.configureLookAndFeel(gvc);
					// Mark column for disposal on selection change
					dataColumns.add(gvc);

					gvc.getColumn().setHeaderRenderer(columnHeaderCenteredRenderer);
					gvc.getColumn().setText("𝚫");
					gvc.setLabelProvider(new FieldTypeMapperLabelProvider(selectedObject));
					gvc.getColumn().setWidth(100);
				}
				// The aggregate element
			} else if (selectedObject instanceof List<?>) {
				final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.NONE);
				GridViewerHelper.configureLookAndFeel(gvc);
				// Mark column for disposal on selection change
				dataColumns.add(gvc);

				gvc.getColumn().setHeaderRenderer(columnHeaderCenteredRenderer);
				gvc.getColumn().setText("Σ");
				gvc.setLabelProvider(new FieldTypeMapperLabelProvider(selectedObject));
				gvc.getColumn().setWidth(100);
			} else if (selectedObject instanceof VesselEventVisit) {
				final VesselEventVisit vesselEventVisit = (VesselEventVisit) selectedObject;

				final GridColumnGroup gridColumnGroup = gridColumnGroupsMap.get(vesselEventVisit.name());
				final GridColumn gc = new GridColumn(gridColumnGroup, SWT.NONE);
				final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
				GridViewerHelper.configureLookAndFeel(gvc);
				// Mark column for disposal on selection change
				dataColumns.add(gvc);

				gvc.getColumn().setText("");
				gvc.setLabelProvider(new FieldTypeMapperLabelProvider(selectedObject));
				gvc.getColumn().setWidth(100);
				if (selectedDataProvider != null && selectedDataProvider.isPinnedObject(vesselEventVisit)) {
					gvc.getColumn().setHeaderRenderer(columnImageHeaderCenteredRenderer);
					gvc.getColumn().setImage(pinImage);
				}
			} else if (selectedObject instanceof CharterLengthEvent) {
				final CharterLengthEvent charterLengthEvent = (CharterLengthEvent) selectedObject;

				final GridColumnGroup gridColumnGroup = gridColumnGroupsMap.get(charterLengthEvent.name());
				final GridColumn gc = new GridColumn(gridColumnGroup, SWT.NONE);
				final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
				GridViewerHelper.configureLookAndFeel(gvc);
				// Mark column for disposal on selection change
				dataColumns.add(gvc);

				gvc.getColumn().setText("");
				gvc.setLabelProvider(new FieldTypeMapperLabelProvider(selectedObject));
				gvc.getColumn().setWidth(100);

				if (selectedDataProvider != null && selectedDataProvider.isPinnedObject(charterLengthEvent)) {
					gvc.getColumn().setHeaderRenderer(columnImageHeaderCenteredRenderer);
					gvc.getColumn().setImage(pinImage);
				}
			} else if (selectedObject instanceof GroupedCharterLengthEvent) {
				final GroupedCharterLengthEvent charterLengthEvent = (GroupedCharterLengthEvent) selectedObject;

				final GridColumnGroup gridColumnGroup = gridColumnGroupsMap.get(charterLengthEvent.name());
				final GridColumn gc = new GridColumn(gridColumnGroup, SWT.NONE);
				final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
				GridViewerHelper.configureLookAndFeel(gvc);
				// Mark column for disposal on selection change
				dataColumns.add(gvc);

				gvc.getColumn().setText("");
				gvc.setLabelProvider(new FieldTypeMapperLabelProvider(selectedObject));
				gvc.getColumn().setWidth(100);

				if (selectedDataProvider != null && selectedDataProvider.isPinnedObject(charterLengthEvent)) {
					gvc.getColumn().setHeaderRenderer(columnImageHeaderCenteredRenderer);
					gvc.getColumn().setImage(pinImage);
				}
			} else if (selectedObject instanceof GroupedCharterOutEvent) {
				final GroupedCharterOutEvent charterLengthEvent = (GroupedCharterOutEvent) selectedObject;

				final GridColumnGroup gridColumnGroup = gridColumnGroupsMap.get(charterLengthEvent.name());
				final GridColumn gc = new GridColumn(gridColumnGroup, SWT.NONE);
				final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
				GridViewerHelper.configureLookAndFeel(gvc);
				// Mark column for disposal on selection change
				dataColumns.add(gvc);

				gvc.getColumn().setText("");
				gvc.setLabelProvider(new FieldTypeMapperLabelProvider(selectedObject));
				gvc.getColumn().setWidth(100);

				if (selectedDataProvider != null && selectedDataProvider.isPinnedObject(charterLengthEvent)) {
					gvc.getColumn().setHeaderRenderer(columnImageHeaderCenteredRenderer);
					gvc.getColumn().setImage(pinImage);
				}
			} else if (selectedObject instanceof StartEvent || selectedObject instanceof EndEvent || selectedObject instanceof GeneratedCharterOut) {
				final Event event = (Event) selectedObject;

				final GridColumnGroup gridColumnGroup = gridColumnGroupsMap.get(event.name());
				final GridColumn gc = new GridColumn(gridColumnGroup, SWT.NONE);
				final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);
				GridViewerHelper.configureLookAndFeel(gvc);
				// Mark column for disposal on selection change
				dataColumns.add(gvc);

				gvc.getColumn().setText("");
				gvc.setLabelProvider(new FieldTypeMapperLabelProvider(selectedObject));
				gvc.getColumn().setWidth(100);

				if (selectedDataProvider != null && selectedDataProvider.isPinnedObject(event)) {
					gvc.getColumn().setHeaderRenderer(columnImageHeaderCenteredRenderer);
					gvc.getColumn().setImage(pinImage);
				}
			} else if (selectedObject instanceof MarketAllocation) {
				final MarketAllocation cargoAllocation = (MarketAllocation) selectedObject;

				final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.NONE);
				GridViewerHelper.configureLookAndFeel(gvc);

				// Mark column for disposal on selection change
				dataColumns.add(gvc);
				gvc.getColumn().setText(cargoAllocation.getSlot().getName());
				gvc.setLabelProvider(new FieldTypeMapperLabelProvider(cargoAllocation));

				gvc.getColumn().setWidth(100);

				if (selectedDataProvider != null && selectedDataProvider.isPinnedObject(cargoAllocation)) {
					gvc.getColumn().setHeaderRenderer(columnImageHeaderCenteredRenderer);
					gvc.getColumn().setImage(pinImage);
				}
			} else if (selectedObject instanceof PaperDealAllocation) {
				final PaperDealAllocation paperDealAllocation = (PaperDealAllocation) selectedObject;

				final GridColumnGroup gridColumnGroup = gridColumnGroupsMap.get(paperDealAllocation.getPaperDeal().getName());
				final GridColumn gc = new GridColumn(gridColumnGroup, SWT.NONE);
				final GridViewerColumn gvc = new GridViewerColumn(viewer, gc);

				GridViewerHelper.configureLookAndFeel(gvc);
				// Mark column for disposal on selection change
				dataColumns.add(gvc);

				gvc.setLabelProvider(new FieldTypeMapperLabelProvider(selectedObject));
				gvc.getColumn().setWidth(100);

				if (selectedDataProvider != null && selectedDataProvider.isPinnedObject(paperDealAllocation)) {
					gvc.getColumn().setHeaderRenderer(columnImageHeaderCenteredRenderer);
					gvc.getColumn().setImage(pinImage);
					gvc.getColumn().setText(""); // † <- Use this in copy/paste?

				}
			}
		}

		// Update view data
		viewer.setInput(rows);
	}

	private void createCenteringGroupRenderer(final GridColumnGroup gcg) {
		final CenteringColumnGroupHeaderRenderer renderer = new CenteringColumnGroupHeaderRenderer();
		gcg.setHeaderRenderer(renderer);
	}

	public void setCopyPasteMode(final boolean copyPasteMode) {
		setIncludedUnit(!copyPasteMode);
		options.alwaysShowRawValue = copyPasteMode;
	}

	public boolean isShowPnLCalcs() {
		return options.showPnLCalcs;
	}

	public void setShowPnLCalcs(final boolean showPnLCalcs) {
		this.options.showPnLCalcs = showPnLCalcs;
	}

}
