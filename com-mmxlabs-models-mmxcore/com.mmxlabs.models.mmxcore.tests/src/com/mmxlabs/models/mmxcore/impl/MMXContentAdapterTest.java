/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.mmxcore.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.junit.Assert;
import org.junit.Test;

import com.mmxlabs.models.mmxcore.MMXCorePackage;

public class MMXContentAdapterTest {

	/**
	 * This test case tests that notifications for features which should be ignored are not passed to the noficication methods.
	 */
	@Test
	public void testEnablementFlag() {

		final MMXContentAdapter adapter = new MMXContentAdapter() {

			@Override
			public void reallyNotifyChanged(final Notification notification) {

			}
		};

		// Enabled by default
		Assert.assertTrue(adapter.enabled);

		// Test standard enable method
		adapter.disable();
		Assert.assertFalse(adapter.enabled);
		adapter.enable();
		Assert.assertTrue(adapter.enabled);

		// Test alternative enable method - arg == true
		adapter.disable();
		Assert.assertFalse(adapter.enabled);
		adapter.enable(true);
		Assert.assertTrue(adapter.enabled);

		// Test alternative enable method - arg == false
		adapter.disable();
		Assert.assertFalse(adapter.enabled);
		adapter.enable(false);
		Assert.assertTrue(adapter.enabled);

	}

	/**
	 * Test that all notifications are delivered as expected when we are always enabled.
	 */
	@Test
	public void testEnablement_AlwaysEnabled() {

		final List<EStructuralFeature> seenFeatures = new ArrayList<EStructuralFeature>();

		// Construct a MMXContentAdapter which fails should the notification methods find an ignored feature.
		final MMXContentAdapter adapter = new MMXContentAdapter() {

			@Override
			public void reallyNotifyChanged(final Notification n) {
				seenFeatures.add((EStructuralFeature) n.getFeature());
			};

			@Override
			protected void missedNotifications(final java.util.List<Notification> missed) {
				Assert.fail("Notifications are expected to be processed immediately.");
			}

		};

		adapter.enable();

		final List<EStructuralFeature> acceptableFeatures = new ArrayList<EStructuralFeature>();
		acceptableFeatures.add(MMXCorePackage.eINSTANCE.getMMXObject_Extensions());
		acceptableFeatures.add(MMXCorePackage.eINSTANCE.getNamedObject_Name());

		for (final EStructuralFeature feature : acceptableFeatures) {
			final Notification n = mock(Notification.class);
			when(n.getFeature()).thenReturn(feature);

			adapter.notifyChanged(n);
		}

		Assert.assertEquals(acceptableFeatures, seenFeatures);

		// Check no more notification delivered or redelivered
		seenFeatures.clear();
		adapter.disable();
		adapter.enable();
		Assert.assertTrue(seenFeatures.isEmpty());
	}

	/**
	 * Test that notifications are processed as we call default enabled method
	 */
	@Test
	public void testEnablement_callEnabled() {

		final List<EStructuralFeature> seenFeatures = new ArrayList<EStructuralFeature>();

		// Construct a MMXContentAdapter which fails should the notification methods find an ignored feature.
		final MMXContentAdapter adapter = new MMXContentAdapter() {

			@Override
			public void reallyNotifyChanged(final Notification n) {
				// seenFeatures.add((EStructuralFeature) n);
				Assert.fail("Notifications are expected to be in the missed queue.");
			};

			@Override
			protected void missedNotifications(final java.util.List<Notification> missed) {
				for (final Notification n : missed) {
					seenFeatures.add((EStructuralFeature) n.getFeature());
				}

			}

		};

		// Suspend notification delivery
		adapter.disable();

		final List<EStructuralFeature> acceptableFeatures = new ArrayList<EStructuralFeature>();
		acceptableFeatures.add(MMXCorePackage.eINSTANCE.getMMXObject_Extensions());
		acceptableFeatures.add(MMXCorePackage.eINSTANCE.getNamedObject_Name());

		for (final EStructuralFeature feature : acceptableFeatures) {
			final Notification n = mock(Notification.class);
			when(n.getFeature()).thenReturn(feature);

			adapter.notifyChanged(n);
		}

		// Deliver the notifications
		adapter.enable();

		Assert.assertEquals(acceptableFeatures, seenFeatures);
		// Check no more notification delivered or redelivered
		seenFeatures.clear();
		adapter.disable();
		adapter.enable();
		Assert.assertTrue(seenFeatures.isEmpty());
	}

