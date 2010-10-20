/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.jobcontroller.core.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
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
import scenario.port.Port;
import scenario.port.PortFactory;
import scenario.schedule.ScheduleFactory;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.ICargo;
import com.mmxlabs.scheduler.optimiser.components.ICharterOut;
import com.mmxlabs.scheduler.optimiser.components.IConsumptionRateCalculator;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IStartEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.IXYPort;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
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
	private final Map<IDischargeSlot, scenario.cargo.Slot> eDischargeSlots = new HashMap<IDischargeSlot, scenario.cargo.Slot>();
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
			final long maxVolume, final int unitPrice, int cargoCVValue, final int duration) {

		ILoadSlot slot = delegate.createLoadSlot(id, port, window, minVolume,
				maxVolume, unitPrice, cargoCVValue, duration);

		scenario.cargo.LoadSlot eLoadSlot = CargoFactory.eINSTANCE
				.createLoadSlot();
		eLoadSlot.setId(id);
		eLoadSlot.setMaxQuantity((int) maxVolume);
		eLoadSlot.setMinQuantity((int) minVolume);
		eLoadSlot.setPort(ePorts.get(port));
		eLoadSlot.setUnitPrice(unitPrice);
		eLoadSlot.setCargoCVvalue(cargoCVValue);
		Calendar now = Calendar.getInstance();
		now.add(Calendar.HOUR_OF_DAY, window.getStart());
		eLoadSlot.setWindowStart(now.getTime());
		eLoadSlot.setWindowDuration(window.getEnd() - window.getStart());

		eLoadSlot.setSlotDuration(duration);
		
		eLoadSlots.put(slot, eLoadSlot);

		return slot;
	}

	@Override
	public IDischargeSlot createDischargeSlot(final String id,
			final IPort port, final ITimeWindow window, final long minVolume,
			final long maxVolume, final int unitPrice, final int duration) {

		IDischargeSlot slot = delegate.createDischargeSlot(id, port, window,
				minVolume, maxVolume, unitPrice, duration);

		scenario.cargo.Slot eDischargeSlot = CargoFactory.eINSTANCE
				.createSlot();
		eDischargeSlot.setId(id);
		eDischargeSlot.setMaxQuantity((int) maxVolume);
		eDischargeSlot.setMinQuantity((int) minVolume);
		eDischargeSlot.setPort(ePorts.get(port));
		eDischargeSlot.setUnitPrice(unitPrice);
		Calendar now = Calendar.getInstance();
		now.add(Calendar.HOUR_OF_DAY, window.getStart());
		eDischargeSlot.setWindowStart(now.getTime());
		eDischargeSlot.setWindowDuration(window.getEnd() - window.getStart());

		eDischargeSlot.setSlotDuration(duration);
		
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
	public void setPortToPortDistance(final IPort from, final IPort to,
			final String route, final int distance) {

		delegate.setPortToPortDistance(from, to, route, distance);


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
			final int minHeel, int baseFuelUnitPrice, int baseFuelEquivalenceFactor) {

		final IVesselClass vesselClass = delegate.createVesselClass(name,
				minSpeed, maxSpeed, capacity, minHeel, baseFuelUnitPrice, baseFuelEquivalenceFactor);

		scenario.fleet.VesselClass eVesselClass = FleetFactory.eINSTANCE
				.createVesselClass();
		eVesselClass.setName(name);
		eVesselClass.setBaseFuelUnitPrice(baseFuelUnitPrice);
		eVesselClass.setBaseFuelEquivalenceFactor(baseFuelEquivalenceFactor);
		eVesselClass.setCapacity(capacity);
		eVesselClass.setMinSpeed(minSpeed);
		eVesselClass.setMaxSpeed(maxSpeed);

		eVesselClass.setMinHeelVolume(minHeel);
		
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

	@Override
	public IVesselClass createVesselClass(String name, int minSpeed,
			int maxSpeed, long capacity, int minHeel, int baseFuelUnitPrice, int baseFuelEquivalenceFactor,
			int hourlyCharterPrice) {
		return createVesselClass(name, minSpeed, maxSpeed, capacity, minHeel, baseFuelUnitPrice, baseFuelEquivalenceFactor);
	}

	@Override
	public IVessel createVessel(String name, IVesselClass vesselClass,
			IStartEndRequirement startConstraint,
			IStartEndRequirement endConstraint) {
		
		
		IVessel vessel = delegate.createVessel(name, vesselClass, startConstraint, endConstraint);

		scenario.fleet.Vessel eVessel = FleetFactory.eINSTANCE.createVessel();
		eVessel.setName(name);
		eVessel.setClass(eVesselClasses.get(vesselClass));

		eVessel.setStartRequirement(FleetFactory.eINSTANCE.createPortAndTime());
		eVessel.setEndRequirement(FleetFactory.eINSTANCE.createPortAndTime());
		
		//TODO fix time constraints
		if (startConstraint.hasPortRequirement()) {
			eVessel.getStartRequirement().setPort(ePorts.get(startConstraint.getLocation()));
		}
		
		if (endConstraint.hasPortRequirement()) {
			eVessel.getEndRequirement().setPort(ePorts.get(endConstraint.getLocation()));
		}
		
		FleetModel fleetModel = eScenario.getFleetModel();
		fleetModel.getFleet().add(eVessel);

		eVessels.put(vessel, eVessel);

		return vessel;
	}

	@Override
	public IVessel createVessel(String name, IVesselClass vesselClass,
			VesselInstanceType vesselInstanceType, IStartEndRequirement start,
			IStartEndRequirement end) {
		return null;
	}

	@Override
	public IStartEndRequirement createStartEndRequirement() {
		return delegate.createStartEndRequirement();
	}

	@Override
	public IStartEndRequirement createStartEndRequirement(IPort fixedPort) {
		return delegate.createStartEndRequirement(fixedPort);
		
	}

	@Override
	public IStartEndRequirement createStartEndRequirement(int fixedTime) {
		return delegate.createStartEndRequirement(fixedTime);
	}

	@Override
	public IStartEndRequirement createStartEndRequirement(IPort fixedPort,
			int fixedTime) {
		return delegate.createStartEndRequirement(fixedPort, fixedTime);
	}

	@Override
	public List<IVessel> createSpotVessels(String namePrefix,
			IVesselClass vesselClass, int count) {
		List<IVessel> answer = new ArrayList<IVessel>(count);
		for (int i = 0; i<count; i++) {
			answer.add(createSpotVessel(namePrefix + "-" + (i+1), vesselClass));
		}
		return answer;
	}

	@Override
	public IVessel createSpotVessel(String name, IVesselClass vesselClass) {
		IStartEndRequirement start = createStartEndRequirement();
		IStartEndRequirement end = createStartEndRequirement();
		
		return createVessel(name, vesselClass, VesselInstanceType.SPOT_CHARTER, start, end);
	}

	@Override
	public void setVesselClassInaccessiblePorts(IVesselClass vc,
			Set<IPort> inaccessiblePorts) {
		delegate.setVesselClassInaccessiblePorts(vc, inaccessiblePorts);
		EList<Port> ports = eVesselClasses.get(vc).getInaccessiblePorts();
		for (IPort port : inaccessiblePorts) {
			ports.add(ePorts.get(port));
		}
	}

	public ICharterOut createCharterOut(ITimeWindow arrivalTimeWindow,
			IPort port, int durationHours) {
		return delegate
				.createCharterOut(arrivalTimeWindow, port, durationHours);
	}

	public void addCharterOutVessel(ICharterOut charterOut, IVessel vessel) {
		delegate.addCharterOutVessel(charterOut, vessel);
	}

	public void addCharterOutVesselClass(ICharterOut charterOut,
			IVesselClass vesselClass) {
		delegate.addCharterOutVesselClass(charterOut, vesselClass);
	}

	@Override
	public void setVesselClassRouteCost(String route, IVesselClass vesselClass,
			VesselState state, int tollPrice) {
		delegate.setVesselClassRouteCost(route, vesselClass, state, tollPrice);
		
	}

	@Override
	public void setDefaultRouteCost(String route, int defaultPrice) {
		delegate.setDefaultRouteCost(route, defaultPrice);
	}
	
	
}
