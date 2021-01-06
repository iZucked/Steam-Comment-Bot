/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo;

import com.mmxlabs.models.lng.port.Port;

import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;

import com.mmxlabs.models.lng.types.VesselAssignmentType;

import com.mmxlabs.models.mmxcore.MMXObject;
import java.time.LocalDateTime;

import java.time.ZonedDateTime;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Charter In Market Override</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.CharterInMarketOverride#getCharterInMarket <em>Charter In Market</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.CharterInMarketOverride#getSpotIndex <em>Spot Index</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.CharterInMarketOverride#getStartHeel <em>Start Heel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.CharterInMarketOverride#getStartDate <em>Start Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.CharterInMarketOverride#getEndPort <em>End Port</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.CharterInMarketOverride#getEndDate <em>End Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.CharterInMarketOverride#getEndHeel <em>End Heel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.CharterInMarketOverride#isIncludeBallastBonus <em>Include Ballast Bonus</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.CharterInMarketOverride#getMinDuration <em>Min Duration</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.CharterInMarketOverride#getMaxDuration <em>Max Duration</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCharterInMarketOverride()
 * @model
 * @generated
 */
public interface CharterInMarketOverride extends MMXObject, VesselAssignmentType {
	/**
	 * Returns the value of the '<em><b>Charter In Market</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Charter In Market</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Charter In Market</em>' reference.
	 * @see #setCharterInMarket(CharterInMarket)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCharterInMarketOverride_CharterInMarket()
	 * @model
	 * @generated
	 */
	CharterInMarket getCharterInMarket();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.CharterInMarketOverride#getCharterInMarket <em>Charter In Market</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Charter In Market</em>' reference.
	 * @see #getCharterInMarket()
	 * @generated
	 */
	void setCharterInMarket(CharterInMarket value);

	/**
	 * Returns the value of the '<em><b>Spot Index</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Spot Index</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Spot Index</em>' attribute.
	 * @see #setSpotIndex(int)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCharterInMarketOverride_SpotIndex()
	 * @model
	 * @generated
	 */
	int getSpotIndex();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.CharterInMarketOverride#getSpotIndex <em>Spot Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Spot Index</em>' attribute.
	 * @see #getSpotIndex()
	 * @generated
	 */
	void setSpotIndex(int value);

