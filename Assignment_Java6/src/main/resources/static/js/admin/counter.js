app.controller("counter-ctrl", function ($scope, $http) {
    // Danh sách tất cả sản phẩm
    $scope.allItems = [];
    // Danh sách lọc theo search
    $scope.filteredItems = [];
    // Tìm kiếm
    $scope.searchText = "";

    // Đơn hàng chờ (pending orders) lưu trong localStorage
    $scope.pendingOrders = JSON.parse(localStorage.getItem("pendingOrders")) || [];
    $scope.selectedOrder = $scope.pendingOrders[0] || null;

    // Giỏ hàng hiện tại lấy từ đơn hàng được chọn
    $scope.cart = {items: $scope.selectedOrder?.items || []};

    // Khach hang
    $scope.username = "None"
    $scope.accountData = null;
    $scope.inputQty = 0;

    $scope.qtyProduct = 0;

    $scope.moneyGiven = 0;
    $scope.change = undefined;

    // Tạo đơn hàng mới
    $scope.createNewOrder = function () {
        if($scope.pendingOrders && $scope.pendingOrders.length >= 5){
            return;
        }
        const newOrder = {
            id: Date.now(),
            items: [],
            createDate: new Date(),
            status: 'pending'
        };
        $scope.pendingOrders.push(newOrder);
        $scope.selectedOrder = newOrder;
        $scope.cart.items = newOrder.items;
        saveOrdersToLocal();
    };

    // Chọn đơn hàng đang thao tác
    $scope.selectOrder = function (order) {
        $scope.selectedOrder = order;
        $scope.cart.items = order.items;
    };

    // Xóa đơn hàng chờ
    $scope.removePendingOrder = function (orderId) {
        $scope.pendingOrders = $scope.pendingOrders.filter(o => o.id !== orderId);
        if ($scope.selectedOrder?.id === orderId) {
            $scope.selectedOrder = $scope.pendingOrders[0] || null;
            $scope.cart.items = $scope.selectedOrder ? $scope.selectedOrder.items : [];
        }
        saveOrdersToLocal();
    };

    // Phân trang
    $scope.pager = {
        page: 0,
        size: 6,
        totalPages: 0,
        get items() {
            const start = this.page * this.size;
            return $scope.filteredItems.slice(start, start + this.size);
        },
        first() {
            this.page = 0;
        },
        prev() {
            if (this.page > 0) this.page--;
        },
        next() {
            if (this.page < this.totalPages - 1) this.page++;
        },
        last() {
            this.page = this.totalPages - 1;
        },
    };

    // Load danh sách sản phẩm từ API
    $http.get("/rest/products").then(resp => {
        $scope.allItems = resp.data;
        $scope.filteredItems = [...$scope.allItems];
        $scope.pager.totalPages = Math.ceil($scope.filteredItems.length / $scope.pager.size);
    }).catch(err => {
        console.error("Lỗi khi lấy sản phẩm:", err);
    });

    // Load kích thước (size) của sản phẩm khi hover
    $scope.loadSizes = function (item) {
        $http.get("/rest/products/size/" + item.id).then(resp => {
            item.sizes = resp.data;
        }).catch(err => console.error("Lỗi khi load size:", err));
    };

    // Thêm sản phẩm vào giỏ hàng
    $scope.addToCart = function (productId, size) {
        if (!$scope.selectedOrder) {
            swal('Cảnh báo', "Vui lòng chọn hoặc tạo hóa đơn trước!", "waring")
            return;
        }

        const product = $scope.allItems.find(i => i.id === productId);
        if (!product) return;

        const sizeInfo = product.sizes?.find(s => s.size === size);
        if (!sizeInfo || sizeInfo.quantity < 1) {
            alert("Sản phẩm không hợp lệ hoặc tồn kho không đủ!");
            return;
        }

        const dataPut = {
            productId: productId,
            size: size,
            quantity: 1,
            increase: false
        }
        $http.put("/rest/productsize/update/quantity", dataPut).then(resp => {

        }).catch(err => {
            showToastError("Số lượng sản phẩm trong kho không đủ - ", err);
        })

        // Giảm tồn kho
        sizeInfo.quantity -= 1;
        $scope.qtyProduct = sizeInfo.quantity;

        // Tìm sản phẩm đã có trong giỏ
        let existingItem = $scope.selectedOrder.items.find(i => i.id === productId && i.size === size);
        if (existingItem) {
            existingItem.qty += 1;
            existingItem.inputQty += 1;
        } else {
            $scope.selectedOrder.items.push({
                id: productId,
                name: product.name,
                color: product.color,
                image: product.image,
                price: product.price,
                size: size,
                qty: 1,
                inputQty: 1,
                product: {id: productId},
            });
        }

        $scope.cart.items = $scope.selectedOrder.items;
        saveOrdersToLocal();
    };

    // Tăng số lượng sản phẩm
    $scope.increaseQty = function (item) {
        if (item.disabled) return;
        item.inputQty = (parseInt(item.inputQty) || 0) + 1;
        $scope.updateItemQty(item);
    };

    // Giảm số lượng sản phẩm
    $scope.decreaseQty = function (item) {
        if (item.disabled) return;
        let newQty = (parseInt(item.inputQty) || 0) - 1;
        if (newQty >= 1) {
            item.inputQty = newQty;
            $scope.updateItemQty(item);
        }
    };

    // Cập nhật số lượng sản phẩm trong giỏ hàng
    $scope.updateItemQty = function (item) {
        let inputQty = parseInt(item.inputQty);
        let currentQty = item.qty;

        if (isNaN(inputQty) || isNaN(currentQty)) {
            showToastError("Giá trị số lượng không hợp lệ");
            return;
        }

        fetchQuantityProduct(item.id, item.size).then(data => {
            let isIncrease = inputQty < currentQty;
            if (data === 0 && !isIncrease) {
                showToastError("Sản phẩm trong kho không đủ!");
                item.inputQty = currentQty;
                $scope.maxQtyProduct = currentQty + data;
                return;
            }

            let qtyUse = Math.abs(inputQty - currentQty);
            if (qtyUse === 0) return;
            if (!isIncrease && qtyUse > data) {
                showToastError("Số lượng vượt quá tồn kho!");
                return;
            }

            item.qty = inputQty;

            const dataPut = {
                productId: item.id,
                size: item.size,
                quantity: qtyUse,
                increase: isIncrease
            };

            $http({
                method: "put",
                url: "/rest/productsize/update/quantity",
                data: dataPut,
                headers: {
                    'Content-Type': 'application/json'
                }
            }).then(resp => {
                if (resp.status === 200) {
                    $scope.loadSizes(item);
                }
            }).catch(err => {
                showToastError("Lỗi cập nhật số lượng sản phẩm!", err);
            });

            saveOrdersToLocal();
            $scope.getCartTotal();
        });
    };

    // Xóa sản phẩm khỏi giỏ hàng
    $scope.removeItemFromCart = function (item) {
        const idx = $scope.selectedOrder.items.indexOf(item);
        if (idx > -1) {
            $scope.selectedOrder.items.splice(idx, 1);
            $scope.cart.items = $scope.selectedOrder.items;
            saveOrdersToLocal();
        }

        const dataPut = {
            productId: item.id,
            size: item.size,
            quantity: item.qty,
            increase: true
        };

        $http({
            method: "put",
            url: "/rest/productsize/update/quantity",
            data: dataPut,
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(resp => {
            if (resp.status === 200) {
                $scope.loadSizes(item);
            }
        }).catch(err => {
            showToastError("Lỗi cập nhật số lượng sản phẩm!", err);
        });

        saveOrdersToLocal();
        $scope.getCartTotal();
    };

    // Xóa toàn bộ giỏ hàng
    $scope.clearCart = function () {
        if ($scope.selectedOrder) {
            $scope.selectedOrder.items = [];
            $scope.cart.items = [];
            saveOrdersToLocal();
        }
    };

    // Tính tổng tiền giỏ hàng
    $scope.getCartTotal = function () {
        return $scope.cart.items.reduce((sum, item) => sum + (item.price * item.qty), 0);
    };

    // Lưu đơn hàng pending vào localStorage
    function saveOrdersToLocal() {
        localStorage.setItem("pendingOrders", JSON.stringify($scope.pendingOrders));
    }

    // Watch thay đổi tìm kiếm để lọc sản phẩm
    $scope.$watch('searchText', function (val) {
        const lowerVal = (val || '').toLowerCase();
        $scope.filteredItems = lowerVal
            ? $scope.allItems.filter(item =>
                (item.name?.toLowerCase().includes(lowerVal)) ||
                (item.code?.toLowerCase().includes(lowerVal)) ||
                (item.brand?.toLowerCase().includes(lowerVal)) ||
                (item.color?.name?.toLowerCase().includes(lowerVal)) ||
                (item.sizes?.some(s => s.size.toString().includes(lowerVal)))
            )
            : [...$scope.allItems];

        $scope.pager.page = 0;
        $scope.pager.totalPages = Math.ceil($scope.filteredItems.length / $scope.pager.size);
    });

    $scope.quickAddCustomer = function () {
        $('#modifyCustomerModal').modal('show');
        $http.get("/rest/accounts").then(res => {
            if (res?.data?.length === 0) {
                $scope.customers = [];
            } else {
                $scope.customers = res?.data;
            }
        }).catch(err => {
            showToastError('An occurred while fetching customer!');
        });
    }

    $scope.selectCustomer = function(username) {
        $http.get("/rest/accounts/username?username=" + username).then(resp => {
            $scope.accountData = resp?.data;
        }).catch(err => {
            console.error("An occurred while fetching data customer: ", err);
        });
        $scope.username = username;
        $('#modifyCustomerModal').modal('hide');
    }

    $scope.handlePayment = function () {

        if (!$scope.selectedOrder) {
            swal('Cảnh báo', "Vui lòng chọn hóa đơn cần thanh toán!", "warning")
            return;
        }

        if($scope.change === undefined) {
            swal('Cảnh báo', "Số tiền khách đưa không hợp lệ!", "warning")
            return;
        }

        const orderDetails = [];

        if (Array.isArray($scope.cart.items)) {
            $scope.cart.items.forEach((item) => {
                orderDetails.push({
                    quantity: item.qty,
                    reviewstatus: 0,
                    size: item.size,
                    product: item.product || null,
                });
            });
        }

        const orderData = {
            address: $scope.accountData?.address || null,
            phoneNumber: $scope.accountData?.sdt || null,
            totalAmount: $scope.getCartTotal() || null,
            pay: 1,
            status: 'completed',
            createDate: Date.now(),
            account: $scope.accountData,
            orderDetails: orderDetails,
        }

        if(!validateOrderData(orderData)) return;

        swal({
            title: "Xác nhận?",
            text: "Bạn chắc chắn muốn hoàn thành đơn hàng?",
            type: "warning",
            buttons: {
                cancel: {
                    visible: true,
                    text: "Hủy",
                    className: "btn btn-dark",
                },
                confirm: {
                    text: "Hoàn thành",
                    className: "btn btn-outline-dark",
                },
            },
        }).then((confirm) => {
            if (confirm) {
                $http.post("/rest/orders", orderData).then(resp => {
                    if(resp.status === 200) {
                        const orderId = $scope.selectedOrder.id;
                        $scope.removePendingOrder(orderId);
                        $scope.username = "None";
                        $scope.change = undefined;
                        $scope.moneyGiven = 0;
                        swal('Thành công', "Thanh toán thành công!", "success")
                    }
                }).catch(err => {
                    swal('Lỗi', "Có lỗi xảy ra trong quá trình thanh toán!", "error")
                })
            } else {
                showToastSuccess('Bạn đã hủy thành công!')
            }
        });
    }

    const fetchQuantityProduct = function(idProduct, size) {
        return $http.get(`/rest/productsize/get-quantity?id=${idProduct}&size=${size}`)
            .then(resp => {
                if (resp.status === 200) {
                    return resp.data;
                }
            })
            .catch(err => {
                console.log("An error occurred:", err);
            });
    };

    $scope.openModalCreateCustomer = () => {
        $('#createNewCustomerModal').modal('show');
    }

    $('#createCustomerButton').on('click', function () {
        $('#createNewCustomerModal').modal('hide');
        swal({
            title: "Xác nhận?",
            text: "Bạn chắc chắn muốn thêm khách hàng không?",
            type: "warning",
            buttons: {
                cancel: {
                    visible: true,
                    text: "Hủy",
                    className: "btn btn-dark",
                },
                confirm: {
                    text: "Thêm",
                    className: "btn btn-outline-dark",
                },
            },
        }).then((confirm) => {
            if (confirm) {
                createCustomer();
            } else {
                $('#createNewCustomerModal').modal('show');
            }
        });
    });

    function validateData(data) {
        if (!data.username) {
            swal("Lỗi", "Tên đăng nhập không được để trống", "error");
            return false;
        }
        if (!data.fullname) {
            swal("Lỗi", "Họ tên không được để trống", "error");
            return false;
        }
        if (!data.email) {
            swal("Lỗi", "Email không được để trống", "error");
            return false;
        }
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(data.email)) {
            swal("Lỗi", "Email không hợp lệ", "error");
            return false;
        }
        if (!data.sdt) {
            swal("Lỗi", "Số điện thoại không được để trống", "error");
            return false;
        }
        const phoneRegex = /^[0-9]{9,15}$/;
        if (!phoneRegex.test(data.sdt)) {
            swal("Lỗi", "Số điện thoại không hợp lệ", "error");
            return false;
        }
        if (!data.address) {
            swal("Lỗi", "Địa chỉ không được để trống", "error");
            return false;
        }
        if (!data.password) {
            swal("Lỗi", "Mật khẩu không được để trống", "error");
            return false;
        }
        if (data.password !== data.confirmPassword) {
            swal("Lỗi", "Mật khẩu xác nhận không khớp", "error");
            return false;
        }
        return true;
    }


    function createCustomer() {
        let data = {
            username: $('#username').val().trim(),
            password: "abc",
            confirmPassword: "abc",
            fullname: $('#fullName').val().trim(),
            email: $('#email').val().trim(),
            sdt: $('#phoneNumber').val().trim(),
            address: $('#address').val().trim(),
        }

        if(validateData(data)) {
            $http.post("/login/create-customer", data).then(resp => {
                if (resp.status === 200) {
                    showToastSuccess("Tạo khách hàng thành công!");
                    $scope.selectCustomer(resp.data?.username);
                    $('#createNewCustomerModal').modal('hide');
                }
            }).catch(err => {
                showToastError("Có lỗi xảy ra khi tạo khách hàng!");
                $('#createNewCustomerModal').modal('show');
            })
        }
    }

    function validateOrderData(orderData) {
        if (!orderData.totalAmount || orderData.totalAmount <= 0) {
            swal("Lỗi", "Giỏ hàng đang trống hoặc số tiền không hợp lệ", "error");
            return false;
        }

        if (!Array.isArray(orderData.orderDetails) || orderData.orderDetails.length === 0) {
            swal("Lỗi", "Không có sản phẩm nào trong đơn hàng", "error");
            return false;
        }

        for (let i = 0; i < orderData.orderDetails.length; i++) {
            const item = orderData.orderDetails[i];
            if (!item.size || !item.quantity || item.quantity <= 0) {
                swal("Lỗi", `Thông tin sản phẩm ở dòng ${i + 1} không hợp lệ`, "error");
                return false;
            }
        }
        return true;
    }

    $scope.updateChange = function () {
        const total = $scope.getCartTotal?.() || 0;
        const pay = parseFloat($scope.moneyGiven) || 0;

        if (pay >= total) {
            $scope.change = pay - total;
        } else {
            $scope.change = undefined;
        }
    };

});
