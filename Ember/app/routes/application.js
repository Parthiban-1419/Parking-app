import Route from '@ember/routing/route';
import { inject as service } from '@ember/service';

export default class ApplicationRoute extends Route {
  @service session;

  beforeModel(transition) {
    this.session.requireAuthentication(transition, 'log-in');
  }

  // model(){
  //   var req = new XMLHttpRequest();
  //   let self = this;

  //   if(this.session.isAuthenticated){
  //     req.onload = function () {
  //       self.session.buildings = JSON.parse(this.responseText);
  //       console.log(self.session.buildings);
  //     };

  //     req.open('POST', 'http://localhost:8080/Parking-App/get-buildings', false);
  //     req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
  //     req.send(
  //       'name=' +
  //         self.session.data.authenticated.token.loginName +
  //         '&role= ' +
  //         self.session.data.authenticated.token.role
  //     );
  //   }
  // }
}
