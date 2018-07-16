/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp.impl;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.FleetConstraint;
import com.mmxlabs.models.lng.adp.FleetProfile;

import com.mmxlabs.models.lng.cargo.VesselAvailability;

import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.fleet.Vessel;
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
 * An implementation of the model object '<em><b>Fleet Profile</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.FleetProfileImpl#getVesselAvailabilities <em>Vessel Availabilities</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.FleetProfileImpl#isIncludeEnabledCharterMarkets <em>Include Enabled Charter Markets</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.FleetProfileImpl#getConstraints <em>Constraints</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.FleetProfileImpl#getVesselEvents <em>Vessel Events</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.FleetProfileImpl#getDefaultVessel <em>Default Vessel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.FleetProfileImpl#getDefaultVesselCharterInRate <em>Default Vessel Charter In Rate</em>}</li>
 * </ul>
 *
 * @generated
 */
public class FleetProfileImpl extends EObjectImpl implements FleetProfile {
	/**
	 * The cached value of the '{@link #getVesselAvailabilities() <em>Vessel Availabilities</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselAvailabilities()
	 * @generated
	 * @ordered
	 */
	protected EList<VesselAvailability> vesselAvailabilities;

	/**
	 * The default value of the '{@link #isIncludeEnabledCharterMarkets() <em>Include Enabled Charter Markets</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIncludeEnabledCharterMarkets()
	 * @generated
	 * @ordered
	 */
	protected static final boolean INCLUDE_ENABLED_CHARTER_MARKETS_EDEFAULT = false;
	/**
	 * The cached value of the '{@link #isIncludeEnabledCharterMarkets() <em>Include Enabled Charter Markets</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIncludeEnabledCharterMarkets()
	 * @generated
	 * @ordered
	 */
	protected boolean includeEnabledCharterMarkets = INCLUDE_ENABLED_CHARTER_MARKETS_EDEFAULT;

	/**
	 * The cached value of the '{@link #getConstraints() <em>Constraints</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConstraints()
	 * @generated
	 * @ordered
	 */
	protected EList<FleetConstraint> constraints;

	/**
	 * The cached value of the '{@link #getVesselEvents() <em>Vessel Events</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselEvents()
	 * @generated
	 * @ordered
	 */
	protected EList<VesselEvent> vesselEvents;

	/**
	 * The cached value of the '{@link #getDefaultVessel() <em>Default Vessel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultVessel()
	 * @generated
	 * @ordered
	 */
	protected Vessel defaultVessel;

