/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp.impl;

import java.time.YearMonth;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.FleetProfile;
import com.mmxlabs.models.lng.adp.MullProfile;
import com.mmxlabs.models.lng.adp.PurchaseContractProfile;
import com.mmxlabs.models.lng.adp.SalesContractProfile;
import com.mmxlabs.models.lng.adp.SpacingProfile;
import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;

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
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.ADPModelImpl#getFleetProfile <em>Fleet Profile</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.ADPModelImpl#getMullProfile <em>Mull Profile</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.ADPModelImpl#getSpacingProfile <em>Spacing Profile</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ADPModelImpl extends UUIDObjectImpl implements ADPModel {
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
	 * The cached value of the '{@link #getFleetProfile() <em>Fleet Profile</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFleetProfile()
	 * @generated
	 * @ordered
	 */
	protected FleetProfile fleetProfile;

	/**
	 * The cached value of the '{@link #getMullProfile() <em>Mull Profile</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMullProfile()
	 * @generated
	 * @ordered
	 */
	protected MullProfile mullProfile;

	/**
	 * The cached value of the '{@link #getSpacingProfile() <em>Spacing Profile</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSpacingProfile()
	 * @generated
	 * @ordered
	 */
	protected SpacingProfile spacingProfile;

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
	@Override
	public YearMonth getYearEnd() {
		return yearEnd;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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
	@Override
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
	@Override
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
	@Override
	public MullProfile getMullProfile() {
		if (mullProfile != null && mullProfile.eIsProxy()) {
			InternalEObject oldMullProfile = (InternalEObject)mullProfile;
			mullProfile = (MullProfile)eResolveProxy(oldMullProfile);
			if (mullProfile != oldMullProfile) {
				InternalEObject newMullProfile = (InternalEObject)mullProfile;
				NotificationChain msgs = oldMullProfile.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ADPPackage.ADP_MODEL__MULL_PROFILE, null, null);
				if (newMullProfile.eInternalContainer() == null) {
					msgs = newMullProfile.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ADPPackage.ADP_MODEL__MULL_PROFILE, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ADPPackage.ADP_MODEL__MULL_PROFILE, oldMullProfile, mullProfile));
			}
		}
		return mullProfile;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MullProfile basicGetMullProfile() {
		return mullProfile;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetMullProfile(MullProfile newMullProfile, NotificationChain msgs) {
		MullProfile oldMullProfile = mullProfile;
		mullProfile = newMullProfile;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ADPPackage.ADP_MODEL__MULL_PROFILE, oldMullProfile, newMullProfile);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMullProfile(MullProfile newMullProfile) {
		if (newMullProfile != mullProfile) {
			NotificationChain msgs = null;
			if (mullProfile != null)
				msgs = ((InternalEObject)mullProfile).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ADPPackage.ADP_MODEL__MULL_PROFILE, null, msgs);
			if (newMullProfile != null)
				msgs = ((InternalEObject)newMullProfile).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ADPPackage.ADP_MODEL__MULL_PROFILE, null, msgs);
			msgs = basicSetMullProfile(newMullProfile, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.ADP_MODEL__MULL_PROFILE, newMullProfile, newMullProfile));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SpacingProfile getSpacingProfile() {
		if (spacingProfile != null && spacingProfile.eIsProxy()) {
			InternalEObject oldSpacingProfile = (InternalEObject)spacingProfile;
			spacingProfile = (SpacingProfile)eResolveProxy(oldSpacingProfile);
			if (spacingProfile != oldSpacingProfile) {
				InternalEObject newSpacingProfile = (InternalEObject)spacingProfile;
				NotificationChain msgs = oldSpacingProfile.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ADPPackage.ADP_MODEL__SPACING_PROFILE, null, null);
				if (newSpacingProfile.eInternalContainer() == null) {
					msgs = newSpacingProfile.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ADPPackage.ADP_MODEL__SPACING_PROFILE, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ADPPackage.ADP_MODEL__SPACING_PROFILE, oldSpacingProfile, spacingProfile));
			}
		}
		return spacingProfile;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SpacingProfile basicGetSpacingProfile() {
		return spacingProfile;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSpacingProfile(SpacingProfile newSpacingProfile, NotificationChain msgs) {
		SpacingProfile oldSpacingProfile = spacingProfile;
		spacingProfile = newSpacingProfile;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ADPPackage.ADP_MODEL__SPACING_PROFILE, oldSpacingProfile, newSpacingProfile);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSpacingProfile(SpacingProfile newSpacingProfile) {
		if (newSpacingProfile != spacingProfile) {
			NotificationChain msgs = null;
			if (spacingProfile != null)
				msgs = ((InternalEObject)spacingProfile).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ADPPackage.ADP_MODEL__SPACING_PROFILE, null, msgs);
			if (newSpacingProfile != null)
				msgs = ((InternalEObject)newSpacingProfile).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ADPPackage.ADP_MODEL__SPACING_PROFILE, null, msgs);
			msgs = basicSetSpacingProfile(newSpacingProfile, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.ADP_MODEL__SPACING_PROFILE, newSpacingProfile, newSpacingProfile));
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
			case ADPPackage.ADP_MODEL__FLEET_PROFILE:
				return basicSetFleetProfile(null, msgs);
			case ADPPackage.ADP_MODEL__MULL_PROFILE:
				return basicSetMullProfile(null, msgs);
			case ADPPackage.ADP_MODEL__SPACING_PROFILE:
				return basicSetSpacingProfile(null, msgs);
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
			case ADPPackage.ADP_MODEL__FLEET_PROFILE:
				if (resolve) return getFleetProfile();
				return basicGetFleetProfile();
			case ADPPackage.ADP_MODEL__MULL_PROFILE:
				if (resolve) return getMullProfile();
				return basicGetMullProfile();
			case ADPPackage.ADP_MODEL__SPACING_PROFILE:
				if (resolve) return getSpacingProfile();
				return basicGetSpacingProfile();
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
			case ADPPackage.ADP_MODEL__FLEET_PROFILE:
				setFleetProfile((FleetProfile)newValue);
				return;
			case ADPPackage.ADP_MODEL__MULL_PROFILE:
				setMullProfile((MullProfile)newValue);
				return;
			case ADPPackage.ADP_MODEL__SPACING_PROFILE:
				setSpacingProfile((SpacingProfile)newValue);
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
			case ADPPackage.ADP_MODEL__FLEET_PROFILE:
				setFleetProfile((FleetProfile)null);
				return;
			case ADPPackage.ADP_MODEL__MULL_PROFILE:
				setMullProfile((MullProfile)null);
				return;
			case ADPPackage.ADP_MODEL__SPACING_PROFILE:
				setSpacingProfile((SpacingProfile)null);
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
			case ADPPackage.ADP_MODEL__FLEET_PROFILE:
				return fleetProfile != null;
			case ADPPackage.ADP_MODEL__MULL_PROFILE:
				return mullProfile != null;
			case ADPPackage.ADP_MODEL__SPACING_PROFILE:
				return spacingProfile != null;
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
		result.append(" (yearStart: ");
		result.append(yearStart);
		result.append(", yearEnd: ");
		result.append(yearEnd);
		result.append(')');
		return result.toString();
	}

} //ADPModelImpl
