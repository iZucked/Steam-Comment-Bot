/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.reports.views;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;

import com.google.common.collect.Lists;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.fleet.CharterOutEvent;
import com.mmxlabs.models.lng.fleet.DryDockEvent;
import com.mmxlabs.models.lng.fleet.MaintenanceEvent;
import com.mmxlabs.models.lng.fleet.VesselEvent;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.types.ExtraData;
import com.mmxlabs.models.lng.types.ExtraDataContainer;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.shiplingo.platform.reports.IScenarioInstanceElementCollector;
import com.mmxlabs.shiplingo.platform.reports.ScheduleElementCollector;
import com.mmxlabs.trading.optimiser.TradingConstants;

/**
 * @since 2.0 `
 */
public class SchedulePnLReport extends EMFReportView {
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.shiplingo.platform.reports.views.SchedulePnLReport";

	public SchedulePnLReport() {
		super("com.mmxlabs.shiplingo.platform.reports.CargoPnLReportView");

		final SchedulePackage s = SchedulePackage.eINSTANCE;

		final EAttribute name = MMXCorePackage.eINSTANCE.getNamedObject_Name();

		addScheduleColumn("Schedule", containingScheduleFormatter);

		addColumn("ID", objectFormatter, s.getEvent__Name());

		addColumn("Type", new BaseFormatter() {
			@Override
			public String format(final Object object) {
				if (object instanceof SlotVisit) {
					return "Cargo";
				} else if (object instanceof StartEvent) {
					return "Start";
				} else if (object instanceof GeneratedCharterOut) {
					return "Generated Charter Out";
				} else if (object instanceof VesselEventVisit) {
					final VesselEvent vesselEvent = ((VesselEventVisit) object).getVesselEvent();
					if (vesselEvent instanceof DryDockEvent) {
						return "Dry Dock";
					} else if (vesselEvent instanceof MaintenanceEvent) {
						return "Maintenance";
					} else if (vesselEvent instanceof CharterOutEvent) {
						return "Charter Out";
					}
				}
				return "Unknown";
			}
		});

		addPNLColumn();

		addColumn("Purchase Price", new BaseFormatter() {
			@Override
			public String format(final Object object) {
				if (object instanceof SlotVisit) {
					final SlotVisit slotVisit = (SlotVisit) object;
					return super.format(slotVisit.getSlotAllocation().getPrice());
				}
				return null;
			}

			@Override
			public Comparable getComparable(final Object object) {
				if (object instanceof SlotVisit) {
					final SlotVisit slotVisit = (SlotVisit) object;
					return slotVisit.getSlotAllocation().getPrice();
				}
				return 0.0;
			}
		});
		addColumn("Sales Price", new BaseFormatter() {
			@Override
			public String format(final Object object) {
				if (object instanceof SlotVisit) {
					final SlotVisit slotVisit = (SlotVisit) object;
					return super.format(slotVisit.getSlotAllocation().getCargoAllocation().getDischargeAllocation().getPrice());
				}
				return null;
			}

			@Override
			public Comparable getComparable(final Object object) {
				if (object instanceof SlotVisit) {
					final SlotVisit slotVisit = (SlotVisit) object;
					return slotVisit.getSlotAllocation().getCargoAllocation().getDischargeAllocation().getPrice();
				}
				return 0.0;// super.getComparable(object);
			}
		});
		addColumn("Shipping Cost", new BaseFormatter() {

			double getValue(final SlotVisit visit) {
				final CargoAllocation cargoAllocation = visit.getSlotAllocation().getCargoAllocation();

				// TODO: Fixed (other) port costs?
				// TODO: Boil-off included?

				final ExtraData dataWithKey = cargoAllocation.getDataWithKey(TradingConstants.ExtraData_ShippingCostIncBoilOff);
				if (dataWithKey != null) {
					final Integer v = dataWithKey.getValueAs(Integer.class);
					if (v != null) {
						final double dischargeVolumeInMMBTu = (double) cargoAllocation.getDischargeVolume() * ((LoadSlot) cargoAllocation.getLoadAllocation().getSlot()).getSlotOrPortCV();
						if (dischargeVolumeInMMBTu == 0.0) {
							return 0.0;
						}
						final double shipping = (double) v.doubleValue() / dischargeVolumeInMMBTu;
						return shipping;
					}
				}
				return 0.0;
			}

			@Override
			public String format(final Object object) {
				if (object instanceof SlotVisit) {
					final SlotVisit slotVisit = (SlotVisit) object;
					return String.format("%,.3f", getValue(slotVisit));

				}
				return null;
			}

			@Override
			public Comparable getComparable(final Object object) {
				if (object instanceof SlotVisit) {
					final SlotVisit slotVisit = (SlotVisit) object;
					return getValue(slotVisit);
				}
				return 0.0;
			}
		});
	}

