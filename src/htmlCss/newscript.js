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
    messageList = loadMessages() || [newMessage('Dasha')];
    usernameNow = messageList[messageList.length - 1].username;
    document.getElementById('enter_name_box').setAttribute('placeholder', usernameNow);
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
    if (textNewMessage.value) {
        var message = newMessage(usernameNow, textNewMessage.value);
        var imaginaryUsername = messageList.pop();
        messageList.push(message);
        messageList.push(imaginaryUsername);
        textNewMessage.value = '';
        renderMessage(message); // отрисовка
        saveMessages(messageList);
    }
}

function onDeleteIconClick(element) {
    var index = indexByElement(element.parentElement, messageList);
    var deletedMessage = messageList[index];
    deletedMessage.indDelete = true;
    deletedMessage.textMessage = '';
    renderMessageState(element.parentElement, messageList[index]);
    if (element.childNodes.length == 6  && element.lastElementChild.style.display == 'block')
        element.lastElementChild.style.display = 'none';
    saveMessages(messageList);
}

function onEditIconClick(element) {
    var boxForEdit = element.lastElementChild;
    if (boxForEdit.style.display == 'block')
        return;
    boxForEdit.style.display = 'block';
    boxForEdit.firstElementChild.innerHTML = element.firstElementChild.innerHTML;
    boxForEdit.lastElementChild.firstElementChild.addEventListener('click', function() {
        onEditButtonSent(element);
    });
}

function onEditButtonSent(element) {
    var boxForEdit = element.lastElementChild;
    var index = indexByElement(element.parentElement, messageList);
    var editedMessage = messageList[index];
    if(element.lastElementChild.firstElementChild.value) {
        editedMessage.indEdit = true;
        editedMessage.textMessage = element.lastElementChild.firstElementChild.value;
        renderMessageState(element.parentElement, editedMessage);
        boxForEdit.style.display = 'none';
        saveMessages(messageList);
    }
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
    for (var i = 0; i < messages.length - 1; i++) {
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
    listToSave[listToSave.length - 1].username = usernameNow;
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