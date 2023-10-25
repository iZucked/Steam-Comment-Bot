/**
 */
package com.mmxlabs.models.lng.parameters;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Adp Optimisation Mode</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getAdpOptimisationMode()
 * @model
 * @generated
 */
public enum AdpOptimisationMode implements Enumerator {
	/**
	 * The '<em><b>NON CLEAN SLATE</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #NON_CLEAN_SLATE_VALUE
	 * @generated
	 * @ordered
	 */
	NON_CLEAN_SLATE(0, "NON_CLEAN_SLATE", "NON_CLEAN_SLATE"), /**
	 * The '<em><b>PARTIAL CLEAN SLATE</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #PARTIAL_CLEAN_SLATE_VALUE
	 * @generated
	 * @ordered
	 */
	PARTIAL_CLEAN_SLATE(1, "PARTIAL_CLEAN_SLATE", "PARTIAL_CLEAN_SLATE"), /**
	 * The '<em><b>CLEAN SLATE</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #CLEAN_SLATE_VALUE
	 * @generated
	 * @ordered
	 */
	CLEAN_SLATE(2, "CLEAN_SLATE", "CLEAN_SLATE");

	/**
	 * The '<em><b>NON CLEAN SLATE</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #NON_CLEAN_SLATE
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int NON_CLEAN_SLATE_VALUE = 0;

	/**
	 * The '<em><b>PARTIAL CLEAN SLATE</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #PARTIAL_CLEAN_SLATE
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int PARTIAL_CLEAN_SLATE_VALUE = 1;

	/**
	 * The '<em><b>CLEAN SLATE</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #CLEAN_SLATE
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int CLEAN_SLATE_VALUE = 2;

	/**
	 * An array of all the '<em><b>Adp Optimisation Mode</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final AdpOptimisationMode[] VALUES_ARRAY =
		new AdpOptimisationMode[] {
			NON_CLEAN_SLATE,
			PARTIAL_CLEAN_SLATE,
			CLEAN_SLATE,
		};

	/**
	 * A public read-only list of all the '<em><b>Adp Optimisation Mode</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<AdpOptimisationMode> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Adp Optimisation Mode</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param literal the literal.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static AdpOptimisationMode get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			AdpOptimisationMode result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Adp Optimisation Mode</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param name the name.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static AdpOptimisationMode getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			AdpOptimisationMode result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Adp Optimisation Mode</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the integer value.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static AdpOptimisationMode get(int value) {
		switch (value) {
			case NON_CLEAN_SLATE_VALUE: return NON_CLEAN_SLATE;
			case PARTIAL_CLEAN_SLATE_VALUE: return PARTIAL_CLEAN_SLATE;
			case CLEAN_SLATE_VALUE: return CLEAN_SLATE;
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
	private AdpOptimisationMode(int value, String name, String literal) {
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
	
} //AdpOptimisationMode
