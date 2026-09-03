<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Help & User Guide | Sunrise Dental Clinic</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/dashboard.css">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/help.css">

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


        <!-- =================================================
             NAVIGATION
             ================================================= -->

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


            <!-- =============================================
                 ADMIN ONLY NAVIGATION
                 ============================================= -->

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
               class="nav-link active">

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
                        id="menuButton"
                        type="button">

                    ☰

                </button>


                <div>

                    <h1>
                        Help & User Guide
                    </h1>

                    <p>
                        Step-by-step instructions for using the clinic management system
                    </p>

                </div>

            </div>


            <div class="topbar-right">

                <div class="profile">


                    <!-- USER INITIAL -->

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


                    <!-- USER INFORMATION -->

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
             HELP CONTENT
             ================================================= -->

        <section class="help-content">


            <!-- =================================================
                 HERO
                 ================================================= -->

            <div class="help-hero">

                <div>

                    <span class="help-label">
                        SYSTEM USER SUPPORT
                    </span>

                    <h2>
                        Sunrise Dental Clinic Help Center
                    </h2>

                    <p>
                        Follow the guides below to register patients,
                        manage appointments, generate bills,
                        record payments and safely use the system.
                    </p>

                </div>


                <div class="help-hero-icon">
                    📘
                </div>

            </div>


            <!-- =================================================
                 GETTING STARTED
                 ================================================= -->

            <div class="help-section">

                <div class="section-heading">

                    <div class="section-heading-icon">
                        🚀
                    </div>

                    <div>

                        <h3>
                            Getting Started
                        </h3>

                        <p>
                            Basic steps for new clinic staff
                        </p>

                    </div>

                </div>


                <div class="step-grid">


                    <div class="step-card">

                        <span class="step-number">
                            1
                        </span>

                        <h4>
                            Login
                        </h4>

                        <p>
                            Enter your authorized username and password
                            on the login page.
                        </p>

                    </div>


                    <div class="step-card">

                        <span class="step-number">
                            2
                        </span>

                        <h4>
                            Use the Dashboard
                        </h4>

                        <p>
                            The dashboard provides access to
                            patients, appointments, billing and payments.
                        </p>

                    </div>


                    <div class="step-card">

                        <span class="step-number">
                            3
                        </span>

                        <h4>
                            Select a Module
                        </h4>

                        <p>
                            Choose the required feature using
                            the sidebar navigation menu.
                        </p>

                    </div>


                    <div class="step-card">

                        <span class="step-number">
                            4
                        </span>

                        <h4>
                            Logout Safely
                        </h4>

                        <p>
                            Always use the Logout button after completing work.
                        </p>

                    </div>

                </div>

            </div>


            <!-- =================================================
                 PATIENT REGISTRATION
                 ================================================= -->

            <div class="help-section">

                <div class="section-heading">

                    <div class="section-heading-icon">
                        👥
                    </div>

                    <div>

                        <h3>
                            Register a Patient
                        </h3>

                        <p>
                            How to create a new patient record
                        </p>

                    </div>

                </div>


                <div class="instruction-card">

                    <ol>

                        <li>
                            Open <strong>Patients</strong> from the sidebar.
                        </li>

                        <li>
                            Click the <strong>Add Patient</strong>
                            or registration button.
                        </li>

                        <li>
                            Enter the patient's full name,
                            address and contact number.
                        </li>

                        <li>
                            Check all information before saving.
                        </li>

                        <li>
                            Click <strong>Save / Register Patient</strong>.
                        </li>

                    </ol>


                    <div class="guide-note">

                        <strong>
                            Tip:
                        </strong>

                        Make sure the patient's contact number
                        is entered correctly before registration.

                    </div>

                </div>

            </div>


            <!-- =================================================
                 APPOINTMENTS
                 ================================================= -->

            <div class="help-section">

                <div class="section-heading">

                    <div class="section-heading-icon">
                        📅
                    </div>

                    <div>

                        <h3>
                            Book an Appointment
                        </h3>

                        <p>
                            Schedule a patient visit with a dentist
                        </p>

                    </div>

                </div>


                <div class="instruction-card">

                    <ol>

                        <li>
                            Open <strong>Appointments</strong>.
                        </li>

                        <li>
                            Click <strong>Book Appointment</strong>.
                        </li>

                        <li>
                            Select the patient.
                        </li>

                        <li>
                            Select the dentist.
                        </li>

                        <li>
                            Select or enter the treatment type.
                        </li>

                        <li>
                            Choose the appointment date and time.
                        </li>

                        <li>
                            Click <strong>Book Appointment</strong>.
                        </li>

                        <li>
                            The system automatically generates
                            a unique appointment number.
                        </li>

                    </ol>


                    <div class="guide-warning">

                        <strong>
                            Double-booking protection:
                        </strong>

                        The system prevents a dentist from
                        receiving two appointments at the same date and time.

                    </div>

                </div>

            </div>


            <!-- =================================================
                 APPOINTMENT SEARCH
                 ================================================= -->

            <div class="help-section">

                <div class="section-heading">

                    <div class="section-heading-icon">
                        🔎
                    </div>

                    <div>

                        <h3>
                            Search an Appointment
                        </h3>

                        <p>
                            Find a complete patient visit using the appointment number
                        </p>

                    </div>

                </div>


                <div class="instruction-card">

                    <ol>

                        <li>
                            Select <strong>Search Appointment</strong>
                            from the sidebar.
                        </li>

                        <li>
                            Enter the appointment number,
                            for example <strong>APT-12AB34CD</strong>.
                        </li>

                        <li>
                            Click <strong>Search Appointment</strong>.
                        </li>

                        <li>
                            Review patient, dentist,
                            treatment, billing and payment information.
                        </li>

                        <li>
                            Use <strong>Print Details</strong>
                            if a printed copy is required.
                        </li>

                    </ol>

                </div>

            </div>


            <!-- =================================================
                 BILLING
                 ================================================= -->

            <div class="help-section">

                <div class="section-heading">

                    <div class="section-heading-icon">
                        🧾
                    </div>

                    <div>

                        <h3>
                            Generate a Bill
                        </h3>

                        <p>
                            Create and print a patient bill
                        </p>

                    </div>

                </div>


                <div class="instruction-card">

                    <ol>

                        <li>
                            Open <strong>Billing</strong>.
                        </li>

                        <li>
                            Click <strong>Generate Bill</strong>.
                        </li>

                        <li>
                            Select an appointment.
                        </li>

                        <li>
                            Confirm the treatment information.
                        </li>

                        <li>
                            The system calculates the treatment charge
                            and consultation fee automatically.
                        </li>

                        <li>
                            Click <strong>Generate Bill</strong>.
                        </li>

                        <li>
                            Use <strong>Print Bill</strong>
                            from the billing table when required.
                        </li>

                    </ol>

                </div>

            </div>


            <!-- =================================================
                 PAYMENTS
                 ================================================= -->

            <div class="help-section">

                <div class="section-heading">

                    <div class="section-heading-icon">
                        💳
                    </div>

                    <div>

                        <h3>
                            Record a Payment
                        </h3>

                        <p>
                            Process a patient payment and print a receipt
                        </p>

                    </div>

                </div>


                <div class="instruction-card">

                    <ol>

                        <li>
                            Open <strong>Payments</strong>.
                        </li>

                        <li>
                            Click <strong>Record Payment</strong>.
                        </li>

                        <li>
                            Select the required bill.
                        </li>

                        <li>
                            Enter the amount paid.
                        </li>

                        <li>
                            Select the payment method.
                        </li>

                        <li>
                            Save the payment.
                        </li>

                        <li>
                            Check the payment status such as
                            <strong>PAID</strong> or
                            <strong>PARTIALLY PAID</strong>.
                        </li>

                        <li>
                            Use <strong>Print Receipt</strong>
                            from the payment table.
                        </li>

                    </ol>

                </div>

            </div>


            <!-- =================================================
                 ADMIN ONLY HELP
                 ================================================= -->

            <c:if test="${loggedInUser != null && loggedInUser.role == 'ADMIN'}">


                <!-- =============================================
                     REPORTS
                     ============================================= -->

                <div class="help-section">

                    <div class="section-heading">

                        <div class="section-heading-icon">
                            📊
                        </div>

                        <div>

                            <h3>
                                Reports & Analytics
                            </h3>

                            <p>
                                Use management reports for decision making
                            </p>

                        </div>

                    </div>


                    <div class="instruction-card">

                        <ol>

                            <li>
                                Open <strong>Reports</strong>.
                            </li>

                            <li>
                                Review patient, appointment,
                                bill and revenue KPIs.
                            </li>

                            <li>
                                Use the date range filter
                                to analyze a selected period.
                            </li>

                            <li>
                                Review monthly revenue,
                                appointment status and treatment popularity charts.
                            </li>

                            <li>
                                Use recent payment records
                                to monitor clinic income.
                            </li>

                        </ol>

                    </div>

                </div>


                <!-- =============================================
                     STAFF MANAGEMENT
                     ============================================= -->

                <div class="help-section">

                    <div class="section-heading">

                        <div class="section-heading-icon">
                            🔐
                        </div>

                        <div>

                            <h3>
                                Staff Management
                            </h3>

                            <p>
                                Administrator-only account management
                            </p>

                        </div>

                    </div>


                    <div class="instruction-card">

                        <ol>

                            <li>
                                Open <strong>Staff</strong>.
                            </li>

                            <li>
                                Click <strong>Add Staff</strong>
                                to create a new authorized account.
                            </li>

                            <li>
                                Enter full name,
                                username and password.
                            </li>

                            <li>
                                Assign the correct system role.
                            </li>

                            <li>
                                Administrators can change roles,
                                activate/deactivate accounts,
                                and reset passwords.
                            </li>

                        </ol>


                        <div class="guide-warning">

                            <strong>
                                Security:
                            </strong>

                            Staff passwords are securely stored
                            using BCrypt hashing.

                        </div>

                    </div>

                </div>


                <!-- =============================================
                     AUDIT LOGS
                     ============================================= -->

                <div class="help-section">

                    <div class="section-heading">

                        <div class="section-heading-icon">
                            🛡️
                        </div>

                        <div>

                            <h3>
                                Audit Logs
                            </h3>

                            <p>
                                Review important system activities
                            </p>

                        </div>

                    </div>


                    <div class="instruction-card">

                        <ol>

                            <li>
                                Open <strong>Audit Logs</strong>.
                            </li>

                            <li>
                                Review staff activities,
                                modules and timestamps.
                            </li>

                            <li>
                                Search by username or activity.
                            </li>

                            <li>
                                Filter activities by module or action.
                            </li>

                            <li>
                                Use the audit history to support
                                accountability and security monitoring.
                            </li>

                        </ol>

                    </div>

                </div>

            </c:if>


            <!-- =================================================
                 FAQ
                 ================================================= -->

            <div class="help-section">

                <div class="section-heading">

                    <div class="section-heading-icon">
                        💡
                    </div>

                    <div>

                        <h3>
                            Frequently Asked Questions
                        </h3>

                        <p>
                            Common questions and solutions
                        </p>

                    </div>

                </div>


                <div class="faq-list">


                    <details>

                        <summary>
                            Why can't I book an appointment?
                        </summary>

                        <p>
                            Check whether the selected dentist already
                            has an appointment at the selected date and time.
                        </p>

                    </details>


                    <details>

                        <summary>
                            Why can't I generate a second bill?
                        </summary>

                        <p>
                            Each appointment can only have one generated bill.
                        </p>

                    </details>


                    <details>

                        <summary>
                            Why can't I see Staff or Audit Logs?
                        </summary>

                        <p>
                            These modules are restricted to administrator accounts.
                        </p>

                    </details>


                    <details>

                        <summary>
                            How can I print a bill or receipt?
                        </summary>

                        <p>
                            Use the Print Bill button in Billing
                            or Print Receipt button in Payments.
                        </p>

                    </details>


                    <details>

                        <summary>
                            What should I do when my work is finished?
                        </summary>

                        <p>
                            Always click Logout before leaving the system.
                        </p>

                    </details>

                </div>

            </div>


            <!-- =================================================
                 SECURITY
                 ================================================= -->

            <div class="security-help-card">

                <div class="security-help-icon">
                    🔒
                </div>

                <div>

                    <h3>
                        Secure System Usage
                    </h3>

                    <p>
                        Never share your username or password.
                        Always verify patient information before saving,
                        and log out when leaving the workstation.
                    </p>

                </div>

            </div>


            <!-- =================================================
                 FOOTER
                 ================================================= -->

            <footer class="help-footer">

                <span>
                    Sunrise Dental Clinic Management System
                </span>

                <span>
                    Help & User Guide
                </span>

            </footer>

        </section>

    </main>

</div>


<!-- =========================================================
     SIDEBAR JAVASCRIPT
     ========================================================= -->

<script>

    const sidebar =
        document.getElementById(
            "sidebar"
        );

    const menuButton =
        document.getElementById(
            "menuButton"
        );


    if (menuButton && sidebar) {

        menuButton.addEventListener(
            "click",
            () => {

                if (window.innerWidth <= 800) {

                    sidebar
                        .classList
                        .toggle(
                            "mobile-open"
                        );

                } else {

                    sidebar
                        .classList
                        .toggle(
                            "collapsed"
                        );

                }

            }
        );

    }

</script>

</body>

</html>