
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <!--<meta charset="utf-8">-->
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Справочник АСОУП</title>

        <!-- Bootstrap -->
        <link href="resources/css/bootstrap.css" rel="stylesheet">
        <link href="resources/css/style.css" rel="stylesheet">

        <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
        <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
        <!--[if lt IE 9]>
        <script src="resources/js/html5shiv.min.js"></script>
        <script src="resources/js/respond.min.js"></script>
        <![endif]-->
    </head>
    <body>

        <div class="container-fluid">
            <div class="row header">
                <div class="col-md-4 hleft">
                    <h2>Справочник АСОУП</h2>
                </div>
                <div class="col-md-8 hright">
                    <a href="#">Выход</a>
                    <br/>
                    <h4><strong>Здравствуйте, ${user.fName} ${user.lName}!</strong></h4>

                </div>
            </div>
            <div class="row">
                <form class="form-inline" role="form">
                    <div class="form-group">
                        <label class="sr-only" for="numMess">Mess</label>
                        <select class="form-control" id="numMess">
                            <option>№ запроса</option>
                            <option>212</option>
                            <option>213</option>
                        </select>
                        <label class="sr-only" for="numSpr">Spr</label>
                        <select class="form-control" id="numSpr">
                            <option>№ справочника</option>
                            <option>93</option>
                            <option>95</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label class="sr-only" for="st">Станция</label>
                        <input type="text" class="form-control" id="st" placeholder="Код станции">
                        <input type="hidden" name="id_user" value="${user.id}" id="id_user"/>
                    </div>
                    <button type="button" class="btn btn-info" onclick="writing();">OK</button>
                </form>

            </div>
            <div class="row" id="otvet">
                <!--<label><h3>ВЦ УТИ 93 31.05 13-46 ВЦ 73 НАЛИЦИЕ ПОЕЗДОВ НАХОДЯЩИХСЯ НА СТ. ЧУКУР</h3></label>
                <div class="col-md-12 tabl">
                    <table class="mytable" cellspacing="0">
                        <thead>
                            <tr>
                                <th>№</th>
                                <th>НОМЕР</th>
                                <th>ИНДЕКС</th>
                                <th>СОСТ</th>
                                <th>ДАТА</th>
                                <th>ВРЕМЯ</th>
                                <th>ПАРК</th>
                                <th>ВАГ</th>
                                <th>УДЛ</th>
                                <th>БРУТТ</th>
                                <th>ТГНЛ</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>1</td>
                                <td>2033</td>
                                <td>7200 34 6980</td>
                                <td>ФОРМ</td>
                                <td>31.05</td>
                                <td>00-19</td>
                                <td>00/00</td>
                                <td>50</td>
                                <td>50</td>
                                <td>1187</td>
                                <td>
                                    <button type="button" class="btn btn-default">Показать</button>
                                </td>
                            </tr>
                            <tr>
                                <td>2</td>
                                <td>2890</td>
                                <td>6980 636 7200</td>
                                <td>ПРИБ</td>
                                <td>31.05</td>
                                <td>13-02</td>
                                <td>00/00</td>
                                <td>53</td>
                                <td>62</td>
                                <td>4575</td>
                                <td>
                                    <button type="button" class="btn btn-default">Показать</button>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>-->
            </div>
        </div>

        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="resources/js/jquery.min.js"></script>
        <!-- Include all compiled plugins (below), or include individual files as needed -->
        <script src="resources/js/bootstrap.js"></script>


        <!--<div>
            
            <form action="writing" method="GET">
                                <label>Введите сообщение</label>
                                <input type="text" name="mess"/>
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
            <b>${pageContext.session.id}</b><br/>
            <b>${user.autoNo}</b>
            <input type="text" id="ttt"/><button id="xxx" onclick="www();">qwe</button>
            <textarea id="messagesTextArea" rows="50" cols="100"></textarea>
        </div>-->
        <script type="text/javascript">
            var rg = /^([a-z_0-9.]{1,})\|([\s\S]*)/;
            function writing() {
                var q = 0;//document.getElementById('q').value;
                var kod = document.getElementById('numMess').value;
                var kodDok = document.getElementById('numSpr').value;
                var object = document.getElementById('st').value;
                var id_user = document.getElementById('id_user').value;

                webSocket.send("spr|" + q + "," + kod + "," + kodDok + "," + object + "," + id_user);
//                windows.spr(p){
//                    document.getElementById("otvet").innerHTML = message.data;
//                };

//                                $.get('writing', {q: q, kod: kod, kodDok: kodDok, object: object, id_user: id_user},
//                        function (data) {
//
//                        });
            }
//                    function www() {
//                        webSocket.send(document.getElementById('ttt').value);
//                    }
            var webSocket = new WebSocket("ws://localhost:8080/MessageToASOUP//ws");
//                    var messagesTextArea = document.getElementById("messagesTextArea");
            webSocket.onopen = function (message) {
                processOpen(message);
                console.log(message);
            };
            webSocket.onmessage = function (message) {
                // processMessage(message);
                console.log(message);
                document.getElementById("otvet").innerHTML = message.data;
                var r = rg.exec(response.data);
                try {
                    if (r[1].includes('.')) {
                        var d = r[1].split(',');
                        window[d[0]][d[1]](r[2]);
                    } else {
                        window[r[1]](r[2]);
                    }
                } catch (er) {
                    console.log('ошибка ' + er.stack);
                    console.log('вызов ' + r[1]);
                    console.trace();

                }

            };
            function processOpen(message) {
                console.log(message);
//                messagesTextArea.value += "Server connected...";
            }
            function processMessage(message) {
                console.log(message.valueOf());
                messagesTextArea.value = "";
                messagesTextArea.value += message.data;
            }

        </script>
    </body>