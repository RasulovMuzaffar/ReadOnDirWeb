<%-- 
    Document   : newjsp
    Created on : 14.06.2017, 16:20:26
    Author     : Muzaffar
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <div>
            <form action="writing" method="GET">
                <!--                <label>Введите сообщение</label>
                                <input type="text" name="mess"/>-->
                <input type="hidden" value="0" name="q"/>
                <label>Код сообщение</label>
                <select name="kod">
                    <option value="212">212</option>
                    <option value="213">213</option>
                </select>
                <label>форма документа</label>
                <select name="kodDok">
                    <option value="93">93</option>
                    <option value="94">94</option>
                </select>
                <label>объект</label>
                <select name="object">
                    <option value="7200">Чукурсай</option>
                    <option value="8200">Хаваст</option>
                </select>
                <input type="hidden" name="id_user" value="${user.id}"/>
                <input type="submit" value="submit"/>
            </form>
            <b>${pageContext.session.id}</b><br/>
            <b>${user.autoNo}</b>
            <input type="text" id="ttt"/><button id="xxx" onclick="www();">qwe</button>
            <textarea id="messagesTextArea" rows="50" cols="50"></textarea>
        </div>
        <script type="text/javascript">
            function www() {
                webSocket.send(document.getElementById('ttt').value);
            }
            var webSocket = new WebSocket("ws://localhost:8080/MessageToASOUP//ws");
            var messagesTextArea = document.getElementById("messagesTextArea");
            webSocket.onopen = function (message) {
                processOpen(message);
            };
            webSocket.onmessage = function (message) {
                processMessage(message);
            };
            function processOpen(message) {
                messagesTextArea.value += "Server connected...";
            }
            function processMessage(message) {
                console.log(message);
                messagesTextArea.value += message;
            }

        </script>
    </body>