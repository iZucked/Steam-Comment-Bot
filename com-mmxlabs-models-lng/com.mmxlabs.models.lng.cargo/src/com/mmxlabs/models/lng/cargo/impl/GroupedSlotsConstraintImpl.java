/**
 */
package com.mmxlabs.models.lng.cargo.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.GroupedSlotsConstraint;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.Contract;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Grouped Slots Constraint</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.GroupedSlotsConstraintImpl#getSlots <em>Slots</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.GroupedSlotsConstraintImpl#getMinimumBound <em>Minimum Bound</em>}</li>
 * </ul>
 *
 * @generated
 */
public class GroupedSlotsConstraintImpl<U extends Contract, T extends Slot<U>> extends EObjectImpl implements GroupedSlotsConstraint<U, T> {
	/**
	 * The cached value of the '{@link #getSlots() <em>Slots</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSlots()
	 * @generated
	 * @ordered
	 */
	protected EList<T> slots;

	/**
	 * The default value of the '{@link #getMinimumBound() <em>Minimum Bound</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinimumBound()
	 * @generated
	 * @ordered
	 */
	protected static final int MINIMUM_BOUND_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMinimumBound() <em>Minimum Bound</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinimumBound()
	 * @generated
	 * @ordered
	 */
	protected int minimumBound = MINIMUM_BOUND_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected GroupedSlotsConstraintImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CargoPackage.Literals.GROUPED_SLOTS_CONSTRAINT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<T> getSlots() {
		if (slots == null) {
			slots = new EObjectResolvingEList<T>(Slot.class, this, CargoPackage.GROUPED_SLOTS_CONSTRAINT__SLOTS);
		}
		return slots;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getMinimumBound() {
		return minimumBound;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMinimumBound(int newMinimumBound) {
		int oldMinimumBound = minimumBound;
		minimumBound = newMinimumBound;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.GROUPED_SLOTS_CONSTRAINT__MINIMUM_BOUND, oldMinimumBound, minimumBound));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CargoPackage.GROUPED_SLOTS_CONSTRAINT__SLOTS:
				return getSlots();
			case CargoPackage.GROUPED_SLOTS_CONSTRAINT__MINIMUM_BOUND:
				return getMinimumBound();
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
			case CargoPackage.GROUPED_SLOTS_CONSTRAINT__SLOTS:
				getSlots().clear();
				getSlots().addAll((Collection<? extends T>)newValue);
				return;
			case CargoPackage.GROUPED_SLOTS_CONSTRAINT__MINIMUM_BOUND:
				setMinimumBound((Integer)newValue);
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
			case CargoPackage.GROUPED_SLOTS_CONSTRAINT__SLOTS:
				getSlots().clear();
				return;
			case CargoPackage.GROUPED_SLOTS_CONSTRAINT__MINIMUM_BOUND:
				setMinimumBound(MINIMUM_BOUND_EDEFAULT);
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
			case CargoPackage.GROUPED_SLOTS_CONSTRAINT__SLOTS:
				return slots != null && !slots.isEmpty();
			case CargoPackage.GROUPED_SLOTS_CONSTRAINT__MINIMUM_BOUND:
				return minimumBound != MINIMUM_BOUND_EDEFAULT;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (minimumBound: ");
		result.append(minimumBound);
		result.append(')');
		return result.toString();
	}

} //GroupedSlotsConstraintImpl
