var dd = {
    "пор": "Порожные",
    "назн": "Назначение",
    "груз": "Код груза",
    "собств": "Собственник",
    "получ": "Код получателя",
    "опер": "Последняя операция с вагоном",
    "увпп": "Прием вагона с подъездного пути",
    "искп": "Исключение из поезда",
    "рпрз": "Перечисление вагона из резерва дороги в рабочий парк",
    "отпр": "Отправление состава грузового поезда/локомотива со станции",
    "выг1": "Выгрузка вагона на местах общего пользования",
    "приб": "Прибытие состава грузового поезда/локомотива на станцию",
}
// var d = document.getElementById('tb');
// d.addEventListener('mouseover', function (v) {
//     if (v.target.tagName === "TD") {
//         console.log(dd[v.target.innerText] === undefined ? "":dd[v.target.innerText]);
//     }
// });

//'use strict'
document.addEventListener('selectend', function () {
    if (dd[window.getSelection().toString().toLowerCase()] !== undefined) {
        alert(dd[window.getSelection().toString().toLowerCase()]);
    }
})

//Создаем событие 'selectionend'
;
(function () {
    if (!window.getSelection) {
        return;
    }

    var event = document.createEvent('Event'); //Старым способом, что бы в ИЕ работало
    event.initEvent('selectend', true, true);

    var selectstart = false;
    document.addEventListener('selectstart', function () {
        selectstart = true;
    })

    //Выделение мышей
    document.addEventListener('mouseup', function () {
        raiseEvent();
    })
    //Комбинацией ctrl+a
    document.addEventListener('keyup', function () {
        raiseEvent();
    })

    //Проводим проверку, и вызываем событие
    function raiseEvent() {
        if (window.getSelection().toString().length && selectstart) {
            selectstart = false;
            document.dispatchEvent(event);
        }
    }
}());

