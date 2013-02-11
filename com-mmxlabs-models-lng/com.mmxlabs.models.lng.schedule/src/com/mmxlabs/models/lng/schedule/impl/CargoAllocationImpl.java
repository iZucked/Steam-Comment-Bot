/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.impl;
import java.io.Serializable;
import java.lang.Iterable;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.schedule.AdditionalData;
import com.mmxlabs.models.lng.schedule.AdditionalDataHolder;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.types.ExtraData;
import com.mmxlabs.models.lng.types.ExtraDataContainer;
import com.mmxlabs.models.lng.types.ExtraDataFormatType;
import com.mmxlabs.models.lng.types.TypesFactory;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.mmxcore.impl.MMXObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Cargo Allocation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.CargoAllocationImpl#getExtraData <em>Extra Data</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.CargoAllocationImpl#getLoadAllocation <em>Load Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.CargoAllocationImpl#getDischargeAllocation <em>Discharge Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.CargoAllocationImpl#getLoadVolume <em>Load Volume</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.CargoAllocationImpl#getDischargeVolume <em>Discharge Volume</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.CargoAllocationImpl#getInputCargo <em>Input Cargo</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.CargoAllocationImpl#getLadenLeg <em>Laden Leg</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.CargoAllocationImpl#getBallastLeg <em>Ballast Leg</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.CargoAllocationImpl#getLadenIdle <em>Laden Idle</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.CargoAllocationImpl#getBallastIdle <em>Ballast Idle</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.CargoAllocationImpl#getSequence <em>Sequence</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CargoAllocationImpl extends MMXObjectImpl implements CargoAllocation {
	/**
	 * The cached value of the '{@link #getExtraData() <em>Extra Data</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExtraData()
	 * @generated
	 * @ordered
	 */
	protected EList<ExtraData> extraData;

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
	 * The default value of the '{@link #getLoadVolume() <em>Load Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLoadVolume()
	 * @generated
	 * @ordered
	 */
	protected static final int LOAD_VOLUME_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getLoadVolume() <em>Load Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLoadVolume()
	 * @generated
	 * @ordered
	 */
	protected int loadVolume = LOAD_VOLUME_EDEFAULT;

	/**
	 * The default value of the '{@link #getDischargeVolume() <em>Discharge Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDischargeVolume()
	 * @generated
	 * @ordered
	 */
	protected static final int DISCHARGE_VOLUME_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getDischargeVolume() <em>Discharge Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDischargeVolume()
	 * @generated
	 * @ordered
	 */
	protected int dischargeVolume = DISCHARGE_VOLUME_EDEFAULT;

	/**
	 * The cached value of the '{@link #getInputCargo() <em>Input Cargo</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInputCargo()
	 * @generated
	 * @ordered
	 */
	protected Cargo inputCargo;

	/**
	 * This is true if the Input Cargo reference has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean inputCargoESet;

	/**
	 * The cached value of the '{@link #getLadenLeg() <em>Laden Leg</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLadenLeg()
	 * @generated
	 * @ordered
	 */
	protected Journey ladenLeg;

	/**
	 * The cached value of the '{@link #getBallastLeg() <em>Ballast Leg</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBallastLeg()
	 * @generated
	 * @ordered
	 */
	protected Journey ballastLeg;

	/**
	 * The cached value of the '{@link #getLadenIdle() <em>Laden Idle</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLadenIdle()
	 * @generated
	 * @ordered
	 */
	protected Idle ladenIdle;

	/**
	 * The cached value of the '{@link #getBallastIdle() <em>Ballast Idle</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBallastIdle()
	 * @generated
	 * @ordered
	 */
	protected Idle ballastIdle;

	/**
	 * The cached value of the '{@link #getSequence() <em>Sequence</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSequence()
	 * @generated
	 * @ordered
	 */
	protected Sequence sequence;

	/**
	 * This is true if the Sequence reference has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean sequenceESet;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CargoAllocationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchedulePackage.Literals.CARGO_ALLOCATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SlotAllocation getLoadAllocation() {
		if (loadAllocation != null && loadAllocation.eIsProxy()) {
			InternalEObject oldLoadAllocation = (InternalEObject)loadAllocation;
			loadAllocation = (SlotAllocation)eResolveProxy(oldLoadAllocation);
			if (loadAllocation != oldLoadAllocation) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.CARGO_ALLOCATION__LOAD_ALLOCATION, oldLoadAllocation, loadAllocation));
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
	public void setLoadAllocation(SlotAllocation newLoadAllocation) {
		SlotAllocation oldLoadAllocation = loadAllocation;
		loadAllocation = newLoadAllocation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.CARGO_ALLOCATION__LOAD_ALLOCATION, oldLoadAllocation, loadAllocation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SlotAllocation getDischargeAllocation() {
		if (dischargeAllocation != null && dischargeAllocation.eIsProxy()) {
			InternalEObject oldDischargeAllocation = (InternalEObject)dischargeAllocation;
			dischargeAllocation = (SlotAllocation)eResolveProxy(oldDischargeAllocation);
			if (dischargeAllocation != oldDischargeAllocation) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.CARGO_ALLOCATION__DISCHARGE_ALLOCATION, oldDischargeAllocation, dischargeAllocation));
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
	public void setDischargeAllocation(SlotAllocation newDischargeAllocation) {
		SlotAllocation oldDischargeAllocation = dischargeAllocation;
		dischargeAllocation = newDischargeAllocation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.CARGO_ALLOCATION__DISCHARGE_ALLOCATION, oldDischargeAllocation, dischargeAllocation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getLoadVolume() {
		return loadVolume;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLoadVolume(int newLoadVolume) {
		int oldLoadVolume = loadVolume;
		loadVolume = newLoadVolume;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.CARGO_ALLOCATION__LOAD_VOLUME, oldLoadVolume, loadVolume));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getDischargeVolume() {
		return dischargeVolume;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDischargeVolume(int newDischargeVolume) {
		int oldDischargeVolume = dischargeVolume;
		dischargeVolume = newDischargeVolume;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.CARGO_ALLOCATION__DISCHARGE_VOLUME, oldDischargeVolume, dischargeVolume));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Cargo getInputCargo() {
		if (inputCargo != null && inputCargo.eIsProxy()) {
			InternalEObject oldInputCargo = (InternalEObject)inputCargo;
			inputCargo = (Cargo)eResolveProxy(oldInputCargo);
			if (inputCargo != oldInputCargo) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.CARGO_ALLOCATION__INPUT_CARGO, oldInputCargo, inputCargo));
			}
		}
		return inputCargo;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Cargo basicGetInputCargo() {
		return inputCargo;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setInputCargo(Cargo newInputCargo) {
		Cargo oldInputCargo = inputCargo;
		inputCargo = newInputCargo;
		boolean oldInputCargoESet = inputCargoESet;
		inputCargoESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.CARGO_ALLOCATION__INPUT_CARGO, oldInputCargo, inputCargo, !oldInputCargoESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetInputCargo() {
		Cargo oldInputCargo = inputCargo;
		boolean oldInputCargoESet = inputCargoESet;
		inputCargo = null;
		inputCargoESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, SchedulePackage.CARGO_ALLOCATION__INPUT_CARGO, oldInputCargo, null, oldInputCargoESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetInputCargo() {
		return inputCargoESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Journey getLadenLeg() {
		if (ladenLeg != null && ladenLeg.eIsProxy()) {
			InternalEObject oldLadenLeg = (InternalEObject)ladenLeg;
			ladenLeg = (Journey)eResolveProxy(oldLadenLeg);
			if (ladenLeg != oldLadenLeg) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.CARGO_ALLOCATION__LADEN_LEG, oldLadenLeg, ladenLeg));
			}
		}
		return ladenLeg;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Journey basicGetLadenLeg() {
		return ladenLeg;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLadenLeg(Journey newLadenLeg) {
		Journey oldLadenLeg = ladenLeg;
		ladenLeg = newLadenLeg;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.CARGO_ALLOCATION__LADEN_LEG, oldLadenLeg, ladenLeg));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Journey getBallastLeg() {
		if (ballastLeg != null && ballastLeg.eIsProxy()) {
			InternalEObject oldBallastLeg = (InternalEObject)ballastLeg;
			ballastLeg = (Journey)eResolveProxy(oldBallastLeg);
			if (ballastLeg != oldBallastLeg) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.CARGO_ALLOCATION__BALLAST_LEG, oldBallastLeg, ballastLeg));
			}
		}
		return ballastLeg;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Journey basicGetBallastLeg() {
		return ballastLeg;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBallastLeg(Journey newBallastLeg) {
		Journey oldBallastLeg = ballastLeg;
		ballastLeg = newBallastLeg;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.CARGO_ALLOCATION__BALLAST_LEG, oldBallastLeg, ballastLeg));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Idle getLadenIdle() {
		if (ladenIdle != null && ladenIdle.eIsProxy()) {
			InternalEObject oldLadenIdle = (InternalEObject)ladenIdle;
			ladenIdle = (Idle)eResolveProxy(oldLadenIdle);
			if (ladenIdle != oldLadenIdle) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.CARGO_ALLOCATION__LADEN_IDLE, oldLadenIdle, ladenIdle));
			}
		}
		return ladenIdle;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Idle basicGetLadenIdle() {
		return ladenIdle;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLadenIdle(Idle newLadenIdle) {
		Idle oldLadenIdle = ladenIdle;
		ladenIdle = newLadenIdle;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.CARGO_ALLOCATION__LADEN_IDLE, oldLadenIdle, ladenIdle));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Idle getBallastIdle() {
		if (ballastIdle != null && ballastIdle.eIsProxy()) {
			InternalEObject oldBallastIdle = (InternalEObject)ballastIdle;
			ballastIdle = (Idle)eResolveProxy(oldBallastIdle);
			if (ballastIdle != oldBallastIdle) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.CARGO_ALLOCATION__BALLAST_IDLE, oldBallastIdle, ballastIdle));
			}
		}
		return ballastIdle;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Idle basicGetBallastIdle() {
		return ballastIdle;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBallastIdle(Idle newBallastIdle) {
		Idle oldBallastIdle = ballastIdle;
		ballastIdle = newBallastIdle;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.CARGO_ALLOCATION__BALLAST_IDLE, oldBallastIdle, ballastIdle));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Sequence getSequence() {
		if (sequence != null && sequence.eIsProxy()) {
			InternalEObject oldSequence = (InternalEObject)sequence;
			sequence = (Sequence)eResolveProxy(oldSequence);
			if (sequence != oldSequence) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.CARGO_ALLOCATION__SEQUENCE, oldSequence, sequence));
			}
		}
		return sequence;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Sequence basicGetSequence() {
		return sequence;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSequence(Sequence newSequence) {
		Sequence oldSequence = sequence;
		sequence = newSequence;
		boolean oldSequenceESet = sequenceESet;
		sequenceESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.CARGO_ALLOCATION__SEQUENCE, oldSequence, sequence, !oldSequenceESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetSequence() {
		Sequence oldSequence = sequence;
		boolean oldSequenceESet = sequenceESet;
		sequence = null;
		sequenceESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, SchedulePackage.CARGO_ALLOCATION__SEQUENCE, oldSequence, null, oldSequenceESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetSequence() {
		return sequenceESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ExtraData> getExtraData() {
		if (extraData == null) {
			extraData = new EObjectContainmentEList<ExtraData>(ExtraData.class, this, SchedulePackage.CARGO_ALLOCATION__EXTRA_DATA);
		}
		return extraData;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public String getName() {
		if (isSetInputCargo()) {
			return getInputCargo().getName();
		} else {
			return getLoadAllocation().getName();
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public String getType() {
		if (isSetInputCargo()) {
			return getInputCargo().getCargoType().toString();
		} 
		return "Unknown";
		
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ExtraData getDataWithPath(Iterable<String> keys) {
		java.util.Iterator<String> iterator = keys.iterator();
				if (iterator.hasNext() == false) return null;
				ExtraData edc = getDataWithKey(iterator.next());
				while (edc != null && iterator.hasNext()) {
					edc = edc.getDataWithKey(iterator.next());
				}
				return edc;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ExtraData getDataWithKey(String key) {
		for (final ExtraData e : getExtraData()) {
			if (e.getKey().equals(key)) return e;
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ExtraData addExtraData(String key, String name) {
		final ExtraData result = TypesFactory.eINSTANCE.createExtraData();
		result.setKey(key);
		result.setName(name);
		getExtraData().add(result);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ExtraData addExtraData(String key, String name, Serializable value, ExtraDataFormatType format) {
		final ExtraData result = addExtraData(key, name);
		result.setValue(value);
		result.setFormatType(format);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public <T> T getValueWithPathAs(Iterable<String> path, Class<T> clazz, T defaultValue) {
		final ExtraData ed = getDataWithPath(path);
		if (ed == null) return defaultValue;
		final T value = ed.getValueAs(clazz);
		if (value == null) return defaultValue;
		return value;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchedulePackage.CARGO_ALLOCATION__EXTRA_DATA:
				return ((InternalEList<?>)getExtraData()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SchedulePackage.CARGO_ALLOCATION__EXTRA_DATA:
				return getExtraData();
			case SchedulePackage.CARGO_ALLOCATION__LOAD_ALLOCATION:
				if (resolve) return getLoadAllocation();
				return basicGetLoadAllocation();
			case SchedulePackage.CARGO_ALLOCATION__DISCHARGE_ALLOCATION:
				if (resolve) return getDischargeAllocation();
				return basicGetDischargeAllocation();
			case SchedulePackage.CARGO_ALLOCATION__LOAD_VOLUME:
				return getLoadVolume();
			case SchedulePackage.CARGO_ALLOCATION__DISCHARGE_VOLUME:
				return getDischargeVolume();
			case SchedulePackage.CARGO_ALLOCATION__INPUT_CARGO:
				if (resolve) return getInputCargo();
				return basicGetInputCargo();
			case SchedulePackage.CARGO_ALLOCATION__LADEN_LEG:
				if (resolve) return getLadenLeg();
				return basicGetLadenLeg();
			case SchedulePackage.CARGO_ALLOCATION__BALLAST_LEG:
				if (resolve) return getBallastLeg();
				return basicGetBallastLeg();
			case SchedulePackage.CARGO_ALLOCATION__LADEN_IDLE:
				if (resolve) return getLadenIdle();
				return basicGetLadenIdle();
			case SchedulePackage.CARGO_ALLOCATION__BALLAST_IDLE:
				if (resolve) return getBallastIdle();
				return basicGetBallastIdle();
			case SchedulePackage.CARGO_ALLOCATION__SEQUENCE:
				if (resolve) return getSequence();
				return basicGetSequence();
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
			case SchedulePackage.CARGO_ALLOCATION__EXTRA_DATA:
				getExtraData().clear();
				getExtraData().addAll((Collection<? extends ExtraData>)newValue);
				return;
			case SchedulePackage.CARGO_ALLOCATION__LOAD_ALLOCATION:
				setLoadAllocation((SlotAllocation)newValue);
				return;
			case SchedulePackage.CARGO_ALLOCATION__DISCHARGE_ALLOCATION:
				setDischargeAllocation((SlotAllocation)newValue);
				return;
			case SchedulePackage.CARGO_ALLOCATION__LOAD_VOLUME:
				setLoadVolume((Integer)newValue);
				return;
			case SchedulePackage.CARGO_ALLOCATION__DISCHARGE_VOLUME:
				setDischargeVolume((Integer)newValue);
				return;
			case SchedulePackage.CARGO_ALLOCATION__INPUT_CARGO:
				setInputCargo((Cargo)newValue);
				return;
			case SchedulePackage.CARGO_ALLOCATION__LADEN_LEG:
				setLadenLeg((Journey)newValue);
				return;
			case SchedulePackage.CARGO_ALLOCATION__BALLAST_LEG:
				setBallastLeg((Journey)newValue);
				return;
			case SchedulePackage.CARGO_ALLOCATION__LADEN_IDLE:
				setLadenIdle((Idle)newValue);
				return;
			case SchedulePackage.CARGO_ALLOCATION__BALLAST_IDLE:
				setBallastIdle((Idle)newValue);
				return;
			case SchedulePackage.CARGO_ALLOCATION__SEQUENCE:
				setSequence((Sequence)newValue);
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
			case SchedulePackage.CARGO_ALLOCATION__EXTRA_DATA:
				getExtraData().clear();
				return;
			case SchedulePackage.CARGO_ALLOCATION__LOAD_ALLOCATION:
				setLoadAllocation((SlotAllocation)null);
				return;
			case SchedulePackage.CARGO_ALLOCATION__DISCHARGE_ALLOCATION:
				setDischargeAllocation((SlotAllocation)null);
				return;
			case SchedulePackage.CARGO_ALLOCATION__LOAD_VOLUME:
				setLoadVolume(LOAD_VOLUME_EDEFAULT);
				return;
			case SchedulePackage.CARGO_ALLOCATION__DISCHARGE_VOLUME:
				setDischargeVolume(DISCHARGE_VOLUME_EDEFAULT);
				return;
			case SchedulePackage.CARGO_ALLOCATION__INPUT_CARGO:
				unsetInputCargo();
				return;
			case SchedulePackage.CARGO_ALLOCATION__LADEN_LEG:
				setLadenLeg((Journey)null);
				return;
			case SchedulePackage.CARGO_ALLOCATION__BALLAST_LEG:
				setBallastLeg((Journey)null);
				return;
			case SchedulePackage.CARGO_ALLOCATION__LADEN_IDLE:
				setLadenIdle((Idle)null);
				return;
			case SchedulePackage.CARGO_ALLOCATION__BALLAST_IDLE:
				setBallastIdle((Idle)null);
				return;
			case SchedulePackage.CARGO_ALLOCATION__SEQUENCE:
				unsetSequence();
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
			case SchedulePackage.CARGO_ALLOCATION__EXTRA_DATA:
				return extraData != null && !extraData.isEmpty();
			case SchedulePackage.CARGO_ALLOCATION__LOAD_ALLOCATION:
				return loadAllocation != null;
			case SchedulePackage.CARGO_ALLOCATION__DISCHARGE_ALLOCATION:
				return dischargeAllocation != null;
			case SchedulePackage.CARGO_ALLOCATION__LOAD_VOLUME:
				return loadVolume != LOAD_VOLUME_EDEFAULT;
			case SchedulePackage.CARGO_ALLOCATION__DISCHARGE_VOLUME:
				return dischargeVolume != DISCHARGE_VOLUME_EDEFAULT;
			case SchedulePackage.CARGO_ALLOCATION__INPUT_CARGO:
				return isSetInputCargo();
			case SchedulePackage.CARGO_ALLOCATION__LADEN_LEG:
				return ladenLeg != null;
			case SchedulePackage.CARGO_ALLOCATION__BALLAST_LEG:
				return ballastLeg != null;
			case SchedulePackage.CARGO_ALLOCATION__LADEN_IDLE:
				return ladenIdle != null;
			case SchedulePackage.CARGO_ALLOCATION__BALLAST_IDLE:
				return ballastIdle != null;
			case SchedulePackage.CARGO_ALLOCATION__SEQUENCE:
				return isSetSequence();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
		if (baseClass == ExtraDataContainer.class) {
			switch (derivedFeatureID) {
				case SchedulePackage.CARGO_ALLOCATION__EXTRA_DATA: return TypesPackage.EXTRA_DATA_CONTAINER__EXTRA_DATA;
				default: return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
		if (baseClass == ExtraDataContainer.class) {
			switch (baseFeatureID) {
				case TypesPackage.EXTRA_DATA_CONTAINER__EXTRA_DATA: return SchedulePackage.CARGO_ALLOCATION__EXTRA_DATA;
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedOperationID(int baseOperationID, Class<?> baseClass) {
		if (baseClass == ExtraDataContainer.class) {
			switch (baseOperationID) {
				case TypesPackage.EXTRA_DATA_CONTAINER___GET_DATA_WITH_PATH__ITERABLE: return SchedulePackage.CARGO_ALLOCATION___GET_DATA_WITH_PATH__ITERABLE;
				case TypesPackage.EXTRA_DATA_CONTAINER___GET_DATA_WITH_KEY__STRING: return SchedulePackage.CARGO_ALLOCATION___GET_DATA_WITH_KEY__STRING;
				case TypesPackage.EXTRA_DATA_CONTAINER___ADD_EXTRA_DATA__STRING_STRING: return SchedulePackage.CARGO_ALLOCATION___ADD_EXTRA_DATA__STRING_STRING;
				case TypesPackage.EXTRA_DATA_CONTAINER___ADD_EXTRA_DATA__STRING_STRING_SERIALIZABLE_EXTRADATAFORMATTYPE: return SchedulePackage.CARGO_ALLOCATION___ADD_EXTRA_DATA__STRING_STRING_SERIALIZABLE_EXTRADATAFORMATTYPE;
				case TypesPackage.EXTRA_DATA_CONTAINER___GET_VALUE_WITH_PATH_AS__ITERABLE_CLASS_OBJECT: return SchedulePackage.CARGO_ALLOCATION___GET_VALUE_WITH_PATH_AS__ITERABLE_CLASS_OBJECT;
				default: return -1;
			}
		}
		return super.eDerivedOperationID(baseOperationID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case SchedulePackage.CARGO_ALLOCATION___GET_NAME:
				return getName();
			case SchedulePackage.CARGO_ALLOCATION___GET_TYPE:
				return getType();
			case SchedulePackage.CARGO_ALLOCATION___GET_DATA_WITH_PATH__ITERABLE:
				return getDataWithPath((Iterable<String>)arguments.get(0));
			case SchedulePackage.CARGO_ALLOCATION___GET_DATA_WITH_KEY__STRING:
				return getDataWithKey((String)arguments.get(0));
			case SchedulePackage.CARGO_ALLOCATION___ADD_EXTRA_DATA__STRING_STRING:
				return addExtraData((String)arguments.get(0), (String)arguments.get(1));
			case SchedulePackage.CARGO_ALLOCATION___ADD_EXTRA_DATA__STRING_STRING_SERIALIZABLE_EXTRADATAFORMATTYPE:
				return addExtraData((String)arguments.get(0), (String)arguments.get(1), (Serializable)arguments.get(2), (ExtraDataFormatType)arguments.get(3));
			case SchedulePackage.CARGO_ALLOCATION___GET_VALUE_WITH_PATH_AS__ITERABLE_CLASS_OBJECT:
				return getValueWithPathAs((Iterable<String>)arguments.get(0), (Class)arguments.get(1), arguments.get(2));
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

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (loadVolume: ");
		result.append(loadVolume);
		result.append(", dischargeVolume: ");
		result.append(dischargeVolume);
		result.append(')');
		return result.toString();
	}

} // end of CargoAllocationImpl

// finish type fixing
