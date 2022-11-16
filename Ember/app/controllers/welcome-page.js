import Controller from '@ember/controller';
import { action } from '@ember/object';
import { tracked } from '@glimmer/tracking';
import { inject as service } from '@ember/service';

export default class WelcomePageController extends Controller {
  @service session;
  @service myService;
  @tracked ind;
  @tracked arr = [1, 2, 3];

  @tracked demo = { bikes: [0, 1, 2] };

  @tracked i = 2;

  @action
  add() {
    this.arr = [...this.arr, 1];
    this.myService.buildings = [...this.myService.buildings, 1];

    // this.ind = this.arr.length;
    // this.i = this.arr.length;
    // this.arr[this.i] = this.ind;
    // this.i++ ;
  }
}
