<?xml version="1.0" encoding="UTF-8"?>
<html xsl:version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<head>
<title>HMI Codingrules</title>
<style type="text/css">
body {
  margin:           10px;
  background-color: #FFFFFF;
  font-family:      Arial, Helvetica, sans-serif;
  font-size:        11px;
  font-weight:      normal;
}

.info {
  font-size:        11px;
}

.mono {
  font-family:      Courier, monospace;
  font-size:        9px;
  font-weight:      bold;
}

table {
  border: 1px solid #999999;
    font-size:       11px; 
}

tr {
  border: 1px solid #999999;
}

th {
  text-align: center;
  font-weight: bold;
  color:       white;
  background-color: #999999;
  padding-left: 5px;
  padding-right: 5px;
}

td {
  color:       black;
  padding-left: 5px;
  padding-right: 5px;
  border-bottom: 1px solid #999999
}

</style>
</head>
<body>
<div class="info">This set of codingrules presents the checkstyle configuration used with this release</div>  
<div class="info">action have to be taken depending on severity level:
<ul>
  <li>error - to be analyzed and fixed</li>
  <li>warning - this may impact future usage - to be fixed if source is modified next time</li>
  <li>info - reported for usage with other measures only</li>  
</ul>
</div>
<table>
	<tr>
		<th width="100">id</th><th>description</th><th>severity</th><th>checker</th>
	</tr>
	
	<xsl:for-each select="/module/module[@name='TreeWalker']/module">
	<xsl:sort select="property[@name='id']/@value" />
	<tr>
			<td class="mono"><xsl:value-of select="property[@name='id']/@value" /></td>
      <td><xsl:value-of select="metadata[@name='com.atlassw.tools.eclipse.checkstyle.comment']/@value" />
          <xsl:text> </xsl:text><xsl:value-of select="property[@name='format']/@value" />
          <xsl:text> </xsl:text><xsl:value-of select="property[@name='illegalPkgs']/@value" />
      </td>
      <xsl:variable name="severity"><xsl:value-of select="property[@name='severity']/@value" /></xsl:variable>
      <td><xsl:choose>
        <xsl:when test="$severity = ''">
           <xsl:text>warning</xsl:text>
        </xsl:when>
        <xsl:otherwise><xsl:value-of select="property[@name='severity']/@value" /></xsl:otherwise>      
      </xsl:choose></td>
      <td><xsl:value-of select="@name" /></td>
	</tr>
	</xsl:for-each>
</table>
</body>
</html>
