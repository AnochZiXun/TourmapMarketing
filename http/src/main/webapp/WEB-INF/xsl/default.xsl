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
			<STYLE><![CDATA[.bulletins{margin-top:-25px;margin-right:15px;float:right}A.frequentlyAskedQuestion{cursor:help}]]></STYLE>
			<SCRIPT src="//ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"/>
			<SCRIPT src="/SCRIPT/unslider.min.js"/>
			<SCRIPT src="/SCRIPT/default.js"/>
			<SCRIPT src="/SCRIPT/welcome.js"/>
			<TITLE>旅途觀止國際行銷雲端平台</TITLE>
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
				<DIV class="rightContentVideo">
					<DIV id="mediaVideo">
						<IFRAME id="youtube" frameborder="0" src="{youtubes/@first}" allowfullscreen="" width="375" height="233"/>
					</DIV>
					<H1>旅圖觀止國際行銷相關影片介紹</H1>
					<P>如欲了解請點選以下列表: </P>
					<OL>
						<xsl:for-each select="youtubes/*">
							<LI>
								<A href="{@url}">
									<xsl:value-of select="."/>
								</A>
							</LI>
						</xsl:for-each>
					</OL>
					<P>&#160;</P>
				</DIV>
				<HR/>
				<DIV id="rightContent2">
					<IMG class="rightContentH1Img" alt="活動消息" src="/IMG/gearIcon.png"/>
					<H1>活動消息/公佈欄須登入才能觀看</H1>
					<A href="/bulletin/">
						<IMG class="bulletins" alt="公佈欄" src="/IMG/bulletins.png"/>
					</A>
					<OL>
						<xsl:for-each select="announcements/*">
							<LI>
								<A href="/announcement/{@id}.asp">
									<xsl:value-of select="."/>
								</A>
							</LI>
						</xsl:for-each>
					</OL>
					<DIV class="contentMore">
						<A href="/announcement/">更多其他活動消息 &#187;</A>
					</DIV>
				</DIV>
				<HR/>
				<DIV id="CON2">
					<IMG class="rightContentH1Img" alt="問與答" src="/IMG/qaIcon.png"/>
					<H1>問題Q&#38;A</H1>
					<OL>
						<xsl:for-each select="frequentlyAskedQuestions/*">
							<LI>
								<A class="frequentlyAskedQuestion">
									<xsl:value-of select="question"/>
								</A>
								<P>
									<xsl:value-of select="answer"/>
								</P>
							</LI>
						</xsl:for-each>
					</OL>
					<DIV class="contentMore">
						<A href="/frequentlyAskedQuestion/">更多其他Q&#38;A &#187;</A>
					</DIV>
				</DIV>
				<HR/>
				<DIV>
					<IMG class="rightContentH1Img" alt="意見回饋" src="/IMG/penIcon.png"/>
					<H1>意見回饋</H1>
					<P>&#160;</P>
					<FORM class="backForm" action="/feedback.asp" method="POST">
						<TABLE>
							<TBODY>
								<TR>
									<TD>
										<LABEL for="fullname">姓名：</LABEL>
									</TD>
									<TD>
										<INPUT class="textfield" id="fullname" name="fullname" type="text"/>
									</TD>
								</TR>
								<TR>
									<TD>
										<LABEL for="contactNumber">聯絡電話：</LABEL>
									</TD>
									<TD>
										<INPUT class="textfield" id="contactNumber" name="contactNumber" type="text"/>
									</TD>
								</TR>
								<TR>
									<TD>
										<LABEL for="email">電子信箱：</LABEL>
									</TD>
									<TD>
										<INPUT class="textfield" id="email" name="email" type="text"/>
									</TD>
								</TR>
								<TR>
									<TD>
										<LABEL for="textarea">意見回饋：</LABEL>
									</TD>
									<TD>
										<TEXTAREA class="textfield" id="textarea" style="width:100%;height:240px" name="textarea" cols="1" rows="1"/>
									</TD>
								</TR>
								<TR>
									<TD style="text-align:center" colspan="2">
										<INPUT style="border:0;width:71px;height:24px;background-image:url(IMG/submitBtn.png);cursor:pointer" type="submit" value=""/>
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