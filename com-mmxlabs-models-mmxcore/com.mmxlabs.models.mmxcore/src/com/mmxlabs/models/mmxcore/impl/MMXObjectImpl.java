/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.mmxcore.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.models.mmxcore.MMXCoreFactory;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXObject;
import com.mmxlabs.models.mmxcore.MMXProxy;
import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>MMX Object</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.mmxcore.impl.MMXObjectImpl#getExtensions <em>Extensions</em>}</li>
 *   <li>{@link com.mmxlabs.models.mmxcore.impl.MMXObjectImpl#getProxies <em>Proxies</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MMXObjectImpl extends EObjectImpl implements MMXObject {
	/**
	 * The cached value of the '{@link #getExtensions() <em>Extensions</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExtensions()
	 * @generated
	 * @ordered
	 */
	protected EList<UUIDObject> extensions;

	/**
	 * The cached value of the '{@link #getProxies() <em>Proxies</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProxies()
	 * @generated
	 * @ordered
	 */
	protected EList<MMXProxy> proxies;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MMXObjectImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MMXCorePackage.Literals.MMX_OBJECT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<UUIDObject> getExtensions() {
		if (extensions == null) {
			extensions = new EObjectResolvingEList<UUIDObject>(UUIDObject.class, this, MMXCorePackage.MMX_OBJECT__EXTENSIONS);
		}
		return extensions;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<MMXProxy> getProxies() {
		if (proxies == null) {
			proxies = new EObjectContainmentEList<MMXProxy>(MMXProxy.class, this, MMXCorePackage.MMX_OBJECT__PROXIES);
		}
		return proxies;
	}

	/**
	 * This method examines all references in this object; any non-containment reference whose referent is in another resource
	 * will be converted into a proxy, and the reference itself will be cleared to null.
	 * 
	 * The method is then applied recursively to all suitable contents
	 * 
	 * @generated NO
	 */
	public void makeProxies() {
		for (final EReference ref : eClass().getEAllReferences()) {
			if (!ref.isContainment()) {
				if (ref.isMany()) {
					@SuppressWarnings("unchecked")
					final List<? extends EObject> value = (List<? extends EObject>) eGet(ref);
					final Iterator<? extends EObject> iter = value.iterator();
					int index = 0;
					while (iter.hasNext()) {
						if (installProxy(ref, iter.next(), index++)) {
							iter.remove();
						}
					}
				} else {
					if (installProxy(ref, (EObject) eGet(ref), 0)) {
						eSet(ref, null);
					}
				}
			}
		}

		for (final EObject o : eContents()) {
			if (o instanceof MMXObject) {
				((MMXObject) o).makeProxies();
			}
		}
	}

	/**
	 * Create a proxy for the given ref/value pair, if needed
	 * @param ref
	 * @param referent
	 * @param index
	 * @return true if a proxy was created, false if no proxy is needed / possible.
	 */
	private boolean installProxy(final EReference ref, final EObject referent, int index) {
		if (!(referent instanceof UUIDObject)) return false;
		if (referent.eResource() == eResource()) return false;
		if (ref.isTransient()) return false;
		
		final UUIDObject u = (UUIDObject) referent;
		
		final MMXProxy proxy = MMXCoreFactory.eINSTANCE.createMMXProxy();
		proxy.setReference(ref);
		proxy.setResolvedReferent(u);
		proxy.setReferentID(u.getUuid());
		if (referent.eResource() != null) {
			proxy.setReferentOwner(referent.eResource().getURI().toString());
		}
		proxy.setIndex(index);
		
		getProxies().add(proxy);
		
		return true;
	}
	
	/**
	 * Looks up any proxies that are present in the given map, recursively over contents.
	 * @generated NO
	 */
	public void resolveProxies(final Map<String, UUIDObject> objectsByUUID) {
		final Iterator<MMXProxy> iterator = getProxies().iterator();
		while (iterator.hasNext()) {
			final MMXProxy p = iterator.next();
			p.setResolvedReferent(objectsByUUID.get(p.getReferentID()));
		}
		
		for (final EObject o : eContents()) {
			if (o instanceof MMXObject) ((MMXObject) o).resolveProxies(objectsByUUID);
		}
	}

	/**
	 * This should usually be called either after {@link #makeProxies()} or {@link #resolveProxies(Map)}.
	 * 
	 * It will set the references for any refs that have a proxy, and then delete their proxies, on this and contents.
	 * @generated NO
	 */
	public void restoreProxies() {
		final Iterator<MMXProxy> iterator = getProxies().iterator();
		while (iterator.hasNext()) {
			final MMXProxy p = iterator.next();
			if (p.getResolvedReferent() == null) {
				continue;
			}
			final UUIDObject referent = p.getResolvedReferent();
			final EReference reference = p.getReference();
			if (!reference.isTransient()) {
				if (reference.isMany()) {
					@SuppressWarnings("unchecked")
					final List<EObject> values = (List<EObject>) eGet(reference);
					values.add(p.getIndex(), referent);
				} else {
					eSet(reference, referent);
				}
			}
			iterator.remove();
		}

		for (final EObject o : eContents()) {
			if (o instanceof MMXObject) {
				((MMXObject) o).restoreProxies();
			}
		}
	}

	/**
	 * Return all the {@link UUIDObject} implementors contained in this object or any of its children, including this object itself
	 * @generated NO
	 */
	public void collectUUIDObjects(final Map<String, UUIDObject> objectsByUUID) {
		for (final EObject o : eContents()) {
			if (o instanceof MMXObject) {
				((MMXObject) o).collectUUIDObjects(objectsByUUID);
			} else {
				collectUUIDObjectsFor(objectsByUUID, o);
			}
		}
	}
	
	/**
	 * A helper method for collecting UUID objects even in children which are not themselves MMXObjects
	 * @param objectsByUUID
	 * @param object
	 * @since 2.2
	 */
	public void collectUUIDObjectsFor(final Map<String, UUIDObject> objectsByUUID, final EObject object) {
		for (final EObject o : object.eContents()) {
			if (o instanceof MMXObject) {
				((MMXObject) o).collectUUIDObjects(objectsByUUID);
			} else {
				collectUUIDObjectsFor(objectsByUUID, o);
			}
		}
	}
	
	/**
	 * This is equivalent to
	 * <code>
	 * HashMap m = new HashMap();
	 * mmxobject.collectUUIDObjects(m);
	 * </code>
	 * 
	 * See {@link #collectUUIDObjects(Map)} for more details.
	 * 
	 * @generated NO
	 */
	public Map<String, UUIDObject> collectUUIDObjects() {
		final HashMap<String, UUIDObject> collection = new HashMap<String, UUIDObject>();
		collectUUIDObjects(collection);
		return collection;
	}

	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NO
	 * @since 3.1
	 */
	public DelegateInformation getUnsetValueOrDelegate(EStructuralFeature feature) {
		return new DelegateInformation(null, null, eGet(feature));
	}

	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NO
	 */
	public Object getUnsetValue(EStructuralFeature feature) {
		DelegateInformation dfi = getUnsetValueOrDelegate(feature);
		if (dfi != null) {
			if (dfi.delegate != null) {
				MMXObject delegate = (MMXObject) eGet(dfi.delegate);
				if (delegate != null)
					return delegate.eGet(dfi.delegateFeature);
			}
			
			return dfi.absentDelegateValue;
		}
		else {
			return eGet(feature);
		}
	}


	/**
	 * <!-- begin-user-doc -->
	 * For unsettable values with a default in another object, this method attempts to return the local instance value if present, otherwise the default object.
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public Object eGetWithDefault(EStructuralFeature feature) {
		
		if (feature.isUnsettable() && !eIsSet(feature)) {
			return getUnsetValue(feature);
		} else {
			return eGet(feature);
		}
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EObject eContainerOp() {
		return eContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case MMXCorePackage.MMX_OBJECT__PROXIES:
				return ((InternalEList<?>)getProxies()).basicRemove(otherEnd, msgs);
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
			case MMXCorePackage.MMX_OBJECT__EXTENSIONS:
				return getExtensions();
			case MMXCorePackage.MMX_OBJECT__PROXIES:
				return getProxies();
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
			case MMXCorePackage.MMX_OBJECT__EXTENSIONS:
				getExtensions().clear();
				getExtensions().addAll((Collection<? extends UUIDObject>)newValue);
				return;
			case MMXCorePackage.MMX_OBJECT__PROXIES:
				getProxies().clear();
				getProxies().addAll((Collection<? extends MMXProxy>)newValue);
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
			case MMXCorePackage.MMX_OBJECT__EXTENSIONS:
				getExtensions().clear();
				return;
			case MMXCorePackage.MMX_OBJECT__PROXIES:
				getProxies().clear();
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
			case MMXCorePackage.MMX_OBJECT__EXTENSIONS:
				return extensions != null && !extensions.isEmpty();
			case MMXCorePackage.MMX_OBJECT__PROXIES:
				return proxies != null && !proxies.isEmpty();
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
			case MMXCorePackage.MMX_OBJECT___MAKE_PROXIES:
				makeProxies();
				return null;
			case MMXCorePackage.MMX_OBJECT___RESOLVE_PROXIES__MAP:
				resolveProxies((Map<String, UUIDObject>)arguments.get(0));
				return null;
			case MMXCorePackage.MMX_OBJECT___RESTORE_PROXIES:
				restoreProxies();
				return null;
			case MMXCorePackage.MMX_OBJECT___COLLECT_UUID_OBJECTS__MAP:
				collectUUIDObjects((Map<String, UUIDObject>)arguments.get(0));
				return null;
			case MMXCorePackage.MMX_OBJECT___COLLECT_UUID_OBJECTS:
				return collectUUIDObjects();
			case MMXCorePackage.MMX_OBJECT___GET_UNSET_VALUE__ESTRUCTURALFEATURE:
				return getUnsetValue((EStructuralFeature)arguments.get(0));
			case MMXCorePackage.MMX_OBJECT___EGET_WITH_DEFAULT__ESTRUCTURALFEATURE:
				return eGetWithDefault((EStructuralFeature)arguments.get(0));
			case MMXCorePackage.MMX_OBJECT___ECONTAINER_OP:
				return eContainerOp();
		}
		return super.eInvoke(operationID, arguments);
	}

} //MMXObjectImpl
