app.controller("authority-ctrl", function($scope, $http, $location) {

	$scope.roles = [];
	$scope.admins = [];
	$scope.authorities = [];

	$scope.initialize = function() {
		//load all roles

		$http.get("/rest/roles").then(resp => {
			$scope.roles = resp.data;
			
		})
		//load staffs and directors
		$http.get("/rest/accounts?admin = true").then(resp => {
			$scope.admins = resp.data;
		})

		//load authorities of staffs and directors
		$http.get("/rest/authority?admin = true").then(resp => {
			$scope.authorities = resp.data;
		}).catch(error => {
			$location.path("/unauthorized");
		})
	}
	$scope.initialize();

	$scope.authority_of = function(acc, role) {
		if ($scope.authorities) {
			return $scope.authorities.find(ur => ur.account.username == acc.username && ur.role.id == role.id);
		}
	}

	$scope.authority_changed = function(acc, role) {
		let authority = $scope.authority_of(acc, role);
		if (authority) {
			$scope.revoke_authority(authority);
		} else {
			authority = { account: acc, role: role };
			$scope.grant_authority(authority);
		}
	}
	
	//add authority
	$scope.grant_authority = function(authority){
		$http.post(`/rest/authority`,authority).then(resp =>{
			$scope.authorities.push(resp.data);
			alert("add authority successfully");
		}).catch(error =>{
			alert("Error");
			console.log("Error,",error);
		})
	}
	//delete authority
	$scope.revoke_authority = function(authority){
		$http.delete(`/rest/authority/${authority.id}`).then(resp =>{
			let index = $scope.authorities.findIndex( a => a.id == authority.id);
			$scope.authorities.splice(index,1);
			alert("Revoke_Authority successfully");
		}).catch(error =>{
			alert("Error");
			console.log("Error,",error);
		})
	}
})
