function newMessage(username, text, edit, del) {
    return {
        author: username,
        text: text,
        indEdit: !!edit,
        indDelete: !!del,
        id: '' + uniqueId(),
        timeStamp: Date.now()
    };
}

function copyMessage(message) {
    return {
        author: message.author,
        text: message.text,
        indEdit: message.indEdit,
        indDelete: message.indDelete,
        id: message.id,
        timeStamp: message.timeStamp
    }
}

function uniqueId() {
    var date = Date.now();
    var random = Math.random() * Math.random();
    return Math.floor(date * random);
}

var Application = {
    mainUrl: 'http://localhost:8080/chat',
    messageList: [],
    usernameNow: '',
    token: 'TN11EN',
    realMessages: []
    // добавляем что-то типа realMessages - только сообщения. чтобы по ид искать для удаления и изменения. добавлять сюда в updateList
};

var isConnected = void 0;

function whileConnected() {
    isConnected = setTimeout(function () {
        loadMessages(function () {
            render(Application);
            renderError(false);
            whileConnected();
        }, function () {
            renderError(true);
            whileConnected();
        });
    }, seconds(1));
}

function seconds(value) {
    return Math.round(value * 1000);
}

function run() { //пока не залогинишься  - истории не будет))))
    var appContainer = document.getElementsByClassName('bugchat')[0];
    appContainer.addEventListener('click', delegateEvent);
    Application.usernameNow = "Dasha";
    connect();
}

function connect() {
    if (isConnected)
        return;
    whileConnected();
}

function delegateEvent(evtObj) {
    if (evtObj.type === 'click' && evtObj.target.getAttribute('id') == 'button_sent')
        onAddButtonClick();
    if (evtObj.type === 'click' && evtObj.target.classList.contains('delete_icon'))
        onDeleteIconClick(evtObj.target.parentElement.parentElement.parentElement.parentElement.parentElement);
    if (evtObj.type === 'click' && evtObj.target.classList.contains('edit_icon'))
        onEditIconClick(evtObj.target.parentElement.parentElement.parentElement.parentElement);
    if (evtObj.type === 'click' && evtObj.target.getAttribute('id') == 'enter_name_button')
        onEnterNameButtonClick(evtObj.target.parentElement);
}

function renderError(flag) {
    document.getElementById('server_problem').style.display = flag ? 'block' : 'none';
}

function onEnterNameButtonClick(element) {//логинишься типа
    var newNameBox = element.firstElementChild.nextElementSibling;
    Application.usernameNow = newNameBox.value;
    newNameBox.setAttribute('placeholder', Application.usernameNow);
    newNameBox.value = '';
    var items = document.getElementById('history');
    updateListAfterChangeName(items);
}

function onAddButtonClick() {
    var textNewMessage = document.getElementById('new_message');
    if (textNewMessage.value) {
        var message = newMessage(Application.usernameNow, textNewMessage.value);
        ajax('POST', Application.mainUrl, JSON.stringify(message), function () {
            textNewMessage.value = '';
        });
    }
}

function onDeleteIconClick(element) { //tag one_message
    var index = indexByElement(element, Application.realMessages);
    var id = idFromElement(element);
    var boxForEdit = element.lastElementChild.lastElementChild;
    deleteMessage(id, function() {
        if (boxForEdit.style.display == 'block')
            boxForEdit.style.display = 'none';
    });
}

function onEditIconClick(element) { // tag my_message
    var boxForEdit = element.lastElementChild;
    if (boxForEdit.style.display == 'block')
        return;
    boxForEdit.style.display = 'block';
    boxForEdit.firstElementChild.innerHTML = element.firstElementChild.innerHTML;
    boxForEdit.lastElementChild.firstElementChild.addEventListener('click', function () {
        onEditButtonSent(element);
    });
    boxForEdit.lastElementChild.lastElementChild.addEventListener('click', function () {
        onEditButtonCancel(element);
    });
}

function onEditButtonSent(element) { //tag my_message
    var boxForEdit = element.lastElementChild;
    var text = boxForEdit.firstElementChild.value;
    if (text != null) {
        var id = idFromElement(element.parentElement);
        editMessage(id, text, function() {
            boxForEdit.style.display = 'none';
        });
    }
}

function onEditButtonCancel(element) {
    var boxForEdit = element.lastElementChild;
    boxForEdit.firstElementChild.value = '';
    boxForEdit.style.display = 'none';
}

function idFromElement(element) {
    return element.getAttribute('data-task-id');
}

function loadMessages(done, doneError) {
    var url = Application.mainUrl + '?token=' + Application.token;
    ajax('GET', url, null, function (responseText) {
        var response = JSON.parse(responseText);
        Application.token = response.token;
        Application.messageList = response.messages;
        done(Application);
    }, doneError);
}


function deleteMessage(id, done) {
    var index = indexById(Application.realMessages, id);
    var tempMessage = Application.realMessages[index];
    var messageToDelete = copyMessage(tempMessage);
    messageToDelete.text = '';
    messageToDelete.indDelete = true;
    ajax('DELETE', Application.rl, JSON.stringify(messageToDelete), done);
}

