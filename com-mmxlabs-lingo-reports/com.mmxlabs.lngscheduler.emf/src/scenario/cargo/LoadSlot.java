/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.cargo;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Load Slot</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.cargo.LoadSlot#getCargoCVvalue <em>Cargo CVvalue</em>}</li>
 *   <li>{@link scenario.cargo.LoadSlot#isArriveCold <em>Arrive Cold</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.cargo.CargoPackage#getLoadSlot()
 * @model
 * @generated
 */
public interface LoadSlot extends Slot {
	/**
	 * Returns the value of the '<em><b>Cargo CVvalue</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The energy content conversion factor for this cargo; because LNG is priced by MMBTU but measured by volume, we need to know both the energy content and volume. This value lets us derive the former from the latter.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Cargo CVvalue</em>' attribute.
	 * @see #isSetCargoCVvalue()
	 * @see #unsetCargoCVvalue()
	 * @see #setCargoCVvalue(float)
	 * @see scenario.cargo.CargoPackage#getLoadSlot_CargoCVvalue()
	 * @model unsettable="true"
	 * @generated
	 */
	float getCargoCVvalue();

	/**
	 * Sets the value of the '{@link scenario.cargo.LoadSlot#getCargoCVvalue <em>Cargo CVvalue</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cargo CVvalue</em>' attribute.
	 * @see #isSetCargoCVvalue()
	 * @see #unsetCargoCVvalue()
	 * @see #getCargoCVvalue()
	 * @generated
	 */
	void setCargoCVvalue(float value);

	/**
	 * Unsets the value of the '{@link scenario.cargo.LoadSlot#getCargoCVvalue <em>Cargo CVvalue</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetCargoCVvalue()
	 * @see #getCargoCVvalue()
	 * @see #setCargoCVvalue(float)
	 * @generated
	 */
	void unsetCargoCVvalue();

	/**
	 * Returns whether the value of the '{@link scenario.cargo.LoadSlot#getCargoCVvalue <em>Cargo CVvalue</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Cargo CVvalue</em>' attribute is set.
	 * @see #unsetCargoCVvalue()
	 * @see #getCargoCVvalue()
	 * @see #setCargoCVvalue(float)
	 * @generated
	 */
	boolean isSetCargoCVvalue();

	/**
	 * Returns the value of the '<em><b>Arrive Cold</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Arrive Cold</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Arrive Cold</em>' attribute.
	 * @see #isSetArriveCold()
	 * @see #unsetArriveCold()
	 * @see #setArriveCold(boolean)
	 * @see scenario.cargo.CargoPackage#getLoadSlot_ArriveCold()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	boolean isArriveCold();

	/**
	 * Sets the value of the '{@link scenario.cargo.LoadSlot#isArriveCold <em>Arrive Cold</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Arrive Cold</em>' attribute.
	 * @see #isSetArriveCold()
	 * @see #unsetArriveCold()
	 * @see #isArriveCold()
	 * @generated
	 */
	void setArriveCold(boolean value);

	/**
	 * Unsets the value of the '{@link scenario.cargo.LoadSlot#isArriveCold <em>Arrive Cold</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetArriveCold()
	 * @see #isArriveCold()
	 * @see #setArriveCold(boolean)
	 * @generated
	 */
	void unsetArriveCold();

	/**
	 * Returns whether the value of the '{@link scenario.cargo.LoadSlot#isArriveCold <em>Arrive Cold</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Arrive Cold</em>' attribute is set.
	 * @see #unsetArriveCold()
	 * @see #isArriveCold()
	 * @see #setArriveCold(boolean)
	 * @generated
	 */
	boolean isSetArriveCold();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='if (isSetCargoCVvalue()) \r\n\treturn getCargoCVvalue();\r\nelse if (getPort()!=null)\r\n\treturn getPort().getDefaultCVvalue();\r\nelse\r\n\treturn 0;'"
	 * @generated
	 */
	float getCargoOrPortCVValue();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='if (isSetSlotDuration())\n\treturn getSlotDuration();\nelse\n\treturn getPort().getDefaultLoadDuration();'"
	 * @generated
	 */
	int getSlotOrPortDuration();

} // LoadSlot
