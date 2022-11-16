import Component from '@glimmer/component';
import { action } from '@ember/object';
import { inject as service } from '@ember/service';

export default class BuildingsComponent extends Component {
  @service session;
  @service router;

  buildingName;
  isOwner = 'owner' === this.session.data.authenticated.token.role;

  @action
  selectBuilding(buildingName) {
    this.session.vehiclePrices = this.args.building.vehiclePrices;
    this.router.transitionTo('main-page', buildingName);
  }

  getFloors() {
    var req = new XMLHttpRequest();

    req.onload = function () {
      console.log(this.responseText);
    };

    req.open('POST', 'http://localhost:8080/Parking-App/get-floor', false);
    req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    req.send('buildingName=' + this.buildingName);
  }
}
