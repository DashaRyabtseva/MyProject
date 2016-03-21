/**
 * Created by Dasha on 08.03.2016.
 */
window.onload = function () {
    //window.scrollTo(0, document.body.scrollHeight);
   //window.scrollTo(0, document.getElementsByClassName('history')[0].scrollHeight);
   // scrollTo(0, document.getElementsByClassName('history').scrollHeight);
    document.getElementById('history').scrollTop = document.getElementById('history').scrollHeight;
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
                '<a href="#" class="edit_icon"><img src="edit.ico" alt="edit"/ ></a>' +
                '<a href="#" class="delete_icon"><img src="delete.ico" alt="del"/></a>' +
                '<span class="server_message"></span>' +
                '</span>' +
                '</div>' +
                '</div>';
            var div = document.getElementById('history');
            div.insertAdjacentHTML('beforeend', messageTemplate);
            var mess = document.getElementsByClassName('text_message');
            mess[mess.length - 1].innerHTML = text_message;
            document.getElementById('new_message').value = '';
            var edit_but = document.getElementsByClassName('edit_icon');
            edit_but[edit_but.length - 1].onclick = edit_message;
            var delete_but = document.getElementsByClassName('delete_icon');
            delete_but[delete_but.length - 1].onclick = delete_message;
            document.getElementById('history').scrollTop = document.getElementById('history').scrollHeight;

        }
    }


    var delete_but = document.getElementsByClassName('delete_icon');
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

    var edit_but = document.getElementsByClassName('edit_icon');
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
       spanh.parentElement.insertAdjacentHTML('beforeend', '<div><textarea></textarea> <span id="button_sent"><button class="button" >Sent</button></span> </div>');
    }

    function edit_after_sent () {
        var text_box = document.getElementById('new_message');
        editMessage = false;
        if (text_box.value) {
            editLink.nextElementSibling.nextElementSibling.innerHTML = 'Edited';
            var spanh = editLink.parentElement;
            var t_mh = spanh.previousElementSibling;
            t_mh.innerHTML = text_box.value;
            spanh.parentElement.removeChild(spanh.nextElementSibling);
            document.getElementById('new_message').value = '';
            document.getElementById('history').scrollTop = document.getElementById('history').scrollHeight;

        }
    }

    var enter_button = document.getElementById('enter_button');
    enter_button.onclick = enter_new_name;

    function  enter_new_name () {
        username = this.previousElementSibling.value;
       // this.previousElementSibling.value = '';
    }
}