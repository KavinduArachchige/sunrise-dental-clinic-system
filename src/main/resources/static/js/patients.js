const PATIENT_API = "/api/patients";

let patients = [];

document.addEventListener("DOMContentLoaded", () => {
    setupSidebar();
    setupModal();
    setupForm();
    setupSearch();
    setupRefresh();

    loadPatients();
});

function setupSidebar() {
    const sidebar = document.getElementById("sidebar");
    const menuButton = document.getElementById("menuButton");

    menuButton.addEventListener("click", () => {
        if (window.innerWidth <= 800) {
            sidebar.classList.toggle("mobile-open");
        } else {
            sidebar.classList.toggle("collapsed");
        }
    });
}

function setupModal() {
    const modal = document.getElementById("patientModal");

    document
        .getElementById("openPatientModal")
        .addEventListener("click", () => {
            openNewPatientModal();
        });

    document
        .getElementById("closePatientModal")
        .addEventListener("click", closePatientModal);

    document
        .getElementById("cancelPatientModal")
        .addEventListener("click", closePatientModal);

    modal.addEventListener("click", event => {
        if (event.target === modal) {
            closePatientModal();
        }
    });
}

function setupForm() {
    document
        .getElementById("patientForm")
        .addEventListener("submit", async event => {
            event.preventDefault();

            const patientId =
                document.getElementById("patientId").value;

            const payload = {
                patientName:
                    document.getElementById("patientName").value.trim(),

                contactNumber:
                    document.getElementById("contactNumber").value.trim(),

                address:
                    document.getElementById("address").value.trim()
            };

            if (!payload.patientName ||
                !payload.contactNumber ||
                !payload.address) {

                showToast("Please complete all patient fields.");
                return;
            }

            try {
                if (patientId) {
                    await updatePatient(patientId, payload);
                    showToast("Patient updated successfully.");
                } else {
                    await createPatient(payload);
                    showToast("Patient registered successfully.");
                }

                closePatientModal();
                await loadPatients();

            } catch (error) {
                console.error(error);
                showToast(error.message || "Unable to save patient.");
            }
        });
}

function setupSearch() {
    document
        .getElementById("patientSearch")
        .addEventListener("input", event => {
            const searchTerm =
                event.target.value.toLowerCase().trim();

            const filtered =
                patients.filter(patient => {

                    return [
                        patient.patientName,
                        patient.contactNumber,
                        patient.address
                    ]
                        .some(value =>
                            String(value || "")
                                .toLowerCase()
                                .includes(searchTerm)
                        );
                });

            renderPatients(filtered);
        });
}

function setupRefresh() {
    document
        .getElementById("refreshPatients")
        .addEventListener("click", loadPatients);
}

async function loadPatients() {
    try {
        const response = await fetch(PATIENT_API);

        if (!response.ok) {
            throw new Error("Failed to load patients.");
        }

        patients = await response.json();

        document.getElementById("patientCount").textContent =
            patients.length;

        renderPatients(patients);

    } catch (error) {
        console.error(error);

        document.getElementById("patientTableBody").innerHTML = `
            <tr>
                <td colspan="5" class="loading-row">
                    Unable to load patients.
                </td>
            </tr>
        `;

        showToast("Unable to load patients.");
    }
}

function renderPatients(patientList) {
    const tableBody =
        document.getElementById("patientTableBody");

    tableBody.innerHTML = "";

    if (!patientList || patientList.length === 0) {
        tableBody.innerHTML = `
            <tr>
                <td colspan="5" class="empty-state">
                    No patient records found.
                </td>
            </tr>
        `;

        return;
    }

    patientList.forEach(patient => {
        const row = document.createElement("tr");

        row.innerHTML = `
            <td>
                #${safeText(patient.patientId)}
            </td>

            <td>
                <strong>
                    ${safeText(patient.patientName)}
                </strong>
            </td>

            <td>
                ${safeText(patient.contactNumber)}
            </td>

            <td>
                ${safeText(patient.address)}
            </td>

            <td>
                <div class="action-buttons">

                    <button
                        class="edit-btn"
                        data-id="${patient.patientId}">
                        Edit
                    </button>

                    <button
                        class="delete-btn"
                        data-id="${patient.patientId}">
                        Delete
                    </button>

                </div>
            </td>
        `;

        tableBody.appendChild(row);
    });

    attachActionListeners();
}

function attachActionListeners() {
    document
        .querySelectorAll(".edit-btn")
        .forEach(button => {

            button.addEventListener("click", () => {
                const patientId =
                    Number(button.dataset.id);

                editPatient(patientId);
            });
        });

    document
        .querySelectorAll(".delete-btn")
        .forEach(button => {

            button.addEventListener("click", () => {
                const patientId =
                    Number(button.dataset.id);

                deletePatient(patientId);
            });
        });
}

function openNewPatientModal() {
    document.getElementById("patientModalTitle").textContent =
        "Register Patient";

    document.getElementById("patientId").value = "";
    document.getElementById("patientName").value = "";
    document.getElementById("contactNumber").value = "";
    document.getElementById("address").value = "";

    document
        .getElementById("patientModal")
        .classList.add("show");
}

function editPatient(patientId) {
    const patient =
        patients.find(item =>
            item.patientId === patientId
        );

    if (!patient) {
        showToast("Patient record not found.");
        return;
    }

    document.getElementById("patientModalTitle").textContent =
        "Edit Patient";

    document.getElementById("patientId").value =
        patient.patientId;

    document.getElementById("patientName").value =
        patient.patientName || "";

    document.getElementById("contactNumber").value =
        patient.contactNumber || "";

    document.getElementById("address").value =
        patient.address || "";

    document
        .getElementById("patientModal")
        .classList.add("show");
}

function closePatientModal() {
    document
        .getElementById("patientModal")
        .classList.remove("show");
}

async function createPatient(payload) {
    const response = await fetch(PATIENT_API, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(payload)
    });

    if (!response.ok) {
        throw new Error(
            await getErrorMessage(
                response,
                "Unable to register patient."
            )
        );
    }

    return await response.json();
}

async function updatePatient(patientId, payload) {
    const response =
        await fetch(`${PATIENT_API}/${patientId}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(payload)
        });

    if (!response.ok) {
        throw new Error(
            await getErrorMessage(
                response,
                "Unable to update patient."
            )
        );
    }

    return await response.json();
}

async function deletePatient(patientId) {
    const patient =
        patients.find(item =>
            item.patientId === patientId
        );

    const patientName =
        patient?.patientName || "this patient";

    const confirmed =
        window.confirm(
            `Are you sure you want to delete ${patientName}?`
        );

    if (!confirmed) {
        return;
    }

    try {
        const response =
            await fetch(`${PATIENT_API}/${patientId}`, {
                method: "DELETE"
            });

        if (!response.ok) {
            throw new Error(
                await getErrorMessage(
                    response,
                    "Unable to delete patient."
                )
            );
        }

        showToast("Patient deleted successfully.");

        await loadPatients();

    } catch (error) {
        console.error(error);
        showToast(error.message);
    }
}

async function getErrorMessage(
    response,
    fallbackMessage
) {
    try {
        const data = await response.json();

        return data.message || fallbackMessage;

    } catch {
        return fallbackMessage;
    }
}

function showToast(message) {
    const toast =
        document.getElementById("toast");

    const toastMessage =
        document.getElementById("toastMessage");

    toastMessage.textContent = message;

    toast.classList.add("show");

    setTimeout(() => {
        toast.classList.remove("show");
    }, 3000);
}

function safeText(value) {
    if (value === null || value === undefined) {
        return "";
    }

    return String(value)
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");
}