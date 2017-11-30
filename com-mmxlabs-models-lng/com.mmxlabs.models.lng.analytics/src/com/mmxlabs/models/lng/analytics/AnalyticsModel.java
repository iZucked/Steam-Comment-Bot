/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics;
import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.AnalyticsModel#getOptionModels <em>Option Models</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.AnalyticsModel#getOptimisations <em>Optimisations</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getAnalyticsModel()
 * @model
 * @generated
 */
public interface AnalyticsModel extends UUIDObject {
	/**
	 * Returns the value of the '<em><b>Option Models</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.analytics.OptionAnalysisModel}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Option Models</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Option Models</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getAnalyticsModel_OptionModels()
	 * @model containment="true"
	 * @generated
	 */
	EList<OptionAnalysisModel> getOptionModels();

	/**
	 * Returns the value of the '<em><b>Optimisations</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.analytics.AbstractSolutionSet}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Optimisations</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Optimisations</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getAnalyticsModel_Optimisations()
	 * @model containment="true"
	 * @generated
	 */
	EList<AbstractSolutionSet> getOptimisations();

} // end of  AnalyticsModel

// finish type fixing
