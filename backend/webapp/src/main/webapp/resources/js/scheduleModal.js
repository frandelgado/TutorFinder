$(document).ready(function() {

    var addModal = document.getElementById('add-time-modal');
    var addBtn = document.getElementById("add-time-modal-button");

    var deleteModal = document.getElementById("delete-time-modal");
    var deleteBtn = document.getElementById("delete-time-modal-button");

    var deleteClass = document.getElementById('delete-class-modal');
    var deleteClassBtn = document.getElementById("delete-class-modal-button");

    var span = addModal.getElementsByClassName("modal-close")[0];

    var deleteSpan = deleteModal.getElementsByClassName("modal-close")[0];

    var deleteClassSpan = deleteClass.getElementsByClassName("modal-close")[0];

    addBtn.onclick = function() {
        addModal.style.display = "block";
    };

    deleteBtn.onclick = function() {
        deleteModal.style.display = "block";
    };

    deleteClassBtn.onclick = function() {
        deleteClass.style.display = "block";
    };

    span.onclick = function() {
        addModal.style.display = "none";
    };

    deleteSpan.onclick = function() {
        deleteModal.style.display = "none";
    };

    deleteClassSpan.onclick = function() {
        deleteClass.style.display = "none";
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

    window.addEventListener("click", function (event) {
        if (event.target == deleteClass) {
            deleteClass.style.display = "none";
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

    var error = deleteClass.getElementsByClassName("error-text")[0];
    if(error != null) {
        deleteClass.style.display = "block";
    }
});