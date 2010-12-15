/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.schedule.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import scenario.schedule.CargoAllocation;
import scenario.schedule.Schedule;
import scenario.schedule.ScheduleFitness;
import scenario.schedule.SchedulePackage;
import scenario.schedule.Sequence;
import scenario.schedule.fleetallocation.AllocatedVessel;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Schedule</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.schedule.impl.ScheduleImpl#getSequences <em>Sequences</em>}</li>
 *   <li>{@link scenario.schedule.impl.ScheduleImpl#getName <em>Name</em>}</li>
 *   <li>{@link scenario.schedule.impl.ScheduleImpl#getCargoAllocations <em>Cargo Allocations</em>}</li>
 *   <li>{@link scenario.schedule.impl.ScheduleImpl#getFleet <em>Fleet</em>}</li>
 *   <li>{@link scenario.schedule.impl.ScheduleImpl#getFitness <em>Fitness</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ScheduleImpl extends EObjectImpl implements Schedule {
	/**
	 * The cached value of the '{@link #getSequences() <em>Sequences</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSequences()
	 * @generated
	 * @ordered
	 */
	protected EList<Sequence> sequences;

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
	 * The cached value of the '{@link #getCargoAllocations() <em>Cargo Allocations</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCargoAllocations()
	 * @generated
	 * @ordered
	 */
	protected EList<CargoAllocation> cargoAllocations;

	/**
	 * The cached value of the '{@link #getFleet() <em>Fleet</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFleet()
	 * @generated
	 * @ordered
	 */
	protected EList<AllocatedVessel> fleet;

	/**
	 * The cached value of the '{@link #getFitness() <em>Fitness</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFitness()
	 * @generated
	 * @ordered
	 */
	protected EList<ScheduleFitness> fitness;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ScheduleImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchedulePackage.Literals.SCHEDULE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Sequence> getSequences() {
		if (sequences == null) {
			sequences = new EObjectContainmentEList<Sequence>(Sequence.class, this, SchedulePackage.SCHEDULE__SEQUENCES);
		}
		return sequences;
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
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.SCHEDULE__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<CargoAllocation> getCargoAllocations() {
		if (cargoAllocations == null) {
			cargoAllocations = new EObjectContainmentEList<CargoAllocation>(CargoAllocation.class, this, SchedulePackage.SCHEDULE__CARGO_ALLOCATIONS);
		}
		return cargoAllocations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<AllocatedVessel> getFleet() {
		if (fleet == null) {
			fleet = new EObjectContainmentEList<AllocatedVessel>(AllocatedVessel.class, this, SchedulePackage.SCHEDULE__FLEET);
		}
		return fleet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ScheduleFitness> getFitness() {
		if (fitness == null) {
			fitness = new EObjectContainmentEList<ScheduleFitness>(ScheduleFitness.class, this, SchedulePackage.SCHEDULE__FITNESS);
		}
		return fitness;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchedulePackage.SCHEDULE__SEQUENCES:
				return ((InternalEList<?>)getSequences()).basicRemove(otherEnd, msgs);
			case SchedulePackage.SCHEDULE__CARGO_ALLOCATIONS:
				return ((InternalEList<?>)getCargoAllocations()).basicRemove(otherEnd, msgs);
			case SchedulePackage.SCHEDULE__FLEET:
				return ((InternalEList<?>)getFleet()).basicRemove(otherEnd, msgs);
			case SchedulePackage.SCHEDULE__FITNESS:
				return ((InternalEList<?>)getFitness()).basicRemove(otherEnd, msgs);
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
			case SchedulePackage.SCHEDULE__SEQUENCES:
				return getSequences();
			case SchedulePackage.SCHEDULE__NAME:
				return getName();
			case SchedulePackage.SCHEDULE__CARGO_ALLOCATIONS:
				return getCargoAllocations();
			case SchedulePackage.SCHEDULE__FLEET:
				return getFleet();
			case SchedulePackage.SCHEDULE__FITNESS:
				return getFitness();
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
			case SchedulePackage.SCHEDULE__SEQUENCES:
				getSequences().clear();
				getSequences().addAll((Collection<? extends Sequence>)newValue);
				return;
			case SchedulePackage.SCHEDULE__NAME:
				setName((String)newValue);
				return;
			case SchedulePackage.SCHEDULE__CARGO_ALLOCATIONS:
				getCargoAllocations().clear();
				getCargoAllocations().addAll((Collection<? extends CargoAllocation>)newValue);
				return;
			case SchedulePackage.SCHEDULE__FLEET:
				getFleet().clear();
				getFleet().addAll((Collection<? extends AllocatedVessel>)newValue);
				return;
			case SchedulePackage.SCHEDULE__FITNESS:
				getFitness().clear();
				getFitness().addAll((Collection<? extends ScheduleFitness>)newValue);
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
			case SchedulePackage.SCHEDULE__SEQUENCES:
				getSequences().clear();
				return;
			case SchedulePackage.SCHEDULE__NAME:
				setName(NAME_EDEFAULT);
				return;
			case SchedulePackage.SCHEDULE__CARGO_ALLOCATIONS:
				getCargoAllocations().clear();
				return;
			case SchedulePackage.SCHEDULE__FLEET:
				getFleet().clear();
				return;
			case SchedulePackage.SCHEDULE__FITNESS:
				getFitness().clear();
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
			case SchedulePackage.SCHEDULE__SEQUENCES:
				return sequences != null && !sequences.isEmpty();
			case SchedulePackage.SCHEDULE__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case SchedulePackage.SCHEDULE__CARGO_ALLOCATIONS:
				return cargoAllocations != null && !cargoAllocations.isEmpty();
			case SchedulePackage.SCHEDULE__FLEET:
				return fleet != null && !fleet.isEmpty();
			case SchedulePackage.SCHEDULE__FITNESS:
				return fitness != null && !fitness.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (name: ");
		result.append(name);
		result.append(')');
		return result.toString();
	}

} //ScheduleImpl
