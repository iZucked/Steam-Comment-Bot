/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.optimiser.impl;
import java.util.Date;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import com.mmxlabs.models.lng.optimiser.OptimisationRange;
import com.mmxlabs.models.lng.optimiser.OptimiserPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Optimisation Range</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.optimiser.impl.OptimisationRangeImpl#getOptimiseAfter <em>Optimise After</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.optimiser.impl.OptimisationRangeImpl#getOptimiseBefore <em>Optimise Before</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class OptimisationRangeImpl extends EObjectImpl implements OptimisationRange {
	/**
	 * The default value of the '{@link #getOptimiseAfter() <em>Optimise After</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOptimiseAfter()
	 * @generated
	 * @ordered
	 */
	protected static final Date OPTIMISE_AFTER_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getOptimiseAfter() <em>Optimise After</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOptimiseAfter()
	 * @generated
	 * @ordered
	 */
	protected Date optimiseAfter = OPTIMISE_AFTER_EDEFAULT;

	/**
	 * This is true if the Optimise After attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean optimiseAfterESet;

	/**
	 * The default value of the '{@link #getOptimiseBefore() <em>Optimise Before</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOptimiseBefore()
	 * @generated
	 * @ordered
	 */
	protected static final Date OPTIMISE_BEFORE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getOptimiseBefore() <em>Optimise Before</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOptimiseBefore()
	 * @generated
	 * @ordered
	 */
	protected Date optimiseBefore = OPTIMISE_BEFORE_EDEFAULT;

	/**
	 * This is true if the Optimise Before attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean optimiseBeforeESet;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected OptimisationRangeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return OptimiserPackage.Literals.OPTIMISATION_RANGE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Date getOptimiseAfter() {
		return optimiseAfter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOptimiseAfter(Date newOptimiseAfter) {
		Date oldOptimiseAfter = optimiseAfter;
		optimiseAfter = newOptimiseAfter;
		boolean oldOptimiseAfterESet = optimiseAfterESet;
		optimiseAfterESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OptimiserPackage.OPTIMISATION_RANGE__OPTIMISE_AFTER, oldOptimiseAfter, optimiseAfter, !oldOptimiseAfterESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetOptimiseAfter() {
		Date oldOptimiseAfter = optimiseAfter;
		boolean oldOptimiseAfterESet = optimiseAfterESet;
		optimiseAfter = OPTIMISE_AFTER_EDEFAULT;
		optimiseAfterESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, OptimiserPackage.OPTIMISATION_RANGE__OPTIMISE_AFTER, oldOptimiseAfter, OPTIMISE_AFTER_EDEFAULT, oldOptimiseAfterESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetOptimiseAfter() {
		return optimiseAfterESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Date getOptimiseBefore() {
		return optimiseBefore;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOptimiseBefore(Date newOptimiseBefore) {
		Date oldOptimiseBefore = optimiseBefore;
		optimiseBefore = newOptimiseBefore;
		boolean oldOptimiseBeforeESet = optimiseBeforeESet;
		optimiseBeforeESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OptimiserPackage.OPTIMISATION_RANGE__OPTIMISE_BEFORE, oldOptimiseBefore, optimiseBefore, !oldOptimiseBeforeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetOptimiseBefore() {
		Date oldOptimiseBefore = optimiseBefore;
		boolean oldOptimiseBeforeESet = optimiseBeforeESet;
		optimiseBefore = OPTIMISE_BEFORE_EDEFAULT;
		optimiseBeforeESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, OptimiserPackage.OPTIMISATION_RANGE__OPTIMISE_BEFORE, oldOptimiseBefore, OPTIMISE_BEFORE_EDEFAULT, oldOptimiseBeforeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetOptimiseBefore() {
		return optimiseBeforeESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case OptimiserPackage.OPTIMISATION_RANGE__OPTIMISE_AFTER:
				return getOptimiseAfter();
			case OptimiserPackage.OPTIMISATION_RANGE__OPTIMISE_BEFORE:
				return getOptimiseBefore();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case OptimiserPackage.OPTIMISATION_RANGE__OPTIMISE_AFTER:
				setOptimiseAfter((Date)newValue);
				return;
			case OptimiserPackage.OPTIMISATION_RANGE__OPTIMISE_BEFORE:
				setOptimiseBefore((Date)newValue);
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
			case OptimiserPackage.OPTIMISATION_RANGE__OPTIMISE_AFTER:
				unsetOptimiseAfter();
				return;
			case OptimiserPackage.OPTIMISATION_RANGE__OPTIMISE_BEFORE:
				unsetOptimiseBefore();
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
			case OptimiserPackage.OPTIMISATION_RANGE__OPTIMISE_AFTER:
				return isSetOptimiseAfter();
			case OptimiserPackage.OPTIMISATION_RANGE__OPTIMISE_BEFORE:
				return isSetOptimiseBefore();
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

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (optimiseAfter: ");
		if (optimiseAfterESet) result.append(optimiseAfter); else result.append("<unset>");
		result.append(", optimiseBefore: ");
		if (optimiseBeforeESet) result.append(optimiseBefore); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} // end of OptimisationRangeImpl

// finish type fixing
