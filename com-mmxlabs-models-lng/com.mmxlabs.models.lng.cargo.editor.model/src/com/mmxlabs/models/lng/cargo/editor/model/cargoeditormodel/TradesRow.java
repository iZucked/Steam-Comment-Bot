/**
 */
package com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;

import com.mmxlabs.models.lng.commercial.SlotContractParams;

import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.MarketAllocation;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotAllocation;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Trades Row</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow#getLoadSlot <em>Load Slot</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow#getDischargeSlot <em>Discharge Slot</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow#getCargo <em>Cargo</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow#getCargoAllocation <em>Cargo Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow#getLoadAllocation <em>Load Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow#getDischargeAllocation <em>Discharge Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow#getOpenSlotAllocation <em>Open Slot Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow#getInputEquivalents <em>Input Equivalents</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow#getLoadSlotContractParams <em>Load Slot Contract Params</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow#isPrimaryRecord <em>Primary Record</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow#getDischargeSlotContractParams <em>Discharge Slot Contract Params</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow#getMarketAllocation <em>Market Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow#isLoadTerminalValid <em>Load Terminal Valid</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow#isDischargeTerminalValid <em>Discharge Terminal Valid</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow#getGroup <em>Group</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.CargoEditorModelPackage#getTradesRow()
 * @model
 * @generated
 */
public interface TradesRow extends EObject {
	/**
	 * Returns the value of the '<em><b>Load Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Load Slot</em>' reference.
	 * @see #setLoadSlot(LoadSlot)
	 * @see com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.CargoEditorModelPackage#getTradesRow_LoadSlot()
	 * @model
	 * @generated
	 */
	LoadSlot getLoadSlot();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow#getLoadSlot <em>Load Slot</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Load Slot</em>' reference.
	 * @see #getLoadSlot()
	 * @generated
	 */
	void setLoadSlot(LoadSlot value);

	/**
	 * Returns the value of the '<em><b>Discharge Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Discharge Slot</em>' reference.
	 * @see #setDischargeSlot(DischargeSlot)
	 * @see com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.CargoEditorModelPackage#getTradesRow_DischargeSlot()
	 * @model
	 * @generated
	 */
	DischargeSlot getDischargeSlot();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow#getDischargeSlot <em>Discharge Slot</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Discharge Slot</em>' reference.
	 * @see #getDischargeSlot()
	 * @generated
	 */
	void setDischargeSlot(DischargeSlot value);

	/**
	 * Returns the value of the '<em><b>Cargo</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cargo</em>' reference.
	 * @see #setCargo(Cargo)
	 * @see com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.CargoEditorModelPackage#getTradesRow_Cargo()
	 * @model
	 * @generated
	 */
	Cargo getCargo();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow#getCargo <em>Cargo</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cargo</em>' reference.
	 * @see #getCargo()
	 * @generated
	 */
	void setCargo(Cargo value);

	/**
	 * Returns the value of the '<em><b>Cargo Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cargo Allocation</em>' reference.
	 * @see #setCargoAllocation(CargoAllocation)
	 * @see com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.CargoEditorModelPackage#getTradesRow_CargoAllocation()
	 * @model
	 * @generated
	 */
	CargoAllocation getCargoAllocation();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow#getCargoAllocation <em>Cargo Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cargo Allocation</em>' reference.
	 * @see #getCargoAllocation()
	 * @generated
	 */
	void setCargoAllocation(CargoAllocation value);

	/**
	 * Returns the value of the '<em><b>Load Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Load Allocation</em>' reference.
	 * @see #setLoadAllocation(SlotAllocation)
	 * @see com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.CargoEditorModelPackage#getTradesRow_LoadAllocation()
	 * @model
	 * @generated
	 */
	SlotAllocation getLoadAllocation();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow#getLoadAllocation <em>Load Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Load Allocation</em>' reference.
	 * @see #getLoadAllocation()
	 * @generated
	 */
	void setLoadAllocation(SlotAllocation value);

	/**
	 * Returns the value of the '<em><b>Discharge Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Discharge Allocation</em>' reference.
	 * @see #setDischargeAllocation(SlotAllocation)
	 * @see com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.CargoEditorModelPackage#getTradesRow_DischargeAllocation()
	 * @model
	 * @generated
	 */
	SlotAllocation getDischargeAllocation();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow#getDischargeAllocation <em>Discharge Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Discharge Allocation</em>' reference.
	 * @see #getDischargeAllocation()
	 * @generated
	 */
	void setDischargeAllocation(SlotAllocation value);

