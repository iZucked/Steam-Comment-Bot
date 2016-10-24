/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.ResultContainer;

import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotAllocation;

import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Result Container</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.ResultContainerImpl#getCargoAllocation <em>Cargo Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.ResultContainerImpl#getOpenSlotAllocations <em>Open Slot Allocations</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.ResultContainerImpl#getSlotAllocations <em>Slot Allocations</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ResultContainerImpl extends EObjectImpl implements ResultContainer {
	/**
	 * The cached value of the '{@link #getCargoAllocation() <em>Cargo Allocation</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCargoAllocation()
	 * @generated
	 * @ordered
	 */
	protected CargoAllocation cargoAllocation;

	/**
	 * The cached value of the '{@link #getOpenSlotAllocations() <em>Open Slot Allocations</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOpenSlotAllocations()
	 * @generated
	 * @ordered
	 */
	protected EList<OpenSlotAllocation> openSlotAllocations;

	/**
	 * The cached value of the '{@link #getSlotAllocations() <em>Slot Allocations</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSlotAllocations()
	 * @generated
	 * @ordered
	 */
	protected EList<SlotAllocation> slotAllocations;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ResultContainerImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.RESULT_CONTAINER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CargoAllocation getCargoAllocation() {
		return cargoAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetCargoAllocation(CargoAllocation newCargoAllocation, NotificationChain msgs) {
		CargoAllocation oldCargoAllocation = cargoAllocation;
		cargoAllocation = newCargoAllocation;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AnalyticsPackage.RESULT_CONTAINER__CARGO_ALLOCATION, oldCargoAllocation, newCargoAllocation);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCargoAllocation(CargoAllocation newCargoAllocation) {
		if (newCargoAllocation != cargoAllocation) {
			NotificationChain msgs = null;
			if (cargoAllocation != null)
				msgs = ((InternalEObject)cargoAllocation).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.RESULT_CONTAINER__CARGO_ALLOCATION, null, msgs);
			if (newCargoAllocation != null)
				msgs = ((InternalEObject)newCargoAllocation).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.RESULT_CONTAINER__CARGO_ALLOCATION, null, msgs);
			msgs = basicSetCargoAllocation(newCargoAllocation, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.RESULT_CONTAINER__CARGO_ALLOCATION, newCargoAllocation, newCargoAllocation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<OpenSlotAllocation> getOpenSlotAllocations() {
		if (openSlotAllocations == null) {
			openSlotAllocations = new EObjectContainmentEList<OpenSlotAllocation>(OpenSlotAllocation.class, this, AnalyticsPackage.RESULT_CONTAINER__OPEN_SLOT_ALLOCATIONS);
		}
		return openSlotAllocations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<SlotAllocation> getSlotAllocations() {
		if (slotAllocations == null) {
			slotAllocations = new EObjectContainmentEList<SlotAllocation>(SlotAllocation.class, this, AnalyticsPackage.RESULT_CONTAINER__SLOT_ALLOCATIONS);
		}
		return slotAllocations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AnalyticsPackage.RESULT_CONTAINER__CARGO_ALLOCATION:
				return basicSetCargoAllocation(null, msgs);
			case AnalyticsPackage.RESULT_CONTAINER__OPEN_SLOT_ALLOCATIONS:
				return ((InternalEList<?>)getOpenSlotAllocations()).basicRemove(otherEnd, msgs);
			case AnalyticsPackage.RESULT_CONTAINER__SLOT_ALLOCATIONS:
				return ((InternalEList<?>)getSlotAllocations()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case AnalyticsPackage.RESULT_CONTAINER__CARGO_ALLOCATION:
				return getCargoAllocation();
			case AnalyticsPackage.RESULT_CONTAINER__OPEN_SLOT_ALLOCATIONS:
				return getOpenSlotAllocations();
			case AnalyticsPackage.RESULT_CONTAINER__SLOT_ALLOCATIONS:
				return getSlotAllocations();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case AnalyticsPackage.RESULT_CONTAINER__CARGO_ALLOCATION:
				setCargoAllocation((CargoAllocation)newValue);
				return;
			case AnalyticsPackage.RESULT_CONTAINER__OPEN_SLOT_ALLOCATIONS:
				getOpenSlotAllocations().clear();
				getOpenSlotAllocations().addAll((Collection<? extends OpenSlotAllocation>)newValue);
				return;
			case AnalyticsPackage.RESULT_CONTAINER__SLOT_ALLOCATIONS:
				getSlotAllocations().clear();
				getSlotAllocations().addAll((Collection<? extends SlotAllocation>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case AnalyticsPackage.RESULT_CONTAINER__CARGO_ALLOCATION:
				setCargoAllocation((CargoAllocation)null);
				return;
			case AnalyticsPackage.RESULT_CONTAINER__OPEN_SLOT_ALLOCATIONS:
				getOpenSlotAllocations().clear();
				return;
			case AnalyticsPackage.RESULT_CONTAINER__SLOT_ALLOCATIONS:
				getSlotAllocations().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case AnalyticsPackage.RESULT_CONTAINER__CARGO_ALLOCATION:
				return cargoAllocation != null;
			case AnalyticsPackage.RESULT_CONTAINER__OPEN_SLOT_ALLOCATIONS:
				return openSlotAllocations != null && !openSlotAllocations.isEmpty();
			case AnalyticsPackage.RESULT_CONTAINER__SLOT_ALLOCATIONS:
				return slotAllocations != null && !slotAllocations.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //ResultContainerImpl
