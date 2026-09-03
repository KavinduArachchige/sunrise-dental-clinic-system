<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Audit Logs | Sunrise Dental Clinic</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/audit-logs.css">

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

                <h2>
                    Sunrise
                </h2>

                <span>
                    Dental Clinic
                </span>

            </div>

        </div>


        <nav class="sidebar-nav">

            <a href="${pageContext.request.contextPath}/"
               class="nav-link">

                <span class="nav-icon">⌂</span>
                <span>Dashboard</span>

            </a>


            <a href="${pageContext.request.contextPath}/patients"
               class="nav-link">

                <span class="nav-icon">👥</span>
                <span>Patients</span>

            </a>


            <a href="${pageContext.request.contextPath}/dentists"
               class="nav-link">

                <span class="nav-icon">🦷</span>
                <span>Dentists</span>

            </a>


            <a href="${pageContext.request.contextPath}/appointments"
               class="nav-link">

                <span class="nav-icon">📅</span>
                <span>Appointments</span>

            </a>


            <a href="${pageContext.request.contextPath}/appointment-search"
               class="nav-link">

                <span class="nav-icon">🔎</span>
                <span>Search Appointment</span>

            </a>


            <a href="${pageContext.request.contextPath}/treatments"
               class="nav-link">

                <span class="nav-icon">✚</span>
                <span>Treatments</span>

            </a>


            <a href="${pageContext.request.contextPath}/billing"
               class="nav-link">

                <span class="nav-icon">🧾</span>
                <span>Billing</span>

            </a>


            <a href="${pageContext.request.contextPath}/payments"
               class="nav-link">

                <span class="nav-icon">💳</span>
                <span>Payments</span>

            </a>


            <!-- ADMIN ONLY -->

            <c:if test="${loggedInUser != null && loggedInUser.role == 'ADMIN'}">

                <a href="${pageContext.request.contextPath}/reports"
                   class="nav-link">

                    <span class="nav-icon">📊</span>
                    <span>Reports</span>

                </a>


                <a href="${pageContext.request.contextPath}/staff"
                   class="nav-link">

                    <span class="nav-icon">🔐</span>
                    <span>Staff</span>

                </a>


                <a href="${pageContext.request.contextPath}/audit-logs"
                   class="nav-link active">

                    <span class="nav-icon">🛡️</span>
                    <span>Audit Logs</span>

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

            <div class="system-status">

                <span class="status-dot"></span>

                <div>

                    <strong>
                        System Online
                    </strong>

                    <small>
                        Audit monitoring active
                    </small>

                </div>

            </div>


            <form action="${pageContext.request.contextPath}/logout"
                  method="post">

                <button type="submit"
                        class="logout-btn">

                    <span>↪</span>
                    Logout

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

            <div>

                <span class="page-label">
                    ADMINISTRATION
                </span>

                <h1>
                    Audit Logs
                </h1>

                <p>
                    Monitor system activities and security-sensitive operations.
                </p>

            </div>


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
                            A
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

        </header>


        <!-- =================================================
             PAGE CONTENT
             ================================================= -->

        <section class="content">


            <!-- =================================================
                 HERO
                 ================================================= -->

            <div class="audit-hero">

                <div>

                    <span class="hero-label">
                        SECURITY & ACCOUNTABILITY
                    </span>

                    <h2>
                        System Activity Monitor
                    </h2>

                    <p>
                        Review user actions across patient management,
                        appointments, billing, payments and staff administration.
                    </p>

                </div>


                <div class="hero-icon">
                    🛡️
                </div>

            </div>


            <!-- =================================================
                 SUMMARY CARDS
                 ================================================= -->

            <div class="stats-grid">


                <div class="stat-card">

                    <div class="stat-icon">
                        📋
                    </div>

                    <div>

                        <span>
                            Total Activities
                        </span>

                        <strong id="totalLogs">
                            0
                        </strong>

                    </div>

                </div>


                <div class="stat-card">

                    <div class="stat-icon">
                        👤
                    </div>

                    <div>

                        <span>
                            Active Users
                        </span>

                        <strong id="uniqueUsers">
                            0
                        </strong>

                    </div>

                </div>


                <div class="stat-card">

                    <div class="stat-icon">
                        🧩
                    </div>

                    <div>

                        <span>
                            Modules Tracked
                        </span>

                        <strong id="moduleCount">
                            0
                        </strong>

                    </div>

                </div>


                <div class="stat-card">

                    <div class="stat-icon">
                        🕒
                    </div>

                    <div>

                        <span>
                            Today's Activities
                        </span>

                        <strong id="todayLogs">
                            0
                        </strong>

                    </div>

                </div>

            </div>


            <!-- =================================================
                 FILTER PANEL
                 ================================================= -->

            <div class="filter-panel">

                <div class="filter-header">

                    <div>

                        <h3>
                            Activity History
                        </h3>

                        <p>
                            Search and filter recorded system events.
                        </p>

                    </div>


                    <button type="button"
                            class="refresh-btn"
                            id="refreshButton">

                        ↻ Refresh

                    </button>

                </div>


                <div class="filters">


                    <!-- SEARCH -->

                    <div class="filter-group search-group">

                        <label for="searchInput">
                            Search
                        </label>

                        <input type="text"
                               id="searchInput"
                               placeholder="Search username, action or details...">

                    </div>


                    <!-- MODULE -->

                    <div class="filter-group">

                        <label for="moduleFilter">
                            Module
                        </label>

                        <select id="moduleFilter">

                            <option value="">
                                All Modules
                            </option>

                            <option value="PATIENT">
                                Patient
                            </option>

                            <option value="APPOINTMENT">
                                Appointment
                            </option>

                            <option value="BILLING">
                                Billing
                            </option>

                            <option value="PAYMENT">
                                Payment
                            </option>

                            <option value="STAFF">
                                Staff
                            </option>

                        </select>

                    </div>


                    <!-- ACTION -->

                    <div class="filter-group">

                        <label for="actionFilter">
                            Action
                        </label>

                        <select id="actionFilter">

                            <option value="">
                                All Actions
                            </option>

                        </select>

                    </div>


                    <!-- CLEAR -->

                    <div class="filter-group filter-button-group">

                        <button type="button"
                                class="clear-btn"
                                id="clearFiltersButton">

                            Clear Filters

                        </button>

                    </div>

                </div>

            </div>


            <!-- =================================================
                 AUDIT TABLE
                 ================================================= -->

            <div class="table-panel">

                <div class="table-header">

                    <div>

                        <h3>
                            Audit Trail
                        </h3>

                        <p id="resultText">
                            Loading activities...
                        </p>

                    </div>


                    <span class="security-badge">
                        🔒 ADMIN ONLY
                    </span>

                </div>


                <div class="table-wrapper">

                    <table>

                        <thead>

                        <tr>

                            <th>
                                Date & Time
                            </th>

                            <th>
                                User
                            </th>

                            <th>
                                Module
                            </th>

                            <th>
                                Action
                            </th>

                            <th>
                                Activity Details
                            </th>

                        </tr>

                        </thead>


                        <tbody id="auditTableBody">

                        <tr>

                            <td colspan="5"
                                class="table-message">

                                Loading audit logs...

                            </td>

                        </tr>

                        </tbody>

                    </table>

                </div>

            </div>


            <!-- =================================================
                 FOOTER
                 ================================================= -->

            <footer class="page-footer">

                <p>
                    © 2026 Sunrise Dental Clinic Management System
                </p>

                <span>
                    Secure • Auditable • Accountable
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
     EXISTING AUDIT JS
     ========================================================= -->

<script src="${pageContext.request.contextPath}/js/audit-logs.js"></script>

</body>

</html>