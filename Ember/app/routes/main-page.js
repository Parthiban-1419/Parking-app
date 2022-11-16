import Route from '@ember/routing/route';
import { inject as service } from '@ember/service';

export default class MainPageRoute extends Route {
  @service session;
  @service router;

  buildingName;

  model(param) {
    const { buildingName } = param;
    this.buildingName = buildingName;
  }

  setupController = function (controller) {
    let self = this;
    $(document).ready(function () {
      // $('.Restricted').click(function(){
      //   if(self.session.data.authenticated.token.role === 'customer'){
      //     alert('Restricted place. Unable to book');
      //   }
      //   else{
      //     controller.freePlace($(this));
      //   }
      // });
      $('.image:first').css('background-color', 'aqua');
    });
    controller.set('buildingName', self.buildingName);
    controller.getData();
  };
}
