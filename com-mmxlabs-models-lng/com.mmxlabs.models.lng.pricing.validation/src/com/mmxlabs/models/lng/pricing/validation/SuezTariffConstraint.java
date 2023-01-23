/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.validation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.SuezCanalRouteRebate;
import com.mmxlabs.models.lng.pricing.SuezCanalTariff;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class SuezTariffConstraint extends AbstractModelMultiConstraint {

	public static final String KEY_REBATE_EMPTY_PORTS = SuezTariffConstraint.class.getCanonicalName() + ".emptyports";
	public static final String KEY_REBATE_PORT_ON_BOTH_SIDES = SuezTariffConstraint.class.getCanonicalName() + ".bothsides";
	public static final String KEY_REBATE_DUPLICATE_ENTRY = SuezTariffConstraint.class.getCanonicalName() + ".duplicateentry";
	public static final String KEY_REBATE_FACTOR = SuezTariffConstraint.class.getCanonicalName() + ".factor";

	@Override
	protected void doValidate(@NonNull final IValidationContext ctx, @NonNull final IExtraValidationContext extraContext, @NonNull final List<IStatus> statuses) {

		final EObject target = ctx.getTarget();
		if (target instanceof SuezCanalTariff suezCanalTariff) {
			final Map<Pair<Port, Port>, SuezCanalRouteRebate> items = new HashMap<>();

			for (final SuezCanalRouteRebate r : suezCanalTariff.getRouteRebates()) {
				if (r.getFrom().isEmpty()) {
					DetailConstraintStatusFactory.makeStatus().withName("Suez Canal Tariff") //
							.withMessage("Rebate entry needs one or port ports specified") //
							.withObjectAndFeature(r, PricingPackage.Literals.SUEZ_CANAL_ROUTE_REBATE__FROM) //
							.withConstraintKey(KEY_REBATE_EMPTY_PORTS) //
							.make(ctx, statuses);
				}

				if (r.getTo().isEmpty()) {
					DetailConstraintStatusFactory.makeStatus().withName("Suez Canal Tariff") //
							.withMessage("Rebate entry needs one or port ports specified") //
							.withObjectAndFeature(r, PricingPackage.Literals.SUEZ_CANAL_ROUTE_REBATE__TO) //
							.withConstraintKey(KEY_REBATE_EMPTY_PORTS) //
							.make(ctx, statuses);

				}

				if (!r.getFrom().isEmpty() && !r.getTo().isEmpty()) {
					final Set<Port> fromPorts = SetUtils.getObjects(r.getFrom());
					final Set<Port> toPorts = SetUtils.getObjects(r.getTo());

					for (final Port from : fromPorts) {
						if (toPorts.contains(from)) {
							DetailConstraintStatusFactory.makeStatus().withName("Suez Canal Tariff") //
									.withMessage(String.format("Port can only be used on one side of the rebate (%s)", from.getName())) //
									.withObjectAndFeature(r, PricingPackage.Literals.SUEZ_CANAL_ROUTE_REBATE__FROM) //
									.withObjectAndFeature(r, PricingPackage.Literals.SUEZ_CANAL_ROUTE_REBATE__TO) //
									.withConstraintKey(KEY_REBATE_PORT_ON_BOTH_SIDES) //
									.make(ctx, statuses);
							
							continue;
						}

						for (final Port to : toPorts) {
							if (from == to) {
								// Error port appears on boths sides of the line
							} else {
								Pair<Port, Port> fromTo = Pair.of(from, to);
								Pair<Port, Port> toFrom = Pair.of(to, from);

								SuezCanalRouteRebate existingFromTo = items.get(fromTo);
								SuezCanalRouteRebate existingToFrom = items.get(toFrom);

								// NEED FEATURE

								// The combo already exists!
								if (existingFromTo != null || existingToFrom != null) {
									DetailConstraintStatusFactory factory = DetailConstraintStatusFactory.makeStatus().withName("Suez Canal Tariff") //
											.withMessage(String.format("Port pair can only be used in one rebate entry (%s, %s)", from.getName(), to.getName())) //
											.withObjectAndFeature(r, PricingPackage.Literals.SUEZ_CANAL_ROUTE_REBATE__FROM) //
											.withObjectAndFeature(r, PricingPackage.Literals.SUEZ_CANAL_ROUTE_REBATE__TO) //
											.withConstraintKey(KEY_REBATE_DUPLICATE_ENTRY) //

									;
									if (existingFromTo == existingToFrom) {
										assert existingFromTo != null;
										// Same entry
										//
										factory.withObjectAndFeature(existingFromTo, PricingPackage.Literals.SUEZ_CANAL_ROUTE_REBATE__FROM) //
												.withObjectAndFeature(existingFromTo, PricingPackage.Literals.SUEZ_CANAL_ROUTE_REBATE__TO) //
												.make(ctx, statuses);
									} else {

										if (existingFromTo != null) {
											factory.withObjectAndFeature(existingFromTo, PricingPackage.Literals.SUEZ_CANAL_ROUTE_REBATE__FROM);
											factory.withObjectAndFeature(existingFromTo, PricingPackage.Literals.SUEZ_CANAL_ROUTE_REBATE__TO);
										}
										if (existingToFrom != null) {
											factory.withObjectAndFeature(existingToFrom, PricingPackage.Literals.SUEZ_CANAL_ROUTE_REBATE__FROM);
											factory.withObjectAndFeature(existingToFrom, PricingPackage.Literals.SUEZ_CANAL_ROUTE_REBATE__TO);

										}
										factory.make(ctx, statuses);
									}
								} else {
									items.put(toFrom, r);
									items.put(fromTo, r);
								}
							}
						}
					}
				}

				if (r.getRebate() <= 0.0 || r.getRebate() >= 1.0) {
					DetailConstraintStatusFactory.makeStatus().withName("Suez Canal Tariff") //
							.withMessage("Rebate factor needs to be between 0% and 100%") //
							.withObjectAndFeature(r, PricingPackage.Literals.SUEZ_CANAL_ROUTE_REBATE__REBATE) //
							.withConstraintKey(KEY_REBATE_FACTOR) //
							.make(ctx, statuses);
				}
			}
		}

	}
}
