import Component from '@glimmer/component';
import { inject as service } from '@ember/service';
import { tracked } from '@glimmer/tracking';

export default class FloorComponentComponent extends Component {
  @service session;

  @tracked rows = this.args.floor.rows;

  isOwner = this.session.data.authenticated.token.role === 'owner';

  addRow(floorId) {
    if (confirm('Are you sure, Do you want to add row?') == true) {
      var self = this,
        req = new XMLHttpRequest(),
        responseMessage,
        row = 0;

      if (self.rows.length > 0)
        row = parseInt(self.rows[self.rows.length - 1].row);

      req.onload = function () {
        responseMessage = JSON.parse(this.responseText);
        console.log(responseMessage);

        if ('success' === responseMessage.status) {
          alert('Row added successfully');
          $('.window').css('display', 'none');
          self.rows = [...self.rows, responseMessage.message];
          console.log(self.args.floor.rows);
        }
      };

      req.open('POST', 'http://localhost:8080/Parking-App/add-row', false);
      req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
      req.send('floorId=' + floorId + '&row=' + (row + 1));
    }
  }

  // addBlock(floorId, floorIndex){
  //     let self = this;
  //     var req = new XMLHttpRequest(), responseMessage;

  //     req.onload = function(){
  //         responseMessage = JSON.parse(this.responseText);

  //         if('success' === responseMessage.status){
  //             alert('Block created successfully');
  //             $('.window').css('display', 'none');
  //             self.rows = [...self.rows, responseMessage.message];
  //             console.log(self.args.floor.rows);
  //         }
  //     }

  //     req.open('POST', 'http://localhost:8080/Parking-App/add-block', false);
  //     req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
  //     req.send('block=' + $('#newBlock').val() + '&floorId=' + floorId + '&row=' + (self.rows[self.rows.length-1].row + 1));
  // }
}
