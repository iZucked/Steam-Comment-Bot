/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.rcp.common.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.widgets.Event;

/**
 * An action which adds some extra locking properties
 * 
 * @author hinton
 * 
 */
public abstract class LockableAction extends Action {
	protected boolean lockedForEditing;
	protected boolean enabledIfUnlocked = true;

	public LockableAction() {
		super();
	}

	public LockableAction(final String text, final ImageDescriptor image) {
		super(text, image);
	}

	public LockableAction(final String text, final int style) {
		super(text, style);
	}

	public LockableAction(final String text) {
		super(text);
	}

	@Override
	public void setEnabled(final boolean enabled) {
		this.enabledIfUnlocked = enabled;
		if (!lockedForEditing) {
			super.setEnabled(enabled);
		}
	}

	public boolean isLockedForEditing() {
		return lockedForEditing;
	}

	public void setLocked(final boolean lockedForEditing) {
		this.lockedForEditing = lockedForEditing;
		if (lockedForEditing) {
			super.setEnabled(false);
		} else {
			super.setEnabled(enabledIfUnlocked);
		}
	}

	public static LockableAction wrap(final IAction action) {
		if (action instanceof LockableAction) {
			return (LockableAction) action;
		}
		return new LockableAction() {
			@Override
			public void setEnabled(final boolean enabled) {
				this.enabledIfUnlocked = enabled;
				if (!lockedForEditing) {
					action.setEnabled(enabled);
				}
			}

			@Override
			public void setLocked(final boolean lockedForEditing) {
				this.lockedForEditing = lockedForEditing;
				if (lockedForEditing) {
					action.setEnabled(false);
				} else {
					action.setEnabled(enabledIfUnlocked);
				}
			}

			// delegate methods follow; not interesting

			@Override
			public void addPropertyChangeListener(final IPropertyChangeListener listener) {
				action.addPropertyChangeListener(listener);
			}

			@Override
			public int getAccelerator() {
				return action.getAccelerator();
			}

			@Override
			public String getActionDefinitionId() {
				return action.getActionDefinitionId();
			}

			@Override
			public String getDescription() {
				return action.getDescription();
			}

			@Override
			public ImageDescriptor getDisabledImageDescriptor() {
				return action.getDisabledImageDescriptor();
			}

			@Override
			public HelpListener getHelpListener() {
				return action.getHelpListener();
			}

			@Override
			public ImageDescriptor getHoverImageDescriptor() {
				return action.getHoverImageDescriptor();
			}

			@Override
			public String getId() {
				return action.getId();
			}

			@Override
			public ImageDescriptor getImageDescriptor() {
				return action.getImageDescriptor();
			}

			@Override
			public IMenuCreator getMenuCreator() {
				return action.getMenuCreator();
			}

			@Override
			public int getStyle() {
				return action.getStyle();
			}

			@Override
			public String getText() {
				return action.getText();
			}

			@Override
			public String getToolTipText() {
				return action.getToolTipText();
			}

			@Override
			public boolean isChecked() {
				return action.isChecked();
			}

			@Override
			public boolean isHandled() {
				return action.isHandled();
			}

			@Override
			public void removePropertyChangeListener(final IPropertyChangeListener listener) {
				action.removePropertyChangeListener(listener);
			}

			@Override
			public void run() {
				action.run();
			}

			@Override
			public void runWithEvent(final Event event) {
				action.runWithEvent(event);
			}

			@Override
			public void setActionDefinitionId(final String id) {
				action.setActionDefinitionId(id);
			}

			@Override
			public void setChecked(final boolean checked) {
				action.setChecked(checked);
			}

			@Override
			public void setDescription(final String text) {
				action.setDescription(text);
			}

			@Override
			public void setDisabledImageDescriptor(final ImageDescriptor newImage) {
				action.setDisabledImageDescriptor(newImage);
			}

			@Override
			public void setHelpListener(final HelpListener listener) {
				action.setHelpListener(listener);
			}

			@Override
			public void setHoverImageDescriptor(final ImageDescriptor newImage) {
				action.setHoverImageDescriptor(newImage);
			}

			@Override
			public void setId(final String id) {
				action.setId(id);
			}

			@Override
			public void setImageDescriptor(final ImageDescriptor newImage) {
				action.setImageDescriptor(newImage);
			}

			@Override
			public void setMenuCreator(final IMenuCreator creator) {
				action.setMenuCreator(creator);
			}

			@Override
			public void setText(final String text) {
				action.setText(text);
			}

			@Override
			public void setToolTipText(final String text) {
				action.setToolTipText(text);
			}

			@Override
			public void setAccelerator(final int keycode) {
				action.setAccelerator(keycode);
			}

			@Override
			public boolean isEnabled() {
				return action.isEnabled();
			}
		};
	}
}