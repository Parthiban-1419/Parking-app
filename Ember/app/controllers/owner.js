import Controller from '@ember/controller';
import { action } from '@ember/object';
import { service } from '@ember/service';

export default class OwnerController extends Controller {
  @service session;
  @service myService;
  @service router;

  newBuilding = {};

  @action
  addBuilding() {
    var req = new XMLHttpRequest();
    let self = this;

    req.onload = function () {
      alert(this.responseText);
      if (this.response === 'Building added successfully') {
        // self.newBuilding.building_name = $('#buildingName').val();
        // self.newBuilding.owner_name = self.session.data.authenticated.token.loginNam;
        // self.newBuilding.street_address = $('#streetAddress').val();
        // self.newBuilding.area = $('#area').val();
        // self.newBuilding.pincode = $('#pincode').val();
        // self.newBuilding.description = $('#description').val();

        // self.session.buildings[self.session.buildings.length] = self.newBuilding;
        // self.myService.buildings[self.myService.buildings.length] = self.newBuilding;
      }
    };

    req.open('POST', 'http://localhost:8080/Parking-App/add-building', true);
    req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    req.send(
      'name=' +
        self.session.data.authenticated.token.loginName +
        '&buildingName=' +
        $('#buildingName').val() +
        '&streetAddress=' +
        $('#streetAddress').val() +
        '&area=' +
        $('#area').val() +
        '&pincode=' +
        $('#pincode').val() +
        '&phoneNumber=' +
        $('#phone').val() +
        '&description=' +
        $('#description').val()
    );
  }
}

       

        // $('.contents').append('<OwnerComnent></OwnerComponent>');
        // self.session.buildings[self.session.buildings.length] = self.newBuilding;
