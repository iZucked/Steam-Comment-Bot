/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package scenario.optimiser.lso;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Thresholder Settings</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.optimiser.lso.ThresholderSettings#getAlpha <em>Alpha</em>}</li>
 *   <li>{@link scenario.optimiser.lso.ThresholderSettings#getInitialAcceptanceRate <em>Initial Acceptance Rate</em>}</li>
 *   <li>{@link scenario.optimiser.lso.ThresholderSettings#getEpochLength <em>Epoch Length</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.optimiser.lso.LsoPackage#getThresholderSettings()
 * @model
 * @generated
 */
public interface ThresholderSettings extends EObject {
	/**
	 * Returns the value of the '<em><b>Alpha</b></em>' attribute.
	 * The default value is <code>"0.95"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Alpha</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Alpha</em>' attribute.
	 * @see #setAlpha(double)
	 * @see scenario.optimiser.lso.LsoPackage#getThresholderSettings_Alpha()
	 * @model default="0.95"
	 * @generated
	 */
	double getAlpha();

	/**
	 * Sets the value of the '{@link scenario.optimiser.lso.ThresholderSettings#getAlpha <em>Alpha</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Alpha</em>' attribute.
	 * @see #getAlpha()
	 * @generated
	 */
	void setAlpha(double value);

	/**
	 * Returns the value of the '<em><b>Initial Acceptance Rate</b></em>' attribute.
	 * The default value is <code>"0.75"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Initial Acceptance Rate</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Initial Acceptance Rate</em>' attribute.
	 * @see #setInitialAcceptanceRate(double)
	 * @see scenario.optimiser.lso.LsoPackage#getThresholderSettings_InitialAcceptanceRate()
	 * @model default="0.75"
	 * @generated
	 */
	double getInitialAcceptanceRate();

	/**
	 * Sets the value of the '{@link scenario.optimiser.lso.ThresholderSettings#getInitialAcceptanceRate <em>Initial Acceptance Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Initial Acceptance Rate</em>' attribute.
	 * @see #getInitialAcceptanceRate()
	 * @generated
	 */
	void setInitialAcceptanceRate(double value);

	/**
	 * Returns the value of the '<em><b>Epoch Length</b></em>' attribute.
	 * The default value is <code>"1000"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Epoch Length</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Epoch Length</em>' attribute.
	 * @see #setEpochLength(int)
	 * @see scenario.optimiser.lso.LsoPackage#getThresholderSettings_EpochLength()
	 * @model default="1000"
	 * @generated
	 */
	int getEpochLength();

	/**
	 * Sets the value of the '{@link scenario.optimiser.lso.ThresholderSettings#getEpochLength <em>Epoch Length</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Epoch Length</em>' attribute.
	 * @see #getEpochLength()
	 * @generated
	 */
	void setEpochLength(int value);

} // ThresholderSettings
