import Route from '@ember/routing/route';
import { action } from '@ember/object';
import { inject as service } from '@ember/service';
import { tracked } from '@glimmer/tracking';

export default class OwnerRoute extends Route {
  @service session;
  @service myService;
  @service router;

  @tracked building;

  model() {
    if (this.session.data.authenticated.token.role != 'owner') {
      this.router.transitionTo('error');
    }

    var req = new XMLHttpRequest();
    let self = this;

    req.onload = function () {
      console.log(this.responseText);
      self.session.buildings = JSON.parse(this.responseText);
    //   self.myService.buildings = JSON.parse(this.responseText);
      console.log(self.session.buildings);
    };

    req.open('POST', 'http://localhost:8080/Parking-App/get-buildings', false);
    req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    req.send(
      'name=' +
        self.session.data.authenticated.token.loginName +
        '&role=' +
        self.session.data.authenticated.token.role
    );

    return self.session.buildings;
  }

  @action
  getBuilding() {}
}
