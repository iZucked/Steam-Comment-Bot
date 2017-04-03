/**
 */
package com.mmxlabs.models.lng.schedule.impl;

import com.mmxlabs.models.lng.schedule.NotionalJourneyContractDetails;
import com.mmxlabs.models.lng.schedule.SchedulePackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Notional Journey Contract Details</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.NotionalJourneyContractDetailsImpl#getReturnPort <em>Return Port</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.NotionalJourneyContractDetailsImpl#getDistance <em>Distance</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.NotionalJourneyContractDetailsImpl#getTotalTimeInDays <em>Total Time In Days</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.NotionalJourneyContractDetailsImpl#getTotalFuelUsed <em>Total Fuel Used</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.NotionalJourneyContractDetailsImpl#getFuelPrice <em>Fuel Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.NotionalJourneyContractDetailsImpl#getTotalFuelCost <em>Total Fuel Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.NotionalJourneyContractDetailsImpl#getHireRate <em>Hire Rate</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.NotionalJourneyContractDetailsImpl#getHireCost <em>Hire Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.NotionalJourneyContractDetailsImpl#getRouteTaken <em>Route Taken</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.NotionalJourneyContractDetailsImpl#getCanalCost <em>Canal Cost</em>}</li>
 * </ul>
 *
 * @generated
 */
public class NotionalJourneyContractDetailsImpl extends MatchingContractDetailsImpl implements NotionalJourneyContractDetails {
	/**
	 * The default value of the '{@link #getReturnPort() <em>Return Port</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReturnPort()
	 * @generated
	 * @ordered
	 */
	protected static final String RETURN_PORT_EDEFAULT = "";

	/**
	 * The cached value of the '{@link #getReturnPort() <em>Return Port</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReturnPort()
	 * @generated
	 * @ordered
	 */
	protected String returnPort = RETURN_PORT_EDEFAULT;

	/**
	 * The default value of the '{@link #getDistance() <em>Distance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDistance()
	 * @generated
	 * @ordered
	 */
	protected static final int DISTANCE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getDistance() <em>Distance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDistance()
	 * @generated
	 * @ordered
	 */
	protected int distance = DISTANCE_EDEFAULT;

	/**
	 * The default value of the '{@link #getTotalTimeInDays() <em>Total Time In Days</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTotalTimeInDays()
	 * @generated
	 * @ordered
	 */
	protected static final double TOTAL_TIME_IN_DAYS_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getTotalTimeInDays() <em>Total Time In Days</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTotalTimeInDays()
	 * @generated
	 * @ordered
	 */
	protected double totalTimeInDays = TOTAL_TIME_IN_DAYS_EDEFAULT;

	/**
	 * The default value of the '{@link #getTotalFuelUsed() <em>Total Fuel Used</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTotalFuelUsed()
	 * @generated
	 * @ordered
	 */
	protected static final int TOTAL_FUEL_USED_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getTotalFuelUsed() <em>Total Fuel Used</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTotalFuelUsed()
	 * @generated
	 * @ordered
	 */
	protected int totalFuelUsed = TOTAL_FUEL_USED_EDEFAULT;

	/**
	 * The default value of the '{@link #getFuelPrice() <em>Fuel Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFuelPrice()
	 * @generated
	 * @ordered
	 */
	protected static final double FUEL_PRICE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getFuelPrice() <em>Fuel Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFuelPrice()
	 * @generated
	 * @ordered
	 */
	protected double fuelPrice = FUEL_PRICE_EDEFAULT;

	/**
	 * The default value of the '{@link #getTotalFuelCost() <em>Total Fuel Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTotalFuelCost()
	 * @generated
	 * @ordered
	 */
	protected static final int TOTAL_FUEL_COST_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getTotalFuelCost() <em>Total Fuel Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTotalFuelCost()
	 * @generated
	 * @ordered
	 */
	protected int totalFuelCost = TOTAL_FUEL_COST_EDEFAULT;

	/**
	 * The default value of the '{@link #getHireRate() <em>Hire Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHireRate()
	 * @generated
	 * @ordered
	 */
	protected static final int HIRE_RATE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getHireRate() <em>Hire Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHireRate()
	 * @generated
	 * @ordered
	 */
	protected int hireRate = HIRE_RATE_EDEFAULT;

