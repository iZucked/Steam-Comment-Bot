/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.port.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;

import scenario.port.Port;
import scenario.port.PortGroup;
import scenario.port.PortPackage;
import scenario.port.PortSelection;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Group</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link scenario.port.impl.PortGroupImpl#getContents <em>Contents</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class PortGroupImpl extends PortSelectionImpl implements PortGroup {
	/**
	 * The cached value of the '{@link #getContents() <em>Contents</em>}' reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getContents()
	 * @generated
	 * @ordered
	 */
	protected EList<PortSelection> contents;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected PortGroupImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PortPackage.Literals.PORT_GROUP;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EList<PortSelection> getContents() {
		if (contents == null) {
			contents = new EObjectResolvingEList<PortSelection>(PortSelection.class, this, PortPackage.PORT_GROUP__CONTENTS);
		}
		return contents;
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
		}

		final org.eclipse.emf.common.util.UniqueEList<Port> result = new org.eclipse.emf.common.util.UniqueEList<Port>();
		ignoreSelections.add(this);

		for (final PortSelection selection : getContents()) {
			result.addAll(selection.getClosure(ignoreSelections));
		}

		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(final int featureID, final boolean resolve, final boolean coreType) {
		switch (featureID) {
		case PortPackage.PORT_GROUP__CONTENTS:
			return getContents();
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
		case PortPackage.PORT_GROUP__CONTENTS:
			getContents().clear();
			getContents().addAll((Collection<? extends PortSelection>) newValue);
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
		case PortPackage.PORT_GROUP__CONTENTS:
			getContents().clear();
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
		case PortPackage.PORT_GROUP__CONTENTS:
			return (contents != null) && !contents.isEmpty();
		}
		return super.eIsSet(featureID);
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
		case PortPackage.PORT_GROUP___GET_CLOSURE__ELIST:
			return getClosure((EList<PortSelection>) arguments.get(0));
		}
		return super.eInvoke(operationID, arguments);
	}

} // PortGroupImpl
