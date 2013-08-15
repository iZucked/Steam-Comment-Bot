/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.reports.views;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

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
import com.mmxlabs.shiplingo.platform.reports.IScenarioInstanceElementCollector;
import com.mmxlabs.shiplingo.platform.reports.ScheduledEventCollector;

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

		vesselColumn = addColumn("Vessel", objectFormatter, MMXCorePackage.eINSTANCE.getMMXObject__EContainerOp(), sp.getSequence__GetName());

		addColumn("Type", objectFormatter, sp.getEvent__Type());

		addColumn("ID", objectFormatter, sp.getEvent__Name());

		dateColumn = addColumn("Start Date", calendarFormatter, sp.getEvent__GetLocalStart());

		addColumn("End Date", calendarFormatter, sp.getEvent__GetLocalEnd());

		durationColumn = addColumn("Duration (DD:HH)", new BaseFormatter() {
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
		addColumn("Speed", objectFormatter, sp.getJourney_Speed());
		addColumn("Distance", integerFormatter, sp.getJourney_Distance());
		addColumn("From Port", objectFormatter, sp.getEvent_Port(), name);
		addColumn("To Port", objectFormatter, sp.getJourney_Destination(), name);
		addColumn("At Port", objectFormatter, sp.getEvent_Port(), name);
		addColumn("Route", objectFormatter, sp.getJourney_Route(), name);

		addColumn("Transfer Volume", "In m3", new IntegerFormatter() {
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
		});

		for (final Fuel fuelName : Fuel.values()) {
			addColumn(fuelName.toString(), "In " + fuelQuanityUnits.get(fuelName), new IntegerFormatter() {
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
			});
			addColumn(fuelName + " Unit Price", "Price per " + fuelUnitPriceUnits.get(fuelName), new PriceFormatter() {
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
			});
			addColumn(fuelName + " Cost", new CostFormatter() {
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

		addColumn("Fuel Cost", new CostFormatter() {
			@Override
			public Integer getIntValue(final Object object) {
				if (object instanceof FuelUsage) {
					return ((FuelUsage) object).getFuelCost();
				} else {
					return null;
				}
			}
		});
		addColumn("Charter Cost", new CostFormatter() {
			@Override
			public Integer getIntValue(final Object object) {
				return (int) ((Event) object).getCharterCost();
			}
		});

		addColumn("Canal Cost", new CostFormatter(), sp.getJourney_Toll());

		addColumn("Port Costs", new CostFormatter() {
			@Override
			public Integer getIntValue(final Object object) {
				if (object instanceof PortVisit) {
					final PortVisit visit = (PortVisit) object;
					return visit.getPortCost();
				}
				return null;
			}
		});

		addColumn("Total Cost", new CostFormatter() {
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
	protected IStructuredContentProvider getContentProvider() {
		final IStructuredContentProvider superProvider = super.getContentProvider();
		return new IStructuredContentProvider() {

			@Override
			public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
				superProvider.inputChanged(viewer, oldInput, newInput);
				// Display.getCurrent().asyncExec(new Runnable() {
				// @Override
				// public void run() {
				// if (viewer.getControl().isDisposed()) {
				// return;
				// }
				// final Set<Scenario> scenarios = new HashSet<Scenario>();
				// if (newInput instanceof Iterable) {
				// for (final Object element : ((Iterable<?>) newInput)) {
				// if (element instanceof Schedule) {
				// // find all referenced entities
				// for (final String s : entityColumnNames) {
				// removeColumn(s);
				// }
				// entityColumnNames.clear();
				//
				// EObject o = (EObject) element;
				// while ((o != null) && !(o instanceof Scenario)) {
				// o = o.eContainer();
				// }
				//
				// if (o != null) {
				// scenarios.add((Scenario) o);
				// }
				// }
				// }
				//
				// }
				// for (final Scenario scenario : scenarios) {
				// addEntityColumns(scenario);
				// }
				// viewer.refresh();
				// }
				//
				// });

			}

			@Override
			public void dispose() {
				superProvider.dispose();
			}

			@Override
			public Object[] getElements(final Object object) {
				clearInputEquivalents();
				final Object[] result = superProvider.getElements(object);

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
