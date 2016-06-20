/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port;

import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.mmxcore.OtherNamesObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Port</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.port.Port#getShortName <em>Short Name</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.Port#getCapabilities <em>Capabilities</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.Port#getTimeZone <em>Time Zone</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.Port#getLoadDuration <em>Load Duration</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.Port#getDischargeDuration <em>Discharge Duration</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.Port#getBerths <em>Berths</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.Port#getCvValue <em>Cv Value</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.Port#getDefaultStartTime <em>Default Start Time</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.Port#isAllowCooldown <em>Allow Cooldown</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.Port#getDefaultWindowSize <em>Default Window Size</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.Port#getDefaultWindowSizeUnits <em>Default Window Size Units</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.Port#getLocation <em>Location</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.Port#getAtobviacCode <em>Atobviac Code</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.Port#getDataloyCode <em>Dataloy Code</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.Port#getVesonCode <em>Veson Code</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.Port#getExternalCode <em>External Code</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.Port#getUNLocode <em>UN Locode</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.Port#getMinCvValue <em>Min Cv Value</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.Port#getMaxCvValue <em>Max Cv Value</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.port.PortPackage#getPort()
 * @model annotation="http://www.mmxlabs.com/models/mmxcore/annotations/namedobject showOtherNames='true'"
 * @generated
 */
public interface Port extends APortSet<Port>, OtherNamesObject {

	/**
	 * Returns the value of the '<em><b>Capabilities</b></em>' attribute list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.PortCapability}.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.types.PortCapability}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Capabilities</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Capabilities</em>' attribute list.
	 * @see com.mmxlabs.models.lng.types.PortCapability
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
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='hours' formatString='##,##0'"
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
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='hours' formatString='##,##0'"
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
	 * Returns the value of the '<em><b>Berths</b></em>' attribute.
	 * The default value is <code>"1"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Berths</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Berths</em>' attribute.
	 * @see #setBerths(int)
	 * @see com.mmxlabs.models.lng.port.PortPackage#getPort_Berths()
	 * @model default="1"
	 * @generated
	 */
	int getBerths();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.port.Port#getBerths <em>Berths</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Berths</em>' attribute.
	 * @see #getBerths()
	 * @generated
	 */
	void setBerths(int value);

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
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='mmBtu/m\263' formatString='#0.###'"
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

	/**
	 * Returns the value of the '<em><b>Default Window Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Default Window Size</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Default Window Size</em>' attribute.
	 * @see #setDefaultWindowSize(int)
	 * @see com.mmxlabs.models.lng.port.PortPackage#getPort_DefaultWindowSize()
	 * @model required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='hours' formatString='##,##0'"
	 * @generated
	 */
	int getDefaultWindowSize();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.port.Port#getDefaultWindowSize <em>Default Window Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Default Window Size</em>' attribute.
	 * @see #getDefaultWindowSize()
	 * @generated
	 */
	void setDefaultWindowSize(int value);

	/**
	 * Returns the value of the '<em><b>Default Window Size Units</b></em>' attribute.
	 * The default value is <code>"HOURS"</code>.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.types.TimePeriod}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Default Window Size Units</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Default Window Size Units</em>' attribute.
	 * @see com.mmxlabs.models.lng.types.TimePeriod
	 * @see #setDefaultWindowSizeUnits(TimePeriod)
	 * @see com.mmxlabs.models.lng.port.PortPackage#getPort_DefaultWindowSizeUnits()
	 * @model default="HOURS" required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='hours' formatString='##,##0'"
	 * @generated
	 */
	TimePeriod getDefaultWindowSizeUnits();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.port.Port#getDefaultWindowSizeUnits <em>Default Window Size Units</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Default Window Size Units</em>' attribute.
	 * @see com.mmxlabs.models.lng.types.TimePeriod
	 * @see #getDefaultWindowSizeUnits()
	 * @generated
	 */
	void setDefaultWindowSizeUnits(TimePeriod value);

	/**
	 * Returns the value of the '<em><b>Location</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Location</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Location</em>' containment reference.
	 * @see #setLocation(Location)
	 * @see com.mmxlabs.models.lng.port.PortPackage#getPort_Location()
	 * @model containment="true" required="true"
	 * @generated
	 */
	Location getLocation();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.port.Port#getLocation <em>Location</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Location</em>' containment reference.
	 * @see #getLocation()
	 * @generated
	 */
	void setLocation(Location value);

	/**
	 * Returns the value of the '<em><b>Atobviac Code</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Atobviac Code</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Atobviac Code</em>' attribute.
	 * @see #setAtobviacCode(String)
	 * @see com.mmxlabs.models.lng.port.PortPackage#getPort_AtobviacCode()
	 * @model
	 * @generated
	 */
	String getAtobviacCode();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.port.Port#getAtobviacCode <em>Atobviac Code</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Atobviac Code</em>' attribute.
	 * @see #getAtobviacCode()
	 * @generated
	 */
	void setAtobviacCode(String value);

