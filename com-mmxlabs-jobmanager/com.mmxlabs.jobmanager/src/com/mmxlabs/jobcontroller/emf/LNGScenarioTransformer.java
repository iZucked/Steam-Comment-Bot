package com.mmxlabs.jobcontroller.emf;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.management.timer.Timer;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import scenario.Scenario;
import scenario.ScenarioPackage;
import scenario.cargo.Cargo;
import scenario.cargo.Slot;
import scenario.fleet.FuelConsumptionLine;
import scenario.fleet.PortAndTime;
import scenario.fleet.Vessel;
import scenario.fleet.VesselClass;
import scenario.fleet.VesselStateAttributes;
import scenario.optimiser.OptimisationSettings;
import scenario.port.Canal;
import scenario.port.DistanceLine;
import scenario.port.PartialDistance;
import scenario.port.Port;

import com.mmxlabs.common.Association;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.builder.impl.SchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.components.IStartEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.impl.InterpolatingConsumptionRateCalculator;

/**
 * Wrapper for an EMF LNG Scheduling {@link scenario.Scenario}, providing utility methods to convert it into
 * an optimization job. Typical usage is to construct an LNGScenarioTransformer with a given Scenario, and then call
 * the {@link createOptimisationData} method
 * 
 * @author hinton
 *
 */
public class LNGScenarioTransformer {
	private Scenario scenario;
	private TimeZone timezone;
	private Date earliestTime;
	private Date latestTime;

	/**
	 * Create a transformer for the given scenario; the class holds a reference, so changes
	 * made to the scenario after construction will be reflected in calls to the various helper methods.
	 * 
	 * @param scenario
	 */
	public LNGScenarioTransformer(Scenario scenario) {
		init(scenario);
	}
	
	/*
	 * Create a transformer by loading a scenario from a URI
	 */
	public LNGScenarioTransformer(URI uri) {
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put
		(Resource.Factory.Registry.DEFAULT_EXTENSION, 
				new XMIResourceFactoryImpl());
		resourceSet.getPackageRegistry().put(ScenarioPackage.eNS_URI, ScenarioPackage.eINSTANCE);
		
		Resource resource = resourceSet.getResource(uri, true);
		for (EObject e : resource.getContents()) {
			if (e instanceof Scenario) {
				init((Scenario) e);
			}
		}
	}
	
	protected void init(Scenario scenario) {
		this.scenario = scenario;
		this.timezone = TimeZone.getTimeZone("UTC");
	}

