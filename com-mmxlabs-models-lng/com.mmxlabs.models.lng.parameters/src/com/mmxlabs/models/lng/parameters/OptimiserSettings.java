/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.parameters;
import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Settings</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.parameters.OptimiserSettings#getObjectives <em>Objectives</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.OptimiserSettings#getConstraints <em>Constraints</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.OptimiserSettings#getRange <em>Range</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.OptimiserSettings#getAnnealingSettings <em>Annealing Settings</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.OptimiserSettings#getSeed <em>Seed</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.OptimiserSettings#getArguments <em>Arguments</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.OptimiserSettings#isGenerateCharterOuts <em>Generate Charter Outs</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.OptimiserSettings#isShippingOnly <em>Shipping Only</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.OptimiserSettings#getSimilaritySettings <em>Similarity Settings</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.OptimiserSettings#getSolutionImprovementSettings <em>Solution Improvement Settings</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.OptimiserSettings#isBuildActionSets <em>Build Action Sets</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.OptimiserSettings#getActionPlanSettings <em>Action Plan Settings</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.OptimiserSettings#getFloatingDaysLimit <em>Floating Days Limit</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getOptimiserSettings()
 * @model
 * @generated
 */
public interface OptimiserSettings extends UUIDObject, NamedObject {
	/**
	 * Returns the value of the '<em><b>Objectives</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.parameters.Objective}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Objectives</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Objectives</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getOptimiserSettings_Objectives()
	 * @model containment="true"
	 * @generated
	 */
	EList<Objective> getObjectives();

	/**
	 * Returns the value of the '<em><b>Constraints</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.parameters.Constraint}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Constraints</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Constraints</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getOptimiserSettings_Constraints()
	 * @model containment="true"
	 * @generated
	 */
	EList<Constraint> getConstraints();

	/**
	 * Returns the value of the '<em><b>Range</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Range</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Range</em>' containment reference.
	 * @see #setRange(OptimisationRange)
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getOptimiserSettings_Range()
	 * @model containment="true" required="true"
	 * @generated
	 */
	OptimisationRange getRange();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.OptimiserSettings#getRange <em>Range</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Range</em>' containment reference.
	 * @see #getRange()
	 * @generated
	 */
	void setRange(OptimisationRange value);

	/**
	 * Returns the value of the '<em><b>Annealing Settings</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Annealing Settings</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Annealing Settings</em>' containment reference.
	 * @see #setAnnealingSettings(AnnealingSettings)
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getOptimiserSettings_AnnealingSettings()
	 * @model containment="true" required="true"
	 * @generated
	 */
	AnnealingSettings getAnnealingSettings();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.OptimiserSettings#getAnnealingSettings <em>Annealing Settings</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Annealing Settings</em>' containment reference.
	 * @see #getAnnealingSettings()
	 * @generated
	 */
	void setAnnealingSettings(AnnealingSettings value);

	/**
	 * Returns the value of the '<em><b>Seed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Seed</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Seed</em>' attribute.
	 * @see #setSeed(int)
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getOptimiserSettings_Seed()
	 * @model required="true"
	 * @generated
	 */
	int getSeed();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.OptimiserSettings#getSeed <em>Seed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Seed</em>' attribute.
	 * @see #getSeed()
	 * @generated
	 */
	void setSeed(int value);

	/**
	 * Returns the value of the '<em><b>Arguments</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.parameters.Argument}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Arguments</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Arguments</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getOptimiserSettings_Arguments()
	 * @model containment="true"
	 * @generated
	 */
	EList<Argument> getArguments();

	/**
	 * Returns the value of the '<em><b>Generate Charter Outs</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Generate Charter Outs</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Generate Charter Outs</em>' attribute.
	 * @see #setGenerateCharterOuts(boolean)
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getOptimiserSettings_GenerateCharterOuts()
	 * @model
	 * @generated
	 */
	boolean isGenerateCharterOuts();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.OptimiserSettings#isGenerateCharterOuts <em>Generate Charter Outs</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Generate Charter Outs</em>' attribute.
	 * @see #isGenerateCharterOuts()
	 * @generated
	 */
	void setGenerateCharterOuts(boolean value);

