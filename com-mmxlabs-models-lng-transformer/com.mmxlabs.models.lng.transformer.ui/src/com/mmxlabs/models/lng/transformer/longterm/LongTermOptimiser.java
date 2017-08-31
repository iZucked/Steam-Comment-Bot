/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.longterm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.parameters.Constraint;
import com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.transformer.chain.impl.LNGDataTransformer;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.optimiser.valuepair.LoadDischargePairValueCalculatorUnit;
import com.mmxlabs.models.lng.transformer.optimiser.valuepair.ProfitAndLossExtractor;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.constraints.impl.LadenLegLimitConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.MinMaxVolumeConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.PromptRoundTripVesselPermissionConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.RoundTripVesselPermissionConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.providers.ILongTermSlotsProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVirtualVesselSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * Long term optimisation. Drives a long term optimisation using an {@link ILongTermMatrixOptimiser}.
 * @author achurchill
 *
 */
public class LongTermOptimiser {

	@Inject
	private ILongTermSlotsProvider longTermSlotsProvider;

	@Inject
	private ILongTermMatrixOptimiser matrixOptimiser;

	@Inject
	private LongTermOptimisationData optimiserRecorder;

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private IVirtualVesselSlotProvider virtualVesselSlotProvider;

	/**
	 * Perform a long term (nominal) trading optimisation
	 * @param executorService
	 * @param dataTransformer
	 * @param charterInMarket
	 * @return
	 */
	public Pair<ISequences, Long> optimise(final ExecutorService executorService, final LNGDataTransformer dataTransformer, CharterInMarket charterInMarket) {
		// (1) Identify LT slots
		@NonNull
		Collection<IPortSlot> longTermSlots = longTermSlotsProvider.getLongTermSlots();
		List<ILoadOption> loads = longTermSlots.stream().filter(s -> (s instanceof ILoadOption)).map(m -> (ILoadOption) m).collect(Collectors.toCollection(ArrayList::new));
		List<IDischargeOption> discharges = longTermSlots.stream().filter(s -> (s instanceof IDischargeOption)).map(m -> (IDischargeOption) m).collect(Collectors.toCollection(ArrayList::new));
		optimiserRecorder.init(loads, discharges);
		// (2) Generate S2S bindings matrix for LT slots
		ExecutorService es = Executors.newSingleThreadExecutor();
		getS2SBindings(loads, discharges, charterInMarket, es, dataTransformer, optimiserRecorder);
		// now using our profits recorder we have a full matrix of constraints
		// and pnl
		Long[][] profit = optimiserRecorder.getProfit();
		// (3) Optimise matrix
//		produceDataForGurobi(optimiserRecorder, "c:/dev/data/");
		boolean[][] pairingsMatrix = matrixOptimiser.findOptimalPairings(profit, optimiserRecorder.getOptionalLoads(), optimiserRecorder.getOptionalDischarges());
		if (false) {
			try {
				pairingsMatrix = getPrestoredBooleans2D("c:/dev/data/gurobi_allocations.lt");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			}
		}

		// (4) Export the pairings matrix to a Map
		Map<ILoadOption, IDischargeOption> pairingsMap = new HashMap<>();
		for (ILoadOption load : loads) {
			pairingsMap.put(load, optimiserRecorder.getPairedDischarge(load, pairingsMatrix));
		}

		if (false) {
		// print pairings for debug
		printPairings(loads, pairingsMatrix);
		}
		
		// (5) Export the pairings matrix to the raw sequences
		ModifiableSequences rawSequences = new ModifiableSequences(dataTransformer.getInitialSequences());
		IResource nominal = getNominal(rawSequences, charterInMarket);
		assert nominal != null;
		updateSequences(rawSequences, pairingsMap, nominal);
		return new Pair<>(rawSequences, 0L);
	}

