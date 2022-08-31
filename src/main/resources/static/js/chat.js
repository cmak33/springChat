'use strict';

var addUserForm = document.querySelector('#addUserForm');
var sendMessageForm = document.querySelector('#sendMessageForm');
var messageArea = document.querySelector('#messageArea');
var errorDiv = document.querySelector('#error');
var messageInput = document.querySelector('#message');
var usernameInput = document.querySelector('#usernameInput');
var leaveForm = document.querySelector("#leaveForm");

var stompClient = null;
var username = null;

function connect() {
    console.log('connected');
    errorDiv.classList.add('hidden');
    var socket = new SockJS('/endpoint');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, onConnected, onError);
}
function onConnected() {
    stompClient.subscribe('/chat/message', onMessageReceived);
    stompClient.subscribe('/chat/event',onEventReceived);
}
function onError(error) {
    errorDiv.classList.remove('hidden');
    errorDiv.textContent = 'error occurred';
}
function sendMessage(event) {
    var messageContent = messageInput.value.trim();
    if(messageContent && stompClient) {
        console.log(messageInput.value);
        stompClient.send("/application/chat.sendMessage", {}, messageInput.value);
        messageInput.value = '';
    }
    event.preventDefault();
}

function addUser(event){
    var username = usernameInput.value.trim();
    stompClient.send("/application/chat.addByUsername",{},username);
    event.preventDefault();
}

function onEventReceived(payload){
    var messageElement = document.createElement('li');
    var textElement = document.createElement('p');
    var messageText = document.createTextNode(payload.body);
    textElement.appendChild(messageText);
    messageElement.appendChild(textElement);
    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}

function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);
    var messageElement = document.createElement('li');
    var textElement = document.createElement('p');
    var messageText = document.createTextNode(message.username+' : '+message.message);
    textElement.appendChild(messageText);
    messageElement.appendChild(textElement);
    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}

function leaveFormEvent(event){
    stompClient.send("/application/chat.leave",{},"");
}


connect();
sendMessageForm.addEventListener('submit', sendMessage, true);
addUserForm.addEventListener('submit', addUser, true);
leaveForm.addEventListener('submit',leaveFormEvent,true);