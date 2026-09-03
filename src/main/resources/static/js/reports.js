const REPORT_API = {
    patients: "/api/patients",
    dentists: "/api/dentists",
    appointments: "/api/appointments",
    treatments: "/api/treatments",
    bills: "/api/bills",
    payments: "/api/payments"
};


let reportData = {
    patients: [],
    dentists: [],
    appointments: [],
    treatments: [],
    bills: [],
    payments: []
};


let filteredData = {
    patients: [],
    dentists: [],
    appointments: [],
    treatments: [],
    bills: [],
    payments: []
};


document.addEventListener(
    "DOMContentLoaded",
    () => {

        setupSidebar();
        setupControls();
        setReportDate();

        loadReports();
    }
);


function setupSidebar() {

    const sidebar =
        document.getElementById(
            "sidebar"
        );


    document
        .getElementById(
            "menuButton"
        )
        .addEventListener(
            "click",
            () => {

                if (
                    window.innerWidth <= 800
                ) {

                    sidebar.classList.toggle(
                        "mobile-open"
                    );

                } else {

                    sidebar.classList.toggle(
                        "collapsed"
                    );
                }
            }
        );
}


function setupControls() {

    document
        .getElementById(
            "refreshReports"
        )
        .addEventListener(
            "click",
            loadReports
        );


    document
        .getElementById(
            "applyDateFilter"
        )
        .addEventListener(
            "click",
            applyDateFilter
        );


    document
        .getElementById(
            "resetDateFilter"
        )
        .addEventListener(
            "click",
            resetDateFilter
        );
}


function setReportDate() {

    document
        .getElementById(
            "reportDate"
        )
        .textContent =
        new Date()
            .toLocaleDateString(
                "en-US",
                {
                    day: "2-digit",
                    month: "long",
                    year: "numeric"
                }
            );
}


async function loadReports() {

    try {

        const [
            patients,
            dentists,
            appointments,
            treatments,
            bills,
            payments
        ] = await Promise.all([

            fetchJson(
                REPORT_API.patients
            ),

            fetchJson(
                REPORT_API.dentists
            ),

            fetchJson(
                REPORT_API.appointments
            ),

            fetchJson(
                REPORT_API.treatments
            ),

            fetchJson(
                REPORT_API.bills
            ),

            fetchJson(
                REPORT_API.payments
            )
        ]);


        reportData = {
            patients,
            dentists,
            appointments,
            treatments,
            bills,
            payments
        };


        filteredData = {
            patients: [...patients],
            dentists: [...dentists],
            appointments: [...appointments],
            treatments: [...treatments],
            bills: [...bills],
            payments: [...payments]
        };


        renderAllReports();


        showToast(
            "Reports refreshed successfully.",
            "success"
        );


    } catch (error) {

        console.error(
            "Report loading error:",
            error
        );


        showToast(
            "Unable to load reports.",
            "error"
        );
    }
}


async function fetchJson(url) {

    const response =
        await fetch(
            url,
            {
                credentials:
                    "same-origin",

                headers: {
                    "Accept":
                        "application/json"
                }
            }
        );


    if (
        response.status === 401
    ) {

        window.location.href =
            "/login";

        throw new Error(
            "Authentication required."
        );
    }


    if (
        response.status === 403
    ) {

        window.location.href =
            "/access-denied";

        throw new Error(
            "Access denied."
        );
    }


    if (!response.ok) {

        throw new Error(
            `Request failed: ${url}`
        );
    }


    return await response.json();
}


function applyDateFilter() {

    const fromValue =
        document
            .getElementById(
                "fromDate"
            )
            .value;


    const toValue =
        document
            .getElementById(
                "toDate"
            )
            .value;


    if (
        fromValue &&
        toValue &&
        fromValue > toValue
    ) {

        showToast(
            "From Date cannot be after To Date.",
            "warning"
        );

        return;
    }


    filteredData = {
        patients:
            [...reportData.patients],

        dentists:
            [...reportData.dentists],

        treatments:
            [...reportData.treatments],

        appointments:
            filterRecordsByDate(
                reportData.appointments,
                "appointmentDate",
                fromValue,
                toValue
            ),

        bills:
            filterRecordsByDate(
                reportData.bills,
                "billDate",
                fromValue,
                toValue
            ),

        payments:
            filterRecordsByDate(
                reportData.payments,
                "paymentDate",
                fromValue,
                toValue
            )
    };


    renderAllReports();


    document
        .getElementById(
            "activeFilterText"
        )
        .textContent =
        createFilterLabel(
            fromValue,
            toValue
        );


    showToast(
        "Report filter applied.",
        "success"
    );
}


