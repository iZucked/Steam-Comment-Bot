package com.mmxlabs.models.lng.transformer.period;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.SpotLoadSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.editor.utils.CollectedAssignment;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.HeelOptions;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioFactory;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;

public class PeriodTestUtils {

	public static Date createDate(final int year, final int month, final int day, final int hour) {
		final Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("UTC"));
		cal.clear();

		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, day);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		return cal.getTime();
	}

	static Date createDate(final int year, final int month, final int day) {
		return PeriodTestUtils.createDate(year, month, day, 0);
	}

	public static LNGScenarioModel createBasicScenario() {
		final LNGScenarioModel scenarioModel = LNGScenarioFactory.eINSTANCE.createLNGScenarioModel();
		final LNGPortfolioModel portfolioModel = LNGScenarioFactory.eINSTANCE.createLNGPortfolioModel();
		final CargoModel cargoModel = CargoFactory.eINSTANCE.createCargoModel();
		final FleetModel fleetModel = FleetFactory.eINSTANCE.createFleetModel();
		final PortModel portModel = PortFactory.eINSTANCE.createPortModel();

		scenarioModel.setFleetModel(fleetModel);
		scenarioModel.setPortfolioModel(portfolioModel);
		scenarioModel.setPortModel(portModel);
		portfolioModel.setCargoModel(cargoModel);

		return scenarioModel;
	}

	public static Vessel createVessel(final LNGScenarioModel scenarioModel, final String name) {
		final Vessel vessel = FleetFactory.eINSTANCE.createVessel();
		vessel.setName(name);
		scenarioModel.getFleetModel().getVessels().add(vessel);
		return vessel;
	}

	public static VesselAvailability createVesselAvailability(final LNGScenarioModel scenarioModel, final Vessel vessel) {
		final VesselAvailability vesselAvailability = CargoFactory.eINSTANCE.createVesselAvailability();
		vesselAvailability.setVessel(vessel);

		final HeelOptions heelOptions = FleetFactory.eINSTANCE.createHeelOptions();
		vesselAvailability.setStartHeel(heelOptions);

		scenarioModel.getPortfolioModel().getCargoModel().getVesselAvailabilities().add(vesselAvailability);
		return vesselAvailability;
	}

	public static Port createPort(final LNGScenarioModel scenarioModel, final String name) {
		final Port port = PortFactory.eINSTANCE.createPort();
		port.setName(name);
		scenarioModel.getPortModel().getPorts().add(port);
		return port;
	}

	public static Cargo createCargo(final LNGScenarioModel scenarioModel, final String name, final Slot... slots) {
		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();
		cargo.setName(name);
		scenarioModel.getPortfolioModel().getCargoModel().getCargoes().add(cargo);

		for (final Slot slot : slots) {
			cargo.getSlots().add(slot);
		}

		return cargo;
	}

	public static Cargo createCargo(final LNGScenarioModel scenarioModel, final String name, final Port loadPort, final Date loadDate, final Port dischargePort, final Date dischargeDate) {
		final Cargo cargo = CargoFactory.eINSTANCE.createCargo();
		cargo.setName(name);
		scenarioModel.getPortfolioModel().getCargoModel().getCargoes().add(cargo);

		final LoadSlot loadSlot = createLoadSlot(scenarioModel, name + "-load");
		loadSlot.setWindowStart(loadDate);
		cargo.getSlots().add(loadSlot);
		scenarioModel.getPortfolioModel().getCargoModel().getLoadSlots().add(loadSlot);

		final DischargeSlot dischargeSlot = createDischargeSlot(scenarioModel, name + "-discharge");
		dischargeSlot.setWindowStart(dischargeDate);
		cargo.getSlots().add(dischargeSlot);
		scenarioModel.getPortfolioModel().getCargoModel().getDischargeSlots().add(dischargeSlot);

		return cargo;
	}

	public static CharterOutEvent createCharterOutEvent(final LNGScenarioModel scenarioModel, final String name) {
		final CharterOutEvent event = CargoFactory.eINSTANCE.createCharterOutEvent();
		event.setName(name);
		scenarioModel.getPortfolioModel().getCargoModel().getVesselEvents().add(event);

		return event;
	}

	public static LoadSlot createLoadSlot(final LNGScenarioModel scenarioModel, final String name) {
		final LoadSlot slot = CargoFactory.eINSTANCE.createLoadSlot();
		slot.setName(name);
		scenarioModel.getPortfolioModel().getCargoModel().getLoadSlots().add(slot);
		return slot;
	}

	public static SpotLoadSlot createSpotLoadSlot(final LNGScenarioModel scenarioModel, final String name) {
		final SpotLoadSlot slot = CargoFactory.eINSTANCE.createSpotLoadSlot();
		slot.setName(name);
		scenarioModel.getPortfolioModel().getCargoModel().getLoadSlots().add(slot);
		return slot;
	}

	public static DischargeSlot createDischargeSlot(final LNGScenarioModel scenarioModel, final String name) {
		final DischargeSlot slot = CargoFactory.eINSTANCE.createDischargeSlot();
		slot.setName(name);
		scenarioModel.getPortfolioModel().getCargoModel().getDischargeSlots().add(slot);
		return slot;
	}

	public static SpotDischargeSlot createSpotDischargeSlot(final LNGScenarioModel scenarioModel, final String name) {
		final SpotDischargeSlot slot = CargoFactory.eINSTANCE.createSpotDischargeSlot();
		slot.setName(name);
		scenarioModel.getPortfolioModel().getCargoModel().getDischargeSlots().add(slot);
		return slot;
	}

	public static PortVisit createPortVisit(final Port port, final Date date) {

		final PortVisit portVisit = ScheduleFactory.eINSTANCE.createPortVisit();
		portVisit.setPort(port);
		portVisit.setStart(date);
		portVisit.setEnd(date);

		return portVisit;
	}

	public static CollectedAssignment createCollectedAssignment(final Vessel vessel, final AssignableElement... elements) {

		return new CollectedAssignment(Arrays.asList(elements), vessel, null);
	}

}
