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