const customTooltip = $("#brandTooltip");
function updateTtPosition(event) {
  const customTooltip = $("#brandTooltip");
  const offsetX = 10;
  const offsetY = 10;

  customTooltip.css("left", event.pageX + offsetX + "px");
  customTooltip.css("top", event.pageY + offsetY + "px");
}
$(document).ready(function () {
  $("#brandTooltip").hide();
});

$(".brand-box").hover(
  function (event) {
    const brand = $(this).attr("data-brand");
    customTooltip.text(brand);
    updateTtPosition(event);
    customTooltip.show();
  },
  function () {
    customTooltip.hide();
  }
);

document.addEventListener("mousemove", function (event) {
  if (customTooltip.is(":visible")) {
    updateTtPosition(event);
  }
});
