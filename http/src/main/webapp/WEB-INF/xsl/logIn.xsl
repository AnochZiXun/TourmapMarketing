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
			<LINK href="/STYLE/signUp.css" media="all" rel="stylesheet" type="text/css"/>
			<SCRIPT src="//ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"/>
			<SCRIPT src="/SCRIPT/unslider.min.js"/>
			<SCRIPT src="/SCRIPT/default.js"/>
			<SCRIPT src="/SCRIPT/signUpAgent.js"/>
			<TITLE>旅途國際 &#187; 登入</TITLE>
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
						<H1>登入</H1>
						<FORM action="/logIn.asp" method="POST">
							<TABLE style="margin:0 auto">
								<xsl:if test="errorMessage">
									<CAPTION>
										<xsl:value-of select="errorMessage"/>
									</CAPTION>
								</xsl:if>
								<TR>
									<TD style="text-align:center" colspan="2">
										<SELECT name="role" required="" title="選擇登入身份">
											<OPTION value="vendor">微網店家</OPTION>
											<OPTION value="agent">業務團隊</OPTION>
											<OPTION value="advertiser">旅圖廣告</OPTION>
										</SELECT>
									</TD>
								</TR>
								<TR>
									<TH class="must">
										<LABEL for="login">帳號</LABEL>
									</TH>
									<TD>
										<INPUT id="login" name="login" required="" type="text" value="{login}"/>
									</TD>
								</TR>
								<TR>
									<TH class="must">
										<LABEL for="shadow">密碼</LABEL>
									</TH>
									<TD>
										<INPUT id="shadow" name="shadow" required="" type="password"/>
									</TD>
								</TR>
								<TR>
									<TD style="text-align:center" colspan="2">
										<A href="javascript:void(0)" target="_blank">忘記密碼查詢</A>
										<INPUT class="theButton" style="float:left;background-color:#2EBDFB" type="reset" value="重填"/>
										<INPUT class="theButton" style="float:right" type="submit" value="確定送出"/>
									</TD>
								</TR>
							</TABLE>
						</FORM>
					</DIV>
				</DIV>
			</DIV>
			<xsl:call-template name="footer"/>
		</BODY>
	</xsl:template>

</xsl:stylesheet>