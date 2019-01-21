$(document).ready(function(){

    $(document).click(function(e){

        var content = document.getElementById("dropdown-content");

        if(content != null) {

            if (e.target.id == 'dropdown' || e.target.id == 'dropdown-button') {
                if (content.style.display === "block") {
                    content.style.display = "none";
                } else {
                    content.style.display = "block";
                }
                return true;
            } else if ($(e.target).closest("#dropdown").length > 0) {
                return true;
            }

            content.style.display = "none";
        }
    });
});