/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo.impl;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.NonShippedCargoSpecification;
import com.mmxlabs.models.lng.cargo.ScheduleSpecification;
import com.mmxlabs.models.lng.cargo.ScheduleSpecificationEvent;
import com.mmxlabs.models.lng.cargo.VesselScheduleSpecification;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Schedule Specification</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.ScheduleSpecificationImpl#getVesselScheduleSpecifications <em>Vessel Schedule Specifications</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.ScheduleSpecificationImpl#getNonShippedCargoSpecifications <em>Non Shipped Cargo Specifications</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.ScheduleSpecificationImpl#getOpenEvents <em>Open Events</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ScheduleSpecificationImpl extends EObjectImpl implements ScheduleSpecification {
	/**
	 * The cached value of the '{@link #getVesselScheduleSpecifications() <em>Vessel Schedule Specifications</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselScheduleSpecifications()
	 * @generated
	 * @ordered
	 */
	protected EList<VesselScheduleSpecification> vesselScheduleSpecifications;

	/**
	 * The cached value of the '{@link #getNonShippedCargoSpecifications() <em>Non Shipped Cargo Specifications</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNonShippedCargoSpecifications()
	 * @generated
	 * @ordered
	 */
	protected EList<NonShippedCargoSpecification> nonShippedCargoSpecifications;

	/**
	 * The cached value of the '{@link #getOpenEvents() <em>Open Events</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOpenEvents()
	 * @generated
	 * @ordered
	 */
	protected EList<ScheduleSpecificationEvent> openEvents;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ScheduleSpecificationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CargoPackage.Literals.SCHEDULE_SPECIFICATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<VesselScheduleSpecification> getVesselScheduleSpecifications() {
		if (vesselScheduleSpecifications == null) {
			vesselScheduleSpecifications = new EObjectContainmentEList.Resolving<VesselScheduleSpecification>(VesselScheduleSpecification.class, this, CargoPackage.SCHEDULE_SPECIFICATION__VESSEL_SCHEDULE_SPECIFICATIONS);
		}
		return vesselScheduleSpecifications;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<NonShippedCargoSpecification> getNonShippedCargoSpecifications() {
		if (nonShippedCargoSpecifications == null) {
			nonShippedCargoSpecifications = new EObjectContainmentEList.Resolving<NonShippedCargoSpecification>(NonShippedCargoSpecification.class, this, CargoPackage.SCHEDULE_SPECIFICATION__NON_SHIPPED_CARGO_SPECIFICATIONS);
		}
		return nonShippedCargoSpecifications;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<ScheduleSpecificationEvent> getOpenEvents() {
		if (openEvents == null) {
			openEvents = new EObjectContainmentEList.Resolving<ScheduleSpecificationEvent>(ScheduleSpecificationEvent.class, this, CargoPackage.SCHEDULE_SPECIFICATION__OPEN_EVENTS);
		}
		return openEvents;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case CargoPackage.SCHEDULE_SPECIFICATION__VESSEL_SCHEDULE_SPECIFICATIONS:
				return ((InternalEList<?>)getVesselScheduleSpecifications()).basicRemove(otherEnd, msgs);
			case CargoPackage.SCHEDULE_SPECIFICATION__NON_SHIPPED_CARGO_SPECIFICATIONS:
				return ((InternalEList<?>)getNonShippedCargoSpecifications()).basicRemove(otherEnd, msgs);
			case CargoPackage.SCHEDULE_SPECIFICATION__OPEN_EVENTS:
				return ((InternalEList<?>)getOpenEvents()).basicRemove(otherEnd, msgs);
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
			case CargoPackage.SCHEDULE_SPECIFICATION__VESSEL_SCHEDULE_SPECIFICATIONS:
				return getVesselScheduleSpecifications();
			case CargoPackage.SCHEDULE_SPECIFICATION__NON_SHIPPED_CARGO_SPECIFICATIONS:
				return getNonShippedCargoSpecifications();
			case CargoPackage.SCHEDULE_SPECIFICATION__OPEN_EVENTS:
				return getOpenEvents();
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
			case CargoPackage.SCHEDULE_SPECIFICATION__VESSEL_SCHEDULE_SPECIFICATIONS:
				getVesselScheduleSpecifications().clear();
				getVesselScheduleSpecifications().addAll((Collection<? extends VesselScheduleSpecification>)newValue);
				return;
			case CargoPackage.SCHEDULE_SPECIFICATION__NON_SHIPPED_CARGO_SPECIFICATIONS:
				getNonShippedCargoSpecifications().clear();
				getNonShippedCargoSpecifications().addAll((Collection<? extends NonShippedCargoSpecification>)newValue);
				return;
			case CargoPackage.SCHEDULE_SPECIFICATION__OPEN_EVENTS:
				getOpenEvents().clear();
				getOpenEvents().addAll((Collection<? extends ScheduleSpecificationEvent>)newValue);
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
			case CargoPackage.SCHEDULE_SPECIFICATION__VESSEL_SCHEDULE_SPECIFICATIONS:
				getVesselScheduleSpecifications().clear();
				return;
			case CargoPackage.SCHEDULE_SPECIFICATION__NON_SHIPPED_CARGO_SPECIFICATIONS:
				getNonShippedCargoSpecifications().clear();
				return;
			case CargoPackage.SCHEDULE_SPECIFICATION__OPEN_EVENTS:
				getOpenEvents().clear();
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
			case CargoPackage.SCHEDULE_SPECIFICATION__VESSEL_SCHEDULE_SPECIFICATIONS:
				return vesselScheduleSpecifications != null && !vesselScheduleSpecifications.isEmpty();
			case CargoPackage.SCHEDULE_SPECIFICATION__NON_SHIPPED_CARGO_SPECIFICATIONS:
				return nonShippedCargoSpecifications != null && !nonShippedCargoSpecifications.isEmpty();
			case CargoPackage.SCHEDULE_SPECIFICATION__OPEN_EVENTS:
				return openEvents != null && !openEvents.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //ScheduleSpecificationImpl
