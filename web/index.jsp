
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
            <div class="row">
                <p class="progressInfo"></p>
            </div>
            <div class="row" id="tbl">
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

                        };

                        var webSocket = new WebSocket("ws://localhost:8080/MessageToASOUP//ws");
                        webSocket.onopen = function (message) {
                            processOpen(message);
                            console.log(message);
                        };
                        webSocket.onmessage = function (message) {
                            // processMessage(message);
                            console.log(message);
                            document.getElementById("tbl").innerHTML = message.data;

                            checking();

//                            var r = rg.exec(response.data);
//                            try {
//                                if (r[1].includes('.')) {
//                                    var d = r[1].split(',');
//                                    window[d[0]][d[1]](r[2]);
//                                } else {
//                                    window[r[1]](r[2]);
//                                }
//                            } catch (er) {
//                                console.log('ошибка ' + er.stack);
//                                console.log('вызов ' + r[1]);
//                                console.trace();
//
//                            }

                        };
                        function processOpen(message) {
                            console.log(message);
                        }
                        function processMessage(message) {
                            console.log(message.valueOf());
//                            messagesTextArea.value = "";
//                            messagesTextArea.value += message.data;
                        }

                        function checking() {
//                            $(".progressInfo").html($(".mytable tr:last td:first").html());
                            console.log($(".mytable tr").length);
                            console.log($(".mytable tr:last td:first").html());
                            var rowsCount = document.getElementsByClassName("mytable")[0].getElementsByTagName('tr').length;
                            var endRowFCellVal = document.getElementsByClassName("mytable")[0].rows[rowsCount - 1].cells[0].innerHTML;
//                            document.getElementsByClassName("progressInfo")[0].innerHTML = endRowFCellVal;

//                            console.log(rowsCount - 1);
//                            console.log(endRowFCellVal);
                            if ((rowsCount - 1) != endRowFCellVal) {
                                document.getElementsByClassName("progressInfo")[0].innerHTML = "Таблица неполная!";
                                console.log("Таблица неполная!")
                            }
                        }
        </script>
    </body>