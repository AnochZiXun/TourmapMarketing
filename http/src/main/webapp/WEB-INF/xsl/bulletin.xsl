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
			<STYLE><![CDATA[.bulletinsTableNo{float:left:width:60px}.bulletinsTableStore{float:left:width:340px}.bulletinsTableFile{margin:0 auto;float:left:width:130px}.bulletinsTableContent{height:30px;line-height:30px;color:#000;background-color:#FFF;font-size:14px;text-align:center}.bulletinsTableFile IMG{margin-top:2px}]]></STYLE>
			<SCRIPT src="//ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"/>
			<SCRIPT src="/SCRIPT/unslider.min.js"/>
			<SCRIPT src="/SCRIPT/default.js"/>
			<TITLE>旅途國際 &#187; 公佈欄</TITLE>
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
					<H1>公佈欄</H1>
					<TABLE style="margin-top:40px;margin-left:100px;width:540px">
						<TBODY>
							<TR>
								<TD style="height:22px;line-height:22px;background-color:#EF7E00;font-size:14px;text-align:center">
									<DIV class="bulletinsTableNo">編號</DIV>
									<DIV class="bulletinsTableStore">檔案名稱</DIV>
									<DIV class="bulletinsTableFile">下載</DIV>
								</TD>
							</TR>
							<xsl:for-each select="list/*">
								<TR>
									<TD class="bulletinsTableContent">
										<DIV class="bulletinsTableNo">
											<xsl:value-of select="position()"/>
										</DIV>
										<DIV class="bulletinsTableStore" title="{@id}">
											<xsl:value-of select="."/>
										</DIV>
										<DIV class="bulletinsTableFile">
											<A href="/bulletin/{.}">
												<IMG class="downloadIconPdf" alt="pdf" src="/IMG/imgPDF.png"/>
											</A>
										</DIV>
									</TD>
								</TR>
							</xsl:for-each>
						</TBODY>
					</TABLE>
				</DIV>
			</DIV>
			<xsl:call-template name="footer"/>
		</BODY>
	</xsl:template>

</xsl:stylesheet>