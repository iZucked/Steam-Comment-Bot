/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.port;

import scenario.AnnotatedObject;
import scenario.NamedObject;
import scenario.UUIDObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Port</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.port.Port#getTimeZone <em>Time Zone</em>}</li>
 *   <li>{@link scenario.port.Port#getRegasEfficiency <em>Regas Efficiency</em>}</li>
 *   <li>{@link scenario.port.Port#getDefaultCVvalue <em>Default CVvalue</em>}</li>
 *   <li>{@link scenario.port.Port#getDefaultWindowStart <em>Default Window Start</em>}</li>
 *   <li>{@link scenario.port.Port#getDefaultSlotDuration <em>Default Slot Duration</em>}</li>
 *   <li>{@link scenario.port.Port#isShouldArriveCold <em>Should Arrive Cold</em>}</li>
 *   <li>{@link scenario.port.Port#getDefaultLoadDuration <em>Default Load Duration</em>}</li>
 *   <li>{@link scenario.port.Port#getDefaultDischargeDuration <em>Default Discharge Duration</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.port.PortPackage#getPort()
 * @model
 * @generated
 */
public interface Port extends UUIDObject, NamedObject, AnnotatedObject {
	/**
	 * Returns the value of the '<em><b>Time Zone</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Time Zone</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Time Zone</em>' attribute.
	 * @see #setTimeZone(String)
	 * @see scenario.port.PortPackage#getPort_TimeZone()
	 * @model
	 * @generated
	 */
	String getTimeZone();

	/**
	 * Sets the value of the '{@link scenario.port.Port#getTimeZone <em>Time Zone</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Time Zone</em>' attribute.
	 * @see #getTimeZone()
	 * @generated
	 */
	void setTimeZone(String value);

	/**
	 * Returns the value of the '<em><b>Regas Efficiency</b></em>' attribute.
	 * The default value is <code>"1.0"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Regas Efficiency</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Regas Efficiency</em>' attribute.
	 * @see #setRegasEfficiency(Double)
	 * @see scenario.port.PortPackage#getPort_RegasEfficiency()
	 * @model default="1.0" dataType="scenario.Percentage" required="true"
	 * @generated
	 */
	Double getRegasEfficiency();

	/**
	 * Sets the value of the '{@link scenario.port.Port#getRegasEfficiency <em>Regas Efficiency</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Regas Efficiency</em>' attribute.
	 * @see #getRegasEfficiency()
	 * @generated
	 */
	void setRegasEfficiency(Double value);

	/**
	 * Returns the value of the '<em><b>Default CVvalue</b></em>' attribute.
	 * The default value is <code>"22.8"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Default CVvalue</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Default CVvalue</em>' attribute.
	 * @see #setDefaultCVvalue(float)
	 * @see scenario.port.PortPackage#getPort_DefaultCVvalue()
	 * @model default="22.8" required="true"
	 * @generated
	 */
	float getDefaultCVvalue();

	/**
	 * Sets the value of the '{@link scenario.port.Port#getDefaultCVvalue <em>Default CVvalue</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Default CVvalue</em>' attribute.
	 * @see #getDefaultCVvalue()
	 * @generated
	 */
	void setDefaultCVvalue(float value);

	/**
	 * Returns the value of the '<em><b>Default Window Start</b></em>' attribute.
	 * The default value is <code>"0"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Default Window Start</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Default Window Start</em>' attribute.
	 * @see #setDefaultWindowStart(int)
	 * @see scenario.port.PortPackage#getPort_DefaultWindowStart()
	 * @model default="0" required="true"
	 * @generated
	 */
	int getDefaultWindowStart();

	/**
	 * Sets the value of the '{@link scenario.port.Port#getDefaultWindowStart <em>Default Window Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Default Window Start</em>' attribute.
	 * @see #getDefaultWindowStart()
	 * @generated
	 */
	void setDefaultWindowStart(int value);

	/**
	 * Returns the value of the '<em><b>Default Slot Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Default Slot Duration</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Default Slot Duration</em>' attribute.
	 * @see #setDefaultSlotDuration(int)
	 * @see scenario.port.PortPackage#getPort_DefaultSlotDuration()
	 * @model required="true"
	 * @generated
	 */
	int getDefaultSlotDuration();

	/**
	 * Sets the value of the '{@link scenario.port.Port#getDefaultSlotDuration <em>Default Slot Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Default Slot Duration</em>' attribute.
	 * @see #getDefaultSlotDuration()
	 * @generated
	 */
	void setDefaultSlotDuration(int value);

	/**
	 * Returns the value of the '<em><b>Should Arrive Cold</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Should Arrive Cold</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Should Arrive Cold</em>' attribute.
	 * @see #setShouldArriveCold(boolean)
	 * @see scenario.port.PortPackage#getPort_ShouldArriveCold()
	 * @model required="true"
	 * @generated
	 */
	boolean isShouldArriveCold();

	/**
	 * Sets the value of the '{@link scenario.port.Port#isShouldArriveCold <em>Should Arrive Cold</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Should Arrive Cold</em>' attribute.
	 * @see #isShouldArriveCold()
	 * @generated
	 */
	void setShouldArriveCold(boolean value);

	/**
	 * Returns the value of the '<em><b>Default Load Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Default Load Duration</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Default Load Duration</em>' attribute.
	 * @see #setDefaultLoadDuration(int)
	 * @see scenario.port.PortPackage#getPort_DefaultLoadDuration()
	 * @model required="true"
	 * @generated
	 */
	int getDefaultLoadDuration();

	/**
	 * Sets the value of the '{@link scenario.port.Port#getDefaultLoadDuration <em>Default Load Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Default Load Duration</em>' attribute.
	 * @see #getDefaultLoadDuration()
	 * @generated
	 */
	void setDefaultLoadDuration(int value);

	/**
	 * Returns the value of the '<em><b>Default Discharge Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Default Discharge Duration</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Default Discharge Duration</em>' attribute.
	 * @see #setDefaultDischargeDuration(int)
	 * @see scenario.port.PortPackage#getPort_DefaultDischargeDuration()
	 * @model required="true"
	 * @generated
	 */
	int getDefaultDischargeDuration();

	/**
	 * Sets the value of the '{@link scenario.port.Port#getDefaultDischargeDuration <em>Default Discharge Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Default Discharge Duration</em>' attribute.
	 * @see #getDefaultDischargeDuration()
	 * @generated
	 */
	void setDefaultDischargeDuration(int value);

} // Port
