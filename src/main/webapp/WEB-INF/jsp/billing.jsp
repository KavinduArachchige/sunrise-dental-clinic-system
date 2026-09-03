<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Billing | Sunrise Dental Clinic</title>

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
               class="nav-link active">

                <span class="nav-icon">🧾</span>
                <span class="nav-text">Billing</span>

            </a>


            <a href="${pageContext.request.contextPath}/payments"
               class="nav-link">

                <span class="nav-icon">💳</span>
                <span class="nav-text">Payments</span>

            </a>


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

        <header class="topbar">

            <div class="topbar-left">

                <button class="menu-button"
                        id="menuButton">
                    ☰
                </button>

                <div>

                    <h1>
                        Billing
                    </h1>

                    <p>
                        Generate and manage appointment bills
                    </p>

                </div>

            </div>


            <div class="topbar-right">

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


                <button class="primary-btn"
                        id="openBillModal"
                        type="button">

                    + Generate Bill

                </button>

            </div>

        </header>


        <!-- =================================================
             BILLING CONTENT
             ================================================= -->

        <section class="patients-content">

            <div class="patients-header-card">

                <div>

                    <span class="page-label">
                        BILLING MANAGEMENT
                    </span>

                    <h2>
                        Clinic Bills
                    </h2>

                    <p>
                        Generate bills for appointments and monitor billing records.
                    </p>

                </div>


                <div class="patient-total-box">

                    <span>
                        Total Bills
                    </span>

                    <strong id="billCount">
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
                                id="billSearch"
                                placeholder="Search bill number, appointment, patient...">

                    </div>


                    <button
                            class="secondary-btn"
                            id="refreshBills"
                            type="button">

                        Refresh

                    </button>

                </div>


                <!-- BILL TABLE -->

                <div class="table-wrapper">

                    <table>

                        <thead>

                        <tr>

                            <th>Bill No.</th>

                            <th>Appointment</th>

                            <th>Patient</th>

                            <th>Treatment</th>

                            <th>Treatment Amount</th>

                            <th>Consultation Fee</th>

                            <th>Total</th>

                            <th>Bill Date</th>

                            <th>Print</th>

                        </tr>

                        </thead>


                        <tbody id="billTableBody">

                        <tr>

                            <td colspan="9"
                                class="loading-row">

                                Loading bills...

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
     BILL MODAL
     ========================================================= -->

<div class="modal-overlay"
     id="billModal">

    <div class="modal-card">

        <div class="modal-header">

            <div>

                <h2>
                    Generate Bill
                </h2>

                <p>
                    Select an appointment to create the bill.
                </p>

            </div>


            <button class="close-modal"
                    id="closeBillModal"
                    type="button">

                ×

            </button>

        </div>


        <form id="billForm">

            <div class="form-group">

                <label for="appointmentId">
                    Appointment
                </label>

                <select id="appointmentId"
                        required>

                    <option value="">
                        Select appointment
                    </option>

                </select>

            </div>


            <div class="modal-actions">

                <button type="button"
                        class="secondary-btn"
                        id="cancelBillModal">

                    Cancel

                </button>


                <button type="submit"
                        class="primary-btn">

                    Generate Bill

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
     EXISTING BILLING JS
     ========================================================= -->

<script src="${pageContext.request.contextPath}/js/billing.js"></script>

</body>

</html>