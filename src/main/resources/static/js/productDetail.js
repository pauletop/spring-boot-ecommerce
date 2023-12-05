$(document).ready(()=>{
    const urlParams = new URLSearchParams(window.location.search);
    const hasReviewParam = urlParams.has('review');

    // If the url has review param, show review tab
    if (hasReviewParam) {
        $("#reviewTab").tab("show");
    }

    $(".add-to-cart .add-to-cart-btn").click((e)=>{
        let $qty = $("#qty"), id = $(e.target).data("id");
        if ($qty.val() < 1) {
            $qty.val(1);
        } else if ($qty.val() > 99) {
            $qty.val(99);
        }
        let qty = $qty.val();
        if (id) {
            $.ajax({
                url: "/cart/add",
                type: "POST",
                contentType: "application/json",
                dataType: "json",
                data: JSON.stringify({
                    pdId: id,
                    qty: qty
                }),
                success: (res)=>{
                    let dia = $('#message-dialog');
                    dia.find("#goCart").hide();
                    if (res.status === 200) {
                        let cartNum = $("#cart-count");
                        cartNum.text(parseInt(cartNum.text()) + 1);
                        cartNum.show();
                        dia.find(".modal-body").text(res.message);
                        dia.find("#goCart").show();
                        dia.modal('show');
                    }
                    else if (res.status === 401) {
                        dia.find(".modal-body").text(res.message);
                        dia.modal('show');
                        // waiting for close modal and redirect to login page
                        $("#loginA").show();
                        dia.on('hidden.bs.modal',  (e) => {
                            $("#loginA").hide();
                        });
                    }
                    else {
                        dia.find(".modal-body").text(res.message);
                        dia.modal('show');
                    }

                }
            });
        }
    });

    $(".review-form button").click((e)=>{
        e.preventDefault();
        let formData = $(".review-form").serializeArray();
        let data = {}, $dia = $("#message-dialog");
        $.each(formData, (i, field)=>{
            data[field.name] = field.value;
        });
        if (data.dName.length < 2) {
            $dia.find(".modal-body").text("Please enter your display name");
            $dia.modal('show');
            return;
        }
        if (!data.rating){
            $dia.find(".modal-body").text("Please rate the product");
            $dia.modal('show');
            return;
        }
        if (data.comment.length < 10) {
            $dia.find(".modal-body").text("Comment must be at least 10 characters");
            $dia.modal('show');
            return;
        }
        $.ajax({
            url: "/product/review",
            type: "POST",
            contentType: "application/json",
            dataType: "json",
            data: JSON.stringify(data),
            success: (res)=>{
                if (res.status === 200) {
                    $dia.find(".modal-body").text(res.message);
                    $dia.modal('show');
                    setTimeout(()=>{
                        location.href = location.pathname + "?review";
                    }, 1500);
                }
                else {
                    $dia.find(".modal-body").text(res.message);
                    $dia.modal('show');
                }
            },
            error: (res)=>{
                let $dia = $("#message-dialog");
                if (res.status === 401) {
                    // waiting for close modal and redirect to login page
                    $("#loginA").show();
                    $dia.on('hidden.bs.modal',  (e) => {
                        $("#loginA").hide();
                    });
                }
                $dia.find(".modal-body").text(res.responseJSON.message);
                $dia.modal('show');
            }
        });
    });
});

