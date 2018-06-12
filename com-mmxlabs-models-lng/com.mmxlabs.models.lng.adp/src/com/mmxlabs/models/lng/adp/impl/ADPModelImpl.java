/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp.impl;

import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.ADPModelResult;
import com.mmxlabs.models.lng.adp.ADPPackage;

import com.mmxlabs.models.lng.adp.FleetProfile;
import com.mmxlabs.models.lng.adp.PurchaseContractProfile;
import com.mmxlabs.models.lng.adp.SalesContractProfile;
import com.mmxlabs.models.lng.adp.SpotMarketsProfile;
import java.time.YearMonth;

import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.ADPModelImpl#getYearStart <em>Year Start</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.ADPModelImpl#getYearEnd <em>Year End</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.ADPModelImpl#getPurchaseContractProfiles <em>Purchase Contract Profiles</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.ADPModelImpl#getSalesContractProfiles <em>Sales Contract Profiles</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.ADPModelImpl#getSpotMarketsProfile <em>Spot Markets Profile</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.ADPModelImpl#getFleetProfile <em>Fleet Profile</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.ADPModelImpl#getResult <em>Result</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ADPModelImpl extends EObjectImpl implements ADPModel {
	/**
	 * The default value of the '{@link #getYearStart() <em>Year Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getYearStart()
	 * @generated
	 * @ordered
	 */
	protected static final YearMonth YEAR_START_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getYearStart() <em>Year Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getYearStart()
	 * @generated
	 * @ordered
	 */
	protected YearMonth yearStart = YEAR_START_EDEFAULT;

	/**
	 * The default value of the '{@link #getYearEnd() <em>Year End</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getYearEnd()
	 * @generated
	 * @ordered
	 */
	protected static final YearMonth YEAR_END_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getYearEnd() <em>Year End</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getYearEnd()
	 * @generated
	 * @ordered
	 */
	protected YearMonth yearEnd = YEAR_END_EDEFAULT;

	/**
	 * The cached value of the '{@link #getPurchaseContractProfiles() <em>Purchase Contract Profiles</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPurchaseContractProfiles()
	 * @generated
	 * @ordered
	 */
	protected EList<PurchaseContractProfile> purchaseContractProfiles;

	/**
	 * The cached value of the '{@link #getSalesContractProfiles() <em>Sales Contract Profiles</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSalesContractProfiles()
	 * @generated
	 * @ordered
	 */
	protected EList<SalesContractProfile> salesContractProfiles;

	/**
	 * The cached value of the '{@link #getSpotMarketsProfile() <em>Spot Markets Profile</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSpotMarketsProfile()
	 * @generated
	 * @ordered
	 */
	protected SpotMarketsProfile spotMarketsProfile;

	/**
	 * The cached value of the '{@link #getFleetProfile() <em>Fleet Profile</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFleetProfile()
	 * @generated
	 * @ordered
	 */
	protected FleetProfile fleetProfile;

	/**
	 * The cached value of the '{@link #getResult() <em>Result</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResult()
	 * @generated
	 * @ordered
	 */
	protected ADPModelResult result;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ADPModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ADPPackage.Literals.ADP_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public YearMonth getYearStart() {
		return yearStart;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setYearStart(YearMonth newYearStart) {
		YearMonth oldYearStart = yearStart;
		yearStart = newYearStart;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.ADP_MODEL__YEAR_START, oldYearStart, yearStart));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public YearMonth getYearEnd() {
		return yearEnd;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setYearEnd(YearMonth newYearEnd) {
		YearMonth oldYearEnd = yearEnd;
		yearEnd = newYearEnd;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.ADP_MODEL__YEAR_END, oldYearEnd, yearEnd));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<PurchaseContractProfile> getPurchaseContractProfiles() {
		if (purchaseContractProfiles == null) {
			purchaseContractProfiles = new EObjectContainmentEList.Resolving<PurchaseContractProfile>(PurchaseContractProfile.class, this, ADPPackage.ADP_MODEL__PURCHASE_CONTRACT_PROFILES);
		}
		return purchaseContractProfiles;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<SalesContractProfile> getSalesContractProfiles() {
		if (salesContractProfiles == null) {
			salesContractProfiles = new EObjectContainmentEList.Resolving<SalesContractProfile>(SalesContractProfile.class, this, ADPPackage.ADP_MODEL__SALES_CONTRACT_PROFILES);
		}
		return salesContractProfiles;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SpotMarketsProfile getSpotMarketsProfile() {
		if (spotMarketsProfile != null && spotMarketsProfile.eIsProxy()) {
			InternalEObject oldSpotMarketsProfile = (InternalEObject)spotMarketsProfile;
			spotMarketsProfile = (SpotMarketsProfile)eResolveProxy(oldSpotMarketsProfile);
			if (spotMarketsProfile != oldSpotMarketsProfile) {
				InternalEObject newSpotMarketsProfile = (InternalEObject)spotMarketsProfile;
				NotificationChain msgs = oldSpotMarketsProfile.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ADPPackage.ADP_MODEL__SPOT_MARKETS_PROFILE, null, null);
				if (newSpotMarketsProfile.eInternalContainer() == null) {
					msgs = newSpotMarketsProfile.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ADPPackage.ADP_MODEL__SPOT_MARKETS_PROFILE, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ADPPackage.ADP_MODEL__SPOT_MARKETS_PROFILE, oldSpotMarketsProfile, spotMarketsProfile));
			}
		}
		return spotMarketsProfile;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SpotMarketsProfile basicGetSpotMarketsProfile() {
		return spotMarketsProfile;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSpotMarketsProfile(SpotMarketsProfile newSpotMarketsProfile, NotificationChain msgs) {
		SpotMarketsProfile oldSpotMarketsProfile = spotMarketsProfile;
		spotMarketsProfile = newSpotMarketsProfile;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ADPPackage.ADP_MODEL__SPOT_MARKETS_PROFILE, oldSpotMarketsProfile, newSpotMarketsProfile);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSpotMarketsProfile(SpotMarketsProfile newSpotMarketsProfile) {
		if (newSpotMarketsProfile != spotMarketsProfile) {
			NotificationChain msgs = null;
			if (spotMarketsProfile != null)
				msgs = ((InternalEObject)spotMarketsProfile).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ADPPackage.ADP_MODEL__SPOT_MARKETS_PROFILE, null, msgs);
			if (newSpotMarketsProfile != null)
				msgs = ((InternalEObject)newSpotMarketsProfile).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ADPPackage.ADP_MODEL__SPOT_MARKETS_PROFILE, null, msgs);
			msgs = basicSetSpotMarketsProfile(newSpotMarketsProfile, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.ADP_MODEL__SPOT_MARKETS_PROFILE, newSpotMarketsProfile, newSpotMarketsProfile));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FleetProfile getFleetProfile() {
		if (fleetProfile != null && fleetProfile.eIsProxy()) {
			InternalEObject oldFleetProfile = (InternalEObject)fleetProfile;
			fleetProfile = (FleetProfile)eResolveProxy(oldFleetProfile);
			if (fleetProfile != oldFleetProfile) {
				InternalEObject newFleetProfile = (InternalEObject)fleetProfile;
				NotificationChain msgs = oldFleetProfile.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ADPPackage.ADP_MODEL__FLEET_PROFILE, null, null);
				if (newFleetProfile.eInternalContainer() == null) {
					msgs = newFleetProfile.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ADPPackage.ADP_MODEL__FLEET_PROFILE, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ADPPackage.ADP_MODEL__FLEET_PROFILE, oldFleetProfile, fleetProfile));
			}
		}
		return fleetProfile;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FleetProfile basicGetFleetProfile() {
		return fleetProfile;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetFleetProfile(FleetProfile newFleetProfile, NotificationChain msgs) {
		FleetProfile oldFleetProfile = fleetProfile;
		fleetProfile = newFleetProfile;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ADPPackage.ADP_MODEL__FLEET_PROFILE, oldFleetProfile, newFleetProfile);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFleetProfile(FleetProfile newFleetProfile) {
		if (newFleetProfile != fleetProfile) {
			NotificationChain msgs = null;
			if (fleetProfile != null)
				msgs = ((InternalEObject)fleetProfile).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ADPPackage.ADP_MODEL__FLEET_PROFILE, null, msgs);
			if (newFleetProfile != null)
				msgs = ((InternalEObject)newFleetProfile).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ADPPackage.ADP_MODEL__FLEET_PROFILE, null, msgs);
			msgs = basicSetFleetProfile(newFleetProfile, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.ADP_MODEL__FLEET_PROFILE, newFleetProfile, newFleetProfile));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ADPModelResult getResult() {
		if (result != null && result.eIsProxy()) {
			InternalEObject oldResult = (InternalEObject)result;
			result = (ADPModelResult)eResolveProxy(oldResult);
			if (result != oldResult) {
				InternalEObject newResult = (InternalEObject)result;
				NotificationChain msgs = oldResult.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ADPPackage.ADP_MODEL__RESULT, null, null);
				if (newResult.eInternalContainer() == null) {
					msgs = newResult.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ADPPackage.ADP_MODEL__RESULT, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ADPPackage.ADP_MODEL__RESULT, oldResult, result));
			}
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ADPModelResult basicGetResult() {
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetResult(ADPModelResult newResult, NotificationChain msgs) {
		ADPModelResult oldResult = result;
		result = newResult;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ADPPackage.ADP_MODEL__RESULT, oldResult, newResult);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setResult(ADPModelResult newResult) {
		if (newResult != result) {
			NotificationChain msgs = null;
			if (result != null)
				msgs = ((InternalEObject)result).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ADPPackage.ADP_MODEL__RESULT, null, msgs);
			if (newResult != null)
				msgs = ((InternalEObject)newResult).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ADPPackage.ADP_MODEL__RESULT, null, msgs);
			msgs = basicSetResult(newResult, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.ADP_MODEL__RESULT, newResult, newResult));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ADPPackage.ADP_MODEL__PURCHASE_CONTRACT_PROFILES:
				return ((InternalEList<?>)getPurchaseContractProfiles()).basicRemove(otherEnd, msgs);
			case ADPPackage.ADP_MODEL__SALES_CONTRACT_PROFILES:
				return ((InternalEList<?>)getSalesContractProfiles()).basicRemove(otherEnd, msgs);
			case ADPPackage.ADP_MODEL__SPOT_MARKETS_PROFILE:
				return basicSetSpotMarketsProfile(null, msgs);
			case ADPPackage.ADP_MODEL__FLEET_PROFILE:
				return basicSetFleetProfile(null, msgs);
			case ADPPackage.ADP_MODEL__RESULT:
				return basicSetResult(null, msgs);
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
			case ADPPackage.ADP_MODEL__YEAR_START:
				return getYearStart();
			case ADPPackage.ADP_MODEL__YEAR_END:
				return getYearEnd();
			case ADPPackage.ADP_MODEL__PURCHASE_CONTRACT_PROFILES:
				return getPurchaseContractProfiles();
			case ADPPackage.ADP_MODEL__SALES_CONTRACT_PROFILES:
				return getSalesContractProfiles();
			case ADPPackage.ADP_MODEL__SPOT_MARKETS_PROFILE:
				if (resolve) return getSpotMarketsProfile();
				return basicGetSpotMarketsProfile();
			case ADPPackage.ADP_MODEL__FLEET_PROFILE:
				if (resolve) return getFleetProfile();
				return basicGetFleetProfile();
			case ADPPackage.ADP_MODEL__RESULT:
				if (resolve) return getResult();
				return basicGetResult();
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
			case ADPPackage.ADP_MODEL__YEAR_START:
				setYearStart((YearMonth)newValue);
				return;
			case ADPPackage.ADP_MODEL__YEAR_END:
				setYearEnd((YearMonth)newValue);
				return;
			case ADPPackage.ADP_MODEL__PURCHASE_CONTRACT_PROFILES:
				getPurchaseContractProfiles().clear();
				getPurchaseContractProfiles().addAll((Collection<? extends PurchaseContractProfile>)newValue);
				return;
			case ADPPackage.ADP_MODEL__SALES_CONTRACT_PROFILES:
				getSalesContractProfiles().clear();
				getSalesContractProfiles().addAll((Collection<? extends SalesContractProfile>)newValue);
				return;
			case ADPPackage.ADP_MODEL__SPOT_MARKETS_PROFILE:
				setSpotMarketsProfile((SpotMarketsProfile)newValue);
				return;
			case ADPPackage.ADP_MODEL__FLEET_PROFILE:
				setFleetProfile((FleetProfile)newValue);
				return;
			case ADPPackage.ADP_MODEL__RESULT:
				setResult((ADPModelResult)newValue);
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
			case ADPPackage.ADP_MODEL__YEAR_START:
				setYearStart(YEAR_START_EDEFAULT);
				return;
			case ADPPackage.ADP_MODEL__YEAR_END:
				setYearEnd(YEAR_END_EDEFAULT);
				return;
			case ADPPackage.ADP_MODEL__PURCHASE_CONTRACT_PROFILES:
				getPurchaseContractProfiles().clear();
				return;
			case ADPPackage.ADP_MODEL__SALES_CONTRACT_PROFILES:
				getSalesContractProfiles().clear();
				return;
			case ADPPackage.ADP_MODEL__SPOT_MARKETS_PROFILE:
				setSpotMarketsProfile((SpotMarketsProfile)null);
				return;
			case ADPPackage.ADP_MODEL__FLEET_PROFILE:
				setFleetProfile((FleetProfile)null);
				return;
			case ADPPackage.ADP_MODEL__RESULT:
				setResult((ADPModelResult)null);
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
			case ADPPackage.ADP_MODEL__YEAR_START:
				return YEAR_START_EDEFAULT == null ? yearStart != null : !YEAR_START_EDEFAULT.equals(yearStart);
			case ADPPackage.ADP_MODEL__YEAR_END:
				return YEAR_END_EDEFAULT == null ? yearEnd != null : !YEAR_END_EDEFAULT.equals(yearEnd);
			case ADPPackage.ADP_MODEL__PURCHASE_CONTRACT_PROFILES:
				return purchaseContractProfiles != null && !purchaseContractProfiles.isEmpty();
			case ADPPackage.ADP_MODEL__SALES_CONTRACT_PROFILES:
				return salesContractProfiles != null && !salesContractProfiles.isEmpty();
			case ADPPackage.ADP_MODEL__SPOT_MARKETS_PROFILE:
				return spotMarketsProfile != null;
			case ADPPackage.ADP_MODEL__FLEET_PROFILE:
				return fleetProfile != null;
			case ADPPackage.ADP_MODEL__RESULT:
				return result != null;
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
		result.append(" (yearStart: ");
		result.append(yearStart);
		result.append(", yearEnd: ");
		result.append(yearEnd);
		result.append(')');
		return result.toString();
	}

} //ADPModelImpl
