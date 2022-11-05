import Component from '@glimmer/component';
import { action } from '@ember/object';
import { inject as service } from '@ember/service';
import { tracked } from '@glimmer/tracking';

export default class CustomerComponentComponent extends Component {
  @service router;
  @service session;

  @tracked data;
  @tracked bill;
  @tracked isTracked;

  @action
  goHome() {
    this.router.transitionTo('customer');
  }

  @action
  book() {
    this.router.transitionTo('main-page');
  }

  getUserDetail() {
    var req = new XMLHttpRequest();
    let self = this;

    req.onload = function () {
      console.log(this.responseText);
      self.data = JSON.parse(this.responseText);
      console.log(self.data);
    };

    req.open('POST', 'http://localhost:8080/Parking-App/vehicle-detail', false);
    req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    req.send('name=' + self.session.data.authenticated.token.loginName);

    if (self.data.length > 0) {
      $(document).ready(function () {
        for (let i = 0; i < self.data.length; i++)
          $('.trackMenu').append(
            '<div class="vehicle" id="' +
              i +
              '">' +
              self.data[i].vehicleNumber +
              '</div>'
          );
        $('.vehicle').hover(
          function () {
            if ($(this).attr('id') != $('.vehicleInfo').attr('vehicle'))
              $(this).css('background-color', 'rgb(200, 216, 216)');
          },
          function () {
            if ($(this).attr('id') != $('.vehicleInfo').attr('vehicle'))
              $(this).css('background-color', 'rgb(215, 241, 248)');
          }
        );
        $('.vehicle').click(function () {
          self.createWindow($(this).attr('id'));
        });
      });
    } else self.bill = 0;
  }

  createWindow(index) {
    var self = this;
    var bill = parseInt(
      40 * ((Date.now() - self.data[index].entryTime) / 3600000)
    );

    console.log('index : ' + index);
    // $('.trackMenu #' + $('.vehicleInfo').attr('vehicle')).css('background-color', 'rgb(215, 241, 248)').css('border-top-right-radius', '40px').css('border-bottom-right-radius', '40px');
    $('.vehicle')
      .css('background-color', 'rgb(215, 241, 248)')
      .css('border-top-right-radius', '40px')
      .css('border-bottom-right-radius', '40px');
    $('.trackMenu #' + index)
      .css('background-color', 'aqua')
      .css('border-top-right-radius', 'initial')
      .css('border-bottom-right-radius', 'initial');
    $('.vehicleInfo').attr('vehicle', index);
    $('.vehicleInfo').html('<br>');
    if (self.isTracked) {
      $('.vehicleInfo').append(
        '<p>Your ' +
          self.data[index].type +
          ' is parked Floor : ' +
          self.data[index].floor +
          ', Block : ' +
          self.data[index].block +
          ', Place : ' +
          self.data[index].place +
          '</p><br><br>'
      );
      $('.vehicleInfo').append(
        '<p>Amount to be paid till now : Rs.' + bill + '</p>'
      );
    } else {
      $('.vehicleInfo').append(
        '<input type="number" id="accountNumber" placeholder="Account number"><br><br>' +
          '<input type="text" id="ifsc" placeholder="IFSC code"><br><br>' +
          '<input type="text" id="name" placeholder="Account holder name"><br><br>' +
          '<input type="text" id="billAmount" disabled style="background-color: white"  placeholder="Amount Rs.' +
          bill +
          '"><br><br>' +
          '<button id="pay">Pay</button>'
      );
      $('#pay').click(function () {
        self.pay();
      });
    }
  }

  @action
  trackVehicle() {
    let self = this;
    this.isTracked = true;

    $('.window').html(
      '<div class="trackMenu"><br>Your vehicles<br><br>' +
        '</div><div class="vehicleInfo" vehicle="0"></div>'
    );
    this.getUserDetail();
    setTimeout(function () {
      $('.main').css('opacity', '0.5');
      $('.window').css('display', 'initial');
      if (self.data.length > 0) {
        self.createWindow(0);
      } else {
        $('.vehicleInfo').append('<br><br><br><p>Nothing to track : )</p');
      }
    }, 1);
  }

  @action
  payBill() {
    let self = this;
    this.isTracked = false;

    $('.window').html(
      '<div class="trackMenu"><br>Your vehicles<br><br>' +
        '</div><div class="vehicleInfo" vehicle="0"></div>'
    );
    this.getUserDetail();
    setTimeout(function () {
      $('.main').css('opacity', '0.5');
      $('.window').css('display', 'initial');
      $('.vehicleInfo').html('<br>');
      if (self.data.length > 0) {
        self.createWindow(0);
      } else {
        $('.vehicleInfo').append('<p>Nothing to pay : )</p');
      }
    }, 1);
  }


  pay() {
    var req = new XMLHttpRequest();

    req.onload = function () {
      alert(this.responseText);
      if (this.responseText === 'Paid successfully') {
        $('.window').css('display', 'none');
        $('.main').css('opacity', '1');
      }
    };

    req.open('POST', 'http://localhost:8080/Parking-App/pay-bill', true);
    req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    req.send(
      'accountNumber=' +
        $('#accountNumber').val() +
        '&ifsc=' +
        $('#ifsc').val() +
        '&name=' + 
        $('#name').val() +
        '&placeId=' +
        1
    );
  }
}
