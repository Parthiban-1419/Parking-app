import Component from '@glimmer/component';
import { inject as service } from '@ember/service';
import { action } from '@ember/object';
import { tracked } from '@glimmer/tracking';

export default class RowComponentComponent extends Component {
  @service session;

  isOwner = this.session.data.authenticated.token.role === 'owner';

  @tracked blocks = this.args.row.blocks;

  @action
  addBlock(rowid) {
    let self = this;

    $('.main').css('opacity', '0.2');
    $('.window').css('display', 'initial');
    $('.window').html('<br><br><br><br>');
    $('.window').append(
      '<input type="text" id="newBlock" placeholder="Block name"><br><br>'
    );
    $('.window').append('<button id="addBlock">Add block</button>');

    $('#addBlock').click(function () {
      self.addBlockInDb(rowid);
    });
  }
  addBlockInDb(rowid) {
    let self = this;
    var req = new XMLHttpRequest(),
      responseMessage;

    req.onload = function () {
      responseMessage = JSON.parse(this.responseText);

      if ('success' === responseMessage.status) {
        alert('Block added successfully');
        $('.window').css('display', 'none');
        $('.main').css('opacity', '1');
        self.blocks = [...self.blocks, responseMessage.message];
      }
    };

    req.open('POST', 'http://localhost:8080/Parking-App/add-block', false);
    req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    req.send('block=' + $('#newBlock').val() + '&rowId=' + rowid);
  }
}
