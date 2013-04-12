/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.parameters;
import java.util.Date;
import org.eclipse.emf.ecore.EObject;
import java.util.Date;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Optimisation Range</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.parameters.OptimisationRange#getOptimiseAfter <em>Optimise After</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.OptimisationRange#getOptimiseBefore <em>Optimise Before</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getOptimisationRange()
 * @model
 * @generated
 */
public interface OptimisationRange extends EObject {
	/**
	 * Returns the value of the '<em><b>Optimise After</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Optimise After</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Optimise After</em>' attribute.
	 * @see #isSetOptimiseAfter()
	 * @see #unsetOptimiseAfter()
	 * @see #setOptimiseAfter(Date)
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getOptimisationRange_OptimiseAfter()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	Date getOptimiseAfter();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.OptimisationRange#getOptimiseAfter <em>Optimise After</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Optimise After</em>' attribute.
	 * @see #isSetOptimiseAfter()
	 * @see #unsetOptimiseAfter()
	 * @see #getOptimiseAfter()
	 * @generated
	 */
	void setOptimiseAfter(Date value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.parameters.OptimisationRange#getOptimiseAfter <em>Optimise After</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetOptimiseAfter()
	 * @see #getOptimiseAfter()
	 * @see #setOptimiseAfter(Date)
	 * @generated
	 */
	void unsetOptimiseAfter();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.parameters.OptimisationRange#getOptimiseAfter <em>Optimise After</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Optimise After</em>' attribute is set.
	 * @see #unsetOptimiseAfter()
	 * @see #getOptimiseAfter()
	 * @see #setOptimiseAfter(Date)
	 * @generated
	 */
	boolean isSetOptimiseAfter();

	/**
	 * Returns the value of the '<em><b>Optimise Before</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Optimise Before</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Optimise Before</em>' attribute.
	 * @see #isSetOptimiseBefore()
	 * @see #unsetOptimiseBefore()
	 * @see #setOptimiseBefore(Date)
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getOptimisationRange_OptimiseBefore()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	Date getOptimiseBefore();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.OptimisationRange#getOptimiseBefore <em>Optimise Before</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Optimise Before</em>' attribute.
	 * @see #isSetOptimiseBefore()
	 * @see #unsetOptimiseBefore()
	 * @see #getOptimiseBefore()
	 * @generated
	 */
	void setOptimiseBefore(Date value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.parameters.OptimisationRange#getOptimiseBefore <em>Optimise Before</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetOptimiseBefore()
	 * @see #getOptimiseBefore()
	 * @see #setOptimiseBefore(Date)
	 * @generated
	 */
	void unsetOptimiseBefore();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.parameters.OptimisationRange#getOptimiseBefore <em>Optimise Before</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Optimise Before</em>' attribute is set.
	 * @see #unsetOptimiseBefore()
	 * @see #getOptimiseBefore()
	 * @see #setOptimiseBefore(Date)
	 * @generated
	 */
	boolean isSetOptimiseBefore();

} // end of  OptimisationRange

// finish type fixing