	/**
	 * Returns the value of the '<em><b>Start Heel</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Start Heel</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start Heel</em>' containment reference.
	 * @see #isSetStartHeel()
	 * @see #unsetStartHeel()
	 * @see #setStartHeel(StartHeelOptions)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCharterInMarketOverride_StartHeel()
	 * @model containment="true" resolveProxies="true" unsettable="true" required="true"
	 * @generated
	 */
	StartHeelOptions getStartHeel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.CharterInMarketOverride#getStartHeel <em>Start Heel</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start Heel</em>' containment reference.
	 * @see #isSetStartHeel()
	 * @see #unsetStartHeel()
	 * @see #getStartHeel()
	 * @generated
	 */
	void setStartHeel(StartHeelOptions value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.CharterInMarketOverride#getStartHeel <em>Start Heel</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetStartHeel()
	 * @see #getStartHeel()
	 * @see #setStartHeel(StartHeelOptions)
	 * @generated
	 */
	void unsetStartHeel();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.CharterInMarketOverride#getStartHeel <em>Start Heel</em>}' containment reference is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Start Heel</em>' containment reference is set.
	 * @see #unsetStartHeel()
	 * @see #getStartHeel()
	 * @see #setStartHeel(StartHeelOptions)
	 * @generated
	 */
	boolean isSetStartHeel();

	/**
	 * Returns the value of the '<em><b>Start Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Start Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start Date</em>' attribute.
	 * @see #isSetStartDate()
	 * @see #unsetStartDate()
	 * @see #setStartDate(LocalDateTime)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCharterInMarketOverride_StartDate()
	 * @model unsettable="true" dataType="com.mmxlabs.models.datetime.LocalDateTime"
	 * @generated
	 */
	LocalDateTime getStartDate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.CharterInMarketOverride#getStartDate <em>Start Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start Date</em>' attribute.
	 * @see #isSetStartDate()
	 * @see #unsetStartDate()
	 * @see #getStartDate()
	 * @generated
	 */
	void setStartDate(LocalDateTime value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.CharterInMarketOverride#getStartDate <em>Start Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetStartDate()
	 * @see #getStartDate()
	 * @see #setStartDate(LocalDateTime)
	 * @generated
	 */
	void unsetStartDate();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.CharterInMarketOverride#getStartDate <em>Start Date</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Start Date</em>' attribute is set.
	 * @see #unsetStartDate()
	 * @see #getStartDate()
	 * @see #setStartDate(LocalDateTime)
	 * @generated
	 */
	boolean isSetStartDate();

	/**
	 * Returns the value of the '<em><b>End Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>End Port</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>End Port</em>' reference.
	 * @see #isSetEndPort()
	 * @see #unsetEndPort()
	 * @see #setEndPort(Port)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCharterInMarketOverride_EndPort()
	 * @model unsettable="true"
	 * @generated
	 */
	Port getEndPort();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.CharterInMarketOverride#getEndPort <em>End Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>End Port</em>' reference.
	 * @see #isSetEndPort()
	 * @see #unsetEndPort()
	 * @see #getEndPort()
	 * @generated
	 */
	void setEndPort(Port value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.CharterInMarketOverride#getEndPort <em>End Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetEndPort()
	 * @see #getEndPort()
	 * @see #setEndPort(Port)
	 * @generated
	 */
	void unsetEndPort();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.CharterInMarketOverride#getEndPort <em>End Port</em>}' reference is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>End Port</em>' reference is set.
	 * @see #unsetEndPort()
	 * @see #getEndPort()
	 * @see #setEndPort(Port)
	 * @generated
	 */
	boolean isSetEndPort();

	/**
	 * Returns the value of the '<em><b>End Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>End Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>End Date</em>' attribute.
	 * @see #isSetEndDate()
	 * @see #unsetEndDate()
	 * @see #setEndDate(LocalDateTime)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCharterInMarketOverride_EndDate()
	 * @model unsettable="true" dataType="com.mmxlabs.models.datetime.LocalDateTime"
	 * @generated
	 */
	LocalDateTime getEndDate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.CharterInMarketOverride#getEndDate <em>End Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>End Date</em>' attribute.
	 * @see #isSetEndDate()
	 * @see #unsetEndDate()
	 * @see #getEndDate()
	 * @generated
	 */
	void setEndDate(LocalDateTime value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.CharterInMarketOverride#getEndDate <em>End Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetEndDate()
	 * @see #getEndDate()
	 * @see #setEndDate(LocalDateTime)
	 * @generated
	 */
	void unsetEndDate();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.CharterInMarketOverride#getEndDate <em>End Date</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>End Date</em>' attribute is set.
	 * @see #unsetEndDate()
	 * @see #getEndDate()
	 * @see #setEndDate(LocalDateTime)
	 * @generated
	 */
	boolean isSetEndDate();

	/**
	 * Returns the value of the '<em><b>End Heel</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>End Heel</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>End Heel</em>' containment reference.
	 * @see #isSetEndHeel()
	 * @see #unsetEndHeel()
	 * @see #setEndHeel(EndHeelOptions)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCharterInMarketOverride_EndHeel()
	 * @model containment="true" resolveProxies="true" unsettable="true"
	 * @generated
	 */
	EndHeelOptions getEndHeel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.CharterInMarketOverride#getEndHeel <em>End Heel</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>End Heel</em>' containment reference.
	 * @see #isSetEndHeel()
	 * @see #unsetEndHeel()
	 * @see #getEndHeel()
	 * @generated
	 */
	void setEndHeel(EndHeelOptions value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.CharterInMarketOverride#getEndHeel <em>End Heel</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetEndHeel()
	 * @see #getEndHeel()
	 * @see #setEndHeel(EndHeelOptions)
	 * @generated
	 */
	void unsetEndHeel();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.CharterInMarketOverride#getEndHeel <em>End Heel</em>}' containment reference is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>End Heel</em>' containment reference is set.
	 * @see #unsetEndHeel()
	 * @see #getEndHeel()
	 * @see #setEndHeel(EndHeelOptions)
	 * @generated
	 */
	boolean isSetEndHeel();

	/**
	 * Returns the value of the '<em><b>Include Ballast Bonus</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Include Ballast Bonus</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Include Ballast Bonus</em>' attribute.
	 * @see #setIncludeBallastBonus(boolean)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCharterInMarketOverride_IncludeBallastBonus()
	 * @model
	 * @generated
	 */
	boolean isIncludeBallastBonus();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.CharterInMarketOverride#isIncludeBallastBonus <em>Include Ballast Bonus</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Include Ballast Bonus</em>' attribute.
	 * @see #isIncludeBallastBonus()
	 * @generated
	 */
	void setIncludeBallastBonus(boolean value);

	/**
	 * Returns the value of the '<em><b>Min Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Min Duration</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Min Duration</em>' attribute.
	 * @see #isSetMinDuration()
	 * @see #unsetMinDuration()
	 * @see #setMinDuration(int)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCharterInMarketOverride_MinDuration()
	 * @model unsettable="true" required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='days' formatString='##0'"
	 * @generated
	 */
	int getMinDuration();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.CharterInMarketOverride#getMinDuration <em>Min Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Min Duration</em>' attribute.
	 * @see #isSetMinDuration()
	 * @see #unsetMinDuration()
	 * @see #getMinDuration()
	 * @generated
	 */
	void setMinDuration(int value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.CharterInMarketOverride#getMinDuration <em>Min Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetMinDuration()
	 * @see #getMinDuration()
	 * @see #setMinDuration(int)
	 * @generated
	 */
	void unsetMinDuration();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.CharterInMarketOverride#getMinDuration <em>Min Duration</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Min Duration</em>' attribute is set.
	 * @see #unsetMinDuration()
	 * @see #getMinDuration()
	 * @see #setMinDuration(int)
	 * @generated
	 */
	boolean isSetMinDuration();

	/**
	 * Returns the value of the '<em><b>Max Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Max Duration</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Max Duration</em>' attribute.
	 * @see #isSetMaxDuration()
	 * @see #unsetMaxDuration()
	 * @see #setMaxDuration(int)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCharterInMarketOverride_MaxDuration()
	 * @model unsettable="true" required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='days' formatString='##0'"
	 * @generated
	 */
	int getMaxDuration();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.CharterInMarketOverride#getMaxDuration <em>Max Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Max Duration</em>' attribute.
	 * @see #isSetMaxDuration()
	 * @see #unsetMaxDuration()
	 * @see #getMaxDuration()
	 * @generated
	 */
	void setMaxDuration(int value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.CharterInMarketOverride#getMaxDuration <em>Max Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetMaxDuration()
	 * @see #getMaxDuration()
	 * @see #setMaxDuration(int)
	 * @generated
	 */
	void unsetMaxDuration();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.CharterInMarketOverride#getMaxDuration <em>Max Duration</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Max Duration</em>' attribute is set.
	 * @see #unsetMaxDuration()
	 * @see #getMaxDuration()
	 * @see #setMaxDuration(int)
	 * @generated
	 */
	boolean isSetMaxDuration();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" dataType="com.mmxlabs.models.datetime.DateTime"
	 * @generated
	 */
	ZonedDateTime getStartDateAsDateTime();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" dataType="com.mmxlabs.models.datetime.DateTime"
	 * @generated
	 */
	ZonedDateTime getEndDateAsDateTime();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	int getLocalOrDelegateMinDuration();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	int getLocalOrDelegateMaxDuration();

} // CharterInMarketOverride
