/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.reports.views;

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
import com.mmxlabs.shiplingo.platform.reports.ScheduledEventCollector;

/**
 * @author Simon Goodall
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

	@Override
	protected IScenarioInstanceElementCollector getElementCollector() {
		return new ScheduledEventCollector() {

			private EObject dataModelInstance;

			@Override
			public void beginCollecting() {
				super.beginCollecting();

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
											setInputEquivalents(row, expandEvent(event));
											rows.add(row);
										}
									}
								}
							}
						}
					}
				}

				return rows;
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
					} else {
						objects.add(slotAllocation);
					}
				}

				return objects;
			}
		};
	}

	@Override
	protected boolean handleSelections() {
		return true;
	}
}
