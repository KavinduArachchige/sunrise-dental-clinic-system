const BILL_API = "/api/bills";
const APPOINTMENT_API = "/api/appointments";

let bills = [];
let appointments = [];

document.addEventListener(
    "DOMContentLoaded",
    () => {

        setupSidebar();
        setupModal();
        setupForm();
        setupSearch();

        document
            .getElementById("refreshBills")
            .addEventListener(
                "click",
                initializePage
            );

        initializePage();
    }
);


async function initializePage() {

    try {

        await Promise.all([
            loadBills(),
            loadAppointments()
        ]);

    } catch (error) {

        console.error(error);

        showToast(
            "Unable to load billing information.",
            "error"
        );
    }
}


function setupSidebar() {

    const sidebar =
        document.getElementById("sidebar");

    document
        .getElementById("menuButton")
        .addEventListener(
            "click",
            () => {

                if (
                    window.innerWidth <= 800
                ) {

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


function setupModal() {

    const modal =
        document.getElementById(
            "billModal"
        );

    document
        .getElementById(
            "openBillModal"
        )
        .addEventListener(
            "click",
            openBillModal
        );

    document
        .getElementById(
            "closeBillModal"
        )
        .addEventListener(
            "click",
            closeBillModal
        );

    document
        .getElementById(
            "cancelBillModal"
        )
        .addEventListener(
            "click",
            closeBillModal
        );

    modal.addEventListener(
        "click",
        event => {

            if (
                event.target === modal
            ) {
                closeBillModal();
            }
        }
    );
}


function setupForm() {

    document
        .getElementById(
            "billForm"
        )
        .addEventListener(
            "submit",
            async event => {

                event.preventDefault();

                const appointmentId =
                    Number(
                        document
                            .getElementById(
                                "appointmentId"
                            )
                            .value
                    );

                if (
                    !appointmentId
                ) {

                    showToast(
                        "Please select an appointment.",
                        "warning"
                    );

                    return;
                }


                try {

                    const response =
                        await fetch(
                            `${BILL_API}/generate/${appointmentId}`,
                            {
                                method:
                                    "POST"
                            }
                        );


                    if (
                        !response.ok
                    ) {

                        throw new Error(
                            await getErrorMessage(
                                response,
                                "Unable to generate bill."
                            )
                        );
                    }


                    showToast(
                        "Bill generated successfully.",
                        "success"
                    );


                    closeBillModal();


                    await Promise.all([
                        loadBills(),
                        loadAppointments()
                    ]);


                } catch (error) {

                    console.error(
                        error
                    );


                    showToast(
                        error.message ||
                        "Unable to generate bill.",
                        "error"
                    );
                }
            }
        );
}


function setupSearch() {

    document
        .getElementById(
            "billSearch"
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
                    bills.filter(
                        bill => {

                            return [

                                bill.billNumber,

                                bill
                                    .appointment
                                    ?.appointmentNumber,

                                bill
                                    .appointment
                                    ?.patient
                                    ?.patientName,

                                bill
                                    .appointment
                                    ?.treatmentType,

                                bill.totalAmount

                            ].some(
                                value =>

                                    String(
                                        value || ""
                                    )
                                        .toLowerCase()
                                        .includes(
                                            term
                                        )
                            );
                        }
                    );


                renderBills(
                    filtered
                );
            }
        );
}


async function loadBills() {

    const response =
        await fetch(
            BILL_API
        );


    if (
        !response.ok
    ) {

        throw new Error(
            "Unable to load bills."
        );
    }


    bills =
        await response.json();


    document
        .getElementById(
            "billCount"
        )
        .textContent =
        bills.length;


    renderBills(
        bills
    );
}


async function loadAppointments() {

    const response =
        await fetch(
            APPOINTMENT_API
        );


    if (
        !response.ok
    ) {

        throw new Error(
            "Unable to load appointments."
        );
    }


    appointments =
        await response.json();


    populateAppointmentSelect();
}


function populateAppointmentSelect() {

    const select =
        document.getElementById(
            "appointmentId"
        );


    select.innerHTML = `

        <option value="">
            Select appointment
        </option>
    `;


    const billedAppointmentIds =
        new Set(
            bills
                .filter(
                    bill =>
                        bill.appointment &&
                        bill.appointment.appointmentId
                )
                .map(
                    bill =>
                        bill.appointment.appointmentId
                )
        );


    const availableAppointments =
        appointments.filter(
            appointment =>

                !billedAppointmentIds.has(
                    appointment.appointmentId
                )
        );


    availableAppointments.forEach(
        appointment => {

            const option =
                document.createElement(
                    "option"
                );


            option.value =
                appointment.appointmentId;


            const patientName =
                appointment
                    .patient
                    ?.patientName ||
                "Unknown Patient";


            option.textContent =

                `${appointment.appointmentNumber} - ${patientName} - ${appointment.treatmentType}`;


            select.appendChild(
                option
            );
        }
    );
}


function renderBills(list) {

    const body =
        document.getElementById(
            "billTableBody"
        );

    body.innerHTML = "";

    if (!list || list.length === 0) {

        body.innerHTML = `
            <tr>
                <td
                    colspan="9"
                    class="empty-state">
                    No billing records found.
                </td>
            </tr>
        `;

        return;
    }

    list.forEach(
        bill => {

            const row =
                document.createElement(
                    "tr"
                );

            row.innerHTML = `

                <td>
                    <strong>
                        ${safeText(
                bill.billNumber
            )}
                    </strong>
                </td>

                <td>
                    ${safeText(
                bill
                    .appointment
                    ?.appointmentNumber
            )}
                </td>

                <td>
                    ${safeText(
                bill
                    .appointment
                    ?.patient
                    ?.patientName
            )}
                </td>

                <td>
                    ${safeText(
                bill
                    .appointment
                    ?.treatmentType
            )}
                </td>

                <td>
                    ${formatCurrency(
                bill.treatmentAmount
            )}
                </td>

                <td>
                    ${formatCurrency(
                bill.consultationFee
            )}
                </td>

                <td>
                    <strong>
                        ${formatCurrency(
                bill.totalAmount
            )}
                    </strong>
                </td>

                <td>
                    ${formatDateTime(
                bill.billDate
            )}
                </td>

                <td>
                    <button
                        type="button"
                        class="print-record-btn"
                        onclick="printBill(${bill.billId})">
                        🖨 Print Bill
                    </button>
                </td>
            `;

            body.appendChild(
                row
            );
        }
    );
}


function openBillModal() {

    document
        .getElementById(
            "billForm"
        )
        .reset();


    populateAppointmentSelect();


    document
        .getElementById(
            "billModal"
        )
        .classList
        .add(
            "show"
        );
}


function closeBillModal() {

    document
        .getElementById(
            "billModal"
        )
        .classList
        .remove(
            "show"
        );
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
                    minimumFractionDigits: 2,
                    maximumFractionDigits: 2
                }
            )
    );
}


function formatDateTime(
    value
) {

    if (
        !value
    ) {

        return "-";
    }


    const date =
        new Date(
            value
        );


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
        window.billToastTimer
    );


    window.billToastTimer =
        setTimeout(
            () => {

                toast
                    .classList
                    .remove(
                        "show"
                    );

            },
            4000
        );
}


function safeText(
    value
) {

    if (
        value === null ||
        value === undefined
    ) {

        return "";
    }


    return String(
        value
    )

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

function printBill(billId) {

    if (!billId) {
        showToast(
            "Unable to identify this bill.",
            "error"
        );

        return;
    }

    window.open(
        `/print/bill/${billId}`,
        "_blank"
    );
}