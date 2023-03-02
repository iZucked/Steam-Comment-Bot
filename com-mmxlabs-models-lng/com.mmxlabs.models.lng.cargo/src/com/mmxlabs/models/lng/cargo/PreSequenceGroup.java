/**
 */
package com.mmxlabs.models.lng.cargo;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Pre Sequence Group</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.PreSequenceGroup#getSequence <em>Sequence</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getPreSequenceGroup()
 * @model
 * @generated
 */
public interface PreSequenceGroup extends EObject {
	/**
	 * Returns the value of the '<em><b>Sequence</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.cargo.ScheduleSpecificationEvent}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sequence</em>' reference list.
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getPreSequenceGroup_Sequence()
	 * @model
	 * @generated
	 */
	EList<ScheduleSpecificationEvent> getSequence();

} // PreSequenceGroup
