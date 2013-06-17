package com.mmxlabs.shiplingo.platform.reports.views;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.views.properties.PropertySheet;

import com.google.common.collect.Sets;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.types.ExtraData;
import com.mmxlabs.models.lng.types.ExtraDataContainer;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.editing.IScenarioServiceEditorInput;
import com.mmxlabs.scheduler.optimiser.TradingConstants;

/**
 * The {@link CargoPropertiesReport} is a vertical report similar in concept to the Properties View. This table is the transpose of most other tables. Columns represent the input data and rows are
 * pre-defined.
 * 
 * @author Simon Goodall
 * 
 */
public class CargoPropertiesReport extends ViewPart {

	private GridTableViewer viewer;
	private ISelectionListener selectionListener;

	/**
	 * List of dynamically generated columns to be disposed on selection changes
	 */
	private final List<GridViewerColumn> dataColumns = new LinkedList<GridViewerColumn>();

	@Override
	public void createPartControl(final Composite parent) {
		viewer = new GridTableViewer(parent);

		// Add the name column
		{
			final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.NONE);
			gvc.getColumn().setText("Name");
			gvc.setLabelProvider(new FieldTypeNameLabelProvider());
			gvc.getColumn().setWidth(70);
		}

		// Add the unit column
		{
			final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.NONE);
			gvc.getColumn().setText("Unit");
			gvc.setLabelProvider(new FieldTypeUnitLabelProvider());
			gvc.getColumn().setWidth(70);
		}

		// All other columns dynamically added.

		// Array content provider as we pass in an array of enums
		viewer.setContentProvider(new ArrayContentProvider());
		// Our input!
		viewer.setInput(FieldType.values());

		viewer.getGrid().setHeaderVisible(true);

		selectionListener = new ISelectionListener() {

			@Override
			public void selectionChanged(final IWorkbenchPart part, final ISelection selection) {
				// Filter out uninteresting views
				if (part == CargoPropertiesReport.this) {
					return;
				}
				// Ignore properties view
				if (part instanceof PropertySheet) {
					return;
				}
				// TODO: Ignore navigator

				// Dispose old data columns - clone list to try to avoid concurrent modification exceptions
				final List<GridViewerColumn> oldColumns = new ArrayList<GridViewerColumn>(dataColumns);
				dataColumns.clear();
				for (final GridViewerColumn gvc : oldColumns) {
					gvc.getColumn().dispose();
				}

				// Find valid, selected objects

				final Collection<Object> validObjects = processSelection(part, selection);

				for (final Object selectedObject : validObjects) {

					// Currently only CargoAllocations
					if (selectedObject instanceof CargoAllocation) {
						final CargoAllocation cargoAllocation = (CargoAllocation) selectedObject;

						final GridViewerColumn gvc = new GridViewerColumn(viewer, SWT.NONE);
						// Mark column for disposal on selection change
						dataColumns.add(gvc);
						gvc.getColumn().setText(cargoAllocation.getName());
						gvc.setLabelProvider(new FieldTypeMapperLabelProvider(new CargoAllocationFieldTypeMapper(cargoAllocation)));

						gvc.getColumn().setWidth(70);
					}
				}

				// Trigger view refresh
				viewer.setInput(FieldType.values());

			}
		};
		getSite().getPage().addSelectionListener(selectionListener);
	}

	@Override
	public void dispose() {

		getSite().getPage().removeSelectionListener(selectionListener);

		super.dispose();
	}

	@Override
	public void setFocus() {
		final Control control = viewer.getControl();
		if (control != null) {
			control.setFocus();
		}
	}

	static final DecimalFormat MillionsFormat = new DecimalFormat( "##,###,###,###" );
	static final DecimalFormat DollarsPermmBtuFormat = new DecimalFormat( "###.###" );

	/**
	 * The {@link FieldType} is the row data. Each enum is a different row. Currently there is no table sorter so enum order is display order
	 * 
	 */
	private static enum FieldType {

		// @formatter:off
		BUY_COST_TOTAL("Buy Cost", "$", MillionsFormat), 
		BUY_VOLUME_IN_MMBTU("Volume", "mmBTu", MillionsFormat),
		SHIPPING_COST_TOTAL("Shipping Cost", "$", MillionsFormat),
		SHIPPING_BUNKERS_COST_TOTAL("- Bunkers", "$", MillionsFormat), 
		SHIPPING_BOIL_OFF_COST_TOTAL("- Boil Off", "$", MillionsFormat), 
		SHIPPING_CHARTER_COST_TOTAL("- Charter Cost", "$", MillionsFormat), 
		SELL_REVENUE_TOTAL("Sell: Revenue", "$", MillionsFormat),
		SELL_VOLUME_IN_MMBTU("Sell: Volume", "$", MillionsFormat),
		PNL_TOTAL("P&L", "$", MillionsFormat),
		PNL_PER_MMBTU("P&L", "$/mmBTu", DollarsPermmBtuFormat);
		// @formatter:on

		private final String name;
		private final String unit;
		
		private final DecimalFormat df;


		public String getName() {
			return name;
		}

		public String getUnit() {
			return unit;
		}

		public DecimalFormat getDf() {
			return df;
		}

		private FieldType(final String name, final String unit, DecimalFormat df) {
			this.name = name;
			this.unit = unit;
			this.df = df;
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
			case BUY_COST_TOTAL: {
				final double volumeInMMBTu = (Double) getObject(FieldType.BUY_VOLUME_IN_MMBTU);
				return volumeInMMBTu * cargoAllocation.getLoadAllocation().getPrice();
			}
			case BUY_VOLUME_IN_MMBTU: {
				final int loadVolumeVolumeInM3 = cargoAllocation.getLoadVolume();
				final double cv = ((LoadSlot) cargoAllocation.getLoadAllocation().getSlot()).getSlotOrPortCV();
				return ((double) loadVolumeVolumeInM3 * cv);
			}
			case PNL_PER_MMBTU: {
				final Integer pnl = CargoPropertiesReport.getPNLValue(cargoAllocation);
				if (pnl != null) {
					final double dischargeVolumeInMMBTu = (Double) getObject(FieldType.SELL_VOLUME_IN_MMBTU);
					return (double) pnl / dischargeVolumeInMMBTu;
				}
				break;
			}
			case PNL_TOTAL: {
				return CargoPropertiesReport.getPNLValue(cargoAllocation);
			}
			case SELL_REVENUE_TOTAL: {
				final double volumeInMMBTu = (Double) getObject(FieldType.SELL_VOLUME_IN_MMBTU);
				return volumeInMMBTu * cargoAllocation.getDischargeAllocation().getPrice();
			}
			case SELL_VOLUME_IN_MMBTU: {
				final int dischargeVolumeInM3 = cargoAllocation.getDischargeVolume();
				final double cv = ((LoadSlot) cargoAllocation.getLoadAllocation().getSlot()).getSlotOrPortCV();
				return ((double) dischargeVolumeInM3 * cv);
			}
			case SHIPPING_BOIL_OFF_COST_TOTAL: {
				int cost = 0;
				cost += getFuelCost(cargoAllocation.getLoadAllocation().getSlotVisit(), Fuel.NBO, Fuel.FBO);
				cost += cargoAllocation.getLadenLeg() == null ? 0 : getFuelCost(cargoAllocation.getLadenLeg(), Fuel.NBO, Fuel.FBO);
				cost += cargoAllocation.getLadenIdle() == null ? 0 : getFuelCost(cargoAllocation.getLadenIdle(), Fuel.NBO, Fuel.FBO);
				cost += getFuelCost(cargoAllocation.getDischargeAllocation().getSlotVisit(), Fuel.NBO, Fuel.FBO);
				cost += cargoAllocation.getBallastLeg() == null ? 0 : getFuelCost(cargoAllocation.getBallastLeg(), Fuel.NBO, Fuel.FBO);
				cost += cargoAllocation.getBallastIdle() == null ? 0 : getFuelCost(cargoAllocation.getBallastIdle(), Fuel.NBO, Fuel.FBO);
				return cost;
			}
			case SHIPPING_BUNKERS_COST_TOTAL: {
				int cost = 0;
				cost += getFuelCost(cargoAllocation.getLoadAllocation().getSlotVisit(), Fuel.BASE_FUEL, Fuel.PILOT_LIGHT);
				cost += cargoAllocation.getLadenLeg() == null ? 0 : getFuelCost(cargoAllocation.getLadenLeg(), Fuel.BASE_FUEL, Fuel.PILOT_LIGHT);
				cost += cargoAllocation.getLadenIdle() == null ? 0 : getFuelCost(cargoAllocation.getLadenIdle(), Fuel.BASE_FUEL, Fuel.PILOT_LIGHT);
				cost += getFuelCost(cargoAllocation.getDischargeAllocation().getSlotVisit(), Fuel.BASE_FUEL, Fuel.PILOT_LIGHT);
				cost += cargoAllocation.getBallastLeg() == null ? 0 : getFuelCost(cargoAllocation.getBallastLeg(), Fuel.BASE_FUEL, Fuel.PILOT_LIGHT);
				cost += cargoAllocation.getBallastIdle() == null ? 0 : getFuelCost(cargoAllocation.getBallastIdle(), Fuel.BASE_FUEL, Fuel.PILOT_LIGHT);
				return cost;
			}
			case SHIPPING_CHARTER_COST_TOTAL:
				int duration = 0;
				duration += cargoAllocation.getLoadAllocation().getSlot().getDuration();
				duration += cargoAllocation.getLadenLeg() == null ? 0 : cargoAllocation.getLadenLeg().getDuration();
				duration += cargoAllocation.getLadenIdle() == null ? 0 : cargoAllocation.getLadenIdle().getDuration();
				duration += cargoAllocation.getDischargeAllocation().getSlot().getDuration();
				duration += cargoAllocation.getBallastLeg() == null ? 0 : cargoAllocation.getBallastLeg().getDuration();
				duration += cargoAllocation.getBallastIdle() == null ? 0 : cargoAllocation.getBallastIdle().getDuration();
				return duration * cargoAllocation.getSequence().getDailyHireRate();
			case SHIPPING_COST_TOTAL:
				return CargoPropertiesReport.getShippingCost(cargoAllocation);
			default:
				break;

			}
			return null;
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
	 * A label provider for the "Name" column
	 * 
	 */
	private static class FieldTypeNameLabelProvider extends CellLabelProvider implements ILabelProvider {

		@Override
		public Image getImage(final Object element) {
			return null;
		}

		@Override
		public String getText(final Object element) {
			if (element instanceof FieldType) {
				final FieldType fieldType = (FieldType) element;
				return fieldType.getName() + " (" + fieldType.getUnit() + ")";
			}
			return null;
		}

		@Override
		public void update(final ViewerCell cell) {
			cell.setText(getText(cell.getElement()));

		}

	}

	/**
	 * A label provider for the "Units" column
	 * 
	 */
	private static class FieldTypeUnitLabelProvider extends CellLabelProvider implements ILabelProvider {

		@Override
		public Image getImage(final Object element) {
			return null;
		}

		@Override
		public String getText(final Object element) {
			if (element instanceof FieldType) {
				final FieldType fieldType = (FieldType) element;
				return fieldType.getUnit();
			}
			return null;
		}

		@Override
		public void update(final ViewerCell cell) {
			cell.setText(getText(cell.getElement()));
		}

	}

	/**
	 * Label Provider created for each column. Returns text for the given {@link FieldType} enum (as the input element) for the {@link IFieldTypeMapper} object passed in during creation.
	 * 
	 */
	private static class FieldTypeMapperLabelProvider extends CellLabelProvider implements ILabelProvider {

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

		@Override
		public void update(final ViewerCell cell) {
			cell.setText(getText(cell.getElement()));

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
					obj = getSegementStart((Event) obj);
				}

				if (obj instanceof CargoAllocation) {
					validObjects.add(obj);
				} else if (obj instanceof SlotAllocation) {
					validObjects.add(((SlotAllocation) obj).getCargoAllocation());
				} else if (obj instanceof SlotVisit) {
					validObjects.add((((SlotVisit) obj).getSlotAllocation().getCargoAllocation()));
				} else if (obj instanceof Cargo || obj instanceof Slot) {
					Cargo cargo = null;
					if (obj instanceof Cargo) {
						cargo = (Cargo) obj;
					} else {
						// Must be a slot
						assert obj instanceof Slot;
						if (obj instanceof LoadSlot) {
							cargo = ((LoadSlot) obj).getCargo();
						} else if (obj instanceof DischargeSlot) {
							cargo = ((DischargeSlot) obj).getCargo();
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
								if (instance instanceof MMXRootObject) {
									final MMXRootObject rootObject = (MMXRootObject) instance;
									final ScheduleModel scheduleModel = rootObject.getSubModel(ScheduleModel.class);
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

					}

				}

			}
			// Remove invalid items
			validObjects.remove(null);
		}
		return validObjects;
	}

	/**
	 * Find the first event in the event chain. E.g. Find the SlotVisit corresponding to the Load in a Cargo.
	 * 
	 * @param event
	 * @return
	 */
	private Event getSegementStart(final Event event) {

		Event next = event;
		while (next != null && !isSegmentStart(next)) {
			next = next.getPreviousEvent();
		}
		return next;

	}

	/**
	 * TODO: Similar methods in a few places. E.g. SchedulerView
	 * 
	 * @param event
	 * @return
	 */
	private boolean isSegmentStart(final Event event) {
		if (event instanceof VesselEventVisit) {
			return true;
		} else if (event instanceof GeneratedCharterOut) {
			return true;
		} else if (event instanceof StartEvent) {
			return true;
		} else if (event instanceof EndEvent) {
			return true;
		} else if (event instanceof SlotVisit) {
			if (((SlotVisit) event).getSlotAllocation().getSlot() instanceof LoadSlot) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Get total cargo PNL value
	 * 
	 * @param container
	 * @param entity
	 * @return
	 */
	protected static Integer getPNLValue(final ExtraDataContainer container) {
		if (container == null) {
			return null;
		}

		// supplying null for the entity name indicates that the total group P&L should be returned
		final ExtraData data = container.getDataWithKey(TradingConstants.ExtraData_GroupValue);
		if (data == null) {
			return null;
		}

		return data.getValueAs(Integer.class);

	}

	protected static Integer getShippingCost(final ExtraDataContainer container) {

		if (container == null) {
			return null;
		}

		final ExtraData data = container.getDataWithKey(TradingConstants.ExtraData_ShippingCostIncBoilOff);
		if (data == null) {
			return null;
		}

		return data.getValueAs(Integer.class);
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
