import Route from '@ember/routing/route';
import { inject as service } from '@ember/service';
export default class CustomerRoute extends Route {
  @service session;
  @service router;

  model() {
    if (this.session.data.authenticated.token.role != 'customer') {
      this.router.transitionTo('error');
    }
    var req = new XMLHttpRequest();
    let self = this;

    req.onload = function () {
      self.session.buildings = JSON.parse(this.responseText);
      console.log(self.session.buildings);
    };

    req.open('POST', 'http://localhost:8080/Parking-App/get-buildings', false);
    req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    req.send(
      'name=' +
        self.session.data.authenticated.token.loginName +
        '&role= ' +
        self.session.data.authenticated.token.role
    );

    return this.session.buildings;
  }
}
