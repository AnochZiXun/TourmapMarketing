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
			<SCRIPT src="//ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"/>
			<SCRIPT src="/SCRIPT/unslider.min.js"/>
			<SCRIPT src="/SCRIPT/default.js"/>
			<TITLE>旅途國際 &#187; 連絡我們</TITLE>
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
					<IMG class="rightContentH1Img" alt="連絡我們" src="/IMG/penIcon.png"/>
					<H1>連絡我們</H1>
					<P>&#160;</P>
					<FORM class="backForm" action="/contactUs.asp" method="POST">
						<TABLE>
							<CAPTION style="margin:8px auto;color:C00;font-size:120%">
								<xsl:value-of select="form/@errorMessage"/>
							</CAPTION>
							<TBODY>
								<TR>
									<TD>
										<LABEL for="fullname">姓名：</LABEL>
									</TD>
									<TD>
										<INPUT class="textfield" id="fullname" name="fullname" type="text" value="{form/fullname}"/>
									</TD>
								</TR>
								<TR>
									<TD>
										<LABEL for="company">公司：</LABEL>
									</TD>
									<TD>
										<INPUT class="textfield" id="company" name="company" type="text" value="{form/company}"/>
									</TD>
								</TR>
								<TR>
									<TD>
										<LABEL for="contactNumber">聯絡電話：</LABEL>
									</TD>
									<TD>
										<INPUT class="textfield" id="contactNumber" name="contactNumber" type="text" value="{form/contactNumber}"/>
									</TD>
								</TR>
								<TR>
									<TD>
										<LABEL for="email">電子信箱：</LABEL>
									</TD>
									<TD>
										<INPUT class="textfield" id="email" name="email" type="text" value="{form/email}"/>
									</TD>
								</TR>
								<TR>
									<TD>
										<LABEL for="content">連絡內容：</LABEL>
									</TD>
									<TD>
										<TEXTAREA class="textfield" id="content" style="width:100%;height:240px" name="content" cols="1" rows="1">
											<xsl:value-of select="form/content"/>
										</TEXTAREA>
									</TD>
								</TR>
								<TR>
									<TD style="text-align:center" colspan="2">
										<INPUT style="border:0;width:71px;height:24px;background-image:url(/IMG/submitBtn.png);cursor:pointer" type="submit" value=""/>
									</TD>
								</TR>
							</TBODY>
						</TABLE>
					</FORM>
				</DIV>
			</DIV>
			<xsl:call-template name="footer"/>
		</BODY>
	</xsl:template>

</xsl:stylesheet>