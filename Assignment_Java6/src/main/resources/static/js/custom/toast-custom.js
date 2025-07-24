const showToastError = (message) => {
    Toastify({
        text: message,
        className: "toastify-custom-error",
        backgroundColor: "#990000",
        style: {
            color: "#ffffff"
        },
        close: true,
        duration: 2000
    }).showToast();
}

const showToastWarning = (message) => {
    Toastify({
        text: message,
        className: "toastify-custom-error",
        backgroundColor: "rgba(250,142,0,0.87)",
        style: {
            color: "#ffffff"
        },
        close: true,
        duration: 2000
    }).showToast();
}

const showToastSuccess = (message) => {
    Toastify({
        text: message,
        className: "toastify-custom-error",
        close: true,
        duration: 2000
    }).showToast();
}