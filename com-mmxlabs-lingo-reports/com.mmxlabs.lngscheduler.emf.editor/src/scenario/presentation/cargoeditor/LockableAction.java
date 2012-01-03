/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.presentation.cargoeditor;

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

	public LockableAction(String text, ImageDescriptor image) {
		super(text, image);
	}

	public LockableAction(String text, int style) {
		super(text, style);
	}

	public LockableAction(String text) {
		super(text);
	}

	public void setEnabled(final boolean enabled) {
		this.enabledIfUnlocked = enabled;
		if (!lockedForEditing) {
			super.setEnabled(enabled);
		}
	}
	
	public boolean isLockedForEditing() {
		return lockedForEditing;
	}

	public void setLockedForEditing(boolean lockedForEditing) {
		this.lockedForEditing = lockedForEditing;
		if (lockedForEditing) {
			super.setEnabled(false);
		} else {
			super.setEnabled(enabledIfUnlocked);
		}
	}
	
	public static LockableAction wrap(final IAction action) {
		if (action instanceof LockableAction) return (LockableAction) action;
		return new LockableAction() {
			public void setEnabled(boolean enabled) {
				this.enabledIfUnlocked = enabled;
				if (!lockedForEditing) {
					action.setEnabled(enabled);
				}
			}

			public void setLockedForEditing(boolean lockedForEditing) {
				this.lockedForEditing = lockedForEditing;
				if (lockedForEditing) {
					action.setEnabled(false);
				} else {
					action.setEnabled(enabledIfUnlocked);
				}
			}
			
			// delegate methods follow; not interesting
			
			public void addPropertyChangeListener(IPropertyChangeListener listener) {
				action.addPropertyChangeListener(listener);
			}

			public int getAccelerator() {
				return action.getAccelerator();
			}

			public String getActionDefinitionId() {
				return action.getActionDefinitionId();
			}

			public String getDescription() {
				return action.getDescription();
			}

			public ImageDescriptor getDisabledImageDescriptor() {
				return action.getDisabledImageDescriptor();
			}

			public HelpListener getHelpListener() {
				return action.getHelpListener();
			}

			public ImageDescriptor getHoverImageDescriptor() {
				return action.getHoverImageDescriptor();
			}

			public String getId() {
				return action.getId();
			}

			public ImageDescriptor getImageDescriptor() {
				return action.getImageDescriptor();
			}

			public IMenuCreator getMenuCreator() {
				return action.getMenuCreator();
			}

			public int getStyle() {
				return action.getStyle();
			}

			public String getText() {
				return action.getText();
			}

			public String getToolTipText() {
				return action.getToolTipText();
			}

			public boolean isChecked() {
				return action.isChecked();
			}

			public boolean isHandled() {
				return action.isHandled();
			}

			public void removePropertyChangeListener(IPropertyChangeListener listener) {
				action.removePropertyChangeListener(listener);
			}

			public void run() {
				action.run();
			}

			public void runWithEvent(Event event) {
				action.runWithEvent(event);
			}

			public void setActionDefinitionId(String id) {
				action.setActionDefinitionId(id);
			}

			public void setChecked(boolean checked) {
				action.setChecked(checked);
			}

			public void setDescription(String text) {
				action.setDescription(text);
			}

			public void setDisabledImageDescriptor(ImageDescriptor newImage) {
				action.setDisabledImageDescriptor(newImage);
			}

			public void setHelpListener(HelpListener listener) {
				action.setHelpListener(listener);
			}

			public void setHoverImageDescriptor(ImageDescriptor newImage) {
				action.setHoverImageDescriptor(newImage);
			}

			public void setId(String id) {
				action.setId(id);
			}

			public void setImageDescriptor(ImageDescriptor newImage) {
				action.setImageDescriptor(newImage);
			}

			public void setMenuCreator(IMenuCreator creator) {
				action.setMenuCreator(creator);
			}

			public void setText(String text) {
				action.setText(text);
			}

			public void setToolTipText(String text) {
				action.setToolTipText(text);
			}

			public void setAccelerator(int keycode) {
				action.setAccelerator(keycode);
			}

			@Override
			public boolean isEnabled() {
				return action.isEnabled();
			}
		};
	}
}
