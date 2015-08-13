/**
 */
package com.mmxlabs.lingo.reports.views.changeset.model.impl;

import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage;
import com.mmxlabs.lingo.reports.views.changeset.model.Metrics;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Metrics</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.MetricsImpl#getPnlDelta <em>Pnl Delta</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.MetricsImpl#getLatenessDelta <em>Lateness Delta</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.impl.MetricsImpl#getCapacityDelta <em>Capacity Delta</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MetricsImpl extends MinimalEObjectImpl.Container implements Metrics {
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
	protected MetricsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ChangesetPackage.Literals.METRICS;
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
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.METRICS__PNL_DELTA, oldPnlDelta, pnlDelta));
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
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.METRICS__LATENESS_DELTA, oldLatenessDelta, latenessDelta));
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
			eNotify(new ENotificationImpl(this, Notification.SET, ChangesetPackage.METRICS__CAPACITY_DELTA, oldCapacityDelta, capacityDelta));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ChangesetPackage.METRICS__PNL_DELTA:
				return getPnlDelta();
			case ChangesetPackage.METRICS__LATENESS_DELTA:
				return getLatenessDelta();
			case ChangesetPackage.METRICS__CAPACITY_DELTA:
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
			case ChangesetPackage.METRICS__PNL_DELTA:
				setPnlDelta((Integer)newValue);
				return;
			case ChangesetPackage.METRICS__LATENESS_DELTA:
				setLatenessDelta((Integer)newValue);
				return;
			case ChangesetPackage.METRICS__CAPACITY_DELTA:
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
			case ChangesetPackage.METRICS__PNL_DELTA:
				setPnlDelta(PNL_DELTA_EDEFAULT);
				return;
			case ChangesetPackage.METRICS__LATENESS_DELTA:
				setLatenessDelta(LATENESS_DELTA_EDEFAULT);
				return;
			case ChangesetPackage.METRICS__CAPACITY_DELTA:
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
			case ChangesetPackage.METRICS__PNL_DELTA:
				return pnlDelta != PNL_DELTA_EDEFAULT;
			case ChangesetPackage.METRICS__LATENESS_DELTA:
				return latenessDelta != LATENESS_DELTA_EDEFAULT;
			case ChangesetPackage.METRICS__CAPACITY_DELTA:
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

} //MetricsImpl
