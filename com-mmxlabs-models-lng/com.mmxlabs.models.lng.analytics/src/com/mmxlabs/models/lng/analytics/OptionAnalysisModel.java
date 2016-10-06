/**
 */
package com.mmxlabs.models.lng.analytics;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Option Analysis Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.OptionAnalysisModel#getBuys <em>Buys</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.OptionAnalysisModel#getSells <em>Sells</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.OptionAnalysisModel#getBaseCase <em>Base Case</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.OptionAnalysisModel#getRules <em>Rules</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.OptionAnalysisModel#getPartialCase <em>Partial Case</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.OptionAnalysisModel#getResultSets <em>Result Sets</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.OptionAnalysisModel#isUseTargetPNL <em>Use Target PNL</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getOptionAnalysisModel()
 * @model
 * @generated
 */
public interface OptionAnalysisModel extends EObject {
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
	 * Returns the value of the '<em><b>Buys</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.analytics.BuyOption}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Buys</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Buys</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getOptionAnalysisModel_Buys()
	 * @model containment="true"
	 * @generated
	 */
	EList<BuyOption> getBuys();

	/**
	 * Returns the value of the '<em><b>Sells</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.analytics.SellOption}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sells</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sells</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getOptionAnalysisModel_Sells()
	 * @model containment="true"
	 * @generated
	 */
	EList<SellOption> getSells();

	/**
	 * Returns the value of the '<em><b>Rules</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.analytics.OptionRule}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Rules</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Rules</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getOptionAnalysisModel_Rules()
	 * @model containment="true"
	 * @generated
	 */
	EList<OptionRule> getRules();

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
	 * Returns the value of the '<em><b>Result Sets</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.analytics.ResultSet}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Result Sets</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Result Sets</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getOptionAnalysisModel_ResultSets()
	 * @model containment="true"
	 * @generated
	 */
	EList<ResultSet> getResultSets();

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

} // OptionAnalysisModel
