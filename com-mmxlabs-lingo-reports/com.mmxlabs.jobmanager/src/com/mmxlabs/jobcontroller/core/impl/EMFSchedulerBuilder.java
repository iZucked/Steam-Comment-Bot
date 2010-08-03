package com.mmxlabs.jobcontroller.core.impl;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;

import scenario.Scenario;
import scenario.ScenarioFactory;
import scenario.cargo.CargoFactory;
import scenario.contract.ContractFactory;
import scenario.fleet.FleetFactory;
import scenario.fleet.FleetModel;
import scenario.market.MarketFactory;
import scenario.port.PortFactory;
import scenario.schedule.ScheduleFactory;

import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.optimiser.components.ITimeWindow;
import com.mmxlabs.optimiser.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.ICargo;
import com.mmxlabs.scheduler.optimiser.components.IConsumptionRateCalculator;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.IXYPort;
import com.mmxlabs.scheduler.optimiser.components.VesselState;

/**
 * Implementation of {@link ISchedulerBuilder}
 * 
 * @author Simon Goodall
 * 
 */
public final class EMFSchedulerBuilder implements ISchedulerBuilder {

	private final ISchedulerBuilder delegate;

	private final Scenario eScenario;

	private final Map<IPort, scenario.port.Port> ePorts = new HashMap<IPort, scenario.port.Port>();
	private final Map<ILoadSlot, scenario.cargo.LoadSlot> eLoadSlots = new HashMap<ILoadSlot, scenario.cargo.LoadSlot>();
	private final Map<IDischargeSlot, scenario.cargo.DischargeSlot> eDischargeSlots = new HashMap<IDischargeSlot, scenario.cargo.DischargeSlot>();
	private final Map<ICargo, scenario.cargo.Cargo> eCargoes = new HashMap<ICargo, scenario.cargo.Cargo>();
	private final Map<IVessel, scenario.fleet.Vessel> eVessels = new HashMap<IVessel, scenario.fleet.Vessel>();
	private final Map<IVesselClass, scenario.fleet.VesselClass> eVesselClasses = new HashMap<IVesselClass, scenario.fleet.VesselClass>();

	public EMFSchedulerBuilder(ISchedulerBuilder delegate) {

		this.delegate = delegate;

		// Initialise model
		eScenario = ScenarioFactory.eINSTANCE.createScenario();
		eScenario.setCargoModel(CargoFactory.eINSTANCE.createCargoModel());
		eScenario.setContractModel(ContractFactory.eINSTANCE
				.createContractModel());
		eScenario.setFleetModel(FleetFactory.eINSTANCE.createFleetModel());
		eScenario.setMarketModel(MarketFactory.eINSTANCE.createMarketModel());
		eScenario.setPortModel(PortFactory.eINSTANCE.createPortModel());
		eScenario.setScheduleModel(ScheduleFactory.eINSTANCE
				.createScheduleModel());
		eScenario.setDistanceModel(PortFactory.eINSTANCE.createDistanceModel());
	}

	@Override
	public ILoadSlot createLoadSlot(final String id, final IPort port,
			final ITimeWindow window, final long minVolume,
			final long maxVolume, final int unitPrice) {

		ILoadSlot slot = delegate.createLoadSlot(id, port, window, minVolume,
				maxVolume, unitPrice);

		scenario.cargo.LoadSlot eLoadSlot = CargoFactory.eINSTANCE
				.createLoadSlot();
		eLoadSlot.setId(id);
		eLoadSlot.setMaxQuantity(maxVolume);
		eLoadSlot.setMinQuantity(minVolume);
		eLoadSlot.setPort(ePorts.get(port));
		eLoadSlot.setUnitPrice(unitPrice);
		Calendar now = Calendar.getInstance();
		now.add(Calendar.HOUR_OF_DAY, window.getStart());
		eLoadSlot.setWindowStart(now.getTime());
		eLoadSlot.setWindowDuration(window.getEnd() - window.getStart());

		eLoadSlots.put(slot, eLoadSlot);

		return slot;
	}

	@Override
	public IDischargeSlot createDischargeSlot(final String id,
			final IPort port, final ITimeWindow window, final long minVolume,
			final long maxVolume, final int unitPrice) {

		IDischargeSlot slot = delegate.createDischargeSlot(id, port, window,
				minVolume, maxVolume, unitPrice);

		scenario.cargo.DischargeSlot eDischargeSlot = CargoFactory.eINSTANCE
				.createDischargeSlot();
		eDischargeSlot.setId(id);
		eDischargeSlot.setMaxQuantity(maxVolume);
		eDischargeSlot.setMinQuantity(minVolume);
		eDischargeSlot.setPort(ePorts.get(port));
		eDischargeSlot.setUnitPrice(unitPrice);
		Calendar now = Calendar.getInstance();
		now.add(Calendar.HOUR_OF_DAY, window.getStart());
		eDischargeSlot.setWindowStart(now.getTime());
		eDischargeSlot.setWindowDuration(window.getEnd() - window.getStart());

		eDischargeSlots.put(slot, eDischargeSlot);

		return slot;
	}

