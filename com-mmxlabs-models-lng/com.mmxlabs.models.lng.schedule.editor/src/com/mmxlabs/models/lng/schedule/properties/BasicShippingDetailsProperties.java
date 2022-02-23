/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
Non * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.properties;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.EventGrouping;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.SequenceType;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils.ShippingCostType;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.properties.DetailProperty;
import com.mmxlabs.models.ui.properties.PropertiesFactory;
import com.mmxlabs.models.ui.properties.factory.AbstractDetailPropertyFactory;
import com.mmxlabs.models.ui.properties.ui.StringFormatLabelProvider;

public class BasicShippingDetailsProperties extends AbstractDetailPropertyFactory {

	private static final String CATEGORY_PNL = "pnl";

	@Override
	@Nullable
	public DetailProperty createProperties(@NonNull final EObject eObject) {
		if (eObject instanceof EventGrouping eventGrouping) {
			return createTree(eventGrouping, null);
		}

		return null;
	}

	private DetailProperty createTree(@NonNull final EventGrouping eventGrouping, @Nullable final MMXRootObject rootObject) {

		// Check for unshipped cargoes
		if (eventGrouping instanceof CargoAllocation cargoAllocation) {
			if (cargoAllocation.getSequence().getSequenceType() == SequenceType.FOB_SALE) {
				return null;
			}
			if (cargoAllocation.getSequence().getSequenceType() == SequenceType.DES_PURCHASE) {
				return null;
			}
		}

		final DetailProperty details = PropertiesFactory.eINSTANCE.createDetailProperty();
		details.setName("Real shipping");
		details.setDescription("Actual costs before considering transfer costs and tax");
		{
			final long value = ScheduleModelKPIUtils.calculateEventShippingCost(eventGrouping, false, false, ShippingCostType.HIRE_COSTS);
			addDetailProperty("Hire", "", "$", "", value, new StringFormatLabelProvider("%,d"), details);
		}
		{
			final long value = ScheduleModelKPIUtils.calculateEventShippingCost(eventGrouping, false, false, ShippingCostType.BUNKER_COSTS);
			addDetailProperty("Bunkers", "", "$", "", value, new StringFormatLabelProvider("%,d"), details);
		}
		// {
		// long value = ScheduleModelKPIUtils.calculateEventShippingCost(eventGrouping,
		// false, false, ShippingCostType.LNG_COSTS);
		// addDetailProperty("LNG", "", "$", "", value, new
		// StringFormatLabelProvider("%,d"), details);
		// }
		{
			final long value = ScheduleModelKPIUtils.calculateEventShippingCost(eventGrouping, false, false, ShippingCostType.PORT_COSTS);
			addDetailProperty("Port", "", "$", "", value, new StringFormatLabelProvider("%,d"), details);
		}
		{
			final long value = ScheduleModelKPIUtils.calculateEventShippingCost(eventGrouping, false, false, ShippingCostType.CANAL_COSTS);
			addDetailProperty("Canal", "", "$", "", value, new StringFormatLabelProvider("%,d"), details);
		}
		{
			final long value = ScheduleModelKPIUtils.calculateEventShippingCost(eventGrouping, false, false, ShippingCostType.PURGE_COSTS);
			if (value != 0) {
				addDetailProperty("Purge", "", "$", "", value, new StringFormatLabelProvider("%,d"), details);
			}
		}
		{
			final long value = ScheduleModelKPIUtils.calculateEventShippingCost(eventGrouping, false, false, ShippingCostType.COOLDOWN_COSTS);
			addDetailProperty("Cooldown", "", "$", "", value, new StringFormatLabelProvider("%,d"), details);
		}
		{
			final long value = ScheduleModelKPIUtils.calculateEventShippingCost(eventGrouping, false, false, ShippingCostType.OTHER_COSTS);
			addDetailProperty("Other", "", "$", "", value, new StringFormatLabelProvider("%,d"), details);
		}

		// TODO: Add in volume
		{
			final long value = ScheduleModelKPIUtils.calculateEventShippingCost(eventGrouping, false, false, ShippingCostType.HEEL_COST);
			final DetailProperty dp = addDetailProperty("Heel cost", "", "$", "", value, new StringFormatLabelProvider("%,d"), details);
			for (final Event event : eventGrouping.getEvents()) {
				if (event instanceof PortVisit) {
					final PortVisit portVisit = (PortVisit) event;
					if (portVisit.getHeelCost() != 0.0) {
						addDetailProperty("Price", "", "$", "/mmBtu", portVisit.getHeelCostUnitPrice(), new StringFormatLabelProvider("%.2f"), dp);
					}
				}
			}
			// add child heel volumes
		}
		{
			final long value = ScheduleModelKPIUtils.calculateEventShippingCost(eventGrouping, false, false, ShippingCostType.HEEL_REVENUE);
			final DetailProperty dp = addDetailProperty("Heel revenue", "", "$", "", -value, new StringFormatLabelProvider("%,d"), details);
			for (final Event event : eventGrouping.getEvents()) {
				if (event instanceof PortVisit portVisit) {
					if (portVisit.getHeelRevenue() != 0.0) {
						addDetailProperty("Price", "", "$", "/mmBtu", portVisit.getHeelRevenueUnitPrice(), new StringFormatLabelProvider("%.2f"), dp);
					}
				}
			}
		}

		if (eventGrouping instanceof GeneratedCharterOut event) {
			addDetailProperty("Charter revenue", "", "$", "", event.getRevenue(), new StringFormatLabelProvider("%,d"), details);
		} else if (eventGrouping instanceof VesselEventVisit vesselEventVisit) {
			if (vesselEventVisit.getVesselEvent() instanceof CharterOutEvent charterOutEvent) {
				final long value = charterOutEvent.getDurationInDays() * charterOutEvent.getHireRate();
				addDetailProperty("Charter revenue", "", "$", "", value, new StringFormatLabelProvider("%,d"), details);
			}
		}

		if (eventGrouping instanceof StartEvent startEvent) {
			{
				// addDetailProperty("Repositioning", "", "$", "", 0, new
				// StringFormatLabelProvider("%,d"), details);
			}
		}

		return details;
	}
}
