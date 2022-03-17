/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics;

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
 *   <li>{@link com.mmxlabs.models.lng.analytics.OptionAnalysisModel#getResults <em>Results</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.OptionAnalysisModel#isUseTargetPNL <em>Use Target PNL</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.OptionAnalysisModel#getMode <em>Mode</em>}</li>
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
	 * Returns the value of the '<em><b>Results</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Results</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Results</em>' containment reference.
	 * @see #setResults(AbstractSolutionSet)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getOptionAnalysisModel_Results()
	 * @model containment="true"
	 * @generated
	 */
	AbstractSolutionSet getResults();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.OptionAnalysisModel#getResults <em>Results</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Results</em>' containment reference.
	 * @see #getResults()
	 * @generated
	 */
	void setResults(AbstractSolutionSet value);

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
	 * Returns the value of the '<em><b>Mode</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Mode</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Mode</em>' attribute.
	 * @see #setMode(int)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getOptionAnalysisModel_Mode()
	 * @model
	 * @generated
	 */
	int getMode();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.OptionAnalysisModel#getMode <em>Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Mode</em>' attribute.
	 * @see #getMode()
	 * @generated
	 */
	void setMode(int value);

} // OptionAnalysisModel
