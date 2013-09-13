/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.reports.views;

import java.util.EnumMap;
import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;

import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SequenceType;
import com.mmxlabs.models.mmxcore.MMXCorePackage;

/**
 * '90 Day' cargo report view. Columns are in a different order to the standard report, there is an additional "notes" column and the string literals for the "vessel" column are slightly different.
 * 
 * @author Simon McGregor
 * 
 */
public class NinetyDayCargoReportView extends AbstractCargoReportView {
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.shiplingo.platform.reports.views.NinetyDayCargoReportView";

	/** lookup table of special string literals for the "vessel" field */
	private static final Map<SequenceType, String> vesselTextMap = createVesselTextMap();

	private static Map<SequenceType, String> createVesselTextMap() {
		EnumMap<SequenceType, String> result = new EnumMap<SequenceType, String>(SequenceType.class);
		result.put(SequenceType.DES_PURCHASE, "Ex Ship");
		result.put(SequenceType.FOB_SALE, "Ex Ship");
		result.put(SequenceType.SPOT_VESSEL, "Charter");
		return result;
	}

	/** special formatter for the "notes" field */
	private final IFormatter notesFormatter = new NotesFormatter();
	/** special formatter for the "vessel" field */
	private final IFormatter vesselFormatter = new VesselFormatter();

	/**
	 * Concatenate the notes from each slot for the "notes" column in the report.
	 * 
	 * @author Simon McGregor
	 * 
	 */
	class NotesFormatter extends BaseFormatter {
		final String delimiter = " \n";

		@Override
		public String format(Object object) {
			// Concatenate all the notes in the cargo slots.
			final CargoAllocation allocation = (CargoAllocation) object;

			String result = "";
			boolean firstPass = true;

			for (Slot slot : allocation.getInputCargo().getSlots()) {
				String note = slot.getNotes();
				if (note != null) {
					if (firstPass == false) {
						result += delimiter;
					}
					result += slot.getNotes();
					firstPass = false;
				}
			}
			return result;
		}

	}

	/**
	 * Override the default text for special vessels with '90 day' specific literals.
	 * 
	 * @author Simon McGregor
	 * 
	 */
	class VesselFormatter extends BaseFormatter {

		@Override
		public String format(Object object) {
			Sequence sequence = (Sequence) object;
			SequenceType type = sequence.getSequenceType();

			if (vesselTextMap.containsKey(type)) {
				return vesselTextMap.get(type);
			}

			return sequence.getName();
		}

	}

	public NinetyDayCargoReportView() {
		super(ID);

		final SchedulePackage s = SchedulePackage.eINSTANCE;

		final EAttribute name = MMXCorePackage.eINSTANCE.getNamedObject_Name();

		addScheduleColumn("Schedule", containingScheduleFormatter);

		// TODO cargo id not slot id.
		addColumn("ID", objectFormatter, cargoAllocationRef, s.getCargoAllocation__GetName());

		addColumn("Purchase Contract", objectFormatter, loadAllocationRef, s.getSlotAllocation__GetContract(), name);

		addColumn("Load Port", objectFormatter, loadAllocationRef, s.getSlotAllocation__GetPort(), name);
		addColumn("Load Date", datePartFormatter, loadAllocationRef, s.getSlotAllocation__GetLocalStart());

		addColumn("Sales Contract", objectFormatter, dischargeAllocationRef, s.getSlotAllocation__GetContract(), name);
		addColumn("Discharge Port", objectFormatter, dischargeAllocationRef, s.getSlotAllocation__GetPort(), name);
		addColumn("Discharge Date", datePartFormatter, dischargeAllocationRef, s.getSlotAllocation__GetLocalStart());

		addColumn("Vessel", vesselFormatter, cargoAllocationRef, s.getCargoAllocation_Sequence());
		addColumn("Notes", notesFormatter, cargoAllocationRef);
	}
}
