/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.port;

import scenario.NamedObject;

import scenario.UUIDObject;
import scenario.contract.Contract;

import scenario.market.Index;

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
 * </ul>
 * </p>
 *
 * @see scenario.port.PortPackage#getPort()
 * @model
 * @generated
 */
public interface Port extends UUIDObject, NamedObject {
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
	 * @see #setRegasEfficiency(float)
	 * @see scenario.port.PortPackage#getPort_RegasEfficiency()
	 * @model default="1.0" required="true"
	 * @generated
	 */
	float getRegasEfficiency();

	/**
	 * Sets the value of the '{@link scenario.port.Port#getRegasEfficiency <em>Regas Efficiency</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Regas Efficiency</em>' attribute.
	 * @see #getRegasEfficiency()
	 * @generated
	 */
	void setRegasEfficiency(float value);

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

} // Port
