/**
 */
package com.mmxlabs.models.lng.analytics;

import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotAllocation;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Result Container</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.ResultContainer#getCargoAllocation <em>Cargo Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.ResultContainer#getOpenSlotAllocations <em>Open Slot Allocations</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.ResultContainer#getSlotAllocations <em>Slot Allocations</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getResultContainer()
 * @model
 * @generated
 */
public interface ResultContainer extends EObject {
	/**
	 * Returns the value of the '<em><b>Cargo Allocation</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cargo Allocation</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cargo Allocation</em>' containment reference.
	 * @see #setCargoAllocation(CargoAllocation)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getResultContainer_CargoAllocation()
	 * @model containment="true"
	 * @generated
	 */
	CargoAllocation getCargoAllocation();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.ResultContainer#getCargoAllocation <em>Cargo Allocation</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cargo Allocation</em>' containment reference.
	 * @see #getCargoAllocation()
	 * @generated
	 */
	void setCargoAllocation(CargoAllocation value);

	/**
	 * Returns the value of the '<em><b>Open Slot Allocations</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.schedule.OpenSlotAllocation}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Open Slot Allocations</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Open Slot Allocations</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getResultContainer_OpenSlotAllocations()
	 * @model containment="true"
	 * @generated
	 */
	EList<OpenSlotAllocation> getOpenSlotAllocations();

	/**
	 * Returns the value of the '<em><b>Slot Allocations</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.schedule.SlotAllocation}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Slot Allocations</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Slot Allocations</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getResultContainer_SlotAllocations()
	 * @model containment="true"
	 * @generated
	 */
	EList<SlotAllocation> getSlotAllocations();

} // ResultContainer
