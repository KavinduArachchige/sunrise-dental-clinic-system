const APPOINTMENT_DETAILS_API =
    "/api/appointments/details";


document.addEventListener(
    "DOMContentLoaded",
    () => {

        setupSidebar();
        setupSearch();
        setupPrint();
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


function setupSearch() {

    const form =
        document.getElementById(
            "appointmentSearchForm"
        );


    const input =
        document.getElementById(
            "appointmentNumber"
        );


    input.addEventListener(
        "input",
        () => {

            input.value =
                input.value
                    .toUpperCase();
        }
    );


    form.addEventListener(
        "submit",
        async event => {

            event.preventDefault();


            const appointmentNumber =
                input.value
                    .trim()
                    .toUpperCase();


            if (!appointmentNumber) {

                showToast(
                    "Please enter an appointment number.",
                    "warning"
                );

                return;
            }


            await searchAppointment(
                appointmentNumber
            );
        }
    );
}


async function searchAppointment(
    appointmentNumber
) {

    const searchButton =
        document.getElementById(
            "searchButton"
        );


    setLoadingState(
        searchButton,
        true
    );


    hideAllStates();


    try {

        const response =
            await fetch(
                `${APPOINTMENT_DETAILS_API}/${encodeURIComponent(
                    appointmentNumber
                )}`,
                {
                    method: "GET",

                    credentials:
                        "same-origin",

                    headers: {
                        "Accept":
                            "application/json"
                    }
                }
            );


        if (response.status === 401) {

            window.location.href =
                "/login";

            return;
        }


        if (response.status === 403) {

            window.location.href =
                "/access-denied";

            return;
        }


        if (response.status === 404) {

            showNotFound(
                appointmentNumber
            );

            return;
        }


        if (!response.ok) {

            throw new Error(
                `Unable to search appointment. HTTP ${response.status}`
            );
        }


        const contentType =
            response.headers.get(
                "content-type"
            ) || "";


        if (
            !contentType.includes(
                "application/json"
            )
        ) {

            throw new Error(
                "Invalid response received from the server."
            );
        }


        const appointment =
            await response.json();


        renderAppointment(
            appointment
        );


    } catch (error) {

        console.error(
            "Appointment search error:",
            error
        );


        showNotFound(
            appointmentNumber,
            error.message
        );


        showToast(
            error.message ||
            "Unable to search appointment.",
            "error"
        );


    } finally {

        setLoadingState(
            searchButton,
            false
        );
    }
}


function renderAppointment(
    appointment
) {

    document
        .getElementById(
            "emptyState"
        )
        .classList
        .add(
            "hidden"
        );


    document
        .getElementById(
            "notFoundState"
        )
        .classList
        .add(
            "hidden"
        );


    document
        .getElementById(
            "resultSection"
        )
        .classList
        .remove(
            "hidden"
        );


    setText(
        "resultAppointmentNumber",
        appointment.appointmentNumber
    );


    setText(
        "patientId",
        appointment.patientId
            ? `#${appointment.patientId}`
            : "-"
    );


    setText(
        "patientName",
        appointment.patientName
    );


    setText(
        "patientAddress",
        appointment.patientAddress
    );


    setText(
        "patientContact",
        appointment.patientContactNumber
    );


    setText(
        "dentistId",
        appointment.dentistId
            ? `#${appointment.dentistId}`
            : "-"
    );


    setText(
        "dentistName",
        appointment.dentistName
    );


    setText(
        "dentistSpecialization",
        appointment.dentistSpecialization
    );


    setText(
        "treatmentType",
        appointment.treatmentType
    );


    setText(
        "appointmentDate",
        formatDate(
            appointment.appointmentDate
        )
    );


    setText(
        "appointmentTime",
        formatTime(
            appointment.appointmentTime
        )
    );


    setText(
        "appointmentStatusText",
        formatLabel(
            appointment.appointmentStatus
        )
    );


    updateAppointmentStatusBadge(
        appointment.appointmentStatus
    );


    renderBilling(
        appointment
    );


    renderPayment(
        appointment
    );


    showToast(
        "Appointment found successfully.",
        "success"
    );
}


function renderBilling(
    appointment
) {

    const available =
        document.getElementById(
            "billAvailable"
        );


    const unavailable =
        document.getElementById(
            "billUnavailable"
        );


    if (
        appointment.billGenerated
    ) {

        available.classList.remove(
            "hidden"
        );


        unavailable.classList.add(
            "hidden"
        );


        setText(
            "billNumber",
            appointment.billNumber
        );


        setText(
            "treatmentAmount",
            formatCurrency(
                appointment.treatmentAmount
            )
        );


        setText(
            "consultationFee",
            formatCurrency(
                appointment.consultationFee
            )
        );


        setText(
            "totalAmount",
            formatCurrency(
                appointment.totalAmount
            )
        );


    } else {

        available.classList.add(
            "hidden"
        );


        unavailable.classList.remove(
            "hidden"
        );
    }
}


function renderPayment(
    appointment
) {

    const available =
        document.getElementById(
            "paymentAvailable"
        );


    const unavailable =
        document.getElementById(
            "paymentUnavailable"
        );


    if (
        appointment.paymentRecorded
    ) {

        available.classList.remove(
            "hidden"
        );


        unavailable.classList.add(
            "hidden"
        );


        setText(
            "receiptNumber",
            appointment.receiptNumber
        );


        setText(
            "paidAmount",
            formatCurrency(
                appointment.paidAmount
            )
        );


        setText(
            "paymentMethod",
            formatLabel(
                appointment.paymentMethod
            )
        );


        setText(
            "paymentStatus",
            formatLabel(
                appointment.paymentStatus
            )
        );


    } else {

        available.classList.add(
            "hidden"
        );


        unavailable.classList.remove(
            "hidden"
        );
    }
}


function updateAppointmentStatusBadge(
    status
) {

    const badge =
        document.getElementById(
            "appointmentStatusBadge"
        );


    const normalized =
        String(
            status || ""
        )
            .toUpperCase();


    badge.classList.remove(
        "status-scheduled-search",
        "status-completed-search",
        "status-cancelled-search",
        "status-default-search"
    );


    switch (normalized) {

        case "SCHEDULED":

            badge.classList.add(
                "status-scheduled-search"
            );

            break;


        case "COMPLETED":

            badge.classList.add(
                "status-completed-search"
            );

            break;


        case "CANCELLED":

            badge.classList.add(
                "status-cancelled-search"
            );

            break;


        default:

            badge.classList.add(
                "status-default-search"
            );
    }


    badge.textContent =
        normalized ||
        "UNKNOWN";
}


function showNotFound(
    appointmentNumber,
    customMessage = null
) {

    document
        .getElementById(
            "emptyState"
        )
        .classList
        .add(
            "hidden"
        );


    document
        .getElementById(
            "resultSection"
        )
        .classList
        .add(
            "hidden"
        );


    document
        .getElementById(
            "notFoundState"
        )
        .classList
        .remove(
            "hidden"
        );


    document
        .getElementById(
            "notFoundMessage"
        )
        .textContent =
        customMessage ||
        `No appointment was found for ${appointmentNumber}.`;
}


function hideAllStates() {

    document
        .getElementById(
            "emptyState"
        )
        .classList
        .add(
            "hidden"
        );


    document
        .getElementById(
            "notFoundState"
        )
        .classList
        .add(
            "hidden"
        );


    document
        .getElementById(
            "resultSection"
        )
        .classList
        .add(
            "hidden"
        );
}


function setupPrint() {

    document
        .getElementById(
            "printButton"
        )
        .addEventListener(
            "click",
            () => {

                window.print();
            }
        );
}


function setLoadingState(
    button,
    loading
) {

    button.disabled =
        loading;


    button.textContent =
        loading
            ? "Searching..."
            : "Search Appointment";
}


function setText(
    id,
    value
) {

    document
        .getElementById(
            id
        )
        .textContent =
        value === null ||
        value === undefined ||
        value === ""
            ? "-"
            : value;
}


function formatCurrency(
    amount
) {

    return (
        "Rs. " +
        Number(
            amount || 0
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


function formatDate(
    value
) {

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


    return date.toLocaleDateString(
        "en-GB",
        {
            day: "2-digit",
            month: "long",
            year: "numeric"
        }
    );
}


function formatTime(
    value
) {

    if (!value) {
        return "-";
    }


    const parts =
        String(value)
            .split(":");


    if (
        parts.length < 2
    ) {

        return value;
    }


    let hours =
        Number(parts[0]);


    const minutes =
        parts[1];


    const suffix =
        hours >= 12
            ? "PM"
            : "AM";


    hours =
        hours % 12 ||
        12;


    return `${hours}:${minutes} ${suffix}`;
}


function formatLabel(
    value
) {

    if (!value) {
        return "-";
    }


    return String(value)
        .replaceAll(
            "_",
            " "
        )
        .toLowerCase()
        .replace(
            /\b\w/g,
            character =>
                character.toUpperCase()
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


    const toastMessage =
        document.getElementById(
            "toastMessage"
        );


    if (
        !toast ||
        !toastMessage
    ) {

        return;
    }


    toastMessage.textContent =
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
        window.appointmentSearchToastTimer
    );


    window.appointmentSearchToastTimer =
        setTimeout(
            () => {

                toast.classList.remove(
                    "show"
                );

            },
            3500
        );
}