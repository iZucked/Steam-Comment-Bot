/*******************************************************************************
 * Copyright (c) Emil Crumhorn - Hexapixel.com - emil.crumhorn@gmail.com
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    emil.crumhorn@gmail.com - initial API and implementation
 *******************************************************************************/ 

package org.eclipse.nebula.widgets.ganttchart;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class ImageCache {

    private static Map mImageMap;
    
    static {
    	mImageMap = new HashMap();
    }
    
    /**
     * Returns an image that is also cached if it has to be created and does not already exist in the cache.
     * 
     * @param fileName Filename of image to fetch
     * @return Image file or null if it could not be found
     */
    public static Image getImage(final String fileName) {
        Image image = (Image) mImageMap.get(fileName);
        if (image == null) {
            image = createImage(fileName);
            mImageMap.put(fileName, image);
        }
        return image;
    }

    // creates the image, and tries really hard to do so
    private static Image createImage(final String fileName) {
        final ClassLoader classLoader = ImageCache.class.getClassLoader();
        InputStream stream = classLoader.getResourceAsStream(fileName);
        if (stream == null) {
            // the old way didn't have leading slash, so if we can't find the image stream,
            // let's see if the old way works.
            stream = classLoader.getResourceAsStream(fileName.substring(1));

            if (stream == null) {
                stream = classLoader.getResourceAsStream(fileName);
                if (stream == null) { // NOPMD - get over yourself
                    stream = classLoader.getResourceAsStream(fileName.substring(1));
                    if (stream == null) {
                        //logger.debug("null input stream for both " + path + " and " + path);
                        return null;
                    }
                }
            }
        }

        final Image img = new Image(Display.getDefault(), stream);
        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace(); // NOPMD - we don't truly care that much
        }

        return img;
    }

    /**
     * Disposes ALL images that have been cached.
     *
     */
    public static void dispose() {
        final Iterator iterator = mImageMap.values().iterator();
        while (iterator.hasNext()) {
        	((Image)iterator.next()).dispose();
        }
    }
}
