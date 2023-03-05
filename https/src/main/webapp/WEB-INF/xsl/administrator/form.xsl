<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<xsl:output
		method="html"
		encoding="UTF-8"
		omit-xml-declaration="yes"
		indent="no"
		media-type="text/html"
	/>

	<xsl:import href="/cPanel.xsl"/>

	<xsl:template match="/">
		<xsl:text disable-output-escaping="yes">&#60;!DOCTYPE HTML&#62;</xsl:text>
		<HTML dir="ltr" lang="zh-TW">
			<xsl:apply-templates/>
		</HTML>
	</xsl:template>

	<xsl:template match="document">
		<HEAD>
			<META charset="UTF-8"/>
			<META name="viewport" content="width=device-width,initial-scale=1.0"/>
			<LINK href="//ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/themes/ui-darkness/jquery-ui.css" rel="stylesheet" media="all" type="text/css"/>
			<LINK href="/STYLE/default.css" rel="stylesheet" media="all" type="text/css"/>
			<LINK href="/STYLE/cPanel.css" rel="stylesheet" media="all" type="text/css"/>
			<SCRIPT src="//ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"/>
			<SCRIPT src="//ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/jquery-ui.min.js"/>
			<SCRIPT src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/i18n/jquery-ui-i18n.min.js"/>
			<SCRIPT src="/SCRIPT/cPanel.js"/>
			<TITLE>控制台 &#187; <xsl:value-of select="@title"/></TITLE>
		</HEAD>
		<xsl:comment>
			<xsl:value-of select="system-property('xsl:version')"/>
		</xsl:comment>
		<BODY>
			<TABLE id="cW">
				<TBODY>
					<TR>
						<TD>
							<xsl:call-template name="aside"/>
						</TD>
						<xsl:call-template name="signature"/>
						<TD>
							<xsl:apply-templates select="form"/>
						</TD>
					</TR>
				</TBODY>
			</TABLE>
			<NAV>
				<DIV class="fL">
					<A href="/">控制台首頁</A>
					<SPAN>&#187;</SPAN>
					<A>
						<xsl:value-of select="@title"/>
					</A>
				</DIV>
				<xsl:call-template name="signature"/>
				<DIV class="fR">
					<A>
						<xsl:value-of select="@myName"/>
					</A>
				</DIV>
			</NAV>
		</BODY>
	</xsl:template>

	<xsl:template match="form">
		<FORM action="{@action}" method="POST">
			<FIELDSET>
				<LEGEND>
					<xsl:value-of select="@legend"/>
				</LEGEND>
				<TABLE class="form">
					<xsl:if test="@errorMessage">
						<CAPTION>
							<xsl:value-of select="@errorMessage"/>
						</CAPTION>
					</xsl:if>
					<TBODY>
						<TR>
							<TH class="must">
								<LABEL for="{generate-id(alias)}">別名</LABEL>
							</TH>
							<TD>
								<INPUT id="{generate-id(alias)}" name="alias" required="" type="text" value="{alias}"/>
							</TD>
							<TD>必填。</TD>
						</TR>
						<TR>
							<TH class="must">
								<LABEL for="{generate-id(login)}">帳號</LABEL>
							</TH>
							<TD>
								<INPUT id="{generate-id(login)}" name="login" placeholder="電子郵件" required="" type="text" value="{login}"/>
							</TD>
							<TD>必填且不可重複。</TD>
						</TR>
						<!--
						<TR>
							<TH>
								<LABEL for="{generate-id(shadow)}">密碼</LABEL>
							</TH>
							<TD>
								<INPUT id="{generate-id(shadow)}" name="shadow" type="password" value="{shadow}"/>
							</TD>
							<TD>未填視同停用。</TD>
						</TR>
						-->
						<TR>
							<TD colspan="3">
								<INPUT class="fL" type="reset" value="復原"/>
								<xsl:call-template name="signature"/>
								<INPUT class="fR" type="submit" value="確定"/>
							</TD>
						</TR>
					</TBODY>
				</TABLE>
			</FIELDSET>
		</FORM>
	</xsl:template>

</xsl:stylesheet>