const app = angular.module("cart", []);

app.controller("cart-ctrl", function ($scope, $http, $location) {
	$scope.userHome = {};

	const usernameProfile = $('#user-profile').val();
	if (usernameProfile !== undefined) {
		$http.get(`/rest/accounts/profile?username=${usernameProfile}`).then(resp => {
			console.log(resp.data)
			$scope.userHome = resp.data;
		})
	}

	$scope.updateUser = function () {
		const user = angular.copy($scope.userHome);
		$http.put('/rest/accounts/update', user).then(resp => {
			alert('Cập nhật tài khoản thành công!')
		}).catch(e => console.log(e));
	}

	$scope.reviewProduct = []

	const productIdDetail = $('.feedback').attr('product-id');

	if (productIdDetail !== undefined) {
		$http.get(`/rest/feedbacks?id=${productIdDetail}`).then(resp => {
			$scope.reviewProduct = resp.data;
		}).catch(e => console.log(e))
	}

	$(".cart-hover").each(function () {
		const itemId = $(this).attr("item-size-id");
		const url = "/rest/products/size/" + itemId;

		$.ajax({
			url: url,
			type: "GET",
			success: function (response) {
				response.forEach(data => {
					const li = $('<li></li>')
						.text(`Sizes ${data.size} (${data.quantity})`)
						.click(function () {
							if (data.quantity > 0) {
								$scope.cart.add(itemId, data.size, 1);
							} else {
								return false;
							}
						})
						.css('cursor', data.quantity === 0 ? 'not-allowed' : 'pointer');

					$(this).append(li);
				});
			}.bind(this),
			error: function (xhr, status, error) {
				console.log(error);
			}
		});
	})

	$scope.isRatingChecked = function (star, rating) {
		return star === rating;
	};

	$scope.averageRating = function (reviewProduct) {
		var totalStars = 0;
		angular.forEach(reviewProduct, function (feedback) {
			totalStars += feedback.star;
		});

		if (totalStars === 0) {
			return 'chưa có dữ liệu'
		} else {
			return Math.round((totalStars / reviewProduct.length) * 10) / 10;
		}
	};

	$scope.detailReviews = []

	$scope.loadDetailReview = function (id) {
		$http.get(`/rest/orders/detail?id=${id}`).then(resp => {
			const needReivew = resp.data.filter(item => item.reviewstatus === false);
			$scope.detailReviews = needReivew;
		}).catch(e => {
			console.log(e);
		});
	}

	$scope.feedback = {
		reviewday: new Date(),
		star: '',
		review: '',
		product: {
			id: null
		},
		account: {
			username: null
		}
	}

	$scope.sendReview = function (order, id, index) {
		order.reviewstatus = true;
		let review = angular.copy($scope.feedback);
		review.product.id = $(`#review-product-id-${id}`).val();
		review.account.username = $('#review-username').val();
		review.star = $('input[name="rating' + id + '"]:checked').val();
		review.review = $(`#review-textarea-${id}`).val();

		$http.post('/rest/feedbacks/send', review).then(resp => {
			$scope.detailReviews.splice(index, 1);
			$http.put('/rest/orders/updateDetail', order);
			alert("Đánh giá thành công!")
		}).catch(e => {
			console.log(e)
		})
	}

	$scope.cart = {
		items: [],

		cancelOrder(id) {
			const confirmCancel = window.confirm('Bạn có muốn hủy đơn hàng này?');
			if (!confirmCancel) {
				return;
			}

			$http.get(`/rest/orders/${id}`).then(resp => {
				const order = resp.data;

				// Kiểm tra trạng thái không cho phép hủy
				if (order.status === 'delivering') {
					alert('Đơn hàng đang được giao không thể hủy.');
					return;
				}
				if (order.status === 'completed') {
					alert('Đơn hàng đã hoàn thành không thể hủy.');
					return;
				}
				order.status = "cancel";

				$http.put(`/rest/orders/update`, order).then(resp => {
					alert('Hủy đơn hàng thành công');
					location.href = '/order/list';
				}).catch(e => {
					console.log(e);
					alert('Có lỗi xảy ra khi hủy đơn hàng');
				});

			}).catch(e => {
				console.log(e);
				alert('Không tìm thấy đơn hàng');
			});
		},

		//add items to cart
		add(id, size, quantity) {
			const existingItem = this.items.find(item => item.id == id && item.size == size);

			// Gọi API lấy tồn kho theo size
			$http.get(`/rest/products/size/${id}`).then(resp => {
				const productSizeInfo = resp.data.find(p => p.size == size);
				if (!productSizeInfo) {
					alert(`Không tìm thấy thông tin tồn kho cho Size ${size}`);
					return;
				}
				const availableQuantity = productSizeInfo.quantity;

				if (existingItem) {
					// Nếu tổng qty vượt quá tồn kho
					if (existingItem.qty + quantity > availableQuantity) {
						alert(`Số lượng sản phẩm Size ${size} không đủ. Chỉ còn ${availableQuantity} sản phẩm.`);
						return;
					}
					existingItem.qty += quantity;
					alert("Thêm vào giỏ hàng thành công!");
					this.saveToLocalStorage();
				} else {
					if (quantity > availableQuantity) {
						alert(`Số lượng sản phẩm Size ${size} không đủ. Chỉ còn ${availableQuantity} sản phẩm.`);
						return;
					}
					// Lấy thông tin sản phẩm
					$http.get(`/rest/products/${id}`).then(resp => {
						resp.data.qty = quantity;
						resp.data.size = size;
						this.items.push(resp.data);
						alert("Thêm vào giỏ hàng thành công!");
						this.saveToLocalStorage();
					}).catch(e => console.log(e));
				}
			}).catch(e => {
				console.log(e);
				alert('Lỗi khi kiểm tra số lượng sản phẩm');
			});
		},
		addAndPay(id, size, quantity) {
			const existingItem = this.items.find(item => item.id == id && item.size == size);

			$http.get(`/rest/products/size/${id}`).then(resp => {
				const productSizeInfo = resp.data.find(p => p.size == size);
				if (!productSizeInfo) {
					alert(`Không tìm thấy thông tin tồn kho cho Size ${size}`);
					return;
				}
				const availableQuantity = productSizeInfo.quantity;

				if (existingItem) {
					if (existingItem.qty + quantity > availableQuantity) {
						alert(`Số lượng sản phẩm Size ${size} không đủ. Chỉ còn ${availableQuantity} sản phẩm.`);
						return;
					}
					existingItem.qty += quantity;
					this.saveToLocalStorage();
					location.href = '/cart/view';
				} else {
					if (quantity > availableQuantity) {
						alert(`Số lượng sản phẩm Size ${size} không đủ. Chỉ còn ${availableQuantity} sản phẩm.`);
						return;
					}
					$http.get(`/rest/products/${id}`).then(resp => {
						resp.data.qty = quantity;
						resp.data.size = size;
						this.items.push(resp.data);
						this.saveToLocalStorage();
						location.href = '/cart/view';
					}).catch(e => console.log(e));
				}
			}).catch(e => {
				console.log(e);
				alert('Lỗi khi kiểm tra số lượng sản phẩm');
			});
		},

		updateQuantity(item, newQuantity) {
			// Gọi API lấy tồn kho theo size
			$http.get(`/rest/products/size/${item.id}`).then(resp => {
				const productSizeInfo = resp.data.find(p => p.size == item.size);
				if (!productSizeInfo) {
					alert(`Không tìm thấy thông tin tồn kho cho Size ${item.size}`);
					return;
				}
				const availableQuantity = productSizeInfo.quantity;

				if (newQuantity > availableQuantity) {
					alert(`Số lượng sản phẩm Size ${item.size} không đủ. Chỉ còn ${availableQuantity} sản phẩm.`);
					// Reset lại số lượng cũ
					item.qty = availableQuantity;
				} else if (newQuantity < 1) {
					alert(`Số lượng tối thiểu là 1`);
					item.qty = 1;
				} else {
					item.qty = newQuantity;
					// alert("Cập nhật số lượng thành công!");
				}

				this.saveToLocalStorage();
			}).catch(e => {
				console.log(e);
				alert('Lỗi khi kiểm tra số lượng sản phẩm');
			});
		},

		getsize() {
			return $('#size-product').val();
		},

		getquantity() {
			return parseInt($('#quantity-product').val());
		},

		//remove item of cart
		remove(id) {
			let index = this.items.findIndex(item => item.id == id);
			this.items.splice(index, 1);
			this.saveToLocalStorage();
		},

		// clear all items in cart
		clear() {
			this.items = []
			this.saveToLocalStorage();
		},

		//return total of items
		get count() {
			return this.items.length
			// .map(item => item.qty)
			// .reduce((total, qty) => total += qty, 0);
		},

		//return total money of items

		get amount() {
			return this.items
				.map(item => item.qty * item.price)
				.reduce((total, qty) => total += qty, 0)
		},



		//save the cart at localStorage
		saveToLocalStorage() {
			let json = JSON.stringify(angular.copy(this.items));
			localStorage.setItem("cart", json);
		},

		//display data to table
		loadFromLocalStorage() {
			let json = localStorage.getItem("cart");
			this.items = json ? JSON.parse(json) : [];
		}

	}

	$scope.cart.checkEnter = function (event, item) {
		if (event.keyCode === 13) { // Enter
			this.updateQuantity(item, item.qty);
			event.preventDefault();
		}
	};

	$scope.cart.changeQuantity = function (item) {
		// Gọi updateQuantity để check tồn kho khi dùng spinner
		this.updateQuantity(item, item.qty);
	};

	$scope.cart.loadFromLocalStorage();

	$scope.order = {
		createDate: new Date(),
		address: $('#user-address').val(),
		phoneNumber: $('#user-sdt').val(),
		account: {
			username: $("#username").val()
		},
		status: 'confirmed',
		pay: false,
		totalAmount: $scope.cart.amount,
		get orderDetails() {
			return $scope.cart.items.map(item => {
				return {
					product: { id: item.id },
					size: item.size,
					quantity: item.qty,
					reviewstatus: false
				}
			});
		},

		purchase() {
			const payment = $('#payment').val();
			if (payment === '2') {
				const bank = selectedValue = $('input[name="bank"]:checked').val();
				const money = parseInt($scope.cart.amount);
				$http.post(`/createPayment?amount=${money}&bankCode=${bank}`).then(resp => {
					location.href = resp.data.data;
					let order = angular.copy(this);
					order.pay = true;
					$http.post("/rest/orders", order).then(resp => {
						console.log('success!')
					}).catch(error => {
						console.log(error);
					})
				}).catch(e => console.log(e));
			} else {
				let order = angular.copy(this);

				$http.post("/rest/orders", order).then(resp => {
					alert("Đặt hàng thành công!");
					$scope.cart.clear();
					location.href = "/order/detail/" + resp.data.id;
				}).catch(error => {
					console.log(error);
				})
			}
		}
	}

	if ($location.absUrl().includes('/order/list') && $location.absUrl().includes('vnp_ResponseCode')) {
		alert('Đặt hàng thành công');
		$scope.cart.clear();
	}
})

app.directive('spinnerChange', function () {
	return {
		restrict: 'A',
		link: function (scope, element, attrs) {
			element.on('change', function () {
				// Áp dụng cho spinner click hoặc blur
				scope.$apply(function () {
					scope.cart.updateQuantity(scope.item, scope.item.qty);
				});
			});
		}
	};
});
