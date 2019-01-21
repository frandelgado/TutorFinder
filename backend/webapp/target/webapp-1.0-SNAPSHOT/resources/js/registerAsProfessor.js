$(document).ready(function() {
    $('#picture').bind('change', function() {
        var fileSize = this.files[0].size/1024;

        if(fileSize > 80) {
            $('#registerAsProfessorSubmit').prop('disabled', true);
            $('#pictureError').show();
        }
        else {
            $('#registerAsProfessorSubmit').prop('disabled', false);
            $('#pictureError').hide();
        }
    });

    $('#pic').bind('change', function() {
        var fileSize = this.files[0].size/1024;

        if(fileSize > 80) {
            $('#modifyProfessorSubmit').prop('disabled', true);
            $('#pictureError').show();
        }
        else {
            $('#modifyProfessorSubmit').prop('disabled', false);
            $('#pictureError').hide();
        }
    });
});