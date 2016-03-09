/**
 * Created by Dasha on 08.03.2016.
 */
window.onload = function () {
    var set_button = document.getElementById('button_sent');
    set_button.onclick = add_new_message;

    function add_new_message() {
        var text_message = document.getElementById('new_message').value;
        var messageTemplate = '<div class="all_my_message">' +
            '<div class="username"> Dasha </div>' +
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
        return false;
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
    edit_but.onclick = edit_message;

   /* function edit_message () { // в процессе
        var spanh = this.parentElement();
        var t_mh = spanh.previousSibling;
    }*/
}