$(document).ready(function() {
    $('#file').bind('change', function() {
        var fileSize = this.files[0].size/(1024*1024);

        if(fileSize > 10) {
            $('#uploadSubmit').prop('disabled', true);
            $('#fileError').show();
        }
        else {
            $('#uploadSubmit').prop('disabled', false);
            $('#fileError').hide();
        }
    });
});