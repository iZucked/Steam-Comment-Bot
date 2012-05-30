

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.cargo;
import com.mmxlabs.models.mmxcore.NamedObject;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Group</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.CargoGroup#getCargoes <em>Cargoes</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCargoGroup()
 * @model
 * @generated
 */
public interface CargoGroup extends NamedObject {
	/**
	 * Returns the value of the '<em><b>Cargoes</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.cargo.Cargo}.
	 * It is bidirectional and its opposite is '{@link com.mmxlabs.models.lng.cargo.Cargo#getGroups <em>Groups</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cargoes</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cargoes</em>' reference list.
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getCargoGroup_Cargoes()
	 * @see com.mmxlabs.models.lng.cargo.Cargo#getGroups
	 * @model opposite="groups"
	 * @generated
	 */
	EList<Cargo> getCargoes();

} // end of  CargoGroup

// finish type fixing
