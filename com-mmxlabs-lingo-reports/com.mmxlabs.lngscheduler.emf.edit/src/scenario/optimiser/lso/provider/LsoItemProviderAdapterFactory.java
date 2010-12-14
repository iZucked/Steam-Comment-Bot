/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package scenario.optimiser.lso.provider;

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
import org.eclipse.emf.edit.provider.IItemColorProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.INotifyChangedListener;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;

import scenario.optimiser.lso.util.LsoAdapterFactory;

/**
 * This is the factory that is used to provide the interfaces needed to support Viewers.
 * The adapters generated by this factory convert EMF adapter notifications into calls to {@link #fireNotifyChanged fireNotifyChanged}.
 * The adapters also support Eclipse property sheets.
 * Note that most of the adapters are shared among multiple instances.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class LsoItemProviderAdapterFactory extends LsoAdapterFactory implements ComposeableAdapterFactory, IChangeNotifier, IDisposable {
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
	public LsoItemProviderAdapterFactory() {
		supportedTypes.add(IEditingDomainItemProvider.class);
		supportedTypes.add(IStructuredItemContentProvider.class);
		supportedTypes.add(ITreeItemContentProvider.class);
		supportedTypes.add(IItemLabelProvider.class);
		supportedTypes.add(IItemPropertySource.class);
		supportedTypes.add(IItemColorProvider.class);
	}

	/**
	 * This keeps track of the one adapter used for all {@link scenario.optimiser.lso.LSOSettings} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected LSOSettingsItemProvider lsoSettingsItemProvider;

	/**
	 * This creates an adapter for a {@link scenario.optimiser.lso.LSOSettings}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createLSOSettingsAdapter() {
		if (lsoSettingsItemProvider == null) {
			lsoSettingsItemProvider = new LSOSettingsItemProvider(this);
		}

		return lsoSettingsItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link scenario.optimiser.lso.ThresholderSettings} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ThresholderSettingsItemProvider thresholderSettingsItemProvider;

	/**
	 * This creates an adapter for a {@link scenario.optimiser.lso.ThresholderSettings}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createThresholderSettingsAdapter() {
		if (thresholderSettingsItemProvider == null) {
			thresholderSettingsItemProvider = new ThresholderSettingsItemProvider(this);
		}

		return thresholderSettingsItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link scenario.optimiser.lso.MoveGeneratorSettings} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MoveGeneratorSettingsItemProvider moveGeneratorSettingsItemProvider;

	/**
	 * This creates an adapter for a {@link scenario.optimiser.lso.MoveGeneratorSettings}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createMoveGeneratorSettingsAdapter() {
		if (moveGeneratorSettingsItemProvider == null) {
			moveGeneratorSettingsItemProvider = new MoveGeneratorSettingsItemProvider(this);
		}

		return moveGeneratorSettingsItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link scenario.optimiser.lso.RandomMoveGeneratorSettings} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected RandomMoveGeneratorSettingsItemProvider randomMoveGeneratorSettingsItemProvider;

	/**
	 * This creates an adapter for a {@link scenario.optimiser.lso.RandomMoveGeneratorSettings}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createRandomMoveGeneratorSettingsAdapter() {
		if (randomMoveGeneratorSettingsItemProvider == null) {
			randomMoveGeneratorSettingsItemProvider = new RandomMoveGeneratorSettingsItemProvider(this);
		}

		return randomMoveGeneratorSettingsItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link scenario.optimiser.lso.ConstrainedMoveGeneratorSettings} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ConstrainedMoveGeneratorSettingsItemProvider constrainedMoveGeneratorSettingsItemProvider;

	/**
	 * This creates an adapter for a {@link scenario.optimiser.lso.ConstrainedMoveGeneratorSettings}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createConstrainedMoveGeneratorSettingsAdapter() {
		if (constrainedMoveGeneratorSettingsItemProvider == null) {
			constrainedMoveGeneratorSettingsItemProvider = new ConstrainedMoveGeneratorSettingsItemProvider(this);
		}

		return constrainedMoveGeneratorSettingsItemProvider;
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
		if (lsoSettingsItemProvider != null) lsoSettingsItemProvider.dispose();
		if (thresholderSettingsItemProvider != null) thresholderSettingsItemProvider.dispose();
		if (moveGeneratorSettingsItemProvider != null) moveGeneratorSettingsItemProvider.dispose();
		if (randomMoveGeneratorSettingsItemProvider != null) randomMoveGeneratorSettingsItemProvider.dispose();
		if (constrainedMoveGeneratorSettingsItemProvider != null) constrainedMoveGeneratorSettingsItemProvider.dispose();
	}

}
