import Controller from '@ember/controller';
import { inject as service } from '@ember/service';
import { action } from '@ember/object';
import { tracked } from '@glimmer/tracking';

export default class CustomerController extends Controller {
    @service session;

    @tracked buildings;
    @tracked mode;

    @action
    getData(key, mode){
        var req = new XMLHttpRequest();
        let self = this;
    
        console.log('key : ' + key + " mode : " + mode);

        if(this.session.buildings == undefined){
          req.onload = function () {
            self.buildings = [...JSON.parse(this.responseText)];
            console.log(self.buildings);
          };
          
          req.open('POST', 'http://localhost:8080/Parking-App/get-buildings', false);
          req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
          req.send(
            'name=' +
              self.session.data.authenticated.token.loginName +
              '&role= ' +
              self.session.data.authenticated.token.role + 
              '&sort=' + key + 
              '&mode=' + mode
          );
        }
      }

      @action
      filterData(self, mode){
        var key = 'nothing';

        $('[mode=' + mode + ']').css('background-color', 'white');
        if(self.mode != mode){
            key = 'name';
            self.mode = mode;
            $('[mode=' + mode + ']').css('background-color', 'rgb(193, 223, 250)');
            $('[mode=' + !mode + ']').css('background-color', 'white');
        }
        self.getData(key, mode);
      }
}
