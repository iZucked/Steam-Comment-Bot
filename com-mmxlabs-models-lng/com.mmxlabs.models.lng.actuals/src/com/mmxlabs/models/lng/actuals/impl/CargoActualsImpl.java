/**
 */
package com.mmxlabs.models.lng.actuals.impl;

import com.mmxlabs.models.lng.actuals.ActualsPackage;
import com.mmxlabs.models.lng.actuals.CargoActuals;
import com.mmxlabs.models.lng.actuals.SlotActuals;

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
 * An implementation of the model object '<em><b>Cargo Actuals</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.actuals.impl.CargoActualsImpl#getBaseFuelPrice <em>Base Fuel Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.impl.CargoActualsImpl#getVolume <em>Volume</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.impl.CargoActualsImpl#getInsurancePremium <em>Insurance Premium</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.impl.CargoActualsImpl#getCrewBonus <em>Crew Bonus</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.impl.CargoActualsImpl#getActuals <em>Actuals</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CargoActualsImpl extends EObjectImpl implements CargoActuals {
	/**
	 * The default value of the '{@link #getBaseFuelPrice() <em>Base Fuel Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseFuelPrice()
	 * @generated
	 * @ordered
	 */
	protected static final float BASE_FUEL_PRICE_EDEFAULT = 0.0F;

	/**
	 * The cached value of the '{@link #getBaseFuelPrice() <em>Base Fuel Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseFuelPrice()
	 * @generated
	 * @ordered
	 */
	protected float baseFuelPrice = BASE_FUEL_PRICE_EDEFAULT;

	/**
	 * The default value of the '{@link #getVolume() <em>Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolume()
	 * @generated
	 * @ordered
	 */
	protected static final float VOLUME_EDEFAULT = 0.0F;

	/**
	 * The cached value of the '{@link #getVolume() <em>Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolume()
	 * @generated
	 * @ordered
	 */
	protected float volume = VOLUME_EDEFAULT;

	/**
	 * The default value of the '{@link #getInsurancePremium() <em>Insurance Premium</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInsurancePremium()
	 * @generated
	 * @ordered
	 */
	protected static final int INSURANCE_PREMIUM_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getInsurancePremium() <em>Insurance Premium</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInsurancePremium()
	 * @generated
	 * @ordered
	 */
	protected int insurancePremium = INSURANCE_PREMIUM_EDEFAULT;

	/**
	 * The default value of the '{@link #getCrewBonus() <em>Crew Bonus</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCrewBonus()
	 * @generated
	 * @ordered
	 */
	protected static final int CREW_BONUS_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getCrewBonus() <em>Crew Bonus</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCrewBonus()
	 * @generated
	 * @ordered
	 */
	protected int crewBonus = CREW_BONUS_EDEFAULT;

	/**
	 * The cached value of the '{@link #getActuals() <em>Actuals</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getActuals()
	 * @generated
	 * @ordered
	 */
	protected EList<SlotActuals> actuals;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CargoActualsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ActualsPackage.Literals.CARGO_ACTUALS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public float getBaseFuelPrice() {
		return baseFuelPrice;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setBaseFuelPrice(float newBaseFuelPrice) {
		float oldBaseFuelPrice = baseFuelPrice;
		baseFuelPrice = newBaseFuelPrice;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ActualsPackage.CARGO_ACTUALS__BASE_FUEL_PRICE, oldBaseFuelPrice, baseFuelPrice));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public float getVolume() {
		return volume;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setVolume(float newVolume) {
		float oldVolume = volume;
		volume = newVolume;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ActualsPackage.CARGO_ACTUALS__VOLUME, oldVolume, volume));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getInsurancePremium() {
		return insurancePremium;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setInsurancePremium(int newInsurancePremium) {
		int oldInsurancePremium = insurancePremium;
		insurancePremium = newInsurancePremium;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ActualsPackage.CARGO_ACTUALS__INSURANCE_PREMIUM, oldInsurancePremium, insurancePremium));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getCrewBonus() {
		return crewBonus;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCrewBonus(int newCrewBonus) {
		int oldCrewBonus = crewBonus;
		crewBonus = newCrewBonus;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ActualsPackage.CARGO_ACTUALS__CREW_BONUS, oldCrewBonus, crewBonus));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<SlotActuals> getActuals() {
		if (actuals == null) {
			actuals = new EObjectContainmentEList.Resolving<SlotActuals>(SlotActuals.class, this, ActualsPackage.CARGO_ACTUALS__ACTUALS);
		}
		return actuals;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ActualsPackage.CARGO_ACTUALS__ACTUALS:
				return ((InternalEList<?>)getActuals()).basicRemove(otherEnd, msgs);
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
			case ActualsPackage.CARGO_ACTUALS__BASE_FUEL_PRICE:
				return getBaseFuelPrice();
			case ActualsPackage.CARGO_ACTUALS__VOLUME:
				return getVolume();
			case ActualsPackage.CARGO_ACTUALS__INSURANCE_PREMIUM:
				return getInsurancePremium();
			case ActualsPackage.CARGO_ACTUALS__CREW_BONUS:
				return getCrewBonus();
			case ActualsPackage.CARGO_ACTUALS__ACTUALS:
				return getActuals();
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
			case ActualsPackage.CARGO_ACTUALS__BASE_FUEL_PRICE:
				setBaseFuelPrice((Float)newValue);
				return;
			case ActualsPackage.CARGO_ACTUALS__VOLUME:
				setVolume((Float)newValue);
				return;
			case ActualsPackage.CARGO_ACTUALS__INSURANCE_PREMIUM:
				setInsurancePremium((Integer)newValue);
				return;
			case ActualsPackage.CARGO_ACTUALS__CREW_BONUS:
				setCrewBonus((Integer)newValue);
				return;
			case ActualsPackage.CARGO_ACTUALS__ACTUALS:
				getActuals().clear();
				getActuals().addAll((Collection<? extends SlotActuals>)newValue);
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
			case ActualsPackage.CARGO_ACTUALS__BASE_FUEL_PRICE:
				setBaseFuelPrice(BASE_FUEL_PRICE_EDEFAULT);
				return;
			case ActualsPackage.CARGO_ACTUALS__VOLUME:
				setVolume(VOLUME_EDEFAULT);
				return;
			case ActualsPackage.CARGO_ACTUALS__INSURANCE_PREMIUM:
				setInsurancePremium(INSURANCE_PREMIUM_EDEFAULT);
				return;
			case ActualsPackage.CARGO_ACTUALS__CREW_BONUS:
				setCrewBonus(CREW_BONUS_EDEFAULT);
				return;
			case ActualsPackage.CARGO_ACTUALS__ACTUALS:
				getActuals().clear();
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
			case ActualsPackage.CARGO_ACTUALS__BASE_FUEL_PRICE:
				return baseFuelPrice != BASE_FUEL_PRICE_EDEFAULT;
			case ActualsPackage.CARGO_ACTUALS__VOLUME:
				return volume != VOLUME_EDEFAULT;
			case ActualsPackage.CARGO_ACTUALS__INSURANCE_PREMIUM:
				return insurancePremium != INSURANCE_PREMIUM_EDEFAULT;
			case ActualsPackage.CARGO_ACTUALS__CREW_BONUS:
				return crewBonus != CREW_BONUS_EDEFAULT;
			case ActualsPackage.CARGO_ACTUALS__ACTUALS:
				return actuals != null && !actuals.isEmpty();
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
		result.append(" (baseFuelPrice: ");
		result.append(baseFuelPrice);
		result.append(", volume: ");
		result.append(volume);
		result.append(", insurancePremium: ");
		result.append(insurancePremium);
		result.append(", crewBonus: ");
		result.append(crewBonus);
		result.append(')');
		return result.toString();
	}

} //CargoActualsImpl
