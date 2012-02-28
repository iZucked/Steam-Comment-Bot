/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port;

import com.mmxlabs.models.lng.types.APort;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Port</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.port.Port#getCapabilities <em>Capabilities</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.Port#getTimeZone <em>Time Zone</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.Port#getLoadDuration <em>Load Duration</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.Port#getDischargeDuration <em>Discharge Duration</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.Port#getCvValue <em>Cv Value</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.Port#getDefaultStartTime <em>Default Start Time</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.Port#isAllowCooldown <em>Allow Cooldown</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.port.PortPackage#getPort()
 * @model
 * @generated
 */
public interface Port extends APort {

	/**
	 * Returns the value of the '<em><b>Capabilities</b></em>' attribute list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.port.PortCapability}.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.port.PortCapability}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Capabilities</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Capabilities</em>' attribute list.
	 * @see com.mmxlabs.models.lng.port.PortCapability
	 * @see com.mmxlabs.models.lng.port.PortPackage#getPort_Capabilities()
	 * @model
	 * @generated
	 */
	EList<PortCapability> getCapabilities();

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
	 * @see com.mmxlabs.models.lng.port.PortPackage#getPort_TimeZone()
	 * @model required="true"
	 * @generated
	 */
	String getTimeZone();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.port.Port#getTimeZone <em>Time Zone</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Time Zone</em>' attribute.
	 * @see #getTimeZone()
	 * @generated
	 */
	void setTimeZone(String value);

	/**
	 * Returns the value of the '<em><b>Load Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Load Duration</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Load Duration</em>' attribute.
	 * @see #setLoadDuration(int)
	 * @see com.mmxlabs.models.lng.port.PortPackage#getPort_LoadDuration()
	 * @model required="true"
	 * @generated
	 */
	int getLoadDuration();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.port.Port#getLoadDuration <em>Load Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Load Duration</em>' attribute.
	 * @see #getLoadDuration()
	 * @generated
	 */
	void setLoadDuration(int value);

	/**
	 * Returns the value of the '<em><b>Discharge Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Discharge Duration</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Discharge Duration</em>' attribute.
	 * @see #setDischargeDuration(int)
	 * @see com.mmxlabs.models.lng.port.PortPackage#getPort_DischargeDuration()
	 * @model required="true"
	 * @generated
	 */
	int getDischargeDuration();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.port.Port#getDischargeDuration <em>Discharge Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Discharge Duration</em>' attribute.
	 * @see #getDischargeDuration()
	 * @generated
	 */
	void setDischargeDuration(int value);

	/**
	 * Returns the value of the '<em><b>Cv Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cv Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cv Value</em>' attribute.
	 * @see #setCvValue(double)
	 * @see com.mmxlabs.models.lng.port.PortPackage#getPort_CvValue()
	 * @model required="true"
	 * @generated
	 */
	double getCvValue();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.port.Port#getCvValue <em>Cv Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cv Value</em>' attribute.
	 * @see #getCvValue()
	 * @generated
	 */
	void setCvValue(double value);

	/**
	 * Returns the value of the '<em><b>Default Start Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Default Start Time</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Default Start Time</em>' attribute.
	 * @see #setDefaultStartTime(int)
	 * @see com.mmxlabs.models.lng.port.PortPackage#getPort_DefaultStartTime()
	 * @model required="true"
	 * @generated
	 */
	int getDefaultStartTime();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.port.Port#getDefaultStartTime <em>Default Start Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Default Start Time</em>' attribute.
	 * @see #getDefaultStartTime()
	 * @generated
	 */
	void setDefaultStartTime(int value);

	/**
	 * Returns the value of the '<em><b>Allow Cooldown</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Allow Cooldown</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Allow Cooldown</em>' attribute.
	 * @see #setAllowCooldown(boolean)
	 * @see com.mmxlabs.models.lng.port.PortPackage#getPort_AllowCooldown()
	 * @model required="true"
	 * @generated
	 */
	boolean isAllowCooldown();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.port.Port#isAllowCooldown <em>Allow Cooldown</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Allow Cooldown</em>' attribute.
	 * @see #isAllowCooldown()
	 * @generated
	 */
	void setAllowCooldown(boolean value);
} // Port
