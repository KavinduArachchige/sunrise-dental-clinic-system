const STAFF_API =
    "/api/staff";

let staffUsers = [];


document.addEventListener(
    "DOMContentLoaded",
    () => {

        setupSidebar();
        setupModal();
        setupForm();
        setupSearch();

        document
            .getElementById(
                "refreshStaff"
            )
            .addEventListener(
                "click",
                loadStaff
            );

        loadStaff();
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


function setupModal() {

    const modal =
        document.getElementById(
            "staffModal"
        );

    document
        .getElementById(
            "openStaffModal"
        )
        .addEventListener(
            "click",
            openStaffModal
        );

    document
        .getElementById(
            "closeStaffModal"
        )
        .addEventListener(
            "click",
            closeStaffModal
        );

    document
        .getElementById(
            "cancelStaffModal"
        )
        .addEventListener(
            "click",
            closeStaffModal
        );

    modal.addEventListener(
        "click",
        event => {

            if (
                event.target === modal
            ) {

                closeStaffModal();
            }
        }
    );
}


function setupForm() {

    document
        .getElementById(
            "staffForm"
        )
        .addEventListener(
            "submit",
            async event => {

                event.preventDefault();


                const payload = {

                    fullName:
                        document
                            .getElementById(
                                "fullName"
                            )
                            .value
                            .trim(),

                    username:
                        document
                            .getElementById(
                                "username"
                            )
                            .value
                            .trim(),

                    password:
                    document
                        .getElementById(
                            "password"
                        )
                        .value,

                    role:
                    document
                        .getElementById(
                            "role"
                        )
                        .value
                };


                try {

                    const response =
                        await fetch(
                            STAFF_API,
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
                                "Unable to create staff account."
                            )
                        );
                    }


                    showToast(
                        "Staff account created successfully.",
                        "success"
                    );


                    closeStaffModal();

                    await loadStaff();


                } catch (error) {

                    console.error(
                        error
                    );

                    showToast(
                        error.message,
                        "error"
                    );
                }
            }
        );
}


function setupSearch() {

    document
        .getElementById(
            "staffSearch"
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
                    staffUsers.filter(
                        user => {

                            return [

                                user.fullName,

                                user.username,

                                user.role

                            ].some(
                                value =>

                                    String(
                                        value ||
                                        ""
                                    )
                                        .toLowerCase()
                                        .includes(
                                            term
                                        )
                            );
                        }
                    );


                renderStaff(
                    filtered
                );
            }
        );
}


async function loadStaff() {

    try {

        const response =
            await fetch(
                STAFF_API,
                {
                    method: "GET",
                    credentials: "same-origin",
                    headers: {
                        "Accept": "application/json"
                    }
                }
            );


        const contentType =
            response.headers.get(
                "content-type"
            ) || "";


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
                `Unable to load staff accounts. HTTP ${response.status}`
            );
        }


        if (
            !contentType.includes(
                "application/json"
            )
        ) {

            const responseText =
                await response.text();


            console.error(
                "Expected JSON but received:",
                responseText.substring(
                    0,
                    300
                )
            );


            if (
                responseText.includes(
                    "<!DOCTYPE html"
                ) ||
                responseText.includes(
                    "<html"
                )
            ) {

                throw new Error(
                    "The server returned an HTML page instead of staff data. Please login again and retry."
                );
            }


            throw new Error(
                "Invalid response received from the server."
            );
        }


        staffUsers =
            await response.json();


        if (
            !Array.isArray(
                staffUsers
            )
        ) {

            throw new Error(
                "Invalid staff data received from server."
            );
        }


        document
            .getElementById(
                "staffCount"
            )
            .textContent =
            staffUsers.length;


        renderStaff(
            staffUsers
        );


    } catch (error) {

        console.error(
            "Staff loading error:",
            error
        );


        showToast(
            error.message ||
            "Unable to load staff accounts.",
            "error"
        );
    }
}


