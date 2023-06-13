/**
 */
package com.mmxlabs.models.lng.adp;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Vessel Usage Distribution Profile Constraint</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.VesselUsageDistributionProfileConstraint#getDistributions <em>Distributions</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.adp.ADPPackage#getVesselUsageDistributionProfileConstraint()
 * @model
 * @generated
 */
public interface VesselUsageDistributionProfileConstraint extends ProfileConstraint {
	/**
	 * Returns the value of the '<em><b>Distributions</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.adp.VesselUsageDistribution}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Distributions</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getVesselUsageDistributionProfileConstraint_Distributions()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<VesselUsageDistribution> getDistributions();

} // VesselUsageDistributionProfileConstraint
