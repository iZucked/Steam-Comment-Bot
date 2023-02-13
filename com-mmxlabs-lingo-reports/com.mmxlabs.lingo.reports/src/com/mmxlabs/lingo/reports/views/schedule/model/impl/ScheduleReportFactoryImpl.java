/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.lingo.reports.views.schedule.model.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import com.mmxlabs.lingo.reports.views.schedule.model.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ScheduleReportFactoryImpl extends EFactoryImpl implements ScheduleReportFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static ScheduleReportFactory init() {
		try {
			ScheduleReportFactory theScheduleReportFactory = (ScheduleReportFactory)EPackage.Registry.INSTANCE.getEFactory(ScheduleReportPackage.eNS_URI);
			if (theScheduleReportFactory != null) {
				return theScheduleReportFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new ScheduleReportFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ScheduleReportFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case ScheduleReportPackage.ROW: return createRow();
			case ScheduleReportPackage.ROW_GROUP: return createRowGroup();
			case ScheduleReportPackage.DIFF_OPTIONS: return createDiffOptions();
			case ScheduleReportPackage.COMPOSITE_ROW: return createCompositeRow();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case ScheduleReportPackage.CHANGE_TYPE:
				return createChangeTypeFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case ScheduleReportPackage.CHANGE_TYPE:
				return convertChangeTypeToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Row createRow() {
		RowImpl row = new RowImpl();
		return row;
	}
	 
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public RowGroup createRowGroup() {
		RowGroupImpl rowGroup = new RowGroupImpl();
		return rowGroup;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DiffOptions createDiffOptions() {
		DiffOptionsImpl diffOptions = new DiffOptionsImpl();
		return diffOptions;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CompositeRow createCompositeRow() {
		CompositeRowImpl compositeRow = new CompositeRowImpl();
		return compositeRow;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ChangeType createChangeTypeFromString(EDataType eDataType, String initialValue) {
		ChangeType result = ChangeType.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertChangeTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ScheduleReportPackage getScheduleReportPackage() {
		return (ScheduleReportPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static ScheduleReportPackage getPackage() {
		return ScheduleReportPackage.eINSTANCE;
	}

} //ScheduleReportFactoryImpl
