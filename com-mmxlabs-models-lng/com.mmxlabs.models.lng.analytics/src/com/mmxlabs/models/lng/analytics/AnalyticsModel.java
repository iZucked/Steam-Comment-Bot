/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
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
 *   <li>{@link com.mmxlabs.models.lng.analytics.AnalyticsModel#getViabilityModel <em>Viability Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.AnalyticsModel#getMtmModel <em>Mtm Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.AnalyticsModel#getBreakevenModels <em>Breakeven Models</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getAnalyticsModel()
 * @model annotation="http://www.mmxlabs.com/models/mmxcore/validation/NamedObject nonUniqueChildren='true'"
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

	/**
	 * Returns the value of the '<em><b>Viability Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Viability Model</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Viability Model</em>' containment reference.
	 * @see #setViabilityModel(ViabilityModel)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getAnalyticsModel_ViabilityModel()
	 * @model containment="true"
	 * @generated
	 */
	ViabilityModel getViabilityModel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.AnalyticsModel#getViabilityModel <em>Viability Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Viability Model</em>' containment reference.
	 * @see #getViabilityModel()
	 * @generated
	 */
	void setViabilityModel(ViabilityModel value);

	/**
	 * Returns the value of the '<em><b>Mtm Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Mtm Model</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Mtm Model</em>' containment reference.
	 * @see #setMtmModel(MTMModel)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getAnalyticsModel_MtmModel()
	 * @model containment="true"
	 * @generated
	 */
	MTMModel getMtmModel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.AnalyticsModel#getMtmModel <em>Mtm Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Mtm Model</em>' containment reference.
	 * @see #getMtmModel()
	 * @generated
	 */
	void setMtmModel(MTMModel value);

	/**
	 * Returns the value of the '<em><b>Breakeven Models</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.analytics.BreakEvenAnalysisModel}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Breakeven Models</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Breakeven Models</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getAnalyticsModel_BreakevenModels()
	 * @model containment="true"
	 * @generated
	 */
	EList<BreakEvenAnalysisModel> getBreakevenModels();

} // end of  AnalyticsModel

// finish type fixing
