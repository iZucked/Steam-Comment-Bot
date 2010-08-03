/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.cargo;

import java.util.Date;

import org.eclipse.emf.ecore.EObject;

import scenario.port.Port;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Load Slot</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.cargo.LoadSlot#getId <em>Id</em>}</li>
 *   <li>{@link scenario.cargo.LoadSlot#getMinQuantity <em>Min Quantity</em>}</li>
 *   <li>{@link scenario.cargo.LoadSlot#getMaxQuantity <em>Max Quantity</em>}</li>
 *   <li>{@link scenario.cargo.LoadSlot#getUnitPrice <em>Unit Price</em>}</li>
 *   <li>{@link scenario.cargo.LoadSlot#getPort <em>Port</em>}</li>
 *   <li>{@link scenario.cargo.LoadSlot#getWindowStart <em>Window Start</em>}</li>
 *   <li>{@link scenario.cargo.LoadSlot#getWindowDuration <em>Window Duration</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.cargo.CargoPackage#getLoadSlot()
 * @model
 * @generated
 */
public interface LoadSlot extends EObject {
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
	 * @see scenario.cargo.CargoPackage#getLoadSlot_Id()
	 * @model
	 * @generated
	 */
	String getId();

	/**
	 * Sets the value of the '{@link scenario.cargo.LoadSlot#getId <em>Id</em>}' attribute.
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
	 * @see #setMinQuantity(long)
	 * @see scenario.cargo.CargoPackage#getLoadSlot_MinQuantity()
	 * @model
	 * @generated
	 */
	long getMinQuantity();

	/**
	 * Sets the value of the '{@link scenario.cargo.LoadSlot#getMinQuantity <em>Min Quantity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Min Quantity</em>' attribute.
	 * @see #getMinQuantity()
	 * @generated
	 */
	void setMinQuantity(long value);

	/**
	 * Returns the value of the '<em><b>Max Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Max Quantity</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Max Quantity</em>' attribute.
	 * @see #setMaxQuantity(long)
	 * @see scenario.cargo.CargoPackage#getLoadSlot_MaxQuantity()
	 * @model
	 * @generated
	 */
	long getMaxQuantity();

	/**
	 * Sets the value of the '{@link scenario.cargo.LoadSlot#getMaxQuantity <em>Max Quantity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Max Quantity</em>' attribute.
	 * @see #getMaxQuantity()
	 * @generated
	 */
	void setMaxQuantity(long value);

	/**
	 * Returns the value of the '<em><b>Unit Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Unit Price</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Unit Price</em>' attribute.
	 * @see #setUnitPrice(int)
	 * @see scenario.cargo.CargoPackage#getLoadSlot_UnitPrice()
	 * @model
	 * @generated
	 */
	int getUnitPrice();

	/**
	 * Sets the value of the '{@link scenario.cargo.LoadSlot#getUnitPrice <em>Unit Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Unit Price</em>' attribute.
	 * @see #getUnitPrice()
	 * @generated
	 */
	void setUnitPrice(int value);

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
	 * @see scenario.cargo.CargoPackage#getLoadSlot_Port()
	 * @model
	 * @generated
	 */
	Port getPort();

	/**
	 * Sets the value of the '{@link scenario.cargo.LoadSlot#getPort <em>Port</em>}' reference.
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
	 * @see scenario.cargo.CargoPackage#getLoadSlot_WindowStart()
	 * @model
	 * @generated
	 */
	Date getWindowStart();

	/**
	 * Sets the value of the '{@link scenario.cargo.LoadSlot#getWindowStart <em>Window Start</em>}' attribute.
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
	 * @see scenario.cargo.CargoPackage#getLoadSlot_WindowDuration()
	 * @model
	 * @generated
	 */
	int getWindowDuration();

	/**
	 * Sets the value of the '{@link scenario.cargo.LoadSlot#getWindowDuration <em>Window Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Window Duration</em>' attribute.
	 * @see #getWindowDuration()
	 * @generated
	 */
	void setWindowDuration(int value);

} // LoadSlot