function editMessage(id, text, done) {
    var index = indexById(Application.realMessages, id);
    var tempMessage = Application.realMessages[index];
    var messageToEdit = copyMessage(tempMessage);
    messageToEdit.text = text;
    messageToEdit.indEdit = true;
    ajax('PUT', Application.mainUrl, JSON.stringify(messageToEdit),done);
}

function indexByElement(element, messages) {
    var id = element.getAttribute('data-task-id');//attributes['data-task-id'].value;
    for (var i = 0; i < messages.length; ++i) {
        if (messages[i].id == id)
            return i;
    }
}

function render(app) { //общая отрисовочка!
    var items = document.getElementById('history');
    if (app.messageList.length != 0) {
        var messagesMap = app.messageList.reduce(function (accumulator, message) {
            accumulator[message.id] = message;
            return accumulator;
        }, {});
        updateList(items, messagesMap);
        appendToList(items, app.messageList, messagesMap);
        document.getElementById('history').scrollTop = document.getElementById('history').scrollHeight;
    }
}

function updateListAfterChangeName (element) {
    if (Application.realMessages.length != 0) {
        var messagesMap = Application.realMessages.reduce(function (accumulator, message) {
            accumulator[message.id] = message;
            return accumulator;
        }, {});
    }
    var children = element.children;
    for (var i = 0; i < children.length; i++) {
        var child = children[i];
        if (child.style.display != 'none') { //не рисуем шаблон
            var id = child.attributes['data-task-id'].value;
            var item = messagesMap[id];
            if (item != undefined) {
                var userName = child.firstElementChild;
                var myMessage = userName.nextElementSibling;
                var textMessage = myMessage.firstElementChild;
                var icons = textMessage.nextElementSibling.firstElementChild;
                if (item.author == Application.usernameNow) {
                    if (item.indDelete != true)
                        icons.style.display = 'block';
                    myMessage.setAttribute('class', 'my_message css_color_my');
                    child.setAttribute('class', 'one_message css_my')
                }
                else {
                    icons.style.display = 'none';
                    myMessage.setAttribute('class', 'my_message css_color_not_my');
                    child.setAttribute('class', 'one_message css_not_my')
                }
                document.getElementById('history').scrollTop = document.getElementById('history').scrollHeight;
            }
        }
    }
}


function updateList(element, itemMap) { //перерисовываем что есть
    var children = element.children;
    for (var i = 0; i < children.length; i++) {
        var child = children[i];
        if (child.style.display != 'none') { //не рисуем шаблон
            var id = child.attributes['data-task-id'].value;
            var item = itemMap[id];

            if (item != undefined) {
                renderMessageState(child, item, Application.usernameNow);
                itemMap[id] = null;
            }
        }
    }
}

function appendToList(element, items, itemMap) { //добавояем новые
    for (var i = 0; i < items.length; i++) {
        var item = items[i];
        if (itemMap[item.id] == null)
            continue;
        itemMap[item.id] = null;
        var child = elementFromTemplate();
        Application.realMessages.push(item);
        renderMessageState(child, item, Application.usernameNow);
        element.appendChild(child);
    }
}

function renderMessageState(element, message, username) { //элемент - отображение в html, message - объект из js
    var userName = element.firstElementChild;
    var myMessage = userName.nextElementSibling;
    var textMessage = myMessage.firstElementChild;
    var icons = textMessage.nextElementSibling.firstElementChild;
    var status = textMessage.nextElementSibling.lastElementChild;
    userName.innerHTML = message.author;
    if (message.author == username) {
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
        icons.style.display = 'none'; //невидимые иконки
    }
    else
        textMessage.innerHTML = message.text; //текст, если сообщение не удалено
    element.setAttribute('data-task-id', message.id);
    element.style.display = 'block'; //дорисовали и делаем элемент видимым
    document.getElementById('history').scrollTop = document.getElementById('history').scrollHeight;
}

function elementFromTemplate() {
    var template = document.getElementById("message_template");
    return template.cloneNode(true);
}

function indexById(list, id) {
    return list.findIndex(function (item) {
        return item.id == id;
    });
}

function ajax(method, url, data, continueWith, continueWithError_) {
    var continueWithError = continueWithError_ || renderError;
    try {
        var xhr = new XMLHttpRequest();
        xhr.open(method || 'GET', url, true); //сходили на сервер

        xhr.onload = function () { // далее обрабатываем, что получилось
            if (xhr.readyState !== 4)
                return;
            if (xhr.status != 200) {
                console.error("xhr.stasus", xhr.status);
                continueWithError();
                return;
            }
            if (isError(xhr.responseText)) {
                console.error("wrong response");
                continueWithError();
                return;
            }
            continueWith(xhr.responseText);
        };
        xhr.ontimeout = function () {
            console.error("ontimeout");
            continueWithError();
        };
        xhr.onerror = function (e) {
            console.error("onerror", e);
            continueWithError();
        };
        xhr.send(data);
    }
    catch (e) {
        setTimeout(function () {
            console.error("onerror", e);
            continueWithError();
        }, 1000);
    }
}

function isError(text) {
    if (text == "")
        return false;
    try {
        var obj = JSON.parse(text);
    }
    catch (ex) {
        return true;
    }
    return !!obj.error;
}