/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.reports.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.lng.schedule.CapacityViolationType;
import com.mmxlabs.models.lng.schedule.CapacityViolationsHolder;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.ui.tabular.generic.GenericEMFTableDataModel;
import com.mmxlabs.shiplingo.platform.reports.IScenarioInstanceElementCollector;
import com.mmxlabs.shiplingo.platform.reports.ScheduleElementCollector;
import com.mmxlabs.shiplingo.platform.reports.utils.ScheduleDiffUtils;

/**
 * @author Simon Goodall
 * @since 4.2
 * 
 */
public class CapacityViolationReportView extends EMFReportView {
	public static final String ID = "com.mmxlabs.shiplingo.platform.reports.views.CapacityViolationReportView";

	private EPackage tableDataModel;

	private final String NODE_OWNER = "owner";
	private final String ATTRIBUTE_QUANTITY = "quantity";
	private final String ATTRIBUTE_TYPE = "type";

	private EAttribute attrib_Row_Type;
	private EAttribute attrib_Row_Quantity;
	private EReference ref_Row_Owner;

	public CapacityViolationReportView() {
		super();

		createDataModel();

		addScheduleColumn("Schedule", containingScheduleFormatter);

		final SchedulePackage sp = SchedulePackage.eINSTANCE;

		addColumn("ID", objectFormatter, ref_Row_Owner, sp.getEvent__Name());

		addColumn("Type", objectFormatter, ref_Row_Owner, sp.getEvent__Type());

		addColumn("Violation", objectFormatter, attrib_Row_Type);
		addColumn("Quantity (m³)", objectFormatter, attrib_Row_Quantity);
	}

	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);

		viewer.setComparator(GenericEMFTableDataModel.createGroupComparator(viewer.getComparator(), tableDataModel));
	}

	@Override
	protected boolean handleSelections() {
		return true;
	}

	@Override
	protected void processInputs(final Object[] result) {

		for (final Object obj : result) {
			if (obj instanceof EObject) {
				final EObject row = (EObject) obj;
				final Event event = (Event) row.eGet(ref_Row_Owner);
				setInputEquivalents(row, expandEvent(event));
			}
		}
	}

	private Collection<Object> expandEvent(final Event event) {

		final Set<Object> objects = new HashSet<Object>();
		objects.add(event);
		if (event instanceof SlotVisit) {
			final SlotVisit slotVisit = (SlotVisit) event;
			final SlotAllocation slotAllocation = slotVisit.getSlotAllocation();
			final CargoAllocation cargoAllocation = slotAllocation.getCargoAllocation();
			if (cargoAllocation != null) {
				for (final SlotAllocation sa : cargoAllocation.getSlotAllocations()) {
					objects.add(sa);
					objects.add(sa.getSlotVisit());
				}
				objects.add(cargoAllocation);
				objects.add(cargoAllocation.getInputCargo());
			} else {
				objects.add(slotAllocation);
			}
		}

		return objects;
	}

	@Override
	protected boolean isElementDifferent(final EObject pinnedObject, final EObject otherObject) {
		return ScheduleDiffUtils.isElementDifferent(pinnedObject, otherObject);
	}

	@Override
	protected IScenarioInstanceElementCollector getElementCollector() {
		return new ScheduleElementCollector() {

			private EObject dataModelInstance;

			@Override
			public void beginCollecting() {
				super.beginCollecting();
				CapacityViolationReportView.this.clearPinModeData();

				dataModelInstance = GenericEMFTableDataModel.createRootInstance(tableDataModel);
			}

			@Override
			protected Collection<? extends Object> collectElements(final Schedule schedule, final boolean pinned) {

				final List<EObject> rows = new LinkedList<EObject>();

				for (final Sequence sequence : schedule.getSequences()) {
					for (final Event event : sequence.getEvents()) {
						if (event instanceof CapacityViolationsHolder) {
							final CapacityViolationsHolder capacityViolationsHolder = (CapacityViolationsHolder) event;
							final EMap<CapacityViolationType, Long> violationMap = capacityViolationsHolder.getViolations();
							if (!violationMap.isEmpty()) {
								for (final CapacityViolationType cvt : CapacityViolationType.values()) {
									if (violationMap.containsKey(cvt)) {
										final Long qty = violationMap.get(cvt);
										if (qty != null) {
											final EObject row = createRow(dataModelInstance, event, cvt, qty);
											rows.add(row);
										}
									}
								}
							}
						}
					}
				}

				CapacityViolationReportView.this.collectPinModeElements(rows, pinned);

				return rows;
			}
		};
	}

	/**
	 * Returns a key of some kind for the element
	 * 
	 * @param element
	 * @return
	 * @since 1.1
	 */
	@Override
	protected String getElementKey(EObject element) {

		if (element.eIsSet(ref_Row_Owner)) {
			element = (EObject) element.eGet(ref_Row_Owner);
		}

		if (element instanceof Event) {
			return ((Event) element).name();
		}
		return super.getElementKey(element);
	}

	@Override
	protected List<?> adaptSelectionFromWidget(final List<?> selection) {
		final List<Object> adaptedSelection = new ArrayList<Object>(selection.size());
		for (final Object obj : selection) {
			if (obj instanceof EObject) {
				adaptedSelection.add(((EObject) obj).eGet(ref_Row_Owner));
			}
		}

		return adaptedSelection;
	}

	private void createDataModel() {
		tableDataModel = GenericEMFTableDataModel.createEPackage(NODE_OWNER);

		final EClass rowClass = GenericEMFTableDataModel.getRowClass(tableDataModel);
		attrib_Row_Type = GenericEMFTableDataModel.createRowAttribute(rowClass, EcorePackage.eINSTANCE.getEEnum(), ATTRIBUTE_TYPE);
		attrib_Row_Quantity = GenericEMFTableDataModel.createRowAttribute(rowClass, EcorePackage.eINSTANCE.getELong(), ATTRIBUTE_QUANTITY);

		ref_Row_Owner = (EReference) GenericEMFTableDataModel.getRowFeature(tableDataModel, NODE_OWNER);
	}

	private EObject createRow(final EObject dataModelInstance, final EObject owner, final CapacityViolationType type, final long qty) {
		final EObject row = GenericEMFTableDataModel.createRow(tableDataModel, dataModelInstance, null);
		row.eSet(ref_Row_Owner, owner);
		row.eSet(attrib_Row_Type, type);
		row.eSet(attrib_Row_Quantity, qty);

		return row;
	}
}
