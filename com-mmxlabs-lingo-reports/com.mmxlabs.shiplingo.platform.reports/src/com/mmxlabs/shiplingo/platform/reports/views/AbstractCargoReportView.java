/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.reports.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.ui.tabular.generic.GenericEMFTableDataModel;
import com.mmxlabs.shiplingo.platform.reports.IScenarioInstanceElementCollector;
import com.mmxlabs.shiplingo.platform.reports.ScheduleElementCollector;
import com.mmxlabs.shiplingo.platform.reports.utils.CargoAllocationUtils;
import com.mmxlabs.shiplingo.platform.reports.utils.ScheduleDiffUtils;

/**
 * Abstract base class for the various reports based off CargoAllocations. We use the {@link GenericEMFTableDataModel} to create a table model where each "Node" or row contains references to the
 * CargoAllocation, and SlotAllocations for the load and discharge. For cargoes with more that one load or discharge, there will be multiple Node elements created - with the same CargoAllocation and
 * "Group" reference
 * 
 * @since 4.0
 */
public abstract class AbstractCargoReportView extends EMFReportView {
	protected EPackage tableDataModel;
	protected EStructuralFeature cargoAllocationRef;
	protected EStructuralFeature loadAllocationRef;
	protected EStructuralFeature dischargeAllocationRef;

	public AbstractCargoReportView(String id) {
		super(id);

		tableDataModel = GenericEMFTableDataModel.createEPackage(CargoAllocationUtils.NODE_FEATURE_CARGO, CargoAllocationUtils.NODE_FEATURE_LOAD, CargoAllocationUtils.NODE_FEATURE_DISCHARGE);
		cargoAllocationRef = GenericEMFTableDataModel.getRowFeature(tableDataModel, CargoAllocationUtils.NODE_FEATURE_CARGO);
		loadAllocationRef = GenericEMFTableDataModel.getRowFeature(tableDataModel, CargoAllocationUtils.NODE_FEATURE_LOAD);
		dischargeAllocationRef = GenericEMFTableDataModel.getRowFeature(tableDataModel, CargoAllocationUtils.NODE_FEATURE_DISCHARGE);

		
		// Add a column to show the former wiring of the cargo
		//TODO: This is exactly the same code as in SchedulePnLReport - should refactor		
		pinDiffColumnManager
			.addColumn("Former wiring", new BaseFormatter() {
		    	 @Override
		    	 public String format(final Object obj) {

		    		 StringBuffer sb = new StringBuffer();
		    		 
		    		 if (obj instanceof EObject) {
		    			 final EObject eObj = (EObject) obj;

		    			 	if (eObj.eIsSet(cargoAllocationRef)) {
		    			 		// TODO: Q: can any of these lookups return null?
		    			 		// TODO: Q: can there be a chain with more than 2 ports?
		    			 		try { 
			    			 		final CargoAllocation ca = (CargoAllocation) eObj.eGet(cargoAllocationRef);
			    			 		
			    			 		EList<SlotAllocation> caSlotAllocations = ca.getSlotAllocations();
			    			 				    			 		
			    			 		SlotAllocation caAlloc0 = caSlotAllocations.get(0);
			    			 		sb.append(caAlloc0.getPort().getName());
			    			 		
			    					for (int i = 1; i < caSlotAllocations.size(); ++i) {
			    						SlotAllocation caAllocation = caSlotAllocations.get(i);
			    							sb.append(" -> ").append(caAllocation.getPort().getName());
			    					}	
		    			 		} 
		    			 		catch (Exception e) {
		    			 			throw(e);
		    			 		}
		    			 		
		    			 	}
		    		 }

		    		 return sb.toString();
		    	 }
			}
	   );	
			
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
		for (final Object row : result) {
			// Map our "Node" data to the CargoAllocation object
			if (row instanceof EObject) {
				EObject eObj = (EObject) row;
				if (eObj.eIsSet(cargoAllocationRef)) {

					final CargoAllocation allocation = (CargoAllocation) eObj.eGet(cargoAllocationRef);

					final List<Object> equivalents = new LinkedList<Object>();
					for (final SlotAllocation slotAllocation : allocation.getSlotAllocations()) {
						equivalents.add(slotAllocation.getSlot());
						equivalents.add(slotAllocation.getSlotVisit());
					}
					equivalents.addAll(allocation.getEvents());
					equivalents.add(allocation.getInputCargo());
					setInputEquivalents(row, equivalents);
				}
			}
		}
	}

	@Override
	protected boolean isElementDifferent(final EObject pinnedObject, final EObject otherObject) {
		return ScheduleDiffUtils.isElementDifferent((EObject)pinnedObject.eGet(cargoAllocationRef), (EObject)otherObject.eGet(cargoAllocationRef));
	}

	@Override
	protected IScenarioInstanceElementCollector getElementCollector() {
		return new ScheduleElementCollector() {

			private EObject dataModelInstance;

			@Override
			public void beginCollecting() {
				super.beginCollecting();
				AbstractCargoReportView.this.clearPinModeData();

				dataModelInstance = GenericEMFTableDataModel.createRootInstance(tableDataModel);
			}

			@Override
			protected Collection<? extends Object> collectElements(final Schedule schedule, final boolean isPinned) {

				final List<CargoAllocation> cargoAllocations = schedule.getCargoAllocations();
				final List<EObject> nodes = CargoAllocationUtils.generateNodes(tableDataModel, dataModelInstance, cargoAllocations);
				AbstractCargoReportView.this.collectPinModeElements(nodes, isPinned);

				return nodes;
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
		
		if (element.eIsSet(cargoAllocationRef)) {
			element = (EObject) element.eGet(cargoAllocationRef);
		}
		
		if (element instanceof CargoAllocation) {
			return ((CargoAllocation) element).getName();
		}
		return super.getElementKey(element);
	}

	@Override
	protected List<?> adaptSelectionFromWidget(List<?> selection) {
		List<Object> adaptedSelection = new ArrayList<Object>(selection.size());
		for (Object obj : selection) {
			if (obj instanceof EObject) {
				adaptedSelection.add(((EObject) obj).eGet(cargoAllocationRef));
			}
		}

		return adaptedSelection;
	}
}
