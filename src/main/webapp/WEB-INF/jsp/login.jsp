<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Login | Sunrise Dental Clinic</title>

    <style>

        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            min-height: 100vh;

            font-family:
                    "Segoe UI",
                    Arial,
                    sans-serif;

            display: flex;
            align-items: center;
            justify-content: center;

            background:
                    linear-gradient(
                            135deg,
                            #073b4c,
                            #0b4f6c,
                            #19a7ce
                    );

            padding: 20px;
        }

        .login-container {
            width: 100%;
            max-width: 420px;

            background:
                    rgba(
                            255,
                            255,
                            255,
                            0.97
                    );

            border-radius: 22px;

            padding: 38px;

            box-shadow:
                    0 25px 70px
                    rgba(
                            0,
                            0,
                            0,
                            0.22
                    );
        }

        .logo {
            width: 70px;
            height: 70px;

            margin:
                    0 auto
                    18px;

            border-radius: 18px;

            display: flex;
            align-items: center;
            justify-content: center;

            font-size: 34px;

            color: white;

            background:
                    linear-gradient(
                            135deg,
                            #146c94,
                            #19a7ce
                    );
        }

        h1 {
            text-align: center;

            font-size: 25px;

            margin-bottom: 5px;

            color: #1d2939;
        }

        .subtitle {
            text-align: center;

            color: #667085;

            font-size: 12px;

            margin-bottom: 30px;
        }

        .form-group {
            margin-bottom: 18px;
        }

        label {
            display: block;

            font-size: 11px;
            font-weight: 600;

            color: #344054;

            margin-bottom: 7px;
        }

        input {
            width: 100%;

            padding: 13px 14px;

            border:
                    1px solid
                    #d0d5dd;

            border-radius: 10px;

            outline: none;

            font-size: 13px;
        }

        input:focus {
            border-color: #19a7ce;

            box-shadow:
                    0 0 0 3px
                    rgba(
                            25,
                            167,
                            206,
                            0.12
                    );
        }

        button {
            width: 100%;

            border: none;

            border-radius: 10px;

            padding: 13px;

            cursor: pointer;

            color: white;

            font-size: 13px;
            font-weight: 600;

            background:
                    linear-gradient(
                            135deg,
                            #146c94,
                            #19a7ce
                    );

            margin-top: 5px;
        }

        button:hover {
            box-shadow:
                    0 10px 24px
                    rgba(
                            20,
                            108,
                            148,
                            0.25
                    );
        }

        .message {
            padding: 11px 13px;

            border-radius: 9px;

            margin-bottom: 18px;

            text-align: center;

            font-size: 11px;
        }

        .error {
            background: #fef3f2;
            color: #b42318;
        }

        .success {
            background: #ecfdf3;
            color: #027a48;
        }

        .footer {
            text-align: center;

            margin-top: 25px;

            font-size: 10px;

            color: #98a2b3;
        }

    </style>

</head>

<body>

<div class="login-container">

    <div class="logo">
        🦷
    </div>

    <h1>
        Sunrise Dental Clinic
    </h1>

    <p class="subtitle">
        Staff Management Portal
    </p>


    <%-- LOGIN ERROR MESSAGE --%>

    <%
        if (request.getParameter("error") != null) {
    %>

    <div class="message error">
        Invalid username or password.
    </div>

    <%
        }
    %>


    <%-- LOGOUT SUCCESS MESSAGE --%>

    <%
        if (request.getParameter("logout") != null) {
    %>

    <div class="message success">
        You have been logged out successfully.
    </div>

    <%
        }
    %>


    <%-- SPRING SECURITY LOGIN FORM --%>

    <form action="${pageContext.request.contextPath}/login"
          method="post">

        <div class="form-group">

            <label for="username">
                Username
            </label>

            <input
                    type="text"
                    id="username"
                    name="username"
                    required
                    autocomplete="username"
                    placeholder="Enter username">

        </div>


        <div class="form-group">

            <label for="password">
                Password
            </label>

            <input
                    type="password"
                    id="password"
                    name="password"
                    required
                    autocomplete="current-password"
                    placeholder="Enter password">

        </div>


        <button type="submit">
            Secure Login
        </button>

    </form>


    <div class="footer">
        Authorized Sunrise Dental Clinic staff only
    </div>

</div>

</body>
</html>