/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.rcp.common;

import java.net.URL;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ResourceLocator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Item;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.FrameworkUtil;

/**
 * Snippet for client code:
 * setImageDescriptor(CommonImages.getImageDescriptor(IconPaths.,
 * IconMode.Enabled));
 * setDisabledImageDescriptor(CommonImages.getImageDescriptor(IconPaths.,
 * IconMode.Disabled)); // For plugin.xml: TRY
 * platform:/plugin/com.example.plugin.workspace/icons/workspace.png OR
 * platform://com.mmxlabs.rcp.common/icons/plug.png Old code from pre-this
 * class: //
 * setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
 * //
 * setDisabledImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE_DISABLED));
 */
public final class CommonImages {

	public enum IconPaths {
// 16x16
		Pack("icons/16x16/pack.png"), //
		Plus("icons/16x16/plus.png", true), //
		Plusplus("icons/16x16/plusplus.png", true), //
		Delete("icons/16x16/delete.png"), //
		Filter("icons/16x16/filter.png"), //
		Sort("icons/16x16/sort.png"), //
		Copy("icons/16x16/copy.png", true), //
		Paste("icons/16x16/paste.png"), //
		CollapseAll("icons/16x16/collapse_all.png"), //
		ExpandAll("icons/16x16/expand_all.png"), //
		Cut("icons/16x16/cut.png"), //
		Edit("icons/16x16/edit.png"), //
		Pin_8("icons/8x8/pin.png"), //
		Pin("icons/16x16/pin.png"), //
		Sandbox("icons/16x16/sandbox.png"), //
		Scenario("icons/16x16/scenario.png"), //
		Hub("icons/16x16/hub.png", true), //
		Local("icons/16x16/local.png", true), //
		
		
		Play_16("icons/16x16/optimise.png", true), //
		CloudPlay_16("icons/16x16/cloud_run.png", true), //
		Cloud_16("icons/16x16/cloud.png"), //
		ZoomIn("icons/16x16/zoom_in.png"), //
		ZoomOut("icons/16x16/zoom_out.png"), //
		PinnedRow("icons/8x8/pin.png"), //
		Console("icons/16x16/console.png"), //
		

		Error("icons/legacy/16x16/error.gif"), //
		Warning("icons/legacy/16x16/warning.gif"), //
		Information("icons/legacy/16x16/information.gif"), //

		
		//24x24
		Play("icons/24x24/optimise.png", true), //
		CloudPlay_24("icons/24x24/cloud_run.png", true), //
		Cloud_24("icons/24x24/cloud.png"), //

//		Resume("icons/24x24/terminate.png", true), //
		Terminate("icons/24x24/terminate.png", true), //
		Fork("icons/24x24/fork.png", true), //
		
		Save("icons/24x24/save.png", true), //
		Saveall("icons/24x24/save_all.png", true), //
		ReEvaluate16("icons/16x16/reevaluate.png"), //
		ReEvaluate24("icons/24x24/reevaluate.png"), //
		;

//		PackGridHand 44: element.setIconURI(FrameworkUtil.getBundle(PackGridHandler.class).getEntry("/icons/pack.png").toString());  
//		FilterField 71: setImageDescriptor(ImageDescriptor.createFromURL(new URL("platform:/plugin/com.mmxlabs.models.ui.tabular/icons/filter.png")));
// ContractPage 378:		ResourceLocator.imageDescriptorFromBundle("com.mmxlabs.rcp.common", "/icons/pack.png").ifPresent(packAction::setImageDescriptor);

		private final String path;
		private final String disabledPath;

		private IconPaths(final String path, final String disabledPath) {
			this.path = path;
			this.disabledPath = disabledPath;
		}

		private IconPaths(final String path) {
			this.path = path;
			this.disabledPath = null;
		}

		private IconPaths(final String path, final boolean defaultDisabled) {
			this.path = path;
			final StringBuilder sb = new StringBuilder(path);
			disabledPath = sb.insert(path.lastIndexOf("."), "_disabled").toString();
		}

	}

	public enum IconMode {
		Enabled, Disabled;
	}

	public static ImageDescriptor getImageDescriptor(final IconPaths iconPath, final IconMode mode) {

		switch (mode) {
		case Enabled:
			return ResourceLocator.imageDescriptorFromBundle("com.mmxlabs.rcp.common", iconPath.path).orElse(null);
		case Disabled:
			if (iconPath.disabledPath != null) {
				return ResourceLocator.imageDescriptorFromBundle("com.mmxlabs.rcp.common", iconPath.disabledPath).orElse(null);
			} else {
				return ImageDescriptor.createWithFlags(AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.rcp.common", iconPath.path), SWT.IMAGE_DISABLE);
			}
		default:
			throw new IllegalArgumentException("Bad icon spec");
		}
	}

	public static void setImageDescriptors(final IAction a, final IconPaths iconPath) {
		setImageDescriptors(a, iconPath, false);
	}
	
	public static URL getImageURL(final IconPaths iconPath, IconMode mode) {
		switch (mode) {
		case Enabled:
			return FrameworkUtil.getBundle(CommonImages.class).getEntry("/" + iconPath.path);
		case Disabled:
			if (iconPath.disabledPath != null) {
				return FrameworkUtil.getBundle(CommonImages.class).getEntry("/" + iconPath.disabledPath);
			} else {
				throw new IllegalArgumentException("Bad icon spec");
			}
		default:
			throw new IllegalArgumentException("Bad icon spec");
		}
	}
	

	public static URL getImageURL(final IconPaths iconPath) {
		return FrameworkUtil.getBundle(CommonImages.class).getEntry("/" + iconPath.path);
	}

	public static String getImageURI(final IconPaths iconPath) {
		return getImageURL(iconPath).toString();
	}
	
	public static void setImageDescriptors(final IAction a, final IconPaths iconPath, final boolean includeHover) {

		a.setImageDescriptor(getImageDescriptor(iconPath, IconMode.Enabled));
		a.setDisabledImageDescriptor(getImageDescriptor(iconPath, IconMode.Disabled));
		if (includeHover) {
			a.setHoverImageDescriptor(getImageDescriptor(iconPath, IconMode.Enabled));
		}
	}

	public static void setImage(final Item item, final IconPaths iconPath) {

		final ImageDescriptor imageDescriptor = getImageDescriptor(iconPath, IconMode.Enabled);
		final Image img = imageDescriptor.createImage();
		item.addDisposeListener(e -> img.dispose());
		item.setImage(img);

	}
}
