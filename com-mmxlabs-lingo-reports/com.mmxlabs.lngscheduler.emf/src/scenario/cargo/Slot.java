/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.cargo;

import java.util.Date;

import org.eclipse.emf.ecore.EObject;

import scenario.contract.Contract;
import scenario.port.Port;

import com.mmxlabs.lngscheduler.emf.datatypes.DateAndOptionalTime;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Slot</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.cargo.Slot#getId <em>Id</em>}</li>
 *   <li>{@link scenario.cargo.Slot#getPort <em>Port</em>}</li>
 *   <li>{@link scenario.cargo.Slot#getWindowStart <em>Window Start</em>}</li>
 *   <li>{@link scenario.cargo.Slot#getContract <em>Contract</em>}</li>
 *   <li>{@link scenario.cargo.Slot#getFixedPrice <em>Fixed Price</em>}</li>
 *   <li>{@link scenario.cargo.Slot#getMinQuantity <em>Min Quantity</em>}</li>
 *   <li>{@link scenario.cargo.Slot#getMaxQuantity <em>Max Quantity</em>}</li>
 *   <li>{@link scenario.cargo.Slot#getWindowDuration <em>Window Duration</em>}</li>
 *   <li>{@link scenario.cargo.Slot#getSlotDuration <em>Slot Duration</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.cargo.CargoPackage#getSlot()
 * @model
 * @generated
 */
public interface Slot extends EObject {
	/**
	 * Returns the value of the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Id</em>' attribute isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(String)
	 * @see scenario.cargo.CargoPackage#getSlot_Id()
	 * @model
	 * @generated
	 */
	String getId();

	/**
	 * Sets the value of the '{@link scenario.cargo.Slot#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(String value);

	/**
	 * Returns the value of the '<em><b>Min Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Min Quantity</em>' attribute isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Min Quantity</em>' attribute.
	 * @see #isSetMinQuantity()
	 * @see #unsetMinQuantity()
	 * @see #setMinQuantity(int)
	 * @see scenario.cargo.CargoPackage#getSlot_MinQuantity()
	 * @model unsettable="true"
	 * @generated
	 */
	int getMinQuantity();

	/**
	 * Sets the value of the '{@link scenario.cargo.Slot#getMinQuantity <em>Min Quantity</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Min Quantity</em>' attribute.
	 * @see #isSetMinQuantity()
	 * @see #unsetMinQuantity()
	 * @see #getMinQuantity()
	 * @generated
	 */
	void setMinQuantity(int value);

	/**
	 * Unsets the value of the '{@link scenario.cargo.Slot#getMinQuantity <em>Min Quantity</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #isSetMinQuantity()
	 * @see #getMinQuantity()
	 * @see #setMinQuantity(int)
	 * @generated
	 */
	void unsetMinQuantity();

	/**
	 * Returns whether the value of the '{@link scenario.cargo.Slot#getMinQuantity <em>Min Quantity</em>}' attribute is set.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return whether the value of the '<em>Min Quantity</em>' attribute is set.
	 * @see #unsetMinQuantity()
	 * @see #getMinQuantity()
	 * @see #setMinQuantity(int)
	 * @generated
	 */
	boolean isSetMinQuantity();

	/**
	 * Returns the value of the '<em><b>Max Quantity</b></em>' attribute.
	 * The default value is <code>"2147483647"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Max Quantity</em>' attribute isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Max Quantity</em>' attribute.
	 * @see #isSetMaxQuantity()
	 * @see #unsetMaxQuantity()
	 * @see #setMaxQuantity(int)
	 * @see scenario.cargo.CargoPackage#getSlot_MaxQuantity()
	 * @model default="2147483647" unsettable="true"
	 * @generated
	 */
	int getMaxQuantity();

	/**
	 * Sets the value of the '{@link scenario.cargo.Slot#getMaxQuantity <em>Max Quantity</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Max Quantity</em>' attribute.
	 * @see #isSetMaxQuantity()
	 * @see #unsetMaxQuantity()
	 * @see #getMaxQuantity()
	 * @generated
	 */
	void setMaxQuantity(int value);

	/**
	 * Unsets the value of the '{@link scenario.cargo.Slot#getMaxQuantity <em>Max Quantity</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #isSetMaxQuantity()
	 * @see #getMaxQuantity()
	 * @see #setMaxQuantity(int)
	 * @generated
	 */
	void unsetMaxQuantity();

	/**
	 * Returns whether the value of the '{@link scenario.cargo.Slot#getMaxQuantity <em>Max Quantity</em>}' attribute is set.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return whether the value of the '<em>Max Quantity</em>' attribute is set.
	 * @see #unsetMaxQuantity()
	 * @see #getMaxQuantity()
	 * @see #setMaxQuantity(int)
	 * @generated
	 */
	boolean isSetMaxQuantity();

	/**
	 * Returns the value of the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Port</em>' reference isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Port</em>' reference.
	 * @see #setPort(Port)
	 * @see scenario.cargo.CargoPackage#getSlot_Port()
	 * @model
	 * @generated
	 */
	Port getPort();

	/**
	 * Sets the value of the '{@link scenario.cargo.Slot#getPort <em>Port</em>}' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Port</em>' reference.
	 * @see #getPort()
	 * @generated
	 */
	void setPort(Port value);

	/**
	 * Returns the value of the '<em><b>Window Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Window Start</em>' attribute isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Window Start</em>' attribute.
	 * @see #setWindowStart(DateAndOptionalTime)
	 * @see scenario.cargo.CargoPackage#getSlot_WindowStart()
	 * @model dataType="scenario.DateAndOptionalTime"
	 * @generated
	 */
	DateAndOptionalTime getWindowStart();

	/**
	 * Sets the value of the '{@link scenario.cargo.Slot#getWindowStart <em>Window Start</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Window Start</em>' attribute.
	 * @see #getWindowStart()
	 * @generated
	 */
	void setWindowStart(DateAndOptionalTime value);

	/**
	 * Returns the value of the '<em><b>Window Duration</b></em>' attribute.
	 * The default value is <code>"24"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Window Duration</em>' attribute isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Window Duration</em>' attribute.
	 * @see #setWindowDuration(int)
	 * @see scenario.cargo.CargoPackage#getSlot_WindowDuration()
	 * @model default="24"
	 * @generated
	 */
	int getWindowDuration();

	/**
	 * Sets the value of the '{@link scenario.cargo.Slot#getWindowDuration <em>Window Duration</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Window Duration</em>' attribute.
	 * @see #getWindowDuration()
	 * @generated
	 */
	void setWindowDuration(int value);

	/**
	 * Returns the value of the '<em><b>Slot Duration</b></em>' attribute.
	 * The default value is <code>"6"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Slot Duration</em>' attribute isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Slot Duration</em>' attribute.
	 * @see #isSetSlotDuration()
	 * @see #unsetSlotDuration()
	 * @see #setSlotDuration(int)
	 * @see scenario.cargo.CargoPackage#getSlot_SlotDuration()
	 * @model default="6" unsettable="true"
	 * @generated
	 */
	int getSlotDuration();

	/**
	 * Sets the value of the '{@link scenario.cargo.Slot#getSlotDuration <em>Slot Duration</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Slot Duration</em>' attribute.
	 * @see #isSetSlotDuration()
	 * @see #unsetSlotDuration()
	 * @see #getSlotDuration()
	 * @generated
	 */
	void setSlotDuration(int value);

	/**
	 * Unsets the value of the '{@link scenario.cargo.Slot#getSlotDuration <em>Slot Duration</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #isSetSlotDuration()
	 * @see #getSlotDuration()
	 * @see #setSlotDuration(int)
	 * @generated
	 */
	void unsetSlotDuration();

	/**
	 * Returns whether the value of the '{@link scenario.cargo.Slot#getSlotDuration <em>Slot Duration</em>}' attribute is set.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return whether the value of the '<em>Slot Duration</em>' attribute is set.
	 * @see #unsetSlotDuration()
	 * @see #getSlotDuration()
	 * @see #setSlotDuration(int)
	 * @generated
	 */
	boolean isSetSlotDuration();

	/**
	 * Returns the value of the '<em><b>Contract</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Contract</em>' reference isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Contract</em>' reference.
	 * @see #setContract(Contract)
	 * @see scenario.cargo.CargoPackage#getSlot_Contract()
	 * @model
	 * @generated
	 */
	Contract getContract();

	/**
	 * Sets the value of the '{@link scenario.cargo.Slot#getContract <em>Contract</em>}' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Contract</em>' reference.
	 * @see #getContract()
	 * @generated
	 */
	void setContract(Contract value);

	/**
	 * Returns the value of the '<em><b>Fixed Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fixed Price</em>' attribute isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fixed Price</em>' attribute.
	 * @see #isSetFixedPrice()
	 * @see #unsetFixedPrice()
	 * @see #setFixedPrice(float)
	 * @see scenario.cargo.CargoPackage#getSlot_FixedPrice()
	 * @model unsettable="true"
	 * @generated
	 */
	float getFixedPrice();

	/**
	 * Sets the value of the '{@link scenario.cargo.Slot#getFixedPrice <em>Fixed Price</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fixed Price</em>' attribute.
	 * @see #isSetFixedPrice()
	 * @see #unsetFixedPrice()
	 * @see #getFixedPrice()
	 * @generated
	 */
	void setFixedPrice(float value);

	/**
	 * Unsets the value of the '{@link scenario.cargo.Slot#getFixedPrice <em>Fixed Price</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #isSetFixedPrice()
	 * @see #getFixedPrice()
	 * @see #setFixedPrice(float)
	 * @generated
	 */
	void unsetFixedPrice();

	/**
	 * Returns whether the value of the '{@link scenario.cargo.Slot#getFixedPrice <em>Fixed Price</em>}' attribute is set.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return whether the value of the '<em>Fixed Price</em>' attribute is set.
	 * @see #unsetFixedPrice()
	 * @see #getFixedPrice()
	 * @see #setFixedPrice(float)
	 * @generated
	 */
	boolean isSetFixedPrice();

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='final java.util.Calendar calendar = java.util.Calendar.getInstance(\njava.util.TimeZone.getTimeZone(getPort().getTimeZone())\n);\ncalendar.setTime(getWindowStart().getDateWithDefaults(getPort()));\nreturn calendar;'"
	 * @generated
	 */
	Object getLocalWindowStart();

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='return new Date(getWindowStart().getDateWithDefaults(getPort())\r\n\t\t\t\t\t\t.getTime()\r\n\t\t\t\t\t\t+ javax.management.timer.Timer.ONE_HOUR\r\n\t\t\t\t\t\t* getWindowDuration());'"
	 * @generated
	 */
	Date getWindowEnd();

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @model required="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='if (getContract() != null)\r\n\t\t\treturn getContract();\r\nelse if (getPort() != null) {\r\n\t\t\tfinal scenario.port.Port p = getPort();\r\n\t\t\tif (scenario instanceof scenario.Scenario) {\r\nfinal scenario.Scenario scenario2 = (scenario.Scenario) scenario;\r\n\t\t\tif (scenario2.getContractModel() != null) {\r\n\t\t\t\tfinal scenario.contract.ContractModel cm = scenario2.getContractModel();\r\n\t\t\t\treturn cm.getDefaultContract(p);\r\n\t\t\t}\r\n}\r\n\t\t\treturn null;\r\n\r\n} else\r\n\t\t\treturn null;'"
	 * @generated
	 */
	Contract getSlotOrPortContract(Object scenario);

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='if (isSetSlotDuration())\n\treturn getSlotDuration();\nelse\n\treturn getPort().getDefaultDischargeDuration();'"
	 * @generated
	 */
	int getSlotOrPortDuration();

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @model annotation="http://www.eclipse.org/emf/2002/GenModel body='if (isSetMinQuantity())\n\treturn getMinQuantity();\nelse {\n\tfinal scenario.contract.Contract c = getSlotOrPortContract(scenario);\n\tif (c == null) return getMinQuantity();\n\treturn c.getMinQuantity();\n}'"
	 * @generated
	 */
	int getSlotOrContractMinQuantity(Object scenario);

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @model annotation="http://www.eclipse.org/emf/2002/GenModel body='if (isSetMaxQuantity())\n\treturn getMaxQuantity();\nelse {\n\tfinal scenario.contract.Contract c = getSlotOrPortContract(scenario);\n\tif (c == null) return getMaxQuantity();\n\treturn c.getMaxQuantity();\n}'"
	 * @generated
	 */
	int getSlotOrContractMaxQuantity(Object scenario);

} // Slot
