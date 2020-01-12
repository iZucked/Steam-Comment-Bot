/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.impl;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import com.mmxlabs.models.lng.port.Location;
import com.mmxlabs.models.lng.port.OtherIdentifiers;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.OtherNamesObject;
import com.mmxlabs.models.mmxcore.impl.NamedObjectImpl;
import java.lang.reflect.InvocationTargetException;
import java.time.ZoneId;
import java.util.Collection;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Location</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.LocationImpl#getOtherNames <em>Other Names</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.LocationImpl#getMmxId <em>Mmx Id</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.LocationImpl#getTimeZone <em>Time Zone</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.LocationImpl#getCountry <em>Country</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.LocationImpl#getLat <em>Lat</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.LocationImpl#getLon <em>Lon</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.LocationImpl#getOtherIdentifiers <em>Other Identifiers</em>}</li>
 * </ul>
 *
 * @generated
 */
public class LocationImpl extends NamedObjectImpl implements Location {
	/**
	 * The cached value of the '{@link #getOtherNames() <em>Other Names</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOtherNames()
	 * @generated
	 * @ordered
	 */
	protected EList<String> otherNames;

	/**
	 * The default value of the '{@link #getMmxId() <em>Mmx Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMmxId()
	 * @generated
	 * @ordered
	 */
	protected static final String MMX_ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getMmxId() <em>Mmx Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMmxId()
	 * @generated
	 * @ordered
	 */
	protected String mmxId = MMX_ID_EDEFAULT;

	/**
	 * The default value of the '{@link #getTimeZone() <em>Time Zone</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTimeZone()
	 * @generated
	 * @ordered
	 */
	protected static final String TIME_ZONE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTimeZone() <em>Time Zone</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTimeZone()
	 * @generated
	 * @ordered
	 */
	protected String timeZone = TIME_ZONE_EDEFAULT;

	/**
	 * The default value of the '{@link #getCountry() <em>Country</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCountry()
	 * @generated
	 * @ordered
	 */
	protected static final String COUNTRY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getCountry() <em>Country</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCountry()
	 * @generated
	 * @ordered
	 */
	protected String country = COUNTRY_EDEFAULT;

	/**
	 * The default value of the '{@link #getLat() <em>Lat</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLat()
	 * @generated
	 * @ordered
	 */
	protected static final double LAT_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getLat() <em>Lat</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLat()
	 * @generated
	 * @ordered
	 */
	protected double lat = LAT_EDEFAULT;

	/**
	 * The default value of the '{@link #getLon() <em>Lon</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLon()
	 * @generated
	 * @ordered
	 */
	protected static final double LON_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getLon() <em>Lon</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLon()
	 * @generated
	 * @ordered
	 */
	protected double lon = LON_EDEFAULT;

