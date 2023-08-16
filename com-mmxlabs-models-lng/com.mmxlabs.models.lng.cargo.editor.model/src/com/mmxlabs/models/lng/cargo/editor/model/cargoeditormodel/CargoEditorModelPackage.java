/**
 */
package com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
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
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.CargoEditorModelFactory
 * @model kind="package"
 * @generated
 */
public interface CargoEditorModelPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "cargoeditormodel";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.mmxlabs.com/models/lng/cargoeditormodel/1/";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "lng.cargoeditormodel";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	CargoEditorModelPackage eINSTANCE = com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.impl.CargoEditorModelPackageImpl.init();

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.impl.TradesRowImpl <em>Trades Row</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.impl.TradesRowImpl
	 * @see com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.impl.CargoEditorModelPackageImpl#getTradesRow()
	 * @generated
	 */
	int TRADES_ROW = 0;

	/**
	 * The feature id for the '<em><b>Load Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRADES_ROW__LOAD_SLOT = 0;

	/**
	 * The feature id for the '<em><b>Discharge Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRADES_ROW__DISCHARGE_SLOT = 1;

	/**
	 * The feature id for the '<em><b>Cargo</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRADES_ROW__CARGO = 2;

	/**
	 * The feature id for the '<em><b>Cargo Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRADES_ROW__CARGO_ALLOCATION = 3;

	/**
	 * The feature id for the '<em><b>Load Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRADES_ROW__LOAD_ALLOCATION = 4;

	/**
	 * The feature id for the '<em><b>Discharge Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRADES_ROW__DISCHARGE_ALLOCATION = 5;

	/**
	 * The feature id for the '<em><b>Open Slot Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRADES_ROW__OPEN_SLOT_ALLOCATION = 6;

	/**
	 * The feature id for the '<em><b>Input Equivalents</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRADES_ROW__INPUT_EQUIVALENTS = 7;

	/**
	 * The feature id for the '<em><b>Load Slot Contract Params</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRADES_ROW__LOAD_SLOT_CONTRACT_PARAMS = 8;

	/**
	 * The feature id for the '<em><b>Primary Record</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRADES_ROW__PRIMARY_RECORD = 9;

	/**
	 * The feature id for the '<em><b>Discharge Slot Contract Params</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRADES_ROW__DISCHARGE_SLOT_CONTRACT_PARAMS = 10;

	/**
	 * The feature id for the '<em><b>Market Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRADES_ROW__MARKET_ALLOCATION = 11;

	/**
	 * The feature id for the '<em><b>Load Terminal Valid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRADES_ROW__LOAD_TERMINAL_VALID = 12;

	/**
	 * The feature id for the '<em><b>Discharge Terminal Valid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRADES_ROW__DISCHARGE_TERMINAL_VALID = 13;

	/**
	 * The feature id for the '<em><b>Group</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRADES_ROW__GROUP = 14;

	/**
	 * The number of structural features of the '<em>Trades Row</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRADES_ROW_FEATURE_COUNT = 15;

	/**
	 * The operation id for the '<em>Get Assignable Object</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRADES_ROW___GET_ASSIGNABLE_OBJECT = 0;

	/**
	 * The number of operations of the '<em>Trades Row</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRADES_ROW_OPERATION_COUNT = 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.impl.TradesTableRootImpl <em>Trades Table Root</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.impl.TradesTableRootImpl
	 * @see com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.impl.CargoEditorModelPackageImpl#getTradesTableRoot()
	 * @generated
	 */
	int TRADES_TABLE_ROOT = 1;

	/**
	 * The feature id for the '<em><b>Trades Rows</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRADES_TABLE_ROOT__TRADES_ROWS = 0;

	/**
	 * The number of structural features of the '<em>Trades Table Root</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRADES_TABLE_ROOT_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Trades Table Root</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRADES_TABLE_ROOT_OPERATION_COUNT = 0;


	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow <em>Trades Row</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Trades Row</em>'.
	 * @see com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow
	 * @generated
	 */
	EClass getTradesRow();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow#getLoadSlot <em>Load Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Load Slot</em>'.
	 * @see com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow#getLoadSlot()
	 * @see #getTradesRow()
	 * @generated
	 */
	EReference getTradesRow_LoadSlot();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow#getDischargeSlot <em>Discharge Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Discharge Slot</em>'.
	 * @see com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow#getDischargeSlot()
	 * @see #getTradesRow()
	 * @generated
	 */
	EReference getTradesRow_DischargeSlot();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow#getCargo <em>Cargo</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Cargo</em>'.
	 * @see com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow#getCargo()
	 * @see #getTradesRow()
	 * @generated
	 */
	EReference getTradesRow_Cargo();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow#getCargoAllocation <em>Cargo Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Cargo Allocation</em>'.
	 * @see com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow#getCargoAllocation()
	 * @see #getTradesRow()
	 * @generated
	 */
	EReference getTradesRow_CargoAllocation();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow#getLoadAllocation <em>Load Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Load Allocation</em>'.
	 * @see com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow#getLoadAllocation()
	 * @see #getTradesRow()
	 * @generated
	 */
	EReference getTradesRow_LoadAllocation();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow#getDischargeAllocation <em>Discharge Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Discharge Allocation</em>'.
	 * @see com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow#getDischargeAllocation()
	 * @see #getTradesRow()
	 * @generated
	 */
	EReference getTradesRow_DischargeAllocation();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow#getOpenSlotAllocation <em>Open Slot Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Open Slot Allocation</em>'.
	 * @see com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow#getOpenSlotAllocation()
	 * @see #getTradesRow()
	 * @generated
	 */
	EReference getTradesRow_OpenSlotAllocation();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow#getInputEquivalents <em>Input Equivalents</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Input Equivalents</em>'.
	 * @see com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow#getInputEquivalents()
	 * @see #getTradesRow()
	 * @generated
	 */
	EReference getTradesRow_InputEquivalents();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow#getLoadSlotContractParams <em>Load Slot Contract Params</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Load Slot Contract Params</em>'.
	 * @see com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow#getLoadSlotContractParams()
	 * @see #getTradesRow()
	 * @generated
	 */
	EReference getTradesRow_LoadSlotContractParams();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow#isPrimaryRecord <em>Primary Record</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Primary Record</em>'.
	 * @see com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow#isPrimaryRecord()
	 * @see #getTradesRow()
	 * @generated
	 */
	EAttribute getTradesRow_PrimaryRecord();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow#getDischargeSlotContractParams <em>Discharge Slot Contract Params</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Discharge Slot Contract Params</em>'.
	 * @see com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow#getDischargeSlotContractParams()
	 * @see #getTradesRow()
	 * @generated
	 */
	EReference getTradesRow_DischargeSlotContractParams();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow#getMarketAllocation <em>Market Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Market Allocation</em>'.
	 * @see com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow#getMarketAllocation()
	 * @see #getTradesRow()
	 * @generated
	 */
	EReference getTradesRow_MarketAllocation();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow#isLoadTerminalValid <em>Load Terminal Valid</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Load Terminal Valid</em>'.
	 * @see com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow#isLoadTerminalValid()
	 * @see #getTradesRow()
	 * @generated
	 */
	EAttribute getTradesRow_LoadTerminalValid();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow#isDischargeTerminalValid <em>Discharge Terminal Valid</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Discharge Terminal Valid</em>'.
	 * @see com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow#isDischargeTerminalValid()
	 * @see #getTradesRow()
	 * @generated
	 */
	EAttribute getTradesRow_DischargeTerminalValid();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow#getGroup <em>Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Group</em>'.
	 * @see com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow#getGroup()
	 * @see #getTradesRow()
	 * @generated
	 */
	EAttribute getTradesRow_Group();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow#getAssignableObject() <em>Get Assignable Object</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Assignable Object</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow#getAssignableObject()
	 * @generated
	 */
	EOperation getTradesRow__GetAssignableObject();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesTableRoot <em>Trades Table Root</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Trades Table Root</em>'.
	 * @see com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesTableRoot
	 * @generated
	 */
	EClass getTradesTableRoot();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesTableRoot#getTradesRows <em>Trades Rows</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Trades Rows</em>'.
	 * @see com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesTableRoot#getTradesRows()
	 * @see #getTradesTableRoot()
	 * @generated
	 */
	EReference getTradesTableRoot_TradesRows();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	CargoEditorModelFactory getCargoEditorModelFactory();

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
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.impl.TradesRowImpl <em>Trades Row</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.impl.TradesRowImpl
		 * @see com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.impl.CargoEditorModelPackageImpl#getTradesRow()
		 * @generated
		 */
		EClass TRADES_ROW = eINSTANCE.getTradesRow();

		/**
		 * The meta object literal for the '<em><b>Load Slot</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRADES_ROW__LOAD_SLOT = eINSTANCE.getTradesRow_LoadSlot();

		/**
		 * The meta object literal for the '<em><b>Discharge Slot</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRADES_ROW__DISCHARGE_SLOT = eINSTANCE.getTradesRow_DischargeSlot();

		/**
		 * The meta object literal for the '<em><b>Cargo</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRADES_ROW__CARGO = eINSTANCE.getTradesRow_Cargo();

		/**
		 * The meta object literal for the '<em><b>Cargo Allocation</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRADES_ROW__CARGO_ALLOCATION = eINSTANCE.getTradesRow_CargoAllocation();

		/**
		 * The meta object literal for the '<em><b>Load Allocation</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRADES_ROW__LOAD_ALLOCATION = eINSTANCE.getTradesRow_LoadAllocation();

		/**
		 * The meta object literal for the '<em><b>Discharge Allocation</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRADES_ROW__DISCHARGE_ALLOCATION = eINSTANCE.getTradesRow_DischargeAllocation();

		/**
		 * The meta object literal for the '<em><b>Open Slot Allocation</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRADES_ROW__OPEN_SLOT_ALLOCATION = eINSTANCE.getTradesRow_OpenSlotAllocation();

		/**
		 * The meta object literal for the '<em><b>Input Equivalents</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRADES_ROW__INPUT_EQUIVALENTS = eINSTANCE.getTradesRow_InputEquivalents();

		/**
		 * The meta object literal for the '<em><b>Load Slot Contract Params</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRADES_ROW__LOAD_SLOT_CONTRACT_PARAMS = eINSTANCE.getTradesRow_LoadSlotContractParams();

		/**
		 * The meta object literal for the '<em><b>Primary Record</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TRADES_ROW__PRIMARY_RECORD = eINSTANCE.getTradesRow_PrimaryRecord();

		/**
		 * The meta object literal for the '<em><b>Discharge Slot Contract Params</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRADES_ROW__DISCHARGE_SLOT_CONTRACT_PARAMS = eINSTANCE.getTradesRow_DischargeSlotContractParams();

		/**
		 * The meta object literal for the '<em><b>Market Allocation</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRADES_ROW__MARKET_ALLOCATION = eINSTANCE.getTradesRow_MarketAllocation();

		/**
		 * The meta object literal for the '<em><b>Load Terminal Valid</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TRADES_ROW__LOAD_TERMINAL_VALID = eINSTANCE.getTradesRow_LoadTerminalValid();

		/**
		 * The meta object literal for the '<em><b>Discharge Terminal Valid</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TRADES_ROW__DISCHARGE_TERMINAL_VALID = eINSTANCE.getTradesRow_DischargeTerminalValid();

		/**
		 * The meta object literal for the '<em><b>Group</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TRADES_ROW__GROUP = eINSTANCE.getTradesRow_Group();

		/**
		 * The meta object literal for the '<em><b>Get Assignable Object</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation TRADES_ROW___GET_ASSIGNABLE_OBJECT = eINSTANCE.getTradesRow__GetAssignableObject();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.impl.TradesTableRootImpl <em>Trades Table Root</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.impl.TradesTableRootImpl
		 * @see com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.impl.CargoEditorModelPackageImpl#getTradesTableRoot()
		 * @generated
		 */
		EClass TRADES_TABLE_ROOT = eINSTANCE.getTradesTableRoot();

		/**
		 * The meta object literal for the '<em><b>Trades Rows</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRADES_TABLE_ROOT__TRADES_ROWS = eINSTANCE.getTradesTableRoot_TradesRows();

	}

} //CargoEditorModelPackage
