package com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.metaheuristic;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.ILightWeightOptimisationData;
import com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.LightWeightCargoDetails;
import com.mmxlabs.models.lng.transformer.optimiser.lightweightscheduler.metaheuristic.TabuLightWeightSequenceOptimiser.Interval;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

public class TabuLightWeightSequenceOptimiserDataTransformer {
	double[][][] cargoToCargoCostsProcessed;
	List<List<Integer>> cargoVesselRestrictionAsList;
	double[] capacity;
	double[] cargoPNLasDouble;
	Interval[] loads;
	Interval[] discharges;
	LightWeightCargoDetails[] cargoDetails;

	TabuLightWeightSequenceOptimiserDataTransformer(ILightWeightOptimisationData lightWeightOptimisationData) {
		List<List<IPortSlot>> cargoes = lightWeightOptimisationData.getCargoes();
		List<IVesselAvailability> vessels = lightWeightOptimisationData.getVessels();
		long[] cargoPNL = lightWeightOptimisationData.getCargoPNL();
		Long[][][] cargoToCargoCostsOnAvailability = lightWeightOptimisationData.getCargoToCargoCostsOnAvailability();
		List<Set<Integer>> cargoVesselRestrictions = lightWeightOptimisationData.getCargoVesselRestrictions();
		LightWeightCargoDetails[] cargoDetails = lightWeightOptimisationData.getCargoDetails();
		
		// Capacity
		this.capacity = vessels.stream().mapToDouble(v -> v.getVessel().getCargoCapacity() / 1000).toArray();
		
		// CargoPNL
		this.cargoPNLasDouble = new double[cargoPNL.length];
		for (int i = 0; i < cargoPNLasDouble.length; i++) {
			cargoPNLasDouble[i] = (double) cargoPNL[i];
		}
		
		// Cargo Restriction
		this.cargoVesselRestrictionAsList = new ArrayList<List<Integer>>(cargoVesselRestrictions.size());
		for (Set<Integer> restriction : cargoVesselRestrictions) {
			cargoVesselRestrictionAsList.add(restriction.stream().collect(Collectors.toList()));
		}

		for (int i = 0; i < cargoDetails.length; i++) {
			LightWeightCargoDetails cargoDetail = cargoDetails[i];

			if (cargoDetail.getType() == PortType.DryDock || cargoDetail.getType() == PortType.CharterOut) {
				if (cargoDetail.isAssociatedToVessel()) {
					this.cargoVesselRestrictionAsList.get(i).add(cargoDetail.getVessel());
				}
			}
		}
		
		// Cargo To Cargo cost
		this.cargoToCargoCostsProcessed = new double[cargoToCargoCostsOnAvailability.length][cargoToCargoCostsOnAvailability[0].length][cargoToCargoCostsOnAvailability[0][0].length];
		for (int i = 0; i < cargoToCargoCostsOnAvailability.length; i++) {
			for (int j = 0; j < cargoToCargoCostsOnAvailability[i].length; j++) {
				for (int k = 0; k < cargoToCargoCostsOnAvailability[i][j].length; k++) {
					this.cargoToCargoCostsProcessed[i][j][k] = (double) cargoToCargoCostsOnAvailability[i][j][k] / 1_000_000.0;
				}
			}
		}

		// Loads & discharges
		this.loads = new Interval[cargoes.size()];
		this.discharges = new Interval[cargoes.size()];

		for (int i = 0; i < cargoes.size(); i++) {
			ITimeWindow loadTW = cargoes.get(i).get(0).getTimeWindow();
			ITimeWindow dischargeTW = cargoes.get(i).get(1).getTimeWindow();

			this.loads[i] = new Interval(loadTW.getInclusiveStart(), loadTW.getExclusiveEnd());
			this.discharges[i] = new Interval(dischargeTW.getInclusiveStart(), dischargeTW.getExclusiveEnd());
		}
	}

	public double[][][] getCargoToCargoCostsProcessed() {
		return cargoToCargoCostsProcessed;
	}

	public List<List<Integer>> getCargoVesselRestrictionAsList() {
		return cargoVesselRestrictionAsList;
	}

	public double[] getCapacity() {
		return capacity;
	}

	public double[] getCargoPNLasDouble() {
		return cargoPNLasDouble;
	}

	public Interval[] getLoads() {
		return loads;
	}

	public Interval[] getDischarges() {
		return discharges;
	}

	public LightWeightCargoDetails[] getCargoDetails() {
		return cargoDetails;
	}
	
}
