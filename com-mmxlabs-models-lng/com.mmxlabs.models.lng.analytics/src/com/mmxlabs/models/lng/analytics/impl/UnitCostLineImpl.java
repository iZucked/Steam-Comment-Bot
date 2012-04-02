

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.analytics.impl;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.UnitCostLine;

import com.mmxlabs.models.lng.port.Port;

import com.mmxlabs.models.mmxcore.impl.MMXObjectImpl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Unit Cost Line</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.UnitCostLineImpl#getUnitCost <em>Unit Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.UnitCostLineImpl#getTotalCost <em>Total Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.UnitCostLineImpl#getMmbtuDelivered <em>Mmbtu Delivered</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.UnitCostLineImpl#getFrom <em>From</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.UnitCostLineImpl#getTo <em>To</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.UnitCostLineImpl#getDuration <em>Duration</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.UnitCostLineImpl#getVolumeLoaded <em>Volume Loaded</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.UnitCostLineImpl#getVolumeDischarged <em>Volume Discharged</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class UnitCostLineImpl extends MMXObjectImpl implements UnitCostLine {
	/**
	 * The default value of the '{@link #getUnitCost() <em>Unit Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUnitCost()
	 * @generated
	 * @ordered
	 */
	protected static final double UNIT_COST_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getUnitCost() <em>Unit Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUnitCost()
	 * @generated
	 * @ordered
	 */
	protected double unitCost = UNIT_COST_EDEFAULT;

	/**
	 * The default value of the '{@link #getTotalCost() <em>Total Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTotalCost()
	 * @generated
	 * @ordered
	 */
	protected static final int TOTAL_COST_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getTotalCost() <em>Total Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTotalCost()
	 * @generated
	 * @ordered
	 */
	protected int totalCost = TOTAL_COST_EDEFAULT;

	/**
	 * The default value of the '{@link #getMmbtuDelivered() <em>Mmbtu Delivered</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMmbtuDelivered()
	 * @generated
	 * @ordered
	 */
	protected static final int MMBTU_DELIVERED_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMmbtuDelivered() <em>Mmbtu Delivered</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMmbtuDelivered()
	 * @generated
	 * @ordered
	 */
	protected int mmbtuDelivered = MMBTU_DELIVERED_EDEFAULT;

	/**
	 * The cached value of the '{@link #getFrom() <em>From</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFrom()
	 * @generated
	 * @ordered
	 */
	protected Port from;

	/**
	 * The cached value of the '{@link #getTo() <em>To</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTo()
	 * @generated
	 * @ordered
	 */
	protected Port to;

	/**
	 * The default value of the '{@link #getDuration() <em>Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDuration()
	 * @generated
	 * @ordered
	 */
	protected static final int DURATION_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getDuration() <em>Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDuration()
	 * @generated
	 * @ordered
	 */
	protected int duration = DURATION_EDEFAULT;

	/**
	 * The default value of the '{@link #getVolumeLoaded() <em>Volume Loaded</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolumeLoaded()
	 * @generated
	 * @ordered
	 */
	protected static final int VOLUME_LOADED_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getVolumeLoaded() <em>Volume Loaded</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolumeLoaded()
	 * @generated
	 * @ordered
	 */
	protected int volumeLoaded = VOLUME_LOADED_EDEFAULT;

	/**
	 * The default value of the '{@link #getVolumeDischarged() <em>Volume Discharged</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolumeDischarged()
	 * @generated
	 * @ordered
	 */
	protected static final int VOLUME_DISCHARGED_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getVolumeDischarged() <em>Volume Discharged</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolumeDischarged()
	 * @generated
	 * @ordered
	 */
	protected int volumeDischarged = VOLUME_DISCHARGED_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected UnitCostLineImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.UNIT_COST_LINE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getUnitCost() {
		return unitCost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUnitCost(double newUnitCost) {
		double oldUnitCost = unitCost;
		unitCost = newUnitCost;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.UNIT_COST_LINE__UNIT_COST, oldUnitCost, unitCost));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getTotalCost() {
		return totalCost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTotalCost(int newTotalCost) {
		int oldTotalCost = totalCost;
		totalCost = newTotalCost;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.UNIT_COST_LINE__TOTAL_COST, oldTotalCost, totalCost));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getMmbtuDelivered() {
		return mmbtuDelivered;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMmbtuDelivered(int newMmbtuDelivered) {
		int oldMmbtuDelivered = mmbtuDelivered;
		mmbtuDelivered = newMmbtuDelivered;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.UNIT_COST_LINE__MMBTU_DELIVERED, oldMmbtuDelivered, mmbtuDelivered));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port getFrom() {
		if (from != null && from.eIsProxy()) {
			InternalEObject oldFrom = (InternalEObject)from;
			from = (Port)eResolveProxy(oldFrom);
			if (from != oldFrom) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalyticsPackage.UNIT_COST_LINE__FROM, oldFrom, from));
			}
		}
		return from;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port basicGetFrom() {
		return from;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFrom(Port newFrom) {
		Port oldFrom = from;
		from = newFrom;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.UNIT_COST_LINE__FROM, oldFrom, from));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port getTo() {
		if (to != null && to.eIsProxy()) {
			InternalEObject oldTo = (InternalEObject)to;
			to = (Port)eResolveProxy(oldTo);
			if (to != oldTo) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalyticsPackage.UNIT_COST_LINE__TO, oldTo, to));
			}
		}
		return to;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port basicGetTo() {
		return to;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTo(Port newTo) {
		Port oldTo = to;
		to = newTo;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.UNIT_COST_LINE__TO, oldTo, to));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDuration(int newDuration) {
		int oldDuration = duration;
		duration = newDuration;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.UNIT_COST_LINE__DURATION, oldDuration, duration));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getVolumeLoaded() {
		return volumeLoaded;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVolumeLoaded(int newVolumeLoaded) {
		int oldVolumeLoaded = volumeLoaded;
		volumeLoaded = newVolumeLoaded;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.UNIT_COST_LINE__VOLUME_LOADED, oldVolumeLoaded, volumeLoaded));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getVolumeDischarged() {
		return volumeDischarged;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVolumeDischarged(int newVolumeDischarged) {
		int oldVolumeDischarged = volumeDischarged;
		volumeDischarged = newVolumeDischarged;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.UNIT_COST_LINE__VOLUME_DISCHARGED, oldVolumeDischarged, volumeDischarged));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case AnalyticsPackage.UNIT_COST_LINE__UNIT_COST:
				return getUnitCost();
			case AnalyticsPackage.UNIT_COST_LINE__TOTAL_COST:
				return getTotalCost();
			case AnalyticsPackage.UNIT_COST_LINE__MMBTU_DELIVERED:
				return getMmbtuDelivered();
			case AnalyticsPackage.UNIT_COST_LINE__FROM:
				if (resolve) return getFrom();
				return basicGetFrom();
			case AnalyticsPackage.UNIT_COST_LINE__TO:
				if (resolve) return getTo();
				return basicGetTo();
			case AnalyticsPackage.UNIT_COST_LINE__DURATION:
				return getDuration();
			case AnalyticsPackage.UNIT_COST_LINE__VOLUME_LOADED:
				return getVolumeLoaded();
			case AnalyticsPackage.UNIT_COST_LINE__VOLUME_DISCHARGED:
				return getVolumeDischarged();
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
			case AnalyticsPackage.UNIT_COST_LINE__UNIT_COST:
				setUnitCost((Double)newValue);
				return;
			case AnalyticsPackage.UNIT_COST_LINE__TOTAL_COST:
				setTotalCost((Integer)newValue);
				return;
			case AnalyticsPackage.UNIT_COST_LINE__MMBTU_DELIVERED:
				setMmbtuDelivered((Integer)newValue);
				return;
			case AnalyticsPackage.UNIT_COST_LINE__FROM:
				setFrom((Port)newValue);
				return;
			case AnalyticsPackage.UNIT_COST_LINE__TO:
				setTo((Port)newValue);
				return;
			case AnalyticsPackage.UNIT_COST_LINE__DURATION:
				setDuration((Integer)newValue);
				return;
			case AnalyticsPackage.UNIT_COST_LINE__VOLUME_LOADED:
				setVolumeLoaded((Integer)newValue);
				return;
			case AnalyticsPackage.UNIT_COST_LINE__VOLUME_DISCHARGED:
				setVolumeDischarged((Integer)newValue);
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
			case AnalyticsPackage.UNIT_COST_LINE__UNIT_COST:
				setUnitCost(UNIT_COST_EDEFAULT);
				return;
			case AnalyticsPackage.UNIT_COST_LINE__TOTAL_COST:
				setTotalCost(TOTAL_COST_EDEFAULT);
				return;
			case AnalyticsPackage.UNIT_COST_LINE__MMBTU_DELIVERED:
				setMmbtuDelivered(MMBTU_DELIVERED_EDEFAULT);
				return;
			case AnalyticsPackage.UNIT_COST_LINE__FROM:
				setFrom((Port)null);
				return;
			case AnalyticsPackage.UNIT_COST_LINE__TO:
				setTo((Port)null);
				return;
			case AnalyticsPackage.UNIT_COST_LINE__DURATION:
				setDuration(DURATION_EDEFAULT);
				return;
			case AnalyticsPackage.UNIT_COST_LINE__VOLUME_LOADED:
				setVolumeLoaded(VOLUME_LOADED_EDEFAULT);
				return;
			case AnalyticsPackage.UNIT_COST_LINE__VOLUME_DISCHARGED:
				setVolumeDischarged(VOLUME_DISCHARGED_EDEFAULT);
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
			case AnalyticsPackage.UNIT_COST_LINE__UNIT_COST:
				return unitCost != UNIT_COST_EDEFAULT;
			case AnalyticsPackage.UNIT_COST_LINE__TOTAL_COST:
				return totalCost != TOTAL_COST_EDEFAULT;
			case AnalyticsPackage.UNIT_COST_LINE__MMBTU_DELIVERED:
				return mmbtuDelivered != MMBTU_DELIVERED_EDEFAULT;
			case AnalyticsPackage.UNIT_COST_LINE__FROM:
				return from != null;
			case AnalyticsPackage.UNIT_COST_LINE__TO:
				return to != null;
			case AnalyticsPackage.UNIT_COST_LINE__DURATION:
				return duration != DURATION_EDEFAULT;
			case AnalyticsPackage.UNIT_COST_LINE__VOLUME_LOADED:
				return volumeLoaded != VOLUME_LOADED_EDEFAULT;
			case AnalyticsPackage.UNIT_COST_LINE__VOLUME_DISCHARGED:
				return volumeDischarged != VOLUME_DISCHARGED_EDEFAULT;
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
		result.append(" (unitCost: ");
		result.append(unitCost);
		result.append(", totalCost: ");
		result.append(totalCost);
		result.append(", mmbtuDelivered: ");
		result.append(mmbtuDelivered);
		result.append(", duration: ");
		result.append(duration);
		result.append(", volumeLoaded: ");
		result.append(volumeLoaded);
		result.append(", volumeDischarged: ");
		result.append(volumeDischarged);
		result.append(')');
		return result.toString();
	}

} // end of UnitCostLineImpl

// finish type fixing