function renderStaff(list) {

    const body =
        document.getElementById(
            "staffTableBody"
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
                    colspan="6"
                    class="empty-state">

                    No staff accounts found.

                </td>

            </tr>
        `;

        return;
    }


    list.forEach(
        user => {

            const row =
                document.createElement(
                    "tr"
                );


            row.innerHTML = `

                <td>
                    #${safeText(
                user.userId
            )}
                </td>

                <td>

                    <strong>
                        ${safeText(
                user.fullName
            )}
                    </strong>

                </td>

                <td>
                    ${safeText(
                user.username
            )}
                </td>

                <td>

                    <span
                        class="
                            staff-role
                            ${
                user.role ===
                "ADMIN"
                    ? "role-admin"
                    : "role-receptionist"
            }
                        "
                    >

                        ${safeText(
                user.role
            )}

                    </span>

                </td>

                <td>

                    <span
                        class="
                            staff-status
                            ${
                user.active
                    ? "status-active"
                    : "status-inactive"
            }
                        "
                    >

                        ${
                user.active
                    ? "Active"
                    : "Inactive"
            }

                    </span>

                </td>

                <td>

                    <div class="staff-action-group">

                        <button
                            class="role-btn"
                            onclick="
                                changeRole(
                                    ${user.userId},
                                    '${user.role}'
                                )
                            ">

                            Change Role

                        </button>


                        <button
                            class="status-btn"
                            onclick="
                                changeStatus(
                                    ${user.userId},
                                    ${user.active}
                                )
                            ">

                            ${
                user.active
                    ? "Deactivate"
                    : "Activate"
            }

                        </button>


                        <button
                            class="password-btn"
                            onclick="
                                resetPassword(
                                    ${user.userId}
                                )
                            ">

                            Reset Password

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


function openStaffModal() {

    document
        .getElementById(
            "staffForm"
        )
        .reset();


    document
        .getElementById(
            "staffModal"
        )
        .classList.add(
        "show"
    );
}


function closeStaffModal() {

    document
        .getElementById(
            "staffModal"
        )
        .classList.remove(
        "show"
    );
}


async function changeRole(
    id,
    currentRole
) {

    const newRole =
        currentRole ===
        "ADMIN"
            ? "RECEPTIONIST"
            : "ADMIN";


    if (
        !confirm(
            `Change role to ${newRole}?`
        )
    ) {

        return;
    }


    try {

        const response =
            await fetch(
                `${STAFF_API}/${id}/role`,
                {

                    method: "PUT",

                    headers: {
                        "Content-Type":
                            "application/json"
                    },

                    body:
                        JSON.stringify({
                            role:
                            newRole
                        })
                }
            );


        if (!response.ok) {

            throw new Error(
                await getErrorMessage(
                    response,
                    "Unable to update role."
                )
            );
        }


        showToast(
            "Staff role updated successfully.",
            "success"
        );


        await loadStaff();


    } catch (error) {

        showToast(
            error.message,
            "error"
        );
    }
}


async function changeStatus(
    id,
    active
) {

    const newStatus =
        !active;


    const message =
        newStatus
            ? "Activate this staff account?"
            : "Deactivate this staff account?";


    if (
        !confirm(
            message
        )
    ) {

        return;
    }


    try {

        const response =
            await fetch(
                `${STAFF_API}/${id}/status`,
                {

                    method: "PUT",

                    headers: {
                        "Content-Type":
                            "application/json"
                    },

                    body:
                        JSON.stringify({
                            active:
                            newStatus
                        })
                }
            );


        if (!response.ok) {

            throw new Error(
                await getErrorMessage(
                    response,
                    "Unable to update account status."
                )
            );
        }


        showToast(
            "Account status updated successfully.",
            "success"
        );


        await loadStaff();


    } catch (error) {

        showToast(
            error.message,
            "error"
        );
    }
}


async function resetPassword(
    id
) {

    const password =
        prompt(
            "Enter new password (minimum 6 characters):"
        );


    if (
        password === null
    ) {

        return;
    }


    if (
        password.length < 6
    ) {

        showToast(
            "Password must contain at least 6 characters.",
            "warning"
        );

        return;
    }


    try {

        const response =
            await fetch(
                `${STAFF_API}/${id}/password`,
                {

                    method:
                        "PUT",

                    headers: {
                        "Content-Type":
                            "application/json"
                    },

                    body:
                        JSON.stringify({
                            password:
                            password
                        })
                }
            );


        if (!response.ok) {

            throw new Error(
                await getErrorMessage(
                    response,
                    "Unable to reset password."
                )
            );
        }


        showToast(
            "Password reset successfully.",
            "success"
        );


    } catch (error) {

        showToast(
            error.message,
            "error"
        );
    }
}


async function getErrorMessage(
    response,
    fallback
) {

    try {

        const contentType =
            response.headers.get(
                "content-type"
            ) || "";


        if (
            contentType.includes(
                "application/json"
            )
        ) {

            const data =
                await response.json();


            return (
                data.message ||
                data.error ||
                fallback
            );
        }


        const text =
            await response.text();


        console.error(
            "Non-JSON server response:",
            text.substring(
                0,
                300
            )
        );


        return fallback;


    } catch (error) {

        console.error(
            "Unable to read server error:",
            error
        );


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


    const messageElement =
        document.getElementById(
            "toastMessage"
        );


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
        window.staffToastTimer
    );


    window.staffToastTimer =
        setTimeout(
            () => {

                toast.classList.remove(
                    "show"
                );

            },
            3500
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