<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Appointment Search | Sunrise Dental Clinic</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/dashboard.css">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/appointment-search.css">

</head>

<body>

<div class="app-layout">

    <!-- =====================================================
         SIDEBAR
         ===================================================== -->

    <aside class="sidebar"
           id="sidebar">

        <div class="brand">

            <div class="brand-logo">
                🦷
            </div>

            <div class="brand-text">
                <h2>Sunrise</h2>
                <span>Dental Clinic</span>
            </div>

        </div>


        <nav class="sidebar-nav">

            <a href="${pageContext.request.contextPath}/"
               class="nav-link">

                <span class="nav-icon">⌂</span>
                <span class="nav-text">Dashboard</span>

            </a>


            <a href="${pageContext.request.contextPath}/patients"
               class="nav-link">

                <span class="nav-icon">👥</span>
                <span class="nav-text">Patients</span>

            </a>


            <a href="${pageContext.request.contextPath}/dentists"
               class="nav-link">

                <span class="nav-icon">🦷</span>
                <span class="nav-text">Dentists</span>

            </a>


            <a href="${pageContext.request.contextPath}/appointments"
               class="nav-link">

                <span class="nav-icon">📅</span>
                <span class="nav-text">Appointments</span>

            </a>


            <a href="${pageContext.request.contextPath}/appointment-search"
               class="nav-link active">

                <span class="nav-icon">🔎</span>
                <span class="nav-text">Search Appointment</span>

            </a>


            <a href="${pageContext.request.contextPath}/treatments"
               class="nav-link">

                <span class="nav-icon">✚</span>
                <span class="nav-text">Treatments</span>

            </a>


            <a href="${pageContext.request.contextPath}/billing"
               class="nav-link">

                <span class="nav-icon">🧾</span>
                <span class="nav-text">Billing</span>

            </a>


            <a href="${pageContext.request.contextPath}/payments"
               class="nav-link">

                <span class="nav-icon">💳</span>
                <span class="nav-text">Payments</span>

            </a>


            <!-- ADMIN ONLY -->

            <c:if test="${loggedInUser != null && loggedInUser.role == 'ADMIN'}">

                <a href="${pageContext.request.contextPath}/reports"
                   class="nav-link">

                    <span class="nav-icon">📊</span>
                    <span class="nav-text">Reports</span>

                </a>


                <a href="${pageContext.request.contextPath}/staff"
                   class="nav-link">

                    <span class="nav-icon">🔐</span>
                    <span class="nav-text">Staff</span>

                </a>


                <a href="${pageContext.request.contextPath}/audit-logs"
                   class="nav-link">

                    <span class="nav-icon">🛡️</span>
                    <span class="nav-text">Audit Logs</span>

                </a>

            </c:if>


            <a href="${pageContext.request.contextPath}/help"
               class="nav-link">

                <span class="nav-icon">❓</span>
                <span class="nav-text">Help & Guide</span>

            </a>

        </nav>


        <!-- =================================================
             SIDEBAR FOOTER
             ================================================= -->

        <div class="sidebar-footer">

            <div class="clinic-status">

                <span class="status-dot"></span>

                <div>
                    <strong>System Online</strong>
                    <small>Database connected</small>
                </div>

            </div>


            <form action="${pageContext.request.contextPath}/logout"
                  method="post"
                  class="logout-form">

                <button type="submit"
                        class="logout-btn">

                    <span>↪</span>
                    <span class="logout-text">Logout</span>

                </button>

            </form>

        </div>

    </aside>


    <!-- =====================================================
         MAIN CONTENT
         ===================================================== -->

    <main class="main-content">

        <!-- =================================================
             TOP BAR
             ================================================= -->

        <header class="topbar">

            <div class="topbar-left">

                <button class="menu-button"
                        id="menuButton">
                    ☰
                </button>

                <div>

                    <h1>
                        Appointment Search
                    </h1>

                    <p>
                        Find complete appointment information using the appointment number
                    </p>

                </div>

            </div>


            <div class="topbar-right">

                <!-- LOGGED-IN USER -->

                <div class="profile">

                    <div class="profile-avatar">

                        <c:choose>

                            <c:when test="${loggedInUser != null && not empty loggedInUser.fullName}">

                                ${fn:substring(
                                    loggedInUser.fullName,
                                    0,
                                    1
                                )}

                            </c:when>

                            <c:otherwise>
                                U
                            </c:otherwise>

                        </c:choose>

                    </div>


                    <div class="profile-info">

                        <strong>

                            <c:choose>

                                <c:when test="${loggedInUser != null}">
                                    <c:out value="${loggedInUser.fullName}"/>
                                </c:when>

                                <c:otherwise>
                                    User
                                </c:otherwise>

                            </c:choose>

                        </strong>


                        <span>

                            <c:choose>

                                <c:when test="${loggedInUser != null}">
                                    <c:out value="${loggedInUser.role}"/>
                                </c:when>

                                <c:otherwise>
                                    STAFF
                                </c:otherwise>

                            </c:choose>

                        </span>

                    </div>

                </div>

            </div>

        </header>


        <!-- =================================================
             SEARCH PAGE
             ================================================= -->

        <section class="search-page-content">


            <!-- =================================================
                 HERO
                 ================================================= -->

            <div class="search-hero">

                <div>

                    <span class="hero-label">
                        APPOINTMENT LOOKUP
                    </span>

                    <h2>
                        Search Patient Appointment
                    </h2>

                    <p>
                        Enter the unique appointment number to retrieve patient,
                        dentist, treatment, billing and payment details.
                    </p>

                </div>


                <div class="hero-icon">
                    🔎
                </div>

            </div>


            <!-- =================================================
                 SEARCH FORM
                 ================================================= -->

            <div class="search-panel">

                <form id="appointmentSearchForm">

                    <label for="appointmentNumber">
                        Appointment Number
                    </label>


                    <div class="search-row">

                        <input
                                type="text"
                                id="appointmentNumber"
                                placeholder="Example: APT-12AB34CD"
                                autocomplete="off"
                                required>


                        <button
                                type="submit"
                                id="searchButton"
                                class="search-btn">

                            Search Appointment

                        </button>

                    </div>


                    <small>
                        Search is not case-sensitive.
                    </small>

                </form>

            </div>


            <!-- =================================================
                 EMPTY STATE
                 ================================================= -->

            <div id="emptyState"
                 class="empty-state-card">

                <div class="empty-icon">
                    📅
                </div>

                <h3>
                    Search for an appointment
                </h3>

                <p>
                    Appointment information will appear here after a successful search.
                </p>

            </div>


            <!-- =================================================
                 NOT FOUND
                 ================================================= -->

            <div id="notFoundState"
                 class="not-found-card hidden">

                <div class="not-found-icon">
                    ⚠️
                </div>

                <h3>
                    Appointment Not Found
                </h3>

                <p id="notFoundMessage">
                    No appointment was found for the entered appointment number.
                </p>

            </div>


            <!-- =================================================
                 RESULT SECTION
                 ================================================= -->

            <div id="resultSection"
                 class="result-section hidden">


                <!-- RESULT HEADER -->

                <div class="result-header">

                    <div>

                        <span class="result-label">
                            APPOINTMENT RECORD
                        </span>

                        <h2 id="resultAppointmentNumber">
                            APT-XXXXXXXX
                        </h2>

                    </div>


                    <div class="result-header-actions">

                        <span id="appointmentStatusBadge"
                              class="appointment-status-badge">

                            SCHEDULED

                        </span>


                        <button
                                type="button"
                                class="print-btn"
                                id="printButton">

                            🖨 Print Details

                        </button>

                    </div>

                </div>


                <!-- =================================================
                     DETAILS GRID
                     ================================================= -->

                <div class="detail-grid">


                    <!-- =============================================
                         PATIENT INFORMATION
                         ============================================= -->

                    <div class="details-card">

                        <div class="card-header">

                            <div class="card-icon">
                                👤
                            </div>

                            <div>
                                <h3>Patient Information</h3>
                                <p>Registered patient details</p>
                            </div>

                        </div>


                        <div class="details-list">

                            <div class="detail-row">

                                <span>
                                    Patient ID
                                </span>

                                <strong id="patientId">
                                    -
                                </strong>

                            </div>


                            <div class="detail-row">

                                <span>
                                    Full Name
                                </span>

                                <strong id="patientName">
                                    -
                                </strong>

                            </div>


                            <div class="detail-row">

                                <span>
                                    Address
                                </span>

                                <strong id="patientAddress">
                                    -
                                </strong>

                            </div>


                            <div class="detail-row">

                                <span>
                                    Contact Number
                                </span>

                                <strong id="patientContact">
                                    -
                                </strong>

                            </div>

                        </div>

                    </div>


                    <!-- =============================================
                         DENTIST INFORMATION
                         ============================================= -->

                    <div class="details-card">

                        <div class="card-header">

                            <div class="card-icon">
                                🦷
                            </div>

                            <div>
                                <h3>Dentist Information</h3>
                                <p>Assigned dental professional</p>
                            </div>

                        </div>


                        <div class="details-list">

                            <div class="detail-row">

                                <span>
                                    Dentist ID
                                </span>

                                <strong id="dentistId">
                                    -
                                </strong>

                            </div>


                            <div class="detail-row">

                                <span>
                                    Dentist Name
                                </span>

                                <strong id="dentistName">
                                    -
                                </strong>

                            </div>


                            <div class="detail-row">

                                <span>
                                    Specialization
                                </span>

                                <strong id="dentistSpecialization">
                                    -
                                </strong>

                            </div>

                        </div>

                    </div>


                    <!-- =============================================
                         APPOINTMENT INFORMATION
                         ============================================= -->

                    <div class="details-card">

                        <div class="card-header">

                            <div class="card-icon">
                                📅
                            </div>

                            <div>
                                <h3>Appointment Information</h3>
                                <p>Scheduled visit details</p>
                            </div>

                        </div>


                        <div class="details-list">

                            <div class="detail-row">

                                <span>
                                    Treatment
                                </span>

                                <strong id="treatmentType">
                                    -
                                </strong>

                            </div>


                            <div class="detail-row">

                                <span>
                                    Date
                                </span>

                                <strong id="appointmentDate">
                                    -
                                </strong>

                            </div>


                            <div class="detail-row">

                                <span>
                                    Time
                                </span>

                                <strong id="appointmentTime">
                                    -
                                </strong>

                            </div>


                            <div class="detail-row">

                                <span>
                                    Status
                                </span>

                                <strong id="appointmentStatusText">
                                    -
                                </strong>

                            </div>

                        </div>

                    </div>


                    <!-- =============================================
                         BILLING INFORMATION
                         ============================================= -->

                    <div class="details-card">

                        <div class="card-header">

                            <div class="card-icon">
                                🧾
                            </div>

                            <div>
                                <h3>Billing Information</h3>
                                <p>Generated bill summary</p>
                            </div>

                        </div>


                        <!-- BILL AVAILABLE -->

                        <div id="billAvailable">

                            <div class="details-list">

                                <div class="detail-row">

                                    <span>
                                        Bill Number
                                    </span>

                                    <strong id="billNumber">
                                        -
                                    </strong>

                                </div>


                                <div class="detail-row">

                                    <span>
                                        Treatment Amount
                                    </span>

                                    <strong id="treatmentAmount">
                                        -
                                    </strong>

                                </div>


                                <div class="detail-row">

                                    <span>
                                        Consultation Fee
                                    </span>

                                    <strong id="consultationFee">
                                        -
                                    </strong>

                                </div>


                                <div class="detail-row total-row">

                                    <span>
                                        Total Amount
                                    </span>

                                    <strong id="totalAmount">
                                        -
                                    </strong>

                                </div>

                            </div>

                        </div>


                        <!-- BILL NOT AVAILABLE -->

                        <div id="billUnavailable"
                             class="record-unavailable hidden">

                            <span class="record-unavailable-icon">
                                🧾
                            </span>

                            <strong>
                                No Bill Generated
                            </strong>

                            <small>
                                A bill has not been generated for this appointment.
                            </small>

                        </div>

                    </div>


                    <!-- =============================================
                         PAYMENT INFORMATION
                         ============================================= -->

                    <div class="details-card payment-card">

                        <div class="card-header">

                            <div class="card-icon">
                                💳
                            </div>

                            <div>
                                <h3>Payment Information</h3>
                                <p>Receipt and payment status</p>
                            </div>

                        </div>


                        <!-- PAYMENT AVAILABLE -->

                        <div id="paymentAvailable">

                            <div class="details-list">

                                <div class="detail-row">

                                    <span>
                                        Receipt Number
                                    </span>

                                    <strong id="receiptNumber">
                                        -
                                    </strong>

                                </div>


                                <div class="detail-row">

                                    <span>
                                        Paid Amount
                                    </span>

                                    <strong id="paidAmount">
                                        -
                                    </strong>

                                </div>


                                <div class="detail-row">

                                    <span>
                                        Payment Method
                                    </span>

                                    <strong id="paymentMethod">
                                        -
                                    </strong>

                                </div>


                                <div class="detail-row">

                                    <span>
                                        Payment Status
                                    </span>

                                    <strong id="paymentStatus">
                                        -
                                    </strong>

                                </div>

                            </div>

                        </div>


                        <!-- PAYMENT NOT AVAILABLE -->

                        <div id="paymentUnavailable"
                             class="record-unavailable hidden">

                            <span class="record-unavailable-icon">
                                💳
                            </span>

                            <strong>
                                No Payment Recorded
                            </strong>

                            <small>
                                A payment has not yet been recorded for this appointment.
                            </small>

                        </div>

                    </div>

                </div>

            </div>


            <!-- =================================================
                 FOOTER
                 ================================================= -->

            <footer class="search-page-footer">

                <p>
                    © 2026 Sunrise Dental Clinic Management System
                </p>

                <span>
                    Appointment Lookup • Secure Staff Access
                </span>

            </footer>

        </section>

    </main>

</div>


<!-- =========================================================
     TOAST
     ========================================================= -->

<div id="toast"
     class="toast">

    <span id="toastMessage"></span>

</div>


<!-- =========================================================
     EXISTING JS
     ========================================================= -->

<script src="${pageContext.request.contextPath}/js/appointment-search.js"></script>

</body>

</html>