	/**
	 * The default value of the '{@link #getDefaultVesselCharterInRate() <em>Default Vessel Charter In Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultVesselCharterInRate()
	 * @generated
	 * @ordered
	 */
	protected static final String DEFAULT_VESSEL_CHARTER_IN_RATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDefaultVesselCharterInRate() <em>Default Vessel Charter In Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultVesselCharterInRate()
	 * @generated
	 * @ordered
	 */
	protected String defaultVesselCharterInRate = DEFAULT_VESSEL_CHARTER_IN_RATE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected FleetProfileImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ADPPackage.Literals.FLEET_PROFILE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<VesselAvailability> getVesselAvailabilities() {
		if (vesselAvailabilities == null) {
			vesselAvailabilities = new EObjectContainmentEList.Resolving<VesselAvailability>(VesselAvailability.class, this, ADPPackage.FLEET_PROFILE__VESSEL_AVAILABILITIES);
		}
		return vesselAvailabilities;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isIncludeEnabledCharterMarkets() {
		return includeEnabledCharterMarkets;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIncludeEnabledCharterMarkets(boolean newIncludeEnabledCharterMarkets) {
		boolean oldIncludeEnabledCharterMarkets = includeEnabledCharterMarkets;
		includeEnabledCharterMarkets = newIncludeEnabledCharterMarkets;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.FLEET_PROFILE__INCLUDE_ENABLED_CHARTER_MARKETS, oldIncludeEnabledCharterMarkets, includeEnabledCharterMarkets));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<FleetConstraint> getConstraints() {
		if (constraints == null) {
			constraints = new EObjectContainmentEList.Resolving<FleetConstraint>(FleetConstraint.class, this, ADPPackage.FLEET_PROFILE__CONSTRAINTS);
		}
		return constraints;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<VesselEvent> getVesselEvents() {
		if (vesselEvents == null) {
			vesselEvents = new EObjectContainmentEList.Resolving<VesselEvent>(VesselEvent.class, this, ADPPackage.FLEET_PROFILE__VESSEL_EVENTS);
		}
		return vesselEvents;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Vessel getDefaultVessel() {
		if (defaultVessel != null && defaultVessel.eIsProxy()) {
			InternalEObject oldDefaultVessel = (InternalEObject)defaultVessel;
			defaultVessel = (Vessel)eResolveProxy(oldDefaultVessel);
			if (defaultVessel != oldDefaultVessel) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ADPPackage.FLEET_PROFILE__DEFAULT_VESSEL, oldDefaultVessel, defaultVessel));
			}
		}
		return defaultVessel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Vessel basicGetDefaultVessel() {
		return defaultVessel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDefaultVessel(Vessel newDefaultVessel) {
		Vessel oldDefaultVessel = defaultVessel;
		defaultVessel = newDefaultVessel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.FLEET_PROFILE__DEFAULT_VESSEL, oldDefaultVessel, defaultVessel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDefaultVesselCharterInRate() {
		return defaultVesselCharterInRate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDefaultVesselCharterInRate(String newDefaultVesselCharterInRate) {
		String oldDefaultVesselCharterInRate = defaultVesselCharterInRate;
		defaultVesselCharterInRate = newDefaultVesselCharterInRate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.FLEET_PROFILE__DEFAULT_VESSEL_CHARTER_IN_RATE, oldDefaultVesselCharterInRate, defaultVesselCharterInRate));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ADPPackage.FLEET_PROFILE__VESSEL_AVAILABILITIES:
				return ((InternalEList<?>)getVesselAvailabilities()).basicRemove(otherEnd, msgs);
			case ADPPackage.FLEET_PROFILE__CONSTRAINTS:
				return ((InternalEList<?>)getConstraints()).basicRemove(otherEnd, msgs);
			case ADPPackage.FLEET_PROFILE__VESSEL_EVENTS:
				return ((InternalEList<?>)getVesselEvents()).basicRemove(otherEnd, msgs);
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
			case ADPPackage.FLEET_PROFILE__VESSEL_AVAILABILITIES:
				return getVesselAvailabilities();
			case ADPPackage.FLEET_PROFILE__INCLUDE_ENABLED_CHARTER_MARKETS:
				return isIncludeEnabledCharterMarkets();
			case ADPPackage.FLEET_PROFILE__CONSTRAINTS:
				return getConstraints();
			case ADPPackage.FLEET_PROFILE__VESSEL_EVENTS:
				return getVesselEvents();
			case ADPPackage.FLEET_PROFILE__DEFAULT_VESSEL:
				if (resolve) return getDefaultVessel();
				return basicGetDefaultVessel();
			case ADPPackage.FLEET_PROFILE__DEFAULT_VESSEL_CHARTER_IN_RATE:
				return getDefaultVesselCharterInRate();
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
			case ADPPackage.FLEET_PROFILE__VESSEL_AVAILABILITIES:
				getVesselAvailabilities().clear();
				getVesselAvailabilities().addAll((Collection<? extends VesselAvailability>)newValue);
				return;
			case ADPPackage.FLEET_PROFILE__INCLUDE_ENABLED_CHARTER_MARKETS:
				setIncludeEnabledCharterMarkets((Boolean)newValue);
				return;
			case ADPPackage.FLEET_PROFILE__CONSTRAINTS:
				getConstraints().clear();
				getConstraints().addAll((Collection<? extends FleetConstraint>)newValue);
				return;
			case ADPPackage.FLEET_PROFILE__VESSEL_EVENTS:
				getVesselEvents().clear();
				getVesselEvents().addAll((Collection<? extends VesselEvent>)newValue);
				return;
			case ADPPackage.FLEET_PROFILE__DEFAULT_VESSEL:
				setDefaultVessel((Vessel)newValue);
				return;
			case ADPPackage.FLEET_PROFILE__DEFAULT_VESSEL_CHARTER_IN_RATE:
				setDefaultVesselCharterInRate((String)newValue);
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
			case ADPPackage.FLEET_PROFILE__VESSEL_AVAILABILITIES:
				getVesselAvailabilities().clear();
				return;
			case ADPPackage.FLEET_PROFILE__INCLUDE_ENABLED_CHARTER_MARKETS:
				setIncludeEnabledCharterMarkets(INCLUDE_ENABLED_CHARTER_MARKETS_EDEFAULT);
				return;
			case ADPPackage.FLEET_PROFILE__CONSTRAINTS:
				getConstraints().clear();
				return;
			case ADPPackage.FLEET_PROFILE__VESSEL_EVENTS:
				getVesselEvents().clear();
				return;
			case ADPPackage.FLEET_PROFILE__DEFAULT_VESSEL:
				setDefaultVessel((Vessel)null);
				return;
			case ADPPackage.FLEET_PROFILE__DEFAULT_VESSEL_CHARTER_IN_RATE:
				setDefaultVesselCharterInRate(DEFAULT_VESSEL_CHARTER_IN_RATE_EDEFAULT);
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
			case ADPPackage.FLEET_PROFILE__VESSEL_AVAILABILITIES:
				return vesselAvailabilities != null && !vesselAvailabilities.isEmpty();
			case ADPPackage.FLEET_PROFILE__INCLUDE_ENABLED_CHARTER_MARKETS:
				return includeEnabledCharterMarkets != INCLUDE_ENABLED_CHARTER_MARKETS_EDEFAULT;
			case ADPPackage.FLEET_PROFILE__CONSTRAINTS:
				return constraints != null && !constraints.isEmpty();
			case ADPPackage.FLEET_PROFILE__VESSEL_EVENTS:
				return vesselEvents != null && !vesselEvents.isEmpty();
			case ADPPackage.FLEET_PROFILE__DEFAULT_VESSEL:
				return defaultVessel != null;
			case ADPPackage.FLEET_PROFILE__DEFAULT_VESSEL_CHARTER_IN_RATE:
				return DEFAULT_VESSEL_CHARTER_IN_RATE_EDEFAULT == null ? defaultVesselCharterInRate != null : !DEFAULT_VESSEL_CHARTER_IN_RATE_EDEFAULT.equals(defaultVesselCharterInRate);
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
		result.append(" (includeEnabledCharterMarkets: ");
		result.append(includeEnabledCharterMarkets);
		result.append(", defaultVesselCharterInRate: ");
		result.append(defaultVesselCharterInRate);
		result.append(')');
		return result.toString();
	}

} //FleetProfileImpl
