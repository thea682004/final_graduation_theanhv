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

    // Tạo đơn hàng mới
    $scope.createNewOrder = function () {
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
        if (!item.sizes) {
            $http.get("/rest/products/size/" + item.id).then(resp => {
                item.sizes = resp.data;
            }).catch(err => console.error("Lỗi khi load size:", err));
        }
    };

    // Thêm sản phẩm vào giỏ hàng
    $scope.addToCart = function (productId, size) {
        if (!$scope.selectedOrder) {
            alert("Vui lòng chọn hoặc tạo hóa đơn trước!");
            return;
        }

        const product = $scope.allItems.find(i => i.id === productId);
        if (!product) return;

        const sizeInfo = product.sizes?.find(s => s.size === size);
        if (!sizeInfo || sizeInfo.quantity < 1) {
            alert("Sản phẩm không hợp lệ hoặc tồn kho không đủ!");
            return;
        }

        // Giảm tồn kho
        sizeInfo.quantity -= 1;

        // Tìm sản phẩm đã có trong giỏ
        let existingItem = $scope.selectedOrder.items.find(i => i.id === productId && i.size === size);
        if (existingItem) {
            existingItem.qty += 1;
        } else {
            $scope.selectedOrder.items.push({
                id: productId,
                name: product.name,
                color: product.color,
                image: product.image,
                price: product.price,
                size: size,
                qty: 1,
                product: {id: productId},
            });
        }

        $scope.cart.items = $scope.selectedOrder.items;
        saveOrdersToLocal();
    };

    // Cập nhật số lượng sản phẩm trong giỏ hàng
    $scope.updateItemQty = function (item) {
        saveOrdersToLocal();
        $scope.getCartTotal();
    };

    // Xóa sản phẩm khỏi giỏ hàng
    $scope.removeItemFromCart = function (item) {
        const idx = $scope.selectedOrder.items.indexOf(item);
        if (idx > -1) {
            $scope.selectedOrder.items.splice(idx, 1);
            $scope.cart.items = $scope.selectedOrder.items;
            saveOrdersToLocal();
        }
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

    // Handle quick customer add
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

        const orderDetails = [];2

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

        $http.post("/rest/orders", orderData).then(resp => {
            if(resp.status === 200) {
                const orderId = $scope.selectedOrder.id;
                $scope.removePendingOrder(orderId);
                $scope.username = "None";
                showToastSuccess("Successfully!")
            }
        }).catch(err => {
            console.log(err);
            alert("An occurred while payment order!");
        })
    }

});