	/**
	 * Returns the value of the '<em><b>Open Slot Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Open Slot Allocation</em>' reference.
	 * @see #setOpenSlotAllocation(OpenSlotAllocation)
	 * @see com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.CargoEditorModelPackage#getTradesRow_OpenSlotAllocation()
	 * @model
	 * @generated
	 */
	OpenSlotAllocation getOpenSlotAllocation();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow#getOpenSlotAllocation <em>Open Slot Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Open Slot Allocation</em>' reference.
	 * @see #getOpenSlotAllocation()
	 * @generated
	 */
	void setOpenSlotAllocation(OpenSlotAllocation value);

	/**
	 * Returns the value of the '<em><b>Input Equivalents</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.EObject}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Input Equivalents</em>' reference list.
	 * @see com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.CargoEditorModelPackage#getTradesRow_InputEquivalents()
	 * @model
	 * @generated
	 */
	EList<EObject> getInputEquivalents();

	/**
	 * Returns the value of the '<em><b>Load Slot Contract Params</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Load Slot Contract Params</em>' reference.
	 * @see #setLoadSlotContractParams(SlotContractParams)
	 * @see com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.CargoEditorModelPackage#getTradesRow_LoadSlotContractParams()
	 * @model
	 * @generated
	 */
	SlotContractParams getLoadSlotContractParams();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow#getLoadSlotContractParams <em>Load Slot Contract Params</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Load Slot Contract Params</em>' reference.
	 * @see #getLoadSlotContractParams()
	 * @generated
	 */
	void setLoadSlotContractParams(SlotContractParams value);

	/**
	 * Returns the value of the '<em><b>Discharge Slot Contract Params</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Discharge Slot Contract Params</em>' reference.
	 * @see #setDischargeSlotContractParams(SlotContractParams)
	 * @see com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.CargoEditorModelPackage#getTradesRow_DischargeSlotContractParams()
	 * @model
	 * @generated
	 */
	SlotContractParams getDischargeSlotContractParams();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow#getDischargeSlotContractParams <em>Discharge Slot Contract Params</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Discharge Slot Contract Params</em>' reference.
	 * @see #getDischargeSlotContractParams()
	 * @generated
	 */
	void setDischargeSlotContractParams(SlotContractParams value);

	/**
	 * Returns the value of the '<em><b>Primary Record</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Primary Record</em>' attribute.
	 * @see #setPrimaryRecord(boolean)
	 * @see com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.CargoEditorModelPackage#getTradesRow_PrimaryRecord()
	 * @model
	 * @generated
	 */
	boolean isPrimaryRecord();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow#isPrimaryRecord <em>Primary Record</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Primary Record</em>' attribute.
	 * @see #isPrimaryRecord()
	 * @generated
	 */
	void setPrimaryRecord(boolean value);

	/**
	 * Returns the value of the '<em><b>Market Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Market Allocation</em>' reference.
	 * @see #setMarketAllocation(MarketAllocation)
	 * @see com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.CargoEditorModelPackage#getTradesRow_MarketAllocation()
	 * @model
	 * @generated
	 */
	MarketAllocation getMarketAllocation();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow#getMarketAllocation <em>Market Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Market Allocation</em>' reference.
	 * @see #getMarketAllocation()
	 * @generated
	 */
	void setMarketAllocation(MarketAllocation value);

	/**
	 * Returns the value of the '<em><b>Load Terminal Valid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Load Terminal Valid</em>' attribute.
	 * @see #setLoadTerminalValid(boolean)
	 * @see com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.CargoEditorModelPackage#getTradesRow_LoadTerminalValid()
	 * @model
	 * @generated
	 */
	boolean isLoadTerminalValid();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow#isLoadTerminalValid <em>Load Terminal Valid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Load Terminal Valid</em>' attribute.
	 * @see #isLoadTerminalValid()
	 * @generated
	 */
	void setLoadTerminalValid(boolean value);

	/**
	 * Returns the value of the '<em><b>Discharge Terminal Valid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Discharge Terminal Valid</em>' attribute.
	 * @see #setDischargeTerminalValid(boolean)
	 * @see com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.CargoEditorModelPackage#getTradesRow_DischargeTerminalValid()
	 * @model
	 * @generated
	 */
	boolean isDischargeTerminalValid();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow#isDischargeTerminalValid <em>Discharge Terminal Valid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Discharge Terminal Valid</em>' attribute.
	 * @see #isDischargeTerminalValid()
	 * @generated
	 */
	void setDischargeTerminalValid(boolean value);

	/**
	 * Returns the value of the '<em><b>Group</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Group</em>' attribute.
	 * @see #setGroup(Object)
	 * @see com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.CargoEditorModelPackage#getTradesRow_Group()
	 * @model ordered="false"
	 * @generated
	 */
	Object getGroup();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow#getGroup <em>Group</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Group</em>' attribute.
	 * @see #getGroup()
	 * @generated
	 */
	void setGroup(Object value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	EObject getAssignableObject();

} // TradesRow
