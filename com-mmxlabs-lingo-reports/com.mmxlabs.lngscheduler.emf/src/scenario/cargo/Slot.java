/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package scenario.cargo;

import java.util.Date;

import org.eclipse.emf.ecore.EObject;

import scenario.market.Market;
import scenario.port.Port;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Slot</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.cargo.Slot#getId <em>Id</em>}</li>
 *   <li>{@link scenario.cargo.Slot#getMinQuantity <em>Min Quantity</em>}</li>
 *   <li>{@link scenario.cargo.Slot#getMaxQuantity <em>Max Quantity</em>}</li>
 *   <li>{@link scenario.cargo.Slot#getPort <em>Port</em>}</li>
 *   <li>{@link scenario.cargo.Slot#getWindowStart <em>Window Start</em>}</li>
 *   <li>{@link scenario.cargo.Slot#getWindowDuration <em>Window Duration</em>}</li>
 *   <li>{@link scenario.cargo.Slot#getSlotDuration <em>Slot Duration</em>}</li>
 *   <li>{@link scenario.cargo.Slot#getMarket <em>Market</em>}</li>
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
	 * If the meaning of the '<em>Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(String value);

	/**
	 * Returns the value of the '<em><b>Min Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Min Quantity</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Min Quantity</em>' attribute.
	 * @see #setMinQuantity(int)
	 * @see scenario.cargo.CargoPackage#getSlot_MinQuantity()
	 * @model
	 * @generated
	 */
	int getMinQuantity();

	/**
	 * Sets the value of the '{@link scenario.cargo.Slot#getMinQuantity <em>Min Quantity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Min Quantity</em>' attribute.
	 * @see #getMinQuantity()
	 * @generated
	 */
	void setMinQuantity(int value);

	/**
	 * Returns the value of the '<em><b>Max Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Max Quantity</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Max Quantity</em>' attribute.
	 * @see #setMaxQuantity(int)
	 * @see scenario.cargo.CargoPackage#getSlot_MaxQuantity()
	 * @model
	 * @generated
	 */
	int getMaxQuantity();

	/**
	 * Sets the value of the '{@link scenario.cargo.Slot#getMaxQuantity <em>Max Quantity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Max Quantity</em>' attribute.
	 * @see #getMaxQuantity()
	 * @generated
	 */
	void setMaxQuantity(int value);

	/**
	 * Returns the value of the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Port</em>' reference isn't clear,
	 * there really should be more of a description here...
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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Port</em>' reference.
	 * @see #getPort()
	 * @generated
	 */
	void setPort(Port value);

	/**
	 * Returns the value of the '<em><b>Window Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Window Start</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Window Start</em>' attribute.
	 * @see #setWindowStart(Date)
	 * @see scenario.cargo.CargoPackage#getSlot_WindowStart()
	 * @model
	 * @generated
	 */
	Date getWindowStart();

	/**
	 * Sets the value of the '{@link scenario.cargo.Slot#getWindowStart <em>Window Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Window Start</em>' attribute.
	 * @see #getWindowStart()
	 * @generated
	 */
	void setWindowStart(Date value);

	/**
	 * Returns the value of the '<em><b>Window Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Window Duration</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Window Duration</em>' attribute.
	 * @see #setWindowDuration(int)
	 * @see scenario.cargo.CargoPackage#getSlot_WindowDuration()
	 * @model
	 * @generated
	 */
	int getWindowDuration();

	/**
	 * Sets the value of the '{@link scenario.cargo.Slot#getWindowDuration <em>Window Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Window Duration</em>' attribute.
	 * @see #getWindowDuration()
	 * @generated
	 */
	void setWindowDuration(int value);

	/**
	 * Returns the value of the '<em><b>Slot Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Slot Duration</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Slot Duration</em>' attribute.
	 * @see #setSlotDuration(int)
	 * @see scenario.cargo.CargoPackage#getSlot_SlotDuration()
	 * @model
	 * @generated
	 */
	int getSlotDuration();

	/**
	 * Sets the value of the '{@link scenario.cargo.Slot#getSlotDuration <em>Slot Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Slot Duration</em>' attribute.
	 * @see #getSlotDuration()
	 * @generated
	 */
	void setSlotDuration(int value);

	/**
	 * Returns the value of the '<em><b>Market</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Market</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Market</em>' reference.
	 * @see #isSetMarket()
	 * @see #unsetMarket()
	 * @see #setMarket(Market)
	 * @see scenario.cargo.CargoPackage#getSlot_Market()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	Market getMarket();

	/**
	 * Sets the value of the '{@link scenario.cargo.Slot#getMarket <em>Market</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Market</em>' reference.
	 * @see #isSetMarket()
	 * @see #unsetMarket()
	 * @see #getMarket()
	 * @generated
	 */
	void setMarket(Market value);

	/**
	 * Unsets the value of the '{@link scenario.cargo.Slot#getMarket <em>Market</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetMarket()
	 * @see #getMarket()
	 * @see #setMarket(Market)
	 * @generated
	 */
	void unsetMarket();

	/**
	 * Returns whether the value of the '{@link scenario.cargo.Slot#getMarket <em>Market</em>}' reference is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Market</em>' reference is set.
	 * @see #unsetMarket()
	 * @see #getMarket()
	 * @see #setMarket(Market)
	 * @generated
	 */
	boolean isSetMarket();
	
	Date getWindowEnd();

} // Slot
