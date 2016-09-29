/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.views.properties.PropertySheet;

import com.google.common.collect.Sets;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.BasicSlotPNLDetails;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Cooldown;
import com.mmxlabs.models.lng.schedule.EntityProfitAndLoss;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.GeneralPNLDetails;
import com.mmxlabs.models.lng.schedule.GroupProfitAndLoss;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.MarketAllocation;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotPNLDetails;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelUtils;
import com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.ui.tabular.GridViewerHelper;
import com.mmxlabs.models.ui.tabular.renderers.ColumnHeaderRenderer;
import com.mmxlabs.rcp.common.SelectionHelper;
import com.mmxlabs.rcp.common.ViewerHelper;
import com.mmxlabs.rcp.common.actions.CopyToClipboardActionFactory;
import com.mmxlabs.rcp.common.actions.PackActionFactory;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.editing.IScenarioServiceEditorInput;

/**
 * The {@link CargoEconsReport} is a vertical report similar in concept to the Properties View. This table is the transpose of most other tables. Columns represent the input data and rows are
 * pre-defined.
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
	private org.eclipse.e4.ui.workbench.modeling.ISelectionListener selectionListener;

	/**
	 * List of dynamically generated columns to be disposed on selection changes
	 */
	private final List<GridViewerColumn> dataColumns = new LinkedList<GridViewerColumn>();

	@Override
	public void createPartControl(final Composite parent) {
		viewer = new GridTableViewer(parent);
		GridViewerHelper.configureLookAndFeel(viewer);

		// Add the name column
		{
			final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.NONE);
			gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
			gvc.getColumn().setText("Name");
			gvc.setLabelProvider(new FieldTypeNameLabelProvider());
			gvc.getColumn().setWidth(100);
		}

		// All other columns dynamically added.

		// Array content provider as we pass in an array of enums
		viewer.setContentProvider(new ArrayContentProvider());
		// Our input!
		viewer.setInput(FieldType.getFilteredValues());
		viewer.getGrid().setHeaderVisible(true);

		selectionListener = new org.eclipse.e4.ui.workbench.modeling.ISelectionListener() {

			@Override
			public void selectionChanged(final MPart part, final Object selectedObjects) {
				final IWorkbenchPart e3part = SelectionHelper.getE3Part(part);
				{
					// TODO: Ignore navigator
					if (e3part == CargoEconsReport.this) {
						return;
					}
					if (e3part instanceof PropertySheet) {
						return;
					}
				}

				final ISelection selection = SelectionHelper.adaptSelection(selectedObjects);

				// Dispose old data columns - clone list to try to avoid concurrent modification exceptions
				final List<GridViewerColumn> oldColumns = new ArrayList<GridViewerColumn>(dataColumns);
				dataColumns.clear();
				for (final GridViewerColumn gvc : oldColumns) {
					gvc.getColumn().dispose();
				}

				// Find valid, selected objects

				final Collection<Object> validObjects = CargoEconsReport.this.processSelection(e3part, selection);

				for (final Object selectedObject : validObjects) {

					// Currently only CargoAllocations
					if (selectedObject instanceof CargoAllocation) {
						final CargoAllocation cargoAllocation = (CargoAllocation) selectedObject;

						final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.NONE);
						gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());
						// Mark column for disposal on selection change
						dataColumns.add(gvc);
						gvc.getColumn().setText(cargoAllocation.getName());
						gvc.setLabelProvider(new FieldTypeMapperLabelProvider(new CargoAllocationFieldTypeMapper(cargoAllocation)));

						gvc.getColumn().setWidth(100);
					} else if (selectedObject instanceof MarketAllocation) {
						final MarketAllocation cargoAllocation = (MarketAllocation) selectedObject;

						final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.NONE);
						gvc.getColumn().setHeaderRenderer(new ColumnHeaderRenderer());

						// Mark column for disposal on selection change
						dataColumns.add(gvc);
						gvc.getColumn().setText(cargoAllocation.getSlot().getName());
						gvc.setLabelProvider(new FieldTypeMapperLabelProvider(new MarketAllocationFieldTypeMapper(cargoAllocation)));

						gvc.getColumn().setWidth(100);
					}
				}

				// Trigger view refresh
				ViewerHelper.setInput(viewer, true, FieldType.getFilteredValues());

			}
		};

		final Action packAction = PackActionFactory.createPackColumnsAction(viewer);
		final Action copyAction = CopyToClipboardActionFactory.createCopyToClipboardAction(viewer);
		getViewSite().getActionBars().getToolBarManager().add(packAction);
		getViewSite().getActionBars().getToolBarManager().add(copyAction);
		getViewSite().getActionBars().setGlobalActionHandler(ActionFactory.COPY.getId(), copyAction);

		// Get e4 selection service!
		final ESelectionService service = (ESelectionService) getSite().getService(ESelectionService.class);
		service.addPostSelectionListener(selectionListener);

		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "com.mmxlabs.lingo.doc.Reports_CargoEcons");
	}

	@Override
	public void dispose() {

		final ESelectionService service = (ESelectionService) getSite().getService(ESelectionService.class);
		service.removePostSelectionListener(selectionListener);

		super.dispose();
	}

	@Override
	public void setFocus() {
		ViewerHelper.setFocus(viewer);
	}

	static final DecimalFormat DollarsFormat = new DecimalFormat("$##,###,###,###");
	static final DecimalFormat VolumeMMBtuFormat = new DecimalFormat("##,###,###,###mmBtu");
	static final DecimalFormat DollarsPerMMBtuFormat = new DecimalFormat("$###.###/mmBtu");

	/**
	 * The {@link FieldType} is the row data. Each enum is a different row. Currently there is no table sorter so enum order is display order
	 * 
	 */
	private static enum FieldType {

		// @formatter:off
		BUY_COST_TOTAL("Purchase Cost", "$", DollarsFormat), 
		BUY_PRICE("> Purchase Price", "$/mmBTu", DollarsPerMMBtuFormat), 
		BUY_VOLUME_IN_MMBTU("> Volume", "mmBTu", VolumeMMBtuFormat),
		SHIPPING_COST_TOTAL("Shipping Cost", "$", DollarsFormat),
		SHIPPING_BUNKERS_COST_TOTAL("> Bunkers", "$", DollarsFormat), 
		SHIPPING_PORT_COST_TOTAL("> Port", "$", DollarsFormat), 
		SHIPPING_CANAL_COST_TOTAL("> Canal", "$", DollarsFormat), 
		SHIPPING_BOIL_OFF_COST_TOTAL("> Boil-off", "$", DollarsFormat), 
		SHIPPING_CHARTER_COST_TOTAL("> Charter fees", "$", DollarsFormat), 
		SELL_REVENUE_TOTAL("Sale Revenue", "$", DollarsFormat),
		SELL_PRICE("> Sales Price", "$/mmBTu", DollarsPerMMBtuFormat), 
		SELL_VOLUME_IN_MMBTU("> Volume", "mmBtu", VolumeMMBtuFormat),
		EQUITY_PNL("Equity P&L", "$", DollarsFormat),
		ADDITIONAL_PNL("Addn. P&L", "$", DollarsFormat),
		PNL_TOTAL("P&L", "$", DollarsFormat),
		PNL_PER_MMBTU("Margin", "$/mmBTu", DollarsPerMMBtuFormat);
		// @formatter:on

		private final String name;

		private final DecimalFormat df;

		public String getName() {
			return name;
		}

		public DecimalFormat getDf() {
			return df;
		}

		private FieldType(final String name, final String unit, final DecimalFormat df) {
			this.name = name;
			this.df = df;
		}
		
		public static FieldType[] getFilteredValues() {
			Set<FieldType> restrictedValues = restrictedValues();
			return Arrays.stream(values()).filter(e -> (!restrictedValues.contains(e))).toArray(size -> new FieldType[size]);
		}
		
		public static Set<FieldType> restrictedValues() {
			Set<FieldType> restricted = new HashSet<>();
			if (!SecurityUtils.getSubject().isPermitted("features:report-equity-book")) {
				restricted.add(EQUITY_PNL);
			}
			return restricted;
		}
	}

	/**
	 * Interface to provide table data for a given {@link FieldType} enum on some kind of wrapped data.
	 * 
	 */
	private static interface IFieldTypeMapper {

		/**
		 * Return the raw object data for the field type
		 * 
		 * @param fieldType
		 * @return
		 */
		Object getObject(FieldType fieldType);

		/**
		 * Return the string representation (to display) for the field type
		 * 
		 * @param fieldType
		 * @return
		 */
		String getText(FieldType fieldType);

		public abstract boolean isFleet();
	}

	/**
	 * A {@link IFieldTypeMapper} implementation for {@link CargoAllocation} objects
	 */
	private static class CargoAllocationFieldTypeMapper implements IFieldTypeMapper {

		private final CargoAllocation cargoAllocation;

		CargoAllocationFieldTypeMapper(final CargoAllocation cargoAllocation) {
			this.cargoAllocation = cargoAllocation;
		}

		@Override
		public Object getObject(final FieldType fieldType) {

			switch (fieldType) {
			case BUY_PRICE: {
				// Returns first purchase price
				for (final SlotAllocation allocation : cargoAllocation.getSlotAllocations()) {
					if (allocation.getSlot() instanceof LoadSlot) {
						return allocation.getPrice();
					}
				}
				return 0.0;
			}
			case SELL_PRICE: {
				// Returns first sales price
				for (final SlotAllocation allocation : cargoAllocation.getSlotAllocations()) {
					if (allocation.getSlot() instanceof DischargeSlot) {
						return allocation.getPrice();
					}
				}
				return 0.0;
			}
			case BUY_COST_TOTAL: {

				long cost = 0;
				for (final SlotAllocation allocation : cargoAllocation.getSlotAllocations()) {
					if (allocation.getSlot() instanceof LoadSlot) {
						final int volumeInMMBTu = allocation.getEnergyTransferred();
						cost += (double) volumeInMMBTu * allocation.getPrice();
					}
				}
				return cost;

			}
			case BUY_VOLUME_IN_MMBTU: {
				long cost = 0;
				for (final SlotAllocation allocation : cargoAllocation.getSlotAllocations()) {
					if (allocation.getSlot() instanceof LoadSlot) {
						final int volumeInMMBTu = allocation.getEnergyTransferred();
						cost += volumeInMMBTu;
					}
				}
				return cost;
			}
			case PNL_PER_MMBTU: {
				final Integer pnl = CargoEconsReport.getPNLValue(cargoAllocation);
				if (pnl != null) {
					final double dischargeVolumeInMMBTu = ((Number) getObject(FieldType.SELL_VOLUME_IN_MMBTU)).doubleValue();
					return (double) pnl / dischargeVolumeInMMBTu;
				}
				break;
			}
			case EQUITY_PNL: {
				return CargoEconsReport.getEquityPNLValue(cargoAllocation);
			}
			case ADDITIONAL_PNL: {
				return CargoEconsReport.getAdditionalPNLValue(cargoAllocation);
			}
			case PNL_TOTAL: {
				return CargoEconsReport.getPNLValue(cargoAllocation);
			}
			case SELL_REVENUE_TOTAL: {
				long revenue = 0;
				for (final SlotAllocation allocation : cargoAllocation.getSlotAllocations()) {
					if (allocation.getSlot() instanceof DischargeSlot) {
						final int volumeInMMBTu = allocation.getEnergyTransferred();
						revenue += (double) volumeInMMBTu * allocation.getPrice();
					}
				}
				return revenue;
			}
			case SELL_VOLUME_IN_MMBTU: {
				long dischargeVolume = 0;
				for (final SlotAllocation allocation : cargoAllocation.getSlotAllocations()) {
					if (allocation.getSlot() instanceof DischargeSlot) {
						final int volumeInMMBTu = allocation.getEnergyTransferred();
						dischargeVolume += volumeInMMBTu;
					}
				}
				return dischargeVolume;
			}
			case SHIPPING_BOIL_OFF_COST_TOTAL: {
				int cost = 0;
				for (final Event event : cargoAllocation.getEvents()) {
					if (event instanceof FuelUsage) {
						final FuelUsage fuelUsage = (FuelUsage) event;
						cost += getFuelCost(fuelUsage, Fuel.NBO, Fuel.FBO);
					}
				}
				return cost;
			}
			case SHIPPING_BUNKERS_COST_TOTAL: {
				int cost = 0;

				for (final Event event : cargoAllocation.getEvents()) {
					if (event instanceof FuelUsage) {
						final FuelUsage fuelUsage = (FuelUsage) event;
						cost += getFuelCost(fuelUsage, Fuel.BASE_FUEL, Fuel.PILOT_LIGHT);
					}
				}

				return cost;
			}
			case SHIPPING_PORT_COST_TOTAL: {
				int cost = 0;
				for (final Event event : cargoAllocation.getEvents()) {
					if (event instanceof PortVisit) {
						final PortVisit portVisit = (PortVisit) event;
						cost += portVisit.getPortCost();
					}
				}

				return cost;
			}
			case SHIPPING_CANAL_COST_TOTAL: {
				int cost = 0;
				for (final Event event : cargoAllocation.getEvents()) {
					if (event instanceof Journey) {
						final Journey journey = (Journey) event;
						cost += journey.getToll();
					}
				}

				return cost;
			}
			case SHIPPING_CHARTER_COST_TOTAL:
				int charterCost = 0;
				for (final Event event : cargoAllocation.getEvents()) {
					charterCost += event.getCharterCost();
				}
				return charterCost;
			case SHIPPING_COST_TOTAL:
				return CargoEconsReport.getShippingCost(cargoAllocation);
			default:
				break;

			}
			return null;
		}

		@Override
		public boolean isFleet() {

			return !(cargoAllocation.getSequence().isSpotVessel() || cargoAllocation.getSequence().isTimeCharterVessel());
		}

		@Override
		public String getText(final FieldType fieldType) {

			final Object obj = getObject(fieldType);
			if (obj != null) {
				// TODO: Format appropriately, maybe have a format specifier on the enum?
				return fieldType.getDf().format(obj);
			}

			return null;
		}
	}

	/**
	 * A {@link IFieldTypeMapper} implementation for {@link MarketAllocation} objects
	 */
	private static class MarketAllocationFieldTypeMapper implements IFieldTypeMapper {

		private final MarketAllocation marketAllocation;

		MarketAllocationFieldTypeMapper(final MarketAllocation marketAllocation) {
			this.marketAllocation = marketAllocation;
		}

		@Override
		public Object getObject(final FieldType fieldType) {

			switch (fieldType) {
			case BUY_COST_TOTAL: {
				// Find the CV & price
				final double cv;
				final double price;
				final SlotAllocation allocation = marketAllocation.getSlotAllocation();
				if (allocation.getSlot() instanceof LoadSlot) {
					final LoadSlot loadSlot = (LoadSlot) allocation.getSlot();
					cv = loadSlot.getSlotOrDelegatedCV();
					price = allocation.getPrice();
				} else {
					cv = getMarketCV(marketAllocation.getMarket());
					price = marketAllocation.getPrice();
				}

				final int loadVolumeVolumeInM3 = allocation.getVolumeTransferred();
				final double volumeInMMBTu = (loadVolumeVolumeInM3 * cv);
				return volumeInMMBTu * price;
			}
			case BUY_VOLUME_IN_MMBTU: {
				// / Find the CV
				final double cv;
				final SlotAllocation allocation = marketAllocation.getSlotAllocation();
				if (allocation.getSlot() instanceof LoadSlot) {
					final LoadSlot loadSlot = (LoadSlot) allocation.getSlot();
					cv = loadSlot.getSlotOrDelegatedCV();
				} else {
					cv = getMarketCV(marketAllocation.getMarket());
				}

				final int loadVolumeVolumeInM3 = allocation.getVolumeTransferred();
				final double volumeInMMBTu = (loadVolumeVolumeInM3 * cv);
				return volumeInMMBTu;
			}
			case PNL_PER_MMBTU: {
				final Integer pnl = CargoEconsReport.getPNLValue(marketAllocation);
				if (pnl != null) {
					final Number number = (Number) getObject(FieldType.SELL_VOLUME_IN_MMBTU);
					if (number != null) {
						final double dischargeVolumeInMMBTu = number.doubleValue();
						return (double) pnl / dischargeVolumeInMMBTu;
					}
				}
				break;

			}
			case ADDITIONAL_PNL: {
				return CargoEconsReport.getAdditionalPNLValue(marketAllocation);
			}
			case PNL_TOTAL: {
				return CargoEconsReport.getPNLValue(marketAllocation);
			}
			case SELL_REVENUE_TOTAL: {
				// Find the CV & price
				final double cv;
				final double price;
				final SlotAllocation allocation = marketAllocation.getSlotAllocation();
				if (allocation.getSlot() instanceof LoadSlot) {
					final LoadSlot loadSlot = (LoadSlot) allocation.getSlot();
					cv = loadSlot.getSlotOrDelegatedCV();
					price = marketAllocation.getPrice();
				} else {
					cv = getMarketCV(marketAllocation.getMarket());
					price = allocation.getPrice();
				}

				final int volumeInM3 = allocation.getVolumeTransferred();
				final double volumeInMMBTu = (volumeInM3 * cv);
				return volumeInMMBTu * price;
			}
			case SELL_VOLUME_IN_MMBTU: {
				double cv = 0.0;
				// Find the CV
				final SlotAllocation allocation = marketAllocation.getSlotAllocation();
				if (allocation.getSlot() instanceof LoadSlot) {
					final LoadSlot loadSlot = (LoadSlot) allocation.getSlot();
					// TODO: Avg CV
					cv = loadSlot.getSlotOrDelegatedCV();
				} else {
					cv = getMarketCV(marketAllocation.getMarket());
				}
				final int loadVolumeVolumeInM3 = allocation.getVolumeTransferred();
				final double volumeInMMBTu = (loadVolumeVolumeInM3 * cv);
				return volumeInMMBTu;
			}
			case SHIPPING_BOIL_OFF_COST_TOTAL:
			case SHIPPING_BUNKERS_COST_TOTAL:
			case SHIPPING_PORT_COST_TOTAL:
			case SHIPPING_CANAL_COST_TOTAL:
			case SHIPPING_CHARTER_COST_TOTAL:
			case SHIPPING_COST_TOTAL:
			default:
				break;

			}
			return null;
		}

		@Override
		public boolean isFleet() {
			return false;
		}

		@Override
		public String getText(final FieldType fieldType) {

			final Object obj = getObject(fieldType);
			if (obj != null) {
				// TODO: Format appropriately, maybe have a format specifier on the enum?
				return fieldType.getDf().format(obj);
			}

			return null;
		}

		private double getMarketCV(final SpotMarket market) {
			if (market instanceof DESPurchaseMarket) {
				return ((DESPurchaseMarket) market).getCv();
			}
			if (market instanceof FOBPurchasesMarket) {
				return ((FOBPurchasesMarket) market).getCv();
			}
			return 0.0;

		}
	}

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
			if (element instanceof FieldType) {
				final FieldType fieldType = (FieldType) element;
				return fieldType.getName(); // + " (" + fieldType.getUnit() + ")";
			}
			return null;
		}

		// @Override
		// public void update(final ViewerCell cell) {
		// cell.setText(getText(cell.getElement()));
		//
		// }

	}

	/**
	 * Label Provider created for each column. Returns text for the given {@link FieldType} enum (as the input element) for the {@link IFieldTypeMapper} object passed in during creation.
	 * 
	 */
	private static class FieldTypeMapperLabelProvider extends ColumnLabelProvider {

		private final IFieldTypeMapper fieldTypeMapper;

		public FieldTypeMapperLabelProvider(final IFieldTypeMapper fieldTypeMapper) {
			this.fieldTypeMapper = fieldTypeMapper;
		}

		@Override
		public Image getImage(final Object element) {
			return null;
		}

		@Override
		public String getText(final Object element) {
			if (element instanceof FieldType) {
				return fieldTypeMapper.getText((FieldType) element);
			}
			return null;
		}

		// @Override
		// public void update(final ViewerCell cell) {
		// cell.setText(getText(cell.getElement()));
		//
		// }
		//
		@Override
		public Color getForeground(final Object element) {
			if (element instanceof FieldType) {
				final FieldType ft = ((FieldType) element);
				if (ft == FieldType.SHIPPING_BOIL_OFF_COST_TOTAL || (ft == FieldType.SHIPPING_CHARTER_COST_TOTAL && fieldTypeMapper.isFleet())) {
					return Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
				}
			}
			return null;
		}
	}

	/**
	 * This method processes an {@link ISelection} object and return a list of objects that this report knows how to process. Currently we try to find associated {@link CargoAllocation} objects.
	 * 
	 * @param selection
	 * @return
	 */
	private Collection<Object> processSelection(final IWorkbenchPart part, final ISelection selection) {
		final Collection<Object> validObjects = new LinkedHashSet<Object>();

		if (selection instanceof IStructuredSelection) {

			final Iterator<?> itr = ((IStructuredSelection) selection).iterator();
			while (itr.hasNext()) {
				Object obj = itr.next();
				if (obj instanceof Event) {
					obj = ScheduleModelUtils.getSegmentStart((Event) obj);
				}

				if (obj instanceof CargoAllocation) {
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
				} else if (obj instanceof Cargo || obj instanceof Slot) {
					Cargo cargo = null;
					Slot slot = null;
					if (obj instanceof Cargo) {
						cargo = (Cargo) obj;
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

					if (cargo != null) {

						// TODO: Look up ScheduleModel somehow....

						if (part instanceof IEditorPart) {
							final IEditorPart editorPart = (IEditorPart) part;
							final IEditorInput editorInput = editorPart.getEditorInput();
							if (editorInput instanceof IScenarioServiceEditorInput) {
								final IScenarioServiceEditorInput scenarioServiceEditorInput = (IScenarioServiceEditorInput) editorInput;
								final ScenarioInstance scenarioInstance = scenarioServiceEditorInput.getScenarioInstance();
								final EObject instance = scenarioInstance.getInstance();
								if (instance instanceof LNGScenarioModel) {
									final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) instance;
									final ScheduleModel scheduleModel = lngScenarioModel.getScheduleModel();
									if (scheduleModel != null) {
										final Schedule schedule = scheduleModel.getSchedule();
										if (schedule != null) {
											for (final CargoAllocation cargoAllocation : schedule.getCargoAllocations()) {
												if (cargo == cargoAllocation.getInputCargo()) {
													validObjects.add(cargoAllocation);
													break;
												}
											}
										}
									}
								}
							}
						}
					} else if (slot != null) {

						// TODO: Look up ScheduleModel somehow....

						if (part instanceof IEditorPart) {
							final IEditorPart editorPart = (IEditorPart) part;
							final IEditorInput editorInput = editorPart.getEditorInput();
							if (editorInput instanceof IScenarioServiceEditorInput) {
								final IScenarioServiceEditorInput scenarioServiceEditorInput = (IScenarioServiceEditorInput) editorInput;
								final ScenarioInstance scenarioInstance = scenarioServiceEditorInput.getScenarioInstance();
								final EObject instance = scenarioInstance.getInstance();
								if (instance instanceof LNGScenarioModel) {
									final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) instance;
									final ScheduleModel scheduleModel = lngScenarioModel.getScheduleModel();
									if (scheduleModel != null) {
										final Schedule schedule = scheduleModel.getSchedule();
										if (schedule != null) {
											for (final MarketAllocation marketAllocation : schedule.getMarketAllocations()) {
												if (slot == marketAllocation.getSlot()) {
													validObjects.add(marketAllocation);
													break;
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
			// Remove invalid items
			validObjects.remove(null);
		}
		return validObjects;
	}

	/**
	 * Get total cargo PNL value
	 * 
	 * @param container
	 * @param entity
	 * @return
	 */
	protected static Integer getPNLValue(final ProfitAndLossContainer container) {
		if (container == null) {
			return null;
		}

		final GroupProfitAndLoss groupProfitAndLoss = container.getGroupProfitAndLoss();
		if (groupProfitAndLoss == null) {
			return null;
		}
		// Rounding!
		return (int) groupProfitAndLoss.getProfitAndLoss();
	}

	protected static Integer getAdditionalPNLValue(final ProfitAndLossContainer container) {

		int addnPNL = 0;

		if (container != null) {

			final GroupProfitAndLoss dataWithKey = container.getGroupProfitAndLoss();
			if (dataWithKey != null) {
				for (final GeneralPNLDetails generalPNLDetails : container.getGeneralPNLDetails()) {
					if (generalPNLDetails instanceof SlotPNLDetails) {
						final SlotPNLDetails slotPNLDetails = (SlotPNLDetails) generalPNLDetails;
						for (final GeneralPNLDetails details : slotPNLDetails.getGeneralPNLDetails()) {
							if (details instanceof BasicSlotPNLDetails) {
								addnPNL += ((BasicSlotPNLDetails) details).getAdditionalPNL();
								addnPNL += ((BasicSlotPNLDetails) details).getExtraShippingPNL();
								addnPNL += ((BasicSlotPNLDetails) details).getExtraUpsidePNL();
							}
						}
					}
				}
			}
		}

		return addnPNL;
	}

	public static Integer getEquityPNLValue(final ProfitAndLossContainer container) {

		int equityPNL = 0;

		if (container != null) {

			final GroupProfitAndLoss dataWithKey = container.getGroupProfitAndLoss();
			if (dataWithKey != null) {
				EList<EntityProfitAndLoss> entityProfitAndLosses = dataWithKey.getEntityProfitAndLosses();
				for (EntityProfitAndLoss entityProfitAndLoss : entityProfitAndLosses) {
					if (entityProfitAndLoss.getEntityBook().equals(entityProfitAndLoss.getEntity().getUpstreamBook())) {
						equityPNL += entityProfitAndLoss.getProfitAndLoss();
					}
				}
				
			}
		}

		return equityPNL;
	}

	// /**
	// * Get total cargo PNL value excluding time charter rate
	// *
	// * @param container
	// * @param entity
	// * @return
	// */
	// private static Integer getPNLValueNoTC(final ProfitAndLossContainer container) {
	// if (container == null) {
	// return null;
	// }
	//
	// final GroupProfitAndLoss groupProfitAndLoss = container.getGroupProfitAndLossNoTimeCharter();
	// if (groupProfitAndLoss == null) {
	// return null;
	// }
	// // Rounding!
	// return (int) groupProfitAndLoss.getProfitAndLoss();
	// }

	protected static Integer getShippingCost(final CargoAllocation cargoAllocation) {

		if (cargoAllocation == null) {
			return null;
		}

		// Bit of a double count here, but need to decide what to add to the model
		int shippingCost = 0;
		int charterCost = 0;
		for (final Event event : cargoAllocation.getEvents()) {

			charterCost += event.getCharterCost();

			if (event instanceof SlotVisit) {
				final SlotVisit slotVisit = (SlotVisit) event;
				// Port Costs
				shippingCost += slotVisit.getPortCost();
			}

			if (event instanceof Journey) {
				final Journey journey = (Journey) event;
				// Canal Costs
				shippingCost += journey.getToll();
			}

			if (event instanceof FuelUsage) {
				final FuelUsage fuelUsage = (FuelUsage) event;
				// Base fuel costs
				shippingCost += getFuelCost(fuelUsage, Fuel.BASE_FUEL, Fuel.PILOT_LIGHT);
			}

			if (event instanceof Cooldown) {
				final Cooldown cooldown = (Cooldown) event;
				shippingCost += cooldown.getCost();
			}
		}

		// Add on chartering costs
		if (cargoAllocation.getSequence().isSpotVessel() || cargoAllocation.getSequence().isTimeCharterVessel()) {
			shippingCost += charterCost;
		}
		return shippingCost;

	}

	protected static int getFuelCost(final FuelUsage fuelUser, final Fuel... fuels) {
		final Set<Fuel> fuelsOfInterest = Sets.newHashSet(fuels);
		int sum = 0;
		if (fuelUser != null) {
			final EList<FuelQuantity> fuelQuantities = fuelUser.getFuels();
			for (final FuelQuantity fq : fuelQuantities) {
				if (fuelsOfInterest.contains(fq.getFuel())) {
					sum += fq.getCost();
				}
			}
		}
		return sum;
	}

}
