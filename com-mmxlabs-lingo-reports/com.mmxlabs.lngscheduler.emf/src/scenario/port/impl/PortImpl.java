/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.port.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import scenario.contract.Contract;
import scenario.impl.NamedObjectImpl;
import scenario.market.Index;
import scenario.port.Port;
import scenario.port.PortPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Port</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.port.impl.PortImpl#getDefaultIndex <em>Default Index</em>}</li>
 *   <li>{@link scenario.port.impl.PortImpl#getTimeZone <em>Time Zone</em>}</li>
 *   <li>{@link scenario.port.impl.PortImpl#getDefaultContract <em>Default Contract</em>}</li>
 *   <li>{@link scenario.port.impl.PortImpl#getRegasEfficiency <em>Regas Efficiency</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class PortImpl extends NamedObjectImpl implements Port {
	/**
	 * The cached value of the '{@link #getDefaultIndex() <em>Default Index</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultIndex()
	 * @generated
	 * @ordered
	 */
	protected Index defaultIndex;

	/**
	 * The default value of the '{@link #getTimeZone() <em>Time Zone</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTimeZone()
	 * @generated
	 * @ordered
	 */
	protected static final String TIME_ZONE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTimeZone() <em>Time Zone</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTimeZone()
	 * @generated
	 * @ordered
	 */
	protected String timeZone = TIME_ZONE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getDefaultContract() <em>Default Contract</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultContract()
	 * @generated
	 * @ordered
	 */
	protected Contract defaultContract;

	/**
	 * The default value of the '{@link #getRegasEfficiency() <em>Regas Efficiency</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRegasEfficiency()
	 * @generated
	 * @ordered
	 */
	protected static final float REGAS_EFFICIENCY_EDEFAULT = 1.0F;

	/**
	 * The cached value of the '{@link #getRegasEfficiency() <em>Regas Efficiency</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRegasEfficiency()
	 * @generated
	 * @ordered
	 */
	protected float regasEfficiency = REGAS_EFFICIENCY_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PortImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PortPackage.Literals.PORT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Index getDefaultIndex() {
		if (defaultIndex != null && defaultIndex.eIsProxy()) {
			InternalEObject oldDefaultIndex = (InternalEObject)defaultIndex;
			defaultIndex = (Index)eResolveProxy(oldDefaultIndex);
			if (defaultIndex != oldDefaultIndex) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, PortPackage.PORT__DEFAULT_INDEX, oldDefaultIndex, defaultIndex));
			}
		}
		return defaultIndex;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Index basicGetDefaultIndex() {
		return defaultIndex;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDefaultIndex(Index newDefaultIndex) {
		Index oldDefaultIndex = defaultIndex;
		defaultIndex = newDefaultIndex;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.PORT__DEFAULT_INDEX, oldDefaultIndex, defaultIndex));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getTimeZone() {
		return timeZone;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setTimeZone(String newTimeZone) {
		String oldTimeZone = timeZone;
		timeZone = newTimeZone;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.PORT__TIME_ZONE, oldTimeZone, timeZone));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Contract getDefaultContract() {
		if (defaultContract != null && defaultContract.eIsProxy()) {
			InternalEObject oldDefaultContract = (InternalEObject)defaultContract;
			defaultContract = (Contract)eResolveProxy(oldDefaultContract);
			if (defaultContract != oldDefaultContract) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, PortPackage.PORT__DEFAULT_CONTRACT, oldDefaultContract, defaultContract));
			}
		}
		return defaultContract;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Contract basicGetDefaultContract() {
		return defaultContract;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDefaultContract(Contract newDefaultContract) {
		Contract oldDefaultContract = defaultContract;
		defaultContract = newDefaultContract;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.PORT__DEFAULT_CONTRACT, oldDefaultContract, defaultContract));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public float getRegasEfficiency() {
		return regasEfficiency;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRegasEfficiency(float newRegasEfficiency) {
		float oldRegasEfficiency = regasEfficiency;
		regasEfficiency = newRegasEfficiency;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.PORT__REGAS_EFFICIENCY, oldRegasEfficiency, regasEfficiency));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case PortPackage.PORT__DEFAULT_INDEX:
				if (resolve) return getDefaultIndex();
				return basicGetDefaultIndex();
			case PortPackage.PORT__TIME_ZONE:
				return getTimeZone();
			case PortPackage.PORT__DEFAULT_CONTRACT:
				if (resolve) return getDefaultContract();
				return basicGetDefaultContract();
			case PortPackage.PORT__REGAS_EFFICIENCY:
				return getRegasEfficiency();
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
			case PortPackage.PORT__DEFAULT_INDEX:
				setDefaultIndex((Index)newValue);
				return;
			case PortPackage.PORT__TIME_ZONE:
				setTimeZone((String)newValue);
				return;
			case PortPackage.PORT__DEFAULT_CONTRACT:
				setDefaultContract((Contract)newValue);
				return;
			case PortPackage.PORT__REGAS_EFFICIENCY:
				setRegasEfficiency((Float)newValue);
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
			case PortPackage.PORT__DEFAULT_INDEX:
				setDefaultIndex((Index)null);
				return;
			case PortPackage.PORT__TIME_ZONE:
				setTimeZone(TIME_ZONE_EDEFAULT);
				return;
			case PortPackage.PORT__DEFAULT_CONTRACT:
				setDefaultContract((Contract)null);
				return;
			case PortPackage.PORT__REGAS_EFFICIENCY:
				setRegasEfficiency(REGAS_EFFICIENCY_EDEFAULT);
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
			case PortPackage.PORT__DEFAULT_INDEX:
				return defaultIndex != null;
			case PortPackage.PORT__TIME_ZONE:
				return TIME_ZONE_EDEFAULT == null ? timeZone != null : !TIME_ZONE_EDEFAULT.equals(timeZone);
			case PortPackage.PORT__DEFAULT_CONTRACT:
				return defaultContract != null;
			case PortPackage.PORT__REGAS_EFFICIENCY:
				return regasEfficiency != REGAS_EFFICIENCY_EDEFAULT;
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
		result.append(" (timeZone: ");
		result.append(timeZone);
		result.append(", regasEfficiency: ");
		result.append(regasEfficiency);
		result.append(')');
		return result.toString();
	}

} //PortImpl
