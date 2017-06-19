/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.panamaslots;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.cargo.CanalBookingSlot;
import com.mmxlabs.models.lng.cargo.CanalBookings;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.LNGPriceCalculatorParameters;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.contracts.IContractTransformer;
import com.mmxlabs.models.lng.transformer.util.DateAndCurveHelper;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IRouteOptionBooking;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.IPanamaBookingsProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPromptPeriodProvider;

/**
 * @author robert
 */
public class PanamaSlotsTransformer implements IContractTransformer {

	@Inject
	private IPanamaBookingsProviderEditor panamaBookingsProviderEditor;

	@Inject
	private IPromptPeriodProvider promptPeriodProvider;

	@Inject
	private DateAndCurveHelper dateAndCurveHelper;

	@Inject
	private ModelEntityMap modelEntityMap;

	private final List<CanalBookingSlot> providedPanamaBookings = new ArrayList<>();
	private int relaxedBoundaryOffsetDays;
	private int relaxedBookingsCount;
	private int strictBoundaryOffsetDays;
	private int arrivalMargin;

	@Override
	public void startTransforming(final LNGScenarioModel rootObject, final ModelEntityMap modelEntityMap, final ISchedulerBuilder builder) {
		final PortModel portModel = ScenarioModelUtil.getPortModel(rootObject);
		final Optional<Route> potentialPanama = portModel.getRoutes().stream().filter(r -> r.getRouteOption() == RouteOption.PANAMA).findFirst();
		if (!potentialPanama.isPresent()) {
			return;
		}

		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(rootObject);
		final CanalBookings canalBookings = cargoModel.getCanalBookings();
		if (canalBookings == null) {
			return;
		}
		this.providedPanamaBookings.addAll(canalBookings.getCanalBookingSlots());

		strictBoundaryOffsetDays = canalBookings.getStrictBoundaryOffsetDays();
		relaxedBoundaryOffsetDays = canalBookings.getRelaxedBoundaryOffsetDays();
		relaxedBookingsCount = canalBookings.getFlexibleBookingAmount();
		arrivalMargin = canalBookings.getArrivalMarginHours();
	}

	@Override
	public void finishTransforming() {
		final Map<IPort, SortedSet<IRouteOptionBooking>> panamaSlots = new HashMap<>();
		providedPanamaBookings.forEach(eBooking -> {
			final IPort optPort = modelEntityMap.getOptimiserObject(eBooking.getEntryPoint().getPort(), IPort.class);
			if (optPort == null) {
				throw new IllegalStateException("No optimiser port found for: " + eBooking.getEntryPoint().getName());
			}
			
			final int date = dateAndCurveHelper.convertTime(eBooking.getBookingDateAsDateTime());
			final IRouteOptionBooking oBooking;
			if (eBooking.getSlot() != null) {
				oBooking = IRouteOptionBooking.of(date, optPort, ERouteOption.PANAMA, modelEntityMap.getOptimiserObjectNullChecked(eBooking.getSlot(), IPortSlot.class));
			} else {
				oBooking = IRouteOptionBooking.of(date, optPort, ERouteOption.PANAMA);
			}
			panamaSlots.computeIfAbsent(optPort, key -> new TreeSet<>()).add(oBooking);

			modelEntityMap.addModelObject(eBooking, oBooking);
		});

		panamaBookingsProviderEditor.setBookings(panamaSlots);

		panamaBookingsProviderEditor.setStrictBoundary(promptPeriodProvider.getStartOfPromptPeriod() + strictBoundaryOffsetDays * 24);
		panamaBookingsProviderEditor.setRelaxedBoundary(promptPeriodProvider.getStartOfPromptPeriod() + relaxedBoundaryOffsetDays * 24);
		panamaBookingsProviderEditor.setRelaxedBookingCount(relaxedBookingsCount);
		panamaBookingsProviderEditor.setArrivalMargin(arrivalMargin);
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
