/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo.impl;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;

import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.ITimezoneProvider;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Vessel Event</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.VesselEventImpl#getName <em>Name</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.VesselEventImpl#getSequenceHint <em>Sequence Hint</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.VesselEventImpl#getVesselAssignmentType <em>Vessel Assignment Type</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.VesselEventImpl#getSpotIndex <em>Spot Index</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.VesselEventImpl#isLocked <em>Locked</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.VesselEventImpl#getDurationInDays <em>Duration In Days</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.VesselEventImpl#getAllowedVessels <em>Allowed Vessels</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.VesselEventImpl#getPort <em>Port</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.VesselEventImpl#getStartAfter <em>Start After</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.VesselEventImpl#getStartBy <em>Start By</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class VesselEventImpl extends UUIDObjectImpl implements VesselEvent {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getSequenceHint() <em>Sequence Hint</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSequenceHint()
	 * @generated
	 * @ordered
	 */
	protected static final int SEQUENCE_HINT_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getSequenceHint() <em>Sequence Hint</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSequenceHint()
	 * @generated
	 * @ordered
	 */
	protected int sequenceHint = SEQUENCE_HINT_EDEFAULT;

	/**
	 * The cached value of the '{@link #getVesselAssignmentType() <em>Vessel Assignment Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselAssignmentType()
	 * @generated
	 * @ordered
	 */
	protected VesselAssignmentType vesselAssignmentType;

	/**
	 * The default value of the '{@link #getSpotIndex() <em>Spot Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSpotIndex()
	 * @generated
	 * @ordered
	 */
	protected static final int SPOT_INDEX_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getSpotIndex() <em>Spot Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSpotIndex()
	 * @generated
	 * @ordered
	 */
	protected int spotIndex = SPOT_INDEX_EDEFAULT;

	/**
	 * The default value of the '{@link #isLocked() <em>Locked</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isLocked()
	 * @generated
	 * @ordered
	 */
	protected static final boolean LOCKED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isLocked() <em>Locked</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isLocked()
	 * @generated
	 * @ordered
	 */
	protected boolean locked = LOCKED_EDEFAULT;

	/**
	 * The default value of the '{@link #getDurationInDays() <em>Duration In Days</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDurationInDays()
	 * @generated
	 * @ordered
	 */
	protected static final int DURATION_IN_DAYS_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getDurationInDays() <em>Duration In Days</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDurationInDays()
	 * @generated
	 * @ordered
	 */
	protected int durationInDays = DURATION_IN_DAYS_EDEFAULT;

	/**
	 * The cached value of the '{@link #getAllowedVessels() <em>Allowed Vessels</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAllowedVessels()
	 * @generated
	 * @ordered
	 */
	protected EList<AVesselSet<Vessel>> allowedVessels;

	/**
	 * The cached value of the '{@link #getPort() <em>Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPort()
	 * @generated
	 * @ordered
	 */
	protected Port port;

	/**
	 * The default value of the '{@link #getStartAfter() <em>Start After</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartAfter()
	 * @generated
	 * @ordered
	 */
	protected static final LocalDateTime START_AFTER_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getStartAfter() <em>Start After</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartAfter()
	 * @generated
	 * @ordered
	 */
	protected LocalDateTime startAfter = START_AFTER_EDEFAULT;

	/**
	 * The default value of the '{@link #getStartBy() <em>Start By</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartBy()
	 * @generated
	 * @ordered
	 */
	protected static final LocalDateTime START_BY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getStartBy() <em>Start By</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartBy()
	 * @generated
	 * @ordered
	 */
	protected LocalDateTime startBy = START_BY_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected VesselEventImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CargoPackage.Literals.VESSEL_EVENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.VESSEL_EVENT__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getSpotIndex() {
		return spotIndex;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSpotIndex(int newSpotIndex) {
		int oldSpotIndex = spotIndex;
		spotIndex = newSpotIndex;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.VESSEL_EVENT__SPOT_INDEX, oldSpotIndex, spotIndex));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getSequenceHint() {
		return sequenceHint;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSequenceHint(int newSequenceHint) {
		int oldSequenceHint = sequenceHint;
		sequenceHint = newSequenceHint;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.VESSEL_EVENT__SEQUENCE_HINT, oldSequenceHint, sequenceHint));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isLocked() {
		return locked;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLocked(boolean newLocked) {
		boolean oldLocked = locked;
		locked = newLocked;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.VESSEL_EVENT__LOCKED, oldLocked, locked));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselAssignmentType getVesselAssignmentType() {
		if (vesselAssignmentType != null && vesselAssignmentType.eIsProxy()) {
			InternalEObject oldVesselAssignmentType = (InternalEObject)vesselAssignmentType;
			vesselAssignmentType = (VesselAssignmentType)eResolveProxy(oldVesselAssignmentType);
			if (vesselAssignmentType != oldVesselAssignmentType) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoPackage.VESSEL_EVENT__VESSEL_ASSIGNMENT_TYPE, oldVesselAssignmentType, vesselAssignmentType));
			}
		}
		return vesselAssignmentType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselAssignmentType basicGetVesselAssignmentType() {
		return vesselAssignmentType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVesselAssignmentType(VesselAssignmentType newVesselAssignmentType) {
		VesselAssignmentType oldVesselAssignmentType = vesselAssignmentType;
		vesselAssignmentType = newVesselAssignmentType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.VESSEL_EVENT__VESSEL_ASSIGNMENT_TYPE, oldVesselAssignmentType, vesselAssignmentType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getDurationInDays() {
		return durationInDays;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDurationInDays(int newDurationInDays) {
		int oldDurationInDays = durationInDays;
		durationInDays = newDurationInDays;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.VESSEL_EVENT__DURATION_IN_DAYS, oldDurationInDays, durationInDays));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<AVesselSet<Vessel>> getAllowedVessels() {
		if (allowedVessels == null) {
			allowedVessels = new EObjectResolvingEList<AVesselSet<Vessel>>(AVesselSet.class, this, CargoPackage.VESSEL_EVENT__ALLOWED_VESSELS);
		}
		return allowedVessels;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port getPort() {
		if (port != null && port.eIsProxy()) {
			InternalEObject oldPort = (InternalEObject)port;
			port = (Port)eResolveProxy(oldPort);
			if (port != oldPort) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoPackage.VESSEL_EVENT__PORT, oldPort, port));
			}
		}
		return port;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port basicGetPort() {
		return port;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPort(Port newPort) {
		Port oldPort = port;
		port = newPort;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.VESSEL_EVENT__PORT, oldPort, port));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LocalDateTime getStartAfter() {
		return startAfter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStartAfter(LocalDateTime newStartAfter) {
		LocalDateTime oldStartAfter = startAfter;
		startAfter = newStartAfter;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.VESSEL_EVENT__START_AFTER, oldStartAfter, startAfter));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LocalDateTime getStartBy() {
		return startBy;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStartBy(LocalDateTime newStartBy) {
		LocalDateTime oldStartBy = startBy;
		startBy = newStartBy;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.VESSEL_EVENT__START_BY, oldStartBy, startBy));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public ZonedDateTime getStartByAsDateTime() {
		final LocalDateTime ldt = getStartBy();
		if (ldt != null) {
			return ldt.atZone(ZoneId.of(getTimeZone(CargoPackage.Literals.VESSEL_EVENT__START_BY)));
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public ZonedDateTime getStartAfterAsDateTime() {
		final LocalDateTime ldt = getStartAfter();
		if (ldt != null) {
			return ldt.atZone(ZoneId.of(getTimeZone(CargoPackage.Literals.VESSEL_EVENT__START_AFTER)));
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public String getTimeZone(EAttribute attribute) {
		if (getPort() != null) {
			final String timeZone = getPort().getTimeZone();
			if (timeZone != null && !timeZone.isEmpty()) {
				return timeZone;
			}
		}
		return "UTC";
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CargoPackage.VESSEL_EVENT__NAME:
				return getName();
			case CargoPackage.VESSEL_EVENT__SEQUENCE_HINT:
				return getSequenceHint();
			case CargoPackage.VESSEL_EVENT__VESSEL_ASSIGNMENT_TYPE:
				if (resolve) return getVesselAssignmentType();
				return basicGetVesselAssignmentType();
			case CargoPackage.VESSEL_EVENT__SPOT_INDEX:
				return getSpotIndex();
			case CargoPackage.VESSEL_EVENT__LOCKED:
				return isLocked();
			case CargoPackage.VESSEL_EVENT__DURATION_IN_DAYS:
				return getDurationInDays();
			case CargoPackage.VESSEL_EVENT__ALLOWED_VESSELS:
				return getAllowedVessels();
			case CargoPackage.VESSEL_EVENT__PORT:
				if (resolve) return getPort();
				return basicGetPort();
			case CargoPackage.VESSEL_EVENT__START_AFTER:
				return getStartAfter();
			case CargoPackage.VESSEL_EVENT__START_BY:
				return getStartBy();
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
			case CargoPackage.VESSEL_EVENT__NAME:
				setName((String)newValue);
				return;
			case CargoPackage.VESSEL_EVENT__SEQUENCE_HINT:
				setSequenceHint((Integer)newValue);
				return;
			case CargoPackage.VESSEL_EVENT__VESSEL_ASSIGNMENT_TYPE:
				setVesselAssignmentType((VesselAssignmentType)newValue);
				return;
			case CargoPackage.VESSEL_EVENT__SPOT_INDEX:
				setSpotIndex((Integer)newValue);
				return;
			case CargoPackage.VESSEL_EVENT__LOCKED:
				setLocked((Boolean)newValue);
				return;
			case CargoPackage.VESSEL_EVENT__DURATION_IN_DAYS:
				setDurationInDays((Integer)newValue);
				return;
			case CargoPackage.VESSEL_EVENT__ALLOWED_VESSELS:
				getAllowedVessels().clear();
				getAllowedVessels().addAll((Collection<? extends AVesselSet<Vessel>>)newValue);
				return;
			case CargoPackage.VESSEL_EVENT__PORT:
				setPort((Port)newValue);
				return;
			case CargoPackage.VESSEL_EVENT__START_AFTER:
				setStartAfter((LocalDateTime)newValue);
				return;
			case CargoPackage.VESSEL_EVENT__START_BY:
				setStartBy((LocalDateTime)newValue);
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
			case CargoPackage.VESSEL_EVENT__NAME:
				setName(NAME_EDEFAULT);
				return;
			case CargoPackage.VESSEL_EVENT__SEQUENCE_HINT:
				setSequenceHint(SEQUENCE_HINT_EDEFAULT);
				return;
			case CargoPackage.VESSEL_EVENT__VESSEL_ASSIGNMENT_TYPE:
				setVesselAssignmentType((VesselAssignmentType)null);
				return;
			case CargoPackage.VESSEL_EVENT__SPOT_INDEX:
				setSpotIndex(SPOT_INDEX_EDEFAULT);
				return;
			case CargoPackage.VESSEL_EVENT__LOCKED:
				setLocked(LOCKED_EDEFAULT);
				return;
			case CargoPackage.VESSEL_EVENT__DURATION_IN_DAYS:
				setDurationInDays(DURATION_IN_DAYS_EDEFAULT);
				return;
			case CargoPackage.VESSEL_EVENT__ALLOWED_VESSELS:
				getAllowedVessels().clear();
				return;
			case CargoPackage.VESSEL_EVENT__PORT:
				setPort((Port)null);
				return;
			case CargoPackage.VESSEL_EVENT__START_AFTER:
				setStartAfter(START_AFTER_EDEFAULT);
				return;
			case CargoPackage.VESSEL_EVENT__START_BY:
				setStartBy(START_BY_EDEFAULT);
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
			case CargoPackage.VESSEL_EVENT__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case CargoPackage.VESSEL_EVENT__SEQUENCE_HINT:
				return sequenceHint != SEQUENCE_HINT_EDEFAULT;
			case CargoPackage.VESSEL_EVENT__VESSEL_ASSIGNMENT_TYPE:
				return vesselAssignmentType != null;
			case CargoPackage.VESSEL_EVENT__SPOT_INDEX:
				return spotIndex != SPOT_INDEX_EDEFAULT;
			case CargoPackage.VESSEL_EVENT__LOCKED:
				return locked != LOCKED_EDEFAULT;
			case CargoPackage.VESSEL_EVENT__DURATION_IN_DAYS:
				return durationInDays != DURATION_IN_DAYS_EDEFAULT;
			case CargoPackage.VESSEL_EVENT__ALLOWED_VESSELS:
				return allowedVessels != null && !allowedVessels.isEmpty();
			case CargoPackage.VESSEL_EVENT__PORT:
				return port != null;
			case CargoPackage.VESSEL_EVENT__START_AFTER:
				return START_AFTER_EDEFAULT == null ? startAfter != null : !START_AFTER_EDEFAULT.equals(startAfter);
			case CargoPackage.VESSEL_EVENT__START_BY:
				return START_BY_EDEFAULT == null ? startBy != null : !START_BY_EDEFAULT.equals(startBy);
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
		if (baseClass == NamedObject.class) {
			switch (derivedFeatureID) {
				case CargoPackage.VESSEL_EVENT__NAME: return MMXCorePackage.NAMED_OBJECT__NAME;
				default: return -1;
			}
		}
		if (baseClass == ITimezoneProvider.class) {
			switch (derivedFeatureID) {
				default: return -1;
			}
		}
		if (baseClass == AssignableElement.class) {
			switch (derivedFeatureID) {
				case CargoPackage.VESSEL_EVENT__SEQUENCE_HINT: return CargoPackage.ASSIGNABLE_ELEMENT__SEQUENCE_HINT;
				case CargoPackage.VESSEL_EVENT__VESSEL_ASSIGNMENT_TYPE: return CargoPackage.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE;
				case CargoPackage.VESSEL_EVENT__SPOT_INDEX: return CargoPackage.ASSIGNABLE_ELEMENT__SPOT_INDEX;
				case CargoPackage.VESSEL_EVENT__LOCKED: return CargoPackage.ASSIGNABLE_ELEMENT__LOCKED;
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
		if (baseClass == NamedObject.class) {
			switch (baseFeatureID) {
				case MMXCorePackage.NAMED_OBJECT__NAME: return CargoPackage.VESSEL_EVENT__NAME;
				default: return -1;
			}
		}
		if (baseClass == ITimezoneProvider.class) {
			switch (baseFeatureID) {
				default: return -1;
			}
		}
		if (baseClass == AssignableElement.class) {
			switch (baseFeatureID) {
				case CargoPackage.ASSIGNABLE_ELEMENT__SEQUENCE_HINT: return CargoPackage.VESSEL_EVENT__SEQUENCE_HINT;
				case CargoPackage.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE: return CargoPackage.VESSEL_EVENT__VESSEL_ASSIGNMENT_TYPE;
				case CargoPackage.ASSIGNABLE_ELEMENT__SPOT_INDEX: return CargoPackage.VESSEL_EVENT__SPOT_INDEX;
				case CargoPackage.ASSIGNABLE_ELEMENT__LOCKED: return CargoPackage.VESSEL_EVENT__LOCKED;
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
		if (baseClass == NamedObject.class) {
			switch (baseOperationID) {
				default: return -1;
			}
		}
		if (baseClass == ITimezoneProvider.class) {
			switch (baseOperationID) {
				case TypesPackage.ITIMEZONE_PROVIDER___GET_TIME_ZONE__EATTRIBUTE: return CargoPackage.VESSEL_EVENT___GET_TIME_ZONE__EATTRIBUTE;
				default: return -1;
			}
		}
		if (baseClass == AssignableElement.class) {
			switch (baseOperationID) {
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
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case CargoPackage.VESSEL_EVENT___GET_START_BY_AS_DATE_TIME:
				return getStartByAsDateTime();
			case CargoPackage.VESSEL_EVENT___GET_START_AFTER_AS_DATE_TIME:
				return getStartAfterAsDateTime();
			case CargoPackage.VESSEL_EVENT___GET_TIME_ZONE__EATTRIBUTE:
				return getTimeZone((EAttribute)arguments.get(0));
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
		result.append(" (name: ");
		result.append(name);
		result.append(", sequenceHint: ");
		result.append(sequenceHint);
		result.append(", spotIndex: ");
		result.append(spotIndex);
		result.append(", locked: ");
		result.append(locked);
		result.append(", durationInDays: ");
		result.append(durationInDays);
		result.append(", startAfter: ");
		result.append(startAfter);
		result.append(", startBy: ");
		result.append(startBy);
		result.append(')');
		return result.toString();
	}

} //VesselEventImpl
