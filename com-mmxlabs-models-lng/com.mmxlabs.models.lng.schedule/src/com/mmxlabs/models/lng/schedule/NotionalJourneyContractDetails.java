/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.schedule;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Notional Journey Contract Details</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.NotionalJourneyContractDetails#getReturnPort <em>Return Port</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.NotionalJourneyContractDetails#getDistance <em>Distance</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.NotionalJourneyContractDetails#getTotalTimeInDays <em>Total Time In Days</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.NotionalJourneyContractDetails#getTotalFuelUsed <em>Total Fuel Used</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.NotionalJourneyContractDetails#getFuelPrice <em>Fuel Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.NotionalJourneyContractDetails#getTotalFuelCost <em>Total Fuel Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.NotionalJourneyContractDetails#getHireRate <em>Hire Rate</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.NotionalJourneyContractDetails#getHireCost <em>Hire Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.NotionalJourneyContractDetails#getRouteTaken <em>Route Taken</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.NotionalJourneyContractDetails#getCanalCost <em>Canal Cost</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getNotionalJourneyContractDetails()
 * @model
 * @generated
 */
public interface NotionalJourneyContractDetails extends MatchingContractDetails {
	/**
	 * Returns the value of the '<em><b>Return Port</b></em>' attribute.
	 * The default value is <code>""</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Return Port</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Return Port</em>' attribute.
	 * @see #setReturnPort(String)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getNotionalJourneyContractDetails_ReturnPort()
	 * @model default="" required="true"
	 *        annotation="http://www.mmxlabs.com/models/pricing/expressionType type='basefuel'"
	 * @generated
	 */
	String getReturnPort();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.NotionalJourneyContractDetails#getReturnPort <em>Return Port</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Return Port</em>' attribute.
	 * @see #getReturnPort()
	 * @generated
	 */
	void setReturnPort(String value);

	/**
	 * Returns the value of the '<em><b>Distance</b></em>' attribute.
	 * The default value is <code>"0"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Distance</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Distance</em>' attribute.
	 * @see #setDistance(int)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getNotionalJourneyContractDetails_Distance()
	 * @model default="0" required="true"
	 * @generated
	 */
	int getDistance();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.NotionalJourneyContractDetails#getDistance <em>Distance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Distance</em>' attribute.
	 * @see #getDistance()
	 * @generated
	 */
	void setDistance(int value);

	/**
	 * Returns the value of the '<em><b>Total Time In Days</b></em>' attribute.
	 * The default value is <code>"0"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Total Time In Days</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Total Time In Days</em>' attribute.
	 * @see #setTotalTimeInDays(double)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getNotionalJourneyContractDetails_TotalTimeInDays()
	 * @model default="0" required="true"
	 * @generated
	 */
	double getTotalTimeInDays();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.NotionalJourneyContractDetails#getTotalTimeInDays <em>Total Time In Days</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Total Time In Days</em>' attribute.
	 * @see #getTotalTimeInDays()
	 * @generated
	 */
	void setTotalTimeInDays(double value);

	/**
	 * Returns the value of the '<em><b>Total Fuel Used</b></em>' attribute.
	 * The default value is <code>"0"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Total Fuel Used</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Total Fuel Used</em>' attribute.
	 * @see #setTotalFuelUsed(int)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getNotionalJourneyContractDetails_TotalFuelUsed()
	 * @model default="0" required="true"
	 * @generated
	 */
	int getTotalFuelUsed();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.NotionalJourneyContractDetails#getTotalFuelUsed <em>Total Fuel Used</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Total Fuel Used</em>' attribute.
	 * @see #getTotalFuelUsed()
	 * @generated
	 */
	void setTotalFuelUsed(int value);

	/**
	 * Returns the value of the '<em><b>Fuel Price</b></em>' attribute.
	 * The default value is <code>"0"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fuel Price</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fuel Price</em>' attribute.
	 * @see #setFuelPrice(double)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getNotionalJourneyContractDetails_FuelPrice()
	 * @model default="0" required="true"
	 * @generated
	 */
	double getFuelPrice();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.NotionalJourneyContractDetails#getFuelPrice <em>Fuel Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fuel Price</em>' attribute.
	 * @see #getFuelPrice()
	 * @generated
	 */
	void setFuelPrice(double value);

	/**
	 * Returns the value of the '<em><b>Total Fuel Cost</b></em>' attribute.
	 * The default value is <code>"0"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Total Fuel Cost</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Total Fuel Cost</em>' attribute.
	 * @see #setTotalFuelCost(int)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getNotionalJourneyContractDetails_TotalFuelCost()
	 * @model default="0" required="true"
	 * @generated
	 */
	int getTotalFuelCost();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.NotionalJourneyContractDetails#getTotalFuelCost <em>Total Fuel Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Total Fuel Cost</em>' attribute.
	 * @see #getTotalFuelCost()
	 * @generated
	 */
	void setTotalFuelCost(int value);

	/**
	 * Returns the value of the '<em><b>Hire Rate</b></em>' attribute.
	 * The default value is <code>"0"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Hire Rate</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Hire Rate</em>' attribute.
	 * @see #setHireRate(int)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getNotionalJourneyContractDetails_HireRate()
	 * @model default="0" required="true"
	 * @generated
	 */
	int getHireRate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.NotionalJourneyContractDetails#getHireRate <em>Hire Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Hire Rate</em>' attribute.
	 * @see #getHireRate()
	 * @generated
	 */
	void setHireRate(int value);

	/**
	 * Returns the value of the '<em><b>Hire Cost</b></em>' attribute.
	 * The default value is <code>"0"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Hire Cost</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Hire Cost</em>' attribute.
	 * @see #setHireCost(int)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getNotionalJourneyContractDetails_HireCost()
	 * @model default="0" required="true"
	 * @generated
	 */
	int getHireCost();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.NotionalJourneyContractDetails#getHireCost <em>Hire Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Hire Cost</em>' attribute.
	 * @see #getHireCost()
	 * @generated
	 */
	void setHireCost(int value);

	/**
	 * Returns the value of the '<em><b>Route Taken</b></em>' attribute.
	 * The default value is <code>""</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Route Taken</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Route Taken</em>' attribute.
	 * @see #setRouteTaken(String)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getNotionalJourneyContractDetails_RouteTaken()
	 * @model default="" required="true"
	 *        annotation="http://www.mmxlabs.com/models/pricing/expressionType type='basefuel'"
	 * @generated
	 */
	String getRouteTaken();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.NotionalJourneyContractDetails#getRouteTaken <em>Route Taken</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Route Taken</em>' attribute.
	 * @see #getRouteTaken()
	 * @generated
	 */
	void setRouteTaken(String value);

	/**
	 * Returns the value of the '<em><b>Canal Cost</b></em>' attribute.
	 * The default value is <code>"0"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Canal Cost</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Canal Cost</em>' attribute.
	 * @see #setCanalCost(int)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getNotionalJourneyContractDetails_CanalCost()
	 * @model default="0" required="true"
	 * @generated
	 */
	int getCanalCost();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.NotionalJourneyContractDetails#getCanalCost <em>Canal Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Canal Cost</em>' attribute.
	 * @see #getCanalCost()
	 * @generated
	 */
	void setCanalCost(int value);

} // NotionalJourneyContractDetails
