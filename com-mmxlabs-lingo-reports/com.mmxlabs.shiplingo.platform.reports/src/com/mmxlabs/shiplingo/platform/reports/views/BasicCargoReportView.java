/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.reports.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.shiplingo.platform.reports.IScenarioInstanceElementCollector;
import com.mmxlabs.shiplingo.platform.reports.ScheduleElementCollector;
import com.mmxlabs.shiplingo.platform.reports.utils.ScheduleDiffUtils;

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

		final CargoPackage c = CargoPackage.eINSTANCE;
		final SchedulePackage s = SchedulePackage.eINSTANCE;

		final EAttribute name = MMXCorePackage.eINSTANCE.getNamedObject_Name();

		addScheduleColumn("Schedule", containingScheduleFormatter);

		// TODO cargo id not slot id.
		addColumn("ID", objectFormatter, cargoAllocationRef, s.getCargoAllocation__GetName());

		addColumn("Type", objectFormatter, cargoAllocationRef, s.getCargoAllocation_InputCargo(), c.getCargo__GetCargoType());

		addColumn("Load Port", objectFormatter, loadAllocationRef, s.getSlotAllocation__GetPort(), name);
		addColumn("Load Date", datePartFormatter, loadAllocationRef, s.getSlotAllocation__GetLocalStart());
		addColumn("Purchase Contract", objectFormatter, loadAllocationRef, s.getSlotAllocation__GetContract(), name);

		addColumn("Discharge Port", objectFormatter, dischargeAllocationRef, s.getSlotAllocation__GetPort(), name);
		addColumn("Discharge Date", datePartFormatter, dischargeAllocationRef, s.getSlotAllocation__GetLocalStart());
		addColumn("Sales Contract", objectFormatter, dischargeAllocationRef, s.getSlotAllocation__GetContract(), name);

		addColumn("Vessel", objectFormatter, cargoAllocationRef, s.getCargoAllocation_Sequence(), SchedulePackage.eINSTANCE.getSequence__GetName());
	}
}
