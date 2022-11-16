import Controller from '@ember/controller';
import { action } from '@ember/object';
import { tracked } from '@glimmer/tracking';
import { inject as service } from '@ember/service';

export default class MainPageController extends Controller {
  @service session;

  @tracked vehicle;
  @tracked vehiclePrices;
  @tracked datas = {};
  @tracked floors;
  @tracked buildingName;
  @tracked placeId;
  @tracked blockIndex;
  @tracked placeIndex;

  isOwner = this.session.data.authenticated.token.role === 'owner';

  @action
  getData() {
    var req = new XMLHttpRequest();
    var self = this;
    // $('#' + self.vehicle + 'Image')
    //   .css('background-color', 'white')
    //   .css('height', '55px')
    //   .css('box-shadow', '3px 3px 10px 2px');

    // $('#' + vehicle + 'Image')
    //   .css('background-color', 'aqua')
    //   .css('height', '60px')
    //   .css('box-shadow', '3px 3px 20px 2px');

    req.onload = function () {
      self.datas = JSON.parse(this.responseText);
      console.log(self.datas);
      self.session.vehiclePrices = self.datas.vehiclePrices;
      self.vehicle = self.session.vehiclePrices[0].vehicle_type;
      self.floors = self.datas[self.vehicle];
      self.changeVehicle(self.vehicle, self);
    };

    req.open('POST', 'http://localhost:8080/Parking-App/get-data', false);
    req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    req.send('action=getData&buildingName=' + self.buildingName);
  }

  changeVehicle(vehicle, self) {
    $('.image').css('background-color', 'white');
    $('#' + vehicle + 'Image').css('background-color', 'aqua');
    self.vehicle = vehicle;
    self.floors = self.datas[vehicle];

    $(document).ready(function () {
      $('.Available').click(function () {
        $('.selected').attr('class', 'Available');
        $(this).css('class', 'selected');
        if (self.session.data.authenticated.token.role === 'customer')
          self.getVehicleDetail($(this));
        else {
          self.restrictPlace($(this));
        }
      });
      $('.Restricted').click(function () {
        if (self.session.data.authenticated.token.role === 'customer') {
          alert('Restricted place. Unable to book');
        } else {
          self.freePlace($(this));
        }
      });
    });
  }

  // createPage() {
  //   $('#floor').html('');
  //   for (let i = 0; i < this.datas[this.vehicle].length; i++) {
  //     $('#floor').append(
  //       '<option value=' +
  //         i +
  //         '>Floor ' +
  //         this.datas[this.vehicle][i].floor +
  //         '</option>'
  //     );
  //   }
  //   this.createBlocks(0);
  // }

  // createBlocks(index) {
  //   let self = this;
  //   var blocks = this.datas[this.vehicle][index].blocks,
  //     places;
  //   $('#myTable').html('');

  //   for (let i = 0; i < blocks.length; i++) {
  //     $('#myTable').append(
  //       '<tr class="blockRow" id="' +
  //         blocks[i].block +
  //         '" index="' +
  //         i +
  //         '"><td id=blockName>' +
  //         blocks[i].block +
  //         '</td>'
  //     );
  //     places = blocks[i].places;
  //     for (let j = 0; j < places.length; j++) {
  //       $('#' + blocks[i].block).append(
  //         '<td id="' +
  //           places[j].place_id +
  //           '" index="' +
  //           j +
  //           '"class=\'' +
  //           places[j].status +
  //           "'>" +
  //           places[j].place +
  //           '</td>'
  //       );
  //     }
  //     $('#myTable').append('</tr>');
  //   }
  //
  //   $('.Available').click(function () {
  //     $('#' + self.placeId).attr('class', 'Available');
  //     self.placeId = $(this).attr('id');
  //     self.blockIndex = $(this).parent().attr('index');
  //     self.placeIndex = $(this).attr('index');

  //     $('.window').html(
  //       '<center><br><br>Enter Vehicle no. : <input type="text" id="vehicleNumber" placeholder="Eg., TN 01 AB 1234"><br><br>' +
  //         '<h1>Note:</h1>' +
  //         '<p>For Bike: Rs.20 per hour</p>' +
  //         '<p>For Car: Rs.40 per hour</p>' +
  //         '<button id="bookButton">Book</button>' +
  //         '</center>'
  //     );
  //     $(this).attr('class', 'selected');
  //     setTimeout(function () {
  //       $('.main').css('opacity', '0.5');
  //       $('.window').fadeIn('200');
  //     }, 1);
  //   });
  // }

  // @action
  // changeTable() {
  //   this.createBlocks($('#floor').val());
  // }

