package com.mmxlabs.models.migration.utils;

import java.io.InputStream;
import java.util.Collections;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * 
 * @author Simon Goodall
 * @noinstantiate This class is not intended to be instantiated by clients.
 */

public class EcoreHelper {

	/**
	 * Method to extract the package namespace URI from an XMI based ecore model
	 * 
	 * @param uri
	 * @return
	 * @throws Exception
	 */
	public static String getPackageNS(final URI uri) throws Exception {

		// Use a single element array to store the result from parsing
		final String[] value = new String[1];

		// Use an URIConverter to get an InputStream from a URI
		final ExtensibleURIConverterImpl uc = new ExtensibleURIConverterImpl();
		InputStream inputStream = null;
		try {
			inputStream = uc.createInputStream(uri, Collections.emptyMap());

			// TODO: There may well be a better way to do this.

			// Use an XML parser to parse document
			final SAXParser parser = SAXParserFactory.newInstance().newSAXParser();

			// Use handler to intercept the parsing an extract the NS URI attribute from document root.
			final DefaultHandler handler = new DefaultHandler() {
				@Override
				public void startElement(final String uri, final String localName, final String qName, final Attributes attributes) throws SAXException {
					if (qName.equals("ecore:EPackage")) {
						// Store the result
						value[0] = attributes.getValue("nsURI");
						// TODO: Break out of parsing early?
					}

					super.startElement(uri, localName, qName, attributes);
				}

			};

			// Parse the document!
			parser.parse(inputStream, handler);
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}

		return value[0];
	}
}
