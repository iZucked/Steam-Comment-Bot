/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.presentation.cargoeditor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import scenario.Scenario;
import scenario.cargo.Cargo;
import scenario.fleet.Vessel;
import scenario.fleet.VesselClass;
import scenario.schedule.CargoAllocation;
import scenario.schedule.Schedule;
import scenario.schedule.ScheduleFactory;
import scenario.schedule.SchedulePackage;
import scenario.schedule.fleetallocation.AllocatedVessel;
import scenario.schedule.fleetallocation.FleetVessel;
import scenario.schedule.fleetallocation.FleetallocationFactory;
import scenario.schedule.fleetallocation.SpotVessel;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.common.Pair;

/**
 * A column manipulator which sets/displays the initial vessel for a cargo. This
 * is the vessel which the cargo will be put on in the initial schedule, either
 * because it's contained in a sequence, or because its cargoallocation is
 * providing that advice to the ISB.
 * 
 * This could be speeded up by cargo / allocation relationships, but that would
 * need lots of notifiers hooking up.
 * 
 * TODO allow to clear allocation
 * 
 * @author Tom Hinton
 * 
 */
public class CargoInitialVesselManipulator implements ICellManipulator,
		ICellRenderer {

	private ComboBoxCellEditor editor;
	private final EditingDomain editingDomain;

	/**
	 * @param editingDomain
	 */
	public CargoInitialVesselManipulator(final EditingDomain editingDomain) {
		this.editingDomain = editingDomain;
	}

	private Scenario getScenario(final Cargo cargo) {
		if (cargo.eContainer() != null
				&& cargo.eContainer().eContainer() instanceof Scenario) {
			return (Scenario) (cargo.eContainer().eContainer());
		}
		return null;
	}

	private CargoAllocation getCargoAllocation(final Schedule schedule,
			final Cargo cargo) {
		if (schedule == null || cargo == null)
			return null;

		// find the cargo allocation for this cargo, if there is one.
		for (final CargoAllocation ca : schedule.getCargoAllocations()) {
			if (ca.getLoadSlot() == cargo.getLoadSlot()
					&& ca.getDischargeSlot() == cargo.getDischargeSlot()) {
				return ca;
			}
		}
		return null;
	}

	private AllocatedVessel getAllocatedVessel(final Schedule schedule,
			final Cargo cargo) {
		final CargoAllocation ca = getCargoAllocation(schedule, cargo);
		if (ca != null)
			return ca.getVessel();
		return null;
	}

	@Override
	public String render(final Object object) {
		if (object instanceof Cargo) {
			final Cargo cargo = (Cargo) object;
			final Scenario scenario = getScenario(cargo);
			if (scenario != null) {
				final Schedule schedule = getSchedule(scenario);
				final AllocatedVessel av = getAllocatedVessel(schedule, cargo);
				if (av != null) {
					return av.getName();
				}
			}
		}
		return "";
	}

	/**
	 * Pull the initial sequences from a scenario, or null if there is a missing
	 * link somewhere.
	 * 
	 * @param scenario
	 * @return
	 */
	private Schedule getSchedule(final Scenario scenario) {
		if (scenario == null)
			return null;
		if (scenario.getOptimisation() == null)
			return null;
		if (scenario.getOptimisation().getCurrentSettings() == null)
			return null;
		if (scenario.getOptimisation().getCurrentSettings()
				.getInitialSchedule() == null)
			return null;
		return scenario.getOptimisation().getCurrentSettings()
				.getInitialSchedule();
	}

	private Schedule getOrCreateSchedule(final Scenario scenario) {
		Schedule schedule = getSchedule(scenario);

		if (schedule == null) {
			if (scenario == null)
				return null;
			if (scenario.getOptimisation() == null)
				return null;
			if (scenario.getOptimisation().getCurrentSettings() == null)
				return null;

			schedule = ScheduleFactory.eINSTANCE.createSchedule();
			scenario.getScheduleModel().getSchedules().add(schedule);
			// create fleet (create spots?)
			for (final Vessel v : scenario.getFleetModel().getFleet()) {
				final FleetVessel av = FleetallocationFactory.eINSTANCE
						.createFleetVessel();
				av.setVessel(v);
				schedule.getFleet().add(av);
			}

			scenario.getOptimisation().getCurrentSettings()
					.setInitialSchedule(schedule);
		}
		return schedule;
	}

	@Override
	public Comparable getComparable(Object object) {
		return render(object);
	}

	@Override
	public CellEditor getCellEditor(final Composite parent, final Object object) {
		editor = new ComboBoxCellEditor(parent, new String[0], SWT.READ_ONLY);
		setEditorNames();
		return editor;
	}

	/**
	 * Set the names into the editor control, if it's not null
	 */
	private void setEditorNames() {
		if (editor != null) {
			editor.setItems(names.toArray(new String[0]));
		}
	}

	@Override
	public Object getValue(final Object object) {
		if (object instanceof Cargo) {
			final Cargo cargo = (Cargo) object;
			final Scenario scenario = getScenario(cargo);
			final Schedule schedule = getSchedule(scenario);
			if (scenario == null || schedule == null)
				return -1;
			final AllocatedVessel av = getAllocatedVessel(schedule, cargo);
			if (av == null)
				return -1;
			return vessels.indexOf(av);
		}
		return -1;
	}

	@Override
	public void setValue(final Object object, final Object value) {
		if (value instanceof Integer && object instanceof Cargo) {
			final Cargo cargo = (Cargo) object;
			final Integer integerValue = (Integer) value;
			if (integerValue.intValue() == -1) return;
			final AllocatedVessel vessel = vessels.get(integerValue);

			final Scenario scenario = getScenario(cargo);
			final Schedule schedule = getSchedule(scenario);
			if (scenario == null || schedule == null)
				return;
			final CompoundCommand command = new CompoundCommand();
			
			CargoAllocation allocation = getCargoAllocation(schedule, cargo);
			if (allocation == null) {
				allocation = ScheduleFactory.eINSTANCE.createCargoAllocation();
				// schedule.getCargoAllocations().add(allocation);
				command.append(AddCommand.create(editingDomain, schedule,
						SchedulePackage.eINSTANCE
								.getSchedule_CargoAllocations(), allocation));
				allocation.setLoadSlot(cargo.getLoadSlot());
				allocation.setDischargeSlot(cargo.getDischargeSlot());
				allocation.setVessel(vessel);
			} else {
				if (allocation.getVessel() == vessel) return;
				command.append(SetCommand.create(editingDomain, allocation,
						SchedulePackage.eINSTANCE.getCargoAllocation_Vessel(),
						vessel));
			}

			if (allocation.getLoadSlotVisit() != null) {
				// delete all the stuff previously associated with this
				// allocation, so that the solution is consistent.
				// the initial sequence builder should do the rest.
				command.append(DeleteCommand.create(
						editingDomain,
						CollectionsUtil.makeArrayList(
								allocation.getLoadSlotVisit(),
								allocation.getDischargeSlotVisit(),
								allocation.getLadenIdle(),
								allocation.getLadenLeg(),
								allocation.getBallastIdle(),
								allocation.getBallastLeg())));
			}
			
			editingDomain.getCommandStack().execute(command);
		}
	}

	private final ArrayList<String> names = new ArrayList<String>();
	private final ArrayList<AllocatedVessel> vessels = new ArrayList<AllocatedVessel>();

	@Override
	public boolean canEdit(final Object object) {
		if (object instanceof Cargo) {
			final Cargo cargo = (Cargo) object;
			final Scenario scenario = getScenario(cargo);
			final Schedule schedule = getOrCreateSchedule(scenario);

			if (scenario == null || schedule == null)
				return false;

			// get vessel names
			final ArrayList<Pair<String, AllocatedVessel>> namesAndValues = new ArrayList<Pair<String, AllocatedVessel>>(
					schedule.getFleet().size());
			for (final AllocatedVessel av : schedule.getFleet()) {
				// TODO filter out vessels which cannot carry this cargo.
				final VesselClass avClass;
				if (av instanceof FleetVessel) {
					avClass = ((FleetVessel)av).getVessel().getClass_();
				} else {
					avClass = ((SpotVessel) av).getVesselClass();
				}
				if (avClass.getInaccessiblePorts().contains(cargo.getLoadSlot().getPort()) ||
						avClass.getInaccessiblePorts().contains(cargo.getDischargeSlot().getPort()))
					continue;
				namesAndValues.add(new Pair<String, AllocatedVessel>(av
						.getName(), av));
			}

			Collections.sort(namesAndValues,
					new Comparator<Pair<String, AllocatedVessel>>() {
						@Override
						public int compare(Pair<String, AllocatedVessel> arg0,
								Pair<String, AllocatedVessel> arg1) {
							return arg0.getFirst().compareTo(arg1.getFirst());
						}
					});

			names.clear();
			vessels.clear();

			for (final Pair<String, AllocatedVessel> v : namesAndValues) {
				names.add(v.getFirst());
				vessels.add(v.getSecond());
			}

			setEditorNames();

			return true;
		}

		return false;
	}
}
