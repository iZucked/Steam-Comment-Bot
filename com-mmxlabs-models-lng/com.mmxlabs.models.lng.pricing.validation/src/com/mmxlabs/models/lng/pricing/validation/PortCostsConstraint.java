/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.validation;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.google.common.base.Joiner;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.pricing.CostModel;
import com.mmxlabs.models.lng.pricing.PortCost;
import com.mmxlabs.models.lng.pricing.PortCostEntry;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.validation.internal.Activator;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioElementNameHelper;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class PortCostsConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject target = ctx.getTarget();

		if (target instanceof CostModel) {
			CostModel costModel = (CostModel) target;
			Map<Pair<Port, PortCapability>, Boolean> hasCost = new HashMap<>();

			for (PortCost portCost : costModel.getPortCosts()) {
				for (final Port port : SetUtils.getObjects(portCost.getPorts())) {
					for (PortCostEntry e : portCost.getEntries()) {
						if (e.getCost() != 0) {
							hasCost.put(new Pair<>(port, e.getActivity()), Boolean.TRUE);
						}
					}

				}
			}

			MMXRootObject rootObject = extraContext.getRootObject();
			if (rootObject instanceof LNGScenarioModel) {
				LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
				PortModel portModel = ScenarioModelUtil.getPortModel(lngScenarioModel);
				for (Port p : portModel.getPorts()) {
					for (PortCapability capability : p.getCapabilities()) {
						if (!hasCost.containsKey(new Pair<>(p, capability))) {
							final DetailConstraintStatusFactory factoryBase = DetailConstraintStatusFactory.makeStatus().withTypedName("Port", p.getName());

							final DetailConstraintStatusFactory factory = factoryBase.copyName()//
									.withMessage(String.format("Has no %s cost", capability.getName()));
							factory.withObjectAndFeature(p, MMXCorePackage.Literals.NAMED_OBJECT__NAME);
							factory.make(ctx, failures);
						}
					}
				}
			}
		}

		if (target instanceof PortCost) {
			final PortCost portCost = (PortCost) target;

			final String portSetName = ScenarioElementNameHelper.getName(portCost.getPorts(), "Port Cost <No ports>");
			final DetailConstraintStatusFactory factoryBase = DetailConstraintStatusFactory.makeStatus().withName(portSetName);

			if (portCost.getPorts().isEmpty()) {
				final DetailConstraintStatusFactory factory = factoryBase.copyName()//
						.withMessage(String.format("No ports specified"));
				factory.withObjectAndFeature(portCost, PricingPackage.Literals.PORTS_EXPRESSION_MAP__PORTS);
				factory.make(ctx, failures);
			}

			// TODO: Need to copy canal cost/cooldown cost constraint logic for implicit/explicit checks. But split into load/discharge costs.
			// TODO: Check the transformer logic here!
			// TODO: Make mini test cases! -> Transform logic and grab the DCP and test

			final Map<PortCapability, Set<APortSet<Port>>> explicitMap = new EnumMap<>(PortCapability.class);
			final Map<PortCapability, Set<Port>> implicitMap = new EnumMap<>(PortCapability.class);

			final List<EObject> objects = extraContext.getSiblings(target);
			for (final EObject obj : objects) {
				if (obj instanceof PortCost) {
					final PortCost otherPortCost = (PortCost) obj;

					if (otherPortCost == portCost) {
						continue;
					}

					for (final APortSet<Port> portSet : otherPortCost.getPorts()) {
						otherPortCost.getEntries().forEach(e -> {
							if (e.getCost() != 0) {
								explicitMap.computeIfAbsent(e.getActivity(), x -> new HashSet<>()).add(portSet);
							}
						});
						if (!(portSet instanceof Port)) {
							otherPortCost.getEntries().forEach(e -> {
								if (e.getCost() != 0) {
									implicitMap.computeIfAbsent(e.getActivity(), x -> new HashSet<>()).addAll(SetUtils.getObjects(portSet));
								}
							});
						}
					}
				}
			}

			boolean containsLoad = false;
			boolean containsDischarge = false;
			final List<String> portNames = new LinkedList<>();
			for (final Port port : SetUtils.getObjects(portCost.getPorts())) {
				if (port.getCapabilities().contains(PortCapability.DISCHARGE)) {
					containsDischarge = true;
				}
				if (port.getCapabilities().contains(PortCapability.LOAD)) {
					containsLoad = true;
				}
				portNames.add(port.getName());
			}

			boolean warnedExplcit = false;
			final boolean warnedImplcit = false;
			for (final APortSet<Port> portSet : portCost.getPorts()) {
				for (final PortCostEntry e : portCost.getEntries()) {
					if (e.getCost() != 0) {
						final Set<APortSet<Port>> explicit = explicitMap.get(e.getActivity());
						if (explicit != null && explicit.contains(portSet) && !warnedExplcit) {
							final DetailConstraintStatusFactory factory = factoryBase.copyName()//
									.withSeverity(IStatus.ERROR) //
									.withMessage(String.format("Port %s already contained explicitly in another port cost entry for %s capability", portSet.getName(), e.getActivity().getName()));
							factory.withObjectAndFeature(portCost, PricingPackage.Literals.PORTS_EXPRESSION_MAP__PORTS);
							factory.make(ctx, failures);
							warnedExplcit = true;
						}
					}
				}
				if (portSet instanceof Port) {
					// No extra check here
				} else {
					for (final Port port : SetUtils.getObjects(portSet)) {
						for (final PortCostEntry e : portCost.getEntries()) {
							if (e.getCost() == 0) {
								continue;
							}
							final Set<Port> implicit = implicitMap.get(e.getActivity());
							if (implicit != null && implicit.contains(port) && !warnedImplcit) {
								final DetailConstraintStatusFactory factory = factoryBase.copyName()//
										.withSeverity(IStatus.ERROR) //
										.withMessage(String.format("Port %s in group %s already contained in another port cost entry for %s capability", port.getName(), portSet.getName(),
												e.getActivity().getName()));
								factory.withObjectAndFeature(portCost, PricingPackage.Literals.PORTS_EXPRESSION_MAP__PORTS);
								factory.make(ctx, failures);
								// warnedImplcit = true;
							}
						}
					}
				}
			}

			final Joiner joiner = Joiner.on(", ").skipNulls();
			final String portsInQuestion = joiner.join(portNames);

			for (final PortCostEntry entry : portCost.getEntries()) {
				final PortCapability capability = entry.getActivity();
				if (capability.equals(PortCapability.LOAD) && containsLoad && entry.getCost() < 0) {
					final String failureMessage = String.format("Port%s [%s] contain%s a load capability but load cost is < $0", portNames.size() > 1 ? "s" : "", portsInQuestion,
							portNames.size() > 1 ? "" : "s");
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage, IStatus.WARNING));
					dsd.addEObjectAndFeature(portCost, PricingPackage.Literals.COST_MODEL__PORT_COSTS);
					failures.add(dsd);
				}
				if (capability.equals(PortCapability.DISCHARGE) && containsDischarge && entry.getCost() < 0) {
					final String failureMessage = String.format("Port%s [%s] contain%s a discharge capability but discharge cost is < $0", portNames.size() > 1 ? "s" : "", portsInQuestion,
							portNames.size() > 1 ? "" : "s");
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage, IStatus.WARNING));
					dsd.addEObjectAndFeature(portCost, PricingPackage.Literals.COST_MODEL__PORT_COSTS);
					failures.add(dsd);
				}
			}

		}

		return Activator.PLUGIN_ID;
	}
}
