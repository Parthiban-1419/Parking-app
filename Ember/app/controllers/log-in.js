import Controller from '@ember/controller';
import { action } from '@ember/object';
import { inject as service } from '@ember/service';

export default class LogInController extends Controller {
  @service router;
  @service session;

  @action
  async logIn() {
    var loginName = $('#userId').val();
    var password = $('#password').val();

    try {
      await this.session.authenticate(
        'authenticator:token',
        loginName,
        password
      );
    } catch (Error) {
      alert(Error);
    }
  }
}
