$("#logout").click(function(event) {
    if (!confirm("Are you sure you want to logout?")) {
        return false;
    }
    $.ajax({
        url: '/account/logout',
        type: 'POST',
        success: function(data) {
            location.reload();
        }
    });
});

function changePassword(e) {
    let $form = $("#changePas form"),
        formData = new FormData($form[0]),
        oldPassword = formData.get("oldPassword"),
        newPassword = formData.get("newPassword"),
        confirmPassword = formData.get("confPassword"),
        $dia = $("#message-dialog"),
        // Minimum eight characters, at least one uppercase letter, one lowercase letter and one number, and/or special character
        passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d!@#$%^&*()_+\-=\[\]\\|,.<>\/?~]{8,}$/;
    let err = null;
    if (oldPassword === "" || newPassword === "" || confirmPassword === "") {
        err = "All fields are required";
    }
    if (err === null && !passwordRegex.test(newPassword)) {
        err = "Password must be at least 8 characters, at least one uppercase letter, one lowercase letter and one number";
    }
    if ( err === null && newPassword !== confirmPassword) {
        err = "New password and confirm password must be the same";
    }
    if (err != null) {
        $dia.find(".modal-body").text(err);
        $dia.modal("show");
        return;
    }
    $.ajax({
        url: '/account/change-password',
        type: 'POST',
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify({
            oldPassword: oldPassword,
            newPassword: newPassword
        }),
        success: function(response) {
            $dia.find(".modal-body").text(response.responseText);
            $dia.modal("show");
        },
        error: function(response) {
            $dia.find(".modal-body").text(response.responseText);
            $dia.modal("show");
            if (response.status === 200) {
                setTimeout(()=>{
                    location.reload();
                }, 1000);
            }
        }
    });
}