function resetDateFilter() {

    document
        .getElementById(
            "fromDate"
        )
        .value = "";


    document
        .getElementById(
            "toDate"
        )
        .value = "";


    filteredData = {
        patients:
            [...reportData.patients],

        dentists:
            [...reportData.dentists],

        appointments:
            [...reportData.appointments],

        treatments:
            [...reportData.treatments],

        bills:
            [...reportData.bills],

        payments:
            [...reportData.payments]
    };


    renderAllReports();


    document
        .getElementById(
            "activeFilterText"
        )
        .textContent =
        "Showing all records";


    showToast(
        "Date filter cleared.",
        "success"
    );
}


function filterRecordsByDate(
    records,
    field,
    fromValue,
    toValue
) {

    return records.filter(
        record => {

            const rawValue =
                record?.[field];


            if (!rawValue) {

                return false;
            }


            const recordDate =
                extractDateKey(
                    rawValue
                );


            const afterFrom =
                !fromValue ||
                recordDate >= fromValue;


            const beforeTo =
                !toValue ||
                recordDate <= toValue;


            return (
                afterFrom &&
                beforeTo
            );
        }
    );
}


function extractDateKey(value) {

    return String(value)
        .substring(
            0,
            10
        );
}


function createFilterLabel(
    fromValue,
    toValue
) {

    if (
        !fromValue &&
        !toValue
    ) {

        return "Showing all records";
    }


    if (
        fromValue &&
        toValue
    ) {

        return (
            `Showing ${formatSimpleDate(fromValue)} to ` +
            `${formatSimpleDate(toValue)}`
        );
    }


    if (fromValue) {

        return (
            `Showing records from ` +
            `${formatSimpleDate(fromValue)}`
        );
    }


    return (
        `Showing records up to ` +
        `${formatSimpleDate(toValue)}`
    );
}


function renderAllReports() {

    updateMainStatistics();

    updateAppointmentStatus();

    updatePaymentMethods();

    updateFinancialSummary();

    renderMonthlyRevenueChart();

    renderTreatmentChart();

    renderTreatmentPerformance();

    renderRecentPayments();
}


function updateMainStatistics() {

    document
        .getElementById(
            "reportPatients"
        )
        .textContent =
        filteredData.patients.length;


    document
        .getElementById(
            "reportAppointments"
        )
        .textContent =
        filteredData.appointments.length;


    document
        .getElementById(
            "reportBills"
        )
        .textContent =
        filteredData.bills.length;


    document
        .getElementById(
            "reportDentists"
        )
        .textContent =
        filteredData.dentists.length;


    document
        .getElementById(
            "reportTreatments"
        )
        .textContent =
        filteredData.treatments.length;


    document
        .getElementById(
            "reportPayments"
        )
        .textContent =
        filteredData.payments.length;


    const revenue =
        filteredData.payments.reduce(
            (sum, payment) => {

                return (
                    sum +
                    Number(
                        payment.paidAmount ||
                        0
                    )
                );
            },
            0
        );


    document
        .getElementById(
            "reportRevenue"
        )
        .textContent =
        formatCurrency(
            revenue
        );
}


