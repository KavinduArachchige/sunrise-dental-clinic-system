<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Payments | Sunrise Dental Clinic</title>

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
               class="nav-link active">

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
                        Payments
                    </h1>

                    <p>
                        Record and manage clinic payments
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


                <button class="primary-btn"
                        id="openPaymentModal"
                        type="button">

                    + Record Payment

                </button>

            </div>

        </header>


        <!-- =================================================
             PAYMENT CONTENT
             ================================================= -->

        <section class="patients-content">

            <div class="patients-header-card">

                <div>

                    <span class="page-label">
                        PAYMENT MANAGEMENT
                    </span>

                    <h2>
                        Clinic Payments
                    </h2>

                    <p>
                        Record bill payments and monitor collected revenue.
                    </p>

                </div>


                <div class="patient-total-box">

                    <span>
                        Total Revenue
                    </span>

                    <strong id="totalRevenue">
                        Rs. 0.00
                    </strong>

                </div>

            </div>


            <div class="patient-panel">


                <!-- TOOLBAR -->

                <div class="patient-toolbar">

                    <div class="search-box">

                        <input
                                type="text"
                                id="paymentSearch"
                                placeholder="Search receipt, bill, patient, method or status...">

                    </div>


                    <button
                            class="secondary-btn"
                            id="refreshPayments"
                            type="button">

                        Refresh

                    </button>

                </div>


                <!-- PAYMENT TABLE -->

                <div class="table-wrapper">

                    <table>

                        <thead>

                        <tr>

                            <th>
                                Receipt No.
                            </th>

                            <th>
                                Bill No.
                            </th>

                            <th>
                                Patient
                            </th>

                            <th>
                                Paid Amount
                            </th>

                            <th>
                                Method
                            </th>

                            <th>
                                Status
                            </th>

                            <th>
                                Payment Date
                            </th>

                            <th>
                                Print
                            </th>

                        </tr>

                        </thead>


                        <tbody id="paymentTableBody">

                        <tr>

                            <td colspan="8"
                                class="loading-row">

                                Loading payments...

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
     PAYMENT MODAL
     ========================================================= -->

<div class="modal-overlay"
     id="paymentModal">

    <div class="modal-card">

        <div class="modal-header">

            <div>

                <h2>
                    Record Payment
                </h2>

                <p>
                    Select a bill and enter payment details.
                </p>

            </div>


            <button class="close-modal"
                    id="closePaymentModal"
                    type="button">

                ×

            </button>

        </div>


        <form id="paymentForm">


            <!-- BILL -->

            <div class="form-group">

                <label for="billId">
                    Bill
                </label>

                <select id="billId"
                        required>

                    <option value="">
                        Select bill
                    </option>

                </select>

            </div>


            <!-- PAID AMOUNT -->

            <div class="form-group">

                <label for="paidAmount">
                    Paid Amount (LKR)
                </label>

                <input
                        type="number"
                        id="paidAmount"
                        min="0.01"
                        step="0.01"
                        required
                        placeholder="6500.00">

            </div>


            <!-- PAYMENT METHOD -->

            <div class="form-group">

                <label for="paymentMethod">
                    Payment Method
                </label>

                <select id="paymentMethod"
                        required>

                    <option value="">
                        Select payment method
                    </option>

                    <option value="CASH">
                        Cash
                    </option>

                    <option value="CARD">
                        Card
                    </option>

                    <option value="BANK_TRANSFER">
                        Bank Transfer
                    </option>

                </select>

            </div>


            <!-- MODAL BUTTONS -->

            <div class="modal-actions">

                <button
                        type="button"
                        class="secondary-btn"
                        id="cancelPaymentModal">

                    Cancel

                </button>


                <button
                        type="submit"
                        class="primary-btn">

                    Record Payment

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
     EXISTING PAYMENTS JS
     ========================================================= -->

<script src="${pageContext.request.contextPath}/js/payments.js"></script>

</body>

</html>