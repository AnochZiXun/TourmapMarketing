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
			<LINK href="//ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/themes/ui-darkness/jquery-ui.css" rel="stylesheet" media="all" type="text/css"/>
			<LINK href="/STYLE/default.css" media="all" rel="stylesheet" type="text/css"/>
			<LINK href="/STYLE/signUp.css" media="all" rel="stylesheet" type="text/css"/>
			<SCRIPT src="//ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"/>
			<SCRIPT src="//ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/jquery-ui.min.js"/>
			<SCRIPT src="//ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/i18n/jquery-ui-i18n.min.js"/>
			<SCRIPT src="/SCRIPT/unslider.min.js"/>
			<SCRIPT src="/SCRIPT/default.js"/>
			<SCRIPT src="/SCRIPT/signUp.js"/>
			<TITLE>旅途國際 &#187; 加入微網店家</TITLE>
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
					<DIV id="step1">
						<xsl:if test="hideStep1">
							<xsl:attribute name="style">display:none</xsl:attribute>
						</xsl:if>
						<H1>加入微網店家說明</H1>
						<DIV>
							<xsl:value-of select="step1Html" disable-output-escaping="yes"/>
						</DIV>
						<P style="text-align:center">
							<LABEL>
								<INPUT id="agreeToStep2" type="checkbox"/>
								<SPAN>我同意！</SPAN>
							</LABEL>
							<A class="theButton" id="goToStep2">確定送出</A>
						</P>
					</DIV>
					<DIV id="step2">
						<xsl:if test="not(hideStep1)">
							<xsl:attribute name="style">display:none</xsl:attribute>
						</xsl:if>
						<H1>填寫公司資料</H1>
						<H2>填寫公司基本資料內容</H2>
						<FORM action="/signUp/vendor.asp" method="POST" enctype="multipart/form-data">
							<TABLE>
								<xsl:if test="errorMessage">
									<CAPTION>
										<xsl:value-of select="errorMessage"/>
									</CAPTION>
								</xsl:if>
								<TR>
									<TH class="must">
										<LABEL for="name">公司名稱</LABEL>
									</TH>
									<TD>
										<INPUT id="name" name="name" required="" type="text" value="{form/name}"/>
									</TD>
									<TH class="must">
										<LABEL for="businessIdentificationNumber">統編</LABEL>
									</TH>
									<TD>
										<INPUT id="businessIdentificationNumber" maxlength="8" name="businessIdentificationNumber" required="" type="text" value="{form/businessIdentificationNumber}"/>
									</TD>
								</TR>
								<TR>
									<TH class="must">
										<LABEL for="phone">電話</LABEL>
									</TH>
									<TD>
										<INPUT id="phone" name="phone" required="" type="text" value="{form/phone}"/>
									</TD>
									<TH>
										<LABEL for="fax">傳真</LABEL>
									</TH>
									<TD>
										<INPUT id="fax" name="fax" type="text" value="{form/fax}"/>
									</TD>
								</TR>
								<TR>
									<TH>
										<LABEL for="address">公司地址</LABEL>
									</TH>
									<TD colspan="3">
										<INPUT id="address" name="address" type="text" value="{form/address}"/>
									</TD>
								</TR>
								<TR>
									<TH class="must">
										<LABEL for="contact">聯絡人</LABEL>
									</TH>
									<TD colspan="3">
										<INPUT id="contact" name="contact" required="" type="text" value="{form/contact}"/>
									</TD>
								</TR>
								<TR>
									<TH class="must">
										<LABEL for="email">電子信箱</LABEL>
									</TH>
									<TD colspan="3">
										<INPUT id="email" name="email" required="" type="text" value="{form/email}"/>
									</TD>
								</TR>
								<TR>
									<TH class="must">
										<LABEL for="category">主要營業項目</LABEL>
									</TH>
									<TD>
										<SELECT id="category" name="category">
											<xsl:apply-templates select="form/category/*"/>
										</SELECT>
									</TD>
									<TD colspan="2"/>
								</TR>
								<TR>
									<TH class="must">
										<LABEL for="shadow1">密碼設定</LABEL>
									</TH>
									<TD>
										<INPUT id="shadow1" name="shadow1" required="" type="password"/>
									</TD>
									<TH class="must">
										<LABEL for="shadow2">確認密碼</LABEL>
									</TH>
									<TD>
										<INPUT id="shadow2" name="shadow2" required="" type="password"/>
									</TD>
								</TR>
								<TR>
									<TD colspan="4"/>
								</TR>
								<TR>
									<TH>
										<LABEL for="certificateOfProfitSeekingEnterprise">營業登記證圖檔</LABEL>
									</TH>
									<TD colspan="3">
										<INPUT id="certificateOfProfitSeekingEnterprise" accept="image/jpeg,image/png" name="certificateOfProfitSeekingEnterprise" type="file"/>
									</TD>
								</TR>
								<TR>
									<TD/>
									<TD colspan="3">
										<DIV>營利事業登記證範例圖</DIV>
										<IMG alt="營利事業登記證範例圖" src="/IMG/certificateOfProfitSeekingEnterprise.png"/>
									</TD>
								</TR>
								<TR>
									<TH>
										<LABEL for="personInCharge">負責人身分證正面圖檔</LABEL>
									</TH>
									<TD colspan="3">
										<INPUT id="personInCharge" accept="image/jpeg,image/png" name="personInCharge" type="file"/>
									</TD>
								</TR>
								<TR>
									<TH>
										<LABEL for="personInCharge_">負責人身分證反面圖檔</LABEL>
									</TH>
									<TD colspan="3">
										<INPUT id="personInCharge_" accept="image/jpeg,image/png" name="personInCharge_" type="file"/>
									</TD>
								</TR>
								<TR>
									<TH>
										<LABEL for="statementCover">公司銀行存摺封面圖檔</LABEL>
									</TH>
									<TD colspan="3">
										<INPUT id="statementCover" accept="image/jpeg,image/png" name="statementCover" type="file"/>
									</TD>
								</TR>
								<TR>
									<TD style="text-align:center" colspan="4">
										<HR/>
										<P>105年12月31日之前千家加入「開店」免費&#160;<IMG style="vertical-align:middle" alt="" src="/IMG/freeJoin.png"/></P>
									</TD>
								</TR>
								<TR>
									<TD style="text-align:center" colspan="4">
										<HR/>
										<P>當您按下「確定送出」時即表示您已同意<A id="clickToAgree">線上合約法律條款說明</A>。</P>
										<DIV id="jDialog">
											<xsl:value-of select="step2Html" disable-output-escaping="yes"/>
										</DIV>
									</TD>
								</TR>
								<!--
								<TR>
									<TD colspan="4">
										<H1>線上合約法律條款說明</H1>
										<DIV>
											<xsl:value-of select="step2Html" disable-output-escaping="yes"/>
										</DIV>
									</TD>
								</TR>
								-->
								<TR>
									<TD style="text-align:center" colspan="4">
										<LABEL>
											<INPUT id="agreeAndSubmit" type="checkbox"/>
											<SPAN>我同意！</SPAN>
										</LABEL>
										<INPUT class="theButton" id="submit" type="submit" value="確定送出"/>
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