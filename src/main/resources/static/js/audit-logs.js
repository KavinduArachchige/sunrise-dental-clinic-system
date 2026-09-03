const AUDIT_API = "/api/audit-logs";

let auditLogs = [];


document.addEventListener(
    "DOMContentLoaded",
    () => {

        bindEvents();
        loadAuditLogs();
    }
);


function bindEvents() {

    document
        .getElementById("searchInput")
        .addEventListener(
            "input",
            applyFilters
        );


    document
        .getElementById("moduleFilter")
        .addEventListener(
            "change",
            applyFilters
        );


    document
        .getElementById("actionFilter")
        .addEventListener(
            "change",
            applyFilters
        );


    document
        .getElementById("refreshButton")
        .addEventListener(
            "click",
            loadAuditLogs
        );


    document
        .getElementById("clearFiltersButton")
        .addEventListener(
            "click",
            clearFilters
        );
}


async function loadAuditLogs() {

    const tableBody =
        document.getElementById(
            "auditTableBody"
        );


    tableBody.innerHTML = `
        <tr>
            <td colspan="5"
                class="table-message">
                Loading audit logs...
            </td>
        </tr>
    `;


    try {

        const response =
            await fetch(
                AUDIT_API,
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


        if (!response.ok) {

            throw new Error(
                `Unable to load audit logs. HTTP ${response.status}`
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
                "Server returned an invalid response."
            );
        }


        const data =
            await response.json();


        if (!Array.isArray(data)) {

            throw new Error(
                "Invalid audit log data received."
            );
        }


        auditLogs =
            data;


        populateActionFilter();

        updateStatistics();

        applyFilters();


    } catch (error) {

        console.error(
            "Audit log loading error:",
            error
        );


        tableBody.innerHTML = `
            <tr>
                <td colspan="5"
                    class="table-message">
                    Unable to load audit logs.
                </td>
            </tr>
        `;


        showToast(
            error.message ||
            "Unable to load audit logs.",
            "error"
        );
    }
}


function updateStatistics() {

    document
        .getElementById(
            "totalLogs"
        )
        .textContent =
        auditLogs.length;


    const users =
        new Set(
            auditLogs
                .map(log =>
                    log.username
                )
                .filter(Boolean)
        );


    document
        .getElementById(
            "uniqueUsers"
        )
        .textContent =
        users.size;


    const modules =
        new Set(
            auditLogs
                .map(log =>
                    log.module
                )
                .filter(Boolean)
        );


    document
        .getElementById(
            "moduleCount"
        )
        .textContent =
        modules.size;


    const today =
        getLocalDateKey(
            new Date()
        );


    const todayCount =
        auditLogs.filter(
            log => {

                if (!log.createdAt) {
                    return false;
                }


                const date =
                    new Date(
                        log.createdAt
                    );


                if (
                    Number.isNaN(
                        date.getTime()
                    )
                ) {
                    return false;
                }


                return (
                    getLocalDateKey(
                        date
                    ) === today
                );
            }
        ).length;


    document
        .getElementById(
            "todayLogs"
        )
        .textContent =
        todayCount;
}


function populateActionFilter() {

    const select =
        document.getElementById(
            "actionFilter"
        );


    const currentValue =
        select.value;


    const actions =
        [
            ...new Set(
                auditLogs
                    .map(log =>
                        log.action
                    )
                    .filter(Boolean)
            )
        ]
            .sort();


    select.innerHTML =
        `<option value="">
            All Actions
        </option>`;


    actions.forEach(
        action => {

            const option =
                document.createElement(
                    "option"
                );


            option.value =
                action;


            option.textContent =
                formatLabel(
                    action
                );


            select.appendChild(
                option
            );
        }
    );


    if (
        actions.includes(
            currentValue
        )
    ) {

        select.value =
            currentValue;
    }
}


function applyFilters() {

    const search =
        document
            .getElementById(
                "searchInput"
            )
            .value
            .trim()
            .toLowerCase();


    const module =
        document
            .getElementById(
                "moduleFilter"
            )
            .value;


    const action =
        document
            .getElementById(
                "actionFilter"
            )
            .value;


    const filtered =
        auditLogs.filter(
            log => {

                const searchableText =
                    [
                        log.username,
                        log.action,
                        log.module,
                        log.details
                    ]
                        .filter(Boolean)
                        .join(" ")
                        .toLowerCase();


                const matchesSearch =
                    !search ||
                    searchableText.includes(
                        search
                    );


                const matchesModule =
                    !module ||
                    log.module === module;


                const matchesAction =
                    !action ||
                    log.action === action;


                return (
                    matchesSearch &&
                    matchesModule &&
                    matchesAction
                );
            }
        );


    renderAuditLogs(
        filtered
    );
}


function renderAuditLogs(logs) {

    const tableBody =
        document.getElementById(
            "auditTableBody"
        );


    const resultText =
        document.getElementById(
            "resultText"
        );


    resultText.textContent =
        `${logs.length} ${
            logs.length === 1
                ? "activity"
                : "activities"
        } found`;


    if (logs.length === 0) {

        tableBody.innerHTML = `
            <tr>
                <td colspan="5"
                    class="table-message">
                    No audit activities found.
                </td>
            </tr>
        `;

        return;
    }


    tableBody.innerHTML =
        logs
            .map(
                log => {

                    const date =
                        formatDateTime(
                            log.createdAt
                        );


                    return `
                        <tr>

                            <td>
                                <span class="date-main">
                                    ${escapeHtml(date.date)}
                                </span>

                                <span class="time-sub">
                                    ${escapeHtml(date.time)}
                                </span>
                            </td>


                            <td>
                                <span class="user-badge">
                                    👤
                                    ${escapeHtml(
                        log.username ||
                        "SYSTEM"
                    )}
                                </span>
                            </td>


                            <td>
                                <span class="
                                    module-badge
                                    ${getModuleClass(
                        log.module
                    )}
                                ">
                                    ${escapeHtml(
                        formatLabel(
                            log.module ||
                            "SYSTEM"
                        )
                    )}
                                </span>
                            </td>


                            <td>
                                <span class="
                                    action-badge
                                    ${getActionClass(
                        log.action
                    )}
                                ">
                                    ${escapeHtml(
                        formatLabel(
                            log.action ||
                            "ACTION"
                        )
                    )}
                                </span>
                            </td>


                            <td class="details-cell">
                                ${escapeHtml(
                        log.details ||
                        "-"
                    )}
                            </td>

                        </tr>
                    `;
                }
            )
            .join("");
}


function clearFilters() {

    document
        .getElementById(
            "searchInput"
        )
        .value = "";


    document
        .getElementById(
            "moduleFilter"
        )
        .value = "";


    document
        .getElementById(
            "actionFilter"
        )
        .value = "";


    applyFilters();
}


function getModuleClass(module) {

    switch (
        String(module || "")
            .toUpperCase()
        ) {

        case "PATIENT":
            return "module-patient";

        case "APPOINTMENT":
            return "module-appointment";

        case "BILLING":
            return "module-billing";

        case "PAYMENT":
            return "module-payment";

        case "STAFF":
            return "module-staff";

        default:
            return "module-default";
    }
}


function getActionClass(action) {

    const value =
        String(action || "")
            .toUpperCase();


    if (
        value.includes("CREATE") ||
        value.includes("BOOK") ||
        value.includes("ACTIVATE")
    ) {

        return "action-create";
    }


    if (
        value.includes("PAYMENT")
    ) {

        return "action-payment";
    }


    if (
        value.includes("DELETE")
    ) {

        return "action-delete";
    }


    if (
        value.includes("DEACTIVATE")
    ) {

        return "action-deactivate";
    }


    if (
        value.includes("CANCEL")
    ) {

        return "action-cancel";
    }


    if (
        value.includes("ROLE")
    ) {

        return "action-role";
    }


    if (
        value.includes("RESET")
    ) {

        return "action-reset";
    }


    if (
        value.includes("UPDATE")
    ) {

        return "action-update";
    }


    return "action-default";
}


function formatLabel(value) {

    if (!value) {
        return "";
    }


    return String(value)
        .replaceAll("_", " ")
        .toLowerCase()
        .replace(
            /\b\w/g,
            letter =>
                letter.toUpperCase()
        );
}


function formatDateTime(value) {

    if (!value) {

        return {
            date: "-",
            time: "-"
        };
    }


    const date =
        new Date(value);


    if (
        Number.isNaN(
            date.getTime()
        )
    ) {

        return {
            date: value,
            time: ""
        };
    }


    return {

        date:
            date.toLocaleDateString(
                "en-GB",
                {
                    day: "2-digit",
                    month: "short",
                    year: "numeric"
                }
            ),

        time:
            date.toLocaleTimeString(
                "en-US",
                {
                    hour: "2-digit",
                    minute: "2-digit"
                }
            )
    };
}


function getLocalDateKey(date) {

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


function escapeHtml(value) {

    return String(value ?? "")
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");
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


    messageElement.textContent =
        message;


    toast.classList.remove(
        "error"
    );


    if (type === "error") {

        toast.classList.add(
            "error"
        );
    }


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