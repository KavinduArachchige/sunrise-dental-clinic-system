const API = {

    patients: "/api/patients",

    dentists: "/api/dentists",

    appointments: "/api/appointments",

    treatments: "/api/treatments",

    bills: "/api/bills",

    payments: "/api/payments"
};


document.addEventListener(
    "DOMContentLoaded",
    () => {

        setCurrentDate();

        setupSidebar();

        loadDashboardData();
    }
);


function setCurrentDate() {

    const currentDate =
        document.getElementById("currentDate");

    const now =
        new Date();

    currentDate.textContent =
        now.toLocaleDateString(
            "en-US",
            {
                weekday: "long",
                year: "numeric",
                month: "long",
                day: "numeric"
            }
        );
}


function setupSidebar() {

    const sidebar =
        document.getElementById("sidebar");

    const menuButton =
        document.getElementById("menuButton");

    menuButton.addEventListener(
        "click",
        () => {

            if (window.innerWidth <= 800) {

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


async function loadDashboardData() {

    try {

        const [
            patients,
            dentists,
            appointments,
            treatments,
            bills,
            payments
        ] = await Promise.all([

            fetchData(API.patients),

            fetchData(API.dentists),

            fetchData(API.appointments),

            fetchData(API.treatments),

            fetchData(API.bills),

            fetchData(API.payments)
        ]);


        updateStatistics(
            patients,
            dentists,
            appointments,
            treatments,
            bills,
            payments
        );


        displayAppointments(
            appointments
        );


    } catch (error) {

        console.error(
            "Dashboard loading error:",
            error
        );

        showToast(
            "Unable to load dashboard information."
        );
    }
}


async function fetchData(url) {

    const response =
        await fetch(url);

    if (!response.ok) {

        throw new Error(
            `Request failed: ${url}`
        );
    }

    return await response.json();
}


function updateStatistics(
    patients,
    dentists,
    appointments,
    treatments,
    bills,
    payments
) {

    document.getElementById(
        "totalPatients"
    ).textContent =
        patients.length;


    document.getElementById(
        "totalDentists"
    ).textContent =
        dentists.length;


    document.getElementById(
        "totalAppointments"
    ).textContent =
        appointments.length;


    document.getElementById(
        "totalTreatments"
    ).textContent =
        treatments.length;


    document.getElementById(
        "totalBills"
    ).textContent =
        bills.length;


    const totalRevenue =
        payments.reduce(
            (total, payment) => {

                return total +
                    Number(
                        payment.paidAmount || 0
                    );
            },
            0
        );


    const formattedRevenue =
        formatCurrency(totalRevenue);


    document.getElementById(
        "totalRevenue"
    ).textContent =
        formattedRevenue;


    document.getElementById(
        "revenueLarge"
    ).textContent =
        formattedRevenue;


    const paidBillIds =
        new Set(
            payments
                .filter(
                    payment =>
                        payment.bill &&
                        payment.bill.billId
                )
                .map(
                    payment =>
                        payment.bill.billId
                )
        );


    const pendingBills =
        bills.filter(
            bill =>
                !paidBillIds.has(
                    bill.billId
                )
        ).length;


    document.getElementById(
        "pendingBills"
    ).textContent =
        pendingBills;


    updateTodayAppointments(
        appointments
    );


    updateRevenueProgress(
        totalRevenue
    );
}


function updateTodayAppointments(
    appointments
) {

    const today =
        getLocalDateString(
            new Date()
        );


    const todayCount =
        appointments.filter(
            appointment =>
                appointment.appointmentDate ===
                today
        ).length;


    document.getElementById(
        "todayAppointments"
    ).textContent =
        todayCount;
}


function displayAppointments(
    appointments
) {

    const tableBody =
        document.getElementById(
            "appointmentTableBody"
        );


    tableBody.innerHTML = "";


    if (
        !appointments ||
        appointments.length === 0
    ) {

        tableBody.innerHTML = `
            <tr>
                <td
                    colspan="6"
                    class="loading-row">
                    No appointments available.
                </td>
            </tr>
        `;

        return;
    }


    const sortedAppointments =
        [...appointments]
            .sort(
                (a, b) => {

                    const first =
                        new Date(
                            `${a.appointmentDate}T${a.appointmentTime}`
                        );

                    const second =
                        new Date(
                            `${b.appointmentDate}T${b.appointmentTime}`
                        );

                    return first - second;
                }
            )
            .slice(0, 6);


    sortedAppointments.forEach(
        appointment => {

            const row =
                document.createElement(
                    "tr"
                );


            const status =
                appointment.status ||
                "SCHEDULED";


            row.innerHTML = `

                <td>
                    <strong>
                        ${safeText(
                appointment.patient?.patientName
                || "Unknown Patient"
            )}
                    </strong>
                </td>

                <td>
                    ${safeText(
                appointment.dentist?.dentistName
                || "Unknown Dentist"
            )}
                </td>

                <td>
                    ${safeText(
                appointment.treatmentType
                || "-"
            )}
                </td>

                <td>
                    ${formatDate(
                appointment.appointmentDate
            )}
                </td>

                <td>
                    ${formatTime(
                appointment.appointmentTime
            )}
                </td>

                <td>

                    <span
                        class="
                            status-badge
                            ${getStatusClass(status)}
                        "
                    >
                        ${safeText(status)}
                    </span>

                </td>
            `;


            tableBody.appendChild(
                row
            );
        }
    );
}


function getStatusClass(status) {

    switch (
        status.toUpperCase()
        ) {

        case "COMPLETED":
            return "status-completed";

        case "CANCELLED":
            return "status-cancelled";

        default:
            return "status-scheduled";
    }
}


function formatCurrency(amount) {

    return (
        "Rs. " +
        Number(amount)
            .toLocaleString(
                "en-LK",
                {
                    minimumFractionDigits: 2,
                    maximumFractionDigits: 2
                }
            )
    );
}


function formatDate(dateValue) {

    if (!dateValue) {
        return "-";
    }


    const date =
        new Date(
            `${dateValue}T00:00:00`
        );


    return date.toLocaleDateString(
        "en-US",
        {
            month: "short",
            day: "2-digit",
            year: "numeric"
        }
    );
}


function formatTime(timeValue) {

    if (!timeValue) {
        return "-";
    }


    const parts =
        timeValue.split(":");


    let hour =
        Number(parts[0]);


    const minute =
        parts[1];


    const period =
        hour >= 12
            ? "PM"
            : "AM";


    hour =
        hour % 12 || 12;


    return `${hour}:${minute} ${period}`;
}


function updateRevenueProgress(
    totalRevenue
) {

    const target =
        100000;


    let percentage =
        (
            totalRevenue /
            target
        ) * 100;


    if (percentage > 100) {
        percentage = 100;
    }


    setTimeout(
        () => {

            document.getElementById(
                "revenueProgress"
            ).style.width =
                `${percentage}%`;
        },
        250
    );
}


function getLocalDateString(
    date
) {

    const year =
        date.getFullYear();


    const month =
        String(
            date.getMonth() + 1
        ).padStart(
            2,
            "0"
        );


    const day =
        String(
            date.getDate()
        ).padStart(
            2,
            "0"
        );


    return `${year}-${month}-${day}`;
}


function safeText(value) {

    if (
        value === null ||
        value === undefined
    ) {

        return "";
    }

    return String(value)
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");
}


function showToast(message) {

    const toast =
        document.getElementById(
            "toast"
        );


    const toastMessage =
        document.getElementById(
            "toastMessage"
        );


    toastMessage.textContent =
        message;


    toast.classList.add(
        "show"
    );


    setTimeout(
        () => {

            toast.classList.remove(
                "show"
            );
        },
        3000
    );
}