app.controller("product-ctrl", function ($scope, $http) {
	const productButton = $('.product-button');
	const productTab = $('.tab-content');
	const cancelBtn = $('.cancel-delete-btn');
	const modalConfirm = $('#confirm');
	const deleteBtn = $('.btn-delete-product');

	deleteBtn.on('click', function () {
		modalConfirm.css('display', 'block');
	});

	cancelBtn.on('click', function () {
		modalConfirm.css('display', 'none');
	});

	productButton.on('click', function (e) {
		const btnTarget = e.target;
		if (btnTarget.matches('button')) {
			$(this).children().removeClass('active');
			$(btnTarget).addClass('active');
		}

		productTab.removeClass('active-tab');
		$(`#${btnTarget.id}-tab`).addClass('active-tab');
	});

	const listBtn = $('#admin-list-product');
	const createBtn = $('#admin-create-product');
	const listTab = $('#admin-list-product-tab');
	const createTab = $('#admin-create-product-tab');

	$scope.items = [];
	$scope.cates = [];
	$scope.brands = [];
	$scope.colors = [];
	$scope.form = {};
	$scope.productSizes = [];

	$scope.initializeProductSize = function () {
		$scope.productSizes = [];
		for (let size = 36; size <= 45; size++) {
			$scope.productSizes.push({
				id: null,
				size: size.toString(),
				quantity: 0,
				product: {
					id: null
				}
			});
		}
	};

	$scope.initializeForm = function () {
		$scope.form = {
			createDate: new Date(),
			image: "upload.png",
			image1: "upload.png",
			image2: "upload.png",
			image3: "upload.png",
			available: true,
			price: 0
		};
	};

	$scope.initialize = function () {
		$scope.initializeForm();
		$scope.initializeProductSize();

		$http.get("/rest/products").then(resp => {
			$scope.items = resp.data;
			$scope.items.forEach(item => {
				item.createDate = new Date(item.createDate);
			});
		});

		$http.get("/rest/categories").then(resp => {
			$scope.cates = resp.data;
		});

		$http.get("/rest/brands").then(resp => {
			$scope.brands = resp.data;
		});

		$http.get("/rest/colors").then(resp => {
			$scope.colors = resp.data;
		});
	};

	$scope.initialize();

	$scope.reset = function () {
		$scope.initializeForm();
		$scope.initializeProductSize();
	};

	$scope.edit = function (item) {
		$scope.form = angular.copy(item);

		$http.get(`/rest/productsize?id=${item.id}`).then(resp => {
			let productSizes = resp.data;
			productSizes.forEach((sizeData, index) => {
				if ($scope.productSizes[index]) {
					angular.extend($scope.productSizes[index], sizeData);
				}
			});
		});

		listBtn.removeClass('active');
		createBtn.addClass('active');
		listTab.removeClass('active-tab');
		createTab.addClass('active-tab');
	};

	$scope.create = function () {
		var item = angular.copy($scope.form);
		$http.post(`/rest/products`, item).then(resp => {
			let productId = resp.data.id;
			resp.data.createDate = new Date(resp.data.createDate);
			$scope.items.push(resp.data);

			$scope.productSizes.forEach(size => {
				size.product.id = productId;
				$http.post('/rest/productsize', size);
			});
			$scope.initialize();
			$scope.reset();
			$scope.changeDefaultTab();
			alert("Create successfully!");
		}).catch(error => {
			alert("Error! Please try again");
			console.log("Error :", error);
		});
	};

	$scope.update = function () {
		var item = angular.copy($scope.form);
		let sizesToUpdate = $scope.productSizes.map(size => angular.copy(size));

		sizesToUpdate.forEach(size => {
			$http.put('/rest/productsize/update', size);
		});

		$http.put(`/rest/products/${item.id}`, item).then(resp => {
			var index = $scope.items.findIndex(p => p.id == item.id);
			$scope.items[index] = item;
			$scope.initialize();
			$scope.changeDefaultTab();
			alert("Update successfully!");
		}).catch(error => {
			alert("Error");
			console.log("Error :", error);
		});
	};

	$scope.delete = function () {
		var item = angular.copy($scope.form);
		let sizesToDelete = $scope.productSizes.map(size => angular.copy(size));

		sizesToDelete.forEach(size => {
			if (size.id != null) {
				$http.delete(`/rest/productsize/delete/${size.id}`);
			}
		});

		$http.delete(`/rest/products/${item.id}`).then(resp => {
			var index = $scope.items.findIndex(p => p.id == item.id);
			$scope.items.splice(index, 1);
			$scope.initialize();
			$scope.reset();
			$scope.changeDefaultTab();
			alert("Delete successfully!");
		}).catch(error => {
			alert("Error");
			console.log("Error :", error);
		});
	};

	$scope.imageChanged = function (files, imageName) {
		var data = new FormData();
		data.append('file', files[0]);
		$http.post('/rest/upload/images', data, {
			transformRequest: angular.identity,
			headers: { 'Content-Type': undefined }
		}).then(resp => {
			$scope.form[imageName] = resp.data.name;
		}).catch(error => {
			alert("Error");
			console.log("Error :", error);
		});
	};

	$scope.changeDefaultTab = function () {
		listBtn.addClass('active');
		createBtn.removeClass('active');
		listTab.addClass('active-tab');
		createTab.removeClass('active-tab');
	};

	$scope.pager = {
		page: 0,
		size: 5,
		get items() {
			var start = this.page * this.size;
			return $scope.items.slice(start, start + this.size);
		},
		get count() {
			return Math.ceil(1.0 * $scope.items.length / this.size);
		},
		first() {
			this.page = 0;
		},
		prev() {
			this.page--;
			if (this.page < 0) {
				this.last();
			}
		},
		next() {
			this.page++;
			if (this.page >= this.count) {
				this.first();
			}
		},
		last() {
			this.page = this.count - 1;
		}
	};
});
