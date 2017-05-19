/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.panamaslots;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.jdt.annotation.NonNull;

import com.google.common.collect.ImmutableSet;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.LNGPriceCalculatorParameters;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.port.EntryPoint;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.contracts.IContractTransformer;
import com.mmxlabs.models.lng.transformer.util.DateAndCurveHelper;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.providers.IPortProvider;

/**
 */
public class PanamaSlotsTransformer implements IContractTransformer {

	@Inject
	private IPanamaSlotsProviderEditor panamaSlotsProviderEditor;
	
	@Inject
	private IPortProvider portProvider;

	@Inject
	private DateAndCurveHelper dateAndCurveHelper;
	
	@Inject
	private ModelEntityMap modelEntityMap;

	private LNGScenarioModel rootObject;
	// TODO: these maps probably don't need to be separate from one another - it would simplify matters to use just one Map<EObject, Collection<ISequenceElement>>
	private final Map<Contract, Collection<ISequenceElement>> contractMap = new HashMap<>();
	private final Map<Port, Collection<ISequenceElement>> portMap = new HashMap<>();
	private final Map<Slot, Collection<ISequenceElement>> slotMap = new HashMap<>();
	
	private final Set<Pair<EntryPoint, LocalDateTime>> providedPanamaSlots = new HashSet<Pair<EntryPoint, LocalDateTime>>();

	private final Set<ISequenceElement> allElements = new HashSet<ISequenceElement>();


	/**
	 */
	@Override
	public void startTransforming(final LNGScenarioModel rootObject, final ModelEntityMap modelEntityMap, final ISchedulerBuilder builder) {
		this.rootObject = rootObject;
		List<Pair<EntryPoint, LocalDateTime>> panamaDates = new ArrayList<>();
		
		Route panama = rootObject.getReferenceModel().getPortModel().getRoutes().stream().filter(r -> r.getRouteOption().equals(RouteOption.PANAMA)).findFirst().get();
		EntryPoint colon = panama.getEntryPoints().get(0);
		EntryPoint balboa = panama.getEntryPoints().get(1);
		
		panamaDates.add(new Pair<>(colon, LocalDateTime.of(LocalDate.of(2017, Month.JUNE, 10), LocalTime.of(14, 35))));
		panamaDates.add(new Pair<>(colon, LocalDateTime.of(LocalDate.of(2017, Month.JUNE, 20), LocalTime.of(14, 35))));
		panamaDates.add(new Pair<>(balboa, LocalDateTime.of(LocalDate.of(2017, Month.JUNE, 20), LocalTime.of(14, 35))));
		panamaDates.add(new Pair<>(balboa, LocalDateTime.of(LocalDate.of(2017, Month.JULY, 20), LocalTime.of(14, 35))));
		providedPanamaSlots.addAll(panamaDates);
	}

	@Override
	public void finishTransforming() {
		Map<IPort, SortedSet<Integer>> panamaSlots = new HashMap<>();
		providedPanamaSlots.forEach(slot -> {
			IPort optPort = modelEntityMap.getOptimiserObject(slot.getFirst().getPort(), IPort.class);
			panamaSlots.computeIfAbsent(optPort, key -> new TreeSet<Integer>())
			.add( dateAndCurveHelper.convertTime(slot.getSecond().toLocalDate()) + slot.getSecond().getHour());
		});

		panamaSlotsProviderEditor.setSlots(panamaSlots);
		rootObject = null;
		contractMap.clear();
		portMap.clear();
		slotMap.clear();
		allElements.clear();
	}

	@Override
	public void slotTransformed(@NonNull final Slot modelSlot, @NonNull final IPortSlot optimiserSlot) {
	
	}

	@Override
	public Collection<EClass> getContractEClasses() {
		return Collections.emptySet();
	}

	@Override
	public ISalesPriceCalculator transformSalesPriceParameters(final SalesContract salesContract, final LNGPriceCalculatorParameters priceParameters) {
		return null;
	}

	@Override
	public ILoadPriceCalculator transformPurchasePriceParameters(final PurchaseContract purchaseContract, final LNGPriceCalculatorParameters priceParameters) {
		return null;
	}
}
