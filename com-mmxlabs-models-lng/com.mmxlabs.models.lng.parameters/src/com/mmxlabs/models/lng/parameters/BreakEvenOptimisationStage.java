/**
 */
package com.mmxlabs.models.lng.parameters;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Break Even Optimisation Stage</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.parameters.BreakEvenOptimisationStage#getTargetProfitAndLoss <em>Target Profit And Loss</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getBreakEvenOptimisationStage()
 * @model
 * @generated
 */
public interface BreakEvenOptimisationStage extends OptimisationStage {
	/**
	 * Returns the value of the '<em><b>Target Profit And Loss</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Target Profit And Loss</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Target Profit And Loss</em>' attribute.
	 * @see #setTargetProfitAndLoss(long)
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getBreakEvenOptimisationStage_TargetProfitAndLoss()
	 * @model
	 * @generated
	 */
	long getTargetProfitAndLoss();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.BreakEvenOptimisationStage#getTargetProfitAndLoss <em>Target Profit And Loss</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Target Profit And Loss</em>' attribute.
	 * @see #getTargetProfitAndLoss()
	 * @generated
	 */
	void setTargetProfitAndLoss(long value);

} // BreakEvenOptimisationStage
