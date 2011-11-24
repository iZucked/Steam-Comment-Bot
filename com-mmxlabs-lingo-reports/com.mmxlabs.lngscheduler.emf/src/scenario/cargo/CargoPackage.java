/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.cargo;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import scenario.ScenarioPackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see scenario.cargo.CargoFactory
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
	String eNS_URI = "http://com.mmxlabs.lng.emf2/cargo";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "com.mmxlabs.lng.emf.cargo";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	CargoPackage eINSTANCE = scenario.cargo.impl.CargoPackageImpl.init();

	/**
	 * The meta object id for the '{@link scenario.cargo.impl.CargoModelImpl <em>Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.cargo.impl.CargoModelImpl
	 * @see scenario.cargo.impl.CargoPackageImpl#getCargoModel()
	 * @generated
	 */
	int CARGO_MODEL = 0;

	/**
	 * The feature id for the '<em><b>Cargoes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_MODEL__CARGOES = 0;

	/**
	 * The feature id for the '<em><b>Spare Discharge Slots</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_MODEL__SPARE_DISCHARGE_SLOTS = 1;

	/**
	 * The feature id for the '<em><b>Spare Load Slots</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_MODEL__SPARE_LOAD_SLOTS = 2;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_MODEL_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_MODEL_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link scenario.cargo.impl.CargoImpl <em>Cargo</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.cargo.impl.CargoImpl
	 * @see scenario.cargo.impl.CargoPackageImpl#getCargo()
	 * @generated
	 */
	int CARGO = 1;

	/**
	 * The feature id for the '<em><b>Notes</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO__NOTES = ScenarioPackage.ANNOTATED_OBJECT__NOTES;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO__ID = ScenarioPackage.ANNOTATED_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Allowed Vessels</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO__ALLOWED_VESSELS = ScenarioPackage.ANNOTATED_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Load Slot</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO__LOAD_SLOT = ScenarioPackage.ANNOTATED_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Discharge Slot</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO__DISCHARGE_SLOT = ScenarioPackage.ANNOTATED_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Cargo Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO__CARGO_TYPE = ScenarioPackage.ANNOTATED_OBJECT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Allow Rewiring</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO__ALLOW_REWIRING = ScenarioPackage.ANNOTATED_OBJECT_FEATURE_COUNT + 5;

	/**
	 * The number of structural features of the '<em>Cargo</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_FEATURE_COUNT = ScenarioPackage.ANNOTATED_OBJECT_FEATURE_COUNT + 6;

	/**
	 * The number of operations of the '<em>Cargo</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_OPERATION_COUNT = ScenarioPackage.ANNOTATED_OBJECT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link scenario.cargo.impl.SlotImpl <em>Slot</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.cargo.impl.SlotImpl
	 * @see scenario.cargo.impl.CargoPackageImpl#getSlot()
	 * @generated
	 */
	int SLOT = 2;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__ID = 0;

	/**
	 * The feature id for the '<em><b>Min Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__MIN_QUANTITY = 1;

	/**
	 * The feature id for the '<em><b>Max Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__MAX_QUANTITY = 2;

	/**
	 * The feature id for the '<em><b>Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__PORT = 3;

	/**
	 * The feature id for the '<em><b>Window Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__WINDOW_START = 4;

	/**
	 * The feature id for the '<em><b>Window Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__WINDOW_DURATION = 5;

	/**
	 * The feature id for the '<em><b>Slot Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__SLOT_DURATION = 6;

	/**
	 * The feature id for the '<em><b>Fixed Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__FIXED_PRICE = 7;

	/**
	 * The feature id for the '<em><b>Contract</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT__CONTRACT = 8;

	/**
	 * The number of structural features of the '<em>Slot</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_FEATURE_COUNT = 9;

	/**
	 * The operation id for the '<em>Get Local Window Start</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT___GET_LOCAL_WINDOW_START = 0;

	/**
	 * The operation id for the '<em>Get Window End</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT___GET_WINDOW_END = 1;

	/**
	 * The operation id for the '<em>Get Slot Or Port Contract</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT___GET_SLOT_OR_PORT_CONTRACT__OBJECT = 2;

	/**
	 * The operation id for the '<em>Get Slot Or Port Duration</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT___GET_SLOT_OR_PORT_DURATION = 3;

	/**
	 * The operation id for the '<em>Get Slot Or Contract Min Quantity</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT___GET_SLOT_OR_CONTRACT_MIN_QUANTITY__OBJECT = 4;

	/**
	 * The operation id for the '<em>Get Slot Or Contract Max Quantity</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT___GET_SLOT_OR_CONTRACT_MAX_QUANTITY__OBJECT = 5;

	/**
	 * The number of operations of the '<em>Slot</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_OPERATION_COUNT = 6;

	/**
	 * The meta object id for the '{@link scenario.cargo.impl.LoadSlotImpl <em>Load Slot</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.cargo.impl.LoadSlotImpl
	 * @see scenario.cargo.impl.CargoPackageImpl#getLoadSlot()
	 * @generated
	 */
	int LOAD_SLOT = 3;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__ID = SLOT__ID;

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
	 * The feature id for the '<em><b>Window Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__WINDOW_DURATION = SLOT__WINDOW_DURATION;

	/**
	 * The feature id for the '<em><b>Slot Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__SLOT_DURATION = SLOT__SLOT_DURATION;

	/**
	 * The feature id for the '<em><b>Fixed Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__FIXED_PRICE = SLOT__FIXED_PRICE;

	/**
	 * The feature id for the '<em><b>Contract</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__CONTRACT = SLOT__CONTRACT;

	/**
	 * The feature id for the '<em><b>Cargo CVvalue</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT__CARGO_CVVALUE = SLOT_FEATURE_COUNT + 0;

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
	 * The operation id for the '<em>Get Local Window Start</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___GET_LOCAL_WINDOW_START = SLOT___GET_LOCAL_WINDOW_START;

	/**
	 * The operation id for the '<em>Get Window End</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___GET_WINDOW_END = SLOT___GET_WINDOW_END;

	/**
	 * The operation id for the '<em>Get Slot Or Port Contract</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___GET_SLOT_OR_PORT_CONTRACT__OBJECT = SLOT___GET_SLOT_OR_PORT_CONTRACT__OBJECT;

	/**
	 * The operation id for the '<em>Get Slot Or Contract Min Quantity</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___GET_SLOT_OR_CONTRACT_MIN_QUANTITY__OBJECT = SLOT___GET_SLOT_OR_CONTRACT_MIN_QUANTITY__OBJECT;

	/**
	 * The operation id for the '<em>Get Slot Or Contract Max Quantity</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___GET_SLOT_OR_CONTRACT_MAX_QUANTITY__OBJECT = SLOT___GET_SLOT_OR_CONTRACT_MAX_QUANTITY__OBJECT;

	/**
	 * The operation id for the '<em>Get Cargo Or Port CV Value</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___GET_CARGO_OR_PORT_CV_VALUE = SLOT_OPERATION_COUNT + 0;

	/**
	 * The operation id for the '<em>Get Slot Or Port Duration</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT___GET_SLOT_OR_PORT_DURATION = SLOT_OPERATION_COUNT + 1;

	/**
	 * The number of operations of the '<em>Load Slot</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_SLOT_OPERATION_COUNT = SLOT_OPERATION_COUNT + 2;

	/**
	 * The meta object id for the '{@link scenario.cargo.CargoType <em>Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.cargo.CargoType
	 * @see scenario.cargo.impl.CargoPackageImpl#getCargoType()
	 * @generated
	 */
	int CARGO_TYPE = 4;


	/**
	 * Returns the meta object for class '{@link scenario.cargo.CargoModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model</em>'.
	 * @see scenario.cargo.CargoModel
	 * @generated
	 */
	EClass getCargoModel();

	/**
	 * Returns the meta object for the containment reference list '{@link scenario.cargo.CargoModel#getCargoes <em>Cargoes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Cargoes</em>'.
	 * @see scenario.cargo.CargoModel#getCargoes()
	 * @see #getCargoModel()
	 * @generated
	 */
	EReference getCargoModel_Cargoes();

	/**
	 * Returns the meta object for the containment reference list '{@link scenario.cargo.CargoModel#getSpareDischargeSlots <em>Spare Discharge Slots</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Spare Discharge Slots</em>'.
	 * @see scenario.cargo.CargoModel#getSpareDischargeSlots()
	 * @see #getCargoModel()
	 * @generated
	 */
	EReference getCargoModel_SpareDischargeSlots();

	/**
	 * Returns the meta object for the containment reference list '{@link scenario.cargo.CargoModel#getSpareLoadSlots <em>Spare Load Slots</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Spare Load Slots</em>'.
	 * @see scenario.cargo.CargoModel#getSpareLoadSlots()
	 * @see #getCargoModel()
	 * @generated
	 */
	EReference getCargoModel_SpareLoadSlots();

	/**
	 * Returns the meta object for class '{@link scenario.cargo.Cargo <em>Cargo</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Cargo</em>'.
	 * @see scenario.cargo.Cargo
	 * @generated
	 */
	EClass getCargo();

	/**
	 * Returns the meta object for the attribute '{@link scenario.cargo.Cargo#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see scenario.cargo.Cargo#getId()
	 * @see #getCargo()
	 * @generated
	 */
	EAttribute getCargo_Id();

	/**
	 * Returns the meta object for the containment reference '{@link scenario.cargo.Cargo#getLoadSlot <em>Load Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Load Slot</em>'.
	 * @see scenario.cargo.Cargo#getLoadSlot()
	 * @see #getCargo()
	 * @generated
	 */
	EReference getCargo_LoadSlot();

	/**
	 * Returns the meta object for the containment reference '{@link scenario.cargo.Cargo#getDischargeSlot <em>Discharge Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Discharge Slot</em>'.
	 * @see scenario.cargo.Cargo#getDischargeSlot()
	 * @see #getCargo()
	 * @generated
	 */
	EReference getCargo_DischargeSlot();

	/**
	 * Returns the meta object for the reference list '{@link scenario.cargo.Cargo#getAllowedVessels <em>Allowed Vessels</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Allowed Vessels</em>'.
	 * @see scenario.cargo.Cargo#getAllowedVessels()
	 * @see #getCargo()
	 * @generated
	 */
	EReference getCargo_AllowedVessels();

	/**
	 * Returns the meta object for the attribute '{@link scenario.cargo.Cargo#getCargoType <em>Cargo Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Cargo Type</em>'.
	 * @see scenario.cargo.Cargo#getCargoType()
	 * @see #getCargo()
	 * @generated
	 */
	EAttribute getCargo_CargoType();

	/**
	 * Returns the meta object for the attribute '{@link scenario.cargo.Cargo#isAllowRewiring <em>Allow Rewiring</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Allow Rewiring</em>'.
	 * @see scenario.cargo.Cargo#isAllowRewiring()
	 * @see #getCargo()
	 * @generated
	 */
	EAttribute getCargo_AllowRewiring();

	/**
	 * Returns the meta object for class '{@link scenario.cargo.Slot <em>Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Slot</em>'.
	 * @see scenario.cargo.Slot
	 * @generated
	 */
	EClass getSlot();

	/**
	 * Returns the meta object for the attribute '{@link scenario.cargo.Slot#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see scenario.cargo.Slot#getId()
	 * @see #getSlot()
	 * @generated
	 */
	EAttribute getSlot_Id();

	/**
	 * Returns the meta object for the attribute '{@link scenario.cargo.Slot#getMinQuantity <em>Min Quantity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Min Quantity</em>'.
	 * @see scenario.cargo.Slot#getMinQuantity()
	 * @see #getSlot()
	 * @generated
	 */
	EAttribute getSlot_MinQuantity();

	/**
	 * Returns the meta object for the attribute '{@link scenario.cargo.Slot#getMaxQuantity <em>Max Quantity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Max Quantity</em>'.
	 * @see scenario.cargo.Slot#getMaxQuantity()
	 * @see #getSlot()
	 * @generated
	 */
	EAttribute getSlot_MaxQuantity();

	/**
	 * Returns the meta object for the reference '{@link scenario.cargo.Slot#getPort <em>Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Port</em>'.
	 * @see scenario.cargo.Slot#getPort()
	 * @see #getSlot()
	 * @generated
	 */
	EReference getSlot_Port();

	/**
	 * Returns the meta object for the attribute '{@link scenario.cargo.Slot#getWindowStart <em>Window Start</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Window Start</em>'.
	 * @see scenario.cargo.Slot#getWindowStart()
	 * @see #getSlot()
	 * @generated
	 */
	EAttribute getSlot_WindowStart();

	/**
	 * Returns the meta object for the attribute '{@link scenario.cargo.Slot#getWindowDuration <em>Window Duration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Window Duration</em>'.
	 * @see scenario.cargo.Slot#getWindowDuration()
	 * @see #getSlot()
	 * @generated
	 */
	EAttribute getSlot_WindowDuration();

	/**
	 * Returns the meta object for the attribute '{@link scenario.cargo.Slot#getSlotDuration <em>Slot Duration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Slot Duration</em>'.
	 * @see scenario.cargo.Slot#getSlotDuration()
	 * @see #getSlot()
	 * @generated
	 */
	EAttribute getSlot_SlotDuration();

	/**
	 * Returns the meta object for the reference '{@link scenario.cargo.Slot#getContract <em>Contract</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Contract</em>'.
	 * @see scenario.cargo.Slot#getContract()
	 * @see #getSlot()
	 * @generated
	 */
	EReference getSlot_Contract();

	/**
	 * Returns the meta object for the attribute '{@link scenario.cargo.Slot#getFixedPrice <em>Fixed Price</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Fixed Price</em>'.
	 * @see scenario.cargo.Slot#getFixedPrice()
	 * @see #getSlot()
	 * @generated
	 */
	EAttribute getSlot_FixedPrice();

	/**
	 * Returns the meta object for the '{@link scenario.cargo.Slot#getLocalWindowStart() <em>Get Local Window Start</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Local Window Start</em>' operation.
	 * @see scenario.cargo.Slot#getLocalWindowStart()
	 * @generated
	 */
	EOperation getSlot__GetLocalWindowStart();

	/**
	 * Returns the meta object for the '{@link scenario.cargo.Slot#getWindowEnd() <em>Get Window End</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Window End</em>' operation.
	 * @see scenario.cargo.Slot#getWindowEnd()
	 * @generated
	 */
	EOperation getSlot__GetWindowEnd();

	/**
	 * Returns the meta object for the '{@link scenario.cargo.Slot#getSlotOrPortContract(java.lang.Object) <em>Get Slot Or Port Contract</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Slot Or Port Contract</em>' operation.
	 * @see scenario.cargo.Slot#getSlotOrPortContract(java.lang.Object)
	 * @generated
	 */
	EOperation getSlot__GetSlotOrPortContract__Object();

	/**
	 * Returns the meta object for the '{@link scenario.cargo.Slot#getSlotOrPortDuration() <em>Get Slot Or Port Duration</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Slot Or Port Duration</em>' operation.
	 * @see scenario.cargo.Slot#getSlotOrPortDuration()
	 * @generated
	 */
	EOperation getSlot__GetSlotOrPortDuration();

	/**
	 * Returns the meta object for the '{@link scenario.cargo.Slot#getSlotOrContractMinQuantity(java.lang.Object) <em>Get Slot Or Contract Min Quantity</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Slot Or Contract Min Quantity</em>' operation.
	 * @see scenario.cargo.Slot#getSlotOrContractMinQuantity(java.lang.Object)
	 * @generated
	 */
	EOperation getSlot__GetSlotOrContractMinQuantity__Object();

	/**
	 * Returns the meta object for the '{@link scenario.cargo.Slot#getSlotOrContractMaxQuantity(java.lang.Object) <em>Get Slot Or Contract Max Quantity</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Slot Or Contract Max Quantity</em>' operation.
	 * @see scenario.cargo.Slot#getSlotOrContractMaxQuantity(java.lang.Object)
	 * @generated
	 */
	EOperation getSlot__GetSlotOrContractMaxQuantity__Object();

	/**
	 * Returns the meta object for class '{@link scenario.cargo.LoadSlot <em>Load Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Load Slot</em>'.
	 * @see scenario.cargo.LoadSlot
	 * @generated
	 */
	EClass getLoadSlot();

	/**
	 * Returns the meta object for the attribute '{@link scenario.cargo.LoadSlot#getCargoCVvalue <em>Cargo CVvalue</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Cargo CVvalue</em>'.
	 * @see scenario.cargo.LoadSlot#getCargoCVvalue()
	 * @see #getLoadSlot()
	 * @generated
	 */
	EAttribute getLoadSlot_CargoCVvalue();

	/**
	 * Returns the meta object for the attribute '{@link scenario.cargo.LoadSlot#isArriveCold <em>Arrive Cold</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Arrive Cold</em>'.
	 * @see scenario.cargo.LoadSlot#isArriveCold()
	 * @see #getLoadSlot()
	 * @generated
	 */
	EAttribute getLoadSlot_ArriveCold();

	/**
	 * Returns the meta object for the '{@link scenario.cargo.LoadSlot#getCargoOrPortCVValue() <em>Get Cargo Or Port CV Value</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Cargo Or Port CV Value</em>' operation.
	 * @see scenario.cargo.LoadSlot#getCargoOrPortCVValue()
	 * @generated
	 */
	EOperation getLoadSlot__GetCargoOrPortCVValue();

	/**
	 * Returns the meta object for the '{@link scenario.cargo.LoadSlot#getSlotOrPortDuration() <em>Get Slot Or Port Duration</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Slot Or Port Duration</em>' operation.
	 * @see scenario.cargo.LoadSlot#getSlotOrPortDuration()
	 * @generated
	 */
	EOperation getLoadSlot__GetSlotOrPortDuration();

	/**
	 * Returns the meta object for enum '{@link scenario.cargo.CargoType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Type</em>'.
	 * @see scenario.cargo.CargoType
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
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link scenario.cargo.impl.CargoModelImpl <em>Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.cargo.impl.CargoModelImpl
		 * @see scenario.cargo.impl.CargoPackageImpl#getCargoModel()
		 * @generated
		 */
		EClass CARGO_MODEL = eINSTANCE.getCargoModel();

		/**
		 * The meta object literal for the '<em><b>Cargoes</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CARGO_MODEL__CARGOES = eINSTANCE.getCargoModel_Cargoes();

		/**
		 * The meta object literal for the '<em><b>Spare Discharge Slots</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CARGO_MODEL__SPARE_DISCHARGE_SLOTS = eINSTANCE.getCargoModel_SpareDischargeSlots();

		/**
		 * The meta object literal for the '<em><b>Spare Load Slots</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CARGO_MODEL__SPARE_LOAD_SLOTS = eINSTANCE.getCargoModel_SpareLoadSlots();

		/**
		 * The meta object literal for the '{@link scenario.cargo.impl.CargoImpl <em>Cargo</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.cargo.impl.CargoImpl
		 * @see scenario.cargo.impl.CargoPackageImpl#getCargo()
		 * @generated
		 */
		EClass CARGO = eINSTANCE.getCargo();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CARGO__ID = eINSTANCE.getCargo_Id();

		/**
		 * The meta object literal for the '<em><b>Load Slot</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CARGO__LOAD_SLOT = eINSTANCE.getCargo_LoadSlot();

		/**
		 * The meta object literal for the '<em><b>Discharge Slot</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CARGO__DISCHARGE_SLOT = eINSTANCE.getCargo_DischargeSlot();

		/**
		 * The meta object literal for the '<em><b>Allowed Vessels</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CARGO__ALLOWED_VESSELS = eINSTANCE.getCargo_AllowedVessels();

		/**
		 * The meta object literal for the '<em><b>Cargo Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CARGO__CARGO_TYPE = eINSTANCE.getCargo_CargoType();

		/**
		 * The meta object literal for the '<em><b>Allow Rewiring</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CARGO__ALLOW_REWIRING = eINSTANCE.getCargo_AllowRewiring();

		/**
		 * The meta object literal for the '{@link scenario.cargo.impl.SlotImpl <em>Slot</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.cargo.impl.SlotImpl
		 * @see scenario.cargo.impl.CargoPackageImpl#getSlot()
		 * @generated
		 */
		EClass SLOT = eINSTANCE.getSlot();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT__ID = eINSTANCE.getSlot_Id();

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
		 * The meta object literal for the '<em><b>Port</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SLOT__PORT = eINSTANCE.getSlot_Port();

		/**
		 * The meta object literal for the '<em><b>Window Start</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT__WINDOW_START = eINSTANCE.getSlot_WindowStart();

		/**
		 * The meta object literal for the '<em><b>Window Duration</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT__WINDOW_DURATION = eINSTANCE.getSlot_WindowDuration();

		/**
		 * The meta object literal for the '<em><b>Slot Duration</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT__SLOT_DURATION = eINSTANCE.getSlot_SlotDuration();

		/**
		 * The meta object literal for the '<em><b>Contract</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SLOT__CONTRACT = eINSTANCE.getSlot_Contract();

		/**
		 * The meta object literal for the '<em><b>Fixed Price</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT__FIXED_PRICE = eINSTANCE.getSlot_FixedPrice();

		/**
		 * The meta object literal for the '<em><b>Get Local Window Start</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SLOT___GET_LOCAL_WINDOW_START = eINSTANCE.getSlot__GetLocalWindowStart();

		/**
		 * The meta object literal for the '<em><b>Get Window End</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SLOT___GET_WINDOW_END = eINSTANCE.getSlot__GetWindowEnd();

		/**
		 * The meta object literal for the '<em><b>Get Slot Or Port Contract</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SLOT___GET_SLOT_OR_PORT_CONTRACT__OBJECT = eINSTANCE.getSlot__GetSlotOrPortContract__Object();

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
		EOperation SLOT___GET_SLOT_OR_CONTRACT_MIN_QUANTITY__OBJECT = eINSTANCE.getSlot__GetSlotOrContractMinQuantity__Object();

		/**
		 * The meta object literal for the '<em><b>Get Slot Or Contract Max Quantity</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SLOT___GET_SLOT_OR_CONTRACT_MAX_QUANTITY__OBJECT = eINSTANCE.getSlot__GetSlotOrContractMaxQuantity__Object();

		/**
		 * The meta object literal for the '{@link scenario.cargo.impl.LoadSlotImpl <em>Load Slot</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.cargo.impl.LoadSlotImpl
		 * @see scenario.cargo.impl.CargoPackageImpl#getLoadSlot()
		 * @generated
		 */
		EClass LOAD_SLOT = eINSTANCE.getLoadSlot();

		/**
		 * The meta object literal for the '<em><b>Cargo CVvalue</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LOAD_SLOT__CARGO_CVVALUE = eINSTANCE.getLoadSlot_CargoCVvalue();

		/**
		 * The meta object literal for the '<em><b>Arrive Cold</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LOAD_SLOT__ARRIVE_COLD = eINSTANCE.getLoadSlot_ArriveCold();

		/**
		 * The meta object literal for the '<em><b>Get Cargo Or Port CV Value</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation LOAD_SLOT___GET_CARGO_OR_PORT_CV_VALUE = eINSTANCE.getLoadSlot__GetCargoOrPortCVValue();

		/**
		 * The meta object literal for the '<em><b>Get Slot Or Port Duration</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation LOAD_SLOT___GET_SLOT_OR_PORT_DURATION = eINSTANCE.getLoadSlot__GetSlotOrPortDuration();

		/**
		 * The meta object literal for the '{@link scenario.cargo.CargoType <em>Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.cargo.CargoType
		 * @see scenario.cargo.impl.CargoPackageImpl#getCargoType()
		 * @generated
		 */
		EEnum CARGO_TYPE = eINSTANCE.getCargoType();

	}

} //CargoPackage
