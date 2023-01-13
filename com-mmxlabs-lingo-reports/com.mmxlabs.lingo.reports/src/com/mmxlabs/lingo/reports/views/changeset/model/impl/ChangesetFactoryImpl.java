/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.lingo.reports.views.changeset.model.impl;

import com.mmxlabs.lingo.reports.views.changeset.model.*;
import com.mmxlabs.models.lng.analytics.ChangeDescription;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.scenario.service.ScenarioResult;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSet;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRoot;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetFactory;
import com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage;
import com.mmxlabs.lingo.reports.views.changeset.model.DeltaMetrics;
import com.mmxlabs.lingo.reports.views.changeset.model.Metrics;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ChangesetFactoryImpl extends EFactoryImpl implements ChangesetFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static ChangesetFactory init() {
		try {
			ChangesetFactory theChangesetFactory = (ChangesetFactory)EPackage.Registry.INSTANCE.getEFactory(ChangesetPackage.eNS_URI);
			if (theChangesetFactory != null) {
				return theChangesetFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new ChangesetFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ChangesetFactoryImpl() {
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
			case ChangesetPackage.CHANGE_SET_ROOT: return createChangeSetRoot();
			case ChangesetPackage.CHANGE_SET: return createChangeSet();
			case ChangesetPackage.METRICS: return createMetrics();
			case ChangesetPackage.DELTA_METRICS: return createDeltaMetrics();
			case ChangesetPackage.CHANGE_SET_ROW_DATA_GROUP: return createChangeSetRowDataGroup();
			case ChangesetPackage.CHANGE_SET_ROW: return createChangeSetRow();
			case ChangesetPackage.CHANGE_SET_ROW_DATA: return createChangeSetRowData();
			case ChangesetPackage.CHANGE_SET_TABLE_GROUP: return createChangeSetTableGroup();
			case ChangesetPackage.CHANGE_SET_TABLE_ROW: return createChangeSetTableRow();
			case ChangesetPackage.CHANGE_SET_TABLE_ROOT: return createChangeSetTableRoot();
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
			case ChangesetPackage.CHANGE_SET_VESSEL_TYPE:
				return createChangeSetVesselTypeFromString(eDataType, initialValue);
			case ChangesetPackage.SCENARIO_RESULT:
				return createScenarioResultFromString(eDataType, initialValue);
			case ChangesetPackage.CHANGE_DESCRIPTION:
				return createChangeDescriptionFromString(eDataType, initialValue);
			case ChangesetPackage.USER_SETTINGS:
				return createUserSettingsFromString(eDataType, initialValue);
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
			case ChangesetPackage.CHANGE_SET_VESSEL_TYPE:
				return convertChangeSetVesselTypeToString(eDataType, instanceValue);
			case ChangesetPackage.SCENARIO_RESULT:
				return convertScenarioResultToString(eDataType, instanceValue);
			case ChangesetPackage.CHANGE_DESCRIPTION:
				return convertChangeDescriptionToString(eDataType, instanceValue);
			case ChangesetPackage.USER_SETTINGS:
				return convertUserSettingsToString(eDataType, instanceValue);
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
	public ChangeSetRoot createChangeSetRoot() {
		ChangeSetRootImpl changeSetRoot = new ChangeSetRootImpl();
		return changeSetRoot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ChangeSet createChangeSet() {
		ChangeSetImpl changeSet = new ChangeSetImpl();
		return changeSet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Metrics createMetrics() {
		MetricsImpl metrics = new MetricsImpl();
		return metrics;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DeltaMetrics createDeltaMetrics() {
		DeltaMetricsImpl deltaMetrics = new DeltaMetricsImpl();
		return deltaMetrics;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ChangeSetRowDataGroup createChangeSetRowDataGroup() {
		ChangeSetRowDataGroupImpl changeSetRowDataGroup = new ChangeSetRowDataGroupImpl();
		return changeSetRowDataGroup;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ChangeSetRow createChangeSetRow() {
		ChangeSetRowImpl changeSetRow = new ChangeSetRowImpl();
		return changeSetRow;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ChangeSetRowData createChangeSetRowData() {
		ChangeSetRowDataImpl changeSetRowData = new ChangeSetRowDataImpl();
		return changeSetRowData;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ChangeSetTableGroup createChangeSetTableGroup() {
		ChangeSetTableGroupImpl changeSetTableGroup = new ChangeSetTableGroupImpl();
		return changeSetTableGroup;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ChangeSetTableRow createChangeSetTableRow() {
		ChangeSetTableRowImpl changeSetTableRow = new ChangeSetTableRowImpl();
		return changeSetTableRow;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ChangeSetTableRoot createChangeSetTableRoot() {
		ChangeSetTableRootImpl changeSetTableRoot = new ChangeSetTableRootImpl();
		return changeSetTableRoot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ChangeSetVesselType createChangeSetVesselTypeFromString(EDataType eDataType, String initialValue) {
		ChangeSetVesselType result = ChangeSetVesselType.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertChangeSetVesselTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ScenarioResult createScenarioResultFromString(EDataType eDataType, String initialValue) {
		return (ScenarioResult)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertScenarioResultToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ChangeDescription createChangeDescriptionFromString(EDataType eDataType, String initialValue) {
		return (ChangeDescription)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertChangeDescriptionToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UserSettings createUserSettingsFromString(EDataType eDataType, String initialValue) {
		return (UserSettings)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertUserSettingsToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ChangesetPackage getChangesetPackage() {
		return (ChangesetPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static ChangesetPackage getPackage() {
		return ChangesetPackage.eINSTANCE;
	}

} //ChangesetFactoryImpl
