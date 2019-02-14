/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics;

import com.mmxlabs.models.mmxcore.NamedObject;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Option Analysis Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.OptionAnalysisModel#getBaseCase <em>Base Case</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.OptionAnalysisModel#getPartialCase <em>Partial Case</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.OptionAnalysisModel#getBaseCaseResult <em>Base Case Result</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.OptionAnalysisModel#getResults <em>Results</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.OptionAnalysisModel#isUseTargetPNL <em>Use Target PNL</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.OptionAnalysisModel#getChildren <em>Children</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getOptionAnalysisModel()
 * @model
 * @generated
 */
public interface OptionAnalysisModel extends AbstractAnalysisModel {
	/**
	 * Returns the value of the '<em><b>Base Case</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Base Case</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Case</em>' containment reference.
	 * @see #setBaseCase(BaseCase)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getOptionAnalysisModel_BaseCase()
	 * @model containment="true"
	 * @generated
	 */
	BaseCase getBaseCase();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.OptionAnalysisModel#getBaseCase <em>Base Case</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Case</em>' containment reference.
	 * @see #getBaseCase()
	 * @generated
	 */
	void setBaseCase(BaseCase value);

	/**
	 * Returns the value of the '<em><b>Partial Case</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Partial Case</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Partial Case</em>' containment reference.
	 * @see #setPartialCase(PartialCase)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getOptionAnalysisModel_PartialCase()
	 * @model containment="true"
	 * @generated
	 */
	PartialCase getPartialCase();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.OptionAnalysisModel#getPartialCase <em>Partial Case</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Partial Case</em>' containment reference.
	 * @see #getPartialCase()
	 * @generated
	 */
	void setPartialCase(PartialCase value);

	/**
	 * Returns the value of the '<em><b>Base Case Result</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Base Case Result</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Case Result</em>' containment reference.
	 * @see #setBaseCaseResult(Result)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getOptionAnalysisModel_BaseCaseResult()
	 * @model containment="true"
	 * @generated
	 */
	Result getBaseCaseResult();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.OptionAnalysisModel#getBaseCaseResult <em>Base Case Result</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Case Result</em>' containment reference.
	 * @see #getBaseCaseResult()
	 * @generated
	 */
	void setBaseCaseResult(Result value);

	/**
	 * Returns the value of the '<em><b>Results</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Results</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Results</em>' containment reference.
	 * @see #setResults(Result)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getOptionAnalysisModel_Results()
	 * @model containment="true"
	 * @generated
	 */
	Result getResults();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.OptionAnalysisModel#getResults <em>Results</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Results</em>' containment reference.
	 * @see #getResults()
	 * @generated
	 */
	void setResults(Result value);

	/**
	 * Returns the value of the '<em><b>Use Target PNL</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Use Target PNL</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Use Target PNL</em>' attribute.
	 * @see #setUseTargetPNL(boolean)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getOptionAnalysisModel_UseTargetPNL()
	 * @model
	 * @generated
	 */
	boolean isUseTargetPNL();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.OptionAnalysisModel#isUseTargetPNL <em>Use Target PNL</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Use Target PNL</em>' attribute.
	 * @see #isUseTargetPNL()
	 * @generated
	 */
	void setUseTargetPNL(boolean value);

	/**
	 * Returns the value of the '<em><b>Children</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.analytics.OptionAnalysisModel}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Children</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Children</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getOptionAnalysisModel_Children()
	 * @model containment="true"
	 * @generated
	 */
	EList<OptionAnalysisModel> getChildren();

} // OptionAnalysisModel
