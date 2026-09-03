<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Staff Management | Sunrise Dental Clinic</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/dashboard.css">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/patients.css">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/staff.css">

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


        <!-- =================================================
             NAVIGATION
             ================================================= -->

        <nav class="sidebar-nav">

            <a href="${pageContext.request.contextPath}/"
               class="nav-link">

                <span class="nav-icon">
                    ⌂
                </span>

                <span class="nav-text">
                    Dashboard
                </span>

            </a>


            <a href="${pageContext.request.contextPath}/patients"
               class="nav-link">

                <span class="nav-icon">
                    👥
                </span>

                <span class="nav-text">
                    Patients
                </span>

            </a>


            <a href="${pageContext.request.contextPath}/dentists"
               class="nav-link">

                <span class="nav-icon">
                    🦷
                </span>

                <span class="nav-text">
                    Dentists
                </span>

            </a>


            <a href="${pageContext.request.contextPath}/appointments"
               class="nav-link">

                <span class="nav-icon">
                    📅
                </span>

                <span class="nav-text">
                    Appointments
                </span>

            </a>


            <a href="${pageContext.request.contextPath}/appointment-search"
               class="nav-link">

                <span class="nav-icon">
                    🔎
                </span>

                <span class="nav-text">
                    Search Appointment
                </span>

            </a>


            <a href="${pageContext.request.contextPath}/treatments"
               class="nav-link">

                <span class="nav-icon">
                    ✚
                </span>

                <span class="nav-text">
                    Treatments
                </span>

            </a>


            <a href="${pageContext.request.contextPath}/billing"
               class="nav-link">

                <span class="nav-icon">
                    🧾
                </span>

                <span class="nav-text">
                    Billing
                </span>

            </a>


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
                 ADMIN ONLY NAVIGATION
                 ================================================= -->

            <c:if test="${loggedInUser != null && loggedInUser.role == 'ADMIN'}">

                <a href="${pageContext.request.contextPath}/reports"
                   class="nav-link">

                    <span class="nav-icon">
                        📊
                    </span>

                    <span class="nav-text">
                        Reports
                    </span>

                </a>


                <a href="${pageContext.request.contextPath}/staff"
                   class="nav-link active">

                    <span class="nav-icon">
                        🔐
                    </span>

                    <span class="nav-text">
                        Staff
                    </span>

                </a>


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

                <button
                        class="menu-button"
                        id="menuButton"
                        type="button">

                    ☰

                </button>


                <div>

                    <h1>
                        Staff Management
                    </h1>

                    <p>
                        Manage authorized clinic staff accounts and roles
                    </p>

                </div>

            </div>


            <div class="topbar-right">


                <!-- LOGGED IN USER -->

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


                <button
                        class="primary-btn"
                        id="openStaffModal"
                        type="button">

                    + Add Staff

                </button>

            </div>

        </header>


        <!-- =================================================
             STAFF CONTENT
             ================================================= -->

        <section class="patients-content">


            <!-- =================================================
                 HEADER CARD
                 ================================================= -->

            <div class="patients-header-card">

                <div>

                    <span class="page-label">
                        ACCESS CONTROL
                    </span>

                    <h2>
                        Authorized Staff
                    </h2>

                    <p>
                        Create staff accounts, assign roles and control account access.
                    </p>

                </div>


                <div class="patient-total-box">

                    <span>
                        Total Staff
                    </span>

                    <strong id="staffCount">
                        0
                    </strong>

                </div>

            </div>


            <!-- =================================================
                 STAFF PANEL
                 ================================================= -->

            <div class="patient-panel">


                <!-- TOOLBAR -->

                <div class="patient-toolbar">

                    <div class="search-box">

                        <input
                                type="text"
                                id="staffSearch"
                                placeholder="Search name, username or role...">

                    </div>


                    <button
                            type="button"
                            class="secondary-btn"
                            id="refreshStaff">

                        Refresh

                    </button>

                </div>


                <!-- =================================================
                     STAFF TABLE
                     ================================================= -->

                <div class="table-wrapper">

                    <table>

                        <thead>

                        <tr>

                            <th>
                                ID
                            </th>

                            <th>
                                Full Name
                            </th>

                            <th>
                                Username
                            </th>

                            <th>
                                Role
                            </th>

                            <th>
                                Status
                            </th>

                            <th>
                                Actions
                            </th>

                        </tr>

                        </thead>


                        <tbody id="staffTableBody">

                        <tr>

                            <td colspan="6"
                                class="loading-row">

                                Loading staff accounts...

                            </td>

                        </tr>

                        </tbody>

                    </table>

                </div>

            </div>

        </section>

    </main>

</div>


<!-- =========================================================
     ADD STAFF MODAL
     ========================================================= -->

<div class="modal-overlay"
     id="staffModal">

    <div class="modal-card">


        <!-- MODAL HEADER -->

        <div class="modal-header">

            <div>

                <h2>
                    Add Staff Account
                </h2>

                <p>
                    Create an authorized clinic staff account.
                </p>

            </div>


            <button
                    type="button"
                    class="close-modal"
                    id="closeStaffModal">

                ×

            </button>

        </div>


        <!-- =================================================
             STAFF FORM
             ================================================= -->

        <form id="staffForm">


            <!-- FULL NAME -->

            <div class="form-group">

                <label for="fullName">
                    Full Name
                </label>

                <input
                        type="text"
                        id="fullName"
                        required
                        placeholder="Enter staff full name">

            </div>


            <!-- USERNAME -->

            <div class="form-group">

                <label for="username">
                    Username
                </label>

                <input
                        type="text"
                        id="username"
                        required
                        autocomplete="off"
                        placeholder="Enter username">

            </div>


            <!-- PASSWORD -->

            <div class="form-group">

                <label for="password">
                    Password
                </label>

                <input
                        type="password"
                        id="password"
                        required
                        minlength="6"
                        autocomplete="new-password"
                        placeholder="Minimum 6 characters">

            </div>


            <!-- ROLE -->

            <div class="form-group">

                <label for="role">
                    Role
                </label>

                <select
                        id="role"
                        required>

                    <option value="">
                        Select role
                    </option>

                    <option value="ADMIN">
                        Admin
                    </option>

                    <option value="RECEPTIONIST">
                        Receptionist
                    </option>

                </select>

            </div>


            <!-- MODAL ACTIONS -->

            <div class="modal-actions">

                <button
                        type="button"
                        class="secondary-btn"
                        id="cancelStaffModal">

                    Cancel

                </button>


                <button
                        type="submit"
                        class="primary-btn">

                    Create Account

                </button>

            </div>

        </form>

    </div>

</div>


<!-- =========================================================
     TOAST
     ========================================================= -->

<div id="toast"
     class="toast">

    <span id="toastMessage"></span>

</div>


<!-- =========================================================
     STAFF JAVASCRIPT
     ========================================================= -->

<script src="${pageContext.request.contextPath}/js/staff.js"></script>

</body>

</html>