	@Override
	protected boolean handleSelections() {
		return true;
	}

	@Override
	protected Class<?> getSelectionAdaptionClass() {
		return Event.class;
	}

	private void addPNLColumn() {

		final String title = "P&L";
		addColumn(title, new IntegerFormatter() {
			@Override
			public Integer getIntValue(final Object object) {
				ExtraDataContainer container = null;

				if (object instanceof CargoAllocation) {
					container = (CargoAllocation) object;
				}
				if (object instanceof SlotVisit) {
					final SlotVisit slotVisit = (SlotVisit) object;
					if (slotVisit.getSlotAllocation().getSlot() instanceof LoadSlot) {
						container = slotVisit.getSlotAllocation().getCargoAllocation();
					}
				}
				if (object instanceof VesselEventVisit) {
					container = (VesselEventVisit) object;
				}
				if (object instanceof StartEvent) {
					container = (StartEvent) object;
				}
				if (object instanceof GeneratedCharterOut) {
					container = (GeneratedCharterOut) object;
				}

				if (container != null) {
					final ExtraData dataWithKey = container.getDataWithKey(TradingConstants.ExtraData_GroupValue);
					if (dataWithKey != null) {
						final Integer v = dataWithKey.getValueAs(Integer.class);
						if (v != null) {
							return v;
						}
					}
				}

				return null;
			}
		});
	}

	@Override
	protected void processInputs(final Object[] result) {
		for (final Object a : result) {
			// map to events
			if (a instanceof CargoAllocation) {
				final CargoAllocation allocation = (CargoAllocation) a;

				setInputEquivalents(
						allocation,
						Arrays.asList(new Object[] { allocation.getLoadAllocation().getSlotVisit(), allocation.getLoadAllocation().getSlot(), allocation.getDischargeAllocation().getSlotVisit(),
								allocation.getDischargeAllocation().getSlot(), allocation.getBallastIdle(), allocation.getBallastLeg(), allocation.getLadenIdle(), allocation.getLadenLeg(),
								allocation.getInputCargo() }));
			} else if (a instanceof SlotVisit) {
				final SlotVisit slotVisit = (SlotVisit) a;

				final CargoAllocation allocation = slotVisit.getSlotAllocation().getCargoAllocation();

				setInputEquivalents(
						allocation,
						Arrays.asList(new Object[] { allocation.getLoadAllocation().getSlotVisit(), allocation.getLoadAllocation().getSlot(), allocation.getDischargeAllocation().getSlotVisit(),
								allocation.getDischargeAllocation().getSlot(), allocation.getBallastIdle(), allocation.getBallastLeg(), allocation.getLadenIdle(), allocation.getLadenLeg(),
								allocation.getInputCargo() }));
			} else if (a instanceof VesselEventVisit) {
				final VesselEventVisit vesselEventVisit = (VesselEventVisit) a;
				setInputEquivalents(vesselEventVisit, Lists.<Object> newArrayList(vesselEventVisit.getVesselEvent()));
			} else if (a instanceof StartEvent) {
				final StartEvent startEvent = (StartEvent) a;
				setInputEquivalents(startEvent, Lists.<Object> newArrayList(startEvent.getSequence().getVessel()));
			}
		}
	}

