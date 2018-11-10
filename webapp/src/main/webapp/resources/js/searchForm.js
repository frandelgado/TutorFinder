$(document).ready(function(){
    $('.js-example-basic-multiple').select2();
    var selected = $('select[name=type] option:selected');
    if(selected.val() == "course") {
        $('.filter-panel').css("display", "flex");
    }

    $('select[name=type]').change(function () {
        // hide all optional elements

        $("select[name=type] option:selected").each(function () {
            var value = $(this).val();
            if(value == "professor" || value == "area") {
                $('.filter-panel').hide();

            } else if(value == "course") {
                $('.filter-panel').css("display", "flex");
            }

        });
    });

    var minPriceObj = document.getElementById('minPrice');
    var maxPriceObj = document.getElementById('maxPrice');

    var minPrice = parseFloat(minPriceObj.value);
    var maxPrice = parseFloat(maxPriceObj.value);

    if(Number.isInteger(minPrice)) {
        minPriceObj.value = minPrice | 0;
    }

    if(Number.isInteger(maxPrice)) {
        maxPriceObj.value = maxPrice | 0;
    }
    document.getElementById("startHourSelect").onclick = function() {
        $("#endHourSelect option").each(function (e) {
            var startHour = document.getElementById("startHourSelect");
            var startHourValue = startHour[startHour.selectedIndex].value;
            var option = $("#endHourSelect option[value='" + e.valueOf() + "']");
            option.show();
            if(e.valueOf() <= startHourValue) {
                //option.remove();
                option.hide();
            }
        })
    }
});