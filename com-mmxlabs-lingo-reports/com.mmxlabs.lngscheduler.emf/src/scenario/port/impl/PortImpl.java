/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.port.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;

import scenario.AnnotatedObject;
import scenario.NamedObject;
import scenario.ScenarioObject;
import scenario.ScenarioPackage;
import scenario.impl.UUIDObjectImpl;
import scenario.port.Port;
import scenario.port.PortCapability;
import scenario.port.PortPackage;
import scenario.port.PortSelection;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Port</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link scenario.port.impl.PortImpl#getName <em>Name</em>}</li>
 * <li>{@link scenario.port.impl.PortImpl#getNotes <em>Notes</em>}</li>
 * <li>{@link scenario.port.impl.PortImpl#getTimeZone <em>Time Zone</em>}</li>
 * <li>{@link scenario.port.impl.PortImpl#getDefaultCVvalue <em>Default CVvalue</em>}</li>
 * <li>{@link scenario.port.impl.PortImpl#getDefaultWindowStart <em>Default Window Start</em>}</li>
 * <li>{@link scenario.port.impl.PortImpl#getDefaultSlotDuration <em>Default Slot Duration</em>}</li>
 * <li>{@link scenario.port.impl.PortImpl#isShouldArriveCold <em>Should Arrive Cold</em>}</li>
 * <li>{@link scenario.port.impl.PortImpl#getDefaultLoadDuration <em>Default Load Duration</em>}</li>
 * <li>{@link scenario.port.impl.PortImpl#getDefaultDischargeDuration <em>Default Discharge Duration</em>}</li>
 * <li>{@link scenario.port.impl.PortImpl#getCapabilities <em>Capabilities</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class PortImpl extends UUIDObjectImpl implements Port {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

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
	 * The default value of the '{@link #getTimeZone() <em>Time Zone</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getTimeZone()
	 * @generated
	 * @ordered
	 */
	protected static final String TIME_ZONE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTimeZone() <em>Time Zone</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getTimeZone()
	 * @generated
	 * @ordered
	 */
	protected String timeZone = TIME_ZONE_EDEFAULT;

	/**
	 * The default value of the '{@link #getDefaultCVvalue() <em>Default CVvalue</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getDefaultCVvalue()
	 * @generated
	 * @ordered
	 */
	protected static final float DEFAULT_CVVALUE_EDEFAULT = 22.8F;

	/**
	 * The cached value of the '{@link #getDefaultCVvalue() <em>Default CVvalue</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getDefaultCVvalue()
	 * @generated
	 * @ordered
	 */
	protected float defaultCVvalue = DEFAULT_CVVALUE_EDEFAULT;

	/**
	 * The default value of the '{@link #getDefaultWindowStart() <em>Default Window Start</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getDefaultWindowStart()
	 * @generated
	 * @ordered
	 */
	protected static final int DEFAULT_WINDOW_START_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getDefaultWindowStart() <em>Default Window Start</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getDefaultWindowStart()
	 * @generated
	 * @ordered
	 */
	protected int defaultWindowStart = DEFAULT_WINDOW_START_EDEFAULT;

	/**
	 * The default value of the '{@link #getDefaultSlotDuration() <em>Default Slot Duration</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getDefaultSlotDuration()
	 * @generated
	 * @ordered
	 */
	protected static final int DEFAULT_SLOT_DURATION_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getDefaultSlotDuration() <em>Default Slot Duration</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getDefaultSlotDuration()
	 * @generated
	 * @ordered
	 */
	protected int defaultSlotDuration = DEFAULT_SLOT_DURATION_EDEFAULT;

	/**
	 * The default value of the '{@link #isShouldArriveCold() <em>Should Arrive Cold</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isShouldArriveCold()
	 * @generated
	 * @ordered
	 */
	protected static final boolean SHOULD_ARRIVE_COLD_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isShouldArriveCold() <em>Should Arrive Cold</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isShouldArriveCold()
	 * @generated
	 * @ordered
	 */
	protected boolean shouldArriveCold = SHOULD_ARRIVE_COLD_EDEFAULT;

	/**
	 * The default value of the '{@link #getDefaultLoadDuration() <em>Default Load Duration</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getDefaultLoadDuration()
	 * @generated
	 * @ordered
	 */
	protected static final int DEFAULT_LOAD_DURATION_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getDefaultLoadDuration() <em>Default Load Duration</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getDefaultLoadDuration()
	 * @generated
	 * @ordered
	 */
	protected int defaultLoadDuration = DEFAULT_LOAD_DURATION_EDEFAULT;

	/**
	 * The default value of the '{@link #getDefaultDischargeDuration() <em>Default Discharge Duration</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getDefaultDischargeDuration()
	 * @generated
	 * @ordered
	 */
	protected static final int DEFAULT_DISCHARGE_DURATION_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getDefaultDischargeDuration() <em>Default Discharge Duration</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getDefaultDischargeDuration()
	 * @generated
	 * @ordered
	 */
	protected int defaultDischargeDuration = DEFAULT_DISCHARGE_DURATION_EDEFAULT;

	/**
	 * The cached value of the '{@link #getCapabilities() <em>Capabilities</em>}' attribute list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getCapabilities()
	 * @generated
	 * @ordered
	 */
	protected EList<PortCapability> capabilities;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected PortImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PortPackage.Literals.PORT;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setName(final String newName) {
		final String oldName = name;
		name = newName;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.PORT__NAME, oldName, name));
		}
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
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.PORT__NOTES, oldNotes, notes));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EObject getContainer() {
		return eContainer();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String getTimeZone() {
		return timeZone;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setTimeZone(final String newTimeZone) {
		final String oldTimeZone = timeZone;
		timeZone = newTimeZone;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.PORT__TIME_ZONE, oldTimeZone, timeZone));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public float getDefaultCVvalue() {
		return defaultCVvalue;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setDefaultCVvalue(final float newDefaultCVvalue) {
		final float oldDefaultCVvalue = defaultCVvalue;
		defaultCVvalue = newDefaultCVvalue;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.PORT__DEFAULT_CVVALUE, oldDefaultCVvalue, defaultCVvalue));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public int getDefaultWindowStart() {
		return defaultWindowStart;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setDefaultWindowStart(final int newDefaultWindowStart) {
		final int oldDefaultWindowStart = defaultWindowStart;
		defaultWindowStart = newDefaultWindowStart;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.PORT__DEFAULT_WINDOW_START, oldDefaultWindowStart, defaultWindowStart));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public int getDefaultSlotDuration() {
		return defaultSlotDuration;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setDefaultSlotDuration(final int newDefaultSlotDuration) {
		final int oldDefaultSlotDuration = defaultSlotDuration;
		defaultSlotDuration = newDefaultSlotDuration;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.PORT__DEFAULT_SLOT_DURATION, oldDefaultSlotDuration, defaultSlotDuration));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean isShouldArriveCold() {
		return shouldArriveCold;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setShouldArriveCold(final boolean newShouldArriveCold) {
		final boolean oldShouldArriveCold = shouldArriveCold;
		shouldArriveCold = newShouldArriveCold;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.PORT__SHOULD_ARRIVE_COLD, oldShouldArriveCold, shouldArriveCold));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public int getDefaultLoadDuration() {
		return defaultLoadDuration;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setDefaultLoadDuration(final int newDefaultLoadDuration) {
		final int oldDefaultLoadDuration = defaultLoadDuration;
		defaultLoadDuration = newDefaultLoadDuration;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.PORT__DEFAULT_LOAD_DURATION, oldDefaultLoadDuration, defaultLoadDuration));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public int getDefaultDischargeDuration() {
		return defaultDischargeDuration;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setDefaultDischargeDuration(final int newDefaultDischargeDuration) {
		final int oldDefaultDischargeDuration = defaultDischargeDuration;
		defaultDischargeDuration = newDefaultDischargeDuration;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.PORT__DEFAULT_DISCHARGE_DURATION, oldDefaultDischargeDuration, defaultDischargeDuration));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EList<PortCapability> getCapabilities() {
		if (capabilities == null) {
			capabilities = new EDataTypeUniqueEList<PortCapability>(PortCapability.class, this, PortPackage.PORT__CAPABILITIES);
		}
		return capabilities;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EList<Port> getClosure(final EList<PortSelection> ignoreSelections) {
		if (ignoreSelections.contains(this)) {
			return org.eclipse.emf.common.util.ECollections.emptyEList();
		} else {
			ignoreSelections.add(this);
			return (EList<Port>) org.eclipse.emf.common.util.ECollections.singletonEList((Port) this);
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(final int featureID, final boolean resolve, final boolean coreType) {
		switch (featureID) {
		case PortPackage.PORT__NAME:
			return getName();
		case PortPackage.PORT__NOTES:
			return getNotes();
		case PortPackage.PORT__TIME_ZONE:
			return getTimeZone();
		case PortPackage.PORT__DEFAULT_CVVALUE:
			return getDefaultCVvalue();
		case PortPackage.PORT__DEFAULT_WINDOW_START:
			return getDefaultWindowStart();
		case PortPackage.PORT__DEFAULT_SLOT_DURATION:
			return getDefaultSlotDuration();
		case PortPackage.PORT__SHOULD_ARRIVE_COLD:
			return isShouldArriveCold();
		case PortPackage.PORT__DEFAULT_LOAD_DURATION:
			return getDefaultLoadDuration();
		case PortPackage.PORT__DEFAULT_DISCHARGE_DURATION:
			return getDefaultDischargeDuration();
		case PortPackage.PORT__CAPABILITIES:
			return getCapabilities();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(final int featureID, final Object newValue) {
		switch (featureID) {
		case PortPackage.PORT__NAME:
			setName((String) newValue);
			return;
		case PortPackage.PORT__NOTES:
			setNotes((String) newValue);
			return;
		case PortPackage.PORT__TIME_ZONE:
			setTimeZone((String) newValue);
			return;
		case PortPackage.PORT__DEFAULT_CVVALUE:
			setDefaultCVvalue((Float) newValue);
			return;
		case PortPackage.PORT__DEFAULT_WINDOW_START:
			setDefaultWindowStart((Integer) newValue);
			return;
		case PortPackage.PORT__DEFAULT_SLOT_DURATION:
			setDefaultSlotDuration((Integer) newValue);
			return;
		case PortPackage.PORT__SHOULD_ARRIVE_COLD:
			setShouldArriveCold((Boolean) newValue);
			return;
		case PortPackage.PORT__DEFAULT_LOAD_DURATION:
			setDefaultLoadDuration((Integer) newValue);
			return;
		case PortPackage.PORT__DEFAULT_DISCHARGE_DURATION:
			setDefaultDischargeDuration((Integer) newValue);
			return;
		case PortPackage.PORT__CAPABILITIES:
			getCapabilities().clear();
			getCapabilities().addAll((Collection<? extends PortCapability>) newValue);
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
		case PortPackage.PORT__NAME:
			setName(NAME_EDEFAULT);
			return;
		case PortPackage.PORT__NOTES:
			setNotes(NOTES_EDEFAULT);
			return;
		case PortPackage.PORT__TIME_ZONE:
			setTimeZone(TIME_ZONE_EDEFAULT);
			return;
		case PortPackage.PORT__DEFAULT_CVVALUE:
			setDefaultCVvalue(DEFAULT_CVVALUE_EDEFAULT);
			return;
		case PortPackage.PORT__DEFAULT_WINDOW_START:
			setDefaultWindowStart(DEFAULT_WINDOW_START_EDEFAULT);
			return;
		case PortPackage.PORT__DEFAULT_SLOT_DURATION:
			setDefaultSlotDuration(DEFAULT_SLOT_DURATION_EDEFAULT);
			return;
		case PortPackage.PORT__SHOULD_ARRIVE_COLD:
			setShouldArriveCold(SHOULD_ARRIVE_COLD_EDEFAULT);
			return;
		case PortPackage.PORT__DEFAULT_LOAD_DURATION:
			setDefaultLoadDuration(DEFAULT_LOAD_DURATION_EDEFAULT);
			return;
		case PortPackage.PORT__DEFAULT_DISCHARGE_DURATION:
			setDefaultDischargeDuration(DEFAULT_DISCHARGE_DURATION_EDEFAULT);
			return;
		case PortPackage.PORT__CAPABILITIES:
			getCapabilities().clear();
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
		case PortPackage.PORT__NAME:
			return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
		case PortPackage.PORT__NOTES:
			return NOTES_EDEFAULT == null ? notes != null : !NOTES_EDEFAULT.equals(notes);
		case PortPackage.PORT__TIME_ZONE:
			return TIME_ZONE_EDEFAULT == null ? timeZone != null : !TIME_ZONE_EDEFAULT.equals(timeZone);
		case PortPackage.PORT__DEFAULT_CVVALUE:
			return defaultCVvalue != DEFAULT_CVVALUE_EDEFAULT;
		case PortPackage.PORT__DEFAULT_WINDOW_START:
			return defaultWindowStart != DEFAULT_WINDOW_START_EDEFAULT;
		case PortPackage.PORT__DEFAULT_SLOT_DURATION:
			return defaultSlotDuration != DEFAULT_SLOT_DURATION_EDEFAULT;
		case PortPackage.PORT__SHOULD_ARRIVE_COLD:
			return shouldArriveCold != SHOULD_ARRIVE_COLD_EDEFAULT;
		case PortPackage.PORT__DEFAULT_LOAD_DURATION:
			return defaultLoadDuration != DEFAULT_LOAD_DURATION_EDEFAULT;
		case PortPackage.PORT__DEFAULT_DISCHARGE_DURATION:
			return defaultDischargeDuration != DEFAULT_DISCHARGE_DURATION_EDEFAULT;
		case PortPackage.PORT__CAPABILITIES:
			return (capabilities != null) && !capabilities.isEmpty();
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
		if (baseClass == ScenarioObject.class) {
			switch (derivedFeatureID) {
			default:
				return -1;
			}
		}
		if (baseClass == NamedObject.class) {
			switch (derivedFeatureID) {
			case PortPackage.PORT__NAME:
				return ScenarioPackage.NAMED_OBJECT__NAME;
			default:
				return -1;
			}
		}
		if (baseClass == AnnotatedObject.class) {
			switch (derivedFeatureID) {
			case PortPackage.PORT__NOTES:
				return ScenarioPackage.ANNOTATED_OBJECT__NOTES;
			default:
				return -1;
			}
		}
		if (baseClass == PortSelection.class) {
			switch (derivedFeatureID) {
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
		if (baseClass == ScenarioObject.class) {
			switch (baseFeatureID) {
			default:
				return -1;
			}
		}
		if (baseClass == NamedObject.class) {
			switch (baseFeatureID) {
			case ScenarioPackage.NAMED_OBJECT__NAME:
				return PortPackage.PORT__NAME;
			default:
				return -1;
			}
		}
		if (baseClass == AnnotatedObject.class) {
			switch (baseFeatureID) {
			case ScenarioPackage.ANNOTATED_OBJECT__NOTES:
				return PortPackage.PORT__NOTES;
			default:
				return -1;
			}
		}
		if (baseClass == PortSelection.class) {
			switch (baseFeatureID) {
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
	public int eDerivedOperationID(final int baseOperationID, final Class<?> baseClass) {
		if (baseClass == ScenarioObject.class) {
			switch (baseOperationID) {
			case ScenarioPackage.SCENARIO_OBJECT___GET_CONTAINER:
				return PortPackage.PORT___GET_CONTAINER;
			default:
				return -1;
			}
		}
		if (baseClass == NamedObject.class) {
			switch (baseOperationID) {
			default:
				return -1;
			}
		}
		if (baseClass == AnnotatedObject.class) {
			switch (baseOperationID) {
			default:
				return -1;
			}
		}
		if (baseClass == PortSelection.class) {
			switch (baseOperationID) {
			case PortPackage.PORT_SELECTION___GET_CLOSURE__ELIST:
				return PortPackage.PORT___GET_CLOSURE__ELIST;
			default:
				return -1;
			}
		}
		return super.eDerivedOperationID(baseOperationID, baseClass);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Object eInvoke(final int operationID, final EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
		case PortPackage.PORT___GET_CLOSURE__ELIST:
			return getClosure((EList<PortSelection>) arguments.get(0));
		case PortPackage.PORT___GET_CONTAINER:
			return getContainer();
		}
		return super.eInvoke(operationID, arguments);
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
		result.append(" (name: ");
		result.append(name);
		result.append(", notes: ");
		result.append(notes);
		result.append(", timeZone: ");
		result.append(timeZone);
		result.append(", defaultCVvalue: ");
		result.append(defaultCVvalue);
		result.append(", defaultWindowStart: ");
		result.append(defaultWindowStart);
		result.append(", defaultSlotDuration: ");
		result.append(defaultSlotDuration);
		result.append(", shouldArriveCold: ");
		result.append(shouldArriveCold);
		result.append(", defaultLoadDuration: ");
		result.append(defaultLoadDuration);
		result.append(", defaultDischargeDuration: ");
		result.append(defaultDischargeDuration);
		result.append(", Capabilities: ");
		result.append(capabilities);
		result.append(')');
		return result.toString();
	}

} // PortImpl
