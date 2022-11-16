import Component from '@glimmer/component';
import { inject as service } from '@ember/service';
import { action } from '@ember/object';
import { tracked } from '@glimmer/tracking';

export default class BlockComponentComponent extends Component {
  @service session;

  @tracked places = this.args.block.places;

  isOwner = this.session.data.authenticated.token.role === 'owner';

  @action
  addPlace(blockId) {
    let self = this;

    $('.main').css('opacity', '0.2');
    $('.window').css('display', 'initial');
    $('.window').html('<br><br><br><br>');
    $('.window').append(
      '<input type="number" id="newPlace" placeholder="Place number"><br><br>'
    );
    $('.window').append('<button id="addBlock">Add place</button>');

    $('#addBlock').click(function () {
      self.addPlaceInDb(blockId);
    });
  }

  addPlaceInDb(blockId) {
    var req = new XMLHttpRequest(),
      self = this,
      responseMessage;

    console.log(blockId);

    req.onload = function () {
      responseMessage = JSON.parse(this.responseText);
      if (responseMessage.status === 'success') {
        self.places = [...self.places, responseMessage.message];
        $('.main').css('opacity', '1');
        $('.window').css('display', 'none');
        alert('Place added successfully');
      }
    };

    req.open('POST', 'http://localhost:8080/Parking-App/add-place', false);
    req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    req.send('blockId=' + blockId + '&place=' + $('#newPlace').val());
  }
}