function updateAppointmentStatus() {

    const scheduled =
        filteredData.appointments.filter(
            appointment =>
                appointment.status ===
                "SCHEDULED"
        ).length;


    const completed =
        filteredData.appointments.filter(
            appointment =>
                appointment.status ===
                "COMPLETED"
        ).length;


    const cancelled =
        filteredData.appointments.filter(
            appointment =>
                appointment.status ===
                "CANCELLED"
        ).length;


    document
        .getElementById(
            "scheduledCount"
        )
        .textContent =
        scheduled;


    document
        .getElementById(
            "completedCount"
        )
        .textContent =
        completed;


    document
        .getElementById(
            "cancelledCount"
        )
        .textContent =
        cancelled;


    const total =
        scheduled +
        completed +
        cancelled;


    document
        .getElementById(
            "appointmentDonutTotal"
        )
        .textContent =
        total;


    const donut =
        document.getElementById(
            "appointmentDonut"
        );


    if (total === 0) {

        donut.style.background =
            "conic-gradient(#e4e7ec 0deg 360deg)";

        return;
    }


    const scheduledDegrees =
        (scheduled / total) *
        360;


    const completedDegrees =
        (completed / total) *
        360;


    const completedEnd =
        scheduledDegrees +
        completedDegrees;


    donut.style.background =
        `conic-gradient(
            #1570ef 0deg ${scheduledDegrees}deg,
            #12b76a ${scheduledDegrees}deg ${completedEnd}deg,
            #f04438 ${completedEnd}deg 360deg
        )`;
}


function updatePaymentMethods() {

    const cash =
        filteredData.payments.filter(
            payment =>
                normalizeValue(
                    payment.paymentMethod
                ) === "CASH"
        ).length;


    const card =
        filteredData.payments.filter(
            payment =>
                normalizeValue(
                    payment.paymentMethod
                ) === "CARD"
        ).length;


    const bank =
        filteredData.payments.filter(
            payment => {

                const method =
                    normalizeValue(
                        payment.paymentMethod
                    );


                return (
                    method ===
                    "BANK_TRANSFER" ||
                    method ===
                    "BANK TRANSFER"
                );
            }
        ).length;


    document
        .getElementById(
            "cashCount"
        )
        .textContent =
        cash;


    document
        .getElementById(
            "cardCount"
        )
        .textContent =
        card;


    document
        .getElementById(
            "bankCount"
        )
        .textContent =
        bank;
}


function updateFinancialSummary() {

    const totalBilled =
        filteredData.bills.reduce(
            (sum, bill) => {

                return (
                    sum +
                    Number(
                        bill.totalAmount ||
                        0
                    )
                );
            },
            0
        );


    const collected =
        filteredData.payments.reduce(
            (sum, payment) => {

                return (
                    sum +
                    Number(
                        payment.paidAmount ||
                        0
                    )
                );
            },
            0
        );


    const outstanding =
        Math.max(
            totalBilled -
            collected,
            0
        );


    document
        .getElementById(
            "totalBilled"
        )
        .textContent =
        formatCurrency(
            totalBilled
        );


    document
        .getElementById(
            "totalCollected"
        )
        .textContent =
        formatCurrency(
            collected
        );


    document
        .getElementById(
            "outstandingAmount"
        )
        .textContent =
        formatCurrency(
            outstanding
        );
}


function renderMonthlyRevenueChart() {

    const chart =
        document.getElementById(
            "revenueChart"
        );


    const monthly =
        {};


    filteredData.payments.forEach(
        payment => {

            if (
                !payment.paymentDate
            ) {

                return;
            }


            const date =
                new Date(
                    payment.paymentDate
                );


            if (
                Number.isNaN(
                    date.getTime()
                )
            ) {

                return;
            }


            const key =
                `${date.getFullYear()}-${String(
                    date.getMonth() + 1
                ).padStart(2, "0")}`;


            monthly[key] =
                (
                    monthly[key] ||
                    0
                ) +
                Number(
                    payment.paidAmount ||
                    0
                );
        }
    );


    const entries =
        Object.entries(
            monthly
        )
            .sort(
                (a, b) =>
                    a[0]
                        .localeCompare(
                            b[0]
                        )
            );


    if (
        entries.length === 0
    ) {

        chart.innerHTML = `
            <div class="chart-empty">
                No revenue data available.
            </div>
        `;

        return;
    }


    const maxValue =
        Math.max(
            ...entries.map(
                entry =>
                    entry[1]
            ),
            1
        );


    chart.innerHTML =
        entries
            .map(
                ([month, amount]) => {

                    const percentage =
                        Math.max(
                            (
                                amount /
                                maxValue
                            ) * 100,
                            2
                        );


                    return `
                        <div class="bar-item">

                            <span class="bar-value">
                                ${safeText(
                        formatCompactCurrency(
                            amount
                        )
                    )}
                            </span>

                            <div class="bar-track">

                                <div
                                    class="bar-fill"
                                    style="
                                        height:
                                        ${percentage}%;
                                    ">
                                </div>

                            </div>

                            <span class="bar-label">
                                ${safeText(
                        formatMonthKey(
                            month
                        )
                    )}
                            </span>

                        </div>
                    `;
                }
            )
            .join("");
}


