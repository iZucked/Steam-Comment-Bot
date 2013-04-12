/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo;

import com.mmxlabs.models.lng.types.TypesPackage;

import com.mmxlabs.models.mmxcore.MMXCorePackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see com.mmxlabs.models.lng.cargo.CargoFactory
 * @model kind="package"
 * @generated
 */
public interface CargoPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "cargo";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.mmxlabs.com/models/lng/cargo/1/";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "lng.cargo";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	CargoPackage eINSTANCE = com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl.init();

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.cargo.impl.CargoImpl <em>Cargo</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoImpl
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getCargo()
	 * @generated
	 */
	int CARGO = 0;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO__EXTENSIONS = TypesPackage.ACARGO__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO__UUID = TypesPackage.ACARGO__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO__NAME = TypesPackage.ACARGO__NAME;

	/**
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO__OTHER_NAMES = TypesPackage.ACARGO__OTHER_NAMES;

	/**
	 * The feature id for the '<em><b>Allow Rewiring</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO__ALLOW_REWIRING = TypesPackage.ACARGO_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Allowed Vessels</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO__ALLOWED_VESSELS = TypesPackage.ACARGO_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Slots</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO__SLOTS = TypesPackage.ACARGO_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Cargo</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_FEATURE_COUNT = TypesPackage.ACARGO_FEATURE_COUNT + 3;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO___GET_UNSET_VALUE__ESTRUCTURALFEATURE = TypesPackage.ACARGO___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = TypesPackage.ACARGO___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO___ECONTAINER_OP = TypesPackage.ACARGO___ECONTAINER_OP;

	/**
	 * The operation id for the '<em>Get Cargo Type</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO___GET_CARGO_TYPE = TypesPackage.ACARGO_OPERATION_COUNT + 0;

	/**
	 * The operation id for the '<em>Get Sorted Slots</em>' operation.
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO___GET_SORTED_SLOTS = TypesPackage.ACARGO_OPERATION_COUNT + 1;

	/**
	 * The number of operations of the '<em>Cargo</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_OPERATION_COUNT = TypesPackage.ACARGO_OPERATION_COUNT + 2;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl <em>Slot</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.impl.SlotImpl
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getSlot()
	 * @generated
	 */
	int SLOT = 1;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__EXTENSIONS = TypesPackage.ASLOT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__UUID = TypesPackage.ASLOT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__NAME = TypesPackage.ASLOT__NAME;

	/**
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__OTHER_NAMES = TypesPackage.ASLOT__OTHER_NAMES;

	/**
	 * The feature id for the '<em><b>Contract</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__CONTRACT = TypesPackage.ASLOT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__PORT = TypesPackage.ASLOT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Window Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__WINDOW_START = TypesPackage.ASLOT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Window Start Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__WINDOW_START_TIME = TypesPackage.ASLOT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Window Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__WINDOW_SIZE = TypesPackage.ASLOT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__DURATION = TypesPackage.ASLOT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Min Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__MIN_QUANTITY = TypesPackage.ASLOT_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Max Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__MAX_QUANTITY = TypesPackage.ASLOT_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Optional</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__OPTIONAL = TypesPackage.ASLOT_FEATURE_COUNT + 8;

	/**
	 * The feature id for the '<em><b>Price Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__PRICE_EXPRESSION = TypesPackage.ASLOT_FEATURE_COUNT + 9;

	/**
	 * The feature id for the '<em><b>Cargo</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__CARGO = TypesPackage.ASLOT_FEATURE_COUNT + 10;

	/**
	 * The number of structural features of the '<em>Slot</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_FEATURE_COUNT = TypesPackage.ASLOT_FEATURE_COUNT + 11;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT___GET_UNSET_VALUE__ESTRUCTURALFEATURE = TypesPackage.ASLOT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = TypesPackage.ASLOT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT___ECONTAINER_OP = TypesPackage.ASLOT___ECONTAINER_OP;

	/**
	 * The operation id for the '<em>Get Time Zone</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT___GET_TIME_ZONE__EATTRIBUTE = TypesPackage.ASLOT_OPERATION_COUNT + 0;

	/**
	 * The operation id for the '<em>Get Slot Or Port Duration</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT___GET_SLOT_OR_PORT_DURATION = TypesPackage.ASLOT_OPERATION_COUNT + 1;

	/**
	 * The operation id for the '<em>Get Slot Or Contract Min Quantity</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT___GET_SLOT_OR_CONTRACT_MIN_QUANTITY = TypesPackage.ASLOT_OPERATION_COUNT + 2;

	/**
	 * The operation id for the '<em>Get Slot Or Contract Max Quantity</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT___GET_SLOT_OR_CONTRACT_MAX_QUANTITY = TypesPackage.ASLOT_OPERATION_COUNT + 3;

	/**
	 * The operation id for the '<em>Get Window End With Slot Or Port Time</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT___GET_WINDOW_END_WITH_SLOT_OR_PORT_TIME = TypesPackage.ASLOT_OPERATION_COUNT + 4;

	/**
	 * The operation id for the '<em>Get Window Start With Slot Or Port Time</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT___GET_WINDOW_START_WITH_SLOT_OR_PORT_TIME = TypesPackage.ASLOT_OPERATION_COUNT + 5;

	/**
	 * The operation id for the '<em>Get Slot Or Port Window Size</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT___GET_SLOT_OR_PORT_WINDOW_SIZE = TypesPackage.ASLOT_OPERATION_COUNT + 6;

	/**
	 * The number of operations of the '<em>Slot</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_OPERATION_COUNT = TypesPackage.ASLOT_OPERATION_COUNT + 7;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.cargo.impl.LoadSlotImpl <em>Load Slot</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.impl.LoadSlotImpl
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getLoadSlot()
	 * @generated
	 */
	int LOAD_SLOT = 2;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__EXTENSIONS = SLOT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__UUID = SLOT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__NAME = SLOT__NAME;

	/**
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__OTHER_NAMES = SLOT__OTHER_NAMES;

	/**
	 * The feature id for the '<em><b>Contract</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__CONTRACT = SLOT__CONTRACT;

	/**
	 * The feature id for the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__PORT = SLOT__PORT;

	/**
	 * The feature id for the '<em><b>Window Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__WINDOW_START = SLOT__WINDOW_START;

	/**
	 * The feature id for the '<em><b>Window Start Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__WINDOW_START_TIME = SLOT__WINDOW_START_TIME;

	/**
	 * The feature id for the '<em><b>Window Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__WINDOW_SIZE = SLOT__WINDOW_SIZE;

	/**
	 * The feature id for the '<em><b>Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__DURATION = SLOT__DURATION;

	/**
	 * The feature id for the '<em><b>Min Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__MIN_QUANTITY = SLOT__MIN_QUANTITY;

	/**
	 * The feature id for the '<em><b>Max Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__MAX_QUANTITY = SLOT__MAX_QUANTITY;

	/**
	 * The feature id for the '<em><b>Optional</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__OPTIONAL = SLOT__OPTIONAL;

	/**
	 * The feature id for the '<em><b>Price Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__PRICE_EXPRESSION = SLOT__PRICE_EXPRESSION;

	/**
	 * The feature id for the '<em><b>Cargo</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__CARGO = SLOT__CARGO;

	/**
	 * The feature id for the '<em><b>Cargo CV</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__CARGO_CV = SLOT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Arrive Cold</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__ARRIVE_COLD = SLOT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>DES Purchase</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__DES_PURCHASE = SLOT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Load Slot</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT_FEATURE_COUNT = SLOT_FEATURE_COUNT + 3;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___GET_UNSET_VALUE__ESTRUCTURALFEATURE = SLOT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = SLOT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___ECONTAINER_OP = SLOT___ECONTAINER_OP;

	/**
	 * The operation id for the '<em>Get Time Zone</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___GET_TIME_ZONE__EATTRIBUTE = SLOT___GET_TIME_ZONE__EATTRIBUTE;

	/**
	 * The operation id for the '<em>Get Slot Or Port Duration</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___GET_SLOT_OR_PORT_DURATION = SLOT___GET_SLOT_OR_PORT_DURATION;

	/**
	 * The operation id for the '<em>Get Slot Or Contract Min Quantity</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___GET_SLOT_OR_CONTRACT_MIN_QUANTITY = SLOT___GET_SLOT_OR_CONTRACT_MIN_QUANTITY;

	/**
	 * The operation id for the '<em>Get Slot Or Contract Max Quantity</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___GET_SLOT_OR_CONTRACT_MAX_QUANTITY = SLOT___GET_SLOT_OR_CONTRACT_MAX_QUANTITY;

	/**
	 * The operation id for the '<em>Get Window End With Slot Or Port Time</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___GET_WINDOW_END_WITH_SLOT_OR_PORT_TIME = SLOT___GET_WINDOW_END_WITH_SLOT_OR_PORT_TIME;

	/**
	 * The operation id for the '<em>Get Window Start With Slot Or Port Time</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___GET_WINDOW_START_WITH_SLOT_OR_PORT_TIME = SLOT___GET_WINDOW_START_WITH_SLOT_OR_PORT_TIME;

	/**
	 * The operation id for the '<em>Get Slot Or Port Window Size</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___GET_SLOT_OR_PORT_WINDOW_SIZE = SLOT___GET_SLOT_OR_PORT_WINDOW_SIZE;

	/**
	 * The operation id for the '<em>Get Slot Or Port CV</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___GET_SLOT_OR_PORT_CV = SLOT_OPERATION_COUNT + 0;

	/**
	 * The number of operations of the '<em>Load Slot</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT_OPERATION_COUNT = SLOT_OPERATION_COUNT + 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.cargo.impl.DischargeSlotImpl <em>Discharge Slot</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.impl.DischargeSlotImpl
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getDischargeSlot()
	 * @generated
	 */
	int DISCHARGE_SLOT = 3;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__EXTENSIONS = SLOT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__UUID = SLOT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__NAME = SLOT__NAME;

	/**
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__OTHER_NAMES = SLOT__OTHER_NAMES;

	/**
	 * The feature id for the '<em><b>Contract</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__CONTRACT = SLOT__CONTRACT;

	/**
	 * The feature id for the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__PORT = SLOT__PORT;

	/**
	 * The feature id for the '<em><b>Window Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__WINDOW_START = SLOT__WINDOW_START;

	/**
	 * The feature id for the '<em><b>Window Start Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__WINDOW_START_TIME = SLOT__WINDOW_START_TIME;

	/**
	 * The feature id for the '<em><b>Window Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__WINDOW_SIZE = SLOT__WINDOW_SIZE;

	/**
	 * The feature id for the '<em><b>Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__DURATION = SLOT__DURATION;

	/**
	 * The feature id for the '<em><b>Min Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__MIN_QUANTITY = SLOT__MIN_QUANTITY;

	/**
	 * The feature id for the '<em><b>Max Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__MAX_QUANTITY = SLOT__MAX_QUANTITY;

	/**
	 * The feature id for the '<em><b>Optional</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__OPTIONAL = SLOT__OPTIONAL;

	/**
	 * The feature id for the '<em><b>Price Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__PRICE_EXPRESSION = SLOT__PRICE_EXPRESSION;

	/**
	 * The feature id for the '<em><b>Cargo</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__CARGO = SLOT__CARGO;

	/**
	 * The feature id for the '<em><b>FOB Sale</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__FOB_SALE = SLOT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Purchase Delivery Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__PURCHASE_DELIVERY_TYPE = SLOT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Discharge Slot</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT_FEATURE_COUNT = SLOT_FEATURE_COUNT + 2;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT___GET_UNSET_VALUE__ESTRUCTURALFEATURE = SLOT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = SLOT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT___ECONTAINER_OP = SLOT___ECONTAINER_OP;

	/**
	 * The operation id for the '<em>Get Time Zone</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT___GET_TIME_ZONE__EATTRIBUTE = SLOT___GET_TIME_ZONE__EATTRIBUTE;

	/**
	 * The operation id for the '<em>Get Slot Or Port Duration</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT___GET_SLOT_OR_PORT_DURATION = SLOT___GET_SLOT_OR_PORT_DURATION;

	/**
	 * The operation id for the '<em>Get Slot Or Contract Min Quantity</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT___GET_SLOT_OR_CONTRACT_MIN_QUANTITY = SLOT___GET_SLOT_OR_CONTRACT_MIN_QUANTITY;

	/**
	 * The operation id for the '<em>Get Slot Or Contract Max Quantity</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT___GET_SLOT_OR_CONTRACT_MAX_QUANTITY = SLOT___GET_SLOT_OR_CONTRACT_MAX_QUANTITY;

	/**
	 * The operation id for the '<em>Get Window End With Slot Or Port Time</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT___GET_WINDOW_END_WITH_SLOT_OR_PORT_TIME = SLOT___GET_WINDOW_END_WITH_SLOT_OR_PORT_TIME;

	/**
	 * The operation id for the '<em>Get Window Start With Slot Or Port Time</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT___GET_WINDOW_START_WITH_SLOT_OR_PORT_TIME = SLOT___GET_WINDOW_START_WITH_SLOT_OR_PORT_TIME;

	/**
	 * The operation id for the '<em>Get Slot Or Port Window Size</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT___GET_SLOT_OR_PORT_WINDOW_SIZE = SLOT___GET_SLOT_OR_PORT_WINDOW_SIZE;

	/**
	 * The number of operations of the '<em>Discharge Slot</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT_OPERATION_COUNT = SLOT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.cargo.impl.CargoModelImpl <em>Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoModelImpl
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getCargoModel()
	 * @generated
	 */
	int CARGO_MODEL = 4;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_MODEL__EXTENSIONS = MMXCorePackage.UUID_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_MODEL__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Load Slots</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_MODEL__LOAD_SLOTS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Discharge Slots</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_MODEL__DISCHARGE_SLOTS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Cargoes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_MODEL__CARGOES = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Cargo Groups</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_MODEL__CARGO_GROUPS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_MODEL_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_MODEL___GET_UNSET_VALUE__ESTRUCTURALFEATURE = MMXCorePackage.UUID_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_MODEL___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = MMXCorePackage.UUID_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_MODEL___ECONTAINER_OP = MMXCorePackage.UUID_OBJECT___ECONTAINER_OP;

	/**
	 * The number of operations of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_MODEL_OPERATION_COUNT = MMXCorePackage.UUID_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.cargo.impl.SpotSlotImpl <em>Spot Slot</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.impl.SpotSlotImpl
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getSpotSlot()
	 * @generated
	 */
	int SPOT_SLOT = 5;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_SLOT__EXTENSIONS = MMXCorePackage.MMX_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Market</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_SLOT__MARKET = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Spot Slot</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_SLOT_FEATURE_COUNT = MMXCorePackage.MMX_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_SLOT___GET_UNSET_VALUE__ESTRUCTURALFEATURE = MMXCorePackage.MMX_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_SLOT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = MMXCorePackage.MMX_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_SLOT___ECONTAINER_OP = MMXCorePackage.MMX_OBJECT___ECONTAINER_OP;

	/**
	 * The number of operations of the '<em>Spot Slot</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_SLOT_OPERATION_COUNT = MMXCorePackage.MMX_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.cargo.impl.SpotLoadSlotImpl <em>Spot Load Slot</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.impl.SpotLoadSlotImpl
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getSpotLoadSlot()
	 * @generated
	 */
	int SPOT_LOAD_SLOT = 6;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__EXTENSIONS = LOAD_SLOT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__UUID = LOAD_SLOT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__NAME = LOAD_SLOT__NAME;

	/**
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__OTHER_NAMES = LOAD_SLOT__OTHER_NAMES;

	/**
	 * The feature id for the '<em><b>Contract</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__CONTRACT = LOAD_SLOT__CONTRACT;

	/**
	 * The feature id for the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__PORT = LOAD_SLOT__PORT;

	/**
	 * The feature id for the '<em><b>Window Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__WINDOW_START = LOAD_SLOT__WINDOW_START;

	/**
	 * The feature id for the '<em><b>Window Start Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__WINDOW_START_TIME = LOAD_SLOT__WINDOW_START_TIME;

	/**
	 * The feature id for the '<em><b>Window Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__WINDOW_SIZE = LOAD_SLOT__WINDOW_SIZE;

	/**
	 * The feature id for the '<em><b>Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__DURATION = LOAD_SLOT__DURATION;

	/**
	 * The feature id for the '<em><b>Min Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__MIN_QUANTITY = LOAD_SLOT__MIN_QUANTITY;

	/**
	 * The feature id for the '<em><b>Max Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__MAX_QUANTITY = LOAD_SLOT__MAX_QUANTITY;

	/**
	 * The feature id for the '<em><b>Optional</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__OPTIONAL = LOAD_SLOT__OPTIONAL;

	/**
	 * The feature id for the '<em><b>Price Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__PRICE_EXPRESSION = LOAD_SLOT__PRICE_EXPRESSION;

	/**
	 * The feature id for the '<em><b>Cargo</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__CARGO = LOAD_SLOT__CARGO;

	/**
	 * The feature id for the '<em><b>Cargo CV</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__CARGO_CV = LOAD_SLOT__CARGO_CV;

	/**
	 * The feature id for the '<em><b>Arrive Cold</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__ARRIVE_COLD = LOAD_SLOT__ARRIVE_COLD;

	/**
	 * The feature id for the '<em><b>DES Purchase</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__DES_PURCHASE = LOAD_SLOT__DES_PURCHASE;

	/**
	 * The feature id for the '<em><b>Market</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__MARKET = LOAD_SLOT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Spot Load Slot</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT_FEATURE_COUNT = LOAD_SLOT_FEATURE_COUNT + 1;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT___GET_UNSET_VALUE__ESTRUCTURALFEATURE = LOAD_SLOT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = LOAD_SLOT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT___ECONTAINER_OP = LOAD_SLOT___ECONTAINER_OP;

	/**
	 * The operation id for the '<em>Get Time Zone</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT___GET_TIME_ZONE__EATTRIBUTE = LOAD_SLOT___GET_TIME_ZONE__EATTRIBUTE;

	/**
	 * The operation id for the '<em>Get Slot Or Port Duration</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT___GET_SLOT_OR_PORT_DURATION = LOAD_SLOT___GET_SLOT_OR_PORT_DURATION;

	/**
	 * The operation id for the '<em>Get Slot Or Contract Min Quantity</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT___GET_SLOT_OR_CONTRACT_MIN_QUANTITY = LOAD_SLOT___GET_SLOT_OR_CONTRACT_MIN_QUANTITY;

	/**
	 * The operation id for the '<em>Get Slot Or Contract Max Quantity</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT___GET_SLOT_OR_CONTRACT_MAX_QUANTITY = LOAD_SLOT___GET_SLOT_OR_CONTRACT_MAX_QUANTITY;

	/**
	 * The operation id for the '<em>Get Window End With Slot Or Port Time</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT___GET_WINDOW_END_WITH_SLOT_OR_PORT_TIME = LOAD_SLOT___GET_WINDOW_END_WITH_SLOT_OR_PORT_TIME;

	/**
	 * The operation id for the '<em>Get Window Start With Slot Or Port Time</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT___GET_WINDOW_START_WITH_SLOT_OR_PORT_TIME = LOAD_SLOT___GET_WINDOW_START_WITH_SLOT_OR_PORT_TIME;

	/**
	 * The operation id for the '<em>Get Slot Or Port Window Size</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT___GET_SLOT_OR_PORT_WINDOW_SIZE = LOAD_SLOT___GET_SLOT_OR_PORT_WINDOW_SIZE;

	/**
	 * The operation id for the '<em>Get Slot Or Port CV</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT___GET_SLOT_OR_PORT_CV = LOAD_SLOT___GET_SLOT_OR_PORT_CV;

	/**
	 * The number of operations of the '<em>Spot Load Slot</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT_OPERATION_COUNT = LOAD_SLOT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.cargo.impl.SpotDischargeSlotImpl <em>Spot Discharge Slot</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.impl.SpotDischargeSlotImpl
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getSpotDischargeSlot()
	 * @generated
	 */
	int SPOT_DISCHARGE_SLOT = 7;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__EXTENSIONS = DISCHARGE_SLOT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__UUID = DISCHARGE_SLOT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__NAME = DISCHARGE_SLOT__NAME;

	/**
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__OTHER_NAMES = DISCHARGE_SLOT__OTHER_NAMES;

	/**
	 * The feature id for the '<em><b>Contract</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__CONTRACT = DISCHARGE_SLOT__CONTRACT;

	/**
	 * The feature id for the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__PORT = DISCHARGE_SLOT__PORT;

	/**
	 * The feature id for the '<em><b>Window Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__WINDOW_START = DISCHARGE_SLOT__WINDOW_START;

	/**
	 * The feature id for the '<em><b>Window Start Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__WINDOW_START_TIME = DISCHARGE_SLOT__WINDOW_START_TIME;

	/**
	 * The feature id for the '<em><b>Window Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__WINDOW_SIZE = DISCHARGE_SLOT__WINDOW_SIZE;

	/**
	 * The feature id for the '<em><b>Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__DURATION = DISCHARGE_SLOT__DURATION;

	/**
	 * The feature id for the '<em><b>Min Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__MIN_QUANTITY = DISCHARGE_SLOT__MIN_QUANTITY;

	/**
	 * The feature id for the '<em><b>Max Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__MAX_QUANTITY = DISCHARGE_SLOT__MAX_QUANTITY;

	/**
	 * The feature id for the '<em><b>Optional</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__OPTIONAL = DISCHARGE_SLOT__OPTIONAL;

	/**
	 * The feature id for the '<em><b>Price Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__PRICE_EXPRESSION = DISCHARGE_SLOT__PRICE_EXPRESSION;

	/**
	 * The feature id for the '<em><b>Cargo</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__CARGO = DISCHARGE_SLOT__CARGO;

	/**
	 * The feature id for the '<em><b>FOB Sale</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__FOB_SALE = DISCHARGE_SLOT__FOB_SALE;

	/**
	 * The feature id for the '<em><b>Purchase Delivery Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__PURCHASE_DELIVERY_TYPE = DISCHARGE_SLOT__PURCHASE_DELIVERY_TYPE;

	/**
	 * The feature id for the '<em><b>Market</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__MARKET = DISCHARGE_SLOT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Spot Discharge Slot</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT_FEATURE_COUNT = DISCHARGE_SLOT_FEATURE_COUNT + 1;


	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT___GET_UNSET_VALUE__ESTRUCTURALFEATURE = DISCHARGE_SLOT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = DISCHARGE_SLOT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT___ECONTAINER_OP = DISCHARGE_SLOT___ECONTAINER_OP;

	/**
	 * The operation id for the '<em>Get Time Zone</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT___GET_TIME_ZONE__EATTRIBUTE = DISCHARGE_SLOT___GET_TIME_ZONE__EATTRIBUTE;

	/**
	 * The operation id for the '<em>Get Slot Or Port Duration</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT___GET_SLOT_OR_PORT_DURATION = DISCHARGE_SLOT___GET_SLOT_OR_PORT_DURATION;

	/**
	 * The operation id for the '<em>Get Slot Or Contract Min Quantity</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT___GET_SLOT_OR_CONTRACT_MIN_QUANTITY = DISCHARGE_SLOT___GET_SLOT_OR_CONTRACT_MIN_QUANTITY;

	/**
	 * The operation id for the '<em>Get Slot Or Contract Max Quantity</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT___GET_SLOT_OR_CONTRACT_MAX_QUANTITY = DISCHARGE_SLOT___GET_SLOT_OR_CONTRACT_MAX_QUANTITY;

	/**
	 * The operation id for the '<em>Get Window End With Slot Or Port Time</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT___GET_WINDOW_END_WITH_SLOT_OR_PORT_TIME = DISCHARGE_SLOT___GET_WINDOW_END_WITH_SLOT_OR_PORT_TIME;

	/**
	 * The operation id for the '<em>Get Window Start With Slot Or Port Time</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT___GET_WINDOW_START_WITH_SLOT_OR_PORT_TIME = DISCHARGE_SLOT___GET_WINDOW_START_WITH_SLOT_OR_PORT_TIME;

	/**
	 * The operation id for the '<em>Get Slot Or Port Window Size</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT___GET_SLOT_OR_PORT_WINDOW_SIZE = DISCHARGE_SLOT___GET_SLOT_OR_PORT_WINDOW_SIZE;

	/**
	 * The number of operations of the '<em>Spot Discharge Slot</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT_OPERATION_COUNT = DISCHARGE_SLOT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.cargo.impl.CargoGroupImpl <em>Group</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoGroupImpl
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getCargoGroup()
	 * @generated
	 */
	int CARGO_GROUP = 8;

	/**
	 * The feature id for the '<em><b>Extensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_GROUP__EXTENSIONS = MMXCorePackage.NAMED_OBJECT__EXTENSIONS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_GROUP__NAME = MMXCorePackage.NAMED_OBJECT__NAME;

	/**
	 * The feature id for the '<em><b>Other Names</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_GROUP__OTHER_NAMES = MMXCorePackage.NAMED_OBJECT__OTHER_NAMES;

	/**
	 * The feature id for the '<em><b>Cargoes</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_GROUP__CARGOES = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Group</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_GROUP_FEATURE_COUNT = MMXCorePackage.NAMED_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The operation id for the '<em>Get Unset Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_GROUP___GET_UNSET_VALUE__ESTRUCTURALFEATURE = MMXCorePackage.NAMED_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EGet With Default</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_GROUP___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE = MMXCorePackage.NAMED_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>EContainer Op</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_GROUP___ECONTAINER_OP = MMXCorePackage.NAMED_OBJECT___ECONTAINER_OP;

	/**
	 * The number of operations of the '<em>Group</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_GROUP_OPERATION_COUNT = MMXCorePackage.NAMED_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.cargo.CargoType <em>Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.CargoType
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getCargoType()
	 * @generated
	 */
	int CARGO_TYPE = 9;


	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.cargo.Cargo <em>Cargo</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Cargo</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Cargo
	 * @generated
	 */
	EClass getCargo();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.Cargo#isAllowRewiring <em>Allow Rewiring</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Allow Rewiring</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Cargo#isAllowRewiring()
	 * @see #getCargo()
	 * @generated
	 */
	EAttribute getCargo_AllowRewiring();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.cargo.Cargo#getAllowedVessels <em>Allowed Vessels</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Allowed Vessels</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Cargo#getAllowedVessels()
	 * @see #getCargo()
	 * @generated
	 */
	EReference getCargo_AllowedVessels();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.cargo.Cargo#getSlots <em>Slots</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Slots</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Cargo#getSlots()
	 * @see #getCargo()
	 * @generated
	 */
	EReference getCargo_Slots();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.Cargo#getCargoType() <em>Get Cargo Type</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Cargo Type</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.Cargo#getCargoType()
	 * @generated
	 */
	EOperation getCargo__GetCargoType();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.Cargo#getSortedSlots() <em>Get Sorted Slots</em>}' operation.
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Sorted Slots</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.Cargo#getSortedSlots()
	 * @generated
	 */
	EOperation getCargo__GetSortedSlots();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.cargo.Slot <em>Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Slot</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot
	 * @generated
	 */
	EClass getSlot();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.cargo.Slot#getContract <em>Contract</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Contract</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getContract()
	 * @see #getSlot()
	 * @generated
	 */
	EReference getSlot_Contract();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.Slot#getWindowStart <em>Window Start</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Window Start</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getWindowStart()
	 * @see #getSlot()
	 * @generated
	 */
	EAttribute getSlot_WindowStart();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.Slot#getWindowStartTime <em>Window Start Time</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Window Start Time</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getWindowStartTime()
	 * @see #getSlot()
	 * @generated
	 */
	EAttribute getSlot_WindowStartTime();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.Slot#getWindowSize <em>Window Size</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Window Size</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getWindowSize()
	 * @see #getSlot()
	 * @generated
	 */
	EAttribute getSlot_WindowSize();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.cargo.Slot#getPort <em>Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Port</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getPort()
	 * @see #getSlot()
	 * @generated
	 */
	EReference getSlot_Port();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.cargo.Slot#getContract <em>Contract</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Contract</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getContract()
	 * @see #getSlot()
	 * @generated
	 */
