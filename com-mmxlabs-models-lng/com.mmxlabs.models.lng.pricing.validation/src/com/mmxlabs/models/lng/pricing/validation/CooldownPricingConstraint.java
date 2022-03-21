/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.validation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.NonNull;

import com.google.common.base.Joiner;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.pricing.CooldownPrice;
import com.mmxlabs.models.lng.pricing.CostModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils.ValidationResult;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioElementNameHelper;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * A constraint for ensuring that all ports have a cooldown constraint in the
 * given pricing model.
 * 
 * @author hinton
 * 
 */
public class CooldownPricingConstraint extends AbstractModelMultiConstraint {

	@Override
	protected void doValidate(IValidationContext ctx, final IExtraValidationContext extraContext, List<IStatus> failures) {
		final EObject target = ctx.getTarget();

		if (target instanceof CooldownPrice c) {

			final String portSetName = ScenarioElementNameHelper.getName(c.getPorts(), "Cooldown Cost <No ports>");
			final DetailConstraintStatusFactory factoryBase = DetailConstraintStatusFactory.makeStatus().withName(portSetName);

			if (c.getExpression() == null) {
				final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Cooldown definition is missing a price expression."));
				dcsd.addEObjectAndFeature(c, PricingPackage.Literals.PORTS_EXPRESSION_MAP__EXPRESSION);
				failures.add(dcsd);
			} else {
				ValidationResult result = PriceExpressionUtils.validatePriceExpression(ctx, c, PricingPackage.Literals.PORTS_EXPRESSION_MAP__EXPRESSION, c.getExpression());
				if (!result.isOk()) {
					String message = String.format("[Cooldown Price {%s}] %s", getPorts(c), result.getErrorDetails());
					final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
					dcsd.addEObjectAndFeature(c, PricingPackage.Literals.PORTS_EXPRESSION_MAP__EXPRESSION);
					failures.add(dcsd);
				} else {
					if (!c.isLumpsum()) {
						PriceExpressionUtils.constrainPriceExpression(ctx, c, PricingPackage.Literals.PORTS_EXPRESSION_MAP__EXPRESSION, c.getExpression(), 0.0, 90.0, null, failures);
					}
				}
				if (c.getPorts().isEmpty()) {
					final DetailConstraintStatusFactory factory = factoryBase.copyName()//
							.withMessage(String.format("No ports specified"));
					factory.withObjectAndFeature(c, PricingPackage.Literals.PORTS_EXPRESSION_MAP__PORTS);
					factory.make(ctx, failures);
				} else {
					final List<EObject> objects = extraContext.getSiblings(target);
					final Set<APortSet<Port>> explicit = new HashSet<>();
					final Set<Port> implicit = new HashSet<>();
					for (final EObject obj : objects) {
						if (obj instanceof CooldownPrice otherCooldownPrice) {

							if (otherCooldownPrice == c) {
								continue;
							}

							for (final APortSet<Port> portSet : otherCooldownPrice.getPorts()) {
								explicit.add(portSet);
								if (!(portSet instanceof Port)) {
									implicit.addAll(SetUtils.getObjects(portSet));
								}
							}
						}
					}
					boolean warnedExplcit = false;
					final boolean warnedImplcit = false;
					for (final APortSet<Port> portSet : c.getPorts()) {
						if (explicit.contains(portSet) && !warnedExplcit) {
							final DetailConstraintStatusFactory factory = factoryBase.copyName()//
									.withMessage(String.format("Port %s already contained explicitly in another cooldown price entry", portSet.getName()));
							factory.withObjectAndFeature(c, PricingPackage.Literals.PORTS_EXPRESSION_MAP__PORTS);
							factory.make(ctx, failures);
							warnedExplcit = true;
						}
						if (portSet instanceof Port) {
							// No extra check here
						} else {
							for (final Port port : SetUtils.getObjects(portSet)) {
								if (implicit.contains(port) && !warnedImplcit) {
									final DetailConstraintStatusFactory factory = factoryBase.copyName()//
											.withMessage(String.format("Port %s in group %s already contained in another cooldown price entry", port.getName(), portSet.getName()));
									factory.withObjectAndFeature(c, PricingPackage.Literals.PORTS_EXPRESSION_MAP__PORTS);
									factory.make(ctx, failures);
									// warnedImplcit = true;
								}
							}
						}
					}
				}
			}
		} else if (target instanceof CostModel costModel) {
			{
				final PortModel portModel = ScenarioModelUtil.getPortModel(extraContext.getScenarioDataProvider());
				// count the number of cooldown prices attached to each port
				final Map<Port, Set<CooldownPrice>> pricingPerPort = new HashMap<>();
				for (final Port port : portModel.getPorts()) {
					pricingPerPort.put(port, new HashSet<>());
				}

				for (final CooldownPrice c : costModel.getCooldownCosts()) {
					for (final Port port : SetUtils.getObjects(c.getPorts())) {
						pricingPerPort.get(port).add(c);
					}

				}

				for (final Entry<Port, Set<CooldownPrice>> entry : pricingPerPort.entrySet()) {
					final Port port = entry.getKey();
					final int count = entry.getValue().size();

					if (count == 0 && port.getCapabilities().contains(PortCapability.LOAD)) {
						final String message = String.format("Load port %s has no cooldown price.", port.getName());
						final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
						dcsd.addEObjectAndFeature(port, null);
						if (count > 0) {
							for (final CooldownPrice c : entry.getValue()) {
								dcsd.addEObjectAndFeature(c, PricingPackage.Literals.PORTS_EXPRESSION_MAP__PORTS);
							}
						}
						failures.add(dcsd);
					}
				}
			}
		}
	}

	@NonNull
	private String getPorts(@NonNull CooldownPrice c) {

		List<String> portNames = new LinkedList<>();
		for (APortSet<Port> p : c.getPorts()) {
			portNames.add(p.getName());
		}
		return Joiner.on(", ").join(portNames);
	}
}
