import Route from '@ember/routing/route';
import { inject as service } from '@ember/service';

export default class ApplicationRoute extends Route {
  @service session;

  beforeModel(transition) {
    this.session.requireAuthentication(transition, 'log-in');
  }

  model() {
    $(document).ready(function () {
      $('.main').click(function () {
        $('.main').css('opacity', '1');
        $('.window').css('display', 'none');
      });
    });
  }
}
