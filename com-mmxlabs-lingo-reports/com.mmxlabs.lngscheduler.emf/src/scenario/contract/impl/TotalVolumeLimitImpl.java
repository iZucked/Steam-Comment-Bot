/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.contract.impl;

import java.util.Collection;
import java.util.Date;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;

import scenario.contract.ContractPackage;
import scenario.contract.TotalVolumeLimit;
import scenario.port.Port;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Total Volume Limit</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.contract.impl.TotalVolumeLimitImpl#getPorts <em>Ports</em>}</li>
 *   <li>{@link scenario.contract.impl.TotalVolumeLimitImpl#getMaximumVolume <em>Maximum Volume</em>}</li>
 *   <li>{@link scenario.contract.impl.TotalVolumeLimitImpl#getStartDate <em>Start Date</em>}</li>
 *   <li>{@link scenario.contract.impl.TotalVolumeLimitImpl#getDuration <em>Duration</em>}</li>
 *   <li>{@link scenario.contract.impl.TotalVolumeLimitImpl#isRepeating <em>Repeating</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TotalVolumeLimitImpl extends EObjectImpl implements TotalVolumeLimit {
	/**
	 * The cached value of the '{@link #getPorts() <em>Ports</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPorts()
	 * @generated
	 * @ordered
	 */
	protected EList<Port> ports;

	/**
	 * The default value of the '{@link #getMaximumVolume() <em>Maximum Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaximumVolume()
	 * @generated
	 * @ordered
	 */
	protected static final long MAXIMUM_VOLUME_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getMaximumVolume() <em>Maximum Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaximumVolume()
	 * @generated
	 * @ordered
	 */
	protected long maximumVolume = MAXIMUM_VOLUME_EDEFAULT;

	/**
	 * The default value of the '{@link #getStartDate() <em>Start Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartDate()
	 * @generated
	 * @ordered
	 */
	protected static final Date START_DATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getStartDate() <em>Start Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartDate()
	 * @generated
	 * @ordered
	 */
	protected Date startDate = START_DATE_EDEFAULT;

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
	 * The default value of the '{@link #isRepeating() <em>Repeating</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isRepeating()
	 * @generated
	 * @ordered
	 */
	protected static final boolean REPEATING_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isRepeating() <em>Repeating</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isRepeating()
	 * @generated
	 * @ordered
	 */
	protected boolean repeating = REPEATING_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TotalVolumeLimitImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ContractPackage.Literals.TOTAL_VOLUME_LIMIT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Port> getPorts() {
		if (ports == null) {
			ports = new EObjectResolvingEList<Port>(Port.class, this, ContractPackage.TOTAL_VOLUME_LIMIT__PORTS);
		}
		return ports;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public long getMaximumVolume() {
		return maximumVolume;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMaximumVolume(long newMaximumVolume) {
		long oldMaximumVolume = maximumVolume;
		maximumVolume = newMaximumVolume;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ContractPackage.TOTAL_VOLUME_LIMIT__MAXIMUM_VOLUME, oldMaximumVolume, maximumVolume));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStartDate(Date newStartDate) {
		Date oldStartDate = startDate;
		startDate = newStartDate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ContractPackage.TOTAL_VOLUME_LIMIT__START_DATE, oldStartDate, startDate));
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
			eNotify(new ENotificationImpl(this, Notification.SET, ContractPackage.TOTAL_VOLUME_LIMIT__DURATION, oldDuration, duration));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isRepeating() {
		return repeating;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRepeating(boolean newRepeating) {
		boolean oldRepeating = repeating;
		repeating = newRepeating;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ContractPackage.TOTAL_VOLUME_LIMIT__REPEATING, oldRepeating, repeating));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ContractPackage.TOTAL_VOLUME_LIMIT__PORTS:
				return getPorts();
			case ContractPackage.TOTAL_VOLUME_LIMIT__MAXIMUM_VOLUME:
				return getMaximumVolume();
			case ContractPackage.TOTAL_VOLUME_LIMIT__START_DATE:
				return getStartDate();
			case ContractPackage.TOTAL_VOLUME_LIMIT__DURATION:
				return getDuration();
			case ContractPackage.TOTAL_VOLUME_LIMIT__REPEATING:
				return isRepeating();
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
			case ContractPackage.TOTAL_VOLUME_LIMIT__PORTS:
				getPorts().clear();
				getPorts().addAll((Collection<? extends Port>)newValue);
				return;
			case ContractPackage.TOTAL_VOLUME_LIMIT__MAXIMUM_VOLUME:
				setMaximumVolume((Long)newValue);
				return;
			case ContractPackage.TOTAL_VOLUME_LIMIT__START_DATE:
				setStartDate((Date)newValue);
				return;
			case ContractPackage.TOTAL_VOLUME_LIMIT__DURATION:
				setDuration((Integer)newValue);
				return;
			case ContractPackage.TOTAL_VOLUME_LIMIT__REPEATING:
				setRepeating((Boolean)newValue);
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
			case ContractPackage.TOTAL_VOLUME_LIMIT__PORTS:
				getPorts().clear();
				return;
			case ContractPackage.TOTAL_VOLUME_LIMIT__MAXIMUM_VOLUME:
				setMaximumVolume(MAXIMUM_VOLUME_EDEFAULT);
				return;
			case ContractPackage.TOTAL_VOLUME_LIMIT__START_DATE:
				setStartDate(START_DATE_EDEFAULT);
				return;
			case ContractPackage.TOTAL_VOLUME_LIMIT__DURATION:
				setDuration(DURATION_EDEFAULT);
				return;
			case ContractPackage.TOTAL_VOLUME_LIMIT__REPEATING:
				setRepeating(REPEATING_EDEFAULT);
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
			case ContractPackage.TOTAL_VOLUME_LIMIT__PORTS:
				return ports != null && !ports.isEmpty();
			case ContractPackage.TOTAL_VOLUME_LIMIT__MAXIMUM_VOLUME:
				return maximumVolume != MAXIMUM_VOLUME_EDEFAULT;
			case ContractPackage.TOTAL_VOLUME_LIMIT__START_DATE:
				return START_DATE_EDEFAULT == null ? startDate != null : !START_DATE_EDEFAULT.equals(startDate);
			case ContractPackage.TOTAL_VOLUME_LIMIT__DURATION:
				return duration != DURATION_EDEFAULT;
			case ContractPackage.TOTAL_VOLUME_LIMIT__REPEATING:
				return repeating != REPEATING_EDEFAULT;
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
		result.append(" (maximumVolume: ");
		result.append(maximumVolume);
		result.append(", startDate: ");
		result.append(startDate);
		result.append(", duration: ");
		result.append(duration);
		result.append(", repeating: ");
		result.append(repeating);
		result.append(')');
		return result.toString();
	}

} //TotalVolumeLimitImpl
