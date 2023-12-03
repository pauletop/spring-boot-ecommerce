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

    formData.append("isAdv", true);
    formData.append("keyword", $("input[name='keyword']").val());
    let sort = $("select#sortBy").val();
    if (sort !== "")
        formData.append("sort", sort);

    // submit the form with get method
    let url = "/store/search?" + (new URLSearchParams(formData)).toString();
    location.href = url;
}