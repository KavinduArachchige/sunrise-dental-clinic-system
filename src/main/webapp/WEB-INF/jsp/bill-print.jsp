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
        Bill <c:out value="${bill.billNumber}"/> | Sunrise Dental Clinic
    </title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/print-document.css">
</head>

<body>

<div class="document-page">

    <!-- =========================
         ACTION BAR
         ========================= -->

    <div class="document-actions no-print">

        <button type="button"
                class="back-btn"
                onclick="window.history.back()">
            ← Back
        </button>

        <button type="button"
                class="print-btn"
                onclick="window.print()">
            🖨 Print Bill
        </button>

    </div>


    <!-- =========================
         DOCUMENT
         ========================= -->

    <div class="document">

        <!-- =========================
             HEADER
             ========================= -->

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


            <div class="document-type">

                <span>
                    PATIENT
                </span>

                <h2>
                    BILL
                </h2>

            </div>

        </header>


        <div class="header-line"></div>


        <!-- =========================
             BILL META
             ========================= -->

        <section class="document-meta">

            <div>

                <span>
                    Bill Number
                </span>

                <strong>
                    <c:out value="${bill.billNumber}"/>
                </strong>

            </div>


            <div>

                <span>
                    Bill Date
                </span>

                <strong>
                    <c:out value="${formattedBillDate}"/>
                </strong>

            </div>


            <div>

                <span>
                    Appointment No.
                </span>

                <strong>
                    <c:out value="${bill.appointment.appointmentNumber}"/>
                </strong>

            </div>

        </section>


        <!-- =========================
             PATIENT INFORMATION
             ========================= -->

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
                        Patient and appointment details
                    </p>

                </div>

            </div>


            <div class="info-grid">

                <div class="info-item">

                    <span>
                        Patient Name
                    </span>

                    <strong>
                        <c:out value="${bill.appointment.patient.patientName}"/>
                    </strong>

                </div>


                <div class="info-item">

                    <span>
                        Contact Number
                    </span>

                    <strong>
                        <c:out value="${bill.appointment.patient.contactNumber}"/>
                    </strong>

                </div>


                <div class="info-item">

                    <span>
                        Address
                    </span>

                    <strong>
                        <c:out value="${bill.appointment.patient.address}"/>
                    </strong>

                </div>


                <div class="info-item">

                    <span>
                        Appointment Date
                    </span>

                    <strong>
                        <c:out value="${bill.appointment.appointmentDate}"/>
                    </strong>

                </div>

            </div>

        </section>


        <!-- =========================
             DENTAL SERVICE
             ========================= -->

        <section class="info-section">

            <div class="section-title">

                <span class="section-icon">
                    🦷
                </span>

                <div>

                    <h3>
                        Dental Service
                    </h3>

                    <p>
                        Treatment and dentist information
                    </p>

                </div>

            </div>


            <div class="info-grid">

                <div class="info-item">

                    <span>
                        Dentist
                    </span>

                    <strong>
                        <c:out value="${bill.appointment.dentist.dentistName}"/>
                    </strong>

                </div>


                <div class="info-item">

                    <span>
                        Specialization
                    </span>

                    <strong>
                        <c:out value="${bill.appointment.dentist.specialization}"/>
                    </strong>

                </div>


                <div class="info-item">

                    <span>
                        Treatment
                    </span>

                    <strong>
                        <c:out value="${bill.appointment.treatmentType}"/>
                    </strong>

                </div>


                <div class="info-item">

                    <span>
                        Appointment Time
                    </span>

                    <strong>
                        <c:out value="${bill.appointment.appointmentTime}"/>
                    </strong>

                </div>

            </div>

        </section>


        <!-- =========================
             CHARGES
             ========================= -->

        <section class="charges-section">

            <div class="section-title">

                <span class="section-icon">
                    🧾
                </span>

                <div>

                    <h3>
                        Bill Summary
                    </h3>

                    <p>
                        Charges for the dental service
                    </p>

                </div>

            </div>


            <div class="charges-table">

                <div class="charge-row charge-header">

                    <span>
                        Description
                    </span>

                    <span>
                        Amount
                    </span>

                </div>


                <div class="charge-row">

                    <span>
                        <c:out value="${bill.appointment.treatmentType}"/>
                    </span>

                    <strong>
                        LKR
                        <fmt:formatNumber
                                value="${bill.treatmentAmount}"
                                type="number"
                                minFractionDigits="2"
                                maxFractionDigits="2"
                                groupingUsed="true"/>
                    </strong>

                </div>


                <div class="charge-row">

                    <span>
                        Consultation Fee
                    </span>

                    <strong>
                        LKR
                        <fmt:formatNumber
                                value="${bill.consultationFee}"
                                type="number"
                                minFractionDigits="2"
                                maxFractionDigits="2"
                                groupingUsed="true"/>
                    </strong>

                </div>


                <div class="charge-row total-charge">

                    <span>
                        Total Amount
                    </span>

                    <strong>
                        LKR
                        <fmt:formatNumber
                                value="${bill.totalAmount}"
                                type="number"
                                minFractionDigits="2"
                                maxFractionDigits="2"
                                groupingUsed="true"/>
                    </strong>

                </div>

            </div>

        </section>


        <!-- =========================
             NOTE
             ========================= -->

        <section class="document-note">

            <strong>
                Important Note
            </strong>

            <p>
                This bill was generated electronically by the Sunrise Dental
                Clinic Management System. Please retain this document for
                payment and clinic records.
            </p>

        </section>


        <!-- =========================
             SIGNATURE
             ========================= -->

        <section class="signature-section">

            <div class="signature-box">

                <div class="signature-line"></div>

                <span>
                    Patient / Guardian
                </span>

            </div>


            <div class="signature-box">

                <div class="signature-line"></div>

                <span>
                    Authorized Staff
                </span>

            </div>

        </section>


        <!-- =========================
             FOOTER
             ========================= -->

        <footer class="document-footer">

            <div>

                <strong>
                    Sunrise Dental Clinic
                </strong>

                <span>
                    Dental Clinic Management System
                </span>

            </div>


            <div class="footer-right">

                <span>
                    Bill:
                    <c:out value="${bill.billNumber}"/>
                </span>

            </div>

        </footer>

    </div>

</div>

</body>
</html>