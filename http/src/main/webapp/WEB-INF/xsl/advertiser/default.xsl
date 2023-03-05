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
			<LINK href="/STYLE/management.css" media="all" rel="stylesheet" type="text/css"/>
			<SCRIPT src="//ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"/>
			<SCRIPT src="/SCRIPT/unslider.min.js"/>
			<SCRIPT src="/SCRIPT/default.js"/>
			<TITLE>旅途國際 &#187; 旅圖廣告後臺</TITLE>
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
					<DIV>
						<H1>
							<xsl:value-of select="name"/>
						</H1>
						<OL>
							<!--
							<LI>
								<SPAN>店家後台管理(編修網頁)</SPAN>
								<A class="button pinkButton" href="manage.asp">管理</A>
							</LI>
							-->
							<LI>
								<SPAN>繳費資訊(貨幣單位: 新台幣)</SPAN>
								<TABLE class="heWeiKuan">
									<THEAD>
										<TR>
											<TH>NO</TH>
											<TH>項目</TH>
											<TH>有效日期</TH>
											<TH>金額</TH>
											<TH>購買</TH>
										</TR>
									</THEAD>
									<TBODY>
										<TR>
											<TD>1</TD>
											<TD style="text-align:left">一次性網頁製作費</TD>
											<TD>(n/a)</TD>
											<TD>
												<xsl:value-of select="format-number(oneTimeCharge/@amount, '###,###')"/>
											</TD>
											<TD>
												<A class="button blueButton">
													<xsl:choose>
														<xsl:when test="oneTimeCharge/@paid='true'">
															<SPAN>已繳</SPAN>
														</xsl:when>
														<xsl:otherwise>
															<xsl:attribute name="href">oneTimeCharge.asp</xsl:attribute>
															<SPAN>待繳</SPAN>
														</xsl:otherwise>
													</xsl:choose>
												</A>
											</TD>
										</TR>
										<TR>
											<TD>2</TD>
											<TD style="text-align:left">每月上架費</TD>
											<TD>
												<xsl:value-of select="monthlyCharge/@until"/>
											</TD>
											<TD>
												<xsl:value-of select="format-number(monthlyCharge/@amount, '###,###')"/>
											</TD>
											<TD>
												<A class="button blueButton">
													<xsl:choose>
														<xsl:when test="monthlyCharge/@paid='true'">
															<SPAN>已繳</SPAN>
														</xsl:when>
														<xsl:otherwise>
															<xsl:attribute name="href">monthlyCharge.asp</xsl:attribute>
															<SPAN>待繳</SPAN>
														</xsl:otherwise>
													</xsl:choose>
												</A>
											</TD>
										</TR>
									</TBODY>
								</TABLE>
							</LI>
						</OL>
					</DIV>
				</DIV>
			</DIV>
			<xsl:call-template name="footer"/>
		</BODY>
	</xsl:template>

</xsl:stylesheet>