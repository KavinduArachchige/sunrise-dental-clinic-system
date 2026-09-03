const TREATMENT_API = "/api/treatments";

let treatments = [];

document.addEventListener(
    "DOMContentLoaded",
    () => {

        setupSidebar();
        setupModal();
        setupForm();
        setupSearch();

        document
            .getElementById(
                "refreshTreatments"
            )
            .addEventListener(
                "click",
                loadTreatments
            );

        loadTreatments();
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
            "treatmentModal"
        );


    document
        .getElementById(
            "openTreatmentModal"
        )
        .addEventListener(
            "click",
            openNewTreatmentModal
        );


    document
        .getElementById(
            "closeTreatmentModal"
        )
        .addEventListener(
            "click",
            closeTreatmentModal
        );


    document
        .getElementById(
            "cancelTreatmentModal"
        )
        .addEventListener(
            "click",
            closeTreatmentModal
        );


    modal.addEventListener(
        "click",
        event => {

            if (
                event.target === modal
            ) {

                closeTreatmentModal();
            }
        }
    );
}


function setupForm() {

    document
        .getElementById(
            "treatmentForm"
        )
        .addEventListener(
            "submit",
            async event => {

                event.preventDefault();


                const treatmentId =
                    document
                        .getElementById(
                            "treatmentId"
                        )
                        .value;


                const payload = {

                    treatmentName:
                        document
                            .getElementById(
                                "treatmentName"
                            )
                            .value
                            .trim(),

                    description:
                        document
                            .getElementById(
                                "treatmentDescription"
                            )
                            .value
                            .trim(),

                    price:
                        Number(
                            document
                                .getElementById(
                                    "treatmentPrice"
                                )
                                .value
                        )
                };


                if (
                    !payload.treatmentName ||
                    !payload.description ||
                    !payload.price ||
                    payload.price <= 0
                ) {

                    showToast(
                        "Please enter valid treatment details.",
                        "warning"
                    );

                    return;
                }


                try {

                    if (
                        treatmentId
                    ) {

                        await saveTreatment(
                            `${TREATMENT_API}/${treatmentId}`,
                            "PUT",
                            payload
                        );


                        showToast(
                            "Treatment updated successfully.",
                            "success"
                        );

                    } else {

                        await saveTreatment(
                            TREATMENT_API,
                            "POST",
                            payload
                        );


                        showToast(
                            "Treatment added successfully.",
                            "success"
                        );
                    }


                    closeTreatmentModal();

                    await loadTreatments();


                } catch (error) {

                    console.error(
                        error
                    );


                    showToast(
                        error.message ||
                        "Unable to save treatment.",
                        "error"
                    );
                }
            }
        );
}


