/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.util.importer.impl;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.datetime.DateTimePackage;
import com.mmxlabs.models.datetime.importers.LocalDateAttributeImporter;
import com.mmxlabs.models.datetime.importers.LocalDateTimeAttributeImporter;
import com.mmxlabs.models.datetime.importers.YearMonthAttributeImporter;
import com.mmxlabs.models.util.importer.IAttributeImporter;
import com.mmxlabs.models.util.importer.IMMXExportContext;
import com.mmxlabs.models.util.importer.IMMXImportContext;

public class DefaultAttributeImporter implements IAttributeImporter {

	@Override
	public void setAttribute(@NonNull final EObject container, @NonNull final EAttribute attribute, @NonNull final String value, @NonNull final IMMXImportContext context) {
		if (attribute.isMany()) {
			final String[] values = value.split(",");
			@SuppressWarnings({ "unchecked", "rawtypes" })
			final EList<Object> eValues = (EList) container.eGet(attribute);
			for (final String v : values) {
				if (v.trim().isEmpty()) {
					continue;
				}

				// Simple (de)encoding - should expand into something more robust
				final String decodedValue = EncoderUtil.decode(v);
				assert decodedValue != null;

				eValues.add(attributeFromString(container, attribute, decodedValue, context));
			}
		} else {
			if (attribute.isUnsettable() && value.trim().isEmpty()) {
				container.eUnset(attribute);
			} else if (!value.isEmpty()) {
				final Object oValue = attributeFromString(container, attribute, value, context);
				if (oValue != null)
					container.eSet(attribute, oValue);
			}
		}
	}

	@Override
	public String writeAttribute(@NonNull final EObject container, @NonNull final EAttribute attribute, @NonNull final Object value, @NonNull final IMMXExportContext context) {
		if (attribute.isMany()) {
			final EList<?> eValues = (EList<?>) container.eGet(attribute);
			final StringBuffer result = new StringBuffer();
			boolean comma = false;
			for (final Object o : eValues) {
				if (o == null) {
					continue;
				}

				if (comma) {
					result.append(",");
				}
				comma = true;
				final String rawValue = stringFromAttribute(container, attribute, o, context);
				// Simple (de)encoding - should expand into something more robust
				final String encodedValue = EncoderUtil.encode(rawValue);

				result.append(encodedValue);
			}
			return result.toString();
		} else {
			return stringFromAttribute(container, attribute, value, context);
		}
	}

	@Nullable
	protected Object attributeFromString(@NonNull final EObject container, @NonNull final EAttribute attribute, @NonNull final String value, @NonNull final IMMXImportContext context) {
		final EDataType dt = attribute.getEAttributeType();
		assert dt != null;
		try {
			if (isNumberDataType(dt)) {
				return importNumberDataType(dt, attribute, value, context);
			} else if (isDateDataType(dt)) {
				return importDateDataType(dt, value, context);

			} else {
				return dt.getEPackage().getEFactoryInstance().createFromString(dt, value);
			}
		} catch (final Exception ex) {
			context.addProblem(context.createProblem("Error parsing value " + value + " for " + attribute.getName() + ":" + ex.toString(), true, true, true));
		}
		return null;
	}

	protected String stringFromAttribute(@NonNull final EObject container, @NonNull final EAttribute attribute, @NonNull final Object value, @NonNull final IMMXExportContext context) {
		final EDataType dt = attribute.getEAttributeType();
		assert dt != null;

		if (attribute.isUnsettable() && container.eIsSet(attribute) == false) {
			return "";
		}

		if (isNumberDataType(dt)) {
			return exportNumberDataType(dt, attribute, value, context);
		} else if (isDateDataType(dt)) {
			return exportDateDataType(dt, value, context);
		} else {
			return attribute.getEAttributeType().getEPackage().getEFactoryInstance().convertToString(attribute.getEAttributeType(), value);
		}
	}

