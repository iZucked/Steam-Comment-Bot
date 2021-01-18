/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Pre Defined Distribution Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.PreDefinedDistributionModel#getDates <em>Dates</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.adp.ADPPackage#getPreDefinedDistributionModel()
 * @model
 * @generated
 */
public interface PreDefinedDistributionModel extends DistributionModel {
	/**
	 * Returns the value of the '<em><b>Dates</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.adp.PreDefinedDate}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Dates</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Dates</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getPreDefinedDistributionModel_Dates()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<PreDefinedDate> getDates();

} // PreDefinedDistributionModel
