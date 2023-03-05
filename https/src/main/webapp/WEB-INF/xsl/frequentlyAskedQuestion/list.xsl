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
			<LINK href="//maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css" rel="stylesheet" media="all" type="text/css"/>
			<LINK href="/FONT/font-awesome.min.css" rel="stylesheet" media="all" type="text/css"/>
			<LINK href="/STYLE/default.css" rel="stylesheet" media="all" type="text/css"/>
			<LINK href="/STYLE/cPanel.css" rel="stylesheet" media="all" type="text/css"/>
			<SCRIPT src="//ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"/>
			<SCRIPT src="//ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/jquery-ui.min.js"/>
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
							<xsl:apply-templates select="list"/>
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

	<xsl:template match="list">
		<FORM id="pagination" action="" method="GET">
			<TABLE class="list">
				<THEAD>
					<TR>
						<TH>問題</TH>
						<TH>排序</TH>
						<TH/>
					</TR>
				</THEAD>
				<TFOOT>
					<TR>
						<TD colspan="3">
							<DIV class="fL">
								<A class="button" href="add.php">新增</A>
							</DIV>
							<DIV class="fR">
								<xsl:apply-templates select="../pagination"/>
							</DIV>
						</TD>
					</TR>
				</TFOOT>
				<TBODY>
					<xsl:for-each select="row">
						<TR>
							<xsl:if test="position()mod'2'='0'">
								<xsl:attribute name="class">even</xsl:attribute>
							</xsl:if>
							<TD>
								<xsl:value-of select="question"/>
							</TD>
							<TD class="taCenter">
								<A class="fa fa-chevron-circle-down post" title="下移" href="{@id}/down.json"/>
								<B>&#160;</B>
								<A class="fa fa-chevron-circle-up post" title="上移" href="{@id}/up.json"/>
							</TD>
							<TD class="taCenter">
								<A class="fa fa-edit" title="B 或者 C" href="{@id}/businessOrConsumer.json">
									<xsl:attribute name="class">
										<xsl:choose>
											<xsl:when test="consumer='true'">fa fa-shopping-basket post</xsl:when>
											<xsl:otherwise>fa fa-briefcase post</xsl:otherwise>
										</xsl:choose>
									</xsl:attribute>
								</A>
								<B>&#160;</B>
								<A class="fa fa-edit" title="修改" href="{@id}/"/>
								<B>&#160;</B>
								<A class="fa fa-times-circle post" title="刪除" href="{@id}/remove.json"/>
							</TD>
						</TR>
					</xsl:for-each>
				</TBODY>
			</TABLE>
		</FORM>
	</xsl:template>

</xsl:stylesheet>