import Controller from '@ember/controller';
import { action } from '@ember/object';
import { tracked } from '@glimmer/tracking';
import { inject as service } from '@ember/service';

export default class MainPageController extends Controller {
  @service session;

  @tracked vehicle;
  @tracked datas = {};
  @tracked buildingName;
  @tracked placeId;
  @tracked blockIndex;
  @tracked placeIndex;

  @action
  getData(vehicle) {
    var req = new XMLHttpRequest();
    var self = this;
    $('#' + self.vehicle + 'Image')
      .css('background-color', 'white')
      .css('height', '55px')
      .css('box-shadow', '3px 3px 10px 2px');

    $('#' + vehicle + 'Image')
      .css('background-color', 'aqua')
      .css('height', '60px')
      .css('box-shadow', '3px 3px 20px 2px');
    self.vehicle = vehicle;

    try {
      if (self.datas[self.vehicle] == undefined) throw new Error('undefined');
      else self.createPage();
    } catch (error) {
      req.onload = function () {
        console.log(this.responseText);
        self.datas[self.vehicle] = JSON.parse(this.responseText);
        self.createPage();
      };

      req.open('POST', 'http://localhost:8080/Parking-App/get-data', false);
      req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
      req.send(
        'action=getData&vehicleType=' +
          self.vehicle +
          '&buildingName=' +
          self.buildingName
      );
    }
  }

  createPage() {
    $('#floor').html('');
    for (let i = 0; i < this.datas[this.vehicle].length; i++) {
      $('#floor').append(
        '<option value=' +
          i +
          '>Floor ' +
          this.datas[this.vehicle][i].floor +
          '</option>'
      );
    }
    this.createBlocks(0);
  }

  createBlocks(index) {
    let self = this;
    var blocks = this.datas[this.vehicle][index].blocks,
      places;
    $('#myTable').html('');

    for (let i = 0; i < blocks.length; i++) {
      $('#myTable').append(
        '<tr class="blockRow" id="' +
          blocks[i].block +
          '" index="' +
          i +
          '"><td id=blockName>' +
          blocks[i].block +
          '</td>'
      );
      places = blocks[i].places;
      for (let j = 0; j < places.length; j++) {
        $('#' + blocks[i].block).append(
          '<td id="' +
            places[j].place_id +
            '" index="' +
            j +
            '"class=\'' +
            places[j].status +
            "'>" +
            places[j].place +
            '</td>'
        );
      }
      $('#myTable').append('</tr>');
    }

    $('.Available').click(function () {
      $('#' + self.placeId).attr('class', 'Available');
      self.placeId = $(this).attr('id');
      self.blockIndex = $(this).parent().attr('index');
      self.placeIndex = $(this).attr('index');

      $('.window').html(
        '<center><br><br>Enter Vehicle no. : <input type="text" id="vehicleNumber" placeholder="Eg., TN 01 AB 1234"><br><br>' +
          '<h1>Note:</h1>' +
          '<p>For Bike: Rs.20 per hour</p>' +
          '<p>For Car: Rs.40 per hour</p>' +
          '<button id="bookButton">Book</button>' +
          '</center>'
      );
      $(this).attr('class', 'selected');
      setTimeout(function () {
        $('.main').css('opacity', '0.5');
        $('.window').fadeIn('200');
      }, 1);
    });
  }

  @action
  changeTable() {
    this.createBlocks($('#floor').val());
  }

  @action
  bookPlace() {
    let self = this;
    var req = new XMLHttpRequest();

    req.onload = function () {
      alert(this.responseText);
      if (this.responseText === 'Place booked successfully') {
        $('.window').css('display', 'none');
        $('.main').css('opacity', '1');
        $('#' + self.placeId).attr('class', 'Unavailable');
        self.datas[self.vehicle][$('#floor').val()].blocks[
          self.blockIndex
        ].places[self.placeIndex].status = 'Unavailable';
        self.placeId = 0;
      }
    };

    req.open('POST', 'http://localhost:8080/Parking-App/book-place', false);
    req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    req.send(
      'action=bookPlace&placeId=' +
        self.placeId +
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
}