	/**
	 * Instantiates and returns an {@link IOptimisationData} isomorphic to the contained scenario.
	 * @return
	 * @throws IncompleteScenarioException 
	 */
	public IOptimisationData<ISequenceElement> createOptimisationData() throws IncompleteScenarioException {
		SchedulerBuilder builder = new SchedulerBuilder();
		/**
		 * Bidirectionally maps EMF {@link Port} Models to {@link IPort}s in the builder.
		 */
		Association<Port, IPort> portAssociation = new Association<Port, IPort>();
		/**
		 * Lists all the {@link IPort}s created for this scenario 
		 */
		List<IPort> allPorts = new ArrayList<IPort>();
		/**
		 * A reverse-lookup for the {@link allPorts} array.
		 */
		Map<IPort, Integer> portIndices = new HashMap<IPort, Integer>();
		/*
		 * Construct ports for each port in the scenario port model, and keep them in a
		 * two-way lookup table (the two-way lookup is needed to do things like setting distances later).
		 */
		for (Port ePort : scenario.getPortModel().getPorts()) {
			IPort port = builder.createPort(ePort.getName());
			portAssociation.add(ePort, port);
			portIndices.put(port, allPorts.size());
			allPorts.add(port);
		}

		buildDistances(builder, portAssociation, allPorts, portIndices);
		buildFleet(builder, portAssociation);
		buildCargoes(builder, portAssociation);
		return builder.getOptimisationData();
	}
	/**
	 * Extract the cargoes from the scenario and add them to the given builder.
	 * @param builder current builder. should already have ports/distances/vessels built
	 */
	private void buildCargoes(SchedulerBuilder builder, Association<Port, IPort> ports) {
		/*
		 * Find the earliest date, to convert from absolute date and time to offset hours 
		 */
		earliestTime = null;
		latestTime = null;
		for (Cargo eCargo: scenario.getCargoModel().getCargoes()) {
			final Date loadDate = eCargo.getLoadSlot().getWindowStart();
			if (earliestTime == null || earliestTime.after(loadDate)) {
				earliestTime = loadDate;
			}
			if (latestTime == null || latestTime.before(loadDate)) {
				latestTime = loadDate;
			}
		}
		
		for (Cargo eCargo: scenario.getCargoModel().getCargoes()) {
			//not escargot.
			Slot loadSlot = eCargo.getLoadSlot();
			Slot dischargeSlot = eCargo.getDischargeSlot();
			int loadStart = convertTime(earliestTime, loadSlot.getWindowStart());
			int dischargeStart = convertTime(earliestTime, dischargeSlot.getWindowStart());
			
			//TODO check units again
			ITimeWindow loadWindow = builder.createTimeWindow(loadStart, loadStart + loadSlot.getWindowDuration());
			ITimeWindow dischargeWindow = builder.createTimeWindow(dischargeStart, dischargeStart + dischargeSlot.getWindowDuration());
			
			ILoadSlot load = builder.createLoadSlot(loadSlot.getId(), 
					ports.lookup(loadSlot.getPort()), 
					loadWindow, 
					loadSlot.getMinQuantity(), 
					loadSlot.getMaxQuantity(), 
					loadSlot.getUnitPrice(), loadSlot.getCargoCVValue());
			
			IDischargeSlot discharge = builder.createDischargeSlot(dischargeSlot.getId(), 
					ports.lookup(dischargeSlot.getPort()), 
					dischargeWindow, 
					dischargeSlot.getMinQuantity(),
					dischargeSlot.getMaxQuantity(), 
					dischargeSlot.getUnitPrice());
			
			builder.createCargo(eCargo.getId(), load, discharge);
		}
	}

	/**
	 * Convert a date into relative hours; returns the number of hours between windowStart and earliest.
	 * @param earliest
	 * @param windowStart
	 * @return number of hours between earliest and windowStart
	 */
	private int convertTime(Date earliest, Date windowStart) {
		//I am using two calendars, because the java date objects are all
		//deprecated; however, timezones should not be a problem because
		//every Date in the EMF representation is in UTC.
		Calendar a = Calendar.getInstance(timezone);
		a.setTime(earliest);
		Calendar b = Calendar.getInstance(timezone);
		b.setTime(windowStart);
		long difference = b.getTimeInMillis() - a.getTimeInMillis();
		return (int)(difference / Timer.ONE_HOUR);
	}

