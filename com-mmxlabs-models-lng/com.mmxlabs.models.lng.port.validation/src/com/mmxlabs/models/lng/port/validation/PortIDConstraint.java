/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.validation;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class PortIDConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(@NonNull final IValidationContext ctx, @NonNull final IExtraValidationContext extraContext, @NonNull final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();
		if (target instanceof PortModel) {
			final PortModel portModel = (PortModel) target;

			final Map<String, List<Port>> groupedById = portModel.getPorts().stream() //
					.filter(p -> p.getLocation() != null) //
					.filter(p -> p.getLocation().getMmxId() != null) //
					.filter(p -> !p.getLocation().getMmxId().isEmpty()) //
					.collect(Collectors.groupingBy(p -> p.getLocation().getMmxId(), Collectors.toList()));

			for (final Map.Entry<String, List<Port>> e : groupedById.entrySet()) {
				if (e.getValue().size() > 1) {

					final String names = e.getValue().stream() //
							.map(p -> p.getName()) //
							.sorted() //
							.collect(Collectors.joining(", "));

					final String message = String.format(
							"Ports %s are considered to be duplicates and should be merged using the merge ports option in the Ports view. Please contact Minimax Labs if you need them to be separate.",
							names);
					final DetailConstraintStatusFactory factory = DetailConstraintStatusFactory.makeStatus() //
							.withMessage(message);

					e.getValue().forEach(p -> factory.withObjectAndFeature(p, MMXCorePackage.Literals.NAMED_OBJECT__NAME));

					factory.make(ctx, statuses);
				}
			}
		}
		return Activator.PLUGIN_ID;
	}
}
