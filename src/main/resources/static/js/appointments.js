const API = {
    appointments: "/api/appointments",
    patients: "/api/patients",
    dentists: "/api/dentists",
    treatments: "/api/treatments"
};

let appointments = [];

document.addEventListener("DOMContentLoaded", () => {

    setupSidebar();
    setupModal();
    setupForm();
    setupSearch();

    document
        .getElementById("refreshAppointments")
        .addEventListener(
            "click",
            loadAppointments
        );

    initializePage();
});


async function initializePage() {

    try {

        await Promise.all([
            loadPatients(),
            loadDentists(),
            loadTreatments()
        ]);

        await loadAppointments();

        setMinimumDate();

    } catch (error) {

        console.error(
            "Appointment page initialization error:",
            error
        );

        showToast(
            "Unable to fully load appointment information.",
            "error"
        );
    }
}


function setupSidebar() {

    const sidebar =
        document.getElementById("sidebar");

    document
        .getElementById("menuButton")
        .addEventListener("click", () => {

            if (window.innerWidth <= 800) {

                sidebar.classList.toggle(
                    "mobile-open"
                );

            } else {

                sidebar.classList.toggle(
                    "collapsed"
                );
            }
        });
}


function setupModal() {

    const modal =
        document.getElementById(
            "appointmentModal"
        );

    document
        .getElementById(
            "openAppointmentModal"
        )
        .addEventListener(
            "click",
            openAppointmentModal
        );

    document
        .getElementById(
            "closeAppointmentModal"
        )
        .addEventListener(
            "click",
            closeAppointmentModal
        );

    document
        .getElementById(
            "cancelAppointmentModal"
        )
        .addEventListener(
            "click",
            closeAppointmentModal
        );

    modal.addEventListener(
        "click",
        event => {

            if (event.target === modal) {
                closeAppointmentModal();
            }
        }
    );
}


function setupForm() {

    document
        .getElementById(
            "appointmentForm"
        )
        .addEventListener(
            "submit",
            async event => {

                event.preventDefault();

                const payload = {

                    patientId:
                        Number(
                            document
                                .getElementById(
                                    "patientId"
                                )
                                .value
                        ),

                    dentistId:
                        Number(
                            document
                                .getElementById(
                                    "dentistId"
                                )
                                .value
                        ),

                    treatmentType:
                    document
                        .getElementById(
                            "treatmentType"
                        )
                        .value,

                    appointmentDate:
                    document
                        .getElementById(
                            "appointmentDate"
                        )
                        .value,

                    appointmentTime:
                    document
                        .getElementById(
                            "appointmentTime"
                        )
                        .value
                };


                if (
                    !payload.patientId ||
                    !payload.dentistId ||
                    !payload.treatmentType ||
                    !payload.appointmentDate ||
                    !payload.appointmentTime
                ) {

                    showToast(
                        "Please complete all appointment fields.",
                        "warning"
                    );

                    return;
                }


                try {

                    const response =
                        await fetch(
                            API.appointments,
                            {
                                method: "POST",

                                headers: {
                                    "Content-Type":
                                        "application/json"
                                },

                                body:
                                    JSON.stringify(
                                        payload
                                    )
                            }
                        );


                    if (!response.ok) {

                        throw new Error(
                            await getErrorMessage(
                                response,
                                "Unable to book appointment."
                            )
                        );
                    }


                    showToast(
                        "Appointment booked successfully.",
                        "success"
                    );

                    closeAppointmentModal();

                    await loadAppointments();


                } catch (error) {

                    console.error(error);

                    showToast(
                        error.message ||
                        "Unable to book appointment.",
                        "error"
                    );
                }
            }
        );
}


function setupSearch() {

    document
        .getElementById(
            "appointmentSearch"
        )
        .addEventListener(
            "input",
            event => {

                const term =
                    event.target
                        .value
                        .toLowerCase()
                        .trim();


                const filtered =
                    appointments.filter(
                        appointment => {

                            return [

                                appointment.appointmentNumber,

                                appointment
                                    .patient
                                    ?.patientName,

                                appointment
                                    .dentist
                                    ?.dentistName,

                                appointment.treatmentType,

                                appointment.appointmentDate,

                                appointment.status

                            ].some(
                                value =>

                                    String(
                                        value || ""
                                    )
                                        .toLowerCase()
                                        .includes(term)
                            );
                        }
                    );


                renderAppointments(
                    filtered
                );
            }
        );
}


async function loadAppointments() {

    try {

        const response =
            await fetch(
                API.appointments
            );

        if (!response.ok) {

            throw new Error(
                "Unable to load appointments."
            );
        }


        appointments =
            await response.json();


        document
            .getElementById(
                "appointmentCount"
            )
            .textContent =
            appointments.length;


        renderAppointments(
            appointments
        );


    } catch (error) {

        console.error(error);

        showToast(
            "Unable to load appointments.",
            "error"
        );
    }
}


