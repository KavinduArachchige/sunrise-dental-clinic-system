<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Reports | Sunrise Dental Clinic</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/dashboard.css">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/reports.css">

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

                <span>
                    Dental Clinic
                </span>

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
               class="nav-link">

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
                   class="nav-link active">

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

                    <strong>
                        System Online
                    </strong>

                    <small>
                        Database connected
                    </small>

                </div>

            </div>


            <form action="${pageContext.request.contextPath}/logout"
                  method="post"
                  class="logout-form">

                <button type="submit"
                        class="logout-btn">

                    <span>↪</span>

                    <span class="logout-text">
                        Logout
                    </span>

                </button>

            </form>

        </div>

    </aside>


    <!-- =====================================================
         MAIN CONTENT
         ===================================================== -->

    <main class="main-content">


        <!-- =================================================
             TOPBAR
             ================================================= -->

        <header class="topbar">

            <div class="topbar-left">

                <button class="menu-button"
                        id="menuButton">
                    ☰
                </button>


                <div>

                    <h1>
                        Reports & Analytics
                    </h1>

                    <p>
                        Clinic performance, financial and operational insights
                    </p>

                </div>

            </div>


            <div class="topbar-right">

                <div class="profile">


                    <!-- PROFILE AVATAR -->

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
                                A
                            </c:otherwise>

                        </c:choose>

                    </div>


                    <!-- PROFILE INFORMATION -->

                    <div class="profile-info">

                        <strong>

                            <c:choose>

                                <c:when test="${loggedInUser != null}">
                                    <c:out value="${loggedInUser.fullName}"/>
                                </c:when>

                                <c:otherwise>
                                    Administrator
                                </c:otherwise>

                            </c:choose>

                        </strong>


                        <span>

                            <c:choose>

                                <c:when test="${loggedInUser != null}">
                                    <c:out value="${loggedInUser.role}"/>
                                </c:when>

                                <c:otherwise>
                                    ADMIN
                                </c:otherwise>

                            </c:choose>

                        </span>

                    </div>

                </div>

            </div>

        </header>


        <!-- =================================================
             REPORT CONTENT
             ================================================= -->

        <section class="reports-content">


            <!-- =================================================
                 HERO
                 ================================================= -->

            <div class="reports-hero">

                <div>

                    <span class="page-label">
                        MANAGEMENT REPORTING
                    </span>

                    <h2>
                        Clinic Performance Overview
                    </h2>

                    <p>
                        Analyze appointments, treatments, billing,
                        revenue and payment activity from one dashboard.
                    </p>

                </div>


                <div class="report-date-box">

                    <span>
                        Report Date
                    </span>

                    <strong id="reportDate">
                        -
                    </strong>

                </div>

            </div>


            <!-- =================================================
                 DATE RANGE FILTER
                 ================================================= -->

            <div class="report-filter-panel">

                <div>

                    <h3>
                        Date Range Filter
                    </h3>

                    <p>
                        Filter appointment, billing and payment analytics by date.
                    </p>

                </div>


                <div class="report-filter-grid">


                    <!-- FROM DATE -->

                    <div class="filter-field">

                        <label for="fromDate">
                            From Date
                        </label>

                        <input
                                type="date"
                                id="fromDate">

                    </div>


                    <!-- TO DATE -->

                    <div class="filter-field">

                        <label for="toDate">
                            To Date
                        </label>

                        <input
                                type="date"
                                id="toDate">

                    </div>


                    <button type="button"
                            class="apply-filter-btn"
                            id="applyDateFilter">

                        Apply Filter

                    </button>


                    <button type="button"
                            class="reset-filter-btn"
                            id="resetDateFilter">

                        Reset

                    </button>


                    <button type="button"
                            class="refresh-report-btn"
                            id="refreshReports">

                        ↻ Refresh

                    </button>

                </div>

            </div>


            <!-- =================================================
                 KPI CARDS
                 ================================================= -->

            <div class="reports-kpi-grid">


                <!-- PATIENTS -->

                <div class="report-kpi-card">

                    <div class="kpi-icon">
                        👥
                    </div>

                    <div>

                        <span>
                            Total Patients
                        </span>

                        <strong id="reportPatients">
                            0
                        </strong>

                        <small>
                            Registered patients
                        </small>

                    </div>

                </div>


                <!-- APPOINTMENTS -->

                <div class="report-kpi-card">

                    <div class="kpi-icon">
                        📅
                    </div>

                    <div>

                        <span>
                            Appointments
                        </span>

                        <strong id="reportAppointments">
                            0
                        </strong>

                        <small>
                            Filtered appointments
                        </small>

                    </div>

                </div>


                <!-- BILLS -->

                <div class="report-kpi-card">

                    <div class="kpi-icon">
                        🧾
                    </div>

                    <div>

                        <span>
                            Generated Bills
                        </span>

                        <strong id="reportBills">
                            0
                        </strong>

                        <small>
                            Filtered bills
                        </small>

                    </div>

                </div>


                <!-- REVENUE -->

                <div class="report-kpi-card">

                    <div class="kpi-icon">
                        💰
                    </div>

                    <div>

                        <span>
                            Collected Revenue
                        </span>

                        <strong id="reportRevenue">
                            Rs. 0.00
                        </strong>

                        <small>
                            Recorded payments
                        </small>

                    </div>

                </div>

            </div>


            <!-- =================================================
                 CHART GRID
                 ================================================= -->

            <div class="chart-grid">


                <!-- =================================================
                     MONTHLY REVENUE
                     ================================================= -->

                <div class="chart-panel chart-panel-wide">

                    <div class="report-panel-header">

                        <div>

                            <h3>
                                Monthly Revenue
                            </h3>

                            <p>
                                Collected revenue grouped by month
                            </p>

                        </div>

                    </div>


                    <div class="bar-chart"
                         id="revenueChart">

                        <div class="chart-empty">
                            Loading revenue chart...
                        </div>

                    </div>

                </div>


                <!-- =================================================
                     APPOINTMENT STATUS
                     ================================================= -->

                <div class="chart-panel">

                    <div class="report-panel-header">

                        <div>

                            <h3>
                                Appointment Status
                            </h3>

                            <p>
                                Scheduled, completed and cancelled visits
                            </p>

                        </div>

                    </div>


                    <div class="donut-area">

                        <div class="donut-chart"
                             id="appointmentDonut">

                            <div class="donut-center">

                                <strong id="appointmentDonutTotal">
                                    0
                                </strong>

                                <span>
                                    Total
                                </span>

                            </div>

                        </div>


                        <div class="donut-legend">

                            <div>

                                <span class="legend-dot legend-scheduled"></span>

                                Scheduled

                                <strong id="scheduledCount">
                                    0
                                </strong>

                            </div>


                            <div>

                                <span class="legend-dot legend-completed"></span>

                                Completed

                                <strong id="completedCount">
                                    0
                                </strong>

                            </div>


                            <div>

                                <span class="legend-dot legend-cancelled"></span>

                                Cancelled

                                <strong id="cancelledCount">
                                    0
                                </strong>

                            </div>

                        </div>

                    </div>

                </div>


                <!-- =================================================
                     PAYMENT METHODS
                     ================================================= -->

                <div class="chart-panel">

                    <div class="report-panel-header">

                        <div>

                            <h3>
                                Payment Methods
                            </h3>

                            <p>
                                Recorded payments by transaction method
                            </p>

                        </div>

                    </div>


                    <div class="payment-method-list">


                        <div class="payment-method-row">

                            <span>
                                Cash
                            </span>

                            <strong id="cashCount">
                                0
                            </strong>

                        </div>


                        <div class="payment-method-row">

                            <span>
                                Card
                            </span>

                            <strong id="cardCount">
                                0
                            </strong>

                        </div>


                        <div class="payment-method-row">

                            <span>
                                Bank Transfer
                            </span>

                            <strong id="bankCount">
                                0
                            </strong>

                        </div>

                    </div>

                </div>

            </div>


            <!-- =================================================
                 FINANCIAL + RESOURCES
                 ================================================= -->

            <div class="report-grid">


                <!-- FINANCIAL SUMMARY -->

                <div class="report-panel">

                    <div class="report-panel-header">

                        <div>

                            <h3>
                                Financial Summary
                            </h3>

                            <p>
                                Billing and payment performance
                            </p>

                        </div>

                    </div>


                    <div class="metric-list">

                        <div class="metric-row">

                            <span>
                                Total Billed
                            </span>

                            <strong id="totalBilled">
                                Rs. 0.00
                            </strong>

                        </div>


                        <div class="metric-row">

                            <span>
                                Total Collected
                            </span>

                            <strong id="totalCollected">
                                Rs. 0.00
                            </strong>

                        </div>


                        <div class="metric-row">

                            <span>
                                Outstanding
                            </span>

                            <strong id="outstandingAmount">
                                Rs. 0.00
                            </strong>

                        </div>

                    </div>

                </div>


                <!-- CLINIC RESOURCES -->

                <div class="report-panel">

                    <div class="report-panel-header">

                        <div>

                            <h3>
                                Clinic Resources
                            </h3>

                            <p>
                                Current available system records
                            </p>

                        </div>

                    </div>


                    <div class="metric-list">

                        <div class="metric-row">

                            <span>
                                Dentists
                            </span>

                            <strong id="reportDentists">
                                0
                            </strong>

                        </div>


                        <div class="metric-row">

                            <span>
                                Treatments
                            </span>

                            <strong id="reportTreatments">
                                0
                            </strong>

                        </div>


                        <div class="metric-row">

                            <span>
                                Payments
                            </span>

                            <strong id="reportPayments">
                                0
                            </strong>

                        </div>

                    </div>

                </div>

            </div>


            <!-- =================================================
                 TREATMENT POPULARITY CHART
                 ================================================= -->

            <div class="report-panel wide-report-panel">

                <div class="report-panel-header">

                    <div>

                        <h3>
                            Treatment Popularity
                        </h3>

                        <p>
                            Most frequently booked dental services
                        </p>

                    </div>

                </div>


                <div class="horizontal-chart"
                     id="treatmentChart">

                    <div class="chart-empty">
                        Loading treatment performance...
                    </div>

                </div>

            </div>


            <!-- =================================================
                 TREATMENT PERFORMANCE TABLE
                 ================================================= -->

            <div class="report-panel wide-report-panel">

                <div class="report-panel-header">

                    <div>

                        <h3>
                            Treatment Performance
                        </h3>

                        <p>
                            Appointment count by treatment
                        </p>

                    </div>

                </div>


                <div class="table-wrapper">

                    <table>

                        <thead>

                        <tr>

                            <th>
                                Rank
                            </th>

                            <th>
                                Treatment
                            </th>

                            <th>
                                Appointments
                            </th>

                        </tr>

                        </thead>


                        <tbody id="treatmentReportBody">

                        <tr>

                            <td colspan="3"
                                class="loading-row">

                                Loading treatment report...

                            </td>

                        </tr>

                        </tbody>

                    </table>

                </div>

            </div>


            <!-- =================================================
                 RECENT PAYMENTS
                 ================================================= -->

            <div class="report-panel wide-report-panel">

                <div class="report-panel-header">

                    <div>

                        <h3>
                            Recent Payments
                        </h3>

                        <p>
                            Latest transactions inside the selected date range
                        </p>

                    </div>

                </div>


                <div class="table-wrapper">

                    <table>

                        <thead>

                        <tr>

                            <th>
                                Receipt
                            </th>

                            <th>
                                Patient
                            </th>

                            <th>
                                Amount
                            </th>

                            <th>
                                Method
                            </th>

                            <th>
                                Status
                            </th>

                            <th>
                                Date
                            </th>

                        </tr>

                        </thead>


                        <tbody id="recentPaymentsBody">

                        <tr>

                            <td colspan="6"
                                class="loading-row">

                                Loading payment records...

                            </td>

                        </tr>

                        </tbody>

                    </table>

                </div>

            </div>


            <!-- =================================================
                 FOOTER
                 ================================================= -->

            <footer class="reports-footer">

                <span>
                    Sunrise Dental Clinic Management Analytics
                </span>

                <span id="activeFilterText">
                    Showing all records
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
     EXISTING REPORTS JS
     ========================================================= -->

<script src="${pageContext.request.contextPath}/js/reports.js"></script>

</body>

</html>