package com.mmxlabs.models.lng.adp.presentation.views;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.IntStream;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.adp.ContractAllocationRow;
import com.mmxlabs.models.lng.adp.InventoryADPEntityRow;
import com.mmxlabs.models.lng.adp.MarketAllocationRow;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;

public class MUDContainer {
	
	private List<Pair<SalesContract, Double>> salesContractRelativeEntitlements;
	private List<Pair<DESSalesMarket, Double>> marketRelativeEntitlements;
	
	private Map<SalesContract, Long> salesContractRunningAllocation;
	private Map<DESSalesMarket, Long> marketRunningAllocation;
	private Map<SalesContract, List<Vessel>> salesContractVesselLists;
	private Map<DESSalesMarket, List<Vessel>> marketVesselLists;
	
	public MUDContainer(final InventoryADPEntityRow entityRow) {
		double totalWeight = IntStream.concat(
				entityRow.getContractAllocationRows().stream().mapToInt(ContractAllocationRow::getWeight),
				entityRow.getMarketAllocationRows().stream().mapToInt(MarketAllocationRow::getWeight)
			).sum();
		
		this.salesContractRelativeEntitlements = new LinkedList<>();
		this.salesContractRunningAllocation = new HashMap<>();
		this.salesContractVesselLists = new HashMap<>();
		for (final ContractAllocationRow contractAllocationRow : entityRow.getContractAllocationRows()) {
			final SalesContract currentSalesContract = contractAllocationRow.getContract();
			final List<Vessel> currentVessels = contractAllocationRow.getVessels();
			final double relEntitle = contractAllocationRow.getWeight()/totalWeight;
			this.salesContractRelativeEntitlements.add(new Pair<>(currentSalesContract, relEntitle));
			this.salesContractRunningAllocation.put(currentSalesContract, 0L);
			this.salesContractVesselLists.put(currentSalesContract, currentVessels);
		}
		this.marketRelativeEntitlements = new LinkedList<>();
		this.marketRunningAllocation = new HashMap<>();
		this.marketVesselLists = new HashMap<>();
		for (final MarketAllocationRow marketAllocationRow : entityRow.getMarketAllocationRows()) {
			final DESSalesMarket currentMarket = marketAllocationRow.getMarket();
			final List<Vessel> currentVessels = marketAllocationRow.getVessels();
			final double relEntitle = marketAllocationRow.getWeight()/totalWeight;
			this.marketRelativeEntitlements.add(new Pair<>(currentMarket, relEntitle));
			this.marketRunningAllocation.put(currentMarket, 0L);
			this.marketVesselLists.put(currentMarket, currentVessels);
		}
	}
	
	public void updateRunningAllocation(final Long additionalAllocation) {
		for (final Pair<SalesContract, Double> p : this.salesContractRelativeEntitlements) {
			this.salesContractRunningAllocation.compute(p.getFirst(), (k, v) -> v + ((Double) (additionalAllocation*p.getSecond())).longValue());
		}
		for (final Pair<DESSalesMarket, Double> p : this.marketRelativeEntitlements) {
			this.marketRunningAllocation.compute(p.getFirst(), (k, v) -> v + ((Double) (additionalAllocation*p.getSecond())).longValue());
		}
	}
	
	public Entry<SalesContract, Long> calculateMUDSalesContract() {
		if (this.salesContractRelativeEntitlements.isEmpty()) {
			return null;
		}
		return this.salesContractRunningAllocation.entrySet().stream() //
				.max((e1, e2) -> e1.getValue().compareTo(e2.getValue())).get();
	}
	
	public Entry<DESSalesMarket, Long> calculateMUDMarket() {
		if (this.marketRelativeEntitlements.isEmpty()) {
			return null;
		}
		return this.marketRunningAllocation.entrySet().stream() //
				.max((e1, e2) -> e1.getValue().compareTo(e2.getValue())).get();
	}
	
	public void dropAllocation(final SalesContract salesContract, final Long allocationDrop) {
		this.salesContractRunningAllocation.compute(salesContract, (k, v) -> v - allocationDrop);
	}
	
	public void dropAllocation(final DESSalesMarket market, final Long allocationDrop) {
		this.marketRunningAllocation.compute(market, (k, v) -> v - allocationDrop);
	}
	
	public int calculateExpectedAllocationDrop(final Map<Vessel, LocalDate> vesselToMostRecentUseDate) {
		final Entry<SalesContract, Long> mudSalesContract = this.calculateMUDSalesContract();
		final Entry<DESSalesMarket, Long> mudMarket = this.calculateMUDMarket();
		Vessel expectedVessel;
		if (mudSalesContract != null) {
			if (mudMarket != null) {
				if (mudSalesContract.getValue() > mudMarket.getValue()) {
					expectedVessel = this.salesContractVesselLists.get(mudSalesContract.getKey()).stream() //
							.min((v1, v2) -> vesselToMostRecentUseDate.get(v1).compareTo(vesselToMostRecentUseDate.get(v2)))
							.get();
				} else {
					expectedVessel = this.marketVesselLists.get(mudMarket.getKey()).stream() //
							.min((v1, v2) -> vesselToMostRecentUseDate.get(v1).compareTo(vesselToMostRecentUseDate.get(v2)))
							.get();
				}
			} else {
				expectedVessel = this.salesContractVesselLists.get(mudSalesContract.getKey()).stream() //
						.min((v1, v2) -> vesselToMostRecentUseDate.get(v1).compareTo(vesselToMostRecentUseDate.get(v2)))
						.get();
			}
		} else {
			expectedVessel = this.marketVesselLists.get(mudMarket.getKey()).stream() //
					.min((v1, v2) -> vesselToMostRecentUseDate.get(v1).compareTo(vesselToMostRecentUseDate.get(v2)))
					.get();
		}
		return (int) (expectedVessel.getVesselOrDelegateCapacity()*expectedVessel.getVesselOrDelegateFillCapacity() - expectedVessel.getVesselOrDelegateSafetyHeel());
	}
}