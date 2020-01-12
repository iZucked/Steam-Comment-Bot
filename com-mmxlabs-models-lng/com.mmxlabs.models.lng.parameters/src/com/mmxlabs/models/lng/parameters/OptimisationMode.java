/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.parameters;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Optimisation Mode</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getOptimisationMode()
 * @model
 * @generated
 */
public enum OptimisationMode implements Enumerator {
	/**
	 * The '<em><b>SHORT TERM</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #SHORT_TERM_VALUE
	 * @generated
	 * @ordered
	 */
	SHORT_TERM(0, "SHORT_TERM", "SHORT_TERM"),

	/**
	 * The '<em><b>ADP</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #ADP_VALUE
	 * @generated
	 * @ordered
	 */
	ADP(1, "ADP", "ADP"),

	/**
	 * The '<em><b>STRATEGIC</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #STRATEGIC_VALUE
	 * @generated
	 * @ordered
	 */
	STRATEGIC(2, "STRATEGIC", "STRATEGIC");

	/**
	 * The '<em><b>SHORT TERM</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>SHORT TERM</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #SHORT_TERM
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int SHORT_TERM_VALUE = 0;

	/**
	 * The '<em><b>ADP</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>ADP</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #ADP
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int ADP_VALUE = 1;

	/**
	 * The '<em><b>STRATEGIC</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>STRATEGIC</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #STRATEGIC
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int STRATEGIC_VALUE = 2;

	/**
	 * An array of all the '<em><b>Optimisation Mode</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final OptimisationMode[] VALUES_ARRAY =
		new OptimisationMode[] {
			SHORT_TERM,
			ADP,
			STRATEGIC,
		};

	/**
	 * A public read-only list of all the '<em><b>Optimisation Mode</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<OptimisationMode> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Optimisation Mode</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param literal the literal.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static OptimisationMode get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			OptimisationMode result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Optimisation Mode</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param name the name.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static OptimisationMode getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			OptimisationMode result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Optimisation Mode</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the integer value.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static OptimisationMode get(int value) {
		switch (value) {
			case SHORT_TERM_VALUE: return SHORT_TERM;
			case ADP_VALUE: return ADP;
			case STRATEGIC_VALUE: return STRATEGIC;
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final int value;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final String name;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final String literal;

	/**
	 * Only this class can construct instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private OptimisationMode(int value, String name, String literal) {
		this.value = value;
		this.name = name;
		this.literal = literal;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getValue() {
	  return value;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getName() {
	  return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getLiteral() {
	  return literal;
	}

	/**
	 * Returns the literal value of the enumerator, which is its string representation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		return literal;
	}
	
} //OptimisationMode