function setupSearch() {

    document
        .getElementById(
            "treatmentSearch"
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
                    treatments.filter(
                        treatment => {

                            return [

                                treatment.treatmentName,

                                treatment.description,

                                treatment.price

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


                renderTreatments(
                    filtered
                );
            }
        );
}


async function loadTreatments() {

    try {

        const response =
            await fetch(
                TREATMENT_API
            );


        if (
            !response.ok
        ) {

            throw new Error(
                "Unable to load treatments."
            );
        }


        treatments =
            await response.json();


        document
            .getElementById(
                "treatmentCount"
            )
            .textContent =
            treatments.length;


        renderTreatments(
            treatments
        );


    } catch (error) {

        console.error(
            error
        );


        showToast(
            "Unable to load treatments.",
            "error"
        );
    }
}


function renderTreatments(
    list
) {

    const body =
        document.getElementById(
            "treatmentTableBody"
        );


    body.innerHTML =
        "";


    if (
        !list ||
        list.length === 0
    ) {

        body.innerHTML = `

            <tr>

                <td
                    colspan="5"
                    class="empty-state">

                    No treatment records found.

                </td>

            </tr>
        `;


        return;
    }


    list.forEach(
        treatment => {

            const row =
                document.createElement(
                    "tr"
                );


            row.innerHTML = `

                <td>
                    #${safeText(
                treatment.treatmentId
            )}
                </td>

                <td>

                    <strong>
                        ${safeText(
                treatment.treatmentName
            )}
                    </strong>

                </td>

                <td>
                    ${safeText(
                treatment.description
            )}
                </td>

                <td>

                    <strong>
                        ${formatCurrency(
                treatment.price
            )}
                    </strong>

                </td>

                <td>

                    <div
                        class="action-buttons">

                        <button
                            class="edit-btn"
                            onclick="
                                editTreatment(
                                    ${treatment.treatmentId}
                                )
                            ">

                            Edit

                        </button>


                        <button
                            class="delete-btn"
                            onclick="
                                deleteTreatment(
                                    ${treatment.treatmentId}
                                )
                            ">

                            Delete

                        </button>

                    </div>

                </td>
            `;


            body.appendChild(
                row
            );
        }
    );
}


function openNewTreatmentModal() {

    document
        .getElementById(
            "treatmentModalTitle"
        )
        .textContent =
        "Add Treatment";


    document
        .getElementById(
            "treatmentId"
        )
        .value =
        "";


    document
        .getElementById(
            "treatmentName"
        )
        .value =
        "";


    document
        .getElementById(
            "treatmentDescription"
        )
        .value =
        "";


    document
        .getElementById(
            "treatmentPrice"
        )
        .value =
        "";


    document
        .getElementById(
            "treatmentModal"
        )
        .classList
        .add(
            "show"
        );
}


function editTreatment(
    id
) {

    const treatment =
        treatments.find(
            item =>
                item.treatmentId === id
        );


    if (
        !treatment
    ) {

        showToast(
            "Treatment record not found.",
            "error"
        );

        return;
    }


    document
        .getElementById(
            "treatmentModalTitle"
        )
        .textContent =
        "Edit Treatment";


    document
        .getElementById(
            "treatmentId"
        )
        .value =
        treatment.treatmentId;


    document
        .getElementById(
            "treatmentName"
        )
        .value =
        treatment.treatmentName || "";


    document
        .getElementById(
            "treatmentDescription"
        )
        .value =
        treatment.description || "";


    document
        .getElementById(
            "treatmentPrice"
        )
        .value =
        treatment.price || "";


    document
        .getElementById(
            "treatmentModal"
        )
        .classList
        .add(
            "show"
        );
}


function closeTreatmentModal() {

    document
        .getElementById(
            "treatmentModal"
        )
        .classList
        .remove(
            "show"
        );
}


async function saveTreatment(
    url,
    method,
    payload
) {

    const response =
        await fetch(
            url,
            {

                method:
                method,

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


    if (
        !response.ok
    ) {

        throw new Error(
            await getErrorMessage(
                response,
                "Unable to save treatment."
            )
        );
    }


    return await response.json();
}


async function deleteTreatment(
    id
) {

    const treatment =
        treatments.find(
            item =>
                item.treatmentId === id
        );


    const confirmed =
        window.confirm(
            `Delete ${
                treatment?.treatmentName ||
                "this treatment"
            }?`
        );


    if (
        !confirmed
    ) {

        return;
    }


    try {

        const response =
            await fetch(
                `${TREATMENT_API}/${id}`,
                {
                    method:
                        "DELETE"
                }
            );


        if (
            !response.ok
        ) {

            throw new Error(
                await getErrorMessage(
                    response,
                    "Unable to delete treatment."
                )
            );
        }


        showToast(
            "Treatment deleted successfully.",
            "success"
        );


        await loadTreatments();


    } catch (error) {

        console.error(
            error
        );


        showToast(
            error.message ||
            "Unable to delete treatment.",
            "error"
        );
    }
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
        window.treatmentToastTimer
    );


    window.treatmentToastTimer =
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