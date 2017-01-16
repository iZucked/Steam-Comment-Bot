/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.util.internal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;

import com.google.common.io.ByteStreams;
import com.mmxlabs.scenario.service.util.encryption.impl.CachingSharedCipherProvider;

/**
 * Small util class decrypt and extract the contents of a .lingo file. Remember to ensure the keyfile is correctly linked up. E.g. by adding the following system property;
 * 
 * <pre>
 * -Dlingo.enc.keyfile=${system_property:user.home}/mmxlabs/keyfiles/<CLIENT_CODE>/lingo.data
 * </pre>
 */
public class LiNGODecrypter {

	public static void main(final String args[]) throws Exception {

		if (args.length == 0) {
			System.err.println("No .lingo file specified");
		}

		final File lingoFile = new File(args[0]);
		final URI rootObjectURI = URI.createURI("archive:" + URI.createFileURI(lingoFile.getAbsolutePath()) + "!/rootObject.xmi");
		final URI manifestURI = URI.createURI("archive:" + URI.createFileURI(lingoFile.getAbsolutePath()) + "!/MANIFEST.xmi");

		final ExtensibleURIConverterImpl convertor = new ExtensibleURIConverterImpl();

		final Map<Object, Object> options = new HashMap<>();
		final URIConverter.Cipher cipher = new CachingSharedCipherProvider().getSharedCipher();
		options.put(Resource.OPTION_CIPHER, cipher);

		// Copy root object
		try (InputStream inputStream = convertor.createInputStream(rootObjectURI, options)) {
			try (FileOutputStream fos = new FileOutputStream(args[0] + ".rootObject.xmi")) {
				ByteStreams.copy(cipher.decrypt(inputStream), fos);
			}
		}
		// Copy manifest object
		try (InputStream inputStream = convertor.createInputStream(manifestURI, options)) {
			try (FileOutputStream fos = new FileOutputStream(args[0] + ".MANIFEST.xmi")) {
				ByteStreams.copy(cipher.decrypt(inputStream), fos);
			}
		}
	}
}
