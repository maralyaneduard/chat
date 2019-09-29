$( document ).ready(function() {
    var roomId = "";
    var chatMessageList = $("#chat-message-list");
    var chatUsersList = $("#chat-users-container");
    var subscription;

    function processGetRequest(url, successFunction, errorFunction){
        $.ajax({
            url: url,
            cache: false,
            success: successFunction,
            error: errorFunction
        });
    }

    function successGetMessage(data){
        chatMessageList.html("");
        for(var i = 0; i < data.length; i++){
            addMessage(chatMessageList, data[i].userName, data[i].text);
        }
    }

    function addMessage(userName, text, isSystem){
        var messageDiv;
        if(isSystem){
            messageDiv = $("<div class='chat-message system-chat-message'></div>");
        } else{
            messageDiv = $("<div class='chat-message'></div>");
        }
        var userName = $("<span class='chat-username'>" + userName + "</span>:");
        var message = $("<span class='chat-message'>" + text + "</span>");
        messageDiv.append(userName);
        messageDiv.append(message);
        chatMessageList.append(messageDiv);
    }

    function errorGetMessage(data){
        console.log(data);
    }

    function successGetUser(data){
        chatUsersList.html("");
        for(var i = 0; i < data.length; i++){
            addUser(data[i].name);
        }
    }

    function addUser(name, id){
        var userDiv = $("<div id='" + name + "' class='chat-user'></div>");
        var userName = $("<span class='chat-username'>" + name + "</span>");
        userDiv.append(userName);
        chatUsersList.append(userDiv);
    }

    function removeUser(name){
        var userDiv = $("#" + name);
        userDiv.remove();
    }

    function errorGetUser(data){
        console.log(data);
    }

    function connect() {
        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, onConnected, onError);
    }

    function onConnected() {
        // Subscribe to the Public Topic
        if(subscription){
            subscription.unsubscribe();
        }
        subscription = stompClient.subscribe('/topic/public/' + selectedRoomId, onMessageReceived, {id: selectedRoomId});

        // Tell your username to the server
        stompClient.send("/app/chat/join/" + selectedRoomId,
            {},
            JSON.stringify({messageType:"JOIN"})
        );
    }

    function onError(error) {
        console.log(error);
    }

    $("#sendMessage").click(function(){
        var messageText = $("#messageBox").val();
        sendMessage(messageText);
    });

    function sendMessage(message) {
        if(message && stompClient) {
            var chatMessage = {
                text: message
            };
            stompClient.send("/app/chat/sendMessage/" + selectedRoomId, {}, JSON.stringify(chatMessage));
            $("#messageBox").val("");
        }
    }

    function onMessageReceived(payload) {
        var message = JSON.parse(payload.body);

        if(message.messageType === 'JOIN') {
            addMessage(message.userName, "Joined the room", true);
            addUser(message.userName);
        }else if (message.messageType === 'LEAVE') {
            addMessage(message.userName, "Leaved the room", true);
            removeUser(message.userName);
        }else if (message.messageType === 'CHAT') {
            addMessage(message.userName, message.text, false);
        }else if (message.messageType === 'KICK') {
            removeUser(message.userName);
            addMessage(message.userName, "Kicked from the room", true);
            if(userName === message.userName){
                window.location.replace("http://localhost:8080/chat/home");
            }
        }
    }

    $("#clearButton").click(function(){
        if(selectedRoomId){
            window.location.replace("http://localhost:8080/chat/clear/" + selectedRoomId);
        }
    });

    if(selectedRoomId){
        connect();
    } else {
        $("#messageBox").prop('disabled', true);
        $("#sendMessage").prop('disabled', true);
        $("#clearButton").prop('disabled', true);
    }
});