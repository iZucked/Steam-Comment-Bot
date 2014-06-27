/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.lingo.reports.IScenarioInstanceElementCollector;
import com.mmxlabs.lingo.reports.IScenarioViewerSynchronizerOutput;
import com.mmxlabs.lingo.reports.utils.ScheduleDiffUtils;
import com.mmxlabs.lingo.reports.views.formatters.BaseFormatter;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.MaintenanceEvent;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.ui.tabular.generic.GenericEMFTableDataModel;

/**
 */
public class SchedulePnLReport extends EMFReportView {
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.shiplingo.platform.reports.views.SchedulePnLReport";

	private final ScheduleBasedReportBuilder builder;

	private final EPackage tableDataModel;
	private final EStructuralFeature nameObjectRef;
	private final EStructuralFeature targetObjectRef;
	private final EStructuralFeature cargoAllocationRef;
	private final EStructuralFeature loadAllocationRef;
	private final EStructuralFeature dischargeAllocationRef;
	private final EStructuralFeature openSlotAllocationRef;

	public SchedulePnLReport() {
		super(ID);

		builder = new ScheduleBasedReportBuilder(this, pinDiffModeHelper);
		tableDataModel = builder.getTableDataModel();
		nameObjectRef = builder.getNameObjectRef();
		targetObjectRef = builder.getTargetObjectRef();
		cargoAllocationRef = builder.getCargoAllocationRef();
		loadAllocationRef = builder.getLoadAllocationRef();
		dischargeAllocationRef = builder.getDischargeAllocationRef();
		openSlotAllocationRef = builder.getOpenSlotAllocationRef();

		// Show everything
		builder.setRowFilter(ScheduleBasedReportBuilder.ROW_FILTER_ALL);

		addScheduleColumn("Schedule", containingScheduleFormatter);

		addColumn("ID", ColumnType.NORMAL, objectFormatter, nameObjectRef);

		// add the total (aggregate) P&L column
		builder.addPNLColumn(CommercialPackage.Literals.BASE_LEGAL_ENTITY__TRADING_BOOK);
		builder.addPNLColumn(CommercialPackage.Literals.BASE_LEGAL_ENTITY__SHIPPING_BOOK);

		addColumn("Discharge Port", ColumnType.NORMAL, new BaseFormatter() {
			@Override
			public String format(final Object object) {
				if (object instanceof SlotAllocation) {
					final SlotAllocation slotAllocation = (SlotAllocation) object;
					return slotAllocation.getPort().getName();
				}
				return null;
			}

			@Override
			public Comparable<?> getComparable(final Object object) {
				if (object instanceof SlotAllocation) {
					final SlotAllocation slotAllocation = (SlotAllocation) object;
					return slotAllocation.getPort().getName();
				}
				return "";
			}
		}, dischargeAllocationRef);
		addColumn("Sales Contract", ColumnType.NORMAL, new BaseFormatter() {
			@Override
			public String format(final Object object) {
				if (object instanceof SlotAllocation) {
					final SlotAllocation slotAllocation = (SlotAllocation) object;
					final Contract contract = slotAllocation.getContract();
					if (contract != null) {
						return contract.getName();
					}
				}
				return null;
			}

			@Override
			public Comparable<?> getComparable(final Object object) {
				if (object instanceof SlotAllocation) {
					final SlotAllocation slotAllocation = (SlotAllocation) object;
					final Contract contract = slotAllocation.getContract();
					if (contract != null) {
						return contract.getName();
					}
				}
				return "";
			}
		}, dischargeAllocationRef);

		addColumn("Purchase Price", ColumnType.NORMAL, new BaseFormatter() {
			@Override
			public String format(final Object object) {
				if (object instanceof SlotAllocation) {
					final SlotAllocation slotAllocation = (SlotAllocation) object;
					return super.format(slotAllocation.getPrice());
				}
				return null;
			}

			@Override
			public Comparable getComparable(final Object object) {
				if (object instanceof SlotAllocation) {
					final SlotAllocation slotAllocation = (SlotAllocation) object;
					return slotAllocation.getPrice();
				}
				return 0.0;
			}
		}, loadAllocationRef);
		addColumn("Sales Price", ColumnType.NORMAL, new BaseFormatter() {
			@Override
			public String format(final Object object) {
				if (object instanceof SlotAllocation) {
					final SlotAllocation slotAllocation = (SlotAllocation) object;
					return super.format(slotAllocation.getPrice());
				}
				return null;
			}

			@Override
			public Comparable getComparable(final Object object) {
				if (object instanceof SlotAllocation) {
					final SlotAllocation slotAllocation = (SlotAllocation) object;
					return slotAllocation.getPrice();
				}
				return 0.0;
			}
		}, dischargeAllocationRef);

		addColumn("Type", ColumnType.NORMAL, new BaseFormatter() {
			@Override
			public String format(final Object object) {
				if (object instanceof OpenSlotAllocation) {
					OpenSlotAllocation openSlotAllocation = (OpenSlotAllocation) object;
					String type = "Open Slot";
					final Slot slot = openSlotAllocation.getSlot();
					if (slot instanceof LoadSlot) {
						type = "Long";
					} else if (slot instanceof DischargeSlot) {
						type = "Short";
					}
					return type;
				} else if (object instanceof SlotVisit || object instanceof CargoAllocation) {
					return "Cargo";
				} else if (object instanceof StartEvent) {
					return "Start";
				} else if (object instanceof GeneratedCharterOut) {
					return "Charter Out (virt)";
				} else if (object instanceof VesselEventVisit) {
					final VesselEvent vesselEvent = ((VesselEventVisit) object).getVesselEvent();
					if (vesselEvent instanceof DryDockEvent) {
						return "Dry Dock";
					} else if (vesselEvent instanceof MaintenanceEvent) {
						return "Maintenance";
					} else if (vesselEvent instanceof CharterOutEvent) {
						return "Charter Out";
					}
				}
				return "Unknown";
			}
		}, targetObjectRef);

		builder.createPinDiffColumns();
	}

	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		viewer.setComparator(GenericEMFTableDataModel.createGroupComparator(viewer.getComparator(), tableDataModel));
	}

	@Override
	protected ITreeContentProvider getContentProvider() {
		return builder.createPNLColumnssContentProvider(super.getContentProvider());
	}

	@Override
	protected void processInputs(final Object[] result) {
		builder.processInputs(result);
	}

	@Override
	protected IScenarioInstanceElementCollector getElementCollector() {
		return builder.getElementCollector();
	}

	/**
	 * Returns a key of some kind for the element
	 * 
	 * @param element
	 * @return
	 */
	@Override
	public String getElementKey(EObject element) {
		return builder.getElementKey(element);
	}

	@Override
	protected boolean isElementDifferent(final EObject pinnedObject, final EObject otherObject) {
		return ScheduleDiffUtils.isElementDifferent((EObject) pinnedObject.eGet(targetObjectRef), (EObject) otherObject.eGet(targetObjectRef));
	}

	@Override
	protected List<?> adaptSelectionFromWidget(final List<?> selection) {
		return builder.adaptSelectionFromWidget(selection);
	}

	/**
	 */
	@Override
	protected boolean handleSelections() {
		return true;
	}
}