function renderTreatmentChart() {

    const chart =
        document.getElementById(
            "treatmentChart"
        );


    const treatmentData =
        getTreatmentCounts();


    if (
        treatmentData.length === 0
    ) {

        chart.innerHTML = `
            <div class="chart-empty">
                No treatment booking data available.
            </div>
        `;

        return;
    }


    const maxCount =
        Math.max(
            ...treatmentData.map(
                item =>
                    item.count
            ),
            1
        );


    chart.innerHTML =
        treatmentData
            .slice(
                0,
                8
            )
            .map(
                item => {

                    const width =
                        (
                            item.count /
                            maxCount
                        ) * 100;


                    return `
                        <div class="horizontal-bar-row">

                            <div class="horizontal-bar-label"
                                 title="${safeText(item.name)}">

                                ${safeText(item.name)}

                            </div>

                            <div class="horizontal-bar-track">

                                <div
                                    class="horizontal-bar-fill"
                                    style="
                                        width:
                                        ${width}%;
                                    ">
                                </div>

                            </div>

                            <div class="horizontal-bar-value">
                                ${item.count}
                            </div>

                        </div>
                    `;
                }
            )
            .join("");
}


function renderTreatmentPerformance() {

    const body =
        document.getElementById(
            "treatmentReportBody"
        );


    const treatmentData =
        getTreatmentCounts();


    body.innerHTML =
        "";


    if (
        treatmentData.length === 0
    ) {

        body.innerHTML = `
            <tr>
                <td
                    colspan="3"
                    class="empty-state">

                    No treatment performance data available.

                </td>
            </tr>
        `;

        return;
    }


    treatmentData.forEach(
        (item, index) => {

            const row =
                document.createElement(
                    "tr"
                );


            row.innerHTML = `

                <td>
                    #${index + 1}
                </td>

                <td>
                    <strong>
                        ${safeText(
                item.name
            )}
                    </strong>
                </td>

                <td>
                    ${item.count}
                </td>
            `;


            body.appendChild(
                row
            );
        }
    );
}


function getTreatmentCounts() {

    const counts =
        {};


    filteredData.appointments.forEach(
        appointment => {

            const treatment =
                appointment.treatmentType ||
                "Unknown";


            counts[treatment] =
                (
                    counts[treatment] ||
                    0
                ) + 1;
        }
    );


    return Object
        .entries(
            counts
        )
        .map(
            ([name, count]) => ({
                name,
                count
            })
        )
        .sort(
            (a, b) =>
                b.count -
                a.count
        );
}


function renderRecentPayments() {

    const body =
        document.getElementById(
            "recentPaymentsBody"
        );


    body.innerHTML =
        "";


    const payments =
        [...filteredData.payments]
            .sort(
                (a, b) =>
                    new Date(
                        b.paymentDate
                    ) -
                    new Date(
                        a.paymentDate
                    )
            )
            .slice(
                0,
                8
            );


    if (
        payments.length === 0
    ) {

        body.innerHTML = `
            <tr>
                <td
                    colspan="6"
                    class="empty-state">

                    No payment records available.

                </td>
            </tr>
        `;

        return;
    }


    payments.forEach(
        payment => {

            const row =
                document.createElement(
                    "tr"
                );


            row.innerHTML = `

                <td>
                    <strong>
                        ${safeText(
                payment.receiptNumber
            )}
                    </strong>
                </td>

                <td>
                    ${safeText(
                payment
                    .bill
                    ?.appointment
                    ?.patient
                    ?.patientName
            )}
                </td>

                <td>
                    <strong>
                        ${formatCurrency(
                payment.paidAmount
            )}
                    </strong>
                </td>

                <td>
                    ${safeText(
                payment.paymentMethod
            )}
                </td>

                <td>
                    <span
                        class="
                            status-badge
                            ${getPaymentStatusClass(
                payment.paymentStatus
            )}
                        ">
                        ${safeText(
                payment.paymentStatus
            )}
                    </span>
                </td>

                <td>
                    ${formatDate(
                payment.paymentDate
            )}
                </td>
            `;


            body.appendChild(
                row
            );
        }
    );
}


