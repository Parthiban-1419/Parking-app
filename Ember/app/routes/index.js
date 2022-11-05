import Route from '@ember/routing/route';
import { inject as service } from '@ember/service';

export default class IndexRoute extends Route {
  @service session;
  @service router;

  model() {
    var role = this.session.data.authenticated.token.role;

    if (role == 'customer') this.router.transitionTo('customer');
    else if (role == 'owner') this.router.transitionTo('owner');
  }
}
