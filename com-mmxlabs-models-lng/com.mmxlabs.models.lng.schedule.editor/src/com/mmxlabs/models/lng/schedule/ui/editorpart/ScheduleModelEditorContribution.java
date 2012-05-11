/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
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
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.MMXSubModel;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.mmxcore.impl.MMXContentAdapter;
import com.mmxlabs.models.ui.editorpart.BaseJointModelEditorContribution;
import com.mmxlabs.models.ui.editorpart.JointModelEditorPart;

/**
 * This editor contribution doesn't display anything in the editor. It does add an adapter which updates the
 * dirty state bit on the schedule model. This may in turn trigger re-evaluation, if live evaluation is on.
 * 
 * @author hinton
 *
 */
public class ScheduleModelEditorContribution extends BaseJointModelEditorContribution<ScheduleModel> {
	private static final Logger log = LoggerFactory.getLogger(ScheduleModelEditorContribution.class);
	private MMXContentAdapter dirtyStateAdapter = new MMXContentAdapter() {
		@Override
		public void reallyNotifyChanged(final Notification notification) {
			final int type = notification.getEventType();
			switch (type) {
			case Notification.RESOLVE:
			case Notification.REMOVING_ADAPTER:
				break;
			default:
				if (notification.getFeature() == MMXCorePackage.eINSTANCE.getNamedObject_Name()) return; // this feature is irrelevant
				EObject target = (EObject) notification.getNotifier();
				while (target != null) {
					if (target == modelObject) return;
					target = target.eContainer();
				}
				log.debug("Setting dirty bit on schedule model");
				ScheduleModelEditorContribution.this.modelObject.setDirty(true);
			}
		}
	};
	private List<UUIDObject> adaptedObjects = new ArrayList<UUIDObject>();
	
	@Override
	public void init(final JointModelEditorPart editorPart, final MMXRootObject rootObject, final UUIDObject modelObject) {
		super.init(editorPart, rootObject, modelObject);
		for (final MMXSubModel subModel : rootObject.getSubModels()) {
			subModel.getSubModelInstance().eAdapters().add(dirtyStateAdapter);
			adaptedObjects.add(subModel.getSubModelInstance());
		}
	}

	@Override
	public void dispose() {
		for (final UUIDObject sub : adaptedObjects) {
			sub.eAdapters().removeAll(Collections.singleton(dirtyStateAdapter));
		}
		super.dispose();
	}



	@Override
	public void addPages(Composite parent) {
		
	}

	@Override
	public void setLocked(boolean locked) {
		
	}
}
