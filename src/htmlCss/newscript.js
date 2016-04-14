function newMessage (username, text, userID, edit, del) {
    return {
        username: username,
        userID: userID,
        textMessage: text,
        indEdit: !! edit,
        indDelete: !! del,
        id:'' + uniqueId(),
        timeStamp: Date.now()
    };
}

function uniqueId() {
    var date = Date.now();
    var random = Math.random()*Math.random();
    return Math.floor(date*random);
}

var Application = {
    mainUrl : 'http://localhost:1555/chat',
    messageList : [],
    usernameNow : 'Dasha',
    userID : '',
    token : 'TE11EN'
};

function run() { //пока не залогинишься  - истории не будет))))
    var appContainer = document.getElementsByClassName('bugchat')[0];
    appContainer.addEventListener('click', delegateEvent);
}

function delegateEvent(evtObj) {
    if(evtObj.type === 'click' && evtObj.target.getAttribute('id') ==  'button_sent')
        onAddButtonClick();
    if(evtObj.type === 'click' && evtObj.target.classList.contains('delete_icon'))
        onDeleteIconClick(evtObj.target.parentElement.parentElement.parentElement.parentElement);
    if(evtObj.type === 'click' && evtObj.target.classList.contains('edit_icon'))
        onEditIconClick(evtObj.target.parentElement.parentElement.parentElement);
    if(evtObj.type === 'click' && evtObj.target.getAttribute('id') ==  'enter_name_button')
        onEnterNameButtonClick(evtObj.target.parentElement);
}

function onEnterNameButtonClick (element) {//логинишься типа
    var newNameBox = element.firstElementChild.nextElementSibling;
    Application.usernameNow = newNameBox.value;
    newNameBox.setAttribute('placeholder', Application.usernameNow);
    newNameBox.value = '';
    Application.messageList = loadMessages(function () {
        render(Application);
    });
}

function onAddButtonClick() {
    var textNewMessage = document.getElementById('new_message');
    if (textNewMessage.value) {
        var message = newMessage(Application.usernameNow, textNewMessage.value, Application.userID);
        messageList.push(message);
        textNewMessage.value = '';
        addMessage(message, function() {
            render(Application);
        });
    }
}

function onDeleteIconClick(element) { //tag one_message
    var index = indexByElement(element, Application.messageList);
    var deletedMessage = Application.messageList[index];
    deletedMessage.indDelete = true;
    deletedMessage.textMessage = '';
    var id = idFromElement(element);
    deleteMessage(id, function() {
        render(Application);
    });
    //проветить как работает
    if (element.children[1].childNodes.length == 6  && element.children[1].lastElementChild.style.display == 'block') //если edit не закрыли
        element.children[1].lastElementChild.style.display = 'none';
}

function onEditIconClick(element) { // tag my_message
    var boxForEdit = element.lastElementChild;
    if (boxForEdit.style.display == 'block')
        return;
    boxForEdit.style.display = 'block';
    boxForEdit.firstElementChild.innerHTML = element.firstElementChild.innerHTML;
    boxForEdit.lastElementChild.firstElementChild.addEventListener('click', function() {
        onEditButtonSent(element);
    });
    boxForEdit.lastElementChild.lastElementChild.addEventListener('click', function() {
        onEditButtonCancel(element);
    });
}

function onEditButtonSent(element) { //tag my_message
    var boxForEdit = element.lastElementChild;
    var text = boxForEdit.firstElementChild.value;
    if (text != null) {
        var index = indexByElement(element.parentElement, Application.messageList);
        var editedMessage = Application.messageList[index];
        editedMessage.indEdit = true;
        editedMessage.textMessage = text;
        var id = idFromElement(element.parentElement);
        editMessage(id, text, function () {
            boxForEdit.style.display = 'none';
            render(Application);
        });
    }
}

function onEditButtonCancel(element) {
    var boxForEdit = element.lastElementChild;
    boxForEdit.style.display = 'none';
}

function idFromElement(element){
    return element.getAttribute('data-task-id');
}

function loadMessages(done) {
    var url = Application.mainUrl + '?token=' + Application.token;
    ajax('GET', url, null, function(responseText){
        var response = JSON.parse(responseText);
        Application.messageList = response.messages;
        Application.token = response.token;
        Application.userID = response.userID;
        done();
    });
}

function addMessage(message, done) {
    ajax('POST', Application.mainUrl, JSON.stringify(message), function(){
        Application.messageList.push(message);
        done();
    });
}

function deleteMessage(id, done) {
    var index = indexById(Application.messageList, id);
    var tempMessage = Application.messageList[index];
    var messageToDelete = {
        id:tempMessage.id
    };

    ajax('PUT', Application.mainUrl, JSON.stringify(messageToDelete), function(){
        tempMessage.textMessage = '';
        done();
    });
}

