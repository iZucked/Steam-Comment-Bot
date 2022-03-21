/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.rcp.icons.lingo;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.ResourceLocator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Item;

import com.mmxlabs.rcp.icons.lingo.internal.Activator;

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

	private static final String PLUGIN_ID = "com.mmxlabs.rcp.icons.lingo";

	public enum IconPaths {
		// 8x8
		Pin_8(8, "icons/8x8/pin.png"), //
		PinnedRow(8, "icons/8x8/pin.png"), //

		// 16x16
		BaseFlag(16, "icons/16x16/base_flag.png"), //
		BaseFlagGreen(16, "icons/16x16/base_flag_green.png"), //
		Pack(16, "icons/16x16/pack.png"), //
		Plus(16, "icons/16x16/plus.png"), //
		Plusplus(16, "icons/16x16/plusplus.png"), //
		Delete(16, "icons/16x16/delete.png"), //
		CanalPanama(16, "icons/16x16/canal_panama.png"), //
		CanalPlain(16, "icons/16x16/canal_Plain.png"), //
		CanalSuez(16, "icons/16x16/canal_suez.png"), //
		Date(16, "icons/16x16/date.png"), //
		Fuel(16, "icons/16x16/fuel.png"), //
		Filter(16, "icons/16x16/filter.png"), //
		Folder(16, "icons/16x16/folder.png"), //
		Sort(16, "icons/16x16/sort.png"), //
		Copy(16, "icons/16x16/copy.png"), //
		Paste(16, "icons/16x16/paste.png"), //
		CollapseAll(16, "icons/16x16/collapse_all.png"), //
		ExpandAll(16, "icons/16x16/expand_all.png"), //
		Cut(16, "icons/16x16/cut.png"), //
		Edit(16, "icons/16x16/edit.png"), //
		Highlighter(16, "icons/16x16/highlighter.png"), //
		Pin(16, "icons/16x16/pin.png"), //
		Sandbox(16, "icons/16x16/sandbox.png"), //
		Scenario(16, "icons/16x16/scenario.png"), //
		Hub(16, "icons/16x16/hub.png"), //
		Local(16, "icons/16x16/local.png"), //
		Label(16, "icons/16x16/label.png"), //

		Play_16(16, "icons/16x16/optimise.png"), //
		CloudPlay_16(16, "icons/16x16/cloud_run.png"), //
		Cloud_16(16, "icons/16x16/cloud.png"), //
		ZoomIn(16, "icons/16x16/zoom_in.png"), //
		ZoomOut(16, "icons/16x16/zoom_out.png"), //
		Console(16, "icons/16x16/console.png"), //

		Error(16, "icons/legacy/16x16/error.gif"), //
		Warning(16, "icons/legacy/16x16/warning.gif"), //
		Information(16, "icons/legacy/16x16/information.gif"), //

		// 24x24
		Play(24, "icons/24x24/optimise.png"), //
		CloudPlay_24(24, "icons/24x24/cloud_run.png"), //
		Cloud_24(24, "icons/24x24/cloud.png"), //
		Evaluate(24, "icons/24x24/evaluate_schedule.png"), //

//		Resume(24,"icons/24x24/terminate.png", true), //
		Terminate(24, "icons/24x24/terminate.png"), //
		Fork(24, "icons/24x24/fork.png"), //

		Save(24, "icons/24x24/save.png"), //
		Saveall(24, "icons/24x24/save_all.png"), //
		ReEvaluate_16(16, "icons/16x16/reevaluate.png"), //
		ReEvaluate_24(24, "icons/24x24/reevaluate.png"), //
		;

//		PackGridHand 44: element.setIconURI(FrameworkUtil.getBundle(PackGridHandler.class).getEntry("/icons/pack.png").toString());  
//		FilterField 71: setImageDescriptor(ImageDescriptor.createFromURL(new URL("platform:/plugin/com.mmxlabs.models.ui.tabular/icons/filter.png")));
// ContractPage 378:		ResourceLocator.imageDescriptorFromBundle("com.mmxlabs.rcp.common", "/icons/pack.png").ifPresent(packAction::setImageDescriptor);

		private int size;
		private final String path;
		private final String disabledPath;

		private IconPaths(final int size, final String path, final String disabledPath) {
			this.size = size;
			this.path = path;
			this.disabledPath = disabledPath;

			final ImageRegistry imageRegistry = Activator.getInstance().getImageRegistry();
			imageRegistry.put(path, ResourceLocator.imageDescriptorFromBundle(PLUGIN_ID, path).orElseThrow());
			imageRegistry.put(disabledPath, ResourceLocator.imageDescriptorFromBundle(PLUGIN_ID, disabledPath).orElseThrow());
		}

		private IconPaths(final int size, final String path) {
			this.size = size;
			this.path = path;
			final StringBuilder sb = new StringBuilder(path);

			this.disabledPath = sb.insert(path.lastIndexOf("."), "_disabled").toString();

			final ImageRegistry imageRegistry = Activator.getInstance().getImageRegistry();

			final ImageDescriptor enabledDescriptor = ResourceLocator.imageDescriptorFromBundle(PLUGIN_ID, path).orElseThrow();
			imageRegistry.put(path, enabledDescriptor);

			final ImageDescriptor d = ResourceLocator.imageDescriptorFromBundle(PLUGIN_ID, disabledPath).orElse(null);
			if (d != null) {
				imageRegistry.put(disabledPath, d);
			} else {
				imageRegistry.put(disabledPath, ImageDescriptor.createWithFlags(enabledDescriptor, SWT.IMAGE_DISABLE));
			}
		}

		public String getPath() {
			return path;
		}

		public int getSize() {
			return size;
		}

		public boolean match(final int size, final String name) {
			if (size == 0 || size == this.size) {
				// Exact name match
				if (name().equalsIgnoreCase(name)) {
					return true;
				}
				if (size != 0) {
					// Append e.g. _16 to the name and try again.
					if (name().equalsIgnoreCase(name + "_" + size)) {
						return true;

					}
				}
			}
			return false;
		}

		public ImageDescriptor getImageDescriptor(IconMode mode) {
			if (mode == IconMode.Enabled) {
				return Activator.getInstance().getImageRegistry().getDescriptor(path);
			} else {
				assert mode == IconMode.Disabled;
				return Activator.getInstance().getImageRegistry().getDescriptor(disabledPath);
			}
		}

		public Image getImage(IconMode mode) {
			if (mode == IconMode.Enabled) {
				return Activator.getInstance().getImageRegistry().get(path);
			} else {
				assert mode == IconMode.Disabled;
				return Activator.getInstance().getImageRegistry().get(disabledPath);
			}
		}

		public URL getImageURL(IconMode mode) {
			try {
				if (mode == IconMode.Enabled) {
					return new URL(String.format("icons:/%s", name()));
				} else {
					assert mode == IconMode.Disabled;
					return new URL(String.format("icons:/%s?disabled", name()));
				}
			} catch (MalformedURLException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public enum IconMode {
		Enabled, Disabled;
	}

	public static ImageDescriptor getImageDescriptor(final IconPaths iconPath, final IconMode mode) {
		return iconPath.getImageDescriptor(mode);
	}

	public static void setImageDescriptors(final IAction a, final IconPaths iconPath) {
		setImageDescriptors(a, iconPath, false);
	}

	public static URL getImageURL(final IconPaths iconPath, final IconMode mode) {
		return iconPath.getImageURL(mode);
	}

	public static URL getImageURL(final IconPaths iconPath) {
		return iconPath.getImageURL(IconMode.Enabled);
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
		item.setImage(iconPath.getImage(IconMode.Enabled));
	}
}
