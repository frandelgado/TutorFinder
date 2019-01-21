$(document).ready(function(){
    document.getElementById("startHour").onclick = function() {

        $("#endHour option").each(function (e) {
            var startHour = document.getElementById("startHour");
            var startHourValue = startHour[startHour.selectedIndex].value;
            var option = $("#endHour option[value='" + e.valueOf() + "']");
            option.show();
            if(e.valueOf() <= startHourValue) {
                //option.remove();
                option.hide();
            }
        })
    }
});