/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.reports.views;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import scenario.Scenario;
import scenario.ScenarioPackage;
import scenario.cargo.LoadSlot;
import scenario.contract.Entity;
import scenario.contract.GroupEntity;
import scenario.schedule.CargoAllocation;
import scenario.schedule.Schedule;
import scenario.schedule.SchedulePackage;
import scenario.schedule.Sequence;
import scenario.schedule.events.EventsPackage;
import scenario.schedule.events.FuelMixture;
import scenario.schedule.events.FuelQuantity;
import scenario.schedule.events.FuelType;
import scenario.schedule.events.Journey;
import scenario.schedule.events.ScheduledEvent;
import scenario.schedule.events.SlotVisit;
import scenario.schedule.events.VesselEventVisit;
import scenario.schedule.fleetallocation.FleetallocationPackage;

/**
 * @author hinton
 * 
 */
public class PortRotationReportView extends EMFReportView {
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.demo.reports.views.PortRotationReportView";
	private ColumnHandler dateColumn;
	private ColumnHandler vesselColumn;
	private ColumnHandler durationColumn;

	public PortRotationReportView() {

		super("com.mmxlabs.demo.reports.PortRotationReportView");

		final SchedulePackage sp = SchedulePackage.eINSTANCE;
		final EventsPackage ep = EventsPackage.eINSTANCE;
		// final CargoPackage cp = CargoPackage.eINSTANCE;
		// final PortPackage pp = PortPackage.eINSTANCE;

		final EStructuralFeature name = ScenarioPackage.eINSTANCE.getNamedObject_Name();

		addColumn("Schedule", containingScheduleFormatter);

		vesselColumn = addColumn("Vessel", objectFormatter, ScenarioPackage.eINSTANCE.getScenarioObject__GetContainer(), sp.getSequence_Vessel(),
				FleetallocationPackage.eINSTANCE.getAllocatedVessel__GetName());

		addColumn("Type", objectFormatter, ep.getScheduledEvent__GetDisplayTypeName());

		addColumn("ID", objectFormatter, ep.getScheduledEvent__GetName());
		// objectFormatter,
		// ep.getSlotVisit_CargoAllocation(),
		// sp.getCargoAllocation__GetName()
		// );

		dateColumn = addColumn("Start Date", datePartFormatter, ep.getScheduledEvent__GetLocalStartTime());
		addColumn("Start Time", timePartFormatter, ep.getScheduledEvent__GetLocalStartTime());

		addColumn("End Date", datePartFormatter, ep.getScheduledEvent__GetLocalEndTime());
		addColumn("End Time", timePartFormatter, ep.getScheduledEvent__GetLocalEndTime());

		durationColumn = addColumn("Duration (DD:HH)", new BaseFormatter() {
			@Override
			public String format(final Object object) {
				final ScheduledEvent se = (ScheduledEvent) object;
				final int duration = se.getEventDuration();
				return String.format("%02d:%02d", duration / 24, duration % 24);
			}

			@Override
			public Comparable getComparable(final Object object) {
				return ((ScheduledEvent) object).getEventDuration();
			}

		});
		addColumn("Speed", objectFormatter, ep.getJourney_Speed());
		addColumn("Distance", integerFormatter, ep.getJourney_Distance());
		addColumn("From Port", objectFormatter, ep.getJourney_FromPort(), name);
		addColumn("To Port", objectFormatter, ep.getJourney_ToPort(), name);
		addColumn("At Port", objectFormatter, ep.getPortVisit_Port(), name);
		addColumn("Route", objectFormatter, ep.getJourney_Route());

		addColumn("Load Volume", new IntegerFormatter() {
			@Override
			public Integer getIntValue(final Object object) {
				if (object instanceof SlotVisit) {
					final SlotVisit sv = (SlotVisit) object;
					final CargoAllocation ca = sv.getCargoAllocation();
					if (ca == null) {
						return null;
					}
					if (ca.getLoadSlot().equals(sv.getSlot())) {
						return (int) ca.getLoadVolume();
					}
				}
				return null;
			}
		});

		addColumn("Discharge Volume", new IntegerFormatter() {
			@Override
			public Integer getIntValue(final Object object) {
				if (object instanceof SlotVisit) {
					final SlotVisit sv = (SlotVisit) object;
					final CargoAllocation ca = sv.getCargoAllocation();
					if (ca == null) {
						return null;
					}
					if (ca.getDischargeSlot().equals(sv.getSlot())) {
						return (int) ca.getDischargeVolume();
					}
				}
				return null;
			}
		});

		for (final FuelType ft : FuelType.values()) {
			addColumn(ft.getName(), new IntegerFormatter() {
				@Override
				public Integer getIntValue(final Object object) {

					if (object instanceof FuelMixture) {
						final FuelMixture mix = (FuelMixture) object;
						for (final FuelQuantity q : mix.getFuelUsage()) {
							if (q.getFuelType().equals(ft)) {
								return (int) q.getQuantity();
							}
						}

						return 0;
					} else {
						return null;
					}
				}
			});
			addColumn(ft.getName() + " Unit Price", new IntegerFormatter() {
				@Override
				public Integer getIntValue(final Object object) {
					if (object instanceof FuelMixture) {
						for (final FuelQuantity q : ((FuelMixture) object).getFuelUsage()) {
							if (q.getFuelType().equals(ft)) {
								return (int) q.getUnitPrice();
							}
						}
					}

					return null;
				}
			});
			addColumn(ft.getName() + " Cost", new IntegerFormatter() {
				@Override
				public Integer getIntValue(final Object object) {

					if (object instanceof FuelMixture) {
						final FuelMixture mix = (FuelMixture) object;
						for (final FuelQuantity q : mix.getFuelUsage()) {
							if (q.getFuelType().equals(ft)) {
								return (int) q.getTotalPrice();
							}
						}
						return 0;
					} else {
						return null;
					}
				}
			});
		}

		addColumn("Fuel Cost", new IntegerFormatter() {
			@Override
			public Integer getIntValue(final Object object) {
				if (object instanceof FuelMixture) {
					final FuelMixture mix = (FuelMixture) object;
					return (int) mix.getTotalFuelCost();
				} else {
					return null;
				}
			}
		});
		addColumn("Charter Cost", new IntegerFormatter() {
			@Override
			public Integer getIntValue(final Object object) {
				return (int) ((ScheduledEvent) object).getHireCost();
			}
		});

		addColumn("Canal Cost", integerFormatter, ep.getJourney_RouteCost());
		addColumn("Total Cost", new IntegerFormatter() {
			@Override
			public Integer getIntValue(final Object object) {
				long total = 0;
				if (object instanceof FuelMixture) {
					total += ((FuelMixture) object).getTotalFuelCost();
				}
				if (object instanceof ScheduledEvent) {
					total += ((ScheduledEvent) object).getHireCost();
				}
				if (object instanceof Journey) {
					total += ((Journey) object).getRouteCost();
				}

				return (int) total;
			}
		});
	}

