import Controller from '@ember/controller';
import { action } from '@ember/object';
import { inject as service } from '@ember/service';

export default class CreateAccountController extends Controller {
  @service router;
  @service session;

  @action
  createAccount() {
    let self = this;
    var req = new XMLHttpRequest();

    var logInName = $('#logInName').val();
    var firstName = $('#firstName').val();
    var middleName = $('#middleName').val();
    var role = $('#role').val();
    var lastName = $('#lastName').val();
    var password = $('#password').val();
    var cPassword = $('#cPassword').val();

    if (password === cPassword) {
      req.onload = function () {
        console.log(this.responseText);
        alert(this.responseText);
        if (this.responseText === 'Account created successfully...') {
          // self.router.transitionTo('log-in');
          self.login(logInName, password);
        }
      };

      req.open(
        'POST',
        'http://localhost:8080/Parking-App/create-account',
        true
      );
      req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
      req.send(
        'logInName=' +
          logInName +
          '&firstName=' +
          firstName +
          '&middleName=' +
          middleName +
          '&lastName=' +
          lastName +
          '&role=' +
          role +
          '&password=' +
          password
      );
    } else alert('Password miss match');
  }

  @action
  checkUserName() {
    var req = new XMLHttpRequest();
    var name = $('#userName').val();

    req.open('POST', 'http://localhost:8080/Parking-app/create-account', false);
    req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    req.send('name=' + name);
  }

  async login(logInName, password) {
    try {
      await this.session.authenticate(
        'authenticator:token',
        logInName,
        password
      );
    } catch (Error) {
      alert(Error);
    }
  }
}
