const PAYMENT_API = "/api/payments";
const BILL_API = "/api/bills";

let payments = [];
let bills = [];

document.addEventListener("DOMContentLoaded", () => {

    setupSidebar();
    setupModal();
    setupForm();
    setupSearch();

    document
        .getElementById("refreshPayments")
        .addEventListener(
            "click",
            initializePage
        );

    initializePage();
});


async function initializePage() {

    try {

        await Promise.all([
            loadPayments(),
            loadBills()
        ]);

        populateBillSelect();

    } catch (error) {

        console.error(error);

        showToast(
            "Unable to load payment information.",
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
            "paymentModal"
        );

    document
        .getElementById(
            "openPaymentModal"
        )
        .addEventListener(
            "click",
            openPaymentModal
        );

    document
        .getElementById(
            "closePaymentModal"
        )
        .addEventListener(
            "click",
            closePaymentModal
        );

    document
        .getElementById(
            "cancelPaymentModal"
        )
        .addEventListener(
            "click",
            closePaymentModal
        );

    modal.addEventListener(
        "click",
        event => {

            if (event.target === modal) {
                closePaymentModal();
            }
        }
    );
}


function setupForm() {

    document
        .getElementById(
            "paymentForm"
        )
        .addEventListener(
            "submit",
            async event => {

                event.preventDefault();

                const billId =
                    Number(
                        document
                            .getElementById(
                                "billId"
                            )
                            .value
                    );

                const paidAmount =
                    Number(
                        document
                            .getElementById(
                                "paidAmount"
                            )
                            .value
                    );

                const paymentMethod =
                    document
                        .getElementById(
                            "paymentMethod"
                        )
                        .value;


                if (
                    !billId ||
                    !paidAmount ||
                    paidAmount <= 0 ||
                    !paymentMethod
                ) {

                    showToast(
                        "Please enter valid payment details.",
                        "warning"
                    );

                    return;
                }


                try {

                    const response =
                        await fetch(
                            `${PAYMENT_API}/bill/${billId}`,
                            {
                                method: "POST",

                                headers: {
                                    "Content-Type":
                                        "application/json"
                                },

                                body:
                                    JSON.stringify({
                                        paidAmount:
                                        paidAmount,

                                        paymentMethod:
                                        paymentMethod
                                    })
                            }
                        );


                    if (!response.ok) {

                        throw new Error(
                            await getErrorMessage(
                                response,
                                "Unable to record payment."
                            )
                        );
                    }


                    showToast(
                        "Payment recorded successfully.",
                        "success"
                    );


                    closePaymentModal();


                    await Promise.all([
                        loadPayments(),
                        loadBills()
                    ]);


                    populateBillSelect();


                } catch (error) {

                    console.error(error);

                    showToast(
                        error.message ||
                        "Unable to record payment.",
                        "error"
                    );
                }
            }
        );
}


function setupSearch() {

    document
        .getElementById(
            "paymentSearch"
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
                    payments.filter(
                        payment => {

                            return [

                                payment.receiptNumber,

                                payment
                                    .bill
                                    ?.billNumber,

                                payment
                                    .bill
                                    ?.appointment
                                    ?.patient
                                    ?.patientName,

                                payment.paymentMethod,

                                payment.paymentStatus,

                                payment.paidAmount

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


                renderPayments(
                    filtered
                );
            }
        );
}


async function loadPayments() {

    const response =
        await fetch(
            PAYMENT_API
        );


    if (!response.ok) {

        throw new Error(
            "Unable to load payments."
        );
    }


    payments =
        await response.json();


    renderPayments(
        payments
    );


    updateRevenue();
}


async function loadBills() {

    const response =
        await fetch(
            BILL_API
        );


    if (!response.ok) {

        throw new Error(
            "Unable to load bills."
        );
    }


    bills =
        await response.json();
}


function populateBillSelect() {

    const select =
        document.getElementById(
            "billId"
        );


    select.innerHTML = `

        <option value="">
            Select bill
        </option>
    `;


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


    const unpaidBills =
        bills.filter(
            bill =>
                !paidBillIds.has(
                    bill.billId
                )
        );


    unpaidBills.forEach(
        bill => {

            const option =
                document.createElement(
                    "option"
                );


            option.value =
                bill.billId;


            const patientName =
                bill
                    .appointment
                    ?.patient
                    ?.patientName ||
                "Unknown Patient";


            option.textContent =
                `${bill.billNumber} - ${patientName} - ${formatCurrency(
                    bill.totalAmount
                )}`;


            select.appendChild(
                option
            );
        }
    );
}


function renderPayments(list) {

    const body =
        document.getElementById(
            "paymentTableBody"
        );

    body.innerHTML = "";

    if (!list || list.length === 0) {

        body.innerHTML = `
            <tr>
                <td
                    colspan="8"
                    class="empty-state">
                    No payment records found.
                </td>
            </tr>
        `;

        return;
    }

    list.forEach(
        payment => {

            const row =
                document.createElement(
                    "tr"
                );

            const status =
                payment.paymentStatus ||
                "PENDING";

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
                    ?.billNumber
            )}
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
                            ${getPaymentStatusClass(status)}
                        "
                    >
                        ${safeText(status)}
                    </span>
                </td>

                <td>
                    ${formatDateTime(
                payment.paymentDate
            )}
                </td>

                <td>
                    <button
                        type="button"
                        class="print-record-btn"
                        onclick="printReceipt(${payment.paymentId})">
                        🖨 Print Receipt
                    </button>
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
        status.toUpperCase()
        ) {

        case "PAID":
            return "status-completed";

        case "PARTIALLY_PAID":
            return "status-scheduled";

        default:
            return "status-cancelled";
    }
}


function updateRevenue() {

    const total =
        payments.reduce(
            (sum, payment) => {

                return sum +
                    Number(
                        payment.paidAmount ||
                        0
                    );
            },
            0
        );


    document
        .getElementById(
            "totalRevenue"
        )
        .textContent =
        formatCurrency(total);
}


function openPaymentModal() {

    document
        .getElementById(
            "paymentForm"
        )
        .reset();


    populateBillSelect();


    document
        .getElementById(
            "paymentModal"
        )
        .classList.add(
        "show"
    );
}


function closePaymentModal() {

    document
        .getElementById(
            "paymentModal"
        )
        .classList.remove(
        "show"
    );
}


function formatCurrency(amount) {

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


function formatDateTime(value) {

    if (!value) {
        return "-";
    }


    const date =
        new Date(value);


    return date.toLocaleString(
        "en-US",
        {
            day: "2-digit",
            month: "short",
            year: "numeric",
            hour: "2-digit",
            minute: "2-digit"
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
        window.paymentToastTimer
    );


    window.paymentToastTimer =
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
}function printReceipt(paymentId) {

    if (!paymentId) {
        showToast(
            "Unable to identify this payment.",
            "error"
        );

        return;
    }

    window.open(
        `/print/receipt/${paymentId}`,
        "_blank"
    );
}