	/**
	 * Returns the value of the '<em><b>Dataloy Code</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Dataloy Code</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Dataloy Code</em>' attribute.
	 * @see #setDataloyCode(String)
	 * @see com.mmxlabs.models.lng.port.PortPackage#getPort_DataloyCode()
	 * @model
	 * @generated
	 */
	String getDataloyCode();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.port.Port#getDataloyCode <em>Dataloy Code</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Dataloy Code</em>' attribute.
	 * @see #getDataloyCode()
	 * @generated
	 */
	void setDataloyCode(String value);

	/**
	 * Returns the value of the '<em><b>Veson Code</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Veson Code</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Veson Code</em>' attribute.
	 * @see #setVesonCode(String)
	 * @see com.mmxlabs.models.lng.port.PortPackage#getPort_VesonCode()
	 * @model
	 * @generated
	 */
	String getVesonCode();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.port.Port#getVesonCode <em>Veson Code</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Veson Code</em>' attribute.
	 * @see #getVesonCode()
	 * @generated
	 */
	void setVesonCode(String value);

	/**
	 * Returns the value of the '<em><b>External Code</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>External Code</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>External Code</em>' attribute.
	 * @see #setExternalCode(String)
	 * @see com.mmxlabs.models.lng.port.PortPackage#getPort_ExternalCode()
	 * @model
	 * @generated
	 */
	String getExternalCode();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.port.Port#getExternalCode <em>External Code</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>External Code</em>' attribute.
	 * @see #getExternalCode()
	 * @generated
	 */
	void setExternalCode(String value);

	/**
	 * Returns the value of the '<em><b>UN Locode</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>UN Locode</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>UN Locode</em>' attribute.
	 * @see #setUNLocode(String)
	 * @see com.mmxlabs.models.lng.port.PortPackage#getPort_UNLocode()
	 * @model
	 * @generated
	 */
	String getUNLocode();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.port.Port#getUNLocode <em>UN Locode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>UN Locode</em>' attribute.
	 * @see #getUNLocode()
	 * @generated
	 */
	void setUNLocode(String value);

	/**
	 * Returns the value of the '<em><b>Min Cv Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Min Cv Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Min Cv Value</em>' attribute.
	 * @see #isSetMinCvValue()
	 * @see #unsetMinCvValue()
	 * @see #setMinCvValue(double)
	 * @see com.mmxlabs.models.lng.port.PortPackage#getPort_MinCvValue()
	 * @model unsettable="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat formatString='#0.###'"
	 * @generated
	 */
	double getMinCvValue();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.port.Port#getMinCvValue <em>Min Cv Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Min Cv Value</em>' attribute.
	 * @see #isSetMinCvValue()
	 * @see #unsetMinCvValue()
	 * @see #getMinCvValue()
	 * @generated
	 */
	void setMinCvValue(double value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.port.Port#getMinCvValue <em>Min Cv Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetMinCvValue()
	 * @see #getMinCvValue()
	 * @see #setMinCvValue(double)
	 * @generated
	 */
	void unsetMinCvValue();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.port.Port#getMinCvValue <em>Min Cv Value</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Min Cv Value</em>' attribute is set.
	 * @see #unsetMinCvValue()
	 * @see #getMinCvValue()
	 * @see #setMinCvValue(double)
	 * @generated
	 */
	boolean isSetMinCvValue();

	/**
	 * Returns the value of the '<em><b>Max Cv Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Max Cv Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Max Cv Value</em>' attribute.
	 * @see #isSetMaxCvValue()
	 * @see #unsetMaxCvValue()
	 * @see #setMaxCvValue(double)
	 * @see com.mmxlabs.models.lng.port.PortPackage#getPort_MaxCvValue()
	 * @model unsettable="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat formatString='#0.###'"
	 * @generated
	 */
	double getMaxCvValue();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.port.Port#getMaxCvValue <em>Max Cv Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Max Cv Value</em>' attribute.
	 * @see #isSetMaxCvValue()
	 * @see #unsetMaxCvValue()
	 * @see #getMaxCvValue()
	 * @generated
	 */
	void setMaxCvValue(double value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.port.Port#getMaxCvValue <em>Max Cv Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetMaxCvValue()
	 * @see #getMaxCvValue()
	 * @see #setMaxCvValue(double)
	 * @generated
	 */
	void unsetMaxCvValue();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.port.Port#getMaxCvValue <em>Max Cv Value</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Max Cv Value</em>' attribute is set.
	 * @see #unsetMaxCvValue()
	 * @see #getMaxCvValue()
	 * @see #setMaxCvValue(double)
	 * @generated
	 */
	boolean isSetMaxCvValue();

	/**
	 * Returns the value of the '<em><b>Short Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Short Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Short Name</em>' attribute.
	 * @see #setShortName(String)
	 * @see com.mmxlabs.models.lng.port.PortPackage#getPort_ShortName()
	 * @model
	 * @generated
	 */
	String getShortName();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.port.Port#getShortName <em>Short Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Short Name</em>' attribute.
	 * @see #getShortName()
	 * @generated
	 */
	void setShortName(String value);
} // Port