	/**
	 * Create a slot 2 slot value matrix
	 * 
	 * @param loads
	 * @param discharges
	 * @param nominalMarket
	 * @param executorService
	 * @param dataTransformer
	 * @param optimiserRecorder
	 */
	private void getS2SBindings(final List<ILoadOption> loads, final List<IDischargeOption> discharges, final @NonNull CharterInMarket nominalMarket, ExecutorService executorService,
			final LNGDataTransformer dataTransformer, final LongTermOptimisationData optimiserRecorder) {
		final ConstraintAndFitnessSettings constraintAndFitnessSettings = ScenarioUtils.createDefaultConstraintAndFitnessSettings();
		// TODO: Filter
		final Iterator<Constraint> iterator = constraintAndFitnessSettings.getConstraints().iterator();
		while (iterator.hasNext()) {
			final Constraint constraint = iterator.next();
			if (constraint.getName().equals(PromptRoundTripVesselPermissionConstraintCheckerFactory.NAME)) {
				iterator.remove();
			}
			if (constraint.getName().equals(RoundTripVesselPermissionConstraintCheckerFactory.NAME)) {
				iterator.remove();
			}
		}
		ScenarioUtils.createOrUpdateContraints(LadenLegLimitConstraintCheckerFactory.NAME, true, constraintAndFitnessSettings);
		ScenarioUtils.createOrUpdateContraints(MinMaxVolumeConstraintCheckerFactory.NAME, true, constraintAndFitnessSettings);

		final LoadDischargePairValueCalculatorUnit calculator = new LoadDischargePairValueCalculatorUnit(dataTransformer, "pairing-stage", dataTransformer.getUserSettings(),
				constraintAndFitnessSettings, executorService, dataTransformer.getInitialSequences(), dataTransformer.getInitialResult(), Collections.emptyList());
		calculator.run(nominalMarket, loads, discharges, new NullProgressMonitor(), new ProfitAndLossExtractor(optimiserRecorder));
	}

	/**
	 * Produces serialized data that can be used by Gurobi
	 * @param longTermOptimisationData
	 * @param path
	 */
	private void produceDataForGurobi(LongTermOptimisationData longTermOptimisationData, String path) {
		long[][] profitAsPrimitive = longTermOptimisationData.getProfitAsPrimitive();
		boolean[][] valid = longTermOptimisationData.getValid();
		boolean[] optionalLoads = longTermOptimisationData.getOptionalLoads();
		boolean[] optionalDischarges = longTermOptimisationData.getOptionalDischarges();
		serialize(profitAsPrimitive, path + "profit.lt");
		serialize(valid, path + "constraints.lt");
		serialize(optionalLoads, path + "loads.lt");
		serialize(optionalDischarges, path + "discharges.lt");
	}

	/**
	 * Updates the raw sequences given an allocations matrix
	 * @param rawSequences
	 * @param pairingsMap
	 * @param nominal
	 */
	private void updateSequences(@NonNull IModifiableSequences rawSequences, @NonNull Map<ILoadOption, IDischargeOption> pairingsMap, @NonNull IResource nominal) {
		moveElementsToUnusedList(rawSequences);
		IModifiableSequence modifiableSequence = rawSequences.getModifiableSequence(nominal);
		int insertIndex = 0;
		for (int i = 0; i < modifiableSequence.size(); i++) {
			if (portSlotProvider.getPortSlot(modifiableSequence.get(i)) != null && portSlotProvider.getPortSlot(modifiableSequence.get(i)).getPortType() == PortType.End) {
				break;
			}
			insertIndex++;
		}
		List<ISequenceElement> unusedElements = rawSequences.getModifiableUnusedElements();
		for (ILoadOption loadOption : pairingsMap.keySet()) {
			IDischargeOption dischargeOption = pairingsMap.get(loadOption);
			// skip if unallocated
			if (dischargeOption == null)
				continue;
			if (loadOption instanceof ILoadSlot && dischargeOption instanceof IDischargeSlot) {
				// FOB-DES
				modifiableSequence.insert(insertIndex++, portSlotProvider.getElement(loadOption));
				modifiableSequence.insert(insertIndex++, portSlotProvider.getElement(dischargeOption));
				unusedElements.remove(portSlotProvider.getElement(loadOption));
				unusedElements.remove(portSlotProvider.getElement(dischargeOption));
			} else if (loadOption instanceof ILoadSlot && dischargeOption instanceof IDischargeOption) {
				// Fob Sale
				IResource resource = getVirtualResource(dischargeOption);
				if (resource != null) {
					IModifiableSequence fobSale = rawSequences.getModifiableSequence(resource);
					insertCargo(unusedElements, loadOption, dischargeOption, fobSale);
				}
			} else if (loadOption instanceof ILoadOption && dischargeOption instanceof IDischargeSlot) {
				// DES purchase
				IResource resource = getVirtualResource(loadOption);
				if (resource != null) {
					IModifiableSequence desPurchase = rawSequences.getModifiableSequence(resource);
					insertCargo(unusedElements, loadOption, dischargeOption, desPurchase);
				}
			}
		}
	}