	/**
	 * Test that notifications are processed as we call enabled with the skip flag set to false
	 */
	@Test
	public void testEnablement_callEnabled_false() {

		final List<EStructuralFeature> seenFeatures = new ArrayList<EStructuralFeature>();

		// Construct a MMXContentAdapter which fails should the notification methods find an ignored feature.
		final MMXContentAdapter adapter = new MMXContentAdapter() {

			@Override
			public void reallyNotifyChanged(final Notification n) {
				Assert.fail("Notifications are expected to be in the missed queue.");
			};

			@Override
			protected void missedNotifications(final java.util.List<Notification> missed) {
				for (final Notification n : missed) {
					seenFeatures.add((EStructuralFeature) n.getFeature());
				}

			}

		};

		// Suspend notification delivery
		adapter.disable();

		final List<EStructuralFeature> acceptableFeatures = new ArrayList<EStructuralFeature>();
		acceptableFeatures.add(MMXCorePackage.eINSTANCE.getMMXObject_Extensions());
		acceptableFeatures.add(MMXCorePackage.eINSTANCE.getNamedObject_Name());

		for (final EStructuralFeature feature : acceptableFeatures) {
			final Notification n = mock(Notification.class);
			when(n.getFeature()).thenReturn(feature);

			adapter.notifyChanged(n);
		}

		// Deliver the notifications
		adapter.enable(false);

		Assert.assertEquals(acceptableFeatures, seenFeatures);
		// Check no more notification delivered or redelivered
		seenFeatures.clear();
		adapter.disable();
		adapter.enable();
		Assert.assertTrue(seenFeatures.isEmpty());
	}

	/**
	 * Test missed notifications are ignored as we can enable with the skip flag set to true
	 */
	@Test
	public void testEnablement_callEnabled_true() {

		final List<EStructuralFeature> seenFeatures = new ArrayList<EStructuralFeature>();

		// Construct a MMXContentAdapter which fails should the notification methods find an ignored feature.
		final MMXContentAdapter adapter = new MMXContentAdapter() {

			@Override
			public void reallyNotifyChanged(final Notification n) {
				Assert.fail("Notifications are expected to be in the missed queue.");
			};

			@Override
			protected void missedNotifications(final java.util.List<Notification> missed) {
				for (final Notification n : missed) {
					seenFeatures.add((EStructuralFeature) n.getFeature());
				}

			}

		};

		// Suspend notification delivery
		adapter.disable();

		final List<EStructuralFeature> acceptableFeatures = new ArrayList<EStructuralFeature>();
		acceptableFeatures.add(MMXCorePackage.eINSTANCE.getMMXObject_Extensions());
		acceptableFeatures.add(MMXCorePackage.eINSTANCE.getNamedObject_Name());

		for (final EStructuralFeature feature : acceptableFeatures) {
			final Notification n = mock(Notification.class);
			when(n.getFeature()).thenReturn(feature);

			adapter.notifyChanged(n);
		}

		// Enable, but skip notification delivery
		adapter.enable(true);

		Assert.assertTrue(seenFeatures.isEmpty());

		// Check no more notification delivered or redelivered
		seenFeatures.clear();
		adapter.disable();
		adapter.enable();
		Assert.assertTrue(seenFeatures.isEmpty());

	}