	protected boolean isNumberDataType(@NonNull final EDataType dt) {
		if (dt == EcorePackage.Literals.EBIG_DECIMAL || dt == EcorePackage.Literals.EBIG_INTEGER || dt == EcorePackage.Literals.ESHORT || dt == EcorePackage.Literals.ESHORT_OBJECT) {
			throw new UnsupportedOperationException("Unable to handle this numerical data type: " + dt.getName());
		}

		if (dt == EcorePackage.Literals.EDOUBLE || dt == EcorePackage.Literals.EDOUBLE_OBJECT || dt == EcorePackage.Literals.EINT || dt == EcorePackage.Literals.EINTEGER_OBJECT
				|| dt == EcorePackage.Literals.EFLOAT || dt == EcorePackage.Literals.EFLOAT_OBJECT) {
			return true;
		}
		return false;
	}

	protected boolean isDateDataType(@NonNull final EDataType dt) {

		if (dt == DateTimePackage.Literals.DATE_TIME || dt == DateTimePackage.Literals.LOCAL_DATE || dt == DateTimePackage.Literals.LOCAL_DATE_TIME || dt == DateTimePackage.Literals.YEAR_MONTH) {
			return true;
		}
		return false;
	}

	protected Object importNumberDataType(@NonNull final EDataType dt, @NonNull EAttribute attribute, @NonNull final String value, @NonNull final IMMXImportContext context) throws ParseException {

		final NumberAttributeImporter nai = new NumberAttributeImporter(context.getDecimalSeparator());
		if (dt == EcorePackage.Literals.EINT || dt == EcorePackage.Literals.EINTEGER_OBJECT) {
			return nai.stringToInt(value, attribute);
		} else if (dt == EcorePackage.Literals.EFLOAT || dt == EcorePackage.Literals.EFLOAT_OBJECT) {
			return nai.stringToFloat(value, attribute);
		} else if (dt == EcorePackage.Literals.EDOUBLE || dt == EcorePackage.Literals.EDOUBLE_OBJECT) {
			return nai.stringToDouble(value, attribute);
		}

		return null;
	}

	protected String exportNumberDataType(@NonNull final EDataType dt, @NonNull EAttribute attribute, @NonNull final Object value, @NonNull final IMMXExportContext context) {

		final NumberAttributeImporter nai = new NumberAttributeImporter(context.getDecimalSeparator());
		if (dt == EcorePackage.Literals.EINT || dt == EcorePackage.Literals.EINTEGER_OBJECT) {
			return nai.intToString((Integer) value, attribute);
		} else if (dt == EcorePackage.Literals.EFLOAT || dt == EcorePackage.Literals.EFLOAT_OBJECT) {
			return nai.floatToString((Float) value, attribute);
		} else if (dt == EcorePackage.Literals.EDOUBLE || dt == EcorePackage.Literals.EDOUBLE_OBJECT) {
			return nai.doubleToString((Double) value, attribute);
		}

		return null;
	}

	protected String exportDateDataType(@NonNull final EDataType dt, @Nullable final Object value, @NonNull final IMMXExportContext context) {
		if (value == null) {
			return null;
		}
		if (dt == DateTimePackage.Literals.YEAR_MONTH) {
			return new YearMonthAttributeImporter().formatYearMonth((YearMonth) value);
		}
		if (dt == DateTimePackage.Literals.LOCAL_DATE) {
			return new LocalDateAttributeImporter().formatLocalDate((LocalDate) value);
		}
		if (dt == DateTimePackage.Literals.LOCAL_DATE_TIME) {
			return new LocalDateTimeAttributeImporter().formatLocalDateTime((LocalDateTime) value);
		}
		if (dt == DateTimePackage.Literals.DATE_TIME) {
			// return new DateTimeAttributeImporter().formatDateTime((ZonedDateTime) value);
		}
		return null;
	}

	protected Object importDateDataType(@NonNull final EDataType dt, @NonNull final String value, @NonNull final IMMXImportContext context) throws ParseException {

		if (dt == DateTimePackage.Literals.YEAR_MONTH) {
			return new YearMonthAttributeImporter().parseYearMonth(value);
		}
		if (dt == DateTimePackage.Literals.LOCAL_DATE) {
			return new LocalDateAttributeImporter().parseLocalDate(value);
		}
		if (dt == DateTimePackage.Literals.LOCAL_DATE_TIME) {
			return new LocalDateTimeAttributeImporter().parseLocalDateTime(value);
		}
		if (dt == DateTimePackage.Literals.DATE_TIME) {
			// return new DateTimeAttributeImporter().parseDateTime(value);
		}
		return null;
	}
}
