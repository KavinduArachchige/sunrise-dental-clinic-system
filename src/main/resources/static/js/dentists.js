const DENTIST_API = "/api/dentists";

let dentists = [];

document.addEventListener("DOMContentLoaded", () => {

    setupSidebar();
    setupModal();
    setupForm();
    setupSearch();

    document
        .getElementById("refreshDentists")
        .addEventListener("click", loadDentists);

    loadDentists();
});


function setupSidebar() {

    const sidebar =
        document.getElementById("sidebar");

    document
        .getElementById("menuButton")
        .addEventListener("click", () => {

            if (window.innerWidth <= 800) {
                sidebar.classList.toggle("mobile-open");
            } else {
                sidebar.classList.toggle("collapsed");
            }
        });
}


function setupModal() {

    const modal =
        document.getElementById("dentistModal");

    document
        .getElementById("openDentistModal")
        .addEventListener("click", openNewDentistModal);

    document
        .getElementById("closeDentistModal")
        .addEventListener("click", closeDentistModal);

    document
        .getElementById("cancelDentistModal")
        .addEventListener("click", closeDentistModal);

    modal.addEventListener("click", event => {

        if (event.target === modal) {
            closeDentistModal();
        }
    });
}


function setupForm() {

    document
        .getElementById("dentistForm")
        .addEventListener("submit", async event => {

            event.preventDefault();

            const dentistId =
                document.getElementById("dentistId").value;

            const payload = {

                dentistName:
                    document
                        .getElementById("dentistName")
                        .value
                        .trim(),

                specialization:
                    document
                        .getElementById("specialization")
                        .value
                        .trim(),

                contactNumber:
                    document
                        .getElementById("dentistContact")
                        .value
                        .trim(),

                email:
                    document
                        .getElementById("dentistEmail")
                        .value
                        .trim()
            };

            try {

                if (dentistId) {

                    await saveDentist(
                        `${DENTIST_API}/${dentistId}`,
                        "PUT",
                        payload
                    );

                    showToast(
                        "Dentist updated successfully."
                    );

                } else {

                    await saveDentist(
                        DENTIST_API,
                        "POST",
                        payload
                    );

                    showToast(
                        "Dentist added successfully."
                    );
                }

                closeDentistModal();

                await loadDentists();

            } catch (error) {

                console.error(error);

                showToast(
                    error.message ||
                    "Unable to save dentist."
                );
            }
        });
}


function setupSearch() {

    document
        .getElementById("dentistSearch")
        .addEventListener("input", event => {

            const term =
                event.target.value
                    .toLowerCase()
                    .trim();

            const filtered =
                dentists.filter(dentist => {

                    return [
                        dentist.dentistName,
                        dentist.specialization,
                        dentist.contactNumber,
                        dentist.email
                    ].some(value =>

                        String(value || "")
                            .toLowerCase()
                            .includes(term)
                    );
                });

            renderDentists(filtered);
        });
}


async function loadDentists() {

    try {

        const response =
            await fetch(DENTIST_API);

        if (!response.ok) {
            throw new Error(
                "Failed to load dentists."
            );
        }

        dentists =
            await response.json();

        document
            .getElementById("dentistCount")
            .textContent =
            dentists.length;

        renderDentists(dentists);

    } catch (error) {

        console.error(error);

        showToast(
            "Unable to load dentists."
        );
    }
}


function renderDentists(list) {

    const body =
        document.getElementById(
            "dentistTableBody"
        );

    body.innerHTML = "";

    if (!list || list.length === 0) {

        body.innerHTML = `
            <tr>
                <td
                    colspan="6"
                    class="empty-state">
                    No dentist records found.
                </td>
            </tr>
        `;

        return;
    }

    list.forEach(dentist => {

        const row =
            document.createElement("tr");

        row.innerHTML = `

            <td>
                #${safeText(dentist.dentistId)}
            </td>

            <td>
                <strong>
                    ${safeText(dentist.dentistName)}
                </strong>
            </td>

            <td>
                ${safeText(dentist.specialization)}
            </td>

            <td>
                ${safeText(dentist.contactNumber)}
            </td>

            <td>
                ${safeText(dentist.email)}
            </td>

            <td>

                <div class="action-buttons">

                    <button
                        class="edit-btn"
                        onclick="editDentist(
                            ${dentist.dentistId}
                        )">
                        Edit
                    </button>

                    <button
                        class="delete-btn"
                        onclick="deleteDentist(
                            ${dentist.dentistId}
                        )">
                        Delete
                    </button>

                </div>

            </td>
        `;

        body.appendChild(row);
    });
}


function openNewDentistModal() {

    document
        .getElementById("dentistModalTitle")
        .textContent =
        "Add Dentist";

    document
        .getElementById("dentistId")
        .value = "";

    document
        .getElementById("dentistName")
        .value = "";

    document
        .getElementById("specialization")
        .value = "";

    document
        .getElementById("dentistContact")
        .value = "";

    document
        .getElementById("dentistEmail")
        .value = "";

    document
        .getElementById("dentistModal")
        .classList.add("show");
}


function editDentist(id) {

    const dentist =
        dentists.find(item =>
            item.dentistId === id
        );

    if (!dentist) {
        return;
    }

    document
        .getElementById("dentistModalTitle")
        .textContent =
        "Edit Dentist";

    document
        .getElementById("dentistId")
        .value =
        dentist.dentistId;

    document
        .getElementById("dentistName")
        .value =
        dentist.dentistName || "";

    document
        .getElementById("specialization")
        .value =
        dentist.specialization || "";

    document
        .getElementById("dentistContact")
        .value =
        dentist.contactNumber || "";

    document
        .getElementById("dentistEmail")
        .value =
        dentist.email || "";

    document
        .getElementById("dentistModal")
        .classList.add("show");
}


function closeDentistModal() {

    document
        .getElementById("dentistModal")
        .classList.remove("show");
}


async function saveDentist(
    url,
    method,
    payload
) {

    const response =
        await fetch(url, {

            method: method,

            headers: {
                "Content-Type": "application/json"
            },

            body:
                JSON.stringify(payload)
        });

    if (!response.ok) {

        throw new Error(
            await getErrorMessage(
                response,
                "Unable to save dentist."
            )
        );
    }

    return await response.json();
}


async function deleteDentist(id) {

    const dentist =
        dentists.find(item =>
            item.dentistId === id
        );

    const confirmed =
        window.confirm(
            `Delete ${dentist?.dentistName || "this dentist"}?`
        );

    if (!confirmed) {
        return;
    }

    try {

        const response =
            await fetch(
                `${DENTIST_API}/${id}`,
                {
                    method: "DELETE"
                }
            );

        if (!response.ok) {

            throw new Error(
                await getErrorMessage(
                    response,
                    "Unable to delete dentist."
                )
            );
        }

        showToast(
            "Dentist deleted successfully."
        );

        await loadDentists();

    } catch (error) {

        console.error(error);

        showToast(error.message);
    }
}


async function getErrorMessage(
    response,
    fallback
) {

    try {

        const data =
            await response.json();

        return data.message || fallback;

    } catch {

        return fallback;
    }
}


function showToast(message) {

    const toast =
        document.getElementById("toast");

    document
        .getElementById("toastMessage")
        .textContent =
        message;

    toast.classList.add("show");

    setTimeout(
        () =>
            toast.classList.remove("show"),
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
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");
}