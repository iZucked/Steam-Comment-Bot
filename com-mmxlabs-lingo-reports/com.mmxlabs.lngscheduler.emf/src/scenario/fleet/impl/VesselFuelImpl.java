/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.fleet.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import scenario.fleet.FleetPackage;
import scenario.fleet.VesselFuel;

import scenario.impl.NamedObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Vessel Fuel</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.fleet.impl.VesselFuelImpl#getUnitPrice <em>Unit Price</em>}</li>
 *   <li>{@link scenario.fleet.impl.VesselFuelImpl#getEquivalenceFactor <em>Equivalence Factor</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class VesselFuelImpl extends NamedObjectImpl implements VesselFuel {
	/**
	 * The default value of the '{@link #getUnitPrice() <em>Unit Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUnitPrice()
	 * @generated
	 * @ordered
	 */
	protected static final float UNIT_PRICE_EDEFAULT = 0.0F;

	/**
	 * The cached value of the '{@link #getUnitPrice() <em>Unit Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUnitPrice()
	 * @generated
	 * @ordered
	 */
	protected float unitPrice = UNIT_PRICE_EDEFAULT;

	/**
	 * The default value of the '{@link #getEquivalenceFactor() <em>Equivalence Factor</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEquivalenceFactor()
	 * @generated
	 * @ordered
	 */
	protected static final float EQUIVALENCE_FACTOR_EDEFAULT = 0.0F;

	/**
	 * The cached value of the '{@link #getEquivalenceFactor() <em>Equivalence Factor</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEquivalenceFactor()
	 * @generated
	 * @ordered
	 */
	protected float equivalenceFactor = EQUIVALENCE_FACTOR_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected VesselFuelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return FleetPackage.Literals.VESSEL_FUEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public float getUnitPrice() {
		return unitPrice;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUnitPrice(float newUnitPrice) {
		float oldUnitPrice = unitPrice;
		unitPrice = newUnitPrice;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_FUEL__UNIT_PRICE, oldUnitPrice, unitPrice));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public float getEquivalenceFactor() {
		return equivalenceFactor;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEquivalenceFactor(float newEquivalenceFactor) {
		float oldEquivalenceFactor = equivalenceFactor;
		equivalenceFactor = newEquivalenceFactor;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_FUEL__EQUIVALENCE_FACTOR, oldEquivalenceFactor, equivalenceFactor));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case FleetPackage.VESSEL_FUEL__UNIT_PRICE:
				return getUnitPrice();
			case FleetPackage.VESSEL_FUEL__EQUIVALENCE_FACTOR:
				return getEquivalenceFactor();
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
			case FleetPackage.VESSEL_FUEL__UNIT_PRICE:
				setUnitPrice((Float)newValue);
				return;
			case FleetPackage.VESSEL_FUEL__EQUIVALENCE_FACTOR:
				setEquivalenceFactor((Float)newValue);
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
			case FleetPackage.VESSEL_FUEL__UNIT_PRICE:
				setUnitPrice(UNIT_PRICE_EDEFAULT);
				return;
			case FleetPackage.VESSEL_FUEL__EQUIVALENCE_FACTOR:
				setEquivalenceFactor(EQUIVALENCE_FACTOR_EDEFAULT);
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
			case FleetPackage.VESSEL_FUEL__UNIT_PRICE:
				return unitPrice != UNIT_PRICE_EDEFAULT;
			case FleetPackage.VESSEL_FUEL__EQUIVALENCE_FACTOR:
				return equivalenceFactor != EQUIVALENCE_FACTOR_EDEFAULT;
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
		result.append(" (unitPrice: ");
		result.append(unitPrice);
		result.append(", equivalenceFactor: ");
		result.append(equivalenceFactor);
		result.append(')');
		return result.toString();
	}

} //VesselFuelImpl
