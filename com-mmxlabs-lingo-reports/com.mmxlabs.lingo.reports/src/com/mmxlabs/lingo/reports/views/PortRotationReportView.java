/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.lingo.reports.IScenarioInstanceElementCollector;
import com.mmxlabs.lingo.reports.ScheduledEventCollector;
import com.mmxlabs.lingo.reports.views.formatters.BaseFormatter;
import com.mmxlabs.lingo.reports.views.formatters.CostFormatter;
import com.mmxlabs.lingo.reports.views.formatters.IntegerFormatter;
import com.mmxlabs.lingo.reports.views.formatters.NumberOfDPFormatter;
import com.mmxlabs.lingo.reports.views.formatters.PriceFormatter;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.schedule.Cooldown;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelAmount;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUnit;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.mmxcore.MMXCorePackage;

/**
 * @author hinton
 * 
 */
public class PortRotationReportView extends EMFReportView {
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.shiplingo.platform.reports.views.PortRotationReportView";
	private ColumnHandler dateColumn;
	private ColumnHandler vesselColumn;
	private ColumnHandler durationColumn;

	/**
	 * Mapping between fuel and the quantity unit displayed.
	 */
	private final Map<Fuel, FuelUnit> fuelQuanityUnits = new HashMap<Fuel, FuelUnit>();
	/**
	 * Mapping between fuel and the unit price unit displayed.
	 */
	private final Map<Fuel, FuelUnit> fuelUnitPriceUnits = new HashMap<Fuel, FuelUnit>();