function renderAppointments(list) {

    const body =
        document.getElementById(
            "appointmentTableBody"
        );

    body.innerHTML = "";


    if (
        !list ||
        list.length === 0
    ) {

        body.innerHTML = `
            <tr>
                <td
                    colspan="8"
                    class="empty-state">

                    No appointment records found.

                </td>
            </tr>
        `;

        return;
    }


    list.forEach(
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
                appointment.appointmentNumber
            )}
                    </strong>
                </td>

                <td>
                    ${safeText(
                appointment
                    .patient
                    ?.patientName
            )}
                </td>

                <td>
                    ${safeText(
                appointment
                    .dentist
                    ?.dentistName
            )}
                </td>

                <td>
                    ${safeText(
                appointment.treatmentType
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

                <td>

                    <div class="action-buttons">

                        ${
                status === "SCHEDULED"
                    ? `
                                    <button
                                        class="edit-btn"
                                        onclick="
                                            updateStatus(
                                                ${appointment.appointmentId},
                                                'COMPLETED'
                                            )
                                        ">
                                        Complete
                                    </button>

                                    <button
                                        class="delete-btn"
                                        onclick="
                                            updateStatus(
                                                ${appointment.appointmentId},
                                                'CANCELLED'
                                            )
                                        ">
                                        Cancel
                                    </button>
                                `
                    : ""
            }

                    </div>

                </td>
            `;


            body.appendChild(row);
        }
    );
}


async function updateStatus(
    appointmentId,
    status
) {

    const message =
        status === "COMPLETED"
            ? "Mark this appointment as completed?"
            : "Cancel this appointment?";


    const confirmed =
        window.confirm(message);


    if (!confirmed) {
        return;
    }


    try {

        const response =
            await fetch(

                `${API.appointments}/${appointmentId}/status?status=${status}`,

                {
                    method: "PUT"
                }
            );


        if (!response.ok) {

            throw new Error(
                await getErrorMessage(
                    response,
                    "Unable to update appointment."
                )
            );
        }


        showToast(
            `Appointment ${status.toLowerCase()} successfully.`,
            "success"
        );


        await loadAppointments();


    } catch (error) {

        console.error(error);

        showToast(
            error.message ||
            "Unable to update appointment.",
            "error"
        );
    }
}


async function loadPatients() {

    try {

        const response =
            await fetch(
                API.patients
            );

        if (!response.ok) {
            throw new Error(
                "Unable to load patients."
            );
        }


        const patients =
            await response.json();


        const select =
            document.getElementById(
                "patientId"
            );


        select.innerHTML = `
            <option value="">
                Select patient
            </option>
        `;


        patients.forEach(
            patient => {

                const option =
                    document.createElement(
                        "option"
                    );

                option.value =
                    patient.patientId;

                option.textContent =
                    `${patient.patientName} - ${patient.contactNumber}`;

                select.appendChild(
                    option
                );
            }
        );

    } catch (error) {

        console.error(error);

        showToast(
            "Unable to load patient list.",
            "error"
        );
    }
}


async function loadDentists() {

    try {

        const response =
            await fetch(
                API.dentists
            );

        if (!response.ok) {
            throw new Error(
                "Unable to load dentists."
            );
        }


        const dentists =
            await response.json();


        const select =
            document.getElementById(
                "dentistId"
            );


        select.innerHTML = `
            <option value="">
                Select dentist
            </option>
        `;


        dentists.forEach(
            dentist => {

                const option =
                    document.createElement(
                        "option"
                    );

                option.value =
                    dentist.dentistId;

                option.textContent =
                    `${dentist.dentistName} - ${dentist.specialization}`;

                select.appendChild(
                    option
                );
            }
        );

    } catch (error) {

        console.error(error);

        showToast(
            "Unable to load dentist list.",
            "error"
        );
    }
}


async function loadTreatments() {

    try {

        const response =
            await fetch(
                API.treatments
            );

        if (!response.ok) {
            throw new Error(
                "Unable to load treatments."
            );
        }


        const treatments =
            await response.json();


        const select =
            document.getElementById(
                "treatmentType"
            );


        select.innerHTML = `
            <option value="">
                Select treatment
            </option>
        `;


        treatments.forEach(
            treatment => {

                const option =
                    document.createElement(
                        "option"
                    );

                option.value =
                    treatment.treatmentName;

                option.textContent =
                    `${treatment.treatmentName} - Rs. ${Number(
                        treatment.price
                    ).toLocaleString("en-LK")}`;

                select.appendChild(
                    option
                );
            }
        );

    } catch (error) {

        console.error(error);

        showToast(
            "Unable to load treatment list.",
            "error"
        );
    }
}


function openAppointmentModal() {

    document
        .getElementById(
            "appointmentForm"
        )
        .reset();


    setMinimumDate();


    document
        .getElementById(
            "appointmentModal"
        )
        .classList.add(
        "show"
    );
}


function closeAppointmentModal() {

    document
        .getElementById(
            "appointmentModal"
        )
        .classList.remove(
        "show"
    );
}


function setMinimumDate() {

    const dateInput =
        document.getElementById(
            "appointmentDate"
        );


    const now =
        new Date();


    const year =
        now.getFullYear();


    const month =
        String(
            now.getMonth() + 1
        ).padStart(
            2,
            "0"
        );


    const day =
        String(
            now.getDate()
        ).padStart(
            2,
            "0"
        );


    dateInput.min =
        `${year}-${month}-${day}`;
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


function formatDate(value) {

    if (!value) {
        return "-";
    }


    const date =
        new Date(
            `${value}T00:00:00`
        );


    return date.toLocaleDateString(
        "en-US",
        {
            day: "2-digit",
            month: "short",
            year: "numeric"
        }
    );
}


function formatTime(value) {

    if (!value) {
        return "-";
    }


    const parts =
        value.split(":");


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


async function getErrorMessage(
    response,
    fallback
) {

    try {

        const data =
            await response.json();


        return (
            data.message ||
            fallback
        );


    } catch {

        return fallback;
    }
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


    if (!toast || !toastMessage) {

        console.warn(
            "Toast elements not found."
        );

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
        window.appointmentToastTimer
    );


    window.appointmentToastTimer =
        setTimeout(
            () => {

                toast.classList.remove(
                    "show"
                );

            },
            4000
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