/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.SlotInsertionOptions;

import com.mmxlabs.models.lng.cargo.Slot;

import com.mmxlabs.models.lng.cargo.VesselEvent;
import java.util.Collection;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Slot Insertion Options</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SlotInsertionOptionsImpl#getSlotsInserted <em>Slots Inserted</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SlotInsertionOptionsImpl#getEventsInserted <em>Events Inserted</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SlotInsertionOptionsImpl extends AbstractSolutionSetImpl implements SlotInsertionOptions {
	/**
	 * The cached value of the '{@link #getSlotsInserted() <em>Slots Inserted</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSlotsInserted()
	 * @generated
	 * @ordered
	 */
	protected EList<Slot> slotsInserted;

	/**
	 * The cached value of the '{@link #getEventsInserted() <em>Events Inserted</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEventsInserted()
	 * @generated
	 * @ordered
	 */
	protected EList<VesselEvent> eventsInserted;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SlotInsertionOptionsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.SLOT_INSERTION_OPTIONS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Slot> getSlotsInserted() {
		if (slotsInserted == null) {
			slotsInserted = new EObjectResolvingEList<Slot>(Slot.class, this, AnalyticsPackage.SLOT_INSERTION_OPTIONS__SLOTS_INSERTED);
		}
		return slotsInserted;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<VesselEvent> getEventsInserted() {
		if (eventsInserted == null) {
			eventsInserted = new EObjectResolvingEList<VesselEvent>(VesselEvent.class, this, AnalyticsPackage.SLOT_INSERTION_OPTIONS__EVENTS_INSERTED);
		}
		return eventsInserted;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case AnalyticsPackage.SLOT_INSERTION_OPTIONS__SLOTS_INSERTED:
				return getSlotsInserted();
			case AnalyticsPackage.SLOT_INSERTION_OPTIONS__EVENTS_INSERTED:
				return getEventsInserted();
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
			case AnalyticsPackage.SLOT_INSERTION_OPTIONS__SLOTS_INSERTED:
				getSlotsInserted().clear();
				getSlotsInserted().addAll((Collection<? extends Slot>)newValue);
				return;
			case AnalyticsPackage.SLOT_INSERTION_OPTIONS__EVENTS_INSERTED:
				getEventsInserted().clear();
				getEventsInserted().addAll((Collection<? extends VesselEvent>)newValue);
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
			case AnalyticsPackage.SLOT_INSERTION_OPTIONS__SLOTS_INSERTED:
				getSlotsInserted().clear();
				return;
			case AnalyticsPackage.SLOT_INSERTION_OPTIONS__EVENTS_INSERTED:
				getEventsInserted().clear();
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
			case AnalyticsPackage.SLOT_INSERTION_OPTIONS__SLOTS_INSERTED:
				return slotsInserted != null && !slotsInserted.isEmpty();
			case AnalyticsPackage.SLOT_INSERTION_OPTIONS__EVENTS_INSERTED:
				return eventsInserted != null && !eventsInserted.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //SlotInsertionOptionsImpl
