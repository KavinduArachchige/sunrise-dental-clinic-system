<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Dentists | Sunrise Dental Clinic</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/dashboard.css">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/patients.css">

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
               class="nav-link active">

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
                   class="nav-link">

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

        <header class="topbar">

            <div class="topbar-left">

                <button class="menu-button"
                        id="menuButton">
                    ☰
                </button>

                <div>

                    <h1>
                        Dentists
                    </h1>

                    <p>
                        Manage clinic dental professionals
                    </p>

                </div>

            </div>


            <div class="topbar-right">

                <div class="profile">

                    <div class="profile-avatar">

                        <c:choose>

                            <c:when test="${loggedInUser != null && not empty loggedInUser.fullName}">
                                ${fn:substring(loggedInUser.fullName, 0, 1)}
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


                <button class="primary-btn"
                        id="openDentistModal"
                        type="button">

                    + Add Dentist

                </button>

            </div>

        </header>


        <!-- =================================================
             DENTIST CONTENT
             ================================================= -->

        <section class="patients-content">

            <div class="patients-header-card">

                <div>

                    <span class="page-label">
                        DENTIST MANAGEMENT
                    </span>

                    <h2>
                        Clinic Dentists
                    </h2>

                    <p>
                        Manage dentists, specializations and contact information.
                    </p>

                </div>


                <div class="patient-total-box">

                    <span>
                        Total Dentists
                    </span>

                    <strong id="dentistCount">
                        0
                    </strong>

                </div>

            </div>


            <div class="patient-panel">

                <!-- TOOLBAR -->

                <div class="patient-toolbar">

                    <div class="search-box">

                        <input
                                type="text"
                                id="dentistSearch"
                                placeholder="Search name, specialization, phone or email...">

                    </div>


                    <button
                            class="secondary-btn"
                            id="refreshDentists"
                            type="button">

                        Refresh

                    </button>

                </div>


                <!-- TABLE -->

                <div class="table-wrapper">

                    <table>

                        <thead>

                        <tr>

                            <th>ID</th>

                            <th>
                                Dentist Name
                            </th>

                            <th>
                                Specialization
                            </th>

                            <th>
                                Contact Number
                            </th>

                            <th>
                                Email
                            </th>

                            <th>
                                Actions
                            </th>

                        </tr>

                        </thead>


                        <tbody id="dentistTableBody">

                        <tr>

                            <td colspan="6"
                                class="loading-row">

                                Loading dentists...

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
     DENTIST MODAL
     ========================================================= -->

<div class="modal-overlay"
     id="dentistModal">

    <div class="modal-card">

        <div class="modal-header">

            <div>

                <h2 id="dentistModalTitle">
                    Add Dentist
                </h2>

                <p>
                    Enter dentist information below.
                </p>

            </div>


            <button class="close-modal"
                    id="closeDentistModal"
                    type="button">

                ×

            </button>

        </div>


        <form id="dentistForm">

            <input type="hidden"
                   id="dentistId">


            <div class="form-group">

                <label for="dentistName">
                    Dentist Name
                </label>

                <input
                        type="text"
                        id="dentistName"
                        required
                        placeholder="Dr. Kasun Silva">

            </div>


            <div class="form-group">

                <label for="specialization">
                    Specialization
                </label>

                <input
                        type="text"
                        id="specialization"
                        required
                        placeholder="General Dentistry">

            </div>


            <div class="form-group">

                <label for="dentistContact">
                    Contact Number
                </label>

                <input
                        type="text"
                        id="dentistContact"
                        required
                        placeholder="0712345678">

            </div>


            <div class="form-group">

                <label for="dentistEmail">
                    Email
                </label>

                <input
                        type="email"
                        id="dentistEmail"
                        required
                        placeholder="dentist@sunrisedental.lk">

            </div>


            <div class="modal-actions">

                <button
                        type="button"
                        class="secondary-btn"
                        id="cancelDentistModal">

                    Cancel

                </button>


                <button
                        type="submit"
                        class="primary-btn">

                    Save Dentist

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
     EXISTING DENTIST JS
     ========================================================= -->

<script src="${pageContext.request.contextPath}/js/dentists.js"></script>

</body>
</html>