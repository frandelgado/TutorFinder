function responsiveNavbar() {
    var x = document.getElementById("navbar-buttons");
    if (x.className === "navbar-buttons") {
        x.className += " responsive";
    } else {
        x.className = "navbar-buttons";
    }
}