function getPaymentStatusClass(status) {

    switch (
        normalizeValue(
            status
        )
        ) {

        case "PAID":
            return "status-completed";

        case "PARTIALLY_PAID":
            return "status-scheduled";

        default:
            return "status-cancelled";
    }
}


function normalizeValue(value) {

    return String(
        value ||
        ""
    )
        .trim()
        .toUpperCase();
}


function formatCurrency(amount) {

    return (
        "Rs. " +
        Number(
            amount ||
            0
        )
            .toLocaleString(
                "en-LK",
                {
                    minimumFractionDigits:
                        2,

                    maximumFractionDigits:
                        2
                }
            )
    );
}


function formatCompactCurrency(amount) {

    const value =
        Number(
            amount ||
            0
        );


    if (
        value >=
        1000000
    ) {

        return (
            "Rs. " +
            (
                value /
                1000000
            )
                .toFixed(1) +
            "M"
        );
    }


    if (
        value >=
        1000
    ) {

        return (
            "Rs. " +
            (
                value /
                1000
            )
                .toFixed(1) +
            "K"
        );
    }


    return (
        "Rs. " +
        value.toFixed(0)
    );
}


function formatMonthKey(key) {

    const parts =
        String(key)
            .split("-");


    if (
        parts.length !== 2
    ) {

        return key;
    }


    const date =
        new Date(
            Number(parts[0]),
            Number(parts[1]) - 1,
            1
        );


    return date
        .toLocaleDateString(
            "en-US",
            {
                month:
                    "short",

                year:
                    "2-digit"
            }
        );
}


function formatDate(value) {

    if (!value) {

        return "-";
    }


    const date =
        new Date(
            value
        );


    if (
        Number.isNaN(
            date.getTime()
        )
    ) {

        return value;
    }


    return date
        .toLocaleString(
            "en-US",
            {
                day:
                    "2-digit",

                month:
                    "short",

                year:
                    "numeric",

                hour:
                    "2-digit",

                minute:
                    "2-digit"
            }
        );
}


function formatSimpleDate(value) {

    if (!value) {

        return "-";
    }


    const parts =
        String(value)
            .split("-");


    if (
        parts.length !== 3
    ) {

        return value;
    }


    const date =
        new Date(
            Number(parts[0]),
            Number(parts[1]) - 1,
            Number(parts[2])
        );


    return date
        .toLocaleDateString(
            "en-US",
            {
                day:
                    "2-digit",

                month:
                    "short",

                year:
                    "numeric"
            }
        );
}


function showToast(
    message,
    type = "success"
) {

    const toast =
        document.getElementById(
            "toast"
        );


    const messageElement =
        document.getElementById(
            "toastMessage"
        );


    if (
        !toast ||
        !messageElement
    ) {

        return;
    }


    messageElement.textContent =
        message;


    toast.classList.remove(
        "success",
        "error",
        "warning",
        "show"
    );


    toast.classList.add(
        type
    );


    void toast.offsetWidth;


    toast.classList.add(
        "show"
    );


    clearTimeout(
        window.reportToastTimer
    );


    window.reportToastTimer =
        setTimeout(
            () => {

                toast.classList.remove(
                    "show"
                );

            },
            3000
        );
}


function safeText(value) {

    if (
        value === null ||
        value === undefined
    ) {

        return "";
    }


    return String(value)

        .replaceAll(
            "&",
            "&amp;"
        )

        .replaceAll(
            "<",
            "&lt;"
        )

        .replaceAll(
            ">",
            "&gt;"
        )

        .replaceAll(
            '"',
            "&quot;"
        )

        .replaceAll(
            "'",
            "&#039;"
        );
}