	/**
	 * Create the distance matrix for the given builder.
	 * @param builder the builder we are working with
	 * @param portAssociation an association between ports in the EMF model and IPorts in the builder
	 * @param allPorts the list of all IPorts constructed so far
	 * @param portIndices a reverse-lookup for the ports in allPorts
	 * @throws IncompleteScenarioException
	 */
	private void buildDistances(SchedulerBuilder builder,
			Association<Port, IPort> portAssociation, List<IPort> allPorts,
			Map<IPort, Integer> portIndices) throws IncompleteScenarioException {
		boolean[][] modeledEdges = new boolean[allPorts.size()][allPorts.size()];
		int[][] defaultDistances = new int[allPorts.size()][allPorts.size()];
		/*
		 * Now fill out the distances from the distance model.
		 * Firstly we need to create the default distance matrix.
		 */
		for (DistanceLine dl : scenario.getDistanceModel().getDistances()) {
			IPort from, to;
			from = portAssociation.lookup(dl.getFromPort());
			to = portAssociation.lookup(dl.getToPort());

			modeledEdges[portIndices.get(from)][portIndices.get(to)] = true;

			final int distance = Calculator.scale(dl.getDistance());
			defaultDistances[portIndices.get(from)][portIndices.get(to)] = 
				distance;
			builder.setPortToPortDistance(from, to, IMultiMatrixProvider.Default_Key, 
					distance);
			
		}
//		StringBuilder missingEdges = new StringBuilder();
//		/*
//		 * Check whether any entries were missing from the distance matrix
//		 */
//		for (int i = 0; i<modeledEdges.length; i++) {
//			for (int j = 0; j<modeledEdges.length; j++) {
//				if (modeledEdges[i][j] == false) {
//					if (missingEdges.length() > 0) missingEdges.append(", ");
//					missingEdges.append("from " + allPorts.get(i).getName() + " to " + allPorts.get(j).getName());
//				}
//			}
//		}
//
//		if (missingEdges.length() > 0) {
//			throw new IncompleteScenarioException("Missing edges in distance model : " + missingEdges.toString());
//		}
		
		/*
		 * Next we need to handle the secondary distance matrices for each canal.
		 */
		if (scenario.getCanalModel() != null) {
			for (Canal canal : scenario.getCanalModel().getCanals()) {
				/*
				 * Each canal contains a list of partial distances to enter/leave the canal from/to a particular port,
				 * so the entryDistances are distances from port to whichever end of the canal is closest,
				 * and the exitDistances from whichever end of the canal is closest to the port. 
				 * 
				 * There is a small gotcha here about the possibility of entering and leaving the port from the same end;
				 * because the entry/exit distances don't know which end they are, a peculiar distance matrix might cause
				 * canal edges which don't actually involve going through the canal. This can only happen when the distance
				 * matrix doesn't contain the shortest free path between two points.
				 */
				final String name = canal.getName();
				final int distance = Calculator.scale(canal.getDistance());

				for (PartialDistance enter : canal.getEntryDistances()) {
					for (PartialDistance exit : canal.getExitDistances()) {
						final IPort from = portAssociation.lookup(enter.getPort());
						final IPort to = portAssociation.lookup(exit.getPort());

						final int defaultDistance = defaultDistances[portIndices.get(from)]
						                                             [portIndices.get(to)];

						final int distanceViaCanal = distance + 
						Calculator.scale(enter.getDistance()) +
						Calculator.scale(exit.getDistance());	

						if (distanceViaCanal < defaultDistance) {
							//TODO this currently triggers an NPE, because I don't know how to specify
							//that an extra distance matrix should be created.
							builder.setPortToPortDistance(from, to, name, distanceViaCanal);
						}
					}
				}
			}
		}
	}

	/**
	 * Construct the fleet model for the scenario
	 * @param builder a builder which has had its ports and distances instantiated
	 * @param portAssociation the Port <-> IPort association to connect EMF Ports with builder IPorts
	 */
	private void buildFleet(SchedulerBuilder builder,
			Association<Port, IPort> portAssociation) {

		/*
		 * Build the fleet model - first we must create the vessel classes from the model
		 */
		Association<VesselClass, IVesselClass> vesselClassAssociation = new Association<VesselClass, IVesselClass>();

		for (VesselClass eVc : scenario.getFleetModel().getVesselClasses()) {
			IVesselClass vc = builder.createVesselClass(eVc.getName(), 
					Calculator.scaleToInt(eVc.getMinSpeed()), Calculator.scaleToInt(eVc.getMaxSpeed()), 
					Calculator.scale(eVc.getCapacity() * eVc.getFillCapacity()), // TODO is capacity mean to be scaled?
					Calculator.scaleToInt(eVc.getMinHeelVolume()),
					eVc.getBaseFuelUnitPrice(),
					Calculator.scaleToInt(eVc.getBaseFuelEquivalenceFactor()),
					eVc.getDailyCharterPrice() * 24);
			vesselClassAssociation.add(eVc, vc);

			/*
			 * Set state-specific attributes
			 */
			buildVesselStateAttributes(builder, vc, com.mmxlabs.scheduler.optimiser.components.VesselState.Laden,eVc.getLadenAttributes());
			buildVesselStateAttributes(builder, vc, com.mmxlabs.scheduler.optimiser.components.VesselState.Ballast,eVc.getBallastAttributes());
			
			/*
			 * set up inaccessible ports by applying resource allocation constraints
			 */
			Set<IPort> inaccessiblePorts = new HashSet<IPort>();
			for (Port ePort : eVc.getInaccessiblePorts()) {
				final IPort port = portAssociation.lookup(ePort);
				inaccessiblePorts.add(port);
			}
			
			if (inaccessiblePorts.isEmpty() == false) {
				builder.setVesselClassInaccessiblePorts(vc, inaccessiblePorts);
			}
		}

		/*
		 * Now create each vessel
		 */
		for (Vessel eV : scenario.getFleetModel().getFleet()) {
			IStartEndRequirement startRequirement = createRequirement(builder, portAssociation, eV.getStartRequirement());
			IStartEndRequirement endRequirement = createRequirement(builder, portAssociation, eV.getEndRequirement());
			
			IVessel v = builder.createVessel(eV.getName(), 
					vesselClassAssociation.lookup(eV.getClass_()),
					startRequirement, endRequirement);
		}
		
		/*
		 * Create spot charter vessels with no start/end requirements
		 */
		IStartEndRequirement blankRequirement = builder.createStartEndRequirement();
		for (VesselClass eVc : scenario.getFleetModel().getVesselClasses()) {
			if (eVc.getSpotCharterCount() > 0)
				builder.createSpotVessels("SPOT-" + eVc.getName(), vesselClassAssociation.lookup(eVc), eVc.getSpotCharterCount());
		}
	}

