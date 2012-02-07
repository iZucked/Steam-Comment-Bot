/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.fleet.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import scenario.AnnotatedObject;
import scenario.ScenarioPackage;
import scenario.fleet.FleetPackage;
import scenario.fleet.PortAndTime;
import scenario.fleet.PortTimeAndHeel;
import scenario.fleet.Vessel;
import scenario.fleet.VesselClass;
import scenario.impl.NamedObjectImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Vessel</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link scenario.fleet.impl.VesselImpl#getNotes <em>Notes</em>}</li>
 * <li>{@link scenario.fleet.impl.VesselImpl#isTimeChartered <em>Time Chartered</em>}</li>
 * <li>{@link scenario.fleet.impl.VesselImpl#getDailyCharterOutPrice <em>Daily Charter Out Price</em>}</li>
 * <li>{@link scenario.fleet.impl.VesselImpl#getClass_ <em>Class</em>}</li>
 * <li>{@link scenario.fleet.impl.VesselImpl#getStartRequirement <em>Start Requirement</em>}</li>
 * <li>{@link scenario.fleet.impl.VesselImpl#getEndRequirement <em>End Requirement</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class VesselImpl extends NamedObjectImpl implements Vessel {
	/**
	 * The default value of the '{@link #getNotes() <em>Notes</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getNotes()
	 * @generated
	 * @ordered
	 */
	protected static final String NOTES_EDEFAULT = "";

	/**
	 * The cached value of the '{@link #getNotes() <em>Notes</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getNotes()
	 * @generated
	 * @ordered
	 */
	protected String notes = NOTES_EDEFAULT;

	/**
	 * The default value of the '{@link #isTimeChartered() <em>Time Chartered</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isTimeChartered()
	 * @generated
	 * @ordered
	 */
	protected static final boolean TIME_CHARTERED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isTimeChartered() <em>Time Chartered</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isTimeChartered()
	 * @generated
	 * @ordered
	 */
	protected boolean timeChartered = TIME_CHARTERED_EDEFAULT;

	/**
	 * The default value of the '{@link #getDailyCharterOutPrice() <em>Daily Charter Out Price</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getDailyCharterOutPrice()
	 * @generated
	 * @ordered
	 */
	protected static final int DAILY_CHARTER_OUT_PRICE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getDailyCharterOutPrice() <em>Daily Charter Out Price</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getDailyCharterOutPrice()
	 * @generated
	 * @ordered
	 */
	protected int dailyCharterOutPrice = DAILY_CHARTER_OUT_PRICE_EDEFAULT;

	/**
	 * This is true if the Daily Charter Out Price attribute has been set. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean dailyCharterOutPriceESet;

	/**
	 * The cached value of the '{@link #getClass_() <em>Class</em>}' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getClass_()
	 * @generated
	 * @ordered
	 */
	protected VesselClass class_;

	/**
	 * The cached value of the '{@link #getStartRequirement() <em>Start Requirement</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getStartRequirement()
	 * @generated
	 * @ordered
	 */
	protected PortTimeAndHeel startRequirement;

	/**
	 * The cached value of the '{@link #getEndRequirement() <em>End Requirement</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getEndRequirement()
	 * @generated
	 * @ordered
	 */
	protected PortAndTime endRequirement;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected VesselImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return FleetPackage.Literals.VESSEL;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String getNotes() {
		return notes;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setNotes(final String newNotes) {
		final String oldNotes = notes;
		notes = newNotes;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__NOTES, oldNotes, notes));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public VesselClass getClass_() {
		if ((class_ != null) && class_.eIsProxy()) {
			final InternalEObject oldClass = (InternalEObject) class_;
			class_ = (VesselClass) eResolveProxy(oldClass);
			if (class_ != oldClass) {
				if (eNotificationRequired()) {
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, FleetPackage.VESSEL__CLASS, oldClass, class_));
				}
			}
		}
		return class_;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public VesselClass basicGetClass() {
		return class_;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setClass(final VesselClass newClass) {
		final VesselClass oldClass = class_;
		class_ = newClass;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__CLASS, oldClass, class_));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public PortTimeAndHeel getStartRequirement() {
		if ((startRequirement != null) && startRequirement.eIsProxy()) {
			final InternalEObject oldStartRequirement = (InternalEObject) startRequirement;
			startRequirement = (PortTimeAndHeel) eResolveProxy(oldStartRequirement);
			if (startRequirement != oldStartRequirement) {
				final InternalEObject newStartRequirement = (InternalEObject) startRequirement;
				NotificationChain msgs = oldStartRequirement.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - FleetPackage.VESSEL__START_REQUIREMENT, null, null);
				if (newStartRequirement.eInternalContainer() == null) {
					msgs = newStartRequirement.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - FleetPackage.VESSEL__START_REQUIREMENT, null, msgs);
				}
				if (msgs != null) {
					msgs.dispatch();
				}
				if (eNotificationRequired()) {
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, FleetPackage.VESSEL__START_REQUIREMENT, oldStartRequirement, startRequirement));
				}
			}
		}
		return startRequirement;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public PortTimeAndHeel basicGetStartRequirement() {
		return startRequirement;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetStartRequirement(final PortTimeAndHeel newStartRequirement, NotificationChain msgs) {
		final PortTimeAndHeel oldStartRequirement = startRequirement;
		startRequirement = newStartRequirement;
		if (eNotificationRequired()) {
			final ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__START_REQUIREMENT, oldStartRequirement, newStartRequirement);
			if (msgs == null) {
				msgs = notification;
			} else {
				msgs.add(notification);
			}
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setStartRequirement(final PortTimeAndHeel newStartRequirement) {
		if (newStartRequirement != startRequirement) {
			NotificationChain msgs = null;
			if (startRequirement != null) {
				msgs = ((InternalEObject) startRequirement).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - FleetPackage.VESSEL__START_REQUIREMENT, null, msgs);
			}
			if (newStartRequirement != null) {
				msgs = ((InternalEObject) newStartRequirement).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - FleetPackage.VESSEL__START_REQUIREMENT, null, msgs);
			}
			msgs = basicSetStartRequirement(newStartRequirement, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__START_REQUIREMENT, newStartRequirement, newStartRequirement));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public PortAndTime getEndRequirement() {
		if ((endRequirement != null) && endRequirement.eIsProxy()) {
			final InternalEObject oldEndRequirement = (InternalEObject) endRequirement;
			endRequirement = (PortAndTime) eResolveProxy(oldEndRequirement);
			if (endRequirement != oldEndRequirement) {
				final InternalEObject newEndRequirement = (InternalEObject) endRequirement;
				NotificationChain msgs = oldEndRequirement.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - FleetPackage.VESSEL__END_REQUIREMENT, null, null);
				if (newEndRequirement.eInternalContainer() == null) {
					msgs = newEndRequirement.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - FleetPackage.VESSEL__END_REQUIREMENT, null, msgs);
				}
				if (msgs != null) {
					msgs.dispatch();
				}
				if (eNotificationRequired()) {
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, FleetPackage.VESSEL__END_REQUIREMENT, oldEndRequirement, endRequirement));
				}
			}
		}
		return endRequirement;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public PortAndTime basicGetEndRequirement() {
		return endRequirement;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetEndRequirement(final PortAndTime newEndRequirement, NotificationChain msgs) {
		final PortAndTime oldEndRequirement = endRequirement;
		endRequirement = newEndRequirement;
		if (eNotificationRequired()) {
			final ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__END_REQUIREMENT, oldEndRequirement, newEndRequirement);
			if (msgs == null) {
				msgs = notification;
			} else {
				msgs.add(notification);
			}
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setEndRequirement(final PortAndTime newEndRequirement) {
		if (newEndRequirement != endRequirement) {
			NotificationChain msgs = null;
			if (endRequirement != null) {
				msgs = ((InternalEObject) endRequirement).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - FleetPackage.VESSEL__END_REQUIREMENT, null, msgs);
			}
			if (newEndRequirement != null) {
				msgs = ((InternalEObject) newEndRequirement).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - FleetPackage.VESSEL__END_REQUIREMENT, null, msgs);
			}
			msgs = basicSetEndRequirement(newEndRequirement, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__END_REQUIREMENT, newEndRequirement, newEndRequirement));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean isTimeChartered() {
		return timeChartered;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setTimeChartered(final boolean newTimeChartered) {
		final boolean oldTimeChartered = timeChartered;
		timeChartered = newTimeChartered;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__TIME_CHARTERED, oldTimeChartered, timeChartered));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public int getDailyCharterOutPrice() {
		return dailyCharterOutPrice;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setDailyCharterOutPrice(final int newDailyCharterOutPrice) {
		final int oldDailyCharterOutPrice = dailyCharterOutPrice;
		dailyCharterOutPrice = newDailyCharterOutPrice;
		final boolean oldDailyCharterOutPriceESet = dailyCharterOutPriceESet;
		dailyCharterOutPriceESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL__DAILY_CHARTER_OUT_PRICE, oldDailyCharterOutPrice, dailyCharterOutPrice, !oldDailyCharterOutPriceESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void unsetDailyCharterOutPrice() {
		final int oldDailyCharterOutPrice = dailyCharterOutPrice;
		final boolean oldDailyCharterOutPriceESet = dailyCharterOutPriceESet;
		dailyCharterOutPrice = DAILY_CHARTER_OUT_PRICE_EDEFAULT;
		dailyCharterOutPriceESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, FleetPackage.VESSEL__DAILY_CHARTER_OUT_PRICE, oldDailyCharterOutPrice, DAILY_CHARTER_OUT_PRICE_EDEFAULT,
					oldDailyCharterOutPriceESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean isSetDailyCharterOutPrice() {
		return dailyCharterOutPriceESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(final InternalEObject otherEnd, final int featureID, final NotificationChain msgs) {
		switch (featureID) {
		case FleetPackage.VESSEL__START_REQUIREMENT:
			return basicSetStartRequirement(null, msgs);
		case FleetPackage.VESSEL__END_REQUIREMENT:
			return basicSetEndRequirement(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(final int featureID, final boolean resolve, final boolean coreType) {
		switch (featureID) {
		case FleetPackage.VESSEL__NOTES:
			return getNotes();
		case FleetPackage.VESSEL__TIME_CHARTERED:
			return isTimeChartered();
		case FleetPackage.VESSEL__DAILY_CHARTER_OUT_PRICE:
			return getDailyCharterOutPrice();
		case FleetPackage.VESSEL__CLASS:
			if (resolve) {
				return getClass_();
			}
			return basicGetClass();
		case FleetPackage.VESSEL__START_REQUIREMENT:
			if (resolve) {
				return getStartRequirement();
			}
			return basicGetStartRequirement();
		case FleetPackage.VESSEL__END_REQUIREMENT:
			if (resolve) {
				return getEndRequirement();
			}
			return basicGetEndRequirement();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eSet(final int featureID, final Object newValue) {
		switch (featureID) {
		case FleetPackage.VESSEL__NOTES:
			setNotes((String) newValue);
			return;
		case FleetPackage.VESSEL__TIME_CHARTERED:
			setTimeChartered((Boolean) newValue);
			return;
		case FleetPackage.VESSEL__DAILY_CHARTER_OUT_PRICE:
			setDailyCharterOutPrice((Integer) newValue);
			return;
		case FleetPackage.VESSEL__CLASS:
			setClass((VesselClass) newValue);
			return;
		case FleetPackage.VESSEL__START_REQUIREMENT:
			setStartRequirement((PortTimeAndHeel) newValue);
			return;
		case FleetPackage.VESSEL__END_REQUIREMENT:
			setEndRequirement((PortAndTime) newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eUnset(final int featureID) {
		switch (featureID) {
		case FleetPackage.VESSEL__NOTES:
			setNotes(NOTES_EDEFAULT);
			return;
		case FleetPackage.VESSEL__TIME_CHARTERED:
			setTimeChartered(TIME_CHARTERED_EDEFAULT);
			return;
		case FleetPackage.VESSEL__DAILY_CHARTER_OUT_PRICE:
			unsetDailyCharterOutPrice();
			return;
		case FleetPackage.VESSEL__CLASS:
			setClass((VesselClass) null);
			return;
		case FleetPackage.VESSEL__START_REQUIREMENT:
			setStartRequirement((PortTimeAndHeel) null);
			return;
		case FleetPackage.VESSEL__END_REQUIREMENT:
			setEndRequirement((PortAndTime) null);
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean eIsSet(final int featureID) {
		switch (featureID) {
		case FleetPackage.VESSEL__NOTES:
			return NOTES_EDEFAULT == null ? notes != null : !NOTES_EDEFAULT.equals(notes);
		case FleetPackage.VESSEL__TIME_CHARTERED:
			return timeChartered != TIME_CHARTERED_EDEFAULT;
		case FleetPackage.VESSEL__DAILY_CHARTER_OUT_PRICE:
			return isSetDailyCharterOutPrice();
		case FleetPackage.VESSEL__CLASS:
			return class_ != null;
		case FleetPackage.VESSEL__START_REQUIREMENT:
			return startRequirement != null;
		case FleetPackage.VESSEL__END_REQUIREMENT:
			return endRequirement != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(final int derivedFeatureID, final Class<?> baseClass) {
		if (baseClass == AnnotatedObject.class) {
			switch (derivedFeatureID) {
			case FleetPackage.VESSEL__NOTES:
				return ScenarioPackage.ANNOTATED_OBJECT__NOTES;
			default:
				return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(final int baseFeatureID, final Class<?> baseClass) {
		if (baseClass == AnnotatedObject.class) {
			switch (baseFeatureID) {
			case ScenarioPackage.ANNOTATED_OBJECT__NOTES:
				return FleetPackage.VESSEL__NOTES;
			default:
				return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) {
			return super.toString();
		}

		final StringBuffer result = new StringBuffer(super.toString());
		result.append(" (notes: ");
		result.append(notes);
		result.append(", timeChartered: ");
		result.append(timeChartered);
		result.append(", dailyCharterOutPrice: ");
		if (dailyCharterOutPriceESet) {
			result.append(dailyCharterOutPrice);
		} else {
			result.append("<unset>");
		}
		result.append(')');
		return result.toString();
	}

} // VesselImpl
