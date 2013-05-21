/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.ui.editorpart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Composite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.mmxcore.impl.MMXContentAdapter;
import com.mmxlabs.models.ui.editorpart.BaseJointModelEditorContribution;
import com.mmxlabs.models.ui.editorpart.JointModelEditorPart;

/**
 * This editor contribution doesn't display anything in the editor. It does add an adapter which updates the dirty state bit on the schedule model. This may in turn trigger re-evaluation, if live
 * evaluation is on.
 * 
 * @author hinton
 * 
 */
public class ScheduleModelEditorContribution extends BaseJointModelEditorContribution<ScheduleModel> {
	private static final Logger log = LoggerFactory.getLogger(ScheduleModelEditorContribution.class);

	private final List<UUIDObject> adaptedObjects = new ArrayList<UUIDObject>();

	private final MMXContentAdapter dirtyStateAdapter = new MMXContentAdapter() {
		@Override
		public void reallyNotifyChanged(final Notification notification) {
			handle(notification);
		}

		private boolean handle(final Notification notification) {
			if (notification.isTouch() == false) {
				if (notification.getFeature() == MMXCorePackage.eINSTANCE.getNamedObject_Name()) {
					return false; // this feature is irrelevant
				} else if (notification.getFeature() == SchedulePackage.eINSTANCE.getScheduleModel_Schedule()) {
					// If a new schedule is set, ignore this change
					return false;
				}
				EObject target = (EObject) notification.getNotifier();
				while (target != null) {
					if (target == modelObject) {
						return false;
					}
					target = target.eContainer();
				}
				log.debug("Setting dirty bit on schedule model because " + notification);
				ScheduleModelEditorContribution.this.modelObject.setDirty(true);
				return true;
			}
			return false;
		}

		protected void missedNotifications(final List<Notification> missed) {
			// Re-process missed notifications to update dirty state.
			final List<Notification> copied = new ArrayList<Notification>(missed);
			for (final Notification n : copied) {
				if (handle(n)) {
					break;
				}
			}
		}
	};

	@Override
	public void init(final JointModelEditorPart editorPart, final MMXRootObject rootObject, final UUIDObject modelObject) {
		super.init(editorPart, rootObject, modelObject);
		rootObject.eAdapters().add(dirtyStateAdapter);
		adaptedObjects.add(rootObject);
	}

	@Override
	public void dispose() {
		for (final UUIDObject sub : adaptedObjects) {
			sub.eAdapters().removeAll(Collections.singleton(dirtyStateAdapter));
		}
		super.dispose();
	}

	@Override
	public void addPages(final Composite parent) {

	}

	@Override
	public void setLocked(final boolean locked) {

	}
}
