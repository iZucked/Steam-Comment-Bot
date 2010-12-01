/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.cargo;

import java.util.Date;
import scenario.port.Port;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Load Slot</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.cargo.LoadSlot#getCargoCVvalue <em>Cargo CVvalue</em>}</li>
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
	 * @see #setCargoCVvalue(float)
	 * @see scenario.cargo.CargoPackage#getLoadSlot_CargoCVvalue()
	 * @model
	 * @generated
	 */
	float getCargoCVvalue();

	/**
	 * Sets the value of the '{@link scenario.cargo.LoadSlot#getCargoCVvalue <em>Cargo CVvalue</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cargo CVvalue</em>' attribute.
	 * @see #getCargoCVvalue()
	 * @generated
	 */
	void setCargoCVvalue(float value);

} // LoadSlot
