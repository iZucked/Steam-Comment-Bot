/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.reports.views;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.LegalEntity;
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
import com.mmxlabs.models.lng.types.ExtraDataContainer;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.shiplingo.platform.reports.IScenarioInstanceElementCollector;
import com.mmxlabs.shiplingo.platform.reports.IScenarioViewerSynchronizerOutput;
import com.mmxlabs.shiplingo.platform.reports.ScheduleElementCollector;
import com.mmxlabs.trading.optimiser.TradingConstants;

/**
 * @since 1.1
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
			public String format(Object object) {
				if (object instanceof CargoAllocation) {
					return "Cargo";
				} else if (object instanceof SlotVisit) {
					return "Cargo";
				} else if (object instanceof StartEvent) {
					return "Start";
				} else if (object instanceof GeneratedCharterOut) {
					return "Generated Charter Out";
				} else if (object instanceof VesselEventVisit) {
					VesselEvent vesselEvent = ((VesselEventVisit) object).getVesselEvent();
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

		// addColumn("Type", objectFormatter, s.getCargoAllocation__GetType());

		// // addColumn("Load Port", objectFormatter, s.getCargoAllocation_LoadAllocation(), s.getSlotAllocation__GetPort(), name);
		// addColumn("Load Date", datePartFormatter, s.getCargoAllocation_LoadAllocation(), s.getSlotAllocation__GetLocalStart());
		// addColumn("Purchase Contract", objectFormatter, s.getCargoAllocation_LoadAllocation(), s.getSlotAllocation__GetContract(), name);
		// addColumn("Purchase Price", objectFormatter, s.getCargoAllocation_LoadAllocation(), s.getSlotAllocation_Price());
		//
		// // addColumn("Discharge Port", objectFormatter, s.getCargoAllocation_DischargeAllocation(), s.getSlotAllocation__GetPort(), name);
		// // addColumn("Discharge Date", datePartFormatter, s.getCargoAllocation_DischargeAllocation(), s.getSlotAllocation__GetLocalStart());
		// addColumn("Sales Contract", objectFormatter, s.getCargoAllocation_DischargeAllocation(), s.getSlotAllocation__GetContract(), name);
		// addColumn("Sales Price", objectFormatter, s.getCargoAllocation_DischargeAllocation(), s.getSlotAllocation_Price());

		// addColumn("Vessel", objectFormatter, s.getCargoAllocation_Sequence(), SchedulePackage.eINSTANCE.getSequence__GetName());

	}

	@Override
	protected boolean handleSelections() {
		return true;
	}

	@Override
	protected Class<?> getSelectionAdaptionClass() {
		return Event.class;
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

							final TreeSet<String> entities = new TreeSet<String>();
							for (final MMXRootObject rootObject : rootObjects) {

								final CommercialModel commercialModel = rootObject.getSubModel(CommercialModel.class);
								for (final LegalEntity e : commercialModel.getEntities()) {
									entities.add(e.getName());
								}
							}
							addPNLColumn(entities);
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

	private final List<String> entityColumnNames = new ArrayList<String>();

	private void addPNLColumn(final Collection<String> entityNames) {

		final String title = /* entity.getName() + */"P&L";
		entityColumnNames.add(title);
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

				Integer value = null;
				if (container != null) {
					// TODO: make key well known
					for (final String entityName : entityNames) {
						{
							final Integer v = container.getValueWithPathAs(CollectionsUtil.makeArrayList(TradingConstants.ExtraData_upstream, entityName, TradingConstants.ExtraData_pnl),
									Integer.class, null);
							if (v != null) {
								if (value == null) {
									value = v;
								} else {
									value = value.intValue() + v.intValue();
								}
							}
						}
						{
							final Integer v = container.getValueWithPathAs(CollectionsUtil.makeArrayList(TradingConstants.ExtraData_shipped, entityName, TradingConstants.ExtraData_pnl),
									Integer.class, null);
							if (v != null) {
								if (value == null) {
									value = v;
								} else {
									value = value.intValue() + v.intValue();
								}
							}
						}
						{
							final Integer v = container.getValueWithPathAs(CollectionsUtil.makeArrayList(TradingConstants.ExtraData_downstream, entityName, TradingConstants.ExtraData_pnl),
									Integer.class, null);
							if (v != null) {
								if (value == null) {
									value = v;
								} else {
									value = value.intValue() + v.intValue();
								}
							}
						}

					}
					// if ((allocation.getLoadRevenue() != null) && entity.equals(allocation.getLoadRevenue().getEntity())) {
					// value += allocation.getLoadRevenue().getValue();
					// }
					// if ((allocation.getShippingRevenue() != null) && entity.equals(allocation.getShippingRevenue().getEntity())) {
					// value += allocation.getShippingRevenue().getValue();
					// }
					// if ((allocation.getDischargeRevenue() != null) && entity.equals(allocation.getDischargeRevenue().getEntity())) {
					// value += allocation.getDischargeRevenue().getValue();
					// }
					return value;
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
			}
		}
	}

	@Override
	protected boolean isElementDifferent(final EObject pinnedObject, final EObject otherObject) {
		CargoAllocation ref = null;
		if (pinnedObject instanceof CargoAllocation) {
			ref = (CargoAllocation) pinnedObject;
		}

		CargoAllocation ca = null;
		if (otherObject instanceof CargoAllocation) {
			ca = (CargoAllocation) otherObject;
		}

		if (ca == null || ref == null) {
			return true;
		}

		boolean different = false;

		// Check vessel
		if ((ca.getSequence().getVessel() == null) != (ref.getSequence().getVessel() == null)) {
			different = true;
		} else if ((ca.getSequence().getVesselClass() == null) != (ref.getSequence().getVesselClass() == null)) {
			different = true;
		} else if (ca.getSequence().getVessel() != null && (!ca.getSequence().getVessel().getName().equals(ref.getSequence().getVessel().getName()))) {
			different = true;
		} else if (ca.getSequence().getVesselClass() != null && (!ca.getSequence().getVessel().getName().equals(ref.getSequence().getVesselClass().getName()))) {
			different = true;
		}

		if (!different) {
			if (!ca.getLoadAllocation().getPort().getName().equals(ref.getLoadAllocation().getPort().getName())) {
				different = true;
			}
		}
		if (!different) {
			if (!ca.getLoadAllocation().getContract().getName().equals(ref.getLoadAllocation().getContract().getName())) {
				different = true;
			}
		}
		if (!different) {
			if (!ca.getDischargeAllocation().getPort().getName().equals(ref.getDischargeAllocation().getPort().getName())) {
				different = true;
			}
		}
		if (!different) {
			if (!ca.getDischargeAllocation().getContract().getName().equals(ref.getDischargeAllocation().getContract().getName())) {
				different = true;
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
				for (Sequence sequence : schedule.getSequences()) {
					for (Event event : sequence.getEvents()) {
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
		}
		return super.getElementKey(element);
	}
}
