/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.spotmarkets.impl;

import com.mmxlabs.models.lng.commercial.CharterContract;
import com.mmxlabs.models.lng.port.RouteOption;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXObject;
import com.mmxlabs.models.mmxcore.NamedObject;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Charter In Market</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.CharterInMarketImpl#getExtensions <em>Extensions</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.CharterInMarketImpl#getName <em>Name</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.CharterInMarketImpl#getCharterInRate <em>Charter In Rate</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.CharterInMarketImpl#getSpotCharterCount <em>Spot Charter Count</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.CharterInMarketImpl#isOverrideInaccessibleRoutes <em>Override Inaccessible Routes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.CharterInMarketImpl#getInaccessibleRoutes <em>Inaccessible Routes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.CharterInMarketImpl#getCharterContract <em>Charter Contract</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CharterInMarketImpl extends SpotCharterMarketImpl implements CharterInMarket {
	/**
	 * The cached value of the '{@link #getExtensions() <em>Extensions</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExtensions()
	 * @generated
	 * @ordered
	 */
	protected EList<EObject> extensions;

	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getCharterInRate() <em>Charter In Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCharterInRate()
	 * @generated
	 * @ordered
	 */
	protected static final String CHARTER_IN_RATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getCharterInRate() <em>Charter In Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCharterInRate()
	 * @generated
	 * @ordered
	 */
	protected String charterInRate = CHARTER_IN_RATE_EDEFAULT;

	/**
	 * The default value of the '{@link #getSpotCharterCount() <em>Spot Charter Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSpotCharterCount()
	 * @generated
	 * @ordered
	 */
	protected static final int SPOT_CHARTER_COUNT_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getSpotCharterCount() <em>Spot Charter Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSpotCharterCount()
	 * @generated
	 * @ordered
	 */
	protected int spotCharterCount = SPOT_CHARTER_COUNT_EDEFAULT;

	/**
	 * The default value of the '{@link #isOverrideInaccessibleRoutes() <em>Override Inaccessible Routes</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isOverrideInaccessibleRoutes()
	 * @generated
	 * @ordered
	 */
	protected static final boolean OVERRIDE_INACCESSIBLE_ROUTES_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isOverrideInaccessibleRoutes() <em>Override Inaccessible Routes</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isOverrideInaccessibleRoutes()
	 * @generated
	 * @ordered
	 */
	protected boolean overrideInaccessibleRoutes = OVERRIDE_INACCESSIBLE_ROUTES_EDEFAULT;

	/**
	 * The cached value of the '{@link #getInaccessibleRoutes() <em>Inaccessible Routes</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInaccessibleRoutes()
	 * @generated
	 * @ordered
	 */
	protected EList<RouteOption> inaccessibleRoutes;

	/**
	 * The cached value of the '{@link #getCharterContract() <em>Charter Contract</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCharterContract()
	 * @generated
	 * @ordered
	 */
	protected CharterContract charterContract;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CharterInMarketImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SpotMarketsPackage.Literals.CHARTER_IN_MARKET;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EObject> getExtensions() {
		if (extensions == null) {
			extensions = new EObjectContainmentEList<EObject>(EObject.class, this, SpotMarketsPackage.CHARTER_IN_MARKET__EXTENSIONS);
		}
		return extensions;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SpotMarketsPackage.CHARTER_IN_MARKET__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getSpotCharterCount() {
		return spotCharterCount;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSpotCharterCount(int newSpotCharterCount) {
		int oldSpotCharterCount = spotCharterCount;
		spotCharterCount = newSpotCharterCount;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SpotMarketsPackage.CHARTER_IN_MARKET__SPOT_CHARTER_COUNT, oldSpotCharterCount, spotCharterCount));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isOverrideInaccessibleRoutes() {
		return overrideInaccessibleRoutes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOverrideInaccessibleRoutes(boolean newOverrideInaccessibleRoutes) {
		boolean oldOverrideInaccessibleRoutes = overrideInaccessibleRoutes;
		overrideInaccessibleRoutes = newOverrideInaccessibleRoutes;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SpotMarketsPackage.CHARTER_IN_MARKET__OVERRIDE_INACCESSIBLE_ROUTES, oldOverrideInaccessibleRoutes, overrideInaccessibleRoutes));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<RouteOption> getInaccessibleRoutes() {
		if (inaccessibleRoutes == null) {
			inaccessibleRoutes = new EDataTypeUniqueEList<RouteOption>(RouteOption.class, this, SpotMarketsPackage.CHARTER_IN_MARKET__INACCESSIBLE_ROUTES);
		}
		return inaccessibleRoutes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CharterContract getCharterContract() {
		if (charterContract != null && charterContract.eIsProxy()) {
			InternalEObject oldCharterContract = (InternalEObject)charterContract;
			charterContract = (CharterContract)eResolveProxy(oldCharterContract);
			if (charterContract != oldCharterContract) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SpotMarketsPackage.CHARTER_IN_MARKET__CHARTER_CONTRACT, oldCharterContract, charterContract));
			}
		}
		return charterContract;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CharterContract basicGetCharterContract() {
		return charterContract;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCharterContract(CharterContract newCharterContract) {
		CharterContract oldCharterContract = charterContract;
		charterContract = newCharterContract;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SpotMarketsPackage.CHARTER_IN_MARKET__CHARTER_CONTRACT, oldCharterContract, charterContract));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getCharterInRate() {
		return charterInRate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCharterInRate(String newCharterInRate) {
		String oldCharterInRate = charterInRate;
		charterInRate = newCharterInRate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SpotMarketsPackage.CHARTER_IN_MARKET__CHARTER_IN_RATE, oldCharterInRate, charterInRate));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public Object getUnsetValue(EStructuralFeature feature) {
		DelegateInformation dfi = getUnsetValueOrDelegate(feature);
		if (dfi != null) {
			return dfi.getValue(this);
		}
		else {
			return eGet(feature);
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public Object eGetWithDefault(EStructuralFeature feature) {
		
		if (feature.isUnsettable() && !eIsSet(feature)) {
			return getUnsetValue(feature);
		} else {
			return eGet(feature);
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EObject eContainerOp() {
		 return eContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SpotMarketsPackage.CHARTER_IN_MARKET__EXTENSIONS:
				return ((InternalEList<?>)getExtensions()).basicRemove(otherEnd, msgs);
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
			case SpotMarketsPackage.CHARTER_IN_MARKET__EXTENSIONS:
				return getExtensions();
			case SpotMarketsPackage.CHARTER_IN_MARKET__NAME:
				return getName();
			case SpotMarketsPackage.CHARTER_IN_MARKET__CHARTER_IN_RATE:
				return getCharterInRate();
			case SpotMarketsPackage.CHARTER_IN_MARKET__SPOT_CHARTER_COUNT:
				return getSpotCharterCount();
			case SpotMarketsPackage.CHARTER_IN_MARKET__OVERRIDE_INACCESSIBLE_ROUTES:
				return isOverrideInaccessibleRoutes();
			case SpotMarketsPackage.CHARTER_IN_MARKET__INACCESSIBLE_ROUTES:
				return getInaccessibleRoutes();
			case SpotMarketsPackage.CHARTER_IN_MARKET__CHARTER_CONTRACT:
				if (resolve) return getCharterContract();
				return basicGetCharterContract();
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
			case SpotMarketsPackage.CHARTER_IN_MARKET__EXTENSIONS:
				getExtensions().clear();
				getExtensions().addAll((Collection<? extends EObject>)newValue);
				return;
			case SpotMarketsPackage.CHARTER_IN_MARKET__NAME:
				setName((String)newValue);
				return;
			case SpotMarketsPackage.CHARTER_IN_MARKET__CHARTER_IN_RATE:
				setCharterInRate((String)newValue);
				return;
			case SpotMarketsPackage.CHARTER_IN_MARKET__SPOT_CHARTER_COUNT:
				setSpotCharterCount((Integer)newValue);
				return;
			case SpotMarketsPackage.CHARTER_IN_MARKET__OVERRIDE_INACCESSIBLE_ROUTES:
				setOverrideInaccessibleRoutes((Boolean)newValue);
				return;
			case SpotMarketsPackage.CHARTER_IN_MARKET__INACCESSIBLE_ROUTES:
				getInaccessibleRoutes().clear();
				getInaccessibleRoutes().addAll((Collection<? extends RouteOption>)newValue);
				return;
			case SpotMarketsPackage.CHARTER_IN_MARKET__CHARTER_CONTRACT:
				setCharterContract((CharterContract)newValue);
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
			case SpotMarketsPackage.CHARTER_IN_MARKET__EXTENSIONS:
				getExtensions().clear();
				return;
			case SpotMarketsPackage.CHARTER_IN_MARKET__NAME:
				setName(NAME_EDEFAULT);
				return;
			case SpotMarketsPackage.CHARTER_IN_MARKET__CHARTER_IN_RATE:
				setCharterInRate(CHARTER_IN_RATE_EDEFAULT);
				return;
			case SpotMarketsPackage.CHARTER_IN_MARKET__SPOT_CHARTER_COUNT:
				setSpotCharterCount(SPOT_CHARTER_COUNT_EDEFAULT);
				return;
			case SpotMarketsPackage.CHARTER_IN_MARKET__OVERRIDE_INACCESSIBLE_ROUTES:
				setOverrideInaccessibleRoutes(OVERRIDE_INACCESSIBLE_ROUTES_EDEFAULT);
				return;
			case SpotMarketsPackage.CHARTER_IN_MARKET__INACCESSIBLE_ROUTES:
				getInaccessibleRoutes().clear();
				return;
			case SpotMarketsPackage.CHARTER_IN_MARKET__CHARTER_CONTRACT:
				setCharterContract((CharterContract)null);
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
			case SpotMarketsPackage.CHARTER_IN_MARKET__EXTENSIONS:
				return extensions != null && !extensions.isEmpty();
			case SpotMarketsPackage.CHARTER_IN_MARKET__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case SpotMarketsPackage.CHARTER_IN_MARKET__CHARTER_IN_RATE:
				return CHARTER_IN_RATE_EDEFAULT == null ? charterInRate != null : !CHARTER_IN_RATE_EDEFAULT.equals(charterInRate);
			case SpotMarketsPackage.CHARTER_IN_MARKET__SPOT_CHARTER_COUNT:
				return spotCharterCount != SPOT_CHARTER_COUNT_EDEFAULT;
			case SpotMarketsPackage.CHARTER_IN_MARKET__OVERRIDE_INACCESSIBLE_ROUTES:
				return overrideInaccessibleRoutes != OVERRIDE_INACCESSIBLE_ROUTES_EDEFAULT;
			case SpotMarketsPackage.CHARTER_IN_MARKET__INACCESSIBLE_ROUTES:
				return inaccessibleRoutes != null && !inaccessibleRoutes.isEmpty();
			case SpotMarketsPackage.CHARTER_IN_MARKET__CHARTER_CONTRACT:
				return charterContract != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
		if (baseClass == VesselAssignmentType.class) {
			switch (derivedFeatureID) {
				default: return -1;
			}
		}
		if (baseClass == MMXObject.class) {
			switch (derivedFeatureID) {
				case SpotMarketsPackage.CHARTER_IN_MARKET__EXTENSIONS: return MMXCorePackage.MMX_OBJECT__EXTENSIONS;
				default: return -1;
			}
		}
		if (baseClass == NamedObject.class) {
			switch (derivedFeatureID) {
				case SpotMarketsPackage.CHARTER_IN_MARKET__NAME: return MMXCorePackage.NAMED_OBJECT__NAME;
				default: return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
		if (baseClass == VesselAssignmentType.class) {
			switch (baseFeatureID) {
				default: return -1;
			}
		}
		if (baseClass == MMXObject.class) {
			switch (baseFeatureID) {
				case MMXCorePackage.MMX_OBJECT__EXTENSIONS: return SpotMarketsPackage.CHARTER_IN_MARKET__EXTENSIONS;
				default: return -1;
			}
		}
		if (baseClass == NamedObject.class) {
			switch (baseFeatureID) {
				case MMXCorePackage.NAMED_OBJECT__NAME: return SpotMarketsPackage.CHARTER_IN_MARKET__NAME;
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
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
		result.append(" (name: ");
		result.append(name);
		result.append(", charterInRate: ");
		result.append(charterInRate);
		result.append(", spotCharterCount: ");
		result.append(spotCharterCount);
		result.append(", overrideInaccessibleRoutes: ");
		result.append(overrideInaccessibleRoutes);
		result.append(", inaccessibleRoutes: ");
		result.append(inaccessibleRoutes);
		result.append(')');
		return result.toString();
	}
	
	@Override
	public DelegateInformation getUnsetValueOrDelegate(EStructuralFeature feature) {
		return new DelegateInformation(null, null, null);
	}

} //CharterInMarketImpl
