/**
 * Created by Dasha on 08.03.2016.
 */
window.onload = function () {
  //  document.getElementsByClassName('history').scrollTo(0, document.body.scrollHeight);
    var editMessage = false;
    var editLink;
    var username = 'Dasha';

    var sent_button = document.getElementById('button_sent');
    sent_button.onclick = process_sent;

    function process_sent() {
        editMessage ? edit_after_sent() : add_new_message();
    }

    function add_new_message() {
        var text_message = document.getElementById('new_message').value;
        if (text_message) {
            var messageTemplate = '<div class="all_my_message">' +
                '<div class="username">' + username + '</div>' +
                '<div class="my_message">' +
                '<div class="text_message"></div>' +
                '<span>' +
                '<a href="#" class="edit_message"><img src="edit.ico" /></a>' +
                '<a href="#" class="delete_message"><img src="delete.ico" /></a>' +
                '<span class="server_message"></span>' +
                '</span>' +
                '</div>' +
                '</div>';
            var div = document.getElementsByClassName('history');
            div[0].insertAdjacentHTML('beforeend', messageTemplate);
            var mess = document.getElementsByClassName('text_message');
            mess[mess.length - 1].innerHTML = text_message;
            document.getElementById('new_message').value = '';
            var edit_but = document.getElementsByClassName('edit_message');
            edit_but[edit_but.length - 1].onclick = edit_message;
            var delete_but = document.getElementsByClassName('delete_message');
            delete_but[delete_but.length - 1].onclick = delete_message;
        }
    }


    var delete_but = document.getElementsByClassName('delete_message');
    for (var i = 0; i < delete_but.length; i++) {
        delete_but[i].onclick = delete_message;
    }

    function delete_message() {
        this.previousElementSibling.remove(); //удалили ссылку на редактирование
        this.nextElementSibling.innerHTML = 'Deleted'; //системное сообщение
        var spanh = this.parentElement; // внешний спан
        this.remove(); // удаляем ссылку с делете
        spanh.previousElementSibling.remove(); //удаляем текст_месс
        return false;
    }

    var edit_but = document.getElementsByClassName('edit_message');
    for (var i = 0; i < edit_but.length; i++) {
        edit_but[i].onclick = edit_message;
    }

   function edit_message () {
       editLink = this;
       editMessage = true;
       var spanh = this.parentElement;
       var t_mh = spanh.previousElementSibling;
       var text_box = document.getElementById('new_message');
       text_box.value = t_mh.innerHTML;
    }

    function edit_after_sent () {
        var text_box = document.getElementById('new_message');
        if (text_box) {
            editLink.nextElementSibling.nextElementSibling.innerHTML = 'Edited';
            var spanh = editLink.parentElement;
            var t_mh = spanh.previousElementSibling;
            t_mh.innerHTML = text_box.value;
            document.getElementById('new_message').value = '';
            editMessage = false;
        }
    }

    var enter_button = document.getElementById('enter_button');
    enter_button.onclick = enter_new_name;

    function  enter_new_name () {
        username = this.previousElementSibling.value;
        this.previousElementSibling.value = '';
    }
}