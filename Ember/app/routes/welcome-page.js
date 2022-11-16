import Route from '@ember/routing/route';
import { inject as service } from '@ember/service';

export default class WelcomePageRoute extends Route {
  @service session;
  @service myService;

  beforeModel(transition) {
    this.session.requireAuthentication(transition, 'log-in');
  }

  model() {
    this.myService.buildings = [1, 2, 3];
    //  this.myService.buildings = [{a:1}, {a:2}, {a:3}];
    //  console.log(this.myService.buildings);
  }

  setupController = function (controller) {
    $(document).ready(function () {
      $('.contents').append('1');
      console.log('1');
    });
    for (let i = 0; i < 100; i++) $('.contents').append('0');
    console.log('0');
  };
}
