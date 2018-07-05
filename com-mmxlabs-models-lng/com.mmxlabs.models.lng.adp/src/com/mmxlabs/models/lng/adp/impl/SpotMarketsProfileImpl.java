/**
 */
package com.mmxlabs.models.lng.adp.impl;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.SpotMarketsProfile;

import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
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
 * An implementation of the model object '<em><b>Spot Markets Profile</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.SpotMarketsProfileImpl#isIncludeEnabledSpotMarkets <em>Include Enabled Spot Markets</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.SpotMarketsProfileImpl#getSpotMarkets <em>Spot Markets</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SpotMarketsProfileImpl extends EObjectImpl implements SpotMarketsProfile {
	/**
	 * The default value of the '{@link #isIncludeEnabledSpotMarkets() <em>Include Enabled Spot Markets</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIncludeEnabledSpotMarkets()
	 * @generated
	 * @ordered
	 */
	protected static final boolean INCLUDE_ENABLED_SPOT_MARKETS_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isIncludeEnabledSpotMarkets() <em>Include Enabled Spot Markets</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIncludeEnabledSpotMarkets()
	 * @generated
	 * @ordered
	 */
	protected boolean includeEnabledSpotMarkets = INCLUDE_ENABLED_SPOT_MARKETS_EDEFAULT;

	/**
	 * The cached value of the '{@link #getSpotMarkets() <em>Spot Markets</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSpotMarkets()
	 * @generated
	 * @ordered
	 */
	protected EList<SpotMarket> spotMarkets;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SpotMarketsProfileImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ADPPackage.Literals.SPOT_MARKETS_PROFILE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isIncludeEnabledSpotMarkets() {
		return includeEnabledSpotMarkets;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIncludeEnabledSpotMarkets(boolean newIncludeEnabledSpotMarkets) {
		boolean oldIncludeEnabledSpotMarkets = includeEnabledSpotMarkets;
		includeEnabledSpotMarkets = newIncludeEnabledSpotMarkets;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.SPOT_MARKETS_PROFILE__INCLUDE_ENABLED_SPOT_MARKETS, oldIncludeEnabledSpotMarkets, includeEnabledSpotMarkets));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<SpotMarket> getSpotMarkets() {
		if (spotMarkets == null) {
			spotMarkets = new EObjectContainmentEList.Resolving<SpotMarket>(SpotMarket.class, this, ADPPackage.SPOT_MARKETS_PROFILE__SPOT_MARKETS);
		}
		return spotMarkets;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ADPPackage.SPOT_MARKETS_PROFILE__SPOT_MARKETS:
				return ((InternalEList<?>)getSpotMarkets()).basicRemove(otherEnd, msgs);
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
			case ADPPackage.SPOT_MARKETS_PROFILE__INCLUDE_ENABLED_SPOT_MARKETS:
				return isIncludeEnabledSpotMarkets();
			case ADPPackage.SPOT_MARKETS_PROFILE__SPOT_MARKETS:
				return getSpotMarkets();
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
			case ADPPackage.SPOT_MARKETS_PROFILE__INCLUDE_ENABLED_SPOT_MARKETS:
				setIncludeEnabledSpotMarkets((Boolean)newValue);
				return;
			case ADPPackage.SPOT_MARKETS_PROFILE__SPOT_MARKETS:
				getSpotMarkets().clear();
				getSpotMarkets().addAll((Collection<? extends SpotMarket>)newValue);
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
			case ADPPackage.SPOT_MARKETS_PROFILE__INCLUDE_ENABLED_SPOT_MARKETS:
				setIncludeEnabledSpotMarkets(INCLUDE_ENABLED_SPOT_MARKETS_EDEFAULT);
				return;
			case ADPPackage.SPOT_MARKETS_PROFILE__SPOT_MARKETS:
				getSpotMarkets().clear();
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
			case ADPPackage.SPOT_MARKETS_PROFILE__INCLUDE_ENABLED_SPOT_MARKETS:
				return includeEnabledSpotMarkets != INCLUDE_ENABLED_SPOT_MARKETS_EDEFAULT;
			case ADPPackage.SPOT_MARKETS_PROFILE__SPOT_MARKETS:
				return spotMarkets != null && !spotMarkets.isEmpty();
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
		result.append(" (includeEnabledSpotMarkets: ");
		result.append(includeEnabledSpotMarkets);
		result.append(')');
		return result.toString();
	}

} //SpotMarketsProfileImpl
