import Component from '@glimmer/component';
import { action } from '@ember/object';
import { inject as service } from '@ember/service';

export default class BuildingsComponent extends Component {
  @service session;
  @service router;

  isOwner = 'owner' === this.session.data.authenticated.token.role;

  @action
  getFloors(buildingName) {
    if (!this.isOwner) this.router.transitionTo('main-page', buildingName);
    console.log(buildingName);
  }
}
