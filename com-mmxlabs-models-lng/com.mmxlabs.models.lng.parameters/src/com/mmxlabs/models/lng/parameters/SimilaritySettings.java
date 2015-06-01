/**
 */
package com.mmxlabs.models.lng.parameters;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Similarity Settings</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.parameters.SimilaritySettings#getThreshold <em>Threshold</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getSimilaritySettings()
 * @model
 * @generated
 */
public interface SimilaritySettings extends EObject {
	/**
	 * Returns the value of the '<em><b>Threshold</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Threshold</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Threshold</em>' attribute.
	 * @see #isSetThreshold()
	 * @see #unsetThreshold()
	 * @see #setThreshold(int)
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getSimilaritySettings_Threshold()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	int getThreshold();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.SimilaritySettings#getThreshold <em>Threshold</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Threshold</em>' attribute.
	 * @see #isSetThreshold()
	 * @see #unsetThreshold()
	 * @see #getThreshold()
	 * @generated
	 */
	void setThreshold(int value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.parameters.SimilaritySettings#getThreshold <em>Threshold</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetThreshold()
	 * @see #getThreshold()
	 * @see #setThreshold(int)
	 * @generated
	 */
	void unsetThreshold();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.parameters.SimilaritySettings#getThreshold <em>Threshold</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Threshold</em>' attribute is set.
	 * @see #unsetThreshold()
	 * @see #getThreshold()
	 * @see #setThreshold(int)
	 * @generated
	 */
	boolean isSetThreshold();

} // SimilaritySettings
