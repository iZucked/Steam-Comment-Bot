/**
 */
package com.mmxlabs.models.lng.commercial.impl;

import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.NotionalJourneyBallastBonusContractLine;

import com.mmxlabs.models.lng.port.Port;

import com.mmxlabs.models.lng.types.APortSet;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Notional Journey Ballast Bonus Contract Line</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.NotionalJourneyBallastBonusContractLineImpl#getSpeed <em>Speed</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.NotionalJourneyBallastBonusContractLineImpl#getFuelPriceExpression <em>Fuel Price Expression</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.NotionalJourneyBallastBonusContractLineImpl#getHirePriceExpression <em>Hire Price Expression</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.NotionalJourneyBallastBonusContractLineImpl#getReturnPorts <em>Return Ports</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.impl.NotionalJourneyBallastBonusContractLineImpl#isIncludeCanal <em>Include Canal</em>}</li>
 * </ul>
 *
 * @generated
 */
public class NotionalJourneyBallastBonusContractLineImpl extends BallastBonusContractLineImpl implements NotionalJourneyBallastBonusContractLine {
	/**
	 * The default value of the '{@link #getSpeed() <em>Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSpeed()
	 * @generated
	 * @ordered
	 */
	protected static final double SPEED_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getSpeed() <em>Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSpeed()
	 * @generated
	 * @ordered
	 */
	protected double speed = SPEED_EDEFAULT;

	/**
	 * The default value of the '{@link #getFuelPriceExpression() <em>Fuel Price Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFuelPriceExpression()
	 * @generated
	 * @ordered
	 */
	protected static final String FUEL_PRICE_EXPRESSION_EDEFAULT = "";

	/**
	 * The cached value of the '{@link #getFuelPriceExpression() <em>Fuel Price Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFuelPriceExpression()
	 * @generated
	 * @ordered
	 */
	protected String fuelPriceExpression = FUEL_PRICE_EXPRESSION_EDEFAULT;

	/**
	 * The default value of the '{@link #getHirePriceExpression() <em>Hire Price Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHirePriceExpression()
	 * @generated
	 * @ordered
	 */
	protected static final String HIRE_PRICE_EXPRESSION_EDEFAULT = "";

	/**
	 * The cached value of the '{@link #getHirePriceExpression() <em>Hire Price Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHirePriceExpression()
	 * @generated
	 * @ordered
	 */
	protected String hirePriceExpression = HIRE_PRICE_EXPRESSION_EDEFAULT;

	/**
	 * The cached value of the '{@link #getReturnPorts() <em>Return Ports</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReturnPorts()
	 * @generated
	 * @ordered
	 */
	protected EList<APortSet<Port>> returnPorts;

