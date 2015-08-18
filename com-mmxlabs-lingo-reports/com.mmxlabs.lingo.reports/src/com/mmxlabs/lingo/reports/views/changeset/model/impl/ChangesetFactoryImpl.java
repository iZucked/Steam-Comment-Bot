/**
 */
package com.mmxlabs.lingo.reports.views.changeset.model.impl;

import com.mmxlabs.lingo.reports.views.changeset.model.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

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
			case ChangesetPackage.CHANGE_SET_ROW: return createChangeSetRow();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ChangeSetRoot createChangeSetRoot() {
		ChangeSetRootImpl changeSetRoot = new ChangeSetRootImpl();
		return changeSetRoot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ChangeSet createChangeSet() {
		ChangeSetImpl changeSet = new ChangeSetImpl();
		return changeSet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Metrics createMetrics() {
		MetricsImpl metrics = new MetricsImpl();
		return metrics;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ChangeSetRow createChangeSetRow() {
		ChangeSetRowImpl changeSetRow = new ChangeSetRowImpl();
		return changeSetRow;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
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