	private IResource getVirtualResource(IPortSlot portSlot) {
		IVesselAvailability vesselAvailability = virtualVesselSlotProvider.getVesselAvailabilityForElement(portSlotProvider.getElement(portSlot));
		IResource resource = vesselProvider.getResource(vesselAvailability);
		return resource;
	}

	/**
	 * Moves everything to the unused list
	 * @param sequences
	 */
	private void moveElementsToUnusedList(IModifiableSequences sequences) {
		@NonNull
		List<@NonNull IResource> resources = sequences.getResources();
		for (@NonNull
		IResource resource : resources) {
			IModifiableSequence modifiableSequence = sequences.getModifiableSequence(resource);
			List<ISequenceElement> modifiableUnusedElements = sequences.getModifiableUnusedElements();
			for (int i = modifiableSequence.size() - 1; i > -1; i--) {
				if (portSlotProvider.getPortSlot(modifiableSequence.get(i)) instanceof IDischargeOption || portSlotProvider.getPortSlot(modifiableSequence.get(i)) instanceof ILoadOption) {
					ISequenceElement removed = modifiableSequence.remove(i);
					modifiableUnusedElements.add(removed);
				}
			}
		}
	}

	private void insertCargo(List<ISequenceElement> unusedElements, ILoadOption loadOption, IDischargeOption dischargeOption, IModifiableSequence desPurchase) {
		desPurchase.insert(1, portSlotProvider.getElement(loadOption));
		unusedElements.remove(portSlotProvider.getElement(loadOption));
		desPurchase.insert(2, portSlotProvider.getElement(dischargeOption));
		unusedElements.remove(portSlotProvider.getElement(dischargeOption));
	}

	private IResource getNominal(ModifiableSequences rawSequences, CharterInMarket charterInMarket) {
		for (IResource resource : rawSequences.getResources()) {
			if (resource.getName().contains(charterInMarket.getName()) && vesselProvider.getVesselAvailability(resource).getVesselInstanceType() == VesselInstanceType.ROUND_TRIP) {
				return resource;
			}
		}
		return null;
	}

	private void printPairings(List<ILoadOption> loads, boolean[][] pairingsMatrix) {
		for (ILoadOption load : loads) {
			System.out.println(String.format("%s to %s", load.getId(),
					optimiserRecorder.getPairedDischarge(load, pairingsMatrix) == null ? "null" : optimiserRecorder.getPairedDischarge(load, pairingsMatrix).getId()));
			if (load.getId().contains("Rejected PS4")) {
				System.out.println(String.format("index %s to %s", optimiserRecorder.getIndex(load), optimiserRecorder.getIndex(optimiserRecorder.getPairedDischarge(load, pairingsMatrix))));
			}
		}
	}

	private void serialize(Serializable serializable, String path) {
		try {
			File f = new File(path);
			f.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
		ObjectOutputStream oos = null;
		FileOutputStream fout = null;
		try {
			fout = new FileOutputStream(path, false);
			oos = new ObjectOutputStream(fout);
			oos.writeObject(serializable);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static boolean[][] getPrestoredBooleans2D(String path) throws IOException {
		ObjectInputStream objectinputstream = null;
		boolean[][] readCase = null;
		try {
			FileInputStream streamIn = new FileInputStream(path);
			objectinputstream = new ObjectInputStream(streamIn);
			readCase = (boolean[][]) objectinputstream.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (objectinputstream != null) {
				objectinputstream.close();
			}
		}
		return readCase;
	}

}