	/**
	 * This test case tests that notifications for features which should be ignored are not passed to the noficication methods.
	 */
	@Test
	public void testIgnoredFeatures() {

		final List<EStructuralFeature> seenFeatures = new ArrayList<EStructuralFeature>();

		// Construct a MMXContentAdapter which fails should the notification methods find an ignored feature.
		final MMXContentAdapter adapter = new MMXContentAdapter() {

			@Override
			public void reallyNotifyChanged(final Notification n) {

				if (n.getFeature() == MMXCorePackage.eINSTANCE.getMMXObject_Extensions()) {

					Assert.fail("Ignore feature was not ignored!");
				}

				seenFeatures.add((EStructuralFeature) n.getFeature());
			};

			@Override
			protected void missedNotifications(final java.util.List<Notification> missed) {
				for (final Notification n : missed) {
					if (n.getFeature() == MMXCorePackage.eINSTANCE.getMMXObject_Extensions()) {

						Assert.fail("Ignore feature was not ignored!");
					}
					seenFeatures.add((EStructuralFeature) n.getFeature());
				}

			}

		};

		// Loop over both enabled and disabled adapter states
		for (int i = 0; i < 2; i++) {
			seenFeatures.clear();
			if (i == 0) {
				adapter.enable();
			} else {
				adapter.disable();
			}
			// final List<EStructuralFeature> ignoredFeatures = new ArrayList<EStructuralFeature>();
			// Test all ignored features are ok
			adapter.ignoredFeatures.add(MMXCorePackage.eINSTANCE.getMMXObject_Extensions());

			for (final EStructuralFeature feature : adapter.ignoredFeatures) {
				final Notification n = mock(Notification.class);
				when(n.getFeature()).thenReturn(feature);

				adapter.notifyChanged(n);
			}

			// Test some acceptable features
			final List<EStructuralFeature> acceptableFeatures = new ArrayList<EStructuralFeature>();
			acceptableFeatures.add(MMXCorePackage.eINSTANCE.getNamedObject_Name());
			for (final EStructuralFeature feature : acceptableFeatures) {
				final Notification n = mock(Notification.class);
				when(n.getFeature()).thenReturn(feature);

				adapter.notifyChanged(n);
			}

			if (adapter.enabled == false) {
				adapter.enable(false);
			}

			Assert.assertEquals(acceptableFeatures, seenFeatures);
		}

	}

	/**
	 * Test that if a new missed notification is added during processing of the current list - from the same thread, we continue as expected
	 */
	@Test(timeout = 3000)
	public void testConcurrenyIssue_addNotificationDuringMissedProcessing() {
		final List<EStructuralFeature> seenFeatures = new ArrayList<EStructuralFeature>();

		// Construct a MMXContentAdapter which fails should the notification methods find an ignored feature.
		final MMXContentAdapter adapter = new MMXContentAdapter() {

			private boolean firstMissedNotification = true;

			@Override
			public void reallyNotifyChanged(final Notification n) {
				// seenFeatures.add((EStructuralFeature) n);
				Assert.fail("Notifications are expected to be in the missed queue.");
			};

			@Override
			protected void missedNotifications(final java.util.List<Notification> missed) {
				for (final Notification n : missed) {
					seenFeatures.add((EStructuralFeature) n.getFeature());

					// If this is the first missed notification, fire off another one
					if (firstMissedNotification) {
						firstMissedNotification = false;

						final Notification n2 = mock(Notification.class);
						when(n2.getFeature()).thenReturn(MMXCorePackage.eINSTANCE.getMMXObject_Extensions());
						this.notifyChanged(n2);

					}
				}

			}

		};

		// Suspend notification delivery
		adapter.disable();

		final List<EStructuralFeature> acceptableFeatures = new ArrayList<EStructuralFeature>();
		// acceptableFeatures.add(MMXCorePackage.eINSTANCE.getMMXObject_Extensions());
		acceptableFeatures.add(MMXCorePackage.eINSTANCE.getNamedObject_Name());

		for (final EStructuralFeature feature : acceptableFeatures) {
			final Notification n = mock(Notification.class);
			when(n.getFeature()).thenReturn(feature);

			adapter.notifyChanged(n);
		}

		// Deliver the notifications
		adapter.enable();

		final List<EStructuralFeature> expectedFeatures = new ArrayList<EStructuralFeature>();
		expectedFeatures.add(MMXCorePackage.eINSTANCE.getNamedObject_Name());
		expectedFeatures.add(MMXCorePackage.eINSTANCE.getMMXObject_Extensions());

		Assert.assertEquals(expectedFeatures, seenFeatures);
		// Check no more notification delivered or redelivered
		seenFeatures.clear();
		adapter.disable();
		adapter.enable();
		Assert.assertTrue(seenFeatures.isEmpty());
	}

}
