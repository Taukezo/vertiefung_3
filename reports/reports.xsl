<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template match="/report">
        <html>
            <body>
                <h3>Program report:
                    <xsl:value-of select="classname"/>
                </h3>
                <h4>Summary:</h4>
                <table border="1">
                    <tr>
                        <td>Program</td>
                        <td>
                            <xsl:value-of select="classname"/>
                        </td>
                    </tr>
                    <tr>
                        <td>System</td>
                        <td>
                            <xsl:value-of select="systemname"/>
                        </td>
                    </tr>
                    <tr>
                        <td>Date</td>
                        <td>
                            <xsl:value-of select="date"/>
                        </td>
                    </tr>
                    <tr>
                        <td>Number of cycles</td>
                        <td>
                            <xsl:value-of select="cycles"/>
                        </td>
                    </tr>
                    <tr>
                        <td>Number of threads</td>
                        <td>
                            <xsl:value-of select="threads"/>
                        </td>
                    </tr>
                    <tr>
                        <td>First cycle in calculation</td>
                        <td>
                            <xsl:value-of select="calculatecyclesfrom"/>
                        </td>
                    </tr>
                    <tr>
                        <td>Duration average in millis</td>
                        <td>
                            <xsl:value-of select="durationaverage"/>
                        </td>
                    </tr>
                    <tr>
                        <td>Duration standard deviation in millis</td>
                        <td>
                            <xsl:value-of select="durationstandarddeviation"/>
                        </td>
                    </tr>
                    <tr>
                        <td>Files per thread average</td>
                        <td>
                            <xsl:value-of select="threadfilesaverage"/>
                        </td>
                    </tr>
                    <tr>
                        <td>Files per thread standard deviation</td>
                        <td>
                            <xsl:value-of select="threadfilesstandarddeviation"/>
                        </td>
                    </tr>

                </table>
                <h4>Details:</h4>
                <table border="1">
                    <tr>
                        <th>Cycle</th>
                        <th>Number of files</th>
                        <th>Duration</th>
                        <th>Thread name/Number of files</th>
                    </tr>
                    <xsl:for-each select="reportCycles/reportcycle">
                        <tr>
                            <td>
                                <xsl:value-of select="cycleNo"/>
                            </td>
                            <td>
                                <xsl:value-of select="numberoffiles"/>
                            </td>
                            <td>
                                <xsl:value-of select="duration"/>
                            </td>
                            <td>
                                <table>
                                    <xsl:for-each select="reportthreads/reportthread">
                                        <tr>
                                            <td>
                                                <xsl:value-of select="name"/>:
                                            </td>
                                            <td>
                                                <xsl:value-of select="numberoffiles"/>
                                            </td>
                                        </tr>
                                    </xsl:for-each>
                                </table>
                            </td>
                        </tr>
                    </xsl:for-each>
                </table>
            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>