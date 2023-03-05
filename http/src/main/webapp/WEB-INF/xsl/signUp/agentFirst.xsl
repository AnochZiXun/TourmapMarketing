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
			<SCRIPT src="/SCRIPT/signUpAgent.js"/>
			<TITLE>旅途國際 &#187; 加入業務團隊</TITLE>
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
						<H1>加入業務團隊說明</H1>
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
						<H1>填寫基本資料</H1>
						<FORM action="/signUp/agent.asp" method="POST" enctype="multipart/form-data">
							<TABLE>
								<xsl:if test="errorMessage">
									<CAPTION>
										<xsl:value-of select="errorMessage"/>
									</CAPTION>
								</xsl:if>
								<TR>
									<TD colspan="4">
										<H2>
											<LABEL>
												<INPUT name="individual" required="" type="radio" value="true">
													<xsl:if test="form/individual='true'">
														<xsl:attribute name="checked"/>
													</xsl:if>
												</INPUT>
												<SPAN>個人身份</SPAN>
											</LABEL>
											<LABEL>
												<INPUT name="individual" required="" type="radio" value="false">
													<xsl:if test="form/individual='false'">
														<xsl:attribute name="checked"/>
													</xsl:if>
												</INPUT>
												<SPAN>公司身份</SPAN>
											</LABEL>
										</H2>
									</TD>
								</TR>
								<TR>
									<TH class="must">
										<LABEL for="name">姓名或公司名</LABEL>
									</TH>
									<TD>
										<INPUT id="name" name="name" required="" type="text" value="{form/name}"/>
									</TD>
									<TH class="must">
										<LABEL for="cellular">行動電話</LABEL>
									</TH>
									<TD>
										<INPUT id="cellular" name="cellular" required="" type="text" value="{form/cellular}"/>
									</TD>
								</TR>
								<TR>
									<TH>
										<LABEL for="address">地址</LABEL>
									</TH>
									<TD colspan="3">
										<INPUT id="address" name="address" type="text" value="{form/address}">
											<xsl:if test="form/individual='true'">
												<xsl:attribute name="disabled"/>
											</xsl:if>
										</INPUT>
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
									<TH>
										<LABEL for="industry">從事產業別</LABEL>
									</TH>
									<TD>
										<INPUT id="industry" name="industry" type="text" value="{form/industry}"/>
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
										<INPUT id="certificateOfProfitSeekingEnterprise" accept="image/jpeg,image/png" name="certificateOfProfitSeekingEnterprise" type="file">
											<xsl:if test="form/individual='true'">
												<xsl:attribute name="disabled"/>
											</xsl:if>
										</INPUT>
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
										<INPUT id="personInCharge" accept="image/jpeg,image/png" name="personInCharge" type="file">
											<xsl:if test="form/individual='true'">
												<xsl:attribute name="disabled"/>
											</xsl:if>
										</INPUT>
									</TD>
								</TR>
								<TR>
									<TH>
										<LABEL for="personInCharge_">負責人身分證反面圖檔</LABEL>
									</TH>
									<TD colspan="3">
										<INPUT id="personInCharge_" accept="image/jpeg,image/png" name="personInCharge_" type="file">
											<xsl:if test="form/individual='true'">
												<xsl:attribute name="disabled"/>
											</xsl:if>
										</INPUT>
									</TD>
								</TR>
								<TR>
									<TD/>
									<TD colspan="3">
										<H2>請選擇</H2>
									</TD>
								</TR>
								<TR>
									<TH class="must">
										<SPAN>成為業務</SPAN>
									</TH>
									<TD colspan="3">
										<LABEL>
											<INPUT name="supervisor" required="" type="radio" value="true">
												<xsl:if test="form/supervisor='true'">
													<xsl:attribute name="checked"/>
												</xsl:if>
											</INPUT>
											<SPAN>A(業務處長)</SPAN>
										</LABEL>
										<LABEL>
											<INPUT name="supervisor" required="" type="radio" value="false">
												<xsl:if test="form/supervisor='false'">
													<xsl:attribute name="checked"/>
												</xsl:if>
											</INPUT>
											<SPAN>B(業務主任)</SPAN>
										</LABEL>
									</TD>
								</TR>
								<TR>
									<TD style="text-align:center" colspan="4">
										<IMG id="supervisor" alt="業務條件與收支試算">
											<xsl:attribute name="src">
												<xsl:choose>
													<xsl:when test="form/supervisor='true'">&#47;IMG&#47;spreadsheetSupervisor&#46;png</xsl:when>
													<xsl:otherwise>&#47;IMG&#47;spreadsheetAgent&#46;png</xsl:otherwise>
												</xsl:choose>
											</xsl:attribute>
										</IMG>
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