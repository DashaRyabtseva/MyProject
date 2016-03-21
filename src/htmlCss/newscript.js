/**
 * Created by Dasha on 17.03.2016.
 */

function newMessage (username, text, edit, del) {
    return {
        username: username,
        textMessage: text,
        indEdit: !! edit,
        indDelete: !! del,
        id:'' + uniqueId(),
        my: true,
        timeStamp: Date.now()
    };
}
var usernameNow = 'Dasha';
var messageList = [];


function uniqueId() {
    var date = Date.now();
    var random = Math.random()*Math.random();
    return Math.floor(date*random);
}

function run() {
    var appContainer = document.getElementsByClassName('todos')[0];
    appContainer.addEventListener('click', delegateEvent);
    messageList = loadMessages() || [newMessage('Dasha', 'hello')];
    usernameNow = messageList[messageList.length - 1].username;
    document.getElementById('enter_name_box').setAttribute('placeholder', usernameNow);
    messageList.pop();
    render(messageList);
}

function delegateEvent(evtObj) {
    if(evtObj.type === 'click' && evtObj.target.getAttribute('id') ==  'button_sent')
        onAddButtonClick();
    if(evtObj.type === 'click' && evtObj.target.classList.contains('delete_icon'))
        onDeleteIconClick(evtObj.target.parentElement.parentElement.parentElement);
    if(evtObj.type === 'click' && evtObj.target.classList.contains('edit_icon'))
        onEditIconClick(evtObj.target.parentElement.parentElement.parentElement);
    if(evtObj.type === 'click' && evtObj.target.getAttribute('id') ==  'enter_name_button')
        onEnterNameButtonClick(evtObj.target.parentElement);
}

function onAddButtonClick() {
    var textNewMessage = document.getElementById('new_message');
    var message = newMessage(usernameNow,textNewMessage.value);
    messageList.push(message);
    textNewMessage.value = '';
    render([message]); // отрисовка
    saveMessages(messageList);
}

function onDeleteIconClick(element) {
    var index = indexByElement(element.parentElement, messageList);
    var deletedMessage = messageList[index];
    deletedMessage.indDelete = true;
    deletedMessage.textMessage = '';
    renderMessageState(element.parentElement, messageList[index]);
    saveMessages(messageList);
}

function onEditIconClick(element) {
    element.insertAdjacentHTML('beforeend', '<div id="for_edit"><textarea></textarea> <span id="button_edit_sent"><button class="button" >Sent</button></span> </div>');
    var boxForEdit = element.lastElementChild;
    boxForEdit.firstElementChild.innerHTML = element.firstElementChild.innerHTML;
    boxForEdit.lastElementChild.firstElementChild.addEventListener('click', function() {
        onEditButtonSent(element);
    });
}

function onEditButtonSent(element) {
    var boxForEdit = element.lastElementChild;
    var index = indexByElement(element.parentElement, messageList);
    var editedMessage = messageList[index];
    editedMessage.indEdit = true;
    editedMessage.textMessage = element.lastElementChild/*boxForEdit*/.firstElementChild.value;
    renderMessageState(element.parentElement, editedMessage);
    boxForEdit.parentElement.removeChild(boxForEdit);
    saveMessages(messageList);
}

function onEnterNameButtonClick (element) {
    var newNameBox = element.firstElementChild.nextElementSibling;
    usernameNow = newNameBox.value;
    newNameBox.setAttribute('placeholder', usernameNow);
    newNameBox.value = '';
    saveMessages(messageList);
}

function indexByElement(element, messages){
    var id = element.getAttribute('data-task-id');//attributes['data-task-id'].value;
    for (var i = 0; i < messages.length; ++i) {
        if(messages[i].id == id)
            return i;
    }
}

function render(messages) { //общая отрисовочка!
    for (var i = 0; i < messages.length; i++) {
        renderMessage(messages[i]);
    }
}

function renderMessage (message) {//отрисовка одного сообщения
    var items = document.getElementById('history');
    var element = elementFromTemplate();
    renderMessageState(element, message);
    items.appendChild(element);
    document.getElementById('history').scrollTop = document.getElementById('history').scrollHeight;
}

function renderMessageState(element, message){ //элемент - отображение в html, message - объект из js
    var userName =  element.firstElementChild;
    var myMessage = userName.nextElementSibling;
    var textMessage = myMessage.firstElementChild;
    var status = textMessage.nextElementSibling.firstElementChild.nextElementSibling.nextElementSibling;
    userName.innerHTML = message.username;
    if (message.indEdit)
        status.innerHTML = 'Edited';
    if (message.indDelete) {
        status.innerHTML = 'Deteled';
        textMessage.remove(); //parentElement.removeChild(textMessage);
        status.previousElementSibling.remove(); // удаляем иконки
        status.previousElementSibling.remove();
    }
    else
        textMessage.innerHTML = message.textMessage; //текст, если сообщение не удалено
    if(!message.my) {
        status.previousElementSibling.remove(); // удаляем иконки
        status.previousElementSibling.remove();
    }
    element.setAttribute('data-task-id', message.id);
    element.style.display = 'block';
    document.getElementById('history').scrollTop = document.getElementById('history').scrollHeight;
}

function elementFromTemplate() {
    var template = document.getElementById("message_template");
    return template.cloneNode(true);
}

function saveMessages(listToSave) {
    if(typeof(Storage) == "undefined") {
        alert('localStorage is not accessible');
        return;
    }
    listToSave.push(newMessage(usernameNow));
    localStorage.setItem ("TODOs messagesList", JSON.stringify(listToSave));
}

function loadMessages() {
    if(typeof(Storage) =="undefined") {
        alert('localStorage is not accessible');
        return;
    }
    var item = localStorage.getItem("TODOs messagesList");
    return item && JSON.parse(item);
}