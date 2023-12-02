function filterAction() {
    let $form = $("#filter-form");
    let $inputs = $form.find("input");
    let formData = new FormData();
    let flag = false;


    // Add all form data to formData, if there is a checkbox, add only checked and the name of the input
    $inputs.each(function () {
        if ($(this).attr("type") === "checkbox") {
            if ($(this).is(":checked")) {
                formData.append($(this).attr("name"), $(this).val());
            }
        } else {
            formData.append($(this).attr("name"), $(this).val());
        }
    });

}