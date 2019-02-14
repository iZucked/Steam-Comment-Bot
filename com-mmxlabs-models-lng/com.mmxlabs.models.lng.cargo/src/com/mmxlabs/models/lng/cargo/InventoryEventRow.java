/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo;

import java.time.LocalDate;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Inventory Event Row</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.InventoryEventRow#getStartDate <em>Start Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.InventoryEventRow#getEndDate <em>End Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.InventoryEventRow#getPeriod <em>Period</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.InventoryEventRow#getCounterParty <em>Counter Party</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.InventoryEventRow#getReliability <em>Reliability</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.InventoryEventRow#getVolume <em>Volume</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.InventoryEventRow#getForecastDate <em>Forecast Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.InventoryEventRow#getVolumeLow <em>Volume Low</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.InventoryEventRow#getVolumeHigh <em>Volume High</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getInventoryEventRow()
 * @model
 * @generated
 */
public interface InventoryEventRow extends EObject {
	/**
	 * Returns the value of the '<em><b>Start Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Start Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start Date</em>' attribute.
	 * @see #setStartDate(LocalDate)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getInventoryEventRow_StartDate()
	 * @model dataType="com.mmxlabs.models.datetime.LocalDate"
	 * @generated
	 */
	LocalDate getStartDate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.InventoryEventRow#getStartDate <em>Start Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start Date</em>' attribute.
	 * @see #getStartDate()
	 * @generated
	 */
	void setStartDate(LocalDate value);

	/**
	 * Returns the value of the '<em><b>End Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>End Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>End Date</em>' attribute.
	 * @see #setEndDate(LocalDate)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getInventoryEventRow_EndDate()
	 * @model dataType="com.mmxlabs.models.datetime.LocalDate"
	 * @generated
	 */
	LocalDate getEndDate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.InventoryEventRow#getEndDate <em>End Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>End Date</em>' attribute.
	 * @see #getEndDate()
	 * @generated
	 */
	void setEndDate(LocalDate value);

	/**
	 * Returns the value of the '<em><b>Period</b></em>' attribute.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.cargo.InventoryFrequency}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Period</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Period</em>' attribute.
	 * @see com.mmxlabs.models.lng.cargo.InventoryFrequency
	 * @see #setPeriod(InventoryFrequency)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getInventoryEventRow_Period()
	 * @model
	 * @generated
	 */
	InventoryFrequency getPeriod();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.InventoryEventRow#getPeriod <em>Period</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Period</em>' attribute.
	 * @see com.mmxlabs.models.lng.cargo.InventoryFrequency
	 * @see #getPeriod()
	 * @generated
	 */
	void setPeriod(InventoryFrequency value);

	/**
	 * Returns the value of the '<em><b>Counter Party</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Counter Party</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Counter Party</em>' attribute.
	 * @see #setCounterParty(String)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getInventoryEventRow_CounterParty()
	 * @model
	 * @generated
	 */
	String getCounterParty();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.InventoryEventRow#getCounterParty <em>Counter Party</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Counter Party</em>' attribute.
	 * @see #getCounterParty()
	 * @generated
	 */
	void setCounterParty(String value);

	/**
	 * Returns the value of the '<em><b>Reliability</b></em>' attribute.
	 * The default value is <code>"100.0"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Reliability</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Reliability</em>' attribute.
	 * @see #setReliability(double)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getInventoryEventRow_Reliability()
	 * @model default="100.0"
	 * @generated
	 */
	double getReliability();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.InventoryEventRow#getReliability <em>Reliability</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Reliability</em>' attribute.
	 * @see #getReliability()
	 * @generated
	 */
	void setReliability(double value);

	/**
	 * Returns the value of the '<em><b>Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Volume</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Volume</em>' attribute.
	 * @see #setVolume(int)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getInventoryEventRow_Volume()
	 * @model
	 * @generated
	 */
	int getVolume();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.InventoryEventRow#getVolume <em>Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Volume</em>' attribute.
	 * @see #getVolume()
	 * @generated
	 */
	void setVolume(int value);

	/**
	 * Returns the value of the '<em><b>Forecast Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Forecast Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Forecast Date</em>' attribute.
	 * @see #isSetForecastDate()
	 * @see #unsetForecastDate()
	 * @see #setForecastDate(LocalDate)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getInventoryEventRow_ForecastDate()
	 * @model unsettable="true" dataType="com.mmxlabs.models.datetime.LocalDate"
	 * @generated
	 */
	LocalDate getForecastDate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.InventoryEventRow#getForecastDate <em>Forecast Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Forecast Date</em>' attribute.
	 * @see #isSetForecastDate()
	 * @see #unsetForecastDate()
	 * @see #getForecastDate()
	 * @generated
	 */
	void setForecastDate(LocalDate value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.InventoryEventRow#getForecastDate <em>Forecast Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetForecastDate()
	 * @see #getForecastDate()
	 * @see #setForecastDate(LocalDate)
	 * @generated
	 */
	void unsetForecastDate();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.InventoryEventRow#getForecastDate <em>Forecast Date</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Forecast Date</em>' attribute is set.
	 * @see #unsetForecastDate()
	 * @see #getForecastDate()
	 * @see #setForecastDate(LocalDate)
	 * @generated
	 */
	boolean isSetForecastDate();

	/**
	 * Returns the value of the '<em><b>Volume Low</b></em>' attribute.
	 * The default value is <code>"0"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Volume Low</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Volume Low</em>' attribute.
	 * @see #setVolumeLow(int)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getInventoryEventRow_VolumeLow()
	 * @model default="0"
	 * @generated
	 */
	int getVolumeLow();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.InventoryEventRow#getVolumeLow <em>Volume Low</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Volume Low</em>' attribute.
	 * @see #getVolumeLow()
	 * @generated
	 */
	void setVolumeLow(int value);

	/**
	 * Returns the value of the '<em><b>Volume High</b></em>' attribute.
	 * The default value is <code>"0"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Volume High</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Volume High</em>' attribute.
	 * @see #setVolumeHigh(int)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getInventoryEventRow_VolumeHigh()
	 * @model default="0"
	 * @generated
	 */
	int getVolumeHigh();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.InventoryEventRow#getVolumeHigh <em>Volume High</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Volume High</em>' attribute.
	 * @see #getVolumeHigh()
	 * @generated
	 */
	void setVolumeHigh(int value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	int getReliableVolume();

} // InventoryEventRow
