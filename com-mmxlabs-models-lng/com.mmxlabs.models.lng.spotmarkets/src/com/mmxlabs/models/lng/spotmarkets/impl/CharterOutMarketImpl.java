/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.spotmarkets.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.spotmarkets.CharterOutMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXObject;
import com.mmxlabs.models.mmxcore.NamedObject;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Charter Out Market</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.CharterOutMarketImpl#getExtensions <em>Extensions</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.CharterOutMarketImpl#getName <em>Name</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.CharterOutMarketImpl#getCharterOutRate <em>Charter Out Rate</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.CharterOutMarketImpl#getMinCharterOutDuration <em>Min Charter Out Duration</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.impl.CharterOutMarketImpl#getAvailablePorts <em>Available Ports</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CharterOutMarketImpl extends SpotCharterMarketImpl implements CharterOutMarket {
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
	 * The default value of the '{@link #getCharterOutRate() <em>Charter Out Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCharterOutRate()
	 * @generated
	 * @ordered
	 */
	protected static final String CHARTER_OUT_RATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getCharterOutRate() <em>Charter Out Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCharterOutRate()
	 * @generated
	 * @ordered
	 */
	protected String charterOutRate = CHARTER_OUT_RATE_EDEFAULT;

	/**
	 * The default value of the '{@link #getMinCharterOutDuration() <em>Min Charter Out Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinCharterOutDuration()
	 * @generated
	 * @ordered
	 */
	protected static final int MIN_CHARTER_OUT_DURATION_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMinCharterOutDuration() <em>Min Charter Out Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinCharterOutDuration()
	 * @generated
	 * @ordered
	 */
	protected int minCharterOutDuration = MIN_CHARTER_OUT_DURATION_EDEFAULT;

	/**
	 * The cached value of the '{@link #getAvailablePorts() <em>Available Ports</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAvailablePorts()
	 * @generated
	 * @ordered
	 */
	protected EList<APortSet<Port>> availablePorts;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CharterOutMarketImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SpotMarketsPackage.Literals.CHARTER_OUT_MARKET;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EObject> getExtensions() {
		if (extensions == null) {
			extensions = new EObjectContainmentEList<EObject>(EObject.class, this, SpotMarketsPackage.CHARTER_OUT_MARKET__EXTENSIONS);
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
			eNotify(new ENotificationImpl(this, Notification.SET, SpotMarketsPackage.CHARTER_OUT_MARKET__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getMinCharterOutDuration() {
		return minCharterOutDuration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMinCharterOutDuration(int newMinCharterOutDuration) {
		int oldMinCharterOutDuration = minCharterOutDuration;
		minCharterOutDuration = newMinCharterOutDuration;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SpotMarketsPackage.CHARTER_OUT_MARKET__MIN_CHARTER_OUT_DURATION, oldMinCharterOutDuration, minCharterOutDuration));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<APortSet<Port>> getAvailablePorts() {
		if (availablePorts == null) {
			availablePorts = new EObjectResolvingEList<APortSet<Port>>(APortSet.class, this, SpotMarketsPackage.CHARTER_OUT_MARKET__AVAILABLE_PORTS);
		}
		return availablePorts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getCharterOutRate() {
		return charterOutRate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCharterOutRate(String newCharterOutRate) {
		String oldCharterOutRate = charterOutRate;
		charterOutRate = newCharterOutRate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SpotMarketsPackage.CHARTER_OUT_MARKET__CHARTER_OUT_RATE, oldCharterOutRate, charterOutRate));
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
			case SpotMarketsPackage.CHARTER_OUT_MARKET__EXTENSIONS:
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
			case SpotMarketsPackage.CHARTER_OUT_MARKET__EXTENSIONS:
				return getExtensions();
			case SpotMarketsPackage.CHARTER_OUT_MARKET__NAME:
				return getName();
			case SpotMarketsPackage.CHARTER_OUT_MARKET__CHARTER_OUT_RATE:
				return getCharterOutRate();
			case SpotMarketsPackage.CHARTER_OUT_MARKET__MIN_CHARTER_OUT_DURATION:
				return getMinCharterOutDuration();
			case SpotMarketsPackage.CHARTER_OUT_MARKET__AVAILABLE_PORTS:
				return getAvailablePorts();
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
			case SpotMarketsPackage.CHARTER_OUT_MARKET__EXTENSIONS:
				getExtensions().clear();
				getExtensions().addAll((Collection<? extends EObject>)newValue);
				return;
			case SpotMarketsPackage.CHARTER_OUT_MARKET__NAME:
				setName((String)newValue);
				return;
			case SpotMarketsPackage.CHARTER_OUT_MARKET__CHARTER_OUT_RATE:
				setCharterOutRate((String)newValue);
				return;
			case SpotMarketsPackage.CHARTER_OUT_MARKET__MIN_CHARTER_OUT_DURATION:
				setMinCharterOutDuration((Integer)newValue);
				return;
			case SpotMarketsPackage.CHARTER_OUT_MARKET__AVAILABLE_PORTS:
				getAvailablePorts().clear();
				getAvailablePorts().addAll((Collection<? extends APortSet<Port>>)newValue);
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
			case SpotMarketsPackage.CHARTER_OUT_MARKET__EXTENSIONS:
				getExtensions().clear();
				return;
			case SpotMarketsPackage.CHARTER_OUT_MARKET__NAME:
				setName(NAME_EDEFAULT);
				return;
			case SpotMarketsPackage.CHARTER_OUT_MARKET__CHARTER_OUT_RATE:
				setCharterOutRate(CHARTER_OUT_RATE_EDEFAULT);
				return;
			case SpotMarketsPackage.CHARTER_OUT_MARKET__MIN_CHARTER_OUT_DURATION:
				setMinCharterOutDuration(MIN_CHARTER_OUT_DURATION_EDEFAULT);
				return;
			case SpotMarketsPackage.CHARTER_OUT_MARKET__AVAILABLE_PORTS:
				getAvailablePorts().clear();
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
			case SpotMarketsPackage.CHARTER_OUT_MARKET__EXTENSIONS:
				return extensions != null && !extensions.isEmpty();
			case SpotMarketsPackage.CHARTER_OUT_MARKET__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case SpotMarketsPackage.CHARTER_OUT_MARKET__CHARTER_OUT_RATE:
				return CHARTER_OUT_RATE_EDEFAULT == null ? charterOutRate != null : !CHARTER_OUT_RATE_EDEFAULT.equals(charterOutRate);
			case SpotMarketsPackage.CHARTER_OUT_MARKET__MIN_CHARTER_OUT_DURATION:
				return minCharterOutDuration != MIN_CHARTER_OUT_DURATION_EDEFAULT;
			case SpotMarketsPackage.CHARTER_OUT_MARKET__AVAILABLE_PORTS:
				return availablePorts != null && !availablePorts.isEmpty();
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
		if (baseClass == MMXObject.class) {
			switch (derivedFeatureID) {
				case SpotMarketsPackage.CHARTER_OUT_MARKET__EXTENSIONS: return MMXCorePackage.MMX_OBJECT__EXTENSIONS;
				default: return -1;
			}
		}
		if (baseClass == NamedObject.class) {
			switch (derivedFeatureID) {
				case SpotMarketsPackage.CHARTER_OUT_MARKET__NAME: return MMXCorePackage.NAMED_OBJECT__NAME;
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
		if (baseClass == MMXObject.class) {
			switch (baseFeatureID) {
				case MMXCorePackage.MMX_OBJECT__EXTENSIONS: return SpotMarketsPackage.CHARTER_OUT_MARKET__EXTENSIONS;
				default: return -1;
			}
		}
		if (baseClass == NamedObject.class) {
			switch (baseFeatureID) {
				case MMXCorePackage.NAMED_OBJECT__NAME: return SpotMarketsPackage.CHARTER_OUT_MARKET__NAME;
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
		result.append(", charterOutRate: ");
		result.append(charterOutRate);
		result.append(", minCharterOutDuration: ");
		result.append(minCharterOutDuration);
		result.append(')');
		return result.toString();
	}
	
	@Override
	public DelegateInformation getUnsetValueOrDelegate(EStructuralFeature feature) {
		return new DelegateInformation(null, null, null);
	}
	
} //CharterOutMarketImpl
