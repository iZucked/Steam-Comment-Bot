/**
 */
package com.mmxlabs.models.lng.parameters;

import org.eclipse.jdt.annotation.Nullable;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Parallel Optimisation Stage</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.parameters.ParallelOptimisationStage#getJobCount <em>Job Count</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.ParallelOptimisationStage#getTemplate <em>Template</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getParallelOptimisationStage()
 * @model
 * @generated
 */
public interface ParallelOptimisationStage<T extends ParallisableOptimisationStage> extends OptimisationStage {
	/**
	 * Returns the value of the '<em><b>Job Count</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Job Count</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Job Count</em>' attribute.
	 * @see #setJobCount(int)
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getParallelOptimisationStage_JobCount()
	 * @model
	 * @generated
	 */
	int getJobCount();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.ParallelOptimisationStage#getJobCount <em>Job Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Job Count</em>' attribute.
	 * @see #getJobCount()
	 * @generated
	 */
	void setJobCount(int value);

	/**
	 * Returns the value of the '<em><b>Template</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Template</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Template</em>' containment reference.
	 * @see #setTemplate(ParallisableOptimisationStage)
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getParallelOptimisationStage_Template()
	 * @model containment="true"
	 * @generated
	 */
	T getTemplate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.ParallelOptimisationStage#getTemplate <em>Template</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Template</em>' containment reference.
	 * @see #getTemplate()
	 * @generated
	 */
	void setTemplate(T value);

} // ParallelOptimisationStage
