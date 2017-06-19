/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.validation;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.BallastBonusContract;
import com.mmxlabs.models.lng.commercial.BallastBonusContractLine;
import com.mmxlabs.models.lng.commercial.LumpSumBallastBonusContractLine;
import com.mmxlabs.models.lng.commercial.NotionalJourneyBallastBonusContractLine;
import com.mmxlabs.models.lng.commercial.RuleBasedBallastBonusContract;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.port.util.RouteDistanceLineCache;
import com.mmxlabs.models.lng.scenario.model.LNGReferenceModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * Note: This is in the schedule plugin's as we do not have a top level whole scenario validation plug-in
 * 
 * This validation constraint checks for potential problems caused by a wiring optimisation.
 * 
 * @author Simon Goodall
 * 
 */
public class PotentialMissingMissingDistancesConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {

		final EObject target = ctx.getTarget();

		final MMXRootObject rootObject = extraContext.getRootObject();
		if (target instanceof PortModel) {
			final LNGScenarioModel scenarioModel = (LNGScenarioModel) rootObject;
			final LNGReferenceModel referenceModel = scenarioModel.getReferenceModel();
			final SpotMarketsModel spotMarketsModel = referenceModel.getSpotMarketsModel();
			final CargoModel cargoModel = scenarioModel.getCargoModel();
			final PortModel portModel = ScenarioModelUtil.getPortModel(scenarioModel);

			Map<RouteOption, RouteDistanceLineCache> caches = new EnumMap<>(RouteOption.class);
			for (final Route route : portModel.getRoutes()) {
				caches.put(route.getRouteOption(), (RouteDistanceLineCache) Platform.getAdapterManager().loadAdapter(route, RouteDistanceLineCache.class.getName()));
			}
			final RouteDistanceLineCache fCache = caches.get(RouteOption.DIRECT);
			if (fCache == null) {
				// No direct matrix?
				return Activator.PLUGIN_ID;
			}

			// Lambda function to check two sets of distances
			final BiFunction<Set<Port>, Set<Port>, Set<Pair<Port, Port>>> distanceChecker = (froms, tos) -> {
				return froms.stream()//
						.filter(from -> from != null) //
						.flatMap(from -> tos.stream() //
								.filter(to -> to != null) //
								// Skip identity distance
								.filter(to -> !from.equals(to)) //
								.filter(to -> !fCache.hasDistance(from, to)) //
								.map(to -> new Pair<Port, Port>(from, to))) //
						.collect(Collectors.toSet());
			};
			final Set<Pair<Port, Port>> missingDistances = new HashSet<>();

			final Set<Port> startPorts = new HashSet<>();
			final Set<Port> endPorts = new HashSet<>();
			final Set<Port> eventPorts = new HashSet<>();
			final Set<Port> loadPorts = new HashSet<>();
			final Set<Port> dischargePorts = new HashSet<>();
			final Set<Port> ballastBonusReDeliverToPorts = new HashSet<>();
			final Set<Port> ballastBonusReturnToPorts = new HashSet<>();

			cargoModel.getVesselAvailabilities().forEach(va -> startPorts.addAll(SetUtils.getObjects(va.getStartAt())));
			cargoModel.getVesselAvailabilities().forEach(va -> endPorts.addAll(SetUtils.getObjects(va.getEndAt())));

			cargoModel.getLoadSlots().stream() //
					.filter(s -> !s.isDESPurchase() || s.isDivertible()) //
					.forEach(s -> loadPorts.add(s.getPort()));

			cargoModel.getDischargeSlots().stream() //
					.filter(s -> !s.isFOBSale() || s.isDivertible()) //
					.forEach(s -> dischargePorts.add(s.getPort()));

			// TODO: Complex cargoes will also have load -> load and discharge -> discharge distance requirements

			cargoModel.getVesselEvents().forEach(e -> eventPorts.add(e.getPort()));

			cargoModel.getVesselEvents().stream() //
					.filter(e -> e instanceof CharterOutEvent) //
					.forEach(e -> eventPorts.add(((CharterOutEvent) e).getRelocateTo()));

			spotMarketsModel.getCharterOutMarkets().forEach(m -> eventPorts.addAll(SetUtils.getObjects(m.getAvailablePorts())));

			spotMarketsModel.getFobPurchasesSpotMarket().getMarkets().forEach(m -> loadPorts.add(((FOBPurchasesMarket) m).getNotionalPort()));
			spotMarketsModel.getDesSalesSpotMarket().getMarkets().forEach(m -> dischargePorts.add(((DESSalesMarket) m).getNotionalPort()));

			// Filter end ports by likely end ports as with ballast bonus work we may specify world-wide redelivery, but in practise we would not use all possible ports.
			final Set<Port> likelyEndPorts = new HashSet<>();
			likelyEndPorts.addAll(dischargePorts);
			likelyEndPorts.addAll(eventPorts);
			likelyEndPorts.addAll(startPorts);
			endPorts.retainAll(likelyEndPorts);

			// Use lamba function against our different possible from/tos

			cargoModel.getVesselAvailabilities().forEach(va -> {

				final Set<Port> vaLikelyEndPorts = new HashSet<>();
				vaLikelyEndPorts.addAll(loadPorts);
				vaLikelyEndPorts.addAll(dischargePorts);
				vaLikelyEndPorts.addAll(eventPorts);

				final Set<Port> vaEndPorts = SetUtils.getObjects(va.getEndAt());
				// vaEndPorts.retainAll(vaLikelyEndPorts);
				if (vaEndPorts != null) {
					vaLikelyEndPorts.retainAll(vaEndPorts);
				}
				BallastBonusContract contract = va.getBallastBonusContract();
				if (contract instanceof RuleBasedBallastBonusContract) {
					RuleBasedBallastBonusContract ruleBasedBallastBonusContract = (RuleBasedBallastBonusContract) contract;
					for (BallastBonusContractLine line : ruleBasedBallastBonusContract.getRules()) {
						if (line instanceof NotionalJourneyBallastBonusContractLine) {
							NotionalJourneyBallastBonusContractLine notionalJourneyBallastBonusContractLine = (NotionalJourneyBallastBonusContractLine) line;
							// This is a specific check to do this here, rather than add to main list
							Set<Port> redeliveryPorts = SetUtils.getObjects(notionalJourneyBallastBonusContractLine.getRedeliveryPorts());
							redeliveryPorts.retainAll(vaLikelyEndPorts);
							int intialRedeliveryPortsSize = redeliveryPorts.size();
							if (redeliveryPorts.size() == 0 && intialRedeliveryPortsSize > 0) {
								// Blanked out set, so re-initialise as it was probably important information
								redeliveryPorts = SetUtils.getObjects(notionalJourneyBallastBonusContractLine.getRedeliveryPorts());
							}
							Set<Port> returnPorts = SetUtils.getObjects(notionalJourneyBallastBonusContractLine.getReturnPorts());
							int intialReturnPortsSize = returnPorts.size();
							returnPorts.retainAll(vaLikelyEndPorts);
							if (returnPorts.size() == 0 && intialReturnPortsSize > 0) {
								// Blanked out set, so re-initialise as it was probably important information
								returnPorts = SetUtils.getObjects(notionalJourneyBallastBonusContractLine.getReturnPorts());
							}
							missingDistances.addAll(distanceChecker.apply(redeliveryPorts, returnPorts));

						} else if (line instanceof LumpSumBallastBonusContractLine) {
							LumpSumBallastBonusContractLine lumpSumBallastBonusContractLine = (LumpSumBallastBonusContractLine) line;
						}

					}
				}

				// Remove common ports from group as we will pick the common port and not travels
				// Tmp has unique end ports.
				Set<Port> tmp = new HashSet<>(vaLikelyEndPorts);
				tmp.removeAll(vaEndPorts);

				// Again unique ports
				vaEndPorts.remove(vaLikelyEndPorts);

				missingDistances.addAll(distanceChecker.apply(vaEndPorts, tmp));
			});

			missingDistances.addAll(distanceChecker.apply(startPorts, loadPorts));
			missingDistances.addAll(distanceChecker.apply(loadPorts, dischargePorts));
			missingDistances.addAll(distanceChecker.apply(dischargePorts, loadPorts));
			// missingDistances.addAll(distanceChecker.apply(dischargePorts, endPorts));

			missingDistances.addAll(distanceChecker.apply(startPorts, eventPorts));
			missingDistances.addAll(distanceChecker.apply(eventPorts, loadPorts));
			missingDistances.addAll(distanceChecker.apply(dischargePorts, eventPorts));
			// missingDistances.addAll(distanceChecker.apply(eventPorts, endPorts));

			missingDistances.addAll(distanceChecker.apply(eventPorts, eventPorts));

			// missingDistances.addAll(distanceChecker.apply(startPorts, endPorts));

			// Complex cargoes
			for (final Cargo c : cargoModel.getCargoes()) {
				if (c.getSlots().size() > 2) {
					Port prev = null;
					for (final Slot slot : c.getSortedSlots()) {
						final Port thisPort = slot.getPort();
						if (prev != null) {
							missingDistances.addAll(distanceChecker.apply(Collections.singleton(prev), Collections.singleton(thisPort)));
						}
						prev = thisPort;
					}
				}
			}

			for (final Pair<Port, Port> p : missingDistances) {
				final String msg = String.format("Missing distance between %s and %s.", getPortName(p.getFirst()), getPortName(p.getSecond()));
				final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(msg));
				failure.addEObjectAndFeature(p.getFirst(), MMXCorePackage.Literals.NAMED_OBJECT__NAME);
				failure.addEObjectAndFeature(p.getSecond(), MMXCorePackage.Literals.NAMED_OBJECT__NAME);

				statuses.add(failure);
			}

			// Check load -> discharge and back again - typically needed for notional voyages
			for (Port from : loadPorts) {
				for (Port to : dischargePorts) {
					if (from != to) {
						for (RouteOption route : RouteOption.values()) {
							final RouteDistanceLineCache cache = caches.get(route);
							if (cache != null && cache.hasDistance(from, to)) {
								if (!cache.hasDistance(to, from)) {
									final String msg = String.format("Missing distance between %s and %s for %s route.", getPortName(to), getPortName(from), route);
									final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(msg));
									failure.addEObjectAndFeature(from, MMXCorePackage.Literals.NAMED_OBJECT__NAME);
									failure.addEObjectAndFeature(to, MMXCorePackage.Literals.NAMED_OBJECT__NAME);

									statuses.add(failure);
								}
							}
						}
					}
				}
			}
		}

		return Activator.PLUGIN_ID;
	}

	private String getPortName(final @Nullable Port port) {
		if (port == null) {
			return "<Unspecified port>";
		}
		return port.getName();
	}
}
