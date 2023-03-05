<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<xsl:output
		method="html"
		encoding="UTF-8"
		omit-xml-declaration="yes"
		indent="no"
		media-type="text/html"
	/>

	<!--下拉式選單群組-->
	<xsl:template match="optgroup">
		<OPTGROUP label="{@label}">
			<xsl:apply-templates/>
		</OPTGROUP>
	</xsl:template>

	<!--下拉式選單選項-->
	<xsl:template match="option">
		<OPTION value="{@value}">
			<xsl:if test="@selected">
				<xsl:attribute name="selected"/>
			</xsl:if>
			<xsl:value-of select="." disable-output-escaping="yes"/>
		</OPTION>
	</xsl:template>

	<!--分頁選擇器
	<xsl:template match="pagination">
		<xsl:if test="@previous">
			<xsl:if test="@first">
				<A class="button fa fa-fast-backward paginate" tabindex="{@first}" title="第一頁"/>
			</xsl:if>
			<A class="button fa fa-backward paginate" tabindex="{@previous}" title="上一頁"/>
		</xsl:if>
		<SPAN style="margin:0 3px">
			<LABEL>
				<SPAN>每頁</SPAN>
				<INPUT class="numeric" maxlength="2" name="s" type="text" value="{@size}"/>
				<SPAN>筆</SPAN>
			</LABEL>
			<SPAN>；</SPAN>
			<LABEL>
				<SPAN>第</SPAN>
				<SELECT name="p">
					<xsl:apply-templates select="*"/>
				</SELECT>
				<SPAN>&#47;</SPAN>
				<SPAN>
					<xsl:value-of select="@totalPages"/>
				</SPAN>
				<SPAN>頁</SPAN>
			</LABEL>
			<SPAN>&#40;共</SPAN>
			<SPAN>
				<xsl:value-of select="@totalElements"/>
			</SPAN>
			<SPAN>筆&#41;</SPAN>
		</SPAN>
		<xsl:if test="@next">
			<A class="button fa fa-forward paginate" tabindex="{@next}" title="下一頁"/>
			<xsl:if test="@last">
				<A class="button fa fa-fast-forward paginate" tabindex="{@last}" title="最後頁"/>
			</xsl:if>
		</xsl:if>
	</xsl:template>
	-->

	<!--導覽列-->
	<xsl:template name="header">
		<HEADER>
			<DIV class="headerTop">
				<DIV class="logo">
					<A href="http://tour-map.net/" target="_blank">
						<IMG alt="瞜苟" src="/IMG/logo.png"/>
					</A>
				</DIV>
				<DIV class="headerLeft">
					<DIV class="headerLeftTop">
						<DIV class="headerName">
							<IMG alt="旅途觀止國際行銷雲端平台" src="/IMG/title.png"/>
						</DIV>
						<DIV class="headerSearch">
							<xsl:element name="gcse:searchbox-only" namespace=""/>
						</DIV>
						<DIV class="headerButton01">
							<A href="https://www.facebook.com/T0965582823" target="_blank">
								<IMG alt="臉書" src="/IMG/fbBtn.png"/>
							</A>
						</DIV>
						<DIV class="headerButton02">
							<xsl:choose>
								<xsl:when test="/document/@me">
									<A href="/logOut.asp">
										<IMG alt="登出" src="/IMG/logOut.png"/>
									</A>
								</xsl:when>
								<xsl:otherwise>
									<A href="/logIn.asp">
										<IMG alt="登入" src="/IMG/logIn.png"/>
									</A>
								</xsl:otherwise>
							</xsl:choose>
						</DIV>
					</DIV>
					<NAV>
						<A href="/">首頁</A>
						<A href="/announcement/">活動消息</A>
						<A href="/signUp/vendor.asp">開店</A>
						<A href="/signUp/agent.asp">加入業務團隊</A>
						<A href="/signUp/advertiser.asp">加入旅圖廣告</A>
						<!--<A href="/finance/">國際金融</A>-->
						<A href="">國際金融</A>
						<!--<A href="/others/">其他產品</A>-->
						<A href="">其他產品</A>
						<A href="/contactUs.asp">連絡我們</A>
						<!--<A href="http://tour-map.net/" target="_blank">旅圖觀止</A>-->
					</NAV>
				</DIV>
			</DIV>
		</HEADER>
	</xsl:template>

	<!--橫幅-->
	<xsl:template name="banner">
		<DIV class="headerBanner">
			<UL>
				<LI>
					<IMG alt="橫幅1" src="/banner01.png"/>
				</LI>
				<LI>
					<IMG alt="橫幅2" src="/banner02.png"/>
				</LI>
			</UL>
		</DIV>
	</xsl:template>

	<!--QR-Code 群-->
	<xsl:template name="qrCode">
		<DIV class="leftBar">
			<P>
				<IMG alt="QR-Code 快速加入" src="/IMG/qrcodeArea.png"/>
			</P>

			<A href="/signUp/vendor.asp">
				<IMG alt="加入微網店家" src="/IMG/qrcode01.png"/>
			</A>
			<UL>
				<LI>
					<A href="/signUp/vendor.asp">加入微網店家</A>
				</LI>
			</UL>

			<A href="/signUp/agent.asp">
				<IMG alt="加入業務團隊" src="/IMG/qrcode02.png"/>
			</A>
			<UL>
				<LI>
					<A href="/signUp/agent.asp">加入業務團隊</A>
				</LI>
			</UL>

			<A href="/signUp/advertiser.asp">
				<IMG alt="加入旅圖廣告" src="/IMG/qrcode03.png"/>
			</A>
			<UL>
				<LI>
					<A href="/signUp/advertiser.asp">加入旅圖廣告</A>
				</LI>
			</UL>

			<A href="">
				<IMG alt="國際金融" src="/IMG/qrcode04.png"/>
			</A>
			<UL>
				<LI>
					<!--<A href="/finance/">加入國際金融</A>-->
					<A href="">國際金融</A>
				</LI>
			</UL>

			<A href="">
				<IMG alt="其他產品" src="/IMG/qrcode05.png"/>
			</A>
			<UL>
				<LI>
					<!--<A href="/others/">購買其他產品</A>-->
					<A href="">其他產品</A>
				</LI>
			</UL>
		</DIV>
	</xsl:template>

	<!--簽名-->
	<xsl:template name="footer">
		<FOOTER class="bottom">
			<TABLE style="width:100%;color:#FFF;background-color:#222222">
				<TBODY>
					<TR>
						<TD>
							<SPAN>LINE</SPAN>
							<P class="zeroMargin">
								<IMG alt="" src="/IMG/qrLINE.png" width="120" height="120"/>
							</P>
							<SPAN>0965582823</SPAN>
						</TD>
						<TD>
							<SPAN>Line@生活圈</SPAN>
							<P class="zeroMargin">
								<IMG alt="" src="/IMG/qrLINEa.png" width="120" height="120"/>
							</P>
							<SPAN>@t0965582823</SPAN>
						</TD>
						<TD>
							<SPAN>WeChat</SPAN>
							<P class="zeroMargin">
								<IMG alt="" src="/IMG/qrWeChat.png" width="120" height="120"/>
							</P>
							<SPAN>T0965582823</SPAN>
						</TD>
						<TD>
							<SPAN>QQ</SPAN>
							<P class="zeroMargin">
								<IMG alt="" src="/IMG/qrQQ.png" width="120" height="120"/>
							</P>
							<SPAN>2680946911</SPAN>
						</TD>
						<TD>
							<SPAN>Facebook</SPAN>
							<P class="zeroMargin">
								<A href="https://www.facebook.com/T0965582823" target="_blank">
									<IMG alt="臉書" src="/IMG/fbHuge.png" width="120" height="120"/>
								</A>
							</P>
							<SPAN>&#160;</SPAN>
						</TD>
					</TR>
					<TR>
						<TD colspan="5">
							<P>地址：苗栗市恭敬里聯大1號(產研創新暨推廣大樓5樓)&#160;電話：+886-37-351251&#160;傳真：+886-37-353127</P>
						</TD>
					</TR>
				</TBODY>
			</TABLE>
			<!--
			<DIV class="bottomInfo">
				<P>地址：苗栗市恭敬里聯大1號(產研創新暨推廣大樓5樓)&#160;電話：+886-37-351251&#160;傳真：+886-37-353127</P>
			</DIV>
			-->
			<DIV>
				<P>旅圖觀止國際行銷股份有限公司 版權所有 &#169; 2015 Tour Map Gps All Rights Reserved.</P>
			</DIV>
		</FOOTER>
	</xsl:template>

	<!--簽名-->
	<xsl:template name="signature">
		<xsl:comment>P-C Lin (a.k.a 高科技黑手)</xsl:comment>
	</xsl:template>

</xsl:stylesheet>