$(document).ready(function() {
var modal = document.getElementById('add-time-modal');
var btn = document.getElementById("add-time-modal-button");
var span = document.getElementsByClassName("modal-close")[0];
btn.onclick = function() {
    modal.style.display = "block";
};
span.onclick = function() {
    modal.style.display = "none";
};
window.onclick = function(event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
};
var error = document.getElementsByClassName("error-text")[0];
if(error != null) {
    modal.style.display = "block";
}
});