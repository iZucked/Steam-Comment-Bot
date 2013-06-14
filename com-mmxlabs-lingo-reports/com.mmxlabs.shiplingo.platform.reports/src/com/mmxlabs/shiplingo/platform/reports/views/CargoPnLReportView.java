/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.reports.views;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.GroupProfitAndLoss;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;

/**
 * @since 3.0
 */
public class CargoPnLReportView extends AbstractCargoReportView {
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.shiplingo.platform.reports.views.CargoPnLReportView";

	public CargoPnLReportView() {
		super("com.mmxlabs.shiplingo.platform.reports.CargoPnLReportView");

		final SchedulePackage s = SchedulePackage.eINSTANCE;

		final EAttribute name = MMXCorePackage.eINSTANCE.getNamedObject_Name();

		addScheduleColumn("Schedule", containingScheduleFormatter);

		addColumn("ID", objectFormatter, cargoAllocationRef, s.getCargoAllocation__GetName());

		// addColumn("Type", objectFormatter, s.getCargoAllocation__GetType());
		addPNLColumn(cargoAllocationRef);

		// addColumn("Load Port", objectFormatter, s.getCargoAllocation_LoadAllocation(), s.getSlotAllocation__GetPort(), name);
		addColumn("Load Date", datePartFormatter, loadAllocationRef, s.getSlotAllocation__GetLocalStart());
		// addColumn("Buy Contract", objectFormatter, s.getCargoAllocation_LoadAllocation(), s.getSlotAllocation__GetContract(), name);
		addColumn("Buy Price", objectFormatter, loadAllocationRef, s.getSlotAllocation_Price());

		// addColumn("Discharge Port", objectFormatter, s.getCargoAllocation_DischargeAllocation(), s.getSlotAllocation__GetPort(), name);
		// addColumn("Discharge Date", datePartFormatter, s.getCargoAllocation_DischargeAllocation(), s.getSlotAllocation__GetLocalStart());
		// addColumn("Sell Contract", objectFormatter, s.getCargoAllocation_DischargeAllocation(), s.getSlotAllocation__GetContract(), name);
		addColumn("Sell Price", objectFormatter, dischargeAllocationRef, s.getSlotAllocation_Price());

		// addColumn("Vessel", objectFormatter, value1, s.getCargoAllocation_Sequence(), SchedulePackage.eINSTANCE.getSequence__GetName());

	}

	@Override
	protected boolean handleSelections() {
		return true;
	}

	private void addPNLColumn(final EStructuralFeature feature) {

		final String title = "P&L";
		addColumn(title, new IntegerFormatter() {
			@Override
			public Integer getIntValue(final Object object) {

				if (object instanceof EObject) {
					final EObject eObj = (EObject) object;

					final Object featureObj = eObj.eGet(feature);

					if (featureObj instanceof CargoAllocation) {

						final CargoAllocation cargoAllocation = (CargoAllocation) featureObj;
						final GroupProfitAndLoss dataWithKey = cargoAllocation.getGroupProfitAndLoss();
						if (dataWithKey != null) {
							return (int) dataWithKey.getProfitAndLoss();
						}
					}
				}

				return null;
			}
		});
	}

}
