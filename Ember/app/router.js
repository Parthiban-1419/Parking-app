import EmberRouter from '@ember/routing/router';
import config from 'parking-app/config/environment';

export default class Router extends EmberRouter {
  location = config.locationType;
  rootURL = config.rootURL;
}

Router.map(function () {
  this.route('create-account');
  this.route('main-page', { path: 'main-page/:buildingName' });
  this.route('welcome-page');
  this.route('log-in');
  this.route('track-vehicle');
  this.route('customer');
  this.route('owner');
  this.route('add-building');
  this.route('error', { path: '/*path' });
});