  @action
  bookPlace(place) {
    let self = this;
    var req = new XMLHttpRequest();

    req.onload = function () {
      alert(this.responseText);
      if (this.responseText === 'Place booked successfully') {
        $('.window').css('display', 'none');
        $('.main').css('opacity', '1');
        place.attr('class', 'Unavailable');
        self.floors[place.parent().parent().parent().attr('floorIndex')].rows[
          place.parent().parent().attr('rowIndex')
        ].blocks[place.parent().attr('blockIndex')].places[
          place.attr('placeIndex')
        ].status = 'Unavailable';
        self.datas[self.vehicle] = self.floors;
        console.log(self.datas);
      }
    };

    req.open('POST', 'http://localhost:8080/Parking-App/book-place', false);
    req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    req.send(
      'action=bookPlace&placeId=' +
        $(place).attr('placeId') +
        '&vehicleNumber=' +
        $('#vehicleNumber').val() +
        '&name=' +
        this.session.data.authenticated.token.loginName
    );
  }

  @action
  hideWindow() {
    if (this.windowShown) $('.window').css('display', 'none');
  }

  @action
  addFloor() {
    let self = this;

    $('.main').css('opacity', '0.2');
    $('.window').css('display', 'initial');
    $('.window').html('<br><br><br><br>');
    $('.window').append(
      '<input type="number" id="newFloor" placeholder="Floor number"><br><br>'
    );
    $('.window').append('<br><br><button id="addBlock">Add floor</button>');

    $('#addBlock').click(function () {
      self.addFloorInDb();
    });
  }

  addFloorInDb() {
    var req = new XMLHttpRequest(),
      responseMessage;
    let self = this;

    req.onload = function () {
      responseMessage = JSON.parse(this.responseText);
      if (responseMessage.status === 'success') {
        alert('Floor added successfully');
        $('.window').css('display', 'none');
        $('.main').css('opacity', '1');
        self.datas[self.vehicle] = [
          ...self.datas[self.vehicle],
          responseMessage.message,
        ];
        self.floors = [...self.floors, responseMessage.message];
        console.log(self.datas);
      }
    };

    req.open('POST', 'http://localhost:8080/Parking-App/add-floor', false);
    req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    req.send(
      'buildingName=' +
        self.buildingName +
        '&floor=' +
        $('#newFloor').val() +
        '&vehicleType=' +
        self.vehicle
    );
  }

  getVehicleDetail(place) {
    let self = this;

    console.log(place);

    setTimeout(function () {
      $('.main').css('opacity', '0.2');
      $('.window').css('display', 'initial');
    }, 1);

    $('.window').html(
      '<center><br><br>Enter Vehicle no. : <input type="text" id="vehicleNumber" placeholder="Eg., TN 01 AB 1234"><br><br>' +
        '<h1>Note:</h1>' +
        '<p>For Bike: Rs.20 per hour</p>' +
        '<p>For Car: Rs.40 per hour</p>' +
        '<button id="bookButton">Book</button>' +
        '</center>'
    );

    $('#bookButton').click(function () {
      self.bookPlace(place);
    });
  }

  restrictPlace(place) {
    var self = this;
    setTimeout(function () {
      if (
        confirm('Are you sure, Do you want to restrict this place?') == true
      ) {
        self.changeInDb(place, 'restrictPlace');
        place.css('class', 'Restricted');
        self.floors[place.parent().parent().parent().attr('floorIndex')].rows[
          place.parent().parent().attr('rowIndex')
        ].blocks[place.parent().attr('blockIndex')].places[
          place.attr('placeIndex')
        ].status = 'Restricted';
        self.datas[self.vehicle] = self.floors;
      }
    }, 1);
  }

  freePlace(place) {
    var self = this;
    setTimeout(function () {
      if (confirm('Are you sure, Do you want to free this place?') == true) {
        self.changeInDb(place, 'freePlace');
        place.css('class', 'Available');
        self.floors[place.parent().parent().parent().attr('floorIndex')].rows[
          place.parent().parent().attr('rowIndex')
        ].blocks[place.parent().attr('blockIndex')].places[
          place.attr('placeIndex')
        ].status = 'Available';
        self.datas[self.vehicle] = self.floors;
      }
    }, 1);
  }

  changeInDb(place, action) {
    var req = new XMLHttpRequest();

    req.onload = function () {
      console.log(this.responseText);
    };

    req.open('POST', 'http://localhost:8080/Parking-App/change-place', false);
    req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    req.send('action=' + action + '&placeId=' + place.attr('placeId'));
  }
}
