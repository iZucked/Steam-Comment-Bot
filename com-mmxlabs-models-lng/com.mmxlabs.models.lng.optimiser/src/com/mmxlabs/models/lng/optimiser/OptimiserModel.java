

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.optimiser;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.UUIDObject;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.optimiser.OptimiserModel#getSettings <em>Settings</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.optimiser.OptimiserModel#getActiveSetting <em>Active Setting</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.optimiser.OptimiserPackage#getOptimiserModel()
 * @model
 * @generated
 */
public interface OptimiserModel extends UUIDObject, NamedObject {
	/**
	 * Returns the value of the '<em><b>Settings</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.optimiser.OptimiserSettings}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Settings</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Settings</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.optimiser.OptimiserPackage#getOptimiserModel_Settings()
	 * @model containment="true"
	 * @generated
	 */
	EList<OptimiserSettings> getSettings();

	/**
	 * Returns the value of the '<em><b>Active Setting</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Active Setting</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Active Setting</em>' reference.
	 * @see #setActiveSetting(OptimiserSettings)
	 * @see com.mmxlabs.models.lng.optimiser.OptimiserPackage#getOptimiserModel_ActiveSetting()
	 * @model required="true"
	 * @generated
	 */
	OptimiserSettings getActiveSetting();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.optimiser.OptimiserModel#getActiveSetting <em>Active Setting</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Active Setting</em>' reference.
	 * @see #getActiveSetting()
	 * @generated
	 */
	void setActiveSetting(OptimiserSettings value);

} // end of  OptimiserModel

// finish type fixing