	/**
	 * The cached value of the '{@link #getOtherIdentifiers() <em>Other Identifiers</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOtherIdentifiers()
	 * @generated
	 * @ordered
	 */
	protected EList<OtherIdentifiers> otherIdentifiers;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected LocationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PortPackage.Literals.LOCATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<String> getOtherNames() {
		if (otherNames == null) {
			otherNames = new EDataTypeUniqueEList<String>(String.class, this, PortPackage.LOCATION__OTHER_NAMES);
		}
		return otherNames;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getMmxId() {
		return mmxId;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMmxId(String newMmxId) {
		String oldMmxId = mmxId;
		mmxId = newMmxId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.LOCATION__MMX_ID, oldMmxId, mmxId));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getTimeZone() {
		return timeZone;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setTimeZone(String newTimeZone) {
		String oldTimeZone = timeZone;
		timeZone = newTimeZone;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.LOCATION__TIME_ZONE, oldTimeZone, timeZone));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getCountry() {
		return country;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCountry(String newCountry) {
		String oldCountry = country;
		country = newCountry;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.LOCATION__COUNTRY, oldCountry, country));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getLat() {
		return lat;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLat(double newLat) {
		double oldLat = lat;
		lat = newLat;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.LOCATION__LAT, oldLat, lat));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getLon() {
		return lon;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLon(double newLon) {
		double oldLon = lon;
		lon = newLon;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.LOCATION__LON, oldLon, lon));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<OtherIdentifiers> getOtherIdentifiers() {
		if (otherIdentifiers == null) {
			otherIdentifiers = new EObjectContainmentEList<OtherIdentifiers>(OtherIdentifiers.class, this, PortPackage.LOCATION__OTHER_IDENTIFIERS);
		}
		return otherIdentifiers;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public ZoneId getZoneId() {
		String timeZone = getTimeZone();
		if (timeZone == null || timeZone.isEmpty()) {
			return ZoneId.of("UTC");
		}
		return ZoneId.of(timeZone);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public String getTempMMXID() {
//		todo replace getMMXID with this eOp
		// Temp method until mmxid is working properly
		return getName();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case PortPackage.LOCATION__OTHER_IDENTIFIERS:
				return ((InternalEList<?>)getOtherIdentifiers()).basicRemove(otherEnd, msgs);
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
			case PortPackage.LOCATION__OTHER_NAMES:
				return getOtherNames();
			case PortPackage.LOCATION__MMX_ID:
				return getMmxId();
			case PortPackage.LOCATION__TIME_ZONE:
				return getTimeZone();
			case PortPackage.LOCATION__COUNTRY:
				return getCountry();
			case PortPackage.LOCATION__LAT:
				return getLat();
			case PortPackage.LOCATION__LON:
				return getLon();
			case PortPackage.LOCATION__OTHER_IDENTIFIERS:
				return getOtherIdentifiers();
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
			case PortPackage.LOCATION__OTHER_NAMES:
				getOtherNames().clear();
				getOtherNames().addAll((Collection<? extends String>)newValue);
				return;
			case PortPackage.LOCATION__MMX_ID:
				setMmxId((String)newValue);
				return;
			case PortPackage.LOCATION__TIME_ZONE:
				setTimeZone((String)newValue);
				return;
			case PortPackage.LOCATION__COUNTRY:
				setCountry((String)newValue);
				return;
			case PortPackage.LOCATION__LAT:
				setLat((Double)newValue);
				return;
			case PortPackage.LOCATION__LON:
				setLon((Double)newValue);
				return;
			case PortPackage.LOCATION__OTHER_IDENTIFIERS:
				getOtherIdentifiers().clear();
				getOtherIdentifiers().addAll((Collection<? extends OtherIdentifiers>)newValue);
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
			case PortPackage.LOCATION__OTHER_NAMES:
				getOtherNames().clear();
				return;
			case PortPackage.LOCATION__MMX_ID:
				setMmxId(MMX_ID_EDEFAULT);
				return;
			case PortPackage.LOCATION__TIME_ZONE:
				setTimeZone(TIME_ZONE_EDEFAULT);
				return;
			case PortPackage.LOCATION__COUNTRY:
				setCountry(COUNTRY_EDEFAULT);
				return;
			case PortPackage.LOCATION__LAT:
				setLat(LAT_EDEFAULT);
				return;
			case PortPackage.LOCATION__LON:
				setLon(LON_EDEFAULT);
				return;
			case PortPackage.LOCATION__OTHER_IDENTIFIERS:
				getOtherIdentifiers().clear();
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
			case PortPackage.LOCATION__OTHER_NAMES:
				return otherNames != null && !otherNames.isEmpty();
			case PortPackage.LOCATION__MMX_ID:
				return MMX_ID_EDEFAULT == null ? mmxId != null : !MMX_ID_EDEFAULT.equals(mmxId);
			case PortPackage.LOCATION__TIME_ZONE:
				return TIME_ZONE_EDEFAULT == null ? timeZone != null : !TIME_ZONE_EDEFAULT.equals(timeZone);
			case PortPackage.LOCATION__COUNTRY:
				return COUNTRY_EDEFAULT == null ? country != null : !COUNTRY_EDEFAULT.equals(country);
			case PortPackage.LOCATION__LAT:
				return lat != LAT_EDEFAULT;
			case PortPackage.LOCATION__LON:
				return lon != LON_EDEFAULT;
			case PortPackage.LOCATION__OTHER_IDENTIFIERS:
				return otherIdentifiers != null && !otherIdentifiers.isEmpty();
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
		if (baseClass == OtherNamesObject.class) {
			switch (derivedFeatureID) {
				case PortPackage.LOCATION__OTHER_NAMES: return MMXCorePackage.OTHER_NAMES_OBJECT__OTHER_NAMES;
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
		if (baseClass == OtherNamesObject.class) {
			switch (baseFeatureID) {
				case MMXCorePackage.OTHER_NAMES_OBJECT__OTHER_NAMES: return PortPackage.LOCATION__OTHER_NAMES;
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
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case PortPackage.LOCATION___GET_ZONE_ID:
				return getZoneId();
			case PortPackage.LOCATION___GET_TEMP_MMXID:
				return getTempMMXID();
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
		result.append(" (otherNames: ");
		result.append(otherNames);
		result.append(", mmxId: ");
		result.append(mmxId);
		result.append(", timeZone: ");
		result.append(timeZone);
		result.append(", country: ");
		result.append(country);
		result.append(", lat: ");
		result.append(lat);
		result.append(", lon: ");
		result.append(lon);
		result.append(')');
		return result.toString();
	}

} // end of LocationImpl

// finish type fixing