	@Override
	protected boolean isElementDifferent(final EObject pinnedObject, final EObject otherObject) {

		if (pinnedObject == null || otherObject == null) {
			return true;
		}

		final boolean different = false;
		if (pinnedObject instanceof CargoAllocation && otherObject instanceof CargoAllocation) {
			CargoAllocation ref = null;
			CargoAllocation ca = null;
			ref = (CargoAllocation) pinnedObject;

			ca = (CargoAllocation) otherObject;

			// Check vessel
			if ((ca.getSequence().getVessel() == null) != (ref.getSequence().getVessel() == null)) {
				return true;
			} else if ((ca.getSequence().getVesselClass() == null) != (ref.getSequence().getVesselClass() == null)) {
				return true;
			} else if (ca.getSequence().getVessel() != null && (!ca.getSequence().getVessel().getName().equals(ref.getSequence().getVessel().getName()))) {
				return true;
			} else if (ca.getSequence().getVesselClass() != null && (!ca.getSequence().getVessel().getName().equals(ref.getSequence().getVesselClass().getName()))) {
				return true;
			}

			if (!different) {
				if (!ca.getLoadAllocation().getPort().getName().equals(ref.getLoadAllocation().getPort().getName())) {
					return true;
				}
			}
			if (!different) {
				if (!ca.getLoadAllocation().getContract().getName().equals(ref.getLoadAllocation().getContract().getName())) {
					return true;
				}
			}
			if (!different) {
				if (!ca.getDischargeAllocation().getPort().getName().equals(ref.getDischargeAllocation().getPort().getName())) {
					return true;
				}
			}
			if (!different) {
				if (!ca.getDischargeAllocation().getContract().getName().equals(ref.getDischargeAllocation().getContract().getName())) {
					return true;
				}
			}
		} else if (pinnedObject instanceof Event && otherObject instanceof Event) {
			final Event vev = (Event) otherObject;
			final Event ref = (Event) pinnedObject;

			final int refTime = ref.getDuration() + (ref.getNextEvent() != null ? ref.getNextEvent().getDuration() : 0);
			final int vevTime = vev.getDuration() + (vev.getNextEvent() != null ? vev.getNextEvent().getDuration() : 0);

			// 3 Days
			if (Math.abs(refTime - vevTime) > 3 * 24) {
				return true;
			}

		}

		return different;
	}

	protected IScenarioInstanceElementCollector getElementCollector() {
		return new ScheduleElementCollector() {

			@Override
			public void beginCollecting() {
				super.beginCollecting();
				SchedulePnLReport.this.clearPinModeData();
			}

			@Override
			protected Collection<? extends Object> collectElements(final Schedule schedule, final boolean isPinned) {

				final List<Event> interestingEvents = new LinkedList<Event>();
				for (final Sequence sequence : schedule.getSequences()) {
					for (final Event event : sequence.getEvents()) {
						if (event instanceof StartEvent) {
							interestingEvents.add(event);
						} else if (event instanceof VesselEventVisit) {
							interestingEvents.add(event);
						} else if (event instanceof GeneratedCharterOut) {
							interestingEvents.add(event);
						} else if (event instanceof SlotVisit) {
							final SlotVisit slotVisit = (SlotVisit) event;
							if (slotVisit.getSlotAllocation().getSlot() instanceof LoadSlot) {
								interestingEvents.add(event);
							}
						}
					}
				}

				SchedulePnLReport.this.collectPinModeElements(interestingEvents, isPinned);

				return interestingEvents;
			}
		};
	}

	/**
	 * Returns a key of some kind for the element
	 * 
	 * @param element
	 * @return
	 * @since 1.1
	 */
	protected String getElementKey(final EObject element) {
		if (element instanceof CargoAllocation) {
			return ((CargoAllocation) element).getName();
		} else if (element instanceof Event) {
			return ((Event) element).name();
		}
		return super.getElementKey(element);
	}

}