	/**
	 * Returns the value of the '<em><b>Shipping Only</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Shipping Only</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Shipping Only</em>' attribute.
	 * @see #setShippingOnly(boolean)
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getOptimiserSettings_ShippingOnly()
	 * @model
	 * @generated
	 */
	boolean isShippingOnly();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.OptimiserSettings#isShippingOnly <em>Shipping Only</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Shipping Only</em>' attribute.
	 * @see #isShippingOnly()
	 * @generated
	 */
	void setShippingOnly(boolean value);

	/**
	 * Returns the value of the '<em><b>Similarity Settings</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Similarity Settings</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Similarity Settings</em>' containment reference.
	 * @see #setSimilaritySettings(SimilaritySettings)
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getOptimiserSettings_SimilaritySettings()
	 * @model containment="true" required="true"
	 * @generated
	 */
	SimilaritySettings getSimilaritySettings();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.OptimiserSettings#getSimilaritySettings <em>Similarity Settings</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Similarity Settings</em>' containment reference.
	 * @see #getSimilaritySettings()
	 * @generated
	 */
	void setSimilaritySettings(SimilaritySettings value);

	/**
	 * Returns the value of the '<em><b>Solution Improvement Settings</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Solution Improvement Settings</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Solution Improvement Settings</em>' containment reference.
	 * @see #setSolutionImprovementSettings(IndividualSolutionImprovementSettings)
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getOptimiserSettings_SolutionImprovementSettings()
	 * @model containment="true" required="true"
	 * @generated
	 */
	IndividualSolutionImprovementSettings getSolutionImprovementSettings();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.OptimiserSettings#getSolutionImprovementSettings <em>Solution Improvement Settings</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Solution Improvement Settings</em>' containment reference.
	 * @see #getSolutionImprovementSettings()
	 * @generated
	 */
	void setSolutionImprovementSettings(IndividualSolutionImprovementSettings value);

	/**
	 * Returns the value of the '<em><b>Build Action Sets</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Build Action Sets</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Build Action Sets</em>' attribute.
	 * @see #setBuildActionSets(boolean)
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getOptimiserSettings_BuildActionSets()
	 * @model
	 * @generated
	 */
	boolean isBuildActionSets();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.OptimiserSettings#isBuildActionSets <em>Build Action Sets</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Build Action Sets</em>' attribute.
	 * @see #isBuildActionSets()
	 * @generated
	 */
	void setBuildActionSets(boolean value);

	/**
	 * Returns the value of the '<em><b>Action Plan Settings</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Action Plan Settings</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Action Plan Settings</em>' containment reference.
	 * @see #setActionPlanSettings(ActionPlanSettings)
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getOptimiserSettings_ActionPlanSettings()
	 * @model containment="true" required="true"
	 * @generated
	 */
	ActionPlanSettings getActionPlanSettings();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.OptimiserSettings#getActionPlanSettings <em>Action Plan Settings</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Action Plan Settings</em>' containment reference.
	 * @see #getActionPlanSettings()
	 * @generated
	 */
	void setActionPlanSettings(ActionPlanSettings value);

	/**
	 * Returns the value of the '<em><b>Floating Days Limit</b></em>' attribute.
	 * The default value is <code>"15"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Floating Days Limit</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Floating Days Limit</em>' attribute.
	 * @see #setFloatingDaysLimit(int)
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getOptimiserSettings_FloatingDaysLimit()
	 * @model default="15"
	 * @generated
	 */
	int getFloatingDaysLimit();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.OptimiserSettings#getFloatingDaysLimit <em>Floating Days Limit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Floating Days Limit</em>' attribute.
	 * @see #getFloatingDaysLimit()
	 * @generated
	 */
	void setFloatingDaysLimit(int value);

} // end of  OptimiserSettings

// finish type fixing
