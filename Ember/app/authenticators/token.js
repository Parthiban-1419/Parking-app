import Base from 'ember-simple-auth/authenticators/base';

export default class Token extends Base {
  async restore(data) {
    let { token } = data;
    if (token) {
      return data;
    } else {
      return { token: null };
    }
  }

  async authenticate(loginName, password) {
    var json,
      req = new XMLHttpRequest();

    req.onload = function () {
      console.log(this.responseText);
      json = JSON.parse(this.responseText);
    };

    req.open('POST', 'http://localhost:8080/Parking-App/log-in', false);
    req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    req.send('name=' + loginName + '&password=' + password);

    if (json.status === 'success') {
      return { token: json.message };
    } else {
      throw new Error(json.message);
    }
  }

  async invalidate(data) {}
}
