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

	<!--分頁選擇器-->
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

	<!--手風琴(側邊選單)-->
	<xsl:template name="aside">
		<ASIDE>
			<H3>微網店家</H3>
			<DIV>
				<OL>
					<LI>
						<A href="/signUp/vendor/step1.php">步驟一</A>
					</LI>
					<LI>
						<A href="/signUp/vendor/step2.php">步驟二</A>
					</LI>
					<LI>
						<A href="/signUp/vendor/step3.php">步驟三</A>
					</LI>
				</OL>
			</DIV>
			<H3>業務團隊</H3>
			<DIV>
				<OL>
					<LI>
						<A href="/signUp/agent/step1.php">步驟一</A>
					</LI>
					<LI>
						<A href="/signUp/agent/step2.php">步驟二</A>
					</LI>
					<LI>
						<A href="/signUp/agent/step3.php">步驟三</A>
					</LI>
				</OL>
			</DIV>
			<H3>店家廣告</H3>
			<DIV>
				<OL>
					<LI>
						<A href="/signUp/advertiser/step1.php">步驟一</A>
					</LI>
					<LI>
						<A href="/signUp/advertiser/step2.php">步驟二</A>
					</LI>
					<LI>
						<A href="/signUp/advertiser/step3.php">步驟三</A>
					</LI>
				</OL>
			</DIV>
			<H3>其它</H3>
			<DIV>
				<OL>
					<LI>
						<A href="/youtube/">影片介紹</A>
					</LI>
					<LI>
						<A href="/frequentlyAskedQuestion/">問題Q&#38;A</A>
					</LI>
					<LI>
						<A href="/bulletin/">公佈欄</A>
					</LI>
					<LI>
						<A href="/announcement/">活動消息</A>
					</LI>
					<LI>
						<A href="/administrator/">管理者</A>
					</LI>
				</OL>
			</DIV>
		</ASIDE>
		<!--<IMG src="/IMG/135x47.png" alt="logo"/>-->
	</xsl:template>

	<!--簽名-->
	<xsl:template name="signature">
		<xsl:comment>P-C Lin (a.k.a 高科技黑手)</xsl:comment>
	</xsl:template>

</xsl:stylesheet>