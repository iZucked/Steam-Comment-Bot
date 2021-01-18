/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor;

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
 * @see com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.CargoBulkEditorFactory
 * @model kind="package"
 * @generated
 */
public interface CargoBulkEditorPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "cargobulkeditor";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.mmxlabs.com/models/lng/cargobulkeditor/1/";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "lng.cargobulkeditor";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	CargoBulkEditorPackage eINSTANCE = com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.impl.CargoBulkEditorPackageImpl.init();

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.impl.RowImpl <em>Row</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.impl.RowImpl
	 * @see com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.impl.CargoBulkEditorPackageImpl#getRow()
	 * @generated
	 */
	int ROW = 0;

	/**
	 * The feature id for the '<em><b>Load Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROW__LOAD_SLOT = 0;

	/**
	 * The feature id for the '<em><b>Discharge Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROW__DISCHARGE_SLOT = 1;

	/**
	 * The feature id for the '<em><b>Cargo</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROW__CARGO = 2;

	/**
	 * The feature id for the '<em><b>Cargo Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROW__CARGO_ALLOCATION = 3;

	/**
	 * The feature id for the '<em><b>Load Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROW__LOAD_ALLOCATION = 4;

	/**
	 * The feature id for the '<em><b>Discharge Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROW__DISCHARGE_ALLOCATION = 5;

	/**
	 * The feature id for the '<em><b>Open Slot Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROW__OPEN_SLOT_ALLOCATION = 6;

	/**
	 * The feature id for the '<em><b>Input Equivalents</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROW__INPUT_EQUIVALENTS = 7;

	/**
	 * The feature id for the '<em><b>Load Slot Contract Params</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROW__LOAD_SLOT_CONTRACT_PARAMS = 8;

	/**
	 * The feature id for the '<em><b>Discharge Slot Contract Params</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROW__DISCHARGE_SLOT_CONTRACT_PARAMS = 9;

	/**
	 * The number of structural features of the '<em>Row</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROW_FEATURE_COUNT = 10;

	/**
	 * The operation id for the '<em>Get Assignable Object</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROW___GET_ASSIGNABLE_OBJECT = 0;

	/**
	 * The number of operations of the '<em>Row</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROW_OPERATION_COUNT = 1;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.impl.TableImpl <em>Table</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.impl.TableImpl
	 * @see com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.impl.CargoBulkEditorPackageImpl#getTable()
	 * @generated
	 */
	int TABLE = 1;

	/**
	 * The feature id for the '<em><b>Rows</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TABLE__ROWS = 0;

	/**
	 * The number of structural features of the '<em>Table</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TABLE_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Table</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TABLE_OPERATION_COUNT = 0;


	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.Row <em>Row</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Row</em>'.
	 * @see com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.Row
	 * @generated
	 */
	EClass getRow();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.Row#getLoadSlot <em>Load Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Load Slot</em>'.
	 * @see com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.Row#getLoadSlot()
	 * @see #getRow()
	 * @generated
	 */
	EReference getRow_LoadSlot();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.Row#getDischargeSlot <em>Discharge Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Discharge Slot</em>'.
	 * @see com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.Row#getDischargeSlot()
	 * @see #getRow()
	 * @generated
	 */
	EReference getRow_DischargeSlot();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.Row#getCargo <em>Cargo</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Cargo</em>'.
	 * @see com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.Row#getCargo()
	 * @see #getRow()
	 * @generated
	 */
	EReference getRow_Cargo();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.Row#getCargoAllocation <em>Cargo Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Cargo Allocation</em>'.
	 * @see com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.Row#getCargoAllocation()
	 * @see #getRow()
	 * @generated
	 */
	EReference getRow_CargoAllocation();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.Row#getLoadAllocation <em>Load Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Load Allocation</em>'.
	 * @see com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.Row#getLoadAllocation()
	 * @see #getRow()
	 * @generated
	 */
	EReference getRow_LoadAllocation();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.Row#getDischargeAllocation <em>Discharge Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Discharge Allocation</em>'.
	 * @see com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.Row#getDischargeAllocation()
	 * @see #getRow()
	 * @generated
	 */
	EReference getRow_DischargeAllocation();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.Row#getOpenSlotAllocation <em>Open Slot Allocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Open Slot Allocation</em>'.
	 * @see com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.Row#getOpenSlotAllocation()
	 * @see #getRow()
	 * @generated
	 */
	EReference getRow_OpenSlotAllocation();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.Row#getInputEquivalents <em>Input Equivalents</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Input Equivalents</em>'.
	 * @see com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.Row#getInputEquivalents()
	 * @see #getRow()
	 * @generated
	 */
	EReference getRow_InputEquivalents();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.Row#getLoadSlotContractParams <em>Load Slot Contract Params</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Load Slot Contract Params</em>'.
	 * @see com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.Row#getLoadSlotContractParams()
	 * @see #getRow()
	 * @generated
	 */
	EReference getRow_LoadSlotContractParams();

	/**
	 * Returns the meta object for the reference '{@link com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.Row#getDischargeSlotContractParams <em>Discharge Slot Contract Params</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Discharge Slot Contract Params</em>'.
	 * @see com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.Row#getDischargeSlotContractParams()
	 * @see #getRow()
	 * @generated
	 */
	EReference getRow_DischargeSlotContractParams();

	/**
	 * Returns the meta object for the '{@link com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.Row#getAssignableObject() <em>Get Assignable Object</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Assignable Object</em>' operation.
	 * @see com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.Row#getAssignableObject()
	 * @generated
	 */
	EOperation getRow__GetAssignableObject();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.Table <em>Table</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Table</em>'.
	 * @see com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.Table
	 * @generated
	 */
	EClass getTable();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.Table#getRows <em>Rows</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Rows</em>'.
	 * @see com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.Table#getRows()
	 * @see #getTable()
	 * @generated
	 */
	EReference getTable_Rows();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	CargoBulkEditorFactory getCargoBulkEditorFactory();

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
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.impl.RowImpl <em>Row</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.impl.RowImpl
		 * @see com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.impl.CargoBulkEditorPackageImpl#getRow()
		 * @generated
		 */
		EClass ROW = eINSTANCE.getRow();

		/**
		 * The meta object literal for the '<em><b>Load Slot</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ROW__LOAD_SLOT = eINSTANCE.getRow_LoadSlot();

		/**
		 * The meta object literal for the '<em><b>Discharge Slot</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ROW__DISCHARGE_SLOT = eINSTANCE.getRow_DischargeSlot();

		/**
		 * The meta object literal for the '<em><b>Cargo</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ROW__CARGO = eINSTANCE.getRow_Cargo();

		/**
		 * The meta object literal for the '<em><b>Cargo Allocation</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ROW__CARGO_ALLOCATION = eINSTANCE.getRow_CargoAllocation();

		/**
		 * The meta object literal for the '<em><b>Load Allocation</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ROW__LOAD_ALLOCATION = eINSTANCE.getRow_LoadAllocation();

		/**
		 * The meta object literal for the '<em><b>Discharge Allocation</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ROW__DISCHARGE_ALLOCATION = eINSTANCE.getRow_DischargeAllocation();

		/**
		 * The meta object literal for the '<em><b>Open Slot Allocation</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ROW__OPEN_SLOT_ALLOCATION = eINSTANCE.getRow_OpenSlotAllocation();

		/**
		 * The meta object literal for the '<em><b>Input Equivalents</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ROW__INPUT_EQUIVALENTS = eINSTANCE.getRow_InputEquivalents();

		/**
		 * The meta object literal for the '<em><b>Load Slot Contract Params</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ROW__LOAD_SLOT_CONTRACT_PARAMS = eINSTANCE.getRow_LoadSlotContractParams();

		/**
		 * The meta object literal for the '<em><b>Discharge Slot Contract Params</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ROW__DISCHARGE_SLOT_CONTRACT_PARAMS = eINSTANCE.getRow_DischargeSlotContractParams();

		/**
		 * The meta object literal for the '<em><b>Get Assignable Object</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation ROW___GET_ASSIGNABLE_OBJECT = eINSTANCE.getRow__GetAssignableObject();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.impl.TableImpl <em>Table</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.impl.TableImpl
		 * @see com.mmxlabs.models.lng.cargo.editor.bulk.cargobulkeditor.impl.CargoBulkEditorPackageImpl#getTable()
		 * @generated
		 */
		EClass TABLE = eINSTANCE.getTable();

		/**
		 * The meta object literal for the '<em><b>Rows</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TABLE__ROWS = eINSTANCE.getTable_Rows();

	}

} //CargoBulkEditorPackage