	/**
	 * Convert a PortAndTime from the EMF to an IStartEndRequirement for internal use; may be subject to change later.
	 * @param builder
	 * @param portAssociation
	 * @param pat
	 * @return
	 */
	private IStartEndRequirement createRequirement(SchedulerBuilder builder, Association<Port, IPort> portAssociation, PortAndTime pat) {
		if (pat == null) {
			return builder.createStartEndRequirement();
		}
		if (pat.isSetPort() && pat.isSetTime()) {
			return builder.createStartEndRequirement(
					portAssociation.lookup(pat.getPort()),
					convertTime(pat.getTime()));
		} else if (pat.isSetPort()) {
			return builder.createStartEndRequirement(portAssociation.lookup(pat.getPort()));
		} else if (pat.isSetTime()) {
			return builder.createStartEndRequirement(convertTime(pat.getTime()));
		} else {
			return builder.createStartEndRequirement();
		}
	}
	
	private int convertTime(Date startTime) {
		return convertTime(earliestTime, startTime);
	}

	/**
	 * Tell the builder to set up the given vessel state from the EMF fleet model
	 * @param builder the builder which is currently in use
	 * @param vc the {@link IVesselClass} which the builder has constructed whose attributes we are setting 
	 * @param laden the {@link com.mmxlabs.scheduler.optimiser.components.VesselState} we are setting attributes for
	 * @param ladenAttributes the {@link VesselStateAttributes} from the EMF model
	 */
	private void buildVesselStateAttributes(SchedulerBuilder builder,
			IVesselClass vc,
			com.mmxlabs.scheduler.optimiser.components.VesselState state,
			VesselStateAttributes attrs) {

		//create consumption rate calculator for the curve
		TreeMap<Integer, Long> keypoints = new TreeMap<Integer, Long>();

		for (FuelConsumptionLine line : attrs.getFuelConsumptionCurve()) {
			keypoints.put(Calculator.scaleToInt(line.getSpeed()), Calculator.scale(line.getConsumption()));
		}

		InterpolatingConsumptionRateCalculator consumptionCalculator = new InterpolatingConsumptionRateCalculator(keypoints);

		builder.setVesselClassStateParamaters(vc, state, 
				Calculator.scaleToInt(attrs.getNboRate()), 
				Calculator.scaleToInt(attrs.getIdleNBORate()), 
				Calculator.scaleToInt(attrs.getIdleConsumptionRate()), 
				consumptionCalculator);
	}

	/**
	 * Utility method for getting the current optimisation settings from this scenario.
	 * TODO maybe put this in another file/model somewhere else.
	 * @return
	 */
	public OptimisationSettings getOptimisationSettings() {
		if (scenario.getOptimisation() != null) {
			return scenario.getOptimisation().getCurrentSettings();
		} else {
			return null;
		}
	}
}
