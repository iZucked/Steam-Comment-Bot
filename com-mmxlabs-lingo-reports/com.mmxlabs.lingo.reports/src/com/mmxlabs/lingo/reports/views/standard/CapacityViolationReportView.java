/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard;

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
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.lingo.reports.IReportContents;
import com.mmxlabs.lingo.reports.IScenarioInstanceElementCollector;
import com.mmxlabs.lingo.reports.ScheduleElementCollector;
import com.mmxlabs.lingo.reports.components.ColumnBlock;
import com.mmxlabs.lingo.reports.components.ColumnHandler;
import com.mmxlabs.lingo.reports.components.ColumnType;
import com.mmxlabs.lingo.reports.components.EMFReportView;
import com.mmxlabs.lingo.reports.utils.ScheduleDiffUtils;
import com.mmxlabs.lingo.reports.views.formatters.BaseFormatter;
import com.mmxlabs.lingo.reports.views.formatters.Formatters;
import com.mmxlabs.models.lng.schedule.CapacityViolationType;
import com.mmxlabs.models.lng.schedule.CapacityViolationsHolder;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.ui.tabular.generic.GenericEMFTableDataModel;
import com.mmxlabs.rcp.common.actions.CopyGridToHtmlStringUtil;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

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
		super("com.mmxlabs.lingo.doc.Reports_CapacityViolations");

		createDataModel();

		addColumn("schedule", "Schedule", ColumnType.MULTIPLE, containingScheduleFormatter);

		final SchedulePackage sp = SchedulePackage.eINSTANCE;

		addColumn("id", "ID", ColumnType.NORMAL, Formatters.objectFormatter, ref_Row_Owner, sp.getEvent__Name());

		addColumn("type", "Type", ColumnType.NORMAL, Formatters.objectFormatter, ref_Row_Owner, sp.getEvent__Type());

		addColumn("violation", "Violation", ColumnType.NORMAL, new BaseFormatter() {
			@Override
			public String render(final Object object) {

				if (object instanceof CapacityViolationType) {
					final CapacityViolationType capacityViolationType = (CapacityViolationType) object;
					switch (capacityViolationType) {
					case FORCED_COOLDOWN:
						return "Forced Cooldown";
					case LOST_HEEL:
						return "Lost Heel";
					case MAX_DISCHARGE:
						return "Max Discharge";
					case MAX_HEEL:
						return "Max Heel";
					case MAX_LOAD:
						return "Max Load";
					case MIN_DISCHARGE:
						return "Min Discharge";
					case MIN_LOAD:
						return "Min Load";
					case VESSEL_CAPACITY:
						return "Vessel Capacity";
					default:
						break;
					}
				}

				return super.render(object);
			}
		}, attrib_Row_Type);
		addColumn("qty", "Quantity (m³)", ColumnType.NORMAL, Formatters.objectFormatter, attrib_Row_Quantity);

		getBlockManager().makeAllBlocksVisible();

	}

	@Override
	public void createPartControl(final Composite parent) {
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
		} else if (event instanceof VesselEventVisit) {
			objects.add(event);
		} else if (event instanceof EndEvent) {
			objects.add(event);
		}

		return objects;
	}

	@Override
	protected boolean isElementDifferent(final EObject pinnedObject, final EObject otherObject) {
		return new ScheduleDiffUtils().isElementDifferent(pinnedObject, otherObject);
	}

	@Override
	protected IScenarioInstanceElementCollector getElementCollector() {
		return new ScheduleElementCollector() {

			private EObject dataModelInstance;

			@Override
			public void beginCollecting(boolean pinDiffMode) {
				super.beginCollecting(pinDiffMode);
				CapacityViolationReportView.this.clearPinModeData();

				dataModelInstance = GenericEMFTableDataModel.createRootInstance(tableDataModel);
			}

			@Override
			protected Collection<? extends Object> collectElements(final ScenarioInstance scenarioInstance, final Schedule schedule, final boolean pinned) {

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
	 */
	@Override
	public String getElementKey(EObject element) {

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
		attrib_Row_Type = GenericEMFTableDataModel.createRowAttribute(rowClass, SchedulePackage.Literals.CAPACITY_VIOLATION_TYPE, ATTRIBUTE_TYPE);
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

	public ColumnHandler addColumn(final String blockID, final String title, final ColumnType columnType, final ICellRenderer formatter, final ETypedElement... path) {
		final ColumnBlock block = getBlockManager().createBlock(blockID, title, columnType);
		return getBlockManager().createColumn(block, title, formatter, path);
	}

	@Override
	public <T> T getAdapter(final Class<T> adapter) {

		if (IReportContents.class.isAssignableFrom(adapter)) {

			final CopyGridToHtmlStringUtil util = new CopyGridToHtmlStringUtil(viewer.getGrid(), false, true);
			final String contents = util.convert();
			return (T) new IReportContents() {

				@Override
				public String getStringContents() {
					return contents;
				}
			};

		}
		return super.getAdapter(adapter);
	}
}
