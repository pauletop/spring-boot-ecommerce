$(document).ready(function() {
    var debounceTimeout; // store timeout id
    const changeFormate = (data, isString) => {
        let result = data;
        if (isString) {
            result = result.replace(/[$,]/g, '');
            return result;
        }
        return parseFloat(result).toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,');
    }
    $(".input-number input").on("change", function(e) {
        let $this = $(this);
        debounceTimeout = setTimeout(function() {
            let quantity = $this.val();
            if (quantity < 1) {
                quantity = 1;
                $this.val(quantity);
            } else if (quantity > 99) {
                quantity = 99;
                $this.val(quantity);
            }
            let $row = $($this.context.closest("tr")),
                productId = $row.find("td:first").data("id"),
                total = $row.find("td:nth-child(4) strong").text(), // get total price of a product
                sumary = $(".cart-summary .tol-price:first").text(); // get total price of cart
            total = changeFormate(total, true)
            sumary = changeFormate(sumary, true);
            // ajax call to update quantity
            $.ajax({
                url: "/cart/change",
                type: "POST",
                contentType: "application/json",
                dataType: "json",
                data: JSON.stringify({
                    pdId: productId,
                    qty: quantity
                }),
                success: function(response) {
                    if (response.status === 200) {
                        let cartNum = $("#cart-count"),
                            delta = parseFloat(response.data);
                        // cartNum.text(parseInt(cartNum.text()) + quantity - oQty);
                        total = changeFormate(parseFloat(total) + delta, false)
                        sumary = changeFormate(parseFloat(sumary) + delta, false)
                        $row.find("td:nth-child(4) strong").text(total);
                        $(".cart-summary .tol-price").text("$"+sumary);
                        // cartNum.show();
                        let dia = $("#message-dialog");
                        dia.find(".modal-body").text(response.message);
                        dia.modal("show");
                    }
                }
            });

            debounceTimeout = null; // reset timeout id
        }, 2000);
    });

    $("table tbody .delete-product").on("click", function(e) {
        let $row = $(e.target.closest("tr")),
            productId = $row.find("td:first").data("id"),
            total = $row.find("td:nth-child(4) strong").text(), // get total price of a product
            sumary = $(".cart-summary .tol-price:first").text(); // get total price of cart
        total = changeFormate(total, true)
        sumary = changeFormate(sumary, true);
        // ajax call to delete product
        $.ajax({
            url: "/cart/remove",
            type: "POST",
            contentType: "application/json",
            dataType: "json",
            data: JSON.stringify({
                pdId: productId
            }),
            success: function(response) {
                if (response.status === 200) {
                    let cartNum = $(".cart-count");
                    cartNum.text(parseInt(cartNum.text()) - 1);
                    sumary = changeFormate(parseFloat(sumary) - parseFloat(total), false)
                    $row.remove();
                    $(".cart-summary .tol-price").text("$"+sumary);
                    cartNum.show();
                    let dia = $("#message-dialog");
                    dia.find(".modal-body").text(response.message);
                    dia.modal("show");
                }
                else {
                    let dia = $("#message-dialog");
                    dia.find(".modal-body").text(response.message);
                    dia.modal("show");
                }
            }
        });
    });
    // clear beforeunload event


    window.addEventListener("beforeunload", (e)=>{
        if (debounceTimeout) {
            $("#loading").hide();
            return e.returnValue = 'Are you sure you want to close?';
        }
    });
});