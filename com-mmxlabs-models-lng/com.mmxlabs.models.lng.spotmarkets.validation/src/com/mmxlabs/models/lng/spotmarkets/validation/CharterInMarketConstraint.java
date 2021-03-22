/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.validation;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.commercial.BallastBonusTerm;
import com.mmxlabs.models.lng.commercial.GenericCharterContract;
import com.mmxlabs.models.lng.commercial.SimpleBallastBonusContainer;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioElementNameHelper;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.lng.spotmarkets.validation.internal.Activator;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class CharterInMarketConstraint extends AbstractModelConstraint {

	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject target = ctx.getTarget();

		final List<IStatus> failures = new LinkedList<IStatus>();

		if (target instanceof CharterInMarket) {
			final CharterInMarket spotMarket = (CharterInMarket) target;
			if (spotMarket.getGenericCharterContract() != null && spotMarket.getGenericCharterContract() instanceof GenericCharterContract) {
				GenericCharterContract charterContract = (GenericCharterContract) spotMarket.getGenericCharterContract();
				if (spotMarket.isNominal()) {
					// nominals
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx
							.createFailureStatus(String.format("[Charter in market vessel | %s] has nominals enabled and a ballast bonus chartering contract.", spotMarket.getName())));
					dsd.addEObjectAndFeature(spotMarket, SpotMarketsPackage.eINSTANCE.getSpotMarketsModel_CharterInMarkets());
					failures.add(dsd);
				} else {
					if (charterContract.getBallastBonusTerms() instanceof SimpleBallastBonusContainer) {
						ballastBonusCheckPortGroups(ctx, failures, spotMarket, (SimpleBallastBonusContainer) charterContract.getBallastBonusTerms());
					}
				}
			}

			if (spotMarket.getEntity() == null) {
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx
						.createFailureStatus(String.format("[Charter in market vessel | %s] needs an entity set.", spotMarket.getName())));
				dsd.addEObjectAndFeature(spotMarket, SpotMarketsPackage.eINSTANCE.getSpotMarketsModel_CharterInMarkets());
				failures.add(dsd);
			}
			
			int minDurationInHours = spotMarket.getMarketOrContractMinDuration();
			int maxDurationInHours = spotMarket.getMarketOrContractMaxDuration();

			if (minDurationInHours != 0 || maxDurationInHours != 0) {
				if (spotMarket.isNominal()) {
					// nominals
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus(String.format("[Charter in market vessel | %s] has nominals enabled and a min or max duration.", spotMarket.getName())));
					dsd.addEObjectAndFeature(spotMarket, SpotMarketsPackage.eINSTANCE.getSpotMarketsModel_CharterInMarkets());
					failures.add(dsd);
				}
			}
			if (minDurationInHours != 0 && maxDurationInHours != 0) {
				if (minDurationInHours > maxDurationInHours) {
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus(String.format("[Charter in market vessel | %s] has min duration superior to the max duration.", spotMarket.getName())));
					dsd.addEObjectAndFeature(spotMarket, SpotMarketsPackage.eINSTANCE.getSpotMarketsModel_CharterInMarkets());
					dsd.addEObjectAndFeature(spotMarket, SpotMarketsPackage.eINSTANCE.getCharterInMarket_MinDuration());
					dsd.addEObjectAndFeature(spotMarket, SpotMarketsPackage.eINSTANCE.getCharterInMarket_MaxDuration());
					failures.add(dsd);
				}
			}
			if (spotMarket.isMtm()) {
				if (!spotMarket.isNominal()) {
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus(String.format("[Charter in market vessel | %s] is used for MTM but not Nominal.", spotMarket.getName())));
					dsd.addEObjectAndFeature(spotMarket, SpotMarketsPackage.eINSTANCE.getSpotMarketsModel_CharterInMarkets());
					dsd.addEObjectAndFeature(spotMarket, SpotMarketsPackage.eINSTANCE.getCharterInMarket_Mtm());
					dsd.addEObjectAndFeature(spotMarket, SpotMarketsPackage.eINSTANCE.getCharterInMarket_Nominal());
					failures.add(dsd);
				}
			}
		}
		final MultiStatus multi = new MultiStatus(Activator.PLUGIN_ID, IStatus.ERROR, null, null);
		for (final IStatus s : failures) {
			multi.add(s);
		}
		return multi;
	}
	
	private void ballastBonusCheckPortGroups(final IValidationContext ctx, final List<IStatus> failures, final CharterInMarket spotMarket, final SimpleBallastBonusContainer ballastBonusContainer) {
		final Set<APortSet<Port>> coveredPorts = new HashSet<APortSet<Port>>();
		final List<APortSet<Port>> endAtPorts = new LinkedList<>();
		boolean anywhere = false;
		if (spotMarket.getEndAt().isEmpty()) {
			// could end anywhere - add all ports
			anywhere = true;
		} else {
			endAtPorts.addAll(SetUtils.getObjects(spotMarket.getEndAt()));
		}
		if (!ballastBonusContainer.getTerms().isEmpty()) {
			for (final BallastBonusTerm ballastBonusContractLine : ballastBonusContainer.getTerms()) {
				final EList<APortSet<Port>> redeliveryPorts = ballastBonusContractLine.getRedeliveryPorts();
				if (redeliveryPorts.isEmpty()) {
					return;
				} else {
					coveredPorts.addAll(SetUtils.getObjects(redeliveryPorts));
				}
			}
			for (final APortSet<Port> endAtPort : endAtPorts) {
				if (!coveredPorts.contains(endAtPort)) {
					final DetailConstraintStatusDecorator dsd;
					if (anywhere) {
						dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(
								(String.format("%s is not covered by the ballast bonus rules (note the vessel can end anywhere)", ScenarioElementNameHelper.getName(endAtPort)))));
					} else {
						dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(
								(String.format("%s is not covered by the ballast bonus rules", ScenarioElementNameHelper.getName(endAtPort)))));
					}
					dsd.addEObjectAndFeature(spotMarket, SpotMarketsPackage.Literals.CHARTER_IN_MARKET__GENERIC_CHARTER_CONTRACT);
					failures.add(dsd);
					return;
				}
			}
		}
	}
}
