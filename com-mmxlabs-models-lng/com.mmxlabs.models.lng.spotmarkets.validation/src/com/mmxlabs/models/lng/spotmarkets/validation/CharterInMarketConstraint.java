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
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.commercial.BallastBonusTerm;
import com.mmxlabs.models.lng.commercial.GenericCharterContract;
import com.mmxlabs.models.lng.commercial.RepositioningFeeTerm;
import com.mmxlabs.models.lng.commercial.SimpleBallastBonusContainer;
import com.mmxlabs.models.lng.commercial.SimpleRepositioningFeeContainer;
import com.mmxlabs.models.lng.commercial.StartHeelOptions;
import com.mmxlabs.models.lng.port.CapabilityGroup;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioElementNameHelper;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class CharterInMarketConstraint extends AbstractModelMultiConstraint {
	@Override
	protected void doValidate(@NonNull final IValidationContext ctx, @NonNull final IExtraValidationContext extraContext, @NonNull final List<IStatus> failures) {
		final EObject target = ctx.getTarget();

		if (target instanceof final CharterInMarket spotMarket) {
			final GenericCharterContract charterContract = spotMarket.getGenericCharterContract();
			if (charterContract != null) {
				if (charterContract.getBallastBonusTerms() instanceof final SimpleBallastBonusContainer container) {
					ballastBonusCheckPortGroups(ctx, failures, spotMarket, container);
				}
				if (charterContract.getRepositioningFeeTerms() instanceof final SimpleRepositioningFeeContainer container) {
					repositioningCheckPortGroups(ctx, (LNGScenarioModel) extraContext.getRootObject(), failures, spotMarket, container);
				}
			}

			if (spotMarket.isSetStartHeelCV() && spotMarket.getStartHeelCV() == 0.0) {
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
						(IConstraintStatus) ctx.createFailureStatus(String.format("[Charter in market | %s] has a CV of zero.", spotMarket.getName())));
				dsd.addEObjectAndFeature(spotMarket, SpotMarketsPackage.eINSTANCE.getCharterInMarket_StartHeelCV());
				failures.add(dsd);
			}
			if (spotMarket.isSetStartAt()) {
				boolean cvFound = spotMarket.isSetStartHeelCV();
				if (!cvFound && charterContract != null) {
					final StartHeelOptions startHeel = charterContract.getStartHeel();
					if (startHeel != null) {
						final double cvValue = startHeel.getCvValue();
						cvFound = cvValue != 0.0;
					}
				}

				if (!cvFound) {
					if (charterContract != null) {
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx
								.createFailureStatus(String.format("[Charter in market | %s] has a start port but no start heel CV set and contract CV is zero.", spotMarket.getName())));
						dsd.addEObjectAndFeature(spotMarket, SpotMarketsPackage.eINSTANCE.getCharterInMarket_StartHeelCV());
						failures.add(dsd);
					} else {
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus(String.format("[Charter in market | %s] has a start port but no start heel CV set.", spotMarket.getName())));
						dsd.addEObjectAndFeature(spotMarket, SpotMarketsPackage.eINSTANCE.getCharterInMarket_StartHeelCV());
						failures.add(dsd);
					}
				}

			}

			if (spotMarket.getEntity() == null) {
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
						(IConstraintStatus) ctx.createFailureStatus(String.format("[Charter in market vessel | %s] needs an entity set.", spotMarket.getName())));
				dsd.addEObjectAndFeature(spotMarket, SpotMarketsPackage.eINSTANCE.getSpotMarketsModel_CharterInMarkets());
				failures.add(dsd);
			}

			final int minDurationInHours = spotMarket.getMarketOrContractMinDuration();
			final int maxDurationInHours = spotMarket.getMarketOrContractMaxDuration();
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
	}

	private void ballastBonusCheckPortGroups(final IValidationContext ctx, final List<IStatus> failures, final CharterInMarket spotMarket, final SimpleBallastBonusContainer ballastBonusContainer) {
		final Set<APortSet<Port>> coveredPorts = new HashSet<>();
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
						dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx
								.createFailureStatus((String.format("%s is not covered by the ballast bonus rules (note the vessel can end anywhere)", ScenarioElementNameHelper.getName(endAtPort)))));
					} else {
						dsd = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus(String.format("%s is not covered by the ballast bonus rules", ScenarioElementNameHelper.getName(endAtPort))));
					}
					dsd.addEObjectAndFeature(spotMarket, SpotMarketsPackage.Literals.CHARTER_IN_MARKET__GENERIC_CHARTER_CONTRACT);
					failures.add(dsd);
					return;
				}
			}
		}
	}

	private void repositioningCheckPortGroups(final IValidationContext ctx, LNGScenarioModel scenarioModel, final List<IStatus> failures, final CharterInMarket spotMarket,
			final SimpleRepositioningFeeContainer container) {
		final Set<Port> coveredPorts = new HashSet<>();
		final List<Port> startAtPorts = new LinkedList<>();
		boolean anywhere = false;
		if (spotMarket.isNominal()) {

			for (CapabilityGroup g : scenarioModel.getReferenceModel().getPortModel().getSpecialPortGroups()) {
				if (g.getCapability() == PortCapability.LOAD) {
					startAtPorts.addAll(SetUtils.getObjects(g));
				}
			}

		}

		if (spotMarket.getStartAt() == null) {
			// could end anywhere - add all ports
			anywhere = true;
		} else {
			if (!startAtPorts.contains(spotMarket.getStartAt())) {
				startAtPorts.add(spotMarket.getStartAt());
			}
		}
		if (!container.getTerms().isEmpty()) {
			for (final RepositioningFeeTerm term : container.getTerms()) {
				final EList<APortSet<Port>> termStartPorts = term.getStartPorts();
				if (termStartPorts.isEmpty()) {
					// This covers any port
					return;
				} else {
					coveredPorts.addAll(SetUtils.getObjects(termStartPorts));
				}
			}
			for (final Port port : startAtPorts) {
				if (!coveredPorts.contains(port)) {
					final DetailConstraintStatusDecorator dsd;
					if (anywhere) {
						dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx
								.createFailureStatus((String.format("%s is not covered by the repositioning rules (note the vessel can start anywhere - nominals can start at any load port)", ScenarioElementNameHelper.getName(port)))));
					} else {
						dsd = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus(String.format("%s is not covered by the repositioning rules", ScenarioElementNameHelper.getName(port))));
					}
					dsd.addEObjectAndFeature(spotMarket, SpotMarketsPackage.Literals.CHARTER_IN_MARKET__GENERIC_CHARTER_CONTRACT);
					failures.add(dsd);
					return;
				}
			}
		}
	}
}
