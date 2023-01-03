/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.lingo.reports.views.changeset.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Change Set Vessel Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetVesselType()
 * @model
 * @generated
 */
public enum ChangeSetVesselType implements Enumerator {
	/**
	 * The '<em><b>Fleet</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #FLEET_VALUE
	 * @generated
	 * @ordered
	 */
	FLEET(0, "Fleet", "Fleet"),

	/**
	 * The '<em><b>Market</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #MARKET_VALUE
	 * @generated
	 * @ordered
	 */
	MARKET(1, "Market", "Market"),

	/**
	 * The '<em><b>Nominal</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #NOMINAL_VALUE
	 * @generated
	 * @ordered
	 */
	NOMINAL(2, "Nominal", "Nominal"), /**
	 * The '<em><b>FOB</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #FOB_VALUE
	 * @generated
	 * @ordered
	 */
	FOB(3, "FOB", "FOB"), /**
	 * The '<em><b>DES</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #DES_VALUE
	 * @generated
	 * @ordered
	 */
	DES(4, "DES", "DES");

	/**
	 * The '<em><b>Fleet</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Fleet</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #FLEET
	 * @model name="Fleet"
	 * @generated
	 * @ordered
	 */
	public static final int FLEET_VALUE = 0;

	/**
	 * The '<em><b>Market</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Market</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #MARKET
	 * @model name="Market"
	 * @generated
	 * @ordered
	 */
	public static final int MARKET_VALUE = 1;

	/**
	 * The '<em><b>Nominal</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Nominal</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #NOMINAL
	 * @model name="Nominal"
	 * @generated
	 * @ordered
	 */
	public static final int NOMINAL_VALUE = 2;

	/**
	 * The '<em><b>FOB</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>FOB</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #FOB
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int FOB_VALUE = 3;

	/**
	 * The '<em><b>DES</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>DES</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #DES
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int DES_VALUE = 4;

	/**
	 * An array of all the '<em><b>Change Set Vessel Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final ChangeSetVesselType[] VALUES_ARRAY =
		new ChangeSetVesselType[] {
			FLEET,
			MARKET,
			NOMINAL,
			FOB,
			DES,
		};

	/**
	 * A public read-only list of all the '<em><b>Change Set Vessel Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<ChangeSetVesselType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Change Set Vessel Type</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param literal the literal.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static ChangeSetVesselType get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			ChangeSetVesselType result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Change Set Vessel Type</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param name the name.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static ChangeSetVesselType getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			ChangeSetVesselType result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Change Set Vessel Type</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the integer value.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static ChangeSetVesselType get(int value) {
		switch (value) {
			case FLEET_VALUE: return FLEET;
			case MARKET_VALUE: return MARKET;
			case NOMINAL_VALUE: return NOMINAL;
			case FOB_VALUE: return FOB;
			case DES_VALUE: return DES;
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
	private ChangeSetVesselType(int value, String name, String literal) {
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
	
} //ChangeSetVesselType
