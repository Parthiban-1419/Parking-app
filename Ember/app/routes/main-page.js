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
      $('.main').click(function () {
        $('.main').css('opacity', '1');
        $('.window').css('display', 'none');
        $('#bookButton').click(function () {
          controller.bookPlace();
        });
      });
      controller.set('buildingName', self.buildingName);
      controller.getData('bike');
    });
  };
}
