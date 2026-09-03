<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>
        Receipt <c:out value="${payment.receiptNumber}"/> | Sunrise Dental Clinic
    </title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/print-document.css">

</head>

<body>

<div class="document-page">

    <!-- =====================================================
         ACTION BAR
         ===================================================== -->

    <div class="document-actions no-print">

        <button type="button"
                class="back-btn"
                onclick="window.history.back()">

            ← Back

        </button>


        <button type="button"
                class="print-btn"
                onclick="window.print()">

            🖨 Print Receipt

        </button>

    </div>


    <!-- =====================================================
         RECEIPT DOCUMENT
         ===================================================== -->

    <div class="document receipt-document">


        <!-- =================================================
             HEADER
             ================================================= -->

        <header class="document-header">

            <div class="clinic-brand">

                <div class="clinic-logo">
                    🦷
                </div>


                <div>

                    <h1>
                        Sunrise Dental Clinic
                    </h1>

                    <p>
                        Professional Dental Care & Clinic Management
                    </p>

                </div>

            </div>


            <div class="document-type receipt-type">

                <span>
                    PAYMENT
                </span>

                <h2>
                    RECEIPT
                </h2>

            </div>

        </header>


        <div class="header-line"></div>


        <!-- =================================================
             RECEIPT META
             ================================================= -->

        <section class="document-meta">


            <!-- RECEIPT NUMBER -->

            <div>

                <span>
                    Receipt Number
                </span>

                <strong>
                    <c:out value="${payment.receiptNumber}"/>
                </strong>

            </div>


            <!-- PAYMENT DATE -->

            <div>

                <span>
                    Payment Date
                </span>

                <strong>
                    <c:out value="${formattedPaymentDate}"/>
                </strong>

            </div>


            <!-- BILL NUMBER -->

            <div>

                <span>
                    Bill Number
                </span>

                <strong>
                    <c:out value="${payment.bill.billNumber}"/>
                </strong>

            </div>

        </section>


        <!-- =================================================
             PAYMENT SUCCESS
             ================================================= -->

        <section class="payment-success">

            <div class="success-icon">
                ✓
            </div>


            <div>

                <span>
                    Payment Recorded
                </span>


                <h2>
                    LKR
                    <fmt:formatNumber
                            value="${payment.paidAmount}"
                            type="number"
                            minFractionDigits="2"
                            maxFractionDigits="2"
                            groupingUsed="true"/>
                </h2>


                <p>
                    Payment successfully recorded in the clinic system.
                </p>

            </div>

        </section>


        <!-- =================================================
             PATIENT INFORMATION
             ================================================= -->

        <section class="info-section">

            <div class="section-title">

                <span class="section-icon">
                    👤
                </span>


                <div>

                    <h3>
                        Patient Information
                    </h3>

                    <p>
                        Payment received for this patient
                    </p>

                </div>

            </div>


            <div class="info-grid">


                <!-- PATIENT NAME -->

                <div class="info-item">

                    <span>
                        Patient Name
                    </span>

                    <strong>
                        <c:out value="${payment.bill.appointment.patient.patientName}"/>
                    </strong>

                </div>


                <!-- CONTACT NUMBER -->

                <div class="info-item">

                    <span>
                        Contact Number
                    </span>

                    <strong>
                        <c:out value="${payment.bill.appointment.patient.contactNumber}"/>
                    </strong>

                </div>


                <!-- APPOINTMENT NUMBER -->

                <div class="info-item">

                    <span>
                        Appointment Number
                    </span>

                    <strong>
                        <c:out value="${payment.bill.appointment.appointmentNumber}"/>
                    </strong>

                </div>


                <!-- TREATMENT -->

                <div class="info-item">

                    <span>
                        Treatment
                    </span>

                    <strong>
                        <c:out value="${payment.bill.appointment.treatmentType}"/>
                    </strong>

                </div>

            </div>

        </section>


        <!-- =================================================
             PAYMENT DETAILS
             ================================================= -->

        <section class="info-section">

            <div class="section-title">

                <span class="section-icon">
                    💳
                </span>


                <div>

                    <h3>
                        Payment Details
                    </h3>

                    <p>
                        Transaction information
                    </p>

                </div>

            </div>


            <div class="info-grid">


                <!-- PAYMENT METHOD -->

                <div class="info-item">

                    <span>
                        Payment Method
                    </span>

                    <strong>
                        <c:out value="${payment.paymentMethod}"/>
                    </strong>

                </div>


                <!-- PAYMENT STATUS -->

                <div class="info-item">

                    <span>
                        Payment Status
                    </span>

                    <strong class="payment-status">
                        <c:out value="${payment.paymentStatus}"/>
                    </strong>

                </div>


                <!-- BILL TOTAL -->

                <div class="info-item">

                    <span>
                        Total Bill Amount
                    </span>

                    <strong>

                        LKR

                        <fmt:formatNumber
                                value="${payment.bill.totalAmount}"
                                type="number"
                                minFractionDigits="2"
                                maxFractionDigits="2"
                                groupingUsed="true"/>

                    </strong>

                </div>


                <!-- PAID AMOUNT -->

                <div class="info-item">

                    <span>
                        Amount Received
                    </span>

                    <strong class="received-amount">

                        LKR

                        <fmt:formatNumber
                                value="${payment.paidAmount}"
                                type="number"
                                minFractionDigits="2"
                                maxFractionDigits="2"
                                groupingUsed="true"/>

                    </strong>

                </div>

            </div>

        </section>


        <!-- =================================================
             BALANCE SUMMARY
             ================================================= -->

        <section class="receipt-summary">


            <!-- BILL TOTAL -->

            <div>

                <span>
                    Bill Total
                </span>

                <strong>

                    LKR

                    <fmt:formatNumber
                            value="${payment.bill.totalAmount}"
                            type="number"
                            minFractionDigits="2"
                            maxFractionDigits="2"
                            groupingUsed="true"/>

                </strong>

            </div>


            <!-- AMOUNT PAID -->

            <div>

                <span>
                    Amount Paid
                </span>

                <strong>

                    LKR

                    <fmt:formatNumber
                            value="${payment.paidAmount}"
                            type="number"
                            minFractionDigits="2"
                            maxFractionDigits="2"
                            groupingUsed="true"/>

                </strong>

            </div>


            <!-- OUTSTANDING BALANCE -->

            <div class="balance-row">

                <span>
                    Outstanding Balance
                </span>

                <strong>

                    LKR

                    <fmt:formatNumber
                            value="${payment.bill.totalAmount - payment.paidAmount}"
                            type="number"
                            minFractionDigits="2"
                            maxFractionDigits="2"
                            groupingUsed="true"/>

                </strong>

            </div>

        </section>


        <!-- =================================================
             NOTE
             ================================================= -->

        <section class="document-note">

            <strong>
                Thank You
            </strong>

            <p>
                Thank you for choosing Sunrise Dental Clinic.
                This computer-generated receipt confirms the payment recorded
                against the bill shown above.
            </p>

        </section>


        <!-- =================================================
             SIGNATURE
             ================================================= -->

        <section class="signature-section">

            <div class="signature-box">

                <div class="signature-line"></div>

                <span>
                    Received By
                </span>

            </div>


            <div class="signature-box">

                <div class="signature-line"></div>

                <span>
                    Patient / Guardian
                </span>

            </div>

        </section>


        <!-- =================================================
             FOOTER
             ================================================= -->

        <footer class="document-footer">

            <div>

                <strong>
                    Sunrise Dental Clinic
                </strong>

                <span>
                    Computer Generated Payment Receipt
                </span>

            </div>


            <div class="footer-right">

                <span>
                    Receipt:
                    <c:out value="${payment.receiptNumber}"/>
                </span>

            </div>

        </footer>

    </div>

</div>

</body>

</html>