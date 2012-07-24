

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.pricing.impl;
import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.SpotAvailability;
import com.mmxlabs.models.lng.pricing.SpotMarket;
import com.mmxlabs.models.lng.pricing.SpotMarketGroup;
import com.mmxlabs.models.lng.pricing.SpotType;

import com.mmxlabs.models.mmxcore.impl.NamedObjectImpl;
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
 * An implementation of the model object '<em><b>Spot Market Group</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.SpotMarketGroupImpl#getAvailability <em>Availability</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.SpotMarketGroupImpl#getType <em>Type</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.SpotMarketGroupImpl#getMarkets <em>Markets</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SpotMarketGroupImpl extends EObjectImpl implements SpotMarketGroup {
	/**
	 * The cached value of the '{@link #getAvailability() <em>Availability</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAvailability()
	 * @generated
	 * @ordered
	 */
	protected SpotAvailability availability;

	/**
	 * The default value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected static final SpotType TYPE_EDEFAULT = SpotType.FOB_SALE;

	/**
	 * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected SpotType type = TYPE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getMarkets() <em>Markets</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMarkets()
	 * @generated
	 * @ordered
	 */
	protected EList<SpotMarket> markets;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SpotMarketGroupImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PricingPackage.Literals.SPOT_MARKET_GROUP;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	public SpotAvailability getAvailability() {
		return availability;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetAvailability(SpotAvailability newAvailability, NotificationChain msgs) {
		SpotAvailability oldAvailability = availability;
		availability = newAvailability;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, PricingPackage.SPOT_MARKET_GROUP__AVAILABILITY, oldAvailability, newAvailability);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAvailability(SpotAvailability newAvailability) {
		if (newAvailability != availability) {
			NotificationChain msgs = null;
			if (availability != null)
				msgs = ((InternalEObject)availability).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - PricingPackage.SPOT_MARKET_GROUP__AVAILABILITY, null, msgs);
			if (newAvailability != null)
				msgs = ((InternalEObject)newAvailability).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - PricingPackage.SPOT_MARKET_GROUP__AVAILABILITY, null, msgs);
			msgs = basicSetAvailability(newAvailability, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.SPOT_MARKET_GROUP__AVAILABILITY, newAvailability, newAvailability));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SpotType getType() {
		return type;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setType(SpotType newType) {
		SpotType oldType = type;
		type = newType == null ? TYPE_EDEFAULT : newType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.SPOT_MARKET_GROUP__TYPE, oldType, type));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<SpotMarket> getMarkets() {
		if (markets == null) {
			markets = new EObjectContainmentEList<SpotMarket>(SpotMarket.class, this, PricingPackage.SPOT_MARKET_GROUP__MARKETS);
		}
		return markets;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case PricingPackage.SPOT_MARKET_GROUP__AVAILABILITY:
				return basicSetAvailability(null, msgs);
			case PricingPackage.SPOT_MARKET_GROUP__MARKETS:
				return ((InternalEList<?>)getMarkets()).basicRemove(otherEnd, msgs);
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
			case PricingPackage.SPOT_MARKET_GROUP__AVAILABILITY:
				return getAvailability();
			case PricingPackage.SPOT_MARKET_GROUP__TYPE:
				return getType();
			case PricingPackage.SPOT_MARKET_GROUP__MARKETS:
				return getMarkets();
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
			case PricingPackage.SPOT_MARKET_GROUP__AVAILABILITY:
				setAvailability((SpotAvailability)newValue);
				return;
			case PricingPackage.SPOT_MARKET_GROUP__TYPE:
				setType((SpotType)newValue);
				return;
			case PricingPackage.SPOT_MARKET_GROUP__MARKETS:
				getMarkets().clear();
				getMarkets().addAll((Collection<? extends SpotMarket>)newValue);
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
			case PricingPackage.SPOT_MARKET_GROUP__AVAILABILITY:
				setAvailability((SpotAvailability)null);
				return;
			case PricingPackage.SPOT_MARKET_GROUP__TYPE:
				setType(TYPE_EDEFAULT);
				return;
			case PricingPackage.SPOT_MARKET_GROUP__MARKETS:
				getMarkets().clear();
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
			case PricingPackage.SPOT_MARKET_GROUP__AVAILABILITY:
				return availability != null;
			case PricingPackage.SPOT_MARKET_GROUP__TYPE:
				return type != TYPE_EDEFAULT;
			case PricingPackage.SPOT_MARKET_GROUP__MARKETS:
				return markets != null && !markets.isEmpty();
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
		result.append(" (type: ");
		result.append(type);
		result.append(')');
		return result.toString();
	}

} // end of SpotMarketGroupImpl

// finish type fixing
