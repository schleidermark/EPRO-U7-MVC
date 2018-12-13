var resturl = "http://localhost:8080";

/**
 * Initialize if document is loaded completely
 */
$(document).ready(function(){
    //Get Customers
    getCustomers();
    //Reset Form
    resetForm();

    //Bind reset button
    $("#btnreset").click(function () {
        resetForm();
    });

    //Bind submit button
    $("#submit").click(function() {

        var formObject = $("#UserForm");
        var customerIdField = formObject.find("#customerId");

        var requestUrl = resturl+"/customer";
        var requestType = "POST";

        if (customerIdField.length === 1) {
            requestUrl += "/"+customerIdField.val();
            requestType = "PUT";
        }

        $("#pleaseWaitDialog").modal();

        $.ajax({
            url: requestUrl,
            type: requestType,
            data: JSON.stringify(getFormData($( "#UserForm" ))),
            contentType:"application/json",
            dataType:"json",
            success: function(){
                resetForm();
            },
            error: function(xhr) {
                markErrors(xhr.responseText);
            },

            complete: function () {
                getCustomers();
                $("#pleaseWaitDialog").modal('hide');
            }
        });

    });
});

/**
 * Set the number of customers
 */
function setCustomerCount() {

    $.ajax({
        url: resturl+"/customer/count",
        dataType: "JSON",
        type: "GET",

        success: function(response) {
            $('#count-of-customers').text(" (Count: "+response+")");
        }
    });

}

/**
 * Get the full list of customers
 */
function getCustomers() {

    var template = $("#customer-template");

    $.ajax({
        url: resturl+"/customer",
        dataType: "JSON",
        type: "GET",

        success: function (response) {

            var accordion = $("#customer-accordion");
            accordion.empty();

            var first = true;
            $.each(response, function(key, value){

                var row = template.clone();
                var description = value['city'];

                row.removeClass("hidden").removeAttr("id");
                row.find(".card-link").attr("href", "#customer"+value['customerId']);
                row.find("#customer-header").removeAttr("id").text(value['name']);
                row.find("#customer-description").removeAttr("id").text(description);


                if (first) {
                    row.find("#customer-detail-head").addClass("show");
                    first = false;
                }

                row.find("#customer-detail-head").attr("id", "customer"+value['customerId']);

                var rowBody = row.find("#customer-body");
                var customerBody = $("#customer-details-template").clone();

                rowBody.removeAttr("id");
                customerBody.removeAttr("id").removeClass("hidden");

                $.each(value, function(key, value) {
                    if (key !== "missingFields") {
                        var fieldName = ".customer-" + key;

                        if (key === "zip") {
                            customerBody.find(fieldName).text(value['zipCode']);
                        } else if(key === "discountCode") {
                            customerBody.find(fieldName).text(value['discountCode']+" (Rate: "+value['rate']+")");
                        } else {
                            customerBody.find(fieldName).text(value);
                        }
                    }
                });

                customerBody.find("#button-delete").removeAttr("id").attr("onclick", "deleteCustomer("+value['customerId']+")");
                customerBody.find("#button-edit").removeAttr("id").attr("onclick", "editCustomer("+value['customerId']+")");

                customerBody.appendTo(rowBody);

                row.appendTo(accordion);
            });

        }
    });

    setCustomerCount();
    return false;
}

/**
 * Delete a customer by id
 * @param id
 */
function deleteCustomer(id) {

    if (confirm("Delete customer with id: " + id + "?") === true) {
        $.ajax({
            url: resturl + "/customer/" + id,
            dataType: "JSON",
            type: "DELETE",

            success: function () {
                console.log("Customer " + id + " deleted");
            },
            complete: function () {
                getCustomers();
            }
        });
    }
}

/**
 * Load customer data to the user form
 * @param id
 */
function editCustomer(id) {

    $.ajax({
        url: resturl + "/customer/"+id,
        dataType: "JSON",
        type: "GET",

        success: function (response) {

            var form = $("#UserForm");

            $.each(response, function (key, value) {

                if (key !== "missingFields") {

                    var input = form.find('input[name="'+key+'"]');

                    if (key === "zip") {
                        input.val(value['zipCode']);
                    } else if(key === "discountCode") {
                        input.val(value['discountCode']);
                    } else {
                        input.val(value);
                    }
                }
            });

            var idField = form.find("#customerId");

            if (idField.length === 0) {
                form.append('<input type="hidden" name="customerId" id="customerId" value="' + id + '">');
            } else {
                idField.val(id);
            }
        }
    });

}

/**
 * Reset the Userform an remove the customerId-field
 */
function resetForm() {
    var formObject = $("#UserForm");

    formObject.remove("#customerId");
    formObject[0].reset();

    resetFields();
}

function getFormData($form){
    var unindexed_array = $form.serializeArray();
    var indexed_array = {};

    $.map(unindexed_array, function(n, i){
        if (n['value'].trim() != '') {
            indexed_array[n['name']] = n['value'];
        }
    });

    return indexed_array;
}

function resetFields() {
    $("#error-wrapper").addClass("hidden");
    $("input").removeClass("is-invalid");
}

function ioerror(text) {
    console.log(text);
    $("#missingFields").text(text);
    $("#error-wrapper").removeClass("hidden");
}

function markErrors(text) {
    resetFields();
    var jsonArray = JSON.parse(text);

    if (jsonArray.type && jsonArray.type == "io") {
        ioerror(jsonArray.error);
        return;
    }

    var text = "Felder müssen gefüllt sein:";

    for (var i = 0; i < jsonArray.length; i++) {
        $('#'+jsonArray[i]).addClass("is-invalid");
        text += " " + jsonArray[i] + ", ";
    }

    $("#missingFields").text(text);
    $("#error-wrapper").removeClass("hidden");
}
