/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo;

import com.mmxlabs.models.lng.types.TypesPackage;

import com.mmxlabs.models.mmxcore.MMXCorePackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
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
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO__PROXIES = TypesPackage.ACARGO__PROXIES;

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
	 * The feature id for the '<em><b>Load Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO__LOAD_SLOT = TypesPackage.ACARGO_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Discharge Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO__DISCHARGE_SLOT = TypesPackage.ACARGO_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Allow Rewiring</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO__ALLOW_REWIRING = TypesPackage.ACARGO_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Allowed Vessels</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO__ALLOWED_VESSELS = TypesPackage.ACARGO_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Cargo</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_FEATURE_COUNT = TypesPackage.ACARGO_FEATURE_COUNT + 4;

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
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__PROXIES = TypesPackage.ASLOT__PROXIES;

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
	 * The feature id for the '<em><b>Window Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__WINDOW_START = TypesPackage.ASLOT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Window Start Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__WINDOW_START_TIME = TypesPackage.ASLOT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Window Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__WINDOW_SIZE = TypesPackage.ASLOT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__PORT = TypesPackage.ASLOT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Contract</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__CONTRACT = TypesPackage.ASLOT_FEATURE_COUNT + 4;

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
	 * The feature id for the '<em><b>Fixed Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__FIXED_PRICE = TypesPackage.ASLOT_FEATURE_COUNT + 8;

	/**
	 * The number of structural features of the '<em>Slot</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_FEATURE_COUNT = TypesPackage.ASLOT_FEATURE_COUNT + 9;

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
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__PROXIES = SLOT__PROXIES;

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
	 * The feature id for the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__PORT = SLOT__PORT;

	/**
	 * The feature id for the '<em><b>Contract</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__CONTRACT = SLOT__CONTRACT;

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
	 * The feature id for the '<em><b>Fixed Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__FIXED_PRICE = SLOT__FIXED_PRICE;

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
	 * The number of structural features of the '<em>Load Slot</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT_FEATURE_COUNT = SLOT_FEATURE_COUNT + 2;

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
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__PROXIES = SLOT__PROXIES;

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
	 * The feature id for the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__PORT = SLOT__PORT;

	/**
	 * The feature id for the '<em><b>Contract</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__CONTRACT = SLOT__CONTRACT;

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
	 * The feature id for the '<em><b>Fixed Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT__FIXED_PRICE = SLOT__FIXED_PRICE;

	/**
	 * The number of structural features of the '<em>Discharge Slot</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_SLOT_FEATURE_COUNT = SLOT_FEATURE_COUNT + 0;

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
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_MODEL__PROXIES = MMXCorePackage.UUID_OBJECT__PROXIES;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_MODEL__UUID = MMXCorePackage.UUID_OBJECT__UUID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_MODEL__NAME = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Load Slots</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_MODEL__LOAD_SLOTS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Discharge Slots</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_MODEL__DISCHARGE_SLOTS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Cargos</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_MODEL__CARGOS = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_MODEL_FEATURE_COUNT = MMXCorePackage.UUID_OBJECT_FEATURE_COUNT + 4;

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
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_SLOT__PROXIES = MMXCorePackage.MMX_OBJECT__PROXIES;

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
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__PROXIES = LOAD_SLOT__PROXIES;

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
	 * The feature id for the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__PORT = LOAD_SLOT__PORT;

	/**
	 * The feature id for the '<em><b>Contract</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__CONTRACT = LOAD_SLOT__CONTRACT;

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
	 * The feature id for the '<em><b>Fixed Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_LOAD_SLOT__FIXED_PRICE = LOAD_SLOT__FIXED_PRICE;

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
	 * The feature id for the '<em><b>Proxies</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__PROXIES = DISCHARGE_SLOT__PROXIES;

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
	 * The feature id for the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__PORT = DISCHARGE_SLOT__PORT;

	/**
	 * The feature id for the '<em><b>Contract</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__CONTRACT = DISCHARGE_SLOT__CONTRACT;

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
	 * The feature id for the '<em><b>Fixed Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_DISCHARGE_SLOT__FIXED_PRICE = DISCHARGE_SLOT__FIXED_PRICE;

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
	 * The meta object id for the '{@link com.mmxlabs.models.lng.cargo.CargoType <em>Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.CargoType
	 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getCargoType()
	 * @generated
	 */
	int CARGO_TYPE = 8;


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
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.cargo.Cargo#getLoadSlot <em>Load Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Load Slot</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Cargo#getLoadSlot()
	 * @see #getCargo()
	 * @generated
	 */
	EReference getCargo_LoadSlot();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.cargo.Cargo#getDischargeSlot <em>Discharge Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Discharge Slot</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Cargo#getDischargeSlot()
	 * @see #getCargo()
	 * @generated
	 */
	EReference getCargo_DischargeSlot();

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
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.cargo.Slot <em>Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Slot</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot
	 * @generated
	 */
	EClass getSlot();

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
	EReference getSlot_Contract();

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
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.Slot#getFixedPrice <em>Fixed Price</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Fixed Price</em>'.
	 * @see com.mmxlabs.models.lng.cargo.Slot#getFixedPrice()
	 * @see #getSlot()
	 * @generated
	 */
	EAttribute getSlot_FixedPrice();

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
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.cargo.DischargeSlot <em>Discharge Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Discharge Slot</em>'.
	 * @see com.mmxlabs.models.lng.cargo.DischargeSlot
	 * @generated
	 */
	EClass getDischargeSlot();

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
	 * Returns the meta object for the containment reference list '{@link com.mmxlabs.models.lng.cargo.CargoModel#getCargos <em>Cargos</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Cargos</em>'.
	 * @see com.mmxlabs.models.lng.cargo.CargoModel#getCargos()
	 * @see #getCargoModel()
	 * @generated
	 */
	EReference getCargoModel_Cargos();

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
		 * The meta object literal for the '<em><b>Load Slot</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CARGO__LOAD_SLOT = eINSTANCE.getCargo_LoadSlot();

		/**
		 * The meta object literal for the '<em><b>Discharge Slot</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CARGO__DISCHARGE_SLOT = eINSTANCE.getCargo_DischargeSlot();

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
		 * The meta object literal for the '<em><b>Fixed Price</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT__FIXED_PRICE = eINSTANCE.getSlot_FixedPrice();

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
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.cargo.impl.DischargeSlotImpl <em>Discharge Slot</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.cargo.impl.DischargeSlotImpl
		 * @see com.mmxlabs.models.lng.cargo.impl.CargoPackageImpl#getDischargeSlot()
		 * @generated
		 */
		EClass DISCHARGE_SLOT = eINSTANCE.getDischargeSlot();

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
		 * The meta object literal for the '<em><b>Cargos</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CARGO_MODEL__CARGOS = eINSTANCE.getCargoModel_Cargos();

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
