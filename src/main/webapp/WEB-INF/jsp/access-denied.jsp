<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Access Denied | Sunrise Dental Clinic</title>

    <style>

        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {

            min-height: 100vh;

            display: flex;

            align-items: center;

            justify-content: center;

            padding: 20px;

            font-family:
                    "Segoe UI",
                    Arial,
                    sans-serif;

            background:
                    linear-gradient(
                            135deg,
                            #073b4c,
                            #0b4f6c,
                            #19a7ce
                    );
        }

        .error-card {

            width: 100%;

            max-width: 520px;

            padding: 45px 38px;

            background:
                    rgba(
                            255,
                            255,
                            255,
                            0.98
                    );

            border-radius: 24px;

            text-align: center;

            box-shadow:
                    0 30px 80px
                    rgba(
                            0,
                            0,
                            0,
                            0.22
                    );
        }

        .icon {

            width: 82px;

            height: 82px;

            margin:
                    0 auto
                    20px;

            display: flex;

            align-items: center;

            justify-content: center;

            border-radius: 22px;

            background: #fef3f2;

            font-size: 36px;
        }

        .error-code {

            display: inline-block;

            margin-bottom: 10px;

            color: #b42318;

            font-size: 11px;

            font-weight: 700;

            letter-spacing: 2px;
        }

        h1 {

            margin-bottom: 12px;

            color: #1d2939;

            font-size: 28px;
        }

        p {

            max-width: 400px;

            margin:
                    0 auto
                    25px;

            color: #667085;

            font-size: 13px;

            line-height: 1.7;
        }

        .info-box {

            margin-bottom: 25px;

            padding: 14px 16px;

            border-radius: 10px;

            background: #f9fafb;

            color: #475467;

            font-size: 11px;
        }

        .button-group {

            display: flex;

            justify-content: center;

            gap: 10px;

            flex-wrap: wrap;
        }

        .btn {

            display: inline-flex;

            align-items: center;

            justify-content: center;

            min-width: 150px;

            padding: 12px 18px;

            border: none;

            border-radius: 10px;

            text-decoration: none;

            font-family: inherit;

            font-size: 12px;

            font-weight: 600;

            cursor: pointer;
        }

        .primary {

            color: white;

            background:
                    linear-gradient(
                            135deg,
                            #146c94,
                            #19a7ce
                    );
        }

        .secondary {

            color: #344054;

            background: #f2f4f7;
        }

        .logout-form {
            margin: 0;
        }

        .footer {

            margin-top: 28px;

            color: #98a2b3;

            font-size: 10px;
        }

    </style>

</head>

<body>

<div class="error-card">

    <div class="icon">
        🔒
    </div>

    <span class="error-code">
        ERROR 403
    </span>

    <h1>
        Access Denied
    </h1>

    <p>
        You do not have permission to access this section.
        This page is restricted to authorized staff roles.
    </p>

    <div class="info-box">

        Reports and administrative analytics are available
        only to users with ADMIN privileges.

    </div>

    <div class="button-group">

        <a href="${pageContext.request.contextPath}/"
           class="btn primary">

            Return to Dashboard

        </a>


        <form action="${pageContext.request.contextPath}/logout"
              method="post"
              class="logout-form">

            <button type="submit"
                    class="btn secondary">

                Logout

            </button>

        </form>

    </div>

    <div class="footer">

        Sunrise Dental Clinic Management System

    </div>

</div>

</body>

</html>