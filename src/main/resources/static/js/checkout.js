$(document).ready(function () {
    $(".order-submit").click(function (event) {
        event.preventDefault();
        let form = document.getElementById("order-form"),
            formData = new FormData(form),
            terms = document.getElementById("terms"),
            termsValue = terms.checked,
            payment = document.getElementsByClassName("paymentM"),
            paymentValue = null;
            if (!termsValue) {
                let $dia = $("#message-dialog");
                $dia.find(".modal-body").text("You must agree with terms and conditions to continue");
                $dia.modal("show");
                return;
            }
            // get radio value
            for (let i = 0; i < payment.length; i++) {
                if (payment[i].checked) {
                    paymentValue = payment[i].value;
                }
            }
            formData.append("paymentMethod", paymentValue);
            // form to json
            var jsonData = {};
            formData.forEach(function (value, key) {
                if (value !== "") {
                    jsonData[key] = value;
                }
            });
            $.ajax({
                url: "/order/checkout",
                type: "POST",
                contentType: "application/json",
                data: JSON.stringify(jsonData),
                success: function(response) {
                    if (response.status === 200) {
                        let $dia = $("#message-dialog");
                        $dia.find(".modal-body").text(response.message);
                        $dia.modal("show");
                        setTimeout(function() {
                            window.location.href = "/order/success";
                        }, 2000);
                    }
                    else {
                        let $dia = $("#message-dialog");
                        $dia.find(".modal-body").text(response.message);
                        $dia.modal("show");
                    }
                }
            })

    });
});