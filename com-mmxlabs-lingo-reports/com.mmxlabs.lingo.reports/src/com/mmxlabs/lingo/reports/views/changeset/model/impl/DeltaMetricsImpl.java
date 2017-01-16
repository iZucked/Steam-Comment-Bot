/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.lingo.reports.views.changeset.model.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage;
import com.mmxlabs.lingo.reports.views.changeset.model.DeltaMetrics;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Delta Metrics</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.DeltaMetricsImpl#getPnlDelta <em>Pnl Delta</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.DeltaMetricsImpl#getLatenessDelta <em>Lateness Delta</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.DeltaMetricsImpl#getCapacityDelta <em>Capacity Delta</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DeltaMetricsImpl extends MinimalEObjectImpl.Container implements DeltaMetrics {
	/**
	 * The default value of the '{@link #getPnlDelta() <em>Pnl Delta</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPnlDelta()
	 * @generated
	 * @ordered
	 */
	protected static final int PNL_DELTA_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getPnlDelta() <em>Pnl Delta</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPnlDelta()
	 * @generated
	 * @ordered
	 */
	protected int pnlDelta = PNL_DELTA_EDEFAULT;

	/**
	 * The default value of the '{@link #getLatenessDelta() <em>Lateness Delta</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLatenessDelta()
	 * @generated
	 * @ordered
	 */
	protected static final int LATENESS_DELTA_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getLatenessDelta() <em>Lateness Delta</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLatenessDelta()
	 * @generated
	 * @ordered
	 */
	protected int latenessDelta = LATENESS_DELTA_EDEFAULT;

	/**
	 * The default value of the '{@link #getCapacityDelta() <em>Capacity Delta</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCapacityDelta()
	 * @generated
	 * @ordered
	 */
	protected static final int CAPACITY_DELTA_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getCapacityDelta() <em>Capacity Delta</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCapacityDelta()
	 * @generated
	 * @ordered
	 */
	protected int capacityDelta = CAPACITY_DELTA_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DeltaMetricsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ChangesetPackage.Literals.DELTA_METRICS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getPnlDelta() {
		return pnlDelta;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPnlDelta(int newPnlDelta) {
		int oldPnlDelta = pnlDelta;
		pnlDelta = newPnlDelta;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.DELTA_METRICS__PNL_DELTA, oldPnlDelta, pnlDelta));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getLatenessDelta() {
		return latenessDelta;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLatenessDelta(int newLatenessDelta) {
		int oldLatenessDelta = latenessDelta;
		latenessDelta = newLatenessDelta;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.DELTA_METRICS__LATENESS_DELTA, oldLatenessDelta, latenessDelta));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getCapacityDelta() {
		return capacityDelta;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCapacityDelta(int newCapacityDelta) {
		int oldCapacityDelta = capacityDelta;
		capacityDelta = newCapacityDelta;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.DELTA_METRICS__CAPACITY_DELTA, oldCapacityDelta, capacityDelta));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ChangesetPackage.DELTA_METRICS__PNL_DELTA:
				return getPnlDelta();
			case ChangesetPackage.DELTA_METRICS__LATENESS_DELTA:
				return getLatenessDelta();
			case ChangesetPackage.DELTA_METRICS__CAPACITY_DELTA:
				return getCapacityDelta();
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
			case ChangesetPackage.DELTA_METRICS__PNL_DELTA:
				setPnlDelta((Integer)newValue);
				return;
			case ChangesetPackage.DELTA_METRICS__LATENESS_DELTA:
				setLatenessDelta((Integer)newValue);
				return;
			case ChangesetPackage.DELTA_METRICS__CAPACITY_DELTA:
				setCapacityDelta((Integer)newValue);
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
			case ChangesetPackage.DELTA_METRICS__PNL_DELTA:
				setPnlDelta(PNL_DELTA_EDEFAULT);
				return;
			case ChangesetPackage.DELTA_METRICS__LATENESS_DELTA:
				setLatenessDelta(LATENESS_DELTA_EDEFAULT);
				return;
			case ChangesetPackage.DELTA_METRICS__CAPACITY_DELTA:
				setCapacityDelta(CAPACITY_DELTA_EDEFAULT);
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
			case ChangesetPackage.DELTA_METRICS__PNL_DELTA:
				return pnlDelta != PNL_DELTA_EDEFAULT;
			case ChangesetPackage.DELTA_METRICS__LATENESS_DELTA:
				return latenessDelta != LATENESS_DELTA_EDEFAULT;
			case ChangesetPackage.DELTA_METRICS__CAPACITY_DELTA:
				return capacityDelta != CAPACITY_DELTA_EDEFAULT;
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
		result.append(" (pnlDelta: ");
		result.append(pnlDelta);
		result.append(", latenessDelta: ");
		result.append(latenessDelta);
		result.append(", capacityDelta: ");
		result.append(capacityDelta);
		result.append(')');
		return result.toString();
	}

} //DeltaMetricsImpl
