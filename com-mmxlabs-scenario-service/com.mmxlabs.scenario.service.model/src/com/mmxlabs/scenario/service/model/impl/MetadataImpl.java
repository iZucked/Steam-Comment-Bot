/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.impl;

import java.util.Date;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import com.mmxlabs.scenario.service.model.Metadata;
import com.mmxlabs.scenario.service.model.ScenarioServicePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Metadata</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.MetadataImpl#getCreator <em>Creator</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.MetadataImpl#getCreated <em>Created</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.MetadataImpl#getLastModified <em>Last Modified</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.MetadataImpl#getComment <em>Comment</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.MetadataImpl#getLastModifiedBy <em>Last Modified By</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.MetadataImpl#getContentType <em>Content Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MetadataImpl extends EObjectImpl implements Metadata {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MetadataImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ScenarioServicePackage.eINSTANCE.getMetadata();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected int eStaticFeatureCount() {
		return 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getCreator() {
		return (String) eGet(ScenarioServicePackage.eINSTANCE.getMetadata_Creator(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCreator(String newCreator) {
		eSet(ScenarioServicePackage.eINSTANCE.getMetadata_Creator(), newCreator);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Date getCreated() {
		return (Date) eGet(ScenarioServicePackage.eINSTANCE.getMetadata_Created(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCreated(Date newCreated) {
		eSet(ScenarioServicePackage.eINSTANCE.getMetadata_Created(), newCreated);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Date getLastModified() {
		return (Date) eGet(ScenarioServicePackage.eINSTANCE.getMetadata_LastModified(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLastModified(Date newLastModified) {
		eSet(ScenarioServicePackage.eINSTANCE.getMetadata_LastModified(), newLastModified);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getComment() {
		return (String) eGet(ScenarioServicePackage.eINSTANCE.getMetadata_Comment(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setComment(String newComment) {
		eSet(ScenarioServicePackage.eINSTANCE.getMetadata_Comment(), newComment);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getLastModifiedBy() {
		return (String) eGet(ScenarioServicePackage.eINSTANCE.getMetadata_LastModifiedBy(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLastModifiedBy(String newLastModifiedBy) {
		eSet(ScenarioServicePackage.eINSTANCE.getMetadata_LastModifiedBy(), newLastModifiedBy);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getContentType() {
		return (String) eGet(ScenarioServicePackage.eINSTANCE.getMetadata_ContentType(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setContentType(String newContentType) {
		eSet(ScenarioServicePackage.eINSTANCE.getMetadata_ContentType(), newContentType);
	}

} //MetadataImpl
