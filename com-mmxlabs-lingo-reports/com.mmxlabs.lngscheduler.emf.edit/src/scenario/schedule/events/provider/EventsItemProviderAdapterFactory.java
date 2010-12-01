/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.schedule.events.provider;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.edit.provider.ChangeNotifier;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IChangeNotifier;
import org.eclipse.emf.edit.provider.IDisposable;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.INotifyChangedListener;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;

import scenario.schedule.events.util.EventsAdapterFactory;

/**
 * This is the factory that is used to provide the interfaces needed to support Viewers.
 * The adapters generated by this factory convert EMF adapter notifications into calls to {@link #fireNotifyChanged fireNotifyChanged}.
 * The adapters also support Eclipse property sheets.
 * Note that most of the adapters are shared among multiple instances.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class EventsItemProviderAdapterFactory extends EventsAdapterFactory implements ComposeableAdapterFactory, IChangeNotifier, IDisposable {
	/**
	 * This keeps track of the root adapter factory that delegates to this adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ComposedAdapterFactory parentAdapterFactory;

	/**
	 * This is used to implement {@link org.eclipse.emf.edit.provider.IChangeNotifier}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IChangeNotifier changeNotifier = new ChangeNotifier();

	/**
	 * This keeps track of all the supported types checked by {@link #isFactoryForType isFactoryForType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected Collection<Object> supportedTypes = new ArrayList<Object>();

	/**
	 * This constructs an instance.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EventsItemProviderAdapterFactory() {
		supportedTypes.add(IEditingDomainItemProvider.class);
		supportedTypes.add(IStructuredItemContentProvider.class);
		supportedTypes.add(ITreeItemContentProvider.class);
		supportedTypes.add(IItemLabelProvider.class);
		supportedTypes.add(IItemPropertySource.class);
	}

	/**
	 * This keeps track of the one adapter used for all {@link scenario.schedule.events.FuelMixture} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected FuelMixtureItemProvider fuelMixtureItemProvider;

	/**
	 * This creates an adapter for a {@link scenario.schedule.events.FuelMixture}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createFuelMixtureAdapter() {
		if (fuelMixtureItemProvider == null) {
			fuelMixtureItemProvider = new FuelMixtureItemProvider(this);
		}

		return fuelMixtureItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link scenario.schedule.events.FuelQuantity} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected FuelQuantityItemProvider fuelQuantityItemProvider;

	/**
	 * This creates an adapter for a {@link scenario.schedule.events.FuelQuantity}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createFuelQuantityAdapter() {
		if (fuelQuantityItemProvider == null) {
			fuelQuantityItemProvider = new FuelQuantityItemProvider(this);
		}

		return fuelQuantityItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link scenario.schedule.events.ScheduledEvent} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ScheduledEventItemProvider scheduledEventItemProvider;

	/**
	 * This creates an adapter for a {@link scenario.schedule.events.ScheduledEvent}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createScheduledEventAdapter() {
		if (scheduledEventItemProvider == null) {
			scheduledEventItemProvider = new ScheduledEventItemProvider(this);
		}

		return scheduledEventItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link scenario.schedule.events.Idle} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IdleItemProvider idleItemProvider;

	/**
	 * This creates an adapter for a {@link scenario.schedule.events.Idle}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createIdleAdapter() {
		if (idleItemProvider == null) {
			idleItemProvider = new IdleItemProvider(this);
		}

		return idleItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link scenario.schedule.events.Journey} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected JourneyItemProvider journeyItemProvider;

	/**
	 * This creates an adapter for a {@link scenario.schedule.events.Journey}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createJourneyAdapter() {
		if (journeyItemProvider == null) {
			journeyItemProvider = new JourneyItemProvider(this);
		}

		return journeyItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link scenario.schedule.events.PortVisit} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PortVisitItemProvider portVisitItemProvider;

	/**
	 * This creates an adapter for a {@link scenario.schedule.events.PortVisit}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createPortVisitAdapter() {
		if (portVisitItemProvider == null) {
			portVisitItemProvider = new PortVisitItemProvider(this);
		}

		return portVisitItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link scenario.schedule.events.SlotVisit} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SlotVisitItemProvider slotVisitItemProvider;

	/**
	 * This creates an adapter for a {@link scenario.schedule.events.SlotVisit}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createSlotVisitAdapter() {
		if (slotVisitItemProvider == null) {
			slotVisitItemProvider = new SlotVisitItemProvider(this);
		}

		return slotVisitItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link scenario.schedule.events.CharterOutVisit} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CharterOutVisitItemProvider charterOutVisitItemProvider;

	/**
	 * This creates an adapter for a {@link scenario.schedule.events.CharterOutVisit}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createCharterOutVisitAdapter() {
		if (charterOutVisitItemProvider == null) {
			charterOutVisitItemProvider = new CharterOutVisitItemProvider(this);
		}

		return charterOutVisitItemProvider;
	}

	/**
	 * This returns the root adapter factory that contains this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ComposeableAdapterFactory getRootAdapterFactory() {
		return parentAdapterFactory == null ? this : parentAdapterFactory.getRootAdapterFactory();
	}

	/**
	 * This sets the composed adapter factory that contains this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setParentAdapterFactory(ComposedAdapterFactory parentAdapterFactory) {
		this.parentAdapterFactory = parentAdapterFactory;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object type) {
		return supportedTypes.contains(type) || super.isFactoryForType(type);
	}

	/**
	 * This implementation substitutes the factory itself as the key for the adapter.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter adapt(Notifier notifier, Object type) {
		return super.adapt(notifier, this);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object adapt(Object object, Object type) {
		if (isFactoryForType(type)) {
			Object adapter = super.adapt(object, type);
			if (!(type instanceof Class<?>) || (((Class<?>)type).isInstance(adapter))) {
				return adapter;
			}
		}

		return null;
	}

	/**
	 * This adds a listener.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void addListener(INotifyChangedListener notifyChangedListener) {
		changeNotifier.addListener(notifyChangedListener);
	}

	/**
	 * This removes a listener.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void removeListener(INotifyChangedListener notifyChangedListener) {
		changeNotifier.removeListener(notifyChangedListener);
	}

	/**
	 * This delegates to {@link #changeNotifier} and to {@link #parentAdapterFactory}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void fireNotifyChanged(Notification notification) {
		changeNotifier.fireNotifyChanged(notification);

		if (parentAdapterFactory != null) {
			parentAdapterFactory.fireNotifyChanged(notification);
		}
	}

	/**
	 * This disposes all of the item providers created by this factory. 
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void dispose() {
		if (fuelMixtureItemProvider != null) fuelMixtureItemProvider.dispose();
		if (fuelQuantityItemProvider != null) fuelQuantityItemProvider.dispose();
		if (scheduledEventItemProvider != null) scheduledEventItemProvider.dispose();
		if (idleItemProvider != null) idleItemProvider.dispose();
		if (journeyItemProvider != null) journeyItemProvider.dispose();
		if (portVisitItemProvider != null) portVisitItemProvider.dispose();
		if (slotVisitItemProvider != null) slotVisitItemProvider.dispose();
		if (charterOutVisitItemProvider != null) charterOutVisitItemProvider.dispose();
	}

}
