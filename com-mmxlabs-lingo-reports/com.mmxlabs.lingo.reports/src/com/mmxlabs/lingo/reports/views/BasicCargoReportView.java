/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views;

import org.eclipse.emf.ecore.EAttribute;

import com.mmxlabs.lingo.reports.components.ColumnType;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;

/**
 * 
 * 
 * @author hinton
 * 
 */
public class BasicCargoReportView extends AbstractCargoReportView {
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.shiplingo.platform.reports.views.BasicCargoReportView";

	public BasicCargoReportView() {
		super("com.mmxlabs.shiplingo.platform.reports.BasicCargoReportView");
	}

	@Override
	protected void createColumns() {

		final CargoPackage c = CargoPackage.eINSTANCE;
		final SchedulePackage s = SchedulePackage.eINSTANCE;

		final EAttribute name = MMXCorePackage.eINSTANCE.getNamedObject_Name();

		addScheduleColumn("Schedule", containingScheduleFormatter);

		// TODO cargo id not slot id.
		addColumn("ID", ColumnType.NORMAL, objectFormatter, cargoAllocationRef, s.getCargoAllocation__GetName());

		addColumn("Type", ColumnType.NORMAL, objectFormatter, cargoAllocationRef, s.getCargoAllocation_InputCargo(), c.getCargo__GetCargoType());

		addColumn("Load Port", ColumnType.NORMAL, objectFormatter, loadAllocationRef, s.getSlotAllocation__GetPort(), name);
		addColumn("Load Date", ColumnType.NORMAL, datePartFormatter, loadAllocationRef, s.getSlotAllocation__GetLocalStart());
		addColumn("Purchase Contract", ColumnType.NORMAL, objectFormatter, loadAllocationRef, s.getSlotAllocation__GetContract(), name);

		addColumn("Discharge Port", ColumnType.NORMAL, objectFormatter, dischargeAllocationRef, s.getSlotAllocation__GetPort(), name);
		addColumn("Discharge Date", ColumnType.NORMAL, datePartFormatter, dischargeAllocationRef, s.getSlotAllocation__GetLocalStart());
		addColumn("Sales Contract", ColumnType.NORMAL, objectFormatter, dischargeAllocationRef, s.getSlotAllocation__GetContract(), name);

		addColumn("Vessel", ColumnType.NORMAL, objectFormatter, cargoAllocationRef, s.getCargoAllocation_Sequence(), SchedulePackage.eINSTANCE.getSequence__GetName());

		super.createColumns();
	}
}
