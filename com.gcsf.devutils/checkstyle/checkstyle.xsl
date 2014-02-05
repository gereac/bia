<?xml version="1.0" encoding="UTF-8"?>
<html xsl:version="2.0"
      xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
      xmlns:gcsf="java://com.gcsf.codingrules.XslFunctions">


<xsl:param name="css.filename" />
<xsl:param name="report.date" />
<xsl:param name="his.componentname" />

<head>
<title>GCSF Checkstyle Report: <xsl:value-of select="$his.componentname" /> (<xsl:value-of select="$report.date" />)</title>
<link rel="stylesheet" type="text/css" title="styles"><xsl:attribute name="href"><xsl:value-of select="$css.filename" /></xsl:attribute></link>
</head>
<body>
<p class="overview"><a href="index.html">Overview</a></p>
<h1>Checkstyle Report of <xsl:value-of select="$his.componentname" /></h1>
<p>Date: <xsl:value-of select="$report.date" /></p>
<hr/>
<xsl:for-each select="checkstyle/file">
<xsl:if test="error">
<p>
<span class="name"><xsl:value-of select="gcsf:replace(@name,'D:\\TASHMIDev\\ws-cfl\\src\\.*\\src\\','')" /></span>
<xsl:for-each select="error">
<br/>
<span><xsl:attribute name="class"><xsl:value-of select="gcsf:countSeverity($his.componentname,@severity)" /></xsl:attribute><xsl:value-of select="@line" /> / <xsl:value-of select="@message" /></span>
</xsl:for-each>
</p>
</xsl:if>
</xsl:for-each>
<p/>
</body>
</html>