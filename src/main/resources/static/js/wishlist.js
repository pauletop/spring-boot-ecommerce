$(document).ready(function() {
   $(".new.close, .rm-from-wishlist").on("click", function(e) {
       let product = $(e.target).closest(".view-product"),
           id = product.attr("data-id");
       let $dia = $('#message-dialog');
       $.ajax({
           url: "/account/rmWishList",
           type: "POST",
           contentType: "application/json",
           dataType: "json",
           data: JSON.stringify({
               pdId: id
           }),
           success: (res)=>{
               $dia.find(".modal-body").text(res.message);
               $dia.modal('show');
               $(this).closest(".view-product").parent().remove();
           },
           error: (res)=>{
               $dia.find(".modal-body").text(res.responseJSON.message);
               $dia.modal('show');
           }
       });
   });

   $("#sortBy").on("change", function(e) {
       let value = $(this).val(),
           url = location.pathname + "?sort=" + value;
       if (value !== "") {
           window.location.href = url;
       }
   });
});