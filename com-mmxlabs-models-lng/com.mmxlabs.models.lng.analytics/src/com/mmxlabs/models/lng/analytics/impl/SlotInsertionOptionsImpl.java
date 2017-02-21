/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.SlotInsertionOption;
import com.mmxlabs.models.lng.analytics.SlotInsertionOptions;

import com.mmxlabs.models.lng.cargo.Slot;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Slot Insertion Options</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SlotInsertionOptionsImpl#getSlotsInserted <em>Slots Inserted</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SlotInsertionOptionsImpl#getInsertionOptions <em>Insertion Options</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.SlotInsertionOptionsImpl#getExtraSlots <em>Extra Slots</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SlotInsertionOptionsImpl extends EObjectImpl implements SlotInsertionOptions {
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
	 * The cached value of the '{@link #getInsertionOptions() <em>Insertion Options</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInsertionOptions()
	 * @generated
	 * @ordered
	 */
	protected EList<SlotInsertionOption> insertionOptions;

	/**
	 * The cached value of the '{@link #getExtraSlots() <em>Extra Slots</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExtraSlots()
	 * @generated
	 * @ordered
	 */
	protected EList<Slot> extraSlots;

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
	public EList<SlotInsertionOption> getInsertionOptions() {
		if (insertionOptions == null) {
			insertionOptions = new EObjectContainmentEList<SlotInsertionOption>(SlotInsertionOption.class, this, AnalyticsPackage.SLOT_INSERTION_OPTIONS__INSERTION_OPTIONS);
		}
		return insertionOptions;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Slot> getExtraSlots() {
		if (extraSlots == null) {
			extraSlots = new EObjectContainmentEList<Slot>(Slot.class, this, AnalyticsPackage.SLOT_INSERTION_OPTIONS__EXTRA_SLOTS);
		}
		return extraSlots;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AnalyticsPackage.SLOT_INSERTION_OPTIONS__INSERTION_OPTIONS:
				return ((InternalEList<?>)getInsertionOptions()).basicRemove(otherEnd, msgs);
			case AnalyticsPackage.SLOT_INSERTION_OPTIONS__EXTRA_SLOTS:
				return ((InternalEList<?>)getExtraSlots()).basicRemove(otherEnd, msgs);
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
			case AnalyticsPackage.SLOT_INSERTION_OPTIONS__SLOTS_INSERTED:
				return getSlotsInserted();
			case AnalyticsPackage.SLOT_INSERTION_OPTIONS__INSERTION_OPTIONS:
				return getInsertionOptions();
			case AnalyticsPackage.SLOT_INSERTION_OPTIONS__EXTRA_SLOTS:
				return getExtraSlots();
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
			case AnalyticsPackage.SLOT_INSERTION_OPTIONS__INSERTION_OPTIONS:
				getInsertionOptions().clear();
				getInsertionOptions().addAll((Collection<? extends SlotInsertionOption>)newValue);
				return;
			case AnalyticsPackage.SLOT_INSERTION_OPTIONS__EXTRA_SLOTS:
				getExtraSlots().clear();
				getExtraSlots().addAll((Collection<? extends Slot>)newValue);
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
			case AnalyticsPackage.SLOT_INSERTION_OPTIONS__INSERTION_OPTIONS:
				getInsertionOptions().clear();
				return;
			case AnalyticsPackage.SLOT_INSERTION_OPTIONS__EXTRA_SLOTS:
				getExtraSlots().clear();
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
			case AnalyticsPackage.SLOT_INSERTION_OPTIONS__INSERTION_OPTIONS:
				return insertionOptions != null && !insertionOptions.isEmpty();
			case AnalyticsPackage.SLOT_INSERTION_OPTIONS__EXTRA_SLOTS:
				return extraSlots != null && !extraSlots.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //SlotInsertionOptionsImpl
