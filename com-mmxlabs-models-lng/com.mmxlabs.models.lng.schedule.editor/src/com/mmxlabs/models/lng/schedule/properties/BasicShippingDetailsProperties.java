/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
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
import com.mmxlabs.models.lng.schedule.EventGrouping;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
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
		if (eObject instanceof EventGrouping) {
			final EventGrouping eventGrouping = (EventGrouping) eObject;
			return createTree(eventGrouping, null);
		}

		return null;
	}

	private DetailProperty createTree(@NonNull final EventGrouping eventGrouping, @Nullable final MMXRootObject rootObject) {

		// Check for unshipped cargoes
		if (eventGrouping instanceof CargoAllocation) {
			CargoAllocation cargoAllocation = (CargoAllocation) eventGrouping;
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
		// long value = ScheduleModelKPIUtils.calculateEventShippingCost(eventGrouping, false, false, ShippingCostType.LNG_COSTS);
		// addDetailProperty("LNG", "", "$", "", value, new StringFormatLabelProvider("%,d"), details);
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
			addDetailProperty("Heel cost", "", "$", "", value, new StringFormatLabelProvider("%,d"), details);

			// add child heel volumes
		}
		{
			final long value = ScheduleModelKPIUtils.calculateEventShippingCost(eventGrouping, false, false, ShippingCostType.HEEL_REVENUE);
			addDetailProperty("Heel revenue", "", "$", "", value, new StringFormatLabelProvider("%,d"), details);
			// add child heel volumes
		}

		if (eventGrouping instanceof GeneratedCharterOut) {
			final GeneratedCharterOut event = (GeneratedCharterOut) eventGrouping;
			addDetailProperty("Charter revenue", "", "$", "", event.getRevenue(), new StringFormatLabelProvider("%,d"), details);
		} else if (eventGrouping instanceof VesselEventVisit) {
			final VesselEventVisit vesselEventVisit = (VesselEventVisit) eventGrouping;
			if (vesselEventVisit.getVesselEvent() instanceof CharterOutEvent) {
				final CharterOutEvent charterOutEvent = (CharterOutEvent) vesselEventVisit.getVesselEvent();
				final long value = charterOutEvent.getDurationInDays() * charterOutEvent.getHireRate();
				addDetailProperty("Charter revenue", "", "$", "", value, new StringFormatLabelProvider("%,d"), details);
			}
		}

		if (eventGrouping instanceof StartEvent) {
			StartEvent startEvent = (StartEvent) eventGrouping;
			{
				// addDetailProperty("Repositioning", "", "$", "", 0, new StringFormatLabelProvider("%,d"), details);
			}
		}

		return details;
	}
}
