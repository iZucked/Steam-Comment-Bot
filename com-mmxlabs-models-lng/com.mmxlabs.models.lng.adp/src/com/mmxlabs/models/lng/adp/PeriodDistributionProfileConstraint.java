/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Period Distribution Profile Constraint</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.PeriodDistributionProfileConstraint#getDistributions <em>Distributions</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.adp.ADPPackage#getPeriodDistributionProfileConstraint()
 * @model
 * @generated
 */
public interface PeriodDistributionProfileConstraint extends ProfileConstraint {
	/**
	 * Returns the value of the '<em><b>Distributions</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.adp.PeriodDistribution}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Distributions</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Distributions</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getPeriodDistributionProfileConstraint_Distributions()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<PeriodDistribution> getDistributions();

} // PeriodDistributionProfileConstraint
