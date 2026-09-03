<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>
        Dashboard | Sunrise Dental Clinic
    </title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/dashboard.css">

</head>

<body>

<div class="app-layout">

    <!-- =====================================================
         SIDEBAR
         ===================================================== -->

    <aside class="sidebar"
           id="sidebar">

        <!-- BRAND -->

        <div class="brand">

            <div class="brand-logo">
                🦷
            </div>

            <div class="brand-text">

                <h2>
                    Sunrise
                </h2>

                <span>
                    Dental Clinic
                </span>

            </div>

        </div>


        <!-- =================================================
             NAVIGATION
             ================================================= -->

        <nav class="sidebar-nav">


            <!-- DASHBOARD -->

            <a href="${pageContext.request.contextPath}/"
               class="nav-link active">

                <span class="nav-icon">
                    ⌂
                </span>

                <span class="nav-text">
                    Dashboard
                </span>

            </a>


            <!-- PATIENTS -->

            <a href="${pageContext.request.contextPath}/patients"
               class="nav-link">

                <span class="nav-icon">
                    👥
                </span>

                <span class="nav-text">
                    Patients
                </span>

            </a>


            <!-- DENTISTS -->

            <a href="${pageContext.request.contextPath}/dentists"
               class="nav-link">

                <span class="nav-icon">
                    🦷
                </span>

                <span class="nav-text">
                    Dentists
                </span>

            </a>


            <!-- APPOINTMENTS -->

            <a href="${pageContext.request.contextPath}/appointments"
               class="nav-link">

                <span class="nav-icon">
                    📅
                </span>

                <span class="nav-text">
                    Appointments
                </span>

            </a>


            <!-- APPOINTMENT SEARCH -->

            <a href="${pageContext.request.contextPath}/appointment-search"
               class="nav-link">

                <span class="nav-icon">
                    🔎
                </span>

                <span class="nav-text">
                    Search Appointment
                </span>

            </a>


            <!-- TREATMENTS -->

            <a href="${pageContext.request.contextPath}/treatments"
               class="nav-link">

                <span class="nav-icon">
                    ✚
                </span>

                <span class="nav-text">
                    Treatments
                </span>

            </a>


            <!-- BILLING -->

            <a href="${pageContext.request.contextPath}/billing"
               class="nav-link">

                <span class="nav-icon">
                    🧾
                </span>

                <span class="nav-text">
                    Billing
                </span>

            </a>


            <!-- PAYMENTS -->

            <a href="${pageContext.request.contextPath}/payments"
               class="nav-link">

                <span class="nav-icon">
                    💳
                </span>

                <span class="nav-text">
                    Payments
                </span>

            </a>


            <!-- =================================================
                 ADMIN ONLY
                 ================================================= -->

            <c:if test="${loggedInUser != null && loggedInUser.role == 'ADMIN'}">

                <!-- REPORTS -->

                <a href="${pageContext.request.contextPath}/reports"
                   class="nav-link">

                    <span class="nav-icon">
                        📊
                    </span>

                    <span class="nav-text">
                        Reports
                    </span>

                </a>


                <!-- STAFF -->

                <a href="${pageContext.request.contextPath}/staff"
                   class="nav-link">

                    <span class="nav-icon">
                        🔐
                    </span>

                    <span class="nav-text">
                        Staff
                    </span>

                </a>


                <!-- AUDIT LOGS -->

                <a href="${pageContext.request.contextPath}/audit-logs"
                   class="nav-link">

                    <span class="nav-icon">
                        🛡️
                    </span>

                    <span class="nav-text">
                        Audit Logs
                    </span>

                </a>

            </c:if>


            <!-- HELP -->

            <a href="${pageContext.request.contextPath}/help"
               class="nav-link">

                <span class="nav-icon">
                    ❓
                </span>

                <span class="nav-text">
                    Help & Guide
                </span>

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


            <!-- LOGOUT -->

            <form action="${pageContext.request.contextPath}/logout"
                  method="post"
                  class="logout-form">

                <button type="submit"
                        class="logout-btn">

                    <span>
                        ↪
                    </span>

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
                        Dashboard
                    </h1>

                    <p>
                        Welcome to Sunrise Dental Clinic Management System
                    </p>

                </div>

            </div>


            <div class="topbar-right">


                <!-- DATE -->

                <div class="current-date"
                     id="currentDate">
                </div>


                <!-- NOTIFICATION -->

                <button class="notification-btn"
                        type="button">

                    🔔

                    <span class="notification-dot">
                    </span>

                </button>


                <!-- LOGGED-IN USER -->

                <div class="profile">


                    <div class="profile-avatar">

                        <c:choose>

                            <c:when test="${loggedInUser != null
                                            && not empty loggedInUser.fullName}">

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
             DASHBOARD CONTENT
             ================================================= -->

        <section class="dashboard-content">


            <!-- =================================================
                 WELCOME CARD
                 ================================================= -->

            <div class="welcome-card">

                <div class="welcome-text">


                    <span class="welcome-label">
                        CLINIC OVERVIEW
                    </span>


                    <h2>

                        Good day,

                        <span>

                            <c:choose>

                                <c:when test="${loggedInUser != null}">
                                    <c:out value="${loggedInUser.fullName}"/>
                                </c:when>

                                <c:otherwise>
                                    Staff Member
                                </c:otherwise>

                            </c:choose>

                        </span>

                        👋

                    </h2>


                    <p>
                        Monitor appointments, patients,
                        billing and clinic performance
                        from one central dashboard.
                    </p>

                </div>


                <div class="welcome-decoration">
                    🦷
                </div>

            </div>


            <!-- =================================================
                 KPI CARDS
                 ================================================= -->

            <div class="stats-grid">


                <!-- PATIENTS -->

                <div class="stat-card">

                    <div class="stat-icon patients-icon">
                        👥
                    </div>

                    <div class="stat-details">

                        <span>
                            Total Patients
                        </span>

                        <h3 id="totalPatients">
                            0
                        </h3>

                        <p>
                            Registered patients
                        </p>

                    </div>

                </div>


                <!-- DENTISTS -->

                <div class="stat-card">

                    <div class="stat-icon dentist-icon">
                        🦷
                    </div>

                    <div class="stat-details">

                        <span>
                            Dentists
                        </span>

                        <h3 id="totalDentists">
                            0
                        </h3>

                        <p>
                            Active dentists
                        </p>

                    </div>

                </div>


                <!-- APPOINTMENTS -->

                <div class="stat-card">

                    <div class="stat-icon appointment-icon">
                        📅
                    </div>

                    <div class="stat-details">

                        <span>
                            Total Appointments
                        </span>

                        <h3 id="totalAppointments">
                            0
                        </h3>

                        <p>
                            All bookings
                        </p>

                    </div>

                </div>


                <!-- REVENUE -->

                <div class="stat-card">

                    <div class="stat-icon revenue-icon">
                        LKR
                    </div>

                    <div class="stat-details">

                        <span>
                            Total Revenue
                        </span>

                        <h3 id="totalRevenue">
                            Rs. 0.00
                        </h3>

                        <p>
                            Recorded payments
                        </p>

                    </div>

                </div>

            </div>


            <!-- =================================================
                 QUICK ACTIONS
                 ================================================= -->

            <div class="section-header">

                <div>

                    <h2>
                        Quick Actions
                    </h2>

                    <p>
                        Frequently used clinic operations
                    </p>

                </div>

            </div>


            <div class="quick-actions">


                <!-- REGISTER PATIENT -->

                <a href="${pageContext.request.contextPath}/patients"
                   class="action-card">

                    <div class="action-icon">
                        +
                    </div>

                    <div>

                        <strong>
                            Register Patient
                        </strong>

                        <span>
                            Add a new clinic patient
                        </span>

                    </div>

                </a>


                <!-- BOOK APPOINTMENT -->

                <a href="${pageContext.request.contextPath}/appointments"
                   class="action-card">

                    <div class="action-icon">
                        📅
                    </div>

                    <div>

                        <strong>
                            Book Appointment
                        </strong>

                        <span>
                            Schedule patient visit
                        </span>

                    </div>

                </a>


                <!-- GENERATE BILL -->

                <a href="${pageContext.request.contextPath}/billing"
                   class="action-card">

                    <div class="action-icon">
                        🧾
                    </div>

                    <div>

                        <strong>
                            Generate Bill
                        </strong>

                        <span>
                            Create appointment bill
                        </span>

                    </div>

                </a>


                <!-- RECORD PAYMENT -->

                <a href="${pageContext.request.contextPath}/payments"
                   class="action-card">

                    <div class="action-icon">
                        💳
                    </div>

                    <div>

                        <strong>
                            Record Payment
                        </strong>

                        <span>
                            Process clinic payment
                        </span>

                    </div>

                </a>

            </div>


            <!-- =================================================
                 ADMINISTRATION - ADMIN ONLY
                 ================================================= -->

            <c:if test="${loggedInUser != null && loggedInUser.role == 'ADMIN'}">


                <div class="section-header">

                    <div>

                        <h2>
                            Administration
                        </h2>

                        <p>
                            Administrative and security controls
                        </p>

                    </div>

                </div>


                <div class="quick-actions">


                    <!-- STAFF -->

                    <a href="${pageContext.request.contextPath}/staff"
                       class="action-card">

                        <div class="action-icon">
                            🔐
                        </div>

                        <div>

                            <strong>
                                Staff Management
                            </strong>

                            <span>
                                Manage users, roles and account access
                            </span>

                        </div>

                    </a>


                    <!-- AUDIT LOGS -->

                    <a href="${pageContext.request.contextPath}/audit-logs"
                       class="action-card">

                        <div class="action-icon">
                            🛡️
                        </div>

                        <div>

                            <strong>
                                Audit Logs
                            </strong>

                            <span>
                                Review system activities and security events
                            </span>

                        </div>

                    </a>


                    <!-- REPORTS -->

                    <a href="${pageContext.request.contextPath}/reports"
                       class="action-card">

                        <div class="action-icon">
                            📊
                        </div>

                        <div>

                            <strong>
                                Management Reports
                            </strong>

                            <span>
                                Review clinic performance
                            </span>

                        </div>

                    </a>


                    <!-- HELP -->

                    <a href="${pageContext.request.contextPath}/help"
                       class="action-card">

                        <div class="action-icon">
                            ❓
                        </div>

                        <div>

                            <strong>
                                Help & User Guide
                            </strong>

                            <span>
                                Review system operating instructions
                            </span>

                        </div>

                    </a>

                </div>

            </c:if>


            <!-- =================================================
                 DASHBOARD GRID
                 ================================================= -->

            <div class="dashboard-grid">


                <!-- =================================================
                     UPCOMING APPOINTMENTS
                     ================================================= -->

                <div class="panel appointments-panel">


                    <div class="panel-header">

                        <div>

                            <h2>
                                Upcoming Appointments
                            </h2>

                            <p>
                                Next scheduled patient visits
                            </p>

                        </div>


                        <a href="${pageContext.request.contextPath}/appointments"
                           class="view-all-btn">

                            View All →

                        </a>

                    </div>


                    <div class="table-wrapper">

                        <table>

                            <thead>

                            <tr>

                                <th>
                                    Patient
                                </th>

                                <th>
                                    Dentist
                                </th>

                                <th>
                                    Treatment
                                </th>

                                <th>
                                    Date
                                </th>

                                <th>
                                    Time
                                </th>

                                <th>
                                    Status
                                </th>

                            </tr>

                            </thead>


                            <tbody id="appointmentTableBody">

                            <tr>

                                <td colspan="6"
                                    class="loading-row">

                                    Loading appointments...

                                </td>

                            </tr>

                            </tbody>

                        </table>

                    </div>

                </div>


                <!-- =================================================
                     RIGHT COLUMN
                     ================================================= -->

                <div class="right-column">


                    <!-- TODAY OVERVIEW -->

                    <div class="panel summary-panel">

                        <div class="panel-header">

                            <div>

                                <h2>
                                    Today Overview
                                </h2>

                                <p>
                                    Clinic activity summary
                                </p>

                            </div>

                        </div>


                        <div class="summary-list">


                            <!-- TODAY APPOINTMENTS -->

                            <div class="summary-item">

                                <div>

                                    <span>
                                        Today's Appointments
                                    </span>

                                    <strong id="todayAppointments">
                                        0
                                    </strong>

                                </div>

                                <div class="summary-symbol">
                                    📅
                                </div>

                            </div>


                            <!-- TREATMENTS -->

                            <div class="summary-item">

                                <div>

                                    <span>
                                        Treatments Available
                                    </span>

                                    <strong id="totalTreatments">
                                        0
                                    </strong>

                                </div>

                                <div class="summary-symbol">
                                    ✚
                                </div>

                            </div>


                            <!-- BILLS -->

                            <div class="summary-item">

                                <div>

                                    <span>
                                        Generated Bills
                                    </span>

                                    <strong id="totalBills">
                                        0
                                    </strong>

                                </div>

                                <div class="summary-symbol">
                                    🧾
                                </div>

                            </div>


                            <!-- PENDING BILLS -->

                            <div class="summary-item">

                                <div>

                                    <span>
                                        Pending Bills
                                    </span>

                                    <strong id="pendingBills">
                                        0
                                    </strong>

                                </div>

                                <div class="summary-symbol">
                                    ⏳
                                </div>

                            </div>

                        </div>

                    </div>


                    <!-- =================================================
                         PAYMENT SUMMARY
                         ================================================= -->

                    <div class="panel revenue-panel">

                        <div class="panel-header">

                            <div>

                                <h2>
                                    Payment Summary
                                </h2>

                                <p>
                                    Financial overview
                                </p>

                            </div>

                        </div>


                        <div class="revenue-box">

                            <span>
                                Collected Revenue
                            </span>

                            <h2 id="revenueLarge">
                                Rs. 0.00
                            </h2>


                            <div class="revenue-progress">

                                <div class="revenue-progress-bar"
                                     id="revenueProgress">
                                </div>

                            </div>


                            <small>
                                Based on recorded payments
                            </small>

                        </div>

                    </div>

                </div>

            </div>


            <!-- =================================================
                 FOOTER
                 ================================================= -->

            <footer class="dashboard-footer">

                <p>
                    © 2026 Sunrise Dental Clinic Management System
                </p>

                <span>
                    Secure • Reliable • Professional
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
     EXISTING DASHBOARD JS
     ========================================================= -->

<script src="${pageContext.request.contextPath}/js/dashboard.js"></script>

</body>

</html>