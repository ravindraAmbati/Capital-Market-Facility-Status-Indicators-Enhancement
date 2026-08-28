function editFacilityType(button) {
    document.getElementById("facilityIsNew").value = "false";
    document.getElementById("facilityTypeCode").value =
        button.dataset.code || "";
    document.getElementById("facilityTypeDescription").value =
        button.dataset.description || "";
    document.getElementById("facilityAdvised").value =
        button.dataset.advised || "Y";
    document.getElementById("facilityCommitted").value =
        button.dataset.committed || "Y";

    document.getElementById("facilityTypeCode").readOnly = true;

    document.getElementById("facilityTypeForm")
        .scrollIntoView({behavior: "smooth"});
}

function resetFacilityForm() {
    document.getElementById("facilityIsNew").value = "true";
    document.getElementById("facilityTypeForm").reset();
    document.getElementById("facilityTypeCode").readOnly = false;
}

function editPurposeCode(button) {
    document.getElementById("purposeIsNew").value = "false";
    document.getElementById("purposeCodeHub").value =
        button.dataset.hub || "";
    document.getElementById("purposeCodeCarm").value =
        button.dataset.carm || "";
    document.getElementById("purposeDescription").value =
        button.dataset.description || "";
    document.getElementById("purposeUnconditional").value =
        button.dataset.unconditional || "Y";

    document.getElementById("purposeCodeHub").readOnly = true;
    document.getElementById("purposeCodeCarm").readOnly = true;

    document.getElementById("purposeCodeForm")
        .scrollIntoView({behavior: "smooth"});
}

function resetPurposeForm() {
    document.getElementById("purposeIsNew").value = "true";
    document.getElementById("purposeCodeForm").reset();
    document.getElementById("purposeCodeHub").readOnly = false;
    document.getElementById("purposeCodeCarm").readOnly = false;
}

function confirmDelete(type) {
    return window.confirm(
        "Are you sure you want to delete this " + type + "?"
    );
}
