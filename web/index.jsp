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
        <script src="resources/js/jquery-3.2.0.min.js"></script>
    </head>
    <body>
        <div>
            <form action="writing" method="GET">
                <!--                <label>Введите сообщение</label>
                                <input type="text" name="mess"/>-->
                <input type="hidden" value="0" id="q" name="q"/>
                <label>Код сообщение</label>
                <select name="kod" id="kod">
                    <option value="212">212</option>
                    <option value="213">213</option>
                </select>
                <label>форма документа</label>
                <select name="kodDok" id="kodDok">
                    <option value="93">93</option>
                    <option value="94">94</option>
                </select>
                <label>объект</label>
                <select name="object" id="object">
                    <option value="7200">Чукурсай</option>
                    <option value="7300">Бухара</option>
                </select>
                <input type="hidden" name="id_user" value="${user.id}" id="id_user"/>
                <input type="button" value="submit" onclick="writing();"/>
            </form>
                <br/>
            <!--<b>${pageContext.session.id}</b><br/>-->
            <!--<b>${user.autoNo}</b>-->
<!--            <input type="text" id="ttt"/><button id="xxx" onclick="www();">qwe</button>-->
            <textarea id="messagesTextArea" rows="50" cols="100"></textarea>
        </div>
        <script type="text/javascript">
            function writing() {
                var q = document.getElementById('q').value;
                var kod = document.getElementById('kod').value;
                var kodDok = document.getElementById('kodDok').value;
                var object = document.getElementById('object').value;
                var id_user = document.getElementById('id_user').value;
                $.get('writing', {q: q, kod: kod, kodDok: kodDok, object: object, id_user: id_user},
                        function (data) {

                        });
            }
            function www() {
                webSocket.send(document.getElementById('ttt').value);
            }
            var webSocket = new WebSocket("ws://localhost:8080/MessageToASOUP//ws");
            var messagesTextArea = document.getElementById("messagesTextArea");
            webSocket.onopen = function (message) {
                processOpen(message);
//                console.log(message);
            };
            webSocket.onmessage = function (message) {
                processMessage(message);
//                console.log(message);
            };
            function processOpen(message) {
//                console.log(message);
                messagesTextArea.value += "Server connected...";
            }
            function processMessage(message) {
                console.log(message.valueOf());
                messagesTextArea.value = "";
                messagesTextArea.value += message.data;
            }

        </script>
    </body>