	public PortRotationReportView() {

		super("com.mmxlabs.shiplingo.platform.reports.PortRotationReportView");

		{
			fuelQuanityUnits.put(Fuel.BASE_FUEL, FuelUnit.MT);
			fuelQuanityUnits.put(Fuel.FBO, FuelUnit.M3);
			fuelQuanityUnits.put(Fuel.NBO, FuelUnit.M3);
			fuelQuanityUnits.put(Fuel.PILOT_LIGHT, FuelUnit.MT);

			fuelUnitPriceUnits.put(Fuel.BASE_FUEL, FuelUnit.MT);
			fuelUnitPriceUnits.put(Fuel.FBO, FuelUnit.MMBTU);
			fuelUnitPriceUnits.put(Fuel.NBO, FuelUnit.MMBTU);
			fuelUnitPriceUnits.put(Fuel.PILOT_LIGHT, FuelUnit.MT);
		}

		final SchedulePackage sp = SchedulePackage.eINSTANCE;

		// final CargoPackage cp = CargoPackage.eINSTANCE;
		// final PortPackage pp = PortPackage.eINSTANCE;

		final EStructuralFeature name = MMXCorePackage.eINSTANCE.getNamedObject_Name();

		addScheduleColumn("Schedule", containingScheduleFormatter);

		vesselColumn = addColumn("Vessel", ColumnType.NORMAL, objectFormatter, MMXCorePackage.eINSTANCE.getMMXObject__EContainerOp(), sp.getSequence__GetName());

		addColumn("Type", ColumnType.NORMAL, objectFormatter, sp.getEvent__Type());

		addColumn("ID", ColumnType.NORMAL, objectFormatter, sp.getEvent__Name());

		addColumn("Contract", ColumnType.NORMAL, new BaseFormatter() {
			@Override
			public String format(final Object object) {
				final Event se = (Event) object;
				if (se instanceof SlotVisit) {
					SlotVisit sv = (SlotVisit) se;
					Contract c = sv.getSlotAllocation().getContract();
					if (c != null)
						return c.getName();
				}
				return null;
			}

			@Override
			public Comparable getComparable(final Object object) {
				return ((Event) object).getDuration();
			}

		});

		dateColumn = addColumn("Start Date", ColumnType.NORMAL, calendarFormatter, sp.getEvent__GetLocalStart());

		addColumn("End Date", ColumnType.NORMAL, calendarFormatter, sp.getEvent__GetLocalEnd());

		durationColumn = addColumn("Duration (DD:HH)", ColumnType.NORMAL, new BaseFormatter() {
			@Override
			public String format(final Object object) {
				final Event se = (Event) object;
				final int duration = se.getDuration();
				return String.format("%02d:%02d", duration / 24, duration % 24);
			}

			@Override
			public Comparable getComparable(final Object object) {
				return ((Event) object).getDuration();
			}

		});
		addColumn("Speed", ColumnType.NORMAL, new NumberOfDPFormatter(1), sp.getJourney_Speed());
		addColumn("Distance", ColumnType.NORMAL, integerFormatter, sp.getJourney_Distance());
		addColumn("From Port", ColumnType.NORMAL, objectFormatter, sp.getEvent_Port(), name);
		addColumn("To Port", ColumnType.NORMAL, objectFormatter, sp.getJourney_Destination(), name);
		addColumn("At Port", ColumnType.NORMAL, objectFormatter, sp.getEvent_Port(), name);
		addColumn("Route", ColumnType.NORMAL, objectFormatter, sp.getJourney_Route(), name);

		addColumn("Transfer Volume", ColumnType.NORMAL, new IntegerFormatter() {
			@Override
			public Integer getIntValue(final Object object) {
				if (object instanceof SlotVisit) {
					final SlotVisit sv = (SlotVisit) object;
					final SlotAllocation sa = sv.getSlotAllocation();
					if (sa == null) {
						return null;
					}
					return sa.getVolumeTransferred();
				}
				return null;
			}
		}).setTooltip("In m³");
		addColumn("Heel Start", ColumnType.NORMAL, new IntegerFormatter() {
			@Override
			public Integer getIntValue(final Object object) {
				if (object instanceof PortVisit) {
					final PortVisit pv = (PortVisit) object;
					return pv.getHeelAtStart();
				}
				return null;
			}
		}).setTooltip("In m³");
		addColumn("Heel End", ColumnType.NORMAL, new IntegerFormatter() {
			@Override
			public Integer getIntValue(final Object object) {
				if (object instanceof PortVisit) {
					final PortVisit pv = (PortVisit) object;
					return pv.getHeelAtEnd();
				}
				return null;
			}
		}).setTooltip("In m³");

		for (final Fuel fuelName : Fuel.values()) {
			addColumn(fuelName.toString(), ColumnType.NORMAL, new IntegerFormatter() {
				@Override
				public Integer getIntValue(final Object object) {
					if (object instanceof FuelUsage) {
						final FuelUsage mix = (FuelUsage) object;
						for (final FuelQuantity q : mix.getFuels()) {
							if (q.getFuel().equals(fuelName)) {
								final FuelUnit unit = fuelQuanityUnits.get(fuelName);
								for (final FuelAmount fa : q.getAmounts()) {
									if (fa.getUnit() == unit) {
										return fa.getQuantity();
									}
								}
							}
						}

						return 0;
					} else {
						return null;
					}
				}
			}).setTooltip("In " + fuelQuanityUnits.get(fuelName));
			addColumn(fuelName + " Unit Price", ColumnType.NORMAL, new PriceFormatter(true) {
				@Override
				public Double getDoubleValue(final Object object) {
					if (object instanceof FuelUsage) {
						final FuelUsage mix = (FuelUsage) object;
						for (final FuelQuantity q : mix.getFuels()) {
							if (q.getFuel() == fuelName) {
								final FuelUnit unit = fuelUnitPriceUnits.get(fuelName);
								for (final FuelAmount fa : q.getAmounts()) {
									if (fa.getUnit() == unit) {
										return fa.getUnitPrice();
									}
								}
							}
						}
					}
					return null;
				}
			}).setTooltip("Price per " + fuelUnitPriceUnits.get(fuelName));
			addColumn(fuelName + " Cost", ColumnType.NORMAL, new CostFormatter(true) {
				@Override
				public Integer getIntValue(final Object object) {
					if (object instanceof FuelUsage) {
						for (final FuelQuantity q : ((FuelUsage) object).getFuels()) {
							if (q.getFuel().equals(fuelName)) {
								return q.getCost();
							}
						}
						return 0;
					} else {
						return null;
					}
				}
			});
		}

		addColumn("Fuel Cost", ColumnType.NORMAL, new CostFormatter(true) {
			@Override
			public Integer getIntValue(final Object object) {
				if (object instanceof FuelUsage) {
					return ((FuelUsage) object).getFuelCost();
				} else {
					return null;
				}
			}
		});
		addColumn("Charter Cost", ColumnType.NORMAL, new CostFormatter(true) {
			@Override
			public Integer getIntValue(final Object object) {
				return (int) ((Event) object).getCharterCost();
			}
		});

		addColumn("Canal Cost", ColumnType.NORMAL, new CostFormatter(true), sp.getJourney_Toll());

		addColumn("Port Costs", ColumnType.NORMAL, new CostFormatter(true) {
			@Override
			public Integer getIntValue(final Object object) {
				if (object instanceof PortVisit) {
					final PortVisit visit = (PortVisit) object;
					return visit.getPortCost();
				}
				return null;
			}
		});

		addColumn("Total Cost", ColumnType.NORMAL, new CostFormatter(true) {
			@Override
			public Integer getIntValue(final Object object) {
				long total = 0;
				if (object instanceof FuelUsage) {
					total += ((FuelUsage) object).getFuelCost();
				}
				if (object instanceof Event) {
					total += ((Event) object).getCharterCost();
				}
				if (object instanceof Journey) {
					total += ((Journey) object).getToll();
				}
				if (object instanceof Cooldown) {
					total += ((Cooldown) object).getCost();
				}
				if (object instanceof PortVisit) {
					total += ((PortVisit) object).getPortCost();
				}

				return (int) total;
			}
		});
	}

