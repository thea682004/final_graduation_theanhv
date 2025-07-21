app.controller("category-ctrl", function($scope, $http){
	$scope.items = [];
	$scope.form = {};

	$http.get("/rest/categories/count").then(resp => {
		$scope.items = resp.data;
	})

	$scope.create = function() {
		var item = angular.copy($scope.form);

		$http.post('/rest/categories', item).then(resp => {
			$scope.items.push([resp.data, 0]);
			$scope.reset();
			alert("Create successfully!")
		}).catch(error => {
			alert("Error! Please try again");
			console.log("Error :", error);
		})
	}

	$scope.reset = function() {
		$scope.form = {}
	}

	$scope.pager = {
		page: 0,
		size: 8,
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
	}
})