	private final List<String> entityColumnNames = new ArrayList<String>();

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
		return new IStructuredContentProvider() {

			@Override
			public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
				Display.getCurrent().asyncExec(new Runnable() {
					@Override
					public void run() {
						if (viewer.getControl().isDisposed()) {
							return;
						}
						final Set<Scenario> scenarios = new HashSet<Scenario>();
						if (newInput instanceof Iterable) {
							for (final Object element : ((Iterable<?>) newInput)) {
								if (element instanceof Schedule) {
									// find all referenced entities
									for (final String s : entityColumnNames) {
										removeColumn(s);
									}
									entityColumnNames.clear();

									EObject o = (EObject) element;
									while ((o != null) && !(o instanceof Scenario)) {
										o = o.eContainer();
									}

									if (o != null) {
										scenarios.add((Scenario) o);
									}
								}
							}

						}
						for (final Scenario scenario : scenarios) {
							addEntityColumns(scenario);
						}
						viewer.refresh();
					}

				});

			}

			@Override
			public void dispose() {

			}

			@Override
			public Object[] getElements(final Object object) {
				final ArrayList<ScheduledEvent> allEvents = new ArrayList<ScheduledEvent>();
				clearInputEquivalents();
				if (object instanceof Iterable) {
					for (final Object o : ((Iterable<?>) object)) {
						if (o instanceof Schedule) {
							for (final Sequence seq : ((Schedule) o).getSequences()) {
								allEvents.addAll(seq.getEvents());
							}
						}
					}
				}
				for (final ScheduledEvent event : allEvents) {
					if (event instanceof SlotVisit) {
						setInputEquivalents(event, Arrays.asList(new Object[] { ((SlotVisit) event).getCargoAllocation() }));
					} else {
						setInputEquivalents(event, Collections.emptyList());
					}
				}
				return allEvents.toArray();
			}
		};
	}

	protected void addEntityColumns(final Scenario o) {
		for (final Entity e : o.getContractModel().getEntities()) {
			addEntityColumn(e);
		}
		addEntityColumn(o.getContractModel().getShippingEntity());
	}

	private void addEntityColumn(final Entity entity) {
		if (!(entity instanceof GroupEntity)) {
			return;
		}
		final String title = "Profit to " + entity.getName();
		entityColumnNames.add(title);
		addColumn(title, new IntegerFormatter() {
			@Override
			public Integer getIntValue(final Object object) {
				if (object instanceof SlotVisit) {
					final SlotVisit slotVisit = (SlotVisit) object;
					if (slotVisit.getSlot() instanceof LoadSlot) {
						// display P&L
						int value = 0;
						final CargoAllocation allocation = slotVisit.getCargoAllocation();

						if (allocation == null) {
							return null;
						}

						if (allocation.getLoadRevenue() != null) {
							if (entity.equals(allocation.getLoadRevenue().getEntity())) {
								value += allocation.getLoadRevenue().getValue();
							}
						}
						if (allocation.getShippingRevenue() != null) {
							if (entity.equals(allocation.getShippingRevenue().getEntity())) {
								value += allocation.getShippingRevenue().getValue();
							}
						}
						if (allocation.getDischargeRevenue() != null) {
							if (entity.equals(allocation.getDischargeRevenue().getEntity())) {
								value += allocation.getDischargeRevenue().getValue();
							}
						}
						return value;
					}
				} else if (object instanceof VesselEventVisit) {
					final VesselEventVisit cov = (VesselEventVisit) object;
					if (cov.getRevenue() == null) {
						return null;
					}
					if (entity.equals(cov.getRevenue().getEntity())) {
						return cov.getRevenue().getValue();
					}
				}
				return null;
			}
		});
	}
}
