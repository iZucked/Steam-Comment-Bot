/**
 */
package com.mmxlabs.models.lng.adp.impl;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.MarketAllocationRow;

import com.mmxlabs.models.lng.fleet.Vessel;

import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Market Allocation Row</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.MarketAllocationRowImpl#getMarket <em>Market</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.MarketAllocationRowImpl#getWeight <em>Weight</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.MarketAllocationRowImpl#getVessels <em>Vessels</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MarketAllocationRowImpl extends EObjectImpl implements MarketAllocationRow {
	/**
	 * The cached value of the '{@link #getMarket() <em>Market</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMarket()
	 * @generated
	 * @ordered
	 */
	protected DESSalesMarket market;

	/**
	 * The default value of the '{@link #getWeight() <em>Weight</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWeight()
	 * @generated
	 * @ordered
	 */
	protected static final int WEIGHT_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getWeight() <em>Weight</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWeight()
	 * @generated
	 * @ordered
	 */
	protected int weight = WEIGHT_EDEFAULT;

	/**
	 * The cached value of the '{@link #getVessels() <em>Vessels</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVessels()
	 * @generated
	 * @ordered
	 */
	protected EList<Vessel> vessels;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MarketAllocationRowImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ADPPackage.Literals.MARKET_ALLOCATION_ROW;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DESSalesMarket getMarket() {
		if (market != null && market.eIsProxy()) {
			InternalEObject oldMarket = (InternalEObject)market;
			market = (DESSalesMarket)eResolveProxy(oldMarket);
			if (market != oldMarket) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ADPPackage.MARKET_ALLOCATION_ROW__MARKET, oldMarket, market));
			}
		}
		return market;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DESSalesMarket basicGetMarket() {
		return market;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMarket(DESSalesMarket newMarket) {
		DESSalesMarket oldMarket = market;
		market = newMarket;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.MARKET_ALLOCATION_ROW__MARKET, oldMarket, market));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getWeight() {
		return weight;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setWeight(int newWeight) {
		int oldWeight = weight;
		weight = newWeight;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.MARKET_ALLOCATION_ROW__WEIGHT, oldWeight, weight));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Vessel> getVessels() {
		if (vessels == null) {
			vessels = new EObjectResolvingEList<Vessel>(Vessel.class, this, ADPPackage.MARKET_ALLOCATION_ROW__VESSELS);
		}
		return vessels;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ADPPackage.MARKET_ALLOCATION_ROW__MARKET:
				if (resolve) return getMarket();
				return basicGetMarket();
			case ADPPackage.MARKET_ALLOCATION_ROW__WEIGHT:
				return getWeight();
			case ADPPackage.MARKET_ALLOCATION_ROW__VESSELS:
				return getVessels();
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
			case ADPPackage.MARKET_ALLOCATION_ROW__MARKET:
				setMarket((DESSalesMarket)newValue);
				return;
			case ADPPackage.MARKET_ALLOCATION_ROW__WEIGHT:
				setWeight((Integer)newValue);
				return;
			case ADPPackage.MARKET_ALLOCATION_ROW__VESSELS:
				getVessels().clear();
				getVessels().addAll((Collection<? extends Vessel>)newValue);
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
			case ADPPackage.MARKET_ALLOCATION_ROW__MARKET:
				setMarket((DESSalesMarket)null);
				return;
			case ADPPackage.MARKET_ALLOCATION_ROW__WEIGHT:
				setWeight(WEIGHT_EDEFAULT);
				return;
			case ADPPackage.MARKET_ALLOCATION_ROW__VESSELS:
				getVessels().clear();
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
			case ADPPackage.MARKET_ALLOCATION_ROW__MARKET:
				return market != null;
			case ADPPackage.MARKET_ALLOCATION_ROW__WEIGHT:
				return weight != WEIGHT_EDEFAULT;
			case ADPPackage.MARKET_ALLOCATION_ROW__VESSELS:
				return vessels != null && !vessels.isEmpty();
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
		result.append(" (weight: ");
		result.append(weight);
		result.append(')');
		return result.toString();
	}

} //MarketAllocationRowImpl
