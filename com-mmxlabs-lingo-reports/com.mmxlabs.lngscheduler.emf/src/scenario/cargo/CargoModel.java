/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.cargo;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.cargo.CargoModel#getCargoes <em>Cargoes</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.cargo.CargoPackage#getCargoModel()
 * @model
 * @generated
 */
public interface CargoModel extends EObject {
	/**
	 * Returns the value of the '<em><b>Cargoes</b></em>' containment reference list.
	 * The list contents are of type {@link scenario.cargo.Cargo}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cargoes</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cargoes</em>' containment reference list.
	 * @see scenario.cargo.CargoPackage#getCargoModel_Cargoes()
	 * @model containment="true"
	 * @generated
	 */
	EList<Cargo> getCargoes();

} // CargoModel