	// private final List<String> entityColumnNames = new ArrayList<String>();

	@Override
	public void createPartControl(final Composite parent) {
		super.createPartControl(parent);
		durationColumn.column.getColumn().notifyListeners(SWT.Selection, null);
		dateColumn.column.getColumn().notifyListeners(SWT.Selection, null);
		vesselColumn.column.getColumn().notifyListeners(SWT.Selection, null);
	}

	@Override
	protected boolean handleSelections() {
		return true;
	}

	@Override
	protected ITreeContentProvider getContentProvider() {
		final ITreeContentProvider superProvider = super.getContentProvider();
		return new ITreeContentProvider() {

			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
				superProvider.inputChanged(viewer, oldInput, newInput);
			}

			@Override
			public void dispose() {
				superProvider.dispose();
			}

			@Override
			public boolean hasChildren(Object element) {
				return superProvider.hasChildren(element);
			}

			@Override
			public Object getParent(Object element) {
				return superProvider.getParent(element);
			}

			@Override
			public Object[] getElements(Object inputElement) {
				clearInputEquivalents();
				final Object[] result = superProvider.getElements(inputElement);

				for (final Object event : result) {
					if (event instanceof SlotVisit) {
						setInputEquivalents(event, Arrays.asList(new Object[] { ((SlotVisit) event).getSlotAllocation().getCargoAllocation() }));
					} else if (event instanceof VesselEventVisit) {
						setInputEquivalents(event, Arrays.asList(new Object[] { ((VesselEventVisit) event).getVesselEvent() }));
					} else {
						setInputEquivalents(event, Collections.emptyList());
					}
				}

				return result;
			}

			@Override
			public Object[] getChildren(Object parentElement) {
				return superProvider.getChildren(parentElement);
			}
		};
	}

	@Override
	protected IScenarioInstanceElementCollector getElementCollector() {
		return new ScheduledEventCollector();
	}

	// protected void addEntityColumns(final Scenario o) {
	// for (final Entity e : o.getContractModel().getEntities()) {
	// addEntityColumn(e);
	// }
	// addEntityColumn(o.getContractModel().getShippingEntity());
	// }

	// private void addEntityColumn(final Entity entity) {
	// if (!(entity instanceof GroupEntity)) {
	// return;
	// }
	// final String title = "Profit to " + entity.getName();
	// entityColumnNames.add(title);
	// addColumn(title, new IntegerFormatter() {
	// @Override
	// public Integer getIntValue(final Object object) {
	// if (object instanceof SlotVisit) {
	// final SlotVisit slotVisit = (SlotVisit) object;
	// if (slotVisit.getSlot() instanceof LoadSlot) {
	// // display P&L
	// int value = 0;
	// final CargoAllocation allocation = slotVisit.getCargoAllocation();
	//
	// if (allocation == null) {
	// return null;
	// }
	//
	// if (allocation.getLoadRevenue() != null) {
	// if (entity.equals(allocation.getLoadRevenue().getEntity())) {
	// value += allocation.getLoadRevenue().getValue();
	// }
	// }
	// if (allocation.getShippingRevenue() != null) {
	// if (entity.equals(allocation.getShippingRevenue().getEntity())) {
	// value += allocation.getShippingRevenue().getValue();
	// }
	// }
	// if (allocation.getDischargeRevenue() != null) {
	// if (entity.equals(allocation.getDischargeRevenue().getEntity())) {
	// value += allocation.getDischargeRevenue().getValue();
	// }
	// }
	// return value;
	// }
	// } else if (object instanceof VesselEventVisit) {
	// final VesselEventVisit cov = (VesselEventVisit) object;
	// if (cov.getRevenue() == null) {
	// return null;
	// }
	// if (entity.equals(cov.getRevenue().getEntity())) {
	// return cov.getRevenue().getValue();
	// }
	// }
	// return null;
	// }
	// });
	// }
}