	/**
	 * The default value of the '{@link #getHireCost() <em>Hire Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHireCost()
	 * @generated
	 * @ordered
	 */
	protected static final int HIRE_COST_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getHireCost() <em>Hire Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHireCost()
	 * @generated
	 * @ordered
	 */
	protected int hireCost = HIRE_COST_EDEFAULT;

	/**
	 * The default value of the '{@link #getRouteTaken() <em>Route Taken</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRouteTaken()
	 * @generated
	 * @ordered
	 */
	protected static final String ROUTE_TAKEN_EDEFAULT = "";

	/**
	 * The cached value of the '{@link #getRouteTaken() <em>Route Taken</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRouteTaken()
	 * @generated
	 * @ordered
	 */
	protected String routeTaken = ROUTE_TAKEN_EDEFAULT;

	/**
	 * The default value of the '{@link #getCanalCost() <em>Canal Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCanalCost()
	 * @generated
	 * @ordered
	 */
	protected static final int CANAL_COST_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getCanalCost() <em>Canal Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCanalCost()
	 * @generated
	 * @ordered
	 */
	protected int canalCost = CANAL_COST_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected NotionalJourneyContractDetailsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchedulePackage.Literals.NOTIONAL_JOURNEY_CONTRACT_DETAILS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getReturnPort() {
		return returnPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setReturnPort(String newReturnPort) {
		String oldReturnPort = returnPort;
		returnPort = newReturnPort;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.NOTIONAL_JOURNEY_CONTRACT_DETAILS__RETURN_PORT, oldReturnPort, returnPort));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getDistance() {
		return distance;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDistance(int newDistance) {
		int oldDistance = distance;
		distance = newDistance;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.NOTIONAL_JOURNEY_CONTRACT_DETAILS__DISTANCE, oldDistance, distance));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getTotalTimeInDays() {
		return totalTimeInDays;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTotalTimeInDays(double newTotalTimeInDays) {
		double oldTotalTimeInDays = totalTimeInDays;
		totalTimeInDays = newTotalTimeInDays;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.NOTIONAL_JOURNEY_CONTRACT_DETAILS__TOTAL_TIME_IN_DAYS, oldTotalTimeInDays, totalTimeInDays));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getTotalFuelUsed() {
		return totalFuelUsed;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTotalFuelUsed(int newTotalFuelUsed) {
		int oldTotalFuelUsed = totalFuelUsed;
		totalFuelUsed = newTotalFuelUsed;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.NOTIONAL_JOURNEY_CONTRACT_DETAILS__TOTAL_FUEL_USED, oldTotalFuelUsed, totalFuelUsed));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getFuelPrice() {
		return fuelPrice;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFuelPrice(double newFuelPrice) {
		double oldFuelPrice = fuelPrice;
		fuelPrice = newFuelPrice;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.NOTIONAL_JOURNEY_CONTRACT_DETAILS__FUEL_PRICE, oldFuelPrice, fuelPrice));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getTotalFuelCost() {
		return totalFuelCost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTotalFuelCost(int newTotalFuelCost) {
		int oldTotalFuelCost = totalFuelCost;
		totalFuelCost = newTotalFuelCost;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.NOTIONAL_JOURNEY_CONTRACT_DETAILS__TOTAL_FUEL_COST, oldTotalFuelCost, totalFuelCost));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getHireRate() {
		return hireRate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setHireRate(int newHireRate) {
		int oldHireRate = hireRate;
		hireRate = newHireRate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.NOTIONAL_JOURNEY_CONTRACT_DETAILS__HIRE_RATE, oldHireRate, hireRate));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getHireCost() {
		return hireCost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setHireCost(int newHireCost) {
		int oldHireCost = hireCost;
		hireCost = newHireCost;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.NOTIONAL_JOURNEY_CONTRACT_DETAILS__HIRE_COST, oldHireCost, hireCost));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getRouteTaken() {
		return routeTaken;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRouteTaken(String newRouteTaken) {
		String oldRouteTaken = routeTaken;
		routeTaken = newRouteTaken;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.NOTIONAL_JOURNEY_CONTRACT_DETAILS__ROUTE_TAKEN, oldRouteTaken, routeTaken));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getCanalCost() {
		return canalCost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCanalCost(int newCanalCost) {
		int oldCanalCost = canalCost;
		canalCost = newCanalCost;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.NOTIONAL_JOURNEY_CONTRACT_DETAILS__CANAL_COST, oldCanalCost, canalCost));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SchedulePackage.NOTIONAL_JOURNEY_CONTRACT_DETAILS__RETURN_PORT:
				return getReturnPort();
			case SchedulePackage.NOTIONAL_JOURNEY_CONTRACT_DETAILS__DISTANCE:
				return getDistance();
			case SchedulePackage.NOTIONAL_JOURNEY_CONTRACT_DETAILS__TOTAL_TIME_IN_DAYS:
				return getTotalTimeInDays();
			case SchedulePackage.NOTIONAL_JOURNEY_CONTRACT_DETAILS__TOTAL_FUEL_USED:
				return getTotalFuelUsed();
			case SchedulePackage.NOTIONAL_JOURNEY_CONTRACT_DETAILS__FUEL_PRICE:
				return getFuelPrice();
			case SchedulePackage.NOTIONAL_JOURNEY_CONTRACT_DETAILS__TOTAL_FUEL_COST:
				return getTotalFuelCost();
			case SchedulePackage.NOTIONAL_JOURNEY_CONTRACT_DETAILS__HIRE_RATE:
				return getHireRate();
			case SchedulePackage.NOTIONAL_JOURNEY_CONTRACT_DETAILS__HIRE_COST:
				return getHireCost();
			case SchedulePackage.NOTIONAL_JOURNEY_CONTRACT_DETAILS__ROUTE_TAKEN:
				return getRouteTaken();
			case SchedulePackage.NOTIONAL_JOURNEY_CONTRACT_DETAILS__CANAL_COST:
				return getCanalCost();
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
			case SchedulePackage.NOTIONAL_JOURNEY_CONTRACT_DETAILS__RETURN_PORT:
				setReturnPort((String)newValue);
				return;
			case SchedulePackage.NOTIONAL_JOURNEY_CONTRACT_DETAILS__DISTANCE:
				setDistance((Integer)newValue);
				return;
			case SchedulePackage.NOTIONAL_JOURNEY_CONTRACT_DETAILS__TOTAL_TIME_IN_DAYS:
				setTotalTimeInDays((Double)newValue);
				return;
			case SchedulePackage.NOTIONAL_JOURNEY_CONTRACT_DETAILS__TOTAL_FUEL_USED:
				setTotalFuelUsed((Integer)newValue);
				return;
			case SchedulePackage.NOTIONAL_JOURNEY_CONTRACT_DETAILS__FUEL_PRICE:
				setFuelPrice((Double)newValue);
				return;
			case SchedulePackage.NOTIONAL_JOURNEY_CONTRACT_DETAILS__TOTAL_FUEL_COST:
				setTotalFuelCost((Integer)newValue);
				return;
			case SchedulePackage.NOTIONAL_JOURNEY_CONTRACT_DETAILS__HIRE_RATE:
				setHireRate((Integer)newValue);
				return;
			case SchedulePackage.NOTIONAL_JOURNEY_CONTRACT_DETAILS__HIRE_COST:
				setHireCost((Integer)newValue);
				return;
			case SchedulePackage.NOTIONAL_JOURNEY_CONTRACT_DETAILS__ROUTE_TAKEN:
				setRouteTaken((String)newValue);
				return;
			case SchedulePackage.NOTIONAL_JOURNEY_CONTRACT_DETAILS__CANAL_COST:
				setCanalCost((Integer)newValue);
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
			case SchedulePackage.NOTIONAL_JOURNEY_CONTRACT_DETAILS__RETURN_PORT:
				setReturnPort(RETURN_PORT_EDEFAULT);
				return;
			case SchedulePackage.NOTIONAL_JOURNEY_CONTRACT_DETAILS__DISTANCE:
				setDistance(DISTANCE_EDEFAULT);
				return;
			case SchedulePackage.NOTIONAL_JOURNEY_CONTRACT_DETAILS__TOTAL_TIME_IN_DAYS:
				setTotalTimeInDays(TOTAL_TIME_IN_DAYS_EDEFAULT);
				return;
			case SchedulePackage.NOTIONAL_JOURNEY_CONTRACT_DETAILS__TOTAL_FUEL_USED:
				setTotalFuelUsed(TOTAL_FUEL_USED_EDEFAULT);
				return;
			case SchedulePackage.NOTIONAL_JOURNEY_CONTRACT_DETAILS__FUEL_PRICE:
				setFuelPrice(FUEL_PRICE_EDEFAULT);
				return;
			case SchedulePackage.NOTIONAL_JOURNEY_CONTRACT_DETAILS__TOTAL_FUEL_COST:
				setTotalFuelCost(TOTAL_FUEL_COST_EDEFAULT);
				return;
			case SchedulePackage.NOTIONAL_JOURNEY_CONTRACT_DETAILS__HIRE_RATE:
				setHireRate(HIRE_RATE_EDEFAULT);
				return;
			case SchedulePackage.NOTIONAL_JOURNEY_CONTRACT_DETAILS__HIRE_COST:
				setHireCost(HIRE_COST_EDEFAULT);
				return;
			case SchedulePackage.NOTIONAL_JOURNEY_CONTRACT_DETAILS__ROUTE_TAKEN:
				setRouteTaken(ROUTE_TAKEN_EDEFAULT);
				return;
			case SchedulePackage.NOTIONAL_JOURNEY_CONTRACT_DETAILS__CANAL_COST:
				setCanalCost(CANAL_COST_EDEFAULT);
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
			case SchedulePackage.NOTIONAL_JOURNEY_CONTRACT_DETAILS__RETURN_PORT:
				return RETURN_PORT_EDEFAULT == null ? returnPort != null : !RETURN_PORT_EDEFAULT.equals(returnPort);
			case SchedulePackage.NOTIONAL_JOURNEY_CONTRACT_DETAILS__DISTANCE:
				return distance != DISTANCE_EDEFAULT;
			case SchedulePackage.NOTIONAL_JOURNEY_CONTRACT_DETAILS__TOTAL_TIME_IN_DAYS:
				return totalTimeInDays != TOTAL_TIME_IN_DAYS_EDEFAULT;
			case SchedulePackage.NOTIONAL_JOURNEY_CONTRACT_DETAILS__TOTAL_FUEL_USED:
				return totalFuelUsed != TOTAL_FUEL_USED_EDEFAULT;
			case SchedulePackage.NOTIONAL_JOURNEY_CONTRACT_DETAILS__FUEL_PRICE:
				return fuelPrice != FUEL_PRICE_EDEFAULT;
			case SchedulePackage.NOTIONAL_JOURNEY_CONTRACT_DETAILS__TOTAL_FUEL_COST:
				return totalFuelCost != TOTAL_FUEL_COST_EDEFAULT;
			case SchedulePackage.NOTIONAL_JOURNEY_CONTRACT_DETAILS__HIRE_RATE:
				return hireRate != HIRE_RATE_EDEFAULT;
			case SchedulePackage.NOTIONAL_JOURNEY_CONTRACT_DETAILS__HIRE_COST:
				return hireCost != HIRE_COST_EDEFAULT;
			case SchedulePackage.NOTIONAL_JOURNEY_CONTRACT_DETAILS__ROUTE_TAKEN:
				return ROUTE_TAKEN_EDEFAULT == null ? routeTaken != null : !ROUTE_TAKEN_EDEFAULT.equals(routeTaken);
			case SchedulePackage.NOTIONAL_JOURNEY_CONTRACT_DETAILS__CANAL_COST:
				return canalCost != CANAL_COST_EDEFAULT;
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
		result.append(" (returnPort: ");
		result.append(returnPort);
		result.append(", distance: ");
		result.append(distance);
		result.append(", totalTimeInDays: ");
		result.append(totalTimeInDays);
		result.append(", totalFuelUsed: ");
		result.append(totalFuelUsed);
		result.append(", fuelPrice: ");
		result.append(fuelPrice);
		result.append(", totalFuelCost: ");
		result.append(totalFuelCost);
		result.append(", hireRate: ");
		result.append(hireRate);
		result.append(", hireCost: ");
		result.append(hireCost);
		result.append(", routeTaken: ");
		result.append(routeTaken);
		result.append(", canalCost: ");
		result.append(canalCost);
		result.append(')');
		return result.toString();
	}

} //NotionalJourneyContractDetailsImpl
