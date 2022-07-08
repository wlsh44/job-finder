var bookmarkNum

function popOpen(recruit) {

    console.log(recruit)
    bookmarkNum = recruit

    $('#bookmark-modal').modal('show');

    getCategoryList()
}

function popClose() {
    $('#bookmark-modal').modal('hide');
}

function getCategoryList() {
    $.ajax({
        type: "GET",
        url: "/category",
        dataType: "text",
    }).done(function (fragment) {
        $("#categoryList").replaceWith(fragment);
    });
}

function createCategory() {

    var name = $("#add-input").val();

    var newCategoryRequestDto = {
        name: name
    }

    $.ajax({
        type: "POST",
        url: "/category",
        data: newCategoryRequestDto
    }).done(function (fragment) {
        $("#categoryList").replaceWith(fragment);
    }).fail(function (xhr, status) {
        console.log(xhr)
        alert(xhr.responseJSON["message"])
    });

    rollbackModalFooter()
}

function bookmarkRecruit() {
    let arr = []
    $(".bookmark-input").map(function (idx, ele) {
        if ($(ele).is(":checked")) {
            let categoryName = $(ele).next("label").text().trim();
            arr.push(categoryName)
        }
    });

    for (const arrElement of arr) {
        console.log(arrElement)
    }
    console.log(recruitDtoList[bookmarkNum])

    let newBookmarkRequestDto = {
        categoryList: arr,
        recruitDto: recruitDtoList[bookmarkNum]
    }
    console.log(newBookmarkRequestDto)

    $.ajax({
        type: "POST",
        url: "/bookmark",
        contentType: "application/json",
        data: JSON.stringify(newBookmarkRequestDto)
    }).done(function () {
        alert("북마크 되었습니다.")
        popClose()
    }).fail(function (data) {
        console.log(data)
        alert("북마크에 실패했습니다.")
    })
}

function rollbackModalFooter() {
    let addButton = '<button id="add-category-button" class="btn btn-primary" onclick=addCategory()>카테고리 추가</button>' +
        '<button id="bookmark-button" class="btn btn-primary" onclick=bookmarkRecruit()>북마크 추가</button>'
    $("#bookmark-footer").html(addButton)
}

function addCategory() {
    let addCategory = '<div id="add-category">' +
        '<input type="text" id="add-input">' +
        '<button class="btn btn-secondary" onclick="rollbackModalFooter()">취소</button>' +
        '<button id="add-category" class="btn btn-primary" onclick="createCategory()">저장</button>' +
        '</div>'
    $("#bookmark-footer").html(addCategory)
}