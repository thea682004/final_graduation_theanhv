const getStatus = (status) => {
    const statusNumber = parseInt(status)
    switch (statusNumber) {
        case 0:
            return "Đã đóng";
        case 1:
            return "Dự kiến tổ chức";
        case 2:
            return "Cần sửa";
        case 3:
            return "Chờ phê duyệt";
        case 4:
            return "Đã phê duyệt";
        case 5:
            return "Đã tổ chức";
        case 6:
            return "Đã thông qua CNBM";
        case 8:
            return "Đã thông qua TBĐTCS";
        default:
            return "sự kiện đã tổ chức và mong muốn sửa";
    }
}

const convertShowStatusEntity = (status) => {
    switch (status) {
        case 0:
            return '<span class="tag tag-success">Hoạt động</span>';
        case 1:
            return '<span class="tag tag-warning">Ngừng hoạt động</span>';
        default:
            return '<span class="tag tag-secondary">Không xác định</span>';
    }
}

const convertStringToList = (list) => {
    return list.split(",");
}

const formatDateTime = (number) => {
    if (number) {
        return moment(number).format('DD-MM-YYYY');
    } else {
        return 'Chưa cập nhật';
    }
}

const formatDate = (number) => {
    if (number) {
        return moment(number).format('HH:mm DD-MM-YYYY');
    } else {
        return 'Chưa cập nhật';
    }
}

/**
 * Logic phan trang
 * @param type
 * @returns page request => Call api
 */
const buttonChangePage = (type) => {
    let totalPage = Number($('#total_page').text());
    let currentPage = Number($('#current_page').text());
    let page = null;

    if (type === 1) {
        if (currentPage === 0) {
            page = totalPage - 1;
        } else {
            page = currentPage - 1;
        }
    }
    if (type === 0) {
        if (currentPage === (totalPage - 1)) {
            page = 0;
        } else {
            page = currentPage + 1;
        }
    }

    return page;
}

const changePage = () => {
    let totalPage = Number($("#total_page").text());
    let inputPage = $("#input_page");

    if (inputPage.val().length !== 0) {
        let input = parseInt(inputPage.val());
        if (input <= 0) {
            input = 1;
        }
        if (input > totalPage) {
            input = totalPage;
        }
        return input;
    }

    return null;
}

const callApi = (url, data, method) => {
    let response = null;
    if (method !== "GET") {
        data = JSON.stringify(data);
    }
    $.ajax({
        type: method,
        contentType: "application/json",
        url: url,
        dataType: 'json',
        data: data,
        async: false,
        success: function (responseData) {
            response = responseData;
            if (method !== "GET") {
                if (method === "POST" && url.endsWith("list-event-waiting-approve")) {
                } else {
                }
            }
        },
        error: function (e) {
            console.log(e)
            const messError = e?.responseJSON?.message ?? "Thất bại...";
            showToastError(messError)
        }
    });
    return response;
}