	@Override
	public ICargo createCargo(final String id, final ILoadSlot loadSlot,
			final IDischargeSlot dischargeSlot) {

		ICargo cargo = delegate.createCargo(id, loadSlot, dischargeSlot);

		scenario.cargo.Cargo eCargo = CargoFactory.eINSTANCE.createCargo();
		eCargo.setId(id);
		eCargo.setLoadSlot(eLoadSlots.get(loadSlot));
		eCargo.setDischargeSlot(eDischargeSlots.get(dischargeSlot));

		eCargoes.put(cargo, eCargo);

		eScenario.getCargoModel().getCargoes().add(eCargo);

		return cargo;
	}

	@Override
	public IPort createPort(final String name) {

		IPort port = delegate.createPort(name);

		scenario.port.Port ePort = PortFactory.eINSTANCE.createPort();
		ePort.setName(name);
		ePorts.put(port, ePort);

		eScenario.getPortModel().getPorts().add(ePort);

		return port;
	}

	@Override
	public IXYPort createPort(final String name, final float x, final float y) {

		IXYPort port = delegate.createPort(name, x, y);

		scenario.port.Port ePort = PortFactory.eINSTANCE.createPort();
		ePort.setName(name);
		ePorts.put(port, ePort);

		eScenario.getPortModel().getPorts().add(ePort);

		return port;
	}

	@Override
	public ITimeWindow createTimeWindow(final int start, final int end) {

		ITimeWindow window = delegate.createTimeWindow(start, end);
		return window;
	}

	@Override
	public IVessel createVessel(final String name,
			final IVesselClass vesselClass, IPort startPort, IPort endPort) {

		IVessel vessel = delegate.createVessel(name, vesselClass, startPort,
				endPort);

		scenario.fleet.Vessel eVessel = FleetFactory.eINSTANCE.createVessel();
		eVessel.setName(name);
		eVessel.setStartPort(ePorts.get(startPort));
		eVessel.setEndPort(ePorts.get(endPort));
		eVessel.setClass(eVesselClasses.get(vesselClass));

		FleetModel fleetModel = eScenario.getFleetModel();
		fleetModel.getFleet().add(eVessel);

		eVessels.put(vessel, eVessel);

		return vessel;
	}

	@Override
	public void setPortToPortDistance(final IPort from, final IPort to,
			final int distance) {

		delegate.setPortToPortDistance(from, to, distance);


		scenario.port.DistanceLine eLine = PortFactory.eINSTANCE.createDistanceLine();
		eLine.setFromPort(ePorts.get(from));
		eLine.setToPort(ePorts.get(to));
		eLine.setDistance(distance);
		
		eScenario.getDistanceModel().getDistances().add(eLine);
	}

	@Override
	public void setElementDurations(final ISequenceElement element,
			final IResource resource, final int duration) {

		delegate.setElementDurations(element, resource, duration);
	}

	@Override
	public IOptimisationData<ISequenceElement> getOptimisationData() {

		IOptimisationData<ISequenceElement> data = delegate
				.getOptimisationData();

		return data;
	}

	@Override
	public void dispose() {

		delegate.dispose();
	}

	@Override
	public IVesselClass createVesselClass(final String name,
			final int minSpeed, final int maxSpeed, final long capacity,
			final int minHeel, int baseFuelUnitPrice) {

		final IVesselClass vesselClass = delegate.createVesselClass(name,
				minSpeed, maxSpeed, capacity, minHeel, baseFuelUnitPrice);

		scenario.fleet.VesselClass eVesselClass = FleetFactory.eINSTANCE
				.createVesselClass();
		eVesselClass.setName(name);
		eVesselClass.setBaseFuelUnitPrice(baseFuelUnitPrice);
		eVesselClass.setCapacity(capacity);
		eVesselClass.setMinSpeed(minSpeed);
		eVesselClass.setMaxSpeed(maxSpeed);

		FleetModel fleetModel = eScenario.getFleetModel();
		fleetModel.getVesselClasses().add(eVesselClass);

		eVesselClasses.put(vesselClass, eVesselClass);

		return vesselClass;
	}

	@Override
	public void setVesselClassStateParamaters(final IVesselClass vesselClass,
			final VesselState state, final int nboRate, final int idleNBORate,
			final int idleConsumptionRate,
			final IConsumptionRateCalculator consumptionRateCalculator,
			final int nboSpeed) {

		delegate.setVesselClassStateParamaters(vesselClass, state, nboRate,
				idleNBORate, idleConsumptionRate, consumptionRateCalculator,
				nboSpeed);
		
		scenario.fleet.VesselState eState = null;
		switch (state) {
		case Ballast:
			eState = scenario.fleet.VesselState.BALLAST; 
			break;
		case Laden:
			eState = scenario.fleet.VesselState.LADEN;
			break;
		
		}
		
		scenario.fleet.VesselStateAttributes eAttributes = FleetFactory.eINSTANCE.createVesselStateAttributes();
		
		eAttributes.setIdleConsumptionRate(idleConsumptionRate);
		eAttributes.setIdleNBORate(idleNBORate);
		eAttributes.setNboRate(nboRate);
		eAttributes.setVesselState(eState);
		
		switch (state) {
		case Ballast:
			eVesselClasses.get(vesselClass).setBallastAttributes(eAttributes); 
			break;
		case Laden:
			eVesselClasses.get(vesselClass).setLadenAttributes(eAttributes);
			break;
		}
	}

	public void saveEMFModel(URI uri) {
		// TODO Auto-generated method stub
		final Map<Object, Object> saveOptions = new HashMap<Object, Object>();

		ResourceImpl r = new XMIResourceImpl(uri);
		r.getContents().add(eScenario);

		try {
			r.save(saveOptions);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
}
