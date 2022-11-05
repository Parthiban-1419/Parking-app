import Service from '@ember/service';
import { tracked } from '@glimmer/tracking';

export default class MyServiceService extends Service {
    @tracked buildings;
}
