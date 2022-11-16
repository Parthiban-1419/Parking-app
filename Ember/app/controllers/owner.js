import Controller from '@ember/controller';
import { action } from '@ember/object';
import { service } from '@ember/service';
import { tracked } from '@glimmer/tracking';

export default class OwnerController extends Controller {
  @service session;
  @service myService;
  @service router;

  newBuilding = {};

  @action
  addBuilding() {
    var req = new XMLHttpRequest();
    let self = this;

    var vehicles = [];

    for (let i = 1; i <= $('.vehicle').length; i++) {
      vehicles[i - 1] = {
        vehicle_type: $('#vehicle' + i + ' #vehicleType').val(),
        price_per_hour: parseInt($('#vehicle' + i + ' #price').val()),
      };
    }

    req.onload = function () {
      alert(this.responseText);
      if (this.response === 'Building added successfully') {
        self.newBuilding.building_name = $('#buildingName').val();
        self.newBuilding.owner_name =
          self.session.data.authenticated.token.loginNam;
        self.newBuilding.street_address = $('#streetAddress').val();
        self.newBuilding.area = $('#area').val();
        self.newBuilding.pincode = $('#pincode').val();
        self.newBuilding.description = $('#description').val();
        self.newBuilding.vehiclePrices = vehicles;

        self.myService.buildings = [
          ...self.myService.buildings,
          self.newBuilding,
        ];

        console.log(self.myService.buildings);

        $('#buildingName').val('');
        $('#streetAddress').val('');
        $('#area').val('');
        $('#pincode').val('');
        $('#phone').val('');
        $('#description').val('');
        $('.vehicles').html(
          '<div class="vehicle" id="vehicle1">' +
            '<input type="text" id="vehicleType" placeholder="Vehicle type">' +
            '<input type="number" id="price" placeholder="price">' +
            '</div>'
        );
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
        $('#description').val() +
        '&vehiclePrices=' +
        JSON.stringify(vehicles)
    );
  }

  @action
  addVehicle() {
    var num = $('.vehicle');
    $('.vehicles').append(
      '<div class="vehicle" id="vehicle' +
        (num.length + 1) +
        '">' +
        '<input type="text" id="vehicleType" placeholder="Vehicle type">' +
        '<input type="number" id="price" placeholder="price">' +
        '</div'
    );
  }
}