function editMessage(id, text, done) {
    var index = indexById(Application.messageList, id);
    var tempMessage = Application.messageList[index];
    var messageToEdit = {
        id: tempMessage.id,
        textMessage: text,
    };
    ajax('PUT', Application.mainUrl, JSON.stringify(messageToEdit), function () {
        tempMessage.textMessage = text;
        done();
    })
}

function indexByElement(element, messages){
    var id = element.getAttribute('data-task-id');//attributes['data-task-id'].value;
    for (var i = 0; i < messages.length; ++i) {
        if(messages[i].id == id)
            return i;
    }
}

function render(app) { //общая отрисовочка!
    var items = document.getElementById('history');
    var messagesMap = app.messageList.reduce(function(accumulator, message) {
        accumulator[message.id] = message;
        return accumulator;
    },{});
    updateList(items, messagesMap);
    appendToList(items, app.messageList, messagesMap);
    document.getElementById('history').scrollTop = document.getElementById('history').scrollHeight;
}

function updateList(element, itemMap) { //перерисовываем что есть
    var children = element.children;
    for(var i = 0; i < children.length; i++) {
        var child = children[i];
        var id = child.attributes['data-task-id'].value;
        var item = itemMap[id];
        /*if(item == null) { // если бы удаляли. но мы не удаляем
            notFound.push(child);
            continue;
        }*/
        renderMessageState(child, item, Application.userID); // переделать же
        itemMap[id] = null;
    }
}

function appendToList(element, items, itemMap) { //добавояем новые
    for(var i = 0; i < items.length; i++) {
        var item = items[i];
        if(itemMap[item.id] == null)
            continue;
        itemMap[item.id] = null;
        var child = elementFromTemplate();
        renderMessageState(child, item, Application.userID);
        element.appendChild(child);
    }
}

function renderMessageState(element, message, userID){ //элемент - отображение в html, message - объект из js
    var userName =  element.firstElementChild;
    var myMessage = userName.nextElementSibling;
    var textMessage = myMessage.firstElementChild;
    var icons = textMessage.nextElementSibling.firstElementChild;
    var status = textMessage.nextElementSibling.lastElementChild;
    userName.innerHTML = message.username;
    if (message.userID == userID) {
        icons.style.display = 'block';
        myMessage.setAttribute('class', 'my_message css_color_my');
        element.setAttribute('class', 'one_message css_my')

    }
    else {
        myMessage.setAttribute('class', 'my_message css_color_not_my');
        element.setAttribute('class', 'one_message css_not_my')
    }
    if (message.indEdit)
        status.innerHTML = 'Edited';
    if (message.indDelete) {
        status.innerHTML = 'Deteled';
        textMessage.style.display = 'none';
        status.style.display = 'none'; //невидимые иконки
    }
    else
        textMessage.innerHTML = message.textMessage; //текст, если сообщение не удалено
    element.setAttribute('data-task-id', message.id);
    element.style.display = 'block';
    document.getElementById('history').scrollTop = document.getElementById('history').scrollHeight;
}

function elementFromTemplate() {
    var template = document.getElementById("message_template");
    return template.cloneNode(true);
}

function indexById(list, id){
    return list.findIndex(function(item) {
        return item.id == id;
    });
}

function ajax(method, url, data, continueWith, continueWithError) {
    var xhr = new XMLHttpRequest();

    continueWithError = continueWithError || defaultErrorHandler;
    xhr.open(method || 'GET', url, true); //сходили на сервер

    xhr.onload = function () { // далее обрабатываем, что получилось
        if (xhr.readyState !== 4)
            return;
        if(xhr.status != 200) {
            continueWithError('Error on the server side, response ' + xhr.status);
            return;
        }
        if(isError(xhr.responseText)) {
            continueWithError('Error on the server side, response ' + xhr.responseText);
            return;
        }
        continueWith(xhr.responseText);
    };

    xhr.ontimeout = function () {
        ontinueWithError('Server timed out !');
    }

    xhr.onerror = function (e) {
        var errMsg = 'Server connection error !\n'+
            '\n' +
            'Check if \n'+
            '- server is active\n'+
            '- server sends header "Access-Control-Allow-Origin:*"\n'+
            '- server sends header "Access-Control-Allow-Methods: PUT, DELETE, POST, GET, OPTIONS"\n';

        continueWithError(errMsg);
    };

    xhr.send(data);
}

window.onerror = function(err) {
    output(err.toString());
}

function isError(text) {
    if(text == "")
        return false;
    try {
        var obj = JSON.parse(text);
    }
    catch(ex) {return true;}
    return !!obj.error;
}

function defaultErrorHandler(message) {
    console.error(message);
}