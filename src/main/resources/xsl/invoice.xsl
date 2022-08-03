<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                xmlns:java="http://xml.apache.org/xslt/java"
                exclude-result-prefixes="java" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xsi:schemaLocation="http://xml.apache.org/xslt/java ">

    <!-- Attribute used for table border -->

    <xsl:attribute-set name="tableBorder">

        <xsl:attribute name="border">solid 0.1mm black</xsl:attribute>

    </xsl:attribute-set>

    <xsl:template match="orderInvoice">

        <fo:root>

            <fo:layout-master-set>

                <fo:simple-page-master master-name="simpleA4"

                                       page-height="29.7cm" page-width="25.0cm" margin="1cm">

                    <fo:region-body/>

                </fo:simple-page-master>

            </fo:layout-master-set>

            <fo:page-sequence master-reference="simpleA4">

                <fo:flow flow-name="xsl-region-body">

                    <fo:block font-size="25pt" text-align="center" font-family="Helvetica" font-weight="bold"
                              space-after="2mm">

                        Order Invoice

                    </fo:block>

                    <fo:block font-size="12pt" text-align="center" font-family="Helvetica" font-weight="bold"
                              space-after="5mm">

                        Order Id-<xsl:value-of select="order_id"/>

                    </fo:block>

                    <fo:block font-size="12pt" text-align="center" font-family="Helvetica" font-weight="bold"
                              space-after="5mm">

                        Order Time -
                        <xsl:value-of
                                select="java:format(java:java.text.SimpleDateFormat.new('dd-MM-yyyy hh:mm:ss'), java:java.util.Date.new())"/>

                    </fo:block>

                    <fo:block font-size="10pt">

                        <fo:table table-layout="fixed" width="100%" border-collapse="separate">

                            <fo:table-column column-width="2cm"/>

                            <fo:table-column column-width="5cm"/>

                            <fo:table-column column-width="5cm"/>

                            <fo:table-column column-width="5cm"/>

                            <fo:table-column column-width="5cm"/>

                            <fo:table-column column-width="5cm"/>

                            <fo:table-header font-weight="bold">

                                <fo:table-cell border="1pt solid black" xsl:use-attribute-sets="tableBorder">

                                    <fo:block font-size="13pt" text-align="center" font-weight="bold">S.No.</fo:block>

                                </fo:table-cell>

                                <!--                                <fo:table-cell border="1pt solid black" xsl:use-attribute-sets="tableBorder">-->

                                <!--                                    <fo:block font-size="15pt" text-align="center" font-weight="bold">ProductName</fo:block>-->

                                <!--                                </fo:table-cell>-->

                                <fo:table-cell border="1pt solid black" xsl:use-attribute-sets="tableBorder">

                                    <fo:block font-size="13pt" text-align="center" font-weight="bold">Bar Code
                                    </fo:block>

                                </fo:table-cell>

                                <fo:table-cell border="1pt solid black" xsl:use-attribute-sets="tableBorder">

                                    <fo:block font-size="13pt" text-align="center" font-weight="bold">Quantity
                                    </fo:block>

                                </fo:table-cell>

                                <fo:table-cell border="1pt solid black" xsl:use-attribute-sets="tableBorder">

                                    <fo:block font-size="13pt" text-align="center" font-weight="bold">Selling Price
                                    </fo:block>

                                </fo:table-cell>

                                <fo:table-cell border="1pt solid black" xsl:use-attribute-sets="tableBorder">

                                    <fo:block font-size="13pt" text-align="center" font-weight="bold">Total Price
                                    </fo:block>

                                </fo:table-cell>

                            </fo:table-header>

                            <fo:table-body>

                                <xsl:apply-templates select="orderInvoicePojoList"/>

                            </fo:table-body>

                        </fo:table>

                    </fo:block>

                    <fo:block font-size="16pt" font-family="Helvetica" text-align="left" color="black"
                              font-weight="bold" padding-left="20%" padding-top="5%" space-after="10mm">

                        <!--                         TODO-->
                        Total Bill Amount:
                        <xsl:value-of select="total_amount"/>
                         Rs.
                    </fo:block>

                </fo:flow>

            </fo:page-sequence>

        </fo:root>

    </xsl:template>

    <xsl:template match="orderInvoicePojoList">

        <fo:table-row>

            <fo:table-cell border="1pt solid black" xsl:use-attribute-sets="tableBorder">

                <fo:block text-align="center" font-size="13pt">

                    <xsl:value-of select="SNo"/>

                </fo:block>

            </fo:table-cell>

            <!--            <fo:table-cell  border="1pt solid black" xsl:use-attribute-sets="tableBorder">-->

            <!--                <fo:block text-align="left" font-size="15pt">-->

            <!--                    <xsl:value-of select="name"/>-->

            <!--                </fo:block>-->

            <!--            </fo:table-cell>-->

            <fo:table-cell border="1pt solid black" xsl:use-attribute-sets="tableBorder">

                <fo:block text-align="center" font-size="13pt">

                    <xsl:value-of select="barcode"/>

                </fo:block>

            </fo:table-cell>

            <fo:table-cell border="1pt solid black" xsl:use-attribute-sets="tableBorder">

                <fo:block text-align="center" font-size="13pt">

                    <xsl:value-of select="quantity"/>

                </fo:block>

            </fo:table-cell>

            <fo:table-cell border="1pt solid black" xsl:use-attribute-sets="tableBorder">

                <fo:block text-align="center" font-size="13pt">

                    <xsl:value-of select="selling_price"/>

                </fo:block>

            </fo:table-cell>

            <fo:table-cell border="1pt solid black" xsl:use-attribute-sets="tableBorder">

                <fo:block text-align="center" font-size="13pt">

                    <xsl:value-of select="bill_amount"/>

                </fo:block>

            </fo:table-cell>

        </fo:table-row>

    </xsl:template>

</xsl:stylesheet>