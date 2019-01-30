package com.mmxlabs.lngdataserver.lng.importers.financial;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.lngdataserver.integration.general.model.financial.settled.SettledPriceCurve;
import com.mmxlabs.lngdataserver.integration.general.model.financial.settled.SettledPricesVersion;
import com.mmxlabs.models.lng.pricing.DatePoint;
import com.mmxlabs.models.lng.pricing.DatePointContainer;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.VersionRecord;

public class SettledPricesToScenarioImporter {

	public static Command getUpdateCommand(@NonNull final EditingDomain editingDomain, @NonNull final PricingModel pricingModel, final SettledPricesVersion version) {

		final CompoundCommand cmd = new CompoundCommand("Update settled prices");

		// Gather existing curves
		final Map<String, DatePointContainer> map = new HashMap<>();
		final Set<DatePointContainer> existing = new HashSet<>();

		pricingModel.getSettledPrices().forEach(c -> map.put(c.getName(), c));
		pricingModel.getSettledPrices().forEach(existing::add);

		final Set<DatePointContainer> updated = new HashSet<>();

		for (final SettledPriceCurve curve : version.getCurves()) {
			final String name = curve.getName();

			final List<DatePoint> data = curve.getEntries().stream().map(point -> {
				final DatePoint indexPoint = PricingPackage.eINSTANCE.getPricingFactory().createDatePoint();
				indexPoint.setDate(point.getDate());
				indexPoint.setValue(point.getValue());
				return indexPoint;
			}).collect(Collectors.toList());

			DatePointContainer existingCurve = map.get(name);

			if (existingCurve == null) {
				existingCurve = PricingFactory.eINSTANCE.createDatePointContainer();
				existingCurve.setName(name);
				existingCurve.getPoints().addAll(data);
				cmd.append(AddCommand.create(editingDomain, pricingModel, PricingPackage.Literals.PRICING_MODEL__SETTLED_PRICES, existingCurve));
			} else {
				cmd.append(RemoveCommand.create(editingDomain, existingCurve, PricingPackage.Literals.DATE_POINT_CONTAINER__POINTS, new LinkedList<>(existingCurve.getPoints())));
				cmd.append(AddCommand.create(editingDomain, existingCurve, PricingPackage.Literals.DATE_POINT_CONTAINER__POINTS, data));
			}
			updated.add(existingCurve);
		}

		existing.removeAll(updated);
		if (!existing.isEmpty()) {
			cmd.append(DeleteCommand.create(editingDomain, existing));
		}


		VersionRecord record = pricingModel.getSettledPricesVersionRecord();
		cmd.append(SetCommand.create(editingDomain, record, MMXCorePackage.Literals.VERSION_RECORD__CREATED_BY, version.getCreatedBy()));
		cmd.append(SetCommand.create(editingDomain, record, MMXCorePackage.Literals.VERSION_RECORD__CREATED_AT, version.getCreatedAt()));
		cmd.append(SetCommand.create(editingDomain, record, MMXCorePackage.Literals.VERSION_RECORD__VERSION, version.getIdentifier()));

		return cmd;
	}
}
