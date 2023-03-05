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
			<SCRIPT src="/SCRIPT/vendorManagement.js"/>
			<TITLE>旅途國際 &#187; 微網店家後臺</TITLE>
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
								<SPAN>店家後台管理(編修網頁、上架、核銷、報表)</SPAN>
								<A class="button pinkButton" href="manage.asp">管理</A>
							</LI>
							-->
							<LI>
								<!--<SPAN>店家歐付寶帳號登入</SPAN>-->
								<SPAN>微網店家後臺管理</SPAN>
								<A class="button greenButton" href="http://tour-map.net/company_login.php" target="_blank">登入</A>
							</LI>
							<LI>
								<SPAN>繳費資訊(貨幣單位: 新台幣)</SPAN>
								<TABLE class="heWeiKuan">
									<THEAD>
										<TR>
											<TH>NO</TH>
											<TH>項目</TH>
											<TH>失效日期</TH>
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
												<xsl:choose>
													<xsl:when test="oneTimeCharge/@paid='true'">
														<A class="button blueButton">已繳</A>
													</xsl:when>
													<xsl:otherwise>
														<FORM id="oneTimeCharge" action="{oneTimeCharge}" method="POST">
															<INPUT name="MerchantID" type="hidden" value="{oneTimeCharge/@merchantID}"/>
															<INPUT id="oneTimeChargeMerchantTradeNo" name="MerchantTradeNo" type="hidden"/>
															<INPUT id="oneTimeChargeMerchantTradeDate" name="MerchantTradeDate" type="hidden"/>
															<INPUT name="PaymentType" type="hidden" value="aio"/>
															<INPUT name="TotalAmount" type="hidden" value="{oneTimeCharge/@amount}"/>
															<INPUT name="TradeDesc" type="hidden" value="旅圖國際「微網店家」一次性網頁製作費"/>
															<INPUT name="ItemName" type="hidden" value="微網店家的一次性網頁製作費"/>
															<INPUT name="ReturnURL" type="hidden" value="{oneTimeCharge/@returnUrl}"/>
															<INPUT name="ChoosePayment" type="hidden" value="ALL"/>
															<INPUT id="oneTimeChargeCheckMacValue" name="CheckMacValue" type="hidden"/>
															<INPUT class="button blueButton" style="cursor:pointer" type="button" value="待繳"/>
														</FORM>
													</xsl:otherwise>
												</xsl:choose>
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
												<xsl:choose>
													<xsl:when test="monthlyCharge/@paid='true'">
														<A class="button blueButton">已繳</A>
													</xsl:when>
													<xsl:otherwise>
														<FORM id="monthlyCharge" action="{monthlyCharge}" method="POST">
															<INPUT name="MerchantID" type="hidden" value="{monthlyCharge/@merchantID}"/>
															<INPUT id="monthlyChargeMerchantTradeNo" name="MerchantTradeNo" type="hidden"/>
															<INPUT id="monthlyChargeMerchantTradeDate" name="MerchantTradeDate" type="hidden"/>
															<INPUT name="PaymentType" type="hidden" value="aio"/>
															<INPUT name="TotalAmount" type="hidden" value="{monthlyCharge/@amount}"/>
															<INPUT name="TradeDesc" type="hidden" value="旅圖國際「微網店家」每月上架費"/>
															<INPUT name="ItemName" type="hidden" value="微網店家的每月上架費"/>
															<INPUT name="ReturnURL" type="hidden" value="{monthlyCharge/@returnUrl}"/>
															<INPUT name="ChoosePayment" type="hidden" value="ALL"/>
															<INPUT id="monthlyChargeCheckMacValue" name="CheckMacValue" type="hidden"/>
															<INPUT class="button blueButton" style="cursor:pointer" type="button" value="待繳"/>
														</FORM>
													</xsl:otherwise>
												</xsl:choose>
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