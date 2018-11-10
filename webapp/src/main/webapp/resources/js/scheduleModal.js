$(document).ready(function() {

    var addModal = document.getElementById('add-time-modal');
    var addBtn = document.getElementById("add-time-modal-button");

    var deleteModal = document.getElementById("delete-time-modal");
    var deleteBtn = document.getElementById("delete-time-modal-button");

    var span = document.getElementsByClassName("modal-close")[0];

    var deleteSpan = document.getElementsByClassName("modal-close")[1];

    addBtn.onclick = function() {
        addModal.style.display = "block";
    };

    deleteBtn.onclick = function() {
        deleteModal.style.display = "block";
    };

    span.onclick = function() {
        addModal.style.display = "none";
    };

    deleteSpan.onclick = function() {
        deleteModal.style.display = "none";
    };

    window.addEventListener("click", function (event) {
        if (event.target == addModal) {
            addModal.style.display = "none";
        }
    });

    window.addEventListener("click", function (event) {
        if (event.target == deleteModal) {
            deleteModal.style.display = "none";
        }
    });

    var error = addModal.getElementsByClassName("error-text")[0];
    if(error != null) {
        addModal.style.display = "block";
    }

    var error = deleteModal.getElementsByClassName("error-text")[0];
    if(error != null) {
        deleteModal.style.display = "block";
    }
});