$('a').click(function(event) { 
    event.preventDefault(); 
    $.ajax({
        url: $(this).attr('href'),
        success: function(response) {
            alert(response);
        }
    });
    return false; // for good measure
});