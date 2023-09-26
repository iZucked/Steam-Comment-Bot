/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.impl;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;

import com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.CargoEditorModelPackage;
import com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.TradesRow;

import com.mmxlabs.models.lng.commercial.SlotContractParams;

import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.MarketAllocation;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotAllocation;

import java.lang.reflect.InvocationTargetException;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Trades Row</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.impl.TradesRowImpl#getLoadSlot <em>Load Slot</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.impl.TradesRowImpl#getDischargeSlot <em>Discharge Slot</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.impl.TradesRowImpl#getCargo <em>Cargo</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.impl.TradesRowImpl#getCargoAllocation <em>Cargo Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.impl.TradesRowImpl#getLoadAllocation <em>Load Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.impl.TradesRowImpl#getDischargeAllocation <em>Discharge Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.impl.TradesRowImpl#getOpenSlotAllocation <em>Open Slot Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.impl.TradesRowImpl#getInputEquivalents <em>Input Equivalents</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.impl.TradesRowImpl#getLoadSlotContractParams <em>Load Slot Contract Params</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.impl.TradesRowImpl#isPrimaryRecord <em>Primary Record</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.impl.TradesRowImpl#getDischargeSlotContractParams <em>Discharge Slot Contract Params</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.impl.TradesRowImpl#getMarketAllocation <em>Market Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.impl.TradesRowImpl#isLoadTerminalValid <em>Load Terminal Valid</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.impl.TradesRowImpl#isDischargeTerminalValid <em>Discharge Terminal Valid</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.editor.model.cargoeditormodel.impl.TradesRowImpl#getGroup <em>Group</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TradesRowImpl extends EObjectImpl implements TradesRow {
	/**
	 * The cached value of the '{@link #getLoadSlot() <em>Load Slot</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLoadSlot()
	 * @generated
	 * @ordered
	 */
	protected LoadSlot loadSlot;

	/**
	 * The cached value of the '{@link #getDischargeSlot() <em>Discharge Slot</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDischargeSlot()
	 * @generated
	 * @ordered
	 */
	protected DischargeSlot dischargeSlot;

	/**
	 * The cached value of the '{@link #getCargo() <em>Cargo</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCargo()
	 * @generated
	 * @ordered
	 */
	protected Cargo cargo;

	/**
	 * The cached value of the '{@link #getCargoAllocation() <em>Cargo Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCargoAllocation()
	 * @generated
	 * @ordered
	 */
	protected CargoAllocation cargoAllocation;

	/**
	 * The cached value of the '{@link #getLoadAllocation() <em>Load Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLoadAllocation()
	 * @generated
	 * @ordered
	 */
	protected SlotAllocation loadAllocation;

	/**
	 * The cached value of the '{@link #getDischargeAllocation() <em>Discharge Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDischargeAllocation()
	 * @generated
	 * @ordered
	 */
	protected SlotAllocation dischargeAllocation;

	/**
	 * The cached value of the '{@link #getOpenSlotAllocation() <em>Open Slot Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOpenSlotAllocation()
	 * @generated
	 * @ordered
	 */
	protected OpenSlotAllocation openSlotAllocation;

	/**
	 * The cached value of the '{@link #getInputEquivalents() <em>Input Equivalents</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInputEquivalents()
	 * @generated
	 * @ordered
	 */
	protected EList<EObject> inputEquivalents;

	/**
	 * The cached value of the '{@link #getLoadSlotContractParams() <em>Load Slot Contract Params</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLoadSlotContractParams()
	 * @generated
	 * @ordered
	 */
	protected SlotContractParams loadSlotContractParams;

	/**
	 * The default value of the '{@link #isPrimaryRecord() <em>Primary Record</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isPrimaryRecord()
	 * @generated
	 * @ordered
	 */
	protected static final boolean PRIMARY_RECORD_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isPrimaryRecord() <em>Primary Record</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isPrimaryRecord()
	 * @generated
	 * @ordered
	 */
	protected boolean primaryRecord = PRIMARY_RECORD_EDEFAULT;

	/**
	 * The cached value of the '{@link #getDischargeSlotContractParams() <em>Discharge Slot Contract Params</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDischargeSlotContractParams()
	 * @generated
	 * @ordered
	 */
	protected SlotContractParams dischargeSlotContractParams;

	/**
	 * The cached value of the '{@link #getMarketAllocation() <em>Market Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMarketAllocation()
	 * @generated
	 * @ordered
	 */
	protected MarketAllocation marketAllocation;

	/**
	 * The default value of the '{@link #isLoadTerminalValid() <em>Load Terminal Valid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isLoadTerminalValid()
	 * @generated
	 * @ordered
	 */
	protected static final boolean LOAD_TERMINAL_VALID_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isLoadTerminalValid() <em>Load Terminal Valid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isLoadTerminalValid()
	 * @generated
	 * @ordered
	 */
	protected boolean loadTerminalValid = LOAD_TERMINAL_VALID_EDEFAULT;

	/**
	 * The default value of the '{@link #isDischargeTerminalValid() <em>Discharge Terminal Valid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isDischargeTerminalValid()
	 * @generated
	 * @ordered
	 */
	protected static final boolean DISCHARGE_TERMINAL_VALID_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isDischargeTerminalValid() <em>Discharge Terminal Valid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isDischargeTerminalValid()
	 * @generated
	 * @ordered
	 */
	protected boolean dischargeTerminalValid = DISCHARGE_TERMINAL_VALID_EDEFAULT;

	/**
	 * The default value of the '{@link #getGroup() <em>Group</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGroup()
	 * @generated
	 * @ordered
	 */
	protected static final Object GROUP_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getGroup() <em>Group</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGroup()
	 * @generated
	 * @ordered
	 */
	protected Object group = GROUP_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TradesRowImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CargoEditorModelPackage.Literals.TRADES_ROW;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LoadSlot getLoadSlot() {
		if (loadSlot != null && loadSlot.eIsProxy()) {
			InternalEObject oldLoadSlot = (InternalEObject)loadSlot;
			loadSlot = (LoadSlot)eResolveProxy(oldLoadSlot);
			if (loadSlot != oldLoadSlot) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoEditorModelPackage.TRADES_ROW__LOAD_SLOT, oldLoadSlot, loadSlot));
			}
		}
		return loadSlot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LoadSlot basicGetLoadSlot() {
		return loadSlot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLoadSlot(LoadSlot newLoadSlot) {
		LoadSlot oldLoadSlot = loadSlot;
		loadSlot = newLoadSlot;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoEditorModelPackage.TRADES_ROW__LOAD_SLOT, oldLoadSlot, loadSlot));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DischargeSlot getDischargeSlot() {
		if (dischargeSlot != null && dischargeSlot.eIsProxy()) {
			InternalEObject oldDischargeSlot = (InternalEObject)dischargeSlot;
			dischargeSlot = (DischargeSlot)eResolveProxy(oldDischargeSlot);
			if (dischargeSlot != oldDischargeSlot) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoEditorModelPackage.TRADES_ROW__DISCHARGE_SLOT, oldDischargeSlot, dischargeSlot));
			}
		}
		return dischargeSlot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DischargeSlot basicGetDischargeSlot() {
		return dischargeSlot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDischargeSlot(DischargeSlot newDischargeSlot) {
		DischargeSlot oldDischargeSlot = dischargeSlot;
		dischargeSlot = newDischargeSlot;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoEditorModelPackage.TRADES_ROW__DISCHARGE_SLOT, oldDischargeSlot, dischargeSlot));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Cargo getCargo() {
		if (cargo != null && cargo.eIsProxy()) {
			InternalEObject oldCargo = (InternalEObject)cargo;
			cargo = (Cargo)eResolveProxy(oldCargo);
			if (cargo != oldCargo) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoEditorModelPackage.TRADES_ROW__CARGO, oldCargo, cargo));
			}
		}
		return cargo;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Cargo basicGetCargo() {
		return cargo;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCargo(Cargo newCargo) {
		Cargo oldCargo = cargo;
		cargo = newCargo;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoEditorModelPackage.TRADES_ROW__CARGO, oldCargo, cargo));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CargoAllocation getCargoAllocation() {
		if (cargoAllocation != null && cargoAllocation.eIsProxy()) {
			InternalEObject oldCargoAllocation = (InternalEObject)cargoAllocation;
			cargoAllocation = (CargoAllocation)eResolveProxy(oldCargoAllocation);
			if (cargoAllocation != oldCargoAllocation) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoEditorModelPackage.TRADES_ROW__CARGO_ALLOCATION, oldCargoAllocation, cargoAllocation));
			}
		}
		return cargoAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CargoAllocation basicGetCargoAllocation() {
		return cargoAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCargoAllocation(CargoAllocation newCargoAllocation) {
		CargoAllocation oldCargoAllocation = cargoAllocation;
		cargoAllocation = newCargoAllocation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoEditorModelPackage.TRADES_ROW__CARGO_ALLOCATION, oldCargoAllocation, cargoAllocation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SlotAllocation getLoadAllocation() {
		if (loadAllocation != null && loadAllocation.eIsProxy()) {
			InternalEObject oldLoadAllocation = (InternalEObject)loadAllocation;
			loadAllocation = (SlotAllocation)eResolveProxy(oldLoadAllocation);
			if (loadAllocation != oldLoadAllocation) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoEditorModelPackage.TRADES_ROW__LOAD_ALLOCATION, oldLoadAllocation, loadAllocation));
			}
		}
		return loadAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SlotAllocation basicGetLoadAllocation() {
		return loadAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLoadAllocation(SlotAllocation newLoadAllocation) {
		SlotAllocation oldLoadAllocation = loadAllocation;
		loadAllocation = newLoadAllocation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoEditorModelPackage.TRADES_ROW__LOAD_ALLOCATION, oldLoadAllocation, loadAllocation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SlotAllocation getDischargeAllocation() {
		if (dischargeAllocation != null && dischargeAllocation.eIsProxy()) {
			InternalEObject oldDischargeAllocation = (InternalEObject)dischargeAllocation;
			dischargeAllocation = (SlotAllocation)eResolveProxy(oldDischargeAllocation);
			if (dischargeAllocation != oldDischargeAllocation) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoEditorModelPackage.TRADES_ROW__DISCHARGE_ALLOCATION, oldDischargeAllocation, dischargeAllocation));
			}
		}
		return dischargeAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SlotAllocation basicGetDischargeAllocation() {
		return dischargeAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDischargeAllocation(SlotAllocation newDischargeAllocation) {
		SlotAllocation oldDischargeAllocation = dischargeAllocation;
		dischargeAllocation = newDischargeAllocation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoEditorModelPackage.TRADES_ROW__DISCHARGE_ALLOCATION, oldDischargeAllocation, dischargeAllocation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public OpenSlotAllocation getOpenSlotAllocation() {
		if (openSlotAllocation != null && openSlotAllocation.eIsProxy()) {
			InternalEObject oldOpenSlotAllocation = (InternalEObject)openSlotAllocation;
			openSlotAllocation = (OpenSlotAllocation)eResolveProxy(oldOpenSlotAllocation);
			if (openSlotAllocation != oldOpenSlotAllocation) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoEditorModelPackage.TRADES_ROW__OPEN_SLOT_ALLOCATION, oldOpenSlotAllocation, openSlotAllocation));
			}
		}
		return openSlotAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OpenSlotAllocation basicGetOpenSlotAllocation() {
		return openSlotAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setOpenSlotAllocation(OpenSlotAllocation newOpenSlotAllocation) {
		OpenSlotAllocation oldOpenSlotAllocation = openSlotAllocation;
		openSlotAllocation = newOpenSlotAllocation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoEditorModelPackage.TRADES_ROW__OPEN_SLOT_ALLOCATION, oldOpenSlotAllocation, openSlotAllocation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<EObject> getInputEquivalents() {
		if (inputEquivalents == null) {
			inputEquivalents = new EObjectResolvingEList<EObject>(EObject.class, this, CargoEditorModelPackage.TRADES_ROW__INPUT_EQUIVALENTS);
		}
		return inputEquivalents;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SlotContractParams getLoadSlotContractParams() {
		if (loadSlotContractParams != null && loadSlotContractParams.eIsProxy()) {
			InternalEObject oldLoadSlotContractParams = (InternalEObject)loadSlotContractParams;
			loadSlotContractParams = (SlotContractParams)eResolveProxy(oldLoadSlotContractParams);
			if (loadSlotContractParams != oldLoadSlotContractParams) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoEditorModelPackage.TRADES_ROW__LOAD_SLOT_CONTRACT_PARAMS, oldLoadSlotContractParams, loadSlotContractParams));
			}
		}
		return loadSlotContractParams;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SlotContractParams basicGetLoadSlotContractParams() {
		return loadSlotContractParams;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLoadSlotContractParams(SlotContractParams newLoadSlotContractParams) {
		SlotContractParams oldLoadSlotContractParams = loadSlotContractParams;
		loadSlotContractParams = newLoadSlotContractParams;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoEditorModelPackage.TRADES_ROW__LOAD_SLOT_CONTRACT_PARAMS, oldLoadSlotContractParams, loadSlotContractParams));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isPrimaryRecord() {
		return primaryRecord;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPrimaryRecord(boolean newPrimaryRecord) {
		boolean oldPrimaryRecord = primaryRecord;
		primaryRecord = newPrimaryRecord;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoEditorModelPackage.TRADES_ROW__PRIMARY_RECORD, oldPrimaryRecord, primaryRecord));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SlotContractParams getDischargeSlotContractParams() {
		if (dischargeSlotContractParams != null && dischargeSlotContractParams.eIsProxy()) {
			InternalEObject oldDischargeSlotContractParams = (InternalEObject)dischargeSlotContractParams;
			dischargeSlotContractParams = (SlotContractParams)eResolveProxy(oldDischargeSlotContractParams);
			if (dischargeSlotContractParams != oldDischargeSlotContractParams) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoEditorModelPackage.TRADES_ROW__DISCHARGE_SLOT_CONTRACT_PARAMS, oldDischargeSlotContractParams, dischargeSlotContractParams));
			}
		}
		return dischargeSlotContractParams;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SlotContractParams basicGetDischargeSlotContractParams() {
		return dischargeSlotContractParams;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDischargeSlotContractParams(SlotContractParams newDischargeSlotContractParams) {
		SlotContractParams oldDischargeSlotContractParams = dischargeSlotContractParams;
		dischargeSlotContractParams = newDischargeSlotContractParams;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoEditorModelPackage.TRADES_ROW__DISCHARGE_SLOT_CONTRACT_PARAMS, oldDischargeSlotContractParams, dischargeSlotContractParams));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public MarketAllocation getMarketAllocation() {
		if (marketAllocation != null && marketAllocation.eIsProxy()) {
			InternalEObject oldMarketAllocation = (InternalEObject)marketAllocation;
			marketAllocation = (MarketAllocation)eResolveProxy(oldMarketAllocation);
			if (marketAllocation != oldMarketAllocation) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoEditorModelPackage.TRADES_ROW__MARKET_ALLOCATION, oldMarketAllocation, marketAllocation));
			}
		}
		return marketAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MarketAllocation basicGetMarketAllocation() {
		return marketAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMarketAllocation(MarketAllocation newMarketAllocation) {
		MarketAllocation oldMarketAllocation = marketAllocation;
		marketAllocation = newMarketAllocation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoEditorModelPackage.TRADES_ROW__MARKET_ALLOCATION, oldMarketAllocation, marketAllocation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isLoadTerminalValid() {
		return loadTerminalValid;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLoadTerminalValid(boolean newLoadTerminalValid) {
		boolean oldLoadTerminalValid = loadTerminalValid;
		loadTerminalValid = newLoadTerminalValid;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoEditorModelPackage.TRADES_ROW__LOAD_TERMINAL_VALID, oldLoadTerminalValid, loadTerminalValid));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isDischargeTerminalValid() {
		return dischargeTerminalValid;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDischargeTerminalValid(boolean newDischargeTerminalValid) {
		boolean oldDischargeTerminalValid = dischargeTerminalValid;
		dischargeTerminalValid = newDischargeTerminalValid;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoEditorModelPackage.TRADES_ROW__DISCHARGE_TERMINAL_VALID, oldDischargeTerminalValid, dischargeTerminalValid));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getGroup() {
		return group;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setGroup(Object newGroup) {
		Object oldGroup = group;
		group = newGroup;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoEditorModelPackage.TRADES_ROW__GROUP, oldGroup, group));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public EObject getAssignableObject() {
		if (this.loadSlot != null && this.loadSlot.isDESPurchase()) {
			return this.loadSlot;
		}
		if (this.dischargeSlot != null && this.dischargeSlot.isFOBSale()) {
			return this.dischargeSlot;
		}
		return this.cargo;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CargoEditorModelPackage.TRADES_ROW__LOAD_SLOT:
				if (resolve) return getLoadSlot();
				return basicGetLoadSlot();
			case CargoEditorModelPackage.TRADES_ROW__DISCHARGE_SLOT:
				if (resolve) return getDischargeSlot();
				return basicGetDischargeSlot();
			case CargoEditorModelPackage.TRADES_ROW__CARGO:
				if (resolve) return getCargo();
				return basicGetCargo();
			case CargoEditorModelPackage.TRADES_ROW__CARGO_ALLOCATION:
				if (resolve) return getCargoAllocation();
				return basicGetCargoAllocation();
			case CargoEditorModelPackage.TRADES_ROW__LOAD_ALLOCATION:
				if (resolve) return getLoadAllocation();
				return basicGetLoadAllocation();
			case CargoEditorModelPackage.TRADES_ROW__DISCHARGE_ALLOCATION:
				if (resolve) return getDischargeAllocation();
				return basicGetDischargeAllocation();
			case CargoEditorModelPackage.TRADES_ROW__OPEN_SLOT_ALLOCATION:
				if (resolve) return getOpenSlotAllocation();
				return basicGetOpenSlotAllocation();
			case CargoEditorModelPackage.TRADES_ROW__INPUT_EQUIVALENTS:
				return getInputEquivalents();
			case CargoEditorModelPackage.TRADES_ROW__LOAD_SLOT_CONTRACT_PARAMS:
				if (resolve) return getLoadSlotContractParams();
				return basicGetLoadSlotContractParams();
			case CargoEditorModelPackage.TRADES_ROW__PRIMARY_RECORD:
				return isPrimaryRecord();
			case CargoEditorModelPackage.TRADES_ROW__DISCHARGE_SLOT_CONTRACT_PARAMS:
				if (resolve) return getDischargeSlotContractParams();
				return basicGetDischargeSlotContractParams();
			case CargoEditorModelPackage.TRADES_ROW__MARKET_ALLOCATION:
				if (resolve) return getMarketAllocation();
				return basicGetMarketAllocation();
			case CargoEditorModelPackage.TRADES_ROW__LOAD_TERMINAL_VALID:
				return isLoadTerminalValid();
			case CargoEditorModelPackage.TRADES_ROW__DISCHARGE_TERMINAL_VALID:
				return isDischargeTerminalValid();
			case CargoEditorModelPackage.TRADES_ROW__GROUP:
				return getGroup();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case CargoEditorModelPackage.TRADES_ROW__LOAD_SLOT:
				setLoadSlot((LoadSlot)newValue);
				return;
			case CargoEditorModelPackage.TRADES_ROW__DISCHARGE_SLOT:
				setDischargeSlot((DischargeSlot)newValue);
				return;
			case CargoEditorModelPackage.TRADES_ROW__CARGO:
				setCargo((Cargo)newValue);
				return;
			case CargoEditorModelPackage.TRADES_ROW__CARGO_ALLOCATION:
				setCargoAllocation((CargoAllocation)newValue);
				return;
			case CargoEditorModelPackage.TRADES_ROW__LOAD_ALLOCATION:
				setLoadAllocation((SlotAllocation)newValue);
				return;
			case CargoEditorModelPackage.TRADES_ROW__DISCHARGE_ALLOCATION:
				setDischargeAllocation((SlotAllocation)newValue);
				return;
			case CargoEditorModelPackage.TRADES_ROW__OPEN_SLOT_ALLOCATION:
				setOpenSlotAllocation((OpenSlotAllocation)newValue);
				return;
			case CargoEditorModelPackage.TRADES_ROW__INPUT_EQUIVALENTS:
				getInputEquivalents().clear();
				getInputEquivalents().addAll((Collection<? extends EObject>)newValue);
				return;
			case CargoEditorModelPackage.TRADES_ROW__LOAD_SLOT_CONTRACT_PARAMS:
				setLoadSlotContractParams((SlotContractParams)newValue);
				return;
			case CargoEditorModelPackage.TRADES_ROW__PRIMARY_RECORD:
				setPrimaryRecord((Boolean)newValue);
				return;
			case CargoEditorModelPackage.TRADES_ROW__DISCHARGE_SLOT_CONTRACT_PARAMS:
				setDischargeSlotContractParams((SlotContractParams)newValue);
				return;
			case CargoEditorModelPackage.TRADES_ROW__MARKET_ALLOCATION:
				setMarketAllocation((MarketAllocation)newValue);
				return;
			case CargoEditorModelPackage.TRADES_ROW__LOAD_TERMINAL_VALID:
				setLoadTerminalValid((Boolean)newValue);
				return;
			case CargoEditorModelPackage.TRADES_ROW__DISCHARGE_TERMINAL_VALID:
				setDischargeTerminalValid((Boolean)newValue);
				return;
			case CargoEditorModelPackage.TRADES_ROW__GROUP:
				setGroup(newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case CargoEditorModelPackage.TRADES_ROW__LOAD_SLOT:
				setLoadSlot((LoadSlot)null);
				return;
			case CargoEditorModelPackage.TRADES_ROW__DISCHARGE_SLOT:
				setDischargeSlot((DischargeSlot)null);
				return;
			case CargoEditorModelPackage.TRADES_ROW__CARGO:
				setCargo((Cargo)null);
				return;
			case CargoEditorModelPackage.TRADES_ROW__CARGO_ALLOCATION:
				setCargoAllocation((CargoAllocation)null);
				return;
			case CargoEditorModelPackage.TRADES_ROW__LOAD_ALLOCATION:
				setLoadAllocation((SlotAllocation)null);
				return;
			case CargoEditorModelPackage.TRADES_ROW__DISCHARGE_ALLOCATION:
				setDischargeAllocation((SlotAllocation)null);
				return;
			case CargoEditorModelPackage.TRADES_ROW__OPEN_SLOT_ALLOCATION:
				setOpenSlotAllocation((OpenSlotAllocation)null);
				return;
			case CargoEditorModelPackage.TRADES_ROW__INPUT_EQUIVALENTS:
				getInputEquivalents().clear();
				return;
			case CargoEditorModelPackage.TRADES_ROW__LOAD_SLOT_CONTRACT_PARAMS:
				setLoadSlotContractParams((SlotContractParams)null);
				return;
			case CargoEditorModelPackage.TRADES_ROW__PRIMARY_RECORD:
				setPrimaryRecord(PRIMARY_RECORD_EDEFAULT);
				return;
			case CargoEditorModelPackage.TRADES_ROW__DISCHARGE_SLOT_CONTRACT_PARAMS:
				setDischargeSlotContractParams((SlotContractParams)null);
				return;
			case CargoEditorModelPackage.TRADES_ROW__MARKET_ALLOCATION:
				setMarketAllocation((MarketAllocation)null);
				return;
			case CargoEditorModelPackage.TRADES_ROW__LOAD_TERMINAL_VALID:
				setLoadTerminalValid(LOAD_TERMINAL_VALID_EDEFAULT);
				return;
			case CargoEditorModelPackage.TRADES_ROW__DISCHARGE_TERMINAL_VALID:
				setDischargeTerminalValid(DISCHARGE_TERMINAL_VALID_EDEFAULT);
				return;
			case CargoEditorModelPackage.TRADES_ROW__GROUP:
				setGroup(GROUP_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case CargoEditorModelPackage.TRADES_ROW__LOAD_SLOT:
				return loadSlot != null;
			case CargoEditorModelPackage.TRADES_ROW__DISCHARGE_SLOT:
				return dischargeSlot != null;
			case CargoEditorModelPackage.TRADES_ROW__CARGO:
				return cargo != null;
			case CargoEditorModelPackage.TRADES_ROW__CARGO_ALLOCATION:
				return cargoAllocation != null;
			case CargoEditorModelPackage.TRADES_ROW__LOAD_ALLOCATION:
				return loadAllocation != null;
			case CargoEditorModelPackage.TRADES_ROW__DISCHARGE_ALLOCATION:
				return dischargeAllocation != null;
			case CargoEditorModelPackage.TRADES_ROW__OPEN_SLOT_ALLOCATION:
				return openSlotAllocation != null;
			case CargoEditorModelPackage.TRADES_ROW__INPUT_EQUIVALENTS:
				return inputEquivalents != null && !inputEquivalents.isEmpty();
			case CargoEditorModelPackage.TRADES_ROW__LOAD_SLOT_CONTRACT_PARAMS:
				return loadSlotContractParams != null;
			case CargoEditorModelPackage.TRADES_ROW__PRIMARY_RECORD:
				return primaryRecord != PRIMARY_RECORD_EDEFAULT;
			case CargoEditorModelPackage.TRADES_ROW__DISCHARGE_SLOT_CONTRACT_PARAMS:
				return dischargeSlotContractParams != null;
			case CargoEditorModelPackage.TRADES_ROW__MARKET_ALLOCATION:
				return marketAllocation != null;
			case CargoEditorModelPackage.TRADES_ROW__LOAD_TERMINAL_VALID:
				return loadTerminalValid != LOAD_TERMINAL_VALID_EDEFAULT;
			case CargoEditorModelPackage.TRADES_ROW__DISCHARGE_TERMINAL_VALID:
				return dischargeTerminalValid != DISCHARGE_TERMINAL_VALID_EDEFAULT;
			case CargoEditorModelPackage.TRADES_ROW__GROUP:
				return GROUP_EDEFAULT == null ? group != null : !GROUP_EDEFAULT.equals(group);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case CargoEditorModelPackage.TRADES_ROW___GET_ASSIGNABLE_OBJECT:
				return getAssignableObject();
		}
		return super.eInvoke(operationID, arguments);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (primaryRecord: ");
		result.append(primaryRecord);
		result.append(", loadTerminalValid: ");
		result.append(loadTerminalValid);
		result.append(", dischargeTerminalValid: ");
		result.append(dischargeTerminalValid);
		result.append(", group: ");
		result.append(group);
		result.append(')');
		return result.toString();
	}

} //TradesRowImpl
