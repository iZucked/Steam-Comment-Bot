/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp;

import com.mmxlabs.models.lng.commercial.SalesContract;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Fob Spacing Allocation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.FobSpacingAllocation#getMinSpacing <em>Min Spacing</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.FobSpacingAllocation#getMaxSpacing <em>Max Spacing</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.FobSpacingAllocation#getCargoCount <em>Cargo Count</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.adp.ADPPackage#getFobSpacingAllocation()
 * @model
 * @generated
 */
public interface FobSpacingAllocation extends SpacingAllocation {
	/**
	 * Returns the value of the '<em><b>Min Spacing</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Min Spacing</em>' attribute.
	 * @see #isSetMinSpacing()
	 * @see #unsetMinSpacing()
	 * @see #setMinSpacing(int)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getFobSpacingAllocation_MinSpacing()
	 * @model unsettable="true"
	 * @generated
	 */
	int getMinSpacing();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.FobSpacingAllocation#getMinSpacing <em>Min Spacing</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Min Spacing</em>' attribute.
	 * @see #isSetMinSpacing()
	 * @see #unsetMinSpacing()
	 * @see #getMinSpacing()
	 * @generated
	 */
	void setMinSpacing(int value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.adp.FobSpacingAllocation#getMinSpacing <em>Min Spacing</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetMinSpacing()
	 * @see #getMinSpacing()
	 * @see #setMinSpacing(int)
	 * @generated
	 */
	void unsetMinSpacing();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.adp.FobSpacingAllocation#getMinSpacing <em>Min Spacing</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Min Spacing</em>' attribute is set.
	 * @see #unsetMinSpacing()
	 * @see #getMinSpacing()
	 * @see #setMinSpacing(int)
	 * @generated
	 */
	boolean isSetMinSpacing();

	/**
	 * Returns the value of the '<em><b>Max Spacing</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Max Spacing</em>' attribute.
	 * @see #isSetMaxSpacing()
	 * @see #unsetMaxSpacing()
	 * @see #setMaxSpacing(int)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getFobSpacingAllocation_MaxSpacing()
	 * @model unsettable="true"
	 * @generated
	 */
	int getMaxSpacing();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.FobSpacingAllocation#getMaxSpacing <em>Max Spacing</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Max Spacing</em>' attribute.
	 * @see #isSetMaxSpacing()
	 * @see #unsetMaxSpacing()
	 * @see #getMaxSpacing()
	 * @generated
	 */
	void setMaxSpacing(int value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.adp.FobSpacingAllocation#getMaxSpacing <em>Max Spacing</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetMaxSpacing()
	 * @see #getMaxSpacing()
	 * @see #setMaxSpacing(int)
	 * @generated
	 */
	void unsetMaxSpacing();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.adp.FobSpacingAllocation#getMaxSpacing <em>Max Spacing</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Max Spacing</em>' attribute is set.
	 * @see #unsetMaxSpacing()
	 * @see #getMaxSpacing()
	 * @see #setMaxSpacing(int)
	 * @generated
	 */
	boolean isSetMaxSpacing();

	/**
	 * Returns the value of the '<em><b>Cargo Count</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cargo Count</em>' attribute.
	 * @see #setCargoCount(int)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getFobSpacingAllocation_CargoCount()
	 * @model
	 * @generated
	 */
	int getCargoCount();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.FobSpacingAllocation#getCargoCount <em>Cargo Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cargo Count</em>' attribute.
	 * @see #getCargoCount()
	 * @generated
	 */
	void setCargoCount(int value);

} // FobSpacingAllocation
