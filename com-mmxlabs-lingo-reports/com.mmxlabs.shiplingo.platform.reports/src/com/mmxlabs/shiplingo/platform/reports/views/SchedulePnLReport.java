/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.reports.views;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;

import com.google.common.collect.Lists;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.LegalEntity;
import com.mmxlabs.models.lng.fleet.CharterOutEvent;
import com.mmxlabs.models.lng.fleet.DryDockEvent;
import com.mmxlabs.models.lng.fleet.MaintenanceEvent;
import com.mmxlabs.models.lng.fleet.VesselEvent;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.types.ExtraData;
import com.mmxlabs.models.lng.types.ExtraDataContainer;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.scheduler.optimiser.TradingConstants;
import com.mmxlabs.shiplingo.platform.reports.IScenarioInstanceElementCollector;
import com.mmxlabs.shiplingo.platform.reports.IScenarioViewerSynchronizerOutput;
import com.mmxlabs.shiplingo.platform.reports.ScheduleElementCollector;

/**
 * @since 2.0
 */
public class SchedulePnLReport extends EMFReportView {
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.shiplingo.platform.reports.views.SchedulePnLReport";
	final List<String> entityColumnNames = new ArrayList<String>();

	public SchedulePnLReport() {
		super("com.mmxlabs.shiplingo.platform.reports.CargoPnLReportView");

		final SchedulePackage s = SchedulePackage.eINSTANCE;

		addScheduleColumn("Schedule", containingScheduleFormatter);

		addColumn("ID", objectFormatter, s.getEvent__Name());

		// add the total (aggregate) P&L column
		addPNLColumn();

		// CommercialModel.getEntities();

		// addPNLColumn("Asia");
		// addPNLColumn("Europe");

		addColumn("Discharge Port", new BaseFormatter() {
			@Override
			public String format(final Object object) {
				if (object instanceof SlotVisit) {
					final SlotVisit slotVisit = (SlotVisit) object;
					return slotVisit.getSlotAllocation().getCargoAllocation().getDischargeAllocation().getPort().getName();
				}
				return null;
			}

			@Override
			public Comparable<?> getComparable(final Object object) {
				if (object instanceof SlotVisit) {
					final SlotVisit slotVisit = (SlotVisit) object;
					return slotVisit.getSlotAllocation().getCargoAllocation().getDischargeAllocation().getPort().getName();
				}
				return "";
			}
		});
		addColumn("Sales Contract", new BaseFormatter() {
			@Override
			public String format(final Object object) {
				if (object instanceof SlotVisit) {
					final SlotVisit slotVisit = (SlotVisit) object;
					return slotVisit.getSlotAllocation().getCargoAllocation().getDischargeAllocation().getContract().getName();
				}
				return null;
			}

			@Override
			public Comparable<?> getComparable(final Object object) {
				if (object instanceof SlotVisit) {
					final SlotVisit slotVisit = (SlotVisit) object;
					return slotVisit.getSlotAllocation().getCargoAllocation().getDischargeAllocation().getContract().getName();
				}
				return "";
			}
		});

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
				return 0.0;
			}
		});
		addColumn("Shipping Cost", new BaseFormatter() {

			Double getValue(final SlotVisit visit) {
				final CargoAllocation cargoAllocation = visit.getSlotAllocation().getCargoAllocation();
				if (cargoAllocation == null) {
					return null;
				}
				final Cargo inputCargo = cargoAllocation.getInputCargo();
				if (inputCargo == null) {
					return null;
				}
				if (inputCargo.getCargoType() != CargoType.FLEET) {
					return null;
				}
				// TODO: Fixed (other) port costs?
				// TODO: Boil-off included?

				final ExtraData dataWithKey = cargoAllocation.getDataWithKey(TradingConstants.ExtraData_ShippingCostIncBoilOff);
				if (dataWithKey != null) {
					final Integer v = dataWithKey.getValueAs(Integer.class);
					if (v != null) {
						SlotAllocation loadAllocation = cargoAllocation.getLoadAllocation();
						if (loadAllocation == null) {
							return null;
						}
						final double dischargeVolumeInMMBTu = (double) cargoAllocation.getDischargeVolume() * ((LoadSlot) loadAllocation.getSlot()).getSlotOrPortCV();
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
					final Double value = getValue(slotVisit);
					if (value != null) {
						return String.format("%,.2f", value);
					}

				}
				return null;
			}

			@Override
			public Comparable getComparable(final Object object) {
				if (object instanceof SlotVisit) {
					final SlotVisit slotVisit = (SlotVisit) object;
					final Double value = getValue(slotVisit);
					if (value != null) {
						return value;
					}
				}
				return 0.0;
			}
		});

		addColumn("Type", new BaseFormatter() {
			@Override
			public String format(final Object object) {
				if (object instanceof SlotVisit) {
					return "Cargo";
				} else if (object instanceof StartEvent) {
					return "Start";
				} else if (object instanceof GeneratedCharterOut) {
					return "Charter Out (virt)";
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

	}

	@Override
	protected boolean handleSelections() {
		return true;
	}

	@Override
	protected Class<?> getSelectionAdaptionClass() {
		return Event.class;
	}

	private Integer getEntityPNLEntry(final ExtraDataContainer container, final String entity) {
		if (container == null) {
			return null;
		}

		ExtraData data = null;

		// supplying null for the entity name indicates that the total group P&L should be returned
		if (entity == null) {
			data = container.getDataWithKey(TradingConstants.ExtraData_GroupValue);
		}
		// with a specific entity name, we search the upstream, shipping and downstream entities for the P&L data
		else {
			final String[] options = { TradingConstants.ExtraData_upstream, TradingConstants.ExtraData_shipped, TradingConstants.ExtraData_downstream };
			for (final String option : options) {
				final ExtraData possibleData = container.getDataWithKey(option);
				if (possibleData != null) {
					final ExtraData entityData = possibleData.getDataWithKey(entity);
					if (entityData != null) {
						data = entityData.getDataWithKey(TradingConstants.ExtraData_pnl);
					}
				}

			}
		}

		if (data == null) {
			return null;
		}

		return data.getValueAs(Integer.class);
	}

	private void addPNLColumn() {
		addPNLColumn("Group Total", null);
	}

	private void addPNLColumn(final String entityName) {
		addPNLColumn(entityName, entityName);
	}

	private void addPNLColumn(final String entityLabel, final String entityKey) {
		final String title = String.format("P&L (%s)", entityLabel);

		// HACK: don't the label to the entity column names if the column is for total group P&L
		if (entityKey != null) {
			entityColumnNames.add(title);
		}

		addColumn(title, new IntegerFormatter() {
			@Override
			public Integer getIntValue(final Object object) {
				ExtraDataContainer container = null;

				if (object instanceof CargoAllocation || object instanceof VesselEventVisit || object instanceof StartEvent || object instanceof GeneratedCharterOut) {
					container = (ExtraDataContainer) object;
				}
				if (object instanceof SlotVisit) {
					final SlotVisit slotVisit = (SlotVisit) object;
					if (slotVisit.getSlotAllocation().getSlot() instanceof LoadSlot) {
						container = slotVisit.getSlotAllocation().getCargoAllocation();
					}
				}

				return getEntityPNLEntry(container, entityKey);
			}
		});
	}

	@Override
	protected IStructuredContentProvider getContentProvider() {
		final IStructuredContentProvider superProvider = super.getContentProvider();

		return new IStructuredContentProvider() {
			@Override
			public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
				superProvider.inputChanged(viewer, oldInput, newInput);
				Display.getCurrent().asyncExec(new Runnable() {
					@Override
					public void run() {
						if (viewer.getControl().isDisposed()) {
							return;
						}

						if (newInput instanceof IScenarioViewerSynchronizerOutput) {
							final IScenarioViewerSynchronizerOutput synchronizerOutput = (IScenarioViewerSynchronizerOutput) newInput;
							final Collection<MMXRootObject> rootObjects = synchronizerOutput.getRootObjects();
							for (final String s : entityColumnNames) {
								removeColumn(s);
							}

							entityColumnNames.clear();

							for (final MMXRootObject rootObject : rootObjects) {

								final CommercialModel commercialModel = rootObject.getSubModel(CommercialModel.class);
								if (commercialModel != null) {
									for (final LegalEntity e : commercialModel.getEntities()) {
										addPNLColumn(e.getName());
									}
								}
							}
						}

						viewer.refresh();
					}
				});
			}

			@Override
			public void dispose() {
				superProvider.dispose();
			}

			@Override
			public Object[] getElements(final Object object) {
				return superProvider.getElements(object);
			}
		};
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

		if (pinnedObject instanceof GeneratedCharterOut || otherObject instanceof GeneratedCharterOut) {
			// Specific to each scenario/schedule so always mark as different.
			return true;
		}

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
			} else if (ca.getSequence().getVesselClass() != null && (!ca.getSequence().getVesselClass().getName().equals(ref.getSequence().getVesselClass().getName()))) {
				return true;
			}

			if (!ca.getLoadAllocation().getPort().getName().equals(ref.getLoadAllocation().getPort().getName())) {
				return true;
			}
			if (!ca.getLoadAllocation().getContract().getName().equals(ref.getLoadAllocation().getContract().getName())) {
				return true;
			}
			if (!ca.getDischargeAllocation().getPort().getName().equals(ref.getDischargeAllocation().getPort().getName())) {
				return true;
			}
			if (!ca.getDischargeAllocation().getContract().getName().equals(ref.getDischargeAllocation().getContract().getName())) {
				return true;
			}
		} else if (pinnedObject instanceof SlotVisit && otherObject instanceof SlotVisit) {
			SlotVisit ref = null;
			SlotVisit ca = null;
			ref = (SlotVisit) pinnedObject;

			ca = (SlotVisit) otherObject;

			if ((ca.getSequence().getVessel() == null) != (ref.getSequence().getVessel() == null)) {
				return true;
			} else if ((ca.getSequence().getVesselClass() == null) != (ref.getSequence().getVesselClass() == null)) {
				return true;
			} else if (ca.getSequence().getVessel() != null && (!ca.getSequence().getVessel().getName().equals(ref.getSequence().getVessel().getName()))) {
				return true;
			} else if (ca.getSequence().getVesselClass() != null && (!ca.getSequence().getVesselClass().getName().equals(ref.getSequence().getVesselClass().getName()))) {
				return true;
			}

			if (!ca.getPort().getName().equals(ref.getPort().getName())) {
				return true;
			}
			if (!ca.getSlotAllocation().getContract().getName().equals(ref.getSlotAllocation().getContract().getName())) {
				return true;
			}
			return isElementDifferent(ref.getSlotAllocation().getCargoAllocation(), ca.getSlotAllocation().getCargoAllocation());
		} else if (pinnedObject instanceof Event && otherObject instanceof Event) {
			final Event vev = (Event) otherObject;
			final Event ref = (Event) pinnedObject;

			final int refTime = getEventDuration(ref);
			final int vevTime = getEventDuration(vev);

			// 3 Days
			if (Math.abs(refTime - vevTime) > 3 * 24) {
				return true;
			}

		}

		return false;
	}

	@Override
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
	@Override
	protected String getElementKey(final EObject element) {
		if (element instanceof CargoAllocation) {
			return ((CargoAllocation) element).getName();
		} else if (element instanceof Event) {
			return ((Event) element).name();
		}
		return super.getElementKey(element);
	}

	private int getEventDuration(final Event event) {

		int duration = event.getDuration();
		Event next = event.getNextEvent();
		while (next != null && !isSegmentStart(next)) {
			duration += next.getDuration();
			next = next.getNextEvent();
		}
		return duration;

	}

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
}
