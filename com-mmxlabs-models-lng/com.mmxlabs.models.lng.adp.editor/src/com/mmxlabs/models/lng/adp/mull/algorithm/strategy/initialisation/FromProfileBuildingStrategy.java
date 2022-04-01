package com.mmxlabs.models.lng.adp.mull.algorithm.strategy.initialisation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.adp.mull.algorithm.AlgorithmState;
import com.mmxlabs.models.lng.adp.mull.algorithm.GlobalStatesContainer;
import com.mmxlabs.models.lng.adp.mull.algorithm.InventoryGlobalState;
import com.mmxlabs.models.lng.adp.mull.container.DefaultMudContainer;
import com.mmxlabs.models.lng.adp.mull.container.DesMarketTracker;
import com.mmxlabs.models.lng.adp.mull.container.IAllocationTracker;
import com.mmxlabs.models.lng.adp.mull.container.IMudContainer;
import com.mmxlabs.models.lng.adp.mull.container.IMullContainer;
import com.mmxlabs.models.lng.adp.mull.container.MullContainer;
import com.mmxlabs.models.lng.adp.mull.container.SalesContractTracker;
import com.mmxlabs.models.lng.adp.mull.container.manipulation.IAllocationTrackerManipulationStrategy;
import com.mmxlabs.models.lng.adp.mull.container.manipulation.IMudContainerManipulationStrategy;
import com.mmxlabs.models.lng.adp.mull.container.manipulation.IMullComparatorWrapper;
import com.mmxlabs.models.lng.adp.mull.profile.DesSalesMarketAllocationRow;
import com.mmxlabs.models.lng.adp.mull.profile.IAllocationRow;
import com.mmxlabs.models.lng.adp.mull.profile.IEntityRow;
import com.mmxlabs.models.lng.adp.mull.profile.IMullSubprofile;
import com.mmxlabs.models.lng.adp.mull.profile.SalesContractAllocationRow;
import com.mmxlabs.models.lng.cargo.Inventory;
import com.mmxlabs.models.lng.fleet.Vessel;

@NonNullByDefault
public class FromProfileBuildingStrategy implements IMullContainerBuildingStrategy {

	private IMullSubprofile subprofile;
	private final IMullStrategyContainer mullStrategies;

	
	public FromProfileBuildingStrategy(final IMullSubprofile subprofile, final IMullStrategyContainer mullStrategies) {
		this.subprofile = subprofile;
		this.mullStrategies = mullStrategies;
	}

	@Override
	public IMullContainer build(final GlobalStatesContainer globalStatesContainer, final AlgorithmState algorithmState) {
		final Inventory inventory = subprofile.getInventory();

		final double totalEntityRowsWeight = subprofile.getEntityRows().stream().mapToDouble(IEntityRow::getRelativeEntitlement).sum();
		final List<IMudContainer> mudContainers = new ArrayList<>(subprofile.getEntityRows().size());
		for (final IEntityRow entityRow : subprofile.getEntityRows()) {
			final List<IAllocationTracker> allocationTrackers = buildAllocationTrackers(entityRow, mullStrategies.getAllocationTrackerManipulationStrategy());
			final double relativeEntitlement = entityRow.getRelativeEntitlement() / totalEntityRowsWeight;
			final IMudContainer mudContainer = new DefaultMudContainer(entityRow.getEntity(), relativeEntitlement, entityRow.getInitialAllocation(), allocationTrackers,
					mullStrategies.getMudContainerManipulationStrategy());
			mudContainers.add(mudContainer);
		}
		final InventoryGlobalState inventoryGlobalState = globalStatesContainer.getInventoryGlobalStates().get(inventory);
		return new MullContainer(inventory, mudContainers, mullStrategies.getComparatorWrapper(inventoryGlobalState, algorithmState, globalStatesContainer));
	}

	private static List<IAllocationTracker> buildAllocationTrackers(final IEntityRow entityRow, final IAllocationTrackerManipulationStrategy allocationTrackerManipulationStrategy) {
		final List<IAllocationTracker> allocationTrackers = new ArrayList<>(entityRow.getSalesContractRows().size() + entityRow.getDesSalesMarketRows().size());
		final double totalAllocationRowWeight = calculateAllocationRowsTotalWeight(entityRow);

		for (final SalesContractAllocationRow allocationRow : entityRow.getSalesContractRows()) {
			final double relativeEntitlement = calculateAacqWeightedAverageVesselSize(allocationRow) / totalAllocationRowWeight;
			final SalesContractTracker salesContractTracker = new SalesContractTracker(allocationRow.getSalesContract(), allocationRow.getWeight(), allocationRow.getVessels(), relativeEntitlement,
					allocationTrackerManipulationStrategy);
			allocationTrackers.add(salesContractTracker);
		}
		for (final DesSalesMarketAllocationRow allocationRow : entityRow.getDesSalesMarketRows()) {
			final double relativeEntitlement = calculateAacqWeightedAverageVesselSize(allocationRow) / totalAllocationRowWeight;
			final DesMarketTracker desMarketTracker = new DesMarketTracker(allocationRow.getDesSalesMarket(), allocationRow.getWeight(), allocationRow.getVessels(), relativeEntitlement,
					allocationTrackerManipulationStrategy);
			allocationTrackers.add(desMarketTracker);
		}
		return allocationTrackers;
	}

	private static double calculateAllocationRowsTotalWeight(final IEntityRow entityRow) {
		return Stream.<IAllocationRow> concat(entityRow.getSalesContractRows().stream(), entityRow.getDesSalesMarketRows().stream()) //
				.mapToDouble(FromProfileBuildingStrategy::calculateAacqWeightedAverageVesselSize) //
				.sum();
	}

	private static double calculateAacqWeightedAverageVesselSize(final IAllocationRow allocationRow) {
		return allocationRow.getWeight() * calculateAverageVesselSize(allocationRow.getVessels());
	}

	private static double calculateAverageVesselSize(final List<Vessel> vessels) {
		return vessels.stream().mapToInt(Vessel::getVesselOrDelegateCapacity).average().getAsDouble();
	}
}
