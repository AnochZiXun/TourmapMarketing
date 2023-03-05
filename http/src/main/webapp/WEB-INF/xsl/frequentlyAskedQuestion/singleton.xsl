<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<xsl:output
		method="html"
		encoding="UTF-8"
		omit-xml-declaration="yes"
		indent="no"
		media-type="text/html"
	/>

	<xsl:import href="/import.xsl"/>

	<xsl:template match="/">
		<xsl:text disable-output-escaping="yes">&#60;!DOCTYPE HTML&#62;</xsl:text>
		<HTML dir="ltr" lang="zh-TW">
			<xsl:apply-templates/>
		</HTML>
	</xsl:template>

	<xsl:template match="document">
		<HEAD>
			<META charset="UTF-8"/>
			<LINK href="/STYLE/default.css" media="all" rel="stylesheet" type="text/css"/>
			<STYLE><![CDATA[DIV#pagination{margin:16px auto;text-align:center;}DIV#pagination A{margin:0 6px}]]></STYLE>
			<SCRIPT src="//ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"/>
			<SCRIPT src="/SCRIPT/unslider.min.js"/>
			<SCRIPT src="/SCRIPT/default.js"/>
			<TITLE>旅途國際 &#187; 活動消息 &#187; <xsl:value-of select="subject"/></TITLE>
		</HEAD>
		<xsl:comment>
			<xsl:value-of select="system-property('xsl:version')"/>
		</xsl:comment>
		<BODY>
			<DIV class="wallpaper">
				<xsl:call-template name="header"/>
				<xsl:call-template name="banner"/>
				<xsl:call-template name="qrCode"/>
			</DIV>
			<DIV class="rightContent">
				<DIV>
					<H1>
						<xsl:value-of select="subject"/>
					</H1>
					<DIV>
						<xsl:value-of select="hyperTextMarkupLanguage" disable-output-escaping="yes"/>
					</DIV>
				</DIV>
			</DIV>
			<xsl:call-template name="footer"/>
		</BODY>
	</xsl:template>

</xsl:stylesheet>