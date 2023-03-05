<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<xsl:output
		method="html"
		encoding="UTF-8"
		omit-xml-declaration="yes"
		indent="no"
		media-type="text/html"
	/>

	<xsl:template match="/">
		<xsl:apply-templates select="messageBody"/>
	</xsl:template>

	<xsl:template match="messageBody">
		<DIV style="padding-bottom:25px;width:600px;background-color:#373737">
			<DIV>
				<DIV style="float:left;width:50%;height:180px;text-align:center">
					<IMG style="margin:44px auto;width:175px" alt="logo" src="http://tour-map.net/image/tourLogo.jpg"/>
				</DIV>
				<DIV style="float:right;width:50%;line-height:180px;vertical-align:middle;color:#FFF;font-size:36px;text-align:center">註冊通知信</DIV>
				<DIV style="float:none;clear:both">&#160;</DIV>
			</DIV>
			<DIV style="margin:0 auto;border:2px solid #EF7E00;width:93%;height:870px">
				<H1 style="line-height:65px;color:#FFF;font-size:22px;text-align:center">歡迎加入微網店家！</H1>
				<P style="margin-right:auto;margin-left:auto;width:470px;line-height:26px;color:#FFF;font-size:14px;text-align:left">敬愛的<xsl:value-of select="name"/>　您好！<BR/>感謝您加入旅圖觀止電子商務平台～開店，請確認您的註冊資料：</P>
				<TABLE style="margin:0 auto;width:490px">
					<TBODY>
						<TR>
							<TD style="width:120px;line-height:38px;background-color:#CACAC9;text-align:center">公司名稱</TD>
							<TD style="line-height:38px;background-color:#E5E5E5;text-align:center" colspan="3">
								<xsl:value-of select="name"/>
							</TD>
						</TR>
						<TR>
							<TD style="width:120px;line-height:38px;background-color:#CACAC9;text-align:center">公司地址</TD>
							<TD style="line-height:38px;background-color:#E5E5E5;text-align:center" colspan="3">
								<xsl:value-of select="address"/>
							</TD>
						</TR>
						<TR>
							<TD style="width:120px;line-height:38px;background-color:#CACAC9;text-align:center">統一編號</TD>
							<TD style="line-height:38px;background-color:#E5E5E5;text-align:center">
								<xsl:value-of select="businessIdentificationNumber"/>
							</TD>
							<TD style="width:120px;line-height:38px;background-color:#CACAC9;text-align:center">聯絡人</TD>
							<TD style="line-height:38px;background-color:#E5E5E5;text-align:center">
								<xsl:value-of select="contact"/>
							</TD>
						</TR>
						<TR>
							<TD style="width:120px;line-height:38px;background-color:#CACAC9;text-align:center">電　　話</TD>
							<TD style="line-height:38px;background-color:#E5E5E5;text-align:center">
								<xsl:value-of select="phone"/>
							</TD>
							<TD style="width:120px;line-height:38px;background-color:#CACAC9;text-align:center">傳　真</TD>
							<TD style="line-height:38px;background-color:#E5E5E5;text-align:center">
								<xsl:value-of select="fax"/>
							</TD>
						</TR>
						<TR>
							<TD style="width:120px;line-height:38px;background-color:#CACAC9;text-align:center">帳　　號</TD>
							<TD style="line-height:38px;background-color:#E5E5E5;text-align:center" colspan="3">
								<xsl:value-of select="email"/>
							</TD>
						</TR>
						<TR>
							<TD style="width:120px;line-height:38px;background-color:#CACAC9;text-align:center">密碼</TD>
							<TD style="line-height:38px;background-color:#E5E5E5;text-align:center" colspan="3">
								<xsl:value-of select="shadow"/>
							</TD>
						</TR>
						<TR>
							<TD style="width:120px;line-height:38px;background-color:#CACAC9;text-align:center">產品歸類</TD>
							<TD style="line-height:38px;background-color:#E5E5E5;text-align:center" colspan="3">
								<xsl:value-of select="category"/>
							</TD>
						</TR>
						<TR>
							<TD colspan="4" style="padding:0px 10px 5px 20px;background-color:#E5E5E5;font-size:16px">
								<P style="line-height:48px">操作說明：</P>
								<OL>
									<LI style="line-height:26px">貴司已完成開店註冊，旅圖平台將於7日內完成初審，通過初審之後，旅圖平台將以Email通知貴司，同時開通網頁後台供開店上架管理等。</LI>
									<LI style="line-height:26px">請於初審通過後再以註冊完成之「帳號」、「密碼」在首頁登入即可進行網頁製作，產品上架，更修內容，查詢訂單、報表、核銷、對帳…等作業管理。</LI>
									<LI style="line-height:26px">店家與旅圖平台之互動關係請以帳號，密碼進入並詳閱「公佈欄」隨時查詢最新內容。</LI>
									<LI style="line-height:26px">請妥當保管帳號、密碼以保障貴司之權益。</LI>
								</OL>
								<P style="line-height:48px">旅圖觀止旅行社股份有限公司　敬啟</P>
							</TD>
						</TR>
					</TBODY>
				</TABLE>
			</DIV>
		</DIV>
	</xsl:template>

</xsl:stylesheet>