//	EReference getSlot_Contract();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.Slot#getDuration <em>Duration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Duration</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getDuration()
	 * @see #getSlot()
	 * @generated
	 */
	EAttribute getSlot_Duration();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.Slot#getMinQuantity <em>Min Quantity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Min Quantity</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getMinQuantity()
	 * @see #getSlot()
	 * @generated
	 */
	EAttribute getSlot_MinQuantity();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.Slot#getMaxQuantity <em>Max Quantity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Max Quantity</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getMaxQuantity()
	 * @see #getSlot()
	 * @generated
	 */
	EAttribute getSlot_MaxQuantity();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.Slot#isOptional <em>Optional</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Optional</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot#isOptional()
	 * @see #getSlot()
	 * @generated
	 */
	EAttribute getSlot_Optional();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.Slot#getPriceExpression <em>Price Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Price Expression</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getPriceExpression()
	 * @see #getSlot()
	 * @generated
	 */
	EAttribute getSlot_PriceExpression();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.cargo.Slot#getCargo <em>Cargo</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Cargo</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getCargo()
	 * @see #getSlot()
	 * @generated
	 */
	EReference getSlot_Cargo();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.Slot#getSlotOrPortDuration() <em>Get Slot Or Port Duration</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Slot Or Port Duration</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getSlotOrPortDuration()
	 * @generated
	 */
	EOperation getSlot__GetSlotOrPortDuration();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.Slot#getSlotOrContractMinQuantity() <em>Get Slot Or Contract Min Quantity</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Slot Or Contract Min Quantity</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getSlotOrContractMinQuantity()
	 * @generated
	 */
	EOperation getSlot__GetSlotOrContractMinQuantity();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.Slot#getSlotOrContractMaxQuantity() <em>Get Slot Or Contract Max Quantity</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Slot Or Contract Max Quantity</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getSlotOrContractMaxQuantity()
	 * @generated
	 */
	EOperation getSlot__GetSlotOrContractMaxQuantity();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.Slot#getWindowEndWithSlotOrPortTime() <em>Get Window End With Slot Or Port Time</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Window End With Slot Or Port Time</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getWindowEndWithSlotOrPortTime()
	 * @generated
	 */
	EOperation getSlot__GetWindowEndWithSlotOrPortTime();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.Slot#getWindowStartWithSlotOrPortTime() <em>Get Window Start With Slot Or Port Time</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Window Start With Slot Or Port Time</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getWindowStartWithSlotOrPortTime()
	 * @generated
	 */
	EOperation getSlot__GetWindowStartWithSlotOrPortTime();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.Slot#getSlotOrPortWindowSize() <em>Get Slot Or Port Window Size</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Slot Or Port Window Size</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getSlotOrPortWindowSize()
	 * @generated
	 */
	EOperation getSlot__GetSlotOrPortWindowSize();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.cargo.LoadSlot <em>Load Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Load Slot</em>'.
	 * @see com.mmxlabs.models.lng.cargo.LoadSlot
	 * @generated
	 */
	EClass getLoadSlot();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.LoadSlot#getCargoCV <em>Cargo CV</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Cargo CV</em>'.
	 * @see com.mmxlabs.models.lng.cargo.LoadSlot#getCargoCV()
	 * @see #getLoadSlot()
	 * @generated
	 */
	EAttribute getLoadSlot_CargoCV();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.LoadSlot#isArriveCold <em>Arrive Cold</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Arrive Cold</em>'.
	 * @see com.mmxlabs.models.lng.cargo.LoadSlot#isArriveCold()
	 * @see #getLoadSlot()
	 * @generated
	 */
	EAttribute getLoadSlot_ArriveCold();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.LoadSlot#isDESPurchase <em>DES Purchase</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>DES Purchase</em>'.
	 * @see com.mmxlabs.models.lng.cargo.LoadSlot#isDESPurchase()
	 * @see #getLoadSlot()
	 * @generated
	 */
	EAttribute getLoadSlot_DESPurchase();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.LoadSlot#getSlotOrPortCV() <em>Get Slot Or Port CV</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Slot Or Port CV</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.LoadSlot#getSlotOrPortCV()
	 * @generated
	 */
	EOperation getLoadSlot__GetSlotOrPortCV();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.cargo.DischargeSlot <em>Discharge Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Discharge Slot</em>'.
	 * @see com.mmxlabs.models.lng.cargo.DischargeSlot
	 * @generated
	 */
	EClass getDischargeSlot();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.DischargeSlot#isFOBSale <em>FOB Sale</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>FOB Sale</em>'.
	 * @see com.mmxlabs.models.lng.cargo.DischargeSlot#isFOBSale()
	 * @see #getDischargeSlot()
	 * @generated
	 */
	EAttribute getDischargeSlot_FOBSale();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.DischargeSlot#getPurchaseDeliveryType <em>Purchase Delivery Type</em>}'.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Purchase Delivery Type</em>'.
	 * @see com.mmxlabs.models.lng.cargo.DischargeSlot#getPurchaseDeliveryType()
	 * @see #getDischargeSlot()
	 * @generated
	 */
	EAttribute getDischargeSlot_PurchaseDeliveryType();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.cargo.CargoModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model</em>'.
	 * @see com.mmxlabs.models.lng.cargo.CargoModel
	 * @generated
	 */
	EClass getCargoModel();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.cargo.CargoModel#getLoadSlots <em>Load Slots</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Load Slots</em>'.
	 * @see com.mmxlabs.models.lng.cargo.CargoModel#getLoadSlots()
	 * @see #getCargoModel()
	 * @generated
	 */
	EReference getCargoModel_LoadSlots();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.cargo.CargoModel#getDischargeSlots <em>Discharge Slots</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Discharge Slots</em>'.
	 * @see com.mmxlabs.models.lng.cargo.CargoModel#getDischargeSlots()
	 * @see #getCargoModel()
	 * @generated
	 */
	EReference getCargoModel_DischargeSlots();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.cargo.CargoModel#getCargoes <em>Cargoes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Cargoes</em>'.
	 * @see com.mmxlabs.models.lng.cargo.CargoModel#getCargoes()
	 * @see #getCargoModel()
	 * @generated
	 */
	EReference getCargoModel_Cargoes();

	/**
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.cargo.CargoModel#getCargoGroups <em>Cargo Groups</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Cargo Groups</em>'.
	 * @see com.mmxlabs.models.lng.cargo.CargoModel#getCargoGroups()
	 * @see #getCargoModel()
	 * @generated
	 */
	EReference getCargoModel_CargoGroups();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.cargo.SpotSlot <em>Spot Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Spot Slot</em>'.
	 * @see com.mmxlabs.models.lng.cargo.SpotSlot
	 * @generated
	 */
	EClass getSpotSlot();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.cargo.SpotSlot#getMarket <em>Market</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Market</em>'.
	 * @see com.mmxlabs.models.lng.cargo.SpotSlot#getMarket()
	 * @see #getSpotSlot()
	 * @generated
	 */
	EReference getSpotSlot_Market();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.cargo.SpotLoadSlot <em>Spot Load Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Spot Load Slot</em>'.
	 * @see com.mmxlabs.models.lng.cargo.SpotLoadSlot
	 * @generated
	 */
	EClass getSpotLoadSlot();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.cargo.SpotDischargeSlot <em>Spot Discharge Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Spot Discharge Slot</em>'.
	 * @see com.mmxlabs.models.lng.cargo.SpotDischargeSlot
	 * @generated
	 */
	EClass getSpotDischargeSlot();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.cargo.CargoGroup <em>Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Group</em>'.
	 * @see com.mmxlabs.models.lng.cargo.CargoGroup
	 * @generated
	 */
	EClass getCargoGroup();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.cargo.CargoGroup#getCargoes <em>Cargoes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Cargoes</em>'.
	 * @see com.mmxlabs.models.lng.cargo.CargoGroup#getCargoes()
	 * @see #getCargoGroup()
	 * @generated
	 */
	EReference getCargoGroup_Cargoes();

	/**
	 * Returns the meta object for enum '{@link com.mmxlabs.models.lng.cargo.CargoType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Type</em>'.
	 * @see com.mmxlabs.models.lng.cargo.CargoType
	 * @generated
	 */
	EEnum getCargoType();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	CargoFactory getCargoFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.cargo.impl.CargoImpl <em>Cargo</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.cargo.impl.CargoImpl
		 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getCargo()
		 * @generated
		 */
		EClass CARGO = eINSTANCE.getCargo();

		/**
		 * The meta object literal for the '<em><b>Allow Rewiring</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CARGO__ALLOW_REWIRING = eINSTANCE.getCargo_AllowRewiring();

		/**
		 * The meta object literal for the '<em><b>Allowed Vessels</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CARGO__ALLOWED_VESSELS = eINSTANCE.getCargo_AllowedVessels();

		/**
		 * The meta object literal for the '<em><b>Slots</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * @since 3.0
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CARGO__SLOTS = eINSTANCE.getCargo_Slots();

		/**
		 * The meta object literal for the '<em><b>Get Cargo Type</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation CARGO___GET_CARGO_TYPE = eINSTANCE.getCargo__GetCargoType();

		/**
		 * The meta object literal for the '<em><b>Get Sorted Slots</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * @since 3.0
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation CARGO___GET_SORTED_SLOTS = eINSTANCE.getCargo__GetSortedSlots();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.cargo.impl.SlotImpl <em>Slot</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.cargo.impl.SlotImpl
		 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getSlot()
		 * @generated
		 */
		EClass SLOT = eINSTANCE.getSlot();

		/**
		 * The meta object literal for the '<em><b>Window Start</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT__WINDOW_START = eINSTANCE.getSlot_WindowStart();

		/**
		 * The meta object literal for the '<em><b>Window Start Time</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT__WINDOW_START_TIME = eINSTANCE.getSlot_WindowStartTime();

		/**
		 * The meta object literal for the '<em><b>Window Size</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT__WINDOW_SIZE = eINSTANCE.getSlot_WindowSize();

		/**
		 * The meta object literal for the '<em><b>Port</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SLOT__PORT = eINSTANCE.getSlot_Port();

		/**
		 * The meta object literal for the '<em><b>Contract</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SLOT__CONTRACT = eINSTANCE.getSlot_Contract();

		/**
		 * The meta object literal for the '<em><b>Duration</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT__DURATION = eINSTANCE.getSlot_Duration();

		/**
		 * The meta object literal for the '<em><b>Min Quantity</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT__MIN_QUANTITY = eINSTANCE.getSlot_MinQuantity();

		/**
		 * The meta object literal for the '<em><b>Max Quantity</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT__MAX_QUANTITY = eINSTANCE.getSlot_MaxQuantity();

		/**
		 * The meta object literal for the '<em><b>Optional</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT__OPTIONAL = eINSTANCE.getSlot_Optional();

		/**
		 * The meta object literal for the '<em><b>Price Expression</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * @since 2.0
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT__PRICE_EXPRESSION = eINSTANCE.getSlot_PriceExpression();

		/**
		 * The meta object literal for the '<em><b>Cargo</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * @since 3.0
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SLOT__CARGO = eINSTANCE.getSlot_Cargo();

		/**
		 * The meta object literal for the '<em><b>Get Slot Or Port Duration</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SLOT___GET_SLOT_OR_PORT_DURATION = eINSTANCE.getSlot__GetSlotOrPortDuration();

		/**
		 * The meta object literal for the '<em><b>Get Slot Or Contract Min Quantity</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SLOT___GET_SLOT_OR_CONTRACT_MIN_QUANTITY = eINSTANCE.getSlot__GetSlotOrContractMinQuantity();

		/**
		 * The meta object literal for the '<em><b>Get Slot Or Contract Max Quantity</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SLOT___GET_SLOT_OR_CONTRACT_MAX_QUANTITY = eINSTANCE.getSlot__GetSlotOrContractMaxQuantity();

		/**
		 * The meta object literal for the '<em><b>Get Window End With Slot Or Port Time</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SLOT___GET_WINDOW_END_WITH_SLOT_OR_PORT_TIME = eINSTANCE.getSlot__GetWindowEndWithSlotOrPortTime();

		/**
		 * The meta object literal for the '<em><b>Get Window Start With Slot Or Port Time</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SLOT___GET_WINDOW_START_WITH_SLOT_OR_PORT_TIME = eINSTANCE.getSlot__GetWindowStartWithSlotOrPortTime();

		/**
		 * The meta object literal for the '<em><b>Get Slot Or Port Window Size</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SLOT___GET_SLOT_OR_PORT_WINDOW_SIZE = eINSTANCE.getSlot__GetSlotOrPortWindowSize();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.cargo.impl.LoadSlotImpl <em>Load Slot</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.cargo.impl.LoadSlotImpl
		 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getLoadSlot()
		 * @generated
		 */
		EClass LOAD_SLOT = eINSTANCE.getLoadSlot();

		/**
		 * The meta object literal for the '<em><b>Cargo CV</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LOAD_SLOT__CARGO_CV = eINSTANCE.getLoadSlot_CargoCV();

		/**
		 * The meta object literal for the '<em><b>Arrive Cold</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LOAD_SLOT__ARRIVE_COLD = eINSTANCE.getLoadSlot_ArriveCold();

		/**
		 * The meta object literal for the '<em><b>DES Purchase</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LOAD_SLOT__DES_PURCHASE = eINSTANCE.getLoadSlot_DESPurchase();

		/**
		 * The meta object literal for the '<em><b>Get Slot Or Port CV</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation LOAD_SLOT___GET_SLOT_OR_PORT_CV = eINSTANCE.getLoadSlot__GetSlotOrPortCV();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.cargo.impl.DischargeSlotImpl <em>Discharge Slot</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.cargo.impl.DischargeSlotImpl
		 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getDischargeSlot()
		 * @generated
		 */
		EClass DISCHARGE_SLOT = eINSTANCE.getDischargeSlot();

		/**
		 * The meta object literal for the '<em><b>FOB Sale</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DISCHARGE_SLOT__FOB_SALE = eINSTANCE.getDischargeSlot_FOBSale();

		/**
		 * The meta object literal for the '<em><b>Purchase Delivery Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * @since 2.0
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DISCHARGE_SLOT__PURCHASE_DELIVERY_TYPE = eINSTANCE.getDischargeSlot_PurchaseDeliveryType();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.cargo.impl.CargoModelImpl <em>Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.cargo.impl.CargoModelImpl
		 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getCargoModel()
		 * @generated
		 */
		EClass CARGO_MODEL = eINSTANCE.getCargoModel();

		/**
		 * The meta object literal for the '<em><b>Load Slots</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CARGO_MODEL__LOAD_SLOTS = eINSTANCE.getCargoModel_LoadSlots();

		/**
		 * The meta object literal for the '<em><b>Discharge Slots</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CARGO_MODEL__DISCHARGE_SLOTS = eINSTANCE.getCargoModel_DischargeSlots();

		/**
		 * The meta object literal for the '<em><b>Cargoes</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CARGO_MODEL__CARGOES = eINSTANCE.getCargoModel_Cargoes();

		/**
		 * The meta object literal for the '<em><b>Cargo Groups</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CARGO_MODEL__CARGO_GROUPS = eINSTANCE.getCargoModel_CargoGroups();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.cargo.impl.SpotSlotImpl <em>Spot Slot</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.cargo.impl.SpotSlotImpl
		 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getSpotSlot()
		 * @generated
		 */
		EClass SPOT_SLOT = eINSTANCE.getSpotSlot();

		/**
		 * The meta object literal for the '<em><b>Market</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SPOT_SLOT__MARKET = eINSTANCE.getSpotSlot_Market();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.cargo.impl.SpotLoadSlotImpl <em>Spot Load Slot</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.cargo.impl.SpotLoadSlotImpl
		 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getSpotLoadSlot()
		 * @generated
		 */
		EClass SPOT_LOAD_SLOT = eINSTANCE.getSpotLoadSlot();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.cargo.impl.SpotDischargeSlotImpl <em>Spot Discharge Slot</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.cargo.impl.SpotDischargeSlotImpl
		 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getSpotDischargeSlot()
		 * @generated
		 */
		EClass SPOT_DISCHARGE_SLOT = eINSTANCE.getSpotDischargeSlot();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.cargo.impl.CargoGroupImpl <em>Group</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.cargo.impl.CargoGroupImpl
		 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getCargoGroup()
		 * @generated
		 */
		EClass CARGO_GROUP = eINSTANCE.getCargoGroup();

		/**
		 * The meta object literal for the '<em><b>Cargoes</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CARGO_GROUP__CARGOES = eINSTANCE.getCargoGroup_Cargoes();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.cargo.CargoType <em>Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.cargo.CargoType
		 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getCargoType()
		 * @generated
		 */
		EEnum CARGO_TYPE = eINSTANCE.getCargoType();

	}

} //CargoPackage