	/**
	 * The default value of the '{@link #isIncludeCanal() <em>Include Canal</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIncludeCanal()
	 * @generated
	 * @ordered
	 */
	protected static final boolean INCLUDE_CANAL_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isIncludeCanal() <em>Include Canal</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIncludeCanal()
	 * @generated
	 * @ordered
	 */
	protected boolean includeCanal = INCLUDE_CANAL_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected NotionalJourneyBallastBonusContractLineImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CommercialPackage.Literals.NOTIONAL_JOURNEY_BALLAST_BONUS_CONTRACT_LINE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getSpeed() {
		return speed;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSpeed(double newSpeed) {
		double oldSpeed = speed;
		speed = newSpeed;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_CONTRACT_LINE__SPEED, oldSpeed, speed));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getFuelPriceExpression() {
		return fuelPriceExpression;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFuelPriceExpression(String newFuelPriceExpression) {
		String oldFuelPriceExpression = fuelPriceExpression;
		fuelPriceExpression = newFuelPriceExpression;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_CONTRACT_LINE__FUEL_PRICE_EXPRESSION, oldFuelPriceExpression, fuelPriceExpression));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getHirePriceExpression() {
		return hirePriceExpression;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setHirePriceExpression(String newHirePriceExpression) {
		String oldHirePriceExpression = hirePriceExpression;
		hirePriceExpression = newHirePriceExpression;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_CONTRACT_LINE__HIRE_PRICE_EXPRESSION, oldHirePriceExpression, hirePriceExpression));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<APortSet<Port>> getReturnPorts() {
		if (returnPorts == null) {
			returnPorts = new EObjectResolvingEList<APortSet<Port>>(APortSet.class, this, CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_CONTRACT_LINE__RETURN_PORTS);
		}
		return returnPorts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isIncludeCanal() {
		return includeCanal;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIncludeCanal(boolean newIncludeCanal) {
		boolean oldIncludeCanal = includeCanal;
		includeCanal = newIncludeCanal;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_CONTRACT_LINE__INCLUDE_CANAL, oldIncludeCanal, includeCanal));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_CONTRACT_LINE__SPEED:
				return getSpeed();
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_CONTRACT_LINE__FUEL_PRICE_EXPRESSION:
				return getFuelPriceExpression();
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_CONTRACT_LINE__HIRE_PRICE_EXPRESSION:
				return getHirePriceExpression();
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_CONTRACT_LINE__RETURN_PORTS:
				return getReturnPorts();
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_CONTRACT_LINE__INCLUDE_CANAL:
				return isIncludeCanal();
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
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_CONTRACT_LINE__SPEED:
				setSpeed((Double)newValue);
				return;
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_CONTRACT_LINE__FUEL_PRICE_EXPRESSION:
				setFuelPriceExpression((String)newValue);
				return;
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_CONTRACT_LINE__HIRE_PRICE_EXPRESSION:
				setHirePriceExpression((String)newValue);
				return;
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_CONTRACT_LINE__RETURN_PORTS:
				getReturnPorts().clear();
				getReturnPorts().addAll((Collection<? extends APortSet<Port>>)newValue);
				return;
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_CONTRACT_LINE__INCLUDE_CANAL:
				setIncludeCanal((Boolean)newValue);
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
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_CONTRACT_LINE__SPEED:
				setSpeed(SPEED_EDEFAULT);
				return;
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_CONTRACT_LINE__FUEL_PRICE_EXPRESSION:
				setFuelPriceExpression(FUEL_PRICE_EXPRESSION_EDEFAULT);
				return;
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_CONTRACT_LINE__HIRE_PRICE_EXPRESSION:
				setHirePriceExpression(HIRE_PRICE_EXPRESSION_EDEFAULT);
				return;
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_CONTRACT_LINE__RETURN_PORTS:
				getReturnPorts().clear();
				return;
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_CONTRACT_LINE__INCLUDE_CANAL:
				setIncludeCanal(INCLUDE_CANAL_EDEFAULT);
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
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_CONTRACT_LINE__SPEED:
				return speed != SPEED_EDEFAULT;
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_CONTRACT_LINE__FUEL_PRICE_EXPRESSION:
				return FUEL_PRICE_EXPRESSION_EDEFAULT == null ? fuelPriceExpression != null : !FUEL_PRICE_EXPRESSION_EDEFAULT.equals(fuelPriceExpression);
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_CONTRACT_LINE__HIRE_PRICE_EXPRESSION:
				return HIRE_PRICE_EXPRESSION_EDEFAULT == null ? hirePriceExpression != null : !HIRE_PRICE_EXPRESSION_EDEFAULT.equals(hirePriceExpression);
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_CONTRACT_LINE__RETURN_PORTS:
				return returnPorts != null && !returnPorts.isEmpty();
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_CONTRACT_LINE__INCLUDE_CANAL:
				return includeCanal != INCLUDE_CANAL_EDEFAULT;
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
		result.append(" (speed: ");
		result.append(speed);
		result.append(", fuelPriceExpression: ");
		result.append(fuelPriceExpression);
		result.append(", hirePriceExpression: ");
		result.append(hirePriceExpression);
		result.append(", includeCanal: ");
		result.append(includeCanal);
		result.append(')');
		return result.toString();
	}

} //NotionalJourneyBallastBonusContractLineImpl
