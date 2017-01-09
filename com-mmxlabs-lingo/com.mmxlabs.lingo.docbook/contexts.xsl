<?xml version="1.0"?>
<!--

    Copyright (C) Minimax Labs Ltd., 2010 - 2017
    All rights reserved.

-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:d="http://docbook.org/ns/docbook" xmlns:ng="http://docbook.org/docbook-ng"
	xmlns:db="http://docbook.org/ns/docbook" xmlns:exsl="http://exslt.org/common">

	<xsl:import href="http://docbook.sourceforge.net/release/xsl-ns/current/eclipse/eclipse3.xsl" />

	<xsl:template match="/">
		<!-- Call original code from the imported stylesheet -->
		<xsl:apply-imports />

		<!-- Call custom templates for the contexts.xml -->
		<xsl:call-template name="contexts.xml" />
	</xsl:template>

	<!-- Template for creating auxiliary contexts.xml file -->
	<xsl:template name="contexts.xml">
		<xsl:call-template name="write.chunk">
			<xsl:with-param name="filename">
				<xsl:if test="$manifest.in.base.dir != 0">
					<xsl:value-of select="$chunk.base.dir" />
				</xsl:if>
				<xsl:value-of select="'contexts.xml'" />
			</xsl:with-param>

			<xsl:with-param name="method" select="'xml'" />
			<xsl:with-param name="encoding" select="'utf-8'" />
			<xsl:with-param name="indent" select="'yes'" />
			<xsl:with-param name="content">

				<!-- TODO this should also be in the output -->
				<!--?NLS TYPE="org.eclipse.help.contexts"? -->
				<contexts>

					<xsl:if test="(@id)">
						<!-- Get the title of the root element -->
						<xsl:variable name="title">
							<xsl:apply-templates select="." mode="title.markup" />
						</xsl:variable>

						<!-- Get HTML filename for the root element -->
						<xsl:variable name="href">
							<xsl:call-template name="href.target.with.base.dir">
								<xsl:with-param name="object" select="/*" />
							</xsl:call-template>
						</xsl:variable>

						<context id="{@id}">
							<description>
								<xsl:copy-of select="$title" />
							</description>
							<topic label="{$title}" href="{$href}" />
						</context>
					</xsl:if>

					<!-- Get context for all children of the root element -->
					<xsl:apply-templates select="/*/*" mode="contexts.xml" />

				</contexts>

			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<!-- Template which converts all DocBook containers into one entry in the contexts file -->
	<xsl:template
		match="d:part|d:reference|d:preface|d:chapter|
                               d:bibliography|d:appendix|d:article|
                               d:glossary|d:section|d:sect1|d:sect2|
                                d:sect3|d:sect4|d:sect5|d:refentry|
                                d:colophon|d:bibliodiv|d:index"
		mode="contexts.xml">

		<xsl:if test="(@id)">

			<!-- Get the title of the current element -->
			<xsl:variable name="title">
				<xsl:apply-templates select="." mode="title.markup" />
			</xsl:variable>

			<!-- Get HTML filename for the current element -->
			<xsl:variable name="href">
				<xsl:call-template name="href.target.with.base.dir" />
			</xsl:variable>


			<xsl:variable name="dbeclipse-ctx-description">
				<xsl:call-template name="pi.dbeclipse_ctx_description" />
			</xsl:variable>

			<xsl:variable name="description">
				<xsl:choose>
					<xsl:when test="$dbeclipse-ctx-description != ''">
						<xsl:value-of select="$dbeclipse-ctx-description" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$title" />
					</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>

			<!-- Create ToC entry for the current node and process its container-type children further -->
			<xsl:variable name="dbeclipse-ctx-title">
				<xsl:call-template name="pi.dbeclipse_ctx_title" />
			</xsl:variable>

			<xsl:choose>
				<xsl:when test="$dbeclipse-ctx-title != ''">
					<context id="{@id}" title="{$dbeclipse-ctx-title}">
						<description>
							<xsl:copy-of select="$description" />
						</description>
						<topic label="{$title}" href="{$href}" />
					</context>
				</xsl:when>
				<xsl:otherwise>
					<context id="{@id}">
						<description>
							<xsl:copy-of select="$description" />
						</description>
						<topic label="{$title}" href="{$href}" />
					</context>
				</xsl:otherwise>
			</xsl:choose>


		</xsl:if>

		<xsl:apply-templates
			select="d:part|d:reference|d:preface|d:chapter|
                                 d:bibliography|d:appendix|d:article|
                                 d:glossary|d:section|d:sect1|d:sect2|
                                 d:sect3|d:sect4|d:sect5|d:refentry|
                                 d:colophon|d:bibliodiv|d:index"
			mode="contexts.xml" />

	</xsl:template>

	<!-- Default processing in the contexts.xml mode is no processing -->
	<xsl:template match="text()" mode="contexts.xml" />

	<xsl:template name="pi.dbeclipse_ctx_description">
		<xsl:param name="node" select="." />
		<xsl:call-template name="dbeclipse-attribute">
			<xsl:with-param name="pis" select="$node/processing-instruction('dbeclipse')" />
			<xsl:with-param name="attribute" select="'ctx_description'" />
		</xsl:call-template>
	</xsl:template>

	<xsl:template name="pi.dbeclipse_ctx_title">
		<xsl:param name="node" select="." />
		<xsl:call-template name="dbeclipse-attribute">
			<xsl:with-param name="pis" select="$node/processing-instruction('dbeclipse')" />
			<xsl:with-param name="attribute" select="'ctx_title'" />
		</xsl:call-template>
	</xsl:template>

	<xsl:template name="dbeclipse-attribute">
		<!-- * dbhtml-attribute is an internal utility template for retrieving -->
		<!-- * pseudo-attributes/parameters from PIs -->
		<xsl:param name="pis" select="processing-instruction('dbeclipse')" />
		<xsl:param name="attribute">
			ctx_title,ctx_description
		</xsl:param>
		<xsl:call-template name="pi-attribute">
			<xsl:with-param name="pis" select="$pis" />
			<xsl:with-param name="attribute" select="$attribute" />
		</xsl:call-template>
	</xsl:template>


	<xsl:template match="processing-instruction('dbeclipse')">
		<!-- nop -->
	</xsl:template>


</xsl:stylesheet>
