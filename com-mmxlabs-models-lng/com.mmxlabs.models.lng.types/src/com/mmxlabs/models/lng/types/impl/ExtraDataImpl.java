/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.types.impl;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import com.mmxlabs.models.lng.types.ExtraData;
import com.mmxlabs.models.lng.types.ExtraDataFormatType;
import com.mmxlabs.models.lng.types.TypesPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Extra Data</b></em>'.
 * @since 2.0
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.types.impl.ExtraDataImpl#getKey <em>Key</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.types.impl.ExtraDataImpl#getName <em>Name</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.types.impl.ExtraDataImpl#getValue <em>Value</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.types.impl.ExtraDataImpl#getFormat <em>Format</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.types.impl.ExtraDataImpl#getFormatType <em>Format Type</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ExtraDataImpl extends ExtraDataContainerImpl implements ExtraData {
	/**
	 * The default value of the '{@link #getKey() <em>Key</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKey()
	 * @generated
	 * @ordered
	 */
	protected static final String KEY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getKey() <em>Key</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKey()
	 * @generated
	 * @ordered
	 */
	protected String key = KEY_EDEFAULT;

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
	 * The default value of the '{@link #getValue() <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValue()
	 * @generated
	 * @ordered
	 */
	protected static final Serializable VALUE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getValue() <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValue()
	 * @generated
	 * @ordered
	 */
	protected Serializable value = VALUE_EDEFAULT;

	/**
	 * This is true if the Value attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean valueESet;

	/**
	 * The default value of the '{@link #getFormat() <em>Format</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFormat()
	 * @generated
	 * @ordered
	 */
	protected static final String FORMAT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getFormat() <em>Format</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFormat()
	 * @generated
	 * @ordered
	 */
	protected String format = FORMAT_EDEFAULT;

	/**
	 * The default value of the '{@link #getFormatType() <em>Format Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFormatType()
	 * @generated
	 * @ordered
	 */
	protected static final ExtraDataFormatType FORMAT_TYPE_EDEFAULT = ExtraDataFormatType.AUTO;

	/**
	 * The cached value of the '{@link #getFormatType() <em>Format Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFormatType()
	 * @generated
	 * @ordered
	 */
	protected ExtraDataFormatType formatType = FORMAT_TYPE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ExtraDataImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return TypesPackage.Literals.EXTRA_DATA;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getKey() {
		return key;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setKey(String newKey) {
		String oldKey = key;
		key = newKey;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TypesPackage.EXTRA_DATA__KEY, oldKey, key));
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
			eNotify(new ENotificationImpl(this, Notification.SET, TypesPackage.EXTRA_DATA__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Serializable getValue() {
		return value;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setValue(Serializable newValue) {
		Serializable oldValue = value;
		value = newValue;
		boolean oldValueESet = valueESet;
		valueESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TypesPackage.EXTRA_DATA__VALUE, oldValue, value, !oldValueESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetValue() {
		Serializable oldValue = value;
		boolean oldValueESet = valueESet;
		value = VALUE_EDEFAULT;
		valueESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, TypesPackage.EXTRA_DATA__VALUE, oldValue, VALUE_EDEFAULT, oldValueESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetValue() {
		return valueESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFormat(String newFormat) {
		String oldFormat = format;
		format = newFormat;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TypesPackage.EXTRA_DATA__FORMAT, oldFormat, format));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ExtraDataFormatType getFormatType() {
		return formatType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFormatType(ExtraDataFormatType newFormatType) {
		ExtraDataFormatType oldFormatType = formatType;
		formatType = newFormatType == null ? FORMAT_TYPE_EDEFAULT : newFormatType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TypesPackage.EXTRA_DATA__FORMAT_TYPE, oldFormatType, formatType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public <T> T getValueAs(Class<T> clazz) {
		if (isSetValue()) {
			if (clazz.isInstance(getValue())) {
				return clazz.cast(getValue());
			}
		}

		return null;

	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String formatValue() {
		if (isSetValue()) {
			final Object o = getValue();
			final String format = getFormat();
			ExtraDataFormatType f = getFormatType();
			if (f == ExtraDataFormatType.AUTO) {
				if (o instanceof Integer)
					f = ExtraDataFormatType.INTEGER;
				else if (o instanceof Date)
					f = ExtraDataFormatType.DATE;
			}
			switch (getFormatType()) {
			case AUTO:
				return "" + o;
			case INTEGER:
				return String.format("%,d", o);
			case DURATION:
				if (o instanceof Integer) {
					final int totalHours = (Integer) o;
					final int hrs = totalHours % 24;
					final int days = totalHours / 24;
					if (days > 0) {
						return String.format("%d:%d", days, hrs);
					} else {
						return String.format("%d", hrs);
					}
				}
				break;
			case CURRENCY:
				return String.format("$%,d", o);
			case DATE:
				DateFormat dateFormat;
				if (format != null && !format.isEmpty()) {
					dateFormat = new SimpleDateFormat(format);
				} else {
					dateFormat = DateFormat.getDateTimeInstance();
				}
				return dateFormat.format(o);
			case STRING_FORMAT:
				return String.format(format, o);
			}
		}
		return "";
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case TypesPackage.EXTRA_DATA__KEY:
			return getKey();
		case TypesPackage.EXTRA_DATA__NAME:
			return getName();
		case TypesPackage.EXTRA_DATA__VALUE:
			return getValue();
		case TypesPackage.EXTRA_DATA__FORMAT:
			return getFormat();
		case TypesPackage.EXTRA_DATA__FORMAT_TYPE:
			return getFormatType();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case TypesPackage.EXTRA_DATA__KEY:
			setKey((String) newValue);
			return;
		case TypesPackage.EXTRA_DATA__NAME:
			setName((String) newValue);
			return;
		case TypesPackage.EXTRA_DATA__VALUE:
			setValue((Serializable) newValue);
			return;
		case TypesPackage.EXTRA_DATA__FORMAT:
			setFormat((String) newValue);
			return;
		case TypesPackage.EXTRA_DATA__FORMAT_TYPE:
			setFormatType((ExtraDataFormatType) newValue);
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
		case TypesPackage.EXTRA_DATA__KEY:
			setKey(KEY_EDEFAULT);
			return;
		case TypesPackage.EXTRA_DATA__NAME:
			setName(NAME_EDEFAULT);
			return;
		case TypesPackage.EXTRA_DATA__VALUE:
			unsetValue();
			return;
		case TypesPackage.EXTRA_DATA__FORMAT:
			setFormat(FORMAT_EDEFAULT);
			return;
		case TypesPackage.EXTRA_DATA__FORMAT_TYPE:
			setFormatType(FORMAT_TYPE_EDEFAULT);
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
		case TypesPackage.EXTRA_DATA__KEY:
			return KEY_EDEFAULT == null ? key != null : !KEY_EDEFAULT.equals(key);
		case TypesPackage.EXTRA_DATA__NAME:
			return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
		case TypesPackage.EXTRA_DATA__VALUE:
			return isSetValue();
		case TypesPackage.EXTRA_DATA__FORMAT:
			return FORMAT_EDEFAULT == null ? format != null : !FORMAT_EDEFAULT.equals(format);
		case TypesPackage.EXTRA_DATA__FORMAT_TYPE:
			return formatType != FORMAT_TYPE_EDEFAULT;
		}
		return super.eIsSet(featureID);
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
		case TypesPackage.EXTRA_DATA___GET_VALUE_AS__CLASS:
			return getValueAs((Class) arguments.get(0));
		case TypesPackage.EXTRA_DATA___FORMAT_VALUE:
			return formatValue();
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
		if (eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (key: ");
		result.append(key);
		result.append(", name: ");
		result.append(name);
		result.append(", value: ");
		if (valueESet)
			result.append(value);
		else
			result.append("<unset>");
		result.append(", format: ");
		result.append(format);
		result.append(", formatType: ");
		result.append(formatType);
		result.append(')');
		return result.toString();
	}

} // end of ExtraDataImpl

// finish type fixing
